package org.redhelp.dbutil;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hibernate.criterion.Restrictions.eq;

import java.util.List;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

// The following code is using a design pattern for Generic Data Access Object
// http://www.hibernate.org/328.html?cmd=prntdoc

public abstract class AbstractDAO<T, ID extends Serializable>
{
    @Autowired
    private SessionFactory sessionFactory;
    
    private Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    public AbstractDAO() 
    {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Class<T> getPersistentClass() 
    {
        return persistentClass;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public T findById(ID id) throws HibernateException 
    {
        return (T) getSession().get(getPersistentClass(), id);
    }
    
    @SuppressWarnings("unchecked")
    public T findAndLockById(ID id, LockMode lm) throws HibernateException 
    {
        return (T) getSession().load(getPersistentClass(), id, lm);
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() throws HibernateException 
    {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        return crit.list();
    }

    @SuppressWarnings("unchecked")
    public List<T> findBy(final String fieldName, final Object fieldValue) throws HibernateException 
    {
        if (fieldName == null || fieldName.trim().equals("") || fieldValue == null) 
            throw new IllegalStateException("Invalid Field Name / Field Value");
        
        Criteria crit = getSession().createCriteria(getPersistentClass());
        crit.add(eq(fieldName, fieldValue));

        return crit.list();
    }

    @SuppressWarnings("unchecked")
    public List<T> findBy(final long marketplaceId, final String fieldName, final Object fieldValue) throws HibernateException 
    {
        if (fieldName == null || fieldName.trim().equals("") || fieldValue == null) 
            throw new IllegalStateException("Invalid Field Name / Field Value");
        
        Criteria crit = getSession().createCriteria(getPersistentClass());
        crit.add(eq("marketplaceId", marketplaceId));
        crit.add(eq(fieldName, fieldValue));

        return crit.list();
    }

    @SuppressWarnings("unchecked")
    public T findSingleResult(final String fieldName, final Object fieldValue) throws HibernateException 
    {
        if (fieldName == null || fieldName.trim().equals("") || fieldValue == null)
            throw new IllegalStateException("Invalid Field Name / Field Value");

        Criteria crit = getSession().createCriteria(getPersistentClass());
        crit.add(eq(fieldName, fieldValue));
        crit.setFetchSize(1);
        crit.setMaxResults(1);

        return (T) crit.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public T findSingleResult(final long marketplaceId, final String fieldName, final Object fieldValue) throws HibernateException 
    {
        if (fieldName == null || fieldName.trim().equals("") || fieldValue == null)
            throw new IllegalStateException("Invalid Field Name / Field Value");

        Criteria crit = getSession().createCriteria(getPersistentClass());
        crit.add(eq("marketplaceId", marketplaceId));
        crit.add(eq(fieldName, fieldValue));
        crit.setFetchSize(1);
        crit.setMaxResults(1);

        return (T) crit.uniqueResult();
    }

    public T create(T entity) throws HibernateException 
    {
        getSession().save(entity);
        return entity;
    }

    public T update(T entity) throws HibernateException 
    {
        getSession().update(entity);
        return entity;
    }
    
    public T saveOrUpdate(T entity) throws HibernateException 
    {
        getSession().saveOrUpdate(entity);
        return entity;
    }

    public void remove(T entity) throws HibernateException 
    {
        getSession().delete(entity);
    }
    
    public void flush() throws HibernateException
    {
        getSession().flush();
    }
    
    public void refresh(T entity) throws HibernateException
    {
        getSession().refresh(entity);
    }

}