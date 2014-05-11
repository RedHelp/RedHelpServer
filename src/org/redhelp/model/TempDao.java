package org.redhelp.model;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

public class TempDao extends HibernateDaoSupport{

	public void saveUser(UserAccountModel user_account)
	{
		if(null == user_account){
			System.out.print("null recorded");
		}
		
		HibernateTemplate daoSupport = getHibernateTemplate();
		if(daoSupport == null){
			System.out.print("dao support null");
		}
		daoSupport .setCheckWriteOperations(false);
		daoSupport.save(user_account);
	}
}
