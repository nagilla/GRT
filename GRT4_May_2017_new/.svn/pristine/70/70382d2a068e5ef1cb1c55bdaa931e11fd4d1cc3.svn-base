package com.avaya.grt.mappers;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The persistent class for the SUPER_USERS database table.
 * 
 * @author BEA Workshop
 */
public class SuperUser  implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String userId;
	private String company;
	private String createdBy;
	private java.sql.Timestamp createdDate;
	private String firstName;
	private String isSuperUser;
	private String lastName;
	private String updatedBy;
	private java.sql.Timestamp updateDate;
	private String megaUser;

    public SuperUser() {
    }

	public String getUserId() {
		return this.userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCompany() {
		return this.company;
	}
	public void setCompany(String company) {
		this.company = company;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public java.sql.Timestamp getCreatedDate() {
		return this.createdDate;
	}
	public void setCreatedDate(java.sql.Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getFirstName() {
		return this.firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getIsSuperUser() {
		return this.isSuperUser;
	}
	public void setIsSuperUser(String isSuperUser) {
		this.isSuperUser = isSuperUser;
	}

	public String getLastName() {
		return this.lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public java.sql.Timestamp getUpdateDate() {
		return this.updateDate;
	}
	public void setUpdateDate(java.sql.Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SuperUser)) {
			return false;
		}
		SuperUser castOther = (SuperUser)other;
		return new EqualsBuilder()
			.append(this.getUserId(), castOther.getUserId())
			.isEquals();
    }
    
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getUserId())
			.toHashCode();
    }   

	public String toString() {
		return new ToStringBuilder(this)
			.append("userId", getUserId())
			.toString();
	}

	public String isMegaUser() {
		return megaUser;
	}

	public void setMegaUser(String megaUser) {
		this.megaUser = megaUser;
	}
}