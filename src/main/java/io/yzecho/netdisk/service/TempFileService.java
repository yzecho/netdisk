package io.yzecho.netdisk.service;

import io.yzecho.netdisk.model.TempFile;

import java.util.List;

/**
 * @className: TempService
 * @description:
 * @author: liuzhe
 * @date: 2021/4/12
 **/
public interface TempFileService {

    TempFile queryById(Integer fileId);

    List<TempFile> queryAll();

    List<TempFile> queryAll(TempFile tempFile);

    int insert(TempFile tempFile);

    int update(TempFile tempFile);

    int deleteById(Integer fileId);

}
