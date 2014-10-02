package org.redhelp.util;

import org.redhelp.common.types.NotificationTypes;
import org.redhelp.model.NotificationModel;

public class NotificationsHelper {

    public static final String welcomeNotificationTemplate = "Welcome to RedHelp, Happy donating";
    public static final String bloodRequestReceivedNotificationTemplate = "You have received blood request from %s";
    public static final String bloodProfileAccessNotificationTemplate = "Blood requested by %s";
    public static final String bloodProfileAcceptedNotificationTemplate = "Blood profile accesss request accepted by %s";
    public static final String bloodRequestAcceptedNotificationTemplate = "Your blood request is accepted by %s";
    public static final String bloodRequestExpiredNotificationTemplate = "Your blood request is expired";
    public static final String bloodRequestExpiredExtendedNotificationTemplate = "Your extended blood request has expired, please create a new request";

    
    
    public static NotificationModel getWelcomeNotification(Long b_p_id) {
	NotificationModel welcomeNotification = new NotificationModel();
	welcomeNotification.setB_p_id(b_p_id);
	welcomeNotification.setNotification_type(NotificationTypes.MISC_NOTIFICATION);
	String welcomeNotificationTitle = String.format(welcomeNotificationTemplate);
	welcomeNotification.setTitle(welcomeNotificationTitle);
	return welcomeNotification;
    }
    
    public static NotificationModel getBloodRequestReceivedNotification(Long creator_b_p_id,Long receiver_b_p_id,
	    Long b_r_id, String creator_name) {
	NotificationModel bloodRequestReceivedNotification = new NotificationModel();
	bloodRequestReceivedNotification.setB_p_id(receiver_b_p_id);
	bloodRequestReceivedNotification.setNotification_type(NotificationTypes.BLOOD_REQUEST_RECEIVED_NOTIFICATION);
	String bloodRequestReceivedNotificationTitle = String.format(bloodRequestReceivedNotificationTemplate, creator_name);
	bloodRequestReceivedNotification.setTitle(bloodRequestReceivedNotificationTitle);
	bloodRequestReceivedNotification.setRequester_b_p_id(creator_b_p_id);
	bloodRequestReceivedNotification.setB_r_id(b_r_id);
	return bloodRequestReceivedNotification;
    }
    
    public static NotificationModel getBloodRequestAccptedNotification(Long b_p_id, Long b_r_id,
	    Long acceptor_b_p_id, String acceptor_name) {
	NotificationModel bloodRequestAcceptedNotification = new NotificationModel();
	bloodRequestAcceptedNotification.setB_p_id(b_p_id);
	bloodRequestAcceptedNotification.setNotification_type(NotificationTypes.BLOOD_REQUEST_ACCEPTED_NOTIFICATION);
	String bloodRequestAcceptedNotificationTitle = String.format(bloodRequestAcceptedNotificationTemplate, acceptor_name);
	bloodRequestAcceptedNotification.setTitle(bloodRequestAcceptedNotificationTitle);
	bloodRequestAcceptedNotification.setB_r_id(b_r_id);
	//TODO using requester for requestee field for now.
	bloodRequestAcceptedNotification.setRequester_b_p_id(acceptor_b_p_id);
	return bloodRequestAcceptedNotification;
    }
    
    
    public static NotificationModel getBloodRequestExpiredNotification(Long b_p_id, Long b_r_id) {
	NotificationModel bloodRequestExpiredNotification = new NotificationModel();
	bloodRequestExpiredNotification.setB_p_id(b_p_id);
	bloodRequestExpiredNotification.setNotification_type(NotificationTypes.BLOOD_REQUEST_ACCEPTED_NOTIFICATION);
	String bloodRequestExpiredNotificationTitle = String.format(bloodRequestExpiredNotificationTemplate);
	bloodRequestExpiredNotification.setTitle(bloodRequestExpiredNotificationTitle);
	bloodRequestExpiredNotification.setB_r_id(b_r_id);
	return bloodRequestExpiredNotification;
    }
    
    public static NotificationModel getBloodRequestExtendedExpiredNotification(Long b_p_id, Long b_r_id) {
   	NotificationModel bloodRequestExtendedExpiredNotification = new NotificationModel();
   	bloodRequestExtendedExpiredNotification.setB_p_id(b_p_id);
   	bloodRequestExtendedExpiredNotification.setNotification_type(NotificationTypes.BLOOD_REQUEST_ACCEPTED_NOTIFICATION);
   	String bloodRequestExpiredNotificationTitle = String.format(bloodRequestExpiredExtendedNotificationTemplate);
   	bloodRequestExtendedExpiredNotification.setTitle(bloodRequestExpiredNotificationTitle);
   	bloodRequestExtendedExpiredNotification.setB_r_id(b_r_id);
   	return bloodRequestExtendedExpiredNotification;
    }
    
    public static NotificationModel getBloodProfileAccessNotification(Long creator_b_p_id,Long receiver_b_p_id,
	    String creator_name) {
   	NotificationModel bloodProfileAccessNotification = new NotificationModel();
   	bloodProfileAccessNotification.setB_p_id(receiver_b_p_id);
   	bloodProfileAccessNotification.setNotification_type(NotificationTypes.BLOOD_PROFILE_ACCESS_NOTIFICATION);
   	String bloodRequestReceivedNotificationTitle = String.format(bloodProfileAccessNotificationTemplate, creator_name);
   	bloodProfileAccessNotification.setTitle(bloodRequestReceivedNotificationTitle);
   	bloodProfileAccessNotification.setRequester_b_p_id(creator_b_p_id);
   	return bloodProfileAccessNotification;
       }
    
    public static NotificationModel getBloodProfileAcceptedNotification(Long creator_b_p_id, Long receiver_b_p_id,
	    String acceptor_name) {
   	NotificationModel bloodProfileAccessNotification = new NotificationModel();
   	bloodProfileAccessNotification.setB_p_id(creator_b_p_id);
   	bloodProfileAccessNotification.setNotification_type(NotificationTypes.BLOOD_PROFILE_ACCEPTED_NOTIFICATION);
   	String bloodRequestReceivedNotificationTitle = String.format(bloodProfileAcceptedNotificationTemplate, acceptor_name);
   	bloodProfileAccessNotification.setTitle(bloodRequestReceivedNotificationTitle);
   	//TODO using requester for requestee field for now.
   	bloodProfileAccessNotification.setRequester_b_p_id(receiver_b_p_id);
   	return bloodProfileAccessNotification;
       }
}
