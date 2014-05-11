package org.redhelp.bos;

import org.redhelp.common.exceptions.InvalidRequestException;
import org.redhelp.daos.UserAccountDao;
import org.redhelp.model.UserAccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class UserAccount {

	
	private UserAccountDao user_account_dao;
	private UserAccountModel user_account_model;
	
	private boolean validatePassword(String password_stored, String password_sent) {
		if(password_sent== null || password_stored == null)
			throw new InvalidRequestException("Invalid arguments");
		return password_stored.equals(password_sent);
	}
	
	public UserAccountModel getUserAccountModel() {
		return user_account_model;
	}
	public UserAccount() {
		
	}
	public UserAccount(UserAccountDao user_account_dao, Long u_id) throws Exception {
		this.user_account_dao = user_account_dao;
		user_account_model = user_account_dao.findUser(u_id);
		if(null == user_account_model) {
			String msg = String.format("Unable to retreive UserAccountModel for u_id(%s)", u_id);
			throw new Exception(msg);
		}
	}
	
	public UserAccount(UserAccountModel user_accout_model) 
	{
		this.user_account_model = user_accout_model;
	}
	
	public UserAccount(UserAccountDao user_account_dao, String email) throws Exception 
	{
		user_account_model = user_account_dao.findUser(email);
		if(null == user_account_model) {
			String msg = String.format("Unable to retreive UserAccountModel for u_id(%s)", email);
			throw new Exception(msg);
		}	
	}
	
	public boolean canLoginIn(String password_sent) 
	{
		return validatePassword(user_account_model.getPasswordEncrypted(), password_sent);
	}
}
