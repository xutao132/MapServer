package com.hh.entity;

/**
 * 此类为用户信息
 * @author Administrator
 *
 */
public class User {
	private int Uid;//用户ID
	private String Uaccount;//用户账号
	private String Upassword;//用户密码
	
	public User(int uid, String uaccount, String upassword) {
		super();
		Uid = uid;
		Uaccount = uaccount;
		Upassword = upassword;
	}

	public User() {
		super();
	}

	public String toString() {
		return "User [Uid=" + Uid + ", Uaccount=" + Uaccount + ", Upassword="
				+ Upassword + "]";
	}

	public int getUid() {
		return Uid;
	}
	public void setUid(int uid) {
		Uid = uid;
	}
	public String getUaccount() {
		return Uaccount;
	}
	public void setUaccount(String uaccount) {
		Uaccount = uaccount;
	}
	public String getUpassword() {
		return Upassword;
	}
	public void setUpassword(String upassword) {
		Upassword = upassword;
	}
	
}
