package cn.hexiao.flog.web.admin;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import cn.hexiao.flog.Constants;
import cn.hexiao.flog.FlogException;
import cn.hexiao.flog.business.FlogFactory;
import cn.hexiao.flog.business.IPostManager;
import cn.hexiao.flog.entity.Post;
import cn.hexiao.flog.web.HomePage;

public class EditPage {
	
	//TODO : a 编写处理 编辑文章的时候 更改分类的代码, 重新创建一个 Category类, 放到 post中.
	//暂时不能修改分类, 该功能暂时不实现.
	public String savePost() {
		Post post = (Post)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Constants.EDIT_POST);
		if(post == null)
			return null ;

		try {
			IPostManager postManager = FlogFactory.getFlog().getPostManager();
			postManager.savePost(post);
			
//			SessionFactory sf = HibernateUtil.sessionFactory;
//			sf.evictQueries();
			
			postManager.flush(); // TODO 必须调用flush吗?
			
			//更新缓存
			FacesContext context = FacesContext.getCurrentInstance();
			Application app = context.getApplication();
			HomePage homePage = (HomePage) app.evaluateExpressionGet(context, "#{homePage}", HomePage.class);
			
			//使该缓存失效. 是否正确?
			
//			Session hibernateSession = new HibernateUtil().getSession();
//			hibernateSession.evict(post);
			
			homePage.getItems();
			
			return "post_success";
		} catch (FlogException e) {
			return "post_failure";
		}
	}

}
