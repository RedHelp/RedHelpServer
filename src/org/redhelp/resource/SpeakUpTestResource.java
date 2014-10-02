package org.redhelp.resource;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.redhelp.model.EmailModel;
import org.springframework.stereotype.Component;

@Component
@Path("/speakuptest")
public class SpeakUpTestResource {
    
    @GET
    @Path("gettestdata")
    @Produces( MediaType.APPLICATION_JSON)
    public List<EmailModel> getBloodProfile(@DefaultValue("10") @QueryParam("limit") Integer limit) {
	List<EmailModel> emailModelList = new LinkedList<EmailModel>();
	EmailModel emailModel1 = new EmailModel();
	emailModel1.setEmailContent("email1");
	emailModel1.setEmailSubject("EmailSubject1");
	
	EmailModel emailModel2 = new EmailModel();
	emailModel2.setEmailContent("email2");
	emailModel2.setEmailSubject("EmailSubject2");
	
	EmailModel emailModel3 = new EmailModel();
	emailModel3.setEmailContent("email3");
	emailModel2.setEmailSubject("EmailSubject3");
	
	EmailModel emailModel4 = new EmailModel();
	emailModel3.setEmailContent("email4");
	emailModel3.setEmailSubject("EmailSubject4");
	
	emailModelList.add(emailModel1);
	emailModelList.add(emailModel2);
	emailModelList.add(emailModel3);
	emailModelList.add(emailModel4);
	
	
	
	return emailModelList;
    }

}
