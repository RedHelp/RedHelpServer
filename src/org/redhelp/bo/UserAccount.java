package org.redhelp.bo;

import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.redhelp.common.EditUserAccountRequest;
import org.redhelp.common.EditUserAccountResponse;
import org.redhelp.common.GetBloodProfileResponse;
import org.redhelp.common.GetUserAccountResponse;
import org.redhelp.common.LoginResponse;
import org.redhelp.common.RegisterResponse;
import org.redhelp.common.UserProfileCommonFields;
import org.redhelp.common.exceptions.InvalidRequestException;
import org.redhelp.common.types.EditBloodProfileResponseType;
import org.redhelp.common.types.EditUserResponseTypes;
import org.redhelp.common.types.LoginReponseTypes;
import org.redhelp.common.types.RegisterResponseTypes;
import org.redhelp.dao.UserAccountDAO;
import org.redhelp.dao.UserAdditionalAccountDAO;
import org.redhelp.dao.UserAdditionalAccountTypeDAO;
import org.redhelp.model.NotificationModel;
import org.redhelp.model.UserAccountModel;
import org.redhelp.model.UserAdditionalAccountModel;
import org.redhelp.model.UserAdditionalAccountTypeModel;
import org.redhelp.model.UserBloodProfileModel;
import org.redhelp.types.EmailType;
import org.redhelp.util.Assert;
import org.redhelp.util.BCrypt;
import org.redhelp.util.EmailSender;
import org.redhelp.util.RequestModelAdapters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAccount {
    private Logger logger = Logger.getLogger(UserAccount.class);

    @Autowired
    private UserAccountDAO userAccountDao;

    @Autowired
    private UserAdditionalAccountDAO userAdditionalAccountDAO;

    @Autowired
    private UserAdditionalAccountTypeDAO userAdditionalAccountTypeDAO;
    
    @Autowired
    private BloodProfile bloodProfileBo;

    @Transactional
    public UserAccountModel createUserAccount(UserAccountModel user_account_passed) {
	UserAccountModel user_account_received = userAccountDao.create(user_account_passed);
	logger.info("user account saved: " + user_account_received.toString());
	return user_account_received;
    }

    @Transactional
    public RegisterResponse createUserAdditionalAccount(String email, String external_account_id,
	    Long external_account_type, RegisterResponseTypes responseTypeFb, RegisterResponseTypes responseNormal) {
	
	RegisterResponse registerResponse = new RegisterResponse();
	UserAccountModel user_account_received = userAccountDao.findByEmail(email);
	UserAdditionalAccountModel additional_account = null;
	
	if (user_account_received != null)
	    additional_account = userAdditionalAccountDAO.findByUid(user_account_received);
	
	if (additional_account != null &&  external_account_id != null && external_account_id.equals(additional_account.getExternalAccountId())) {
	    
	    registerResponse.setRegisterResponseType(RegisterResponseTypes.UPDATED_FB);
	    registerResponse.setU_id(user_account_received.getU_id());
	    registerResponse.setUaa_id(additional_account.getUaa_id());
	    return registerResponse;
	
	} else if (external_account_id != null && external_account_type == 0l) {
	 
	    UserAdditionalAccountTypeModel additionalAccountType = userAdditionalAccountTypeDAO
		    .findById(external_account_type);
	    Assert.assertNotNull(additionalAccountType, "additionalAccountType couldn't not be fetched for id:"
		    + external_account_id);

	    UserAdditionalAccountModel additional_account_passed = RequestModelAdapters
		    .constructUserAdditionalAccountModel(external_account_id, additionalAccountType,
		            user_account_received);

	    UserAdditionalAccountModel additional_account_received = userAdditionalAccountDAO
		    .saveAdditionalAccount(additional_account_passed);
	    registerResponse.setRegisterResponseType(responseTypeFb);
	    registerResponse.setU_id(user_account_received.getU_id());
	    registerResponse.setUaa_id(additional_account_received.getUaa_id());
	    return registerResponse;
	
	} else {
	  
	    registerResponse.setRegisterResponseType(responseNormal);
	    registerResponse.setU_id(user_account_received.getU_id());
	    return registerResponse;
	
	}
    }
    
    /*
     * Called when user registers.
     */
    @Transactional
    public RegisterResponse createUserAccount(String email, String name, String password, String phone_number,
	    String external_account_id, Long external_account_type, byte[] user_image) {
	
	// Hash password
	String hash_password = BCrypt.hashpw(password, BCrypt.gensalt());

	// Create user account model
	UserAccountModel user_account_passed = RequestModelAdapters.constructUserAccountModel(email, name, hash_password,
	        phone_number, user_image, null, new Date(), new Date());
	UserAccountModel user_account_received = null;

	// Persist user account model.
	user_account_received = userAccountDao.create(user_account_passed);
	Assert.assertNotNull(user_account_received, "user Account could not be saved");
	RegisterResponse registerResponse = createUserAdditionalAccount(email, external_account_id,
	        external_account_type, RegisterResponseTypes.SUCCESSFUL_FB_NEW, 
	        RegisterResponseTypes.SUCCESSFUL_NEW);
	
	// Send email 
	String email_id = user_account_received.getEmail();
	String user_name = user_account_received.getName();
	
	EmailSender emailTask = new EmailSender(email_id, user_name, EmailType.WELCOME);
	Thread worker = new Thread(emailTask);
	worker.start();
	
	
	return registerResponse;
    }
    
    
    @Transactional
    public LoginResponse loginUser(String email, String password) {
	LoginResponse response = new LoginResponse();
	UserAccountModel user_account_received = userAccountDao.findByEmail(email);
	
	if(user_account_received == null) 
	    response.setLoginResponseType(LoginReponseTypes.WRONG_EMAIL_ID);
	else if (BCrypt.checkpw(password, user_account_received.getPasswordEncrypted())){
	    response.setLoginResponseType(LoginReponseTypes.LOGIN_SUCESSFULL);
	    UserAccountModel user_account = userAccountDao.findByEmail(email);
	    if(user_account != null) {
		response.setEmail(user_account.getEmail());
		response.setName(user_account.getName());
		response.setU_id(user_account.getU_id());
		response.setPhone_number(user_account.getPhoneNo());
	    } else {
		response.setLoginResponseType(LoginReponseTypes.WRONG_EMAIL_ID);
	    }
		
	    UserAdditionalAccountModel additional_model = userAdditionalAccountDAO.findByUid(user_account);
	    if(additional_model != null) {
		response.setUaa_id(additional_model.getUaa_id());
	    }
	    
	    UserBloodProfileModel blood_profile = bloodProfileBo.getBloodProfileModelViaUserAccount(user_account);
	    if(blood_profile != null) {
		response.setB_p_id(blood_profile.getB_p_id());
	    }
	    
	    
	} else
	    response.setLoginResponseType(LoginReponseTypes.WRONG_PASSWORD);
	
	return response;
    }

    @Transactional
    public GetUserAccountResponse getUserAccount(Long u_id) {
	UserAccountModel model_received = userAccountDao.findById(u_id);
	GetUserAccountResponse response = new GetUserAccountResponse();
	if(model_received != null)
	{ 
	    response = createGetBloodProfileResponse(model_received); 
	}
	else {
	    logger.info("model_received is null");
	    throw new InvalidRequestException("can't find model for b_p_id"+u_id);
	}
	logger.info(model_received.toString());
	logger.info("response: "+response.toString());
	return response;
    }
    
    @Transactional
    public EditUserAccountResponse editUserAccount(EditUserAccountRequest editRequest) {
	LoginResponse response = new LoginResponse();
	UserAccountModel user_account_received = userAccountDao.findById(editRequest.getU_id());
	
	if(user_account_received == null) 
	    throw new InvalidRequestException("user_account doesn't exist for u_id:"+editRequest.getU_id());
	
	if(editRequest.getEmail() != null)
	    user_account_received.setEmail(editRequest.getEmail());
	if(editRequest.getName() != null)
	    user_account_received.setName(editRequest.getName());
	if(editRequest.getPhoneNo() != null)
	    user_account_received.setPhoneNo(editRequest.getPhoneNo());
	
	
	UserAccountModel updated_user_account = userAccountDao.update(user_account_received);
	
	EditUserAccountResponse editResponse = new EditUserAccountResponse();
	editResponse.setEditUserResponseType(EditUserResponseTypes.SUCCESSFUL);
	editResponse.setU_id(updated_user_account.getU_id());
	return editResponse;
	
    }

    private GetUserAccountResponse createGetBloodProfileResponse(UserAccountModel model_received) {
	GetUserAccountResponse response = new GetUserAccountResponse();
	UserProfileCommonFields commonFields = new UserProfileCommonFields();
	commonFields.setEmail(model_received.getEmail());
	commonFields.setName(model_received.getName());
	commonFields.setPhone_number(model_received.getPhoneNo());
	commonFields.setUser_image(model_received.getUser_image());
	response.setUserProfileCommonFields(commonFields);
	return response;
    }
    

}
