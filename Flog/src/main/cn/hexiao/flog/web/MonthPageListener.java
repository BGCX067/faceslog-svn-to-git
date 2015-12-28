package cn.hexiao.flog.web;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.hexiao.flog.util.DateUtil;

public class MonthPageListener implements PhaseListener {
	static Log log = LogFactory.getLog(MonthPageListener.class);
	private Date startPost = null; // 每月的开始日期
	private Date endPost = null; // 每月的结束日期

	/**
	 * 
	 */
	private static final long serialVersionUID = -8483487735528518700L;

	public void afterPhase(PhaseEvent arg0) {

	}

	public void beforePhase(PhaseEvent arg0) {

		log.debug("before phase -- get month post from db");
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String, String> paras = fc.getExternalContext()
				.getRequestParameterMap();
		if (paras.containsKey("month")) {
			DateFormat df = new SimpleDateFormat("yyyyMM");
			try {
				startPost = df.parse(paras.get("month"));
			} catch (ParseException e) {
				log.error("bad month paras: " + paras.get("month"), e);
				FacesMessage fm = new FacesMessage("无效的请求参数,月份的格式为yyyyMM.");
				// 通过facesMessage来告诉用户,请求参数错误.
				fc.addMessage(null, fm);  
				return;
			}
			startPost = DateUtil.getStartOfMonth(startPost);
			endPost = DateUtil.getEndOfMonth(startPost);
		} else {
			FacesMessage fm = new FacesMessage("无效的请求参数,月份的格式为yyyyMM.");
			// 通过facesMessage来告诉用户,请求参数错误.
			fc.addMessage(null, fm);  
			return;
		}
		Application app = fc.getApplication();
		MonthPage monthPage = (MonthPage) app.evaluateExpressionGet(fc,
				"#{monthPage}", MonthPage.class);
		monthPage.loadMonthPosts(startPost, endPost);
	}

	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

}
