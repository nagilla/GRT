/**
 * Generated from schema type t=ResultType@http://avaya.com/v1/addresssearch
 */
package com.grt.dto;

public class Result implements java.io.Serializable {

	private java.lang.String name, street, region, city, zip, country, source,
			sAPId, accountNumber;

	private Double score;

	public java.lang.String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(java.lang.String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public java.lang.String getCity() {
		return city;
	}

	public void setCity(java.lang.String city) {
		this.city = city;
	}

	public java.lang.String getCountry() {
		return country;
	}

	public void setCountry(java.lang.String country) {
		this.country = country;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.String getRegion() {
		return region;
	}

	public void setRegion(java.lang.String region) {
		this.region = region;
	}

	public java.lang.String getSAPId() {
		return sAPId;
	}

	public void setSAPId(java.lang.String id) {
		sAPId = id;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public java.lang.String getSource() {
		return source;
	}

	public void setSource(java.lang.String source) {
		this.source = source;
	}

	public java.lang.String getStreet() {
		return street;
	}

	public void setStreet(java.lang.String street) {
		this.street = street;
	}

	public java.lang.String getZip() {
		return zip;
	}

	public void setZip(java.lang.String zip) {
		this.zip = zip;
	}

}
