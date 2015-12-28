package cn.hexiao.flog.business.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import cn.hexiao.flog.FlogException;
import cn.hexiao.flog.business.IUserManager;
import cn.hexiao.flog.entity.User;

public class UserManager implements IUserManager {
	static Log log = LogFactory.getLog(UserManager.class);
	private HibernateUtil hibernateUtil = null;
	
	public UserManager(HibernateUtil util) {
		log.debug("Instantiating Hibernate User Manager");
		this.hibernateUtil = util;
	}

	public User getUser(Long id) throws FlogException {
		return this.hibernateUtil.load(User.class, id);
	}

	public void release() {
		
	}

	public void removeUser(User user) throws FlogException {
		this.hibernateUtil.remove(user);
	}

	public void saveUser(User user) throws FlogException {
		this.hibernateUtil.store(user);
	}

	public User getUser(String username, String password) throws FlogException {
		try {
			Session session = this.hibernateUtil.getSession();
			List<User> users = session.createQuery("from User c where c.name = ? and c.password = ?").setParameter(0, username).setParameter(1, password).list();
			if(users == null || users.size() == 0) {
				return null;
			}
			User user = users.get(0);
			
			return user; 
			
		} catch (HibernateException e) {
			throw new FlogException(e);
		}
	}

}
