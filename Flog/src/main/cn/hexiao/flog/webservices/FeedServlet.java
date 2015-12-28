package cn.hexiao.flog.webservices;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.hexiao.flog.Website;
import cn.hexiao.flog.util.FeedUtil;
import cn.hexiao.flog.util.ModDateHeaderUtil;

public class FeedServlet extends HttpServlet {
	static Log log = LogFactory.getLog(FeedServlet.class);

	private static final long serialVersionUID = 7575870374917626255L;

	/**
     * Init method for this servlet
     */
    public void init(ServletConfig servletConfig) throws ServletException {

        super.init(servletConfig);

        log.info("Initializing FeedServlet");

    }
    
    /**
     * Handle GET requests for weblog feeds.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("Entering");
        
     // determine the lastModified date for this content
        long lastModified = System.currentTimeMillis();
        if(Website.getInstance().getLatestUpdateTime() != null) {
            lastModified = Website.getInstance().getLatestUpdateTime();
            log.debug("latest Update time: " + new Date(lastModified).toGMTString());
        }

        // Respond with 304 Not Modified if it is not modified.
        if (ModDateHeaderUtil.respondIfNotModified(request,response,lastModified)) {
        	log.info("not modified ...");
            return;
        }

        // set last-modified date
        ModDateHeaderUtil.setLastModifiedHeader(response, lastModified);


        // set content type
        String accepts = request.getHeader("Accept");
        String userAgent = request.getHeader("User-Agent");
        if (accepts != null && accepts.indexOf("*/*") != -1 &&
            userAgent != null && userAgent.startsWith("Mozilla")) {
            // client is a browser and feed style is enabled so we want 
            // browsers to load the page rather than popping up the download 
            // dialog, so we provide a content-type that browsers will display
            response.setContentType("text/xml; charset=utf-8");
        } else   {
            response.setContentType("application/rss+xml; charset=utf-8");
        } 
        FeedUtil feed = new FeedUtil();
        feed.loadLatestPosts();
        feed.buildRssContent();
        
        PrintWriter writer = response.getWriter();
        writer.print(feed.getRssContent());
        writer.flush();
        writer.close();
        

        log.debug("Exiting");
    }

}
