package com.kitty.springcloud.common.domain;

/**
 * 
 * 业务实现类传递数据实体类
 * 
 * @author Kitty
 *
 * @param <T>
 */
public class ResponseService<T> {

	// 默认状态0为成功，其它对于业务错误代码
	private int status = 0;

	private String message = "OK";

	private T data;

	public ResponseService() {
	}

	public ResponseService(T data) {
		this.data = data;
	}

	public ResponseService(int status, String message, T data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
