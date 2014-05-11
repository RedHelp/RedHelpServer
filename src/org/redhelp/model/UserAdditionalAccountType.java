package org.redhelp.model;

public class UserAdditionalAccountType {

	private Long uaat_id;
	private String name;
	
	public Long getUaat_id() {
		return uaat_id;
	}
	public void setUaat_id(Long uaat_id) {
		this.uaat_id = uaat_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "UserAdditionalAccountType [uaat_id=" + uaat_id + ", name="
				+ name + "]";
	}
	
	
}
