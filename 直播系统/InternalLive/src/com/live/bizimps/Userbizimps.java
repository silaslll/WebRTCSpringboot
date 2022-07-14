package com.live.bizimps;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.live.biz.Userbiz;
import com.live.entit.User;
import com.live.usermapperdao.UserMapper;
import com.live.util.Util;

public class Userbizimps implements Userbiz {

	/**
	 * 查找用户是否存在
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	@Override
	public User userlist(String name, String password) {
		// 获取session
		SqlSession session = Util.createSqlSession();
		User user = new User();
		// 将参数传入方法中
		user = session.getMapper(UserMapper.class).userlist(name, password);
		return user;
	}

	/**
	 * 查询所有用户
	 * 
	 * @param user
	 * @return
	 */
	@Override
	public List<User> userMap() {
		// 获取session
		SqlSession session = Util.createSqlSession();
		// 创建集合,储存所有用户
		List<User> list = new ArrayList<User>();
		list = session.getMapper(UserMapper.class).userMap();
		return list;
	}

	/**
	 * 删除指定的用户
	 * 
	 * @param user
	 * @return
	 */
	@Override
	public int deleteuser(User user) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 更改指定的用户
	 * 
	 * @param user
	 * @return
	 */
	@Override
	public int updateuser(User user) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 新增用户
	 * 
	 * @param user
	 * @return
	 */
	@Override
	public int insertuser(User user) {
		// 获取session
		SqlSession session = Util.createSqlSession();
		int a = session.getMapper(UserMapper.class).insertuser(user);
		return a;
	}

}
