package com.avaya.grt.mappers;
import java.io.Serializable;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

import com.grt.dto.PurchaseOrder;
import com.grt.dto.RegistrationQuestionsDetail;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.AccessTypeEnum;
import com.grt.util.GRTConstants;
import com.grt.util.TechnicalRegistrationUIUtil;
import com.grt.util.ProcessStepEnum;
import com.grt.util.RegistrationTypeEnum;
import com.grt.util.StatusEnum;
/**
 * The persistent class for the SITE_REGISTRATION database table.
 *
 * @author BEA Workshop
 */
public class SiteRegistration extends BaseDomain implements Serializable {
	//default serial version id, required for serializable classes.
	private final static Logger logger = Logger.getLogger(SiteRegistration.class);
	private static final long serialVersionUID = 1L;
	private String registrationId;
	private String address;
	private String address2;
	private String city;
	private String state;
	private String zip;
	private String phone;
	private String email;
	
	private String company;
	private String companyPhone;
	private String region;
	private java.util.Date createdDate;
	private String customerName;
	private String expedireRefernceNo;
	private String expedite;
	private String finalValidationSrNo;
	private String firstName;
	private String installBaseSrNo;
	private String lastName;
	private String noAdditionalProductFlag;
	private String onsiteEmail;
	private String onsiteFirstName;
	private String onsiteLastName;
	private String onsitePhone;
	private String reportEmailId;
	private String reportPhone;
	private String soldToId;
	private String soldToLocation;
	private String soldToType;
	private String updatedBy;
	private java.util.Date updatedDate;
	private String userName;
	private String siebelSync;
	private String salMigrationOnly;
	private ProcessStep processStep;
	private Status status;
	private String siteCountry;
	private java.util.Set<TechnicalOrder> technicalOrders;
	private java.util.Set<RegistrationQuestions> registrationQuestions;
	private SRRequest installBaseSrRequest;
	private SRRequest finalValidationSrRequest;
	/* Property addded for GRT 4.0 */
	private SRRequest eqrMoveSrRequest;
	private SRRequest eqrActiveContractsSrRequest;
	private String sendMail;
	private String skipInstallBaseCreation;
	private String createdBy;
	private String inventoryFile;
	private Clob inventory;
	private Status installBaseStatus;
	private Status techRegStatus;
	private Status finalValidationStatus;
	/* Property addded for GRT 4.0 */
	private Status eqrMoveStatus;
	private String typeOfImplementation;
	private ProcessStepEnum tempProcessStep;
	private Date cut_over_date;
	private String typeOfUser;
	private String submitted;
	private String userRole;
	private String isSrCompleted;
	private String isEQRSrCompleted;
	/* Property addded for GRT 4.0 */
	private String isEQMSrCompleted;
	private RegistrationType registrationType;
	private String strRegistrationType;
	private String registrationIdentifier;
	private String registrationNotes;
	private Set<SiteList> salMigratedAssets;
	private Status finalValidationSubStatus;
	private Status installBaseSubStatus;
	/* Property addded for GRT 4.0 */
	private Status eqrMoveSubStatus;	
	private long hasSubmittedTrManually;
	private boolean stepBInProcessAction, stepBCompletedAction;
	////
	List<TechnicalRegistration> technicalRegistrationDetailList = new ArrayList<TechnicalRegistration>();
	List<TechnicalRegistration> salRegistrationSummaryList = new ArrayList<TechnicalRegistration>();
	List<SiteList> salMigrationSummaryList = new ArrayList<SiteList>();
	////
	
	private boolean alarmAndConnectivityDisabled = false;
	private boolean superUser;
	private String activeSR;
	
	private java.util.Date ibSubmittedDate;
	private java.util.Date ibCompletedDate;
	private String ibOverriddenBy;
	private java.util.Date eqrSubmittedDate;
	private java.util.Date eqrCompletedDate;
	private String eqrOverriddenBy;
	private java.util.Date tobCompletedDate;
	private String tobOverriddenBy;
    private String soldToBox;
    private String bpLinkId;
    private boolean seidCreationFailureFlag;
    private String ipoAccessType;
    private String Process_Step_Id;
    private String typeOfImpl;
    private String inventoryXML;
    private List<TechnicalOrderDetail> materialEntryList = new ArrayList<TechnicalOrderDetail>();
    private List<RegistrationQuestionsDetail> regQuestnList = new ArrayList<RegistrationQuestionsDetail>();
    private String notes;
    
    /* Properties addded for GRT 4.0 */
    private String toSoldToId;
    private String toSoldToLocation;
    private String toCustomerName;
    private String eqrMoveSrNo;    
    private java.util.Date eqrMoveSubmittedDate;
    private java.util.Date eqrMoveCompletedDate;
    
    //GRT 4.0 : Retest functionality
	List<TechnicalRegistration> retestTRList = new ArrayList<TechnicalRegistration>();
    
    private PurchaseOrder po;
    
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public List<RegistrationQuestionsDetail> getRegQuestnList() {
		return regQuestnList;
	}
	public void setRegQuestnList(List<RegistrationQuestionsDetail> regQuestnList) {
		this.regQuestnList = regQuestnList;
	}
	public List<TechnicalOrderDetail> getMaterialEntryList() {
		return materialEntryList;
	}
	public void setMaterialEntryList(List<TechnicalOrderDetail> materialEntryList) {
		this.materialEntryList = materialEntryList;
	}
	private String status_id;
	
    public String getStatus_id() {
		return status_id;
	}
	public void setStatus_id(String status_id) {
		this.status_id = status_id;
	}
	public String getInventoryXML() {
		return inventoryXML;
	}
	public void setInventoryXML(String inventoryXML) {
		this.inventoryXML = inventoryXML;
	}
	public String getTypeOfImpl() {
		return typeOfImpl;
	}
	public void setTypeOfImpl(String typeOfImpl) {
		this.typeOfImpl = typeOfImpl;
	}
	public String getProcess_Step_Id() {
		return Process_Step_Id;
	}
	public void setProcess_Step_Id(String process_Step_Id) {
		Process_Step_Id = process_Step_Id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIsSrCompleted() {
		return isSrCompleted;
	}
	public void setIsSrCompleted(String isSrCompleted) {
		this.isSrCompleted = isSrCompleted;
	}
	public Date getCut_over_date() {
		return cut_over_date;
	}
	public void setCut_over_date(Date cut_over_date) {
		this.cut_over_date = cut_over_date;
	}
	public String getSubmitted() {
		return submitted;
	}
	public void setSubmitted(String submitted) {
		this.submitted = submitted;
	}
	public String getTypeOfUser() {
		return typeOfUser;
	}
	public void setTypeOfUser(String typeOfUser) {
		this.typeOfUser = typeOfUser;
	}
	public java.util.Set<RegistrationQuestions> getRegistrationQuestions() {
		return registrationQuestions;
	}
	public Status getFinalValidationStatus() {
		return finalValidationStatus;
	}

	public void setFinalValidationStatus(Status finalValidationStatus) {
		if(finalValidationStatus != null && StringUtils.isNotEmpty(finalValidationStatus.getStatusId())) {
			if(finalValidationStatus.equals(StatusEnum.NOTINITIATED.getStatusId())) {
				Status status = new Status();
				status.setStatusId(StatusEnum.NOTINITIATED.getStatusId());
				this.setFinalValidationSubStatus(finalValidationStatus);
			} else if(finalValidationStatus.equals(StatusEnum.SAVED.getStatusId())) {
				Status status = new Status();
				status.setStatusId(StatusEnum.SAVED.getStatusId());
				this.setFinalValidationSubStatus(finalValidationStatus);
			} if(finalValidationStatus.equals(StatusEnum.CANCELLED.getStatusId())) {
				Status status = new Status();
				status.setStatusId(StatusEnum.CANCELLED.getStatusId());
				this.setFinalValidationSubStatus(finalValidationStatus);
			}
		}
		this.finalValidationStatus = finalValidationStatus;
	}

	public Status getEqrMoveStatus() {
		return eqrMoveStatus;
	}
	public void setEqrMoveStatus(Status eqrMoveStatus) {
		this.eqrMoveStatus = eqrMoveStatus;
	}
	public SRRequest getEqrMoveSrRequest() {
		return eqrMoveSrRequest;
	}
	public void setEqrMoveSrRequest(SRRequest eqrMoveSrRequest) {
		this.eqrMoveSrRequest = eqrMoveSrRequest;
	}
	public Status getInstallBaseStatus() {
		return installBaseStatus;
	}

	public void setInstallBaseStatus(Status installBaseStatus) {
		if(installBaseStatus != null && StringUtils.isNotEmpty(installBaseStatus.getStatusId())) {
			if(installBaseStatus.equals(StatusEnum.NOTINITIATED.getStatusId())) {
				Status status = new Status();
				status.setStatusId(StatusEnum.NOTINITIATED.getStatusId());
				this.setInstallBaseSubStatus(installBaseStatus);
			} else if(installBaseStatus.equals(StatusEnum.SAVED.getStatusId())) {
				Status status = new Status();
				status.setStatusId(StatusEnum.SAVED.getStatusId());
				this.setInstallBaseSubStatus(installBaseStatus);
			} if(installBaseStatus.equals(StatusEnum.CANCELLED.getStatusId())) {
				Status status = new Status();
				status.setStatusId(StatusEnum.CANCELLED.getStatusId());
				this.setInstallBaseSubStatus(installBaseStatus);
			}
		}
		this.installBaseStatus = installBaseStatus;
	}

	public Status getTechRegStatus() {
		return techRegStatus;
	}

	public void setTechRegStatus(Status techRegStatus) {
		this.techRegStatus = techRegStatus;
	}



	public void setRegistrationQuestions(
			java.util.Set<RegistrationQuestions> registrationQuestions) {
		this.registrationQuestions = registrationQuestions;
	}

	public String getInventoryFile() {
		return inventoryFile;
	}

	public void setInventoryFile(String inventoryFile) {
		this.inventoryFile = inventoryFile;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getSendMail() {
		return sendMail;
	}

	public void setSendMail(String sendMail) {
		this.sendMail = sendMail;
	}

	public SiteRegistration() {
    }

	public String getRegistrationId() {
		return this.registrationId;
	}
	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public String getAddress() {
		return this.address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public String getCompany() {
		return this.company;
	}
	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompanyPhone() {
		return this.companyPhone;
	}
	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}

	public java.util.Date getCreatedDate() {
		return this.createdDate;
	}
	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCustomerName() {
		return this.customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getExpedireRefernceNo() {
		return this.expedireRefernceNo;
	}
	public void setExpedireRefernceNo(String expedireRefernceNo) {
		this.expedireRefernceNo = expedireRefernceNo;
	}

	public String getExpedite() {
		return this.expedite;
	}
	public void setExpedite(String expedite) {
		this.expedite = expedite;
	}

	public String getFinalValidationSrNo() {
		return this.finalValidationSrNo;
	}
	public void setFinalValidationSrNo(String finalValidationSrNo) {
		this.finalValidationSrNo = finalValidationSrNo;
	}

	public String getFirstName() {
		return this.firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getInstallBaseSrNo() {
		return this.installBaseSrNo;
	}
	public void setInstallBaseSrNo(String installBaseSrNo) {
		this.installBaseSrNo = installBaseSrNo;
	}

	public String getLastName() {
		return this.lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNoAdditionalProductFlag() {
		return this.noAdditionalProductFlag;
	}
	public void setNoAdditionalProductFlag(String noAdditionalProductFlag) {
		this.noAdditionalProductFlag = noAdditionalProductFlag;
	}

	public String getOnsiteEmail() {
		return this.onsiteEmail;
	}
	public void setOnsiteEmail(String onsiteEmail) {
		this.onsiteEmail = onsiteEmail;
	}

	public String getOnsiteFirstName() {
		return this.onsiteFirstName;
	}
	public void setOnsiteFirstName(String onsiteFirstName) {
		this.onsiteFirstName = onsiteFirstName;
	}

	public String getOnsiteLastName() {
		return this.onsiteLastName;
	}
	public void setOnsiteLastName(String onsiteLastName) {
		this.onsiteLastName = onsiteLastName;
	}

	public String getOnsitePhone() {
		return this.onsitePhone;
	}
	public void setOnsitePhone(String onsitePhone) {
		this.onsitePhone = onsitePhone;
	}

	public String getReportEmailId() {
		return this.reportEmailId;
	}
	public void setReportEmailId(String reportEmailId) {
		this.reportEmailId = reportEmailId;
	}

	public String getReportPhone() {
		return this.reportPhone;
	}
	public void setReportPhone(String reportPhone) {
		this.reportPhone = reportPhone;
	}

	public String getSoldToId() {
		return this.soldToId;
	}
	public void setSoldToId(String soldToId) {
		this.soldToId = soldToId;
	}

	public String getSoldToLocation() {
		return this.soldToLocation;
	}
	public void setSoldToLocation(String soldToLocation) {
		this.soldToLocation = soldToLocation;
	}

	public String getSoldToType() {
		return this.soldToType;
	}
	public void setSoldToType(String soldToType) {
		this.soldToType = soldToType;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public java.util.Date getUpdatedDate() {
		return this.updatedDate;
	}
	public void setUpdatedDate(java.util.Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUserName() {
		return this.userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	//bi-directional many-to-one association to ProcessStep
	public ProcessStep getProcessStep() {
		return this.processStep;
	}
	public void setProcessStep(ProcessStep processStep) {
		this.processStep = processStep;
	}

	//bi-directional many-to-one association to Status
	public Status getStatus() {
		return this.status;
	}
	public void setStatus(Status status) {
		this.status = status;
		if(processStep != null && StringUtils.isNotEmpty(this.processStep.getProcessStepId())){
			if(processStep.getProcessStepId().equalsIgnoreCase(ProcessStepEnum.FINAL_RECORD_VALIDATION.getProcessStepId())){
				this.finalValidationStatus = status;
			}else if(processStep.getProcessStepId().equalsIgnoreCase(ProcessStepEnum.INSTALL_BASE_CREATION.getProcessStepId())){
				this.installBaseStatus = status;
			} else if(processStep.getProcessStepId().equalsIgnoreCase(ProcessStepEnum.TECHNICAL_REGISTRATION.getProcessStepId())){
				this.techRegStatus = status;
			} 
		}
	}

	//bi-directional many-to-one association to TechnicalOrder
	public java.util.Set<TechnicalOrder> getTechnicalOrders() {
		return this.technicalOrders;
	}
	public void setTechnicalOrders(java.util.Set<TechnicalOrder> technicalOrders) {
		this.technicalOrders = technicalOrders;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SiteRegistration)) {
			return false;
		}
		SiteRegistration castOther = (SiteRegistration)other;
		return new EqualsBuilder()
			.append(this.getRegistrationId(), castOther.getRegistrationId())
			.isEquals();
    }

	public int hashCode() {
		return new HashCodeBuilder()
			.append(getRegistrationId())
			.toHashCode();
    }

	public String toString() {
		return new ToStringBuilder(this)
			.append("registrationId", getRegistrationId())
			.toString();
	}

	public SRRequest getFinalValidationSrRequest() {
		return finalValidationSrRequest;
	}

	public void setFinalValidationSrRequest(SRRequest finalValidationSrRequest) {
		this.finalValidationSrRequest = finalValidationSrRequest;
	}

	public SRRequest getInstallBaseSrRequest() {
		return installBaseSrRequest;
	}

	public void setInstallBaseSrRequest(SRRequest installBaseSrRequest) {
		this.installBaseSrRequest = installBaseSrRequest;
	}

	public String getSiteCountry() {
		return siteCountry;
	}

	public void setSiteCountry(String siteCountry) {
		this.siteCountry = siteCountry;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getSiebelSync() {
		return siebelSync;
	}

	public void setSiebelSync(String siebelSync) {
		this.siebelSync = siebelSync;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSkipInstallBaseCreation() {
		return skipInstallBaseCreation;
	}

	public void setSkipInstallBaseCreation(String skipInstallBaseCreation) {
		this.skipInstallBaseCreation = skipInstallBaseCreation;
	}

	public String getSalMigrationOnly() {
		return salMigrationOnly;
	}

	public void setSalMigrationOnly(String salMigrationOnly) {
		this.salMigrationOnly = salMigrationOnly;
	}

	public Clob getInventory() {
		return inventory;
	}

	public void setInventory(Clob inventory) {
		this.inventory = inventory;
	}
	public String getTypeOfImplementation() {
		return typeOfImplementation;
	}
	public void setTypeOfImplementation(String typeOfImplementation) {
		this.typeOfImplementation = typeOfImplementation;
	}

	public boolean isOnBoardingFileExisting() {
		boolean returnVal = false;
		if(this.registrationType != null && StringUtils.isNotEmpty(this.registrationType.getRegistrationId()) && this.registrationType.getRegistrationId().equalsIgnoreCase(RegistrationTypeEnum.IPOFFICE.getRegistrationID())){
			if(this.technicalOrders != null) {
				outer:
				for (TechnicalOrder technicalOrder : this.technicalOrders) {
					if(StringUtils.isNotEmpty(technicalOrder.getIsBaseUnit()) && technicalOrder.getIsBaseUnit().equalsIgnoreCase("Y")){
						if(technicalOrder.getTechnicalRegistrations() != null){
							for (TechnicalRegistration technicalRegistration: technicalOrder.getTechnicalRegistrations()) {
								returnVal = StringUtils.isNotEmpty(technicalRegistration.getOnboarding());
								break outer;
							}
						}
					}
				}
			}
		}
		return returnVal;
	}

	public String getOnBoardingFileExisting() {
		String returnVal = "";
		if(this.registrationType != null && StringUtils.isNotEmpty(this.registrationType.getRegistrationId()) && this.registrationType.getRegistrationId().equalsIgnoreCase(RegistrationTypeEnum.IPOFFICE.getRegistrationID())){
			if(this.technicalOrders != null) {
				outer:
				for (TechnicalOrder technicalOrder : this.technicalOrders) {
					if(StringUtils.isNotEmpty(technicalOrder.getIsBaseUnit()) && technicalOrder.getIsBaseUnit().equalsIgnoreCase("Y")){
						if(technicalOrder.getTechnicalRegistrations() != null){
							for (TechnicalRegistration technicalRegistration: technicalOrder.getTechnicalRegistrations()) {
								returnVal = technicalRegistration.getOnboarding();
								break outer;
							}
						}
					}
				}
			}
		}
		return returnVal;
	}
	
	public TechnicalRegistration getIPORecord() {
		if(this.registrationType != null && StringUtils.isNotEmpty(this.registrationType.getRegistrationId()) && this.registrationType.getRegistrationId().equalsIgnoreCase(RegistrationTypeEnum.IPOFFICE.getRegistrationID())){
			if(this.technicalOrders != null) {
				for (TechnicalOrder technicalOrder : this.technicalOrders) {
					if(StringUtils.isNotEmpty(technicalOrder.getIsBaseUnit()) && technicalOrder.getIsBaseUnit().equalsIgnoreCase("Y")) {
						if(technicalOrder.getTechnicalRegistrations() != null) {
							for (TechnicalRegistration technicalRegistration: technicalOrder.getTechnicalRegistrations()) {
								if(technicalRegistration != null && StringUtils.isNotEmpty(technicalRegistration.getAccessType()) && technicalRegistration.getAccessType().equals(GRTConstants.ACCESS_TYPE_IPO)){
									return technicalRegistration;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	public ProcessStepEnum getTempProcessStep() {
		return tempProcessStep;
	}
	public void setTempProcessStep(ProcessStepEnum tempProcessStep) {
		this.tempProcessStep = tempProcessStep;
	}

	public TechnicalOrder getTechnicalOrderByMaterialCode(String materialCode, String serialNumber) {
		if(StringUtils.isNotEmpty(materialCode)) {
			String destination = GRTConstants.BBoxDestination;
			if(StringUtils.isNotEmpty(this.getSoldToBox())) {
				destination = this.getSoldToBox();
			}
			logger.debug("getTechnicalOrderByMaterialCode : Material Code October Release----"+materialCode);
			logger.debug("getTechnicalOrderByMaterialCode : getTechnicalOrders().size()----"+getTechnicalOrders().size());
			if(this.getTechnicalOrders()!= null && this.getTechnicalOrders().size() > 0){
				for (TechnicalOrder technicalOrder : this.getTechnicalOrders()) {
					if(technicalOrder != null){
						if(StringUtils.isNotEmpty(serialNumber)) {
							if(StringUtils.isNotEmpty(technicalOrder.getSerialNumber())) {
								if((materialCode.equalsIgnoreCase(technicalOrder.getMaterialCode()))){
									if(serialNumber.equalsIgnoreCase(technicalOrder.getSerialNumber())){
										logger.debug("technicalOrder.getSerialNumber()---"+technicalOrder.getSerialNumber());
										logger.debug("technicalOrder.getOrderId()---"+technicalOrder.getOrderId());
										return technicalOrder;
									}else{
										logger.debug("Remove zeros on Technical Order Serial number");
										int sizeTechSerNum = technicalOrder.getSerialNumber().length();
										int sizeSerNum = serialNumber.length();
										StringBuffer sb = new StringBuffer();
										if((sizeSerNum!=sizeTechSerNum) && (sizeSerNum < sizeTechSerNum)){
											for(int i=0;i<(sizeTechSerNum-sizeSerNum);i++){
												sb.append("0");
											}	
										}
										sb.append(serialNumber);
										if(sb.toString().equals(technicalOrder.getSerialNumber())){
											logger.debug("technicalOrder.getOrderId()---After putting parenthesis..."+technicalOrder.getOrderId());
											return technicalOrder;	
										}
									}
								}
							}
						}
					}
				}
				for (TechnicalOrder technicalOrder : this.getTechnicalOrders()) {
					if(technicalOrder != null && !technicalOrder.isProcessed()) {
						if((materialCode.equalsIgnoreCase(technicalOrder.getMaterialCode()))){
							if(StringUtils.isEmpty(technicalOrder.getSerialNumber()) || (StringUtils.isNotEmpty(technicalOrder.getSerialNumber()) && GRTConstants.BBoxDestination.equalsIgnoreCase(destination))){
								logger.debug("technicalOrder.getOrderId()---"+technicalOrder.getOrderId());
								technicalOrder.setProcessed(true);
								return technicalOrder;
							}
						}
					}
				}
			} else {
				//invalid input
				logger.debug("Technical Order is not found for material Code");
			}
		}
		return null;
	}
	
	/*
	 * Get Technical Order for Serialized material code
	 */
	public TechnicalOrder getTechOrderForSerializedMatCode(String materialCode,String serialNumber) {
		if(StringUtils.isNotEmpty(materialCode) && StringUtils.isNotEmpty(serialNumber)) {
				logger.debug("getTechOrderForSerializedMatCode, Material Code:" + materialCode);
				logger.debug("getTechOrderForSerializedMatCode, Size:" + getTechnicalOrders().size());
				if(this.getTechnicalOrders()!= null && this.getTechnicalOrders().size() > 0) {
					for (TechnicalOrder technicalOrder : this.getTechnicalOrders()) {
						if(technicalOrder != null && StringUtils.isNotEmpty(technicalOrder.getSerialNumber())) {
							if((materialCode.equalsIgnoreCase(technicalOrder.getMaterialCode()))) {
                                 if(serialNumber.equalsIgnoreCase(technicalOrder.getSerialNumber())) {
                                	 logger.debug("getTechOrderForSerializedMatCode, SerialNumber:" + technicalOrder.getSerialNumber());
                                	 logger.debug("getTechOrderForSerializedMatCode, OrderId:" + technicalOrder.getOrderId());
                                	 return technicalOrder;
                                 } else {
									int sizeTechSerNum = technicalOrder.getSerialNumber().length();
									int sizeSerNum = serialNumber.length();
									
									StringBuffer sb = new StringBuffer();
									if(sizeSerNum < sizeTechSerNum) {
										logger.debug("Adding leading zeros to Siebel passed serialNumber");
										for(int i=0; i<(sizeTechSerNum - sizeSerNum); i++) {
											sb.append("0");
										}
										sb.append(serialNumber);
										if(sb.toString().equals(technicalOrder.getSerialNumber())) {
											logger.debug("getTechOrderToUpdateAsset :technicalOrder.getSerialNumber()---"+technicalOrder.getSerialNumber());
				                            logger.debug("getTechOrderToUpdateAsset : technicalOrder.getOrderId()--"+technicalOrder.getOrderId());
											return technicalOrder;	
										}
									} else {
										logger.debug("Adding leading zeros to Technical Order serialNumber");
										for(int i=0;i<(sizeSerNum-sizeTechSerNum);i++) {
											sb.append("0");
										}
										if(serialNumber.equals(sb.append(technicalOrder.getSerialNumber()).toString())) {
											 logger.debug("getTechOrderToUpdateAsset :technicalOrder.getSerialNumber()---"+technicalOrder.getSerialNumber());
			                               	 logger.debug("getTechOrderToUpdateAsset : technicalOrder.getOrderId()--"+technicalOrder.getOrderId());
											return technicalOrder;	
										}
									}
							}
						}
					}
				}
				
				for(TechnicalOrder technicalOrder : this.getTechnicalOrders()){
					if(technicalOrder != null){
					if((materialCode.equalsIgnoreCase(technicalOrder.getMaterialCode()))){
						if(StringUtils.isEmpty(technicalOrder.getSerialNumber())){
						logger.debug("getTechOrderToUpdateAsset : Ignoring siebel serial number"+serialNumber);
						logger.debug("getTechOrderToUpdateAsset :technicalOrder.getMaterialCode---"+technicalOrder.getMaterialCode());
                   	 	logger.debug("getTechOrderToUpdateAsset : technicalOrder.getOrderId()--"+technicalOrder.getOrderId());
                   	 	return technicalOrder;
						}
					}
				}
				}
				
			}else{
				logger.error("Technical Order not found for the registration Id");
			}
				
		}
		return null;
	}
	
	/*
	 * Get Technical Order for Serialized material code
	 */
	/*
	 * Get Technical Order for Serialized material code
	 */
	public List<TechnicalOrder> getTechOrderForNonSerializedMatCode(String materialCode){
		List<TechnicalOrder> technicalOrderList = new ArrayList<TechnicalOrder>();
		if(StringUtils.isNotEmpty(materialCode)){
				logger.debug("getTechOrderToUpdateAsset : Material Code ----"+materialCode);
				logger.debug("getTechOrderToUpdateAsset :getTechnicalOrders().size()----"+getTechnicalOrders().size());
				if(this.getTechnicalOrders()!= null && this.getTechnicalOrders().size() > 0){
				for(TechnicalOrder technicalOrder : this.getTechnicalOrders()){
					if(technicalOrder != null){
					if((materialCode.equalsIgnoreCase(technicalOrder.getMaterialCode()))){
						technicalOrderList.add(technicalOrder);
					}
				}
			}
				
			}else{
				logger.error("Technical Order not found for the registration Id");
			}
				
		
		}
		return technicalOrderList;
	}
	
	public boolean isAnyTechRegAwaiting () {
		if(this.getTechnicalOrders() != null){
			for (TechnicalOrder technicalOrder : this.getTechnicalOrders()) {
				if(technicalOrder != null){
					if(technicalOrder.getTechnicalRegistrations() != null){
						for (TechnicalRegistration technicalRegistration : technicalOrder.getTechnicalRegistrations()) {
							if(technicalRegistration != null && technicalRegistration.getStatus() != null && StringUtils.isNotEmpty(technicalRegistration.getStatus().getStatusId()) && technicalRegistration.getStatus().getStatusId().equals(StatusEnum.AWAITINGINFO.getStatusId())){
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public RegistrationType getRegistrationType() {
		return registrationType;
	}
	public void setRegistrationType(RegistrationType registrationType) {
		this.registrationType = registrationType;
	}
	public String getRegistrationIdentifier() {
		return registrationIdentifier;
	}
	public void setRegistrationIdentifier(String registrationIdentifier) {
		this.registrationIdentifier = registrationIdentifier;
	}
	public String getRegistrationNotes() {
		return registrationNotes;
	}
	public void setRegistrationNotes(String registrationNotes) {
		this.registrationNotes = registrationNotes;
	}
	public Set<SiteList> getSalMigratedAssets() {
		return salMigratedAssets;
	}
	public void setSalMigratedAssets(Set<SiteList> salMigratedAssets) {
		this.salMigratedAssets = salMigratedAssets;
	}
	/**
	 * @return the finalValidationSubStatus
	 */
	public Status getFinalValidationSubStatus() {
		return finalValidationSubStatus;
	}
	/**
	 * @param finalValidationSubStatus the finalValidationSubStatus to set
	 */
	public void setFinalValidationSubStatus(Status finalValidationSubStatus) {
		this.finalValidationSubStatus = finalValidationSubStatus;
	}
	
	public List<TechnicalRegistration> getNonSalTrs() {
		logger.debug("Entering getNonSalTrs   " );
		List<TechnicalRegistration> trs = new ArrayList<TechnicalRegistration>();
		Set<TechnicalOrder> technicalOrdersSet = this.getTechnicalOrders();
		logger.debug("getTechnicalOrders::"+this.getTechnicalOrders());
		if(technicalOrdersSet != null && technicalOrdersSet.size() >0) {
			for (TechnicalOrder technicalOrder : technicalOrdersSet) {
				//GRT 4.0 Change : For removing retest records
				if( technicalOrder.getOrderType()!=null && 
						!technicalOrder.getOrderType().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_TR_RETEST) ){
					Set<TechnicalRegistration> setOfTechRegs = technicalOrder.getTechnicalRegistrations();
					logger.debug("setOfTechRegs::"+technicalOrder.getTechnicalRegistrations());
					if(setOfTechRegs != null && setOfTechRegs.size() >0) {
						for (TechnicalRegistration tr : setOfTechRegs) {
							///////////////////////////////////
							if (tr != null ){
								tr.getTechnicalRegistrationId();
								if (!GRTConstants.ACCESS_TYPE_SAL.equalsIgnoreCase(tr.getAccessType())) {
									tr = TechnicalRegistrationUIUtil.getNonSalTRs(tr, this.isSuperUser(), this.getTechRegStatus().getStatusId());

									trs.add(tr);
									logger.debug("SIZE of getNonSalTrs::"+trs.size());
								}
							}
							/////////////////////////
						}
					}

				}
			}
		}
		logger.debug("Exitnig getNonSalTrs   " );
		return trs;
	}

	public List<TechnicalRegistration> getSalTrs() {
		logger.debug("Entering getSalTrs " );
		List<TechnicalRegistration> trs = new ArrayList<TechnicalRegistration>();
		Set<TechnicalOrder> technicalOrdersSet = this.getTechnicalOrders();
		if(technicalOrdersSet != null && technicalOrdersSet.size() >0) {
			for (TechnicalOrder technicalOrder : technicalOrdersSet) {
				//GRT 4.0 Change : For removing retest records
				if( technicalOrder.getOrderType()!=null && 
						!technicalOrder.getOrderType().equalsIgnoreCase(GRTConstants.TECH_ORDER_TYPE_TR_RETEST) ){
					Set<TechnicalRegistration> setOfTechRegs = technicalOrder.getTechnicalRegistrations();
					if(setOfTechRegs != null && setOfTechRegs.size() >0) {
						for (TechnicalRegistration tr : setOfTechRegs) {
							///////////////////////////////////
							if (tr != null ){
								tr.getTechnicalRegistrationId();
								if (tr.getAccessType().equalsIgnoreCase(GRTConstants.ACCESS_TYPE_SAL)) {
									tr = TechnicalRegistrationUIUtil.getSalTRs(tr, this.isSuperUser(), this.getTechRegStatus().getStatusId());
									
									trs.add(tr);
								}
							}
							/////////////////////////
						}
					}
				}
			}
		}
		logger.debug("Exitnig getSalTrs  " );
		return trs;
	}
	
	public List<SiteList> getAssets(){
		logger.debug("Entering getAssets  " );
		List<SiteList> siteLists = new ArrayList<SiteList>();
		Set<SiteList> salMigrationSummaryList = this.getSalMigratedAssets();
        logger.debug("The SIZE OF salMigrationSummaryList IS ===============> :: "+ salMigrationSummaryList.size()); 
    	if(salMigrationSummaryList != null && salMigrationSummaryList.size()>0) {

    		for (SiteList siteList : salMigrationSummaryList){
    			if (siteList != null){
    				siteList.getId();
    				siteList.getMaterialCode();
                    logger.debug("The material Code is ===============> :: "+ siteList.getMaterialCode()); 
    				siteList.getMaterialCodeDescription();
                    logger.debug("The getMaterialCodeDescription is ===============> :: "+ siteList.getMaterialCodeDescription());
                    siteList.getGroupId();
                    logger.debug("The getGroupId is ===============> :: "+ siteList.getGroupId());
                    siteList.getSolutionElementId();
                    siteList.getSid();
                    siteList.getMid();
                    siteList.getRemoteAccess();
                    siteList.getPrimarySALGatewaySEID();
                    siteList.getSecondarySALGatewaySEID();
                    siteList.getErrorCode();
                    if(siteList.getErrorDesc() == null){
                    	siteList.setErrorDesc("");
                    }
                    
                    siteList.getSeCode();
                    siteList.getDeviceStatus();
    				siteList.getAlarmId();
    				
    				Status status = siteList.getStatus();
    				Status stepBStatus = siteList.getStepBStatus();
					if (status != null){
						siteList.setStatus(status);
						status.getStatusId();
						logger.debug("Fetching the Status" );
						status.getStatusDescription();
						/////////////
						//siteList.setNumberOfSubmit("0"); //TODO remove this hard coding once the implementation is in place
						if (siteList.getNumberOfSubmit()!= null && ( Integer.parseInt(siteList.getNumberOfSubmit())+1 ) > 2) {
							siteList.setSubmitButtonStatus(true);
							siteList.setUpdateButtonStatus(true);
						} else {
							if(! status.getStatusId().equals(StatusEnum.AWAITINGINFO.getStatusId())) {
								siteList.setSubmitButtonStatus(true);
								siteList.setUpdateButtonStatus(true);
							}
						}
						//Begin Override Logic
						logger.debug("the value of this.isSuperUser()================>:" + this.isSuperUser()+" <<<<<<<<<<<");
						if(!status.getStatusId().equals(StatusEnum.COMPLETED.getStatusId()) && this.isSuperUser()) {
							siteList.setOverRideButtonStatus(false);
						} else {
							siteList.setOverRideButtonStatus(true);
						}
						//End Override Logic
						/////////////
						//Begin Check box enable or disable logic
	                    if (StatusEnum.COMPLETED.getStatusId().equals(status.getStatusId()) &&  (stepBStatus == null || StatusEnum.NOTINITIATED.getStatusId().equals(stepBStatus.getStatusId()))  ){
	                    	siteList.setCheckBoxDisabled(false);
	                    	/*if( GRTConstants.SAL_SE_CODE.equals(siteList.getSeCode()) || GRTConstants.SAL_VIRTUAL_GATEWAY.equals(siteList.getSeCode())   ){
	                    		siteList.setCheckBoxDisabled(true);
	                    	}*/	// wi01114225                    	
	                    } else {
	                    	siteList.setCheckBoxDisabled(true);
	                    }
	                    //Begin Check box enable or disable logic
					}
					if(stepBStatus != null){
						siteList.setStepBStatus(stepBStatus);
						stepBStatus.getStatusId();
						logger.debug("Fetching the Status" );
						stepBStatus.getStatusDescription();
				    	// Begin to populate the Detail Button state i.e Disable / Enable
						// �	The �Detail� button will enable only when Step-B(Alarm & Connectivity status) status is IN-Process or Completed.
				    	if(StatusEnum.COMPLETED.getStatusId().equals(stepBStatus.getStatusId()) || StatusEnum.INPROCESS.getStatusId().equals(stepBStatus.getStatusId())){
				    		siteList.setDetailButtonStatus(false);
				    	} else {
				    		siteList.setDetailButtonStatus(true);
				    	}
				    	// End to populate the Detail Button state i.e Disable / Enable 						
					} else {
						Status stepBst = new Status();
						stepBst.setStatusId(StatusEnum.NOTINITIATED.getStatusId());
						stepBst.setStatusDescription(StatusEnum.NOTINITIATED.getStatusDescription());
						siteList.setStepBStatus(stepBst);
					}
					
					SRRequest srr = siteList.getSrRequest();
					if (srr != null){
						srr.getSiebelSRNo();
						siteList.setSrRequest(srr);
						siteList.setStrSiebelSRNo(srr.getSiebelSRNo());
					} else {
						siteList.setStrSiebelSRNo("");
					}
					SRRequest stepBSRRequest = siteList.getStepBSRRequest();
					if (stepBSRRequest != null){
						stepBSRRequest.getSiebelSRNo();
						siteList.setStepBSRRequest(stepBSRRequest);
						siteList.setStrStepBSRNo(stepBSRRequest.getSiebelSRNo());
					}else{
						siteList.setStrStepBSRNo("");
					}
					
					
    				
                    logger.debug("The getErrorCode is ===============> :: "+ siteList.getErrorCode()); 
    				siteList.getErrorDesc();
    				siteList.setSelectForAlarmAndConnectivity(false);
                    logger.debug("The getErrorDesc is ===============> :: "+ siteList.getErrorDesc());
                    siteList.getPrimarySALGatewaySEID();
                    siteList.getSecondarySALGatewaySEID();
    				
			    	//Begin: Modified for wi01111434
			    	if( StatusEnum.COMPLETED.getStatusId().equals(this.getTechRegStatus().getStatusId()) ){
			    		siteList.setSubmitButtonStatus(true);
			    		siteList.setUpdateButtonStatus(true);
			    		siteList.setCheckBoxDisabled(true);
			    	}
			    	//End: Modified for wi01111434
			    	//Begin: To fetch the ExpandedSolutionElement
			    	List<ExpandedSolutionElement> expSolElements = new ArrayList<ExpandedSolutionElement>();
			    	java.util.Set<ExpandedSolutionElement> explodedSolutionElements = siteList.getExplodedSolutionElements();
			    	if(explodedSolutionElements != null && explodedSolutionElements.size()>0){
				    	for(ExpandedSolutionElement expSolEle : explodedSolutionElements) {
				    		expSolEle.getAlarmId();
				    		expSolEle.getSeCode();
				    		expSolEle.getSeID();
				    		expSolEle.getIpAddress();
				    		expSolEle.getTechnicalRegistration();
				    		
				    		expSolEle.setSid(siteList.getSid());
				    		expSolEle.setMid(siteList.getMid());
				    		expSolEle.setPrimarySalGWSeid(siteList.getPrimarySALGatewaySEID());
				    		expSolEle.setSecondarySalGWSeid(siteList.getSecondarySALGatewaySEID());
				    		//expSolElements.add(expSolEle);
							if(!siteList.getSolutionElementId().equals(expSolEle.getSeID())){
								expSolElements.add(expSolEle);
							}
				    		
				    	}
			    	}
			    	siteList.setExpSolutionElements(expSolElements);
			    	//End: To fetch the ExpandedSolutionElement 
                    siteLists.add(siteList);
    			}
    		}
    		
            logger.debug("The SIZE After conversion to List is ===============> :: "+ siteLists.size());


    	}
    	logger.debug("Exiting getAssets()");
        return siteLists;
	}
	
	
	public long getHasSubmittedTrManually() {
		return hasSubmittedTrManually;
	}
	public void setHasSubmittedTrManually(long hasSubmittedTrManually) {
		this.hasSubmittedTrManually = hasSubmittedTrManually;
	}
	public List<SiteList> getSalMigrationSummaryList() {
		return salMigrationSummaryList;
	}
	public void setSalMigrationSummaryList(List<SiteList> salMigrationSummaryList) {
		this.salMigrationSummaryList = salMigrationSummaryList;
	}
	public List<TechnicalRegistration> getSalRegistrationSummaryList() {
		return salRegistrationSummaryList;
	}
	public void setSalRegistrationSummaryList(
			List<TechnicalRegistration> salRegistrationSummaryList) {
		this.salRegistrationSummaryList = salRegistrationSummaryList;
	}
	public List<TechnicalRegistration> getTechnicalRegistrationDetailList() {
		return technicalRegistrationDetailList;
	}
	public void setTechnicalRegistrationDetailList(
			List<TechnicalRegistration> technicalRegistrationDetailList) {
		this.technicalRegistrationDetailList = technicalRegistrationDetailList;
	}
	public String getStrRegistrationType() {
		return strRegistrationType;
	}
	public void setStrRegistrationType(String strRegistrationType) {
		this.strRegistrationType = strRegistrationType;
	}
	
public StatusEnum computeTRHeaderStatus() {
	StringBuffer sb = new StringBuffer();
	logger.debug("<------------------------ ENTERING INTO computeTRHeaderStatus-------------------------------->");
		StatusEnum status = StatusEnum.INPROCESS;
		boolean isCompleted = true;
		if(this.getRegistrationType().getRegistrationId().equals(RegistrationTypeEnum.SALMIGRATION.getRegistrationID())) {
			if(this.getSalMigratedAssets() != null) {
				for (SiteList sl : this.getSalMigratedAssets()) {
					logger.debug("Looping on SITELIST ID:" + sl.getId());
					if(sl.getStatus() != null) {
						logger.debug("Looping on SITELIST ID:" + sl.getId() + " Status:" + sl.getStatus() + " stepB Status" + sl.getStepBStatus());
						if(sl.getStatus().getStatusId().equals(StatusEnum.AWAITINGINFO.getStatusId())) {
							status = StatusEnum.AWAITINGINFO;
							isCompleted = false;
							break;
						} else if(sl.getStatus().getStatusId().equals(StatusEnum.INPROCESS.getStatusId())) {
							isCompleted = false;
						}  else if(sl.getStatus().getStatusId().equals(StatusEnum.COMPLETED.getStatusId())) {
							if(sl.getStepBStatus() != null) {
								if(sl.getStepBStatus().getStatusId().equals(StatusEnum.AWAITINGINFO.getStatusId())) {
									status = StatusEnum.AWAITINGINFO;
									isCompleted = false;
									break;
								} else if(sl.getStepBStatus().getStatusId().equals(StatusEnum.INPROCESS.getStatusId())) {
									isCompleted = false;
									continue;
								} else if(sl.getStepBStatus().getStatusId().equals(StatusEnum.NOTINITIATED.getStatusId())) {
									isCompleted = false;
									continue;
								}
							} else {
								isCompleted = false;
							}
						}
					}
				}
			}
		} else {
			if(this.getTechnicalOrders() != null) {
				for (TechnicalOrder to : this.getTechnicalOrders()) {
					logger.debug("Looping on TO ID:" + to.getOrderId() + " OrderType:" + to.getOrderType());
					if(to !=null && to.getTechnicalRegistrations() != null) {
						for (TechnicalRegistration tr : to.getTechnicalRegistrations()) {
							logger.debug("Looping on TR ID:" + tr.getTechnicalRegistrationId() + " Status:" + tr.getStatus() + " stepB Status:" + tr.getStepBStatus());
							sb.append("Looping on TR ID:" + tr.getTechnicalRegistrationId() + " Status:" + tr.getStatus() + " stepB Status:" + tr.getStepBStatus()+"...");
							if(tr.getStatus() != null) {
								if(tr.getStatus().getStatusId().equals(StatusEnum.AWAITINGINFO.getStatusId())) {
									status = StatusEnum.AWAITINGINFO;
									isCompleted = false;
									break;
								} else if(tr.getStatus().getStatusId().equals(StatusEnum.INPROCESS.getStatusId())) {
									isCompleted = false;
								}  else if(tr.getStatus().getStatusId().equals(StatusEnum.COMPLETED.getStatusId())) {
									//if(StringUtils.isNotEmpty(tr.getAccessType()) && tr.getAccessType().equalsIgnoreCase(GRTConstants.ACCESS_TYPE_SAL)){
									//GRT 4.0 Change : (Defect#306) Removing Access type condition for Step-B as with RETEST functionality records with any access types
									//can go for alarm & conn(Step B) + 'No Connectivity' records needs to be skipped from Step-B and should directly moved to completed
									if(StringUtils.isNotEmpty(tr.getAccessType()) && !GRTConstants.NO.equalsIgnoreCase(tr.getConnectivity()) && tr.getAccessType().equalsIgnoreCase(GRTConstants.ACCESS_TYPE_SAL)){
										/*if(StringUtils.isNotEmpty(tr.getSolutionElement()) && tr.getSolutionElement().equalsIgnoreCase(GRTConstants.SAL_SE_CODE) && StringUtils.isNotEmpty(tr.getCreatedBy()) && tr.getCreatedBy().equalsIgnoreCase(GRTConstants.USER_ID_SYSTEM)) {
											continue;
										}*/
										sb.append("ACCESS_TYPE_SAL...");
										if(tr.getStepBStatus() != null) {
											if(tr.getStepBStatus().getStatusId().equals(StatusEnum.AWAITINGINFO.getStatusId())) {
												status = StatusEnum.AWAITINGINFO;
												isCompleted = false;
												break;
											} else if(tr.getStepBStatus().getStatusId().equals(StatusEnum.INPROCESS.getStatusId())) {
												isCompleted = false;
												continue;
											} else if(tr.getStepBStatus().getStatusId().equals(StatusEnum.NOTINITIATED.getStatusId())) {
												isCompleted = false;
												continue;
											}
										} else {
											isCompleted = false;
										}
									}
									
									//Added condition for NON-SAL STEP B header directly moving to Completed even when StpeB status is Awaiting Info, need to review the else-if condition QA Defect 645
									else if(StringUtils.isNotEmpty(tr.getAccessType()) && !tr.getAccessType().equalsIgnoreCase(GRTConstants.ACCESS_TYPE_SAL)){
										sb.append("!ACCESS_TYPE_SAL...");
											if(tr.getStepBStatus() != null || GRTConstants.ACCESS_TYPE_IPO.equals(tr.getAccessType())) {
												if(tr.getStepBStatus() != null && tr.getStepBStatus().getStatusId().equals(StatusEnum.AWAITINGINFO.getStatusId())) {
													status = StatusEnum.AWAITINGINFO;
													isCompleted = false;
													break;
												} else if(tr.getStepBStatus() != null && tr.getStepBStatus().getStatusId().equals(StatusEnum.INPROCESS.getStatusId())) {
													isCompleted = false;
													continue;
												}
											}	
									}
									
								}
							}
						}
					}
				}
			}
		}
		if(isCompleted) {
			sb.append("isCompleted"+isCompleted);
			return StatusEnum.COMPLETED;
		}
		logger.debug("<------------------------ LEAVING FROM computeTRHeaderStatus-------------------------------->");
		return status;
	}
		
	public boolean isAlarmAndConnectivityDisabled() {
		return alarmAndConnectivityDisabled;
	}
	public void setAlarmAndConnectivityDisabled(boolean alarmAndConnectivityDisabled) {
		this.alarmAndConnectivityDisabled = alarmAndConnectivityDisabled;
	}
	public boolean isEligibleForStepB() {
		
        //if(this.getRegistrationType().getRegistrationId().equals(RegistrationTypeEnum.SALMIGRATION.getRegistrationID())) {
              if(this.getSalMigratedAssets() != null) {
                    for (SiteList sl : this.getSalMigratedAssets()) {
                          if(sl.getStatus().getStatusId().equals(StatusEnum.COMPLETED.getStatusId()) && (sl.getStepBStatus() == null || sl.getStepBStatus().getStatusId().equals(StatusEnum.NOTINITIATED.getStatusId())) ) {
                        	  logger.debug("<------------------------------------------  Salmigration Record(s) Eligible For Step B --------------------------------->");
                              //if( !GRTConstants.SAL_SE_CODE.equals(sl.getSeCode())  &&  !GRTConstants.SAL_VIRTUAL_GATEWAY.equals(sl.getSeCode()) ){
                              return true;
                              //} // wi01114225
                          }
                    }
              }
        //} else {
              if(this.getTechnicalOrders() != null) {
                    for (TechnicalOrder to : this.getTechnicalOrders()) {
                          if(to != null && to.getTechnicalRegistrations() != null) {
                                for (TechnicalRegistration tr : to.getTechnicalRegistrations()) {
                                      if(tr != null && tr.getAccessType().equals(AccessTypeEnum.SAL.getDbAccessType())) {
                                            if(tr.getStatus().getStatusId().equals(StatusEnum.COMPLETED.getStatusId()) && (tr.getStepBStatus() == null || tr.getStepBStatus().getStatusId().equals(StatusEnum.NOTINITIATED.getStatusId()))) {
                                                logger.debug("<------------------------------------------TR Sal Record(s)  Eligible For Step B --------------------------------->");
                                                //if( !GRTConstants.SAL_SE_CODE.equals(tr.getSolutionElement())  &&  !GRTConstants.SAL_VIRTUAL_GATEWAY.equals(tr.getSolutionElement()) ){
                                                return true;
                                                //} // wi01114225
                                            }
                                                  
                                      }
                                }
                          }
                    }
              }
        //}
        
        logger.debug("<****************************** NOT Eligible for Step B **************************************>");	
        return false;
  }
	public boolean isSuperUser() {
		return superUser;
	}
	public void setSuperUser(boolean superUser) {
		this.superUser = superUser;
	}
	
	public Status getInstallBaseSubStatus() {
		return installBaseSubStatus;
	}
	public void setInstallBaseSubStatus(Status installBaseSubStatus) {
		this.installBaseSubStatus = installBaseSubStatus;
	}
	public Status getEqrMoveSubStatus() {
		return eqrMoveSubStatus;
	}
	public void setEqrMoveSubStatus(Status eqrMoveSubStatus) {
		this.eqrMoveSubStatus = eqrMoveSubStatus;
	}
	public SRRequest getEqrActiveContractsSrRequest() {
		return eqrActiveContractsSrRequest;
	}
	public void setEqrActiveContractsSrRequest(SRRequest eqrActiveContractsSrRequest) {
		this.eqrActiveContractsSrRequest = eqrActiveContractsSrRequest;
	}

	public boolean isRemoteConnectivity() {
		boolean returnVal = false;
		if(this.registrationType != null && StringUtils.isNotEmpty(this.registrationType.getRegistrationId()) && this.registrationType.getRegistrationId().equalsIgnoreCase(RegistrationTypeEnum.IPOFFICE.getRegistrationID())){
			if(this.technicalOrders != null) {
				for (TechnicalOrder technicalOrder : this.technicalOrders) {
					if(StringUtils.isNotEmpty(technicalOrder.getIsBaseUnit()) && technicalOrder.getIsBaseUnit().equalsIgnoreCase("Y")) {
						return technicalOrder.isAutoTR();
					}
				}
			}
		}
		return returnVal;
	}
	public boolean isStepBCompletedAction() {
		return stepBCompletedAction;
	}
	public void setStepBCompletedAction(boolean stepBCompletedAction) {
		this.stepBCompletedAction = stepBCompletedAction;
	}
	public boolean isStepBInProcessAction() {
		return stepBInProcessAction;
	}
	public void setStepBInProcessAction(boolean stepBInProcessAction) {
		this.stepBInProcessAction = stepBInProcessAction;
	}
	
	public boolean isTOBSRCreated() {
        if(this.getRegistrationType().getRegistrationId().equals(RegistrationTypeEnum.SALMIGRATION.getRegistrationID())) {
              if(this.getSalMigratedAssets() != null) {
                    for (SiteList sl : this.getSalMigratedAssets()) {
                          if(sl != null && sl.getSrRequest() != null) {
                              return true;
                          }
                    }
              }
        } else {
              if(this.getTechnicalOrders() != null) {
                    for (TechnicalOrder to : this.getTechnicalOrders()) {
                          if(to != null && to.getTechnicalRegistrations() != null) {
                                for (TechnicalRegistration tr : to.getTechnicalRegistrations()) {
                                      if(tr != null && tr.getSrRequest() != null) {
                                    	  return true;                                                  
                                      }
                                }
                          }
                    }
              }
        }
        return false;
  }
	
	public boolean isBBoxRegistration() {
		if(StringUtils.isNotEmpty(this.getSoldToId())) {
			return this.getSoldToId().startsWith(GRTConstants.BBoxSoldPrefix);
		}
		return false;
	}
	public String getActiveSR() {
		return activeSR;
	}
	public void setActiveSR(String activeSR) {
		this.activeSR = activeSR;
	}
	public java.util.Date getEqrCompletedDate() {
		return eqrCompletedDate;
	}
	public void setEqrCompletedDate(java.util.Date eqrCompletedDate) {
		this.eqrCompletedDate = eqrCompletedDate;
	}
	public String getEqrOverriddenBy() {
		return eqrOverriddenBy;
	}
	public void setEqrOverriddenBy(String eqrOverriddenBy) {
		this.eqrOverriddenBy = eqrOverriddenBy;
	}
	public java.util.Date getEqrSubmittedDate() {
		return eqrSubmittedDate;
	}
	public void setEqrSubmittedDate(java.util.Date eqrSubmittedDate) {
		this.eqrSubmittedDate = eqrSubmittedDate;
	}
	public java.util.Date getIbCompletedDate() {
		return ibCompletedDate;
	}
	public void setIbCompletedDate(java.util.Date ibCompletedDate) {
		this.ibCompletedDate = ibCompletedDate;
	}
	public String getIbOverriddenBy() {
		return ibOverriddenBy;
	}
	public void setIbOverriddenBy(String ibOverriddenBy) {
		this.ibOverriddenBy = ibOverriddenBy;
	}
	public java.util.Date getIbSubmittedDate() {
		return ibSubmittedDate;
	}
	public void setIbSubmittedDate(java.util.Date ibSubmittedDate) {
		this.ibSubmittedDate = ibSubmittedDate;
	}
	public java.util.Date getTobCompletedDate() {
		return tobCompletedDate;
	}
	public void setTobCompletedDate(java.util.Date tobCompletedDate) {
		this.tobCompletedDate = tobCompletedDate;
	}
	public String getTobOverriddenBy() {
		return tobOverriddenBy;
	}
	public void setTobOverriddenBy(String tobOverriddenBy) {
		this.tobOverriddenBy = tobOverriddenBy;
	}
	public String getIsEQRSrCompleted() {
		return isEQRSrCompleted;
	}
	public void setIsEQRSrCompleted(String isEQRSrCompleted) {
		this.isEQRSrCompleted = isEQRSrCompleted;
	}
    public String getSoldToBox()
    {
        return soldToBox;
    }
    public void setSoldToBox(String soldToBox)
    {
        this.soldToBox = soldToBox;
    }
	public String getBpLinkId() {
		return bpLinkId;
	}
	public void setBpLinkId(String bpLinkId) {
		this.bpLinkId = bpLinkId;
	}
	public boolean isSeidCreationFailureFlag() {
		return seidCreationFailureFlag;
	}
	public void setSeidCreationFailureFlag(boolean seidCreationFailureFlag) {
		this.seidCreationFailureFlag = seidCreationFailureFlag;
	}
	public String getIpoAccessType() {
		return ipoAccessType;
	}
	public void setIpoAccessType(String ipoAccessType) {
		this.ipoAccessType = ipoAccessType;
	}
	
	public PurchaseOrder getPo() {
		return po;
	}

	public void setPo(PurchaseOrder po) {
		this.po = po;
	}
	public String getToSoldToId() {
		return toSoldToId;
	}
	public void setToSoldToId(String toSoldToId) {
		this.toSoldToId = toSoldToId;
	}
	public String getToSoldToLocation() {
		return toSoldToLocation;
	}
	public void setToSoldToLocation(String toSoldToLocation) {
		this.toSoldToLocation = toSoldToLocation;
	}
	public String getEqrMoveSrNo() {
		return eqrMoveSrNo;
	}
	public void setEqrMoveSrNo(String eqrMoveSrNo) {
		this.eqrMoveSrNo = eqrMoveSrNo;
	}
	
	
	public java.util.Date getEqrMoveSubmittedDate() {
		return eqrMoveSubmittedDate;
	}
	public void setEqrMoveSubmittedDate(java.util.Date eqrMoveSubmittedDate) {
		this.eqrMoveSubmittedDate = eqrMoveSubmittedDate;
	}
	public java.util.Date getEqrMoveCompletedDate() {
		return eqrMoveCompletedDate;
	}
	public void setEqrMoveCompletedDate(java.util.Date eqrMoveCompletedDate) {
		this.eqrMoveCompletedDate = eqrMoveCompletedDate;
	}
	public String getIsEQMSrCompleted() {
		return isEQMSrCompleted;
	}
	public void setIsEQMSrCompleted(String isEQMSrCompleted) {
		this.isEQMSrCompleted = isEQMSrCompleted;
	}
	public List<TechnicalRegistration> getRetestTRList() {
		return retestTRList;
	}
	public void setRetestTRList(List<TechnicalRegistration> retestTRList) {
		this.retestTRList = retestTRList;
	}
	public String getToCustomerName() {
		return toCustomerName;
	}
	public void setToCustomerName(String toCustomerName) {
		this.toCustomerName = toCustomerName;
	}
	
	
}