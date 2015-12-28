package cn.hexiao.flog;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Website implements Serializable {
	static Log log = LogFactory.getLog(Website.class);
	private static final long serialVersionUID = 9026635176560371934L;
	
	private Long id ;
    private String name;
    private String description;
    private String baseUrl;
    private String logoUri;
    private String homeUri;
    private String rssUri;
    private Long latestUpdateTime = System.currentTimeMillis();
    
    private static Website me;
    
    protected Website() {
    	log.info("init website...");
    	Properties pro = new Properties();
    	try {
			pro.load(Website.class.getClassLoader().getResourceAsStream(Constants.RESOURCE_FILE_NAME));
		} catch (IOException e) {
			log.info("site properties file missing, using default values");
			this.name = "Ryan's weblog";
			this.baseUrl = null;
			return ;
		}
		loadSiteProperties(pro);
    }
    protected void loadSiteProperties(Properties pro) {
    	this.baseUrl = pro.getProperty("baseUrl");
    	this.description = pro.getProperty("description","Ryan's log");
    	this.homeUri = pro.getProperty("homeUri","/home.html");
    	this.logoUri = pro.getProperty("logUri","/logo.gif");
    	this.name = pro.getProperty("name","Ryan's weblog");
    	this.rssUri = pro.getProperty("rssUri","/rss");
    	String time = pro.getProperty("latestUpdateTime");
    	if(time != null) {
    		this.latestUpdateTime = Long.valueOf(time);
    	} else {
    		this.latestUpdateTime = System.currentTimeMillis();
    	}
    	this.id = new Long(1);
    }
    
    public static Website getInstance() {
    	if(me == null) {
    		me = new Website();
    	}
    	return me;
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

//	@Override
//	public void setData(PersistentObject obj) {
//		Website other = (Website)obj;
//		this.setBaseUrl(other.getBaseUrl());
//		this.setDescription(other.getDescription());
//		this.setHomeUri(other.getHomeUri());
//		this.setLatestUpdateTime(other.getLatestUpdateTime());
//		this.setLogoUri(other.getLogoUri());
//		this.setName(other.getName());
//		this.setRssUri(other.getRssUri());
//		
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getLogoUri() {
		return logoUri;
	}

	public void setLogoUri(String logoUri) {
		this.logoUri = logoUri;
	}

	public String getHomeUri() {
		return homeUri;
	}

	public void setHomeUri(String homeUri) {
		this.homeUri = homeUri;
	}

	public String getRssUri() {
		return rssUri;
	}

	public void setRssUri(String rssUri) {
		this.rssUri = rssUri;
	}

	public Long getLatestUpdateTime() {
		return latestUpdateTime;
	}

	public void setLatestUpdateTime(Long latestUpdateTime) {
		this.latestUpdateTime = latestUpdateTime;
	}
	
	public static void main(String[] args) {
		new Website();
	}
}
