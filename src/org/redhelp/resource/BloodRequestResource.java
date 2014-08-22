package org.redhelp.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.redhelp.bo.BloodRequest;
import org.redhelp.common.GetBloodRequestResponse;
import org.redhelp.common.SaveBloodRequestRequest;
import org.redhelp.common.SaveBloodRequestResponse;
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
    
    @GET
    @Path("{b_r_id}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String getBloodRequest(@PathParam("b_r_id") String b_r_id) {
	String log_msg_request = String.format("getBloodRequest called, b_r_id:%s",
		b_r_id);
	logger.debug(log_msg_request);
	
	GetBloodRequestResponse get_blood_request_response = null;
	Long b_r_id_long = Long.valueOf(b_r_id);
	
	try {
	    get_blood_request_response =  bloodRequestBo.getBloodRequest(b_r_id_long);
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
