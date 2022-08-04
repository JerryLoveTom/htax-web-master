/**
 * Copyright (c) 2016-2019 HTAX All rights reserved.
 *
 * 北京华泰安信科技有限公司
 *
 * 版权所有，侵权必究！
 */

package com.htax.common.exception;

/**
 * 自定义异常
 *
 * @author Mark ppzz
 */
public class HTAXException extends RuntimeException {
	private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 500;

    public HTAXException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public HTAXException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}

	public HTAXException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}

	public HTAXException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}


}
