package org.redhelp.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.redhelp.bo.BloodRequest;
import org.redhelp.bo.Search;
import org.redhelp.common.SaveBloodRequestRequest;
import org.redhelp.common.SaveBloodRequestResponse;
import org.redhelp.common.SearchRequest;
import org.redhelp.common.SearchResponse;
import org.redhelp.common.exceptions.DependencyException;
import org.redhelp.common.exceptions.InvalidRequestException;
import org.redhelp.model.BloodRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

@Component
@Path("/search")
public class SearchResource {
    private Logger logger = Logger.getLogger(SearchResource.class);   
    
    @Autowired
    private Search searchBo;
   
   
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String sayPlainTextHello() {
	logger.info("Inside sayPlainTextHello of SearchResource");
	return "Hello world! by SearchResource";
    }
    
    
    @POST
    @Path("/v1")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String search(SearchRequest searchRequest) {
	
	String log_msg_request = String.format("searchRequest operation called, searchRequest:%s", searchRequest.toString());
	logger.debug(log_msg_request);

	try {
	    validateSearchRequest(searchRequest);
	} catch (InvalidRequestException invalid_request_exception) {
	    String invalid_request_msg = "Invalid request, Exception:";
	    logger.error(invalid_request_msg, invalid_request_exception);
	    throw invalid_request_exception;
	}

	SearchResponse searchResponse = null;
	Gson gson = new Gson();

	try {
	    searchResponse = searchBo.search(searchRequest);
	   
	} catch (Exception e) {
	    logger.error("Dependency exception:", e);
	    throw new DependencyException(e.toString());
	}
	
	String searchResponseString = gson.toJson(searchResponse);
	logger.debug(searchResponseString);
	return searchResponseString;
    
    }


    private void validateSearchRequest(SearchRequest searchRequest) {
	// TODO Auto-generated method stub
	
    }
}
