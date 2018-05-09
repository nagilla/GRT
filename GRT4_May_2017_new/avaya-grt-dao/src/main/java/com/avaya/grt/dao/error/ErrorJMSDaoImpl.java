package com.avaya.grt.dao.error;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.avaya.grt.dao.TechnicalRegistrationErrorDao;
import com.grt.util.GRTConstants;



public class ErrorJMSDaoImpl extends TechnicalRegistrationErrorDao implements ErrorJMSDao{
	private static final Logger logger = Logger.getLogger(ErrorJMSDaoImpl.class);
	
	/*public static void main(String[] args) throws Exception {		
		ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
		ErrorJMSDao errorJMSDao = (ErrorJMSDao) context.getBean("errorJMSDao");
		
		errorJMSDao.updateSiteRegistrationSubmittedFlag("270962", GRTConstants.isSubmitted_false);
		
	}*/
}
