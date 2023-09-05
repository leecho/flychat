package com.honvay.flyai.framework.core.protocol;


import com.honvay.flyai.framework.core.ErrorMessage;

import java.io.Serializable;
/**
 * @author LIQIU
 * created on 2018/12/24
 **/
public class Response<T> implements Serializable {

	/**
	 * 结果是否成功
	 */
	private Boolean success;

	/**
	 * 返回信息
	 */
	private String message;

	/**
	 * 返回码
	 */
	private String code;

	/**
	 * 数据
	 */
	private T data;


	private Response(Boolean success, String message, String code, T data) {
		this.success = success;
		this.message = message;
		this.code = code;
		this.data = data;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}

	public String getCode() {
		return code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	/**
	 * 构建返回结果
	 *
	 * @param success 是否成功
	 * @param msg     错误信息
	 * @param code    错误码
	 * @param data    数据
	 * @param <T>     数据类型
	 * @return 结果
	 */
	public static <T> Response<T> of(Boolean success, String msg, String code, T data) {
		return new Response<T>(success, msg, code, data);
	}

	/**
	 * 构建返回结果，code默认值为0
	 *
	 * @param success 是否成功
	 * @param msg     错误信息
	 * @param data    数据
	 * @param <T>     数据类型
	 * @return 结果
	 */
	public static <T> Response<T> of(Boolean success, String msg, T data) {
		return of(success, msg, "0", data);
	}

	/**
	 * 构建成功结果
	 *
	 * @param msg  错误信息
	 * @param data 数据
	 * @param <T>  数据类型
	 * @return 结果
	 */
	public static <T> Response<T> success(String msg, T data) {
		return of(Boolean.TRUE, msg, data);
	}

	/**
	 * 构建失败结果
	 *
	 * @param code 错误码
	 * @param msg  错误信息
	 * @param data 数据
	 * @param <T>  数据类型
	 * @return 结果
	 */
	public static <T> Response<T> fail(String code, String msg, T data) {
		return of(Boolean.FALSE, msg, code, data);
	}

	/**
	 * 构建失败结果
	 *
	 * @param msg  错误信息
	 * @param data 数据
	 * @param <T>  数据类型
	 * @return 结果
	 */
	public static <T> Response<T> fail(String msg, T data) {
		return of(Boolean.FALSE, msg, data);
	}

	/**
	 * 构建成功结果带信息
	 *
	 * @param <T> 数据类型
	 * @return 结果
	 */
	public static <T> Response<T> success() {
		return success(null, null);
	}

	/**
	 * 构建成功结果带信息
	 *
	 * @param msg 错误信息
	 * @param <T> 数据类型
	 * @return 结果
	 */
	public static <T> Response<T> success(String msg) {
		return success(msg, null);
	}

	/**
	 * 构建成功结果待数据
	 *
	 * @param data 数据
	 * @param <T>  数据类型
	 * @return 结果
	 */
	public static <T> Response<T> success(T data) {
		return success(null, data);
	}

	/**
	 * 构建失败结果待数据
	 *
	 * @param msg 错误信息
	 * @param <T> 数据类型
	 * @return 结果
	 */
	public static <T> Response<T> fail(String msg) {
		return of(Boolean.FALSE, msg, null);
	}

	/**
	 * 构建失败结果待数据
	 *
	 * @param code 错误码
	 * @param msg  错误信息
	 * @param <T>  数据类型
	 * @return 结果
	 */
	public static <T> Response<T> fail(String code, String msg) {
		return of(Boolean.FALSE, msg, code, null);
	}

	/**
	 * 构建失败结果待数据
	 *
	 * @param errorMessage 错误数据
	 * @param <T>          数据类型
	 * @return 结果
	 */
	public static <T> Response<T> fail(ErrorMessage errorMessage) {
		return fail(errorMessage.getCode(), errorMessage.getMessage());
	}

	/**
	 * 构建失败结果待数据
	 *
	 * @param errorMessage 错误信息
	 * @param data         数据
	 * @param <T>          类型
	 * @return 结果
	 */
	public static <T> Response<T> fail(ErrorMessage errorMessage, T data) {
		return fail(errorMessage.getCode(), errorMessage.getMessage(), data);
	}

	/**
	 * 构建失败结果带数据
	 *
	 * @param data 数据
	 * @param <T>  类型
	 * @return 结果
	 */
	public static <T> Response<T> fail(T data) {
		return fail("", data);
	}
}
