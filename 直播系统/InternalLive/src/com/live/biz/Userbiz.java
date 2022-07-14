package com.live.biz;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.live.entit.User;

public interface Userbiz {

	/**
	 * �����û��Ƿ����
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	User userlist(@Param("name") String name, @Param("password") String password);

	/**
	 * ��ѯ�����û�
	 * 
	 * @param user
	 * @return
	 */
	List<User> userMap();

	/**
	 * ɾ��ָ�����û�
	 * 
	 * @param user
	 * @return
	 */
	int deleteuser(User user);

	/**
	 * ����ָ�����û�
	 * 
	 * @param user
	 * @return
	 */
	int updateuser(User user);

	/**
	 * �����û�
	 * 
	 * @param user
	 * @return
	 */
	int insertuser(User user);

}
