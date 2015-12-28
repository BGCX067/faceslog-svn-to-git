package cn.hexiao.flog.business;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.hexiao.flog.FlogException;
import cn.hexiao.flog.entity.Category;
import cn.hexiao.flog.entity.Comment;
import cn.hexiao.flog.entity.Post;

public interface IPostManager {
	public Comment getComment(Long id) throws FlogException;
	/**
	 * Generic comments query method.
	 * @param post      Post or null to include all comments
	 * @param searchString query string
     * @param startDate  Start date or null for no restriction
     * @param endDate    End date or null for no restriction
     * @param reverseChrono True for results in reverse chrono order
     * @param offset     Offset into results for paging
     * @param length     Max comments to return (or -1 for no limit)
	 * @return
	 * @throws FlogException
	 */
	public List<Comment> getComments(Post post,String searchString,Date startDate,Date endDate,boolean reverseChrono,int offset,int length) throws FlogException;
	public void removeComment(Comment comment) throws FlogException;
	public void saveComment(Comment comment) throws FlogException;
	/**
     * Get posts next after current post.
     * @param post Current post.
     * @param catName Only return entries in this category (if not null).
     * @param maxPosts Maximum number of Posts to return.
	 * @return list of posts
	 * @throws FlogException
	 */
	public List<Post> getNextPosts(Post post,String catName,int maxPosts) throws FlogException;
	/**
	 *Get the Post following, chronologically, the current post.
     * Restrict by the Category, if named.
     * @param post The "current" post
     * @param catName The value of the requested Category Name
	 * @return next post
	 * @throws FlogException
	 */
	public Post getNextPost(Post post,String catName) throws FlogException;
	/**
     * Get posts previous to current post.
     * @param post Current post.
     * @param catName Only return entries in this category (if not null).
     * @param maxPosts Maximum number of Posts to return.
	 * @return list of posts
	 * @throws FlogException
	 */
	public List<Post> getPreviousPosts(Post post,String catName,int maxPosts) throws FlogException;
	/**
	 *Get the Post prior to, chronologically, the current post.
     * Restrict by the Category, if named.
     * @param post The "current" post
     * @param catName The value of the requested Category Name
	 * @return next post
	 * @throws FlogException
	 */	
	public Post getPreviousPost(Post post,String catName) throws FlogException;
	public List<Post> getPosts(Category cat) throws FlogException;
	/**
	 *  Get posts by offset/length as list in reverse chronological order.
     * The range offset and list arguments enable paging through query results.
     * @param startDate  Start date or null for no start date.
     * @param endDate    End date or null for no end date.
     * @param catName    Category name or null for all categories.
     * @param sortBy     Sort by either 'pubTime' or 'updateTime' (null for pubTime)
     * @param offset     Offset into results for paging
     * @param range     Max comments to return (or -1 for no limit)
	 * @return
	 * @throws FlogException
	 */
	public List<Post> getPosts(Date startDate,Date endDate,String catName,String sortBy,int offset,int range) throws FlogException;
	public Long getPostsCount(Date startDate,Date endDate) throws FlogException;
	public Post getPost(Long id) throws FlogException;
	public List<Post> getPostsPinnedToMain(Integer max) throws FlogException;
	/**
     * Get Posts grouped by day. This method returns a Map that
     * contains Lists, each List contains Post objects, and the
     * Lists are keyed by Date objects.
     * @param startDate  Start date or null for no start date.
     * @param endDate    End date or null for no end date.
     * @param catName    Category path or null for all categories.
     * @param offset     Offset into results for paging
     * @param range     Max comments to return (or -1 for no limit)
	 * @return
	 * @throws FlogException
	 */
	public Map<Date, List<Post>> getPostsObjectMap(Date startDate,Date endDate,String catName,int offset,int range) throws FlogException;
	/**
     * Get Post date strings grouped by day. This method returns a Map
     * that contains Lists, each List contains YYYYMMDD date strings objects,
     * and the Lists are keyed by Date objects.
     * @param startDate  Start date or null for no start date.
     * @param endDate    End date or null for no end date.
     * @param catName    Category path or null for all categories.
     * @param offset     Offset into results for paging
     * @param range     Max comments to return (or -1 for no limit)
	 * @return
	 * @throws FlogException
	 */
	public Map<Date, List<String>> getPostsStringMap(Date startDate,Date endDate,String catName,int offset,int range) throws FlogException;
	public void removePost(Post post) throws FlogException;
	public void savePost(Post post) throws FlogException;
	
	public List<Category> getCategories() throws FlogException;
	public Category getCategory(Long id) throws FlogException;
	public boolean isDuplicateCategoryName(Category category)throws FlogException;
	public void removeCategory(Category category) throws FlogException;
	public void saveCategory(Category category) throws FlogException;
	public Category getCategoryByName(String catName) throws FlogException;
	
	public Date getLastPublishTime() throws FlogException;

	public void release();
	public void flush();
}
