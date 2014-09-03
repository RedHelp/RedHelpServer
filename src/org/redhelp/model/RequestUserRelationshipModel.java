package org.redhelp.model;

import org.redhelp.types.RequestUserRelationshipType;

public class RequestUserRelationshipModel {
    
    private Long r_u_id; 
    private Long b_p_id;
    private Long b_r_id;
    private RequestUserRelationshipType requestUserRelationshipType;
    
    public Long getR_u_id() {
        return r_u_id;
    }
    public void setR_u_id(Long r_u_id) {
        this.r_u_id = r_u_id;
    }
    public Long getB_p_id() {
        return b_p_id;
    }
    public void setB_p_id(Long b_p_id) {
        this.b_p_id = b_p_id;
    }
    public Long getB_r_id() {
        return b_r_id;
    }
    public void setB_r_id(Long b_r_id) {
        this.b_r_id = b_r_id;
    }
    public RequestUserRelationshipType getRequestUserRelationshipType() {
        return requestUserRelationshipType;
    }
    public void setRequestUserRelationshipType(RequestUserRelationshipType requestUserRelationshipType) {
        this.requestUserRelationshipType = requestUserRelationshipType;
    }
    
    @Override
    public String toString() {
	return "RequestUserRelationshipModel [r_u_id=" + r_u_id + ", b_p_id=" + b_p_id + ", b_r_id=" + b_r_id
	        + ", requestUserRelationshipType=" + requestUserRelationshipType + "]";
    }
    
    

}
