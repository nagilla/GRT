/**
 * 
 */
package com.grt.dto;

import java.io.Serializable;

/**
 * DTO class to have the SAL Registration Details.
 * 
 * @author Perficient
 *
 */
public class SALRegistrationDetail implements Serializable{
	private String technicalOrderId = new String();
	private String siebelSrNo = new String();
	private String salRegistrationId;
	private SalMigrationDto salMigration;
	private String materialCode;
	private String salMigrationOnly;
	private String noAddtionalProductToRegister;
	private UniversalProductRegistration universalProductRegistration;
	
	public SalMigrationDto getSalMigration() {
		return salMigration;
	}
	public void setSalMigration(SalMigrationDto salMigration) {
		this.salMigration = salMigration;
	}
	public UniversalProductRegistration getUniversalProductRegistration() {
		return universalProductRegistration;
	}
	public void setUniversalProductRegistration(
			UniversalProductRegistration universalProductRegistration) {
		this.universalProductRegistration = universalProductRegistration;
	}
	public String getTechnicalOrderId() {
		return technicalOrderId;
	}
	public void setTechnicalOrderId(String technicalOrderId) {
		this.technicalOrderId = technicalOrderId;
	}
	public String getSiebelSrNo() {
		return siebelSrNo;
	}
	public void setSiebelSrNo(String siebelSrNo) {
		this.siebelSrNo = siebelSrNo;
	}
	public String getSalRegistrationId() {
		return salRegistrationId;
	}
	public void setSalRegistrationId(String salRegistrationId) {
		this.salRegistrationId = salRegistrationId;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public String getSalMigrationOnly() {
		return salMigrationOnly;
	}
	public void setSalMigrationOnly(String salMigrationOnly) {
		this.salMigrationOnly = salMigrationOnly;
	}
	public String getNoAddtionalProductToRegister() {
		return noAddtionalProductToRegister;
	}
	public void setNoAddtionalProductToRegister(String noAddtionalProductToRegister) {
		this.noAddtionalProductToRegister = noAddtionalProductToRegister;
	}
}
