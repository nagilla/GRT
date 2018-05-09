/**
 * Class to have persist the data from IPO Technical Product Registration.
 *
 * @author Lavanya
 *
 */
package com.grt.dto;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.avaya.grt.mappers.TechnicalOrder;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.grt.util.GRTConstants;
import com.grt.util.OpTypeEnum;

public class IPOProductRegistration implements Serializable {
		private static final long serialVersionUID = 4122194147651352429L;
		
		//Required from all APIs.
		private String soldTo;
		private String filePath;
		private String ipoUserEmail;
		
		//Required Only from Technical Registration of IPO.
		private String SECode = GRTConstants.SE_CODE_IPO;
		private String releaseNo = GRTConstants.RELEASE_NO_IPO;
		private TechnicalOrder technicalOrder;
		
		//Required for Password reset & re-generating OnBoarding xml file.
		private TechnicalRegistration technicalRegistration;
		private String alarmId;
		private String serviceName;		
		
		private String opType = OpTypeEnum.NEWTECHREG.getOpType();	
		
		//Default values - No one has to pass
		private String productType = GRTConstants.PRODUCT_TYPE_IPO;
	
		//Required for SR creation.
		private String materialCode;
		private String serialNumber;
		
		// Return values.
		private String errorCode;
		private String errorDescription;
		
		//Required for retry
		private String srNumber;
		private String seid;
		
		public String getFilePath() {
			return filePath;
		}
		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
		public String getErrorCode() {
			return errorCode;
		}
		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}
		public String getErrorDescription() {
			return errorDescription;
		}
		public void setErrorDescription(String errorDescription) {
			this.errorDescription = errorDescription;
		}
		public String getAlarmId() {
			return alarmId;
		}
		public void setAlarmId(String alarmId) {
			this.alarmId = alarmId;
		}

		public String getOpType() {
			return opType;
		}
		public void setOpType(String opType) {
			this.opType = opType;
		}
		public String getMaterialCode() {
			return materialCode;
		}
		public void setMaterialCode(String materialCode) {
			this.materialCode = materialCode;
		}
		public String getProductType() {
			return productType;
		}
		public void setProductType(String productType) {
			this.productType = productType;
		}
		public String getReleaseNo() {
			return releaseNo;
		}
		public void setReleaseNo(String releaseNo) {
			this.releaseNo = releaseNo;
		}
		public String getSECode() {
			return SECode;
		}
		public void setSECode(String code) {
			SECode = code;
		}

		public String getSoldTo() {
			return soldTo;
		}
		public void setSoldTo(String soldTo) {
			this.soldTo = soldTo;
		}

		public String getSerialNumber() {
			return serialNumber;
		}
		public void setSerialNumber(String serialNumber) {
			this.serialNumber = serialNumber;
		}
		public TechnicalOrder getTechnicalOrder() {
			return technicalOrder;
		}
		public void setTechnicalOrder(TechnicalOrder technicalOrder) {
			this.technicalOrder = technicalOrder;
		}
		public String getServiceName() {
			if(StringUtils.isEmpty(serviceName)){
				//serviceName = getService_name().trim();
				serviceName="AVAYA_SUPPORT";
						
			}
			return serviceName;
		}
		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}
		public TechnicalRegistration getTechnicalRegistration() {
			return technicalRegistration;
		}
		public void setTechnicalRegistration(TechnicalRegistration technicalRegistration) {
			this.technicalRegistration = technicalRegistration;
		}
		public String getIpoUserEmail() {
			return ipoUserEmail;
		}
		public void setIpoUserEmail(String ipoUserEmail) {
			this.ipoUserEmail = ipoUserEmail;
		}
		public String getSeid() {
			return seid;
		}
		public void setSeid(String seid) {
			this.seid = seid;
		}
		public String getSrNumber() {
			return srNumber;
		}
		public void setSrNumber(String srNumber) {
			this.srNumber = srNumber;
		}
}
