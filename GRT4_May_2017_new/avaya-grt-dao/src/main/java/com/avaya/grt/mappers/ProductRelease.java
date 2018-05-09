package com.avaya.grt.mappers;
import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.avaya.grt.mappers.ArtProductType;

/**
 * The persistent class for the PRODUCT_RELEASE database table.
 * 
 * @author BEA Workshop
 */
public class ProductRelease  implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String releaseNo;
	private String releaseNumber;
	private String seCode, template;
	private boolean systemPlatform;
	private String productType;
	private boolean sslVpnEligible;
	private boolean redemptionPrerequisiteSECode;
	
	private ArtProductType artProductType;

    public ProductRelease() {
    }

	public String getReleaseNo() {
		return this.releaseNo;
	}
	public void setReleaseNo(String releaseNo) {
		this.releaseNo = releaseNo;
	}

	public String getReleaseNumber() {
		return this.releaseNumber;
	}
	public void setReleaseNumber(String releaseNumber) {
		this.releaseNumber = releaseNumber;
	}

	//bi-directional many-to-one association to ArtProductType
	public ArtProductType getArtProductType() {
		return this.artProductType;
	}
	public void setArtProductType(ArtProductType artProductType) {
		this.artProductType = artProductType;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ProductRelease)) {
			return false;
		}
		ProductRelease castOther = (ProductRelease)other;
		return new EqualsBuilder()
			.append(this.getReleaseNo(), castOther.getReleaseNo())
			.isEquals();
    }
    
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getReleaseNo())
			.toHashCode();
    }   

	public String toString() {
		return new ToStringBuilder(this)
			.append("releaseNo", getReleaseNo())
			.toString();
	}

	public String getSeCode() {
		return seCode;
	}

	public void setSeCode(String seCode) {
		this.seCode = seCode;
	}

	public boolean isSystemPlatform() {
		return systemPlatform;
	}

	public void setSystemPlatform(boolean systemPlatform) {
		this.systemPlatform = systemPlatform;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public boolean isSslVpnEligible() {
		return sslVpnEligible;
	}

	public void setSslVpnEligible(boolean sslVpnEligible) {
		this.sslVpnEligible = sslVpnEligible;
	}

	public boolean isRedemptionPrerequisiteSECode() {
		return redemptionPrerequisiteSECode;
	}

	public void setRedemptionPrerequisiteSECode(boolean redemptionPrerequisiteSECode) {
		this.redemptionPrerequisiteSECode = redemptionPrerequisiteSECode;
	}
}