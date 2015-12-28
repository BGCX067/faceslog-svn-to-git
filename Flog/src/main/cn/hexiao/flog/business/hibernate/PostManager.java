package cn.hexiao.flog.business.hibernate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.hexiao.flog.FlogException;
import cn.hexiao.flog.business.IPostManager;
import cn.hexiao.flog.entity.Category;
import cn.hexiao.flog.entity.Comment;
import cn.hexiao.flog.entity.Post;
import cn.hexiao.flog.util.DateUtil;

public class PostManager implements IPostManager {
	static Log log = LogFactory.getLog(PostManager.class);
	
	private HibernateUtil hibernateUtil;
	
	public PostManager(HibernateUtil util) {
		log.debug("Instantiating Hibernate Post Manager");
		this.hibernateUtil = util;
	}

	@SuppressWarnings("unchecked")
	public List<Category> getCategories() throws FlogException {
		log.debug("start getting categories.");
		try {
			Session session = this.hibernateUtil.getSession();
			log.debug("get categories.");
			return session.createQuery("from Category c").list();
		} catch (HibernateException e) {
			throw new FlogException(e);
		}
	}

	public Category getCategory(Long id) throws FlogException {
		return this.hibernateUtil.load(Category.class, id);
	}

	public Comment getComment(Long id) throws FlogException {
		return this.hibernateUtil.load(Comment.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Comment> getComments(Post post, String searchString,
			Date startDate, Date endDate, boolean reverseChrono, int offset,
			int length) throws FlogException {
		Session session = this.hibernateUtil.getSession();
		Criteria criteria = session.createCriteria(Comment.class);
		
		if(post != null) {
			criteria.add(Restrictions.eq("post", post));
		}
		
		if(searchString != null) {
			criteria.add(Restrictions.like("content", searchString,MatchMode.ANYWHERE));
		}
		if (startDate != null) {
            criteria.add(Restrictions.ge("postTime", startDate));
        }
        
        if (endDate != null) {
            criteria.add(Restrictions.le("postTime", endDate));
        }
        if (length != -1) {
            criteria.setMaxResults(offset + length);
        }
        
        if (reverseChrono) {
            criteria.addOrder(Order.desc("postTime"));
        } else {
            criteria.addOrder(Order.asc("postTime"));
        }
        
        List<Comment> comments = criteria.list();
        
        if (offset==0 || comments.size() < offset) {
            return comments;
        }
        List<Comment> range = new ArrayList<Comment>();
        for (int i=offset; i<comments.size(); i++) {
            range.add(comments.get(i));
        }
        return range;
		
	}

	@SuppressWarnings("unchecked")
	public Date getLastPublishTime() throws FlogException {
		Session session = this.hibernateUtil.getSession();
		Criteria criteria = session.createCriteria(Post.class);
		criteria.add(Restrictions.le("pubTime", new Date()));
		criteria.addOrder(Order.desc("pubTime"));
        criteria.setMaxResults(1);
        
        List<Post> list = criteria.list();
        if (list.size() > 0) {
            return (list.get(0)).getPubTime();
        } else {
            return null;
        }
	}

	public Post getNextPost(Post current, String catName) throws FlogException {
		Post post = null;
		List<Post> postList = getNextPosts(current, catName, 1);
		if(postList != null && postList.size() > 0) {
			post = postList.get(0);
		}
		return post;
	}

	public List<Post> getNextPosts(Post post, String catName, int maxPosts)
			throws FlogException {
		return getNextPrevPosts(post, catName, maxPosts, true);
	}

	public Post getPost(Long id) throws FlogException {
		return this.hibernateUtil.load(Post.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Post> getPosts(Category cat) throws FlogException {
		Session session = this.hibernateUtil.getSession();
		
		Criteria criteria = session.createCriteria(Post.class);
		criteria.add(Restrictions.eq("category", cat));
		
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Post> getPosts(Date startDate, Date endDate, String catName,
			String sortby, int offset, int range) throws FlogException {
		log.debug("get posts using the query method");
		Category cat = null;
		if(catName != null && !catName.trim().equals("")) {
			cat = getCategoryByName(catName);
			if(cat == null) catName = null;
		}
		
		Session session = this.hibernateUtil.getSession();
		log.debug("after get session");
		ArrayList<Object> params = new ArrayList<Object>();
		StringBuffer queryString = new StringBuffer();
		if(startDate == null && endDate ==null && catName == null) {
			queryString.append("from Post p ");
		} else {
			queryString.append("from Post p where 1=1 ");
		}
		
		if (startDate != null) {
            queryString.append("and pubTime >= ? ");
            params.add(startDate);
        }
        
        if (endDate != null) {
            queryString.append("and pubTime <= ? ");
            params.add(endDate);                
        }
        
        if (cat != null) {
            queryString.append("and category.id = ? ");
            params.add(cat.getId());                
        }
        
        if (sortby != null && sortby.equals("updateTime")) {
            queryString.append("order by updateTime desc ");
        } else {
            queryString.append("order by pubTime desc ");
        }
        log.debug("before create query");
        Query query = session.createQuery(queryString.toString());
        log.debug("after create query");
        
     // set params
        for(int i = 0; i < params.size(); i++) {
          query.setParameter(i, params.get(i));
        }
                    
        if (offset != 0) {
            query.setFirstResult(offset);
        }
        if (range != -1) {
            query.setMaxResults(range);
        }
        log.debug("return the post");
        return query.list();
	}

	@SuppressWarnings("unchecked")
	public Map<Date, List<Post>> getPostsObjectMap(Date startDate, Date endDate,
			String catName, int offset, int range) throws FlogException {
		return getPostsMap(startDate, endDate, catName, offset, range, false);
	}
	@SuppressWarnings("unchecked")
	public Map getPostsMap(Date startDate, Date endDate,
			String catName, int offset, int range,boolean stringsOnly) throws FlogException {
		TreeMap map = new TreeMap(new ReverseComparator());
		
		List<Post> posts = getPosts(startDate, endDate, catName, null, offset, range);
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = DateUtil.get8charDateFormat();
		for (Post post : posts) {
			Date sDate = DateUtil.getNoonOfDay(post.getPubTime(), cal);
            if (stringsOnly) {
                if (map.get(sDate) == null)
                    map.put(sDate, formatter.format(sDate));
            } else {
                List dayEntries = (List) map.get(sDate);
                if (dayEntries == null) {
                    dayEntries = new ArrayList();
                    map.put(sDate, dayEntries);
                }
                dayEntries.add(post);
            }
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public List<Post> getPostsPinnedToMain(Integer max) throws FlogException {
		Session session = this.hibernateUtil.getSession();
		
		Criteria criteria = session.createCriteria(Post.class);
		criteria.add(Restrictions.eq("pinnedToMain", Boolean.TRUE));
		criteria.addOrder(Order.desc("pubTime"));

		if (max != null) {
            criteria.setMaxResults(max.intValue());
        }
        return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public Map<Date, List<String>> getPostsStringMap(Date startDate,
			Date endDate, String catName, int offset, int range)
			throws FlogException {
		return getPostsMap(startDate, endDate, catName, offset, range, true);
	}

	public Post getPreviousPost(Post current, String catName) throws FlogException {
		Post post = null;
		List<Post> postList = getPreviousPosts(current, catName, 1);
		if(postList != null && postList.size() > 0) {
			post = postList.get(0);
		}
		return post;
	}

	public List<Post> getPreviousPosts(Post post, String catName, int maxPosts)
			throws FlogException {
		return getNextPrevPosts(post, catName, maxPosts, false);
	}

	@SuppressWarnings("unchecked")
	public boolean isDuplicateCategoryName(Category category)
			throws FlogException {
		Session session = this.hibernateUtil.getSession();
		//TODO 是否正确?
		List<Category> cs = session.createQuery("from Category c where c.name = ?").setParameter(0, category.getName()).list();
		return cs.size() > 0 ? true:false;
	}

	public void release() {

	}

	public void removeCategory(Category category) throws FlogException {
		if(category.retrievePosts().size() > 0) {
			throw new FlogException("Cannot remove category with Posts");
		}
		this.hibernateUtil.remove(category);
		//TODO 是否提供默认的目录, 如果提供这里有可能需要更新目录
		// update website default cats if needed
	}

	public void removeComment(Comment comment) throws FlogException {
		this.hibernateUtil.remove(comment);
	}

	public void removePost(Post post) throws FlogException {
		//remove comments first.
		List<Comment> comments = getComments(post, null, null, null, false, 0, -1);
		for (Comment comment : comments) {
			this.hibernateUtil.remove(comment);
		}
		
		//remove post
		this.hibernateUtil.remove(post);

	}

	public void saveCategory(Category category) throws FlogException {
		if(isDuplicateCategoryName(category)) {
			throw new FlogException("Duplicate category name, cannot save category");
		}
		
		this.hibernateUtil.store(category);
	}

	public void saveComment(Comment comment) throws FlogException {
		this.hibernateUtil.store(comment);
	}

	public void savePost(Post post) throws FlogException {
		this.hibernateUtil.store(post);
	}
	
	@SuppressWarnings("unchecked")
	public List<Post> getNextPrevPosts(Post current,String catName,int maxPosts,boolean next)throws FlogException {
		Junction conjunction = Restrictions.conjunction();
		if(next) {
			conjunction.add(Restrictions.gt("pubTime", current.getPubTime()));
		} else {
			conjunction.add(Restrictions.lt("pubTime", current.getPubTime()));
		}
		
		if(catName != null && !catName.trim().equals("")) {
			Category cat = getCategoryByName(catName);
			if(cat != null) {
				conjunction.add(Restrictions.eq("category", cat));
			} else {
				throw new FlogException("Cannot find category: "+catName);
			}
		}
		
		try {
			Session session = this.hibernateUtil.getSession();
			Criteria criteria = session.createCriteria(Post.class);
			criteria.addOrder(next ? Order.asc("pubTime") : Order.desc("pubTime"));
			criteria.add(conjunction);
			criteria.setMaxResults(maxPosts);
			List<Post> posts = criteria.list();
			return posts;
		} catch (HibernateException e) {
			throw new FlogException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public Category getCategoryByName(String catName) throws FlogException{
		Category cat = null;
		Session session = this.hibernateUtil.getSession();
		List list = session.createQuery("from Category c where c.name = ?").setParameter(0, catName).list();
		if(list != null && list.size() > 0) {
			cat = (Category)list.get(0);
			log.debug("get the category by name:" + catName);
		}
		
		return cat;
	}

	public HibernateUtil getHibernateUtil() {
		return hibernateUtil;
	}

	public void flush() {
		try {
			this.hibernateUtil.flush();
		} catch (FlogException e) {
			log.error("save comment error.",e);
		}
		
	}

	public Long getPostsCount(Date startDate, Date endDate)
			throws FlogException {
		log.debug("get posts count using the query method");
		Session session = this.hibernateUtil.getSession();
		ArrayList<Object> params = new ArrayList<Object>();
		StringBuffer queryString = new StringBuffer();
		if(startDate == null && endDate ==null ) {
			queryString.append("select count(p) from Post p ");
		} else {
			queryString.append("select count(p) from Post p where 1=1 ");
		}
		
		if (startDate != null) {
            queryString.append("and pubTime >= ? ");
            params.add(startDate);
        }
        
        if (endDate != null) {
            queryString.append("and pubTime <= ? ");
            params.add(endDate);                
        }
        
        Query query = session.createQuery(queryString.toString());
        
        log.debug("after create query");
        
     // set params
        for(int i = 0; i < params.size(); i++) {
          query.setParameter(i, params.get(i));
        }
        return (Long)query.list().get(0);
	}

}
