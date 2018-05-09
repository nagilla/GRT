package com.avaya.grt.mappers;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
/**
 * The persistent class for the COUNTRY database table.
 * 
 * @author BEA Workshop
 */
public class Country implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String code;
	private String threeCharISOCode;
	private String name;
	private boolean euflag;
	private Set<State> states;
	private Set<Province> provinces;
	
	public boolean isEuflag() {
		return euflag;
	}
	public void setEuflag(boolean euflag) {
		this.euflag = euflag;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<Province> getProvinces() {
		return provinces;
	}
	public void setProvinces(Set<Province> provinces) {
		this.provinces = provinces;
	}
	public Set<State> getStates() {
		return states;
	}
	public void setStates(Set<State> states) {
		this.states = states;
	}
	public String getThreeCharISOCode() {
		if(StringUtils.isEmpty(threeCharISOCode)) {
			return this.code;
		} else {
			return threeCharISOCode;
		}
	}
	public void setThreeCharISOCode(String threeCharISOCode) {
		this.threeCharISOCode = threeCharISOCode;
	}
}
