package com.avaya.grt.service.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.avaya.grt.dao.graph.RegistrationGraphDao;
import com.grt.dto.RegistrationCount;
import com.grt.util.DataAccessException;

public class RegistrationGraphServiceImpl implements RegistrationGraphService {
	private static final Logger logger = Logger.getLogger(RegistrationGraphServiceImpl.class);
	public RegistrationGraphDao registrationGraphDao;
	
	public RegistrationGraphDao getRegistrationGraphDao() {
		return registrationGraphDao;
	}

	public void setRegistrationGraphDao(RegistrationGraphDao registrationGraphDao) {
		this.registrationGraphDao = registrationGraphDao;
	}

	
	public List<RegistrationCount> getRegistrationsCompleted(String userId) throws DataAccessException
	{
		logger.debug("Entering RegistrationGraphServiceImpl : getRegistrationsCompleted");
		Iterator registrationCompletedList= getRegistrationGraphDao().getRegistrationsCompleted(userId);
		RegistrationCount registrationCount = null;
		List<RegistrationCount>  regCountList=new ArrayList<RegistrationCount>();
	    while(registrationCompletedList.hasNext()) {
	    	   
	    	   Object[] regList = (Object[]) registrationCompletedList.next();
	    	   registrationCount = new RegistrationCount();
	    	   registrationCount.setCreatedDate(regList[0].toString());
	    	   registrationCount.setIbCompleted(regList[1].toString());
	    	   registrationCount.setEqrCompleted(regList[2].toString());
	    	   registrationCount.setTobCompleted(regList[3].toString());
	    	   logger.debug("CreatedDate"+registrationCount.getCreatedDate());
	    	   logger.debug("InstallBaseStatusId"+registrationCount.getIbCompleted());
	    	   logger.debug("FinalValidationStatusId"+registrationCount.getEqrCompleted());	    	   
	    	   logger.debug("TechRegStatusId"+registrationCount.getTobCompleted());
	    	   regCountList.add(registrationCount);
	    }
	    logger.debug("Exiting RegistrationGraphServiceImpl : getRegistrationsCompleted");

	    return regCountList;
	}
	
	public List<RegistrationCount> getRegistrationsSaved(String userId) throws DataAccessException {
		logger.debug("Entering RegistrationGraphServiceImpl : getRegistrationsSaved");
		Iterator registrationSavedList= getRegistrationGraphDao().getRegistrationsSaved(userId);
		RegistrationCount registrationCount = null;
		List<RegistrationCount>  regCountList=new ArrayList<RegistrationCount>();
	    while(registrationSavedList.hasNext()) {
	    	   
	    	   Object[] regList = (Object[]) registrationSavedList.next();
	    	   registrationCount = new RegistrationCount();
	    	  
	    	   registrationCount.setCreatedDate(regList[0].toString());
	    	   registrationCount.setIbSaved(regList[1].toString());
	    	   registrationCount.setEqrSaved(regList[2].toString());
	    	   registrationCount.setTobSaved(regList[3].toString());
	    	   
	    	   logger.debug("CreatedDate"+registrationCount.getCreatedDate());
	    	   logger.debug("InstallBaseStatusId"+registrationCount.getIbSaved());
	    	   logger.debug("FinalValidationStatusId"+registrationCount.getEqrSaved());   	
	    	   logger.debug("TechRegStatusId"+registrationCount.getTobSaved());
	    	   regCountList.add(registrationCount);
	       }
	    logger.debug("Exiting RegistrationGraphServiceImpl : getRegistrationsSaved");

	    return regCountList;
	}
	
	
	public List<RegistrationCount> getRegistrationsNotCompleted(String userId) throws DataAccessException {
		logger.debug("Entering RegistrationGraphServiceImpl : getRegistrationsNotCompleted");	
		
		Iterator registrationList= getRegistrationGraphDao().getRegistrationsNotCompleted(userId);
		RegistrationCount registrationCount = null;
		List<RegistrationCount> regCountList=new ArrayList<RegistrationCount>();
	    while(registrationList.hasNext()) {
	    	   
	    	   Object[] regList = (Object[]) registrationList.next();
	    	   registrationCount = new RegistrationCount();
	    	   
	    	   registrationCount.setCreatedDate(regList[0].toString());
	    	   registrationCount.setTobReSubmit(regList[1].toString());
	    	   registrationCount.setTobInitiate(regList[2].toString());
	    	  
	    	   logger.debug("CreatedDate"+registrationCount.getCreatedDate());
	    	   logger.debug("Resubmitted"+registrationCount.getTobReSubmit());
	    	   logger.debug("Notintiiated"+registrationCount.getTobInitiate());
	    	   
	    	   regCountList.add(registrationCount);
	    }
	    logger.debug("Exiting RegistrationGraphServiceImpl : getRegistrationsNotCompleted");
	    return regCountList;
		
	}
	
	
	
	public static void main(String[] args) throws Exception {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");		
		RegistrationGraphService registrationGraphService = (RegistrationGraphService) context.getBean("registrationGraphService");
		String userId="cxpemp1";
		List<RegistrationCount> list=registrationGraphService.getRegistrationsCompleted(userId);
		//List<RegistrationCount> list=registrationGraphService.getRegistrationsSaved(userId);
		//List<RegistrationCount> list=registrationGraphService.getRegistrationsNotResubmitted(userId);
		//List<RegistrationCount> list=registrationGraphService.getRegistrationsNotCompleted(userId);
		//List<RegistrationCount> list=registrationGraphService.getRegistrationsCompleted(userId);
	    	
	}

	@Override
	public List<RegistrationCount> getRegistrationsCompletedList(String userId)
			throws DataAccessException {
logger.debug("Entering RegistrationGraphServiceImpl : getRegistrationsNotCompleted");	
		
		Iterator registrationList= getRegistrationGraphDao().getRegistrationsCompletedList(userId);
		RegistrationCount registrationCount = null;
		List<RegistrationCount> regCountList=new ArrayList<RegistrationCount>();
	    while(registrationList.hasNext()) {
	    	   
	    	   Object[] regList = (Object[]) registrationList.next();
	    	   registrationCount = new RegistrationCount();
	    	   
	    	   registrationCount.setNotInitiated(regList[0] != null?regList[0].toString():"0");
	    	   registrationCount.setInProcess(regList[1] != null?regList[1].toString():"0");
	    	   registrationCount.setAwaitingInfo(regList[2] != null?regList[2].toString():"0");
	    	   registrationCount.setSaved(regList[2] != null?regList[3].toString():"0");
	    	  
	    	   logger.info("NotInitiated"+registrationCount.getNotInitiated());
	    	   logger.info("InProcess"+registrationCount.getInProcess());
	    	   logger.info("AwaitingInfo"+registrationCount.getAwaitingInfo());
	    	   
	    	   regCountList.add(registrationCount);
	    }
	    logger.debug("Exiting RegistrationGraphServiceImpl : getRegistrationsNotCompleted");
	    return regCountList;
		
	}

}
