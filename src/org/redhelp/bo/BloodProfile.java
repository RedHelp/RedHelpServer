package org.redhelp.bo;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.redhelp.common.AddEventResponse;
import org.redhelp.common.GetBloodProfileAccessRequest;
import org.redhelp.common.GetBloodProfileAccessResponse;
import org.redhelp.common.GetBloodProfileAccessResponseRequest;
import org.redhelp.common.GetBloodProfileAccessResponseResponse;
import org.redhelp.common.GetBloodProfileRequest;
import org.redhelp.common.GetBloodProfileResponse;
import org.redhelp.common.GetBloodRequestResponse;
import org.redhelp.common.GetEventResponse;
import org.redhelp.common.SaveBloodProfileResponse;
import org.redhelp.common.exceptions.DependencyException;
import org.redhelp.common.exceptions.InvalidRequestException;
import org.redhelp.common.types.BloodGroupType;
import org.redhelp.common.types.CreateBloodProfileResponseTypes;
import org.redhelp.common.types.EventRequestType;
import org.redhelp.common.types.Gender;
import org.redhelp.common.types.GetBloodProfileAccessResponseType;
import org.redhelp.common.types.GetBloodProfileType;
import org.redhelp.common.types.JodaTimeFormatters;
import org.redhelp.common.types.Location;
import org.redhelp.common.types.NotificationTypes;
import org.redhelp.dao.BloodProfileDAO;
import org.redhelp.dao.EventDAO;
import org.redhelp.dao.UserAccountDAO;
import org.redhelp.dao.UserRelationshipDAO;
import org.redhelp.helpers.DateTimeHelper;
import org.redhelp.model.BloodGroupsModel;
import org.redhelp.model.BloodRequestModel;
import org.redhelp.model.EventModel;
import org.redhelp.model.NotificationModel;
import org.redhelp.model.SlotModel;
import org.redhelp.model.UserAccountModel;
import org.redhelp.model.UserBloodProfileModel;
import org.redhelp.model.UsersRelationshipModel;
import org.redhelp.types.UserRelationshipType;
import org.redhelp.util.NotificationsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BloodProfile {

    private Logger logger = Logger.getLogger(BloodProfile.class);

    @Autowired 
    BloodProfileDAO bloodProfileDAO;

    @Autowired 
    UserAccountDAO userAccountDAO;

    @Autowired
    Notification notificationBo;

    @Autowired
    UserRelationshipDAO userRealtionshipDAO;
    
    @Autowired
    private EventDAO eventDAO;

    @Transactional
    public SaveBloodProfileResponse saveBloodProfile(Long u_id, Long b_p_id, Gender gender,
	    String city, BloodGroupType blood_group_type, Double last_known_location_lat,
	    Double last_known_location_long, String birthDate) {
	UserBloodProfileModel model_passed = bloodProfileModelFromSaveRequest(u_id, b_p_id, gender, city,
		blood_group_type, last_known_location_lat, last_known_location_long, birthDate);

	UserBloodProfileModel blood_profile_received = bloodProfileDAO.saveOrUpdate(model_passed);

	SaveBloodProfileResponse response = userBloodProfileToSaveBloodProfileRespone(blood_profile_received);

	if(CreateBloodProfileResponseTypes.SUCCESSFUL.equals(response.getResponse_type())) {
	    //Adding welcome notification to user's profile.
	    Long b_p_id_created = response.getB_p_id();
	    NotificationModel welcomeNotification = NotificationsHelper.getWelcomeNotification(b_p_id_created);
	    notificationBo.addNotification(welcomeNotification);
	}

	return response;
    }

    @Transactional
    public GetBloodProfileResponse getBloodProfile(Long b_p_id) {
	UserBloodProfileModel model_received = bloodProfileDAO.findById(b_p_id);
	GetBloodProfileResponse response = new GetBloodProfileResponse();
	if(model_received != null)
	{ 
	    response = createGetBloodProfileResponse(model_received, GetBloodProfileType.OWN);

	}
	else {
	    logger.info("model_received is null");
	    throw new InvalidRequestException("can't find model for b_p_id"+b_p_id);
	}
	return response;
    }

    @Transactional
    public UserBloodProfileModel getBloodProfileModel(Long b_p_id) {
	UserBloodProfileModel model_received = bloodProfileDAO.findById(b_p_id);

	if(model_received != null)
	{ 

	    /*logger.info(model_received.getBlood_requests());
	    Iterator<BloodRequestModel> it = model_received.getBlood_requests().iterator();
	    logger.info(model_received.getBlood_requests().first().toString());
	    while(it.hasNext()){
		BloodRequestModel request = it.next();
		logger.info("request:"+request.toString());
	    }*/

	}
	else {
	    logger.info("model_received is null");
	    throw new InvalidRequestException("can't find model for b_p_id"+b_p_id);
	}

	return model_received;
    }


    @Transactional
    public GetBloodProfileResponse getBloodProfileViaUid(Long u_id_long) {
	UserAccountModel user_account_model = userAccountDAO.findById(u_id_long);
	GetBloodProfileResponse response = new GetBloodProfileResponse();
	if(user_account_model != null) { 
	    UserBloodProfileModel blood_profile = getBloodProfileModelViaUserAccount(user_account_model);
	    response = createGetBloodProfileResponse(blood_profile, GetBloodProfileType.OWN);
	}
	return response;
    }

    @Transactional
    public UserBloodProfileModel getBloodProfileModelViaUserAccount(UserAccountModel user_account) {
	UserBloodProfileModel model_received = bloodProfileDAO.findByUid(user_account);
	return model_received;
    }




    private UserBloodProfileModel bloodProfileModelFromSaveRequest(Long u_id, Long b_p_id, Gender gender,
	    String city, BloodGroupType blood_group_type, Double last_known_location_lat,
	    Double last_known_location_long, String birthDateStr) {

	UserBloodProfileModel model_passed = new UserBloodProfileModel();

	UserAccountModel user_account_model = userAccountDAO.findById(u_id);
	if(user_account_model != null) { 
	    model_passed.setUser_account(user_account_model);
	    if(b_p_id != null)
		model_passed.setB_p_id(b_p_id);
	    else {
		UserBloodProfileModel existing_model = bloodProfileDAO.findByUid(user_account_model);
		if(existing_model != null)
		    model_passed = existing_model;

	    }
	} else 
	    throw new DependencyException("Invalid state, user_account_model can't be null for b_p_id:"+b_p_id);


	if(gender != null)
	    model_passed.setGender(gender);
	if(blood_group_type != null)
	    model_passed.setBlood_group_type(blood_group_type);
	if(last_known_location_lat!= null) 
	    model_passed.setLast_known_location_lat(last_known_location_lat);
	if(last_known_location_long!= null)
	    model_passed.setLast_known_location_long(last_known_location_long);
	model_passed.setLast_known_location_datetime(new Date());
	if(city != null)
	    model_passed.setCity(city);
	if(birthDateStr != null) {    
	    try {
		Date birthDate = DateTimeHelper.convertStringToJavaDate(birthDateStr, JodaTimeFormatters.dateFormatter);
		model_passed.setBirth_date(birthDate);
	    }catch(Exception e ){}

	}

	return model_passed;

    }
    public static SaveBloodProfileResponse userBloodProfileToSaveBloodProfileRespone(UserBloodProfileModel model)
    {
	SaveBloodProfileResponse response = new SaveBloodProfileResponse();
	response.setB_p_id(model.getB_p_id());

	if(model.getUser_account()!=null)
	    response.setU_id(model.getUser_account().getU_id());
	else 
	    throw new DependencyException("Invalid state, UserAccount is null for b_p_id:"+model.getB_p_id());

	response.setCity(model.getCity());
	response.setGender(model.getGender());
	response.setBlood_group_type(model.getBlood_group_type());
	response.setLast_known_location_lat(model.getLast_known_location_lat());
	response.setLast_known_location_long(model.getLast_known_location_long());

	if(model.getBirth_date() != null) {
	    try{
		String birth_date = DateTimeHelper.convertJavaDateToString(model.getBirth_date(), JodaTimeFormatters.dateFormatter);
		response.setBirth_date(birth_date);
	    }catch(Exception e){}
	}

	response.setResponse_type(CreateBloodProfileResponseTypes.SUCCESSFUL);

	return response;
    }

    @Transactional
    public List<UserBloodProfileModel> searchViaRange(Location southWestLocation,
	    Location northEastLocation) {
	List<UserBloodProfileModel> list_blood_provile_models = bloodProfileDAO.searchViaRange(southWestLocation, northEastLocation);
	return list_blood_provile_models;
    }

    @Transactional
    public List<UserBloodProfileModel> searchAll() {
	List<UserBloodProfileModel> list_blood_provile_models = bloodProfileDAO.findAll();
	return list_blood_provile_models;
    }

    @Transactional
    public GetBloodProfileAccessResponse accessRequest(GetBloodProfileAccessRequest accessRequest) {
	GetBloodProfileAccessResponse response = new GetBloodProfileAccessResponse();
	List<UsersRelationshipModel> userRelationships = userRealtionshipDAO.findByBpid(accessRequest.getRequester_b_p_id());
	if(userRelationships != null) {
	    for(UsersRelationshipModel relationship : userRelationships) {
		if(accessRequest.getReceiver_b_p_id()  == relationship.getRelated_b_p_id()) {
		    if(UserRelationshipType.BLOOD_REQUEST_ACCEPTED.equals(relationship.getUserRelationshipType()))
			response.setAccessResponseType(GetBloodProfileAccessResponseType.REQUEST_ACCEPTED);
		    else if(UserRelationshipType.VIEW_PROFILE_PENDING.equals(relationship.getUserRelationshipType()))
			response.setAccessResponseType(GetBloodProfileAccessResponseType.REQUEST_PENDING);
		    return response;
		}
	    }
	}

	// Set requester-receiver relationship
	UsersRelationshipModel requesterReceiverRelationship  = new UsersRelationshipModel();
	requesterReceiverRelationship.setB_p_id(accessRequest.getRequester_b_p_id());
	requesterReceiverRelationship.setRelated_b_p_id(accessRequest.getReceiver_b_p_id());
	requesterReceiverRelationship.setUserRelationshipType(UserRelationshipType.VIEW_PROFILE_PENDING);

	userRealtionshipDAO.create(requesterReceiverRelationship);

	// Set receiver-requester relationship
	UsersRelationshipModel receiverRequesterRelationship  = new UsersRelationshipModel();
	receiverRequesterRelationship.setB_p_id(accessRequest.getReceiver_b_p_id());
	receiverRequesterRelationship.setRelated_b_p_id(accessRequest.getRequester_b_p_id());
	receiverRequesterRelationship.setUserRelationshipType(UserRelationshipType.VIEW_PROFILE_REQUESTEE);

	userRealtionshipDAO.create(receiverRequesterRelationship);


	// Send notification
	UserBloodProfileModel creatorBloodProfile = getBloodProfileModel(accessRequest.getRequester_b_p_id());
	String creator_name = creatorBloodProfile.getUser_account().getName();
	NotificationModel bloodProfileAccessNotification = NotificationsHelper.getBloodProfileAccessNotification(accessRequest.getRequester_b_p_id(),
		accessRequest.getReceiver_b_p_id(), creator_name);
	notificationBo.addNotification(bloodProfileAccessNotification);

	response.setAccessResponseType(GetBloodProfileAccessResponseType.REQUEST_POSTED_SUCCESSFULLY);
	return response;
    }

    @Transactional
    public GetBloodProfileAccessResponseResponse accessRequest(GetBloodProfileAccessResponseRequest respondRequest) {
	GetBloodProfileAccessResponseResponse response = new GetBloodProfileAccessResponseResponse();
	// Set requestee-reqester relationship
	UsersRelationshipModel requesteeRequesterRelationship  = userRealtionshipDAO.findFirstByBpid(respondRequest.getRequestee_b_p_id());
	requesteeRequesterRelationship.setRelated_b_p_id(respondRequest.getRequester_b_p_id());
	requesteeRequesterRelationship.setUserRelationshipType(UserRelationshipType.VIEW_PROFILE_ACCEPTED);

	userRealtionshipDAO.update(requesteeRequesterRelationship);

	// Set reqester-requestee relationship
	UsersRelationshipModel requesterRequesteeRelationship  = userRealtionshipDAO.findFirstByBpid(respondRequest.getRequester_b_p_id());
	requesterRequesteeRelationship.setRelated_b_p_id(respondRequest.getRequestee_b_p_id());
	requesterRequesteeRelationship.setUserRelationshipType(UserRelationshipType.VIEW_PROFILE_ACCEPTED);

	userRealtionshipDAO.update(requesterRequesteeRelationship);


	// Send notification to Requester
	UserBloodProfileModel requesteeBloodProfile = getBloodProfileModel(respondRequest.getRequestee_b_p_id());
	String requestee_name = requesteeBloodProfile.getUser_account().getName();
	NotificationModel bloodProfileAccessNotification = NotificationsHelper.getBloodProfileAcceptedNotification(respondRequest.getRequester_b_p_id(),
		respondRequest.getRequestee_b_p_id(), requestee_name);
	notificationBo.addNotification(bloodProfileAccessNotification);

	response.setDone(true);
	return response;
    }

    @Transactional
    public GetBloodProfileResponse getBloodProfile(GetBloodProfileRequest profileRequest) {
	GetBloodProfileResponse response = null;

	Long requester_b_p_id = profileRequest.getRequester_b_p_id();
	Long b_p_id = profileRequest.getB_p_id(); 

	UserBloodProfileModel bloodProfileModel = getBloodProfileModel(b_p_id);
	if(requester_b_p_id.equals(b_p_id)) {
	    response = createGetBloodProfileResponse(bloodProfileModel, GetBloodProfileType.OWN);
	    return response;
	} 
	UserRelationshipType relationshipType = getUsersRelationshipType(requester_b_p_id, b_p_id);

	// Mapping of user relationship type to client shown types.
	if(relationshipType != null) {
	    if(relationshipType.equals(UserRelationshipType.VIEW_PROFILE_ACCEPTED) || 
		    relationshipType.equals(UserRelationshipType.BLOOD_REQUEST_ACCEPTED)) {
		response = createGetBloodProfileResponse(bloodProfileModel, GetBloodProfileType.PRIVATE);
	    } else if(relationshipType.equals(UserRelationshipType.VIEW_PROFILE_PENDING)) {
		response = createGetBloodProfileResponse(bloodProfileModel, GetBloodProfileType.PUBLIC_VIEW_PROFILE_PENDING);
	    } else if(relationshipType.equals(UserRelationshipType.BLOOD_REQUEST_PENDING)) {
		response = createGetBloodProfileResponse(bloodProfileModel, GetBloodProfileType.PUBLIC_BLOOD_REQUEST_PENDING);
	    } else if(relationshipType.equals(UserRelationshipType.VIEW_PROFILE_REQUESTEE)) {
		response = createGetBloodProfileResponse(bloodProfileModel, GetBloodProfileType.PUBLIC_VIEW_PROFILE_REQUESTEE);
	    }
	} else {
	    response = createGetBloodProfileResponse(bloodProfileModel, GetBloodProfileType.PUBLIC);
	}

	return response;
    }


    private UserRelationshipType getUsersRelationshipType(Long requester_b_p_id, Long receiver_b_p_id) {
	List<UsersRelationshipModel> userRelationships = userRealtionshipDAO.findByBpid(requester_b_p_id);
	if(userRelationships != null) {
	    for(UsersRelationshipModel relationship : userRelationships) {
		if(relationship.getRelated_b_p_id()  == receiver_b_p_id) {
		    return relationship.getUserRelationshipType();
		}
	    }
	}
	return null;
    }

    private GetBloodProfileResponse createGetBloodProfileResponse(UserBloodProfileModel model_received, GetBloodProfileType profileType) {
	GetBloodProfileResponse response = new GetBloodProfileResponse();
	response.setB_p_id(model_received.getB_p_id());
	response.setCity(model_received.getCity());
	response.setGender(model_received.getGender());
	response.setUser_image(model_received.getUser_account().getUser_image());
	response.setBlood_group_type(model_received.getBlood_group_type());
	
	response.setBlood_requests(convertRequestModels(model_received.getBlood_requests()));
	
	response.setEvent_response(convertEventsModels(model_received.getSlots()));

	if(model_received.getUser_account() != null) {
	    response.setName(model_received.getUser_account().getName());
	    if(GetBloodProfileType.OWN.equals(profileType) || GetBloodProfileType.PRIVATE.equals(profileType)) {
		response.setLast_known_location_lat(model_received.getLast_known_location_lat());
		response.setLast_known_location_long(model_received.getLast_known_location_long());

		response.setEmail(model_received.getUser_account().getEmail());
		response.setPhone_number(model_received.getUser_account().getPhoneNo());
	    }
	}
	response.setResponse_type(profileType);
	return response;
    }

    private LinkedList<GetEventResponse> convertEventsModels(Set<SlotModel> slots) {
	LinkedList<GetEventResponse> getEventResponseList = null;
	if (slots != null) {
	    getEventResponseList = new LinkedList<GetEventResponse>();
	    for (SlotModel slot : slots) {
		
		Long e_id = slot.getE_id();
		EventModel eventModel = eventDAO.findById(e_id);
		if (eventModel != null) {
		    GetEventResponse eventResponse = new GetEventResponse();
		    eventResponse.setE_id(eventModel.getE_id());
		    eventResponse.setName(eventModel.getName());
		    eventResponse.setOrganization(eventModel.getOrganization());
		    
		    String string_date = DateTimeHelper.convertJavaDateToString(eventModel.getCreation_datetime(),
				JodaTimeFormatters.dateFormatter);
		    eventResponse.setCreation_datetime(string_date);
		    
		    eventResponse.setLocation_address(eventModel.getLocation_address());
		    eventResponse.setLocation_lat(eventModel.getLocation_lat());
		    eventResponse.setLocation_long(eventModel.getLocation_long());
		    getEventResponseList.add(eventResponse);
		}
		
	    }
	}
	return getEventResponseList;
    }

    private LinkedList<GetBloodRequestResponse> convertRequestModels(SortedSet<BloodRequestModel> blood_requests) {
	LinkedList<GetBloodRequestResponse> responseSet = null;
	if(blood_requests != null) {
	    responseSet = new LinkedList<GetBloodRequestResponse>();
	    for(BloodRequestModel bloodRequestModel : blood_requests) {
		GetBloodRequestResponse response = new GetBloodRequestResponse();
		response.setB_r_id(bloodRequestModel.getB_r_id());
		String blood_grps_str = null;
		for(BloodGroupsModel bloodGroupsModel: bloodRequestModel.getSet_blood_group()) {
		    BloodGroupType groupType = bloodGroupsModel.getBloodGroupTypeEnum();
		    if(blood_grps_str == null)
			blood_grps_str = groupType.toString();
		    else
			blood_grps_str = blood_grps_str + ", " + groupType.toString();
		}
		response.setBlood_groups_str(blood_grps_str);
		response.setDescription(bloodRequestModel.getPatient_name());
		response.setPlace_location_lat(bloodRequestModel.getPlace_location_lat());
		response.setPlace_location_long(bloodRequestModel.getPlace_location_long());
		response.setPlace_string(bloodRequestModel.getPlace_string());
		responseSet.add(response);
	    }
	}	
	return responseSet;
    }
}
