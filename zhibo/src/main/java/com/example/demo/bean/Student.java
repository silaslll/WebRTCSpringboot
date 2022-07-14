package com.example.demo.bean;


public class Student{
	
	private int id;
	private String username;
	private String password;
	private String leixing;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLeixing() {
		return leixing;
	}
	public void setLeixing(String leixing) {
		this.leixing = leixing;
	}
	@Override
	public String toString() {
		return "Student [id=" + id + ", username=" + username + ", password=" + password + ", leixing=" + leixing + "]";
	}
	
}
