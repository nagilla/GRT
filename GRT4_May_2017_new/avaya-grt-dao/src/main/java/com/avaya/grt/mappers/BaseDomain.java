package com.avaya.grt.mappers;
import java.io.Serializable;

/**
 * The base domain for all persistent object 
 *
 * @author BEA Workshop
 */
public class BaseDomain implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	
    private int version;
    
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
}