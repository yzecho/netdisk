package io.yzecho.netdisk.enums;

/**
 * @className: UploadEnum
 * @description:
 * @author: liuzhe
 * @date: 2021/3/18
 **/
public enum FileEnum {
    UPLOAD_SUCCESS("文件上传成功", 200),
    NO_UPLOAD_PERMISSION("用户没有上传文件的权限", 499),
    FILE_EXISTS("当前文件已存在", 501),
    FILE_NAME_NO_STANDER("文件名不符合规范", 502),
    NOT_ENOUGH_SPACE("仓库空间不足", 503),
    UPLOAD_FAILED("文件上传失败", 504),
    NO_DOWNLOAD_PERMISSION("用户没有下载文件的权限", 505);

    private String msg;
    private Integer code;

    FileEnum(String msg, Integer code) {
        this.msg = msg;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
