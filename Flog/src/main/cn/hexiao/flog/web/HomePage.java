package cn.hexiao.flog.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.hexiao.flog.Constants;
import cn.hexiao.flog.FlogException;
import cn.hexiao.flog.business.FlogFactory;
import cn.hexiao.flog.business.IBookmarkManager;
import cn.hexiao.flog.business.IPostManager;
import cn.hexiao.flog.entity.Bookmark;
import cn.hexiao.flog.entity.Category;
import cn.hexiao.flog.entity.Comment;
import cn.hexiao.flog.entity.PersistentObject;
import cn.hexiao.flog.entity.Post;
import cn.hexiao.flog.web.wrapper.PostWrapper;

public class HomePage extends AbstractPager {
	static Log log = LogFactory.getLog(HomePage.class);
	
	//private int page = 0; // 分页,当前为第0页
	private String catName = null;
	private int length = Constants.PAGE_SIZE;
    // are there more posts? 用于分页
    private boolean more = false;
	
    private List<PostWrapper> postWrappers;
    
	private List<Post> items = null;
	private List<Category> categories = null;
	private List<Comment> latestComments = null;
	private List<Bookmark> bookmarks = null;
	
	private Map<Integer, List<Post>> pagePostsMap = null; //保存分页数据的post list
	
	public HomePage() {
		super("home/",0);
		init();
	}
	public HomePage(String baseUrl,int page) {
		super(baseUrl, page);
		init();
	}
	
	@SuppressWarnings("unchecked")
	public void init() {
		log.debug("init homePage .");
		pagePostsMap = new TreeMap<Integer, List<Post>>();
		this.items = (List<Post>) getItems();
		try {
			IPostManager postManager = FlogFactory.getFlog().getPostManager();
			
			this.categories = postManager.getCategories();
			this.latestComments = postManager.getComments(null, null, null, null, true, 0, 8);
			
			IBookmarkManager bookmarkManager = FlogFactory.getFlog().getBookmarkManager();
			this.bookmarks = bookmarkManager.getBookmarks();
		} catch (FlogException e) {
			log.error("Error initializing Flog :" ,e);
		}
		log.debug("homePage init done");
	}
	
	public void reloadLatestCat()	 {
		try {
			IPostManager postManager = FlogFactory.getFlog().getPostManager();
			
			this.categories = postManager.getCategories();
		} catch (FlogException e) {
			log.error("error when reload Categories.",e);
		}
	}
	
	public void reloadBookmarks()	 {
		try {
			IBookmarkManager bookmarkManager = FlogFactory.getFlog().getBookmarkManager();
			
			this.bookmarks = bookmarkManager.getBookmarks();
		} catch (FlogException e) {
			log.error("error when reload Bookmarks.",e);
		}
	}
	
	public void reloadLatestComments()	 {
		try {
			IPostManager postManager = FlogFactory.getFlog().getPostManager();
			
			this.latestComments = postManager.getComments(null, null, null, null, true, 0, 8);
		} catch (FlogException e) {
			log.error("error when reload latestComments.",e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void nextLinkActionListener(ActionEvent ae) {
		this.items = (List<Post>) getItems();
	}

	public List<? extends PersistentObject> getItems() {
		log.debug("get current page posts.");
		more = false; //TODO right?
		
		int offset = getPage() * length;
		
		List<Post> results = new ArrayList<Post>();
		try {
			IPostManager postManager = FlogFactory.getFlog().getPostManager();
			results = postManager.getPosts(null, null, this.catName, null, offset, length + 1); // length + 1 , 用来判断是否还有更多的文章.
		} catch (FlogException e) {
			log.error("Error when get posts.", e);
		}
		
		// check if there are more results for paging
		if(results.size() > length) {
			more = true;
			results.remove(results.size() -1);
		}
		if(postWrappers == null) {
			postWrappers = new ArrayList<PostWrapper>();
		} else {
			postWrappers.clear();
		}
		for (Post post : results) {
			postWrappers.add(new PostWrapper(post));
		}
		pagePostsMap.put(getPage(), results); // TODO 文章缓存功能, 尚未实现,  在这里 放置每页文章的缓存 是否合适? 
		
		return results;
	}
	
	
	// =================getter setter
    @Override
	public boolean hasMoreItems() {
        return more;
    }
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public boolean isMore() {
		return more;
	}
	public void setMore(boolean more) {
		this.more = more;
	}
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	public List<Comment> getLatestComments() {
		return latestComments;
	}
	public void setLatestComments(List<Comment> latestComments) {
		this.latestComments = latestComments;
	}
	public List<Bookmark> getBookmarks() {
		return bookmarks;
	}
	public void setBookmarks(List<Bookmark> bookmarks) {
		this.bookmarks = bookmarks;
	}
	public Map<Integer, List<Post>> getPagePostsMap() {
		return pagePostsMap;
	}
	public void setPagePostsMap(Map<Integer, List<Post>> pagePostsMap) {
		this.pagePostsMap = pagePostsMap;
	}
	
	public void setItems(List<Post> items) {
		this.items = items;
	}
	public List<Post> getPosts() {
		return this.items;
	}
	public List<PostWrapper> getPostWrappers() {
		return postWrappers;
	}
	public void setPostWrappers(List<PostWrapper> postWrappers) {
		this.postWrappers = postWrappers;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
    
    
   
    



}
