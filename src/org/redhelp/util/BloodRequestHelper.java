package org.redhelp.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.redhelp.bo.BloodProfile;
import org.redhelp.common.types.BloodGroupType;
import org.redhelp.common.types.Location;
import org.redhelp.model.UserBloodProfileModel;

public class BloodRequestHelper {
    
    //TODO Create algorithm to choose potential donors
    public static Set<UserBloodProfileModel> getPotentialDonors(BloodProfile bloodProfileBo, Long creator_b_p_id, Set<BloodGroupType> listBloodGroup) {
	Set<UserBloodProfileModel> sortedSetOfDonors = new HashSet<UserBloodProfileModel>();
	
	UserBloodProfileModel creatorBloodProfile = bloodProfileBo.getBloodProfileModel(creator_b_p_id);
	Location creatorLocation = new Location();
	creatorLocation.latitude = creatorBloodProfile.getLast_known_location_lat();
	creatorLocation.longitude = creatorBloodProfile.getLast_known_location_long();
	
	List<UserBloodProfileModel> listOfBloodDonorsFilterViaLocation = filterViaLocation(creatorLocation, bloodProfileBo);
	
	List<UserBloodProfileModel> listOfBloodDonorsFilterViaBloodGroup = filterViaBloodGroup(listOfBloodDonorsFilterViaLocation,
		listBloodGroup, creator_b_p_id);
	
	if(listOfBloodDonorsFilterViaBloodGroup != null)
	    return new HashSet<UserBloodProfileModel>(listOfBloodDonorsFilterViaBloodGroup);
	else return null;
    }

    private static List<UserBloodProfileModel> filterViaLocation(Location creatorLocation, BloodProfile bloodProfileBo) {
	
	Location northEastBound = LocationHelper.addDistanceToLocation(creatorLocation, 0.22);
	Location southWestBound = LocationHelper.subtractDistanceToLocation(creatorLocation, 0.22);
	
	List<UserBloodProfileModel> listOfBloodDonors = bloodProfileBo.searchViaRange(southWestBound, northEastBound);
	
	return listOfBloodDonors;
    }
    
    private static List<UserBloodProfileModel> filterViaBloodGroup(List<UserBloodProfileModel> listOfBloodProfiles, 
	    Set<BloodGroupType> listBloodGroup, Long creator_b_p_id) {
	List<UserBloodProfileModel> listBloodProfilesToReturn = new LinkedList<UserBloodProfileModel>();
	
	for(UserBloodProfileModel bloodProfile : listOfBloodProfiles) {
	    if(bloodProfile.getB_p_id().equals(creator_b_p_id))
		continue;
	    
	    boolean isOK = listBloodGroup.contains(bloodProfile.getBlood_group_type());
	    if(isOK) {
		listBloodProfilesToReturn.add(bloodProfile);
	    }
	}
	
	List<UserBloodProfileModel> limitBloodProfiles = new LinkedList<UserBloodProfileModel>();
	
	for(int i=0;i<5;i++) {
	    if(listBloodProfilesToReturn.size() == 0)
		break;
	    Collections.shuffle(listBloodProfilesToReturn);
	    limitBloodProfiles.add(listBloodProfilesToReturn.get(0));
	    listBloodProfilesToReturn.remove(0);
	}
	return limitBloodProfiles;
    }
}
