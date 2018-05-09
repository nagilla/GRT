package com.grt.util;

import java.util.ArrayList;
import java.util.List;

public enum RegistrationTypeEnum {
	
	INSTALLBASEONLY ("RTO1", "Install Base Only", "Install Base Creation Only"),
	TECHNICALREGISTRATIONONLY ("RTO2", "Tech On-Board Only", "Technical Onboarding Only"),
	EQUIPMENTREMOVALONLY ("RTO3", "Record Validation Only", "Record Validation Only"),
	IPOFFICE("RTO4", "IP Office", "IP Office"),
	SALMIGRATION ("RTO5", "SAL Migration", "SAL Migration Only"),
	FULLREGISTRATION ("RTO6", "End-to-End Registration", "End to End Registration"),	
	//GRT 4.0 Change
	//EQUIPMENTMOVEONLY ("RTO7", "Equipment Move Only");
	EQUIPMENTMOVEONLY ("RTO7", "Equipment Move", "Equipment/Site Move Only");
	private final String registrationID;
	private final String registrationType;
	private final String registrationTypeDesc;
	
	RegistrationTypeEnum(String registrationID, String registrationType, String registrationTypeDesc)
	{
		this.registrationID = registrationID;
		this.registrationType = registrationType;		
		this.registrationTypeDesc = registrationTypeDesc;
	}

	public String getRegistrationID() {
		return registrationID;
	}

	public String getRegistrationType() {
		return registrationType;
	}
	
	public String getRegistrationTypeDesc() {
		return registrationTypeDesc;
	}

	//Give corresponding id list based on list of types
	public static List<String> getIdValues( String[] types ){
		List<String> idList = new ArrayList<String>();
		
		if( types != null && types.length > 0 ){
			for( String type : types ){
				for(RegistrationTypeEnum regTypeEnum : values()) {
			        if(regTypeEnum.registrationTypeDesc.equalsIgnoreCase(type)){
			        	idList.add( regTypeEnum.registrationID );
			        }
			    }
			}
		}
		return idList;
	}
	public static String getById(String id) {
		   for(RegistrationTypeEnum e : values()) {
		       if(e.registrationID.equals(id)) return e.getRegistrationType();
		   }
		   return null;
		}
	/*public static void main(String args[]){
		String registrationType = "Install Base Only|Records Validation Only|Equipment Move Only";
		String [] regTypeArr = registrationType.split("\\|");
		System.out.println(regTypeArr.toString());
		RegistrationTypeEnum.getIdValues(regTypeArr);
	}*/
	
}
