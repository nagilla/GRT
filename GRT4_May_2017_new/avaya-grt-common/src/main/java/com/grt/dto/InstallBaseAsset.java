package com.grt.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InstallBaseAsset implements Serializable{
	 private static final long serialVersionUID = 1L;
	 	private String registrationId;
	 	private String routingInfo;
		private List<InstallBaseAssetData> installBaseAssetData = new ArrayList<InstallBaseAssetData>();
		
		public List<InstallBaseAssetData> getInstallBaseAssetData() {
			return installBaseAssetData;
		}
		public void setInstallBaseAssetData(
				List<InstallBaseAssetData> installBaseAssetData) {
			this.installBaseAssetData = installBaseAssetData;
		}
		public String getRegistrationId() {
			return registrationId;
		}
		public void setRegistrationId(String registrationId) {
			this.registrationId = registrationId;
		}
		public String getRoutingInfo() {
			return routingInfo;
		}
		public void setRoutingInfo(String routingInfo) {
			this.routingInfo = routingInfo;
		}
}
