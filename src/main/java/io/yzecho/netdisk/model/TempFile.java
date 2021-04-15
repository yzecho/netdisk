package io.yzecho.netdisk.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * temp_file
 * @author 
 */
@Data
@Builder
public class TempFile implements Serializable {
    /**
     * 临时文件ID
     */
    private Integer fileId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小
     */
    private String size;

    /**
     * 上传时间：4小时后删除
     */
    private Date uploadTime;

    /**
     * 文件在FTP上的存放路径
     */
    private String filePath;

    private static final long serialVersionUID = 1L;
}