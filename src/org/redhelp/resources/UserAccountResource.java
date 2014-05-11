package org.redhelp.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.redhelp.bos.UserAccount;
import org.redhelp.common.EditUserAccountRequest;
import org.redhelp.common.EditUserAccountResponse;
import org.redhelp.common.LoginRequest;
import org.redhelp.common.LoginResponse;
import org.redhelp.common.RegisterRequest;
import org.redhelp.common.RegisterResponse;
import org.redhelp.common.exceptions.DependencyException;
import org.redhelp.common.exceptions.InvalidRequestException;
import org.redhelp.common.types.EditUserResponseTypes;
import org.redhelp.common.types.LoginReponseTypes;
import org.redhelp.common.types.RegisterResponseTypes;
import org.redhelp.daos.UserAccountDao;
import org.redhelp.factories.UserAccountFactory;
import org.redhelp.model.UserAccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
@Path("/userAccount")
public class UserAccountResource {

    private static Logger logger = Logger.getLogger(UserAccountResource.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private UserAccountFactory userAccountFactory;

    @Autowired
    UserAccountDao userAccountDaoAutowired;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String sayPlainTextHello() {
	System.out.print("inside sayPlainHello:");
	if (sessionFactory == null)
	    System.out.println("sessionFactory is null");
	if (userAccountDaoAutowired == null)
	    System.out.print("userAccountDaoAutowired is null :(");
	ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
	UserAccountDao tempDao = (UserAccountDao) ctx.getBean("userAccountDao");

	/*
	 * UserAccountModel tempAccount = null; try { tempAccount =
	 * tempDao.findUser("dream_girl@gmail.com"); } catch (Exception e) { //
	 * TODO Auto-generated catch block
	 * System.out.println("exception:"+e.toString()); e.printStackTrace(); }
	 * System.out.println("tempAccount:"+tempAccount.toString());
	 * tempAccount.setName("HarshitSYal2");
	 * tempAccount.setPasswordEncrypted("new_password_2");
	 * System.out.println("tempAccount2:"+tempAccount.toString()); try {
	 * tempDao.editUserAccount(tempAccount); } catch (Exception e) {
	 * System.out.println("Excpetion:"+e.toString()); // TODO Auto-generated
	 * catch block e.printStackTrace(); }
	 * 
	 * try { tempAccount = tempDao.findUser("dream_girl@gmail.com"); } catch
	 * (Exception e) { // TODO Auto-generated catch block
	 * System.out.println("exception:"+e.toString()); e.printStackTrace(); }
	 * return tempAccount.toString();
	 */
	return null;

    }

    @POST
    @Path("/editUserAccount")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String editUserAccount(EditUserAccountRequest editUserAccountRequest) {

	String log_msg_request = String.format("Edit operation called, editUserAccountRequest:%s",
	        editUserAccountRequest.toString());
	logger.debug(log_msg_request);

	UserAccount user_account = null;
	EditUserAccountResponse editUserAccountResponse = new EditUserAccountResponse();
	Gson gson = new Gson();
	try {
	    user_account = userAccountFactory.findUserAccount(editUserAccountRequest.getEmail());
	}  catch (InvalidRequestException invalid_request_excpetion) {
	    logger.info("invalidRequest", invalid_request_excpetion);
	    editUserAccountResponse.setEditUserResponseType(EditUserResponseTypes.DUPLICATE_EMAIL);
	    return gson.toJson(editUserAccountResponse);
	} catch (Exception e) {
	    logger.error("Dependency exception:", e);
	    throw new DependencyException(e.toString());
	}

	UserAccountModel user_account_model = constructUserAccountModel(editUserAccountRequest.getEmail(),
	        editUserAccountRequest.getName(), editUserAccountRequest.getPassword(),
	        editUserAccountRequest.getPhoneNo());
	
	try {
	    user_account = userAccountFactory.editUserAccount(user_account_model);
        } catch (ConstraintViolationException constraint_voilation_exception) {
	    editUserAccountResponse.setEditUserResponseType(EditUserResponseTypes.DUPLICATE_EMAIL);
	    String editUserAccountResponseString = gson.toJson(editUserAccountResponse);

	    String log_msg_response = String.format("Register response json string: (%s)",
		    editUserAccountResponseString);
	    logger.debug(log_msg_response);
	    return editUserAccountResponseString;
	} catch (Exception e) {
	    logger.error("Dependency exception:", e);
	    throw new DependencyException(e.toString());
        }

	if (user_account.getUserAccountModel() == null)
	    throw new DependencyException("Invalid state, couldn't get user details");

	editUserAccountResponse.setU_id(user_account.getUserAccountModel().getU_id());
	editUserAccountResponse.setEditUserResponseType(EditUserResponseTypes.SUCCESSFUL);

	String registerResponseString = gson.toJson(editUserAccountResponse);

	String log_msg_response = String.format("Register response json string: (%s)", registerResponseString);
	logger.debug(log_msg_response);

	return registerResponseString;
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

	UserAccount user_account = null;
	LoginResponse loginResponse = new LoginResponse();
	Gson gson = new Gson();

	try {
	    user_account = userAccountFactory.findUserAccount(loginRequest.getEmail());
	} catch (InvalidRequestException invalid_request_excpetion) {
	    logger.info("invalidRequest", invalid_request_excpetion);
	    loginResponse.setLoginResponseType(LoginReponseTypes.WRONG_EMAIL_ID);
	    return gson.toJson(loginResponse);
	} catch (Exception e) {
	    logger.error("Dependency exception:", e);
	    throw new DependencyException(e.toString());
	}
	boolean match = user_account.canLoginIn(loginRequest.getPassword());

	if (match == true)
	    loginResponse.setLoginResponseType(LoginReponseTypes.LOGIN_SUCESSFULL);
	else
	    loginResponse.setLoginResponseType(LoginReponseTypes.WRONG_PASSWORD);

	String loginResponseString = gson.toJson(loginResponse);
	return loginResponseString;
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

	UserAccountModel user_account_model = constructUserAccountModel(registerRequest.getEmail(),
	        registerRequest.getName(), registerRequest.getPassword(), registerRequest.getPhoneNo());
	UserAccount user_account = null;
	RegisterResponse regsiterResponse = new RegisterResponse();
	Gson gson = new Gson();

	try {
	    user_account = userAccountFactory.crateNewUserAccount(user_account_model);
	} catch (ConstraintViolationException constraint_voilation_exception) {
	    regsiterResponse.setRegisterResponseType(RegisterResponseTypes.DUPLICATE_EMAIL);
	    String registerResponseString = gson.toJson(regsiterResponse);
	    String log_msg_response = String.format("Register response json string: (%s)", registerResponseString);
	    logger.debug(log_msg_response);
	    return registerResponseString;
	} catch (Exception e) {
	    logger.error("Dependency exception:", e);
	    throw new DependencyException(e.toString());
	}

	if (user_account.getUserAccountModel() == null)
	    throw new DependencyException("Invalid state, couldn't get user details");

	regsiterResponse.setU_id(user_account.getUserAccountModel().getU_id());
	regsiterResponse.setRegisterResponseType(RegisterResponseTypes.SUCCESSFUL);

	String registerResponseString = gson.toJson(regsiterResponse);

	String log_msg_response = String.format("Register response json string: (%s)", registerResponseString);
	logger.debug(log_msg_response);

	return registerResponseString;

    }

    private void validateLoginRequest(LoginRequest loginRequest) throws InvalidRequestException {
	if (loginRequest.getEmail() == null || loginRequest.getPassword() == null)
	    throw new InvalidRequestException("Validation exception, invalid arguments");
    }

    private void validateRegisterRequest(RegisterRequest registerRequest) throws InvalidRequestException {
	/*
	 * Currently only phone number is optional argument !
	 */
	if (registerRequest.getEmail() == null || registerRequest.getName() == null
	        || registerRequest.getPassword() == null)
	    throw new InvalidRequestException("Validation exception, invalid arguments");
    }

    private UserAccountModel constructUserAccountModel(String email, String name, String password, String phone_number) {
	UserAccountModel user_account_model = new UserAccountModel();

	user_account_model.setEmail(email);
	user_account_model.setName(name);
	user_account_model.setPasswordEncrypted(password);
	user_account_model.setPhoneNo(phone_number);

	return user_account_model;
    }

}
