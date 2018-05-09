package com.grt.util;

import java.io.Serializable;

public class SearchParam implements Serializable, Comparable<SearchParam> {
	private static final long serialVersionUID = 1L;
	public static final String ORDER_BY_ASC = "asc";
	public static final String ORDER_BY_DESC = "desc";
	public static final String FIELD_TYPE_STRING = "String";
	public static final String FIELD_TYPE_NUMERIC = "Numeric";
	public static final String NUMERIC_OPR_EQUAL = "EQ";
	public static final String NUMERIC_OPR_LESS_THAN = "LT";
	public static final String NUMERIC_OPR_LESS_EQUAL_THAN = "LE";
	public static final String NUMERIC_OPR_GREATER_THAN = "GT";
	public static final String NUMERIC_OPR_GREATER_EQUAL_THAN = "GE";
	
	private String fieldName;
	private String fieldValue;
	private String fieldType;
	private String numericOpr;
	private Boolean reverseMatch;
	private String orderByDirection;
	private Integer orderByWeight;
	private Boolean disabled;
	private Boolean isNull;
	
	public SearchParam(){
		
	}
	
	public SearchParam(String fieldName, String fieldValue){
		this.setFieldName(fieldName);
		this.setFieldValue(fieldValue);
	}
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getFieldValue() {
		if(fieldValue!=null){
			return fieldValue.trim();
		}
		return fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	public String getOrderByDirection() {
		return orderByDirection;
	}
	public void setOrderByDirection(String orderByDirection) {
		this.orderByDirection = orderByDirection;
	}
	public Integer getOrderByWeight() {
		return orderByWeight;
	}
	public void setOrderByWeight(Integer orderByWeight) {
		this.orderByWeight = orderByWeight;
	}
	public Boolean getReverseMatch() {
		return reverseMatch;
	}
	public void setReverseMatch(Boolean reverseMatch) {
		this.reverseMatch = reverseMatch;
	}
	public Boolean getDisabled() {
		return disabled;
	}
	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
	public int compareTo(SearchParam o) {
		if(this.getOrderByWeight() != null && o.getOrderByWeight() != null) {
			return this.getOrderByWeight().compareTo(o.getOrderByWeight());    
		}
		if(this.getOrderByWeight() == null && o.getOrderByWeight() == null) {
			return 0; 
		}
		if(this.getOrderByWeight() != null) {
			return 1;
		}
		else {
			return -1;
		}
	}
	public Boolean getIsNull() {
		return isNull;
	}
	public void setIsNull(Boolean isNull) {
		this.isNull = isNull;
	}
	public String getNumericOpr() {
		return numericOpr;
	}
	public void setNumericOpr(String numericOpr) {
		this.numericOpr = numericOpr;
	}
}
