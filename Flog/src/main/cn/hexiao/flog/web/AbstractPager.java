package cn.hexiao.flog.web;

import java.util.HashMap;
import java.util.Map;

import cn.hexiao.flog.util.URLUtilities;
 


/**
 * Abstract base for simple pagers.
 */
public abstract class AbstractPager implements Pager {
    
    private String url = null;
    private int page = 0;
    
    
    public AbstractPager(String baseUrl, int pageNum) {
        
        this.url = baseUrl;
        if(pageNum > 0) {
            this.page = pageNum;
        }
    }
    
    
    public String getHomeLink() {
        return url;
    }
    
    
    public String getHomeName() {
        return "Home";
    }
    
    
    public String getNextLink() {
        if(hasMoreItems()) {
            int nextPage = page + 1;
            Map<String, String> params = new HashMap<String, String>();
            params.put("page", ""+nextPage);
            return createURL(url, params);
        }
        return null;
    }
    
    
    public String getNextName() {
        if(hasMoreItems()) {
            return "Next";
        }
        return null;
    }
    
    
    public String getPrevLink() {
        if (page > 0) {
            int prevPage = page - 1;
            Map<String, String> params = new HashMap<String, String>();
            params.put("page", ""+prevPage);
            return createURL(url, params);
        }
        return null;
    }
    
    
    public String getPrevName() {
        if (page > 0) {
            return "Previous";
        }
        return null;
    }
    
    
    public boolean hasMoreItems() {
        return false;
    }
    
    
    protected String createURL(String url, Map<String, String> params) {
        
        return url + URLUtilities.getQueryString(params);
    }

    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
    
}
