package com.huajie.entry;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("file")
public class MyFile {
    private Long id;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String fileUrl;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime uploadTime;

    private Boolean isDeleted;
    private Boolean isEnabled;
}
