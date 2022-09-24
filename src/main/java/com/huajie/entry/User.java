package com.huajie.entry;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    Long id;
    String userName;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;
    String nickname;
    String emailAddress;
    String phoneNumber;
    String address;

    @TableField(fill= FieldFill.INSERT)
    String avatarUrl;

    @TableField(fill= FieldFill.INSERT)
    LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    LocalDateTime updateTime;
}
