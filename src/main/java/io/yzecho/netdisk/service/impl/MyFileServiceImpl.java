package io.yzecho.netdisk.service.impl;

import io.yzecho.netdisk.model.FileStoreStatistics;
import io.yzecho.netdisk.model.MyFile;
import io.yzecho.netdisk.service.MyFileService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yzecho
 * @desc
 * @date 17/01/2021 12:52
 */
@Service
public class MyFileServiceImpl implements MyFileService {
    @Override
    public Integer deleteFileByFileId(Integer myFileId) {
        return null;
    }

    @Override
    public Integer deleteFileByParentFolderId(Integer folderId) {
        return null;
    }

    @Override
    public Integer addFileByFileStoreId(MyFile myFile) {
        return null;
    }

    @Override
    public MyFile getFileByFileId(Integer myFileId) {
        return null;
    }

    @Override
    public Integer updateFile(MyFile record) {
        return null;
    }

    @Override
    public List<MyFile> getRootFilesByFileStoreId(Integer fileStoreId) {
        return null;
    }

    @Override
    public List<MyFile> getFilesByParentFolderId(Integer parentFolderId) {
        return null;
    }

    @Override
    public List<MyFile> getFilesByType(Integer storeId, Integer type) {
        return null;
    }

    @Override
    public FileStoreStatistics getCountStatistics(Integer id) {
        return null;
    }
}
