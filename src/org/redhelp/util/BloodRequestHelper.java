package org.redhelp.util;

import java.util.HashSet;
import java.util.Set;

import org.redhelp.bo.BloodProfile;
import org.redhelp.model.UserBloodProfileModel;

public class BloodRequestHelper {
    
    //TODO Create algorithm to choose potential donors
    public static Set<UserBloodProfileModel> getPotentialDonors(BloodProfile bloodProfileBo) {
	Set<UserBloodProfileModel> sortedSetOfDonors = new HashSet<UserBloodProfileModel>();
	sortedSetOfDonors.add(bloodProfileBo.getBloodProfileModel(78l));
	sortedSetOfDonors.add(bloodProfileBo.getBloodProfileModel(80l));
	return sortedSetOfDonors;
    }

}
