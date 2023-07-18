package com.honvay.cola.framework.web;


import java.lang.annotation.*;

/**
 * @author LIQIU
 * created on 2020/9/15
 **/
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponsePayload {
}
