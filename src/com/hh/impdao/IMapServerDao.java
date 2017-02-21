package com.hh.impdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.hh.dao.MapServerDao;
import com.hh.entity.User;
import com.hh.utils.JDBCUtils;

public class IMapServerDao implements MapServerDao {
	/**
	 * 注册到数据库(注册到数据库的话要看看该用户是否存在) 1.链接数据
	 */
	private String sql = "";

	public User registerUser(User user) throws Exception {
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			// 查询用户是否存在
			if (searchUser(user.getUaccount()) != null) {
				throw new Exception("用户已经存在");
			} else {
				connection = JDBCUtils.getConnection();
				sql = "insert into hh_user(userid,account,userpassword) values(?,?,?)";
				ps = connection.prepareStatement(sql);
			}
			int iResult = 0;
			if (user != null) {
				// 参数替换
				ps.setInt(1, user.getUid());
				ps.setString(2, user.getUaccount());
				ps.setString(3, user.getUpassword());
				iResult = ps.executeUpdate();
			}
			// 注册是否成功
			if (iResult > 0) {
				System.out.println("注册成功");
				user = searchUser(user.getUid());
				return user;
			} else {
				System.out.println("注册失败");
				throw new Exception("注册失败");
			}
		} catch (Exception e) {
			throw new Exception("数据库操作失败" + e);
		} finally {
			JDBCUtils.closeConnection(connection, ps, null);
		}

	}

	public User searchUser(String account) throws Exception {
		PreparedStatement ps = null;
		Connection connection = null;
		sql = "select userid,account,userpassword from hh_user where account = ?";
		User user = null;
		try {
			connection = JDBCUtils.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setString(1, account);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				user = new User();
				// 读取查询到的结果
				user.setUid(resultSet.getInt("userid"));
				user.setUaccount(resultSet.getString("account"));
				user.setUpassword(resultSet.getString("userpassword"));
				System.out.println(user.toString());
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			JDBCUtils.closeConnection(connection, ps, null);
		}
		System.out.println(user);
		return user;
	}

	public User searchUser(int userid) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public User login(String uaccount, String upassword) {
		PreparedStatement ps = null; // 创建PreparedStatement对象 (用来做准备查询的对象)
		Connection connection = null;// 创建connection对象
		sql = "select userid,account,userpassword from hh_user where account = ? and userpassword = ?";
		try {
			connection = JDBCUtils.getConnection();//连接数据库
			ps = connection.prepareStatement(sql);
			ps.setObject(1, uaccount);
			ps.setObject(2, upassword);
			ResultSet resultSet = ps.executeQuery();
			if(resultSet.next()){
				User user =  new User();
				 //读取查询到的结果
					user.setUid(resultSet.getInt("userid"));
					user.setUaccount(resultSet.getString("account"));
					user.setUpassword(resultSet.getString("userpassword"));
					//System.out.println(user.toString());
					return  user;
			}else {
				throw new Exception("用户名或密码错误");
			}
		} catch (Exception e) {
			System.out.println("数据库操作有误。。。。");
			e.printStackTrace();
		} finally {
			JDBCUtils.closeConnection(connection, ps, null);
		}
		return null;
		
	}

}
