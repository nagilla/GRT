package com.grt.dto;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class SoldTo implements Serializable{
	
	private static final long serialVersionUID = 1919741183442600462L;
	
	private final int SOLD_TO_ID = 0;
	private final int CUSTOMER_NAME = 1;
	private String soldToId;
	private String customerName;
	
	public SoldTo(Object[] object) {
		if(object.length == 2 && object[SOLD_TO_ID] != null 
				&& object[CUSTOMER_NAME] != null) {
			setSoldToId(object[SOLD_TO_ID].toString());
			setCustomerName(object[CUSTOMER_NAME].toString());
		}
	}
	
	public SoldTo() {
	}
	public String getCustomerName() {
		return customerName;
	}
	
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getSoldToId() {
		return soldToId;
	}
	
	public void setSoldToId(String soldToId) {
		this.soldToId = soldToId;
	}
	
	public String toString() {
		return soldToId + " " + customerName;
	}
	
	public static List<String> convertToStringList(List<SoldTo> soldToObjectList) {
		List<String> soldToStringList = new LinkedList<String>();
        for (SoldTo soldTo : soldToObjectList) {
        	soldToStringList.add(soldTo.toString());
        }
        return soldToStringList;
	}
	
	public static boolean contains(List<String> soldTos, String soldToId) { 

		boolean ret = false;
		if(soldTos != null && soldTos.size() > 0) {
			for (String soldTo : soldTos) {
				if(soldTo.startsWith(soldToId)) {
					return true;
				}
			}
		}
		return ret;

	}

	
}
