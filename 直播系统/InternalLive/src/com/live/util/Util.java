package com.live.util;

import java.io.InputStream;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class Util {

	private static SqlSessionFactory factory;

	static {
		try {
			// 获取核心配置文件
			String configuration = "fonlg.xml";

			// 加载文件到输入流
			InputStream is = Util.class.getClassLoader().getResourceAsStream(
					configuration);

			// 创建SqlSessionFactoryBuilder
			factory = new SqlSessionFactoryBuilder().build(is);
		} catch (Exception e) {
			System.out.println("核心文件工具类出现问题!");
		}
	}

	/**
	 * 获取session会话
	 * 
	 * @return
	 */
	public static SqlSession createSqlSession() {
		return factory.openSession(true);
	}

	/**
	 * 如果session为空就关闭session会话
	 * @param sqlsession
	 */
	public static void closeSqlSession(SqlSession sqlsession) {
		if (null != sqlsession) {
			sqlsession.close();
		}

	}

}
