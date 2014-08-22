package org.redhelp.bo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.redhelp.common.BloodProfileSearchResponse;
import org.redhelp.common.BloodRequestSearchResponse;
import org.redhelp.common.EventSearchResponse;
import org.redhelp.common.SearchRequest;
import org.redhelp.common.SearchResponse;
import org.redhelp.common.types.BloodGroupType;
import org.redhelp.common.types.JodaTimeFormatters;
import org.redhelp.common.types.Location;
import org.redhelp.common.types.SearchItemTypes;
import org.redhelp.common.types.SearchRequestType;
import org.redhelp.helpers.DateTimeHelper;
import org.redhelp.model.BloodGroupsModel;
import org.redhelp.model.BloodRequestModel;
import org.redhelp.model.EventModel;
import org.redhelp.model.UserBloodProfileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Search {
    @Autowired
    private BloodRequest bloodRequestBo;
    
    @Autowired
    private BloodProfile bloodProfileBo;
    
    @Autowired 
    private Event eventBo;
    
    
    @Transactional
    public SearchResponse search(SearchRequest searchRequest) 
    {
	SearchResponse searchResponse = null;
	if(SearchRequestType.BOUNDS_BASED.equals(searchRequest.getSearchRequestType())) {
	    searchResponse = searachViaBounds(searchRequest);
	} else if(SearchRequestType.ALL.equals(searchRequest.getSearchRequestType())) {
	    searchResponse = serachAll(searchRequest);
	}
	return searchResponse;
	
    }
    
    private SearchResponse serachAll(SearchRequest searchRequest) {
	SearchResponse searchResponse = new SearchResponse();
	Set<SearchItemTypes> searchItems = searchRequest.getSearchItems();
	for(SearchItemTypes searchItem : searchItems) {
	    if(searchItem.equals(SearchItemTypes.BLOOD_REQUEST)) {
		List<BloodRequestModel> list_blood_requests = bloodRequestBo.searchAll();
		if(list_blood_requests != null) {
		    searchResponse.setSet_blood_requests(searchResponseSetFromBloodRequestModel(list_blood_requests));
		}
	    }
	    if(searchItem.equals(SearchItemTypes.BLOOD_PROFILE)) {
		List<UserBloodProfileModel> list_blood_profiles = bloodProfileBo.searchAll();
		if(list_blood_profiles != null)
		    searchResponse.setSet_blood_profiles(searchResponseSetFromBloodProfileModel(list_blood_profiles));	
	    }
	    if(searchItem.equals(SearchItemTypes.EVENTS)) {
		List<EventModel> list_events = eventBo.searchAll();
		if(list_events != null)
		    searchResponse.setSet_events(searchResponseSetFromEventsModel(list_events));
	    }
	    
	}
	return searchResponse;
    }

    private SearchResponse searachViaBounds(SearchRequest searchRequest)
    {

	SearchResponse searchResponse = new SearchResponse();
	Set<SearchItemTypes> searchItems = searchRequest.getSearchItems();
	for(SearchItemTypes searchItem : searchItems) {
	    if(searchItem.equals(SearchItemTypes.BLOOD_REQUEST)) {
		List<BloodRequestModel> list_blood_requests = bloodRequestBo.searchViaRange(searchRequest.getSouthWestLocation(), 
			searchRequest.getNorthEastLocation());
		if(list_blood_requests != null) {
		    searchResponse.setSet_blood_requests(searchResponseSetFromBloodRequestModel(list_blood_requests));
		}
	    }
	    if(searchItem.equals(SearchItemTypes.BLOOD_PROFILE)) {
		List<UserBloodProfileModel> list_blood_profiles = bloodProfileBo.searchViaRange(searchRequest.getSouthWestLocation(), 
			searchRequest.getNorthEastLocation());
		if(list_blood_profiles != null)
		    searchResponse.setSet_blood_profiles(searchResponseSetFromBloodProfileModel(list_blood_profiles));	
	    }
	    if(searchItem.equals(SearchItemTypes.EVENTS)) {
		List<EventModel> list_events = eventBo.searchViaRange(searchRequest.getSouthWestLocation(), 
			searchRequest.getNorthEastLocation());
		if(list_events != null)
		    searchResponse.setSet_events(searchResponseSetFromEventsModel(list_events));
	    }
	    
	}
	return searchResponse;
    }
    
    private Set<BloodProfileSearchResponse> searchResponseSetFromBloodProfileModel(
            List<UserBloodProfileModel> list_blood_profiles) {
	Set<BloodProfileSearchResponse> set_response = new HashSet<BloodProfileSearchResponse>();
	for(UserBloodProfileModel blood_profile : list_blood_profiles) {
	    BloodProfileSearchResponse bloodProfileSearchResponse = new BloodProfileSearchResponse();
	    Location location = new Location();
	    location.latitude = blood_profile.getLast_known_location_lat();
	    location.longitude = blood_profile.getLast_known_location_long();
	    
	    bloodProfileSearchResponse.setLast_updated_location(location);
	    bloodProfileSearchResponse.setTitle(blood_profile.getUser_account().getName());
	    bloodProfileSearchResponse.setB_p_id(blood_profile.getB_p_id());
	    if(blood_profile.getBlood_group_type() != null)
		bloodProfileSearchResponse.setBlood_grp(blood_profile.getBlood_group_type().toString());
	    
	    set_response.add(bloodProfileSearchResponse);
	}
	
	return set_response;
    }

    private Set<EventSearchResponse> searchResponseSetFromEventsModel(List<EventModel> list_events) {
	Set<EventSearchResponse > set_response = new HashSet<EventSearchResponse>();
	for(EventModel event : list_events) {
	    EventSearchResponse eventSearchResponse = new EventSearchResponse();
	    Location location = new Location();
	    location.latitude = event.getLocation_lat();
	    location.longitude = event.getLocation_long();
	    
	    eventSearchResponse.setLast_updated_location(location);
	    eventSearchResponse.setTitle(event.getName());
	    eventSearchResponse.setVenue(event.getLocation_address());
	    eventSearchResponse.setE_id(event.getE_id());
	    String master_start_datetime = DateTimeHelper.convertJavaDateToString(event.getMaster_start_datetime(),
	   		JodaTimeFormatters.dateFormatter);
	    eventSearchResponse.setScheduled_date_time(master_start_datetime);
	    set_response.add(eventSearchResponse);
	}
	
	return set_response;
    }

    private Set<BloodRequestSearchResponse> searchResponseSetFromBloodRequestModel(List<BloodRequestModel> list_blood_requests) {
	Set<BloodRequestSearchResponse > set_response = new HashSet<BloodRequestSearchResponse>();
	for(BloodRequestModel bloodRequest : list_blood_requests) {
	    BloodRequestSearchResponse bloodRequestSearchResponse = new BloodRequestSearchResponse();
	    Location location = new Location();
	    location.latitude = bloodRequest.getGps_location_lat();
	    location.longitude =  bloodRequest.getGps_location_long(); 
	    bloodRequestSearchResponse.setlocation(location);
	    bloodRequestSearchResponse.setSummary(bloodRequest.getDescription());
	    bloodRequestSearchResponse.setB_r_id(bloodRequest.getB_r_id());
	    bloodRequestSearchResponse.setTitle(bloodRequest.getPatient_name());
	    
	    String blood_grps_str = null;
	    for(BloodGroupsModel bloodGroupsModel: bloodRequest.getSet_blood_group()) {
		BloodGroupType groupType = bloodGroupsModel.getBloodGroupTypeEnum();
		if(blood_grps_str == null)
		    blood_grps_str = groupType.toString();
		else
		    blood_grps_str = blood_grps_str + ", " + groupType.toString();
 	    }
	    bloodRequestSearchResponse.setBlood_grps_requested_str(blood_grps_str);
	    set_response.add(bloodRequestSearchResponse);
	}
	
	return set_response;
    }
}
