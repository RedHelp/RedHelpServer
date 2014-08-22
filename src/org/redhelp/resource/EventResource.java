package org.redhelp.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.catalina.comet.CometEvent.EventType;
import org.apache.log4j.Logger;
import org.redhelp.bo.BloodRequest;
import org.redhelp.bo.Event;
import org.redhelp.common.AddEventRequest;
import org.redhelp.common.AddEventResponse;
import org.redhelp.common.GetBloodRequestResponse;
import org.redhelp.common.GetEventResponse;
import org.redhelp.common.exceptions.DependencyException;
import org.redhelp.common.exceptions.InvalidRequestException;
import org.redhelp.common.types.EventRequestType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;


@Component
@Path("/event")
public class EventResource {
    private Logger logger = Logger.getLogger(EventResource.class);   
    
    @Autowired
    private Event eventBo;
   
   
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public String sayPlainTextHello() {
	logger.info("Inside sayPlainTextHello of UserBloodProfileResource");
	AddEventRequest request = new AddEventRequest();
	request.setB_p_id(14l);
	request.setS_id(1l);
	request.setRequest_type(EventRequestType.ATTEND);
	String response = addEvent(request);
	
	logger.info("addrventResponse :"+response);
	
	return "Hello world! by UserBloodProfileResource";
    }
    
    
    @POST
    @Path("/addEvent")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String addEvent(AddEventRequest addEventRequest) {

	String log_msg_request = String.format("addEvent operation called, AddEventRequest:%s", addEventRequest.toString());
	logger.debug(log_msg_request);

	try {
	    validateAddEventRequest(addEventRequest);
	    
	} catch (InvalidRequestException invalid_request_exception) {
	    String invalid_request_msg = "Invalid request, Exception:";
	    logger.error(invalid_request_msg, invalid_request_exception);
	    throw invalid_request_exception;
	}

	AddEventResponse addEventResponse = new AddEventResponse();
	Gson gson = new Gson();

	try {
	    addEventResponse = eventBo.addUserToEvent(addEventRequest.getB_p_id(), 
		    addEventRequest.getS_id(), addEventRequest.getRequest_type());
	   
	} catch (Exception e) {
	    logger.error("Dependency exception:", e);
	    throw new DependencyException(e.toString());
	}
	
	String saveResponseString = gson.toJson(addEventResponse);
	logger.debug(saveResponseString);
	return saveResponseString;
    }
    
    
    @GET
    @Path("{e_id}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String getEvent(@PathParam("e_id") String e_id) {
	String log_msg_request = String.format("getEvent called, e_id:%s",
		e_id);
	logger.debug(log_msg_request);
	
	GetEventResponse get_event_response = null;
	Long e_id_long = Long.valueOf(e_id);
	
	try {
	    get_event_response =  eventBo.getEvent(e_id_long);
	} catch(Exception e) {
	    logger.info("Invalid state!", e);
	    throw new DependencyException(e.toString());
	}	
	
	if(get_event_response == null) {
	    logger.info("Invalid state!");
	    throw new DependencyException("couldn't fetch get_event_response");
	}
		
	Gson gson = new Gson();
	String json_get_response = gson.toJson(get_event_response);
	return json_get_response;
    }
    private void validateAddEventRequest(AddEventRequest addEventRequest) {
	if (addEventRequest.getB_p_id() == null || addEventRequest.getS_id() == null)
   	    throw new InvalidRequestException("invalid arguments, b_p_id or s_id can't be null");
	
    }

    
    
}