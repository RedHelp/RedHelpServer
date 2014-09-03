package org.redhelp.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.redhelp.dbutil.AbstractDAO;
import org.redhelp.model.UsersRelationshipModel;
import org.redhelp.util.Assert;
import org.springframework.stereotype.Repository;

@Repository
public class UserRelationshipDAO extends AbstractDAO<UsersRelationshipModel, Long> {
    
    private Logger logger = Logger.getLogger(UserRelationshipDAO.class);
    
    public List<UsersRelationshipModel> findByBpid(Long b_p_id)
    {
	Assert.assertNotNull(b_p_id, "b_p_id passed is null, can't proceed");
	
	List<UsersRelationshipModel> relationship_list = findBy("b_p_id", b_p_id);
	if(relationship_list.size() == 0)
	    return null;
	
	return relationship_list;
    }
    
    public UsersRelationshipModel findFirstByBpid(Long b_p_id)
    {
	Assert.assertNotNull(b_p_id, "b_p_id passed is null, can't proceed");
	
	List<UsersRelationshipModel> relationship_list = findBy("b_p_id", b_p_id);
	if(relationship_list.size() == 0)
	    return null;
	
	return relationship_list.get(0);
    }
    
}
