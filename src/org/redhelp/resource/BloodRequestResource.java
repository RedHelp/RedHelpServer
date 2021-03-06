package org.redhelp.resource;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.redhelp.bo.BloodRequest;
import org.redhelp.common.AcceptBloodRequestRequest;
import org.redhelp.common.AcceptBloodRequestResponse;
import org.redhelp.common.GetBloodRequestResponse;
import org.redhelp.common.SaveBloodRequestRequest;
import org.redhelp.common.SaveBloodRequestResponse;
import org.redhelp.common.UpdateBloodRequest;
import org.redhelp.common.UpdateBloodRequestResponse;
import org.redhelp.common.exceptions.DependencyException;
import org.redhelp.common.exceptions.InvalidRequestException;
import org.redhelp.common.types.JodaTimeFormatters;
import org.redhelp.helpers.DateTimeHelper;
import org.redhelp.model.BloodGroupsModel;
import org.redhelp.model.BloodRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

@Component
@Path("/bloodRequest")
public class BloodRequestResource {
    private Logger logger = Logger.getLogger(BloodRequestResource.class);   
    
    @Autowired
    private BloodRequest bloodRequestBo;
   
   
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public String sayPlainTextHello() {
	logger.info("Inside sayPlainTextHello of UserBloodProfileResource");
	return "Hello world! by UserBloodProfileResource";
    }
    
    
    @GET
    @Path("dailyBloodRequestUpdateJob")
    @Produces(MediaType.TEXT_PLAIN)
    public String dailyBloodRequestUpdateJob() {
	String log_msg_request = String.format("dailyBloodRequestUpdateJob called");	
	logger.debug(log_msg_request);
	
	String receivers = null;
	//List<BloodRequestModel> receivers = new LinkedList<BloodRequestModel>();
	try {
	    receivers =  bloodRequestBo.dailyUpdateJob();
	} catch(Exception e) {
	    logger.info("Invalid state!", e);
	    throw new DependencyException(e.toString());
	}	
	
	String return_msg = String.format("Blood request update for ids:%s", receivers.toString());
	return return_msg;
    }
    
    
    
    
    @POST
    @Path("/saveBloodRequest")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String saveBloodRequest(SaveBloodRequestRequest saveBloodRequest) {
	
	String log_msg_request = String.format("saveBloodRequest operation called, SaveBloodRequestRequest:%s", saveBloodRequest.toString());
	logger.debug(log_msg_request);

	try {
	    validateSaveBloodRequest(saveBloodRequest);
	} catch (InvalidRequestException invalid_request_exception) {
	    String invalid_request_msg = "Invalid request, Exception:";
	    logger.error(invalid_request_msg, invalid_request_exception);
	    throw invalid_request_exception;
	}

	SaveBloodRequestResponse saveRequestResponse = null;
	Gson gson = new Gson();

	try {
	    BloodRequestModel model_received = bloodRequestBo.createBloodRequest(saveBloodRequest);
	    saveRequestResponse = saveBloodRequestResponseFromBloodRequestModel(model_received);
	   
	} catch (Exception e) {
	    logger.error("Dependency exception:", e);
	    throw new DependencyException(e.toString());
	}
	
	String saveResponseString = gson.toJson(saveRequestResponse);
	logger.debug(saveResponseString);
	return saveResponseString;
    }
    
    @POST
    @Path("/acceptBloodRequest")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String acceptBloodRequest(AcceptBloodRequestRequest acceptRequest) {
	
	String log_msg_request = String.format("acceptBloodRequest operation called, AcceptBloodRequestRequest:%s", acceptRequest.toString());
	logger.debug(log_msg_request);

	try {
	    validateAcceptBloodRequest(acceptRequest);
	} catch (InvalidRequestException invalid_request_exception) {
	    String invalid_request_msg = "Invalid request, Exception:";
	    logger.error(invalid_request_msg, invalid_request_exception);
	    throw invalid_request_exception;
	}

	AcceptBloodRequestResponse acceptResponse = null;
	Gson gson = new Gson();

	try {
	    acceptResponse = bloodRequestBo.acceptBloodRequest(acceptRequest);
	} catch (Exception e) {
	    logger.error("Dependency exception:", e);
	    throw new DependencyException(e.toString());
	}
	
	String responseString = gson.toJson(acceptResponse);
	logger.debug(responseString);
	return responseString;
    
    }
    
    
    @POST
    @Path("/updateBloodRequest")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String updateBloodRequest(UpdateBloodRequest request) {
	
	String log_msg_request = String.format("updateBloodRequest operation called," +
			" UpdateBloodRequest:%s", request.toString());
	logger.debug(log_msg_request);

	try {
	    validateUpdateBloodRequest(request);
	} catch (InvalidRequestException invalid_request_exception) {
	    String invalid_request_msg = "Invalid request, Exception:";
	    logger.error(invalid_request_msg, invalid_request_exception);
	    throw invalid_request_exception;
	}

	UpdateBloodRequestResponse updateResponse = null;
	Gson gson = new Gson();

	try {
	    updateResponse = bloodRequestBo.updateBloodRequest(request);
	} catch (Exception e) {
	    logger.error("Dependency exception:", e);
	    throw new DependencyException(e.toString());
	}
	
	String responseString = gson.toJson(updateResponse);
	logger.debug(responseString);
	return responseString;
    
    }
    
    
    
    private void validateUpdateBloodRequest(UpdateBloodRequest request) {
	if (request.getB_r_id() == null)
   	    throw new InvalidRequestException("B_r_id can't be null");
    }


    private void validateAcceptBloodRequest(AcceptBloodRequestRequest acceptRequest) {
	if (acceptRequest.getB_p_id() == null || acceptRequest.getB_r_id() == null)
   	    throw new InvalidRequestException("B_p_id or b_r_id can't be null");
    }


    @GET
    @Path("{b_r_id}/{b_p_id}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String getBloodRequest(@PathParam("b_r_id") String b_r_id, @PathParam("b_p_id") String b_p_id ) {
	String log_msg_request = String.format("getBloodRequestOperation called, b_r_id:%s, b_p_id:%s",
		b_r_id, b_p_id);	
	logger.debug(log_msg_request);
	long startProfiler = System.nanoTime();
	
	GetBloodRequestResponse get_blood_request_response = null;
	Long b_r_id_long = Long.valueOf(b_r_id);
	Long b_p_id_long = Long.valueOf(b_p_id);
	
	try {
	    get_blood_request_response =  bloodRequestBo.getBloodRequest(b_r_id_long, b_p_id_long);
	} catch(Exception e) {
	    logger.info("Invalid state!", e);
	    throw new DependencyException(e.toString());
	}	
	
	if(get_blood_request_response == null) {
	    logger.info("Invalid state!");
	    throw new DependencyException("couldn't fetch blood_profile");
	}
		
	Gson gson = new Gson();
	String json_get_response = gson.toJson(get_blood_request_response);

	long elapsedTime = System.nanoTime() - startProfiler;
	elapsedTime = elapsedTime/1000000000;
	double elapsedTimeSec = (double)elapsedTime/1000000000.0;
	logger.debug(json_get_response);
	logger.info("Time taken by getBloodRequestOperation:" + elapsedTime);
	
	return json_get_response;
    }
    
    
    private void validateSaveBloodRequest(SaveBloodRequestRequest blood_request) {
   	if (blood_request.getB_p_id() == null)
   	    throw new InvalidRequestException("B_p_id can't be null");
    }
    private SaveBloodRequestResponse saveBloodRequestResponseFromBloodRequestModel(BloodRequestModel model)
    {
	SaveBloodRequestResponse response = new SaveBloodRequestResponse();
	response.setActive(model.isActive());
	response.setB_p_id(model.getB_p_id());
	response.setB_r_id(model.getB_r_id());
	String creationDateTime = DateTimeHelper.convertJavaDateToString(
		model.getCreation_datetime(), JodaTimeFormatters.dateTimeFormatter);
	response.setCreation_datetime(creationDateTime);
	
	response.setDescription(model.getDescription());
	response.setPatient_name(model.getPatient_name());
	response.setPhone_number(model.getPhone_number());
	response.setList_blood_group(BloodGroupsModel.getListBloodGroups(model.getSet_blood_group()));
	response.setBlood_requirement_type(model.getBlood_requirement_type());
	response.setUnits(model.getUnits());
	
	response.setGps_location_lat(model.getGps_location_lat());
	response.setGps_location_long(model.getGps_location_long());
	response.setPlace_location_lat(model.getPlace_location_lat());
	response.setPlace_location_long(model.getPlace_location_long());
	response.setPlace_string(model.getPlace_string());
	return response;
    }
    
   
}
