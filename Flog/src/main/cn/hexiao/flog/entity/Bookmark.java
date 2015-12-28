package cn.hexiao.flog.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;

@Entity
@Table(name="flog_bookmark")
public class Bookmark extends PersistentObject {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5576799098786642262L;
	private Long id ;
    private String name;
    private String description;
    private String url;
    private String image;
    private String feedUrl; 
    
    public Bookmark() {}

	public Bookmark(String name, String description, String url, String image,
			String feedUrl) {
		super();
		this.name = name;
		this.description = description;
		this.url = url;
		this.image = image;
		this.feedUrl = feedUrl;
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
	@NotNull
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Column(nullable=true)
	public String getFeedUrl() {
		return feedUrl;
	}

	public void setFeedUrl(String feedUrl) {
		this.feedUrl = feedUrl;
	}

	@Override
	public void setData(PersistentObject obj) {
		Bookmark other = (Bookmark) obj;
		this.description = other.getDescription();
		this.feedUrl = other.getFeedUrl();
		this.id = other.getId();
		this.image = other.getImage();
		this.name = other.getName();
		this.url = other.getUrl();
	}
    

}
