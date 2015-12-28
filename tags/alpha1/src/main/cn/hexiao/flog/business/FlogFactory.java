package cn.hexiao.flog.business;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.hexiao.flog.FlogException;
import cn.hexiao.flog.business.hibernate.HibernateFlog;

/**
 * Provides access to the Flog instance.
 */
public abstract class FlogFactory {
	static Log log = LogFactory.getLog(FlogFactory.class);
	
	private static IFlog flog = null;
	
	private FlogFactory() {}
	
	public static IFlog getFlog() {
		if(flog == null) {
			try {
				flog = HibernateFlog.instantiate();
			} catch (FlogException e) {
				log.fatal("Failed to instantiate   flog impl", e);
			}
		}
		return flog;
	}
	

}
