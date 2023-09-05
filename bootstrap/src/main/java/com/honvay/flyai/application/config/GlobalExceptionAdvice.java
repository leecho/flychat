package com.honvay.flyai.application.config;

import com.honvay.flyai.framework.core.ErrorConstants;
import com.honvay.flyai.framework.core.exception.ServiceException;
import com.honvay.flyai.framework.core.protocol.Response;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全局异常捕获
 *
 * @author cgy
 * @since 2020-10-16
 */
@Slf4j
@ResponseBody
@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public Response<Void> handleException(Exception e) {
        log.error("统一异常处理 {}", e.getMessage(), e);
        e.printStackTrace();
        return Response.fail(ErrorConstants.INTERNAL_SERVER_ERROR.getCode(), "系统错误");
    }

    @ExceptionHandler(ServiceException.class)
    public Response<Void> handleServiceException(ServiceException se) {
        log.warn("业务异常 {}", se.getMessage(), se);
        return Response.fail(se.getCode(), se.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("请求方法不支持：", e);
        return Response.fail(ErrorConstants.INTERNAL_SERVER_ERROR.getCode(), "请求方法不支持");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Response<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.warn("参数错误：", e);
        return Response.fail(ErrorConstants.MISSING_ARGUMENT);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Response<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.warn("参数错误：", e);
        return Response.fail(ErrorConstants.ILLEGAL_ARGUMENT);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Response<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("参数错误：", e);
        return Response.fail(ErrorConstants.ILLEGAL_ARGUMENT);
    }

    @ExceptionHandler(BindException.class)
    public Response<Void> handleBindException(BindException e) {
        log.warn("参数错误：", e);

        String errorMessage = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));
        return Response.fail(ErrorConstants.ILLEGAL_ARGUMENT.getCode(), errorMessage);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Response<Void> handleConstraintViolationException(ConstraintViolationException e) {
        log.warn("参数错误：", e);
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        String message = constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(","));
        return Response.fail(ErrorConstants.ILLEGAL_ARGUMENT.getCode(), message);
    }


    /**
     * 处理 @RequestBody validate 失败后抛出的异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<Void> handleBeanValidation(MethodArgumentNotValidException e) {
        log.warn("参数错误：", e);
        String message = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(";"));

        return Response.fail(ErrorConstants.ILLEGAL_ARGUMENT.getCode(), message);
    }
}
