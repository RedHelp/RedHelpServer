package org.redhelp.model;

import java.util.Date;
import java.util.Set;
import java.util.SortedSet;

import javax.management.Notification;

import org.redhelp.common.types.BloodGroupType;
import org.redhelp.common.types.Gender;

public class UserBloodProfileModel implements Comparable<UserBloodProfileModel>{

    private Long b_p_id;
    public UserAccountModel user_account;
    private Double last_known_location_lat;
    private Double last_known_location_long;
    private Date last_known_location_datetime;
    private Gender gender;
    private BloodGroupType blood_group_type;
    private String city;
    private Date birth_date;
    private SortedSet<BloodRequestModel> blood_requests;
    private SortedSet<NotificationModel> notifications;
    private Set<BloodRequestModel> blood_requests_received;
    private Set<SlotModel> slots;
    
    private Set<UserBloodProfileModel> related_users;
    
    
    public Long getB_p_id() {
        return b_p_id;
    }
    public void setB_p_id(Long b_p_id) {
        this.b_p_id = b_p_id;
    }
   
    public Double getLast_known_location_lat() {
        return last_known_location_lat;
    }
    public void setLast_known_location_lat(Double last_known_location_lat) {
        this.last_known_location_lat = last_known_location_lat;
    }
    public Double getLast_known_location_long() {
        return last_known_location_long;
    }
    public void setLast_known_location_long(Double last_known_location_long) {
        this.last_known_location_long = last_known_location_long;
    }
    public Date getLast_known_location_datetime() {
        return last_known_location_datetime;
    }
    public void setLast_known_location_datetime(Date last_known_location_datetime) {
        this.last_known_location_datetime = last_known_location_datetime;
    }
    public Gender getGender() {
        return gender;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    public BloodGroupType getBlood_group_type() {
        return blood_group_type;
    }
    public void setBlood_group_type(BloodGroupType blood_group_type) {
        this.blood_group_type = blood_group_type;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
 
    public Date getBirth_date() {
        return birth_date;
    }
    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }
    public UserAccountModel getUser_account() {
	return user_account;
    }
    public void setUser_account(UserAccountModel user_account) {
	this.user_account = user_account;
    }
    public SortedSet<BloodRequestModel> getBlood_requests() {
        return blood_requests;
    }
    public void setBlood_requests(SortedSet<BloodRequestModel> blood_requests) {
        this.blood_requests = blood_requests;
    }
    public Set<BloodRequestModel> getBlood_requests_received() {
        return blood_requests_received;
    }
    public void setBlood_requests_received(Set<BloodRequestModel> blood_requests_received) {
        this.blood_requests_received = blood_requests_received;
    }
    
    public Set<SlotModel> getSlots() {
        return slots;
    }
    public void setSlots(Set<SlotModel> slots) {
        this.slots = slots;
    }
    
    @Override
    public String toString() {
	return "UserBloodProfileModel [b_p_id=" + b_p_id + ", user_account=" + user_account
	        + ", last_known_location_lat=" + last_known_location_lat + ", last_known_location_long="
	        + last_known_location_long + ", last_known_location_datetime=" + last_known_location_datetime
	        + ", gender=" + gender + ", blood_group_type=" + blood_group_type + ", city=" + city + ", birth_date="
	        + birth_date + ", blood_requests=" + blood_requests + ", blood_requests_received="
	        + blood_requests_received + ", slots=" + slots + "]" + "notifications=" + notifications;
    }
    @Override
    public int compareTo(UserBloodProfileModel o) {
	// TODO Auto-generated method stub
	return 0;
    }
    public SortedSet<NotificationModel> getNotifications() {
	return notifications;
    }
    public void setNotifications(SortedSet<NotificationModel> notifications) {
	this.notifications = notifications;
    }
    public Set<UserBloodProfileModel> getRelated_users() {
	return related_users;
    }
    public void setRelated_users(Set<UserBloodProfileModel> related_users) {
	this.related_users = related_users;
    }
    
    
   
}
