package cn.hexiao.flog.business;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * The abstract version of the IFlog implementation.
 *
 * Here we put code that pertains to *all* implementations of the IFlog
 * interface, regardless of their persistence strategy.
 */
//TODO now this class do nothing.
public abstract class Flog implements IFlog {
	static Log log = LogFactory.getLog(Flog.class);
	
	
	public Flog() {}
	
	public void release() {
		//TODO nothing
	}

}
