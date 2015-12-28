package cn.hexiao.flog.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RefreshFilter implements Filter {
	static Log log = LogFactory.getLog(RefreshFilter.class);

	public void destroy() {

	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession(false);
		if (session != null) {

			long lastAccessedTime = session.getLastAccessedTime();
			long jg = System.currentTimeMillis() - lastAccessedTime;

			// check if client is allowed
			if (jg < 300) {
				log.debug("BANNED " + request.getRemoteAddr());
				log.debug("ContextPath: " +request.getContextPath());
				log.debug("URL: " +request.getRequestURL());
				log.debug("URI: " +request.getRequestURI());
				response.sendRedirect(request.getContextPath()+"/sorry.jsp?continue="+request.getRequestURL());
				//response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			} else {
				chain.doFilter(request, response);
			}
		} else {
			chain.doFilter(request, response);
		}

	}

	public void init(FilterConfig arg0) throws ServletException {
		log.info("INIT RefreshFilter ");
	}

}
