package io.yzecho.netdisk.model.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @className: GitHubUser
 * @description:
 * @author: liuzhe
 * @date: 2021/3/8
 **/
@Data
@ToString
public class GitHubUser {
    private String id;
    private String name;
    private String bio;
    private String avatarUrl;
}
