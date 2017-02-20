package com.hh.mapserver;

import java.net.Socket;


public interface IServer extends Runnable{
	/**
	 * 获取好友列表
	 */
	public static final int GET_FRIEND_LIST = 1;
	
	/**
	 * 发送给所有用户
	 */
	public static final int LOCATION_ANYONE = 3;
	/**
	 * 用户下线
	 */
	public static final int USER_OFFLINE = 5;
	/**
	 * 查找用户
	 */
	public static final int USER_SEARCH = 7;
	/**
	 * 添加用户
	 */
	public static final int USER_ADD = 9;
	
	/**
	 * 修改个人信息
	 */
	public static final int USER_UPDATA = 11;
	/**
	 * 添加用户
	 */
	public static final int USER_DEL = 13;
	
	/**
	 * 修改个人信息
	 */
	public static final int USER_GROUP_ADD = 15;
	/**
	 * 发送坐标
	 */
	public static final int LOCATION = 17;
	/**
	 * 添加标注
	 * 	 
	 */
	public static final int ADD_MARK = 19;
	/**
	 * 删除标注
	 * 	 
	 */
	public static final int DEL_MARK = 21;
	/**
	 * 查询标注列表
	 * 	 
	 */
	public static final int SEACH_MARK_LIST = 23;
	
	/**
	 * 
	 * @param socket
	 * @param jsonMessage
	 */
	public abstract void sendMessage(String jsonMessage)throws Exception ;
	/**
	 * 
	 * @param socket
	 * @param jsonMessage
	 */
	public abstract void sendMessage(String jsonMessage,Socket socket)throws Exception ;
}
