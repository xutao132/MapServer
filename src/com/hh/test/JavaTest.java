package com.hh.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream.GetField;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import com.alibaba.fastjson.JSON;
import com.hh.entity.JsonBean;
import com.hh.entity.User;
import com.hh.impdao.IMapServerDao;

public class JavaTest {
	static Socket soc;
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
		 * 测试注册/登录（成功）
		 */
		User user = new User(1,"13678132676","abc123");
		JsonBean  jsonBean = new JsonBean(200, user.getUid(),"","");
		jsonBean.setUser(user);
		soc = new Socket();
		soc.connect(new InetSocketAddress("10.7.184.56", 10088),100);
		PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(soc.getOutputStream(),"utf-8"),true);
	    printWriter.println(JSON.toJSONString(jsonBean)+"\n");
	    
	    BufferedReader bufferedReader = new BufferedReader(
	    		new InputStreamReader(soc.getInputStream()));
	    String readLine = bufferedReader.readLine();
	    System.out.println(readLine);
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
