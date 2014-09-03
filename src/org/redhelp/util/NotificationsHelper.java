package org.redhelp.util;

import org.redhelp.common.types.NotificationTypes;
import org.redhelp.model.NotificationModel;

public class NotificationsHelper {

    public static final String welcomeNotificationTemplate = "Welcome to RedHelp, Happy donating";
    public static final String bloodRequestReceivedNotificationTemplate = "You have received blood request";
    public static final String bloodProfileAccessNotificationTemplate = "Please approve blood request access";
    public static final String bloodProfileAcceptedNotificationTemplate = "Blood profile accesss request accepted";
    public static final String bloodRequestAcceptedNotificationTemplate = "Your blood request is accepted";
    
    public static NotificationModel getWelcomeNotification(Long b_p_id) {
	NotificationModel welcomeNotification = new NotificationModel();
	welcomeNotification.setB_p_id(b_p_id);
	welcomeNotification.setNotification_type(NotificationTypes.MISC_NOTIFICATION);
	String welcomeNotificationTitle = String.format(welcomeNotificationTemplate);
	welcomeNotification.setTitle(welcomeNotificationTitle);
	return welcomeNotification;
    }
    
    public static NotificationModel getBloodRequestReceivedNotification(Long creator_b_p_id,Long receiver_b_p_id, Long b_r_id) {
	NotificationModel bloodRequestReceivedNotification = new NotificationModel();
	bloodRequestReceivedNotification.setB_p_id(receiver_b_p_id);
	bloodRequestReceivedNotification.setNotification_type(NotificationTypes.BLOOD_REQUEST_RECEIVED_NOTIFICATION);
	String bloodRequestReceivedNotificationTitle = String.format(bloodRequestReceivedNotificationTemplate);
	bloodRequestReceivedNotification.setTitle(bloodRequestReceivedNotificationTitle);
	bloodRequestReceivedNotification.setRequester_b_p_id(creator_b_p_id);
	bloodRequestReceivedNotification.setB_r_id(b_r_id);
	return bloodRequestReceivedNotification;
    }
    
    public static NotificationModel getBloodRequestAccptedNotification(Long b_p_id, Long b_r_id, Long acceptor_b_p_id) {
	NotificationModel bloodRequestAcceptedNotification = new NotificationModel();
	bloodRequestAcceptedNotification.setB_p_id(b_p_id);
	bloodRequestAcceptedNotification.setNotification_type(NotificationTypes.BLOOD_REQUEST_ACCEPTED_NOTIFICATION);
	String bloodRequestAcceptedNotificationTitle = String.format(bloodRequestAcceptedNotificationTemplate);
	bloodRequestAcceptedNotification.setTitle(bloodRequestAcceptedNotificationTitle);
	bloodRequestAcceptedNotification.setB_r_id(b_r_id);
	//TODO using requester for requestee field for now.
	bloodRequestAcceptedNotification.setRequester_b_p_id(acceptor_b_p_id);
	return bloodRequestAcceptedNotification;
    }
    
    public static NotificationModel getBloodProfileAccessNotification(Long creator_b_p_id,Long receiver_b_p_id) {
   	NotificationModel bloodProfileAccessNotification = new NotificationModel();
   	bloodProfileAccessNotification.setB_p_id(receiver_b_p_id);
   	bloodProfileAccessNotification.setNotification_type(NotificationTypes.BLOOD_PROFILE_ACCESS_NOTIFICATION);
   	String bloodRequestReceivedNotificationTitle = String.format(bloodProfileAccessNotificationTemplate);
   	bloodProfileAccessNotification.setTitle(bloodRequestReceivedNotificationTitle);
   	bloodProfileAccessNotification.setRequester_b_p_id(creator_b_p_id);
   	return bloodProfileAccessNotification;
       }
    
    public static NotificationModel getBloodProfileAcceptedNotification(Long creator_b_p_id,Long receiver_b_p_id) {
   	NotificationModel bloodProfileAccessNotification = new NotificationModel();
   	bloodProfileAccessNotification.setB_p_id(creator_b_p_id);
   	bloodProfileAccessNotification.setNotification_type(NotificationTypes.BLOOD_PROFILE_ACCEPTED_NOTIFICATION);
   	String bloodRequestReceivedNotificationTitle = String.format(bloodProfileAcceptedNotificationTemplate);
   	bloodProfileAccessNotification.setTitle(bloodRequestReceivedNotificationTitle);
   	//TODO using requester for requestee field for now.
   	bloodProfileAccessNotification.setRequester_b_p_id(receiver_b_p_id);
   	return bloodProfileAccessNotification;
       }
}
