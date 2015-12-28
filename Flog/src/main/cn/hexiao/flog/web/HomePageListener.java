package cn.hexiao.flog.web;

import java.util.List;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.hexiao.flog.entity.Post;

public class HomePageListener implements PhaseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 120899243612674897L;
	static Log log = LogFactory.getLog(HomePageListener.class);

	public void afterPhase(PhaseEvent arg0) {
		log.debug("after phase");
	}

	@SuppressWarnings("unchecked")
	public void beforePhase(PhaseEvent arg0) {
		log.debug("before phase");
		FacesContext context = FacesContext.getCurrentInstance();
		Application app = context.getApplication();
		//ValueExpression ve = app.getExpressionFactory().createValueExpression(elContext, "#{homePage}", HomePage.class);
		//HomePage homePage = (HomePage) ve.getValue(elContext);
		HomePage homePage = (HomePage) app.evaluateExpressionGet(context, "#{homePage}", HomePage.class);
		
		Map<String, String> paras = context.getExternalContext().getRequestParameterMap();
		int page = 0;
		String catName = null;
		if(paras.containsKey("page")) {
			try {
				page = Integer.valueOf(paras.get("page"));
				log.debug("the page number is: " + page);
			} catch (NumberFormatException e) {
				log.info("bad request paras.");
			}
		}
		if(paras.containsKey("cat")) {
			try {
				catName =  paras.get("cat");
				log.debug("the cat name is: " + catName);
			} catch (NumberFormatException e) {
				log.info("bad request catName paras.");
			}
		}
		log.debug("get the posts for the page: " + page);
		homePage.setPage(page);
		homePage.setCatName(catName);
		homePage.setItems((List<Post>) homePage.getItems());
		log.debug("get the posts for the page: " + page);
	}

	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

}
