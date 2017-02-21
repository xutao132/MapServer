package com.hh.utils;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class UserPool {
	private static Map<String, Map<String, Socket>> userPool = new HashMap<String, Map<String, Socket>>();
//	private static Map<String, Location> userLocationPool = new HashMap<String, Location>();
//	private static Map<String, Socket> userLocationMap = new HashMap<String, Socket>();
	private static Map<String, Socket> userMessageMap = new HashMap<String, Socket>();
	
	public static Map<String, Socket> getUserMessageMap() {
		return userMessageMap;
	}
//	public static Map<String, Socket> getUserLocationMap() {
//		return userLocationMap;
//	}
//	/**
//	 * 获取所有用户的位置集合
//	 * @return
//	 */
//	public static Map<String, Location> getUserLocationPool() {
//		return userLocationPool;
//	}
//	/**
//	 * 获取指定用户的位置
//	 * @param userid
//	 * @return
//	 */
//	public static Location getLocation(String userid) {
//		return userLocationPool.get(userid);
//	}
//	/**
//	 * 将指定用户位置加入用户位置池
//	 * @param userid
//	 * @param location
//	 */
//	public  static void setLocation(String userid,Location location) {
//		userLocationPool.put(userid, location);
//	}

	public synchronized static Map<String, Map<String, Socket>> getUserPool() {
		return userPool;
	}
	/**
	 * 获取所有在线用户的Socket对象
	 */
	public synchronized static List<Socket> getListUserSocket() {
		List<Socket> sockets = new ArrayList<Socket>();
		Set<Entry<String, Map<String, Socket>>> entrySet = userPool.entrySet();
		Iterator<Entry<String, Map<String, Socket>>> iterator = entrySet
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, Map<String, Socket>> entry = iterator.next();
			String key = entry.getKey();
			Map<String, Socket> map = userPool.get(key);
			Set<Entry<String, Socket>> entrySet2 = map.entrySet();
			Iterator<Entry<String, Socket>> iterator2 = entrySet2.iterator();
			while (iterator2.hasNext()) {
				Entry<String, Socket> entry2 = iterator2.next();
				String key2 = entry2.getKey();
				sockets.add(map.get(key2));
			}
		}

		return sockets;
	}
//	/**
//	 * 获取所有在线用户的位置Socket对象
//	 */
//	public synchronized static List<Socket> getListUserLocationSocket() {
//		List<Socket> sockets = new ArrayList<Socket>();
//		Set<Entry<String,Socket>> entrySet = userLocationMap.entrySet();
//		Iterator<Entry<String, Socket>> iterator = entrySet.iterator();
//		while(iterator.hasNext()){
//			Entry<String, Socket> next = iterator.next();
//			String key = next.getKey();
//			sockets.add(userLocationMap.get(key));
//		}
//		
//		return sockets;
//	}
	/**
	 * 根据userid获取指定在好友Socket
	 * @param userid 用户ID
	 * @return 
	 */
	public synchronized static List<Socket> getUserSocket(String userid) {
		List<Socket> sockets = new ArrayList<Socket>();
	Map<String, Socket> map = userPool.get(userid);
	Set<Entry<String,Socket>> entrySet = map.entrySet();
	Iterator<Entry<String, Socket>> iterator = entrySet.iterator();
	while(iterator.hasNext()){
		Entry<String, Socket> next = iterator.next();
		String key = next.getKey();
		Socket socket = map.get(key);
		sockets.add(socket);
	}
		
		return sockets;
	}

	// public static void main(String[] args) {
	// Map<String, Socket> map = new HashMap<String, Socket>();
	// map.put("s1", new Socket());
	// map.put("s3", new Socket());
	// map.put("s2", new Socket());
	// userPool.put("1", map);
	// Map<String, Socket> map1 = new HashMap<String, Socket>();
	// map1.put("d1", new Socket());
	// map1.put("d3", new Socket());
	// map1.put("d2", new Socket());
	// userPool.put("2", map1);
	// List<Socket> listUserSocket = getListUserSocket();
	// for (int i = 0; i < listUserSocket.size(); i++) {
	// System.out.println(listUserSocket.get(i));
	// }
	// }
}
