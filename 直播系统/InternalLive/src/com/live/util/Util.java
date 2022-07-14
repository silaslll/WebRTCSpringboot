package com.live.util;

import java.io.InputStream;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class Util {

	private static SqlSessionFactory factory;

	static {
		try {
			// ��ȡ���������ļ�
			String configuration = "fonlg.xml";

			// �����ļ���������
			InputStream is = Util.class.getClassLoader().getResourceAsStream(
					configuration);

			// ����SqlSessionFactoryBuilder
			factory = new SqlSessionFactoryBuilder().build(is);
		} catch (Exception e) {
			System.out.println("�����ļ��������������!");
		}
	}

	/**
	 * ��ȡsession�Ự
	 * 
	 * @return
	 */
	public static SqlSession createSqlSession() {
		return factory.openSession(true);
	}

	/**
	 * ���sessionΪ�վ͹ر�session�Ự
	 * @param sqlsession
	 */
	public static void closeSqlSession(SqlSession sqlsession) {
		if (null != sqlsession) {
			sqlsession.close();
		}

	}

}
