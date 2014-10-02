package org.redhelp.bo;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.redhelp.common.AddEventResponse;
import org.redhelp.common.GetEventResponse;
import org.redhelp.common.SlotsCommonFields;
import org.redhelp.common.exceptions.InvalidRequestException;
import org.redhelp.common.types.AddEventResponseType;
import org.redhelp.common.types.EventRequestType;
import org.redhelp.common.types.JodaTimeFormatters;
import org.redhelp.common.types.Location;
import org.redhelp.dao.BloodProfileDAO;
import org.redhelp.dao.EventDAO;
import org.redhelp.dao.SlotDAO;
import org.redhelp.helpers.DateTimeHelper;
import org.redhelp.model.BloodRequestModel;
import org.redhelp.model.EventModel;
import org.redhelp.model.SlotModel;
import org.redhelp.model.UserBloodProfileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Event {

    private Logger logger = Logger.getLogger(Event.class);
    
    @Autowired
    private EventDAO eventDAO;
    
    @Autowired
    private SlotDAO slotDAO;
    
    @Autowired
    private BloodProfileDAO bloodProfileDAO;
    
    @Transactional
    public GetEventResponse getEvent(Long e_id) {
	EventModel eventModel = eventDAO.findById(e_id);
	if(eventModel == null) {
	    logger.info("eventModel is null can't find model for e_id" + e_id);
	    throw new InvalidRequestException("can't find model for e_id"+e_id);
	}
	
	return getEventResponseFromEventModel(eventModel);
    }

    private GetEventResponse getEventResponseFromEventModel(EventModel eventModel) {
	GetEventResponse getEventResponse = new GetEventResponse();
	getEventResponse.setActive(eventModel.isActive());
	getEventResponse.setAdditional_email(eventModel.getAdditional_email());
	String creation_datetime_string = DateTimeHelper.convertJavaDateToString(eventModel.getCreation_datetime(),
   		JodaTimeFormatters.dateFormatter);
	getEventResponse.setCreation_datetime(creation_datetime_string);
	
	getEventResponse.setE_id(eventModel.getE_id());
	getEventResponse.setLocation_address(eventModel.getLocation_address());
	getEventResponse.setLocation_lat(eventModel.getLocation_lat());
	getEventResponse.setLocation_long(eventModel.getLocation_long());
	getEventResponse.setLocation_name(eventModel.getLocation_name());
	
	String master_start_datetime = DateTimeHelper.convertJavaDateToString(eventModel.getMaster_start_datetime(),
   		JodaTimeFormatters.dateTimeFormatter);
	getEventResponse.setMaster_start_datetime(master_start_datetime);
	String master_end_datetime = DateTimeHelper.convertJavaDateToString(eventModel.getMaster_end_datetime(),
   		JodaTimeFormatters.dateTimeFormatter);
	getEventResponse.setMaster_end_datetime(master_end_datetime);
	
	getEventResponse.setName(eventModel.getName());
	getEventResponse.setOrganization(eventModel.getOrganization());
	getEventResponse.setPhone_number(eventModel.getPhone_number());
	
	SortedSet<SlotsCommonFields> slotsCommonFields = new TreeSet<SlotsCommonFields>();
	for(SlotModel slot : eventModel.getSlots()) {
	    SlotsCommonFields commonField = new SlotsCommonFields();
	    commonField.setCurrent_attendees(slot.getCurrent_attendees());
	    commonField.setCurrent_volunteers(slot.getCurrent_volunteers());
	    commonField.setMax_attendees(slot.getMax_attendees());
	    commonField.setMax_volunteers(slot.getMax_volunteers());
	    commonField.setS_id(slot.getS_id());
	    
	    String start_datetime = DateTimeHelper.convertJavaDateToString(slot.getStart_datetime(),
	   		JodaTimeFormatters.dateTimeFormatter);
	    commonField.setStart_datetime(start_datetime);
	   
	    String end_datetime = DateTimeHelper.convertJavaDateToString(slot.getEnd_datetime(),
	   		JodaTimeFormatters.dateTimeFormatter);
	    commonField.setEnd_datetime(end_datetime);
	    
	    slotsCommonFields.add(commonField);
	}
	getEventResponse.setSlots(slotsCommonFields);
	return getEventResponse;
    }

    @Transactional
    public AddEventResponse addUserToEvent(Long b_p_id, Long s_id, EventRequestType request_type) {
	
	SlotModel slot = slotDAO.findById(s_id);
	if(slot == null) 
	    throw new InvalidRequestException("slot not found for :"+s_id);
	
	Set<UserBloodProfileModel> blood_profiles = slot.getUsers_coming();
	
	for(UserBloodProfileModel blood_profile : blood_profiles) {
	    if(blood_profile.getB_p_id() == b_p_id) {
		AddEventResponse response = new AddEventResponse();
		response.setResponse_types(AddEventResponseType.ALREADY_DONE);
		return response;
	    }
	}
	
	UserBloodProfileModel blood_profile = bloodProfileDAO.findById(b_p_id);
	if(blood_profile == null)
	    throw new InvalidRequestException("blood profile not found for "+b_p_id);
	
	blood_profiles.add(blood_profile);
	slot.setUsers_coming(blood_profiles);
	
	int current_value;
	int max_value;
	if(request_type.equals(EventRequestType.ATTEND)) {
	    current_value = slot.getCurrent_attendees();
	    max_value = slot.getMax_attendees();
	    if(current_value > max_value) {
		AddEventResponse response = new AddEventResponse();
		response.setResponse_types(AddEventResponseType.MAX_LIMIT_REACHED);
		return response;
	    }
	    slot.setCurrent_attendees(current_value+1);
	} else {
	    current_value = slot.getCurrent_volunteers();
	    max_value = slot.getMax_volunteers();
	    if(current_value > max_value) {
		AddEventResponse response = new AddEventResponse();
		response.setResponse_types(AddEventResponseType.MAX_LIMIT_REACHED);
		return response;
	    }
	    slot.setCurrent_volunteers(current_value+1);
	}
		
	SlotModel updated_slot = slotDAO.update(slot);
	AddEventResponse  response = addEventResponseFromSlot(updated_slot);
	return response;
    }

    private AddEventResponse addEventResponseFromSlot(SlotModel updated_slot) {
	AddEventResponse response = new AddEventResponse();
	response.setNumber_of_attendees(updated_slot.getCurrent_attendees());
	response.setNumber_of_volunteers(updated_slot.getCurrent_volunteers());
	response.setResponse_types(AddEventResponseType.SUCCESSFUL);
	return response;
    }
    
    @Transactional
    public List<EventModel> searchViaRange(Location southWestLocation,
	    Location northEastLocation) {
	List<EventModel> list_event_models = eventDAO.searchViaRange(southWestLocation, northEastLocation);
	return list_event_models;
    }
    
    @Transactional
    public List<EventModel> searchAll() {
	List<EventModel> list_event_models = eventDAO.findAll();
	return list_event_models;
    }
}
