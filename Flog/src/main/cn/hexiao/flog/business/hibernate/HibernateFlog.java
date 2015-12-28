package cn.hexiao.flog.business.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.hexiao.flog.FlogException;
import cn.hexiao.flog.business.Flog;
import cn.hexiao.flog.business.IBookmarkManager;
import cn.hexiao.flog.business.IFlog;
import cn.hexiao.flog.business.IPostManager;
import cn.hexiao.flog.business.IUserManager;

/**
 * A Hibernate specific implementation of the Flog business layer.
 */
public class HibernateFlog extends Flog {
	static Log log = LogFactory.getLog(HibernateFlog.class);
	
	// our singleton instance
	private static HibernateFlog me = null;
	
	// a persistence utility class
	private HibernateUtil hibernateUitl = null;
	
	private IBookmarkManager bookmarkManager = null;
	private IUserManager userManager = null;
	private IPostManager postManager = null;
	
	protected HibernateFlog() {
		this.hibernateUitl = new HibernateUtil();
	}
	/**
     * Instantiates and returns an instance of HibernateFlog.
     */
	public static IFlog instantiate() throws FlogException {
		if(me == null) {
			log.debug("Instantiating HibernateFlog.");
			me = new HibernateFlog();
		}
		return me;
	}
	
	public void flush() throws FlogException {
		this.hibernateUitl.flush();
	}

	public IBookmarkManager getBookmarkManager() throws FlogException {
		if(this.bookmarkManager == null) {
			this.bookmarkManager = new BookmarkManager(hibernateUitl);
		}
		return this.bookmarkManager;
	}

	public IPostManager getPostManager() throws FlogException {
		if(this.postManager == null) {
			this.postManager = new PostManager(hibernateUitl);
		}
		return this.postManager;
	}

	public IUserManager getUserManager() throws FlogException {
		if(this.userManager == null) {
			this.userManager = new UserManager(hibernateUitl);
		}
		return this.userManager;
	}

	@Override
	public void release() {
		if(this.bookmarkManager != null) this.bookmarkManager.release();
		if(this.postManager != null) this.postManager.release();
		if(this.userManager != null) this.userManager.release();
		
		this.hibernateUitl.release();
		
		super.release();

	}

}
