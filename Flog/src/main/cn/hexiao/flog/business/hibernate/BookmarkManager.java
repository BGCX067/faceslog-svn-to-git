package cn.hexiao.flog.business.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import cn.hexiao.flog.FlogException;
import cn.hexiao.flog.business.IBookmarkManager;
import cn.hexiao.flog.entity.Bookmark;

public class BookmarkManager implements IBookmarkManager {
	static Log log = LogFactory.getLog(BookmarkManager.class);
	private HibernateUtil hibernateUtil = null;
	
	public BookmarkManager(HibernateUtil util) {
		log.debug("Instantiating Hibernate Bookmark Manager");
		this.hibernateUtil = util;
	}
 
	public Bookmark getBookmark(Long id) throws FlogException {
		return this.hibernateUtil.load(Bookmark.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Bookmark> getBookmarks() throws FlogException {
		Session session = hibernateUtil.getSession();
		List<Bookmark> bookmarks = session.createQuery("from Bookmark e").list();
		return bookmarks;
	}

	public void release() {
	}

	public void removeBookmark(Bookmark bookmark) throws FlogException {
		this.hibernateUtil.remove(bookmark);
	}

	public void saveBookmark(Bookmark bookmark) throws FlogException {
		this.hibernateUtil.store(bookmark);
	}
	
	public void flush() {
		try {
			this.hibernateUtil.flush();
		} catch (FlogException e) {
			log.error("save comment error.",e);
		}
		
	}

}
