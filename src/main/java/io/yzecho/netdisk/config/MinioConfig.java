package io.yzecho.netdisk.config;

import io.minio.MinioClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @className: MinioConfig
 * @description:
 * @author: liuzhe
 * @date: 2021/3/15
 **/
@Configuration
@EnableConfigurationProperties(MinioProp.class)
public class MinioConfig {

    @Resource
    private MinioProp minioProp;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioProp.getEndpoint())
                .credentials(minioProp.getAccessKey(), minioProp.getSecretKey())
                .build();
    }

}
