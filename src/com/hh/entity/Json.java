package com.hh.entity;


/**
 *
 * @author Administrator
 *
 */

public class Json {
	private int type;//类型用来判断状态
	private int userid;
	private String key;
	private String message;//判断数据是否请求成功
	
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Json(int type, int userid, String key, String message) {
		super();
		this.type = type;
		this.userid = userid;
		this.key = key;
		this.message = message;
	}

	public Json() {
		super();
	}
	
}
