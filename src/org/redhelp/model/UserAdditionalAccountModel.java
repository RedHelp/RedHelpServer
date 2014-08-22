package org.redhelp.model;

public class UserAdditionalAccountModel {

	private Long uaa_id;
	private UserAdditionalAccountTypeModel accountType;
	private String externalAccountId;
	private UserAccountModel user_account;
	
	public Long getUaa_id() {
		return uaa_id;
	}
	public void setUaa_id(Long uaa_id) {
		this.uaa_id = uaa_id;
	}
	
	public UserAdditionalAccountTypeModel getAccountType() {
	    return accountType;
	}
	public void setAccountType(UserAdditionalAccountTypeModel accountType) {
	    this.accountType = accountType;
	}
	public String getExternalAccountId() {
		return externalAccountId;
	}
	public void setExternalAccountId(String externalAccountId) {
		this.externalAccountId = externalAccountId;
	}
	public UserAccountModel getUser_account() {
	    return user_account;
	}
	public void setUser_account(UserAccountModel user_account) {
	    this.user_account = user_account;
	}
	@Override
        public String toString() {
	    return "UserAdditionalAccountModel [uaa_id=" + uaa_id + ", accountType=" + accountType
	            + ", externalAccountId=" + externalAccountId + ", user_account=" + user_account + "]";
        }
	
	
	
	
}
