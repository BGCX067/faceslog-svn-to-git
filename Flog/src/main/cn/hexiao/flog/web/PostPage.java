package cn.hexiao.flog.web;

import java.sql.Timestamp;
import java.util.Random;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.hexiao.flog.FlogException;
import cn.hexiao.flog.business.FlogFactory;
import cn.hexiao.flog.business.IPostManager;
import cn.hexiao.flog.entity.Comment;
import cn.hexiao.flog.entity.Post;
import cn.hexiao.flog.web.wrapper.PostWrapper;

public class PostPage {
	static Log log = LogFactory.getLog(PostPage.class);
	
	private PostWrapper post = null;
	private PostWrapper next = null;
	private PostWrapper previous = null;
	
	//pub comment
	private String username; //发表评论的name
	private String content; //评论的内容
	//验证码
	private int numa;
	private int numb;
	private int sum;


	public PostPage() {
		log.debug("Post Page init...");
//		numa = new Random().nextInt(20);
//		numb = new Random().nextInt(30);
		
	}

	public String addComment() {
		FacesContext fc = FacesContext.getCurrentInstance();
		if(this.sum != this.numa + this.numb) {
			FacesMessage verifyError = new FacesMessage("验证问题回答不正确,请重新输入.");
			fc.addMessage("pub_comment:c_num", verifyError); //client_id not component_id
			fc.renderResponse();
			
			initRandom();
			return null;
		}
		try {
			IPostManager postManager = FlogFactory.getFlog().getPostManager();
			Comment comment = new Comment();
			comment.setContent(getContent());
			comment.setPostTime(new Timestamp(System.currentTimeMillis()));
			comment.setPost(post.getPost());
			comment.setUsernme(getUsername());
			postManager.saveComment(comment);
			postManager.flush(); //?
			
			//重新加载最新评论
			Application app = fc.getApplication();
			HomePage homePage = (HomePage) app.evaluateExpressionGet(fc, "#{homePage}", HomePage.class);
			homePage.reloadLatestComments();
			this.post.loadComments();
			initRandom();
			//清空输入的表单
			this.content = null;
			this.username = null;
			this.sum = 0;
		} catch (FlogException e) {
			log.error("Save comment error: post id" + post.getPost().getId(),e);
		}
		return null;
		
	}
	
	public PostWrapper getPost() {
		return post;
	}

	public void setPost(PostWrapper post) {
		this.post = post;
	}

	public PostWrapper getNext() {
		return next;
	}

	public void setNext(PostWrapper next) {
		this.next = next;
	}

	public PostWrapper getPrevious() {
		return previous;
	}

	public void setPrevious(PostWrapper previous) {
		this.previous = previous;
	}
	
	public void initRandom() {
		// 初始化验证码
		numa = new Random().nextInt(20);
		numb = new Random().nextInt(30);
	}
	
	public void loadPost(Long id) {
		initRandom();
		
		try {
			IPostManager postManager = FlogFactory.getFlog().getPostManager();
			Post p = postManager.getPost(id);
			
			//fixed, 清空以前的缓存数据
			if(p != null) 
				this.post = new PostWrapper(p);
			else
				this.post = null;
			
			Post n = postManager.getNextPost(p, null);
			if(n != null)
				this.next = new PostWrapper(n);
			else
				this.next = null;
			
			Post pre = postManager.getPreviousPost(p, null);
			if(pre != null)
				this.previous = new PostWrapper(pre);
			else
				this.previous = null;
		} catch (FlogException e) {
			log.error("Error: while reading post from db",e);
		}
	}
	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getNuma() {
		return numa;
	}

	public void setNuma(int numa) {
		this.numa = numa;
	}

	public int getNumb() {
		return numb;
	}

	public void setNumb(int numb) {
		this.numb = numb;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

}
