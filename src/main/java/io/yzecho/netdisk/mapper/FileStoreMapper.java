package io.yzecho.netdisk.mapper;

import io.yzecho.netdisk.model.FileStore;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yzecho
 * @desc
 * @date 16/01/2021 13:19
 */
@Mapper
public interface FileStoreMapper {

    /**
     * 添加文件仓库（用户注册时调用）
     *
     * @param fileStore
     * @return
     */
    Integer addFileStore(FileStore fileStore);

    /**
     * 根据用户ID获得文件仓库
     *
     * @param userId
     * @return
     */
    FileStore getFileStoreByUserId(Integer userId);

    /**
     * 根据文件仓库ID获得文件仓库
     *
     * @param fileStoreId
     * @return
     */
    FileStore getFileStoreByFileStoreId(Integer fileStoreId);

    /**
     * 增加容量
     *
     * @param fileStoreId
     * @param size
     * @return
     */
    Integer addSize(Integer fileStoreId, Integer size);

    /**
     * 减少容量
     *
     * @param fileStoreId
     * @param size
     * @return
     */
    Integer subSize(Integer fileStoreId, Integer size);

    /**
     * 修改仓库权限
     *
     * @param userId
     * @param permission
     * @param size
     * @return
     */
    Integer updatePermission(Integer userId, Integer permission, Integer size);

    /**
     * 通过ID删除文件仓库
     *
     * @param fileStoreId
     * @return
     */
    Integer deleteByFileStoreId(Integer fileStoreId);
}
