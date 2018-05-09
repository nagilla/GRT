package com.avaya.grt.mappers;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * The persistent class for the STATE database table.
 * 
 * @author BEA Workshop
 */
public class State implements Serializable{
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String stateId;
	private String state;
	private Country country;
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getStateId() {
		return stateId;
	}
	public void setStateId(String stateId) {
		this.stateId = stateId;
	}
	
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof State)) {
			return false;
		}
		State castOther = (State)other;
		return new EqualsBuilder()
			.append(this.getStateId(), castOther.getStateId())
			.isEquals();
    }
    
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getStateId())
			.toHashCode();
    }   

	public String toString() {
		return new ToStringBuilder(this)
			.append("materialCode", getStateId()).toString();
	}
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}	
	
}
