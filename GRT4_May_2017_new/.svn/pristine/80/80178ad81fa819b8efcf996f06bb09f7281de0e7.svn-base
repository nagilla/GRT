package com.avaya.grt.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.avaya.grt.dao.TechnicalRegistrationArtDao;
import com.avaya.grt.mappers.ArtProductType;
import com.avaya.grt.mappers.LocalTRConfig;
import com.avaya.grt.mappers.SRRequest;
import com.avaya.grt.mappers.SalProductRegistration;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.grt.dto.Attachment;
import com.grt.dto.SRRequestDto;
import com.grt.dto.ServiceRequest;
import com.grt.dto.TRConfig;
import com.grt.integration.art.ARTClient;
import com.grt.util.DataAccessException;
import com.grt.util.GRTConstants;
import com.grt.util.SRRequestStatusEnum;
import com.grt.util.TechRecordEnum;
import com.grt.util.TechnicalOrderDetailWorsheetProcessor;



public class TechnicalRegistrationArtService extends BaseRegistrationService {
	private static final Logger logger = Logger.getLogger(TechnicalRegistrationArtService.class);
	
	
	public ARTClient artClient;
	//ossno_ip from grt.properties
	public String ossnoIP;
	//service_name from grt.properties
	public String artServiceName;
	
	public TechnicalRegistrationArtDao technicalRegistrationArtDao;
	
	public final String ART_EMAIL_ALERT_TYPE_COMPLETED = "completed";

	public final String ART_EMAIL_ALERT_TYPE_GENERIC_ERROR = "exception";

	public final String ART_EMAIL_ALERT_TYPE_RESPONSE_FAILED = "artResponseFailed";
	
	public List<LocalTRConfig> getLocalTRConfig(TRConfig trConfig) throws DataAccessException {
		logger.debug("Entering TechnicalRegistrationArtService.getLocalTRConfig");
		 
    	List<LocalTRConfig> localTRConfigs =   getTechnicalRegistrationArtDao().getLocalTRConfig(trConfig);

    	if(localTRConfigs != null && localTRConfigs.size()>0) {
    		modifiedLocalTRConfigs(localTRConfigs);
    	}
    	logger.debug("Exiting TechnicalRegistrationArtService.getLocalTRConfig");
    	return localTRConfigs;
    }

     
    
    
	
	  
	/*
	 * [AVAYA] GRT2.0 Creating SR in case of failure of SAL technical registration in ART
	 */
	public SRRequest createSR(String id, TechRecordEnum recordType, String errorCode, String errorDesc) throws Exception {
		
		logger.debug("Entering TechnicalRegistrationArtService.createSR ");
		logger.debug("id: "+id);
		logger.debug("recordType: "+recordType);
		logger.debug("errorCode: "+errorCode);
		logger.debug("errorDesc: "+errorDesc);
		SRRequest srRequest = null;
		SalProductRegistration salProductRegistration = new SalProductRegistration();		
		String generateAttachmentInPath = "";
		try {
			generateAttachmentInPath = getUploadPath(attachmentName + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ATTACHMENT_NAME_EXTENSION);
			} catch (Exception e) {
				logger.debug("error while generating attachement path", e);
			}
			salProductRegistration.setFilePath(generateAttachmentInPath);
			salProductRegistration.setErrorCode(errorCode);
			salProductRegistration.setErrorDescription(errorDesc);

		if(TechRecordEnum.IP_MODEM.getSalType().equals(recordType.getSalType()) || TechRecordEnum.IPO.getSalType().equals(recordType.getSalType())) {
			//Implemented in TechnicalRegistrationArtDao
			TechnicalRegistration technicalRegistration =  getTechnicalRegistrationArtDao().getTechnicalRegistration(id);

			if(technicalRegistration != null){				
				salProductRegistration.setTechnicalRegistration(technicalRegistration);	
				salProductRegistration.setSoldTo(technicalRegistration.getSoldToId());
				logger.debug(" TechnicalRegistration set in case of IPO or Modem");
			}			
		} /*else if(TechRecordEnum.SAL_MIGRATION.getSalType().equals(recordType.getSalType())) {
			SalMigration salMigration = getRegistrationDao().getSalMigrationDetail(id);
			if(salMigration != null){				
				salProductRegistration.setSalMigration(salMigration);
				salProductRegistration.setSoldTo(salMigration.getSalRegistration().getSiteRegistration().getSoldToId());
				logger.debug("salMigration set to DTO ");
			} 		
		} else if( TechRecordEnum.SAL_SITE_LIST.getSalType().equals(recordType.getSalType())){
			SiteList siteList  = getRegistrationDao().getSiteList(id);
			if(siteList != null){
				salProductRegistration.setSiteList(siteList);
				salProductRegistration.setSoldTo(siteList.getSalMigration().getSalRegistration().getSiteRegistration().getSoldToId());
				logger.debug("siteList set to DTO ");
			}
		}*/	
		srRequest = createSR(salProductRegistration, recordType);
		if(srRequest==null){
			logger.debug("SRRequest is Null in SalAsync----CreateSR---");
		}
		logger.debug("Exiting TechnicalRegistrationArtService.createSR ");
		return srRequest;
	}  
	
	public SRRequest createSR(SalProductRegistration salProductRegistration, TechRecordEnum type) throws Exception {
		   logger.debug("Entering TechnicalRegistrationArtService.createSR ... ");
	    	SRRequest srRequest = null;  	
	    	
	    	//Implemented this method in BaseHibernateDao 
	        srRequest = getTechnicalRegistrationArtDao().initSRRequest(salProductRegistration, type);       	
	    	String soldTo = salProductRegistration.getSoldTo(); 
	    	logger.debug("SoldTo:"+soldTo);
	    	logger.debug("Type:" + type.getSalType());
	    	SRRequestDto srRequestDto=null;    	
	    	//Generating worksheet
	    	TechnicalOrderDetailWorsheetProcessor worksheet = new TechnicalOrderDetailWorsheetProcessor();
	    	List<Attachment> attList = new ArrayList<Attachment>();
	    	Attachment attachment = null;
	    	if(TechRecordEnum.IP_MODEM.getSalType().equals(type.getSalType()) || TechRecordEnum.IPO.getSalType().equals(type.getSalType())) {
	    		if (salProductRegistration.getTechnicalRegistration().getAccessType().equals(GRTConstants.ACCESS_TYPE_IPO)){
	    			salProductRegistration.setOptType(GRTConstants.ACCESS_TYPE_IPO);
	    			 attachment = worksheet.generateWorksheetTechRegIPO(salProductRegistration);
	    		}else if (salProductRegistration.getTechnicalRegistration().getAccessType().equals(GRTConstants.ACCESS_TYPE_IP)){
	    			salProductRegistration.setOptType(GRTConstants.ACCESS_TYPE_IP);
	    			if(salProductRegistration.getTechnicalRegistration() != null) {
	    				String aorig = getAorig(salProductRegistration.getTechnicalRegistration().getProductCode(), salProductRegistration.getTechnicalRegistration().getConnectivity());
	    				salProductRegistration.setAorig(aorig);
	    			}
	    			 attachment = worksheet.generateWorksheetTechRegIP(salProductRegistration);
	    		}else if (salProductRegistration.getTechnicalRegistration().getAccessType().equals(GRTConstants.ACCESS_TYPE_MODEM)){
	    			salProductRegistration.setOptType(GRTConstants.ACCESS_TYPE_MODEM);
	    			if(salProductRegistration.getTechnicalRegistration() != null) {
	    				String aorig = getAorig(salProductRegistration.getTechnicalRegistration().getProductCode(), salProductRegistration.getTechnicalRegistration().getConnectivity());
	    				salProductRegistration.setAorig(aorig);
	    			}
	   			 	attachment = worksheet.generateWorksheetTechRegModem(salProductRegistration);
	    		} 
	 	}
	    	 if( TechRecordEnum.SAL_SITE_LIST.getSalType().equals(type.getSalType())){
	    		 salProductRegistration.setOptType(GRTConstants.SALSiteList_desc); 
	    		  attachment = worksheet.generateWorksheetTechRegSALSiteList(salProductRegistration);    	 
	    	 }
	    		 
	        attList.add(attachment);  
	       
	        if(srRequest!=null){
	        	 logger.debug("...SR Request is not null ... ");
		        srRequestDto = TechnicalRegistrationUtil.convertSRRequestDto(srRequest);
		        srRequest = processSRRequestForSal(salProductRegistration, soldTo, attList, srRequestDto, type);
	        }
	        if(srRequest==null){
				logger.debug("SRRequest is Null in ARTService----CreateSR---");
			}
	        logger.debug("Exiting TechnicalRegistrationArtService.createSR ");
	        
	        return srRequest;
	    }
	
	
	
	
	public String getAorig(String productCode, String connectivity){
		 logger.debug("Entering TechnicalRegistrationArtService.getAorig");
		   String aorig = GRTConstants.Y;
		   if(StringUtils.isEmpty(connectivity)) {
			   connectivity = GRTConstants.NO;
		   }
		   try {
			   //Implemented in TechnicalRegistrationArtDao
			   ArtProductType productType = getTechnicalRegistrationArtDao().getProductTypeByTypeCode(productCode);
			   if(productType != null) {
				   if(connectivity.equalsIgnoreCase(GRTConstants.YES) && StringUtils.isNotEmpty(productType.getAorigRequired()) && productType.getAorigRequired().equalsIgnoreCase(GRTConstants.Y)) {
		   				aorig = GRTConstants.Y;
		   			} else {
		   				aorig = GRTConstants.N;
		   			}
		   		} else {
		   			aorig = GRTConstants.N;
		   		}
		   }  
		   
		   catch(DataAccessException dataAccessException){
			   logger.error(dataAccessException);
		   }
		   catch (Exception e) {
			   logger.error(e);
		   }
		   logger.debug("Exiting TechnicalRegistrationArtService.getAorig");
		   return aorig;
	   }
	
	public SRRequest processSRRequestForSal(SalProductRegistration salProductRegistration, String soldToId, List<Attachment> attachmentList, SRRequestDto srRequestDto, TechRecordEnum type) throws Exception {
        logger.debug("Entering TechnicalRegistrationArtService.processSRRequestForSal");
        SRRequest srRequest =null;
        try {
            if(StringUtils.isNotEmpty(srRequestDto.getSiebelSRNo())) {
                ServiceRequest sr = getSiebelClient().querySR(srRequestDto.getSiebelSRNo());
                if (GRTConstants.SIEBEL_SR_STATUS_PENDING.equalsIgnoreCase(sr
                        .getStatus())) {
                	logger.debug("Inside : "+sr.getStatus());
                	logger.debug("SR_STATUS SIEBEL_SR_STATUS_PENDING : ");
                    srRequestDto.setStatusId(SRRequestStatusEnum.SR_CREATED.getStatusId());
                    srRequest = TechnicalRegistrationUtil.convertSRRequest(srRequestDto);
                    getTechnicalRegistrationArtDao().updateSRRequest(srRequest);
                }
            }
 
            if(SRRequestStatusEnum.INITIATION.getStatusId().equalsIgnoreCase(srRequestDto.getStatusId())) {
            	logger.debug("SR_INITIATION : "+srRequestDto.getStatusId());
                ServiceRequest sr = createSiebelSRForSal(salProductRegistration, soldToId, type);
                srRequestDto.setSiebelSRNo(sr.getSrNumber());
                srRequestDto.setStatusId(SRRequestStatusEnum.SR_CREATED.getStatusId());
                srRequest = TechnicalRegistrationUtil.convertSRRequest(srRequestDto);
                getTechnicalRegistrationArtDao().updateSRRequest(srRequest);
            }

            if(SRRequestStatusEnum.SR_CREATED.getStatusId().equalsIgnoreCase(srRequestDto.getStatusId())) {
            	logger.debug("SR_CREATED : "+srRequestDto.getStatusId());
                for (Attachment att:attachmentList){
                    createAttachmentForSiebelSR(srRequestDto.getSiebelSRNo(), att);
                }

                srRequestDto.setStatusId(SRRequestStatusEnum.ATTACHMENT_SENT.getStatusId());
                srRequest = TechnicalRegistrationUtil.convertSRRequest(srRequestDto);
                getTechnicalRegistrationArtDao().updateSRRequest(srRequest);
            }

            if(SRRequestStatusEnum.ATTACHMENT_SENT.getStatusId().equalsIgnoreCase(srRequestDto.getStatusId())) {
            	logger.debug("ATTACHMENT_SENT : "+srRequestDto.getStatusId());
                ServiceRequest sr = getSiebelClient().querySR(srRequestDto.getSiebelSRNo());
                logger.debug("Inside ATTACHMENT_SENT getSrNumber : "+sr.getSrNumber());
                logger.debug("Inside ATTACHMENT_SENT sr.getStatus() : "+sr.getStatus());
                if(GRTConstants.SIEBEL_SR_STATUS_NEW.equalsIgnoreCase(sr.getStatus())) {
                	logger.debug("Inside ATTACHMENT_SENT SIEBEL_SR_STATUS_NEW. : "+sr.getStatus());
                    getSiebelClient().assignSR(srRequestDto.getSiebelSRNo());
                }
                else if(GRTConstants.SIEBEL_SR_STATUS_PENDING.equalsIgnoreCase(sr.getStatus())) {
                	logger.debug("Inside ATTACHMENT_SENT SIEBEL_SR_STATUS_PENDING. : "+sr.getStatus());
                    getSiebelClient().updateSR(srRequestDto.getSiebelSRNo());
                }

                srRequestDto.setStatusId(SRRequestStatusEnum.SUCCESS.getStatusId());
                srRequest = TechnicalRegistrationUtil.convertSRRequest(srRequestDto);
                getTechnicalRegistrationArtDao().updateSRRequest(srRequest);
            }
        }
        catch (Exception ex) {
            logger.error("Unexpected exception while processing registration: " , ex);
            //Neeraj Need to be implement this..
            //sendSystemAlert(Id, srRequestDto);
            throw ex;
        }
        logger.debug("Exiting TechnicalRegistrationArtService.processSRRequestForSal");
        return srRequest;
    }

	
	
	public ARTClient getArtClient() {
		return artClient;
	}


	public void setArtClient(ARTClient artClient) {
		this.artClient = artClient;
	}

	public String getOssnoIP() {
		return ossnoIP;
	}

	public void setOssnoIP(String ossnoIP) {
		this.ossnoIP = ossnoIP;
	}

	public String getArtServiceName() {
		return artServiceName;
	}

	public void setArtServiceName(String artServiceName) {
		this.artServiceName = artServiceName;
	}

	public TechnicalRegistrationArtDao getTechnicalRegistrationArtDao() {
		return technicalRegistrationArtDao;
	}

	public void setTechnicalRegistrationArtDao(
			TechnicalRegistrationArtDao technicalRegistrationArtDao) {
		this.technicalRegistrationArtDao = technicalRegistrationArtDao;
	}

	
				    
	
}
