package com.live.biz;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.live.entit.User;

public interface Userbiz {

	/**
	 * 查找用户是否存在
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	User userlist(@Param("name") String name, @Param("password") String password);

	/**
	 * 查询所有用户
	 * 
	 * @param user
	 * @return
	 */
	List<User> userMap();

	/**
	 * 删除指定的用户
	 * 
	 * @param user
	 * @return
	 */
	int deleteuser(User user);

	/**
	 * 更改指定的用户
	 * 
	 * @param user
	 * @return
	 */
	int updateuser(User user);

	/**
	 * 新增用户
	 * 
	 * @param user
	 * @return
	 */
	int insertuser(User user);

}
