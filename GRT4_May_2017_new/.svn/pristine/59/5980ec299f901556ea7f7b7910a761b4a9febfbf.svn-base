package com.grt.util;

import java.io.Serializable;

public enum ProcessStepEnum implements Serializable {    

	INSTALL_BASE_CREATION ("P0001", "", "Install Base Creation"),
	TECHNICAL_REGISTRATION ("P0002", "", "Technical On-Boarding"),
	FINAL_RECORD_VALIDATION ("P0003", "", "Record Validation"),
	//GRT4.0 - New Process step added in enum and process_step table in GRT DB
	EQUIPMENT_MOVE ("P0004", "", "Equipment Move");
	
	private String processStepId;
	private String processStepDescription;
	private String processStepShortDescription;

	ProcessStepEnum(String processStepId, String processStepDescription, String processStepShortDescription){
		this.processStepId = processStepId;
		this.processStepDescription = processStepDescription;
		this.processStepShortDescription = processStepShortDescription;
	  }

	public String getProcessStepDescription() {
		return processStepDescription;
	}

	public String getProcessStepId() {
		return processStepId;
	}

	public String getProcessStepShortDescription() {
		return processStepShortDescription;
	}
	

}
