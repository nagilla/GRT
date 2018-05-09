package com.avaya.grt.dao.techregistration;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.avaya.grt.dao.TechnicalRegistrationAsyncDao;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.grt.util.StatusEnum;


public class TechRegJMSDaoImpl extends TechnicalRegistrationAsyncDao implements TechRegJMSDao{

	private static final Logger logger = Logger.getLogger(TechRegJMSDaoImpl.class);
	
	/*public static void main(String[] args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");		
		TechRegJMSDao techRegJMSDao = (TechRegJMSDao) context.getBean("techRegJMSDao");
		
	//	TechnicalRegistration techreg=techRegJMSDao.updateTechnicalRegistrationEPNSurveyStatus(tr, StatusEnum.INPROCESS);
	}*/

}
