package org.redhelp.bo;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.redhelp.common.AddEventResponse;
import org.redhelp.common.GetBloodProfileAccessRequest;
import org.redhelp.common.GetBloodProfileAccessResponse;
import org.redhelp.common.GetBloodProfileRequest;
import org.redhelp.common.GetBloodProfileResponse;
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
import org.redhelp.dao.UserAccountDAO;
import org.redhelp.dao.UserRelationshipDAO;
import org.redhelp.helpers.DateTimeHelper;
import org.redhelp.model.BloodRequestModel;
import org.redhelp.model.NotificationModel;
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

    @Transactional
    public SaveBloodProfileResponse saveBloodProfile(Long u_id, Long b_p_id, Gender gender,
	    String city, BloodGroupType blood_group_type, Double last_known_location_lat,
	    Double last_known_location_long, String birthDate) {
	UserBloodProfileModel model_passed = bloodProfileModelFromSaveRequest(u_id, b_p_id, gender, city,
		blood_group_type, last_known_location_lat, last_known_location_long, birthDate);

	UserBloodProfileModel blood_profile_received = bloodProfileDAO.saveOrUpdate(model_passed);
	logger.debug("blood profile received after save:"+blood_profile_received.toString());

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
	logger.info(model_received.toString());
	logger.info("response: "+response.toString());
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
	logger.info(model_received.toString());

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
	logger.info(model_received.toString());
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
	if(last_known_location_lat!= null || last_known_location_lat != 0.0) 
	    model_passed.setLast_known_location_lat(last_known_location_lat);
	if(last_known_location_long!= null || last_known_location_long != 0.0)
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
	UsersRelationshipModel usersRelationship  = new UsersRelationshipModel();
	usersRelationship.setB_p_id(accessRequest.getRequester_b_p_id());
	usersRelationship.setRelated_b_p_id(accessRequest.getReceiver_b_p_id());
	usersRelationship.setUserRelationshipType(UserRelationshipType.VIEW_PROFILE_PENDING);

	userRealtionshipDAO.create(usersRelationship);
	response.setAccessResponseType(GetBloodProfileAccessResponseType.REQUEST_POSTED_SUCCESSFULLY);
	return response;
    }

    @Transactional
    public GetBloodProfileResponse getBloodProfile(GetBloodProfileRequest profileRequest) {
	GetBloodProfileResponse response = null;

	Long requester_b_p_id = profileRequest.getRequester_b_p_id();
	Long b_p_id = profileRequest.getB_p_id(); 

	UserBloodProfileModel bloodProfileModel = getBloodProfileModel(b_p_id);

	UserRelationshipType relationshipType = getUsersRelationshipType(requester_b_p_id, b_p_id);
	if(requester_b_p_id == b_p_id) {
	    response = createGetBloodProfileResponse(bloodProfileModel, GetBloodProfileType.OWN);
	}
	else if(relationshipType != null) {
	    if(relationshipType.equals(UserRelationshipType.VIEW_PROFILE_ACCEPTED) || 
		    relationshipType.equals(UserRelationshipType.BLOOD_REQUEST_ACCEPTED))
		response = createGetBloodProfileResponse(bloodProfileModel, GetBloodProfileType.PRIVATE);
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
	if(!profileType.equals(GetBloodProfileType.PUBLIC)) {
	    response.setLast_known_location_lat(model_received.getLast_known_location_lat());
	    response.setLast_known_location_long(model_received.getLast_known_location_long());
	}

	if(model_received.getUser_account() != null) {
	    response.setName(model_received.getUser_account().getName());
	    if(!profileType.equals(GetBloodProfileType.PUBLIC)) {
		response.setEmail(model_received.getUser_account().getEmail());
		response.setPhone_number(model_received.getUser_account().getPhoneNo());
	    }
	}

	response.setResponse_type(profileType);
	return response;
    }




}
