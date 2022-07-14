package com.example.demo.service;


import java.util.List;
import java.util.Map;

import com.example.demo.bean.Student;

public interface Userservice {
	public	Student get_user(String username, String password);

	public int zhuce(String username, String password, String leixing);

	public Student get_user_username(String username);

	public Map<String, Object> get_id(String username);

	public Map<String, Object> get_username(int parseInt);

	public List<Map<String, Object>> get_user_list(String username);

	public int del_user(int id);

	public int save_user(int id, String username, String password, String leixing);

	public List<Map<String, Object>> get_fangjian_list(String fangjianname, String username);

	public int del_fangjian(int id);

	public Map<String, Object> get_fangjian_name(String fangjianname);

	public int save_fangjian(String fangjianname, String title,String username);

	public int edit_fangjian(int id, String fangjianname, String title);

	public Map<String, Object> get_role(String user_no);

	public int up_shipin(String fileName, String username, String name);

	public List<Map<String, Object>> get_shipin();
}
