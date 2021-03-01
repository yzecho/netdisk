package io.yzecho.netdisk.service.impl;

import io.yzecho.netdisk.model.FileStore;
import io.yzecho.netdisk.service.FileStoreService;
import org.springframework.stereotype.Service;

/**
 * @author yzecho
 * @desc
 * @date 17/01/2021 13:14
 */
@Service
public class FileStoreServiceImpl implements FileStoreService {
    @Override
    public Integer addFileStore(FileStore fileStore) {
        return null;
    }

    @Override
    public FileStore getFileStoreByUserId(Integer userId) {
        return null;
    }

    @Override
    public FileStore getFileStoreById(Integer fileStoreId) {
        return null;
    }

    @Override
    public Integer updateSize(Integer id, Integer size) {
        return null;
    }

    @Override
    public Integer updatePermission(Integer id, Integer permission, Integer size) {
        return null;
    }

    @Override
    public Integer deleteFileStoreById(Integer id) {
        return null;
    }
}
