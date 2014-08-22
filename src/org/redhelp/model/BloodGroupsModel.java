package org.redhelp.model;

import java.util.HashSet;
import java.util.Set;

import org.redhelp.common.types.BloodGroupType;

public class BloodGroupsModel {
    private Long b_g_id;
    private String name;
   
    public static BloodGroupsModel getBloodGroupModel(BloodGroupType bloodGroupType) {
	BloodGroupsModel model = null;
	switch(bloodGroupType) {
	case A:
	    model = new  BloodGroupsModel();
	    model.setName("A");
	    break;
	case AB:
	    model = new  BloodGroupsModel();
	    model.setName("AB");
	    break;
	case AB_:
	    model = new  BloodGroupsModel();
	    model.setName("AB_");
	    break;
	case A_:
	    model = new  BloodGroupsModel();
	    model.setName("A_");
	    break;
	case B:
	    model = new  BloodGroupsModel();
	    model.setName("B");
	    break;
	case B_:
	    model = new  BloodGroupsModel();
	    model.setName("B_");
	    break;
	case O:
	    model = new  BloodGroupsModel();
	    model.setName("O");
	    break;
	case O_:
	    model = new  BloodGroupsModel();
	    model.setName("O_");
	    break;
	}
	return model;
    }
    
    public static Set<BloodGroupType> getListBloodGroups(Set<BloodGroupsModel> groups) {
	Set<BloodGroupType> list_blood_groups = new HashSet<BloodGroupType>();
	for(BloodGroupsModel model:groups) {
	    if(model!=null)
		list_blood_groups.add(model.getBloodGroupTypeEnum());
	}
	return list_blood_groups;
    }
    
    public BloodGroupType getBloodGroupTypeEnum() {
	if("A".equals(name)) {
	    return BloodGroupType.A;
	} else if("A_".equals(name)) {
	    return BloodGroupType.A_;
	} else if("AB".equals(name)) {
	    return BloodGroupType.AB;
	} else if("AB_".equals(name)) {
	    return BloodGroupType.AB_;
	} else if("B".equals(name)) {
	    return BloodGroupType.B;
	} else if("B_".equals(name)) {
	    return BloodGroupType.B_;
	} else if("O".equals(name)) {
	    return BloodGroupType.O;
	} else if("O_".equals(name)) {
	    return BloodGroupType.O_;
	} 
	return null;
    }
    
    
    
    public Long getB_g_id() {
        return b_g_id;
    }
    public void setB_g_id(Long b_g_id) {
        this.b_g_id = b_g_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
	return "BloodGroupsModel [b_g_id=" + b_g_id + ", name=" + name + "]";
    }
    
   
}
