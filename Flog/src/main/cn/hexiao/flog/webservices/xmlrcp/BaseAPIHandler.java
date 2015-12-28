package cn.hexiao.flog.webservices.xmlrcp;

import java.io.Serializable;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.XmlRpcException;

import cn.hexiao.flog.Website;
import cn.hexiao.flog.business.FlogFactory;
import cn.hexiao.flog.business.IUserManager;
import cn.hexiao.flog.entity.User;
import cn.hexiao.flog.web.HomePage;

public class BaseAPIHandler implements Serializable {
	private static final long serialVersionUID = -8000557175765789970L;

	private static Log log =
            LogFactory.getLog(BaseAPIHandler.class);
    
    public static final int AUTHORIZATION_EXCEPTION = 0001;
    public static final String AUTHORIZATION_EXCEPTION_MSG =
            "Invalid Username and/or Password";
    
    public static final int UNKNOWN_EXCEPTION = 1000;
    public static final String UNKNOWN_EXCEPTION_MSG =
            "An error occured processing your request";
    
    public static final int UNSUPPORTED_EXCEPTION = 1001;
    public static final String UNSUPPORTED_EXCEPTION_MSG =
            "Unsupported method -  does not support this method";
    
    public static final int USER_DISABLED = 1002;
    public static final String USER_DISABLED_MSG =
            "User is disabled";
    
    public static final int WEBLOG_NOT_FOUND = 1003;
    public static final String WEBLOG_NOT_FOUND_MSG =
            "Weblog is not found or is disabled";
    
    public static final int WEBLOG_DISABLED = 1004;
    public static final String WEBLOG_DISABLED_MSG =
            "Weblog is not found or is disabled";
    
    public static final int BLOGGERAPI_DISABLED = 1005;
    public static final String BLOGGERAPI_DISABLED_MSG =
            "Weblog does not exist or XML-RPC disabled in web";
    
    public static final int BLOGGERAPI_INCOMPLETE_POST = 1006;
    public static final String BLOGGERAPI_INCOMPLETE_POST_MSG =
            "Incomplete weblog entry";
    
    public static final int INVALID_POSTID = 2000;
    public static final String INVALID_POSTID_MSG =
            "The entry postid you submitted is invalid";
    
    public static final int UPLOAD_DENIED_EXCEPTION = 4000;
    public static final String UPLOAD_DENIED_EXCEPTION_MSG =
            "Upload denied";
    
    public BaseAPIHandler() {
    }
    
    
    protected boolean validate( String username, String password)
    throws Exception {
        boolean authenticated = false;
       IUserManager userManager = FlogFactory.getFlog().getUserManager();
       User user = userManager.getUser(username, password);
       if(user != null) {
    	   authenticated = true;
       } else {
    	   log.error("ERROR internal error validating user" );
       }
       
        if ( !authenticated ) {
            throw new XmlRpcException(
                    AUTHORIZATION_EXCEPTION, AUTHORIZATION_EXCEPTION_MSG);
        }
        return authenticated;
    }
    
    
    protected void flushPage( ) throws Exception {
    	//更新缓存
    	//TODO 在xml rpc 环境中不能使用, 修改缓存更新代码, 如果 站点更新日期 更新了, 就更新缓存.
//		FacesContext context = FacesContext.getCurrentInstance();
//		Application app = context.getApplication();
//		HomePage homePage = (HomePage) app.evaluateExpressionGet(context, "#{homePage}", HomePage.class);
//		homePage.getItems();
		
		// 更新站点更新日期.
		Website site = Website.getInstance();
		site.setLatestUpdateTime(System.currentTimeMillis());
    }
}
