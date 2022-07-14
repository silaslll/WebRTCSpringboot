package com.live.entit;

public class User {

	/**
	 * 用户id
	 */
	private int id;

	/**
	 * 用户名称
	 */
	private String name;

	/**
	 * 用户密码
	 */
	private String password;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

}
