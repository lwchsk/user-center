package com.hsk.usercenter.exception;

import com.hsk.usercenter.common.BaseResponse;
import com.hsk.usercenter.common.ErrorCode;
import com.hsk.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Date:2024/05/08
 * Author:hsk
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse BusinessRuntimeExceptionHandler(BusinessException e) {
        log.error(e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse RuntimeExceptionHandler(RuntimeException e) {
        log.error(e.getMessage(), e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }
}
