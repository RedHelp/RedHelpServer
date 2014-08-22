package org.redhelp.model;

import java.util.Date;
import java.util.SortedSet;

public class EventModel {

    //System related fields 
    private Long e_id;
    private Long b_p_id;
    private Date creation_datetime;
    private boolean active;
    
    // Event related fields
    private String name;
    private String phone_number;
    private String additional_email;
    private String organization;
    
    
    //Location related fields
    private String location_name;
    private String location_address;
    private Double location_lat;
    private Double location_long;
    
    //Time and slots related fields
    private Date master_start_datetime;
    private Date master_end_datetime;
    
    private SortedSet<SlotModel> slots;
    
    public Long getE_id() {
	
        return e_id;
    }
    public void setE_id(Long e_id) {
        this.e_id = e_id;
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
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone_number() {
        return phone_number;
    }
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
    public String getAdditional_email() {
        return additional_email;
    }
    public void setAdditional_email(String additional_email) {
        this.additional_email = additional_email;
    }
    public String getOrganization() {
        return organization;
    }
    public void setOrganization(String organization) {
        this.organization = organization;
    }
    public String getLocation_name() {
        return location_name;
    }
    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }
    public String getLocation_address() {
        return location_address;
    }
    public void setLocation_address(String location_address) {
        this.location_address = location_address;
    }
    public Double getLocation_lat() {
        return location_lat;
    }
    public void setLocation_lat(Double location_lat) {
        this.location_lat = location_lat;
    }
    public Double getLocation_long() {
        return location_long;
    }
    public void setLocation_long(Double location_long) {
        this.location_long = location_long;
    }
    public Date getMaster_start_datetime() {
        return master_start_datetime;
    }
    public void setMaster_start_datetime(Date master_start_datetime) {
        this.master_start_datetime = master_start_datetime;
    }
    public Date getMaster_end_datetime() {
        return master_end_datetime;
    }
    public void setMaster_end_datetime(Date master_end_datetime) {
        this.master_end_datetime = master_end_datetime;
    }
    public SortedSet<SlotModel> getSlots() {
        return slots;
    }
    public void setSlots(SortedSet<SlotModel> slots) {
        this.slots = slots;
    }
    
    
    @Override
    public String toString() {
	return "EventModel [e_id=" + e_id + ", b_p_id=" + b_p_id + ", creation_datetime=" + creation_datetime
	        + ", active=" + active + ", name=" + name + ", phone_number=" + phone_number + ", additional_email="
	        + additional_email + ", organization=" + organization + ", location_name=" + location_name
	        + ", location_address=" + location_address + ", locaiton_lat=" + location_lat + ", location_long="
	        + location_long + ", master_start_datetime=" + master_start_datetime + ", master_end_datetime="
	        + master_end_datetime + "]";
    }
    
    
    
    
}
