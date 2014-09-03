package org.redhelp.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.redhelp.dbutil.AbstractDAO;
import org.redhelp.model.RequestUserRelationshipModel;
import org.redhelp.util.Assert;
import org.springframework.stereotype.Repository;

@Repository
public class RequestUserRelationshipDAO extends AbstractDAO<RequestUserRelationshipModel, Long> {
    
    private Logger logger = Logger.getLogger(RequestUserRelationshipDAO.class);
    
    public List<RequestUserRelationshipModel> findByBpid(Long b_p_id)
    {
	Assert.assertNotNull(b_p_id, "b_p_id passed is null, can't proceed");
	
	List<RequestUserRelationshipModel> relationship_list = findBy("b_p_id", b_p_id);
	if(relationship_list.size() == 0)
	    return null;
	
	return relationship_list;
    }
    
    public List<RequestUserRelationshipModel> findByBrid(Long b_r_id)
    {
	Assert.assertNotNull(b_r_id, "b_r_id passed is null, can't proceed");
	
	List<RequestUserRelationshipModel> relationship_list = findBy("b_r_id", b_r_id);
	if(relationship_list.size() == 0)
	    return null;
	
	return relationship_list;
    }
    
    public RequestUserRelationshipModel findFirstByBpid(Long b_p_id)
    {
	Assert.assertNotNull(b_p_id, "b_p_id passed is null, can't proceed");
	
	List<RequestUserRelationshipModel> relationship_list = findBy("b_p_id", b_p_id);
	if(relationship_list.size() == 0)
	    return null;
	
	return relationship_list.get(0);
    }
    
    public RequestUserRelationshipModel find(Long b_r_id, Long b_p_id)
    {
	Assert.assertNotNull(b_r_id, "b_r_id passed is null, can't proceed");
	Assert.assertNotNull(b_p_id, "b_p_id passed is null, can't proceed");
	
	List<RequestUserRelationshipModel> relationship_list = findBy("b_r_id", b_r_id);
	if(relationship_list.size() == 0)
	    return null;
	else {
	    for(RequestUserRelationshipModel relationship : relationship_list) {
		if(b_p_id.equals(relationship.getB_p_id()))
		    return relationship;
	    }
	    
	}
	return null;
    }
    
}
