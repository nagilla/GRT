package com.avaya.grt.mappers;
import java.io.Serializable;

public class HardwareServer  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String hardwareServerId, template, productType, hardwareServerCode, hardwareServerDescription;

	public String getHardwareServerDescription() {
		return hardwareServerDescription;
	}

	public void setHardwareServerDescription(String hardwareServerDescription) {
		this.hardwareServerDescription = hardwareServerDescription;
	}

	public String getHardwareServerId() {
		return hardwareServerId;
	}

	public String getHardwareServerCode() {
		return hardwareServerCode;
	}

	public void setHardwareServerCode(String hardwareServerCode) {
		this.hardwareServerCode = hardwareServerCode;
	}

	public void setHardwareServerId(String hardwareServerId) {
		this.hardwareServerId = hardwareServerId;
	}



	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
	
	
}