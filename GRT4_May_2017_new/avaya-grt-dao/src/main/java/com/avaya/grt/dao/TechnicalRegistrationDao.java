package com.avaya.grt.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.avaya.grt.mappers.AccountCreation;
import com.avaya.grt.mappers.BPAccountTempAccess;
import com.avaya.grt.mappers.ExpandedSolutionElement;
import com.avaya.grt.mappers.FuzzySearchParam;
import com.avaya.grt.mappers.LogAccountUpdate;
import com.avaya.grt.mappers.PipelineSapTransactions;
import com.avaya.grt.mappers.SiebelAssetData;
import com.avaya.grt.mappers.SiteList;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.mappers.TechnicalOrder;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.avaya.grt.mappers.TokenRedemption;
import com.grt.dto.InstallBaseAssetData;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.DataAccessException;
import com.grt.util.GRTConstants;
import com.grt.util.MedalLevelEnum;
import com.grt.util.ProcessStepEnum;
import com.grt.util.StatusEnum;

public class TechnicalRegistrationDao extends TechnicalRegistrationArtDao {
	
	private final Logger logger = Logger.getLogger(TechnicalRegistrationDao.class);
	private String ibaseQueue;
	private String eqrQueue;

	 public String getIbaseQueue() {
		return ibaseQueue;
	}

	public void setIbaseQueue(String ibaseQueue) {
		this.ibaseQueue = ibaseQueue;
	}

	public String getEqrQueue() {
		return eqrQueue;
	}

	public void setEqrQueue(String eqrQueue) {
		this.eqrQueue = eqrQueue;
	}

	/**
	 * method to create technical registration
	 * 
	 * @param technicalRegistration
	 * @return
	 * @throws DataAccessException
	 */
	public String createTR(TechnicalRegistration technicalRegistration) throws DataAccessException{
	    	String techRegId=null;
	    	try {
	            Session session = getSessionForGRT();
	            session.beginTransaction();
	            session.saveOrUpdate(technicalRegistration);
	            session.getTransaction().commit();
	            techRegId = technicalRegistration.getTechnicalRegistrationId();
	            logger.debug("technical registration id"+techRegId);
	        } catch (HibernateException hibEx) {
	            logger.error("Error in TechnicalRegistrationDao.saveProductRegistration : " +  hibEx.getMessage());
	            throw new DataAccessException(TechnicalRegistrationDao.class, hibEx
	                    .getMessage(), hibEx);
	        } catch (Exception ex){
	            getSessionForGRT().getTransaction().rollback();
	            ex.printStackTrace();
	            logger.error("Error in TechnicalRegistrationDao.saveProductRegistration : " +  ex.getMessage());
	        }
	        return techRegId;
	    }
	    
	    /**
	   	 * API to save or Update PipelineSapTransactions.
	   	 *
	   	 * @param pipelineSapTransactionsList List<PipelineSapTransactions>
	   	 * @throws DataAccessException Exception
	   	 */
	   	public List<PipelineSapTransactions> savePipelineSapTransactionsList(
	   			List<PipelineSapTransactions> pipelineSapTransactionsList)
	   			throws DataAccessException {
	   		logger
	   				.debug("Entering TechnicalRegistrationDao.savePipelineSapTransactionsList()");
	   		try {
	   			Session session = getSessionForGRT();
	   			session.beginTransaction();

	   			for (PipelineSapTransactions pst : pipelineSapTransactionsList) {
	   				PipelineSapTransactions pipelineSapTransactions;
	   				if (!StringUtils.isEmpty(pst.getPlSapTransactionId())) {
	   					pipelineSapTransactions = (PipelineSapTransactions) session
	   							.load(PipelineSapTransactions.class, pst
	   									.getPlSapTransactionId());
	   				} else {
	   					pipelineSapTransactions = new PipelineSapTransactions();
	   				}
	   				pipelineSapTransactions.setRegistrationId(pst
	   						.getRegistrationId());
	   				pipelineSapTransactions.setShipTo(pst.getShipTo());
	   				pipelineSapTransactions.setMaterialCode(pst.getMaterialCode());
	   				pipelineSapTransactions.setSerialNumber(pst.getSerialNumber());
	   				pipelineSapTransactions.setEquipmentNumber(pst
	   						.getEquipmentNumber());
	   				pipelineSapTransactions.setToEquipmentNumber(pst
	   						.getToEquipmentNumber());
	   				pipelineSapTransactions.setQuantity(pst.getQuantity());
	   				pipelineSapTransactions.setAction(pst.getAction());
	   				pipelineSapTransactions.setDateTime(pst.getDateTime());
	   				pipelineSapTransactions.setIbSubmittedDate(pst.getIbSubmittedDate());
	   				pipelineSapTransactions.setProcessed(pst.isProcessed());
	   				pipelineSapTransactions.setTechnicallyRegisterable(pst.
	   						isTechnicallyRegisterable());
	   				pipelineSapTransactions.setSapCompleted(pst.isSapCompleted());
	   				pipelineSapTransactions.setBeforeQuantity(pst.getBeforeQuantity());
	   				pipelineSapTransactions.setAfterQuantity(pst.getAfterQuantity());
	   				session.saveOrUpdate(pipelineSapTransactions);
	   				pst.setPlSapTransactionId(pipelineSapTransactions
	   						.getPlSapTransactionId());//Brought back the PlSapTransactionId after save the PipelineSapTransactions
	   			}
	   			session.getTransaction().commit();
	   			logger.debug("Exiting TechnicalRegistrationDao.savePipelineSapTransactionsList()");
	   			return pipelineSapTransactionsList;
	   		} catch (HibernateException hibEx) {
	   			logger
	   					.error("Error in TechnicalRegistrationDao.savePipelineSapTransactionsList : "
	   							+ hibEx.getMessage());
	   			throw new DataAccessException(TechnicalRegistrationDao.class, hibEx
	   					.getMessage(), hibEx);
	   		}
	   	}
	   	
	   	/**
	     * Update SiteRegistration EQR Sub Status with the given status
	     *
	     * @param siteRegistration
	     * @param status
	     * @return siteRegistration SiteRegistration
	     * @throws DataAccessException
	     */
	    public SiteRegistration updateSiteRegistrationSubStatus(SiteRegistration siteRegistration, StatusEnum status, ProcessStepEnum processStep) throws DataAccessException {
		    	logger.debug("Starting for TechnicalRegistrationDao.updateSiteRegistrationSubStatus with Registration ID [" + siteRegistration.getRegistrationId() + "] and status ID to update [" + status.getStatusId() + "]");
		        Transaction transaction = null;
		 		SiteRegistration registration = null;
		        try {
		        	logger.debug("########"+siteRegistration+"&&&&&"+status+"&&&&&&"+processStep);
		            Session session = getSessionForGRT();
		 			transaction = session.beginTransaction();
					registration = (SiteRegistration) getEntity(session, SiteRegistration.class, "registrationId", siteRegistration.getRegistrationId());
					Status statusP = new Status();
					statusP.setStatusId(status.getStatusId());
					if(processStep.getProcessStepId().equals(ProcessStepEnum.FINAL_RECORD_VALIDATION.getProcessStepId())) {
						registration.setFinalValidationSubStatus(statusP);
					} else if(processStep.getProcessStepId().equals(ProcessStepEnum.INSTALL_BASE_CREATION.getProcessStepId())) {
						registration.setInstallBaseSubStatus(statusP);
					} else if(processStep.getProcessStepId().equals(ProcessStepEnum.EQUIPMENT_MOVE.getProcessStepId())) {
						registration.setEqrMoveSubStatus(statusP); //GRT 4.0 : defect#259
					}
		            session.saveOrUpdate(registration);
		            logger.debug("Committing change into database ...");
		            transaction.commit();
		        } catch (HibernateException hibEx) {
					transaction.rollback();
					logger.debug("Error in updateSiteRegistrationEQRSubStatus");
					hibEx.printStackTrace();
					throw new DataAccessException(TechnicalRegistrationDao.class, hibEx
							.getMessage(), hibEx);
				} catch (Exception ex) {
					transaction.rollback();
					logger.debug("Error in updateSiteRegistrationEQRSubStatus");
					ex.printStackTrace();
					throw new DataAccessException(TechnicalRegistrationDao.class, ex
							.getMessage(), ex);
				}
		        logger.debug("Exiting TechnicalRegistrationDao.updateSiteRegistrationSubStatus");
		        return registration;
	    }
	    
	    /**
	     * method to process pipeline basedon EQR And IB Quantity
	     * 
	     * @param successList
	     * @return
	     * @throws DataAccessException
	     */
	    @SuppressWarnings("unchecked")
		public Map<String,List<String>> processPipelineBasedonEQRAndIBQuantity(List<TechnicalOrderDetail> successList) throws DataAccessException{
	    	logger.debug("Entering TechnicalRegistrationDao.processPipelineBasedonEQRAndIBQuantity");
	    	Map<String,List<String>> registrationIdMap = new HashMap<String, List<String>>();
	    	List<String> mcList = null;
	    	long quantity = 0;
	    	try {
	    		Criteria criteria = null;
	    		Session session = getSessionForGRT();
	            session.beginTransaction();

	            for(TechnicalOrderDetail eqrDto : successList){
	            	quantity = 0;
	                List<PipelineSapTransactions> pipelineSapTransactionsList;
	            	criteria =  session.createCriteria(PipelineSapTransactions.class);
	                criteria.add(Restrictions.eq("materialCode", eqrDto.getMaterialCode()));
	                criteria.add(Restrictions.eq("shipTo", eqrDto.getSoldToId()));
	                //GRT 4.0 change
	                if(GRTConstants.ACTION_TYPE_U.equalsIgnoreCase(eqrDto.getActionType())) {
	                	criteria.add(Restrictions.eq("action", GRTConstants.TECH_ORDER_TYPE_IB));
	                } else {
	                criteria.add(Restrictions.eq("action", eqrDto.getActionType()));
	                }
	                criteria.add(Restrictions.eq("processed", Boolean.FALSE));
	                pipelineSapTransactionsList = (List<PipelineSapTransactions>)  criteria.list();
	                for(PipelineSapTransactions pipelineSapTransactions : pipelineSapTransactionsList){
	                	quantity +=pipelineSapTransactions.getQuantity();
	                }
	                logger.debug("MC:"+eqrDto.getMaterialCode()+"      Pipeline IB Qty:"+quantity + "      EQR Qty:"+eqrDto.getRemovedQuantity());
	                if(quantity == eqrDto.getRemovedQuantity().longValue()){
	                	List<String> reglist = new ArrayList<String>();
	                	reglist.add(eqrDto.getRegistrationId());
	                	List<String> processlist = new ArrayList<String>();
	                	processlist.add(eqrDto.getMaterialCode());
	                	if(GRTConstants.ACTION_TYPE_U.equalsIgnoreCase(eqrDto.getActionType())) {
	                		this.updateProcessedPipelineTransactions(reglist, processlist, GRTConstants.TECH_ORDER_TYPE_FV);
	                	} else {
	                	//GRT 4.0 change
	                	this.updateProcessedPipelineTransactions(reglist, processlist, eqrDto.getActionType());
	                	}
		                for(PipelineSapTransactions pipelineSapTransactions : pipelineSapTransactionsList){
			                pipelineSapTransactions.setQuantity(0);
			                pipelineSapTransactions.setProcessed(true);
			                pipelineSapTransactions.setProcessedDate(new Date());
			                pipelineSapTransactions.setBeforeQuantity(eqrDto.getBeforeQuantity());
			                pipelineSapTransactions.setAfterQuantity(eqrDto.getAfterQuantity());
			                pipelineSapTransactions.setToEquipmentNumber(eqrDto.getToEquipmentNumber());
			                session.saveOrUpdate(pipelineSapTransactions);
			                // RegistrationId already on MAP
			                if(registrationIdMap.containsKey(pipelineSapTransactions.getRegistrationId())){
			                	mcList = registrationIdMap.get(pipelineSapTransactions.getRegistrationId());
			                	// New MC addition to the list to specific RegistrationId in MAP
			                	if(mcList != null && !mcList.contains(pipelineSapTransactions.getMaterialCode())){
			                		mcList.add(pipelineSapTransactions.getMaterialCode());
			                		registrationIdMap.remove(pipelineSapTransactions.getRegistrationId());
				                	registrationIdMap.put(pipelineSapTransactions.getRegistrationId(), mcList);
			                	}
			                } else { // For new RegistrationId
			                	mcList = new ArrayList<String>();
			                	mcList.add(pipelineSapTransactions.getMaterialCode());
			                	registrationIdMap.put(pipelineSapTransactions.getRegistrationId(), mcList);
			                }
		                }
	                }
	            }
	            session.getTransaction().commit();
	        } catch (HibernateException hibEx) {
	        	logger.error("HibernateException:processPipelineBasedonEQRAndIBQuantity:"+ hibEx.getMessage());
	            throw new DataAccessException(TechnicalRegistrationDao.class, hibEx
	                    .getMessage(), hibEx);
	        } catch (Exception ex){
	        	logger.error("Exception:processPipelineBasedonEQRAndIBQuantity:"+ ex.getMessage());
	        	throw new DataAccessException(TechnicalRegistrationDao.class, ex.getMessage(), ex);
	        }
	    	logger.debug("Exiting TechnicalRegistrationDao.processPipelineBasedonEQRAndIBQuantity");
	    	return registrationIdMap;
	    }
	    
	    /**
	     * method to update pipeline SAP transactions SAP completed flag.
	     * 
	     * @param registrationId
	     * @param orderType
	     * @throws DataAccessException
	     */
	    public void updatePipeLineSAPTransactionsSAPCompletedFlag(String registrationId, String orderType) throws DataAccessException{
	    	logger.debug("Entering TechnicalRegistrationDao.updatePipeLineSAPTransactionsSAPCompletedFlag");
	    	Transaction transaction = null;
	    	String orderTypeClause = "";
	    	List<String> orderTypes = new ArrayList<String>();
	    	try {
	    		transaction = getSessionForGRT().beginTransaction();
	    		//GRT 4.0 : Defect #288 EQR status not updated even after success from siebel
	    		if(GRTConstants.TECH_ORDER_TYPE_IB.equalsIgnoreCase(orderType)){
	    			orderTypes.add(GRTConstants.TECH_ORDER_TYPE_IB);
	    		} else if(GRTConstants.TECH_ORDER_TYPE_FV.equalsIgnoreCase(orderType)){
	    			orderTypes.add(GRTConstants.TECH_ORDER_TYPE_FV);
	    			orderTypes.add(GRTConstants.TECH_ORDER_TYPE_EQR);
	    		}else{
	    			orderTypes.add(GRTConstants.TECH_ORDER_TYPE_EM);
	    		} 
	    		Query sqlQuery=getSessionForGRT().getNamedQuery("updatePipeLineSAPTransactionsSAPCompletedFlag");
	    		sqlQuery.setParameter("registrationId", registrationId);
	    		//GRT 4.0 : Defect #288 EQR status not updated even after success from siebel
	    		sqlQuery.setParameterList("orderTypeClause", orderTypes);
				logger.debug("Query : "+sqlQuery.toString());
				sqlQuery.executeUpdate();
				transaction.commit();
	        } catch (HibernateException hibEx) {
	        	logger.error("HibernateException:", hibEx);
	            throw new DataAccessException(TechnicalRegistrationDao.class, hibEx
	                    .getMessage(), hibEx);
	        }
	    	logger.debug("Exiting TechnicalRegistrationDao.updatePipeLineSAPTransactionsSAPCompletedFlag");
	    }
	    
	    
	    
	    /**
	     * method to get Technical Order list for euipment number list
	     * 
	     * @param regId
	     * @param orderType
	     * @param equipNumList
	     * @return
	     * @throws DataAccessException
	     */
	    @SuppressWarnings("unchecked")
		public List<TechnicalOrder> getTechnicalOrderListForEuipmentNumberList(String regId, String orderType, List<String> equipNumList) throws DataAccessException{
			logger.debug("Entering TechnicalRegistrationDao.getTechnicalOrderListForEuipmentNumberList");
	        List<TechnicalOrder> todList = new ArrayList<TechnicalOrder>();
	        try {
	        	if(equipNumList != null && equipNumList.size()> 0){
	        		
		            Query query = getSessionForGRT().getNamedQuery("getTechnicalOrderListForEuipmentNumberList");
		            query.setParameter("orderType", orderType);
		            query.setParameter("registrationId", regId);
		            logger.debug("TechnicalOrderListForEuipmentNumberList Query :"+query.toString());
		            todList = query.list();
	        	}
	        } catch (HibernateException hibEx) {
	            throw new DataAccessException(TechnicalRegistrationDao.class, hibEx
	                    .getMessage(), hibEx);
	        }
	        logger.debug("Exiting TechnicalRegistrationDao.getTechnicalOrderListForEuipmentNumberList");
	        return todList;
		}
	    
	    @SuppressWarnings("unchecked")
		public List<TechnicalOrder> getTechnicalOrderOnRegIdOrderTypeIsSALGateways(String registrationId, String orderType, boolean isSALGateways) throws DataAccessException {
			List<TechnicalOrder> technicalOrderList = null;

			logger.debug("Entering TechnicalRegistrationDao.getTechnicalOrderOnRegIdOrderTypeIsSALGateways");

			try {
				logger.debug("Technical Order Request ID : " + registrationId + " orderType: " + orderType + " isSALGateways: " + isSALGateways);
				String queryString = "Select technicalOrder from TechnicalOrder technicalOrder where technicalOrder.siteRegistration.registrationId=:registrationId";
				if (StringUtils.isNotEmpty(orderType)) {
					queryString += " and technicalOrder.orderType =:orderType";
					//GRT 4.0 Change : added condition for fetching records for equipment move
					if (GRTConstants.TECH_ORDER_TYPE_FV.equalsIgnoreCase(orderType)||GRTConstants.TECH_ORDER_TYPE_EM.equalsIgnoreCase(orderType)) {
						queryString += " and upper(technicalOrder.deleted) = 'YES'";
					}
				}
				if(isSALGateways){
					queryString += " and technicalOrder.salGateway = 1";
				} else {
					queryString += " and technicalOrder.salGateway = 0";
				}
				Query query = getSessionForGRT().createQuery(queryString);
				query.setString("registrationId", registrationId);
				if (StringUtils.isNotEmpty(orderType)) {
					query.setString("orderType", orderType);
				}
				technicalOrderList = query.list();
				if (technicalOrderList != null) {
					logger.debug("technicalOrderList size : " + technicalOrderList.size());
				}
			} catch (HibernateException hibEx) {
				throw new DataAccessException(TechnicalRegistrationDao.class, hibEx .getMessage(), hibEx);
			} finally {
				logger.debug("Exiting TechnicalRegistrationDao.getTechnicalOrderOnRegIdOrderTypeIsSALGateways");
			}
			return technicalOrderList;
		}
	    
	    
		/**
		 * method to get pipeline for processing
		 * 
		 * @param registrationId
		 * @param materialCodes
		 * @return
		 * @throws DataAccessException
		 */
		public List<String[]> getPipelineForProcessing(String registrationId, List<String> materialCodes) throws DataAccessException {
	        logger.debug("Entering TechnicalRegistrationDao.getPipelineForProcessing for Registration Id:" + registrationId);
	        List<String[]> pipelineTransactions = new ArrayList<String[]>();
	        try {
				Query query=getSessionForGRT().getNamedQuery("getPipelineForProcessing");
				query.setParameterList("mcInClause", materialCodes);
				query.setParameter("registrationId",registrationId);
				List<Object[]> queryResult = query.list();
	            for (int i = 0; i < queryResult.size(); i++) {
	                Object[] listItem = (Object[]) queryResult.get(i);
	                String [] data = {(String) listItem[0], (String) listItem[1]};
	                pipelineTransactions.add(data);
	            }
	        } catch (Throwable throwable){
	            logger.error("", throwable);
	        } finally {
	        	logger.debug("Entering getPipelineForProcessing for Registration Id:" + registrationId);
	        }
	        return pipelineTransactions;
	    }
	    
	    /**
	     * Process pipeline records based on MC+EQN+ORDERTYPE
	     * @param errorList
	     * @return
	     * @throws DataAccessException
	     */
	    @SuppressWarnings("unchecked")
		public Map<String,List<String>> updatePipeLineSAPTransactions(List<TechnicalOrderDetail> errorList) throws DataAccessException{
	    	logger.debug("Entering TechnicalRegistrationDao.updatePipeLineSAPTransactions");
	    	Map<String,List<String>> registrationIdMap = new HashMap<String, List<String>>();
	    	List<String> mcList = null;
	    	try {
	    		Criteria criteria = null;
	    		Session session = getSessionForGRT();
	            session.beginTransaction();

	            for(TechnicalOrderDetail eqrDto : errorList){
	                List<PipelineSapTransactions> pipelineSapTransactionsList;
	            	criteria =  session.createCriteria(PipelineSapTransactions.class);
	                criteria.add(Restrictions.eq("materialCode", eqrDto.getMaterialCode()));
	                criteria.add(Restrictions.eq("equipmentNumber", eqrDto.getSummaryEquipmentNumber()));
	                //GRT 4.0 change
	                criteria.add(Restrictions.eq("action", eqrDto.getActionType()));
	                pipelineSapTransactionsList = (List<PipelineSapTransactions>)  criteria.list();
	                for(PipelineSapTransactions pipelineSapTransactions : pipelineSapTransactionsList){
		                pipelineSapTransactions.setQuantity(0);
		                pipelineSapTransactions.setProcessed(true);
		                pipelineSapTransactions.setProcessedDate(new Date());
		                pipelineSapTransactions.setToEquipmentNumber(eqrDto.getToEquipmentNumber());
		                
		                session.saveOrUpdate(pipelineSapTransactions);
		                // RegistrationId already on MAP
		                if(registrationIdMap.containsKey(pipelineSapTransactions.getRegistrationId())){
		                	mcList = registrationIdMap.get(pipelineSapTransactions.getRegistrationId());
		                	// New MC addition to the list to specific RegistrationId in MAP
		                	if(mcList != null && !mcList.contains(pipelineSapTransactions.getMaterialCode())){
		                		mcList.add(pipelineSapTransactions.getMaterialCode());
		                		registrationIdMap.remove(pipelineSapTransactions.getRegistrationId());
			                	registrationIdMap.put(pipelineSapTransactions.getRegistrationId(), mcList);
		                	}
		                } else { // For new RegistrationId
		                	mcList = new ArrayList<String>();
		                	mcList.add(pipelineSapTransactions.getMaterialCode());
		                	registrationIdMap.put(pipelineSapTransactions.getRegistrationId(), mcList);
		                }
	                }
	            }
	            session.getTransaction().commit();
	        } catch (HibernateException hibEx) {
	        	logger.error("", hibEx);
	            throw new DataAccessException(TechnicalRegistrationDao.class, hibEx
	                    .getMessage(), hibEx);
	        }
	    	logger.debug("Exiting TechnicalRegistrationDao.updatePipeLineSAPTransactions");
	    	return registrationIdMap;
	    }
	    
	    
	    /**
		 * @param registrationId
		 * @param source
		 * @return
		 * @throws DataAccessException
		 */
		@SuppressWarnings("unchecked")
		public List<Object[]> queryResponseOnRegistrationId(String registrationId, String source) throws DataAccessException {
			logger.debug("Entering TechnicalRegistrationDao.querySAPResponseOnRegistrationId :" + registrationId);
			long time1 = System.currentTimeMillis();
			String queue = "";
			List<Object[]> list = null;
			try {
				if(GRTConstants.TECH_ORDER_TYPE_IB.equalsIgnoreCase(source)){
					queue = getIbaseQueue().trim();
				} else if(GRTConstants.TECH_ORDER_TYPE_EQR.equalsIgnoreCase(source)){
					queue =getEqrQueue().trim();
				} else if(GRTConstants.SIEBEL_ASSET_UPDATE.equalsIgnoreCase(source)){
					queue = getSiebelUpdateQueue().trim();
				}
				Query query =  getSessionForFMW().getNamedQuery("queryResponseOnRegistrationId");
				query.setString("registrationId", registrationId);
				query.setString("source", queue);
				list = query.list();
			} catch (HibernateException hibEx) {
				throw new DataAccessException(TechnicalRegistrationDao.class, hibEx.getMessage(), hibEx);
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new DataAccessException(TechnicalRegistrationDao.class, ex.getMessage(), ex);
			} finally {
				long time2 = System.currentTimeMillis();
				logger.debug("time taken in seconds:" + ((time2-time1)/1000) + " FMW DB for RegistrationId:" + registrationId);
				logger.debug("Exiting TechnicalRegistrationDao.querySAPResponseOnRegistrationId :" + registrationId);
			}
			return list;
		}
		
		 /**
		  * method to query SAP response on registration id.
		  * 
		  * @param registrationId
		  * @return
		  * @throws DataAccessException
		  */
		@SuppressWarnings("unchecked")
		public Object[] querySAPResponseOnRegistrationId(String registrationId, boolean ibFlag, boolean eqrFlag) throws DataAccessException {
			logger.debug("Entering TechnicalRegistrationDao.querySAPResponseOnRegistrationId :" + registrationId);
			long time1 = System.currentTimeMillis();
			String queue = "";
			List<Object[]> list = null;
			try {
				if(ibFlag){
					queue = getIbaseQueue().trim();
				} else if(eqrFlag){
					queue = getEqrQueue().trim();
				}
				Query query =  getSessionForFMW().getNamedQuery("querySAPResponseOnRegistrationId");
				query.setString("registrationId", registrationId);
				query.setString("source", queue);
				list = query.list();
			} catch (HibernateException hibEx) {
				throw new DataAccessException(TechnicalRegistrationDao.class, hibEx.getMessage(), hibEx);
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new DataAccessException(TechnicalRegistrationDao.class, ex.getMessage(), ex);
			} finally {
				long time2 = System.currentTimeMillis();
				logger.debug("time taken in seconds:" + ((time2-time1)/1000) + " FMW DB for RegistrationId:" + registrationId);
				logger.debug("Exiting querySAPResponseOnRegistrationId :" + registrationId);
			}
			return (list != null && !list.isEmpty())?list.get(0):null;
		}
		
		
	    
		 /**
		  * method to refresh object in GRT session
		  * @param obj
		  */
		public void refresh(Object obj) {
				logger.debug("Entering TechnicalRegistrationDao.refresh for:" + obj);
		    	try {
		    		if(obj != null) {
		    			getSessionForGRT().refresh(obj);
		    		}
		    	} catch(Throwable throwable) {
		    		logger.error("", throwable);
		    	} finally {
		    		logger.debug("Exiting TechnicalRegistrationDao.refresh for:" + obj);
		    	}
		    }
	    
		 /**
		 * method to save siebel Asset data List
		 * 
		 * @param assetUpdateList
		 * @return
		 * @throws DataAccessException
		 */
		public List<SiebelAssetData> saveSiebelAssetDataList(List<InstallBaseAssetData> assetDataList, String registrationId) throws DataAccessException {
	        logger.debug("Entering TechnicalRegistrationDao.saveSiebelAssetUpdateList()");
	        try {
	        	Session session = getSessionForGRT();
	            session.beginTransaction();
	            SiebelAssetData siebelAssetData = null;
	            List<SiebelAssetData> siebelAssetDataList = new ArrayList<SiebelAssetData>();
	            for(InstallBaseAssetData to : assetDataList){
	            	siebelAssetData = new SiebelAssetData();
	                
	            	siebelAssetData.setMaterialCode(to.getMaterialCode());
	            	siebelAssetData.setQuantity(new Long(to.getQuantity()));
	            	siebelAssetData.setRegistrationId(registrationId);
	            	siebelAssetData.setSerialNumber(to.getSerialNumber());
	            	siebelAssetData.setAssetNumber(to.getAssetNumber());

	                session.saveOrUpdate(siebelAssetData);
	                siebelAssetDataList.add(siebelAssetData);
	            }
	            session.getTransaction().commit();
	            return siebelAssetDataList;
	        } catch (HibernateException hibEx) {
	            logger.error("Error in TechnicalRegistrationDao.saveSiebelAssetUpdateList : " +  hibEx.getMessage());
	            throw new DataAccessException(TechnicalRegistrationDao.class, hibEx
	                    .getMessage(), hibEx);
	        }
	    }
		
		/**
		 * method to update Technical Registration EPN Survey Status
		 * 
		 * @param technicalRegistration
		 * @param status
		 * @return
		 * @throws DataAccessException
		 */
		public TechnicalRegistration updateTechnicalRegistrationEPNSurveyStatus(TechnicalRegistration technicalRegistration, StatusEnum status) throws DataAccessException {
	        logger.debug("Entering TechnicalRegistrationDao.updateTechnicalRegistrationEPNSurveyStatus()");
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
	            resultObject.setEpnSurveyStatus(s);
	            session.saveOrUpdate(resultObject);
	            session.getTransaction().commit();

	        } catch (HibernateException hibEx) {
	            throw new DataAccessException(TechnicalRegistrationDao.class, hibEx
	                    .getMessage(), hibEx);
	        }
	        logger.debug("Exiting TechnicalRegistrationDao.updateTechnicalRegistrationEPNSurveyStatus()");

	        return resultObject;
	    }
		
		public void undoStepBForSiteList(SiteList siteList, StatusEnum status) throws DataAccessException {
	    	logger.debug("Entering TechnicalRegistrationDao.undoStepBForSiteList()");
	    	Query sqlQuery = null;
	    	Transaction transaction = null;
	    	try {
		    	transaction = getSessionForGRT().beginTransaction();
		    	sqlQuery = getSessionForGRT().getNamedQuery("undoStepBForSiteList");
		        sqlQuery.setString("TECHREGID", siteList.getId());
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
				logger.debug("Exiting TechnicalRegistrationDao.undoStepBForSiteList()");
			}
	    }
		
		/**
		 * method to delete expanded Solution Elements
		 * 
		 * @param id
		 * @return
		 * @throws DataAccessException
		 */
		public int deleteExpandedSolutionElements(String id) throws DataAccessException{
			int flag = 1;
	        Query sqlQuery = null;
	        logger.debug("Entering TechnicalRegistrationDao.deleteExpandedSolutionElements");
	        logger.debug("The primary key of Site_List Record is::"+ id);
	        try {
	        	logger.debug("Before Delete Query");
	        	sqlQuery=getSessionForGRT().getNamedQuery("deleteExpandedSolutionElements");
	            sqlQuery.setString("siteListId", id);
	            deleteSQLQuery(sqlQuery);
	            logger.debug("After Delete Query");
	        } catch (HibernateException hibEx) {
	        	getSessionForGRT().getTransaction().rollback();
	            throw new DataAccessException(TechnicalRegistrationSRDao.class, hibEx
	                    .getMessage(), hibEx);
	        } catch (Exception ex) {
	        	getSessionForGRT().getTransaction().rollback();
	            throw new DataAccessException(TechnicalRegistrationSRDao.class, ex
	                    .getMessage(), ex);
	        }
	        logger.debug("Exiting TechnicalRegistrationDao.deleteExpandedSolutionElements");
			return flag;
		}
		 
		 
		 /**
		  * method to update Expanded Solution Elements.
		  * 
		  * @param expandedSolutionList
		  * @throws DataAccessException
		  */
		public void updateExpandedSolutionElements(List<ExpandedSolutionElement> expandedSolutionList) throws DataAccessException{
	        logger.debug("Entering TechnicalRegistrationDao.updateExpandedSolutionElements");
	        try {
	        	logger.debug("Before Update Query");
	    		Session session = getSessionForGRT();
	            session.beginTransaction();
	        	if(!expandedSolutionList.isEmpty()){
	    	    	for(ExpandedSolutionElement expandedSolutionElement : expandedSolutionList){
	    	    		expandedSolutionElement.setSelectForAlarming(false);
	    	    		expandedSolutionElement.setSelectForRemoteAccess(false);
	    	    		session.saveOrUpdate(expandedSolutionElement);
	    	    	}
	    	    	session.getTransaction().commit();
	        	}
	            logger.debug("After Delete Query");
	        } catch (HibernateException hibEx) {
	        	getSessionForGRT().getTransaction().rollback();
	            throw new DataAccessException(TechnicalRegistrationDao.class, hibEx
	                    .getMessage(), hibEx);
	        } catch (Exception ex) {
	        	getSessionForGRT().getTransaction().rollback();
	            throw new DataAccessException(TechnicalRegistrationDao.class, ex
	                    .getMessage(), ex);
	        }
	        logger.debug("Exiting TechnicalRegistrationDao.updateExpandedSolutionElements");
		 }
		 
		 public void undoStepBForTechnicalRegistration(TechnicalRegistration technicalRegistration, StatusEnum status) throws DataAccessException {
	    	logger.debug("Entering TechnicalRegistrationDao.undoStepBForTechnicalRegistration()");
	    	Query sqlQuery = null;
	    	Transaction transaction = null;
	    	try {
		    	transaction = getSessionForGRT().beginTransaction();
		    	sqlQuery = getSessionForGRT().getNamedQuery("undoStepBForTechnicalRegistration");
		        sqlQuery.setString("TECHREGID", technicalRegistration.getTechnicalRegistrationId());
		        sqlQuery.executeUpdate();
		        transaction.commit();
		    } catch (HibernateException hibEx) {
				logger.error("", hibEx);
				transaction.rollback();
				throw new DataAccessException(TechnicalRegistrationSRDao.class, hibEx.getMessage(), hibEx);
			} catch (Throwable throwable) {
				logger.error("", throwable);
				transaction.rollback();
				throw new DataAccessException(TechnicalRegistrationSRDao.class, throwable.getMessage(), throwable);
			} finally {
				logger.debug("Exiting TechnicalRegistrationDao.undoStepBForTechnicalRegistration()");
			}
	    }
		
		public static void main(String[] args) throws DataAccessException {
	    	ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");		
	    	TechnicalRegistrationDao technicalRegistrationDao = (TechnicalRegistrationDao) context.getBean("technicalRegistrationDao");
	    	
	    	String registrationId = "6949414";
	    	List<String> materialCodes = new ArrayList<String>();
			materialCodes.add("270393");
			List<String[]> list = technicalRegistrationDao.getPipelineForProcessing(registrationId, materialCodes);
		}
		
		/**
	     * Method to persist the Account Update Log.
	     * @param logAccountUpdate
	     * @throws DataAccessException
	     */
	    public void saveAccountUpdateLog(LogAccountUpdate logAccountUpdate) throws DataAccessException {
	        logger.debug("Entering RegistrationDao : saveAccountUpdateLog()");
	        try {
	        	Session session = getSessionForGRT();
	            session.beginTransaction();
	            session.saveOrUpdate(logAccountUpdate);
	            session.getTransaction().commit();
	        } catch (HibernateException hibEx) {
	        	logger.error("HibernateException in saveAccountUpdateLog: ", hibEx);
	        } catch (Throwable throwable) {
				logger.error("Throwable in saveAccountUpdateLog: ", throwable);
			} finally {
				logger.debug("Exiting RegistrationDao : saveAccountUpdateLog()");
			}
	    }
	    
	    /**
	     * Method to get the FUZZY_SEARCH_PARAM values.
	     *
	     * @return FUZZYSEARCHPARAM ArrayList
	     */
	    public List<FuzzySearchParam> getFuzzySearchParams() throws DataAccessException {
	        logger.debug("Entering RegistrationDao : getFuzzySearchParams");
	        List<FuzzySearchParam> fuzzySearchParamList = null;
	        try {
	            Criteria criteria = getSessionForGRT().createCriteria(FuzzySearchParam.class);
	            fuzzySearchParamList = criteria.list();
	            logger.debug("fuzzySearchParamList.size()="+fuzzySearchParamList.size());
	        } catch (HibernateException hibEx) {
	            throw new DataAccessException(TechnicalRegistrationDao.class, hibEx
	                    .getMessage(), hibEx);
	        } catch (Exception ex) {
	            throw new DataAccessException(TechnicalRegistrationDao.class, ex
	                    .getMessage(), ex);
	        }
	        logger.debug("Exiting RegistrationDao : getFuzzySearchParams");

	        return fuzzySearchParamList;
	    }
	    
	    /**
	     * An API to grant a temporary acccess to BP for a passed account (soldTo & shipTo id).
	     *
	     * @param bpAccountTempAccess
	     * @return
	     * @throws DataAccessException
	     */
	    public BPAccountTempAccess saveBPAccountTempAccess(BPAccountTempAccess bpAccountTempAccess) throws DataAccessException {
	        logger.debug("Entering saveBPAccountTempAccess");
	        try {
	            Session session = getSessionForGRT();
	            session.beginTransaction();

	            session.saveOrUpdate(bpAccountTempAccess);
	            session.getTransaction().commit();
	            return bpAccountTempAccess;

	        } catch (HibernateException hibEx) {
	            throw new DataAccessException(TechnicalRegistrationDao.class, hibEx.getMessage(), hibEx);
	        } finally {
	        	logger.debug("Exiting saveBPAccountTempAccess");
	        }
	    }
	    
	    /**
	     * Method to persist Account Creation Information- ShipTo/SoldTo
	     * @param accountCreation
	     * @throws DataAccessException
	     */
	    public void saveAccountCreation(AccountCreation accountCreation) throws DataAccessException {
	        logger.debug("Entering RegistrationDao : saveAccountCreation()");
	        try {
	        	Session session = getSessionForGRT();
	            session.beginTransaction();
	            session.saveOrUpdate(accountCreation);
	            session.getTransaction().commit();
	        } catch (HibernateException hibEx) {
	        	logger.error("HibernateException in saveAccountCreation: ", hibEx);
	        } catch (Throwable throwable) {
				logger.error("Throwable in saveAccountCreation: ", throwable);
			}
	        logger.debug("Exiting RegistrationDao : saveAccountCreation()");
	    }
	    
	    public String getSalesOrg(String countryISOCode) throws DataAccessException{
	    	logger.debug("Entering getSalesOrg for countryISOCode:" + countryISOCode);
	    	String salesOrg = GRTConstants.AISL_SALES_ORG;
	    	try {
	    		if(StringUtils.isNotEmpty(countryISOCode)) {
			    	String sqlQuery = "SELECT SALES_ORG_TEXT FROM SALES_ORG WHERE COUNTRY_CODE = :COUNTRY_ISO_CODE";
			    	Query query = getSessionForGRT().createSQLQuery(sqlQuery);
			    	query.setString("COUNTRY_ISO_CODE", countryISOCode);
		        	List queryResult = query.list();
		            for (int i = 0; i < queryResult.size(); i++) {
		            	if(queryResult.get(i) instanceof String) {
		            		salesOrg = (String)queryResult.get(i);
		            	} else {
		            		if(queryResult.get(i) != null) {
		            			logger.debug("queryResult.get(i) not a String, but" + queryResult.get(i).getClass());
		            		}
		            	}
		            }
	    		}
		    } catch (HibernateException hibEx) {
				logger.error("", hibEx);
				throw new DataAccessException(TechnicalRegistrationDao.class, hibEx.getMessage(), hibEx);
			} catch (Throwable throwable) {
				logger.error("", throwable);
				throw new DataAccessException(TechnicalRegistrationDao.class, throwable.getMessage(), throwable);
			} finally {
				logger.debug("Exiting getSalesOrg for countryISOCode:" + countryISOCode);
			}
			return salesOrg;
	    }
	    
	    /**
	     * method to fetch regions.
	     * @return
	     * @throws DataAccessException
	     */
	    public Map<String, String> fetchRegions(String specification) throws DataAccessException{
	    	logger.debug("Entering fetchRegions for specification:" + specification);
	    	Map<String, String> regionList = new TreeMap<String, String>();;
	    	Object[] listItem = null;
	    	try {
	    		if(StringUtils.isNotEmpty(specification)) {
	    			String sqlQuery = "SELECT REGION_COUNTRY_CODE, REGION_COUNTRY FROM MAIL_CONFIGS WHERE TYPE = :SPECIFICATION ";
	    			Query query = getSessionForGRT().createSQLQuery(sqlQuery);
	    			if(GRTConstants.OEFC_TOKEN_REDEMPTION.equalsIgnoreCase(specification)){
	    				query.setString("SPECIFICATION", specification);
	    			}
	    			List queryResult = query.list();
	    			if(queryResult != null && queryResult.size() > 0){
	    				logger.debug("No of rows returned:"+queryResult.size());
	    				for (int i = 0; i < queryResult.size(); i++) {
	    					listItem = (Object[]) queryResult.get(i);
	    					regionList.put((listItem[0]!=null?(String)listItem[0]:""), (listItem[1]!=null?(String)listItem[1]:""));
	    				}
	    			}
	    		}
	    	} catch (HibernateException hibEx) {
	    		logger.error("", hibEx);
	    		throw new DataAccessException(TechnicalRegistrationDao.class, hibEx.getMessage(), hibEx);
	    	} catch (Throwable throwable) {
	    		logger.error("", throwable);
	    		throw new DataAccessException(TechnicalRegistrationDao.class, throwable.getMessage(), throwable);
	    	} finally {
	    		logger.debug("Exiting fetchRegions for specification:" + specification);
	    	}
	    	return regionList;
	    }

	    /**
	     * method to get medal Level
	     * 
	     * @param soldToId
	     * @return
	     * @throws DataAccessException
	     */
	    public MedalLevelEnum getMedalLevel(String soldToId) throws DataAccessException{
	    	logger.debug("Entering getMedalLevel for soldToId:" + soldToId);
	    	MedalLevelEnum medalLevel = null;
	    	try {
	    		if(StringUtils.isNotEmpty(soldToId)) {
	    			String sqlQuery = "SELECT MEDAL_LEVEL FROM BP_MEDAL_LEVEL WHERE SOLD_TO_ID = :SOLD_TO_ID";
	    			Query query = getSessionForGRT().createSQLQuery(sqlQuery);
	    			query.setString("SOLD_TO_ID", soldToId);
	    			List queryResult = query.list();

	    			logger.debug("No of result found : " +queryResult.size());
	    			for (int i = 0; i < queryResult.size(); i++) {
	    				logger.debug("Result : " + queryResult.get(i) + queryResult.get(i).getClass().getName());
	    				if(queryResult.get(i) instanceof String) {
	    					medalLevel = MedalLevelEnum.getMedalLevelById((String)queryResult.get(i));
	    				}
	    			}
	    		}
	    	} catch (HibernateException hibEx) {
	    		logger.error("", hibEx);
	    		throw new DataAccessException(TechnicalRegistrationDao.class, hibEx.getMessage(), hibEx);
	    	} catch (Throwable throwable) {
	    		logger.error("", throwable);
	    		throw new DataAccessException(TechnicalRegistrationDao.class, throwable.getMessage(), throwable);
	    	} finally {
	    		logger.debug("Exiting getMedalLevel:" + medalLevel + " for soldToId:" + soldToId);
	    	}
	    	return medalLevel;
	    }
	    
	    /**
	     * Method to persist the Token Redemption Information
	     * @param tokenRedemption
	     * @throws DataAccessException
	     */
	    public void saveTokenRedemption(TokenRedemption tokenRedemption) throws DataAccessException {
	        logger.debug("Entering RegistrationDao : saveTokenRedemption()");
	        try {
	        	Session session = getSessionForGRT();
	            session.beginTransaction();
	            session.saveOrUpdate(tokenRedemption);
	            session.getTransaction().commit();
	        } catch (HibernateException hibEx) {
	        	logger.error("HibernateException in saveTokenRedemption: ", hibEx);
	        } catch (Throwable throwable) {
				logger.error("Throwable in saveTokenRedemption: ", throwable);
			} finally {
				logger.debug("Exiting RegistrationDao : saveTokenRedemption()");
			}
	    }
	    
	    /**
	     * method to get redemption prerequisite SE Codes.
	     * 
	     * @return List
	     * @throws DataAccessException
	     */
	    public List<String> getRedemptionPrerequisiteSeCodes() throws DataAccessException {
			logger.debug("Entering RegistrationDao : getRedemptionPrerequisiteSeCodes");
			List<String> releaseList = null;
			try {
				Query query = getSessionForGRT().createQuery("Select distinct productRelease.seCode from ProductRelease as productRelease"
								+ " where productRelease.redemptionPrerequisiteSECode = 1");
				releaseList = query.list();
			} catch (HibernateException hibEx) {
				throw new DataAccessException(TechnicalRegistrationDao.class, hibEx.getMessage(), hibEx);
			} catch (Exception ex) {
				throw new DataAccessException(TechnicalRegistrationDao.class, ex.getMessage(), ex);
			}
			logger.debug("Entering RegistrationDao : getRedemptionPrerequisiteSeCodes");
			return releaseList;
		}
	    
	    /**
	     * method to fetch OEFC Mail Ids.
	     * 
	     * @param countryCode
	     * @return
	     * @throws DataAccessException
	     */
	    public String fetchOEFCMailIds(String countryCode) throws DataAccessException{
	    	logger.debug("Entering getOEFCMailIds for countryCode:" + countryCode);
	    	String mailIdsOEFC = "";
	    	Object[] listItem = null;
	    	try {
	    		if(StringUtils.isNotEmpty(countryCode)) {
			    	String sqlQuery = "SELECT REGION_COUNTRY_CODE, MAIL_IDS FROM MAIL_CONFIGS WHERE TYPE = 'OEFC_TOKEN_REDEMPTION' AND REGION_COUNTRY_CODE = :COUNTRY_CODE ";
			    	Query query = getSessionForGRT().createSQLQuery(sqlQuery);
			    	query.setString("COUNTRY_CODE", countryCode);
		        	List queryResult = query.list();
		        	if(queryResult != null && queryResult.size() > 0){
		        		logger.debug("No of rows returned:"+queryResult.size());
		        		listItem = (Object[]) queryResult.get(0);
			        	if(listItem != null) {
			        		mailIdsOEFC =  listItem[1]!=null?listItem[1].toString():"";
			        	}
		        	}
	    		}
		    } catch (HibernateException hibEx) {
				logger.error("", hibEx);
				throw new DataAccessException(TechnicalRegistrationDao.class, hibEx.getMessage(), hibEx);
			} catch (Throwable throwable) {
				logger.error("", throwable);
				throw new DataAccessException(TechnicalRegistrationDao.class, throwable.getMessage(), throwable);
			} finally {
				logger.debug("Exiting getOEFCMailIds for countryCode:" + countryCode);
			}
			return mailIdsOEFC;
	    }

}
