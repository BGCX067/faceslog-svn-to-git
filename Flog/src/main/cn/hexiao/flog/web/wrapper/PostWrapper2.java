package cn.hexiao.flog.web.wrapper;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.hexiao.flog.FlogException;
import cn.hexiao.flog.business.FlogFactory;
import cn.hexiao.flog.business.IPostManager;
import cn.hexiao.flog.entity.Category;
import cn.hexiao.flog.entity.Comment;
import cn.hexiao.flog.entity.Post;
import cn.hexiao.flog.util.DateUtil;

public class PostWrapper2 extends Post {
	static Log log = LogFactory.getLog(PostWrapper2.class);
	private static final long serialVersionUID = 2559747881516087341L;
	// wrapper properties for pages.
	private String date;
	private String time;
	private List<Comment> comments;
	
	public PostWrapper2() {}
	public PostWrapper2(Post post) {
		super(post.getTitle(), post.getPubTime(), post.getUpdateTime(), post.getPinnedToMain(), post.getSummary(), post.getContent(), post.getHitCount(), post.getCategory());
		this.setId(post.getId());
	}
	public PostWrapper2(String title, Timestamp pubTime, Timestamp updateTime,
			Boolean pinnedToMain, String summary, String content,
			Long hitCount, Category category) {
		super(title, pubTime, updateTime, pinnedToMain, summary, content, hitCount, category);
	}
	public String getDate() {
		if(this.getPubTime() != null) {
			date = DateUtil.formatIso8601Day(this.getPubTime());
		}
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		if(this.getPubTime() != null) {
			time = DateUtil.friendlyTime(getPubTime());
		}
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public List<Comment> getComments() {
		try {
			IPostManager postManager = FlogFactory.getFlog().getPostManager();
			comments = postManager.getComments(this, null, null, null, false, 0, -1);
		} catch (FlogException e) {
			log.error("Error: when load comments for post: "+this.getId());
		}
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	
	
	

}
