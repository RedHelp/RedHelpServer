package org.redhelp.bo;

import java.util.List;

import org.apache.log4j.Logger;
import org.redhelp.dao.RequestUserRelationshipDAO;
import org.redhelp.model.RequestUserRelationshipModel;
import org.redhelp.types.RequestUserRelationshipType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RequestUserRelationship {
    
    private Logger logger = Logger.getLogger(RequestUserRelationship.class);
    
    @Autowired
    RequestUserRelationshipDAO requestUserRelationshipDAO;
    
    
    public RequestUserRelationshipType getRelationShipType(Long b_r_id, Long b_p_id) {
	List<RequestUserRelationshipModel> relationships = requestUserRelationshipDAO.findByBrid(b_r_id);
	
	if(relationships != null){
	    for(RequestUserRelationshipModel relationship : relationships) {
		if(relationship.getB_p_id().equals(b_p_id)) {
		    return relationship.getRequestUserRelationshipType();
		}
	    }
	}
	
	return null;
	
    }

}
