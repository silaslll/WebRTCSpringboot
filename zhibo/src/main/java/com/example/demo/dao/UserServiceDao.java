package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.bean.Student;

@Mapper
public interface UserServiceDao {
		
		@Select("select * from user where username = #{username} and password = #{password}")
		@Results(
				value= {
						@Result(column="id",property="id"),
						@Result(column="username",property="username"),
						@Result(column="password",property="password"),
						@Result(column="leixing",property="leixing")
				}
				)
		public Student get_user(String username,String password);

		@Insert("insert into user (username,password,leixing) values(#{username},#{password},#{leixing})")
		public int zhuce(String username, String password, String leixing);

		
		@Select("select * from user where username = #{username}")
		@Results(
				value= {
						@Result(column="id",property="id"),
						@Result(column="username",property="username"),
						@Result(column="password",property="password")
				}
				)
		public Student get_user_username(String username);

		
		@Select("<script>"+
			    "select * from user where 1=1 <if test='username!=null'> and username like CONCAT('%',#{username},'%')</if></script>")
		public Map<String, Object> get_id(String username);
		
		@Select("select * from user where id = #{user_id}")
		public Map<String, Object> get_username(int user_id);
		
		@Select("<script>"+
			    "select * from user where 1=1 <if test='_parameter!=null'>and username like CONCAT('%',#{username},'%')</if></script>")
		public List<Map<String, Object>> get_user_list(String username);

		@Delete("delete from user where id = #{id}")
		public int del_user(int id);
		
		@Update("update user set username = #{username},password = #{password} ,leixing = #{leixing} where id = #{id}")
		public int save_user(int id, String username, String password, String leixing);
		
		@Select("<script>"+
			    "select * from fangjian where 1=1 <if test='fangjianname!=null'>and fangjianname like CONCAT('%',#{fangjianname},'%')</if><if test='username!=\"admin\"'>and username like CONCAT('%',#{username},'%')</if></script>")
		public List<Map<String, Object>> get_fangjian_list(String fangjianname, String username);
		
		@Delete("delete from fangjian where id = #{id}")
		public int del_fangjian(int id);
		
		@Select("select * from fangjian where fangjianname = #{fangjianname}")
		public Map<String, Object> get_fangjian_name(String fangjianname);
		
		@Insert("insert into fangjian (fangjianname,title,username) values(#{fangjianname},#{title},#{username})")	
		public int save_fangjian(String fangjianname, String title,String username);
		
		@Update("update fangjian set fangjianname = #{fangjianname},title = #{title} where id = #{id}")	
		public int edit_fangjian(int id, String fangjianname, String title);
		
		@Select("select * from user where username = #{user_no}")
		public Map<String, Object> get_role(String user_no);
		
		@Insert("insert into shipin (shipin_url,username,name) values(#{fileName},#{username},#{name})")
		public int up_shipin(String fileName, String username, String name);
		
		@Select("select * from shipin")
		public List<Map<String, Object>> get_shipin();
		
}
