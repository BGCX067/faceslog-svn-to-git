package cn.hexiao.flog.entity;

import java.io.Serializable;

public abstract class PersistentObject implements Serializable {
	public abstract Long getId();
    public abstract void setId( Long id );
    
    /**
     * Load data based on data from another object.
     */
    public abstract void setData(PersistentObject obj);
    //TODO 是否有必要覆盖这些方法?
//    public boolean equals(Object o) {
//        return EqualsBuilder.reflectionEquals(this, o);
//    }
//    
//    
//    // TODO: how efficient is this?
//    public String toString() {
//        try {
//            // this may throw an exception if called by a thread that
//            return ToStringBuilder.reflectionToString(
//                    this, ToStringStyle.MULTI_LINE_STYLE);
//        } catch (Throwable e) {
//            // alternative toString() implementation used in case of exception
//            return getClass().getName() + ":" + getId();
//        }
//    }
}
