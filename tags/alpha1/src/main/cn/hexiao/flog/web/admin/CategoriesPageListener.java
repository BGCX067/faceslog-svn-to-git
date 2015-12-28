package cn.hexiao.flog.web.admin;

import java.util.Map;

import javax.el.ELContext;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CategoriesPageListener implements PhaseListener {
	static Log log = LogFactory.getLog(CategoriesPageListener.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 2597755433043573564L;

	public void afterPhase(PhaseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void beforePhase(PhaseEvent arg0) {
		log.debug("before phase");
		FacesContext context = FacesContext.getCurrentInstance();
		Application app = context.getApplication();
		CategoriesPage categoriesPage = (CategoriesPage) app.evaluateExpressionGet(context, "#{categoriesPage}", CategoriesPage.class);
		
		Map<String, String> paras = context.getExternalContext().getRequestParameterMap();
//		int page = 0;
		String catName = null;
		if(paras.containsKey("cat")) {
			try {
				catName =  paras.get("cat");
				log.debug("the cat name is: " + catName);
			} catch (NumberFormatException e) {
				log.info("bad request catName paras.");
			}
		}
		categoriesPage.loadPosts(catName);
	}

	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

}
