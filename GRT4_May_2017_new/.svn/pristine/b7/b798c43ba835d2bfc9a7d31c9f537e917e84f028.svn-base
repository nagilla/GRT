package com.avaya.grt.dao.installbase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.avaya.grt.dao.TechnicalRegistrationDao;
import com.avaya.grt.mappers.ArtProductType;
import com.avaya.grt.mappers.SiteList;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.DataAccessException;

public class InstallBaseJMSDaoImpl extends TechnicalRegistrationDao implements InstallBaseJMSDao{
	private static final Logger logger = Logger.getLogger(InstallBaseJMSDaoImpl.class);		
	
	 public static void main(String[] args) throws DataAccessException {
	    	ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");		
	    	InstallBaseJMSDao installBaseJMSDao = (InstallBaseJMSDao) context.getBean("installBaseJMSDao");
	    	
		    	/*String phoneNumber=installBaseJMSDao.getPhoneNoByCountry("Australia");
		    	System.out.println("Phone Number is :"+phoneNumber);*/
	    	
	    	/*String technicalRegistrationId="1230715";
	    	TechnicalRegistration technicalRegistration =installBaseJMSDao.getTechnicalRegistration(technicalRegistrationId);
	    	System.out.println("Technical Registration Sold To:" +technicalRegistration.getSoldToId());*/
	    	
	    	/*TechnicalRegistration technicalRegistration=new TechnicalRegistration();
	    	technicalRegistration.setTechnicalRegistrationId("5690741");
			technicalRegistration.setArtSrNo(null);
          	technicalRegistration.setErrorCode("");
          	technicalRegistration.setSubErrorCode("");
          	technicalRegistration.setErrorDesc("");
          	technicalRegistration.setFailedSeid("");
          	technicalRegistration.setArtId("");
           	technicalRegistration.setGroupId("salgw_SALGW_227272");
            technicalRegistration.setSolutionElementId("");
            technicalRegistration.setNumberOfSubmit("0");
            technicalRegistration.setOnboarding("");
            technicalRegistration.setAlarmId("808650");
            technicalRegistration.setProductId("1-2IL-113");
            technicalRegistration.setInstallScript(null	);
            technicalRegistration.setSid("");
            technicalRegistration.setMid("");
            technicalRegistration.setTransactionDetails("");
            technicalRegistration.setArtId("1117045504");
            technicalRegistration.setSummaryEquipmentNumber("");
            technicalRegistration.setEquipmentNumber("");
            technicalRegistration.setSrRequest(null);
           	technicalRegistration.setArtCreatedSrNo("");
           	technicalRegistration.setIpoUserEmail("dvadakkath@avaya.com");
           	technicalRegistration.setStepACompletedDate(new Date("21-Jan-2015"));
           	technicalRegistration= installBaseJMSDao.saveTechnicalRegistration(technicalRegistration);
           	System.out.println("Saved Id"+technicalRegistration.getTechnicalRegistrationId());*/
            	

	    	/*SiteList site=installBaseJMSDao.getSiteList("421144");
	    	System.out.println("Sold To is :"+site.getSoldToId());*/
	    	
	    	/*String productTypeCode="DEFTY";
	    	ArtProductType art=installBaseJMSDao.getProductTypeByTypeCode(productTypeCode);
	    	System.out.println("Art Registration Type is "+art.getRegistrationType());
	    	*/
	    	
	    	String registrationId ="5715491";
			List<String> materialCodes = new ArrayList<String>();
			materialCodes.add("201393");
			/*materialCodes.add("170472");
			materialCodes.add("175236");
			materialCodes.add("270393");
			materialCodes.add("700469273");*/
			 List<String[]> resultList=installBaseJMSDao.getPipelineForProcessing(registrationId, materialCodes );
			System.out.println("resultList:::"+resultList.size());
	    }
	 
}
