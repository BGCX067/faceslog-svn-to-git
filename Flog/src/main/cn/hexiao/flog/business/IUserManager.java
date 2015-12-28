package cn.hexiao.flog.business;

import cn.hexiao.flog.FlogException;
import cn.hexiao.flog.entity.User;

public interface IUserManager {
	
	public void saveUser(User user) throws FlogException;
	public void removeUser(User user) throws FlogException;
	public User getUser(Long id) throws FlogException;
	public User getUser(String username, String password) throws FlogException;
	 /**
     * Release any resources held by manager.
     */
    public void release();

}
