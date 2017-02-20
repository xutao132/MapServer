package com.hh.test;

import java.io.IOException;
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
		 * 测试注册（成功）?
		 */
		System.out.println("--------1------");
		User user = new User(-1,"18387390391","abc123");
		System.out.println("--------2------");
		JsonBean  jsonBean = new JsonBean(210, user.getUid(),"","");
		System.out.println("--------3------");
		jsonBean.setUser(user);
		System.out.println("---------4-----");
		soc = new Socket();
		System.out.println("---------5-----");
		soc.connect(new InetSocketAddress("10.7.184.60", 10089),100);
		
		System.out.println("----------6----");
		PrintWriter printWriter = 
				new PrintWriter(new OutputStreamWriter(soc.getOutputStream(),"utf-8"),true);
		System.out.println("---------7-----"+jsonBean.getUser());
	    printWriter.println(JSON.toJSONString(jsonBean)+"\n");
		System.out.println(JSON.toJSONString(jsonBean));
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
