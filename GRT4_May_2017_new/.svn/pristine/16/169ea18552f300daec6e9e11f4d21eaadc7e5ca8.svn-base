package com.avaya.grt.mappers;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The persistent class for the MTRL_CODE_SOLN_ELMNT_CODE database table.
 * 
 * @author BEA Workshop
 */
public class MtrlCodeSolnElmntCode  implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String materialCode;
	private SolnElementCode solnElementCode;

    public MtrlCodeSolnElmntCode() {
    }

	public String getMaterialCode() {
		return this.materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	//bi-directional many-to-one association to SolnElementCode
	public SolnElementCode getSolnElementCode() {
		return this.solnElementCode;
	}
	public void setSolnElementCode(SolnElementCode solnElementCode) {
		this.solnElementCode = solnElementCode;
	}
}