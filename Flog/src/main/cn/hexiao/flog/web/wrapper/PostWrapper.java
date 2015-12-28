package cn.hexiao.flog.web.wrapper;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.hexiao.flog.FlogException;
import cn.hexiao.flog.business.FlogFactory;
import cn.hexiao.flog.business.IPostManager;
import cn.hexiao.flog.entity.Comment;
import cn.hexiao.flog.entity.Post;
import cn.hexiao.flog.util.DateUtil;

public class PostWrapper implements Serializable {
	
	static Log log = LogFactory.getLog(PostWrapper.class);
	private static final long serialVersionUID = -2950783616822855178L;
	// wrapper properties for pages.
	private String date = null;
	private String time = null;
	private Post post = null;
	private List<Comment> comments = null;
	
	public PostWrapper(Post post) {
		this.post = post;
		init();
	}
	
	private void init() {
		if(post.getPubTime() != null) {
			date = DateUtil.formatIso8601Day(post.getPubTime());
			time = DateUtil.friendlyTime(post.getPubTime());
		}
		loadComments();
	}
	
	public void loadComments() {
		try {
			IPostManager postManager = FlogFactory.getFlog().getPostManager();
			comments = postManager.getComments(post, null, null, null, true, 0, -1);
			log.debug("comments num: "+comments.size());
		} catch (FlogException e) {
			log.error("Error: when load comments for post: "+post.getId());
		}
		
	}

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	
	public int getCommentsSize() {
		return this.comments.size();
	}
	
	

}
