package io.yzecho.netdisk.mapper;

import io.yzecho.netdisk.model.TempFile;
import io.yzecho.netdisk.model.TempFileExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TempFileMapper {
    long countByExample(TempFileExample example);

    int deleteByExample(TempFileExample example);

    int deleteByPrimaryKey(Integer fileId);

    int insert(TempFile record);

    int insertSelective(TempFile record);

    List<TempFile> selectByExample(TempFileExample example);

    TempFile selectByPrimaryKey(Integer fileId);

    int updateByExampleSelective(@Param("record") TempFile record, @Param("example") TempFileExample example);

    int updateByExample(@Param("record") TempFile record, @Param("example") TempFileExample example);

    int updateByPrimaryKeySelective(TempFile record);

    int updateByPrimaryKey(TempFile record);
}