package com.grt.dto;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class TechRegInputDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	    private String systemIdentifier;
	    
		private String afid;

	    private String almid;

	    /* This element is the Parent Linkage Information(SEID of the
	     * SAL) */
	    private String custipaddress;
	    
	    private String defaultpid;
	    
	    private String datalock;

	    /* This element holds the SAL order number */
	    private String dossno;

	    private String dtype;
	    
	    private String foption;
	    
	    /* This element holds the Hardware server information */
	    private String hwsvr;

	    private String inads;
	    
	    private String ipadd;
	    
	    private String isDOCR;
	    
	    private String LHN;

	    private String login;

	    private String mid;

	    private String mcode;

	    private int oalid;

	    private String ossno;
	    
	    private String prvip;
	    
	    private String pswds;

	    private String procserialno;
	    
	    private String rtschedname;
	    
	    private String sid;
	    
	    /* This element contains the SEID */
	    private String sname;
	    
	    /* This element holds the template name for SAL Products */
	    private String template;

	    private String tr2600Port;
	    
	    private String tr2600Pswd;
	    
	    private String vpmsldn;
	    
	    private String prodtype;
	    
	    private String scode;
	    
	    /* This holds the Operation type information.DN for DB only New,
	     * FN for Full New, DU for DB only update, NU for Full Update, PR for
	     * IPO password reset,RN for IPO onboarding file regeneration. */
	    private String optype;

	    /* This element holds the sold to information */
	    private String fl;
	    
	    private String usrid;
	    
	    /* This element holds the grt id */
	    private String grtid;
	    
	    /* This element holds the alarm origination information (Y/N) */
	    private String aorig;

	    private String rndpwd;
	    
	    private String relno;
	    
	    private String nickname;
	    
	    private String svrname;

	    private String techRegDetail, equipmentNumber, summaryEquipmentNumber, mainSeid, failedSeid, groupId, techRegId;
	    
	    private String username, password;
	    
	    private ParametersType parameters;
	    
	    private Param param;
	    
	    private String gatewaySEID;
	    
	    private String accessType;
	    
	    
		public String getAfid() {
			return afid;
		}

		public void setAfid(String afid) {
			this.afid = afid;
		}

		public String getAorig() {
			return aorig;
		}

		public void setAorig(String aorig) {
			this.aorig = aorig;
		}

		public String getCustipaddress() {
			return custipaddress;
		}

		public void setCustipaddress(String custipaddress) {
			if(StringUtils.isNotEmpty(custipaddress)){
				this.custipaddress = custipaddress.trim();
			} else {
				this.custipaddress = custipaddress;
			}
		}

		public String getDatalock() {
			return datalock;
		}

		public void setDatalock(String datalock) {
			this.datalock = datalock;
		}

		public String getDefaultpid() {
			return defaultpid;
		}

		public void setDefaultpid(String defaultpid) {
			this.defaultpid = defaultpid;
		}

		public String getDossno() {
			return dossno;
		}

		public void setDossno(String dossno) {
			this.dossno = dossno;
		}

		public String getDtype() {
			return dtype;
		}

		public void setDtype(String dtype) {
			this.dtype = dtype;
		}

		public String getFoption() {
			return foption;
		}

		public void setFoption(String foption) {
			this.foption = foption;
		}

		public String getGrtid() {
			return grtid;
		}

		public void setGrtid(String grtid) {
			this.grtid = grtid;
		}

		public String getHwsvr() {
			return hwsvr;
		}

		public void setHwsvr(String hwsvr) {
			this.hwsvr = hwsvr;
		}

		public String getInads() {
			return inads;
		}

		public void setInads(String inads) {
			this.inads = inads;
		}

		public String getIpadd() {
			return ipadd;
		}

		public void setIpadd(String ipadd) {
			this.ipadd = ipadd;
		}

		public String getIsDOCR() {
			return isDOCR;
		}

		public void setIsDOCR(String isDOCR) {
			this.isDOCR = isDOCR;
		}

		public String getLHN() {
			return LHN;
		}

		public void setLHN(String lhn) {
			LHN = lhn;
		}

		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		public String getMcode() {
			return mcode;
		}

		public void setMcode(String mcode) {
			this.mcode = mcode;
		}

		
		public String getOptype() {
			return optype;
		}

		public void setOptype(String optype) {
			this.optype = optype;
		}

		public String getOssno() {
			return ossno;
		}

		public void setOssno(String ossno) {
			this.ossno = ossno;
		}

		public String getProcserialno() {
			return procserialno;
		}

		public void setProcserialno(String procserialno) {
			this.procserialno = procserialno;
		}

		public String getProdtype() {
			return prodtype;
		}

		public void setProdtype(String prodtype) {
			this.prodtype = prodtype;
		}

		public String getPrvip() {
			return prvip;
		}

		public void setPrvip(String prvip) {
			this.prvip = prvip;
		}

		public String getPswds() {
			return pswds;
		}

		public void setPswds(String pswds) {
			this.pswds = pswds;
		}

		public String getRelno() {
			return relno;
		}

		public void setRelno(String relno) {
			this.relno = relno;
		}

		public String getRndpwd() {
			return rndpwd;
		}

		public void setRndpwd(String rndpwd) {
			this.rndpwd = rndpwd;
		}

		public String getRtschedname() {
			return rtschedname;
		}

		public void setRtschedname(String rtschedname) {
			this.rtschedname = rtschedname;
		}

		public String getScode() {
			return scode;
		}

		public void setScode(String scode) {
			this.scode = scode;
		}

		public String getSname() {
			return sname;
		}

		public void setSname(String sname) {
			this.sname = sname;
		}

		public String getTemplate() {
			return template;
		}

		public void setTemplate(String template) {
			this.template = template;
		}

		public String getTr2600Port() {
			return tr2600Port;
		}

		public void setTr2600Port(String tr2600Port) {
			this.tr2600Port = tr2600Port;
		}

		public String getTr2600Pswd() {
			return tr2600Pswd;
		}

		public void setTr2600Pswd(String tr2600Pswd) {
			this.tr2600Pswd = tr2600Pswd;
		}

		public String getVpmsldn() {
			return vpmsldn;
		}

		public void setVpmsldn(String vpmsldn) {
			this.vpmsldn = vpmsldn;
		}

		public String getSystemIdentifier() {
			return systemIdentifier;
		}

		public void setSystemIdentifier(String systemIdentifier) {
			this.systemIdentifier = systemIdentifier;
		}

		public String getFl() {
			return fl;
		}

		public void setFl(String fl) {
			this.fl = fl;
		}
		
		public String getAlmid() {
			return almid;
		}

		public void setAlmid(String almid) {
			this.almid = almid;
		}

		public String getMid() {
			return mid;
		}

		public void setMid(String mid) {
			this.mid = mid;
		}

		public int getOalid() {
			return oalid;
		}

		public void setOalid(int oalid) {
			this.oalid = oalid;
		}

		public String getSid() {
			return sid;
		}

		public void setSid(String sid) {
			this.sid = sid;
		}

		public String getUsrid() {
			return usrid;
		}

		public void setUsrid(String usrid) {
			this.usrid = usrid;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public String getSvrname() {
			return svrname;
		}

		public void setSvrname(String svrname) {
			this.svrname = svrname;
		}

		public String getEquipmentNumber() {
			return equipmentNumber;
		}

		public void setEquipmentNumber(String equipmentNumber) {
			this.equipmentNumber = equipmentNumber;
		}

		public String getSummaryEquipmentNumber() {
			return summaryEquipmentNumber;
		}

		public void setSummaryEquipmentNumber(String summaryEquipmentNumber) {
			this.summaryEquipmentNumber = summaryEquipmentNumber;
		}

		public String getTechRegDetail() {
			return techRegDetail;
		}

		public void setTechRegDetail(String techRegDetail) {
			this.techRegDetail = techRegDetail;
		}

		public String getFailedSeid() {
			return failedSeid;
		}

		public void setFailedSeid(String failedSeid) {
			this.failedSeid = failedSeid;
		}

		public String getMainSeid() {
			return mainSeid;
		}

		public void setMainSeid(String mainSeid) {
			this.mainSeid = mainSeid;
		}

		public String getGroupId() {
			return groupId;
		}

		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}

		public String getTechRegId() {
			return techRegId;
		}

		public void setTechRegId(String techRegId) {
			this.techRegId = techRegId;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}
		
		public String getAccessType() {
			return accessType;
		}

		public void setAccessType(String accessType) {
			this.accessType = accessType;
		}

		public String getGatewaySEID() {
			return gatewaySEID;
		}

		public void setGatewaySEID(String gatewaySEID) {
			this.gatewaySEID = gatewaySEID;
		}

		public ParametersType getParameters() {
			if (this.parameters == null) {
				this.parameters = new ParametersType();
			}
			return this.parameters;
		}

		public void setParameters(ParametersType parameters) {
			this.parameters = parameters;
		}

		public class ParametersType implements Serializable {
			private Param[] param;

			public Param[] getParam() {
				return this.param;
			}

			public void setParam(Param[] param) {
				this.param = param;
			}
		}
		

		public Param getParam() {
			if (this.param == null) {
				this.param = new Param();
			}
			return this.param;
		}

		public void setParam(Param param) {
			this.param = param;
		}
		
		public class Param implements Serializable {
			private String key;
			private String value;

			public String getKey() {
				return this.key;
			}

			public void setKey(String key) {
				this.key = key;
			}

			public String getValue() {
				return this.value;
			}

			public void setValue(String value) {
				this.value = value;
			}
		}


}
