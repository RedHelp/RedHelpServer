package org.redhelp.util;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.redhelp.model.EmailModel;
import org.redhelp.types.EmailType;

public class EmailUtil {
    
    private static final String hostName = "smtp.gmail.com";
    private static final int portNumber = 587;
    private static final String senderEmailId = "redhelp.notificaitons@gmail.com";

    public static void sendEmail(String receivers_email_id, String name, EmailType emailType) throws EmailException {
	HtmlEmail email = new HtmlEmail();
	email.setHostName(hostName);
	email.setSmtpPort(portNumber);
	email.setAuthenticator(new DefaultAuthenticator(senderEmailId, JsonSerializer.p));
	email.setSSLOnConnect(true);
	email.setFrom(senderEmailId, "RedHelp");
	EmailModel emailModel = EmailContentHelper.getEmailModel(emailType, name);
	email.setSubject(emailModel.getEmailSubject());
	
	email.setHtmlMsg(emailModel.getEmailContent());
	
	//TODO think about this !!!  set the alternative message
	email.setTextMsg("This is a link");
	
	
	email.addTo(receivers_email_id);
	email.send();
    }
}
