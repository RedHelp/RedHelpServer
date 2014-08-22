package org.redhelp.model;

import java.util.Date;
import java.util.Set;

public class SlotModel implements Comparable<SlotModel>{
    
    //System related fields
    private Long s_id;
    private Long e_id;
    
    //Date time related fields
    private Date start_datetime;
    private Date end_datetime;
    
    private int max_attendees;
    private int current_attendees;
    private int max_volunteers;
    private int current_volunteers;
    
    private Set<UserBloodProfileModel> users_coming;
    
    public Long getS_id() {
        return s_id;
    }
    public void setS_id(Long s_id) {
        this.s_id = s_id;
    }
    public Long getE_id() {
        return e_id;
    }
    public void setE_id(Long e_id) {
        this.e_id = e_id;
    }
    public Date getStart_datetime() {
        return start_datetime;
    }
    public void setStart_datetime(Date start_datetime) {
        this.start_datetime = start_datetime;
    }
    public Date getEnd_datetime() {
        return end_datetime;
    }
    public void setEnd_datetime(Date end_datetime) {
        this.end_datetime = end_datetime;
    }
    public int getMax_attendees() {
        return max_attendees;
    }
    public void setMax_attendees(int max_attendees) {
        this.max_attendees = max_attendees;
    }
    public int getCurrent_attendees() {
        return current_attendees;
    }
    public void setCurrent_attendees(int current_attendees) {
        this.current_attendees = current_attendees;
    }
    public int getMax_volunteers() {
        return max_volunteers;
    }
    public void setMax_volunteers(int max_volunteers) {
        this.max_volunteers = max_volunteers;
    }
    public int getCurrent_volunteers() {
        return current_volunteers;
    }
    public void setCurrent_volunteers(int current_volunteers) {
        this.current_volunteers = current_volunteers;
    }
    
    public Set<UserBloodProfileModel> getUsers_coming() {
        return users_coming;
    }
    public void setUsers_coming(Set<UserBloodProfileModel> users_coming) {
        this.users_coming = users_coming;
    }
   
    @Override
    public String toString() {
	return "SlotModel [s_id=" + s_id + ", e_id=" + e_id + ", start_datetime=" + start_datetime + ", end_datetime="
	        + end_datetime + ", max_attendees=" + max_attendees + ", current_attendees=" + current_attendees
	        + ", max_volunteers=" + max_volunteers + ", current_volunteers=" + current_volunteers
	        + "]";
    }
    @Override
    public int compareTo(SlotModel that) {
	return (this.start_datetime.before(that.start_datetime)) ? 1 : -1;
    }
}
