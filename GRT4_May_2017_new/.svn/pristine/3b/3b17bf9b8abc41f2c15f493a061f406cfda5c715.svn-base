package com.avaya.grt.mappers;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The persistent class for the ART_PRODUCT_TYPE database table.
 * 
 * @author BEA Workshop
 */
public class ArtProductType  implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String productTypeCode;
	private String afidReqd;
	private String cmProduct;
	private String description;
	private String fullRegistrationRqd;
	private String systemPlatformBased;
	private ArtProductSolutionElement artProductSolutionElement;
	private java.util.Set<ProductRelease> productReleases;
	private String registrationType;
	private String aorigRequired;
	
    public ArtProductType() {
    }

	public String getProductTypeCode() {
		return this.productTypeCode;
	}
	public void setProductTypeCode(String productTypeCode) {
		this.productTypeCode = productTypeCode;
	}

	public String getAfidReqd() {
		return this.afidReqd;
	}
	public void setAfidReqd(String afidReqd) {
		this.afidReqd = afidReqd;
	}

	public String getCmProduct() {
		return this.cmProduct;
	}
	public void setCmProduct(String cmProduct) {
		this.cmProduct = cmProduct;
	}

	public String getDescription() {
		return this.description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getFullRegistrationRqd() {
		return this.fullRegistrationRqd;
	}
	public void setFullRegistrationRqd(String fullRegistrationRqd) {
		this.fullRegistrationRqd = fullRegistrationRqd;
	}

	public String getSystemPlatformBased() {
		return this.systemPlatformBased;
	}
	public void setSystemPlatformBased(String systemPlatformBased) {
		this.systemPlatformBased = systemPlatformBased;
	}

	//bi-directional one-to-one association to ArtProductSolutionElement
	public ArtProductSolutionElement getArtProductSolutionElement() {
		return this.artProductSolutionElement;
	}
	public void setArtProductSolutionElement(ArtProductSolutionElement artProductSolutionElement) {
		this.artProductSolutionElement = artProductSolutionElement;
	}

	//bi-directional many-to-one association to ProductRelease
	public java.util.Set<ProductRelease> getProductReleases() {
		return this.productReleases;
	}
	public void setProductReleases(java.util.Set<ProductRelease> productReleases) {
		this.productReleases = productReleases;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ArtProductType)) {
			return false;
		}
		ArtProductType castOther = (ArtProductType)other;
		return new EqualsBuilder()
			.append(this.getProductTypeCode(), castOther.getProductTypeCode())
			.isEquals();
    }
    
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getProductTypeCode())
			.toHashCode();
    }   

	public String toString() {
		return new ToStringBuilder(this)
			.append("productTypeCode", getProductTypeCode())
			.toString();
	}

	public String getRegistrationType() {
		return registrationType;
	}

	public void setRegistrationType(String registrationType) {
		this.registrationType = registrationType;
	}

	public String getAorigRequired() {
		return aorigRequired;
	}

	public void setAorigRequired(String aorigRequired) {
		this.aorigRequired = aorigRequired;
	}
}