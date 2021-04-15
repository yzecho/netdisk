package io.yzecho.netdisk.mapper;

import io.yzecho.netdisk.model.FileStoreStatistics;
import io.yzecho.netdisk.model.MyFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author yzecho
 * @desc
 * @date 16/01/2021 13:18
 */
@Mapper
public interface MyFileMapper {

    /**
     * 根据文件的ID删除文件
     *
     * @param myFileId
     * @return
     */
    Integer deleteFileByFileId(Integer myFileId);

    /**
     * 根据父文件夹ID删除文件
     *
     * @param folderId
     * @return
     */
    Integer deleteFileByParentFolderId(Integer folderId);

    /**
     * 添加文件
     *
     * @param myFile
     * @return
     */
    Integer addFileByFileStoreId(MyFile myFile);

    /**
     * 根据文件ID获取文件
     *
     * @param myFileId
     * @return
     */
    MyFile getFileByFileId(Integer myFileId);

    /**
     * 更新文件
     *
     * @param record
     * @return
     */
    Integer updateFileByFileId(MyFile record);

    /**
     * 获取该仓库根目录下所有文件
     *
     * @param fileStoreId
     * @return
     */
    List<MyFile> getRootFilesByFileStoreId(Integer fileStoreId);

    /**
     * 根据父文件夹ID获取文件
     *
     * @param parentFolderId
     * @return
     */
    List<MyFile> getFilesByParentFolderId(Integer parentFolderId);

    /**
     * 根据文件类型获取文件
     *
     * @param storeId
     * @param type
     * @return
     */
    List<MyFile> getFilesByType(Integer storeId, Integer type);

    /**
     * 获取仓库的统计信息
     *
     * @param id
     * @return
     */
    FileStoreStatistics getCountStatistics(Integer id);
}
