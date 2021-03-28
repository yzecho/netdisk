package io.yzecho.netdisk.service.impl;

import io.yzecho.netdisk.model.FileStore;
import io.yzecho.netdisk.service.BaseService;
import io.yzecho.netdisk.service.FileStoreService;
import org.springframework.stereotype.Service;

/**
 * @author yzecho
 * @desc
 * @date 17/01/2021 13:14
 */
@Service
public class FileStoreServiceImpl extends BaseService implements FileStoreService {
    @Override
    public Integer addFileStore(FileStore fileStore) {
        return fileStoreMapper.addFileStore(fileStore);
    }

    @Override
    public FileStore getFileStoreByUserId(Integer userId) {
        return fileStoreMapper.getFileStoreByUserId(userId);
    }

    @Override
    public FileStore getFileStoreByFileStoreId(Integer fileStoreId) {
        return fileStoreMapper.getFileStoreByFileStoreId(fileStoreId);
    }

    @Override
    public Integer addSize(Integer userId, Integer size) {
        return fileStoreMapper.addSize(userId, size);
    }

    @Override
    public Integer subSize(Integer fileStoreId, Integer size) {
        return fileStoreMapper.subSize(fileStoreId, size);
    }

    @Override
    public Integer updatePermission(Integer userId, Integer permission, Integer size) {
        return fileStoreMapper.updatePermission(userId, permission, size);
    }

    @Override
    public Integer deleteByFileStoreId(Integer fileStoreId) {
        return fileStoreMapper.deleteByFileStoreId(fileStoreId);
    }
}
