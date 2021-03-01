package io.yzecho.netdisk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yzecho
 * @desc
 * @date 13/01/2021 00:04
 */
@AllArgsConstructor
@Data
@Builder
public class MyFile {

    /**
     * 文件ID
     */
    private Integer myFileId;

    /**
     * 文件名
     */
    private String myFileName;

    /**
     * 文件仓库ID
     */
    private Integer fileStoreId;

    /**
     * 文件存储路径
     */
    private String myFilePath;

    /**
     * 文件下载次数
     */
    private Integer downloadTime;

    /**
     * 文件上传时间
     */
    private LocalDateTime uploadTime;

    /**
     * 父文件夹ID
     */
    private Integer parentFolderId;

    /**
     * 文件大小
     */
    private Integer size;

    /**
     * 文件类型
     */
    private Integer type;

    /**
     * 文件后缀名
     */
    private String postfix;
}
