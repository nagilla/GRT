package com.avaya.grt.mappers;
import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.grt.util.ProcessStepEnum;

/**
 * The persistent class for the PROCESS_STEP database table.
 * 
 * @author BEA Workshop
 */
public class ProcessStep  implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String processStepId;
	private String processStepDescription;
	private String processStepShortDescription;
	private java.util.Set<SiteRegistration> siteRegistrations;

    public ProcessStep() {
    }

	public String getProcessStepId() {
		return this.processStepId;
	}
	public void setProcessStepId(String processStepId) {
		this.processStepId = processStepId;
	}

	public String getProcessStepDescription() {
		return this.processStepDescription;
	}
	public void setProcessStepDescription(String processStepDescription) {
		this.processStepDescription = processStepDescription;
	}

	public String getProcessStepShortDescription() {
		return this.processStepShortDescription;
	}
	public void setProcessStepShortDescription(String processStepShortDescription) {
		this.processStepShortDescription = processStepShortDescription;
	}

	//bi-directional many-to-one association to SiteRegistration
	public java.util.Set<SiteRegistration> getSiteRegistrations() {
		return this.siteRegistrations;
	}
	public void setSiteRegistrations(java.util.Set<SiteRegistration> siteRegistrations) {
		this.siteRegistrations = siteRegistrations;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ProcessStep)) {
			return false;
		}
		ProcessStep castOther = (ProcessStep)other;
		return new EqualsBuilder()
			.append(this.getProcessStepId(), castOther.getProcessStepId())
			.isEquals();
    }
    
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getProcessStepId())
			.toHashCode();
    }   

	public String toString() {
		return new ToStringBuilder(this)
			.append("processStepId", getProcessStepId())
			.toString();
	}
	
	public ProcessStepEnum getProcessStepEnum(){
		ProcessStepEnum processStep = null;
		if(StringUtils.isNotEmpty(this.processStepId) && this.processStepId.equals(ProcessStepEnum.INSTALL_BASE_CREATION.getProcessStepId())){
			processStep = ProcessStepEnum.INSTALL_BASE_CREATION;
		}else if(StringUtils.isNotEmpty(this.processStepId) && this.processStepId.equals(ProcessStepEnum.TECHNICAL_REGISTRATION.getProcessStepId())){
			processStep = ProcessStepEnum.TECHNICAL_REGISTRATION;
		}else if(StringUtils.isNotEmpty(this.processStepId) && this.processStepId.equals(ProcessStepEnum.FINAL_RECORD_VALIDATION.getProcessStepId())){
			processStep = ProcessStepEnum.FINAL_RECORD_VALIDATION;
		}
		return processStep;
	}
}