package cn.hexiao.flog.business;

import java.util.List;

import cn.hexiao.flog.FlogException;
import cn.hexiao.flog.entity.Bookmark;

public interface IBookmarkManager {
	public void saveBookmark(Bookmark bookmark) throws FlogException;
	public void removeBookmark(Bookmark bookmark) throws FlogException;
	public Bookmark getBookmark(Long id) throws FlogException;
	public List<Bookmark> getBookmarks() throws FlogException;
	 /**
     * Release any resources held by manager.
     */
    public void release();
	public void flush();

}
