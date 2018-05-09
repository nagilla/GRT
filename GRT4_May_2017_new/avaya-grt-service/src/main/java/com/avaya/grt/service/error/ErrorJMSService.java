package com.avaya.grt.service.error;

import com.grt.dto.ErrorDto;


public interface ErrorJMSService {
	public ErrorDto processErrorIB(String errCode, String errDesc, String txtTransactionId, String txtTransactionType, String txtErrorId);
	public ErrorDto processErrorEQR(String errCode, String errDesc, String txtTransactionId, String txtTransactionType, String txtErrorId);
	public ErrorDto processErrorTR(String errCode, String errDesc, String txtTransactionId, String txtTransactionType, String txtErrorId);
	public ErrorDto processErrorAlarmTR(String errCode, String errDesc, String txtTransactionId, String txtTransactionType, String txtErrorId);
}
