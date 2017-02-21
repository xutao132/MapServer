package com.hh.dao;

import com.hh.entity.User;

public interface MapServerDao {	
	/**
	 * 用户注册
	 * @param user注册的用户
	 * @return注册成功返回的对象
	 * @throws Exception
	 */
	User registerUser(User user)throws Exception;
	
	/**
	 * 根据ID查找用户
	 * @param userid  用户id
	 * @return 返回查找到的用户 
	 */
	User searchUser(int userid)throws Exception;
	
	
	User searchUser(String account)throws Exception;

}
