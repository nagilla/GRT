package com.grt.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.avaya.grt.mappers.ExpandedSolutionElement;
import com.avaya.grt.mappers.SRRequest;
import com.avaya.grt.mappers.Status;
import com.avaya.grt.mappers.TechnicalOrder;
import com.avaya.grt.mappers.TechnicalRegistration;

/**
 * Process the uploaded Material Entry Excel worksheet or generate a new Excel worksheet based on the TechnicalOrderDetail
 *
 * @author Perficient
 *
 */
public class TechnicalRegistrationUIUtil {

    private final static Logger logger = Logger.getLogger(TechnicalOrderDetailWorsheetProcessor.class);

    public static TechnicalRegistration getNonSalTRs(TechnicalRegistration tr, boolean isSuperUser, String statusId) {
    	logger.debug("Not ACCESS_TYPE_SAL::"+tr.getAccessType());
		TechnicalOrder to = tr.getTechnicalOrder();
		if (to != null){
		to.getMaterialCode();
		to.getDescription();
		} else {
			to = new TechnicalOrder();
			to.setMaterialCode("");
			to.setDescription("");
		}
		tr.setTechnicalOrder(to);
		
		SRRequest srr = tr.getSrRequest();
		if (srr != null){
			srr.getSiebelSRNo();
			tr.setStrSiebelSRNo(srr.getSiebelSRNo());
		} else {
			tr.setStrSiebelSRNo("");
		}
		
		Status st = tr.getStatus();
		if (st != null){
		st.getStatusId();
		st.getStatusDescription();
		//Begin Override Logic
		if(!st.getStatusId().equals(StatusEnum.COMPLETED.getStatusId()) && isSuperUser) {
			tr.setOverRideButtonStatus(false);
		} else {
			tr.setOverRideButtonStatus(true);
		}
		//End Override Logic
		} else {
			st = new Status();
			st.setStatusId("");
			st.setStatusDescription("");
		}
		tr.setStatus(st);
    	
    	//logger.debug("SiebelSRNo  from TechnicalRegistration =============> :: "+ tr.getSrRequest().getSiebelSRNo());
		//////////////
    	if (tr.getAccessType() != null){
	    	if(tr.getAccessType().equalsIgnoreCase(GRTConstants.ACCESS_TYPE_IPO)){
	    		System.out.println("ACCESS_TYPE_IPO::"+tr.getAccessType());
	    		//If AT-IPO: Then Update will be disabled
	    		tr.setUpdateButtonStatus(true);
	    	} else if (tr.getAccessType().equalsIgnoreCase(GRTConstants.ACCESS_TYPE_MODEM) || tr.getAccessType().equalsIgnoreCase(GRTConstants.ACCESS_TYPE_IP)) {
	    		//If AT-RASIP/Modem-Update will be enabled only If TR.NumberOfSubmite is less than 2.
	    		if (Integer.parseInt(tr.getNumberOfSubmit()) < 2){
	    			tr.setUpdateButtonStatus(false);
	    			tr.setSubmitButtonStatus(false);
	    		} else {
	    			tr.setUpdateButtonStatus(true);
	    			tr.setSubmitButtonStatus(true);
	    		}
	    	}
    	}
    	
    	if (st != null){
			/*if(st.getStatusId().equals(StatusEnum.COMPLETED.getStatusId())) {
				tr.setUpdateButtonStatus(true);
				tr.setSubmitButtonStatus(true);
			}*/
			if(! st.getStatusId().equals(StatusEnum.AWAITINGINFO.getStatusId())) {
				System.out.println("AWAITINGINFO::"+StatusEnum.AWAITINGINFO.getStatusId());
				tr.setSubmitButtonStatus(true);
				tr.setUpdateButtonStatus(true);
			}
    	}

    	tr.getPrimarySalGWSeid();
    	
    	tr.getSecondarySalGWSeid();
    	// If AT is Modem: TR.outboundPrefix+TR.dialInNumber - If AT is RASIP/IP: TR.ipAddress
    	if(tr.getAccessType().equalsIgnoreCase(GRTConstants.ACCESS_TYPE_MODEM)){
    		System.out.println("ACCESS_TYPE_MODEM::"+tr.getAccessType());
    		StringBuilder sb = null;
    		if(StringUtils.isNotEmpty(tr.getOutboundCallingPrefix()) ){
    			sb = new StringBuilder();
    			sb.append(tr.getOutboundCallingPrefix());
    			if(StringUtils.isNotEmpty(tr.getDialInNumber()) ){
    				sb.append(tr.getDialInNumber());
    			}
    		} else {
    			if(StringUtils.isNotEmpty(tr.getDialInNumber()) ){
    				sb = new StringBuilder();
    				sb.append(tr.getDialInNumber());
    			}
    		}
    		if(sb != null){
    			tr.setConnectionDetail(sb.toString());
    		}
    	} else {
    		tr.setConnectionDetail(tr.getIpAddress());
    	}
    	
    	//Begin: Modified for wi01111434
    	if( StatusEnum.COMPLETED.getStatusId().equals(statusId) ){
    		tr.setSubmitButtonStatus(true);
			tr.setUpdateButtonStatus(true);
			tr.setCheckBoxDisabled(true);
    	}
    	//End: Modified for wi01111434
    	
    	//Begin: Access Type conversion from DB to UI						    	
    	if( tr.getAccessType().equals(GRTConstants.ACCESS_TYPE_IP) ){
    		tr.setUiAccessType(AccessTypeEnum.IP.getUiAccessType());
    	} else if( tr.getAccessType().equals(GRTConstants.ACCESS_TYPE_IPO) ){
    		tr.setUiAccessType(AccessTypeEnum.IPO.getUiAccessType());
    	} else {
    		tr.setUiAccessType(tr.getAccessType());
    	}
    	//End: Access Type conversion from DB to UI

    	return tr;
    }
    
    
    public static TechnicalRegistration getSalTRs(TechnicalRegistration tr, boolean isSuperUser, String statusId ) {
    	TechnicalOrder to = tr.getTechnicalOrder();
		if (to != null){
		to.getMaterialCode();
		to.getDescription();
		} else {
			to = new TechnicalOrder();
			to.setMaterialCode("");
			to.setDescription("");
		}
		tr.setTechnicalOrder(to);
		
		SRRequest srr = tr.getSrRequest();
		if (srr != null){
			srr.getSiebelSRNo();
			tr.setSrRequest(srr);
			tr.setStrSiebelSRNo(srr.getSiebelSRNo());
		} else {
			tr.setStrSiebelSRNo("");
		}
		SRRequest stepBsrr = tr.getStepBSRRequest();
		if(stepBsrr != null){
			stepBsrr.getSiebelSRNo();
			tr.setStepBSRRequest(stepBsrr);
			tr.setStrStepBSRNo(stepBsrr.getSiebelSRNo());
		}else{
			tr.setStrStepBSRNo("");
		}
		Status st = tr.getStatus();
		Status stepBStatus = tr.getStepBStatus();
		if (st != null){
			st.getStatusId();
			st.getStatusDescription();
			
			if (tr.getNumberOfSubmit()!= null && Integer.parseInt(tr.getNumberOfSubmit()) >= 2) {
				tr.setSubmitButtonStatus(true);
				tr.setUpdateButtonStatus(true);
				if( GRTConstants.SAL_SE_CODE.equals(tr.getSolutionElement()) && st.getStatusId().equals(StatusEnum.AWAITINGINFO.getStatusId())
						&& !GRTConstants.SECODE.ACCCM.equalsIgnoreCase(tr.getSolutionElement())){
					tr.setSubmitButtonStatus(false);
            	}
			} else {
				if(! st.getStatusId().equals(StatusEnum.AWAITINGINFO.getStatusId())) {
					tr.setSubmitButtonStatus(true);
					tr.setUpdateButtonStatus(true);
				} else {
					if( GRTConstants.SAL_SE_CODE.equals(tr.getSolutionElement()) ){
						tr.setUpdateButtonStatus(true);
						if (!GRTConstants.SECODE.ACCCM.equalsIgnoreCase(tr.getSolutionElement())){ 
							tr.setSubmitButtonStatus(false);
						}	
                	}											
				}
			}
			if (GRTConstants.SECODE.ACCCM.equalsIgnoreCase(tr.getSolutionElement())) {
				tr.setSubmitButtonStatus(true);
			}
			
			//Begin Override Logic
			if(!st.getStatusId().equals(StatusEnum.COMPLETED.getStatusId()) && isSuperUser) {
				tr.setOverRideButtonStatus(false);
			} else {
				tr.setOverRideButtonStatus(true);
			}
			//End Override Logic
			
			//Begin Check box enable or disable logic
			if (StatusEnum.COMPLETED.getStatusId().equals(st.getStatusId()) 
            		&&  (stepBStatus == null || StatusEnum.NOTINITIATED.getStatusId().equals(stepBStatus.getStatusId())) 
            		&& (tr.getSolutionElementId() != null && StringUtils.isNotEmpty(tr.getSolutionElementId()))){
            	tr.setCheckBoxDisabled(false);
            	/*if( GRTConstants.SAL_SE_CODE.equals(tr.getSolutionElement()) || GRTConstants.SAL_VIRTUAL_GATEWAY.equals(tr.getSolutionElement())   ){
            		tr.setCheckBoxDisabled(true);
            	}*/ // wi01114225
            	
            } else {
            	tr.setCheckBoxDisabled(true);
            }
            //Begin Check box enable or disable logic

		} else {
			st = new Status();
			st.setStatusId(" ");
			st.setStatusDescription("");
		}
		tr.setStatus(st);
		
		if(stepBStatus != null){
			logger.debug("Fetching the Status" );
			stepBStatus.getStatusId();
			stepBStatus.getStatusDescription();
			tr.setStepBStatus(stepBStatus);
	    	// Begin to populate the Detail Button state i.e Disable / Enable
			// �	The �Detail� button will enable only when Step-B(Alarm & Connectivity status) status is IN-Process or Completed.
	    	if(StatusEnum.COMPLETED.getStatusId().equals(stepBStatus.getStatusId()) || StatusEnum.INPROCESS.getStatusId().equals(stepBStatus.getStatusId())){
	    		tr.setDetailButtonStatus(false);
	    	} else {
	    		tr.setDetailButtonStatus(true);
	    	}
	    	// End to populate the Detail Button state i.e Disable / Enable 
			
		} else {
			Status stepBst = new Status();
			stepBst.setStatusId(StatusEnum.NOTINITIATED.getStatusId());
			stepBst.setStatusDescription(StatusEnum.NOTINITIATED.getStatusDescription());
			tr.setStepBStatus(stepBst);
		}
		
    	logger.debug("Material Code from TechnicalRegistration =============> :: "+ tr.getTechnicalOrder().getMaterialCode());
    	logger.debug("Material Description from TechnicalRegistration =============> :: "+ tr.getTechnicalOrder().getDescription());
    	
    	//logger.debug("SiebelSRNo  from TechnicalRegistration =============> :: "+ tr.getSrRequest().getSiebelSRNo());
    	
    	logger.debug("Status ID  from TechnicalRegistration =============> :: "+ tr.getStatus().getStatusId());
    	logger.debug("StatusDescription  from TechnicalRegistration =============> :: "+ tr.getStatus().getStatusDescription());
    	
    	logger.debug("TR.SolutionElementID   from TechnicalRegistration =============> :: "+ tr.getSolutionElementId());
		//////////////


    	////////////////
    	/*if(tr.getAccessType().equalsIgnoreCase(GRTConstants.ACCESS_TYPE_MODEM)){
    		tr.setUpdateButtonStatus(true);
    		logger.debug("15 15 15 15 15 15  DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD" );
    	}*/
    	///Testing
    	//tr.setOverRideButtonStatus(true);
    	///Testing 
    	tr.getPrimarySalGWSeid();
    	tr.getSecondarySalGWSeid();
    	tr.setSelectForAlarmAndConnectivity(false);
    	
    	//Begin: Modified for wi01111434
    	if( StatusEnum.COMPLETED.getStatusId().equals(statusId) ){
    		tr.setSubmitButtonStatus(true);
			tr.setUpdateButtonStatus(true);
			tr.setCheckBoxDisabled(true);
    	}
    	//End: Modified for wi01111434

    	logger.debug(">>>>>>>>>>>>>..."+tr.getSid());
    	logger.debug(">>>>>>>>>>>>>..."+tr.getMid());
    	//Begin: To fetch the ExpandedSolutionElement
    	List<ExpandedSolutionElement> expSolElements = new ArrayList<ExpandedSolutionElement>();
    	java.util.Set<ExpandedSolutionElement> explodedSolutionElements = tr.getExplodedSolutionElements();
    	if(explodedSolutionElements != null && explodedSolutionElements.size()>0){
	    	for(ExpandedSolutionElement expSolEle : explodedSolutionElements) {
	    		expSolEle.getAlarmId();
	    		expSolEle.getSeCode();
	    		expSolEle.getSeID();
	    		expSolEle.getIpAddress();
	    		expSolEle.getTechnicalRegistration();
	    		
	    		expSolEle.setSid(tr.getSid());
	    		expSolEle.setMid(tr.getMid());
	    		expSolEle.setPrimarySalGWSeid(tr.getPrimarySalGWSeid());
	    		expSolEle.setSecondarySalGWSeid(tr.getSecondarySalGWSeid());
	    		//expSolElements.add(expSolEle);
				if(tr.getSolutionElementId()!= null && !tr.getSolutionElementId().equals(expSolEle.getSeID())){
					expSolElements.add(expSolEle);
				}
	    		
	    	}
    	}
    	tr.setExpSolutionElements(expSolElements);
    	//End: To fetch the ExpandedSolutionElement 

    	if( null != tr.getAccessType() ){
    		tr.setUiAccessType(tr.getAccessType());
    	}
    	
    	return tr;

    }
    
    
}