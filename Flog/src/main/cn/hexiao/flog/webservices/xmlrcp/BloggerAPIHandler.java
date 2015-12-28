package cn.hexiao.flog.webservices.xmlrcp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.XmlRpcException;

import cn.hexiao.flog.FlogException;
import cn.hexiao.flog.Website;
import cn.hexiao.flog.business.FlogFactory;
import cn.hexiao.flog.business.IPostManager;
import cn.hexiao.flog.business.IUserManager;
import cn.hexiao.flog.entity.Post;
import cn.hexiao.flog.entity.User;
import cn.hexiao.flog.util.Utilities;

public class BloggerAPIHandler extends BaseAPIHandler {

	private static final long serialVersionUID = -8937236678338607647L;
	static Log log = LogFactory.getLog(BloggerAPIHandler.class);
	
	public BloggerAPIHandler() {
		super();
	}
	
	 /**
     * Delete a Post
     *
     * @param appkey Unique identifier/passcode of the application sending the post
     * @param postid Unique identifier of the post to be changed
     * @param userid Login for a Blogger user who has permission to post to the blog
     * @param password Password for said username
     * @param publish Ignored
     * @throws XmlRpcException
     * @return
     */
    public boolean deletePost(String appkey, String postid, String userid,
            String password, boolean publish) throws Exception {
        
    	log.debug("deletePost() Called =====[ SUPPORTED ]=====");
    	log.debug("     Appkey: " + appkey);
    	log.debug("     PostId: " + postid);
    	log.debug("     UserId: " + userid);
        
        
        
        if(!validate(userid, password)) {
        	return false;
        }
        
        try {
            // delete the entry
            IPostManager postManager = FlogFactory.getFlog().getPostManager();
            Post post = new Post();
            post.setId(Long.valueOf(postid));
            postManager.removePost(post);
            postManager.flush();
            
            // notify cache
        } catch (Exception e) {
            String msg = "ERROR in blogger.deletePost: "+e.getClass().getName();
            log.error(msg,e);
            throw new XmlRpcException(UNKNOWN_EXCEPTION, msg);
        }
        
        return true;
    }
    
    /**
     * Edits the main index template of a given blog.  
     *
     * @param appkey Unique identifier/passcode of the application sending the post
     * @param blogid Unique identifier of the blog the post will be added to
     * @param userid Login for a Blogger user who has permission to post to the blog
     * @param password Password for said username
     * @param template The text for the new template (usually mostly HTML).
     * @param templateType Determines which of the blog's templates is to be set.
     * @throws XmlRpcException
     * @return
     */
    public boolean setTemplate(String appkey, String blogid, String userid,
            String password, String templateData,
            String templateType) throws Exception {
        
       throw new UnsupportedOperationException("not supported yet.");
    }
    
    /**
     * Returns the main or archive index template of a given blog
     *
     * @param appkey Unique identifier/passcode of the application sending the post
     * @param blogid Unique identifier of the blog the post will be added to
     * @param userid Login for a Blogger user who has permission to post to the blog
     * @param password Password for said username
     * @param templateType Determines which of the blog's templates will be returned. Currently, either "main" or "archiveIndex"
     * @throws XmlRpcException
     * @return
     */
    public String getTemplate(String appkey, String blogid, String userid,
            String password, String templateType)
            throws Exception {
    	 throw new UnsupportedOperationException("not supported yet.");
    }

    /**
     * Authenticates a user and returns a  user object
     *
     * @param appkey Unique identifier/passcode of the application sending the post
     * @param userid Login for a Blogger user who has permission to post to the blog
     * @param password Password for said username
     * @throws XmlRpcException
     * @return
     */
    public Object getUserInfo(String appkey, String userid, String password)
    throws Exception {
        
        log.debug("getUserInfo() Called =====[ SUPPORTED ]=====");
        log.debug("     Appkey: " + appkey);
        log.debug("     UserId: " + userid);
        
        if(!validate(userid, password)) {
        	return null;
        }
        
        try {
            IUserManager userManager = FlogFactory.getFlog().getUserManager();
            User user = userManager.getUser(Long.valueOf(userid));
        
            // populates user information to return as a result
            Hashtable<String, String> result = new Hashtable<String, String>();
            result.put("nickname", user.getName());
            result.put("userid", user.getId().toString());
            result.put("email", user.getEmail());
            result.put("lastname", "Cheng");
            result.put("firstname", user.getName());
             
            return result;
        } catch (Exception e) {
            String msg = "ERROR in BlooggerAPIHander.getInfo";
            log.error(msg,e);
            throw new XmlRpcException(UNKNOWN_EXCEPTION,msg);
        }
    }
    
    /**
     * Returns information on all the blogs a given user is a member of
     *
     * @param appkey Unique identifier/passcode of the application sending the post
     * @param userid Login for a Blogger user who has permission to post to the blog
     * @param password Password for said username
     * @throws XmlRpcException
     * @return
     */
    public Object getUsersBlogs(String appkey, String userid, String password)
    throws Exception {
        
    	log.debug("getUsersBlogs() Called ===[ SUPPORTED ]=======");
        log.debug("     Appkey: " + appkey);
        log.debug("     UserId: " + userid);
        Vector result = new Vector();
        Hashtable blog = null;
        if (validate(userid, password)) {
            try {
                 Website site = Website.getInstance();
                    blog = new Hashtable (3);
                    blog.put("url", site.getBaseUrl());
                    blog.put("blogid", site.getId().toString());
                    blog.put("blogName", site.getName());
                    result.add(blog);
            } catch (Exception e) {
                String msg = "ERROR in BlooggerAPIHander.getUsersBlogs";
                log.error(msg,e);
                throw new XmlRpcException(UNKNOWN_EXCEPTION, msg);
            }
        }
        return result;
    }
    

    /**
     * Edits a given post. Optionally, will publish the blog after making the edit
     *
     * @param appkey Unique identifier/passcode of the application sending the post
     * @param postid Unique identifier of the post to be changed
     * @param userid Login for a Blogger user who has permission to post to the blog
     * @param password Password for said username
     * @param content Contents of the post
     * @param publish If true, the blog will be published immediately after the post is made
     * @throws XmlRpcException
     * @return
     */
    public boolean editPost(String appkey, String postid, String userid,
            String password, String content, boolean publish)
            throws Exception {
        
        log.debug("editPost() Called ========[ SUPPORTED ]=====");
        log.debug("     Appkey: " + appkey);
        log.debug("     PostId: " + postid);
        log.debug("     UserId: " + userid);
        log.debug("    Publish: " + publish);
        log.debug("     Content:\n " + content);
        
        if (validate(userid, password)) {
            try {
                Timestamp current = new Timestamp(System.currentTimeMillis());
                
                IPostManager postManager = FlogFactory.getFlog().getPostManager();
                Post post = postManager.getPost(Long.valueOf(postid));
                
                post.setContent(content);
                post.setUpdateTime(current);
                //TODO 尚未实现
//                if (Boolean.valueOf(publish).booleanValue()) {
//                    post.setStatus(WeblogEntryData.PUBLISHED);
//                } else {
//                    post.setStatus(WeblogEntryData.DRAFT);
//                }
                
                // save the entry
                postManager.savePost(post);
                postManager.flush();
                // notify cache
                flushPage();
                
                return true;
            } catch (Exception e) {
                String msg = "ERROR in BlooggerAPIHander.editPost";
                log.error(msg,e);
                throw new XmlRpcException(UNKNOWN_EXCEPTION, msg);
            }
        }
        return false;
    }
    
    
    /**
     * Makes a new post to a designated blog. Optionally, will publish the blog after making the post
     *
     * @param appkey Unique identifier/passcode of the application sending the post
     * @param blogid Unique identifier of the blog the post will be added to
     * @param userid Login for a Blogger user who has permission to post to the blog
     * @param password Password for said username
     * @param content Contents of the post
     * @param publish If true, the blog will be published immediately after the post is made
     * @throws XmlRpcException
     * @return
     */
    public String newPost(String appkey, String blogid, String userid,
            String password, String content, boolean publish)
            throws Exception {
        
        log.debug("newPost() Called ===========[ SUPPORTED ]=====");
        log.debug("     Appkey: " + appkey);
        log.debug("     BlogId: " + blogid);
        log.debug("     UserId: " + userid);
        log.debug("    Publish: " + publish);
        log.debug("    Content:\n " + content);
        
        if(!validate(userid, password)) {
        	throw new FlogException("xmlrcp validate error,");
        }
        
        // extract the title from the content
        String title = "";
        
        if (content.indexOf("<title>") != -1) {
            title =
                    content.substring(content.indexOf("<title>") + 7,
                    content.indexOf("</title>"));
            content = StringUtils.replace(content, "<title>"+title+"</title>", "");
        }
        if (StringUtils.isEmpty(title)) {
            title = Utilities.truncateNicely(content, 15, 15, "...");
        }
        
        try {
            IPostManager postManager = FlogFactory.getFlog().getPostManager();
            
            Timestamp current = new Timestamp(System.currentTimeMillis());
            
            Post entry = new Post();
            entry.setTitle(title);
            entry.setContent(content);
            entry.setPubTime(current);
            entry.setUpdateTime(current);
            entry.setHitCount(1L);
            entry.setSummary(Utilities.truncateNicely(content, 50, 50, "..."));
//            entry.setCategory(new Category()); //TODO  fix this!
//            if (Boolean.valueOf(publish).booleanValue()) {
//                entry.setStatus(WeblogEntryData.PUBLISHED);
//            } else {
//                entry.setStatus(WeblogEntryData.DRAFT);
//            }
            
            // save the entry
            postManager.savePost(entry);
            postManager.flush();
            
            // notify cache
            flushPage();
            
            return entry.getId().toString();
        } catch (Exception e) {
            String msg = "ERROR in BlooggerAPIHander.newPost";
            log.error(msg,e);
            throw new XmlRpcException(UNKNOWN_EXCEPTION, msg);
        }
    }
    
    
    /**
     * This method was added to the Blogger 1.0 API via an Email from Evan
     * Williams to the Yahoo Group bloggerDev, see the email message for details -
     * http://groups.yahoo.com/group/bloggerDev/message/225
     *
     * @param appkey Unique identifier/passcode of the application sending the post
     * @param blogid Unique identifier of the blog the post will be added to
     * @param userid Login for a Blogger user who has permission to post to the blog
     * @param password Password for said username
     * @param numposts Number of Posts to Retrieve
     * @throws XmlRpcException
     * @return Vector of Hashtables, each containing dateCreated, userid, postid, content
     */
    public Object getRecentPosts(String appkey, String blogid, String userid,
            String password, int numposts)
            throws Exception {
        
        log.debug("getRecentPosts() Called ===========[ SUPPORTED ]=====");
        log.debug("     Appkey: " + appkey);
        log.debug("     BlogId: " + blogid);
        log.debug("     UserId: " + userid);
        log.debug("     Number: " + numposts);
        
        if(!validate(userid,password)) {
        	return null;
        }
        
        try {
            Vector<Hashtable<String, Object>> results = new Vector<Hashtable<String,Object>>();
            
            IPostManager postManager = FlogFactory.getFlog().getPostManager();
                Map<Date,List<Post>> entries = postManager.getPostsObjectMap(
                        null,                   // startDate
                        new Date(),             // endDate
                        null,                   // catName
                          0, -1);
                
                Iterator<List<Post>> iter = entries.values().iterator();
                while (iter.hasNext()) {
                    ArrayList<Post> list = (ArrayList<Post>) iter.next();
                    Iterator<Post> i = list.iterator();
                    while (i.hasNext()) {
                        Post entry = (Post) i.next();
                        Hashtable<String, Object> result = new Hashtable<String, Object>();
                        if (entry.getPubTime() != null) {
                            result.put("dateCreated", entry.getPubTime());
                        }
                        result.put("userid", userid);
                        result.put("postid", entry.getId());
                        result.put("content", entry.getContent());
                        results.add(result);
                    }
                }
            return results;
        } catch (Exception e) {
            String msg = "ERROR in BlooggerAPIHander.getRecentPosts";
            log.error(msg,e);
            throw new XmlRpcException(UNKNOWN_EXCEPTION, msg);
        }
    }
}
