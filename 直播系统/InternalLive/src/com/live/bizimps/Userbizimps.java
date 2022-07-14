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
	 * �����û��Ƿ����
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	@Override
	public User userlist(String name, String password) {
		// ��ȡsession
		SqlSession session = Util.createSqlSession();
		User user = new User();
		// ���������뷽����
		user = session.getMapper(UserMapper.class).userlist(name, password);
		return user;
	}

	/**
	 * ��ѯ�����û�
	 * 
	 * @param user
	 * @return
	 */
	@Override
	public List<User> userMap() {
		// ��ȡsession
		SqlSession session = Util.createSqlSession();
		// ��������,���������û�
		List<User> list = new ArrayList<User>();
		list = session.getMapper(UserMapper.class).userMap();
		return list;
	}

	/**
	 * ɾ��ָ�����û�
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
	 * ����ָ�����û�
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
	 * �����û�
	 * 
	 * @param user
	 * @return
	 */
	@Override
	public int insertuser(User user) {
		// ��ȡsession
		SqlSession session = Util.createSqlSession();
		int a = session.getMapper(UserMapper.class).insertuser(user);
		return a;
	}

}
