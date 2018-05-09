package com.avaya.grt.mappers;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The persistent class for the SOLN_ELEMENT_CODE database table.
 * 
 * @author BEA Workshop
 */
public class SolnElementCode  implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String seCode;
	private String osWinPw;
	private String rasPw;
	private String rootPw;
	private String sidMid;
	private String version;	
	private java.util.Set<ArtProductSolutionElement> artProductSolutionElements;
	private java.util.Set<MtrlCodeSolnElmntCode> mtrlCodeSolnElmntCodes;

    public SolnElementCode() {
    }

	public String getSeCode() {
		return this.seCode;
	}
	public void setSeCode(String seCode) {
		this.seCode = seCode;
	}

	//bi-directional many-to-one association to ArtProductSolutionElement
	public java.util.Set<ArtProductSolutionElement> getArtProductSolutionElements() {
		return this.artProductSolutionElements;
	}
	public void setArtProductSolutionElements(java.util.Set<ArtProductSolutionElement> artProductSolutionElements) {
		this.artProductSolutionElements = artProductSolutionElements;
	}

	//bi-directional many-to-one association to MtrlCodeSolnElmntCode
	public java.util.Set<MtrlCodeSolnElmntCode> getMtrlCodeSolnElmntCodes() {
		return this.mtrlCodeSolnElmntCodes;
	}
	public void setMtrlCodeSolnElmntCodes(java.util.Set<MtrlCodeSolnElmntCode> mtrlCodeSolnElmntCodes) {
		this.mtrlCodeSolnElmntCodes = mtrlCodeSolnElmntCodes;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SolnElementCode)) {
			return false;
		}
		SolnElementCode castOther = (SolnElementCode)other;
		return new EqualsBuilder()
			.append(this.getSeCode(), castOther.getSeCode())
			.isEquals();
    }
    
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getSeCode())
			.toHashCode();
    }   

	public String toString() {
		return new ToStringBuilder(this)
			.append("seCode", getSeCode())
			.toString();
	}

	public String getOsWinPw() {
		return osWinPw;
	}

	public void setOsWinPw(String osWinPw) {
		this.osWinPw = osWinPw;
	}

	public String getRasPw() {
		return rasPw;
	}

	public void setRasPw(String rasPw) {
		this.rasPw = rasPw;
	}

	public String getRootPw() {
		return rootPw;
	}

	public void setRootPw(String rootPw) {
		this.rootPw = rootPw;
	}

	public String getSidMid() {
		return sidMid;
	}

	public void setSidMid(String sidMid) {
		this.sidMid = sidMid;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}