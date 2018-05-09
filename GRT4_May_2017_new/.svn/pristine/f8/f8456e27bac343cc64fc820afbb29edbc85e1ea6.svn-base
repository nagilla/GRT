package com.grt.applicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.avaya.grt.jms.fmw.xml.filePoller.jms.IBaseCreateRespHeader;
import com.avaya.grt.jms.fmw.xml.filePoller.jms.IBaseCreateRespType;
import com.avaya.grt.service.installbase.InstallBaseJMSService;
import com.grt.dto.InstallBaseSapResponseDto;


public class GRTServletContextListener extends ContextLoaderListener {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(GRTServletContextListener.class);
    
    /*
    DefaultMessageListenerContainer techRegLocalContainer; 
    DefaultMessageListenerContainer filePollerLocalContainer;
    DefaultMessageListenerContainer srUpdateLocalContainer;
    DefaultMessageListenerContainer ibaseCreateRetryResponseLocalContainer;
    DefaultMessageListenerContainer errorQueueLocalContainer;
    DefaultMessageListenerContainer assetUpdateLocalContainer;
    DefaultMessageListenerContainer techRegRetryResponseLocalContainer;
    DefaultMessageListenerContainer equipmentRemovalResponseLocalContainer;
    DefaultMessageListenerContainer equipmentRemovalRetryResponseLocalContainer;
    DefaultMessageListenerContainer SALGatewayInstallBaseRequestContainer;
    DefaultMessageListenerContainer cmPollingResponseContainer;
    */
    private static int count = 0;
    private Boolean initialized = false;
    
    @Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		final WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);

		
		logger.debug("Starting Listener............." + ++count);
		synchronized(initialized) {
			if(initialized) {
				return;
			} else {
				initialized = true;
			}
		}
		try {
			 new Thread() {
		            public void run() {			           			            		            	
		            	/*
		            	InstallBaseJMSService installBaseJMSService = (InstallBaseJMSService)appContext.getBean("installBaseJMSService");
		            	IBaseCreateRespHeader iBaseCreateRespHeader = new IBaseCreateRespHeader();
		            	iBaseCreateRespHeader.setTransactionId("0123456");
		            	iBaseCreateRespHeader.setErrorId("Success");
		            	IBaseCreateRespType iBaseCreateRespType = new IBaseCreateRespType();
		            	InstallBaseSapResponseDto installBaseDto = installBaseJMSService.installBaseCreate(iBaseCreateRespHeader, iBaseCreateRespType);
		 
		            	filePollerLocalContainer = (DefaultMessageListenerContainer)appContext.getBean("filePollerContainer");
			            techRegLocalContainer = (DefaultMessageListenerContainer)appContext.getBean("techRegContainer");
			            srUpdateLocalContainer = (DefaultMessageListenerContainer)appContext.getBean("srUpdateContainer");
			            assetUpdateLocalContainer = (DefaultMessageListenerContainer)appContext.getBean("assetUpdateContainer");
			            ibaseCreateRetryResponseLocalContainer = (DefaultMessageListenerContainer)appContext.getBean("ibaseCreateRetryResponseContainer");
			            errorQueueLocalContainer = (DefaultMessageListenerContainer)appContext.getBean("errorQueueContainer");
			            techRegLocalContainer = (DefaultMessageListenerContainer)appContext.getBean("techRegContainer");
			            filePollerLocalContainer = (DefaultMessageListenerContainer)appContext.getBean("filePollerContainer");
			            
			            techRegRetryResponseLocalContainer = (DefaultMessageListenerContainer)appContext.getBean("techRegRetryResponseContainer");
			            equipmentRemovalResponseLocalContainer = (DefaultMessageListenerContainer)appContext.getBean("equipmentRemovalResponseContainer");
			            equipmentRemovalRetryResponseLocalContainer = (DefaultMessageListenerContainer)appContext.getBean("equipmentRemovalRetryResponseContainer");
			            SALGatewayInstallBaseRequestContainer = (DefaultMessageListenerContainer)appContext.getBean("SALGatewayInstallBaseRequestContainer");
			            
			            cmPollingResponseContainer= (DefaultMessageListenerContainer)appContext.getBean("CMPollingResponseContainer");
			            */
			            logger.debug("The JMS listeners are now initialized and ready !!");
		            }
			 }.start();
	     } catch(RuntimeException rte) {
	            logger.error("Unexpected failure while loading GRT JMS Client!", rte);
	            throw rte;
	     }
	     logger.debug("GRT Servelet Context Listner finished, Listeners initiated on a new thread.");
	}

	@Override
    public void contextDestroyed(ServletContextEvent event) {
			/*
            if(srUpdateLocalContainer != null) {
            	logger.debug("Starting to destroy srUpdateLocalContainer...");
            	srUpdateLocalContainer.stop();
//            	srUpdateLocalContainer.destroy();
            	logger.debug("destroied srUpdateLocalContainer...");
            }
            if(assetUpdateLocalContainer != null) {
            	logger.debug("Starting to destroy assetUpdateLocalContainer...");
            	assetUpdateLocalContainer.stop();
//            	assetUpdateLocalContainer.destroy();
            	logger.debug("destroied assetUpdateLocalContainer...");
            }
              if(filePollerLocalContainer != null) {
            	logger.debug("Starting to destroy filePollerLocalContainer...");
            	filePollerLocalContainer.stop();
//            	filePollerLocalContainer.destroy();
            	logger.debug("destroied filePollerLocalContainer...");
            }
            if(techRegLocalContainer != null) {
            	logger.debug("Starting to destroy techRegLocalContainer...");
            	techRegLocalContainer.stop();
//            	techRegLocalContainer.destroy();

            	logger.debug("destroied techRegLocalContainer...");
            }
            if(errorQueueLocalContainer != null) {
            	logger.debug("Starting to destroy errorQueueLocalContainer...");
            	errorQueueLocalContainer.stop();
//            	errorQueueLocalContainer.destroy();
            	logger.debug("destroied errorQueueLocalContainer...");
            }
            if(ibaseCreateRetryResponseLocalContainer != null) {
            	logger.debug("Starting to destroy ibaseCreateRetryResponseLocalContainer...");
            	ibaseCreateRetryResponseLocalContainer.stop();
//            	ibaseCreateRetryResponseLocalContainer.destroy();
            	logger.debug("destroied ibaseCreateRetryResponseLocalContainer...");
            }
            if(techRegRetryResponseLocalContainer != null) {
            	logger.debug("Starting to destroy techRegRetryResponseLocalContainer...");
            	techRegRetryResponseLocalContainer.stop();
//            	techRegRetryResponseLocalContainer.destroy();
            	logger.debug("destroied techRegRetryResponseLocalContainer...");
            }
            if(equipmentRemovalResponseLocalContainer != null) {
            	logger.debug("Starting to destroy equipmentRemovalResponseLocalContainer...");
            	equipmentRemovalResponseLocalContainer.stop();
//            	equipmentRemovalResponseLocalContainer.destroy();
            	logger.debug("destroied equipmentRemovalResponseLocalContainer...");
            }
            if(equipmentRemovalRetryResponseLocalContainer != null) {
            	logger.debug("Starting to destroy equipmentRemovalRetryResponseLocalContainer...");
            	equipmentRemovalRetryResponseLocalContainer.stop();
//            	equipmentRemovalResponseLocalContainer.destroy();
            	logger.debug("destroied equipmentRemovalRetryResponseLocalContainer...");
            }
            if(SALGatewayInstallBaseRequestContainer != null) {
            	logger.debug("Starting to destroy SALGatewayInstallBaseRequestContainer...");
            	SALGatewayInstallBaseRequestContainer.stop();
//            	SALGatewayInstallBaseRequestContainer.destroy();
            	logger.debug("destroied SALGatewayInstallBaseRequestContainer...");
            }
            
            if(cmPollingResponseContainer != null) {
            	logger.debug("Starting to destroy CMPollingResponseContainer...");
            	cmPollingResponseContainer.stop();
//            	SALGatewayInstallBaseRequestContainer.destroy();
            	logger.debug("destroied CMPollingResponseContainer...");
            }
            */
			
			logger.debug("contextDestroyed called...... Stopping all JMS containers...");
    }
}
