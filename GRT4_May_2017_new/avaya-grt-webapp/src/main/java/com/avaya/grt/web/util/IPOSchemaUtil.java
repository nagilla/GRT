package com.avaya.grt.web.util;

import java.util.ArrayList;
import java.util.List;

import com.avaya.grt.service.iposs.Response;
import com.avaya.grt.service.iposs.Response.Data.Inventory.Hardware.Modules;
import com.avaya.grt.service.iposs.Response.Data.Inventory.Hardware.SDCard;
import com.avaya.grt.service.iposs.Response.Data.Inventory.Hardware.Units;
import com.avaya.grt.service.iposs.Response.Data.Inventory.Hardware.Modules.Module;
import com.avaya.grt.service.iposs.Response.Data.Inventory.Hardware.Units.Unit;
import com.avaya.grt.service.iposs.Response.Data.Inventory.Licenses.License;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.grt.dto.TechnicalOrderDetail;

public class IPOSchemaUtil {
	private static final Logger logger = Logger.getLogger(IPOSchemaUtil.class);
	
	public String getVersionFromInventoryXML(Document document) throws Exception{
		Node node = null;
		String version = "";
		NodeList nodeList = document.getChildNodes();
    	node = nodeList.item(0);
    	logger.debug("Node :"+node.getNodeName());
    	if(node.getNodeName().equalsIgnoreCase("response")){
    		NodeList responseChildList = node.getChildNodes();
    		//logger.debug("responseChildList Length:"+responseChildList.getLength());
    		for(int i = 0; i < responseChildList.getLength(); i++){
    			node = responseChildList.item(i);
    			logger.debug("responseChildList Node :"+node.getNodeName());
	    		if(node.getNodeName().equalsIgnoreCase("header")){
	    			NodeList headerChildList = node.getChildNodes();
	    			//logger.debug("headerChildList Length:"+headerChildList.getLength());
	    			for(int j = 0; j < headerChildList.getLength(); j++){
	    				node = headerChildList.item(j);
    	    			logger.debug("headerChildList Node :"+node.getNodeName());
    	    			if(node.getNodeName().equalsIgnoreCase("version")){
    	    				version = node.getLastChild().getNodeValue();
    	    			}
	    			}
	    			break;
	    		}
    		}
    	}
    	return version;
	}
	
	/**
	 * 9.0 schema
	 * @param lib
	 * @param isAutoTR
	 * @return
	 */
	public List<TechnicalOrderDetail> getInventoryXMLDetails(com.avaya.grt.service.iposs.ipoInventory9.Response lib, boolean isAutoTR) {
		List<TechnicalOrderDetail> bucket = new ArrayList<TechnicalOrderDetail>();
		TechnicalOrderDetail tod;
		int numOfUnits = lib.getData().getInventory().getHardware().getUnits()
				.getUnit().size();
		if (numOfUnits > 0) {
			com.avaya.grt.service.iposs.ipoInventory9.Response.Data.Inventory.Hardware.Units allUnits = lib.getData().getInventory().getHardware().getUnits();
			for (int i = 0; i < numOfUnits; i++) {
				com.avaya.grt.service.iposs.ipoInventory9.Response.Data.Inventory.Hardware.Units.Unit aUnit = allUnits.getUnit().get(i);
				if (aUnit.getCode() != null
						&& !aUnit.getCode().trim().equals("")) {
					tod = new TechnicalOrderDetail();
					if (aUnit.getDeviceNumber() == 1) {
						tod.setIsBaseUnit("Y");
						logger.debug("Setting AutoTR to:" + isAutoTR);
						tod.setAutoTR(isAutoTR);
						tod.setSerialNumber(aUnit.getSerialNumber());
						if(StringUtils.isNotEmpty(lib.getHeader().getVersion())) {
							tod.setReleaseString(lib.getHeader().getVersion());
						} else {
							tod.setReleaseString(aUnit.getVersionMajor() + "."
									+ aUnit.getVersionMinor());
						}
					} else {
						tod.setReleaseString(aUnit.getVersionMajor() + "."
								+ aUnit.getVersionMinor());
					}
					tod.setMaterialCode(aUnit.getCode());
					// tod.setDescription(aUnit.getFullDescription());

					//tod.setReleaseString(aUnit.getVersionMajor() + "."
					//		+ aUnit.getVersionMinor());
					tod.setInitialQuantity((long) 1);
					tod.setSelectforRegistration(true);
					tod.setIsSourceIPO("Y");
					tod.setIpoFlagIbaseJsp(true);
					bucket.add(tod);
				}
			}
		}
		int numOfModules = lib.getData().getInventory().getHardware()
				.getModules().getModule().size();
		if (numOfModules > 0) {
			com.avaya.grt.service.iposs.ipoInventory9.Response.Data.Inventory.Hardware.Modules allmodules = lib.getData().getInventory().getHardware()
					.getModules();
			for (int i = 0; i < numOfModules; i++) {
				com.avaya.grt.service.iposs.ipoInventory9.Response.Data.Inventory.Hardware.Modules.Module aModule = allmodules.getModule().get(i);
				if(aModule.getCode() != null && !aModule.getCode().trim().equals("")){
					tod = new TechnicalOrderDetail();
					if (aModule.getDeviceNumber() == 1) {
						tod.setIsBaseUnit("Y");
						tod.setSerialNumber(aModule.getSerialNumber());
					}

					tod.setInitialQuantity((long) 1);
					tod.setReleaseString(aModule.getVersionMajor() + "."
							+ aModule.getVersionMinor());
					tod.setSelectforRegistration(true);
					tod.setIsSourceIPO("Y");
					tod.setIpoFlagIbaseJsp(true);
					tod.setMaterialCode(aModule.getCode());
					// tod.setDescription(aModule.getFullDescription());
					bucket.add(tod);
				}
				if(aModule.getCodeBaseCard() != null && !aModule.getCodeBaseCard().trim().equals("")){
					tod = new TechnicalOrderDetail();
					if (aModule.getDeviceNumber() == 1) {
						tod.setIsBaseUnit("Y");
						tod.setSerialNumber(aModule.getSerialNumber());
					}

					tod.setInitialQuantity((long) 1);
					tod.setReleaseString(aModule.getVersionMajor() + "."
							+ aModule.getVersionMinor());
					tod.setSelectforRegistration(true);
					tod.setIsSourceIPO("Y");
					tod.setIpoFlagIbaseJsp(true);
					tod.setMaterialCode(aModule.getCodeBaseCard());
					// tod.setDescription(aModule.getBaseCardFullDescription());
					bucket.add(tod);
				}
				if(aModule.getCodeTrunkCard() != null && !aModule.getCodeTrunkCard().trim().equals("")){
					tod = new TechnicalOrderDetail();
					if (aModule.getDeviceNumber() == 1) {
						tod.setIsBaseUnit("Y");
						tod.setSerialNumber(aModule.getSerialNumber());
					}

					tod.setInitialQuantity((long) 1);
					tod.setReleaseString(aModule.getVersionMajor() + "."
							+ aModule.getVersionMinor());
					tod.setSelectforRegistration(true);
					tod.setIsSourceIPO("Y");
					tod.setIpoFlagIbaseJsp(true);
					tod.setMaterialCode(aModule.getCodeTrunkCard());
					// tod.setDescription(aModule.getTrunkCardFullDescription());
					bucket.add(tod);
				}
			}
		}

		int numOfSDCards = lib.getData().getInventory().getHardware()
				.getSDCard().size();
		if (numOfSDCards > 0) {
			List<com.avaya.grt.service.iposs.ipoInventory9.Response.Data.Inventory.Hardware.SDCard> allSDCards = lib.getData().getInventory()
					.getHardware().getSDCard();
			for (int i = 0; i < allSDCards.size(); i++) {
				com.avaya.grt.service.iposs.ipoInventory9.Response.Data.Inventory.Hardware.SDCard aSDCard = allSDCards.get(i);
				if (aSDCard.getCode() != null
						&& !aSDCard.getCode().trim().equals("")) {
					tod = new TechnicalOrderDetail();
					tod.setMaterialCode(aSDCard.getCode());
					tod.setInitialQuantity((long) 1);
					//tod.setSerialNumber(Long.toString(aSDCard.getSerialNumber()));
					tod.setSelectforRegistration(true);
					tod.setIsSourceIPO("Y");
					tod.setIpoFlagIbaseJsp(true);
					bucket.add(tod);
				}
			}
		}

		int numOfLicences = lib.getData().getInventory().getLicenses()
				.getLicense().size();
		if (numOfLicences > 0) {
			List<com.avaya.grt.service.iposs.ipoInventory9.Response.Data.Inventory.Licenses.License> allLicences = lib.getData().getInventory()
					.getLicenses().getLicense();
			for (int i = 0; i < allLicences.size(); i++) {
				com.avaya.grt.service.iposs.ipoInventory9.Response.Data.Inventory.Licenses.License aLicense = allLicences.get(i);
				if (aLicense.getCode() != null
						&& !aLicense.getCode().trim().equals("")) {
					tod = new TechnicalOrderDetail();
					tod.setMaterialCode(aLicense.getCode());
					tod.setInitialQuantity((long) 1);
					// tod.setDescription(aLicense.getTypeDescription());
					tod.setSelectforRegistration(true);
					tod.setIsSourceIPO("Y");
					tod.setIpoFlagIbaseJsp(true);
					bucket.add(tod);
				}
			}
		}
		return bucket;
	}

	/**
	 * 8.1 Schema
	 * @param lib
	 * @param isAutoTR
	 * @return
	 */
	public List<TechnicalOrderDetail> getInventoryXMLDetails(Response lib, boolean isAutoTR) {
		List<TechnicalOrderDetail> bucket = new ArrayList<TechnicalOrderDetail>();
		TechnicalOrderDetail tod;
		int numOfUnits = lib.getData().getInventory().getHardware().getUnits()
					.getUnit().size();
		if (numOfUnits > 0) {
			Units allUnits = lib.getData().getInventory().getHardware()
						.getUnits();
			for (int i = 0; i < numOfUnits; i++) {
				Unit aUnit = allUnits.getUnit().get(i);
				if (aUnit.getCode() != null
						&& !aUnit.getCode().trim().equals("")) {
					tod = new TechnicalOrderDetail();
					if (aUnit.getDeviceNumber() == 1) {
						tod.setIsBaseUnit("Y");
						tod.setAutoTR(isAutoTR);
						tod.setSerialNumber(aUnit.getSerialNumber());
						if(StringUtils.isNotEmpty(lib.getHeader().getVersion())) {
							tod.setReleaseString(lib.getHeader().getVersion());
						} else {
							tod.setReleaseString(aUnit.getVersionMajor() + "."
									+ aUnit.getVersionMinor());
						}
					} else {
						tod.setReleaseString(aUnit.getVersionMajor() + "."
								+ aUnit.getVersionMinor());
					}
					tod.setMaterialCode(aUnit.getCode());
					// tod.setDescription(aUnit.getFullDescription());

					//tod.setReleaseString(aUnit.getVersionMajor() + "."
					//		+ aUnit.getVersionMinor());
					tod.setInitialQuantity((long) 1);
					tod.setSelectforRegistration(true);
					tod.setIsSourceIPO("Y");
					tod.setIpoFlagIbaseJsp(true);
					bucket.add(tod);
				}
			}
		}
		int numOfModules = lib.getData().getInventory().getHardware()
				.getModules().getModule().size();
		if (numOfModules > 0) {
			Modules allmodules = lib.getData().getInventory().getHardware()
					.getModules();
			for (int i = 0; i < numOfModules; i++) {
				Module aModule = allmodules.getModule().get(i);
				if(aModule.getCodeBaseCard() != null && !aModule.getCodeBaseCard().trim().equals("")){
					tod = new TechnicalOrderDetail();
					if (aModule.getDeviceNumber() == 1) {
						tod.setIsBaseUnit("Y");
						tod.setSerialNumber(aModule.getSerialNumber());
					}

					tod.setInitialQuantity((long) 1);
					tod.setReleaseString(aModule.getVersionMajor() + "."
							+ aModule.getVersionMinor());
					tod.setSelectforRegistration(true);
					tod.setIsSourceIPO("Y");
					tod.setIpoFlagIbaseJsp(true);
					tod.setMaterialCode(aModule.getCodeBaseCard());
					// tod.setDescription(aModule.getBaseCardFullDescription());
					bucket.add(tod);
				} 
				if(aModule.getCodeTrunkCard() != null && !aModule.getCodeTrunkCard().trim().equals("")){
					tod = new TechnicalOrderDetail();
					if (aModule.getDeviceNumber() == 1) {
						tod.setIsBaseUnit("Y");
						tod.setSerialNumber(aModule.getSerialNumber());
					}

					tod.setInitialQuantity((long) 1);
					tod.setReleaseString(aModule.getVersionMajor() + "."
							+ aModule.getVersionMinor());
					tod.setSelectforRegistration(true);
					tod.setIsSourceIPO("Y");
					tod.setIpoFlagIbaseJsp(true);
					tod.setMaterialCode(aModule.getCodeTrunkCard());
					// tod.setDescription(aModule.getTrunkCardFullDescription());
					bucket.add(tod);
				}
			}
		}

		int numOfSDCards = lib.getData().getInventory().getHardware()
				.getSDCard().size();
		if (numOfSDCards > 0) {
			List<SDCard> allSDCards = lib.getData().getInventory()
					.getHardware().getSDCard();
			for (int i = 0; i < allSDCards.size(); i++) {
				SDCard aSDCard = allSDCards.get(i);
				if (aSDCard.getCode() != null
						&& !aSDCard.getCode().trim().equals("")) {
					tod = new TechnicalOrderDetail();
					tod.setMaterialCode(aSDCard.getCode());
					tod.setInitialQuantity((long) 1);
					//tod.setSerialNumber(Long.toString(aSDCard.getSerialNumber()));
					tod.setSelectforRegistration(true);
					tod.setIsSourceIPO("Y");
					tod.setIpoFlagIbaseJsp(true);
					bucket.add(tod);
				}
			}
		}

		int numOfLicences = lib.getData().getInventory().getLicenses()
				.getLicense().size();
		if (numOfLicences > 0) {
			List<License> allLicences = lib.getData().getInventory()
					.getLicenses().getLicense();
			for (int i = 0; i < allLicences.size(); i++) {
				License aLicense = allLicences.get(i);
				if (aLicense.getCode() != null
						&& !aLicense.getCode().trim().equals("")) {
					tod = new TechnicalOrderDetail();
					tod.setMaterialCode(aLicense.getCode());
					tod.setInitialQuantity((long) 1);
					// tod.setDescription(aLicense.getTypeDescription());
					tod.setSelectforRegistration(true);
					tod.setIsSourceIPO("Y");
					tod.setIpoFlagIbaseJsp(true);
					bucket.add(tod);
				}
			}
		}
		return bucket;
	}
	
	/**
	 * 9.1 Schema
	 * @param lib
	 * @param isAutoTR
	 * @return
	 */
	public List<TechnicalOrderDetail> getInventoryXMLDetails(com.avaya.grt.service.iposs.ipoInventory91.Response lib, boolean isAutoTR) {
		List<TechnicalOrderDetail> bucket = new ArrayList<TechnicalOrderDetail>();
		TechnicalOrderDetail tod;
		int numOfUnits = lib.getData().getInventory().getHardware().getUnits()
				.getUnit().size();
		if (numOfUnits > 0) {
			com.avaya.grt.service.iposs.ipoInventory91.Response.Data.Inventory.Hardware.Units allUnits = lib.getData().getInventory().getHardware().getUnits();
			for (int i = 0; i < numOfUnits; i++) {
				com.avaya.grt.service.iposs.ipoInventory91.Response.Data.Inventory.Hardware.Units.Unit aUnit = allUnits.getUnit().get(i);
				if (aUnit.getCode() != null
						&& !aUnit.getCode().trim().equals("")) {
					tod = new TechnicalOrderDetail();
					if (aUnit.getDeviceNumber() == 1) {
						tod.setIsBaseUnit("Y");
						logger.debug("Setting AutoTR to:" + isAutoTR);
						tod.setAutoTR(isAutoTR);
						tod.setSerialNumber(aUnit.getSerialNumber());
						if(StringUtils.isNotEmpty(lib.getHeader().getVersion())) {
							tod.setReleaseString(lib.getHeader().getVersion());
						} else {
							tod.setReleaseString(aUnit.getVersionMajor() + "."
									+ aUnit.getVersionMinor());
						}
					} else {
						tod.setReleaseString(aUnit.getVersionMajor() + "."
								+ aUnit.getVersionMinor());
					}
					tod.setMaterialCode(aUnit.getCode());
					// tod.setDescription(aUnit.getFullDescription());

					//tod.setReleaseString(aUnit.getVersionMajor() + "."
					//		+ aUnit.getVersionMinor());
					tod.setInitialQuantity((long) 1);
					tod.setSelectforRegistration(true);
					tod.setIsSourceIPO("Y");
					tod.setIpoFlagIbaseJsp(true);
					bucket.add(tod);
				}
			}
		}
		int numOfModules = lib.getData().getInventory().getHardware()
				.getModules().getModule().size();
		if (numOfModules > 0) {
			com.avaya.grt.service.iposs.ipoInventory91.Response.Data.Inventory.Hardware.Modules allmodules = lib.getData().getInventory().getHardware()
					.getModules();
			for (int i = 0; i < numOfModules; i++) {
				com.avaya.grt.service.iposs.ipoInventory91.Response.Data.Inventory.Hardware.Modules.Module aModule = allmodules.getModule().get(i);
				if(aModule.getCode() != null && !aModule.getCode().trim().equals("")){
					tod = new TechnicalOrderDetail();
					if (aModule.getDeviceNumber() == 1) {
						tod.setIsBaseUnit("Y");
						tod.setSerialNumber(aModule.getSerialNumber());
					}

					tod.setInitialQuantity((long) 1);
					tod.setReleaseString(aModule.getVersionMajor() + "."
							+ aModule.getVersionMinor());
					tod.setSelectforRegistration(true);
					tod.setIsSourceIPO("Y");
					tod.setIpoFlagIbaseJsp(true);
					tod.setMaterialCode(aModule.getCode());
					// tod.setDescription(aModule.getFullDescription());
					bucket.add(tod);
				}
				if(aModule.getCodeBaseCard() != null && !aModule.getCodeBaseCard().trim().equals("")){
					tod = new TechnicalOrderDetail();
					if (aModule.getDeviceNumber() == 1) {
						tod.setIsBaseUnit("Y");
						tod.setSerialNumber(aModule.getSerialNumber());
					}

					tod.setInitialQuantity((long) 1);
					tod.setReleaseString(aModule.getVersionMajor() + "."
							+ aModule.getVersionMinor());
					tod.setSelectforRegistration(true);
					tod.setIsSourceIPO("Y");
					tod.setIpoFlagIbaseJsp(true);
					tod.setMaterialCode(aModule.getCodeBaseCard());
					// tod.setDescription(aModule.getBaseCardFullDescription());
					bucket.add(tod);
				}
				if(aModule.getCodeTrunkCard() != null && !aModule.getCodeTrunkCard().trim().equals("")){
					tod = new TechnicalOrderDetail();
					if (aModule.getDeviceNumber() == 1) {
						tod.setIsBaseUnit("Y");
						tod.setSerialNumber(aModule.getSerialNumber());
					}

					tod.setInitialQuantity((long) 1);
					tod.setReleaseString(aModule.getVersionMajor() + "."
							+ aModule.getVersionMinor());
					tod.setSelectforRegistration(true);
					tod.setIsSourceIPO("Y");
					tod.setIpoFlagIbaseJsp(true);
					tod.setMaterialCode(aModule.getCodeTrunkCard());
					// tod.setDescription(aModule.getTrunkCardFullDescription());
					bucket.add(tod);
				}
			}
		}

		int numOfSDCards = lib.getData().getInventory().getHardware()
				.getSDCard().size();
		if (numOfSDCards > 0) {
			List<com.avaya.grt.service.iposs.ipoInventory91.Response.Data.Inventory.Hardware.SDCard> allSDCards = lib.getData().getInventory()
					.getHardware().getSDCard();
			for (int i = 0; i < allSDCards.size(); i++) {
				com.avaya.grt.service.iposs.ipoInventory91.Response.Data.Inventory.Hardware.SDCard aSDCard = allSDCards.get(i);
				if (aSDCard.getCode() != null
						&& !aSDCard.getCode().trim().equals("")) {
					tod = new TechnicalOrderDetail();
					tod.setMaterialCode(aSDCard.getCode());
					tod.setInitialQuantity((long) 1);
					//tod.setSerialNumber(Long.toString(aSDCard.getSerialNumber()));
					tod.setSelectforRegistration(true);
					tod.setIsSourceIPO("Y");
					tod.setIpoFlagIbaseJsp(true);
					bucket.add(tod);
				}
			}
		}

		int numOfLicences = lib.getData().getInventory().getLicenses()
				.getLicense().size();
		if (numOfLicences > 0) {
			List<com.avaya.grt.service.iposs.ipoInventory91.Response.Data.Inventory.Licenses.License> allLicences = lib.getData().getInventory()
					.getLicenses().getLicense();
			for (int i = 0; i < allLicences.size(); i++) {
				com.avaya.grt.service.iposs.ipoInventory91.Response.Data.Inventory.Licenses.License aLicense = allLicences.get(i);
				if (aLicense.getCode() != null
						&& !aLicense.getCode().trim().equals("")) {
					tod = new TechnicalOrderDetail();
					tod.setMaterialCode(aLicense.getCode());
					tod.setInitialQuantity((long) 1);
					// tod.setDescription(aLicense.getTypeDescription());
					tod.setSelectforRegistration(true);
					tod.setIsSourceIPO("Y");
					tod.setIpoFlagIbaseJsp(true);
					bucket.add(tod);
				}
			}
		}
		return bucket;
	}
	
	/**
	 * 10.0 Schema
	 * @param lib
	 * @param isAutoTR
	 * @return
	 */
	public List<TechnicalOrderDetail> getInventoryXMLDetails(com.avaya.grt.service.iposs.ipoInventory10.Response lib, boolean isAutoTR) {
		List<TechnicalOrderDetail> bucket = new ArrayList<TechnicalOrderDetail>();
		TechnicalOrderDetail tod;
		int numOfUnits = lib.getData().getInventory().getHardware().getUnits()
				.getUnit().size();
		if (numOfUnits > 0) {
			com.avaya.grt.service.iposs.ipoInventory10.Response.Data.Inventory.Hardware.Units allUnits = lib.getData().getInventory().getHardware().getUnits();
			for (int i = 0; i < numOfUnits; i++) {
				com.avaya.grt.service.iposs.ipoInventory10.Response.Data.Inventory.Hardware.Units.Unit aUnit = allUnits.getUnit().get(i);
				if (aUnit.getCode() != null
						&& !aUnit.getCode().trim().equals("")) {
					tod = new TechnicalOrderDetail();
					if (aUnit.getDeviceNumber() == 1) {
						tod.setIsBaseUnit("Y");
						logger.debug("Setting AutoTR to:" + isAutoTR);
						tod.setAutoTR(isAutoTR);
						tod.setSerialNumber(aUnit.getSerialNumber());
						if(StringUtils.isNotEmpty(lib.getHeader().getVersion())) {
							tod.setReleaseString(lib.getHeader().getVersion());
						} else {
							tod.setReleaseString(aUnit.getVersionMajor() + "."
									+ aUnit.getVersionMinor());
						}
					} else {
						tod.setReleaseString(aUnit.getVersionMajor() + "."
								+ aUnit.getVersionMinor());
					}
					tod.setMaterialCode(aUnit.getCode());
					// tod.setDescription(aUnit.getFullDescription());

					//tod.setReleaseString(aUnit.getVersionMajor() + "."
					//		+ aUnit.getVersionMinor());
					tod.setInitialQuantity((long) 1);
					tod.setSelectforRegistration(true);
					tod.setIsSourceIPO("Y");
					tod.setIpoFlagIbaseJsp(true);
					bucket.add(tod);
				}
			}
		}
		int numOfModules = lib.getData().getInventory().getHardware()
				.getModules().getModule().size();
		if (numOfModules > 0) {
			com.avaya.grt.service.iposs.ipoInventory10.Response.Data.Inventory.Hardware.Modules allmodules = lib.getData().getInventory().getHardware()
					.getModules();
			for (int i = 0; i < numOfModules; i++) {
				com.avaya.grt.service.iposs.ipoInventory10.Response.Data.Inventory.Hardware.Modules.Module aModule = allmodules.getModule().get(i);
				if(aModule.getCode() != null && !aModule.getCode().trim().equals("")){
					tod = new TechnicalOrderDetail();
					if (aModule.getDeviceNumber() == 1) {
						tod.setIsBaseUnit("Y");
						tod.setSerialNumber(aModule.getSerialNumber());
					}

					tod.setInitialQuantity((long) 1);
					tod.setReleaseString(aModule.getVersionMajor() + "."
							+ aModule.getVersionMinor());
					tod.setSelectforRegistration(true);
					tod.setIsSourceIPO("Y");
					tod.setIpoFlagIbaseJsp(true);
					tod.setMaterialCode(aModule.getCode());
					// tod.setDescription(aModule.getFullDescription());
					bucket.add(tod);
				}
				if(aModule.getCodeBaseCard() != null && !aModule.getCodeBaseCard().trim().equals("")){
					tod = new TechnicalOrderDetail();
					if (aModule.getDeviceNumber() == 1) {
						tod.setIsBaseUnit("Y");
						tod.setSerialNumber(aModule.getSerialNumber());
					}

					tod.setInitialQuantity((long) 1);
					tod.setReleaseString(aModule.getVersionMajor() + "."
							+ aModule.getVersionMinor());
					tod.setSelectforRegistration(true);
					tod.setIsSourceIPO("Y");
					tod.setIpoFlagIbaseJsp(true);
					tod.setMaterialCode(aModule.getCodeBaseCard());
					// tod.setDescription(aModule.getBaseCardFullDescription());
					bucket.add(tod);
				}
				if(aModule.getCodeTrunkCard() != null && !aModule.getCodeTrunkCard().trim().equals("")){
					tod = new TechnicalOrderDetail();
					if (aModule.getDeviceNumber() == 1) {
						tod.setIsBaseUnit("Y");
						tod.setSerialNumber(aModule.getSerialNumber());
					}

					tod.setInitialQuantity((long) 1);
					tod.setReleaseString(aModule.getVersionMajor() + "."
							+ aModule.getVersionMinor());
					tod.setSelectforRegistration(true);
					tod.setIsSourceIPO("Y");
					tod.setIpoFlagIbaseJsp(true);
					tod.setMaterialCode(aModule.getCodeTrunkCard());
					// tod.setDescription(aModule.getTrunkCardFullDescription());
					bucket.add(tod);
				}
			}
		}

		int numOfSDCards = lib.getData().getInventory().getHardware()
				.getSDCard().size();
		if (numOfSDCards > 0) {
			List<com.avaya.grt.service.iposs.ipoInventory10.Response.Data.Inventory.Hardware.SDCard> allSDCards = lib.getData().getInventory()
					.getHardware().getSDCard();
			for (int i = 0; i < allSDCards.size(); i++) {
				com.avaya.grt.service.iposs.ipoInventory10.Response.Data.Inventory.Hardware.SDCard aSDCard = allSDCards.get(i);
				if (aSDCard.getCode() != null
						&& !aSDCard.getCode().trim().equals("")) {
					tod = new TechnicalOrderDetail();
					tod.setMaterialCode(aSDCard.getCode());
					tod.setInitialQuantity((long) 1);
					//tod.setSerialNumber(Long.toString(aSDCard.getSerialNumber()));
					tod.setSelectforRegistration(true);
					tod.setIsSourceIPO("Y");
					tod.setIpoFlagIbaseJsp(true);
					bucket.add(tod);
				}
			}
		}

		int numOfLicences = lib.getData().getInventory().getLicenses()
				.getLicense().size();
		if (numOfLicences > 0) {
			List<com.avaya.grt.service.iposs.ipoInventory10.Response.Data.Inventory.Licenses.License> allLicences = lib.getData().getInventory()
					.getLicenses().getLicense();
			for (int i = 0; i < allLicences.size(); i++) {
				com.avaya.grt.service.iposs.ipoInventory10.Response.Data.Inventory.Licenses.License aLicense = allLicences.get(i);
				if (aLicense.getCode() != null
						&& !aLicense.getCode().trim().equals("")) {
					tod = new TechnicalOrderDetail();
					tod.setMaterialCode(aLicense.getCode());
					tod.setInitialQuantity((long) 1);
					// tod.setDescription(aLicense.getTypeDescription());
					tod.setSelectforRegistration(true);
					tod.setIsSourceIPO("Y");
					tod.setIpoFlagIbaseJsp(true);
					bucket.add(tod);
				}
			}
		}
		return bucket;
	}
}

