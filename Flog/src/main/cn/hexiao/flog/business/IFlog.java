package cn.hexiao.flog.business;

import cn.hexiao.flog.FlogException;

public interface IFlog {
	public IUserManager getUserManager() throws FlogException;
	public IBookmarkManager getBookmarkManager() throws FlogException;
	public IPostManager getPostManager() throws FlogException;
	
    /**
     * Flush object states.
     */
    public void flush() throws FlogException;
    
    
    /**
     * Release all resources associated with Roller session.
     */
    public void release();

}
