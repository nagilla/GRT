package com.grt.dto;

import java.io.Serializable;

public class Pager implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int offSet;
	private int fetchSize;
	private int totalNum;
	
	public int getFetchSize() {
		return fetchSize;
	}
	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}
	public int getOffSet() {
		return offSet;
	}
	public void setOffSet(int offSet) {
		this.offSet = offSet;
	}
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
}
