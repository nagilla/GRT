package com.avaya.grt.service.graph;

import java.util.Iterator;
import java.util.List;

import com.grt.dto.RegistrationCount;
import com.grt.util.DataAccessException;



public interface RegistrationGraphService {
	public List<RegistrationCount> getRegistrationsCompleted(String userId) throws DataAccessException;
	public List<RegistrationCount> getRegistrationsSaved(String userId) throws DataAccessException;
	public List<RegistrationCount> getRegistrationsNotCompleted(String userId) throws DataAccessException;
	public List<RegistrationCount> getRegistrationsCompletedList(String userId) throws DataAccessException;
}
