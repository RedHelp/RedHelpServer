package org.redhelp.util;

import org.redhelp.common.types.NotificationTypes;
import org.redhelp.model.NotificationModel;

public class NotificationsHelper {

    public static final String welcomeNotificationTemplate = "Welcome to RedHelp, Happy donating";
    public static final String bloodRequestReceivedNotificationTemplate = "You have received blood request";
    

    public static NotificationModel getWelcomeNotification(Long b_p_id) {
	NotificationModel welcomeNotification = new NotificationModel();
	welcomeNotification.setB_p_id(b_p_id);
	welcomeNotification.setNotification_type(NotificationTypes.MISC_NOTIFICATION);
	String welcomeNotificationTitle = String.format(welcomeNotificationTemplate);
	welcomeNotification.setTitle(welcomeNotificationTitle);
	return welcomeNotification;
    }
    
    public static NotificationModel getBloodRequestReceivedNotification(Long creator_b_p_id,Long receiver_b_r_id) {
	NotificationModel bloodRequestReceivedNotification = new NotificationModel();
	bloodRequestReceivedNotification.setB_p_id(receiver_b_r_id);
	bloodRequestReceivedNotification.setNotification_type(NotificationTypes.BLOOD_REQUEST_RECEIVED_NOTIFICATION);
	String bloodRequestReceivedNotificationTitle = String.format(bloodRequestReceivedNotificationTemplate);
	bloodRequestReceivedNotification.setTitle(bloodRequestReceivedNotificationTitle);
	bloodRequestReceivedNotification.setRequester_b_p_id(creator_b_p_id);
	return bloodRequestReceivedNotification;
    }
}
