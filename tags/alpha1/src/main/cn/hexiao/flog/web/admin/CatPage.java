package cn.hexiao.flog.web.admin;

import javax.faces.application.Application;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;

import cn.hexiao.flog.FlogException;
import cn.hexiao.flog.business.FlogFactory;
import cn.hexiao.flog.business.IPostManager;
import cn.hexiao.flog.entity.Category;
import cn.hexiao.flog.web.HomePage;

public class CatPage {
	
	private UIData data;
	
	private String name;
	private String des;
	
	public String addCatAction() {
		try {
			IPostManager pm = FlogFactory.getFlog().getPostManager();
			Category c = new Category();
			c.setDescription(this.des);
			c.setName(this.name);
			pm.saveCategory(c);
			pm.flush();
			
			//从新加载分类信息
			FacesContext context = FacesContext.getCurrentInstance();
			Application app = context.getApplication();
			HomePage homePage = (HomePage) app.evaluateExpressionGet(context, "#{homePage}", HomePage.class);
			homePage.reloadLatestCat();
		} catch (FlogException e) {
			 
			e.printStackTrace();
		}
		return null;
	}
	
	public String editCatAction() {
		//TODO 实现该功能
		return null;
	}
	
	public String deleteCatAction() {
		//TODO 实现该功能
		return null;
	}
	
	public UIData getData() {
		return data;
	}
	public void setData(UIData data) {
		this.data = data;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	

}
