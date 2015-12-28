package cn.hexiao.flog.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;

@Entity
@Table(name="flog_post")
public class Post extends PersistentObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9214915440770505642L;
	private Long id ;
	private String title;
	private Timestamp pubTime;
	private Timestamp updateTime;
	private Boolean pinnedToMain;
	private String summary;
	private String content;
	private Long hitCount = 0L;
	
	private Category category;
//	private List<Comment> comments;
	public Post() {}
	public Post(String title, Timestamp pubTime, Timestamp updateTime,
			Boolean pinnedToMain, String summary, String content,
			Long hitCount, Category category) {
		super();
		this.title = title;
		this.pubTime = pubTime;
		this.updateTime = updateTime;
		this.pinnedToMain = pinnedToMain;
		this.summary = summary;
		this.content = content;
		this.hitCount = hitCount;
		this.category = category;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@NotNull
	public Timestamp getPubTime() {
		return pubTime;
	}
	public void setPubTime(Timestamp pubTime) {
		this.pubTime = pubTime;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public Boolean getPinnedToMain() {
		return pinnedToMain;
	}
	public void setPinnedToMain(Boolean pinnedToMain) {
		this.pinnedToMain = pinnedToMain;
	}
	@Lob
	@NotNull
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	@Lob
	@NotNull
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getHitCount() {
		return hitCount;
	}
	public void setHitCount(Long hitCount) {
		this.hitCount = hitCount;
	}
	
	@ManyToOne(targetEntity=Category.class)
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	@Override
	public void setData(PersistentObject obj) {
		Post p = (Post) obj;
		this.category = p.getCategory();
		this.content = p.getContent();
		this.hitCount = p.getHitCount();
		this.id = p.getId();
		this.pinnedToMain = p.getPinnedToMain();
		this.pubTime = p.getPubTime();
		this.summary = p.getSummary();
		this.title = p.getTitle();
		this.updateTime = p.getUpdateTime();
		
	}
	
	

}
