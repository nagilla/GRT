package com.avaya.grt.mappers;
import java.io.Serializable;

public class LocalTRConfig  implements Serializable {

	private static final long serialVersionUID = 1L;
    private String localTRConfigId, productType, seCode, comments, connectivityType, template;
    private boolean privateIpEligible, alarmOriginationEligible, randomPasswordEligible, 
    vpmsldnEligibleSeid, foptionEligible, docrEligible, authFileIdEligible,
    inadsEligible, cmProduct;
    
	public boolean isAlarmOriginationEligible() {
		return alarmOriginationEligible;
	}
	public void setAlarmOriginationEligible(boolean alarmOriginationEligible) {
		this.alarmOriginationEligible = alarmOriginationEligible;
	}
	public boolean isAuthFileIdEligible() {
		return authFileIdEligible;
	}
	public void setAuthFileIdEligible(boolean authFileIdEligible) {
		this.authFileIdEligible = authFileIdEligible;
	}
	public boolean isCmProduct() {
		return cmProduct;
	}
	public void setCmProduct(boolean cmProduct) {
		this.cmProduct = cmProduct;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getConnectivityType() {
		return connectivityType;
	}
	public void setConnectivityType(String connectivityType) {
		this.connectivityType = connectivityType;
	}
	public boolean isDocrEligible() {
		return docrEligible;
	}
	public void setDocrEligible(boolean docrEligible) {
		this.docrEligible = docrEligible;
	}
	public boolean isFoptionEligible() {
		return foptionEligible;
	}
	public void setFoptionEligible(boolean foptionEligible) {
		this.foptionEligible = foptionEligible;
	}
	public boolean isInadsEligible() {
		return inadsEligible;
	}
	public void setInadsEligible(boolean inadsEligible) {
		this.inadsEligible = inadsEligible;
	}
	public String getLocalTRConfigId() {
		return localTRConfigId;
	}
	public void setLocalTRConfigId(String localTRConfigId) {
		this.localTRConfigId = localTRConfigId;
	}
	public boolean isPrivateIpEligible() {
		return privateIpEligible;
	}
	public void setPrivateIpEligible(boolean privateIpEligible) {
		this.privateIpEligible = privateIpEligible;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public boolean isRandomPasswordEligible() {
		return randomPasswordEligible;
	}
	public void setRandomPasswordEligible(boolean randomPasswordEligible) {
		this.randomPasswordEligible = randomPasswordEligible;
	}
	public String getSeCode() {
		return seCode;
	}
	public void setSeCode(String seCode) {
		this.seCode = seCode;
	}
	public boolean isVpmsldnEligibleSeid() {
		return vpmsldnEligibleSeid;
	}
	public void setVpmsldnEligibleSeid(boolean vpmsldnEligibleSeid) {
		this.vpmsldnEligibleSeid = vpmsldnEligibleSeid;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
    
}