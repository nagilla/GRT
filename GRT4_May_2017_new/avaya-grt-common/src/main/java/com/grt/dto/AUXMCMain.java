package com.grt.dto;

import java.io.Serializable;

public class AUXMCMain  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String seid, sid, mid, status;

	public String getSeid() {
		return seid;
	}

	public void setSeid(String seid) {
		this.seid = seid;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
