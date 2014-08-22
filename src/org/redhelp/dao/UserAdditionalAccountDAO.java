package org.redhelp.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.redhelp.common.exceptions.DependencyException;
import org.redhelp.dbutil.AbstractDAO;
import org.redhelp.model.UserAccountModel;
import org.redhelp.model.UserAdditionalAccountModel;
import org.redhelp.util.Assert;
import org.springframework.stereotype.Component;

@Component
public class UserAdditionalAccountDAO extends AbstractDAO<UserAdditionalAccountModel, Long>{

    private static Logger logger = Logger.getLogger(UserAdditionalAccountDAO.class);
    
    public UserAdditionalAccountModel saveAdditionalAccount(
	    UserAdditionalAccountModel additional_account_passed) {
	
	if(additional_account_passed == null)
	    throw new DependencyException("Invalid call to saveAdditionalAccount, UserAdditionalAccountModel passed is null");
	
	UserAdditionalAccountModel additional_account_saved = create(additional_account_passed);
	
	logger.debug("UserAdditionalAccountModel saved: " + additional_account_saved.toString());	
	return additional_account_saved;
	
    }
    
    
    public UserAdditionalAccountModel findByUid(UserAccountModel user_account)
    {
	Assert.assertNotNull(user_account, "user_account passed is null, can't proceed");
	
	List<UserAdditionalAccountModel> account_model_list = findBy("user_account", user_account);
	if(account_model_list.size() > 1)
	    throw new DependencyException("Invalid state, only unique email allowed");
	else if(account_model_list.size() == 0)
	    return null;
	
	return account_model_list.get(0);
	    
    }
    
}
