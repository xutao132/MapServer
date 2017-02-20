package com.hh.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCUtils {
	private static java.sql.Connection conn = null; // 声明连接
	private Statement st = null; // 声明Statement对象
	private ResultSet rs = null;
	private static String url;
	private static String username;
	private static String password;
	/**
	 * 定义静态代码块链接数据库
	 */
	static{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			url = "jdbc:mysql://localhost:3306/mapserversql?characterEncoding=utf-8"; // 数据库db_test的URL
			username = "root";
			password = "123";
		} catch (ClassNotFoundException e) {
			System.out.println("加载数据库驱动异常");
			e.printStackTrace();
		}
	}
	// 获取连接
		public static Connection getConnection() throws Exception{
			try {
				conn = DriverManager.getConnection(url,username,password);
					return conn;
			} catch (SQLException e) {
				throw new Exception("获取数据库连接失败",e);
			}

		}
		/*
		 * 释放资源
		 */
		public static void closeConnection(Connection connection,Statement statement,ResultSet resultSet) {
			try {
				if(resultSet != null) 
					resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				try {
					if(statement != null)
						statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}finally{
					try {
						if(connection != null)
							connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}finally{
						resultSet = null;
						connection = null;
						statement = null;
					}
				}
			}
		}
	
}
