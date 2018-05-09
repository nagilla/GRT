package com.grt.util;

public enum AccessTypeEnum {
/*	DB values =        (Modem, IP,    IPO,     SAL, SALGW )
	Required UI values (Modem, RASIP, SSL/VPN, SAL, SALGW )*/
	
	MODEM ("Modem", "Modem"),
	MODEMCAP ("MODEM", "MODEM"),
	IP ("IP", "RASIP"),
	IPO ("IPO", "SSL/VPN"),
	SAL ("SAL", "SAL"),
	SALGW ("SALGW", "SALGW");	
	
	private final String dbAccessType;
	private final String uiAccessType;
	
	AccessTypeEnum(String dbAccessType, String uiAccessType)
	{
		this.dbAccessType = dbAccessType;
		this.uiAccessType = uiAccessType;		
	}

	public String getDbAccessType() {
		return dbAccessType;
	}

	public String getUiAccessType() {
		return uiAccessType;
	}
}
