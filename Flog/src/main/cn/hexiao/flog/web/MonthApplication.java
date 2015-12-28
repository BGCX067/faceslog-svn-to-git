package cn.hexiao.flog.web;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.hexiao.flog.FlogException;
import cn.hexiao.flog.business.FlogFactory;
import cn.hexiao.flog.business.IPostManager;
import cn.hexiao.flog.entity.Post;
import cn.hexiao.flog.util.DateUtil;

//文章存档
public class MonthApplication {
	static Log log = LogFactory.getLog(MonthApplication.class);

	// Key 为月份的字符串值 如: 2008-01
	private TreeMap<String, Long> monthPostCount = null;
	private String startMonth = "200701";
	private Date startPost = null;

	DateFormat df = new SimpleDateFormat("yyyyMM");

	@SuppressWarnings("unchecked")
	public MonthApplication() {
		log.debug("init...");
		try {
			startPost = df.parse(startMonth);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		startPost = DateUtil.getStartOfMonth(startPost);
		monthPostCount = new TreeMap<String, Long>(new ReverseComparator());
		calculateMonthPost();
	}

	public void calculateMonthPost() {
		log.debug("init month post");
		try {
			IPostManager postManager = FlogFactory.getFlog().getPostManager();
			Date currentMonthStart = null;
			Date currentMonthEnd = DateUtil.getEndOfMonth(null);
			while (currentMonthEnd.after(startPost)) {
				currentMonthStart = DateUtil.getStartOfMonth(currentMonthEnd);
				// List<Post> monthPost =
				// postManager.getPosts(currentMonthStart,
				// currentMonthEnd, null, null, 0, -1);
				// if (monthPost != null && monthPost.size() > 0) {
				// log.debug(DateUtil.format6chars(currentMonthStart)+" month
				// have " +monthPost.size() +" posts");
				// monthPostMap.put(DateUtil.format6chars(currentMonthStart),
				// monthPost);
				// }

				Long c = postManager.getPostsCount(currentMonthStart,
						currentMonthEnd);
				if (c > 0) {
					log.debug(DateUtil.format6chars(currentMonthStart)
							+ " month have " + c + " posts");
					monthPostCount.put(
							DateUtil.format6chars(currentMonthStart), c);
				}
				currentMonthEnd = DateUtil.getPreEndOfMonth(currentMonthEnd);
			}
		} catch (FlogException e) {
			log.error("calculate Month Post error ", e);
		}
	}

	public void recalculateCurrentMonthPost() {
		try {
			IPostManager postManager = FlogFactory.getFlog().getPostManager();
			Date currentMonthStart = DateUtil.getStartOfMonth(null);
			Date currentMonthEnd = DateUtil.getEndOfMonth(null);
			List<Post> monthPost = postManager.getPosts(currentMonthStart,
					currentMonthEnd, null, null, 0, -1);
			String month = DateUtil.format6chars(currentMonthStart);
			// monthPost.size() > 1 or 0? 由于已经保存了一篇新的文章,
			// 所以,只有>1的时候,才说明这不是该月的第一篇文章,
			if (monthPost != null && monthPost.size() > 1) {
				monthPostCount.put(month, monthPostCount.get(month) + 1L);
			} else {
				monthPostCount.put(month, 1L);
			}
		} catch (FlogException e) {
			log.error("calculate Month Post error ", e);
		}
	}

//	public static void main(String[] args) {
//		MonthApplication mp = new MonthApplication();
//
//	}

	public TreeMap<String, Long> getMonthPostCount() {
		return monthPostCount;
	}

	public void setMonthPostCount(TreeMap<String, Long> monthPostCount) {
		this.monthPostCount = monthPostCount;
	}
}
