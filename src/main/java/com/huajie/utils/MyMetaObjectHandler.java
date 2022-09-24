package com.huajie.utils;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.sun.prism.impl.BaseContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("avatarUrl", "https://i1.hdslb.com/bfs/face/02547965e799329040e18fdf886c6a105231b77d.jpg@240w_240h_1c_1s.webp");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", LocalDateTime.now());
    }
}
