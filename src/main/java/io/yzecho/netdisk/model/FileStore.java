package io.yzecho.netdisk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yzecho
 * @desc 文件仓库实体类
 * @date 13/01/2021 13:35
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FileStore implements Serializable {

    /**
     * 文件仓库ID
     */
    private Integer fileStoreId;

    /**
     * 主人ID
     */
    private Integer userId;

    /**
     * 当前容量（单位KB）
     */
    private Integer currentSize;

    /**
     * 最大容量（单位KB）
     */
    private Integer maxSize;

    /**
     * 仓库权限（0：可上传下载，1：只允许下载，2：禁止上传下载）
     */
    private Integer permission;
}
