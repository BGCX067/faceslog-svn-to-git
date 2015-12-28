package cn.hexiao.flog.web;

import java.util.List;

import cn.hexiao.flog.entity.PersistentObject;

/**
 * Common pager interface.
 */
public interface Pager {
    /**
     * Link value for returning to pager home
     */
    public String getHomeLink();

    /**
     * Name of pager home.
     */
    public String getHomeName();

    /**
     * Link value for next page in current collection view
     */
    public String getNextLink();

    /**
     * Name for next page in current collection view
     */
    public String getNextName();

    /**
     * Link value for prev page in current collection view
     */
    public String getPrevLink();

    /**
     * Link value for prev page in current collection view
     */
    public String getPrevName();
    
    /**
     * Get current list of items available from the pager.
     */
    //TODO  <? extends PersistentObject> right?
    public List<? extends PersistentObject> getItems();
    
}

