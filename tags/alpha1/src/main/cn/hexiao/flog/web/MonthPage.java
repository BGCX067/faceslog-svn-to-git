package cn.hexiao.flog.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.hexiao.flog.FlogException;
import cn.hexiao.flog.business.FlogFactory;
import cn.hexiao.flog.business.IPostManager;
import cn.hexiao.flog.entity.Post;
import cn.hexiao.flog.util.DateUtil;
import cn.hexiao.flog.web.wrapper.PostWrapper;

//查看每月的存档
public class MonthPage {
	static Log log = LogFactory.getLog(MonthPage.class);

	private List<PostWrapper> monthPosts = new ArrayList<PostWrapper>();

	@SuppressWarnings("unchecked")
	public MonthPage() {
		log.debug("init...");
	}

	public void loadMonthPosts(Date startDate,Date endDate) {
		log.debug("load month post: month "+startDate.toLocaleString());
		try {
			IPostManager postManager = FlogFactory.getFlog().getPostManager();
				List<Post> monthPost = postManager.getPosts(startDate,
						endDate, null, null, 0, -1);
				if (monthPost != null && monthPost.size() > 0) {
					log.debug(DateUtil.format6chars(startDate)+" month have " +monthPost.size() +" posts");
					for (Post post : monthPost) {
						this.monthPosts.add(new PostWrapper(post));
					}
				}
		} catch (FlogException e) {
			log.error("load Month Post error ", e);
		}
	}
	
	public List<PostWrapper> getMonthPosts() {
		return monthPosts;
	}

}
