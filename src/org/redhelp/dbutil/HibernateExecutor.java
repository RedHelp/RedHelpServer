package org.redhelp.dbutil;

import org.hibernate.Session;
import org.redhelp.common.exceptions.DependencyException;

public interface HibernateExecutor<T> {

    T executeWithSession(Session sess) throws DependencyException, Exception;
}
