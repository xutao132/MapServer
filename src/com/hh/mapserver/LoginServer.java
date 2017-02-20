package com.hh.mapserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.alibaba.fastjson.JSON;
import com.hh.entity.JsonBean;
import com.hh.entity.User;

public class LoginServer implements IServer {
	private Socket socket = null;

	public LoginServer(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		String message = null;
		try {
			message = ReceiveMessage();
			sendMessage(message);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			// e.printStackTrace();
			message = JSON.toJSONString(new JsonBean(401, -1, null, e.getMessage())) + "\n";
			sendMessage(message);
		}
	}

	/**
	 * 发送服务端“处理结果消”息到客户端
	 * @throws IOException 
	 */

	public void sendMessage(String jsonMessage){
		sendMessage(jsonMessage, socket);
	}


	/**
	 * 接收用户登录消息
	 * @throws Exception 
	 * @throws IOException 
	 * 
	 * @return：登录消息的处理结果
	 */
	private String ReceiveMessage() throws IOException, Exception {
		String StrMessage = ReceiveStringMessage();//读取客户端登录时发送过来的字符串
		JsonBean jsonBean = (JsonBean) JSON.parseObject(StrMessage, JsonBean.class);//将字符串转换成Javabean对象
		User user = jsonBean.getUser();
		
		return null;
	}
	
	
	/**
	 * 接收客户端登录时传送过来的消息
	 * @return
	 * @throws Exception 
	 */
	public String ReceiveStringMessage() throws IOException, Exception {

		try {
			String message = null;
			InputStream inputStream = socket.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			message = bufferedReader.readLine();
			System.out.println(message);
			org.json.JSONObject jsonObject = new org.json.JSONObject(message);
			int type = jsonObject.getInt("type");
			if (type != 200) {
				throw new Exception("账号异常");
			}
			return message;
		} catch (IOException e) {
			throw new IOException("与客户端连接异常");
		} 
	}
	/**
	 * 向指定的客户端发送消息
	 */
	public void sendMessage(String jsonMessage, Socket socket){
		try {
			OutputStream os = socket.getOutputStream();//获取客户端的IO流
			BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(os));
			bWriter.write(jsonMessage);
			bWriter.flush();
		} catch (IOException e) {
			System.out.println("OI异常");
			e.printStackTrace();
		}finally{//消息已发完关闭流
			try {
				if(null!=socket)
				socket.close();
			} catch (Exception e2) {
			}
		}

	}

}
