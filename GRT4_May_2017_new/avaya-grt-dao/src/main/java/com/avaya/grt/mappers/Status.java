package com.avaya.grt.mappers;
import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The persistent class for the STATUS database table.
 * 
 * @author BEA Workshop
 */
public class Status  implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String statusId;
	private String statusDescription;
	private String statusShortDescription;
	private java.util.Set<SiteRegistration> siteRegistrations;
	private java.util.Set<TechnicalRegistration> technicalRegistrations;

    public Status() {
    }

	public String getStatusId() {
		return this.statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getStatusDescription() {
		return this.statusDescription;
	}
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	public String getStatusShortDescription() {
		return this.statusShortDescription;
	}
	public void setStatusShortDescription(String statusShortDescription) {
		this.statusShortDescription = statusShortDescription;
	}

	//bi-directional many-to-one association to SiteRegistration
	public java.util.Set<SiteRegistration> getSiteRegistrations() {
		return this.siteRegistrations;
	}
	public void setSiteRegistrations(java.util.Set<SiteRegistration> siteRegistrations) {
		this.siteRegistrations = siteRegistrations;
	}

	//bi-directional many-to-one association to TechnicalRegistration
	public java.util.Set<TechnicalRegistration> getTechnicalRegistrations() {
		return this.technicalRegistrations;
	}
	public void setTechnicalRegistrations(java.util.Set<TechnicalRegistration> technicalRegistrations) {
		this.technicalRegistrations = technicalRegistrations;
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("statusId", getStatusId())
			.toString();
	}
}