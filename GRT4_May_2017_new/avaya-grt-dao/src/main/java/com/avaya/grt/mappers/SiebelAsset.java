package com.avaya.grt.mappers;
import java.io.Serializable;

import com.avaya.grt.mappers.TechnicalOrder;


/**
 * The persistent class for the TECHNICAL_ORDER database table.
 * 
 * @author BEA Workshop
 */
public class SiebelAsset implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private String siebelAssetId;
	private TechnicalOrder techOrder;
	private String quantity;
	private String assetNumber;
	
	public String getAssetNumber() {
		return assetNumber;
	}
	public void setAssetNumber(String assetNumber) {
		this.assetNumber = assetNumber;
	}
	
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getSiebelAssetId() {
		return siebelAssetId;
	}
	public void setSiebelAssetId(String siebelAssetId) {
		this.siebelAssetId = siebelAssetId;
	}
	public TechnicalOrder getTechOrder() {
		return techOrder;
	}
	public void setTechOrder(TechnicalOrder techOrder) {
		this.techOrder = techOrder;
	}
	
	

    
	
}