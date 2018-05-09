/**
 * 
 */
package com.avaya.grt.service.administrator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.avaya.grt.dao.administrator.AdministratorDao;
import com.avaya.grt.mappers.Alert;
import com.avaya.grt.service.BaseRegistrationService;
import com.grt.util.DataAccessException;

/**
 * @author deepa_vadakkath
 *
 */
public class AdministratorServiceImpl extends BaseRegistrationService implements AdministratorService{

	/**
	 * @param args
	 */
	
	private static final Logger logger = Logger.getLogger(AdministratorServiceImpl.class);
	public AdministratorDao administratorDao;
	
	
	public AdministratorDao getAdministratorDao() {
		return administratorDao;
	}


	public void setAdministratorDao(AdministratorDao administratorDao) {
		this.administratorDao = administratorDao;
	}


	/**
     * Method to get Admin Message List.
     *
     * @throws DataAccessException exception
     * @return getAlertMessagesList List
     */
    public List<Alert> getAdminAlerts() throws DataAccessException {
        return getAdministratorDao().getAdminAlerts();
    }
	
    /**
     * Method to save or update Alert details.
     *
     * @param alert Alert
     * @throws DataAccessException exception
     * @return getAlertMessagesList List
     */
    public int saveOrUpdateAlertDetails(Alert alert) throws DataAccessException {
        return getAdministratorDao().saveOrUpdateAlertDetails(alert);
    }

    /**
     * Method to delete Alert.
     *
     * @param alert Alert
     * @throws DataAccessException exception
     * @return getAlertMessagesList List
     */
    public int deleteAlert(Alert alert) throws DataAccessException {
        return getAdministratorDao().deleteAlert(alert);
    }

	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");		
		AdministratorService administratorService = (AdministratorService) context.getBean("administratorService");
		/*List<Alert> list=new ArrayList<Alert>();
		try {
			list = administratorService.getAdminAlerts();
		} catch (DataAccessException e) {
			
			e.printStackTrace();
		}
		System.out.println("list size"+list.size());*/
		/*try {
		SimpleDateFormat sdf = new SimpleDateFormat("m/dd/yyyy hh:mm:ss a");
		String endDateInString = "04/10/2025 11:43:44 PM";
		Date endDate = sdf.parse(endDateInString);
		Alert alert =new Alert();
		alert.setAlertId("7003994");
		alert.setEndDate(endDate);
		alert.setStartDate(new Date());
		alert.setMessage("Testing admin alert---Deepa");
		alert.setLastModifiedBy("cxpemp1");
		alert.setLastModifiedDate(new Date());
		
			int value=administratorService.saveOrUpdateAlertDetails(alert);
			System.out.println("Success/Failure --1/0 ::"+value);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		try {
			Alert alert =new Alert();
			alert.setAlertId("5382701");
			alert.setIsExist("0");
			alert.setLastModifiedBy("cxpemp1");
			alert.setLastModifiedDate(new Date());
			
				int value=administratorService.deleteAlert(alert);
				System.out.println("Success/Failure --1/0 ::"+value);
			} catch (DataAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		
	}

}
