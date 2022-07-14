package com.example.demo.servicempl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.bean.Student;
import com.example.demo.dao.UserServiceDao;
import com.example.demo.service.Userservice;

@Service
public class UserServiceImpl implements Userservice {
	@Autowired
	private UserServiceDao userdao;
	@Override
	public Student get_user(String username, String password) {
		Student stu = userdao.get_user(username, password);
		return stu;
	}
	
	@Override
	public int zhuce(String username, String password,String leixing) {
		// TODO Auto-generated method stub
		return userdao.zhuce(username,password,leixing);
	}
	
	@Override
	public Student get_user_username(String username) {
		// TODO Auto-generated method stub
		return userdao.get_user_username(username);
	}

	@Override
	public Map<String, Object> get_id(String username) {
		// TODO Auto-generated method stub
		return userdao.get_id(username);
	}


	@Override
	public Map<String, Object> get_username(int user_id) {
		// TODO Auto-generated method stub
		return userdao.get_username(user_id);
	}

	@Override
	public List<Map<String, Object>> get_user_list(String username) {
		// TODO Auto-generated method stub
		return userdao.get_user_list(username);
	}

	@Override
	public int del_user(int id) {
		// TODO Auto-generated method stub
		return userdao.del_user(id);
	}

	@Override
	public int save_user(int id, String username, String password, String leixing) {
		// TODO Auto-generated method stub
		return userdao.save_user(id,username,password,leixing);
	}

	@Override
	public List<Map<String, Object>> get_fangjian_list(String fangjianname,String username) {
		// TODO Auto-generated method stub
		return userdao.get_fangjian_list(fangjianname,username);
	}

	@Override
	public int del_fangjian(int id) {
		// TODO Auto-generated method stub
		return userdao.del_fangjian(id);
	}

	@Override
	public Map<String, Object> get_fangjian_name(String fangjianname) {
		// TODO Auto-generated method stub
		return userdao.get_fangjian_name(fangjianname);
	}

	@Override
	public int save_fangjian(String fangjianname, String title,String username) {
		// TODO Auto-generated method stub
		return userdao.save_fangjian( fangjianname, title,username);
	}

	@Override
	public int edit_fangjian(int id, String fangjianname, String title) {
		// TODO Auto-generated method stub
		return userdao.edit_fangjian(id,fangjianname,title);
	}

	@Override
	public Map<String, Object> get_role(String user_no) {
		// TODO Auto-generated method stub
		return userdao.get_role(user_no);
	}

	@Override
	public int up_shipin(String fileName, String username, String name) {
		// TODO Auto-generated method stub
		return userdao.up_shipin(fileName,username,name);
	}

	@Override
	public List<Map<String, Object>> get_shipin() {
		// TODO Auto-generated method stub
		return userdao.get_shipin();
	}
}
