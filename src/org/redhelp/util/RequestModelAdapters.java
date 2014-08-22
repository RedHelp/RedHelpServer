package org.redhelp.util;

import java.util.Date;

import org.redhelp.common.GetBloodProfileResponse;
import org.redhelp.common.SaveBloodProfileResponse;
import org.redhelp.common.exceptions.DependencyException;
import org.redhelp.model.UserAccountModel;
import org.redhelp.model.UserAdditionalAccountModel;
import org.redhelp.model.UserAdditionalAccountTypeModel;
import org.redhelp.model.UserBloodProfileModel;

public class RequestModelAdapters {

    public static UserAdditionalAccountModel constructUserAdditionalAccountModel(
	    String externalAccountId, UserAdditionalAccountTypeModel modelType, 
	    UserAccountModel user_account) {
	UserAdditionalAccountModel user_additional_account_model = new UserAdditionalAccountModel();
	user_additional_account_model.setAccountType(modelType);
	user_additional_account_model.setExternalAccountId(externalAccountId);
	user_additional_account_model.setUser_account(user_account);
	
	return user_additional_account_model;
    }
    
    public static  UserAccountModel constructUserAccountModel(String email, String name, String password,
	    	String phone_number,byte[] user_image, UserAccountModel old_model, Date register_date,
	    	Date last_updated_date) {
	UserAccountModel user_account_model = new UserAccountModel();
	
	
	if(null != email)
	    user_account_model.setEmail(email);
	else if(old_model != null)
	    user_account_model.setEmail(old_model.getEmail());
	
	if(null != name)
	    user_account_model.setName(name);
	else if(old_model != null)
	    user_account_model.setName(old_model.getName());
	
	if(null != password)
	    user_account_model.setPasswordEncrypted(password);
	else if(old_model != null)
	    user_account_model.setName(old_model.getName());
	
	if(null != phone_number)
	    user_account_model.setPhoneNo(phone_number);
	else if(old_model != null)
	    user_account_model.setPhoneNo(old_model.getPhoneNo());
	
	if(null != user_image)
	    user_account_model.setUser_image(user_image);
	else if(old_model != null)
	    user_account_model.setUser_image(old_model.getUser_image());
		
	user_account_model.setLastUpdatedDate(last_updated_date);
	user_account_model.setRegisterDate(register_date);
	if(old_model != null)
	{
	    user_account_model.setU_id(old_model.getU_id());
	    user_account_model.setRegisterDate(old_model.getRegisterDate());
	    user_account_model.setLastUpdatedDate(old_model.getLastUpdatedDate());
	}
	return user_account_model;
    }
    
    public static GetBloodProfileResponse constructGetBloodProfileResponse(UserBloodProfileModel blood_profile_model)
    {
	GetBloodProfileResponse response = new GetBloodProfileResponse();
	
	response.setB_p_id(blood_profile_model.getB_p_id());
	response.setBirth_date(blood_profile_model.getBirth_date());
	response.setBlood_group_type(blood_profile_model.getBlood_group_type());
	response.setGender(blood_profile_model.getGender());
	response.setCity(blood_profile_model.getCity());
	response.setLast_known_location_datetime(blood_profile_model.getLast_known_location_datetime());
	response.setLast_known_location_lat(blood_profile_model.getLast_known_location_lat());
	response.setLast_known_location_long(blood_profile_model.getLast_known_location_long());
	
	return response;
    }
    
  
}
