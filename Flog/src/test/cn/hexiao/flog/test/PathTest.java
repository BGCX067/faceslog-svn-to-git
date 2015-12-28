package cn.hexiao.flog.test;

import javax.faces.context.FacesContext;

public class PathTest {
	
	public String getTest() {
		System.out.println(FacesContext.getCurrentInstance().getExternalContext().getRequestPathInfo());
		System.out.println(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath());
		System.out.println(FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath());
		return "1";
	}

}
