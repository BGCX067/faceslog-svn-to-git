package cn.hexiao.flog.business.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;

import cn.hexiao.flog.FlogException;
import cn.hexiao.flog.entity.PersistentObject;

public class HibernateUtil {
	static Log log = LogFactory.getLog(HibernateUtil.class);
	public static final SessionFactory sessionFactory;
	
	static {
		try {
			log.debug("build session Factory.");
			sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
			//sessionFactory.openSession();
		} catch (Throwable e) {
			log.fatal("build sessionFactory error.");
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public Session getSession() {
		log.debug("Opening Hibernate Session");
		// get Hibernate Session and make sure we are in a transaction
        // this will join existing Session/Transaction if they exist
		//   why getCurrentSession() do not work. 需要在hibernate配置文件中配置 currentsession
        Session session = sessionFactory.getCurrentSession();
        if(session == null) {
        	log.error("can not get the current session?");
        }
        session.beginTransaction();
        log.debug("before return session");
        return session;
	}

	public void flush() throws FlogException {
		Session session = getSession();
        try {
            session.getTransaction().commit();
        } catch(Throwable t) {
            // uh oh ... failed persisting, gotta release
            release();
            
            // wrap and rethrow so caller knows something bad happened
            throw new FlogException(t);
        }
	}
	
	/**
     * Release database session, rollback any uncommitted changes.
     *
     * IMPORTANT: we don't want to open a transaction and force the use of a
     * jdbc connection just to close the session and do a rollback, so this
     * method must be sensitive about how the release is triggered.
     *
     * In particular we don't want to use our custom getSession() method which
     * automatically begins a transaction.  Instead we get a Session and check
     * if there is already an active transaction that needs to be rolled back.
     * If not then we can close the Session without ever getting a jdbc
     * connection, which is important for scalability.
     */
    public void release() {
        
        try {
            Session session = sessionFactory.getCurrentSession();
            
            if(session != null && session.isOpen()) {
                
                log.debug("Closing Hibernate Session");
                
                try {
                    Transaction tx = session.getTransaction();
                    
                    if(tx != null && tx.isActive()) {
                        log.debug("Forcing rollback on active transaction");
                        tx.rollback();
                    }
                } catch(Throwable t) {
                    log.error("ERROR doing Hibernate rollback", t);
                } finally {
                    if(session.isOpen()) {
                        session.close();
                    }
                }
            }
        } catch(Throwable t) {
            log.error("ERROR closing Hibernate Session", t);
        }
    }
    
    @SuppressWarnings("unchecked")
	public <T extends Object> T load(Class<T> clazz,Long id) throws FlogException {
    	if(id == null || clazz == null) {
    		throw new FlogException("Cannot load objects when value is null");
    	}
    	return (T)getSession().get(clazz, id);
    }
    
    /**
     * Store object.
     */
    public void store(PersistentObject obj) throws HibernateException {
        
        if(obj == null) {
            throw new HibernateException("Cannot save null object");
        }
        
        Session session = getSession();
        
        // TODO BACKEND: this is wacky, we should double check logic here
        
        // TODO BACKEND: better to use session.saveOrUpdate() here, if possible
        if ( obj.getId() == null) {
            // Object has never been written to database, so save it.
            // This makes obj into a persistent instance.
            session.save(obj);
        }
        
        /*
         * technically we shouldn't have any reason to support the saving
         * of detached objects, so at some point we should re-evaluate this.
         *
         * objects should be re-attached before being saved again. it would
         * be more appropriate to reject these kinds of saves because they are
         * not really safe.
         *
         * NOTE: this may be coming from the way we use formbeans on the UI.
         *   we very commonly repopulate all data in a pojo (including id) from
         *   form data rather than properly loading the object from a Session
         *   then modifying its properties.
         */
        if ( !session.contains(obj) ) {
            
            log.debug("storing detached object: "+obj.toString());
            
            // Object has been written to database, but instance passed in
            // is not a persistent instance, so must be loaded into session.
            PersistentObject vo =
                    (PersistentObject)session.load(obj.getClass(),obj.getId());
            vo.setData(obj);
            obj = vo;
        }
        
       // session.getTransaction().commit(); // TODO 由于使用的是sessionFactory.openSession(), 在每个方法后面提交事物.
        
    }
    
    /**
     * Remove object.
     *
     * TODO BACKEND: force the use of remove(Object) moving forward.
     */
    @SuppressWarnings("unchecked")
	public void remove(String id, Class clazz) throws HibernateException {
        
        if(id == null || clazz == null) {
            throw new HibernateException("Cannot remove object when values are null");
        }
        
        Session session = getSession();
        
        PersistentObject obj = (PersistentObject) session.load(clazz,id);
        session.delete(obj);
    }
    
    
    /**
     * Remove object.
     */
    public void remove(PersistentObject obj) throws HibernateException {
        
        if(obj == null) {
            throw new HibernateException("Cannot remove null object");
        }
        
        // TODO BACKEND: can hibernate take care of this check for us?
        //               what happens if object does not use id?
        // can't remove transient objects
        if (obj.getId() != null) {
            
            getSession().delete(obj);
        }
    }
    
    
}
