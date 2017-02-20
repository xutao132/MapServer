package com.hh.mapserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Random;
import org.json.JSONException;
import com.alibaba.fastjson.JSON;
import com.hh.dao.MapServerDao;
import com.hh.entity.JsonBean;
import com.hh.entity.User;
import com.hh.impdao.IMapServerDao;
import com.hh.utils.Utils;

public class RegisterServer implements IServer {
	private Socket socket = null;
	
	public RegisterServer(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		String message = null;
		try {
			message = ReciveMessage();// 接收客户端发来的消息的方法
			sendMessage(message);// 发送消息到客户端的方法
		} catch (IOException e) {
			System.out.println(e.getMessage());
			JsonBean jsonBean = new JsonBean(401, 0, "", e.getMessage());
			message = JSON.toJSONString(jsonBean) + "\n";
			try {
				sendMessage(message);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			JsonBean jsonBean = new JsonBean(401, 0, "", e.getMessage());
			message = JSON.toJSONString(jsonBean) + "\n";
			try {
				sendMessage(message);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 接收客户端发来的消息的方法
	 * 
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	private String ReciveMessage() throws IOException, Exception {
		String message = receiveString();// 处理客户端发送过来的消息
		User user = register(message);// 处理客户端发送过来的消息
		
		JsonBean jsonBean = new JsonBean(210, 0, "", "注册成功");
		System.out.println(user.getUid());
		jsonBean.setUser(user);
		String userJson = JSON.toJSONString(jsonBean) + "\n";
		return userJson;
	}

	/**
	 * 像客户端发送消息
	 */
	public void sendMessage(String jsonMessage) throws Exception {
		sendMessage(jsonMessage, socket);
	}

	/**
	 * 接收客户端发送过来的注册消息可能会发生异常
	 * 
	 * @return
	 */
	private String receiveString() throws IOException, Exception {
		try {
			String message = null;
			InputStream inputStream = socket.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			int i = 0;
			while (message == null || message.length() < 9) {// 不断循环的读取客户端发送过来的消息
				socket.getInputStream();
				if (socket == null || i > 50) {// 直到i>50,抛出异常
					throw new IOException("连接异常");
				}
				message = bufferedReader.readLine();
				i++;
			}
			org.json.JSONObject jsonObject = new org.json.JSONObject(message);// 解析客户端发送过来的消息
			int type = jsonObject.getInt("type");
			if (type != 210) {// 如果类型不为210的话抛出异常
				throw new Exception("注册类型错误");
			}
			return message;
		} catch (JSONException e) {
			throw new Exception("JSON字符串错误");
		}
	}

	/**
	 * 处理验证客户端注册发送过来消息，并向数据库注册账号
	 * 
	 * @param messag
	 *            客户端发送过来的字符串
	 * @return 注册成功后的User对象
	 */
	private User register(String message) throws Exception {
		IMapServerDao iuser = new IMapServerDao();
		//把字符串转换为Javabean对象（message——》Javabean对象）
		JsonBean jsonBean = JSON.parseObject(message, JsonBean.class);
		User user = jsonBean.getUser();
		if (user == null) {
			throw new Exception("服务器接收到空对象");
		}
		int id = getUserid(iuser, 1111111, 2000000000);
		String password = user.getUpassword();
		String account = user.getUaccount();
		user.setUid(id);//将生成的随机数赋值给用户
		if (password != null
				&& Utils.regularExpression(account, "^1[3|4|5|7|8][0-9]{9}$")
				&& Utils.regularExpression(password, "^[a-zA-Z]\\w{5,17}$")) {
			iuser.registerUser(user);
		} else {
			throw new Exception("用户名或密码格式错误");
		}
		return user;
	}

	public void sendMessage(String jsonMessage, Socket socket) throws Exception {
		try {
			OutputStream outputStream = socket.getOutputStream();
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
			bufferedWriter.write(jsonMessage);
			bufferedWriter.flush();
		} catch (IOException e) {
			//e.printStackTrace();
		} finally {
			// 发送完消息关闭socket
			try {
				socket.close();
				socket = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 生成账号
	 * 
	 * @param iUser
	 *            执行账号查询操作
	 * @param min
	 *            账号上限
	 * @param max
	 *            账号上限
	 * @return 返回生成的ID号
	 * @throws Exception
	 */
	public static int getUserid(MapServerDao iUser, int min, int max)
			throws Exception {

		int s = 0;
		int i = 0;
		do {
			i++;
			Random random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
		} while (iUser.searchUser(s) == null && i <= 3);
		if (s > 2111111111 || s < 1111111) {// 生成的帐号必须在1000000与2000000000之间
			throw new Exception("帐号创建失败，请稍候再试");
		}
		return s;

	}
}
