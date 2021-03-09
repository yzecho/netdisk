package io.yzecho.netdisk.service;

import io.yzecho.netdisk.mapper.FileFolderMapper;
import io.yzecho.netdisk.mapper.FileStoreMapper;
import io.yzecho.netdisk.mapper.MyFileMapper;
import io.yzecho.netdisk.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @className: BaseService
 * @description:
 * @author: liuzhe
 * @date: 2021/3/7
 **/
public class BaseService {

    @Autowired
    protected UserMapper userMapper;
    @Autowired
    protected MyFileMapper myFileMapper;
    @Autowired
    protected FileFolderMapper fileFolderMapper;
    @Autowired
    protected FileStoreMapper fileStoreMapper;

}
