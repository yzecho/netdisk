package io.yzecho.netdisk.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author yzecho
 * @desc
 * @date 13/01/2021 14:00
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserVO {

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 注册时间
     */
    private Date registerTime;

    /**
     * 头像地址
     */
    private String imagePath;

    /**
     * 当前已使用百分比
     */
    private Integer current;

    /**
     * 仓库最大容量（单位KB）
     */
    private Integer maxSize;

    /**
     * 仓库权限（0：可上传下载，1：只允许下载，2：禁止上传下载）
     */
    private Integer permission;
}
