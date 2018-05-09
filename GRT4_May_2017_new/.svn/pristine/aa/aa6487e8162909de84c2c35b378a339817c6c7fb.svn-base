package com.avaya.grt.mappers;
import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The persistent class for the SR_REQUEST database table.
 * 
 * @author BEA Workshop
 */
public class SRRequest  implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String srRequestId;
	private String siebelSRNo;
	private Date createdDate;
	private Date updatedDate;
	private Status status;

    public SRRequest() {
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("srRequestId", getSrRequestId())
			.toString();
	}
}