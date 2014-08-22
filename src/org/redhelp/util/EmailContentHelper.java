package org.redhelp.util;

import org.redhelp.model.EmailModel;
import org.redhelp.types.EmailType;

public class EmailContentHelper {
    
    public static EmailModel getEmailModel(EmailType emailType, String name) {
	if(EmailType.WELCOME.equals(emailType)) {
	    return getWelcomeEmail(name);
	} else if(EmailType.BLOOD_REQUEST_RECEIVED.equals(emailType)) {
	    return getBloodRequestReceivedEmail();
	} else if(EmailType.BLOOD_REQUEST_POSTED.equals(emailType)) {
	    return getBloodRequestPostedEmail();
	}
	return null;
    }
    
    public static EmailModel getWelcomeEmail(String name) {
	EmailModel welcomeEmail = new EmailModel();
	
	String welcomeEmailContent = String.format(EmailContentHelper.welcomeEmailContent, name);
	welcomeEmail.setEmailSubject(welcomeEmailSubject);
	welcomeEmail.setEmailContent(welcomeEmailContent);
	
	return welcomeEmail;
    }
    
    public static EmailModel getBloodRequestReceivedEmail() {
	EmailModel bloodRequestReceivedEmail = new EmailModel();
	bloodRequestReceivedEmail.setEmailSubject(bloodRequestReceivedSubject);
	bloodRequestReceivedEmail.setEmailContent(bloodRequestReceivedContent);
	
	return bloodRequestReceivedEmail;
    }
    
    public static EmailModel getBloodRequestPostedEmail() {
  	EmailModel bloodRequestPostedEmail = new EmailModel();
  	bloodRequestPostedEmail.setEmailSubject(bloodRequestPostedSubject);
  	bloodRequestPostedEmail.setEmailContent(bloodRequestPostedContent);
  	
  	return bloodRequestPostedEmail;
      }
    
    public static final String bloodRequestReceivedSubject = "RedHelp, New blood request in your area";
    public static final String bloodRequestReceivedContent = "Placeholder content";
    
    public static final String bloodRequestPostedSubject = "RedHelp, Your blood request has been posted";
    public static final String bloodRequestPostedContent = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" + 
    		"<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns=\"http://www.w3.org/1999/xhtml\" style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6; margin: 0; padding: 0;\">\n" + 
    		"  <head>\n" + 
    		"    <meta name=\"viewport\" content=\"width=device-width\" />\n" + 
    		"    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" + 
    		"    <title>Really Simple HTML Email Template</title>\n" + 
    		"  </head>\n" + 
    		"  <body bgcolor=\"#f6f6f6\" style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6; -webkit-font-smoothing: antialiased; -webkit-text-size-adjust: none; width: 100% !important; height: 100%; margin: 0; padding: 0;\">&#13;\n" + 
    		"&#13;\n" + 
    		"<!-- body -->&#13;\n" + 
    		"<table style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6; width: 100%; margin: 0; padding: 20px;\"><tr style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6; margin: 0; padding: 0;\"><td style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6; margin: 0; padding: 0;\"></td>&#13;\n" + 
    		"		<td bgcolor=\"#FFFFFF\" style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6; display: block !important; max-width: 600px !important; clear: both !important; margin: 0 auto; padding: 20px; border: 1px solid #f0f0f0;\">&#13;\n" + 
    		"&#13;\n" + 
    		"			<!-- content -->&#13;\n" + 
    		"			<div style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6; max-width: 600px; display: block; margin: 0 auto; padding: 0;\">&#13;\n" + 
    		"			<p style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.6; font-weight: normal; margin: 0 0 10px; padding: 0;\"><img src=\"email.png\" style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6; max-width: 100%; margin: 0; padding: 0;\" /></p>&#13;\n" + 
    		"			<table style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6; width: 100%; margin: 0; padding: 0;\"><tr style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6; margin: 0; padding: 0;\"><td style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6; margin: 0; padding: 0;\">&#13;\n" + 
    		"						&#13;\n" + 
    		"						<h2 style=\"font-family: 'Helvetica Neue', Helvetica, Arial, 'Lucida Grande', sans-serif; font-size: 28px; line-height: 1.2; color: #000; font-weight: 200; margin: 40px 0 10px; padding: 0;\">Hi 'Name',</h2>&#13;\n" + 
    		"						<p style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.6; font-weight: normal; margin: 0 0 10px; padding: 0;\">You blood request has been posted</p>&#13;\n" + 
    		"						<p style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.6; font-weight: normal; margin: 0 0 10px; padding: 0;\">we have successfully uploaded you request for 'x' units of blood at 'image hospital'.</p>&#13;\n" + 
    		"						<p style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.6; font-weight: normal; margin: 0 0 10px; padding: 0;\">We understand that this is a tough time for you and time is of essence.</p>&#13;\n" + 
    		"						<p style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.6; font-weight: normal; margin: 0 0 10px; padding: 0;\">You will intimated instantaneously as soon as someone responds to your request. Please do let us know if you are facing any issues at support@redhelp.com</p>&#13;\n" + 
    		"						<p style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.6; font-weight: normal; margin: 0 0 10px; padding: 0;\"></p>&#13;\n" + 
    		"						<p style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.6; font-weight: normal; margin: 0 0 10px; padding: 0;\"></p>&#13;\n" + 
    		"						<p style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.6; font-weight: normal; margin: 0 0 10px; padding: 0;\"></p>&#13;\n" + 
    		"						<p style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.6; font-weight: normal; margin: 0 0 10px; padding: 0;\"><a href=\"https://twitter.com/RedHelpSupport\" style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6; color: #348eda; margin: 0; padding: 0;\">Follow us  @redhelpsupport on Twitter</a></p>&#13;\n" + 
    		"						<p style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.6; font-weight: normal; margin: 0 0 10px; padding: 0;\"><a href=\"https://www.facebook.com/redhelpsupport\" style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6; color: #348eda; margin: 0; padding: 0;\">Follow us on Facebook</a></p>&#13;\n" + 
    		"					</td>&#13;\n" + 
    		"				</tr></table></div>&#13;\n" + 
    		"			<!-- /content -->&#13;\n" + 
    		"&#13;\n" + 
    		"		</td>&#13;\n" + 
    		"		<td style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6; margin: 0; padding: 0;\"></td>&#13;\n" + 
    		"	</tr></table><!-- /body --><!-- footer --><table style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6; width: 100%; clear: both !important; margin: 0; padding: 0;\"><tr style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6; margin: 0; padding: 0;\"><td style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6; margin: 0; padding: 0;\"></td>&#13;\n" + 
    		"		<td style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6; display: block !important; max-width: 600px !important; clear: both !important; margin: 0 auto; padding: 0;\">&#13;\n" + 
    		"&#13;\n" + 
    		"			<!-- content -->&#13;\n" + 
    		"			<div style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6; max-width: 600px; display: block; margin: 0 auto; padding: 0;\">&#13;\n" + 
    		"				<table style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6; width: 100%; margin: 0; padding: 0;\"><tr style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6; margin: 0; padding: 0;\"></tr></table></div>&#13;\n" + 
    		"			<!-- /content -->&#13;\n" + 
    		"&#13;\n" + 
    		"		</td>&#13;\n" + 
    		"		<td style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%; line-height: 1.6; margin: 0; padding: 0;\"></td>&#13;\n" + 
    		"	</tr></table><!-- /footer --></body>\n" + 
    		"</html>\n";
    
    
    public static final String welcomeEmailSubject = "RedHelp, Welcome aboard";
    public static final String welcomeEmailContent = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" + 
    		"<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns=\"http://www.w3.org/1999/xhtml\" style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%%%%; line-height: 1.6; margin: 0; padding: 0;\">\n" + 
    		"  <head>\n" + 
    		"    <meta name=\"viewport\" content=\"width=device-width\" />\n" + 
    		"    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" + 
    		"    <title>Really Simple HTML Email Template</title>\n" + 
    		"    <style>\n" + 
    		"<![CDATA[\n" + 
    		"/* -------------------------------------\n" + 
    		"		GLOBAL\n" + 
    		"------------------------------------- */\n" + 
    		"* {\n" + 
    		"	margin: 0;\n" + 
    		"	padding: 0;\n" + 
    		"	font-family: \"Helvetica Neue\", \"Helvetica\", Helvetica, Arial, sans-serif;\n" + 
    		"	font-size: 100%%%%;\n" + 
    		"	line-height: 1.6;\n" + 
    		"}\n" + 
    		"\n" + 
    		"img {\n" + 
    		"	max-width: 100%%%%;\n" + 
    		"}\n" + 
    		"\n" + 
    		"body {\n" + 
    		"	-webkit-font-smoothing: antialiased;\n" + 
    		"	-webkit-text-size-adjust: none;\n" + 
    		"	width: 100%%%%!important;\n" + 
    		"	height: 100%%%%;\n" + 
    		"}\n" + 
    		"\n" + 
    		"\n" + 
    		"/* -------------------------------------\n" + 
    		"		ELEMENTS\n" + 
    		"------------------------------------- */\n" + 
    		"a {\n" + 
    		"	color: #348eda;\n" + 
    		"}\n" + 
    		"\n" + 
    		".btn-primary {\n" + 
    		"	text-decoration: none;\n" + 
    		"	color: #FFF;\n" + 
    		"	background-color: #348eda;\n" + 
    		"	border: solid #348eda;\n" + 
    		"	border-width: 10px 20px;\n" + 
    		"	line-height: 2;\n" + 
    		"	font-weight: bold;\n" + 
    		"	margin-right: 10px;\n" + 
    		"	text-align: center;\n" + 
    		"	cursor: pointer;\n" + 
    		"	display: inline-block;\n" + 
    		"	border-radius: 25px;\n" + 
    		"}\n" + 
    		"\n" + 
    		".btn-secondary {\n" + 
    		"	text-decoration: none;\n" + 
    		"	color: #FFF;\n" + 
    		"	background-color: #aaa;\n" + 
    		"	border: solid #aaa;\n" + 
    		"	border-width: 10px 20px;\n" + 
    		"	line-height: 2;\n" + 
    		"	font-weight: bold;\n" + 
    		"	margin-right: 10px;\n" + 
    		"	text-align: center;\n" + 
    		"	cursor: pointer;\n" + 
    		"	display: inline-block;\n" + 
    		"	border-radius: 25px;\n" + 
    		"}\n" + 
    		"\n" + 
    		".last {\n" + 
    		"	margin-bottom: 0;\n" + 
    		"}\n" + 
    		"\n" + 
    		".first {\n" + 
    		"	margin-top: 0;\n" + 
    		"}\n" + 
    		"\n" + 
    		".padding {\n" + 
    		"	padding: 10px 0;\n" + 
    		"}\n" + 
    		"\n" + 
    		"\n" + 
    		"/* -------------------------------------\n" + 
    		"		BODY\n" + 
    		"------------------------------------- */\n" + 
    		"table.body-wrap {\n" + 
    		"	width: 100%%%%;\n" + 
    		"	padding: 20px;\n" + 
    		"}\n" + 
    		"\n" + 
    		"table.body-wrap .container {\n" + 
    		"	border: 1px solid #f0f0f0;\n" + 
    		"}\n" + 
    		"\n" + 
    		"\n" + 
    		"/* -------------------------------------\n" + 
    		"		FOOTER\n" + 
    		"------------------------------------- */\n" + 
    		"table.footer-wrap {\n" + 
    		"	width: 100%%%%;	\n" + 
    		"	clear: both!important;\n" + 
    		"}\n" + 
    		"\n" + 
    		".footer-wrap .container p {\n" + 
    		"	font-size: 12px;\n" + 
    		"	color: #666;\n" + 
    		"\n" + 
    		"}\n" + 
    		"\n" + 
    		"table.footer-wrap a {\n" + 
    		"	color: #999;\n" + 
    		"}\n" + 
    		"\n" + 
    		"\n" + 
    		"/* -------------------------------------\n" + 
    		"		TYPOGRAPHY\n" + 
    		"------------------------------------- */\n" + 
    		"h1, h2, h3 {\n" + 
    		"	font-family: \"Helvetica Neue\", Helvetica, Arial, \"Lucida Grande\", sans-serif;\n" + 
    		"	line-height: 1.1;\n" + 
    		"	margin-bottom: 15px;\n" + 
    		"	color: #000;\n" + 
    		"	margin: 40px 0 10px;\n" + 
    		"	line-height: 1.2;\n" + 
    		"	font-weight: 200;\n" + 
    		"}\n" + 
    		"\n" + 
    		"h1 {\n" + 
    		"	font-size: 36px;\n" + 
    		"}\n" + 
    		"h2 {\n" + 
    		"	font-size: 28px;\n" + 
    		"}\n" + 
    		"h3 {\n" + 
    		"	font-size: 22px;\n" + 
    		"}\n" + 
    		"\n" + 
    		"p, ul, ol {\n" + 
    		"	margin-bottom: 10px;\n" + 
    		"	font-weight: normal;\n" + 
    		"	font-size: 14px;\n" + 
    		"}\n" + 
    		"\n" + 
    		"ul li, ol li {\n" + 
    		"	margin-left: 5px;\n" + 
    		"	list-style-position: inside;\n" + 
    		"}\n" + 
    		"\n" + 
    		"/* ---------------------------------------------------\n" + 
    		"		RESPONSIVENESS\n" + 
    		"		Nuke it from orbit. It's the only way to be sure.\n" + 
    		"------------------------------------------------------ */\n" + 
    		"\n" + 
    		"/* Set a max-width, and make it display as block so it will automatically stretch to that width, but will also shrink down on a phone or something */\n" + 
    		".container {\n" + 
    		"	display: block!important;\n" + 
    		"	max-width: 600px!important;\n" + 
    		"	margin: 0 auto!important; /* makes it centered */\n" + 
    		"	clear: both!important;\n" + 
    		"}\n" + 
    		"\n" + 
    		"/* Set the padding on the td rather than the div for Outlook compatibility */\n" + 
    		".body-wrap .container {\n" + 
    		"	padding: 20px;\n" + 
    		"}\n" + 
    		"\n" + 
    		"/* This should also be a block element, so that it will fill 100%%%% of the .container */\n" + 
    		".content {\n" + 
    		"	max-width: 600px;\n" + 
    		"	margin: 0 auto;\n" + 
    		"	display: block;\n" + 
    		"}\n" + 
    		"\n" + 
    		"/* Let's make sure tables in the content area are 100%%%% wide */\n" + 
    		".content table {\n" + 
    		"	width: 100%%%%;\n" + 
    		"}\n" + 
    		"\n" + 
    		"]]>\n" + 
    		"    </style>\n" + 
    		"  </head>\n" + 
    		"  <body bgcolor=\"#f6f6f6\" style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%%%%; line-height: 1.6; -webkit-font-smoothing: antialiased; -webkit-text-size-adjust: none; width: 100%%%% !important; height: 100%%%%; margin: 0; padding: 0;\">&#13;\n" + 
    		"&#13;\n" + 
    		"<!-- body -->&#13;\n" + 
    		"<table style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%%%%; line-height: 1.6; width: 100%%%%; margin: 0; padding: 20px;\"><tr style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%%%%; line-height: 1.6; margin: 0; padding: 0;\"><td style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%%; line-height: 1.6; margin: 0; padding: 0;\"></td>&#13;\n" + 
    		"		<td bgcolor=\"#FFFFFF\" style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%%; line-height: 1.6; display: block !important; max-width: 600px !important; clear: both !important; margin: 0 auto; padding: 20px; border: 1px solid #f0f0f0;\">&#13;\n" + 
    		"&#13;\n" + 
    		"			<!-- content -->&#13;\n" + 
    		"			<div style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%%; line-height: 1.6; max-width: 600px; display: block; margin: 0 auto; padding: 0;\">&#13;\n" + 
    		"			<p style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.6; font-weight: normal; margin: 0 0 10px; padding: 0;\"><img src=\"email.png\" style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%%; line-height: 1.6; max-width: 100%%; margin: 0; padding: 0;\" /></p>&#13;\n" + 
    		"			<table style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%%; line-height: 1.6; width: 100%%; margin: 0; padding: 0;\"><tr style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%%; line-height: 1.6; margin: 0; padding: 0;\"><td style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%%; line-height: 1.6; margin: 0; padding: 0;\">&#13;\n" + 
    		"						&#13;\n" + 
    		"						<h2 style=\"font-family: 'Helvetica Neue', Helvetica, Arial, 'Lucida Grande', sans-serif; font-size: 28px; line-height: 1.2; color: #000; font-weight: 200; margin: 40px 0 10px; padding: 0;\">Hi '%s',</h2>&#13;\n" + 
    		"						<p style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.6; font-weight: normal; margin: 0 0 10px; padding: 0;\">Welcome to the world's first mobile blood services App.</p>&#13;\n" + 
    		"						<p style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.6; font-weight: normal; margin: 0 0 10px; padding: 0;\">We welcome you to our growing community of users who share our dream of a better world.</p>&#13;\n" + 
    		"						<p style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.6; font-weight: normal; margin: 0 0 10px; padding: 0;\">With us, you will be able to seek blood donations for your loved ones and also help others in their need of hour. You can also look out for nearby blood donation camps and help volunteer for these events.</p>&#13;\n" + 
    		"						<p style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.6; font-weight: normal; margin: 0 0 10px; padding: 0;\">For feedback, concerns, or any general inquiry, please reach out to us at support@redhelp.com</p>&#13;\n" + 
    		"						<p style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.6; font-weight: normal; margin: 0 0 10px; padding: 0;\"></p>&#13;\n" + 
    		"						<p style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.6; font-weight: normal; margin: 0 0 10px; padding: 0;\"></p>&#13;\n" + 
    		"						<p style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.6; font-weight: normal; margin: 0 0 10px; padding: 0;\"></p>&#13;\n" + 
    		"						<p style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.6; font-weight: normal; margin: 0 0 10px; padding: 0;\"><a href=\"https://twitter.com/RedHelpSupport\" style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%%; line-height: 1.6; color: #348eda; margin: 0; padding: 0;\">Follow us  @redhelpsupport on Twitter</a></p>&#13;\n" + 
    		"						<p style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 1.6; font-weight: normal; margin: 0 0 10px; padding: 0;\"><a href=\"https://www.facebook.com/redhelpsupport\" style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%%; line-height: 1.6; color: #348eda; margin: 0; padding: 0;\">Follow us on Facebook</a></p>&#13;\n" + 
    		"					</td>&#13;\n" + 
    		"				</tr></table></div>&#13;\n" + 
    		"			<!-- /content -->&#13;\n" + 
    		"&#13;\n" + 
    		"		</td>&#13;\n" + 
    		"		<td style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%%; line-height: 1.6; margin: 0; padding: 0;\"></td>&#13;\n" + 
    		"	</tr></table><!-- /body --><!-- footer --><table style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%%; line-height: 1.6; width: 100%%; clear: both !important; margin: 0; padding: 0;\"><tr style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%%; line-height: 1.6; margin: 0; padding: 0;\"><td style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%%; line-height: 1.6; margin: 0; padding: 0;\"></td>&#13;\n" + 
    		"		<td style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%%; line-height: 1.6; display: block !important; max-width: 600px !important; clear: both !important; margin: 0 auto; padding: 0;\">&#13;\n" + 
    		"&#13;\n" + 
    		"			<!-- content -->&#13;\n" + 
    		"			<div style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%%; line-height: 1.6; max-width: 600px; display: block; margin: 0 auto; padding: 0;\">&#13;\n" + 
    		"				<table style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%%; line-height: 1.6; width: 100%%; margin: 0; padding: 0;\"><tr style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%%; line-height: 1.6; margin: 0; padding: 0;\"></tr></table></div>&#13;\n" + 
    		"			<!-- /content -->&#13;\n" + 
    		"&#13;\n" + 
    		"		</td>&#13;\n" + 
    		"		<td style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 100%%; line-height: 1.6; margin: 0; padding: 0;\"></td>&#13;\n" + 
    		"	</tr></table><!-- /footer --></body>\n" + 
    		"</html>\n";
}
