package com.grt.util;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public enum MedalLevelEnum implements Serializable {    

    // Don't change the order of decleration it's used in compareTO 
    // The natural order implemented by compareTo is the order in which the constants are declared.
    
   /* NOTAUTHORIZED ("05", "Not Authorized Reseller"),
    AUTHORIZED ("04", "Authorized Reseller"),
    SILVER ("01", "Silver"),
    GOLD ("02", "Gold"),
    PLATINUM ("03", "Platinum");*/
	
	/*  - Diamond (= Platinum)
- Sapphire (= Gold)
- Emerald (= Authorized)
Silver was promoted to Gold Nov 1 so is no longer a medal level - but ODS fed coming forom SAP.
	 
	     */
	 
    
 //  PTR : 2161.01 -Implemented new order:  01 Silver ,02 Sapphire , 03   Diamond,04 Emerald,05  Not Authorized Reseller
    
    NOTAUTHORIZED ("05", "Not Authorized Reseller"),
    EMERALD ("04", "Emerald"),
    SILVER ("01", "Silver"),
    SAPPHIRE ("02", "Sapphire"),
    DIAMOND ("03", "Diamond");
    

	
	private final String medalLevelId;
	private final String medalLevelDescription;


	MedalLevelEnum(String medalLevelId, String medalLevelDescription) {
		this.medalLevelId = medalLevelId;
		this.medalLevelDescription = medalLevelDescription;
	  }

	public String getMedalLevelDescription() {
		return medalLevelDescription;
	}

	public String getMedalLevelId() {
		return medalLevelId;
	}
	
	public static MedalLevelEnum getMedalLevelById(String medalLevelId) {
		if(StringUtils.isNotEmpty(medalLevelId)) {
			if ("01".equals(medalLevelId)) {
				return SILVER;
			} else if ("02".equals(medalLevelId)) {
				return SAPPHIRE;
			}else if ("03".equals(medalLevelId)) {
				return DIAMOND;
			}else if ("04".equals(medalLevelId)) {
				return EMERALD;
			}else if ("05".equals(medalLevelId)) {
				return NOTAUTHORIZED;
			}
		}
		return null;
	}

	public static MedalLevelEnum getMedalLevelByDescription(String medalLevelDescription) {
		if(StringUtils.isNotEmpty(medalLevelDescription)) {
			if ("Silver".equalsIgnoreCase(medalLevelDescription)) {
				return SILVER;
			} else if ("Diamond".equalsIgnoreCase(medalLevelDescription)) {
				return DIAMOND;
			} else if ("Sapphire".equalsIgnoreCase(medalLevelDescription)) {
				return SAPPHIRE;
			} else if ("Emerald".equalsIgnoreCase(medalLevelDescription)) {
				return EMERALD;
			} else if ("Not Authorized Reseller".equalsIgnoreCase(medalLevelDescription)) {
				return NOTAUTHORIZED;
			}
		}
		return null;
	}
	
	public String toString() {
		return "[" + this.getMedalLevelId() + ":" + this.getMedalLevelDescription() + "]";
	}
   
}
