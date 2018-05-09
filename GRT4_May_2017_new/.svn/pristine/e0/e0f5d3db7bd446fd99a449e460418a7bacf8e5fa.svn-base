package com.avaya.grt.mappers;
import java.io.Serializable;

/**
 * The persistent class for the MATERIAL_EXCLUSION database table.
 * 
 * @author BEA Workshop
 */
public class MaterialExclusion  implements Serializable, Comparable<MaterialExclusion> {
	
	private static final long serialVersionUID = 1L;
	private String materialCode;
	private String exclusionSource;
	private int exclusionOrder;

    public MaterialExclusion() {
    }

	public String getMaterialCode() {
		return this.materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public String getExclusionSource() {
		return exclusionSource;
	}

	public void setExclusionSource(String exclusionSource) {
		this.exclusionSource = exclusionSource;
	}
	
	public int getExclusionOrder() {
		return exclusionOrder;
	}

	public void setExclusionOrder(int exclusionOrder) {
		this.exclusionOrder = exclusionOrder;
	}

	@Override
	public int compareTo(MaterialExclusion o) {
		return (o!=null) ? getExclusionOrder() - o.getExclusionOrder() : 0;
	}
}