package com.avaya.grt.mappers;
import java.io.Serializable;

public class TechRegParameters implements Serializable{
	
//	default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	
	private String parameterId;
	private TechnicalRegistration techRegId;
	private SiteList siteListId;
	private String paramKey;
	private String paramValue;
	/**
	 * @return the parameterId
	 */
	public String getParameterId() {
		return parameterId;
	}
	/**
	 * @param parameterId the parameterId to set
	 */
	public void setParameterId(String parameterId) {
		this.parameterId = parameterId;
	}
	/**
	 * @return the techRegId
	 */
	public TechnicalRegistration getTechRegId() {
		return techRegId;
	}
	/**
	 * @param techRegId the techRegId to set
	 */
	public void setTechRegId(TechnicalRegistration techRegId) {
		this.techRegId = techRegId;
	}
	
	public SiteList getSiteListId() {
		return siteListId;
	}
	
	public void setSiteListId(SiteList siteListId) {
		this.siteListId = siteListId;
	}
	/**
	 * @return the paramKey
	 */
	public String getParamKey() {
		return paramKey;
	}
	/**
	 * @param paramKey the paramKey to set
	 */
	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}
	/**
	 * @return the paramValue
	 */
	public String getParamValue() {
		return paramValue;
	}
	/**
	 * @param paramValue the paramValue to set
	 */
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	
	
	
}