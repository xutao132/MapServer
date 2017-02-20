package com.hh.entity;

public class JsonBean extends Json{
	private User user;//用户对象
	
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	/**
	 * 获取所有用户
	 * @param type 类型
	 * @param userid 用户ID
	 * @param key key
	 * @param message 消息
	 */
	public JsonBean(int type, int userid, String key, String message) {
		super(type, userid, key, message);
	}
	
	public JsonBean() {
		super();
	}

	
}
