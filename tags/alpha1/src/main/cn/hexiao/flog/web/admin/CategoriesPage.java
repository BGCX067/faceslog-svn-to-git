package cn.hexiao.flog.web.admin;

import java.util.List;

import javax.faces.component.UIData;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.hexiao.flog.FlogException;
import cn.hexiao.flog.business.FlogFactory;
import cn.hexiao.flog.business.IPostManager;
import cn.hexiao.flog.entity.Category;
import cn.hexiao.flog.entity.Post;

/**
 * 
 * @author chengyf
 *
 */
public class CategoriesPage {
	static Log log = LogFactory.getLog(CategoriesPage.class);
	
	private UIData data;
	private List<Post> postList = null;
	
	public CategoriesPage() {
		
	}
	
	public String editPostAction() {
		return null;
	}
	public String deletePostAction() {
		return null;
	}
	
	//TODO 添加分页处理功能
	public void loadPosts(String catName) {
		try {
			IPostManager pm = FlogFactory.getFlog().getPostManager();
			if(catName != null) {
				Category cat = pm.getCategoryByName(catName);
				if(cat == null) {
					postList = null;
					return ;
				}
				postList = pm.getPosts(cat);
			} else {
				postList = pm.getPosts(null, null, null, null, 0, 8);
			}
		} catch (FlogException e) {
			log.error("load post error ",e);
		}
		
	}

	public UIData getData() {
		return data;
	}

	public void setData(UIData data) {
		this.data = data;
	}

	public List<Post> getPostList() {
		return postList;
	}

	public void setPostList(List<Post> postList) {
		this.postList = postList;
	}
	
	

}
