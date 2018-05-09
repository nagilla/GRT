package com.avaya.grt.mappers;
import java.io.Serializable;

/**
 * The persistent class for the MATERIAL_CODE database table.
 * 
 * @author BEA Workshop
 */
public class MaterialCode  implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String materialCode;
	private String description;
	private java.util.Set<ProductFamily> productFamilies;

    public MaterialCode() {
    }

	public String getMaterialCode() {
		return this.materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public String getDescription() {
		return this.description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	//bi-directional many-to-one association to ProductFamily
	public java.util.Set<ProductFamily> getProductFamilies() {
		return this.productFamilies;
	}
	public void setProductFamilies(java.util.Set<ProductFamily> productFamilies) {
		this.productFamilies = productFamilies;
	}
}