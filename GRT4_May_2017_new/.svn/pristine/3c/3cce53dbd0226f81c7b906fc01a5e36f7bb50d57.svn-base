package com.avaya.grt.mappers;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The persistent class for the ART_ERROR_CODE database table.
 * 
 * @author BEA Workshop
 */
public class ArtErrorCode  implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String errorCode;
	private String insiteUrl;

    public ArtErrorCode() {
    }

	public String getErrorCode() {
		return this.errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getInsiteUrl() {
		return this.insiteUrl;
	}
	public void setInsiteUrl(String insiteUrl) {
		this.insiteUrl = insiteUrl;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ArtErrorCode)) {
			return false;
		}
		ArtErrorCode castOther = (ArtErrorCode)other;
		return new EqualsBuilder()
			.append(this.getErrorCode(), castOther.getErrorCode())
			.isEquals();
    }
    
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getErrorCode())
			.toHashCode();
    }   

	public String toString() {
		return new ToStringBuilder(this)
			.append("errorCode", getErrorCode())
			.toString();
	}
}