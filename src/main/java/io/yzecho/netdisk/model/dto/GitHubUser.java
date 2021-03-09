package io.yzecho.netdisk.model.dto;

import lombok.Data;

/**
 * @className: GitHubUser
 * @description:
 * @author: liuzhe
 * @date: 2021/3/8
 **/
@Data
public class GitHubUser {
    private Long id;
    private String name;
    private String bio;
    private String avatarUrl;
}
