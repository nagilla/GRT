package com.avaya.grt.web.action.ipossregistration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import com.avaya.grt.service.iposs.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.grt.dto.RegistrationQuestionsDetail;
import com.avaya.grt.web.action.TechnicalRegistrationAction;
import com.avaya.grt.web.action.recordvalidation.RecordValidationAction;
import com.avaya.grt.web.util.IPOSchemaUtil;
import com.grt.dto.TRConfig;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.GRTConstants;

public class IPOSSRegistrationAction extends TechnicalRegistrationAction {
	
	private static final Logger logger = Logger
			.getLogger(IPOSSRegistrationAction.class);
	private Boolean ipoContinueInstallBase = false;
	private Boolean saveAssistIPO = false;
	//private Assembler assembler = new Assembler();
	private IPOSchemaUtil ipoSchemaUtil;
	
	public String ipoRegistration() throws Exception{
		validateSession();
		actionForm.setIpoRegistration(true);
		actionForm.setFirstColumnOnly(true);
		if ("B".equals(getUserFromSession().getUserType())
				|| "C".equals(getUserFromSession().getUserType())) {
			return "toLocationSelection";
		} else {
			return "toAgentLocationSelection";
		}
	}
	
	public String saveSiteRegistration() throws Exception {
		logger.debug("Entering saveSiteRegistration()");
		validateSession();
		actionForm.setActveSRColumnOnly(false);
		// User comes from Home screen->EQR only
		actionForm.setEqrFileDownloaded(false);
		
		//boolean flag=false;
		if (actionForm.isIpoRegistration()) {
			//actionForm.setIpoInventoryNotLoaded(GRTConstants.YES);
			actionForm.setRemoteConnectivity(GRTConstants.YES);
			return "IPO";// actionForm.isTechnicalRegistrationOnly()
		} 
		return null;
	}
	
	public String ipoInstall() throws Exception, NoSuchFieldException {
		validateSession();
		// form.getIpoFile().getInputStream().
		/*File prcsFile = null;
		ipoContinueInstallBase = false;
		saveAssistIPO = false;
		Source schemaFile = null;
		String version = "";
		logger.debug("Type of Instl" + actionForm.getTypeOfInstallation());
		logger.debug("Type of indust" + actionForm.getIndustry());
		logger.debug("Type of emplythis" + actionForm.getEmployeesThisLocation());
		logger.debug("Type of eplyall" + actionForm.getEmployeesAllLocation());
		logger.debug("Type of totlloc" + actionForm.getTotalLocations());
		logger.debug("Type of remotecon" + actionForm.getRemoteConnectivity());
		logger.debug("IPO Acess Type" + actionForm.getIpoAccessType());
		logger.debug("Type of setscvrd" + actionForm.getSetsCovered());
		List<RegistrationQuestionsDetail> ListtoSet = assembler.assembleRegQstnsListFormBean(actionForm);
		System.out.println("Past the assembler for answers");
		actionForm.setRegQstnsDtl(ListtoSet);
		prcsFile = actionForm.getIpoFile();
		if(prcsFile.length()>0){
		logger.debug("File size" + prcsFile.length());
		actionForm.setIpoInventoryNotLoaded(GRTConstants.NO);
		logger.debug("contenttype NOT found" + new MimetypesFileTypeMap().getContentType(prcsFile));
		//String fileName = prcsFile.getFileName();
		String fileName = actionForm.getIpoFileFileName();
		String fileExtn  = getFileExtension(fileName);
		if (fileExtn != null && !fileExtn.equalsIgnoreCase("xml")){
    		this.getRequest().setAttribute("schemaError","Invalid Format,xml format Expected");
	        return "stay";
	    }

		//String allowedSize = GRTPropertiesUtil.getProperty("ipo_file_size");
		String allowedSize = "10485760";
		logger.debug("Fetched new size from grtproperties"+allowedSize);
		if (prcsFile.length() > Integer.parseInt(allowedSize))  {
			this.getRequest().setAttribute("schemaError",
					"Files greater than 10 MB cannot be processed");
			return "stay";
		}
		logger.debug("File size" + prcsFile.length());

		logger.debug("contenttype NOT found" + new MimetypesFileTypeMap().getContentType(prcsFile));
		final String W3C_XML_SCHEMA_NS_URI = "http://www.w3.org/2001/XMLSchema";
		DocumentBuilder parser;
		try {
			logger.debug("Trying to parse file");
			logger.debug("directory" + System.getProperty("user.dir"));

			logger.debug("dir1"
					+ getClass().getProtectionDomain().getCodeSource()
							.getLocation().getPath());
			parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputStream is = new FileInputStream(prcsFile.toString());
			logger.debug("Got input stream");
			InputStream is1 = null;
			InputStream is2 = null;
			InputStream is3 = null;
			
			Document document = parser.parse(is);
			version = getIpoSchemaUtil().getVersionFromInventoryXML(document);
			logger.debug("Parsed Stream..... Inventory File version:"+version);
			SchemaFactory factory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
			if(version.equals("9.1")){
				is3 = this.getClass().getResourceAsStream("sslvpn_inventory_9_1_0.xsd");
				schemaFile = new StreamSource(is3);
			} else if(version.equals("9.0")){
				is2 = this.getClass().getResourceAsStream("sslvpn_inventory_9_0_0.xsd");
				schemaFile = new StreamSource(is2);
			} else {
				is1 = this.getClass().getResourceAsStream("sslvpn_inventory_8_1_0.xsd");
				schemaFile = new StreamSource(is1);
			}
			Schema schema = factory.newSchema(schemaFile);
			Validator validator = schema.newValidator();
			logger.debug("Before validation");
			validator.validate(new DOMSource(document));
			logger.debug("After validation");
			boolean isAutoTR = false;
			if(StringUtils.isNotEmpty(actionForm.getRemoteConnectivity()) && actionForm.getRemoteConnectivity().equalsIgnoreCase(GRTConstants.YES)) {
				isAutoTR = true;
				if (actionForm.getIpoAccessType() != null && GRTConstants.IPO_ACCESS_TYPE_OTHER.equalsIgnoreCase(actionForm.getIpoAccessType())) {
					actionForm.setRemoteConnectivity(GRTConstants.NO);
					isAutoTR = false;
				}
				logger.debug("IPO Access Type:"+actionForm.getIpoAccessType()+"    isAutoTR:"+isAutoTR);
			}
			// InputStream is1 = new
			// FileInputStream(ClassPath.getResource(\sslvpn_inventory.xsd));
			
			// if(getRegistrationDelegate().validateInventory(is)){
			logger.debug("before JAXB");
			JAXBContext jc = null;
			Unmarshaller unmarshaller = null;
			if(version.equals("9.1")){
				jc = JAXBContext.newInstance("com.grt.ipoInventory91");
				logger.debug("before JAXB validation");
				unmarshaller = jc.createUnmarshaller();
				logger.debug("After JAXB");
				com.avaya.grt.service.iposs.ipoInventory91.Response lib = (com.avaya.grt.service.iposs.ipoInventory91.Response) unmarshaller.unmarshal(new FileInputStream(prcsFile.toString()));
				actionForm.setMaterialEntryList(getIpoSchemaUtil().getInventoryXMLDetails(lib, isAutoTR));
			} else if(version.equals("9.0")){
				jc = JAXBContext.newInstance("com.grt.ipoInventory9");
				logger.debug("before JAXB validation");
				unmarshaller = jc.createUnmarshaller();
				logger.debug("After JAXB");
				com.avaya.grt.service.iposs.ipoInventory9.Response lib = (com.avaya.grt.service.iposs.ipoInventory9.Response) unmarshaller.unmarshal(new FileInputStream(prcsFile.toString()));
				actionForm.setMaterialEntryList(getIpoSchemaUtil().getInventoryXMLDetails(lib, isAutoTR));
			} else {
				jc = JAXBContext.newInstance("jaxbClasses");
				logger.debug("before JAXB validation");
				unmarshaller = jc.createUnmarshaller();
				logger.debug("After JAXB");
				com.avaya.grt.service.iposs.Response lib = (com.avaya.grt.service.iposs.Response) unmarshaller.unmarshal(new FileInputStream(prcsFile.toString()));
				actionForm.setMaterialEntryList(getIpoSchemaUtil().getInventoryXMLDetails(lib, isAutoTR));
			}
			logger.debug("Finished parse file got lib");
			
			

			logger.debug("Finished populatiing XML details"
					+ actionForm.getMaterialEntryList().size());
			try {
				actionForm.setMaterialEntryList(validateMaterialCodes(actionForm
						.getMaterialEntryList()));
				if(!actionForm.getMaterialEntryList().isEmpty()){
					actionForm.setSelectAndUnselectAllMeterial(true);
				}
				logger.debug("Finished getting desc"
						+ actionForm.getMaterialEntryList().size());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.debug("size" + actionForm.getMaterialEntryList().size());
			// }
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			logger.debug("ParserConfigurationException");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.debug("FileNotFoundException");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.debug("IOException");
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			logger.error("SAX Exception validating schema",e);
		//	e.printStackTrace();
			this.getRequest().setAttribute("schemaError",
					"Invalid Format,Please Upload the Correct Inventory File");
			return "stay";
		} catch (JAXBException e) {
			logger.debug("JAXB Exception Exception validating schema");
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			 * } catch (DataAccessException e) { // TODO Auto-generated catch
			 * block e.printStackTrace(); } catch (CreateException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); }
			 
		}
		} else if(actionForm.getIpoInventoryNotLoaded() == null || !GRTConstants.NO.equalsIgnoreCase(actionForm.getIpoInventoryNotLoaded())){
			actionForm.setIpoInventoryNotLoaded(GRTConstants.YES);
		}
		// file.
		getRequest().getSession().setAttribute("IPOR","IPOR");

		getRequest().getSession().setAttribute("IBBack","ipreg");
		actionForm.setTypeOfImp("5");
		actionForm.setSaveSiteReg(true);*/
		actionForm.setInstallbaseRegistrationOnly(true);
		return "success";
	}
	
	private String getFileExtension(String fName){
        int mid= fName.lastIndexOf(".");
        String ext=fName.substring(mid+1,fName.length());
        return ext;
    }
	
	public IPOSchemaUtil getIpoSchemaUtil() {
		if(ipoSchemaUtil == null){
			ipoSchemaUtil = new IPOSchemaUtil();
		}
		return ipoSchemaUtil;
	}
	
	private List<TechnicalOrderDetail> validateMaterialCodes(
			List<TechnicalOrderDetail> result) throws Exception {
		List<String> materialCodes = new ArrayList<String>();
		for (TechnicalOrderDetail detail : result) {
			if(detail.getMaterialCode()!= null && StringUtils.isNotEmpty(detail.getMaterialCode()) ){
				materialCodes.add(detail.getMaterialCode());
			}
		}
		if (!materialCodes.isEmpty()) {
			/*Map<String, String> mDescMap = getRegistrationDelegate()
					.getMaterialCodeDescription(materialCodes);*/
			Map<String, String> mDescMap = getInstallBaseService()
			.getMaterialCodeDesc(materialCodes);

			// [AVAYA] GRT2.1: Validation of material code against
			// MATERIAL_EXCLUSION table (Start)

			Map<String, Object> map = getInstallBaseService()
					.validateMaterialExclusion(materialCodes);
			for (TechnicalOrderDetail detail : result) {
				String desc = mDescMap.get(detail.getMaterialCode());
				logger.debug("DECRIPTION" + desc);

				detail.setDescription(desc);

//				 Is TR Eligible
				List<TRConfig> trEligibleList = getInstallBaseService().isTREligible(detail.getMaterialCode());
				if(trEligibleList != null && trEligibleList.size() > 0){
					detail.setTechnicallyRegisterable("Y");
				} else {
					detail.setTechnicallyRegisterable("");
				}
				logger.debug(detail.getMaterialExclusion());
				if (map != null && map.size() > 0) {
					if (map.containsKey(detail.getMaterialCode())) {
						detail.setMaterialExclusion("true");
						logger.debug("MaterialExclusion"
								+ detail.getMaterialExclusion());
						detail.setExclusionSource((String) map.get(detail.getMaterialCode()) );
					} else {
						detail.setMaterialExclusion("false");
						logger.debug("MaterialExclusion"
								+ detail.getMaterialExclusion());
						detail.setExclusionSource(null);
					}
				}else{
					detail.setMaterialExclusion("false");
					logger.debug("Material Excluded is zero"+detail.getMaterialExclusion());
				}
			}
		}
		logger.debug("size after validating code" + result.size());

		return result;
	}
	
	@Override
	public String technicalRegistrationDashboard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String installBaseCreation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String salGatewayMigrationList() {
		// TODO Auto-generated method stub
		return null;
	}
}
