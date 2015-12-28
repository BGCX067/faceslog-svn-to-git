package cn.hexiao.flog.web;

import java.util.Map;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PostPageListener implements PhaseListener {
	static Log log = LogFactory.getLog(PostPageListener.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -7959408572230119883L;

	public void afterPhase(PhaseEvent arg0) {
		log.debug("after phase");
	}

	public void beforePhase(PhaseEvent arg0) {
		log.debug("before phase -- get post from db");
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String, String> paras = fc.getExternalContext().getRequestParameterMap();
		long postId = 0;
		if(paras.containsKey("post")) {
			try {
				postId = Long.valueOf(paras.get("post"));
			} catch (NumberFormatException e) {
				log.info("Bad post id, do nothing");
				return;
			}
		} else {
			return ;
		}
		if(postId > 0) {
			Application app = fc.getApplication();
			PostPage postPage = (PostPage) app.evaluateExpressionGet(fc, "#{postPage}", PostPage.class);
			postPage.loadPost(postId);
		}
		
	}

	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

}
