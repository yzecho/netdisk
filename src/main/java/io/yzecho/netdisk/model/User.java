package io.yzecho.netdisk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yzecho
 * @desc
 * @date 13/01/2021 00:00
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User implements Serializable {

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户githubId
     */
    private String githubId;

    /**
     * 文件仓库ID
     */
    private Integer fileStoreId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 注册时间
     */
    private LocalDateTime registerTime;

    /**
     * 头像地址
     */
    private String imagePath;

    /**
     * 用户角色（0：管理员，1：普通用户）
     */
    private Integer role;
}
