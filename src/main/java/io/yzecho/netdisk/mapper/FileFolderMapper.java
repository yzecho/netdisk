package io.yzecho.netdisk.mapper;

import io.yzecho.netdisk.model.FileFolder;
import io.yzecho.netdisk.model.MyFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author yzecho
 * @desc
 * @date 16/01/2021 13:19
 */
@Mapper
public interface FileFolderMapper {
    /**
     * 根据文件夹的ID删除文件夹
     *
     * @param fileFolderId
     * @return
     */
    Integer deleteFileFolderByFileFolderId(Integer fileFolderId);

    Integer deleteFileFolderByParentFolderId(Integer parentFolderId);

    Integer deleteFileFolderByFileStoreId(Integer fileStoreId);

    /**
     * 创建文件夹
     *
     * @param fileFolder
     * @return
     */
    Integer addFileFolder(FileFolder fileFolder);

    /**
     * 根据文件夹的ID获取文件夹下的文件
     *
     * @param fileFolderId
     * @return
     */
    List<MyFile> getFilesByFileFolderId(Integer fileFolderId);

    /**
     * 根据父文件夹ID获得所有的子文件夹
     *
     * @param parentFileFolderId
     * @return
     */
    List<FileFolder> getFileFolderByParentFileFolderId(Integer parentFileFolderId);

    /**
     * 根据文件夹的ID获取文件夹
     *
     * @param fileFolderId
     * @return
     */
    FileFolder getFileFolderByFileFolderId(Integer fileFolderId);

    List<FileFolder> getFileFolderByFileStoreId(Integer fileStoreId);

    /**
     * 根据文件仓库ID获取文件仓库根目录下的所有文件夹
     *
     * @param fileStoreId
     * @return
     */
    List<FileFolder> getRootFoldersByFileStoreId(Integer fileStoreId);

    /**
     * 根据文件夹ID修改文件夹信息
     *
     * @param fileFolder
     * @return
     */
    Integer updateFileFolderById(FileFolder fileFolder);

    Integer getFileFolderCountByFileStoreId(Integer fileFolderId);


}
