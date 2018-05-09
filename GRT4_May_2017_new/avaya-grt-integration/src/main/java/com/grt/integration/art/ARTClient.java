package com.grt.integration.art;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.Stub;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.avaya.acsbi.ACSBISIDQueryRequest;
import com.avaya.acsbi.ACSBISIDQueryResponse;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.avaya.registration.AccessType;
import com.avaya.registration.DeviceTOBRequestType;
import com.avaya.registration.DeviceTOBResponseType;
import com.avaya.registration.DeviceType;
import com.avaya.registration.DevicesType;
import com.avaya.registration.HeaderType;
import com.avaya.registration.Operation;
import com.avaya.registration.ResponseHeaderType;
import com.avaya.registration.trsyncws.ARTResponseType;
import com.avaya.registration.trsyncws.OutputType;
import com.avaya.registration.trsyncws.ParameterType;
import com.avaya.registration.trsyncws.Parameters;
import com.avaya.registration.trsyncws.ProductDetailsType;
import com.avaya.registration.trsyncws.RegResultType;
import com.avaya.registration.trsyncws.TRRequestType;
import com.avaya.registration.trsyncws.TRResponseType;
import com.avaya.schema.registration.afsproxyws.AssetInfo;
import com.avaya.schema.registration.afsproxyws.SIDMIDInfo;
import com.avaya.v1.salconcentrator.Device_Element;
import com.avaya.v1.salconcentrator.Device_Element1;
import com.avaya.v1.salconcentrator.ManagedDevices;
import com.avaya.v1.salconcentrator.SALDeviceQueryRequest;
import com.avaya.v1.salconcentrator.SALDeviceQueryResponse;
import com.avaya.v1.salconcentrator.SEID;
import com.avaya.v1.salconcentrator.clientproxy.SALConcentratorWSDL_PortType;
import com.avaya.v1.salconcentrator.clientproxy.SALConcentratorWSDL_Service_Impl;
import com.avaya.v2.techregistration.Aorig;
import com.avaya.v2.techregistration.Foption;
import com.avaya.v2.techregistration.IsDOCR;
import com.avaya.v2.techregistration.Optype;
import com.avaya.v2.techregistration.Rndpwd;
import com.avaya.v2.techregistration.TechRegAcknowledgementType;
import com.avaya.v2.techregistration.TechRegDataType;
import com.avaya.v2.techregistration.TechRegRequestType;
import com.avaya.v2.techregistration.TechRegRespType;
import com.grt.dto.ManagedDevice;
import com.grt.dto.SALGatewayInstallerDto;
import com.grt.dto.SALGatewayIntrospection;
import com.grt.dto.TechRegAlarmInputDto;
import com.grt.dto.TechRegInputDto;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.integration.acsbi.fmwclientproxy.EPNSurveySoap;
import com.grt.integration.acsbi.fmwclientproxy.EPNSurvey_Impl;
import com.grt.integration.acsbi.sid.fmwclientproxy.ACSBISIDQueryWSDL_PortType;
import com.grt.integration.acsbi.sid.fmwclientproxy.ACSBISIDQueryWSDL_Service_Impl;
import com.grt.integration.art.afidclientproxy.GRTAFID_PortType;
import com.grt.integration.art.afidclientproxy.GRTAFID_Service_Impl;
import com.grt.integration.art.salgw.ExternalTRSynchWSDL_PortType;
import com.grt.integration.art.salgw.ExternalTRSynchWSDL_Service_Impl;
import com.grt.integration.art.sidmidclientproxy.GRTSIDMID_PortType;
import com.grt.integration.art.sidmidclientproxy.GRTSIDMID_Service_Impl;
import com.grt.integration.art.v2.fmwclientproxy.ExternalTechRegRequestWSDL_PortType;
import com.grt.integration.art.v2.fmwclientproxy.ExternalTechRegRequestWSDL_Service_Impl;
import com.grt.integration.art.v4.fmwclientproxy.ExternalTOBTechRegRequestWSDL_PortType;
import com.grt.integration.art.v4.fmwclientproxy.ExternalTOBTechRegRequestWSDL_Service_Impl;
import com.grt.integration.eqm.siebel.assetupdate.BOC;
import com.grt.integration.eqm.siebel.assetupdate.GRT_SpcEquipment_SpcMove_Impl;
import com.grt.util.ARTOperationType;
import com.grt.util.GRTConstants;
import com.grt.util.GrtSapException;
import com.grt.util.PasswordUtil;
import com.siebel.xml.avayaassetgrtrequest.AssetMgmtAsset;
import com.siebel.xml.avayaassetgrtrequest.AssetRequest;
import com.siebel.xml.avayaassetgrtrequest.AssetRequestTopElmt;
import com.siebel.xml.avayaassetgrtresponse.AssetResponse;
import com.siebel.xml.avayaassetgrtresponse.AssetResponseTopElmt;
import com.siebel.xml.avayaassetgrtresponse.Body;


public class ARTClient {
    private static final Logger logger = Logger.getLogger(ARTClient.class);

    private static final String ART_XPATH = "/ART-Response/Output";
    private static final String SOLUTION_ELEMENTS = "SolutionElements";
    private static final String SOLUTION_ELEMENT = "SolutionElement";
    private static final String SE_CREATED = "SECreated";
    private static final String CURRENT_SE = "CurrentSE";
    private static final String NEW_SEID = "NewSEID";
    private static final String SE_CODE = "SECode";
    private static final String IP_ADDESS = "IPAddress";
    private static final String ALARM_ID = "AlarmID";
    private static final String RECORD_NUM = "RecordNum";
    private static final String ART_XPATH_IPO = "/ART-Intermediate-Response/Output";
    private static final String FWW_XPATH_ART = "/FMW-Intermediate-Response/Output1";
    private static final String FMW_XPATH_ARTDOWN = "/FMW-Intermediate-Response/Output2";

    private int maximumRetry;
    private int maximumFMWRetry;
    private String systemIdentifier;
    private String artEndPoint;
    private String artAlarmEndPoint;
    private String artUser;
    private String artPasswd;
    private int artConnectionTimeOut;
    private int artSocketTimeOut;
    private String alsbUser;
    private String alsbPasswd;
    private String acsbiPollingUrl;
    private String afidEndPoint;
    private String sidMidEndPoint;
    private String acsbiValidatesidUrl;
    private String salConcentratorEndpoint;
    private PasswordUtil passwordUtils;
    private String artSalgwOldEp;
    private String artSalgwNewEp;

    public String sendRequest(HashMap params) throws IOException, SocketTimeoutException, Exception {
        logger.debug("Starting ... ");
        
        String endPointURL = getArtEndPoint();
        String username = getArtUser();
        String password = decryptArtPasswd();        
        int connTimeoutInt = getArtConnectionTimeOut();
        int socketTimeoutInt = getArtSocketTimeOut();        

        HttpPost httpPost = new HttpPost(endPointURL);
        logger.debug("ART URL to send [" + httpPost.getURI() + "]");

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Object param : params.keySet()) {
            String value = (String)params.get((String)param);
            logger.debug("Input Parameters to ART "+ param + ":" + value);
            nameValuePairs.add(new BasicNameValuePair((String)param, value));
        }

        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        //Prepare HTTPClient:
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connTimeoutInt);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, socketTimeoutInt);
        ((DefaultHttpClient) httpClient).getCredentialsProvider().setCredentials(
                new AuthScope(httpPost.getURI().getHost(), httpPost.getURI().getPort(),AuthScope.ANY_REALM),
                new UsernamePasswordCredentials(username, password));


        //Sending HTTP request
        logger.debug("Sending request and waiting for response ...");
        String responseBody = "";
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            responseBody = httpClient.execute(httpPost, responseHandler);
        } catch(SocketTimeoutException ste) {
            //Response timeout (no possible retry):
            logger.error("ERROR: Response timeout while waiting for ART response. \n"
                    + "Original error: " + ste.getMessage() + "\n"
                    + "Connection detail: URL [" + endPointURL + "]\n"
                    + "Connection timeout [" + connTimeoutInt + "] ms \n"
                    + "Socket timeout [" + socketTimeoutInt + "] ms.");
            ste.printStackTrace();
            throw ste;
        } catch (IOException ioe) {
            //IOException may be handled differently for a retry.
            logger.error("ERROR: While trying to connect to ART. \n"
                    + "Original error: " + ioe.getMessage() + "\n"
                    + "Connection detail: URL [" + endPointURL + "]\n"
                    + "Connection timeout [" + connTimeoutInt + "] ms \n"
                    + "Socket timeout [" + socketTimeoutInt + "] ms.");
            ioe.printStackTrace();
            throw ioe;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

       logger.debug("Completed. Response from ART: \n" + responseBody);
       return responseBody;
    }

    public HashMap<String, Object> parseResponseBody(String responseBody) throws Exception{
        logger.debug("Starting ... ");
        HashMap<String, Object> map = null;
        try {
            //Get bytes out of String:
            ByteArrayInputStream xmlContentBytes = new ByteArrayInputStream(responseBody.getBytes());

            //Build new XML our of String:
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlContentBytes);

            //Parse XML doc:
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            XPathExpression expr = xpath.compile(ART_XPATH_IPO);
            if(expr == null) {
                throw new IllegalArgumentException("ERROR: The incoming response body does NOT have "
                        + "expected element [" + ART_XPATH + "]!");
            }
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            NodeList childNodeList = nodes.item(0).getChildNodes();

            //Compose output Map:
            map = new HashMap<String, Object>();
            map = nodeToMap(childNodeList, map);

        } catch(Exception e) {
            logger.error("ERROR: Unexpected exception while processing with error: " + e.getMessage() + "\n"
                    + "Incoming response body to parse: \n"
                    + responseBody);
            e.printStackTrace();
            throw e;
        }
        logger.debug("Completed. Returning parsed result.");
        return map;
    }

    /*
     * [AVAYA] GRT2.1 Parsing the response boby after getting response from ART
     */
    public HashMap<String, Object> parseIPOResponseBody(String responseBody) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{
        logger.debug("Starting ... ");
        HashMap<String, Object> map = null;
        DocumentBuilder builder = null;
        Document doc = null;
        XPathExpression expr = null;
        try {
            //Get bytes out of String:
            ByteArrayInputStream xmlContentBytes = new ByteArrayInputStream(responseBody.getBytes());
            //Build new XML our of String:
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            builder = factory.newDocumentBuilder();
			doc = builder.parse(xmlContentBytes);
            //Parse XML doc:
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            expr = xpath.compile(ART_XPATH_IPO);
			if(expr == null) {
                throw new IllegalArgumentException("ERROR: The incoming response body does NOT have "
                        + "expected element [" + ART_XPATH_IPO + "]!");
            }
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            NodeList childNodeList = nodes.item(0).getChildNodes();

            //Compose output Map:
            map = new HashMap<String, Object>();
            map = nodeToMap(childNodeList, map);
        } catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
        	logger.error("ERROR: Unexpected exception while processing with error: " + e.getMessage() + "\n"
                    + "Incoming response body to parse: \n"
                    + responseBody);
            e.printStackTrace();
            throw e;
		}catch (SAXException e) {
			// TODO Auto-generated catch block
			logger.error("ERROR: Unexpected exception while processing with error: " + e.getMessage() + "\n"
                    + "Incoming response body to parse: \n"
                    + responseBody, e);
            e.printStackTrace();
            throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("ERROR: Unexpected exception while processing with error: " + e.getMessage() + "\n"
                    + "Incoming response body to parse: \n"
                    + responseBody, e);
            e.printStackTrace();
            throw e;
		}catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			logger.error("ERROR: Unexpected exception while processing with error: " + e.getMessage() + "\n"
                    + "Incoming response body to parse: \n"
                    + responseBody, e);
            e.printStackTrace();
            throw e;
		}

        logger.debug("Completed. Returning parsed result.");
        return map;
    }

    /*
     * Get ART-Response/Output/(*)
     */
    private HashMap<String, Object> nodeToMap(NodeList childNodeList, HashMap<String, Object> map) {
        logger.debug("Starting ...");
        int size = childNodeList.getLength();
        for (int i = 0; i < size; i++) {
            Node childNode = childNodeList.item(i);
            if (childNode.getChildNodes().getLength() == 1
                    && childNode.getAttributes().getLength() == 0) {
                String nodeName = childNode.getNodeName();
                String nodeValue = childNode.getTextContent();
                logger.debug("Node name [" + nodeName + "] with value [" + nodeValue + "]");
                map.put(nodeName, nodeValue);

            } else if (childNode.getChildNodes().getLength() > 1) {
                NodeList subChildNodeList = childNode.getChildNodes();
                if (childNode.getNodeName().equals(SOLUTION_ELEMENTS)) {
                    logger.debug("Parsing [" + SOLUTION_ELEMENTS + "]");
                    map = getSEIDS(childNode, map);
                }
                else {
                    logger.debug("Recursive call to get down with this XML nodes ...");
                    map = nodeToMap(subChildNodeList, map);
                }
            }
        }
        return map;
    }



    /*
     * Get ART-Response/Output/SolutionElements/CurrentSE.
     */
    private HashMap<String, Object> getSEIDS(Node childNode, HashMap map) {
        NodeList seIDNodeList = childNode.getChildNodes();
        int size = seIDNodeList.getLength();
        for (int i = 0; i < size; i++) {
            Node seIDNode = seIDNodeList.item(i);
            if (seIDNode != null
                    && seIDNode.getNodeName() != null
                    && seIDNode.getNodeName().equals(CURRENT_SE)) {
                SolutionElement solutionElement = new SolutionElement();
                solutionElement.setNewSEID(seIDNode.getTextContent());
                if (seIDNode.getAttributes().getNamedItem(SE_CODE) != null) {
                    solutionElement.setSeCode(seIDNode.getAttributes().getNamedItem(SE_CODE).getTextContent());
                }
                if (seIDNode.getAttributes().getNamedItem(IP_ADDESS) != null) {
                    solutionElement.setIpAddess(seIDNode.getAttributes().getNamedItem(IP_ADDESS).getTextContent());
                }
                if (seIDNode.getAttributes().getNamedItem(ALARM_ID) != null) {
                    solutionElement.setAlarmId(seIDNode.getAttributes().getNamedItem(ALARM_ID).getTextContent());
                }
                if (seIDNode.getAttributes().getNamedItem(RECORD_NUM) != null) {
                    solutionElement.setRecordNum(seIDNode.getAttributes().getNamedItem(RECORD_NUM).getTextContent());
                }
                map.put(SOLUTION_ELEMENT, solutionElement);
                break;
            }
        }
        return map;
    }

    private String formatParams(HashMap params) {
        StringBuffer buffer = new StringBuffer();
        int i = 0;
        for (Object param : params.keySet()) {
            Object value = params.get(param);
            if (i > 0) {
                buffer.append("&");
            }
            buffer.append(URLEncoder.encode(param.toString())  + "=" + URLEncoder.encode(String.valueOf(value)));
            i++;
        }
        return buffer.toString();
    }

    /**
     * For testing ONLY.
     */
    public static void main(String[] arg) throws Exception {
        /*
    	String xmlString = null;
        FileInputStream xmlContentBytes = new FileInputStream(new File("artdemo1.xml"));
        byte[] buf = new byte[xmlContentBytes.available()];
        xmlContentBytes.read(buf);
        xmlString = new String(buf);

        ARTClient client = new ARTClient();
        client.parseIPOResponseBody(xmlString);
		*/
    	
    	ARTClient client = new ARTClient();
    	/*
    	client.setArtEndPoint("https://fmwuat3.avaya.com:8102/GRT2/v2/Proxy_Services/TechRegWithART_PS");
    	client.setAlsbUser("GRTWS");
    	client.setAlsbPasswd("zsjYeGD0KqK6gF9c9GL8aA==");
    	client.setSystemIdentifier("GRT_GC_Q_1");
    	client.setMaximumRetry(1);
        
    	List<TechRegInputDto>  techRegInputDtoList = new ArrayList<TechRegInputDto>();
    	
    	TechRegInputDto techRegInput = new TechRegInputDto();
    	techRegInput.setGrtid("7000435");
    	techRegInput.setTechRegId("7001003");
		techRegInput.setTechRegDetail("TR");
		techRegInput.setUsrid("grt");		
		techRegInput.setFl("0001237354");
		techRegInput.setGroupId("acccm_ACCCM_303879");
		techRegInput.setSummaryEquipmentNumber("");
		techRegInput.setEquipmentNumber("");
		techRegInput.setMainSeid("(628)084-4337");
		techRegInput.setFailedSeid("");
		techRegInput.setAlmid("");
		techRegInput.setAorig("N");
		techRegInput.setCustipaddress("(628)083-5427");
		techRegInput.setFoption("");
		techRegInput.setHwsvr("");
		techRegInput.setInads("");
		techRegInput.setIpadd("");
		techRegInput.setIsDOCR("");
		techRegInput.setMcode("303879");
		techRegInput.setMid("1");
		techRegInput.setNickname("Test");
		techRegInput.setOptype("DU");
		techRegInput.setOssno("");
		techRegInput.setPrvip("");
		techRegInput.setRelno("");
		techRegInput.setRndpwd("");
		techRegInput.setSid("143718");
		techRegInput.setSvrname("");
		techRegInput.setVpmsldn("");
		techRegInput.setAfid("");
		techRegInput.setUsername("India_WC");
		techRegInput.setPassword("123");
    	
    	techRegInputDtoList.add(techRegInput);
    	
    	client.technicalRegistration(techRegInputDtoList);
    	*/
    	
    	client.setArtAlarmEndPoint("http://tlesbap11.us1.avaya.com:8101/GRT/v4/ProxyServices/TOBRequestWithART_PS");
    	client.setAlsbUser("GRTWS");
    	client.setAlsbPasswd("zsjYeGD0KqK6gF9c9GL8aA==");
    	client.setSystemIdentifier("GRT_GC_Q_1");
    	client.setMaximumRetry(1);
    	
    	
    	TechRegAlarmInputDto techRegAlarmInputDto = new TechRegAlarmInputDto();    	
    	techRegAlarmInputDto.setGrtId("6941766");
    	techRegAlarmInputDto.setSoldTo("0001237354");
    	
    	List<TechRegInputDto>  techRegInputDtoList = new ArrayList<TechRegInputDto>();    	
       	TechRegInputDto techRegInput = new TechRegInputDto();
       	techRegInput.setTechRegId("6941768");
       	//techRegInput.setMainSeid("(628)084-4337");
       	techRegInput.setMainSeid("(628)084-8194");
       	//techRegInput.setGatewaySEID("");
       	//techRegInput.setAlmid("");
       	techRegInput.setAccessType("SAL");
       	techRegInput.setOptype(ARTOperationType.TEST_CONNECTION.getOperationKey());
       	
       	
       	com.grt.dto.TechRegInputDto.Param paramArrayElem = techRegInput.getParam();
       	com.grt.dto.TechRegInputDto.Param[] paramArray = new com.grt.dto.TechRegInputDto.Param[1];
       	paramArrayElem.setKey("");
       	paramArrayElem.setValue("");       	
       	paramArray[0] = paramArrayElem;
       	com.grt.dto.TechRegInputDto.ParametersType parameters = techRegInput.getParameters();
       	parameters.setParam(paramArray);
       	techRegInput.setParameters(parameters);
		
       	
       	techRegInputDtoList.add(techRegInput);
    	techRegAlarmInputDto.setTechRegInputDtoList(techRegInputDtoList);
		
    	client.technicalAlamRegistration(techRegAlarmInputDto);

    	//new com.grt.dto.TechRegInputDto.Param()
    	//new com.grt.dto.TechRegInputDto.ParametersType()
		
    }

    /*
     * [AVAYA] GRT 2.1 Sending Post request to ART
     */
    public String sendIPORequest(HashMap<String,String> params) throws IOException, NoSuchFieldException {
        logger.debug("Starting ... ");

        //Retrieve data from Properties:
        int connTimeoutInt = getArtConnectionTimeOut();
        int socketTimeoutInt = getArtSocketTimeOut();
        String endPointURL  = getArtEndPoint();
        String username = getArtUser();
        String password = decryptArtPasswd();
        

		HttpPost httpPost = new HttpPost(endPointURL);

		logger.debug("****ART URL :" + httpPost.getURI() + ": with username:" + username + ": with params as follow");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String param : params.keySet()) {
			String value = params.get(param);
			logger.debug("paramName:value["+ param + ":" + value + "]");
			nameValuePairs.add(new BasicNameValuePair(param, value));
		}
		logger.debug("****paramList completed !!");

        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        //Prepare HTTPClient:
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connTimeoutInt);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, socketTimeoutInt);
        ((DefaultHttpClient) httpClient).getCredentialsProvider().setCredentials(
                new AuthScope(httpPost.getURI().getHost(), httpPost.getURI().getPort(),AuthScope.ANY_REALM),
                new UsernamePasswordCredentials(username, password));


        logger.debug("***Posting http data to ART");
        String responseBody = "";
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            responseBody = httpClient.execute(httpPost, responseHandler);
        } catch(SocketTimeoutException ste) {
            //Response timeout (no possible retry):
            logger.error("ERROR: Response timeout while waiting for ART response. \n"
                    + "Original error: " + ste.getMessage() + "\n"
                    + "Connection detail: URL [" + endPointURL + "]\n"
                    + "Connection timeout [" + connTimeoutInt + "] ms \n"
                    + "Socket timeout [" + socketTimeoutInt + "] ms.", ste);
            ste.printStackTrace();
            throw ste;
        } catch (IOException ioe) {
            //IOException may be handled differently for a retry.
            logger.error("ERROR: While trying to connect to ART. \n"
                    + "Original error: " + ioe.getMessage() + "\n"
                    + "Connection detail: URL [" + endPointURL + "]\n"
                    + "Connection timeout [" + connTimeoutInt + "] ms \n"
                    + "Socket timeout [" + socketTimeoutInt + "] ms.", ioe);
            ioe.printStackTrace();
            throw ioe;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

       logger.debug("***Response from ART: \n" + responseBody);
       return responseBody;
    }


    /**
     * parsing the response received from FMW
     *
     * There are two synchronous responses received from FMW.
     *
     * Scenario 1: GRT sends a request to FMW, FMW process the message successfully and sends the response back to GRT
     *      Here the Sample XML message
     *
     *      <?xml version="1.0" encoding="UTF-8"?>
			<tns:TechRegResponse xsi:schemaLocation="http://avaya.com/v1/techregistration TechnicalRegistration.xsd" xmlns:tns="http://avaya.com/v1/techregistration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
			<tns:artid>String</tns:artid> <!-- Optional -->
			<tns:grtid>String</tns:grtid> <!-- Optional -->
			<tns:artsr>String</tns:artsr> <!-- Optional -->
			<tns:Code>String</tns:Code> <!-- Optional -->
			<tns:Description>String</tns:Description> <!-- Optional -->
			</tns:TechRegResponse>

     *
     * Scenario 2: GRT sends a request to FMW, FMW unable to talk to ART. FMW constructs message and publish to internal queue for later processing and sends response to GRT accordingly
     *
     * <?xml version="1.0" encoding="UTF-8"?>
		<tns:TechRegResponse xsi:schemaLocation="http://avaya.com/v1/techregistration TechnicalRegistration.xsd" xmlns:tns="http://avaya.com/v1/techregistration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<tns:Code>1000</tns:Code>
		<tns:Description>String</tns:Description> <!-- Optional -->
		</tns:TechRegResponse>
     *
     */
    public HashMap<String, Object> parseFMWResponseBody(String responseBody) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{
        logger.debug("Starting ... ");
        HashMap<String, Object> map = null;
        try {
            //Get bytes out of String:
            ByteArrayInputStream xmlContentBytes = new ByteArrayInputStream(responseBody.getBytes());

            //Build new XML our of String:
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlContentBytes);

            //Parse XML doc:
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();

            XPathExpression expr = xpath.compile(FWW_XPATH_ART);
            XPathExpression expr2 = xpath.compile(FMW_XPATH_ARTDOWN);
            if(expr == null || expr2 == null) {
            	throw new IllegalArgumentException("ERROR: The incoming response body does NOT have "
                        + "expected element [" + FWW_XPATH_ART + "]! or [" + FMW_XPATH_ARTDOWN + "]");
            }

            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            NodeList childNodeList = nodes.item(0).getChildNodes();

            //Compose output Map:
            map = new HashMap<String, Object>();

            int size = childNodeList.getLength();
            for (int i = 0; i < size; i++) {
                Node childNode = childNodeList.item(i);
                if (childNode.getChildNodes().getLength() == 1
                        && childNode.getAttributes().getLength() == 0) {
                    String nodeName = childNode.getNodeName();
                    String nodeValue = childNode.getTextContent();
                    logger.debug("Node name [" + nodeName + "] with value [" + nodeValue + "]");
                    map.put(nodeName, nodeValue);
                }
            }

        } catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
        	logger.error("ERROR: Unexpected exception while processing with error: " + e.getMessage() + "\n"
                    + "Incoming response body to parse: \n"
                    + responseBody);
            e.printStackTrace();
            throw e;
		}catch (SAXException e) {
			// TODO Auto-generated catch block
			logger.error("ERROR: Unexpected exception while processing with error: " + e.getMessage() + "\n"
                    + "Incoming response body to parse: \n"
                    + responseBody, e);
            e.printStackTrace();
            throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("ERROR: Unexpected exception while processing with error: " + e.getMessage() + "\n"
                    + "Incoming response body to parse: \n"
                    + responseBody, e);
            e.printStackTrace();
            throw e;
		}catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			logger.error("ERROR: Unexpected exception while processing with error: " + e.getMessage() + "\n"
                    + "Incoming response body to parse: \n"
                    + responseBody, e);
            e.printStackTrace();
            throw e;
		}

        logger.debug("Completed. Returning parsed result.");
        return map;
    }

    /**
     * Method to set Remote End point
     * @param stub
     * @param url
     * @throws Exception
     */
    private void setRemoteEndPoint(Stub stub, String url) throws Exception {
        logger.debug("Setting for URL [" + url + "]");
        logger.debug("Stub:"+stub);

        stub._setProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY, url);
        stub._setProperty(javax.xml.rpc.Stub.USERNAME_PROPERTY, getAlsbUser());
        stub._setProperty(javax.xml.rpc.Stub.PASSWORD_PROPERTY, decryptAlsbPasswd());
    }

    /**
     *
     * Method to call FMW for techincal registration
     *
     * @param techRegInput
     * @return
     * @throws Exception
     */
   /* public com.avaya.v1.techregistration.TechRegRespType technicalRegistration(TechRegInputDto techRegInput) throws Exception {

    	logger.debug("Starting for Technical Registration, GrtId:"+techRegInput.getGrtid());
    	long c1 = Calendar.getInstance().getTimeInMillis();

    	String artEnURL = null;
        int retry_count = 0;
        int timeout = 0;
        String systemIdentifier = "";
        TechRegRespType techRegResponse = null;
        ExternalTechRegRequestWSDL_PortType portType = null;
        TechRegDataType[] techRegData = new com.avaya.v1.techregistration.TechRegDataType[1];
        TechRegRequestType techRegRequest = new com.avaya.v1.techregistration.TechRegRequestType();

		try {
			portType = new ExternalTechRegRequestWSDL_Service_Impl().getExternalTechRegRequestWSDLSOAP();
			if(portType == null){
				logger.error("portType not acquired, exiting the process !!");
				return null;
			}

		} catch (ServiceException serviceException) {
			logger.error("ERROR: While trying to call the ART in SOAP WSDL call for Technical Registration.", serviceException);
        }
        try {
        	artEnURL = GRTPropertiesUtil.getProperty("art_endpoint_url").trim();
            timeout = Integer.parseInt(GRTPropertiesUtil.getProperty("art_connection_timeout").trim());
            systemIdentifier = GRTPropertiesUtil.getProperty("system_identifier").trim();
            retry_count = Integer.parseInt(GRTPropertiesUtil.getProperty("retry_count").trim());
            logger.debug("artEnURL:"+artEnURL);
            logger.debug("timeout:"+timeout);
            logger.debug("retry_count:"+retry_count);
            setRemoteEndPoint((Stub)portType, artEnURL);
        } catch(Exception e) {
            logger.error("Failure while trying to read FMW endpoint properties for setup.", e);
        }

        try{
        	logger.debug("+++++++++++++++++++Input data:++++++++++++++++++++++++++++");
        	logger.debug("Submitting input data to ART");
        	if(techRegInput != null){
        		techRegData[0]  = new com.avaya.v1.techregistration.TechRegDataType();
        		if (StringUtils.isNotEmpty(systemIdentifier)){
        			techRegData[0].setSystemIdentifier(systemIdentifier);
        			logger.debug("SystemIdentifier:"+systemIdentifier);
        			logger.debug("SystemIdentifier:"+systemIdentifier);
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getAfid())) {
        			techRegData[0].setAfid(techRegInput.getAfid());
        			logger.debug("Afid:"+techRegInput.getAfid());
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getAlmid())) {
        			techRegData[0].setAlmid(techRegInput.getAlmid().toString());
        			logger.debug("Almid:"+techRegInput.getAlmid().toString());
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getAorig())) {
        			techRegData[0].setAorig(techRegInput.getAorig());
        			logger.debug("Aorig:"+techRegInput.getAorig());
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getCustipaddress())) {
        			techRegData[0].setCustipaddress(techRegInput.getCustipaddress());
        			logger.debug("Custipaddress:"+techRegInput.getCustipaddress());
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getDatalock())) {
        			techRegData[0].setDatalock(techRegInput.getDatalock());
        			logger.debug("Datalock:"+techRegInput.getDatalock());
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getDefaultpid())) {
        			techRegData[0].setDefaultpid(techRegInput.getDefaultpid());
        			logger.debug("Defaultpid:"+techRegInput.getDefaultpid());
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getDossno())) {
        			techRegData[0].setDossno(techRegInput.getDossno());
        			logger.debug("Dossno:"+techRegInput.getDossno());
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getDtype())) {
        			techRegData[0].setDtype(techRegInput.getDtype());
        			logger.debug("Dtype:"+techRegInput.getDtype());
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getFl())) {
        			techRegData[0].setFl(techRegInput.getFl());
        			logger.debug("Fl:"+techRegInput.getFl());
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getFoption())) {
        			techRegData[0].setFoption(techRegInput.getFoption());
        			logger.debug("Foption:"+techRegInput.getFoption());
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getGrtid())) {
        			techRegData[0].setGrtid(techRegInput.getGrtid());
        			logger.debug("GrtId:"+techRegInput.getGrtid());
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getHwsvr())) {
        			techRegData[0].setHwsvr(techRegInput.getHwsvr());
        			logger.debug("Hwsvr:"+techRegInput.getHwsvr());
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getInads())) {
        			techRegData[0].setInads(techRegInput.getInads());
        			logger.debug("Inads:"+techRegInput.getInads());
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getIpadd())) {
        			techRegData[0].setIpadd(techRegInput.getIpadd());
        			logger.debug("Ipadd:"+techRegInput.getIpadd());
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getIsDOCR())) {
        			techRegData[0].setIsDOCR(techRegInput.getIsDOCR());
        			logger.debug("IsDOCR:"+techRegInput.getIsDOCR());
        		}

        		if (StringUtils.isNotEmpty(techRegInput.getLHN())) {
        			techRegData[0].setLHN(techRegInput.getLHN());
        			logger.debug("LHN:"+techRegInput.getLHN());
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getLogin())){
        			techRegData[0].setLogin(techRegInput.getLogin());
        			logger.debug("Login:"+techRegInput.getLogin());
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getMcode())) {
        			techRegData[0].setMcode(techRegInput.getMcode());
        			logger.debug("Mcode:"+techRegInput.getMcode());
        		}
        		if (StringUtils.isNotEmpty(Integer.toString(techRegInput.getMid()))) {
        			techRegData[0].setMid((techRegInput.getMid()));
        			logger.debug("Mid:"+techRegInput.getMid());
        		}

        		if (StringUtils.isNotEmpty(techRegInput.getSname())) {
        			techRegData[0].setSname(techRegInput.getSname());
        			logger.debug("Sname:"+techRegInput.getSname());
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getNickname())) {
        			techRegData[0].setNickname(techRegInput.getNickname());
        			logger.debug("Nickname:"+techRegInput.getNickname());
        		}
        		if (StringUtils.isNotEmpty(Integer.toString(techRegInput.getOalid()))) {
        			techRegData[0].setOalid(techRegInput.getOalid());
        			logger.debug("Oalid:"+techRegInput.getOalid());
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getOptype())) {
        			techRegData[0].setOptype(techRegInput.getOptype());
        			logger.debug("Optype:"+techRegInput.getOptype());
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getOssno())) {
        			techRegData[0].setOssno(techRegInput.getOssno());
        			logger.debug("Ossno"+techRegInput.getOssno());
        		}

        		if (StringUtils.isNotEmpty(techRegInput.getProcserialno())) {
        			techRegData[0].setProcserialno(techRegInput.getProcserialno());
        			logger.debug("Procserialno:"+techRegInput.getProcserialno());
        		}

        		if (StringUtils.isNotEmpty(techRegInput.getProdtype())) {
        			techRegData[0].setProdtype(techRegInput.getProdtype().toLowerCase());
        			logger.debug("ProdType:"+techRegInput.getProdtype().toLowerCase());
       			}

        		if (StringUtils.isNotEmpty(techRegInput.getPrvip())) {
        			techRegData[0].setPrvip(techRegInput.getPrvip());
        			logger.debug("Prvip:"+techRegInput.getPrvip());
        		}

        		if (StringUtils.isNotEmpty(techRegInput.getPswds())) {
        			techRegData[0].setPswds(techRegInput.getPswds());
        			logger.debug("Pswds:"+techRegInput.getPswds());
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getRelno())) {
        			techRegData[0].setRelno(techRegInput.getRelno());
        			logger.debug("RelNo:"+techRegInput.getRelno());
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getRndpwd())) {
        			techRegData[0].setRndpwd(techRegInput.getRndpwd());
        			logger.debug("Rndpwd:"+techRegInput.getRndpwd());
        		}

        		if (StringUtils.isNotEmpty(techRegInput.getRtschedname())) {
        			techRegData[0].setRtschedname(techRegInput.getRtschedname());
        			logger.debug("Rtschedname:"+techRegInput.getRtschedname());
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getScode())) {
        			techRegData[0].setScode(techRegInput.getScode());
        			logger.debug("Scode:"+techRegInput.getScode());
        		}
        		if (StringUtils.isNotEmpty(Integer.toString(techRegInput.getSid()))) {
        			techRegData[0].setSid(techRegInput.getSid());
        			logger.debug("Sid:"+techRegInput.getSid());
        		}

        		if (StringUtils.isNotEmpty(techRegInput.getTemplate())) {
        			techRegData[0].setTemplate(techRegInput.getTemplate());
        			logger.debug("Template:"+techRegInput.getTemplate());
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getTr2600Port())) {
        			techRegData[0].setTr2600Port(techRegInput.getTr2600Port());
        			logger.debug("Tr2600Port:"+techRegInput.getTr2600Port());
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getTr2600Pswd())) {
        			techRegData[0].setTr2600Pswd(techRegInput.getTr2600Pswd());
        			logger.debug("Tr2600Pswd:"+techRegInput.getTr2600Pswd());
        		}
        		if (StringUtils.isNotEmpty(techRegInput.getUsrid())) {
        			techRegData[0].setUsrid(techRegInput.getUsrid());
        			logger.debug("Usrid:"+techRegInput.getUsrid());
        		}
        		//techRegData[0].setUsrid("grtws");

        		if (StringUtils.isNotEmpty(techRegInput.getSvrname())) {
        			techRegData[0].setSvrname(techRegInput.getSvrname());
        			logger.debug("SvrName:"+techRegInput.getSvrname());
        		}
        	}
        } catch (Exception e) {
            logger.error("Error while populating InputData", e);
        }
        logger.debug("Retrying logic");


/// Begin Old Comment
		//TODO:Code for printing the SOAP Envelop.
		/*try {
			QName serviceName = new QName("http://java.sun.com/xml/ns/j2ee","ExternalTechRegRequestWSDL");
			URL wsdlLocation = new URL ("https://fmwstagec3.avaya.com:8502/GRT2/v1/Proxy_Services/TechRegWithART_PS?WSDL");
			// Service
			ServiceFactory factory = ServiceFactory.newInstance();
			Service service = factory.createService(wsdlLocation, serviceName);
			HandlerRegistry hr = service.getHandlerRegistry();
			QName  portName = new QName("http://www.avaya.com/ExternalTechRegRequestWSDL/TechRegistration", "ExternalTechRegRequestWSDLSOAP");
			List handlerChain = hr.getHandlerChain(portName);
			HandlerInfo hi = new HandlerInfo();
			hi.setHandlerClass(fmwOutHandler.class);
			handlerChain.add(hi);
		} catch (Exception e) {
			logger.error("Issue while logging the Soap Envelope",e);
		}*/
///End Old Comment

    	/*
	    for(int i = 0;i < retry_count; i++){
	        try{
	        	logger.debug("Calling FMW for Technical Registration, Attempt No:" + (i+1));
	        	techRegRequest.setTechRegData(techRegData);
	        	techRegResponse = portType.techRegistration(techRegRequest);

	        	if (techRegResponse != null){
	        		logger.debug("+++++++++Response++++++++");
	        		logger.debug("GRTId:"+techRegResponse.getGrtid());
	        		logger.debug("ARTId:"+techRegResponse.getArtid());
	        		if(StringUtils.isNotEmpty(techRegResponse.getArtsr())){
	        			logger.debug("ARTSr:"+techRegResponse.getArtsr());
	        		}
	        		logger.debug("ReturnCode:"+techRegResponse.getCode());
	        		logger.debug("Description:"+techRegResponse.getDescription());
	        		logger.debug("+++++++++Response++++++++");
	        		long c2 = Calendar.getInstance().getTimeInMillis();
	    			long c = c2-c1;
	    			logger.debug("Total Time:"+c+" milliseconds");

	        		return techRegResponse;
	        	}
	        } catch (IOException ioe ) {
	        		logger.error("Failed to connect FMW for Technical Registration", ioe);
	        		if(i == retry_count - 1){
	        			techRegResponse = new com.avaya.v1.techregistration.TechRegRespType();
	        			techRegResponse.setCode("100");
	        		}
	        }catch(SOAPFaultException e){
	        			logger.error("Failed while sending data to FMW for Technical Registration", e);
	        			throw e;
	        }
	     }

	 logger.debug("Completed");
     return techRegResponse;
   }
   */


    /**
     * Initiate ACSBI polling for the CM Main products
     */
    public String initiateCMPolling(TechnicalRegistration technicalRegistration, String piePollingData) throws Exception{
    	long c1 = Calendar.getInstance().getTimeInMillis();
    	logger.debug("................ Starting for getACSBIPollingResponse ................");
    	String artEndPointURL = "";
    	EPNSurveySoap portType = null;
    	String epnSurveyStr = "";
    	String systemIdentifier = "";
    	int retry_count = 0;
        int timeout = 0;
    	try {
    		 portType = new EPNSurvey_Impl().getEPNSurveySoap();
			 if(portType == null){
				 logger.error("portType not acquired, exiting the process !!");
				 return null;
			 }
    	} catch (ServiceException serviceException) {
			// TODO: handle exception
    		logger.error("ERROR: While trying to call the ART in SOAP WSDL call for ACSBIPolling."+serviceException);
		}
    	try{
    		artEndPointURL = getAcsbiPollingUrl();
    		timeout = getArtConnectionTimeOut();
            systemIdentifier = getSystemIdentifier();
            retry_count = getMaximumRetry();
    		setRemoteEndPoint((Stub)portType, artEndPointURL);
    	}catch (Exception e) {
			logger.error("", e);
		}
    	logger.debug("Retrying logic");
		try {
			retry_count = getMaximumRetry();
		} catch (Exception e) {
			logger.error("ERROR: While getting the property retry_count", e);
			throw new GrtSapException(e);
		}
		if(technicalRegistration != null){
			if(StringUtils.isEmpty(piePollingData) ) {
				logger.warn("PIE Pollable SECode Not found, Halting EPNSurvey initiation:"  + technicalRegistration.getTechnicalRegistrationId());
				return null;
	    	}
	    	for(int i = 0;i < retry_count; i++) {
	    		try{
	    			logger.debug("Calling ART for ACSBI Polling");
	    			logger.debug("System Identifier:  " + getSystemIdentifier());
	    			String cmSoldTo = "";
	    			String remoteSoldTo = "";
	    			String cmMainSEID = "";
	    			String cmRemoteSEID = "";
	    			String cmMainSID = "";
	    			String cmRemoteMID = "";
	    			String cmRemoteDeviceType = "";
	    			if(technicalRegistration.isCmMain()) {
	    				//GRT 4.0 Change : Get toSoldto(New) for equipment move
	    				if(  technicalRegistration.isEquipmentMove()  ){
	    					cmSoldTo = technicalRegistration.getToSoldToId();
	    				}
	    				else if(StringUtils.isNotEmpty(technicalRegistration.getSoldToId())){
	    					cmSoldTo = technicalRegistration.getSoldToId();
	    				} else {
	    					cmSoldTo = technicalRegistration.getTechnicalOrder().getSiteRegistration().getSoldToId();
	    				}
	    				if(StringUtils.isNotEmpty(piePollingData) && piePollingData.indexOf(":") > 0) {
	    					String [] data = piePollingData.split(":");
	    					cmMainSEID = data[1];
	    				}
	    				cmMainSID = technicalRegistration.getSid();
	    			} else {
	    				cmRemoteDeviceType = technicalRegistration.getRemoteDeviceType();
	    				if(StringUtils.isEmpty(cmRemoteDeviceType)) {
	    					logger.warn(" CM product is configured as remote and remoteDevic type is missing, halting CM polling for TR-ID:"  + technicalRegistration.getTechnicalRegistrationId());
	    				} else if(cmRemoteDeviceType.equalsIgnoreCase(GRTConstants.REMOTE_DEVICE_TYPE_GATEWAY)) {
	    					logger.debug("Remote Device type is Gateway, No CM Polling required for TR-ID:" + technicalRegistration.getTechnicalRegistrationId());
	    				}
	    				cmSoldTo = technicalRegistration.getCmMainSoldToId();
	    				
	    				//GRT 4.0 Change : Get toSoldto(New) for equipment move
	    				if(  technicalRegistration.isEquipmentMove()  ){
	    					remoteSoldTo = technicalRegistration.getToSoldToId();
	    				}else
	    				if(StringUtils.isNotEmpty(technicalRegistration.getSoldToId())){
	    					remoteSoldTo = technicalRegistration.getSoldToId();
	    				} else {
	    					remoteSoldTo = technicalRegistration.getTechnicalOrder().getSiteRegistration().getSoldToId();	
	    				}
	    				if(StringUtils.isNotEmpty(remoteSoldTo)){
	    					cmSoldTo += "-"+remoteSoldTo;
	    					logger.debug("Remote SoldTo is appended to cmMain soldTo.");
	    				}
	    				cmMainSEID = technicalRegistration.getCmMainSeid();
	    				if(StringUtils.isNotEmpty(piePollingData) && piePollingData.indexOf(":") > 0) {
	    					String [] data = piePollingData.split(":");
	    					cmRemoteSEID = data[1];
	    				}
	    				cmMainSID = technicalRegistration.getCmMainSid();
	    				cmRemoteMID = technicalRegistration.getMid();
	    			}
	    			
	    			//GRT4.0 - Pass additional parameter for "move" string parameter added in WS
	    			String move = "false";
	    			if( technicalRegistration.isEquipmentMove() ){
	    				move = "true";
	    			}
	    			
	    			logger.debug("cmSoldTo             :"+cmSoldTo);
	    			logger.debug("cmMainSEID           :"+cmMainSEID);
	    			logger.debug("TechRegId            :"+technicalRegistration.getTechnicalRegistrationId());
	    			logger.debug("system_identifier    :"+systemIdentifier);
	    			logger.debug("cmMainSID            :"+cmMainSID);
	    			logger.debug("cmRemoteSEID         :"+cmRemoteSEID);
	    			logger.debug("cmRemoteMID          :"+cmRemoteMID);
	    			logger.debug("cmRemoteDeviceType   :"+cmRemoteDeviceType);
	    			epnSurveyStr = portType.startEPNSurveyProcess(cmSoldTo, cmMainSEID,
	    					"grt@avaya.com", technicalRegistration.getTechnicalRegistrationId(),
	    					systemIdentifier,
	    					cmMainSID, cmRemoteSEID, cmRemoteMID, cmRemoteDeviceType, move);
	    			//TODO: Once we get reponse, handle it.
	    			//TODO: Update TR with status/errorDescription.
		        	if (epnSurveyStr != null){
		        		logger.debug("epnSurveyStr   :"+epnSurveyStr);
		        		long c2 = Calendar.getInstance().getTimeInMillis();
		    			long c = c2-c1;
		    			logger.debug("Total Time: "+c+" milliseconds");
		        		return epnSurveyStr;
		        	}
	    		} catch(IOException ioe ) {
	        		logger.error("Failed to connect FMW for ACSBI Polling", ioe);
	        		if(i == retry_count - 1) {
	        			//epnSurveyStr = "";
	        			if(ioe.getCause() instanceof SOAPFaultException) {
	        				if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
	        					logger.error("MONITOR:[Outage:ACSBI][ENDPOINT:"+ artEndPointURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
	        					epnSurveyStr = GRTConstants.acsbi_errorCode;
	        				} else {
	        					logger.error("MONITOR:[Outage:FMW][ENDPOINT:"+ artEndPointURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
	        					epnSurveyStr = GRTConstants.fmw_errorCode;
	        				}
	        			} else {
	        				logger.error("FMW is down");
	        				epnSurveyStr = GRTConstants.fmw_errorCode;
	        			}
                    }
		        } catch(SOAPFaultException e) {
		        	logger.error("Failed while sending data to FMW for ACSBI Polling", e);
		        	throw e;
		        }
	    	}
    	}
    	logger.debug("................ Exiting for getACSBIPollingResponse ................");
    	return epnSurveyStr;
    }
    
    /**
     * Initiate AFID Service
     */
    public String AFIDService(String afid) throws Exception{
    	long c1 = Calendar.getInstance().getTimeInMillis();
    	logger.debug("................ Starting for AFIDService ................");
    	String artEndPointURL = "";
    	GRTAFID_PortType portType = null;
    	String returnType = null;
    	String systemIdentifier = "";
    	int retry_count = 0;
        int timeout = 0;
    	try {
    		 portType = new GRTAFID_Service_Impl().getGRTAFIDSOAP();
			 if(portType == null){
				 logger.error("portType not acquired, exiting the process !!");
				 return GRTConstants.FALSE;
			 }
    	} catch (ServiceException serviceException) {
			// TODO: handle exception
    		logger.error("ERROR: While trying to call the ART in SOAP WSDL call for AFID Service."+serviceException);
		}
    	try{
    		artEndPointURL = getAfidEndPoint();
    		timeout = getArtConnectionTimeOut();
            systemIdentifier = getSystemIdentifier();
            retry_count = getMaximumRetry();
    		setRemoteEndPoint((Stub)portType, artEndPointURL);
    	}catch (Exception e) {
			logger.error("", e);
		}
    	logger.debug("Retrying logic");
		try {
			retry_count = getMaximumRetry();
		} catch (Exception e) {
			logger.error("ERROR: While getting the property retry_count", e);
			throw new GrtSapException(e);
		}
		if(StringUtils.isNotEmpty(afid)){
			
	    	for(int i = 0;i < retry_count; i++) {
	    		try{
	    			logger.debug("Calling ART for AFID Service Response");
	    			logger.debug("System Identifier:  " + getSystemIdentifier());
	    			
	    			AssetInfo assetInfo = portType.aFIDOperation(afid);
	    			
	    			//TODO: Once we get reponse, handle it.
		        	if (assetInfo != null){
		        		logger.debug("assetInfo   :"+assetInfo);
		        		long c2 = Calendar.getInstance().getTimeInMillis();
		    			long c = c2-c1;
		    			logger.debug("Total Time: "+c+" milliseconds");
		    			returnType = assetInfo.getCode() + ":" + assetInfo.getMessage();
		        	}
	    		} catch(IOException ioe ) {
	        		logger.error("Failed while sending data to FMW for AFID Service", ioe);
	        		if(i == retry_count - 1){
	        			if(ioe.getCause() instanceof SOAPFaultException) {
	        				if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
	        					logger.error("MONITOR:[Outage:FMW][ENDPOINT:"+ artEndPointURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
	        				} else {
	        					logger.error("MONITOR:[Outage:ART][ENDPOINT:"+ artEndPointURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
	        				}
	        			} else {
	        				logger.error("FMW is down");
	        			}
	        			returnType = GRTConstants.FALSE;
	        		}
		        } catch(SOAPFaultException e) {
		        	logger.error("Failed while sending data to FMW for AFID Service", e);
		        	throw e;
		        }
	    	}
    	}
    	logger.debug("................ Exiting for AFIDService ................");
    	return returnType;
    }
    
    /**
     * Initiate SID MID Service
     */
    public String SIDMIDService(String sid, String mid) throws Exception{
    	long c1 = Calendar.getInstance().getTimeInMillis();
    	logger.debug("................ Starting for SIDMIDService ................");
    	String artEndPointURL = "";
    	GRTSIDMID_PortType portType = null;
    	String returnType = null;
    	//String systemIdentifier = "";
    	int retry_count = 0;
        //int timeout = 0;
    	try {
    		 portType = new GRTSIDMID_Service_Impl().getGRTSIDMIDSOAP();
			 if(portType == null){
				 logger.error("portType not acquired, exiting the process !!");
				 return GRTConstants.FALSE;
			 }
    	} catch (ServiceException serviceException) {
			// TODO: handle exception
    		logger.error("ERROR: While trying to call the ART in SOAP WSDL call for SIDMID Service."+serviceException);
		}
    	try{
    		artEndPointURL = getSidMidEndPoint();
    		//timeout = Integer.parseInt(GRTPropertiesUtil.getProperty("art_connection_timeout").trim());
            //systemIdentifier = GRTPropertiesUtil.getProperty("system_identifier").trim();
            retry_count = getMaximumRetry();
    		setRemoteEndPoint((Stub)portType, artEndPointURL);
    	}catch (Exception e) {
			logger.error("", e);
		}
    	logger.debug("Retrying logic");
		try {
			retry_count = getMaximumRetry();
		} catch (Exception e) {
			logger.error("ERROR: While getting the property retry_count", e);
			throw new GrtSapException(e);
		}
		if(StringUtils.isNotEmpty(sid) && StringUtils.isNotEmpty(mid)){
			SIDMIDInfo sidMidInfo = new SIDMIDInfo();
			sidMidInfo.setSID(sid);
			sidMidInfo.setMID(mid);
			for(int i = 0;i < retry_count; i++) {
	    		try{
	    			logger.debug("Calling ART for SIDMID Service Response");
	    			logger.debug("System Identifier:  " + getSystemIdentifier());
	    			
	    			AssetInfo assetInfo = portType.sIDMIDOperation(sidMidInfo);
	    			
	    			//TODO: Once we get reponse, handle it.
		        	if (assetInfo != null){
		        		logger.debug("assetInfo   :"+assetInfo);
		        		long c2 = Calendar.getInstance().getTimeInMillis();
		    			long c = c2-c1;
		    			logger.debug("Total Time: "+c+" milliseconds");
		    			returnType = assetInfo.getCode() + ":" + assetInfo.getMessage();
		        	}
	    		} catch(IOException ioe ) {
	        		logger.error("Failed while sending data to SIDMID for AFID Service", ioe);
	        		if(i == retry_count - 1){
	        			if(ioe.getCause() instanceof SOAPFaultException) {
	        				if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
	        					logger.error("MONITOR:[Outage:FMW][ENDPOINT:"+ artEndPointURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
	        				} else {
	        					logger.error("MONITOR:[Outage:ART][ENDPOINT:"+ artEndPointURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
	        				}
	        			} else {
	        				logger.error("FMW is down");
	        			}
	        			returnType = GRTConstants.FALSE;
	        		}
		        } catch(SOAPFaultException e) {
		        	logger.error("Failed while sending data to FMW for SIDMID Service", e);
		        	throw e;
		        }
	    	}
    	}
    	logger.debug("................ Exiting for SIDMIDService ................");
    	return returnType;
    }

    public ACSBISIDQueryResponse validateSIDMID(String sid) throws Exception {
    	long c1 = Calendar.getInstance().getTimeInMillis();
    	logger.debug("................ Starting for validateSIDMID ................");
    	String artEndPointURL = "";
    	ACSBISIDQueryWSDL_PortType portType = null;
    	ACSBISIDQueryResponse response = null;
    	int retry_count = 0;
        int timeout = 0;
    	try {
    		 portType = new ACSBISIDQueryWSDL_Service_Impl().getACSBISIDQueryWSDLSOAP();
			 if(portType == null){
				 logger.error("portType not acquired, exiting the process !!");
				 return null;
			 }
    	} catch (ServiceException serviceException) {
    		logger.error("ERROR: While trying to call the ART in SOAP WSDL call for ACSBI SID Query."+serviceException);
		}
    	try{
    		artEndPointURL = getAcsbiValidatesidUrl();
    		timeout = getArtConnectionTimeOut();
            //systemIdentifier = GRTPropertiesUtil.getProperty("system_identifier").trim();
            retry_count = getMaximumRetry();
            logger.debug("timeout:"+timeout);
            logger.debug("retry_count:"+retry_count);
    		setRemoteEndPoint((Stub)portType, artEndPointURL);
    	}catch (Exception e) {
    		logger.error("", e);
		}
    	logger.debug("Retrying logic");
		try {
			retry_count = getMaximumRetry();
		} catch (Exception e) {
			logger.error("ERROR: While getting the property retry_count", e);
			throw new GrtSapException(e);
		}
		if(sid != null && sid.length() > 0){
	    	for(int i = 0;i < retry_count; i++) {
	    		try{
	    			logger.debug("Calling ART for ACSBI SID Query");
	    			logger.debug("System Identifier:  " + getSystemIdentifier());
	    			ACSBISIDQueryRequest acsbiQueryRequest = new ACSBISIDQueryRequest();
	    			acsbiQueryRequest.setSID(sid);
	    			response = portType.aCSBISIDQuery(acsbiQueryRequest);
	    			//TODO: Once we get reponse, handle it.
		        	if (response != null){
		        		long c2 = Calendar.getInstance().getTimeInMillis();
		    			long c = c2-c1;
		    			logger.debug("TIMER for validatin ACSBI SID Time taken in milliseconds: "+c);
		        		return response;
		        	}
	    		} catch (IOException ioe ) {
	        		logger.error("Failed to connect FMW for validating SIDMID", ioe);
	        		if(i == retry_count - 1) {
	        			if(ioe.getCause() instanceof SOAPFaultException) {
	        				if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
	        					logger.error("MONITOR:[Outage:ACSBI][ENDPOINT:"+ artEndPointURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
	        				} else {
	        					logger.error("MONITOR:[Outage:FMW][ENDPOINT:"+ artEndPointURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
	        				}
	        			} else {
	        				logger.error("FMW is down");
	        			}
	        			response = null;
                    }
		        }catch(SOAPFaultException e){
		        	logger.error("Failed while sending data to FMW for ACSBI SID Query", e);
		        	throw e;
		        }
	    	}
    	}
    	logger.debug("................ Exiting for validateSIDMID ................");
    	return response;
    }

    //OP type = FN -- Create. New SAL Gateway. -- In List Page
    //When multiple ---  The ID will be  primary_key of SIte_List record. OP type = DU . Use the data from this recod to populate the to prepare
    /////////////////////Begin Technical Registration from Create New SALGateway and Submit button from SAL Migration details screen

	 public TechRegRespType technicalRegistration(List<TechRegInputDto>  techRegInputDtoList ) throws Exception {
		    long c1 = Calendar.getInstance().getTimeInMillis();
	        logger.debug("................ Starting for Technical Registration ................");
	        String abcartEnURL = getArtEndPoint();
	        logger.debug("art end point url is =================> "+abcartEnURL);
	    	String artEnURL = null;
	        int retry_count = 0;
	        int timeout = 0;
	        String systemIdentifier = "";
	        //com.avaya.v2.techregistration.TechRegRespType techRegRespType = null;
	        TechRegRespType techRegRespType = null;
	        //com.grt.integration.sal.art.fmwclientproxy.ExternalTechRegRequestWSDL_PortType portType = null;
	        ExternalTechRegRequestWSDL_PortType portType = null;
	        TechRegRequestType techRegRequestType = null;

			try {
				portType = new ExternalTechRegRequestWSDL_Service_Impl().getExternalTechRegRequestWSDLSOAP();
				if(portType == null){
					logger.error("portType not acquired, exiting the process !!");
					return null;
				}
			}  catch (ServiceException serviceException) {
				logger.error("ERROR: While trying to call the ART in SOAP WSDL call for Technical Registration.", serviceException);
	        }
	        try {
	        	artEnURL = getArtEndPoint();
	            timeout = getArtConnectionTimeOut();
	            systemIdentifier = getSystemIdentifier();
	            retry_count = getMaximumRetry();
	            logger.debug("artEnURL:"+artEnURL);
	            logger.debug("timeout:"+timeout);
	            logger.debug("retry_count:"+retry_count);
	            setRemoteEndPoint((Stub)portType, artEnURL);
	        } catch(Exception e) {
	            logger.error("Failure while trying to read FMW endpoint properties for setup.", e);
	        }
	        //Prepare inputs and calling sap
	        if(techRegInputDtoList != null){
	        	if(techRegInputDtoList.size() >0){
	        		try{
	        		logger.debug("Prepare inputs");
	        		techRegRequestType = new TechRegRequestType();
	        		techRegRequestType.setSystemId(systemIdentifier);
	        		if(StringUtils.isNotEmpty(techRegRequestType.getSystemId())) {
	        			logger.debug("SystemId:" + techRegRequestType.getSystemId());
	        		}

	        		TechRegDataType[] techRegDataType = new TechRegDataType[techRegInputDtoList.size()];

	        		for(int i=0; i < techRegDataType.length;i++ ){
	        			TechRegInputDto techRegInput = techRegInputDtoList.get(i);
	        			techRegDataType[i] = new TechRegDataType();
	        			techRegRequestType.setTransactionId(techRegInput.getGrtid());
	        			//Change for GRT 4.0
	        			if (StringUtils.isNotEmpty(techRegInput.getAccessType())) {
	            			techRegDataType[i].setAccessType(techRegInput.getAccessType());
	            			logger.debug("Access type:"+techRegInput.getAccessType());
	            		}
	        			if (StringUtils.isNotEmpty(techRegRequestType.getTransactionId())) {
	            			logger.debug("TransactionId:" + techRegRequestType.getTransactionId());
	            		}
	        			if (StringUtils.isNotEmpty(techRegInput.getTechRegId())) {
	            			techRegDataType[i].setTechRegId(techRegInput.getTechRegId());
	            			logger.debug("TechRegId:"+techRegInput.getTechRegId());
	            		}
	        			if (StringUtils.isNotEmpty(techRegInput.getTechRegDetail())) {
	            			techRegDataType[i].setTechRegDetail(techRegInput.getTechRegDetail());
	            			logger.debug("TechRegDetail:"+techRegInput.getTechRegDetail());
	            		}
	        			if (StringUtils.isNotEmpty(techRegInput.getUsrid())) {
	            			techRegDataType[i].setUsrid(techRegInput.getUsrid());
	            			logger.debug("Usrid:"+techRegInput.getUsrid());
	            		}
	            		if (StringUtils.isNotEmpty(techRegInput.getFl())) {
	            			techRegDataType[i].setFL(techRegInput.getFl());
	            			logger.debug("Fl:"+techRegInput.getFl());
	            		}
	            		if (StringUtils.isNotEmpty(techRegInput.getGroupId())) {
	            			techRegDataType[i].setGroupId(techRegInput.getGroupId());
	            			logger.debug("GroupId:"+techRegInput.getGroupId());
	            		}
	            		if (StringUtils.isNotEmpty(techRegInput.getSummaryEquipmentNumber())) {
	            			techRegDataType[i].setSummaryEquipmentNumber(techRegInput.getSummaryEquipmentNumber());
	            			logger.debug("SummaryEquipmentNumber:"+techRegInput.getSummaryEquipmentNumber());
	            		}
	        			if (StringUtils.isNotEmpty(techRegInput.getEquipmentNumber())) {
	            			techRegDataType[i].setEquipmentNumber(techRegInput.getEquipmentNumber());
	            			logger.debug("EquipmentNumber:"+techRegInput.getEquipmentNumber());
	            		}
	        			if (StringUtils.isNotEmpty(techRegInput.getMainSeid())) {
	            			techRegDataType[i].setMainSEID(techRegInput.getMainSeid());
	            			logger.debug("MainSEID:"+techRegInput.getMainSeid());
	            		}
	        			if (StringUtils.isNotEmpty(techRegInput.getFailedSeid())) {
	            			techRegDataType[i].setFailedSEID(techRegInput.getFailedSeid());
	            			logger.debug("FailedSEID:"+techRegInput.getFailedSeid());
	            		}
	            		if (StringUtils.isNotEmpty(techRegInput.getAlmid())) {
	            			techRegDataType[i].setAlmid(techRegInput.getAlmid().toString());
	            			logger.debug("Almid:"+techRegInput.getAlmid().toString());
	            		}
	            		if (StringUtils.isNotEmpty(techRegInput.getAorig())) {
	            			techRegDataType[i].setAorig(Aorig.fromString(techRegInput.getAorig()));
	            			logger.debug("Aorig:"+techRegInput.getAorig());
	            		}
	            		if (StringUtils.isNotEmpty(techRegInput.getCustipaddress())) {
	            			techRegDataType[i].setCustipaddress(techRegInput.getCustipaddress());
	            			logger.debug("Custipaddress:"+techRegInput.getCustipaddress());
	            		}
	            		if (StringUtils.isNotEmpty(techRegInput.getFoption())) {
	            			techRegDataType[i].setFoption(Foption.fromString(techRegInput.getFoption()));
	            			logger.debug("Foption:"+techRegInput.getFoption());
	            		}
	            		if (StringUtils.isNotEmpty(techRegInput.getHwsvr())) {
	            			techRegDataType[i].setHwsvr(techRegInput.getHwsvr());
	            			logger.debug("Hwsvr:"+techRegInput.getHwsvr());
	            		}
	            		if (StringUtils.isNotEmpty(techRegInput.getInads())) {
	            			techRegDataType[i].setInads(techRegInput.getInads());
	            			logger.debug("Inads:"+techRegInput.getInads());
	            		}
	            		if (StringUtils.isNotEmpty(techRegInput.getIpadd())) {
	            			techRegDataType[i].setIpadd(techRegInput.getIpadd());
	            			logger.debug("Ipadd:"+techRegInput.getIpadd());
	            		}
	            		if (StringUtils.isNotEmpty(techRegInput.getIsDOCR())) {
	            			IsDOCR.fromString(techRegInput.getIsDOCR());
	            			techRegDataType[i].setIsDOCR(IsDOCR.fromString(techRegInput.getIsDOCR()));
	            			logger.debug("IsDOCR:"+techRegInput.getIsDOCR());
	            		}
	            		if (StringUtils.isNotEmpty(techRegInput.getMcode())) {
	            			techRegDataType[i].setMcode(techRegInput.getMcode());
	            			logger.debug("Mcode:"+techRegInput.getMcode());
	            		}
	            		if (StringUtils.isNotEmpty(techRegInput.getMid())) {
	            			techRegDataType[i].setMid(String.valueOf(techRegInput.getMid()));
	            			logger.debug("Mid:"+techRegInput.getMid());
	            		}
	            		if (StringUtils.isNotEmpty(techRegInput.getNickname())) {
	            			techRegDataType[i].setNickname(techRegInput.getNickname());
	            			logger.debug("Nickname:"+techRegInput.getNickname());
	            		}

	            		if (StringUtils.isNotEmpty(techRegInput.getOptype())) {
	            			techRegDataType[i].setOptype(Optype.fromString(techRegInput.getOptype()));
	            			logger.debug("Optype:"+techRegInput.getOptype());
	            		}
	            		if (StringUtils.isNotEmpty(techRegInput.getOssno())) {
	            			techRegDataType[i].setOssno(techRegInput.getOssno());
	            			logger.debug("Ossno"+techRegInput.getOssno());
	            		}
	            		if (StringUtils.isNotEmpty(techRegInput.getPrvip())) {
	            			techRegDataType[i].setPrvip(techRegInput.getPrvip());
	            			logger.debug("Prvip:"+techRegInput.getPrvip());
	            		}

	            		if (StringUtils.isNotEmpty(techRegInput.getRelno())) {
	            			techRegDataType[i].setRelno(techRegInput.getRelno());
	            			logger.debug("RelNo:"+techRegInput.getRelno());
	            		}
	            		if (StringUtils.isNotEmpty(techRegInput.getRndpwd())) {
	            			techRegDataType[i].setRndpwd(Rndpwd.fromString(techRegInput.getRndpwd()));
	            			logger.debug("Rndpwd:"+techRegInput.getRndpwd());
	            		}

	            		if (StringUtils.isNotEmpty(techRegInput.getScode())) {
	            			techRegDataType[i].setScode(techRegInput.getScode());
	            			logger.debug("Scode:"+techRegInput.getScode());
	            		}
	            		if (StringUtils.isNotEmpty(techRegInput.getSid())) {
	            			techRegDataType[i].setSid(String.valueOf(techRegInput.getSid()));
	            			logger.debug("Sid:"+techRegInput.getSid());
	            		}

	            		if (StringUtils.isNotEmpty(techRegInput.getSvrname())) {
	            			techRegDataType[i].setSvrname(techRegInput.getSvrname());
	            			logger.debug("SvrName:"+techRegInput.getSvrname());
	            		}
	            		
	            		if (StringUtils.isNotEmpty(techRegInput.getVpmsldn())) {
	            			techRegDataType[i].setVpmsldn(techRegInput.getVpmsldn());
	            			logger.debug("Vpmsldn:"+techRegInput.getVpmsldn());
	            		}
	            		
	            		if (StringUtils.isNotEmpty(techRegInput.getAfid())) {
	            			techRegDataType[i].setAfid(techRegInput.getAfid());
	            			logger.debug("afid:"+techRegInput.getAfid());
	            		}
	            		
	            		if (StringUtils.isNotEmpty(techRegInput.getUsername())) {
	            			techRegDataType[i].setUsername(techRegInput.getUsername());
	            			logger.debug("username:"+techRegInput.getUsername());
	            		}
	            		
	            		if (StringUtils.isNotEmpty(techRegInput.getPassword())) {
	            			techRegDataType[i].setPassword(techRegInput.getPassword());
	            		}

	            		
	            		if ( (techRegInput.getParameters() != null) && (techRegInput.getParameters().getParam() != null) ) {
	            			com.avaya.v2.techregistration.ParametersType parameters = new com.avaya.v2.techregistration.ParametersType();
	            			com.grt.dto.TechRegInputDto.Param[] paramArray = techRegInput.getParameters().getParam();
	            			com.avaya.v2.techregistration.Param[] paramArrayInput = new com.avaya.v2.techregistration.Param[paramArray.length];
	            			for (int j = 0; j < paramArray.length; j++) {
	            				com.grt.dto.TechRegInputDto.Param paramArrayElem = paramArray[j];
	            			
	            				com.avaya.v2.techregistration.Param paramArrayElemInput = new com.avaya.v2.techregistration.Param();
	            				paramArrayElemInput.setKey(paramArrayElem.getKey());
	            				paramArrayElemInput.setValue(paramArrayElem.getValue());
	            				
	            				paramArrayInput[j] = paramArrayElemInput;
	            			}
	            			parameters.setParam(paramArrayInput);
	            			techRegDataType[i].setParameters(parameters);
	            		}
	            		
	        		}
	        		techRegRequestType.setTechRegData(techRegDataType);
	        		}catch (Exception e) {
	        			logger.debug("Exception while preparing the input parameters",e);
	        			throw e;
					}
	        		logger.debug("Retrying logic");
					try {
						retry_count = getMaximumRetry();
					} catch (Exception e) {
						logger.error("ERROR: While getting the property retry_count", e);
						throw new GrtSapException(e);
					}
		        	for(int i = 0;i < retry_count; i++) {
		        		try{
		        			logger.debug("Calling ART for Technical Registration");
		        			long ca1 = Calendar.getInstance().getTimeInMillis();
		        			techRegRespType = portType.techRegistration(techRegRequestType);
		        			if(techRegRespType != null) {
		        				if(techRegRespType.getTechRegAcknowledgement() != null) {
		        					logger.debug("ART Response Starts");
		        					for (TechRegAcknowledgementType ack : techRegRespType.getTechRegAcknowledgement() ) {
		        						logger.debug("TechRegId:" + ack.getTechRegId() + " ARTId:" + ack.getArtid() + " returnCode:" + ack.getCode() + " returnSubCode:" + ack.getSubCode() + " errorDesc:" + ack.getDescription());
		        					}
		        					logger.debug("ART Response Ends");
		        				}
		        			}
		    		        long ca2 = Calendar.getInstance().getTimeInMillis();
		    		        logger.debug("TIMER FOR ART CALL FOR TECHNICAL REGISTRATION >> Time in milliseconds:" + (ca2-ca1));
		    		        logger.debug("Called ART for Technical Registration");
		    	        	if (techRegRespType != null){
		    	        		long c2 = Calendar.getInstance().getTimeInMillis();
		    	    			long c = c2-c1;

		    	    			logger.debug("TIMER FOR TECHINCAL REGISTRATON >> Time in milliseconds:"+c);
		    	        		return techRegRespType;
		    	        	}
		        		} catch (IOException ioe ) {
			        		logger.error("Failed to connect FMW for Technical Registration", ioe);
			        		if(i == retry_count - 1){
			        			if(ioe.getCause() instanceof SOAPFaultException) {
			        				if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
			        					logger.error("MONITOR:[Outage:ART][ENDPOINT:"+ artEnURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
			        				} else {
			        					logger.error("MONITOR:[Outage:FMW][ENDPOINT:"+ artEnURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
			        				}
			        			} else {
			        				logger.error("FMW is down");
			        			}
			        			techRegRespType = new com.avaya.v2.techregistration.TechRegRespType();
			        		}
				        }catch(SOAPFaultException e){
				        			logger.error("Failed while sending data to FMW for Technical Registration", e);
				        			throw e;
				        }
		        	}
	        	}
	        }
	   	 logger.debug("Completed");
	     return techRegRespType;
	 }

	 
	 public DeviceTOBResponseType technicalAlamRegistration(TechRegAlarmInputDto techRegAlarmInputDto) throws Exception {
		    long c1 = Calendar.getInstance().getTimeInMillis();
	        logger.debug("................ Starting for Technical Onboarding ................");
	        String abcartEnURL = getArtAlarmEndPoint();
	        logger.debug("art step2 end point url is =================> "+abcartEnURL);
	    	String artEnURL = null;
	        int retry_count = 0;
	        int timeout = 0;
	        String systemIdentifier = "";
	        
	        DeviceTOBResponseType techRegRespType = null;
	        ExternalTOBTechRegRequestWSDL_PortType portType = null;
	        DeviceTOBRequestType techRegRequestType = null;

			try {
				portType = new ExternalTOBTechRegRequestWSDL_Service_Impl().getExternalTOBTechRegRequestWSDLSOAP();
				if(portType == null){
					logger.error("portType not acquired, exiting the process !!");
					return null;
				}
			}  catch (ServiceException serviceException) {
				logger.error("ERROR: While trying to call the ART in SOAP WSDL call for Technical Registration.", serviceException);
	        }
	        try {
	        	artEnURL = getArtAlarmEndPoint();
	            timeout = getArtConnectionTimeOut();
	            systemIdentifier = getSystemIdentifier();
	            retry_count = getMaximumRetry();
	            logger.debug("artEnURL:"+artEnURL);
	            logger.debug("timeout:"+timeout);
	            logger.debug("retry_count:"+retry_count);
	            setRemoteEndPoint((Stub)portType, artEnURL);
	        } catch(Exception e) {
	            logger.error("Failure while trying to read FMW endpoint properties for setup.", e);
	        }
	        
	        List<TechRegInputDto>  techRegInputDtoList = techRegAlarmInputDto.getTechRegInputDtoList();
	        //Prepare inputs and calling sap
	        if(techRegInputDtoList != null) {
	        	if(techRegInputDtoList.size() >0){
	        		try{
	        		logger.debug("Prepare inputs");
	        		techRegRequestType = new DeviceTOBRequestType();
	        		
	        		HeaderType headerType = new HeaderType();
	        		//This is GRT id
	        		headerType.setRequestId(techRegAlarmInputDto.getGrtId());
	        		logger.debug("RequestId:"+techRegAlarmInputDto.getGrtId());
	        		//This is System identifier 
	        		headerType.setSystemId(getSystemIdentifier());
	        		logger.debug("SystemId:"+getSystemIdentifier());
	        		headerType.setSoldTo(techRegAlarmInputDto.getSoldTo());
	        		logger.debug("SoldTo:"+techRegAlarmInputDto.getSoldTo());
	        		
	        		techRegRequestType.setHeader(headerType);

	        		DeviceType[] techRegDataType = new DeviceType[techRegInputDtoList.size()];

	        		for(int i=0; i < techRegDataType.length;i++ ){
	        			TechRegInputDto techRegInput = techRegInputDtoList.get(i);
	        			techRegDataType[i] = new DeviceType();
	        			
	        			if (StringUtils.isNotEmpty(techRegInput.getTechRegId())) {
	            			techRegDataType[i].setDeviceTOBId(techRegInput.getTechRegId());
	            			logger.debug("TechRegId:"+techRegInput.getTechRegId());
	            		}
	        			
	        			if (StringUtils.isNotEmpty(techRegInput.getMainSeid())) {
	            			techRegDataType[i].setSEID(techRegInput.getMainSeid());
	            			logger.debug("MainSEID:"+techRegInput.getMainSeid());
	            		}
	        			if (StringUtils.isNotEmpty(techRegInput.getGatewaySEID())) {
	        				//GRT 4.0 UAT Defect#862 : For Duplex SAL gateway(having primary & secondary salgw)
	        				//Pass only Primary salgw to the ART for alarming(Discussed with Erik & Vijay)
	        				if( techRegInput.getGatewaySEID()!=null && techRegInput.getGatewaySEID().contains("+") ){
	        					String[] gwArr = techRegInput.getGatewaySEID().split("\\+");
	        					techRegDataType[i].setGatewaySEID(gwArr[0].trim());
	        				}else{
	        					techRegDataType[i].setGatewaySEID(techRegInput.getGatewaySEID());
	        				}
	            			logger.debug("GatewaySEID:"+techRegInput.getGatewaySEID());
	            		}
	            		if (StringUtils.isNotEmpty(techRegInput.getAlmid())) {
	            			techRegDataType[i].setAlarmDestinationInfo(techRegInput.getAlmid().toString());
	            			logger.debug("Almid:"+techRegInput.getAlmid().toString());
	            		}
	            		
	            		if (StringUtils.isNotEmpty(techRegInput.getAccessType())) {
	            			techRegDataType[i].setAccessType(AccessType.fromString(techRegInput.getAccessType().toUpperCase()));
	            			logger.debug("Access type:"+techRegInput.getAccessType());
	            		}
	            		
	            		if (StringUtils.isNotEmpty(techRegInput.getOptype())) {
	            			techRegDataType[i].setOperation(Operation.fromString(techRegInput.getOptype()));
	            			logger.debug("Op type"+techRegInput.getOptype());
	            		}
	            			            		
	            		if ( (techRegInput.getParameters() != null) && (techRegInput.getParameters().getParam() != null) ) {
	            			com.avaya.registration.Parameters parameters = new com.avaya.registration.Parameters();
	            			com.grt.dto.TechRegInputDto.Param[] paramArray = techRegInput.getParameters().getParam();
	            			com.avaya.registration.Param[] paramArrayInput = new com.avaya.registration.Param[paramArray.length];
	            			for (int j = 0; j < paramArray.length; j++) {
	            				com.grt.dto.TechRegInputDto.Param paramArrayElem = paramArray[j];
	            			
	            				com.avaya.registration.Param paramArrayElemInput = new com.avaya.registration.Param();
	            				paramArrayElemInput.setKey(paramArrayElem.getKey());
	            				paramArrayElemInput.setValue(paramArrayElem.getValue());
	            				
	            				paramArrayInput[j] = paramArrayElemInput;
	            			}
	            			parameters.setParam(paramArrayInput);
	            			techRegDataType[i].setParameters(parameters);
	            		}
	        		}
	        		DevicesType devicesType = new DevicesType();
	        		devicesType.setDevice(techRegDataType);
	        		techRegRequestType.setDevices(devicesType);
	        		}catch (Exception e) {
	        			logger.debug("Exception while preparing the input parameters",e);
	        			throw e;
					}
	        		logger.debug("Retrying logic");
					try {
						retry_count = getMaximumRetry();
					} catch (Exception e) {
						logger.error("ERROR: While getting the property retry_count", e);
						throw new GrtSapException(e);
					}
		        	for(int i = 0;i < retry_count; i++) {
		        		try{
		        			String resultCode = null;
		        			
		        			logger.debug("Calling ART for Technical Onboarding");
		        			long ca1 = Calendar.getInstance().getTimeInMillis();
		        			techRegRespType = portType.tOBRequest(techRegRequestType);
		        			if(techRegRespType != null) {
		        				if(techRegRespType.getHeader() != null) {
		        					logger.debug("ART Response Starts");
		        					ResponseHeaderType responseHeaderType = techRegRespType.getHeader();
		        					resultCode = responseHeaderType.getStatus();
		        					logger.debug("TechRegId:" + responseHeaderType.getRequestId() + " SystemId:" + responseHeaderType.getSystemId() + " returnCode:" + responseHeaderType.getStatus());		        					
		        					logger.debug("ART Response Ends");
		        				}
		        			}
		    		        long ca2 = Calendar.getInstance().getTimeInMillis();
		    		        logger.debug("TIMER FOR ART CALL FOR TECHNICAL ONBOARDING >> Time in milliseconds:" + (ca2-ca1));
		    		        logger.debug("Called ART for Technical Registration");
		    	        	if ( (StringUtils.isNotEmpty(resultCode)) && (GRTConstants.SUCCESS.equalsIgnoreCase(resultCode)) ) {
		    	        		long c2 = Calendar.getInstance().getTimeInMillis();
		    	    			long c = c2-c1;

		    	    			logger.debug("TIMER FOR TECHINCAL ONBOARDING >> Time in milliseconds:"+c);
		    	        		return techRegRespType;
		    	        	}
		        		} catch (IOException ioe ) {
			        		logger.error("Failed to connect FMW for Technical Onboarding", ioe);
			        		if(i == retry_count - 1){
			        			if(ioe.getCause() instanceof SOAPFaultException) {
			        				if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
			        					logger.error("MONITOR:[Outage:ART][ENDPOINT:"+ artEnURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
			        				} else {
			        					logger.error("MONITOR:[Outage:FMW][ENDPOINT:"+ artEnURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
			        				}
			        			} else {
			        				logger.error("FMW is down");
			        			}
			        			techRegRespType = new com.avaya.registration.DeviceTOBResponseType();
			        		}
				        }catch(SOAPFaultException e){
				        			logger.error("Failed while sending data to FMW for Technical Onboarding", e);
				        			throw e;
				        }
		        	}
	        	}
	        }
	        logger.debug("................ Completed for Technical Onboarding ................");
	     return techRegRespType;
	 }

	 
	 /////////////////////End Technical Registration from Create New SALGateway

    /**
     * API to introspect SALGateway by SalConcentrator.
     *
     * @param gatewaySeid String
     * @return DTO SALGatewayIntrospection
     */
	 public SALGatewayIntrospection introspectSALGateway(String gatewaySeid) throws Throwable {
		long c1 = Calendar.getInstance().getTimeInMillis();
		logger.debug("Entering into ARTClient.introspectSALGateway()");
		SALGatewayIntrospection salGatewayIntrospection  = null;
		String salConcentratorEnURL = null;
		int retry_count = 0;
		int timeout = 0;
		logger.debug("The gatewaySeid id ====================> :: "+gatewaySeid);
		SALConcentratorWSDL_PortType portType = null;

		try {
		      portType = new SALConcentratorWSDL_Service_Impl().getSALConcentratorWSDLSOAP();

		      logger.debug("----------PorType---------"+portType.toString());
		} catch (ServiceException serviceException) {
		      String errMsg = "Failure when getting the port type from the service locator";
		      logger.error("ERROR:" + errMsg, serviceException);
		      throw new GrtSapException(errMsg);
		}

		try {
		    salConcentratorEnURL = getSalConcentratorEndpoint();
		    timeout = getArtConnectionTimeOut();
		    //systemIdentifier = GRTPropertiesUtil.getProperty("system_identifier").trim();
		    retry_count = getMaximumRetry();
		    logger.debug("artEnURL:"+salConcentratorEnURL);
		    logger.debug("timeout:"+timeout);
		    logger.debug("retry_count:"+retry_count);
		    setRemoteEndPoint((Stub)portType, salConcentratorEnURL);
		} catch(Exception e) {
		    logger.error("Failure while trying to read FMW endpoint properties for setup.", e);
		    throw new GrtSapException(e);
		}

		SEID seid = new SEID();
		seid.setType(GRTConstants.SAL_GATEWAY_FOR_SAL_CONCENTRATOR);
		seid.set_value(gatewaySeid);
		logger.debug("                                   >>>>>>>>>" + seid + "<<<<<<<<<<<                                             ");
		SEID[] seidArray = new SEID[1];
		seidArray[0] = seid;

		SALDeviceQueryRequest sALDeviceQueryRequest = new SALDeviceQueryRequest();
		sALDeviceQueryRequest.setSEID(seidArray);

		SALDeviceQueryResponse  sALDeviceQueryResponse = null;
		Device_Element[] deviceElements = null;
		for(int i = 0;i < retry_count; i++) {
		      try{
		        logger.debug("<-------------------- Before SalConcentrator Service Call ----------------------->");
		        long ca1 = Calendar.getInstance().getTimeInMillis();
		        sALDeviceQueryResponse = portType.sALConcentrator(sALDeviceQueryRequest);
		        long ca2 = Calendar.getInstance().getTimeInMillis();
		        logger.debug("TIMER FOR ART CALL TO FETCH SALGatewayIntrospection DATA FOR gatewaySeid:"+gatewaySeid+". Time in milliseconds:" + (ca2-ca1));
		        logger.debug("<-------------------- Before SalConcentrator Service Call ----------------------->");
		        deviceElements = sALDeviceQueryResponse.getDevice();

		      } catch (IOException ioe ) {
		      	logger.error("Failed to connect to FMW for Sal Concertrator", ioe);
        		if(i == retry_count - 1){
        			if(ioe.getCause() instanceof SOAPFaultException) {
        				if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
        					logger.error("MONITOR:[Outage:SAL CONCENTRATOR][ENDPOINT:"+ salConcentratorEnURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
        				} else {
        					logger.error("MONITOR:[Outage:FMW][ENDPOINT:"+ salConcentratorEnURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
        				}
        			} else {
        				logger.error("FMW is down");
        			}
        		}
		      }catch(SOAPFaultException e){
		      	logger.error("Failed while sending data to FMW for Sal Concertrator", e);
		            throw e;
		      }
		}
		logger.debug("The content of the deviceElements i.e. Response from Webservice is ==========> :: "+ deviceElements);

		if (deviceElements != null && deviceElements.length > 0) {
		      salGatewayIntrospection = new SALGatewayIntrospection();
		      salGatewayIntrospection.setGatewaySEID(gatewaySeid);
		      //salGatewayIntrospection.setAccount(sALDeviceQueryResponseSEID[0].A);
		      if(deviceElements[0].getExistence() != null && StringUtils.isNotEmpty(deviceElements[0].getExistence().getValue())) {
		    	    logger.debug("                                                                              ");
		            logger.debug(" deviceElements[0].getExistence().getValue()                      >>>>>>>>>>" + deviceElements[0].getExistence().getValue() + "<<<<<<<<<<<     deviceElements[0].getExistence().getValue()                    ");
		            logger.debug("                       >>>>>>>>>>" + deviceElements[0].getSEID() + "<<<<<<<<<<<                                           ");
		            logger.debug("                                                                              ");
		      }

		      ManagedDevices mds = deviceElements[0].getManagedDevices();
		      logger.debug("The content of the ManagedDevices ==========> :: "+ mds);
		      if (mds != null) {
		      Device_Element1[] deviceElement1Array = mds.getDevice();
		      logger.debug("The content of the deviceElement1Array lenght ==========> :: "+ deviceElement1Array.length);
		      List<ManagedDevice> manageDevices = new ArrayList<ManagedDevice>();
		      for (int i = 0; i<deviceElement1Array.length ; i++) {
		            Device_Element1 deviceElement1 = deviceElement1Array[i];
		            logger.debug("The content of the deviceElement1  ==========> :: "+ deviceElement1);
		            ManagedDevice md = new ManagedDevice();
		            md.setLastAlarmReceivedDate(deviceElement1.getLastAlarmReceiptTimestamp());
		            md.setLastContact(deviceElement1.getLastConnectivity().getLastContact());
		            md.setSeCode(deviceElement1.getSECODE());
		            md.setSeid(deviceElement1.getSEID());
		            if(deviceElement1.getLastConnectivity() != null 
		            		&& deviceElement1.getLastConnectivity().getStatus() != null){
		            	logger.debug("deviceElements[iVal] LastConnectivity LastContact() >>>> :: " + deviceElement1.getLastConnectivity().getLastContact());
		            	logger.debug("deviceElement1 LastConnectivity Status Value()  ==========> :: " + deviceElement1.getLastConnectivity().getStatus().getValue());
			            md.setStatus(deviceElement1.getLastConnectivity().getStatus().getValue());
			            if(deviceElement1.getLastConnectivity().getStatus().getValue() == null 
			            		|| GRTConstants.STATUS_NULL.equalsIgnoreCase(deviceElement1.getLastConnectivity().getStatus().getValue())){
		                   	 if(deviceElement1.getLastConnectivity().getLastContact() == null){
		               			 md.setStatus(GRTConstants.STATUS_NEVER);
		               		 } else {
		               			 md.setStatus(GRTConstants.STATUS_BAD);
		               		 }
		               	}
		            }
		            manageDevices.add(md);
		      }
		      salGatewayIntrospection.setManagedDevices(manageDevices);
		      }
		      logger.debug("deviceElements[0].getGatewayVersion()  ==========> :: "+ deviceElements[0].getGatewayVersion());
		      salGatewayIntrospection.setGatewaySEID(deviceElements[0].getGatewayVersion());
		      //salGatewayIntrospection.setRegistrationDate(new Date(deviceElements[0].getRegistration()));
		      logger.debug("deviceElements[0].getPingRate()  ==========> :: "+ deviceElements[0].getPingRate());
		      if (deviceElements[0].getPingRate()!= null ){
		            salGatewayIntrospection.setPingRate(Integer.parseInt(deviceElements[0].getPingRate()));
		      }
		      // Begin TODO
		      // Populate the Product Family and Account
		      //salGatewayIntrospection.setProductFamily();
		      //salGatewayIntrospection.setAccount(account);
		      // End TODO
		}
        long c2 = Calendar.getInstance().getTimeInMillis();
        logger.debug("TIMER TO FETCH THE SAL CONCENTRATOR DATA >> Time in milliseconds:" + (c2-c1));
        logger.debug("Exiting from ARTClient.introspectSALGateway()");
        return salGatewayIntrospection;
	 }


 	  /**
	     * API to get SalAlarm Connectivity Details.
	     *
	     * @param seids List
	     * @return DTO SALGatewayIntrospection
	     */
     public SALGatewayIntrospection getSalAlarmConnectivityDetails(List<String> seids) throws Throwable {
         long c1 = Calendar.getInstance().getTimeInMillis();
         logger.debug("Entering into ARTClient.getSalAlarmConnectivityDetails()");
         SALGatewayIntrospection salGatewayIntrospection = new SALGatewayIntrospection();
         List<ManagedDevice> manageDevices = new ArrayList<ManagedDevice>();
         String salConcentratorEnURL = null;
         int retry_count = 0;
         int timeout = 0;
         SALConcentratorWSDL_PortType portType = null;

         try {
               portType = new SALConcentratorWSDL_Service_Impl().getSALConcentratorWSDLSOAP();

               logger.debug("----------PorType---------"+portType.toString());
         } catch (ServiceException serviceException) {
               String errMsg = "Failure when getting the port type from the service locator";
               logger.error("ERROR:" + errMsg, serviceException);
               throw new GrtSapException(errMsg);
         }

         try {
             salConcentratorEnURL = getSalConcentratorEndpoint();
             timeout = getArtConnectionTimeOut();
             //systemIdentifier = GRTPropertiesUtil.getProperty("system_identifier").trim();
             retry_count = getMaximumRetry();
             logger.debug("artEnURL:"+salConcentratorEnURL);
             logger.debug("timeout:"+timeout);
             logger.debug("retry_count:"+retry_count);
             setRemoteEndPoint((Stub)portType, salConcentratorEnURL);
         } catch(Exception e) {
             logger.error("Failure while trying to read FMW endpoint properties for setup.", e);
         }
		         if(seids.size()>0){
		         SEID[] seidArray = new SEID[seids.size()];

		         for (int i=0; i<seids.size(); i++) {
		               SEID seid = new SEID();
		               seid.setType(GRTConstants.DEVICE_FOR_SAL_CONCENTRATOR);
		               seid.set_value(seids.get(i));
		               logger.debug(" >>>>>>>>>" + seids.get(i) + "<<<<<<<<<<< ");
		               seidArray[i] = seid;
		         }

		         SALDeviceQueryRequest sALDeviceQueryRequest = new SALDeviceQueryRequest();
		         sALDeviceQueryRequest.setSEID(seidArray);

		         SALDeviceQueryResponse  sALDeviceQueryResponse = null;
		         Device_Element[] deviceElements = null;
		         for(int intVal = 0;intVal < retry_count; intVal++) {
		               try{
		                 logger.debug("<-------------------- Before SalConcentrator Service Call ----------------------->");
		                 long ca1 = Calendar.getInstance().getTimeInMillis();
		                 sALDeviceQueryResponse = portType.sALConcentrator(sALDeviceQueryRequest);
		                 long ca2 = Calendar.getInstance().getTimeInMillis();
		                 deviceElements = sALDeviceQueryResponse.getDevice();

		               } catch (IOException ioe ) {
		                     logger.error("Failed to connect to FMW for Sal Concertrator", ioe);
		                     if(intVal == retry_count - 1){
		             			if(ioe.getCause() instanceof SOAPFaultException) {
		             				if(((SOAPFaultException)ioe.getCause()).getFaultString().contains(GRTConstants.acsbi_errorDescription)) {
		             					logger.error("MONITOR:[Outage:SAL CONCENTRATOR][ENDPOINT:"+ salConcentratorEnURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
		             				} else {
		             					logger.error("MONITOR:[Outage:FMW][ENDPOINT:"+ salConcentratorEnURL + "][CAUSE:" + ((SOAPFaultException)ioe.getCause()).getFaultString() + "]");
		             				}
		             			} else {
		             				logger.error("FMW is down");
		             			}
		             		}
		               }catch(SOAPFaultException e){
		                     logger.error("Failed while sending data to FMW for Sal Concertrator", e);
		                     throw e;
		               }
		         }
			         logger.debug("The content of the deviceElements i.e. Response from Webservice is ==========> :: "+ deviceElements);
			         if( deviceElements == null ){
			        	 logger.debug("Data is null from Sal concentrator service");
			         }

			         if(!(deviceElements!=null)){
						salGatewayIntrospection.setReturnCode("Failed");
					}


		         if (deviceElements != null && deviceElements.length > 0) {
		        	 for(int iVal=0;iVal<deviceElements.length; iVal++ ){
		               
		               ManagedDevice md = new ManagedDevice();
		               
		               if(deviceElements[iVal].getExistence() != null && StringUtils.isNotEmpty(deviceElements[iVal].getExistence().getValue())){
		            	   md.setExistence(deviceElements[iVal].getExistence().getValue());
		            	   logger.debug(">>>>>>>>>>" + deviceElements[iVal].getSEID() + "<<<<<<<<<<<");
		            	   logger.debug(" deviceElements["+iVal+"].getExistence().getValue()>>>>>>>>>>" + deviceElements[iVal].getExistence().getValue() + "<<<<<<<<<<<deviceElements["+iVal+"].getExistence().getValue()");
		               }
		               
	                     logger.debug("deviceElement1.getLastAlarmReceiptTimestamp()>>>>>"+deviceElements[iVal].getLastAlarmReceiptTimestamp());
	                     if(deviceElements[iVal].getLastAlarmReceiptTimestamp()!=null){
	                    	 md.setLastAlarmReceivedDate( deviceElements[iVal].getLastAlarmReceiptTimestamp());
	                     }
	                     else
	                     {
	                    	 md.setLastAlarmReceivedDate("");
	                     }
	                     if((deviceElements[iVal].getLastConnectivity())!=null &&(deviceElements[iVal].getLastConnectivity().getLastContact())!=null){
	                    	 md.setLastContact(deviceElements[iVal].getLastConnectivity().getLastContact());
	                     }
	                     if(deviceElements[iVal].getSECODE()!=null){
	                    	 md.setSeCode(deviceElements[iVal].getSECODE());
	                     }
	                     if(deviceElements[iVal].getSEID()!=null){
	                    	 md.setSeid(deviceElements[iVal].getSEID());
	                     }
	                     if(deviceElements[iVal].getLastConnectivity() != null  
	                    		 && deviceElements[iVal].getLastConnectivity().getStatus() != null){
	                    	 logger.debug("deviceElements LastConnectivity : " + deviceElements[iVal].getLastConnectivity());
	                    	 logger.debug("deviceElements LastConnectivity Status: " + deviceElements[iVal].getLastConnectivity().getStatus());
		                     logger.debug("deviceElements LastConnectivity Status Value: "+deviceElements[iVal].getLastConnectivity().getStatus().getValue());
		                     md.setStatus(deviceElements[iVal].getLastConnectivity().getStatus().getValue());
	                    	 if(deviceElements[iVal].getLastConnectivity().getStatus().getValue() == null 
	                    			 || GRTConstants.STATUS_NULL.equalsIgnoreCase(deviceElements[iVal].getLastConnectivity().getStatus().getValue())){
		                    	 if(deviceElements[iVal].getLastConnectivity().getLastContact() == null){
	                    			 md.setStatus(GRTConstants.STATUS_NEVER);
	                    		 } else {
	                    			 md.setStatus(GRTConstants.STATUS_BAD);
	                    		 }
	                    	 }
	                     }
	                    if((deviceElements[iVal].getGatewayVersion())!=null){
			               salGatewayIntrospection.setGatewaySEID(deviceElements[iVal].getGatewayVersion());
	                    }
			               //salGatewayIntrospection.setRegistrationDate(new Date(deviceElements[0].getRegistration()));
			               logger.debug("deviceElements["+iVal+"].getPingRate()  ==========> :: "+ deviceElements[iVal].getPingRate());
			               if (deviceElements[iVal].getPingRate()!= null ){
			                     salGatewayIntrospection.setPingRate(Integer.parseInt(deviceElements[iVal].getPingRate()));
			               }

	                     manageDevices.add(md);
	                    
		               }
		        	 salGatewayIntrospection.setManagedDevices(manageDevices);
		           }
		      }
		logger.debug("Exiting ArtClient.getSalAlarmConnectivityDetails");
       return salGatewayIntrospection;
   }

   /* START :: System to System SAL GW Installer */
    
     public ARTResponseType artSalGWTechRegOld(SALGatewayInstallerDto salInstallerDto) throws Exception {
         logger.debug("Entering artSalGWTechRegOld...");
         ARTResponseType artTRSync = null;
         String systemIdentifier = getSystemIdentifier();
         try {
        	
        	  ExternalTRSynchWSDL_PortType portType  = new ExternalTRSynchWSDL_Service_Impl().getExternalTRSynchWSDLSOAP();
         	  TRRequestType trRequestType = new TRRequestType();
         	  
         	  logger.debug("Printing request parameters..");
         	  //Set the parameters
         	  trRequestType.setAlarmRequired( salInstallerDto.getAorig() );
         	  	logger.debug("Aorig : "+  salInstallerDto.getAorig());
         	  trRequestType.setProductReferenceKey( salInstallerDto.getProductIdentifier() );
         	  	logger.debug("ProductIdentifier : "+   salInstallerDto.getProductIdentifier() );
         	  trRequestType.setGroupId( salInstallerDto.getGroupId() );
         	  	logger.debug("GroupId : "+   salInstallerDto.getGroupId() );
         	  trRequestType.setRequestId( salInstallerDto.getGrtId() );
         	  	logger.debug("GrtId : "+   salInstallerDto.getGrtId() );
         	  trRequestType.setRequestorId( salInstallerDto.getUserId() );
         	  	logger.debug("UserId : "+   salInstallerDto.getUserId() );
         	  trRequestType.setSerialNum( salInstallerDto.getSerialNumber() );
         	  	logger.debug("SerialNumber : "+   salInstallerDto.getSerialNumber() );
         	  trRequestType.setSoldTo( salInstallerDto.getSoldToId() );
         	  	logger.debug("SoldToId : "+   salInstallerDto.getSoldToId() );
         	  trRequestType.setSystemId( systemIdentifier );
         	  	logger.debug("systemIdentifier : "+  systemIdentifier );
         	  	
         	  	//Set the additional Parameter for Nick Name
         		logger.debug("Setting Nick Name : "+  salInstallerDto.getNickName());
         	  	Parameters params = new Parameters();
         	  	ParameterType [] pTypeArr = new ParameterType[1];
         	  	ParameterType pTypeObj = new ParameterType();
         	  	pTypeObj.setKey(GRTConstants.SNICK);
         	  	pTypeObj.setValue(salInstallerDto.getNickName());
         	  	pTypeArr[0] = pTypeObj;
         	  	params.setParam(pTypeArr);
         	  	trRequestType.setParameters(params);
         	  	
         	setRemoteEndPoint((Stub)portType, getArtSalgwOldEp());
         	
         	//Call the Art Service
         	artTRSync = portType.tRSync(trRequestType);
         	
         	if( artTRSync!=null ){
         		 logger.debug("Response from ART : "+artTRSync.toString());
         	}
         	
         	/*OutputType outputType = artTRSync.getOutput();
         	ProductDetailsType proType = outputType.getProductDetails();
         	RegResultType regType = outputType.getRegResult();
         	String artId = regType.getARTID();
         	String returnCode = regType.getReturnCode();
         	String description = regType.getDescription();*/
         	  
         } catch (Throwable throwable) {
         	logger.error("Error in artSalGWTechRegOld:"+throwable);
         } finally {
             logger.debug("Exiting artSalGWTechRegOld");
         }
         return artTRSync;
     } 
     
     
     public TRResponseType artSalGWTechRegNew(SALGatewayInstallerDto salInstallerDto) throws Exception {
         logger.debug("Entering artSalGWTechRegNew");
         TRResponseType trResponseType = null;
         String systemIdentifier = getSystemIdentifier();
         try {
        	
        	 com.grt.integration.art.v1.salgw.ExternalTRSynchWSDL_PortType portType = 
        			 new com.grt.integration.art.v1.salgw.ExternalTRSynchWSDL_Service_Impl().getExternalTRSynchWSDLSOAP();
        	 
        	  TRRequestType trRequestType = new TRRequestType();
         	  
        	  logger.debug("Printing request parameters..");
         	  //Set the parameters
         	  trRequestType.setAlarmRequired( salInstallerDto.getAorig() );
         	  	logger.debug("Aorig : "+  salInstallerDto.getAorig());
         	  trRequestType.setProductReferenceKey( salInstallerDto.getProductIdentifier() );
         	  	logger.debug("ProductIdentifier : "+   salInstallerDto.getProductIdentifier() );
         	  trRequestType.setGroupId( salInstallerDto.getGroupId() );
         	  	logger.debug("GroupId : "+   salInstallerDto.getGroupId() );
         	  trRequestType.setRequestId( salInstallerDto.getGrtId() );
         	  	logger.debug("GrtId : "+   salInstallerDto.getGrtId() );
         	  trRequestType.setRequestorId( salInstallerDto.getUserId() );
         	  	logger.debug("UserId : "+   salInstallerDto.getUserId() );
         	  trRequestType.setSerialNum( salInstallerDto.getSerialNumber() );
         	  	logger.debug("SerialNumber : "+   salInstallerDto.getSerialNumber() );
         	  trRequestType.setSoldTo( salInstallerDto.getSoldToId() );
         	  	logger.debug("SoldToId : "+   salInstallerDto.getSoldToId() );
         	  trRequestType.setSystemId( systemIdentifier );
         	  	logger.debug("systemIdentifier : "+  systemIdentifier );
         	  
         	 //Set the additional Parameter for Nick Name
         		logger.debug("Setting Nick Name : "+  salInstallerDto.getNickName());
         	  	Parameters params = new Parameters();
         	  	ParameterType [] pTypeArr = new ParameterType[1];
         	  	ParameterType pTypeObj = new ParameterType();
         	  	pTypeObj.setKey(GRTConstants.SNICK);
         	  	pTypeObj.setValue(salInstallerDto.getNickName());
         	  	pTypeArr[0] = pTypeObj;
         	  	params.setParam(pTypeArr);
         	  	trRequestType.setParameters(params);
         	  
         	setRemoteEndPoint((Stub)portType, getArtSalgwNewEp());
         	
         	//Call the Art Service
            trResponseType = portType.tRSync(trRequestType);
            
            if( trResponseType!=null ){
        		 logger.debug("Response from ART : "+trResponseType.toString());
        	}
           /* trResponseType.getErrorMsg();
            trResponseType.getStatus();
            ProductDetailsType prType = trResponseType.getProductDetails();
            prType.getAlarmID();
            prType.getSEID();*/
         } catch (Throwable throwable) {
         	logger.error("Error in artSalGWTechRegNew:"+throwable);
         } finally {
             logger.debug("Exiting artSalGWTechRegNew");
         }
         return trResponseType;
     } 
     
   /* END :: System to System SAL GW Installer */
     
	public int getMaximumRetry() {
		return maximumRetry;
	}

	public void setMaximumRetry(int maximumRetry) {
		this.maximumRetry = maximumRetry;
	}

	public int getMaximumFMWRetry() {
		return maximumFMWRetry;
	}

	public void setMaximumFMWRetry(int maximumFMWRetry) {
		this.maximumFMWRetry = maximumFMWRetry;
	}

	public String getSystemIdentifier() {
		return systemIdentifier;
	}

	public void setSystemIdentifier(String systemIdentifier) {
		this.systemIdentifier = systemIdentifier;
	}

	public String getArtEndPoint() {
		return artEndPoint;
	}

	public void setArtEndPoint(String artEndPoint) {
		this.artEndPoint = artEndPoint;
	}

	public String getArtUser() {
		return artUser;
	}

	public void setArtUser(String artUser) {
		this.artUser = artUser;
	}

	public String getArtPasswd() {
		return artPasswd;
	}

	public void setArtPasswd(String artPasswd) {
		this.artPasswd = artPasswd;
	}

	public int getArtConnectionTimeOut() {
		return artConnectionTimeOut;
	}

	public void setArtConnectionTimeOut(int artConnectionTimeOut) {
		this.artConnectionTimeOut = artConnectionTimeOut;
	}

	public int getArtSocketTimeOut() {
		return artSocketTimeOut;
	}

	public void setArtSocketTimeOut(int artSocketTimeOut) {
		this.artSocketTimeOut = artSocketTimeOut;
	}

	public String getAlsbUser() {
		return alsbUser;
	}

	public void setAlsbUser(String alsbUser) {
		this.alsbUser = alsbUser;
	}

	public String getAlsbPasswd() {
		return alsbPasswd;
	}

	public void setAlsbPasswd(String alsbPasswd) {
		this.alsbPasswd = alsbPasswd;
	}

	public String getAcsbiPollingUrl() {
		return acsbiPollingUrl;
	}

	public void setAcsbiPollingUrl(String acsbiPollingUrl) {
		this.acsbiPollingUrl = acsbiPollingUrl;
	}

	public String getAfidEndPoint() {
		return afidEndPoint;
	}

	public void setAfidEndPoint(String afidEndPoint) {
		this.afidEndPoint = afidEndPoint;
	}

	public String getSidMidEndPoint() {
		return sidMidEndPoint;
	}

	public void setSidMidEndPoint(String sidMidEndPoint) {
		this.sidMidEndPoint = sidMidEndPoint;
	}

	public String getAcsbiValidatesidUrl() {
		return acsbiValidatesidUrl;
	}

	public void setAcsbiValidatesidUrl(String acsbiValidatesidUrl) {
		this.acsbiValidatesidUrl = acsbiValidatesidUrl;
	}

	public String getSalConcentratorEndpoint() {
		return salConcentratorEndpoint;
	}

	public void setSalConcentratorEndpoint(String salConcentratorEndpoint) {
		this.salConcentratorEndpoint = salConcentratorEndpoint;
	}

	public PasswordUtil getPasswordUtils() {
		return passwordUtils;
	}

	public void setPasswordUtils(PasswordUtil passwordUtils) {
		this.passwordUtils = passwordUtils;
	}

	private String decryptAlsbPasswd() {
		return PasswordUtil.decrypt(getAlsbPasswd());
	}
    
	private String decryptArtPasswd() {
		return PasswordUtil.decrypt(getArtPasswd());
		
	}

	public String getArtAlarmEndPoint() {
		return artAlarmEndPoint;
	}

	public void setArtAlarmEndPoint(String artAlarmEndPoint) {
		this.artAlarmEndPoint = artAlarmEndPoint;
	}

	public static String getAlarmId() {
		return ALARM_ID;
	}

	public String getArtSalgwOldEp() {
		return artSalgwOldEp;
	}

	public void setArtSalgwOldEp(String artSalgwOldEp) {
		this.artSalgwOldEp = artSalgwOldEp;
	}

	public String getArtSalgwNewEp() {
		return artSalgwNewEp;
	}

	public void setArtSalgwNewEp(String artSalgwNewEp) {
		this.artSalgwNewEp = artSalgwNewEp;
	}
 }
