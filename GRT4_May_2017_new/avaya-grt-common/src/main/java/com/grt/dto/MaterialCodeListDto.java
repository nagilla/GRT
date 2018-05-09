package com.grt.dto;

import java.io.Serializable;

public class MaterialCodeListDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String materialCode;
	private String regId;
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
}
