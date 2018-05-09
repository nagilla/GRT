package com.grt.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class EquipmentMoveDataDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private String systemId;	
	private String transactionId;    
    private String fromSoldToId;    
    private String toSoldToId;    
    private String returnCode;    
    private String errorDesc;
    private String routingInfo;
	private List<EquipmentMoveResponseDto> equipmentMoveResponseData;
	
	public String getSystemId() {
		return systemId;
	}
	
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	
	public String getTransactionId() {
		return transactionId;
	}
	
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	public String getFromSoldToId() {
		return fromSoldToId;
	}
	
	public void setFromSoldToId(String fromSoldToId) {
		this.fromSoldToId = fromSoldToId;
	}
	
	public String getToSoldToId() {
		return toSoldToId;
	}
	
	public void setToSoldToId(String toSoldToId) {
		this.toSoldToId = toSoldToId;
	}
	
	public String getReturnCode() {
		return returnCode;
	}
	
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	
	public String getErrorDesc() {
		return errorDesc;
	}
	
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public List<EquipmentMoveResponseDto> getEquipmentMoveResponseData() {
		if (equipmentMoveResponseData == null) {
			equipmentMoveResponseData = new ArrayList<EquipmentMoveResponseDto>();
        }
	
		return equipmentMoveResponseData;
	}

	public void setEquipmentMoveResponseData(
			List<EquipmentMoveResponseDto> equipmentMoveResponseData) {
		this.equipmentMoveResponseData = equipmentMoveResponseData;
	}

	public String getRoutingInfo() {
		return routingInfo;
	}

	public void setRoutingInfo(String routingInfo) {
		this.routingInfo = routingInfo;
	}
	
	
	
}
