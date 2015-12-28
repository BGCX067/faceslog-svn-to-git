package cn.hexiao.flog.web.admin;

import javax.faces.application.Application;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.hexiao.flog.FlogException;
import cn.hexiao.flog.business.FlogFactory;
import cn.hexiao.flog.business.IBookmarkManager;
import cn.hexiao.flog.entity.Bookmark;
import cn.hexiao.flog.web.HomePage;

public class BookmarkPage {
	static Log log = LogFactory.getLog(BookmarkPage.class);
	
	private String name;
	private String url;
	private String des;
	private String feedUrl;
	private String image;
	
	private UIData data;
	
	public String addBmAction() {
		try {
			Bookmark bm = new Bookmark(this.name,this.des,this.url,this.image,this.feedUrl);
			IBookmarkManager bmm = FlogFactory.getFlog().getBookmarkManager();
			bmm.saveBookmark(bm);
			bmm.flush();
			
			reloadBookmarks();
		} catch (FlogException e) {
			log.error("save bookmark error ",e);
		}
		return null;
	}
	
	public String editBmAction() {
		//TODO 实现该功能
		return null;
	}
	
	public String deleteBmAction() {
		try {
			Bookmark bm = (Bookmark)this.data.getRowData();
			IBookmarkManager bmm = FlogFactory.getFlog().getBookmarkManager();
			bmm.removeBookmark(bm);
			bmm.flush();
			reloadBookmarks();
		} catch (FlogException e) {
			log.error("delete bookmark error ",e);
		}
		return null;
	}

	private void reloadBookmarks() {
		//从新加载书签
		FacesContext context = FacesContext.getCurrentInstance();
		Application app = context.getApplication();
		HomePage homePage = (HomePage) app.evaluateExpressionGet(context, "#{homePage}", HomePage.class);
		homePage.reloadBookmarks();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getFeedUrl() {
		return feedUrl;
	}

	public void setFeedUrl(String feedUrl) {
		this.feedUrl = feedUrl;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public UIData getData() {
		return data;
	}

	public void setData(UIData data) {
		this.data = data;
	}
	

}
