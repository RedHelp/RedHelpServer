package org.redhelp.dbutil;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.redhelp.common.exceptions.DependencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HibernateUtil {

    private static Logger logger = Logger.getLogger(HibernateUtil.class);

    @Autowired
    SessionFactory sessionFactory;
    
    public Session getSession()
    {
	if(sessionFactory.getCurrentSession() != null)
	    return sessionFactory.getCurrentSession();
	return sessionFactory.openSession();
	
    }
    
    public void closeSession()
    {
	if(sessionFactory.getCurrentSession() != null)
	    sessionFactory.getCurrentSession().close();
    }

    public <R> R executeInTransaction(final HibernateExecutor<R> exec) throws DependencyException, Exception {
	final Session sess = sessionFactory.openSession();
	boolean success = false;
	Transaction trans = null;
	try {
	    trans = sess.beginTransaction();
	    final R result = exec.executeWithSession(sess);
	    trans.commit();
	    success = true;
	    return result;
	} finally {
	    try {
		if (!success && trans != null) {
		    trans.rollback();
		}
	    } catch (final Exception ex) {
		throw new DependencyException(ex.toString());
	    } finally {
		sess.close();
	    }
	}
    }

    public void testFactory() {
	if (sessionFactory == null)
	    logger.error("factory is null");
	else
	    logger.error("Factory is not null");
    }
}
