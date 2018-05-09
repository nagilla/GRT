package com.avaya.grt.service.installbase;

import java.util.List;

import com.avaya.grt.jms.fmw.xml.filePoller.jms.IBaseCreateRespHeader;
import com.avaya.grt.jms.fmw.xml.filePoller.jms.IBaseCreateRespType;
import com.avaya.grt.mappers.SiteRegistration;
import com.grt.dto.InstallBaseSapResponseDto;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.DataAccessException;


public interface InstallBaseJMSService {
	public InstallBaseSapResponseDto processReceivedInstallBase(InstallBaseSapResponseDto installBaseSapResponseDto) throws Throwable;
	public InstallBaseSapResponseDto installBaseCreate(IBaseCreateRespHeader iBaseCreateRespHeader, IBaseCreateRespType iBaseCreateRespType);
	public InstallBaseSapResponseDto installBaseCreateRetry(IBaseCreateRespHeader iBaseCreateRespHeader, IBaseCreateRespType iBaseCreateRespType);
	public SiteRegistration getSiteRegistrationOnRegId(String registrationId) throws DataAccessException;
	public String getAorig(String productCode, String connectivity) throws DataAccessException;
	public List<TechnicalOrderDetail> getTechnicalOrderByType(
            String registrationId, String orderType) throws DataAccessException;
}
