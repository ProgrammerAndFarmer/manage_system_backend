package com.huajie.exceptionHandle;

import com.baomidou.mybatisplus.extension.api.R;
import com.huajie.entry.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public Result<String> exceptionHandler(RuntimeException ex) {
        ex.printStackTrace();
        return Result.error(ex.getMessage());
    }
}
