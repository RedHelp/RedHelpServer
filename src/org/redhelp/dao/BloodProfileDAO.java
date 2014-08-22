package org.redhelp.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.redhelp.common.exceptions.DependencyException;
import org.redhelp.common.types.Location;
import org.redhelp.dbutil.AbstractDAO;
import org.redhelp.model.BloodRequestModel;
import org.redhelp.model.UserAccountModel;
import org.redhelp.model.UserBloodProfileModel;
import org.redhelp.util.Assert;
import org.springframework.stereotype.Repository;

@Repository
public class BloodProfileDAO extends AbstractDAO<UserBloodProfileModel, Long>{
    private Logger logger = Logger.getLogger(BloodProfileDAO.class);
    
    public List<UserBloodProfileModel> searchViaRange(Location southWestLocation, Location northEastLocation) {

	Criteria criteria = getSession().createCriteria(getPersistentClass());
	criteria.add(Restrictions.lt("last_known_location_lat", northEastLocation.latitude));
	criteria.add(Restrictions.lt("last_known_location_long",northEastLocation.longitude));
	
	criteria.add(Restrictions.gt("last_known_location_lat", southWestLocation.latitude));
	criteria.add(Restrictions.gt("last_known_location_long", southWestLocation.longitude));
	criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	
	if(criteria.list()!= null)
	    logger.debug(criteria.list().toString());
	return criteria.list();	
    }
    
    public UserBloodProfileModel saveBloodProfile(UserBloodProfileModel blood_profile_passed)
    {
	
	if(blood_profile_passed == null)
	    new DependencyException("Invalid call to saveBloodProfile, blood_profile_passed is null");
	UserBloodProfileModel model_received = saveOrUpdate(blood_profile_passed);

	
	return model_received;
    }
    
    public UserBloodProfileModel findByUid(UserAccountModel user_account)
    {
	Assert.assertNotNull(user_account, "user_account passed is null, can't proceed");
	
	List<UserBloodProfileModel> account_model_list = findBy("user_account", user_account);
	if(account_model_list.size() > 1)
	    throw new DependencyException("Invalid state, only unique email allowed");
	else if(account_model_list.size() == 0)
	    return null;
	
	return account_model_list.get(0);
    }

}
