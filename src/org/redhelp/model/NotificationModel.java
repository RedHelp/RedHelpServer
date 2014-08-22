package org.redhelp.model;

import java.util.Date;

import org.redhelp.common.types.NotificationTypes;

public class NotificationModel implements Comparable<NotificationModel> {
    
    private Long n_id;
    private Long b_p_id;
    private String title;
    private Date creation_datetime;
    private boolean read;
    private NotificationTypes notification_type;
    private Long b_r_id;
    private Long requester_b_p_id;
    private Long e_id;
    private String message;

    public Long getN_id() {
        return n_id;
    }
    public void setN_id(Long n_id) {
        this.n_id = n_id;
    }
    public Long getB_p_id() {
        return b_p_id;
    }
    public void setB_p_id(Long b_p_id) {
        this.b_p_id = b_p_id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Date getCreation_datetime() {
	return creation_datetime;
    }
    public void setCreation_datetime(Date creation_datetime) {
	this.creation_datetime = creation_datetime;
    }
    
    public boolean isRead() {
	return read;
    }
    public void setRead(boolean read) {
	this.read = read;
    }
    
    
    public NotificationTypes getNotification_type() {
        return notification_type;
    }
    public void setNotification_type(NotificationTypes notification_type) {
        this.notification_type = notification_type;
    }
    public Long getB_r_id() {
        return b_r_id;
    }
    public void setB_r_id(Long b_r_id) {
        this.b_r_id = b_r_id;
    }
    public Long getRequester_b_p_id() {
        return requester_b_p_id;
    }
    public void setRequester_b_p_id(Long requester_b_p_id) {
        this.requester_b_p_id = requester_b_p_id;
    }
    public Long getE_id() {
        return e_id;
    }
    public void setE_id(Long e_id) {
        this.e_id = e_id;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
	return "NotificationModel [n_id=" + n_id + ", b_p_id=" + b_p_id + ", title=" + title + ", creation_datetime="
	        + creation_datetime + ", read=" + read + ", notification_type=" + notification_type + ", b_r_id="
	        + b_r_id + ", requester_b_p_id=" + requester_b_p_id + ", e_id=" + e_id + ", message=" + message + "]";
    }
    @Override
    public int compareTo(NotificationModel that) {
	return (this.creation_datetime.before(that.creation_datetime)) ? 1 : -1;
    }
}
