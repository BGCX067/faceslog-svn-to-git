package cn.hexiao.flog.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.hexiao.flog.Constants;
import cn.hexiao.flog.entity.User;

public class LoginFilter implements Filter {
	private static final String DEFAULT_LOGIN_URI = "/me.jsf";
	private String loginUri = null;
	
	private static boolean checkLoginState(ServletRequest request,ServletResponse response) {
		HttpSession session = ((HttpServletRequest)request).getSession(false);
		
		User user = null;
		if(session != null && ((user = (User) session.getAttribute(Constants.LOGIN_USER)) != null )) {
			return true;
		}
		return false;
		
	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		boolean isLoggenIn = false;
		isLoggenIn = checkLoginState(request, response);
		
		if(!isLoggenIn) {
			if(loginUri == null || loginUri.trim().equals("")) {
				loginUri = DEFAULT_LOGIN_URI;
			}
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(loginUri);
			
			requestDispatcher.forward(request, response);
			return ;
		} else {
			chain.doFilter(request, response);
		}

	}

	public void init(FilterConfig config) throws ServletException {
		loginUri = config.getInitParameter("login.uri");
	}

}
