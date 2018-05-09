package com.avaya.grt.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.avaya.grt.mappers.ExpandedSolutionElement;
import com.avaya.grt.mappers.SiteList;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.mappers.TechnicalOrder;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.grt.util.DataAccessException;
import com.grt.util.GRTConstants;
import com.grt.util.GRTUtil;
import com.grt.util.ProcessStepEnum;
import com.grt.util.StatusEnum;

public class TechnicalRegistrationSRDao extends TechnicalRegistrationDao {
	private static final Logger logger = Logger.getLogger(TechnicalRegistrationSRDao.class);
	
	
		public List<SiteList> getSiteListByArtSR (String artSrNo) throws DataAccessException {
	        logger.debug("Entering TechnicalRegistrationSRDao : getTechnicalRegistrationByArtSR()");
	        List<SiteList> siteLists = null;
	        try {
	          if(StringUtils.isNotEmpty(artSrNo)) {
	        		Query query=getSessionForGRT().getNamedQuery("getSiteListByArtSR");
	        		query.setParameter("artSrNo", artSrNo);
	            	siteLists = query.list();
	                if (siteLists == null || siteLists.size() <= 0) {
	                    return null;
	                }
	        	}
	        } catch (HibernateException hibEx) {
	            throw new DataAccessException(TechnicalRegistrationSRDao.class, hibEx
	                    .getMessage(), hibEx);
	        }
	        logger.debug("Exiting TechnicalRegistrationSRDao : getTechnicalRegistrationByArtSR()");
	
	        return siteLists;

    	}
	
		public List<TechnicalRegistration> getTechnicalRegistrationByArtSRForStepB(String srNumber) throws DataAccessException {
	        logger.debug("Entering getTechnicalRegistrationByArtSRForStepB srNumber:" + srNumber);
	        List<TechnicalRegistration> technicalRegistrations = null;
	        try {
	            if(StringUtils.isNotEmpty(srNumber)) {
	        	 	Query query=getSessionForGRT().getNamedQuery("getTechnicalRegistrationByArtSRForStepB");
	       	 		query.setParameter("srNumber", srNumber);
	            	technicalRegistrations = query.list();
	                if (technicalRegistrations == null || technicalRegistrations.size() <= 0) {
	                    return null;
	                }
	                // Begin Fetch the Exploded Solution Elements for tr
	                for(TechnicalRegistration tr : technicalRegistrations){
	                    java.util.Set<ExpandedSolutionElement> explodedSolutionElements = tr.getExplodedSolutionElements();
	                    populateExpSolutionElementsList(explodedSolutionElements, tr);
	                }
	                // End Fetch the Exploded Solution Elements for tr
	
	        	}
	        } catch (HibernateException hibEx) {
	            throw new DataAccessException(TechnicalRegistrationSRDao.class, hibEx
	                    .getMessage(), hibEx);
	        } finally {
	        		logger.debug("Exiting getTechnicalRegistrationByArtSRForStepB srNumber:" + srNumber);
	        }
	        return technicalRegistrations;
    	}
	
		
	 
		/**
	     * Search for SiteRegistration with the given siebelSR.
	     * @param siebelSR
	     * @return result SiteRegistration
	     * @throws DataAccessException
	     */
	    public SiteRegistration getSiteRegistrationBySiebelSR(String siebelSR, String typeOfReq) throws DataAccessException {
	        logger.debug("Starting for Siebel SR Number [" + siebelSR + "]");
	        SiteRegistration siteRegistration = null;
	        try {
	           
	        	if ( typeOfReq!=null ){
	        		Query query = null;
	        		//GRT 4.0 change : different queries to improve performance
	        		if( GRTConstants.SIEBEL_SR_SUB_TYPE_INSTALL_BASE_CREATION.equalsIgnoreCase(typeOfReq)){
	        			query = getSessionForGRT().getNamedQuery("getSiteRegistrationBySiebelSRIB");
	        		}else if( GRTConstants.SIEBEL_SR_SUB_TYPE_FINAL_RECORD_VALIDATION.equalsIgnoreCase(typeOfReq)){
	        			query = getSessionForGRT().getNamedQuery("getSiteRegistrationBySiebelSRFV");
	        		}else if( GRTConstants.SIEBEL_SR_SUB_TYPE_EQUIPMENT_MOVE.equalsIgnoreCase(typeOfReq)){
	        			query = getSessionForGRT().getNamedQuery("getSiteRegistrationBySiebelSREQM");
	        		}

	        		query.setString("siebelSR", siebelSR);
	        		siteRegistration = (SiteRegistration) query.uniqueResult();
	        	}
	        } catch (HibernateException hibEx) {
	            logger.error("ERROR: While processing data access for Siebel SR Number ["
	                    + siebelSR + "]. Error message: " + hibEx.getMessage());
	            throw new DataAccessException(TechnicalRegistrationSRDao.class, hibEx.getMessage(), hibEx);
	        }

	        if(siteRegistration != null) {
	            logger.debug("Completed. Returning found record with Registration ID [" + siteRegistration.getRegistrationId() + "]");
	        } else {
	            logger.debug("Completed. No qualified record found given SR Number [" + siebelSR + "]");
	        }
	        return siteRegistration;
	    }
	    
	    /**
	     * Save/Update siteRegistration
	     * @param siteRegistration
	     * @return SiteRegistration
	     * @throws DataAccessException
	     */
	    public SiteRegistration updateSiteRegistrationOnSRUpdate(String registrationId, ProcessStepEnum processStep, StatusEnum statusEnum, String isSrCompleted) throws DataAccessException {
	        logger.debug("Entering TechnicalRegistrationSRDao : updateSiteRegistrationOnSRUpdate()");
	        SiteRegistration registration = null;
	        try {
	            Session session = getSessionForGRT();
	            session.beginTransaction();
	            // Fetching SiteRegistration based on registrationId
				registration = (SiteRegistration) getEntity(session, SiteRegistration.class, "registrationId", registrationId);
				// Fetching Status object
				Criteria criteria =  session.createCriteria(Status.class);
	            criteria.add(Restrictions.eq("statusId", statusEnum.getStatusId()));
	            Status status = (Status)  criteria.uniqueResult();
				
				if (processStep !=null) {
	        		if (processStep.getProcessStepId().equals(ProcessStepEnum.INSTALL_BASE_CREATION.getProcessStepId())) {
	        			registration.setIsSrCompleted(isSrCompleted);
	    				// Update IB Status when status from database is different from status to be updated
	    				if(!statusEnum.getStatusId().equalsIgnoreCase(registration.getInstallBaseStatus().getStatusId())){
	    					registration.setInstallBaseStatus(status);
	    				}
	        			if(StatusEnum.COMPLETED.getStatusId().equals(statusEnum.getStatusId())){
	        				registration.setIbCompletedDate(new Date());
	        			}
	        		} else if (processStep.getProcessStepId().equals(ProcessStepEnum.FINAL_RECORD_VALIDATION.getProcessStepId())){
	        			registration.setIsEQRSrCompleted(isSrCompleted);
	    				// Update EQR Status when status from database is different from status to be updated
	    				if(!statusEnum.getStatusId().equalsIgnoreCase(registration.getFinalValidationStatus().getStatusId())){
	    					registration.setFinalValidationStatus(status);
	    				}
	        			if(StatusEnum.COMPLETED.getStatusId().equals(statusEnum.getStatusId())){
	        				registration.setEqrCompletedDate(new Date());
	        			}
	        		}
	        		/* GRT 4.0 - Start of Equipment Move changes for completing registrations */
	        		else if (processStep.getProcessStepId().equals(ProcessStepEnum.EQUIPMENT_MOVE.getProcessStepId())){
	        			registration.setIsEQMSrCompleted(isSrCompleted);
	    				// Update EQR Status when status from database is different from status to be updated
	    				if(!statusEnum.getStatusId().equalsIgnoreCase(registration.getEqrMoveStatus().getStatusId())){
	    					registration.setEqrMoveStatus(status);
	    				}
	        			if(StatusEnum.COMPLETED.getStatusId().equals(statusEnum.getStatusId())){
	        				registration.setEqrMoveCompletedDate(new Date());
	        			}
	        		}
	        		/* GRT 4.0 - End of Equipment Move changes for completing registrations */
				}
				// Update Open Quantity to 0 for SR Created records
				List<String> assetsToBeProcessed = new ArrayList<String>();
				List<String> processedRegistrations = new ArrayList<String>();
				String orderType = "";
				for (TechnicalOrder order : registration.getTechnicalOrders()) {
	            	String srCreated = order.getSr_Created();
	            	if(processStep.getProcessStepId().equals(ProcessStepEnum.INSTALL_BASE_CREATION.getProcessStepId())){
	            		orderType = GRTConstants.TECH_ORDER_TYPE_IB;
		            	if (srCreated != null && srCreated.equalsIgnoreCase(GRTConstants.YES) 
		            			&& orderType.equalsIgnoreCase(order.getOrderType())){
		            		order.setOpenQuantity(new Long(0));
		            		assetsToBeProcessed.add(order.getMaterialCode());
		            	}
	            	} else if (processStep.getProcessStepId().equals(ProcessStepEnum.FINAL_RECORD_VALIDATION.getProcessStepId())){
	            		orderType = GRTConstants.TECH_ORDER_TYPE_FV;
		            	if (srCreated != null && srCreated.equalsIgnoreCase(GRTConstants.YES) 
		            			&& orderType.equalsIgnoreCase(order.getOrderType())){
		            		order.setOpenQuantity(new Long(0));
		            		assetsToBeProcessed.add(order.getMaterialCode());
		            	}
	            	}
	            	/* GRT 4.0 - Start of Equipment Move changes for completing registrations */
	            	else if (processStep.getProcessStepId().equals(ProcessStepEnum.EQUIPMENT_MOVE.getProcessStepId())){
	            		orderType = GRTConstants.TECH_ORDER_TYPE_EM;
		            	if (srCreated != null && srCreated.equalsIgnoreCase(GRTConstants.YES) 
		            			&& orderType.equalsIgnoreCase(order.getOrderType())){
		            		order.setOpenQuantity(new Long(0));
		            		assetsToBeProcessed.add(order.getMaterialCode());
		            	}
	            	}
	            	/* GRT 4.0 - End of Equipment Move changes for completing registrations */
	            }
				// Processing pipeline records
				if(assetsToBeProcessed.size() > 0){
		        	// Removing duplicates
		        	assetsToBeProcessed = GRTUtil.removeDuplicateStringFromList(assetsToBeProcessed);
					processedRegistrations.add(registrationId);
					this.updateProcessedPipelineTransactions(processedRegistrations, assetsToBeProcessed, orderType);
				}
	            session.saveOrUpdate(registration);
	            session.getTransaction().commit();
	        } catch (HibernateException hibEx) {
	            throw new DataAccessException(TechnicalRegistrationSRDao.class, hibEx
	                    .getMessage(), hibEx);
	        }
	        logger.debug("Exiting TechnicalRegistrationSRDao : updateSiteRegistrationOnSRUpdate()");
	        return registration;
	    }
	    
	    public List<TechnicalRegistration> getTechnicalRegistrationByArtSR(String srNumber) throws DataAccessException {
            logger.debug("Entering getTechnicalRegistrationByArtSR srNumber:" + srNumber);
            List<TechnicalRegistration> technicalRegistrations = null;
            try {
                if(StringUtils.isNotEmpty(srNumber)) {
                	Criteria criteria =  getSessionForGRT().createCriteria(TechnicalRegistration.class);
                    criteria.add(Restrictions.eq("artSrNo", srNumber));
                    technicalRegistrations = criteria.list();
                    if (technicalRegistrations == null || technicalRegistrations.size() <= 0) {
                        return null;
                    }
            	}
            } catch (HibernateException hibEx) {
                throw new DataAccessException(TechnicalRegistrationSRDao.class, hibEx.getMessage(), hibEx);
            } finally {
            	logger.debug("Exiting getTechnicalRegistrationByArtSR srNumber:" + srNumber);
            }
            return technicalRegistrations;

        }
	    
	    
	    public List<SiteList> getSiteListsByArtSr(String artSrNo) throws DataAccessException {
	        logger.debug("Entering getSiteListsByArtSr for ART SR #:" + artSrNo);
	        List<SiteList> siteLists = null;
	        try {
	            Criteria criteria =  getSessionForGRT().createCriteria(SiteList.class);
	            criteria.add(Restrictions.eq("artSrNo", artSrNo));
	            siteLists = criteria.list();
	        } catch (HibernateException hibEx) {
	        	logger.error("", hibEx);
	            throw new DataAccessException(TechnicalRegistrationSRDao.class, hibEx
	                    .getMessage(), hibEx);
	        } finally {
	        	logger.debug("Exiting getSiteListsByArtSr for ART SR #:" + artSrNo);
	        }
	        return siteLists;
	    }
	    
	    /**
	     * Update SiteList StepB Status with the given status
	     *
	     * @param siteRegistration
	     * @param status
	     * @return siteRegistration SiteRegistration
	     * @throws DataAccessException
	     */
	    public SiteList updateSiteListStepBStatus(SiteList siteList, StatusEnum status) throws DataAccessException {
	        logger.debug("Entering TechnicalRegistrationSRDao : updateSiteListStatus()");
	        SiteList resultObject = null;
	        try {
	            Session session = getSessionForGRT();
	            session.beginTransaction();
	            Criteria criteria =  session.createCriteria(SiteList.class);
	            criteria.add(Restrictions.eq("id", siteList.getId()));
	            resultObject = (SiteList)  criteria.uniqueResult();
	            criteria =  session.createCriteria(Status.class);
	            criteria.add(Restrictions.eq("statusId", status.getStatusId()));
	            Status s = (Status)  criteria.uniqueResult();
	            logger.debug("StepBStatus before update:" + s.getStatusDescription());
	            resultObject.setStepBStatus(s);
	            if(siteList.getStepBSRRequest() != null) {
	            	resultObject.setStepBSRRequest(siteList.getStepBSRRequest());
		           	logger.debug("siteList.getArtSrNo():" + siteList.getArtSrNo());
	            	resultObject.setArtSrNo(siteList.getArtSrNo());
	            	if(StringUtils.isNotEmpty(siteList.getDeviceLastAlarmReceivedDate())) {
	            		resultObject.setDeviceLastAlarmReceivedDate(siteList.getDeviceLastAlarmReceivedDate());
	            	}
	            	if(StringUtils.isNotEmpty(siteList.getDeviceStatus())) {
	            		resultObject.setDeviceStatus(siteList.getDeviceStatus());
	            	}
	            }
	            if(GRTConstants.TRUE.equals(siteList.getCancelled())){
	            	if(resultObject.getStepBSRRequest() != null){
	            	session.delete(resultObject.getStepBSRRequest());
	            	}
	            	resultObject.setStepBSRRequest(null);
	            }
	            resultObject.setSelectForRemoteAccess(siteList.isSelectForRemoteAccess());
	            resultObject.setSelectForAlarming(siteList.isSelectForAlarming());
	            java.util.Set<ExpandedSolutionElement> expSolutionElements = getExpSolutionElementsSetForSalMigration(resultObject, siteList.getExpSolutionElements());
	            if(!expSolutionElements.isEmpty()){
	            	resultObject.setExplodedSolutionElements(expSolutionElements);
	            }
	            if(siteList.getStepBSubmittedDate() != null){
	            	resultObject.setStepBSubmittedDate(siteList.getStepBSubmittedDate());
	            }

	            if(siteList.getStepBCompletedDate() != null){
	            	resultObject.setStepBCompletedDate(siteList.getStepBCompletedDate());
	            }
	            
	            //Defect #784 : Empty Error Details When SR is completed
	            resultObject.setErrorCode(siteList.getErrorCode());
	            resultObject.setTroubleShootURL(siteList.getTroubleShootURL());
	            
	            session.saveOrUpdate(resultObject);
	            Status stepBstatus = resultObject.getStepBStatus();
	            logger.debug("After update StepBStatus:" + stepBstatus.getStatusId()+" Description is ::"+stepBstatus.getStatusDescription());
	            Status st = resultObject.getStatus();
	            session.getTransaction().commit();
	            logger.debug("The Site List satus=====================>"+st.getStatusId()+" Description is ::"+st.getStatusDescription());
	            if (st != null && st.getStatusId()!= null) {
	            	resultObject.setStatus(st);
	            }
	        } catch (HibernateException hibEx) {
	        	logger.error("", hibEx);
	            throw new DataAccessException(TechnicalRegistrationSRDao.class, hibEx
	                    .getMessage(), hibEx);
	        }
	        logger.debug("Exiting TechnicalRegistrationSRDao : updateSiteListStatus()");

	        return resultObject;
	    }
	    
	    
	    
	    /**
	     * Update SiteRegistration Status with the given status
	     *
	     * @param siteRegistration
	     * @param status
	     * @return siteRegistration SiteRegistration
	     * @throws DataAccessException
	     */
	    public TechnicalRegistration updateTechnicalRegistrationStepBStatusCancelled(TechnicalRegistration technicalRegistration, StatusEnum status) throws DataAccessException {
	        logger.debug("Entering TechnicalRegistrationSRDao : updateTechnicalRegistrationStatus()");
	        TechnicalRegistration resultObject = null;
	        try {
	            Session session = getSessionForGRT();
	            session.beginTransaction();
	            Criteria criteria =  session.createCriteria(TechnicalRegistration.class);
	            criteria.add(Restrictions.eq("technicalRegistrationId", technicalRegistration.getTechnicalRegistrationId()));
	            resultObject = (TechnicalRegistration)  criteria.uniqueResult();
	            criteria =  session.createCriteria(Status.class);
	            criteria.add(Restrictions.eq("statusId", status.getStatusId()));
	            Status s = (Status)  criteria.uniqueResult();
	            resultObject.setStepBStatus(s);
	            if(technicalRegistration.getStepBSRRequest() != null) {
	            	resultObject.setStepBSRRequest(technicalRegistration.getStepBSRRequest());
	            	logger.debug("technicalRegistration.getArtSrNo():" + technicalRegistration.getArtSrNo());
	            	resultObject.setArtSrNo(technicalRegistration.getArtSrNo());
	            	if(StringUtils.isNotEmpty(technicalRegistration.getDeviceLastAlarmReceivedDate())) {
	            		resultObject.setDeviceLastAlarmReceivedDate(technicalRegistration.getDeviceLastAlarmReceivedDate());
	            	}
	            	if(StringUtils.isNotEmpty(technicalRegistration.getDeviceStatus())) {
	            		resultObject.setDeviceStatus(technicalRegistration.getDeviceStatus());
	            	}
	            }
	            if(GRTConstants.TRUE.equals(technicalRegistration.getCancelled())){
	            	if(resultObject.getStepBSRRequest() != null){
	            	//session.delete(resultObject.getStepBSRRequest());
	            	}
	            	//resultObject.setStepBSRRequest(null);
	            	resultObject.setStepBStatus(s);
	            }
	            resultObject.setSelectForRemoteAccess(technicalRegistration.isSelectForRemoteAccess());
	            resultObject.setSelectForAlarming(technicalRegistration.isSelectForAlarming());
	            java.util.Set<ExpandedSolutionElement> expSolutionElements = getExpSolutionElementsSet(technicalRegistration.getExpSolutionElements());
	            if(!expSolutionElements.isEmpty()){
	            	resultObject.setExplodedSolutionElements(expSolutionElements);
	            }
	            if(technicalRegistration.getStepBSubmittedDate() != null){
	            	resultObject.setStepBSubmittedDate(technicalRegistration.getStepBSubmittedDate());
	            }
	            if(technicalRegistration.getStepBCompletedDate() != null){
	            	resultObject.setStepBCompletedDate(technicalRegistration.getStepBCompletedDate());
	            }
	            //Defect #784 : Empty Error Details
	            resultObject.setErrorCode(technicalRegistration.getErrorCode());
	            resultObject.setTroubleShootURL(technicalRegistration.getTroubleShootURL());
	            
	            session.saveOrUpdate(resultObject);
	            session.getTransaction().commit();

	        } catch (HibernateException hibEx) {
	            throw new DataAccessException(TechnicalRegistrationSRDao.class, hibEx
	                    .getMessage(), hibEx);
	        }
	        logger.debug("Exiting TechnicalRegistrationSRDao : updateTechnicalRegistrationStatus()");

	        return resultObject;
	    }
	    
	    /**
	     * Update SiteRegistration Status with the given status
	     *
	     * @param siteRegistration
	     * @param status
	     * @return siteRegistration SiteRegistration
	     * @throws DataAccessException
	     */
	    public TechnicalRegistration updateTechnicalRegistrationStepBStatus(TechnicalRegistration technicalRegistration, StatusEnum status) throws DataAccessException {
	        logger.debug("Entering TechnicalRegistrationSRDao : updateTechnicalRegistrationStatus()");
	        TechnicalRegistration resultObject = null;
	        try {
	            Session session = getSessionForGRT();
	            session.beginTransaction();
	            Criteria criteria =  session.createCriteria(TechnicalRegistration.class);
	            criteria.add(Restrictions.eq("technicalRegistrationId", technicalRegistration.getTechnicalRegistrationId()));
	            resultObject = (TechnicalRegistration)  criteria.uniqueResult();
	            criteria =  session.createCriteria(Status.class);
	            criteria.add(Restrictions.eq("statusId", status.getStatusId()));
	            Status s = (Status)  criteria.uniqueResult();
	            resultObject.setStepBStatus(s);
	            if(technicalRegistration.getStepBSRRequest() != null) {
	            	resultObject.setStepBSRRequest(technicalRegistration.getStepBSRRequest());
	            	logger.debug("technicalRegistration.getArtSrNo():" + technicalRegistration.getArtSrNo());
	            	resultObject.setArtSrNo(technicalRegistration.getArtSrNo());
	            	if(StringUtils.isNotEmpty(technicalRegistration.getDeviceLastAlarmReceivedDate())) {
	            		resultObject.setDeviceLastAlarmReceivedDate(technicalRegistration.getDeviceLastAlarmReceivedDate());
	            	}
	            	if(StringUtils.isNotEmpty(technicalRegistration.getDeviceStatus())) {
	            		resultObject.setDeviceStatus(technicalRegistration.getDeviceStatus());
	            	}
	            }
	            if(GRTConstants.TRUE.equals(technicalRegistration.getCancelled())){
	            	if(resultObject.getStepBSRRequest() != null){
	            	session.delete(resultObject.getStepBSRRequest());
	            	}
	            	resultObject.setStepBSRRequest(null);
	            }
	            resultObject.setSelectForRemoteAccess(technicalRegistration.isSelectForRemoteAccess());
	            resultObject.setSelectForAlarming(technicalRegistration.isSelectForAlarming());
	            java.util.Set<ExpandedSolutionElement> expSolutionElements = getExpSolutionElementsSet(technicalRegistration.getExpSolutionElements());
	            if(!expSolutionElements.isEmpty()){
	            	resultObject.setExplodedSolutionElements(expSolutionElements);
	            }
	            if(technicalRegistration.getStepBSubmittedDate() != null){
	            	resultObject.setStepBSubmittedDate(technicalRegistration.getStepBSubmittedDate());
	            }
	            if(technicalRegistration.getStepBCompletedDate() != null){
	            	resultObject.setStepBCompletedDate(technicalRegistration.getStepBCompletedDate());
	            }
	            //Defect #784 : Empty Error Details
	            resultObject.setErrorCode(technicalRegistration.getErrorCode());
	            resultObject.setTroubleShootURL(technicalRegistration.getTroubleShootURL());
	            
	            session.saveOrUpdate(resultObject);
	            session.getTransaction().commit();

	        } catch (HibernateException hibEx) {
	            throw new DataAccessException(TechnicalRegistrationSRDao.class, hibEx
	                    .getMessage(), hibEx);
	        }
	        logger.debug("Exiting TechnicalRegistrationSRDao : updateTechnicalRegistrationStatus()");

	        return resultObject;
	    }
	    private java.util.Set<ExpandedSolutionElement> getExpSolutionElementsSet(List<ExpandedSolutionElement> expandedSolutionElements){
	    	java.util.Set<ExpandedSolutionElement> resultsSet = new HashSet<ExpandedSolutionElement>();
	    	if(!expandedSolutionElements.isEmpty()){
		    	for(ExpandedSolutionElement expandedSolutionElement : expandedSolutionElements){
		    		resultsSet.add(expandedSolutionElement);
		    	}
	    	}
	    	return resultsSet;
	    }
	    
	    
	    
	    public SiteList getSiteListByArtSr(String artSrNo) throws DataAccessException {
	        logger.debug("Entering TechnicalRegistrationSRDao : getSiteListByArtSr()");
	        SiteList siteList = null;
	        try {
	            Criteria criteria =  getSessionForGRT().createCriteria(SiteList.class);
	            criteria.add(Restrictions.eq("artSrNo", artSrNo));
	            siteList = (SiteList) criteria.uniqueResult();
	        } catch (HibernateException hibEx) {
	            throw new DataAccessException(TechnicalRegistrationSRDao.class, hibEx
	                    .getMessage(), hibEx);
	        }
	        logger.debug("Exiting TechnicalRegistrationSRDao : getSiteListByArtSr()");
	        return siteList;
	    }
	    
	    public void updateSiteListId(String oldID, String newID) throws DataAccessException {
	    	logger.debug("Entering TechnicalRegistrationSRDao.updateSiteListId()");
	    	Query sqlQuery = null;
	    	Transaction transaction = null;
	    	try {
		    	transaction = getSessionForGRT().beginTransaction();
		    	sqlQuery = getSessionForGRT().getNamedQuery("updateSiteListId");
		        sqlQuery.setString("NEWID", newID);
		        sqlQuery.setString("OLDID", oldID);
		        sqlQuery.executeUpdate();
		        transaction.commit();
		    } catch (HibernateException hibEx) {
				logger.error("", hibEx);
				transaction.rollback();
				throw new DataAccessException(TechnicalRegistrationDao.class, hibEx.getMessage(), hibEx);
			} catch (Throwable throwable) {
				logger.error("", throwable);
				transaction.rollback();
				throw new DataAccessException(TechnicalRegistrationDao.class, throwable.getMessage(), throwable);
			} finally {
				logger.debug("Exiting TechnicalRegistrationSRDao.updateSiteListId()");
			}
	    }
	    
	    public void updateTechnicalRegistrationId(String oldID, String newID) throws DataAccessException {
	    	logger.debug("Entering TechnicalRegistrationSRDao.updateTechnicalRegistrationId()");
	    	Query sqlQuery = null;
	    	Transaction transaction = null;
	    	try {
		    	transaction = getSessionForGRT().beginTransaction();
		    	sqlQuery = getSessionForGRT().getNamedQuery("updateTechnicalRegistrationId");
		        sqlQuery.setString("NEWID", newID);
		        sqlQuery.setString("OLDID", oldID);
		        sqlQuery.executeUpdate();
		        transaction.commit();
		    } catch (HibernateException hibEx) {
				logger.error("", hibEx);
				transaction.rollback();
				throw new DataAccessException(TechnicalRegistrationDao.class, hibEx.getMessage(), hibEx);
			} catch (Throwable throwable) {
				logger.error("", throwable);
				transaction.rollback();
				throw new DataAccessException(TechnicalRegistrationDao.class, throwable.getMessage(), throwable);
			} finally {
				logger.debug("Exiting TechnicalRegistrationSRDao.updateTechnicalRegistrationId()");
			}
	    }
	    
	    public void updateExpandedSolutionElementIdTechRegId(String oldID, String newID, String newTechRedID) throws DataAccessException {
	    	logger.debug("Entering TechnicalRegistrationSRDao.updateExpandedSolutionElementIdTechRegId()");
	    	Query sqlQuery = null;
	    	Transaction transaction = null;
	    	try {
		    	transaction = getSessionForGRT().beginTransaction();
		    	sqlQuery = getSessionForGRT().getNamedQuery("updateExpandedSolutionElementIdTechRegId");
		        sqlQuery.setString("NEWID", newID);
		        sqlQuery.setString("NEWTECHREGID", newTechRedID);
		        sqlQuery.setString("OLDID", oldID);
		        sqlQuery.executeUpdate();
		        transaction.commit();
		    } catch (HibernateException hibEx) {
				logger.error("", hibEx);
				transaction.rollback();
				throw new DataAccessException(TechnicalRegistrationDao.class, hibEx.getMessage(), hibEx);
			} catch (Throwable throwable) {
				logger.error("", throwable);
				transaction.rollback();
				throw new DataAccessException(TechnicalRegistrationDao.class, throwable.getMessage(), throwable);
			} finally {
				logger.debug("Exiting TechnicalRegistrationSRDao.updateExpandedSolutionElementIdTechRegId()");
			}
	    }
		
}
