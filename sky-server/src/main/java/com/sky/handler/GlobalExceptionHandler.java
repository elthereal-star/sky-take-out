package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public Result exceptionHandler(BaseException ex) {
        log.error("Business exception: {}", ex.getMessage(), ex);
        return Result.error(ex.getMessage());
    }

//    @ExceptionHandler
//    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex) {
//        log.error("SQL constraint violation: {}", ex.getMessage(), ex);
//        String message = ex.getMessage();
//        if (message != null && message.contains("Duplicate entry")) {
//            String[] parts = message.split(" ");
//            if (parts.length > 2) {
//                return Result.error(parts[2] + " already exists");
//            }
//        }
//        return Result.error("Database operation failed");
//    }

    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex) {
       String message = ex.getMessage();
       if(message.contains("Duplicate entry")){
           String[] parts = message.split(" ");
           String username = parts[2];
           String msg=username + MessageConstant.ALREADY_EXISTS;
           return Result.error(msg);
        }
       else{
           return Result.error(MessageConstant.UNKNOWN_ERROR);
       }
    }

}
