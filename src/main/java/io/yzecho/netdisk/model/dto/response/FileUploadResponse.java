package io.yzecho.netdisk.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @className: FileUploadResponse
 * @description:
 * @author: liuzhe
 * @date: 2021/3/15
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponse{

    private String urlHttp;

    private String urlPath;
}
