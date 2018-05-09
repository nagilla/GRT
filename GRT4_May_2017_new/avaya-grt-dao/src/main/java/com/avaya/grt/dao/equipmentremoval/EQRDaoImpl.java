package com.avaya.grt.dao.equipmentremoval;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.avaya.grt.dao.TechnicalRegistrationErrorDao;
import com.avaya.grt.mappers.PipelineSapTransactions;
import com.avaya.grt.mappers.SoldToSAPMapping;
import com.avaya.grt.mappers.TechnicalOrder;
import com.grt.util.DataAccessException;
import com.grt.util.GRTConstants;
import com.grt.util.ProcessStepEnum;



public class EQRDaoImpl extends TechnicalRegistrationErrorDao implements EQRDao{
	private static final Logger logger = Logger.getLogger(EQRDaoImpl.class);
	
	 /**
     * Delete technical orders by reg id
     *
     * @param regId string
     * @return processStep ProcessStepEnum
     * @throws DataAccessException Exception
     */
   
	public int deleteTechnicalOrderForRegId(String regId, ProcessStepEnum processStep) throws DataAccessException{
		int flag = 1;
        Query sqlQuery = null;
        String query = "";
        logger.debug("Entering EQRDaoImpl : deleteTechncalOrderList");
        try {
        	if (processStep !=null) {
        		if (processStep.getProcessStepId().equals(ProcessStepEnum.INSTALL_BASE_CREATION.getProcessStepId())) {
        			 sqlQuery = getSessionForGRT().getNamedQuery("deleteTechnicalOrderForRegIdIB");
        		} else if (processStep.getProcessStepId().equals(ProcessStepEnum.FINAL_RECORD_VALIDATION.getProcessStepId())){
        			 sqlQuery = getSessionForGRT().getNamedQuery("deleteTechnicalOrderForRegIdEQR");
        		} else if( processStep.getProcessStepId().equals(ProcessStepEnum.EQUIPMENT_MOVE.getProcessStepId()) ){
        			sqlQuery = getSessionForGRT().getNamedQuery("deleteTechnicalOrderForRegIdEQM");
        		}
            }
            sqlQuery.setString("registrationId", regId);
            deleteSQLQuery(sqlQuery);
        } catch (HibernateException hibEx) {
            throw new DataAccessException(EQRDaoImpl.class, hibEx
                    .getMessage(), hibEx);
        } catch (Exception ex) {
            throw new DataAccessException(EQRDaoImpl.class, ex
                    .getMessage(), ex);
        }
		return flag;
	}
	
	 /**
     * Get SAP Box based on Sold To
     *
     * @param materialCode string
     * @return materialCodeDesc string
     * @throws DataAccessException Exception
     */
   
    public String getSapBox(String soldTo) throws DataAccessException {
    	logger.debug("Entering EQRDaoImpl : getSapBox() ");
    	String sapBox = null;
    	SoldToSAPMapping soldToSapMapping = null;
         try {
        	 Criteria criteria =  getSessionForGRT().createCriteria(SoldToSAPMapping.class);
        	 criteria.add(Restrictions.eq("soldTo", soldTo));
             soldToSapMapping =(SoldToSAPMapping) criteria.uniqueResult();
             if(soldToSapMapping != null){
             sapBox = soldToSapMapping.getSapBox();
             }
         }catch (HibernateException hibEx) {
            throw new DataAccessException(EQRDaoImpl.class, hibEx
                    .getMessage(), hibEx);
        }
		if(sapBox == null){
             if(soldTo.startsWith(GRTConstants.BBoxSoldPrefix)){
                   return GRTConstants.BBoxDestination;
             }else{
                   return GRTConstants.IBoxDestination;
             }
         }

        logger.debug("Exiting EQRDaoImpl : getSapBox() ");
        return sapBox;
    }
    
    /**
     * Method to get the Technical Order for the given registration id and order type.
     *
     * @param registrationId String
     * @param orderType String
     * @return technicalOrderList List
     * @throws DataAccessException Exception
     */
    public List<TechnicalOrder> getTechnicalOrderByType(String registrationId, String orderType, boolean allTOs)
            throws DataAccessException {
        List<TechnicalOrder> technicalOrderList = null;

        logger.debug("Entering EQRDao : getTechnicalOrderByType");

        try {
            logger.debug("Technical Order Request ID : " + registrationId);
            Query query=getSessionForGRT().getNamedQuery("fetchTechnicalOrderByType");
            query.setString("registrationId", registrationId);
            if(StringUtils.isNotEmpty(orderType)) {
            	query.setString("orderType", orderType);
            }
            technicalOrderList = query.list();
            if (technicalOrderList != null) {
                logger.debug("technicalOrderList size : "
                        + technicalOrderList.size());
            }
        } catch (HibernateException hibEx) {
            throw new DataAccessException(EQRDao.class, hibEx
                    .getMessage(), hibEx);
        }

        logger.debug("Exiting EQRDao : getTechnicalOrderByType");

        return technicalOrderList;
    }
   
    /**
     * Fetch Consolidated EQR and IB records from SAP Pipeline table
     *
     * @param soldTo String
     * @throws DataAccessException Exception
     */
    public List<PipelineSapTransactions> getConsolidatedPipelineRecords(String soldTo) throws DataAccessException{
    	logger.debug("Starting updatePipelineTransactionOnRegistrationOverride");
    	List<PipelineSapTransactions> pipelineList = new ArrayList<PipelineSapTransactions>();
    	PipelineSapTransactions pipelineSapTransactions = null;
    	try {
    		Query query = getSessionForGRT().getNamedQuery("getConsolidatedPipelineRecords");
	    	query.setString("soldToId", soldTo);
        	List queryResult = query.list();
            for (int i = 0; i < queryResult.size(); i++) {
                Object[] listItem = (Object[]) queryResult.get(i);
                pipelineSapTransactions = new PipelineSapTransactions();
                pipelineSapTransactions.setMaterialCode(listItem[0]!=null?listItem[0].toString():"");
                pipelineSapTransactions.setAction(listItem[1]!=null?listItem[1].toString():"");
                pipelineSapTransactions.setQuantity(listItem[2]!=null?Long.parseLong(listItem[2].toString()):0L);
                pipelineList.add(pipelineSapTransactions);
            }
	    } catch (HibernateException hibEx) {
			logger.error("", hibEx);
			throw new DataAccessException(EQRDao.class, hibEx.getMessage(), hibEx);
		} catch (Throwable throwable) {
			logger.error("", throwable);
			throw new DataAccessException(EQRDao.class, throwable.getMessage(), throwable);
		} finally {
			logger.debug("Ending updatePipelineTransactionOnRegistrationOverride");
		}
		return pipelineList;
    }


}
