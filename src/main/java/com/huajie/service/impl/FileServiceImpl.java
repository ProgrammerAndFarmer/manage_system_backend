package com.huajie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huajie.entry.MyFile;
import com.huajie.mapper.FileMapper;
import com.huajie.service.FileService;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, MyFile> implements FileService  {
}
