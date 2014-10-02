package org.redhelp.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.redhelp.common.exceptions.DependencyException;
import org.redhelp.dbutil.AbstractDAO;
import org.redhelp.model.NotificationModel;
import org.redhelp.model.UserAccountModel;
import org.redhelp.util.Assert;
import org.springframework.stereotype.Repository;

@Repository
public class NotificationDAO extends AbstractDAO<NotificationModel, Long> {
    
    private Logger logger = Logger.getLogger(NotificationDAO.class);
    
    public List<NotificationModel> findByBpid(Long b_p_id)
    {
	Assert.assertNotNull(b_p_id, "b_p_id passed is null, can't proceed");
	
	List<NotificationModel> notifications_list = findBy("b_p_id", b_p_id);
	if(notifications_list.size() == 0)
	    return null;
	
	return notifications_list;
    }
    
    public List<NotificationModel> findNewNotificationsByBpid(Long b_p_id)
    {
	Assert.assertNotNull(b_p_id, "b_p_id passed is null, can't proceed (findNewNotificationsByBpid)");
	
	Criteria criteria = getSession().createCriteria(getPersistentClass());
	
	criteria.add(Restrictions.eq("read", false));
	criteria.add(Restrictions.eq("b_p_id", b_p_id));
	return criteria.list();	
    }
    
    
}
