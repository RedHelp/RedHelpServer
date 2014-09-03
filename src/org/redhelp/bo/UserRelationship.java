package org.redhelp.bo;

import java.util.List;

import org.redhelp.dao.UserRelationshipDAO;
import org.redhelp.model.RequestUserRelationshipModel;
import org.redhelp.model.UsersRelationshipModel;
import org.redhelp.types.RequestUserRelationshipType;
import org.redhelp.types.UserRelationshipType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRelationship {
    
    @Autowired
    UserRelationshipDAO userRealtionshipDAO;
    
    public UsersRelationshipModel updateRelationship(Long primary_b_p_id, Long secondary_b_p_id, UserRelationshipType relationshipType) {
	UsersRelationshipModel relationshipModel  = getRelationship(primary_b_p_id, secondary_b_p_id);
	if(relationshipModel == null) {
	    relationshipModel = new UsersRelationshipModel();
	    relationshipModel.setB_p_id(primary_b_p_id);
	    relationshipModel.setRelated_b_p_id(secondary_b_p_id);
	} 
	
	relationshipModel.setUserRelationshipType(relationshipType);
	UsersRelationshipModel modelReturned = userRealtionshipDAO.saveOrUpdate(relationshipModel);
	
	return modelReturned;
    }
    
    public UsersRelationshipModel getRelationship(Long primary_b_p_id, Long secondary_b_p_id) {
   	List<UsersRelationshipModel> relationships = userRealtionshipDAO.findByBpid(primary_b_p_id);
   	
   	if(relationships != null){
   	    for(UsersRelationshipModel relationship : relationships) {
   		if(relationship.getB_p_id().equals(secondary_b_p_id)) {
   		    return relationship;
   		}
   	    }
   	}
   	
   	return null;
   	
       }

}
