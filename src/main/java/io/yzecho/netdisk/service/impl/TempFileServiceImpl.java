package io.yzecho.netdisk.service.impl;

import io.yzecho.netdisk.mapper.TempFileMapper;
import io.yzecho.netdisk.model.TempFile;
import io.yzecho.netdisk.model.TempFileExample;
import io.yzecho.netdisk.service.TempFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @className: TempFileServiceImpl
 * @description:
 * @author: liuzhe
 * @date: 2021/4/12
 **/
@Service
public class TempFileServiceImpl implements TempFileService {

    @Autowired
    private TempFileMapper tempFileMapper;


    @Override
    public TempFile queryById(Integer fileId) {
        return tempFileMapper.selectByPrimaryKey(fileId);
    }

    @Override
    public List<TempFile> queryAll() {
        return tempFileMapper.selectByExample(new TempFileExample());
    }

    @Override
    public List<TempFile> queryAll(TempFile tempFile) {
        TempFileExample example = new TempFileExample();
        TempFileExample.Criteria criteria = example.createCriteria();
        if (tempFile.getFileName() != null) {
            criteria.andFileNameEqualTo(tempFile.getFileName());
        }
        if (tempFile.getFilePath() != null) {
            criteria.andFilePathEqualTo(tempFile.getFilePath());
        }
        if (tempFile.getSize() != null) {
            criteria.andSizeEqualTo(tempFile.getSize());
        }
        if (tempFile.getUploadTime() != null) {
            criteria.andUploadTimeEqualTo(tempFile.getUploadTime());
        }

        return tempFileMapper.selectByExample(example);
    }

    @Override
    public int insert(TempFile tempFile) {
        return tempFileMapper.insertSelective(tempFile);
    }

    @Override
    public int update(TempFile tempFile) {
        return tempFileMapper.updateByExampleSelective(tempFile, new TempFileExample());
    }

    @Override
    public int deleteById(Integer fileId) {
        return tempFileMapper.deleteByPrimaryKey(fileId);
    }
}
