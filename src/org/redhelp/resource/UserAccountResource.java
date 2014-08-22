package org.redhelp.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.redhelp.bo.UserAccount;
import org.redhelp.common.EditUserAccountRequest;
import org.redhelp.common.EditUserAccountResponse;
import org.redhelp.common.GetBloodProfileResponse;
import org.redhelp.common.GetUserAccountResponse;
import org.redhelp.common.LoginRequest;
import org.redhelp.common.LoginResponse;
import org.redhelp.common.RegisterRequest;
import org.redhelp.common.RegisterResponse;
import org.redhelp.common.exceptions.DependencyException;
import org.redhelp.common.exceptions.InvalidRequestException;
import org.redhelp.common.types.RegisterResponseTypes;
import org.redhelp.dao.UserAccountDAO;
import org.redhelp.dao.UserAdditionalAccountDAO;
import org.redhelp.dao.UserAdditionalAccountTypeDAO;
import org.redhelp.model.UserAccountModel;
import org.redhelp.model.UserAdditionalAccountModel;
import org.redhelp.model.UserAdditionalAccountTypeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

@Component
@Path("/userAccount")
public class UserAccountResource {
    private static Logger logger = Logger.getLogger(UserAccountResource.class);

    @Autowired
    private UserAccount userAcountBo;
   
    
    //TODO remove these
    @Autowired
    private UserAdditionalAccountTypeDAO userAdditionalAccountTypeDAO;
    
    @Autowired
    private UserAdditionalAccountDAO userAdditionalAccountDAO;
    
    @Autowired
    private UserAccountDAO userAccountDAO;
    
    @GET
    @Transactional
    @Produces(MediaType.TEXT_HTML)
    public String sayPlainTextHello() {
	System.out.print("inside sayPlainHello:");
	UserAdditionalAccountTypeModel type_model = userAdditionalAccountTypeDAO.findById(0l);
	UserAccountModel account_model = userAccountDAO.findById(25l);
	if(account_model == null)
	    return "account_model is null";
		   
	UserAdditionalAccountModel additional_model = new UserAdditionalAccountModel();
	additional_model.setAccountType(type_model);
	additional_model.setExternalAccountId("123");
	additional_model.setUser_account(account_model);
	
	UserAdditionalAccountModel  received = userAdditionalAccountDAO.create(additional_model);
	return received.toString();
    }
    
    @POST
    @Path("/loginUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String loginUser(LoginRequest loginRequest) {

	String log_msg_request = String.format("login operation called, loginRequest:%s", loginRequest.toString());
	logger.debug(log_msg_request);

	try {
	    validateLoginRequest(loginRequest);
	} catch (InvalidRequestException invalid_request_exception) {
	    String invalid_request_msg = "Invalid request, Exception:";
	    logger.error(invalid_request_msg, invalid_request_exception);
	    throw invalid_request_exception;
	}

	LoginResponse loginResponse = new LoginResponse();
	Gson gson = new Gson();

	try {
	   loginResponse = userAcountBo.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
	} catch (Exception e) {
	    logger.error("Dependency exception:", e);
	    throw new DependencyException(e.toString());
	}
	
	String loginResponseString = gson.toJson(loginResponse);
	logger.debug(loginResponseString);
	return loginResponseString;
    }
    
    
    @POST
    @Path("/editUserAccount")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String editUserAccount(EditUserAccountRequest editRequest) {

	String log_msg_request = String.format("editUserAccount operation called, editRequest:%s", editRequest.toString());
	logger.debug(log_msg_request);

	try {
	    validateEditRequest(editRequest);
	} catch (InvalidRequestException invalid_request_exception) {
	    String invalid_request_msg = "Invalid request, Exception:";
	    logger.error(invalid_request_msg, invalid_request_exception);
	    throw invalid_request_exception;
	}

	EditUserAccountResponse editResponse = new EditUserAccountResponse();
	Gson gson = new Gson();

	try {
	    editResponse = userAcountBo.editUserAccount(editRequest);
	} catch (Exception e) {
	    logger.error("Dependency exception:", e);
	    throw new DependencyException(e.toString());
	}
	
	String loginResponseString = gson.toJson(editResponse);
	logger.debug(loginResponseString);
	return loginResponseString;
    }
    
    private void validateEditRequest(EditUserAccountRequest editRequest) {
	// TODO Auto-generated method stub
	
    }

    @POST
    @Path("/registerUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String register(RegisterRequest registerRequest) {

	String log_msg_request = String.format("Register operation called, registerRequest:%s",
	        registerRequest.toString());
	logger.debug(log_msg_request);

	try {
	    validateRegisterRequest(registerRequest);
	} catch (InvalidRequestException invalid_request_exception) {
	    String invalid_request_msg = "Invalid request, Exception:";
	    logger.error(invalid_request_msg, invalid_request_exception);
	    throw invalid_request_exception;
	}
	
	RegisterResponse regsiterResponse = null;
	Gson gson = new Gson();

	try {    
	    regsiterResponse = userAcountBo.createUserAccount(registerRequest.getEmail(),
		    registerRequest.getName(), registerRequest.getPassword(),
		    registerRequest.getPhoneNo(), registerRequest.getExternalAccountId(),
		    registerRequest.getAdditionalAccountType(), registerRequest.getUser_image());
	    
	} catch(ConstraintViolationException constraint_voilation_exception) {
    	    regsiterResponse = userAcountBo.createUserAdditionalAccount(registerRequest.getEmail(),
    		    registerRequest.getExternalAccountId(), registerRequest.getAdditionalAccountType(),
    		    RegisterResponseTypes.UPDATED_FB, RegisterResponseTypes.DUPLICATE_EMAIL);
	} catch (Exception e) {
	    logger.error("Dependency exception:", e);
	    throw new DependencyException(e.toString());
	}

	String registerResponseString = gson.toJson(regsiterResponse);
	String log_msg_response = String.format("Register response json string: (%s)", registerResponseString);
	logger.debug(log_msg_response);
	
	return registerResponseString;
    }
    
    
    @GET
    @Path("{u_id}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String getUserAccount(@PathParam("u_id") String u_id) {
	String log_msg_request = String.format("getUserAccount called, u_id:%s",
		u_id);
	logger.debug(log_msg_request);
	
	GetUserAccountResponse getUserAccountResponse = null;
	Long u_id_long = Long.valueOf(u_id);
	
	try {
	    getUserAccountResponse = userAcountBo.getUserAccount(u_id_long);
	} catch(Exception e) {
	    logger.info("Invalid state!", e);
	    throw new DependencyException(e.toString());
	}	
	
	if(getUserAccountResponse == null) {
	    logger.info("Invalid state!");
	    throw new DependencyException("couldn't fetch getUserAccountResponse");
	}
	
	Gson gson = new Gson();
	String json_get_response = gson.toJson(getUserAccountResponse);
	logger.debug(json_get_response.toString());
	return json_get_response;
    }
    
    

    private void validateRegisterRequest(RegisterRequest registerRequest) throws InvalidRequestException {
	/*
	 * Currently only phone number is optional argument !
	 */
	if(registerRequest.getExternalAccountId() != null 
		&& registerRequest.getAdditionalAccountType() != null 
		&& registerRequest.getEmail() != null)
	    return;	    
	else if (registerRequest.getEmail() == null || registerRequest.getName() == null
	        || registerRequest.getPassword() == null)
	    throw new InvalidRequestException("Validation exception, invalid arguments");
    }
    
    private void validateLoginRequest(LoginRequest loginRequest) throws InvalidRequestException {
	if (loginRequest.getEmail() == null || loginRequest.getPassword() == null)
	    throw new InvalidRequestException("Validation exception, invalid arguments");
    }

    
    
    

}
