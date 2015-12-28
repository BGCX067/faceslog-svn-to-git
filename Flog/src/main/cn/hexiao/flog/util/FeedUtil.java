package cn.hexiao.flog.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.hexiao.flog.FlogException;
import cn.hexiao.flog.Website;
import cn.hexiao.flog.business.FlogFactory;
import cn.hexiao.flog.business.IPostManager;
import cn.hexiao.flog.entity.Post;

public class FeedUtil {
	static Log log = LogFactory.getLog(FeedUtil.class);
	//设置rss缓存
	long lastModified = 0L;
	private List<Post> latestPosts = null;
	
	private String rssContent ;
	
	public FeedUtil() {
		this.latestPosts = new ArrayList<Post>(10); //生成10篇rss信息
		
	}
	public void loadLatestPosts() {
		log.info("load posts for rss");
		try {
			IPostManager postManager = FlogFactory.getFlog().getPostManager();
			this.latestPosts = postManager.getPosts(null, null, null, null, 0, 10);
		} catch (FlogException e) {
			log.error("load post error ",e);
		}
	}
	
	public String buildRssContent() {
		log.info("build rss");
		if(this.latestPosts == null) {
			this.loadLatestPosts();
		}
		Website site = Website.getInstance();
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
			.append("<rss version=\"2.0\">")
			.append("<channel>")
			.append("<title>")
			.append(site.getName())
			.append("</title>")
			.append("<link>")
			.append(site.getBaseUrl()).append(site.getHomeUri())
			.append("</link>")
			.append("<description>来自 ").append(site.getName()).append(" 网站的最新内容</description>")
			.append("<language>zh-CN</language> ")
			.append("<copyright>Copyright 2008 Ryan Weblog.</copyright> ")
			.append("<pubDate>").append(DateUtil.formatRfc822(new Date(site.getLatestUpdateTime()))).append("</pubDate>")
			.append("<image>")
			.append("<title>").append(site.getName()).append("</title>")
			.append("<url>").append(site.getBaseUrl()).append(site.getLogoUri()).append("</url>")
			.append("<link>").append(site.getBaseUrl()).append("</link>")
			.append("</image>");
		for (Post post : this.latestPosts) {
			sb.append("<item>").append("<title>").append(post.getTitle()).append("</title>")
			.append("<description>").append(post.getSummary()).append("</description>")
			.append("<link>").append(site.getBaseUrl()).append("/post_").append(post.getId()).append(".html").append("</link>")
			.append("</item>");
			
		}
		sb.append("</channel></rss>");
		rssContent = sb.toString();
		return rssContent;
	}
	public long getLastModified() {
		return lastModified;
	}
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
	public List<Post> getLatestPosts() {
		return latestPosts;
	}
	public void setLatestPosts(List<Post> latestPosts) {
		this.latestPosts = latestPosts;
	}
	public String getRssContent() {
		return rssContent;
	}
	public void setRssContent(String rssContent) {
		this.rssContent = rssContent;
	}
	
 

}
