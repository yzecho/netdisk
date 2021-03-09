package io.yzecho.netdisk.model.dto;

import lombok.Data;

/**
 * @className: AccessTokenDTO
 * @description:
 * @author: liuzhe
 * @date: 2021/3/8
 **/
@Data
public class AccessTokenDTO {
    private String clientId;
    private String clientSecret;
    private String code;
    private String redirectUri;
    private String state;
}
