package io.yzecho.netdisk.service.impl;

import io.yzecho.netdisk.model.FileFolder;
import io.yzecho.netdisk.model.MyFile;
import io.yzecho.netdisk.service.FileFolderService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yzecho
 * @desc
 * @date 17/01/2021 13:15
 */
@Service
public class FileFolderServiceImpl implements FileFolderService {
    @Override
    public Integer deleteFileFolderById(Integer fileFolderId) {
        return null;
    }

    @Override
    public Integer addFileFolder(FileFolder fileFolder) {
        return null;
    }

    @Override
    public List<MyFile> getFileFolderById(Integer fileFolderId) {
        return null;
    }

    @Override
    public List<FileFolder> getFileFolderByParentFileFolderId(Integer parentFileFolderId) {
        return null;
    }

    @Override
    public FileFolder getFileFolderByFileFolderId(Integer fileFolderId) {
        return null;
    }

    @Override
    public List<FileFolder> getRootFolderByFileStoreId(Integer fileStoreId) {
        return null;
    }

    @Override
    public Integer updateFileFolderById(Integer fileFolderId) {
        return null;
    }
}
