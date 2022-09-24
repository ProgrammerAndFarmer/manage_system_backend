package com.huajie.dto;

import com.huajie.entry.User;
import lombok.Data;

@Data
public class UserDto extends User {
    String token;
}
