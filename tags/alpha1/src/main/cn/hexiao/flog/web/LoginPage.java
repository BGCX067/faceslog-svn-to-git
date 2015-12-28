package cn.hexiao.flog.web;

import javax.faces.application.Application;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.hexiao.flog.Constants;
import cn.hexiao.flog.FlogException;
import cn.hexiao.flog.business.FlogFactory;
import cn.hexiao.flog.business.IUserManager;
import cn.hexiao.flog.entity.User;

public class LoginPage {
	static Log log = LogFactory.getLog(LoginPage.class); 
	private HtmlInputText username;
	private HtmlInputSecret password;
	
	public LoginPage() {
		
	}
	
	public String login() {
		log.debug("login Username: " + username.getValue() +" Password: " + password.getValue());
		User user = null;
		try {
			IUserManager userManager = FlogFactory.getFlog().getUserManager();
			user = userManager.getUser(username.getValue().toString(), password.getValue().toString());
		} catch (FlogException e) {
			log.error("Login failed ",e);
		}
		if(user != null) {
			user.setLogedin(true);
			//store in session
			HttpSession httpSession = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			httpSession.setAttribute(Constants.LOGIN_USER, user);
			
			return "login_success";
		}
		return null; //"login_failure";
	}
	public String logout() {
		HttpSession httpSession = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		httpSession.invalidate();
		
		//navigate back to login page
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		app.getNavigationHandler().handleNavigation(fc, "/me.xhtml", "login");
		return null;
		
	}

	public HtmlInputText getUsername() {
		return username;
	}

	public void setUsername(HtmlInputText username) {
		this.username = username;
	}

	public HtmlInputSecret getPassword() {
		return password;
	}

	public void setPassword(HtmlInputSecret password) {
		this.password = password;
	}

}
