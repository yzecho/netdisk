package io.yzecho.netdisk.utils;

import io.minio.*;
import io.minio.messages.Bucket;
import io.yzecho.netdisk.config.MinioProp;
import io.yzecho.netdisk.model.dto.response.FileUploadResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * @className: MinioUtil
 * @description:
 * @author: liuzhe
 * @date: 2021/3/15
 **/
@Slf4j
@Component
public class MinioUtil {

    @Resource
    private MinioProp minioProp;

    @Resource
    private MinioClient client;

    /**
     * 创建bucket
     *
     * @param bucketName
     * @throws Exception
     */
    public void createBucket(String bucketName) throws Exception {
        if (!client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    /**
     * 上传文件
     *
     * @param file
     * @param bucketName
     * @return
     * @throws Exception
     */
    public FileUploadResponse uploadFile(MultipartFile file, String bucketName) throws Exception {
        // 判断文件是否为空
        if (file == null || file.getSize() == 0) {
            return null;
        }
        String newFileName = null;
        // 判断桶是否存在，不存在则新建
        createBucket(bucketName);
        String fileName = file.getOriginalFilename();
        if (fileName != null) {
            newFileName = bucketName + "-" + System.currentTimeMillis() + "-"
                    + LocalDate.now().toString() + "-"
                    + new Random().nextInt(1000)
                    + fileName.substring(fileName.lastIndexOf("."));
        }
        client.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(newFileName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build());

        String url = minioProp.getEndpoint() + "/" + bucketName + "/" + newFileName;
        String host = minioProp.getFileHost() + "/" + bucketName + "/" + newFileName;

        log.info("file upload success url: [{}], host:[{}]", url, host);
        return new FileUploadResponse(url, host);
    }

    /**
     * 获取全部桶
     *
     * @return
     * @throws Exception
     */
    public List<Bucket> getAllBuckets() throws Exception {
        return client.listBuckets();
    }

    /**
     * 获取指定桶内容
     *
     * @param bucketName
     * @return
     * @throws Exception
     */
    public Optional<Bucket> getBucket(String bucketName) throws Exception {
        return client.listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
    }

    /**
     * 获取文件外链
     *
     * @param bucketName
     * @param objectName
     * @param expires
     * @return
     */
    public String presignedGetObject(String bucketName, String objectName, Integer expires) throws Exception {
        return client.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(objectName).expiry(expires).build());
    }

    /**
     * 获取文件（二进制流）
     *
     * @param bucketName
     * @param objectName
     * @return
     * @throws Exception
     */
    public InputStream getObject(String bucketName, String objectName) throws Exception {
        return client.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    /**
     * 获取文件元数据
     *
     * @param bucketName
     * @param objectName
     * @return
     * @throws Exception
     */
    public StatObjectResponse getObjectInfo(String bucketName, String objectName) throws Exception {
        return client.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    /**
     * 通过InputStream上传对象
     *
     * @param bucketName
     * @param objectName
     * @param stream
     * @param size
     * @param contextType
     * @throws Exception
     */
    public void putObject(String bucketName, String objectName, InputStream stream,
                          long size, String contextType) throws Exception {
        client.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(stream, size, -1).contentType(contextType).build());
    }


    /**
     * 通过文件上传到对象中
     *
     * @param bucketName
     * @param objectName
     * @param stream
     */
    public void putObject(String bucketName, String objectName, InputStream stream) throws Exception {
        client.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(stream, stream.available(), -1).contentType(objectName.substring(objectName.lastIndexOf("."))).build());
    }


    /**
     * 删除桶信息
     *
     * @param bucketName
     * @throws Exception
     */
    public void removeBucket(String bucketName) throws Exception {
        client.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    }

    /**
     * 删除文件
     *
     * @param bucketName
     * @param objectName
     * @throws Exception
     */
    public void removeObject(String bucketName, String objectName) throws Exception {
        client.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }
}
