package com.avaya.grt.dao.srupdate;

import java.util.List;

import com.avaya.grt.mappers.ExpandedSolutionElement;
import com.avaya.grt.mappers.SiteList;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.grt.util.DataAccessException;
import com.grt.util.ProcessStepEnum;
import com.grt.util.StatusEnum;

public interface SRUpdateJMSDao {

	public List<SiteList> getSiteListByArtSR (String artSrNo) throws DataAccessException;
	public void undoStepBForSiteList(SiteList siteList, StatusEnum status) throws DataAccessException;
	public int deleteExpandedSolutionElements(String id) throws DataAccessException;
	 public List<TechnicalRegistration> getTechnicalRegistrationByArtSRForStepB(String srNumber) throws DataAccessException;
	 public void updateExpandedSolutionElements(List<ExpandedSolutionElement> expandedSolutionList) throws DataAccessException;
	 public void undoStepBForTechnicalRegistration(TechnicalRegistration technicalRegistration, StatusEnum status) throws DataAccessException;
	 public SiteRegistration getSiteRegistrationBySiebelSR(String siebelSR, String typeOfReq) throws DataAccessException;
	 public SiteRegistration updateSiteRegistrationOnSRUpdate(String registrationId, ProcessStepEnum processStep, StatusEnum statusEnum, String isSrCompleted) throws DataAccessException;
	 public TechnicalRegistration updateTechnicalRegistrationStatus(TechnicalRegistration technicalRegistration, StatusEnum status) throws DataAccessException;
	 public List<TechnicalRegistration> getTechnicalRegistrationByArtSR(String srNumber) throws DataAccessException;
	 public void refresh(Object obj);
	 public List<SiteList> getSiteListsByArtSr(String artSrNo) throws DataAccessException;
	 public SiteList updateSiteListStepBStatus(SiteList siteList, StatusEnum status) throws DataAccessException;
	 public TechnicalRegistration updateTechnicalRegistrationStepBStatus(TechnicalRegistration technicalRegistration, StatusEnum status) throws DataAccessException;
	 public TechnicalRegistration updateTechnicalRegistrationEPNSurveyStatus(TechnicalRegistration technicalRegistration, StatusEnum status) throws DataAccessException;
	 public SiteList getSiteListByArtSr(String artSrNo) throws DataAccessException;
	 
}
