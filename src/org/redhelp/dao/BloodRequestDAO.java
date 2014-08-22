package org.redhelp.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.redhelp.bo.BloodRequest;
import org.redhelp.common.types.Location;
import org.redhelp.dbutil.AbstractDAO;
import org.redhelp.model.BloodRequestModel;
import org.springframework.stereotype.Repository;

@Repository
public class BloodRequestDAO extends AbstractDAO<BloodRequestModel, Long>{

    private Logger logger = Logger.getLogger(BloodRequestDAO.class);
    
    public List<BloodRequestModel> searchViaRange(Location southWestLocation, Location northEastLocation) {

	Criteria criteria = getSession().createCriteria(getPersistentClass());
	criteria.add(Restrictions.lt("gps_location_lat", northEastLocation.latitude));
	criteria.add(Restrictions.lt("gps_location_long",northEastLocation.longitude));
	
	criteria.add(Restrictions.gt("gps_location_lat", southWestLocation.latitude));
	criteria.add(Restrictions.gt("gps_location_long", southWestLocation.longitude));
	criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	
	if(criteria.list()!= null)
	    logger.error(criteria.list().toString());
	return criteria.list();	
    }
    
}
