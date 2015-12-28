package cn.hexiao.flog.test;

import org.hibernate.Session;

import cn.hexiao.flog.hibernate.HibernateUtil;

public class TestUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Session s = HibernateUtil.getSession();
		s.close();

	}

}
