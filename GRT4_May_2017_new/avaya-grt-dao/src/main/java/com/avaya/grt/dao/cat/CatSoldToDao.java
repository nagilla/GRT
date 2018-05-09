package com.avaya.grt.dao.cat;

import java.util.List;

import com.grt.util.DataAccessException;

public interface CatSoldToDao {

	public List<String> querySoldToListAccess(String bpLinkId, List<String> soldToIdList) throws DataAccessException;
}
