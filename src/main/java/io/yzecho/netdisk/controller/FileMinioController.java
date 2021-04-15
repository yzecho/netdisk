package io.yzecho.netdisk.controller;

import io.minio.errors.MinioException;
import io.yzecho.netdisk.model.TempFile;
import io.yzecho.netdisk.model.dto.response.FileUploadResponse;
import io.yzecho.netdisk.service.TempFileService;
import io.yzecho.netdisk.utils.LogUtil;
import io.yzecho.netdisk.utils.MinioUtil;
import io.yzecho.netdisk.utils.QrCodeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @className: FileMinioController
 * @description:
 * @author: liuzhe
 * @date: 2021/4/1
 **/
@Controller
public class FileMinioController extends BaseController {

    private final Logger logger = LogUtil.getInstance(FileMinioController.class);

    @Autowired
    private MinioUtil minioUtil;

    private static final Map<String, Object> OBJ_MAP = new HashMap<>();

    @Autowired
    private TempFileService tempFileService;

    /**
     * 上传文件(此处暂时指MinIO临时文件)
     *
     * @param file
     * @param bucketName
     * @return
     */
    @PostMapping("/minio/uploadTempFile")
    public String upload(@RequestParam(name = "file") MultipartFile file,
                         String bucketName) {
        OBJ_MAP.put("imgPath", "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2654852821,3851565636&fm=26&gp=0.jpg");
        FileUploadResponse response;
        if (StringUtils.isBlank(bucketName)) {
            bucketName = "temp-bucket";
        }
        try {
            response = minioUtil.uploadFile(file, bucketName);
            String url = "http://localhost:8080/minio/download/" + response.getUrlPath();
            tempFileService.insert(TempFile.builder()
                    .fileName(file.getOriginalFilename())
                    .size(String.valueOf(file.getSize() / 1024.0))
                    .uploadTime(new Date())
                    .filePath(response.getUrlHttp())
                    .build());
            String id = UUID.randomUUID().toString();
            String p = request.getSession().getServletContext().getRealPath("/user_temp_img/");
            File targetFile = new File(p, "");
            if (!targetFile.exists()) {
                boolean result = targetFile.mkdirs();
                if (!result) {
                    logger.error("mkdirs error");
                }
            }
            File f = new File(p, id + ".jpg");
            if (!f.exists()) {
                OutputStream os = new FileOutputStream(f);
                QrCodeUtil.encode(url, "/static/img/logo.png", os, true);
                os.close();
            }
            OBJ_MAP.put("imgPath", "user_temp_img/" + id + ".jpg");
            OBJ_MAP.put("url", url);
            OBJ_MAP.put("msg", "上传成功，扫码/访问链接即可下载");
        } catch (Exception e) {
            logger.error("上传失败");
            e.printStackTrace();
        }
        return "redirect:/temp-file";
    }

    @GetMapping("/temp-file")
    public String showTemplates(Map<String, Object> map) {
        map.putAll(OBJ_MAP);
        return "temp-file";
    }

    @GetMapping("/minio/download/{bucketName}/{folderName}/{objectName}")
    public void downloadObject(@PathVariable("bucketName") String bucketName, @PathVariable("folderName") String folderName,
                               @PathVariable("objectName") String objectName) {
        try {
            InputStream inputStream = minioUtil.getObject(bucketName, folderName + "/" + objectName);
            File file = new File("/Users/admin/Desktop/" + objectName);
            try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                bufferedOutputStream.write(inputStream.readAllBytes());
            }
            logger.info("下载成功: {}", folderName + "/" + objectName);
        } catch (Exception e) {
            logger.error("下载失败: {}", folderName + "/" + objectName);
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     *
     * @param objectName
     * @param bucketName
     * @throws Exception
     */
    @DeleteMapping("/delete/{objectName}")
    public void delete(@PathVariable("objectName") String objectName, @RequestParam(required = false) String bucketName) throws Exception {
        if (StringUtils.isBlank(bucketName)) {
            bucketName = "temp-bucket";
        }

        minioUtil.removeObject(bucketName, objectName);
        logger.info("删除成功: [{}]", objectName);
    }

    /**
     * 下载文件到本地
     *
     * @param objectName
     * @param response
     * @return
     * @throws Exception
     */
    public ResponseEntity<byte[]> downloadToLocal(@PathVariable("objectName") String objectName, HttpServletResponse response) throws Exception {
        ResponseEntity<byte[]> responseEntity = null;
        InputStream stream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            stream = minioUtil.getObject("temp", objectName);
            if (stream == null) {
                logger.info("文件不存在: [{}]", objectName);
            }
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int n;
            while (-1 != (n = stream.read(buffer))) {
                byteArrayOutputStream.write(buffer, 0, n);
            }
            byte[] bytes = byteArrayOutputStream.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept-Ranges", "bytes");
            headers.add("Content-Length", bytes.length + "");
            objectName = new String(objectName.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
            headers.add("Content-Disposition", "attachment; filename=" + objectName);
            headers.add("Content-Type", "text/plain;charset=utf-8");
            responseEntity = new ResponseEntity<>(bytes, headers, HttpStatus.CREATED);
        } catch (MinioException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                stream.close();
            }
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
        }
        return responseEntity;
    }

    /**
     * 浏览器在线预览
     *
     * @param objectName
     * @param response
     * @throws Exception
     */
    @GetMapping("/preViewPicture/{objectName}")
    public void preViewPicture(@PathVariable("objectName") String objectName, HttpServletResponse response) throws Exception {
        response.setContentType("image/jpeg");
        try (ServletOutputStream out = response.getOutputStream()) {
            InputStream stream = minioUtil.getObject("salt", objectName);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int n;
            while (-1 != (n = stream.read(buffer))) {
                output.write(buffer, 0, n);
            }
            byte[] bytes = output.toByteArray();
            out.write(bytes);
            out.flush();
        }
    }
}
