package com.avaya.grt.mappers;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The persistent class for the COUNTRY_ALARM_RECEIVER database table.
 * 
 * @author BEA Workshop
 */
public class CountryAlarmReceiver  implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String countryCode;
	private String phoneNo;

    public CountryAlarmReceiver() {
    }

	public String getCountryCode() {
		return this.countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getPhoneNo() {
		return this.phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof CountryAlarmReceiver)) {
			return false;
		}
		CountryAlarmReceiver castOther = (CountryAlarmReceiver)other;
		return new EqualsBuilder()
			.append(this.getCountryCode(), castOther.getCountryCode())
			.isEquals();
    }
    
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getCountryCode())
			.toHashCode();
    }   

	public String toString() {
		return new ToStringBuilder(this)
			.append("countryCode", getCountryCode())
			.toString();
	}
}