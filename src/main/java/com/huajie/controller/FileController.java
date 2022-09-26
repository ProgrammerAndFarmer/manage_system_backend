package com.huajie.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huajie.entry.MyFile;
import com.huajie.entry.Result;
import com.huajie.entry.User;
import com.huajie.service.FileService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.Servlet;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Autowired
    private FileService fileService;

    /**
     * 分页查询文件
     * @param page
     * @param pageSize
     * @param fileName
     * @param fileType
     * @return
     */
    @GetMapping("/page")
    public Result<Page> getPage(int page, int pageSize, String fileName, String fileType) {
        Page<MyFile> pageInfo = new Page(page, pageSize);

        LambdaQueryWrapper<MyFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MyFile::getIsDeleted, false);
        queryWrapper.like(StringUtils.isNotEmpty(fileName), MyFile::getFileName, fileName);
        queryWrapper.like(StringUtils.isNotEmpty(fileType), MyFile::getFileType, fileType);
        queryWrapper.orderByDesc(MyFile::getUploadTime);

        fileService.page(pageInfo, queryWrapper);

        return Result.success(pageInfo);
    }

    /**
     * 上传文件
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public Result<String> uploadFile(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String fileType = FileUtil.extName(originalFilename);//获取文件扩展名
        Long fileSize = file.getSize();
        //先存储到磁盘
        File uploadParentFileFolder = new File(fileUploadPath);
        //判断配置的文件目录文件夹是否存在，若不存在，则新创建一个文件目录文件夹
        if (!uploadParentFileFolder.exists()) {
            uploadParentFileFolder.mkdirs();
        }
        //定义一个文件的唯一的标识码
        String uuid= IdUtil.fastSimpleUUID();
        //定义要上传的文件完整的路径名
        File uploadFile = new File(fileUploadPath + uuid + StrUtil.DOT + fileType);
        file.transferTo(uploadFile);
        //把文件存到数据库
        MyFile saveFile = new MyFile();
        saveFile.setFileName(FileUtil.mainName(originalFilename));
        saveFile.setFileType(fileType);
        saveFile.setFileSize(fileSize / 1024 == 0 ? 1 : fileSize / 1024);
        saveFile.setFileUrl("http://localhost:8080/file/" + uuid + StrUtil.DOT + fileType);
        if (fileService.save(saveFile)) {
            return Result.success(saveFile.getFileUrl());
        } else {
            return Result.error("文件上传失败！");
        }
    }

    /**
     * 更新文件状态
     * @param fileId
     * @return
     */
    @PutMapping("/enable")
    public Result<String> changeEnableStatus(@RequestParam("id") Long fileId) {
        MyFile file = fileService.getById(fileId);
        file.setIsEnabled(file.getIsEnabled() ? false : true);
        boolean flag = fileService.updateById(file);
        if (flag) {
            return Result.success("启动/禁用成功！");
        } else {
            return Result.error("启动/禁用失败！");
        }
    }

    /**
     * 删除文件
     * @param fileId
     * @return
     */
    @DeleteMapping("/del")
    public Result<String> deleteById(@RequestParam("id") Long fileId) {
        MyFile file = fileService.getById(fileId);
        file.setIsDeleted(true);
        boolean flag = fileService.updateById(file);
        if (flag) {
            return Result.success("文件删除成功！");
        } else {
            return Result.error("文件删除失败！");
        }
    }

    /**
     * 批量删除用户
     * @param ids
     * @return
     */
    @DeleteMapping("/del/batch")
    public Result<String> deleteBatchUsers(@RequestBody List<Long> ids) {
        List<MyFile> myFiles = fileService.listByIds(ids);
        for (MyFile file : myFiles) {
            file.setIsDeleted(true);
        }
        boolean flag = fileService.updateBatchById(myFiles);
        if (flag) {
            return Result.success("用户批量删除成功！");
        } else {
            return Result.error("用户批量删除失败！");
        }
    }

    /**
     * 文件下载
     * @param fileUUID
     * @param response
     * @throws IOException
     */
    @GetMapping("/{fileUUID}")
    public void downloadFile(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        System.out.println(fileUUID);
        //根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        //设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachmen;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));

        //读取文件字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }
}
