package com.grt.dto;

import java.io.Serializable;

public class TRConfig implements Serializable, Comparable  {

	private static final long serialVersionUID = 1L;
	private String groupId, groupDescription, parentMaterialCode, parentMaterialCodeDescription, 
	seCode, seCodeDescription, productType, productTypeDescription, template, templateDescription, childMaterialCode, special_note;
	
	private boolean mainMaterialCode;

	public String getChildMaterialCode() {
		return childMaterialCode;
	}

	public void setChildMaterialCode(String childMaterialCode) {
		this.childMaterialCode = childMaterialCode;
	}

	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public boolean isMainMaterialCode() {
		return mainMaterialCode;
	}

	public void setMainMaterialCode(boolean mainMaterialCode) {
		this.mainMaterialCode = mainMaterialCode;
	}

	public String getParentMaterialCode() {
		return parentMaterialCode;
	}

	public void setParentMaterialCode(String parentMaterialCode) {
		this.parentMaterialCode = parentMaterialCode;
	}

	public String getParentMaterialCodeDescription() {
		return parentMaterialCodeDescription;
	}

	public void setParentMaterialCodeDescription(
			String parentMaterialCodeDescription) {
		this.parentMaterialCodeDescription = parentMaterialCodeDescription;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getProductTypeDescription() {
		return productTypeDescription;
	}

	public void setProductTypeDescription(String productTypeDescription) {
		this.productTypeDescription = productTypeDescription;
	}

	public String getSeCode() {
		return seCode;
	}

	public void setSeCode(String seCode) {
		this.seCode = seCode;
	}

	public String getSeCodeDescription() {
		return seCodeDescription;
	}

	public void setSeCodeDescription(String seCodeDescription) {
		this.seCodeDescription = seCodeDescription;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getTemplateDescription() {
		return templateDescription;
	}

	public void setTemplateDescription(String templateDescription) {
		this.templateDescription = templateDescription;
	}

	public String getSpecial_note() {
		return special_note;
	}

	public void setSpecial_note(String special_note) {
		this.special_note = special_note;
	}

	public int compareTo(Object o) {
        if(o instanceof TRConfig) {
              return this.groupDescription.compareToIgnoreCase(((TRConfig)o).getGroupDescription());
        }
        return 0;
  }

	
	
}
