package org.redhelp.daos;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.redhelp.common.exceptions.InvalidRequestException;
import org.redhelp.model.UserAccountModel;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

public class UserAccountDao extends HibernateDaoSupport
{
	private Logger logger = Logger.getLogger(UserAccountDao.class);

	public Serializable saveUser(UserAccountModel user_account)
	{
		if(null == user_account) {
			throw new NullPointerException("UserAccountModel passed is null");
		}
			
		SessionFactory factory = getSessionFactory();
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();

		//TODO remove date related code, shift this logic to Database triggers
		user_account.setRegisterDate(new Date());
		user_account.setLastUpdatedDate(new Date());
		
		Serializable user_model_returned =  session.save(user_account);
		tx.commit();
		session.close();
		
		String msg = String.format("User (%s) saved..", user_account.toString()); 
		logger.debug(msg);
		
		return user_model_returned;
	}
	
	public UserAccountModel findUser(String email) throws Exception
	{	
		if(null == email) {
			throw new NullPointerException("Email-id passed is null");
		}
			
		SessionFactory factory = getSessionFactory();
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		String hql_query = "FROM UserAccountModel U where U.email = :email_id";
		
		Query query = session.createQuery(hql_query);
		query.setParameter("email_id", email);
		List results = query.list();
		if(results.isEmpty()) 
			throw new InvalidRequestException("Invalid request, no account exists for email:" + email);
		if(results.size() > 1)
			throw new Exception("Invalid state, one email should have only one userObject");
		UserAccountModel user_account_model = (UserAccountModel) results.get(0);
		tx.commit();
		session.close();
		
		String msg = String.format("User (%s) retrieved..", user_account_model.toString()); 
		logger.trace(msg);
		
		return user_account_model;	
	}
	
	public UserAccountModel findUser(Long u_id) throws Exception
	{	
		if(null == u_id) {
			throw new NullPointerException("u-id passed is null");
		}
			
		SessionFactory factory = getSessionFactory();
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		String hql_query = "FROM UserAccountModel U where U.u_id = :u_id";
		
		Query query = session.createQuery(hql_query);
		query.setParameter("u_id", u_id);
		List results = query.list();
		
		if(results.size() > 1)
			throw new Exception("Invalid state, one email should have only one userObject");
		UserAccountModel user_account_model = (UserAccountModel) results.get(0);
		tx.commit();
		session.close();
		
		String msg = String.format("User (%s) retrieved..", user_account_model.toString()); 
		logger.trace(msg);
		
		return user_account_model;	
	}
	
	public void editUserAccount(UserAccountModel account_model) throws Exception
	{
		if(null == account_model) {
			throw new NullPointerException("UserAccountModel passed is null");
		}
		HibernateTemplate daoSupport = getHibernateTemplate();
		
		if(daoSupport == null){
			throw new NullPointerException("Failed to retrieve HibernateTemplate");
		}
		SessionFactory factory = getSessionFactory();
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
			session.update(account_model);
		tx.commit();
		session.close();
	}
}
