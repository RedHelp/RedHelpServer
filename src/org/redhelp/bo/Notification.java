package org.redhelp.bo;

import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.redhelp.common.GetAllNotificationsResponse;
import org.redhelp.common.GetNewNotificationsResponse;
import org.redhelp.common.MarkNotificaionAsReadRequest;
import org.redhelp.common.MarkNotificationReadResponse;
import org.redhelp.common.NotificationCommonFields;
import org.redhelp.common.types.GetNewNotificationsResponseType;
import org.redhelp.common.types.JodaTimeFormatters;
import org.redhelp.common.types.MarkNotificationResponseType;
import org.redhelp.common.types.NotificationTypes;
import org.redhelp.dao.NotificationDAO;
import org.redhelp.helpers.DateTimeHelper;
import org.redhelp.model.NotificationModel;
import org.redhelp.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Notification {

    @Autowired
    private NotificationDAO notificationDAO;

    @Transactional
    public Long addNotification(NotificationModel notificationModel) {
	Assert.assertNotNull(notificationModel, "notificationModel is null, can't proceed forward");

	if(!shouldAddNotification(notificationModel.getB_p_id()))
	    return null;

	notificationModel.setCreation_datetime(new Date());
	NotificationModel notificationModelReturned = notificationDAO.create(notificationModel);	
	Assert.assertNotNull(notificationModelReturned, "notificationModelReturned is null, can't proceed forward");
	return notificationModelReturned.getN_id();
    }

    @Transactional
    public GetNewNotificationsResponse getNewNotificaitons(Long b_p_id) {
	Assert.assertNotNull(b_p_id, "b_p_id is null, can't proceed forward");
	List<NotificationModel> notificationModels = notificationDAO.findNewNotificationsByBpid(b_p_id);

	if(notificationModels != null && notificationModels.size() > 0)
	    return convertNotificationModelsToGetNewNotificationResponse(notificationModels);
	else {
	    GetNewNotificationsResponse response = new GetNewNotificationsResponse();
	    response.setResponseType(GetNewNotificationsResponseType.NO_NEW_NOTIFICATIONS);
	    return response;
	}
    }

    @Transactional
    public GetAllNotificationsResponse getAllNotificaitons(Long b_p_id) {
	Assert.assertNotNull(b_p_id, "b_p_id is null, can't proceed forward");
	List<NotificationModel> notificationModels = notificationDAO.findByBpid(b_p_id);

	if(notificationModels != null)
	    return convertNotificationModelsToGetAllNotificationResponse(notificationModels);
	else 
	    return new GetAllNotificationsResponse();

    }

    @Transactional
    public MarkNotificationReadResponse markAsRead(MarkNotificaionAsReadRequest markRequest) {
	MarkNotificationReadResponse response = new MarkNotificationReadResponse();
	Assert.assertNotNull(markRequest.getN_id_list(), "Notification List is null, returning");
	List<Long> n_id_list = markRequest.getN_id_list();
	for(Long n_id:n_id_list) {
	    NotificationModel notificationModel = notificationDAO.findById(n_id);
	    if(notificationModel == null)
		continue;
	    notificationModel.setRead(true);
	    notificationDAO.update(notificationModel);
	}
	response.setResponseType(MarkNotificationResponseType.SUCCESSFUL);
	return response;
    }


    private boolean shouldAddNotification(Long b_p_id) {
	List<NotificationModel> pastNotifications = notificationDAO.findByBpid(b_p_id);

	Date currentDate = new Date();
	int numOfNotificationsSentIn15Days = 0;
	if(pastNotifications != null){
	    for(NotificationModel notification : pastNotifications) {
		Date notificationSentDate = notification.getCreation_datetime();
		int diffInDays = (int)( (currentDate.getTime() - notificationSentDate.getTime()) 
			/ (1000 * 60 * 60 * 24) );
		if(diffInDays < 15 
			&& NotificationTypes.BLOOD_PROFILE_ACCESS_NOTIFICATION.
			equals(notification.getNotification_type()))
		    numOfNotificationsSentIn15Days++;
	    }
	}

	if(numOfNotificationsSentIn15Days > 3)
	    return false;
	else
	    return true;
    }

    private GetAllNotificationsResponse convertNotificationModelsToGetAllNotificationResponse(
	    List<NotificationModel> notificationModels) {
	GetAllNotificationsResponse response = new GetAllNotificationsResponse();
	response.setNotificationCommonFields(getNotificationsCommonFields(notificationModels));
	return response;
    }

    private GetNewNotificationsResponse convertNotificationModelsToGetNewNotificationResponse(
	    List<NotificationModel> notificationModels) {
	GetNewNotificationsResponse response = new GetNewNotificationsResponse();
	response.setNotificationCommonFields(getNotificationsCommonFields(notificationModels));
	response.setResponseType(GetNewNotificationsResponseType.SUCCESSFUL);
	return response;
    }

    private SortedSet<NotificationCommonFields> getNotificationsCommonFields(List<NotificationModel> notificationModels) {
	SortedSet<NotificationCommonFields> notificationCommonFields = new TreeSet<NotificationCommonFields>();
	for(NotificationModel notification : notificationModels) {
	    NotificationCommonFields notCommonField = new NotificationCommonFields();
	    notCommonField.setN_id(notification.getN_id());
	    notCommonField.setTitle(notification.getTitle());
	    notCommonField.setRead(notification.isRead());
	    notCommonField.setB_r_id(notification.getB_r_id());
	    notCommonField.setNotification_type(notification.getNotification_type());
	    notCommonField.setRequester_b_p_id(notification.getRequester_b_p_id());
	    notCommonField.setE_id(notification.getE_id());
	    notCommonField.setMessage(notification.getMessage());
	    String creation_datetime_str = DateTimeHelper.convertJavaDateToString(notification.getCreation_datetime(),
		    JodaTimeFormatters.dateTimeFormatter);
	    notCommonField.setCreation_datetime(creation_datetime_str);
	    notificationCommonFields.add(notCommonField);
	}
	return notificationCommonFields;
    }




}
