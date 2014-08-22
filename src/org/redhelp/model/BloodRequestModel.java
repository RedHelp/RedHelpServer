package org.redhelp.model;

import java.util.Date;
import java.util.Set;

import org.redhelp.common.types.BloodRequirementType;
//TODO add responses feature. 
public class BloodRequestModel implements Comparable<BloodRequestModel>{
    
    //System related fields
    private Long b_r_id;
    private boolean active;
    private Long b_p_id;
    private Date creation_datetime;
    
    //Functional fields
    private String patient_name;
    private Set<BloodGroupsModel> set_blood_group;
    private BloodRequirementType blood_requirement_type;
    private String description;
    private String units;
    private String phone_number;
    
    private Set<UserBloodProfileModel> blood_request_receivers_profiles;
    
    //Location fields
    private Double gps_location_lat;
    private Double gps_location_long;
    private Double place_location_lat;
    private Double place_location_long;
    private String place_string;
    

    
    
    public Set<BloodGroupsModel> getSet_blood_group() {
        return set_blood_group;
    }

    public void setSet_blood_group(Set<BloodGroupsModel> set_blood_group) {
        this.set_blood_group = set_blood_group;
    }

    public BloodRequirementType getBlood_requirement_type() {
        return blood_requirement_type;
    }

    public void setBlood_requirement_type(BloodRequirementType blood_requirement_type) {
        this.blood_requirement_type = blood_requirement_type;
    }

    public Double getGps_location_lat() {
        return gps_location_lat;
    }

    public void setGps_location_lat(Double gps_location_lat) {
        this.gps_location_lat = gps_location_lat;
    }

    public Double getGps_location_long() {
        return gps_location_long;
    }

    public void setGps_location_long(Double gps_location_long) {
        this.gps_location_long = gps_location_long;
    }

    public Double getPlace_location_lat() {
        return place_location_lat;
    }

    public void setPlace_location_lat(Double place_location_lat) {
        this.place_location_lat = place_location_lat;
    }

    public Double getPlace_location_long() {
        return place_location_long;
    }

    public void setPlace_location_long(Double place_location_long) {
        this.place_location_long = place_location_long;
    }

    public String getPlace_string() {
        return place_string;
    }

    public void setPlace_string(String place_string) {
        this.place_string = place_string;
    }

    public Long getB_r_id() {
        return b_r_id;
    }

    public void setB_r_id(Long b_r_id) {
        this.b_r_id = b_r_id;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getB_p_id() {
        return b_p_id;
    }

    public void setB_p_id(Long b_p_id) {
        this.b_p_id = b_p_id;
    }

    public Date getCreation_datetime() {
        return creation_datetime;
    }

    public void setCreation_datetime(Date creation_datetime) {
        this.creation_datetime = creation_datetime;
    }

    @Override
    public int compareTo(BloodRequestModel that) {
	return (this.creation_datetime.before(that.creation_datetime)) ? 1 : -1;
    }
    

    public Set<UserBloodProfileModel> getBlood_request_receivers_profiles() {
        return blood_request_receivers_profiles;
    }

    public void setBlood_request_receivers_profiles(Set<UserBloodProfileModel> blood_request_receivers_profiles) {
        this.blood_request_receivers_profiles = blood_request_receivers_profiles;
    }
    

    @Override
    public String toString() {
	return "BloodRequestModel [b_r_id=" + b_r_id + ", active=" + active + ", b_p_id=" + b_p_id
	        + ", creation_datetime=" + creation_datetime + ", patient_name=" + patient_name  + ", set_blood_group=" + set_blood_group + ", blood_requirement_type="
	        + blood_requirement_type + ", description=" + description + ", units=" + units + ", phone_number="
	        + phone_number 
	        + ", gps_location_lat=" + gps_location_lat + ", gps_location_long=" + gps_location_long
	        + ", place_location_lat=" + place_location_lat + ", place_location_long=" + place_location_long
	        + ", place_string=" + place_string + "]";
    }

    	
    
    
}
