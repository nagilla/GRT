package com.grt.util;

public enum TechRecordEnum {
	

	SAL_MIGRATION ("SALMIG", "Sal Migration"),
	SAL_NON_UI_PRODUCT_REGISTRATION ("SALNONUI", "Sal Non UI Product Registration"),
	SAL_UI_PRODUCT_REGISTRATION ("SALUI", "Sal Non UI Product Registration"),	
	SAL_SITE_LIST ("SALLIST", "Sal Sal Site List"),
	SAL_NON_UI_PRODUCT_LIST ("SALNONUILIST", "Sal Non Ui Product List"),
	SAL_UI_PRODUCT_LIST ("SALUILIST", "Sal Ui Product List"),
	IP_MODEM ("IP-MODEM", "IP-MODEM techncial registration"),
	IPO ("IPO", "IP-Office techncial registration"),
	ALARM ("ALARM", "SAL-Connectivity/Alarm Testing"),
	TR("TR", "Technical Registration");
	
	private String salType;
	private String salTypeDesc;
	private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	TechRecordEnum(String salType, String salTypeDesc) {
		this.salType = salType;
		this.salTypeDesc = salTypeDesc;
		this.type = salType;
	}
	
	public String getSalType() {
		return salType;
	}
	
	public String getSalTypeDesc() {
		return salTypeDesc;
	}
}
