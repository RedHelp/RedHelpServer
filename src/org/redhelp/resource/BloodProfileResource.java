package org.redhelp.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.redhelp.bo.BloodProfile;
import org.redhelp.common.AddEventRequest;
import org.redhelp.common.AddEventResponse;
import org.redhelp.common.GetBloodProfileAccessRequest;
import org.redhelp.common.GetBloodProfileAccessResponse;
import org.redhelp.common.GetBloodProfileAccessResponseRequest;
import org.redhelp.common.GetBloodProfileAccessResponseResponse;
import org.redhelp.common.GetBloodProfileRequest;
import org.redhelp.common.GetBloodProfileResponse;
import org.redhelp.common.SaveBloodProfileRequest;
import org.redhelp.common.SaveBloodProfileResponse;
import org.redhelp.common.exceptions.DependencyException;
import org.redhelp.common.exceptions.InvalidRequestException;
import org.redhelp.model.UserBloodProfileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
@Path("/bloodProfile")
public class BloodProfileResource {
    private Logger logger = Logger.getLogger(BloodProfileResource.class);
    
    @Autowired
    BloodProfile bloodProfileBo;
    
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String sayPlainTextHello() {
	logger.info("Inside sayPlainTextHello of UserBloodProfileResource");
	
	UserBloodProfileModel model = bloodProfileBo.getBloodProfileModel(15l);
	if(model!=null)
	    logger.info(model.toString());
	
	return "Hello world! by UserBloodProfileResource";
    }

    @POST
    @Path("/saveProfile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String saveProfile(SaveBloodProfileRequest saveRequest) {

	String log_msg_request = String.format("login operation called, SaveBloodProfileRequest:%s", saveRequest.toString());
	logger.debug(log_msg_request);

	try {
	    validateSaveBloodProfileRequest(saveRequest);
	} catch (InvalidRequestException invalid_request_exception) {
	    String invalid_request_msg = "Invalid request, Exception:";
	    logger.error(invalid_request_msg, invalid_request_exception);
	    throw invalid_request_exception;
	}

	SaveBloodProfileResponse saveBpResponse = new SaveBloodProfileResponse();
	Gson gson = new Gson();

	try {
	    saveBpResponse = bloodProfileBo.saveBloodProfile(saveRequest.getU_id(), saveRequest.getB_p_id(),
		    saveRequest.getGender(), saveRequest.getCity(),
		    saveRequest.getBlood_group_type(), saveRequest.getLast_known_location_lat(),
		    saveRequest.getLast_known_location_long(), saveRequest.getBirth_date());
	   
	} catch (Exception e) {
	    logger.error("Dependency exception:", e);
	    throw new DependencyException(e.toString());
	}
	
	String saveResponseString = gson.toJson(saveBpResponse);
	logger.debug(saveResponseString);
	return saveResponseString;
    }
    
    @POST
    @Path("/getBloodProfile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String getBloodProfile(GetBloodProfileRequest profileRequest) {

	String log_msg_request = String.format("getBloodProfile operation called, GetBloodProfileRequest:%s", profileRequest.toString());
	logger.debug(log_msg_request);

	try {
	    validateGetBloodProfileRequest(profileRequest);
	} catch (InvalidRequestException invalid_request_exception) {
	    String invalid_request_msg = "Invalid request, Exception:";
	    logger.error(invalid_request_msg, invalid_request_exception);
	    throw invalid_request_exception;
	}

	GetBloodProfileResponse profileResponse = new GetBloodProfileResponse();
	Gson gson = new Gson();

	try {
	    profileResponse = bloodProfileBo.getBloodProfile(profileRequest);	   
	} catch (Exception e) {
	    logger.error("Dependency exception:", e);
	    throw new DependencyException(e.toString());
	}
	
	String saveResponseString = gson.toJson(profileResponse);
	logger.debug(saveResponseString);
	return saveResponseString;
    }
    
    private void validateGetBloodProfileRequest(GetBloodProfileRequest profileRequest) {
	if (profileRequest.getRequester_b_p_id() == null || profileRequest.getB_p_id() == null)
   	    throw new InvalidRequestException("getRequester_b_p_id or getB_p_id can't be null");
    }

    @POST
    @Path("/requestAccess")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String requestAccess(GetBloodProfileAccessRequest accessRequest) {

	String log_msg_request = String.format("requestAccess operation called, GetBloodProfileAccessRequest:%s", accessRequest.toString());
	logger.debug(log_msg_request);

	try {
	    validateAccessBloodProfileRequest(accessRequest);
	} catch (InvalidRequestException invalid_request_exception) {
	    String invalid_request_msg = "Invalid request, Exception:";
	    logger.error(invalid_request_msg, invalid_request_exception);
	    throw invalid_request_exception;
	}

	GetBloodProfileAccessResponse accessResponse = new GetBloodProfileAccessResponse();
	Gson gson = new Gson();

	try {
	    accessResponse = bloodProfileBo.accessRequest(accessRequest);
	   
	} catch (Exception e) {
	    logger.error("Dependency exception:", e);
	    throw new DependencyException(e.toString());
	}
	
	String saveResponseString = gson.toJson(accessResponse);
	logger.debug(saveResponseString);
	return saveResponseString;
    }
    
    @POST
    @Path("/respondAccess")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String respondAccess(GetBloodProfileAccessResponseRequest respondRequest) {

	String log_msg_request = String.format("respondAccess operation called, GetBloodProfileAccessResponseRequest:%s", respondRequest.toString());
	logger.debug(log_msg_request);

	try {
	    validateRespondRequest(respondRequest);
	} catch (InvalidRequestException invalid_request_exception) {
	    String invalid_request_msg = "Invalid request, Exception:";
	    logger.error(invalid_request_msg, invalid_request_exception);
	    throw invalid_request_exception;
	}

	GetBloodProfileAccessResponseResponse respondResponse = new GetBloodProfileAccessResponseResponse();
	Gson gson = new Gson();

	try {
	    respondResponse = bloodProfileBo.accessRequest(respondRequest);
	} catch (Exception e) {
	    logger.error("Dependency exception:", e);
	    throw new DependencyException(e.toString());
	}
	
	String saveResponseString = gson.toJson(respondResponse);
	logger.debug(saveResponseString);
	return saveResponseString;
    }
    
    private void validateRespondRequest(GetBloodProfileAccessResponseRequest respondRequest) {
	if (respondRequest.getRequestee_b_p_id() == null || respondRequest.getRequester_b_p_id() == null)
   	    throw new InvalidRequestException("getRequestee_b_p_id or getRequester_b_p_id can't be null");
    }

    private void validateAccessBloodProfileRequest(GetBloodProfileAccessRequest accessRequest) {
	if (accessRequest.getReceiver_b_p_id() == null || accessRequest.getRequester_b_p_id() == null)
   	    throw new InvalidRequestException("getReceiver_b_p_id or getRequester_b_p_id can't be null");
    }

    @GET
    @Path("{b_p_id}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String getBloodProfile(@PathParam("b_p_id") String b_p_id) {
	String log_msg_request = String.format("getBloodProfile called, b_p_id:%s",
		b_p_id);
	logger.debug(log_msg_request);
	
	GetBloodProfileResponse get_blood_profile_response = null;
	Long b_p_id_long = Long.valueOf(b_p_id);
	
	try {
	    get_blood_profile_response = bloodProfileBo.getBloodProfile(b_p_id_long);
	} catch(Exception e) {
	    logger.info("Invalid state!", e);
	    throw new DependencyException(e.toString());
	}	
	
	if(get_blood_profile_response == null) {
	    logger.info("Invalid state!");
	    throw new DependencyException("couldn't fetch blood_profile");
	}
		
	Gson gson = new Gson();
	String json_get_response = gson.toJson(get_blood_profile_response);
	
	return json_get_response;
    }
    
    @GET
    @Path("getViaUid/{u_id}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String getBloodProfileViaUid(@PathParam("u_id") String u_id) {
	String log_msg_request = String.format("getBloodProfileViaUid called, u_id:%s",
		u_id);
	logger.debug(log_msg_request);
	
	GetBloodProfileResponse get_blood_profile_response = null;
	Long u_id_long = Long.valueOf(u_id);
	
	try {
	    get_blood_profile_response = bloodProfileBo.getBloodProfileViaUid(u_id_long);
	} catch(Exception e) {
	    logger.info("Invalid state!", e);
	    throw new DependencyException(e.toString());
	}	
	
	if(get_blood_profile_response == null) {
	    logger.info("Invalid state!");
	    throw new DependencyException("couldn't fetch blood_profile");
	}
	
	Gson gson = new Gson();
	String json_get_response = gson.toJson(get_blood_profile_response);
	
	return json_get_response;
    }
    
    
    
    @GET
    @Path("/other/{b_p_id}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String getOthersBloodProfile(@PathParam("b_p_id") String b_p_id) {
	String log_msg_request = String.format("getBloodProfile called, b_p_id:%s",
		b_p_id);
	logger.debug(log_msg_request);
	
	GetBloodProfileResponse get_blood_profile_response = null;
	Long b_p_id_long = Long.valueOf(b_p_id);
	try {
	    get_blood_profile_response = bloodProfileBo.getBloodProfile(b_p_id_long);
	} catch(Exception e) {
	    logger.info("Invalid state!", e);
	    throw new DependencyException(e.toString());
	}	
	if(get_blood_profile_response == null)
	{
	    logger.info("Invalid state!");
	    throw new DependencyException("couldn't fetch blood_profile");
	}
		
	Gson gson = new Gson();
	String json_get_response = gson.toJson(get_blood_profile_response);
	
	return json_get_response;
    }
    
    private void validateSaveBloodProfileRequest(SaveBloodProfileRequest blood_profile_request) {
   	if (blood_profile_request.getU_id() == null)
   	    throw new InvalidRequestException("u_id can't be null");
    }

}
