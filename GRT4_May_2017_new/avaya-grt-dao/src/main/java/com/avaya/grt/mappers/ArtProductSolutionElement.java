package com.avaya.grt.mappers;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The persistent class for the ART_PRODUCT_SOLUTION_ELEMENT database table.
 * 
 * @author BEA Workshop
 */
public class ArtProductSolutionElement  implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String productTypeId;
	private String aorigRequired;
	private String privateIpAddress;
	private String productTypeCode;
	private String randomPasswordGen;
	private String release233Higher;
	private String seidVoicePortalMgmtServer;
	private String sesEdgeCoreRouter;
	private String templateName;
	private SolnElementCode solnElementCode;

    public ArtProductSolutionElement() {
    }

	public String getAorigRequired() {
		return this.aorigRequired;
	}
	public void setAorigRequired(String aorigRequired) {
		this.aorigRequired = aorigRequired;
	}

	public String getPrivateIpAddress() {
		return this.privateIpAddress;
	}
	public void setPrivateIpAddress(String privateIpAddress) {
		this.privateIpAddress = privateIpAddress;
	}

	public String getProductTypeCode() {
		return this.productTypeCode;
	}
	public void setProductTypeCode(String productTypeCode) {
		this.productTypeCode = productTypeCode;
	}

	public String getRandomPasswordGen() {
		return this.randomPasswordGen;
	}
	public void setRandomPasswordGen(String randomPasswordGen) {
		this.randomPasswordGen = randomPasswordGen;
	}

	public String getRelease233Higher() {
		return this.release233Higher;
	}
	public void setRelease233Higher(String release233Higher) {
		this.release233Higher = release233Higher;
	}

	public String getSeidVoicePortalMgmtServer() {
		return this.seidVoicePortalMgmtServer;
	}
	public void setSeidVoicePortalMgmtServer(String seidVoicePortalMgmtServer) {
		this.seidVoicePortalMgmtServer = seidVoicePortalMgmtServer;
	}

	public String getSesEdgeCoreRouter() {
		return this.sesEdgeCoreRouter;
	}
	public void setSesEdgeCoreRouter(String sesEdgeCoreRouter) {
		this.sesEdgeCoreRouter = sesEdgeCoreRouter;
	}

	public String getTemplateName() {
		return this.templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	//bi-directional many-to-one association to SolnElementCode
	public SolnElementCode getSolnElementCode() {
		return this.solnElementCode;
	}
	public void setSolnElementCode(SolnElementCode solnElementCode) {
		this.solnElementCode = solnElementCode;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ArtProductSolutionElement)) {
			return false;
		}
		ArtProductSolutionElement castOther = (ArtProductSolutionElement)other;
		return new EqualsBuilder()
			.isEquals();
    }
    
	public int hashCode() {
		return new HashCodeBuilder()
			.toHashCode();
    }   

	public String toString() {
		return new ToStringBuilder(this)
			.toString();
	}

	public String getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(String productTypeId) {
		this.productTypeId = productTypeId;
	}
}