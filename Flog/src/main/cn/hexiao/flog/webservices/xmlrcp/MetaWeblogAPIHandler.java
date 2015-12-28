package cn.hexiao.flog.webservices.xmlrcp;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.XmlRpcException;

import cn.hexiao.flog.Website;
import cn.hexiao.flog.business.Flog;
import cn.hexiao.flog.business.FlogFactory;
import cn.hexiao.flog.business.IPostManager;
import cn.hexiao.flog.entity.Category;
import cn.hexiao.flog.entity.Post;
import cn.hexiao.flog.util.Utilities;

public class MetaWeblogAPIHandler extends BloggerAPIHandler {

	private static final long serialVersionUID = 2646261901982600745L;
	private static Log mLogger = LogFactory.getLog(MetaWeblogAPIHandler.class);
	
	public MetaWeblogAPIHandler() {
        super();
    }
    
    
    /**
     * Authenticates a user and returns the categories available in the website
     *
     * @param blogid Dummy Value for Roller
     * @param userid Login for a MetaWeblog user who has permission to post to the blog
     * @param password Password for said username
     * @throws Exception
     * @return
     */
    public Object getCategories(String blogid, String userid, String password)
    throws Exception {
        
        mLogger.debug("getCategories() Called =====[ SUPPORTED ]=====");
        mLogger.debug("     BlogId: " + blogid);
        mLogger.debug("     UserId: " + userid);
        
        try {
            Hashtable  result = new Hashtable ();
            IPostManager postManager = FlogFactory.getFlog().getPostManager();
            
            List<Category> cats = postManager.getCategories();
            for (Iterator<Category> wbcItr = cats.iterator(); wbcItr.hasNext();) {
            	Category category =   wbcItr.next();
                result.put(category.getName(),
                		 createCategoryStruct(category, userid));
            }
            return result;
        } catch (Exception e) {
            String msg = "ERROR in MetaWeblogAPIHandler.getCategories";
            mLogger.error(msg,e);
            throw new XmlRpcException(UNKNOWN_EXCEPTION, msg);
        }
    }
    
    
    /**
     * Edits a given post. Optionally, will publish the blog after making the edit
     *
     * @param postid Unique identifier of the post to be changed
     * @param userid Login for a MetaWeblog user who has permission to post to the blog
     * @param password Password for said username
     * @param struct Contents of the post
     * @param publish If true, the blog will be published immediately after the post is made
     * @throws org.apache.xmlrpc.XmlRpcException
     * @return
     */
    public boolean editPost(String postid, String userid, String password,
            Hashtable struct, int publish) throws Exception {
        
        return editPost(postid, userid, password, struct, publish > 0);
    }
    
    
    public boolean editPost(String postid, String userid, String password,
            Hashtable struct, boolean publish) throws Exception {
        
        mLogger.debug("editPost() Called ========[ SUPPORTED ]=====");
        mLogger.debug("     PostId: " + postid);
        mLogger.debug("     UserId: " + userid);
        mLogger.debug("    Publish: " + publish);
        
        IPostManager postManager = FlogFactory.getFlog().getPostManager();
        Post entry = postManager.getPost(Long.valueOf(postid));
        
        if(!validate(userid,password)) {
        	return false;
        }
        
        Hashtable postcontent = struct;
        String description = (String)postcontent.get("description");
        String title = (String)postcontent.get("title");
        if (title == null) title = "";
        
        Date dateCreated = (Date)postcontent.get("dateCreated");
        if (dateCreated == null) dateCreated = (Date)postcontent.get("pubDate");
        
        String cat = null;
        if ( postcontent.get("categories") != null ) {
            Vector cats = (Vector)postcontent.get("categories");
            cat = (String)cats.elementAt(0);
        }
        mLogger.debug("      Title: " + title);
        mLogger.debug("   Category: " + cat);
        
        try {
            
            Timestamp current =
                    new Timestamp(System.currentTimeMillis());
            
            if ( !title.equals("") ) entry.setTitle(title);
            entry.setContent(description);
            entry.setUpdateTime(current);
//            if (Boolean.valueOf(publish).booleanValue()) {
//                entry.setStatus(WeblogEntryData.PUBLISHED);
//            } else {
//                entry.setStatus(WeblogEntryData.DRAFT);
//            }
            if (dateCreated != null) {
                entry.setPubTime(new Timestamp(dateCreated.getTime()));
            }
            
            if ( cat != null ) {
                // Use first category specified by request
                Category cd = postManager.getCategoryByName(cat);
                entry.setCategory(cd);
            }
            
            // save the entry
            postManager.savePost(entry);
            postManager.flush();
            
            // notify cache
            flushPage();
            
            // TODO: Roller timestamps need better than 1 second accuracy
            // Until then, we can't allow more than one post per second
            Thread.sleep(1000);
            
            return true;
        } catch (Exception e) {
            String msg = "ERROR in MetaWeblogAPIHandler.editPost";
            mLogger.error(msg,e);
            throw new XmlRpcException(UNKNOWN_EXCEPTION, msg);
        }
    }
    
    
    /**
     * Makes a new post to a designated blog. Optionally, will publish the blog after making the post
     *
     * @param blogid Unique identifier of the blog the post will be added to
     * @param userid Login for a MetaWeblog user who has permission to post to the blog
     * @param password Password for said username
     * @param struct Contents of the post
     * @param publish If true, the blog will be published immediately after the post is made
     * @throws org.apache.xmlrpc.XmlRpcException
     * @return
     */
    public String newPost(String blogid, String userid, String password,
            Hashtable struct, int publish) throws Exception {
        
        return newPost(blogid, userid, password, struct, publish > 0);
    }
    
    
    public String newPost(String blogid, String userid, String password,
            Hashtable struct, boolean publish) throws Exception {
        
        mLogger.debug("newPost() Called ===========[ SUPPORTED ]=====");
        mLogger.debug("     BlogId: " + blogid);
        mLogger.debug("     UserId: " + userid);
        mLogger.debug("    Publish: " + publish);
        
        if(!validate(userid, password)) {
        	return null;
        }
        	
        
        Hashtable postcontent = struct;
        String description = (String)postcontent.get("description");
        String title = (String)postcontent.get("title");
        if (StringUtils.isEmpty(title) && StringUtils.isEmpty(description)) {
            throw new XmlRpcException(
                    BLOGGERAPI_INCOMPLETE_POST, "Must specify title or description");
        }
        if (StringUtils.isEmpty(title)) {
            title = Utilities.truncateNicely(description, 15, 15, "...");
        }
        
        Date dateCreated = (Date)postcontent.get("dateCreated");
        if (dateCreated == null) dateCreated = (Date)postcontent.get("pubDate");
        if (dateCreated == null) dateCreated = new Date();
        mLogger.debug("      Title: " + title);
        
        try {
            IPostManager postManager = FlogFactory.getFlog().getPostManager();
            
            Timestamp current =
                    new Timestamp(System.currentTimeMillis());
            
            Post entry = new Post();
            entry.setTitle(title);
            entry.setContent(description);
            entry.setSummary(Utilities.truncateNicely(description, 45, 45, "..."));
            entry.setPubTime(new Timestamp(dateCreated.getTime()));
            entry.setUpdateTime(current);
            entry.setHitCount(1L);
//            if (Boolean.valueOf(publish).booleanValue()) {
//                entry.setStatus(WeblogEntryData.PUBLISHED);
//            } else {
//                entry.setStatus(WeblogEntryData.DRAFT);
//            }
            
            // MetaWeblog supports multiple cats, Roller supports one/entry
            // so here we take accept the first category that exists
            Category category = null;
            if ( postcontent.get("categories") != null ) {
                Vector cats = (Vector)postcontent.get("categories");
                if (cats != null && cats.size() > 0) {
                    for (int i=0; i<cats.size(); i++) {
                        String cat = (String)cats.get(i);
                        category = postManager.getCategoryByName(cat);
                        if (category != null) {
                            entry.setCategory(category);
                            break;
                        }
                    }
                }
            }
            if (category == null) {
                //TODO or we fall back to the default Blogger API category
                entry.setCategory(postManager.getCategoryByName("java"));
            }
            
            // save the entry
            postManager.savePost(entry);
            postManager.flush();
            
            // notify cache
            flushPage();
            
            // TODO: Roller timestamps need better than 1 second accuracy
            // Until then, we can't allow more than one post per second
            Thread.sleep(1000);
            
            return entry.getId().toString();
        } catch (Exception e) {
            String msg = "ERROR in MetaWeblogAPIHandler.newPost";
            mLogger.error(msg,e);
            throw new XmlRpcException(UNKNOWN_EXCEPTION, msg);
        }
    }
    
    
    /**
     *
     * @param postid
     * @param userid
     * @param password
     * @return
     * @throws Exception
     */
    public Object getPost(String postid, String userid, String password)
    throws Exception {
        
        mLogger.debug("getPost() Called =========[ SUPPORTED ]=====");
        mLogger.debug("     PostId: " + postid);
        mLogger.debug("     UserId: " + userid);
        
        IPostManager postManager = FlogFactory.getFlog().getPostManager();
        Post entry = postManager.getPost(Long.valueOf(postid));
        
        if(!validate(userid,password)) {
        	return null;
        }
        
        try {
            return createPostStruct(entry, userid);
        } catch (Exception e) {
            String msg = "ERROR in MetaWeblogAPIHandler.getPost";
            mLogger.error(msg,e);
            throw new XmlRpcException(UNKNOWN_EXCEPTION, msg);
        }
    }
    
    
    /**
     * Allows user to post a binary object, a file, to Roller. If the file is
     * allowed by the RollerConfig file-upload settings, then the file will be
     * placed in the user's upload diretory.
     */
    public Object newMediaObject(String blogid, String userid, String password,
            Hashtable struct) throws Exception {
        
        mLogger.debug("newMediaObject() Called =[ SUPPORTED ]=====");
        mLogger.debug("     BlogId: " + blogid);
        mLogger.debug("     UserId: " + userid);
        mLogger.debug("   Password: *********");
        
        throw new UnsupportedOperationException("not supported yet");
    }
    
    
    /**
     * Get a list of recent posts for a category
     *
     * @param blogid Unique identifier of the blog the post will be added to
     * @param userid Login for a Blogger user who has permission to post to the blog
     * @param password Password for said username
     * @param numposts Number of Posts to Retrieve
     * @throws XmlRpcException
     * @return
     */
    public Object getRecentPosts(String blogid, String userid, String password,
            int numposts) throws Exception {
        
        mLogger.debug("getRecentPosts() Called ===========[ SUPPORTED ]=====");
        mLogger.debug("     BlogId: " + blogid);
        mLogger.debug("     UserId: " + userid);
        mLogger.debug("     Number: " + numposts);
        
        if(!validate(userid,password)) {
        	return null;
        }
        
        try {
            Vector results = new Vector();
            
            IPostManager postManager = FlogFactory.getFlog().getPostManager();
            
             
                List<Post> entries = postManager.getPosts(
                        null,              // startDate
                        null,              // endDate
                        null,              // catName
                        "updateTime",      // sortby
                                              0, numposts);
                
                Iterator<Post> iter = entries.iterator();
                while (iter.hasNext()) {
                    Post entry = iter.next();
                    results.addElement(createPostStruct(entry, userid));
                }
            return results;
            
        } catch (Exception e) {
            String msg = "ERROR in MetaWeblogAPIHandler.getRecentPosts";
            mLogger.error(msg,e);
            throw new XmlRpcException(UNKNOWN_EXCEPTION, msg);
        }
    }
    
    
    private Hashtable createPostStruct(Post entry, String userid) {
        
        
        Hashtable struct = new Hashtable();
        struct.put("title", entry.getTitle());
         Website site = Website.getInstance();
         String permalink = site.getBaseUrl()+"/post_"+entry.getId()+".html";
         struct.put("link", permalink);
        struct.put("description", entry.getContent());
        if (entry.getPubTime() != null) {
            struct.put("pubDate", entry.getPubTime());
            struct.put("dateCreated", entry.getPubTime());
        }
        struct.put("guid", Utilities.escapeHTML(permalink));
        struct.put("permaLink", Utilities.escapeHTML(permalink));
        struct.put("postid", entry.getId());
        
        struct.put("userid", userid);
        struct.put("author", userid);
        
        Vector catArray = new Vector();
        catArray.addElement(entry.getCategory().getName());
        struct.put("categories", catArray);
        
        return struct;
    }
    
    
    private Hashtable createCategoryStruct(Category category, String userid) {
    	Website site = Website.getInstance();
        
        Hashtable struct = new Hashtable();
        struct.put("description", category.getDescription());
        
        String catUrl = site.getBaseUrl()+"/home_"+category.getName()+".html";
//        catUrl = StringUtils.replace(catUrl," ","%20");
        struct.put("htmlUrl", catUrl);
        
        String rssUrl = site.getBaseUrl()+"/rss";
//        rssUrl = StringUtils.replace(catUrl," ","%20");
        struct.put("rssUrl",rssUrl);
        
        return struct;
    }
    
}
