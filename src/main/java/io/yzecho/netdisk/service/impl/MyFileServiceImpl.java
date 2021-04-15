package io.yzecho.netdisk.service.impl;

import io.yzecho.netdisk.model.FileStoreStatistics;
import io.yzecho.netdisk.model.MyFile;
import io.yzecho.netdisk.service.BaseService;
import io.yzecho.netdisk.service.MyFileService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yzecho
 * @desc
 * @date 17/01/2021 12:52
 */
@Service
public class MyFileServiceImpl extends BaseService implements MyFileService {
    @Override
    public Integer deleteFileByFileId(Integer myFileId) {
        return myFileMapper.deleteFileByFileId(myFileId);
    }

    @Override
    public Integer deleteFileByParentFolderId(Integer folderId) {
        return myFileMapper.deleteFileByParentFolderId(folderId);
    }

    @Override
    public Integer addFileByFileStoreId(MyFile myFile) {
        return myFileMapper.addFileByFileStoreId(myFile);
    }

    @Override
    public MyFile getFileByFileId(Integer myFileId) {
        return myFileMapper.getFileByFileId(myFileId);
    }

    @Override
    public Integer updateFileByFileId(MyFile record) {
        return myFileMapper.updateFileByFileId(record);
    }

    @Override
    public List<MyFile> getRootFilesByFileStoreId(Integer fileStoreId) {
        return myFileMapper.getRootFilesByFileStoreId(fileStoreId);
    }

    @Override
    public List<MyFile> getFilesByParentFolderId(Integer parentFolderId) {
        return myFileMapper.getFilesByParentFolderId(parentFolderId);
    }

    @Override
    public List<MyFile> getFilesByType(Integer storeId, Integer type) {
        return myFileMapper.getFilesByType(storeId, type);
    }

    @Override
    public FileStoreStatistics getCountStatistics(Integer id) {
        return myFileMapper.getCountStatistics(id);
    }
}
