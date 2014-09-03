package org.redhelp.bo;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.redhelp.common.AcceptBloodRequestRequest;
import org.redhelp.common.AcceptBloodRequestResponse;
import org.redhelp.common.GetBloodRequestResponse;
import org.redhelp.common.SaveBloodRequestRequest;
import org.redhelp.common.UserProfileCommonFields;
import org.redhelp.common.exceptions.InvalidRequestException;
import org.redhelp.common.types.BloodGroupType;
import org.redhelp.common.types.BloodRequestType;
import org.redhelp.common.types.JodaTimeFormatters;
import org.redhelp.common.types.Location;
import org.redhelp.common.types.NotificationTypes;
import org.redhelp.dao.BloodRequestDAO;
import org.redhelp.dao.RequestUserRelationshipDAO;
import org.redhelp.dao.UserRelationshipDAO;
import org.redhelp.helpers.DateTimeHelper;
import org.redhelp.model.BloodGroupsModel;
import org.redhelp.model.BloodRequestModel;
import org.redhelp.model.NotificationModel;
import org.redhelp.model.RequestUserRelationshipModel;
import org.redhelp.model.UserAccountModel;
import org.redhelp.model.UserBloodProfileModel;
import org.redhelp.types.EmailType;
import org.redhelp.types.RequestUserRelationshipType;
import org.redhelp.types.UserRelationshipType;
import org.redhelp.util.BloodRequestHelper;
import org.redhelp.util.EmailSender;
import org.redhelp.util.NotificationsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BloodRequest {

    private Logger logger = Logger.getLogger(BloodRequest.class);

    @Autowired
    private BloodRequestDAO bloodRequestDAO;

    @Autowired
    BloodProfile bloodProfileBo;

    @Autowired
    Notification notificationBo;

    @Autowired
    RequestUserRelationshipDAO requestUserRelationshipDAO;

    @Autowired
    RequestUserRelationship reUserRelationshipBo;

    @Autowired
    UserRelationship userRealtionshipBo;

    public BloodRequestModel createBloodRequest(BloodRequestModel blood_request_model_passed)
    {
	return bloodRequestDAO.create(blood_request_model_passed);
    }

    @Transactional
    public AcceptBloodRequestResponse acceptBloodRequest(AcceptBloodRequestRequest acceptRequest) {
	AcceptBloodRequestResponse response = new AcceptBloodRequestResponse();
	Long b_r_id = acceptRequest.getB_r_id();
	Long acceptor_b_p_id = acceptRequest.getB_p_id();

	BloodRequestModel requestModel = bloodRequestDAO.findById(b_r_id);
	Long creator_b_p_id = requestModel.getB_p_id();


	RequestUserRelationshipModel relationshipModel = requestUserRelationshipDAO.find(b_r_id, acceptor_b_p_id);
	if(relationshipModel == null) {
	    response.setResponse(false);
	    return response;
	}

	// Update relationship
	relationshipModel.setRequestUserRelationshipType(RequestUserRelationshipType.BLOOD_REQUEST_ACCEPTED);
	requestUserRelationshipDAO.update(relationshipModel);

	// Create creator-acceptor Blood profile relationship
	userRealtionshipBo.updateRelationship(creator_b_p_id, acceptor_b_p_id, UserRelationshipType.BLOOD_REQUEST_ACCEPTED);

	// Send notification and email to Creator

	NotificationModel bloodRequestAcceptedNotification = NotificationsHelper.getBloodRequestAccptedNotification(creator_b_p_id, b_r_id, acceptor_b_p_id);
	notificationBo.addNotification(bloodRequestAcceptedNotification);

	// TODO Send email.
	/*try {
	    String email = receiver.getUser_account().getEmail();
	    if(email != null) {
			EmailSender emailTask = new EmailSender(email, null, EmailType.BLOOD_REQUEST_RECEIVED);
		    Thread worker = new Thread(emailTask);
		    worker.start();
		}
	    } catch(Exception e) {
		logger.error("Error while sending email", e);
	    }*/

	response.setResponse(true);
	return response;
    }


    @Transactional
    public BloodRequestModel createBloodRequest(SaveBloodRequestRequest request) {

	// Save Request in database
	BloodRequestModel model_passed = bloodRequestModelFromSaveRequest(request);
	BloodRequestModel model_received = createBloodRequest(model_passed);

	Long b_r_id = model_received.getB_r_id();
	Long creator_b_p_id = request.getB_p_id();

	// Add potential donors in relationship table
	Set<UserBloodProfileModel> bloodRequestReceivers = BloodRequestHelper.getPotentialDonors(bloodProfileBo);
	for(UserBloodProfileModel bloodProfile : bloodRequestReceivers) {
	    Long b_p_id = bloodProfile.getB_p_id();
	    RequestUserRelationshipModel realtionshipModel = new RequestUserRelationshipModel();
	    realtionshipModel.setB_p_id(b_p_id);
	    realtionshipModel.setB_r_id(b_r_id);
	    realtionshipModel.setRequestUserRelationshipType(RequestUserRelationshipType.BLOOD_REQUEST_PENDING_ACCEPTANCE);
	    requestUserRelationshipDAO.create(realtionshipModel);
	}


	// Send email to creator
	try {

	    UserBloodProfileModel creatorBloodProfile = bloodProfileBo.getBloodProfileModel(creator_b_p_id);
	    String creator_email = creatorBloodProfile.getUser_account().getEmail();

	    if(creator_email != null) {
		EmailSender emailTask = new EmailSender(creator_email, null, EmailType.BLOOD_REQUEST_POSTED);
		Thread worker = new Thread(emailTask);
		worker.start();
	    }
	} catch(Exception e) {
	    logger.error("Error while sending email", e);
	}


	// Send blood request receivers notification and email.
	for(UserBloodProfileModel receiver : bloodRequestReceivers) {
	    // Send notification
	    Long receivers_b_p_id = receiver.getB_p_id();
	    NotificationModel bloodRequestReceivedNotification = NotificationsHelper.
		    getBloodRequestReceivedNotification(creator_b_p_id, receivers_b_p_id, b_r_id);
	    notificationBo.addNotification(bloodRequestReceivedNotification);

	    // Send email.
	    try {
		String email = receiver.getUser_account().getEmail();
		if(email != null) {
		    EmailSender emailTask = new EmailSender(email, null, EmailType.BLOOD_REQUEST_RECEIVED);
		    Thread worker = new Thread(emailTask);
		    worker.start();
		}
	    } catch(Exception e) {
		logger.error("Error while sending email", e);
	    }
	}
	return model_received;
    }

    @Transactional
    public GetBloodRequestResponse getBloodRequest(Long b_r_id, Long b_p_id) {
	BloodRequestModel bloodRequestModel = bloodRequestDAO.findById(b_r_id);
	if(bloodRequestModel == null) {
	    logger.info("bloodRequestModel is null can't find model for b_r_id" + b_r_id);
	    throw new InvalidRequestException("can't find model for b_r_id"+b_r_id);
	}
	logger.info("bloodRequestModel :"+bloodRequestModel.toString());

	BloodRequestType requestType = null;
	RequestUserRelationshipType relationshipType = reUserRelationshipBo.getRelationShipType(b_r_id, b_p_id);
	if(relationshipType != null) {
	    if(relationshipType.equals(RequestUserRelationshipType.BLOOD_REQUEST_ACCEPTED))
		requestType = BloodRequestType.ACCEPTED;
	    else if(relationshipType.equals(RequestUserRelationshipType.BLOOD_REQUEST_PENDING_ACCEPTANCE))
		requestType = BloodRequestType.REQUESTED;
	}
	else if(bloodRequestModel.getB_p_id() == b_p_id) {
	    requestType = BloodRequestType.CREATOR;
	} else {
	    requestType = BloodRequestType.PUBLIC;
	}


	return getBloodRequestResponseFromBloodRequestModel(bloodRequestModel, requestType);
    }
    private GetBloodRequestResponse getBloodRequestResponseFromBloodRequestModel(BloodRequestModel bloodRequestModel, BloodRequestType requestType) {
	GetBloodRequestResponse response = new GetBloodRequestResponse();
	response.setB_r_id(bloodRequestModel.getB_r_id());
	response.setB_p_id(bloodRequestModel.getB_p_id());
	response.setActive(bloodRequestModel.isActive());
	
	String string_date = DateTimeHelper.convertJavaDateToString(bloodRequestModel.getCreation_datetime(),
		JodaTimeFormatters.dateTimeFormatter);
	response.setCreation_datetime(string_date);
	
	response.setDescription(bloodRequestModel.getDescription());
	response.setPatient_name(bloodRequestModel.getPatient_name());
	response.setPhone_number(bloodRequestModel.getPhone_number());
	response.setUnits(bloodRequestModel.getUnits());

	String blood_grps_str = null;
	for(BloodGroupsModel bloodGroupsModel: bloodRequestModel.getSet_blood_group()) {
	    BloodGroupType groupType = bloodGroupsModel.getBloodGroupTypeEnum();
	    if(blood_grps_str == null)
		blood_grps_str = groupType.toString();
	    else
		blood_grps_str = blood_grps_str + ", " + groupType.toString();
	}
	response.setBlood_groups_str(blood_grps_str);
	
	List<RequestUserRelationshipModel> requestUserRelationships = requestUserRelationshipDAO.findByBrid(bloodRequestModel.getB_r_id());
	response.setBlood_request_receivers_profiles(getCommonFields(requestUserRelationships));
	
	UserProfileCommonFields creator_user_profile = null;
	try {
	    UserBloodProfileModel creator_blood_profile = bloodProfileBo.getBloodProfileModel(response.getB_p_id());
	    UserAccountModel userAccountModel = creator_blood_profile.getUser_account();
	    creator_user_profile = getUserProfileCommonFields(userAccountModel.getEmail(), userAccountModel.getName(),
		    userAccountModel.getPhoneNo(), userAccountModel.getUser_image(), null, null, null);

	} catch(Exception e){}
	response.setCreator_profile(creator_user_profile);
	
	

	response.setBlood_requirement_type(bloodRequestModel.getBlood_requirement_type());

	response.setGps_location_lat(bloodRequestModel.getGps_location_lat());
	response.setGps_location_long(bloodRequestModel.getGps_location_long());
	response.setPlace_location_lat(bloodRequestModel.getPlace_location_lat());
	response.setPlace_location_long(bloodRequestModel.getPlace_location_long());
	response.setPlace_string(bloodRequestModel.getPlace_string());
	
	if(requestType != null)
	    response.setBloodRequestType(requestType);
	else 
	    response.setBloodRequestType(BloodRequestType.PUBLIC);

	return response;
    }
    private Set<UserProfileCommonFields> getCommonFields(List<RequestUserRelationshipModel> requestUserRelationships) {
	Set<UserProfileCommonFields> user_profile_set = new HashSet<UserProfileCommonFields>();
	if(requestUserRelationships != null) {
	    for(RequestUserRelationshipModel relationship : requestUserRelationships) {
		UserBloodProfileModel model = bloodProfileBo.getBloodProfileModel(relationship.getB_p_id());

		if(model != null){
		    UserProfileCommonFields commonFields = getUserProfileCommonFields(model.getUser_account().getEmail()
			    , model.getUser_account().getName(),
			    model.getUser_account().getPhoneNo(),
			    model.getUser_account().getUser_image(),
			    relationship.getRequestUserRelationshipType(),
			    model.getB_p_id(),
			    model.getBlood_group_type());
		    user_profile_set.add(commonFields);
		}

	    }
	}
	return user_profile_set;
    }
    private UserProfileCommonFields getUserProfileCommonFields(String email,
	    String name, String phone_num,
	    byte[] pro_pic, RequestUserRelationshipType relationshipType,
	    Long b_p_id, BloodGroupType bloodGroupType) {
	UserProfileCommonFields commonFields = new UserProfileCommonFields();

	commonFields.setEmail(email);
	commonFields.setName(name);
	commonFields.setPhone_number(phone_num);
	commonFields.setUser_image(pro_pic);
	if(relationshipType != null) {
	    if(relationshipType.equals(RequestUserRelationshipType.BLOOD_REQUEST_ACCEPTED)){
		commonFields.setIsRequestAccepted(true);
	    } else if(relationshipType.equals(RequestUserRelationshipType.BLOOD_REQUEST_PENDING_ACCEPTANCE)) {
		commonFields.setIsRequestAccepted(false);			
	    }
	}
	commonFields.setB_p_id(b_p_id);
	commonFields.setBlood_group_type(bloodGroupType);

	return commonFields;
    }


    private BloodRequestModel bloodRequestModelFromSaveRequest(SaveBloodRequestRequest request){

	BloodRequestModel bloodRequestModel = new BloodRequestModel();
	bloodRequestModel.setActive(true);
	bloodRequestModel.setB_p_id(request.getB_p_id());

	bloodRequestModel.setBlood_requirement_type(request.getBlood_requirement_type());
	bloodRequestModel.setDescription(request.getDescription());
	bloodRequestModel.setPatient_name(request.getPatient_name());
	bloodRequestModel.setUnits(request.getUnits());
	bloodRequestModel.setPhone_number(request.getPhone_number());	

	bloodRequestModel.setGps_location_lat(request.getGps_location_lat());
	bloodRequestModel.setGps_location_long(request.getGps_location_long());
	bloodRequestModel.setPlace_location_lat(request.getPlace_location_lat());
	bloodRequestModel.setPlace_location_long(request.getPlace_location_long());
	bloodRequestModel.setPlace_string(request.getPlace_string());



	Set<BloodGroupsModel> bloodGroups = new HashSet<BloodGroupsModel>();
	for(BloodGroupType bloodGroupType : request.getList_blood_group()) {
	    BloodGroupsModel bloodGroupsModel = BloodGroupsModel.getBloodGroupModel(bloodGroupType);
	    if(bloodGroupsModel != null) {
		bloodGroups.add(bloodGroupsModel);
	    }
	}
	bloodRequestModel.setSet_blood_group(bloodGroups);
	bloodRequestModel.setCreation_datetime(new Date());
	return bloodRequestModel;
    }




    @Transactional
    public List<BloodRequestModel> searchViaRange(Location southWestLocation,
	    Location northEastLocation) {
	List<BloodRequestModel> list_blood_request_models = bloodRequestDAO.searchViaRange(southWestLocation, northEastLocation);
	return list_blood_request_models;
    }

    @Transactional
    public List<BloodRequestModel> searchAll() {
	List<BloodRequestModel> list_blood_request_models = bloodRequestDAO.findAll();
	return list_blood_request_models;
    }



}
