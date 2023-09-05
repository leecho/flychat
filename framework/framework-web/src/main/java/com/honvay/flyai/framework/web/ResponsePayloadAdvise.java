package com.honvay.flyai.framework.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.honvay.flyai.framework.core.protocol.Response;
import jakarta.annotation.Resource;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


/**
 * @author LIQIU
 * created on 2019/1/14
 **/
@RestControllerAdvice
public class ResponsePayloadAdvise implements ResponseBodyAdvice<Object> {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.hasMethodAnnotation(ResponsePayload.class)
                || returnType.getContainingClass().getAnnotation(ResponsePayload.class) != null;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof String || body == null) {
            try {
                response.getHeaders().add("Content-Type",MediaType.APPLICATION_JSON_VALUE);
                return objectMapper.writeValueAsString(Response.success(body));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        if(body instanceof Response){
            return body;
        }
        return Response.success(body);
    }
}
