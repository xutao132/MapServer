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
			if (searchUser(user.getUid()) != null)
				throw new Exception("用户已经存在");
			int iResult = 0;
			connection = JDBCUtils.getConnection();
			sql = "insert into hh_user(userid,account,userpassword) values(?,?,?)";
			ps = connection.prepareStatement(sql);
			if (user != null) {
				// 参数替换
				ps.setInt(1, user.getUid());
				ps.setString(3, user.getUpassword());
				ps.setString(2, user.getUaccount());
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

	public User searchUser(int userid) throws Exception {
		PreparedStatement ps = null;
		Connection connection = null;
		if (userid < 100000 || userid > 2000000000)
			return null;
		sql = "select userid,account,userpassword from hh_user where userid = ?";
		User user = null;
		try {
			connection = JDBCUtils.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, userid);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				user = new User();
				// 读取查询到的结果
				user.setUid(resultSet.getInt("userid"));
				user.setUaccount(resultSet.getString("account"));
				user.setUpassword(resultSet.getString("userpassword"));
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			JDBCUtils.closeConnection(connection, ps, null);
		}
		return user;
	}

}
