package com.hh.mapserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StartServer {
	private static ExecutorService threadPool = Executors
			.newFixedThreadPool(10000);
	private static String msg = "";

	public static ExecutorService getThreadPool() {
		return threadPool;
	}

	public static void setThreadPool(ExecutorService threadPool) {
		StartServer.threadPool = threadPool;
	}

	public static void main(String[] args) {
		String str = "";
		// 登录服务器
		try {
			final ServerSocket loginServerSocket = new ServerSocket(10090);
			// 开启登录服务器接收用户登录
			threadPool.execute(new Runnable() {
				public void run() {
					while (true) {
						try {
							System.out.println("等待用户连接。。。");
							Socket socket = loginServerSocket.accept();
							if (socket != null) {
								System.out.println("客户端:" + socket.toString()
										+ "已连接上登录服务器...");
								// 为用户开启线程登录线程
								threadPool.execute(new LoginServer(socket));
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("登录服务器异常。。。");
		}
		
		/**
		 * 用户注册
		 */
				try {
					final ServerSocket registerServerSocket = new ServerSocket(10089);
					// 开启注册服务器
					threadPool.execute(new Runnable() {
						public void run() {
							while (true) {
								try {
									System.out.println("等待用户连接。。。");
									Socket socket = registerServerSocket.accept();
									if (socket != null) {
										System.out.println("客户端:" + socket.toString()
												+ "已连接上注册服务器。。。");
										// 为用户开启线程注册线程
										threadPool.execute(new RegisterServer(socket));
									}
								} catch (IOException e) {

									e.printStackTrace();
								}
							}
						}
					});

				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("注册服务器异常。。。");
				}
	
		
		
	
	}
}
