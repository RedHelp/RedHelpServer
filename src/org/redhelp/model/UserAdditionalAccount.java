package org.redhelp.model;

public class UserAdditionalAccount {

	private Long uaa_id;
	private Long accountType;
	private String externalAccountId;
	private Long u_id;
	
	public Long getUaa_id() {
		return uaa_id;
	}
	public void setUaa_id(Long uaa_id) {
		this.uaa_id = uaa_id;
	}
	public Long getAccountType() {
		return accountType;
	}
	public void setAccountType(Long accountType) {
		this.accountType = accountType;
	}
	public String getExternalAccountId() {
		return externalAccountId;
	}
	public void setExternalAccountId(String externalAccountId) {
		this.externalAccountId = externalAccountId;
	}
	public Long getU_id() {
		return u_id;
	}
	public void setU_id(Long u_id) {
		this.u_id = u_id;
	}
	
	@Override
	public String toString() {
		return "UserAdditionalAccount [uaa_id=" + uaa_id + ", accountType="
				+ accountType + ", externalAccountId=" + externalAccountId
				+ ", u_id=" + u_id + "]";
	}
	
	
}
