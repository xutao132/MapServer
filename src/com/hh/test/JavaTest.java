package com.hh.test;

import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import com.alibaba.fastjson.JSON;
import com.hh.entity.JsonBean;
import com.hh.entity.User;
import com.hh.impdao.IMapServerDao;

public class JavaTest {
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		/**
		 * 测试数据库连接（成功）
		 */
		
		/*try {
			Socket soc = new Socket("10.7.184.56", 10088);
			IMapServerDao user = new IMapServerDao();
			User user1 = new User(getUserid1(user, 0, 222222222),"abc123", "18313364024");
			user.registerUser(user1);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/**
		 * 测试注册（成功）
		 */
		Socket soc = new Socket("10.7.184.56", 10089);
		JsonBean  jsonBean = new JsonBean(1, 1, "", "");
		IMapServerDao dao = new IMapServerDao();
		User user = new User(getUserid1(dao, 11111111, 222222222),"1357803720","abc123");
		jsonBean.setUser(user);
		System.out.println(JSON.toJSONString(jsonBean));
		dao.registerUser(user);
	}
	
	private static int getUserid1(IMapServerDao user, int min, int max) throws Exception {
		int s = 0;
		int i = 0;
		do {
			i++;
			Random random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
		} while (user.searchUser(s) == null && i <= 3);
		if(s > 2111111111 || s < 1111111){//生成的帐号必须在1111111与2111111111之间
			throw new Exception("帐号创建失败，请稍候再试");
		}
		return s;
	}

}
