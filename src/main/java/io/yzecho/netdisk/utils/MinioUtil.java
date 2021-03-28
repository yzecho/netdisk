package io.yzecho.netdisk.utils;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.yzecho.netdisk.config.MinioProp;
import io.yzecho.netdisk.model.MyFile;
import io.yzecho.netdisk.model.dto.response.FileUploadResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
    private MinioClient minioClient;

    /**
     * 创建bucket
     *
     * @param bucketName
     * @throws Exception
     */
    public void createBucket(String bucketName) throws Exception {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    public FileUploadResponse uploadFile(MyFile file, String bucketName) throws Exception {
        if (file == null || file.getSize() == 0) {
            return null;
        }
        createBucket(bucketName);

        String fileName = file.getMyFileName();
        return null;
    }
}
