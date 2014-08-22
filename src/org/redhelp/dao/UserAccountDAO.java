package org.redhelp.dao;

import java.util.Date;
import java.util.List;

import org.redhelp.common.exceptions.DependencyException;
import org.redhelp.dbutil.AbstractDAO;
import org.redhelp.model.UserAccountModel;
import org.redhelp.util.Assert;
import org.springframework.stereotype.Repository;


@Repository
public class UserAccountDAO extends AbstractDAO<UserAccountModel, Long> 
{

    public UserAccountModel saveUserAccount(UserAccountModel user_account_passed)
    {
	if(user_account_passed == null)
	    throw new DependencyException("Invalid call to saveUserAccount, UserAccountModel passed is null");

	user_account_passed.setRegisterDate(new Date());
	user_account_passed.setLastUpdatedDate(new Date());
	UserAccountModel user_account_created = create(user_account_passed);
	//System.out.println(user_account_created.toString());
	return user_account_created;
    }
    
    public UserAccountModel findByEmail(String email)
    {
	Assert.assertNotNull(email, "Email passed is null, can't proceed");
	
	List<UserAccountModel> account_model_list = findBy("email", email);
	if(account_model_list.size() > 1)
	    throw new DependencyException("Invalid state, only unique email allowed");
	else if(account_model_list.size() == 0)
	    return null;
	
	return account_model_list.get(0);
	    
    }
}
