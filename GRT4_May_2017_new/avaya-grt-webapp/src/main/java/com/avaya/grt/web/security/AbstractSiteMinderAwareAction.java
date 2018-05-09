package com.avaya.grt.web.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.avaya.grt.service.BaseRegistrationService;
import com.grt.dto.RegistrationFormBean;
import com.grt.util.DataAccessException;
import com.grt.util.GRTConstants;
import com.opensymphony.xwork2.Action;

public class AbstractSiteMinderAwareAction  implements ServletRequestAware,
ServletResponseAware {

	private static final Logger logger = Logger
			.getLogger(AbstractSiteMinderAwareAction.class);

	private HttpServletRequest request;
	private HttpServletResponse response;

	private Map<String, Object> session;

	private final String GRT_BEAN_SESSION_KEY = "grtregistrationbean";

	private CSSPortalUser cssPortalUser;
	protected BaseRegistrationService baseRegistrationService; 



	/**
	 * Method to get the CSSUserProfile from CSS Application.
	 * 
	 * @return cssPortalUser CSSPortalUser
	 */
	public CSSPortalUser getCSSUserProfile() {
		CSSPortalUser cssPortalUser = null;
		logger.debug("Entering AbstractSiteMinderAwareAction : getCSSUserProfile");

		logger.debug("Entering RegistrationController : getCSSUserProfile");
		logger.debug(" getRequest().getHeader('SM_USER') "+getRequest().getHeader("SM_USER"));
		logger.debug(" Header ********* ");
		logger.debug(" getRequest().getHeader('first_name') "+getRequest().getHeader("first_name"));
		logger.debug(" getRequest().getHeader('last_name') "+getRequest().getHeader("last_name"));
		logger.debug(" getRequest().getHeader('SM_USERDN') "+getRequest().getHeader("SM_USERDN"));
		logger.debug(" getRequest().getHeader('user_dn') "+getRequest().getHeader("user_dn"));
		logger.debug(" getRequest().getHeader('Proxy-Remote-User') "+getRequest().getHeader("Proxy-Remote-User"));
		//logger.debug(" getRequest().getHeader('email') "+getRequest().getHeader("email"));

		if (getRequest().getHeader("SM_USER") != null) {
			logger.debug("Entered User -" + getRequest().getHeader("SM_USER"));
			logger.debug("Entered if case...");

			cssPortalUser = new CSSPortalUser();
			if( getRequest().getHeader("SM_USERDN") != null  ){
				cssPortalUser.setUserType(this.getUserType(getRequest().getHeader(
						"SM_USERDN")));
			}else{
				logger.debug("SM_USERDN is Empty...");
				//Temporary fix
				cssPortalUser.setUserType( "B" );
			}
			logger.debug("After User Type....");
			cssPortalUser.setUserId(getRequest().getHeader("SM_USER"));
			logger.debug("After Getting SM_USER....");
			cssPortalUser.setFirstName(getRequest().getHeader("first_name"));
			logger.debug("After Getting First Name....");
			cssPortalUser.setLastName(getRequest().getHeader("last_name"));
			logger.debug("After Getting last_name....");
			
			/*Oceana Chat Phase 1*/
			cssPortalUser.setPhoneNumber(getRequest().getHeader("Phone_Number"));
			logger.debug("After Getting phone_number....");
			cssPortalUser.setCountry(getRequest().getHeader("Country"));
			logger.debug("After Getting country....");
			cssPortalUser.setCity(getRequest().getHeader("City"));
			logger.debug("After Getting City....");
			cssPortalUser.setStreetAddress1(getRequest().getHeader("Address"));
			logger.debug("After Getting Address....");
			/*cssPortalUser.setBpLinkId(getRequest().getHeader("bpLinkId"));
			logger.debug("After Getting bpLinkId....");*/			
			cssPortalUser.setCompany(getRequest().getHeader("Company_Name"));
			logger.debug("After Getting Company_Name....");			
			cssPortalUser.setCustomerNumber(getRequest().getHeader("Cust_Nbr"));
			logger.debug("After Getting Cust_Nbr....");			
			cssPortalUser.setLinkId(getRequest().getHeader("Link_ID"));
			logger.debug("After Getting Link_ID....");
			cssPortalUser.setZipcode(getRequest().getHeader("Zip"));
			logger.debug("After Getting Zip....");			
			
			//Load chat configuration from DB
			try {
				logger.debug("Before fetching Chat configuration from DB....");		
				Map<String,String> chatConfigurationMap = getBaseRegistrationService().getChatConfiguration();				
				cssPortalUser.setChatConfigurations(chatConfigurationMap);
				logger.debug("After fetching Chat configuration from DB....");		
				if(chatConfigurationMap != null) {
					for (Entry<String, String> chatConf : chatConfigurationMap.entrySet()) {
						logger.debug(chatConf.getKey() + " ::: " + chatConf.getValue());
					}
				}
			} catch (DataAccessException e1) {
				logger.error("Error in fetching chat configurations from grt DB");
			}
			
			/*Oceana Chat Phase 1 Ends*/
						
			/*cssPortalUser.setBpLinkId(getBpLinkId(getRequest().getHeader(
					"user_dn")));*/
			if( getRequest().getHeader(
					"user_dn")!=null ){
				if( !getRequest().getHeader(
					"user_dn").contains(",") ){
					cssPortalUser.setBpLinkId(getRequest().getHeader(
						"user_dn"));	
				}else{
					cssPortalUser.setBpLinkId(getBpLinkId(getRequest().getHeader(
							"user_dn")));
				}
			}else{
				cssPortalUser.setBpLinkId(getBpLinkId(getRequest().getHeader(
						"SM_USERDN")));
			}
			logger.debug("After Getting user_dn....");
			cssPortalUser.setEmailAddress(getRequest().getHeader(
					"Proxy-Remote-User"));

			logger.debug("After Getting email Address....");
			// TODO

			try {
				cssPortalUser.setSuperUser(getBaseRegistrationService().isSuperUser(
						cssPortalUser.getUserId()));
			} catch (DataAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			// Keep the data in Session
			if( getRequest()!=null ){
				logger.debug("Before Setting GRTConstants.CSS_USER_PROFILE...");
				getRequest().getSession().setAttribute(
						GRTConstants.CSS_USER_PROFILE, cssPortalUser);
				logger.debug("After Setting GRTConstants.CSS_USER_PROFILE..."+getRequest().getSession()
						.getAttribute(GRTConstants.CSS_USER_PROFILE));
			}

			logger.debug("After Getting Session Attribute....");

			logger.debug("User Profile Info : " + cssPortalUser.toString());
		}
		logger.debug("Exiting AbstractSiteMinderAwareAction : getCSSUserProfile");

		return cssPortalUser;
	}

	/**
	 * Get the user type from the LDAP String.
	 * 
	 * uid=cssadmin,ou=CUST,ou=People,dc=avaya,dc=com
	 * uid=turner514,ou=1421,ou=BP,ou=People,dc=avaya,dc=com
	 * 
	 * @param ldapString
	 * @return
	 */
	public String getUserType(String ldapString) {
		String userType = null;
		if (ldapString != null) {
			if (ldapString.contains("ou=CUST")) {
				userType = "C";
			} else if (ldapString.contains("ou=BP")) {
				userType = "B";
			} else {
				userType = "A";
			}
		}

		return userType;
	}

	/*
	 * get the value of the first "ou"
	 * 
	 * Like the sampe ldapString is
	 * "uid=grtbpa1,ou=87,ou=bp,ou=people,dc=avaya,dc=com", then the return
	 * value should be "87"
	 */
	protected String getBpLinkId(String ldapString) {
		//return ldapString.split(",")[1].split("=")[1];
		//Modified to handled arrayIndexOutOfBound
		String ldapArr[] = ldapString.split(",")[1].split("=");
		if( ldapArr!=null && ldapArr.length > 1 ){
			return ldapArr[1];
		}
		return "";
	}

	/**
	 * This method returns the dummy CSSPortalUser for local environment and
	 * testing purposes.
	 * 
	 * @return
	 * @throws Exception
	 */
	public CSSPortalUser getDummyCssUserProfile() throws Exception {
		logger.debug("Inside Dummy User ** ");
		/*
		 * CSSPortalUser cssPortalUser = new CSSPortalUser("grtbpb1",
		 * "cxp emp1", "LinkID89169", "Avaya Inc.", "B", "2463",
		 * "gsurakasula@avaya.com")
		 */
		/*
		 * CSSPortalUser cssPortalUser = new CSSPortalUser("babygirl", "Mike",
		 * "John", "Perficient", "A", "", "tlavanya@avaya.com");
		 */
		/*CSSPortalUser cssPortalUser = new CSSPortalUser("grtbpb1", "cxp emp1",
				"LinkID89169", "Avaya Inc.", "B", "2463",
				"dvadakkath@avaya.com");*/

		CSSPortalUser cssPortalUser = new CSSPortalUser("cxpemp1", "cxp",
				"emp1", "Avaya Inc.", "A", "",
				"dvadakkath@avaya.com");
		
		/*CSSPortalUser cssPortalUser = new CSSPortalUser("partn14", "John",
				"Smith", "Avaya Inc.", "B", "933",
				"tango_partner1@gmail.com");*/

		//Load chat configuration from DB
		try {
			logger.debug("Before fetching Chat configuration from DB....");		
			Map<String,String> chatConfigurationMap = getBaseRegistrationService().getChatConfiguration();				
			cssPortalUser.setChatConfigurations(chatConfigurationMap);
			logger.debug("After fetching Chat configuration from DB....");		
			if(chatConfigurationMap != null) {
				for (Entry<String, String> chatConf : chatConfigurationMap.entrySet()) {
					logger.debug(chatConf.getKey() + " ::: " + chatConf.getValue());
				}
			}
		} catch (DataAccessException e1) {
			logger.error("Error in fetching chat configurations from grt DB");
		}
		
		/*Oceana Chat Phase 1 Ends*/
		
		//TODO
		cssPortalUser.setSuperUser(getBaseRegistrationService().isSuperUser(
				"babygirl"));
		if( getRequest()!=null ){
			getRequest().getSession().setAttribute(GRTConstants.CSS_USER_PROFILE,
					cssPortalUser);
		}

		return cssPortalUser;
	}

	public void validateSession() throws Exception {
		logger.debug("Entering validateSession");
		CSSPortalUser sessionUser = (CSSPortalUser) getRequest().getSession()
				.getAttribute(GRTConstants.CSS_USER_PROFILE);
		if (sessionUser == null || StringUtils.isEmpty(sessionUser.getUserId())) {
			logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> THROW EXCEPTION <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			throw new IllegalStateException("Test Session sessionUser - "+sessionUser);
		} else {
			logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> VALID SESSION <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< MAX Inactive Interval in secs. "
					+ getRequest().getSession().getMaxInactiveInterval());
		}
		logger.debug("Exiting validateSession");
	}

	/*
	 * get user from page flow session or the dwr session
	 */
	public CSSPortalUser getUserFromSession() {
		CSSPortalUser user = null;
		try {
			logger.debug("fetching user from controller session ...");
			user = (CSSPortalUser) (getRequest().getSession()
					.getAttribute(GRTConstants.CSS_USER_PROFILE));
		} catch (Exception e) {
		} finally {
			if (user == null) {
				logger.debug("---User is null.. Populating from request.. ");
				user = getCSSUserProfile();
			}
		}
		logger.debug("is user exist --> " + (user != null));
		return user;
	}

	public String getLoggedInUserDetails() throws Exception{
		cssPortalUser =  getUserFromSession();
		if( cssPortalUser == null ){
			cssPortalUser = getDummyCssUserProfile();
		}
		return Action.SUCCESS;
	}

	public CSSPortalUser getLoggedInUserDetail(){
		cssPortalUser =  getUserFromSession();
		if( cssPortalUser == null ){
			try {
				cssPortalUser = getDummyCssUserProfile();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return cssPortalUser;
	}

	public String logout() throws Exception {
		try{
			//validateSession();
			CSSPortalUser portalUser = (CSSPortalUser) getRequest().getSession()
					.getAttribute(com.grt.util.GRTConstants.CSS_USER_PROFILE);
			logger.debug("Print Portal User -- "+portalUser);
			if (getRequest().getSession().getAttribute(
					GRTConstants.CSS_USER_PROFILE) != null) {
				portalUser.setUserId(null);
				logger.debug(" Setting portal user profile to null ");
			}

			getRequest().getSession()
			.removeAttribute(GRTConstants.CSS_USER_PROFILE);
			logger.debug(" After remove CSS_USER_PROFILE attribute ");
			//getRequest().getSession().invalidate();

			//custom remove
			//getSession().remove(GRTConstants.CSS_USER_PROFILE);
		}
		catch(Exception e){
			logger.debug(e.getMessage());
			e.printStackTrace();
		}finally{
			Cookie[] cookies = getRequest().getCookies();
			//logger.debug(" Getting Cookies from request... "+cookies.length);
			Cookie cookieToDelete = null;
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equalsIgnoreCase("SMSESSION")) {
					cookieToDelete = cookies[i];
					// mark for deletion by client by setting max age to zero
					cookieToDelete.setMaxAge(0);
					cookieToDelete.setPath("/");
					cookieToDelete.setDomain(".avaya.com");
					getResponse().addCookie(cookieToDelete);
					logger.debug(" After Removing SME Session Cookie ");
				}else if(cookies[i].getName().equalsIgnoreCase("JSESSIONID")){
					cookieToDelete = cookies[i];
					// mark for deletion by client by setting max age to zero
					cookieToDelete.setMaxAge(0);
					cookieToDelete.setPath("/");
					cookieToDelete.setDomain(".avaya.com");
					getResponse().addCookie(cookieToDelete);
					logger.debug(" After Removing SME Session Cookie ");
				}
				
				/*else if (cookies[i].getName().equalsIgnoreCase("CSSSESSIONID")) {
					cookieToDelete = cookies[i];
					// mark for deletion by client by setting max age to zero
					cookieToDelete.setMaxAge(0);
					cookieToDelete.setPath("/");
					cookieToDelete.setDomain(".avaya.com");
					getResponse().addCookie(cookieToDelete);
					logger.debug(" After Removing CSSSESSIONID Session Cookie ");
				} */else if (cookies[i].getName().equalsIgnoreCase("LoginMessage")) {
					cookieToDelete = cookies[i];
					cookieToDelete.setValue("No");
					cookieToDelete.setPath("/");
					cookieToDelete.setDomain(".avaya.com");
					getResponse().addCookie(cookieToDelete);
					logger.debug(" After Removing LoginMessage Session Cookie ");
				}
			}
		}
		/*getResponse().sendRedirect(
				GRTPropertiesUtil.getProperty(GRTConstants.CSS_HOME_URL));*/
		return "default";
	}

	public RegistrationFormBean getBeanFromSession() {
		return (RegistrationFormBean) getSession().get(GRT_BEAN_SESSION_KEY);
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public CSSPortalUser getCssPortalUser() {
		return cssPortalUser;
	}

	public void setCssPortalUser(CSSPortalUser cssPortalUser) {
		this.cssPortalUser = cssPortalUser;
	}

	public BaseRegistrationService getBaseRegistrationService() {
		return baseRegistrationService;
	}

	public void setBaseRegistrationService(
			BaseRegistrationService baseRegistrationService) {
		this.baseRegistrationService = baseRegistrationService;
	}

}
