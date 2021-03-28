package io.yzecho.netdisk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yzecho
 * @desc 文件夹实体类
 * @date 13/01/2021 13:32
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FileFolder implements Serializable {

    /**
     * 文件夹ID
     */
    private Integer fileFolderId;

    /**
     * 文件夹名称
     */
    private String fileFolderName;

    /**
     * 父文件夹ID
     */
    private Integer parentFolderId;

    /**
     * 所属文件仓库ID
     */
    private Integer fileStoreId;

    /**
     * 创建时间
     */
    private LocalDateTime time;
}
