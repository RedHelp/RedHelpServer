package org.redhelp.factories;

import org.redhelp.bos.UserAccount;
import org.redhelp.daos.UserAccountDao;
import org.redhelp.model.UserAccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserAccountFactory {

	@Autowired
	private UserAccountDao userAccountDao;
	
	public UserAccount crateNewUserAccount(UserAccountModel user_account_model) throws Exception
	{	
		Long u_id_returned = (Long) userAccountDao.saveUser(user_account_model);	   
	    return new UserAccount(userAccountDao, u_id_returned);    
	}
	
	public UserAccount findUserAccount(String emailId) throws Exception {
		return new UserAccount(userAccountDao, emailId);
		
	}
	
	public UserAccount editUserAccount(UserAccountModel new_user_account_model) throws Exception
	{
	    userAccountDao.editUserAccount(new_user_account_model);
	    return new UserAccount(userAccountDao, new_user_account_model.getEmail());
	}
}
