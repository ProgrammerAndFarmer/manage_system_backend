package com.huajie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huajie.entry.MyFile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper extends BaseMapper<MyFile> {
}
