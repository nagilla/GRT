package com.avaya.grt.mappers;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The persistent class for the PRODUCT_FAMILY database table.
 * 
 * @author BEA Workshop
 */
public class ProductFamily  implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String productFamilyCode;
	private MaterialCode materialCodeBean;

    public ProductFamily() {
    }

	public String getProductFamilyCode() {
		return this.productFamilyCode;
	}
	public void setProductFamilyCode(String productFamilyCode) {
		this.productFamilyCode = productFamilyCode;
	}

	//bi-directional many-to-one association to MaterialCode
	public MaterialCode getMaterialCodeBean() {
		return this.materialCodeBean;
	}
	public void setMaterialCodeBean(MaterialCode materialCodeBean) {
		this.materialCodeBean = materialCodeBean;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ProductFamily)) {
			return false;
		}
		ProductFamily castOther = (ProductFamily)other;
		return new EqualsBuilder()
			.append(this.getProductFamilyCode(), castOther.getProductFamilyCode())
			.isEquals();
    }
    
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getProductFamilyCode())
			.toHashCode();
    }   

	public String toString() {
		return new ToStringBuilder(this)
			.append("productFamilyCode", getProductFamilyCode())
			.toString();
	}
}