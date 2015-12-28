package cn.hexiao.flog.web.admin;

import java.util.List;

import javax.faces.component.UIData;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.hexiao.flog.Constants;
import cn.hexiao.flog.FlogException;
import cn.hexiao.flog.business.FlogFactory;
import cn.hexiao.flog.business.IPostManager;
import cn.hexiao.flog.entity.Post;

public class AdminHomePage {
	static Log log = LogFactory.getLog(AdminHomePage.class);
	
	private List<Post> latestPosts;
	private UIData data;
	
	public AdminHomePage() {
		loadLatestPosts();
	}

	public void loadLatestPosts() {
		try {
			IPostManager postManager = FlogFactory.getFlog().getPostManager();
			latestPosts = postManager.getPosts(null, null, null, null, 0, 8);
		} catch (FlogException e) {
			log.error("load post error ",e);
		}
	}
	
	public String editPostAction() {
		Post post = (Post)this.data.getRowData();
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.getExternalContext().getSessionMap().put(Constants.EDIT_POST, post);
		return "edit_post";
	}
	
	public String deletePostAction () {
		log.debug("delete post.........");
		Post post = (Post)this.data.getRowData();
		try {
			IPostManager postManager = FlogFactory.getFlog().getPostManager();
			postManager.removePost(post);
			postManager.flush(); // flush
			
			return "success";
		} catch (FlogException e) {
			log.error("delete post error ",e);
			return "failure";
		}
	}

	public List<Post> getLatestPosts() {
		return latestPosts;
	}

	public void setLatestPosts(List<Post> latestPosts) {
		this.latestPosts = latestPosts;
	}

	public UIData getData() {
		return data;
	}

	public void setData(UIData data) {
		this.data = data;
	}

}
