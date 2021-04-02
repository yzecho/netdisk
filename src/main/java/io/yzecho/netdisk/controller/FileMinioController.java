package io.yzecho.netdisk.controller;

import io.minio.errors.MinioException;
import io.yzecho.netdisk.model.dto.response.FileUploadResponse;
import io.yzecho.netdisk.utils.LogUtil;
import io.yzecho.netdisk.utils.MinioUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @className: FileMinioController
 * @description:
 * @author: liuzhe
 * @date: 2021/4/1
 **/
@RestController
@RequestMapping("/minio/file")
public class FileMinioController {

    private final Logger logger = LogUtil.getInstance(FileMinioController.class);

    @Autowired
    private MinioUtil minioUtil;

    /**
     * 上传文件
     *
     * @param file
     * @param bucketName
     * @return
     */
    @PostMapping("/upload")
    public FileUploadResponse upload(@RequestParam(name = "file", required = false) MultipartFile file,
                                     @RequestParam(required = false) String bucketName) {
        FileUploadResponse response = null;
        if (StringUtils.isBlank(bucketName)) {
            bucketName = "temp";
        }
        try {
            response = minioUtil.uploadFile(file, bucketName);
        } catch (Exception e) {
            logger.error("上传失败: [{}]", Arrays.asList(e.getStackTrace()));
        }
        return response;
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
            bucketName = "temp";
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
            headers.add("Content-disposition", "attachment; filename=" + objectName);
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
