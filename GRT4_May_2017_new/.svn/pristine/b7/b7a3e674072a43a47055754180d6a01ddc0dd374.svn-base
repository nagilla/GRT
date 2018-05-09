package com.avaya.grt.mappers;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The persistent class for the COMPATIBILITY_MATRIX database table.
 * 
 * @author BEA Workshop
 */
public class CompatibilityMatrix  implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String seCode;
	private String model;
	private String remoteAccess;
	private String transportAlarm;
	private boolean salOnly;
	
	public boolean isSalOnly() {
		return salOnly;
	}

	public void setSalOnly(boolean salOnly) {
		this.salOnly = salOnly;
	}
	
    public CompatibilityMatrix() {
    }

	public String getSeCode() {
		return this.seCode;
	}
	public void setSeCode(String seCode) {
		this.seCode = seCode;
	}

	public String getModel() {
		return this.model;
	}
	public void setModel(String model) {
		this.model = model;
	}

	public String getRemoteAccess() {
		return this.remoteAccess;
	}
	public void setRemoteAccess(String remoteAccess) {
		this.remoteAccess = remoteAccess;
	}

	public String getTransportAlarm() {
		return this.transportAlarm;
	}
	public void setTransportAlarm(String transportAlarm) {
		this.transportAlarm = transportAlarm;
	}
}