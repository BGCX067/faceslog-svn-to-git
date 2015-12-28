package cn.hexiao.flog.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;

import cn.hexiao.flog.FlogException;
import cn.hexiao.flog.business.IPostManager;
import cn.hexiao.flog.business.hibernate.PostManager;

@Entity
@Table(name="flog_category")
public class Category extends PersistentObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5039708700311845253L;
	private Long id ;
	private String name;
	private String description;
	
	public Category() {}

	public Category(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}
	public Category(Long categoryId) {
		this.id = categoryId;
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
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public void setData(PersistentObject obj) {
		Category c = (Category) obj;
		this.description = c.getDescription();
		this.id = c.getId();
		this.name = c.getName();
		
	}
	
	public List<Post> retrievePosts() throws FlogException{
		//TODO : 如何得到 PostManager实例.
		//WeblogManager wmgr = RollerFactory.getRoller().getWeblogManager();
		IPostManager manager = new PostManager(null);
		return manager.getPosts(this);
	}
	

}
