package org.example.gateway.handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 * 用于统一处理项目中的异常
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    /**
     * 拦截所有异常
     *
     * @param e 异常对象
     * @return 返回错误响应
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus
    public void handlerException(Exception e) {
        // 返回错误响应，包含异常信息
        logger.error("发生错误:{}",e.getMessage());
    }
}
