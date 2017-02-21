package com.hh.mapserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.hh.entity.JsonBean;
import com.hh.entity.User;
import com.hh.impdao.IMapServerDao;
import com.hh.utils.UserPool;
import com.hh.utils.Utils;

public class LoginServer implements IServer {
	private Socket socket = null;

	public LoginServer(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		String message = null;
		try {
			message = ReceiveMessage();
			// System.out.println(message);
			sendMessage(message);
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			// e.printStackTrace();
			message = JSON.toJSONString(new JsonBean(401, -1, null, e
					.getMessage())) + "\n";
			sendMessage(message);
		}
	}

	/**
	 * 发送服务端“处理结果消”息到客户端
	 * 
	 * @throws IOException
	 */

	public void sendMessage(String jsonMessage) {
		sendMessage(jsonMessage, socket);
	}

	/**
	 * 接收用户登录消息
	 * 
	 * @throws Exception
	 * @throws IOException
	 * 
	 * @return：登录消息的处理结果
	 */
	private String ReceiveMessage() throws IOException, Exception {
		String StrMessage = ReceiveStringMessage();// （1）读取客户端登录时发送过来的字符串
		// (2)处理客户端发送过来的消息抽取出其中的user对象，进行查看该对象是是否存在
		JsonBean jsonBean = (JsonBean) JSON.parseObject(StrMessage,JsonBean.class);// 将字符串转换成Javabean对象
		User user = jsonBean.getUser();
		
		
		int uid = user.getUid();// 获取到了客户端发送过来的用户id
		String uaccount = user.getUaccount();// 获取到了客户端发送过来的用户账户
		String upassword = user.getUpassword();// 获取到了客户端发送过来的用户密码
		// System.out.println(uid + "," + uaccount + "," + upassword);
		
		user = login(uid, uaccount, upassword);// 验证用户登录的方法
		
		Map<String, Map<String, Socket>> userPool = UserPool.getUserPool();//获取用户线程池理的所有用户
		//如果我通过登录验证之后，为了防止我的该用户还在其他地方登录，我就得移除其他地方登录的用户
		remove(userPool, uid);
		//移除其他地方登录的用户之后，把我当前登录的用户添加进当前用户池中
		String key = getKey(user.getUaccount());
		
		Map<String, Socket> map2 = new HashMap<String, Socket>();//新建一个用户的map集合对象
		map2.put(key, new Socket());//将我当前登录生成的用户对象存入到map集合中去
		userPool.put(user.getUid() + "", map2);
		JsonBean jsonBean2 = new JsonBean(200, uid, key, "登录成功");
		jsonBean2.setUser(user);
		String userJson = JSON.toJSONString(jsonBean2)+"\n";
		return userJson;
	}

	private String getKey(String uaccount) {
		// TODO Auto-generated method stub
		return new Date().getTime() + uaccount;
	}

	/**
	 * 移除其他地方登录的用户
	 * @param userPool
	 * @param uid
	 */
	private void remove(Map<String, Map<String, Socket>> userPool,int uid) {
		if (userPool != null) {
			Map<String, Socket> map = userPool.get(uid);
			if (map != null) {
				Set<Entry<String, Socket>> entrySet = map.entrySet();
				Iterator<Entry<String, Socket>> iterator = entrySet.iterator();
				while (iterator.hasNext()) {
					Entry<String, Socket> next = iterator.next();
					String key = next.getKey();
					Socket socket2 = map.get(key);
					try {
						// 向已经在线的用户发送下线通知
						sendMessage(
								JSON.toJSONString(new JsonBean(407, uid, key, "你账号在其他地点登录,你被强制下线"))+"\n",
								socket2);// 强制下线
					} catch (Exception e) {
						System.out.println("用户未在线");
					}
					userPool.remove(uid);
				}
			}
		}
		
	}

	/**
	 * 用户登录的验证
	 * 
	 * @param uid
	 *            -->客户端传过来的id
	 * @param uaccount
	 *            -->客户端传过来的uaccount
	 * @param upassword
	 *            -->客户端传过来的Upassword
	 * @return 用户
	 */
	private User login(int uid, String uaccount, String upassword) {
		User user = null;
		IMapServerDao iServerDao = new IMapServerDao();
		// 如果账户和密码不为空并且符合规则就去数据库验证是否正确
		if (uaccount != null
				&& Utils.regularExpression(uaccount, "^1[3|4|5|7|8][0-9]{9}$")
				&& upassword != null
				&& Utils.regularExpression(upassword, "^[a-zA-Z]\\w{5,17}$")) {
			
			user = iServerDao.login(uaccount, upassword);// 数据库验证
			System.out.println(user.toString());
		} else {
			System.out.println("用户信息填写错误");
		}

		return user;
	}

	/**
	 * 接收客户端登录时传送过来的消息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String ReceiveStringMessage() throws IOException, Exception {

		try {
			String message = null;
			InputStream inputStream = socket.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			message = bufferedReader.readLine();
			// System.out.println(message+"00000000000000000000000000000");
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
	public void sendMessage(String jsonMessage, Socket socket) {
		try {
			OutputStream os = socket.getOutputStream();// 获取客户端的IO流
			BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(
					os));
			bWriter.write(jsonMessage);
			bWriter.flush();
			System.out.println("已发送到客户端。。。。。");
		} catch (IOException e) {
			// System.out.println("OI异常");
			// e.printStackTrace();
		} finally {// 消息已发完关闭流
			try {
				if (null != socket)
					socket.close();
			} catch (Exception e2) {
			}
		}

	}

}
