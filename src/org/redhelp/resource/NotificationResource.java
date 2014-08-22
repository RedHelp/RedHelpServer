package org.redhelp.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.redhelp.bo.Event;
import org.redhelp.bo.Notification;
import org.redhelp.common.AddEventRequest;
import org.redhelp.common.EditUserAccountRequest;
import org.redhelp.common.EditUserAccountResponse;
import org.redhelp.common.GetAllNotificationsResponse;
import org.redhelp.common.GetEventResponse;
import org.redhelp.common.GetNewNotificationsResponse;
import org.redhelp.common.MarkNotificaionAsReadRequest;
import org.redhelp.common.MarkNotificationReadResponse;
import org.redhelp.common.exceptions.DependencyException;
import org.redhelp.common.exceptions.InvalidRequestException;
import org.redhelp.common.types.EventRequestType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;


@Component
@Path("/notification")
public class NotificationResource {
    private Logger logger = Logger.getLogger(NotificationResource.class);  
    
    @Autowired
    private Notification notificationBo;
    

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public String sayPlainTextHello() {
	logger.info("Inside sayPlainTextHello of NotificationResource");
	return "Hello world! by NotificationResource";
    }
    
    
    @GET
    @Path("new/{b_p_id}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String getNewNotifications(@PathParam("b_p_id") String b_p_id) {
	String log_msg_request = String.format("getNewNotifications called, b_p_id:%s", b_p_id);
	logger.debug(log_msg_request);
	
	GetNewNotificationsResponse get_new_notifications_response = null;
	Long b_p_id_long = Long.valueOf(b_p_id);
	
	try {
	    get_new_notifications_response =  notificationBo.getNewNotificaitons(b_p_id_long);
	} catch(Exception e) {
	    logger.info("Invalid state!", e);
	    throw new DependencyException(e.toString());
	}	
	
	if(get_new_notifications_response == null) {
	    logger.info("Invalid state!");
	    throw new DependencyException("couldn't fetch get_event_response");
	}
		
	Gson gson = new Gson();
	String json_get_response = gson.toJson(get_new_notifications_response);
	logger.debug("Response "+json_get_response);
	return json_get_response;
    }
    
    @GET
    @Path("all/{b_p_id}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String getAllNotifications(@PathParam("b_p_id") String b_p_id) {
	String log_msg_request = String.format("getAllNotifications called, b_p_id:%s", b_p_id);
	logger.debug(log_msg_request);
	
	GetAllNotificationsResponse get_all_notifications_response = null;
	Long b_p_id_long = Long.valueOf(b_p_id);
	
	try {
	    get_all_notifications_response =  notificationBo.getAllNotificaitons(b_p_id_long);
	} catch(Exception e) {
	    logger.info("Invalid state!", e);
	    throw new DependencyException(e.toString());
	}	
	
	if(get_all_notifications_response == null) {
	    logger.info("Invalid state!");
	    throw new DependencyException("couldn't fetch get_event_response");
	}
		
	Gson gson = new Gson();
	String json_get_response = gson.toJson(get_all_notifications_response);
	logger.debug("Response "+json_get_response);
	return json_get_response;
    }
    
    
    @POST
    @Path("/markAsRead")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String markAsRead(MarkNotificaionAsReadRequest markRequest) {

	String log_msg_request = String.format("markAsRead operation called, markRequest:%s", markRequest.toString());
	logger.debug(log_msg_request);

	MarkNotificationReadResponse markResponse = new MarkNotificationReadResponse();
	Gson gson = new Gson();

	try {
	    markResponse = notificationBo.markAsRead(markRequest);
	} catch (Exception e) {
	    logger.error("Dependency exception:", e);
	    throw new DependencyException(e.toString());
	}
	
	String loginResponseString = gson.toJson(markResponse);
	logger.debug(loginResponseString);
	return loginResponseString;
    }
    
    
    
}

