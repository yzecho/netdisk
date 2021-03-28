package io.yzecho.netdisk.service.impl;

import io.yzecho.netdisk.model.FileFolder;
import io.yzecho.netdisk.model.MyFile;
import io.yzecho.netdisk.service.BaseService;
import io.yzecho.netdisk.service.FileFolderService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yzecho
 * @desc
 * @date 17/01/2021 13:15
 */
@Service
public class FileFolderServiceImpl extends BaseService implements FileFolderService {

    @Override
    public Integer deleteFileFolderByFileFolderId(Integer fileFolderId) {
        return fileFolderMapper.deleteFileFolderByFileFolderId(fileFolderId);
    }

    @Override
    public Integer deleteFileFolderByParentFolderId(Integer parentFolderId) {
        return fileFolderMapper.deleteFileFolderByParentFolderId(parentFolderId);
    }

    @Override
    public Integer deleteFileFolderByFileStoreId(Integer fileStoreId) {
        return fileFolderMapper.deleteFileFolderByFileStoreId(fileStoreId);
    }

    @Override
    public Integer addFileFolder(FileFolder fileFolder) {
        return fileFolderMapper.addFileFolder(fileFolder);
    }

    @Override
    public List<MyFile> getFilesByFileFolderId(Integer fileFolderId) {
        return fileFolderMapper.getFilesByFileFolderId(fileFolderId);
    }

    @Override
    public List<FileFolder> getFileFolderByParentFileFolderId(Integer parentFileFolderId) {
        return fileFolderMapper.getFileFolderByParentFileFolderId(parentFileFolderId);
    }

    @Override
    public FileFolder getFileFolderByFileFolderId(Integer fileFolderId) {
        return fileFolderMapper.getFileFolderByFileFolderId(fileFolderId);
    }

    @Override
    public List<FileFolder> getFileFolderByFileStoreId(Integer fileStoreId) {
        return fileFolderMapper.getFileFolderByFileStoreId(fileStoreId);
    }

    @Override
    public List<FileFolder> getRootFoldersByFileStoreId(Integer fileStoreId) {
        return fileFolderMapper.getRootFoldersByFileStoreId(fileStoreId);
    }

    @Override
    public Integer updateFileFolderById(FileFolder fileFolder) {
        return fileFolderMapper.updateFileFolderById(fileFolder);
    }

    @Override
    public Integer getFileFolderCountByFileStoreId(Integer fileFolderId) {
        return fileFolderMapper.getFileFolderCountByFileStoreId(fileFolderId);
    }
}
