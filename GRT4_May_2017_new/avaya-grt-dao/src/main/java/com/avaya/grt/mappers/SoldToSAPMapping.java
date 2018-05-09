package com.avaya.grt.mappers;
import java.io.Serializable;

/**
 * The persistent class for the SOLDTO_SAP_MAPPING database table.
 * 
 * @author BEA Workshop
 */
public class SoldToSAPMapping  implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String soldTo;
	private String sapBox;
	
	public SoldToSAPMapping() {
    }

	public String getSapBox() {
		return sapBox;
	}

	public void setSapBox(String sapBox) {
		this.sapBox = sapBox;
	}

	public String getSoldTo() {
		return soldTo;
	}

	public void setSoldTo(String soldTo) {
		this.soldTo = soldTo;
	}

	
}