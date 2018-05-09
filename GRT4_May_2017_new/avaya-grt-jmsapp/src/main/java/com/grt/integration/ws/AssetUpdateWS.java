package com.grt.integration.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.avaya.grt.jms.siebel.xml.assetupdate.jms.SiebelMessage;
import com.avaya.grt.service.assetupdate.AssetUpdateJMSService;
import com.grt.dto.InstallBaseAsset;

@WebService(targetNamespace = "http://www.siebel.com/xml/AVAYA%20GRT%20Registration")
public class AssetUpdateWS {

	//DI via Spring
	private AssetUpdateJMSService assetUpdateJMSService;

	@WebMethod(exclude=true)
	public AssetUpdateJMSService getAssetUpdateJMSService() {
		return assetUpdateJMSService;
	}

	@WebMethod(exclude=true)
	public void setAssetUpdateJMSService(AssetUpdateJMSService assetUpdateJMSService) {
		this.assetUpdateJMSService = assetUpdateJMSService;
	}
	
	@WebMethod(operationName="AssetUpdate", action="AssetUpdate")
	@WebResult(name="assetUpdateCreateResult", targetNamespace = "http://www.siebel.com/xml/AVAYA%20GRT%20Registration")
	public InstallBaseAsset assetUpdate(@WebParam(name="SiebelMessage", targetNamespace = "http://www.siebel.com/xml/AVAYA%20GRT%20Registration") 
										SiebelMessage msgAssetUpdate) {
		InstallBaseAsset installBaseAssetDto = assetUpdateJMSService.assetUpdate(msgAssetUpdate);		
		return installBaseAssetDto;
	}
	
}