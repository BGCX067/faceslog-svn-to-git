package cn.hexiao.flog.web.admin;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.hexiao.flog.FlogException;
import cn.hexiao.flog.Website;
import cn.hexiao.flog.business.FlogFactory;
import cn.hexiao.flog.business.IPostManager;
import cn.hexiao.flog.entity.Category;
import cn.hexiao.flog.entity.Post;
import cn.hexiao.flog.web.HomePage;

//create new post
public class NewPage {
	static Log log = LogFactory.getLog(NewPage.class);
	
	private Long categoryId;
	private String title;
	private String summary;
	private String content;
	private boolean pinnedToMain;
	
	private List<Category> categories;
	private Map<String, Long> catItems;
	
	
	public NewPage() {
		loadCategories();
	}
	
	public void loadCategories() {
		try {
			IPostManager postManager = FlogFactory.getFlog().getPostManager();
			this.categories = postManager.getCategories();
			if(this.categories != null) {
				catItems = new HashMap<String, Long>();
				for (Category cat : categories) {
					catItems.put(cat.getName(), cat.getId());
				}
			}
		} catch (FlogException e) {
			log.error("load categories error: ",e);
		}
	}
	
	public String saveNewPost() {
		Post post = new Post();
		post.setCategory(new Category(this.categoryId));
		post.setContent(getContent());
		post.setHitCount(1L);
		post.setPinnedToMain(isPinnedToMain());
		post.setPubTime(new Timestamp(System.currentTimeMillis()));
		post.setSummary(getSummary());
		post.setTitle(getTitle());

		try {
			IPostManager postManager = FlogFactory.getFlog().getPostManager();
			postManager.savePost(post);
			postManager.flush(); // TODO 必须调用flush吗?
			
			//更新缓存
			FacesContext context = FacesContext.getCurrentInstance();
			Application app = context.getApplication();
			HomePage homePage = (HomePage) app.evaluateExpressionGet(context, "#{homePage}", HomePage.class);
			homePage.getItems();
			
			//TODO 更新站点更新日期.
			Website site = Website.getInstance();
			site.setLatestUpdateTime(System.currentTimeMillis());
			
			return "post_success";
		} catch (FlogException e) {
			log.error("save post error ",e);
			return "post_failure";
		}
	}
	

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isPinnedToMain() {
		return pinnedToMain;
	}

	public void setPinnedToMain(boolean pinnedToMain) {
		this.pinnedToMain = pinnedToMain;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}


	public Map<String, Long> getCatItems() {
		return catItems;
	}
}
