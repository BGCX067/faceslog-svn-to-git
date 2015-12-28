package cn.hexiao.flog.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;

import cn.hexiao.flog.entity.Post;

@Entity
@Table(name="flog_comment")
public class Comment extends PersistentObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4361549662196133753L;
	private Long id;
	private String usernme; //TODO  spell wrong? fix it;
	private String content;
	private Timestamp postTime;
	
	private Post post;
	
	public Comment() {
		
	}

	public Comment(String usernme, String content, Timestamp postTime, Post post) {
		super();
		this.usernme = usernme;
		this.content = content;
		this.postTime = postTime;
		this.post = post;
	}


	@Override
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
	@NotNull
	public String getUsernme() {
		return usernme;
	}

	public void setUsernme(String usernme) {
		this.usernme = usernme;
	}
	@NotNull
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
 
	@ManyToOne(targetEntity=Post.class)
	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}
	@NotNull
	public Timestamp getPostTime() {
		return postTime;
	}

	public void setPostTime(Timestamp postTime) {
		this.postTime = postTime;
	}

	@Override
	public void setData(PersistentObject obj) {
		Comment c = (Comment) obj;
		this.content = c.getContent();
		this.id = c.getId();
		this.post = c.getPost();
		this.postTime = c.getPostTime();
		this.usernme = c.getUsernme();
		
	}
	
	

}
