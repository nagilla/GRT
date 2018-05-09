package com.grt.dto;

import java.io.Serializable;

public class InstallBaseCreationResponseDto implements Serializable{
	private static final long serialVersionUID = 1L;

	private String transactioId;
	private String errorId;
	private String code;
	private String description;

	public String getTransactionId() {
		return transactioId;
	}
	public void setTransactioId(String transactioId) {
		this.transactioId = transactioId;
	}
	public String getErrorId() {
		return errorId;
	}
	public void setErrorId(String errorId) {
		this.errorId = errorId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
