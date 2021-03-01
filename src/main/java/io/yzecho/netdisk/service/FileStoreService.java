package io.yzecho.netdisk.service;

import io.yzecho.netdisk.model.FileStore;

/**
 * @author yzecho
 * @desc 文件仓库业务层接口
 * @date 13/01/2021 23:41
 */
public interface FileStoreService {

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
    FileStore getFileStoreById(Integer fileStoreId);

    /**
     * 修改仓库当前已使用的容量
     *
     * @param id
     * @param size
     * @return
     */
    Integer updateSize(Integer id, Integer size);

    /**
     * 修改仓库权限
     *
     * @param id
     * @param permission
     * @param size
     * @return
     */
    Integer updatePermission(Integer id, Integer permission, Integer size);

    /**
     * 通过ID删除文件仓库
     *
     * @param id
     * @return
     */
    Integer deleteFileStoreById(Integer id);
}
