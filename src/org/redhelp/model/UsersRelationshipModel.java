package org.redhelp.model;

import org.redhelp.types.UserRelationshipType;

public class UsersRelationshipModel {
    private Long u_r_id; 
    private Long b_p_id;
    private Long related_b_p_id;
    private UserRelationshipType userRelationshipType;
    
    public Long getU_r_id() {
        return u_r_id;
    }
    public void setU_r_id(Long u_r_id) {
        this.u_r_id = u_r_id;
    }
    public UserRelationshipType getUserRelationshipType() {
        return userRelationshipType;
    }
    public void setUserRelationshipType(UserRelationshipType userRelationshipType) {
        this.userRelationshipType = userRelationshipType;
    }
    public Long getB_p_id() {
        return b_p_id;
    }
    public void setB_p_id(Long b_p_id) {
        this.b_p_id = b_p_id;
    }
    public Long getRelated_b_p_id() {
        return related_b_p_id;
    }
    public void setRelated_b_p_id(Long related_b_p_id) {
        this.related_b_p_id = related_b_p_id;
    }
    @Override
    public String toString() {
	return "UsersRelationshipModel [u_r_id=" + u_r_id + ", b_p_id=" + b_p_id + ", related_b_p_id=" + related_b_p_id
	        + ", userRelationshipType=" + userRelationshipType + "]";
    }
    
    
}
