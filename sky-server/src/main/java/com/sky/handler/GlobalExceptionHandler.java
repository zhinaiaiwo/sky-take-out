package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        // Duplicate entry '123456789012345678' for key 'idx_username'
        String message = ex.getMessage();
        if (message.contains("Duplicate entry")) {
//              正常写法
//            String[] split = message.split(" ");
//            String username = split[2];
//              一句话写完的
//            String split = message.split(" ")[2];
//              使用 stream() 流的
            String username = Arrays.stream(message.split(" ")).skip(2).limit(1).collect(Collectors.joining(" "));
            String msg = username + MessageConstant.ACCOUNT_EXISTS;     // 已存在
            return Result.error(msg);
        }else{
            return Result.error(MessageConstant.UNKNOWN_ERROR);         // 未知错误
        }
    }

}
