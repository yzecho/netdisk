package io.yzecho.netdisk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yzecho
 * @desc 文件仓库数据统计
 * @date 13/01/2021 14:05
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FileStoreStatistics implements Serializable {

    /**
     * 文件仓库
     */
    private FileStore fileStore;

    /**
     * 文档数
     */
    private int docNum;

    /**
     * 音频数
     */
    private int musicNum;

    /**
     * 视频数
     */
    private int videoNum;

    /**
     * 图像数
     */
    private int imageNum;

    /**
     * 其他类型数
     */
    private int otherNum;

    /**
     * 文件数
     */
    private int fileNum;

    /**
     * 文件夹数
     */
    private int folderNum;
}
