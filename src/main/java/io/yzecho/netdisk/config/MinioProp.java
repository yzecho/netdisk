package io.yzecho.netdisk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @className: MinioProp
 * @description:
 * @author: liuzhe
 * @date: 2021/3/15
 **/
@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProp {

    /**
     * 连接地址
     */
    private String endpoint;

    /**
     * 用户名
     */
    private String accessKey;

    /**
     * 密码
     */
    private String secretKey;
}
