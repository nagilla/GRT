package com.avaya.grt.service.technicalonboarding;

import java.util.List;

import com.avaya.grt.jms.avaya.v2.techregistration.ResponseType;
import com.avaya.grt.jms.avaya.v2.techregistration.TRResponseResultType;
import com.avaya.grt.jms.avaya.v2.techregistration.TechRegRespHeader;
import com.avaya.grt.jms.avaya.v2.techregistration.TechRegRespType;
import com.avaya.grt.jms.avaya.v4.techregistration.ResponseAlarmType;
import com.avaya.grt.jms.avaya.v4.techregistration.TechRegResponse;
import com.avaya.grt.mappers.SiteRegistration;
import com.grt.dto.TechRegDataResponseDto;
import com.grt.dto.TechRegResultResponseDto;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.DataAccessException;


public interface TechnicalOnBoardingJMSService {
	public TRResponseResultType processReceivedTechReg(TechRegRespHeader techRegRespHeader, ResponseType responseType);
	public TechRegResultResponseDto processReceivedTechRegRetry(TechRegRespHeader techRegRespHeader, TechRegRespType techRegRespType);
	public TRResponseResultType processReceivedTechRegReqNew(List<TechRegDataResponseDto> techRegResponseDtos) throws Throwable;
	public void processReceivedTechRegRetryReq(TechRegResultResponseDto techRegResultResponseDto) throws Throwable;
	public TechRegResponse processReceivedAlarmTechReg(com.avaya.grt.jms.avaya.v4.techregistration.TechRegRespHeader techRegRespHeader, ResponseAlarmType responseAlarmType);
	public SiteRegistration getSiteRegistrationOnRegId(String registrationId) throws DataAccessException;
	public String getAorig(String productCode, String connectivity) throws DataAccessException;
	public List<TechnicalOrderDetail> getTechnicalOrderByType(
            String registrationId, String orderType) throws DataAccessException;
}
