package com.grt.dto;
import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The dto class for the SRRequest.
 * 
 * @author BEA Workshop
 */
public class SRRequestDto  implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String srRequestId;
	private String siebelSRNo;
	private Date createdDate;
	private Date updatedDate;
	private String statusId;

    public SRRequestDto() {
    }

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getSiebelSRNo() {
		return siebelSRNo;
	}

	public void setSiebelSRNo(String siebelSRNo) {
		this.siebelSRNo = siebelSRNo;
	}

	public String getSrRequestId() {
		return srRequestId;
	}

	public void setSrRequestId(String srRequestId) {
		this.srRequestId = srRequestId;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("srRequestId", getSrRequestId())
			.toString();
	}
}