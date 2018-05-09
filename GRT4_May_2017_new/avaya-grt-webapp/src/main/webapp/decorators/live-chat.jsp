<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/includes/context.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	
	<%
		//For avoiding caching issue
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", 0); // Proxies.
		response.setHeader("X-Powered-By","NONE");
	%>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<!--  Start of CHAT MODULE -->
	<%-- <!-- import AvayaClientServices and its dependencies -->
	<link href="<c:url context="${ _path }" value="/styles/live-chat/jquery-ui.min.css" />" rel="stylesheet" type="text/css" />
	<link href="<c:url context="${ _path }" value="/styles/live-chat/live-assist.css" />" rel="stylesheet" type="text/css" />
	<link href="<c:url context="${ _path }" value="/styles/live-chat/style.css" />" rel="stylesheet" type="text/css" />
	<link href="<c:url context="${ _path }" value="/styles/live-chat/style_custom.css" />" rel="stylesheet" type="text/css" />
    
    <script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/plugins/jquery-1.7.1.min.js" />"></script>
	 
	<!-- <script src="/scripts/live-chat/jquery.min.js" type="text/javascript"></script> -->
	<!-- <script src="/scripts/live-chat/jquery-ui.min.js" type="text/javascript"></script> -->
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/jquery.tabSlideOut.v1.3.js"/>"></script>
	
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/xmlToJSON.js"/>"></script>
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/AvayaClientServices.min.js"/>"></script>
	<!-- Added for Phase 2  -->	
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/AvayaDataStoreClient-1.4.min.js"/>"></script>
	<!-- Added for phase 2 ends -->
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/global.js"/>"></script>
    <script src="<c:url context="${ _path }" value="/scripts/live-chat/dialcodes.js"/>"></script>
    <script src="<c:url context="${ _path }" value="/scripts/live-chat/links.js"/>"></script>
	<!-- Context Store -->
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/contextStore.js"/>"></script>

	<!-- CoBrowse dependencies -->
	<!-- build:js js/avayaChat.js -->

   	<script src="<c:url context="${ _path }" value="/scripts/live-chat/webChatConfig.js"/>"></script>
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/webChatLogon.js"/>"></script>
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/webChatSocket.js"/>"></script>
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/webChat.js"/>"></script>
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/webChatUI.js"/>"></script>
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/webChatUI_Custom.js"/>"></script>
	
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/LAB.min.js"/>"></script>
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/coBrowse.js"/>"></script>
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/coBrowseUI.js"/>"></script>
		
    <!--   <link href="assets/css/chat.css" rel="stylesheet"> -->    	
    
	<link href="<c:url context="${ _path }" value="/styles/live-chat/cbe.css" />" rel="stylesheet" type="text/css" />
	<link href="<c:url context="${ _path }" value="/styles/live-chat/cobrowseUI.css" />" rel="stylesheet" type="text/css" />
	<link href="<c:url context="${ _path }" value="/styles/live-chat/cobrowse.css" />" rel="stylesheet" type="text/css" />
	
	<!-- Context Store -->
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/contextStore.js"/>"></script>
 --%>	
    <!-- endbuild -->
    
    
    
    
    
    
    <!-- New Include :: Test  -->
    
    
    <!-- build:css css/style.min.css -->
	<link href="<c:url context="${ _path }" value="/styles/live-chat/style.css" />" rel="stylesheet" type="text/css" />
	<link href="<c:url context="${ _path }" value="/styles/live-chat/style_custom.css" />" rel="stylesheet" type="text/css" />
	<!-- endbuild-->
	<link href="<c:url context="${ _path }" value="/styles/live-chat/jquery-ui.min.css" />" rel="stylesheet" type="text/css" />

	<script src="<c:url context="${ _path }" value="/scripts/live-chat/jquery.min.js"/>"></script>
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/jquery-ui.min.js"/>"></script>
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/jquery.tabSlideOut.v1.3.js"/>"></script>
	
	<!-- import AvayaClientServices and its dependencies -->
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/xmlToJSON.js"/>"></script>
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/AvayaClientServices.min.js"/>"></script>
	
	
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/global.js"/>"></script>
    <script src="<c:url context="${ _path }" value="/scripts/live-chat/dialcodes.js"/>"></script>
    <script src="<c:url context="${ _path }" value="/scripts/live-chat/links.js"/>"></script>
	<!-- build:js js/avayaChat.js -->
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/webChatConfig.js"/>"></script>
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/webChatLogon.js"/>"></script>
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/webChatSocket.js"/>"></script>
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/webChat.js"/>"></script>
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/webChatUI.js"/>"></script>
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/webChatUI_Custom.js"/>"></script>
	<!-- endbuild -->
	
	<link href="<c:url context="${ _path }" value="/styles/live-chat/live-assist.css" />" rel="stylesheet" type="text/css" />
	
	<!-- CoBrowse dependencies -->
	
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/LAB.min.js"/>"></script>
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/coBrowse.js"/>"></script>
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/coBrowseUI.js"/>"></script>
	
	<link href="<c:url context="${ _path }" value="/styles/live-chat/cbe.css" />" rel="stylesheet" type="text/css" />
	<link href="<c:url context="${ _path }" value="/styles/live-chat/cobrowseUI.css" />" rel="stylesheet" type="text/css" />
	
    
    <!-- New Include :: Test  Ends -->  
    
    
    
    <!-- Oceana Phase 2 : Populate user info and load co-browse urls -->
    <%
		com.avaya.grt.web.security.CSSPortalUser portalUser = (com.avaya.grt.web.security.CSSPortalUser) request
		.getSession().getAttribute(com.grt.util.GRTConstants.CSS_USER_PROFILE);
   	%>
    
    <script type="text/javascript">
		<%-- var cobrowseEnabledUrls = [<% for (int i = 0; i < portalUser.getCobrowseEnabledUrls().size(); i++) { %>"<%= portalUser.getCobrowseEnabledUrls().get(i) %>"<%= i + 1 < portalUser.getCobrowseEnabledUrls().size() ? ",":"" %><% } %>];
		var currentUrl = window.location.pathname;
		currentUrl = currentUrl.split(":")[0];
		console.log("Current URL : " + currentUrl);
		for(var i=0; i<cobrowseEnabledUrls.length ; i++) {
			console.log("Checking configured URL :: " + cobrowseEnabledUrls[i]);
			if(currentUrl == cobrowseEnabledUrls[i]) {
				console.log("Found Match : Enabling co-browse");
				$("#showCoBrowseLink").attr("enable-cobrowse","true");
				//TODO Need to comment
				//$("#showCoBrowseLink").show();
			}
		} --%>
		
		$(function() {
			//Initiate Chat on load of the page.
			$("#openbutton-chat").click();
			
			localStorage.setItem("onChat", "true");
			
			/* window.onbeforeunload = function() {
				console.log('window.onbeforeunload');
				localStorage.setItem("onChat", "false");
			}
			 */
			 $(window).on("beforeunload", function() { 
				 
				//Check whether user has clicked close button, if close button is clicked onChat flag will be false
			
				if ($('#messages p:contains("Close request sent")').length > 0 ) {
					// do nothing and just closed because close button was already clicked.
				} else {

					//code for chatbot reports from Kiko
					sendAutoText("Customer clicked cross(X) on chat window to close");
	                
	                // START: for chatbot reports - if customer leave while waiting for agent
	                if (!visitor_engaged.clickedCloseBtn && visitor_engaged.customer_escalated) {
	                      if (global_chatbot_token != "notoken" && global_chatbot_token.length > 7){
	                                                                                                                                                                
	                           sendToConvDataRepV2('customer_dropped_while_waiting_for_agent', 'yes');
	                      }
	                }
	                // END: for chatbot reports - if customer leave while waiting for agent
					
					var closeRequest = {
                        'apiVersion' : '1.0',
                        'type' : 'request',
                        'body' : {
							'method' : 'closeConversation'
                        }
                    };					
					         
					chatSocket.sendMessage(closeRequest);

				} 
				

				// setting localstorage to false for chatActive
				localStorage.setItem("onChat", "false");
				localStorage.clear;
			   // window.open("http://survey.webengage.com/ws/~177p0a0","GRT Chat WebEngage survey", 
			     //   			"toolbar=0,scrollbars=1,directory=0,location=0,statusbar=0,menubar=0,resizable=1,width=835,height=598,left = 10,top = 10");
			   
			   var email=$("#email-chat").val();
			   var ssoid=$("#ssoid").val();
			   var chatStartTimestamp = '';
			   var chatuser=$("#user-chat").val();
			   if(webChat != null && webChat.chatStartTimestamp != null && webChat.chatStartTimestamp != '') {
					chatStartTimestamp = webChat.chatStartTimestamp;
				}
			    window.open("http://survey.webengage.com/ws/~177p0a0?data(email)="+email+"&data(ssoid)="+ssoid+"&data(username)="+chatuser+"&data(chatStart)="+chatStartTimestamp,"GRT Chat WebEngage survey", 
    			"toolbar=0,scrollbars=1,directory=0,location=0,statusbar=0,menubar=0,resizable=1,width=835,height=598,left = 10,top = 10"); 
				
				// delay the popup dissapearance for closingIN timer below in ms
				var closingIN = 5000;
					console.log('closing window in '+ closingIN);
					var counter = 0, start = new Date().getTime() , end = 0;
					while (counter < closingIN) {
						end = new Date().getTime();
						counter = end - start;
					}
				
			 }); 
			
			/* window.addEventListener("beforeunload", function(e) {
				console.log('window.addEventListener');
			}, false); */
			
		});
	</script>

</head>
<body>
<div id="live-chat">
	<!-- COBROWSE -->
	<iframe id="cobrowse" style="background: white !important;">
	</iframe> 
	

	<!-- COBROWSE CONTROLS -->
	<div id="coBrowseDialog" style="display: none" title="Co-Browse Session">
		<div id="coBrowseText"></div>
	</div>

	<div id="requestControlDialog" style="display: none" title="Request Control">
		<div id="requestControlText"></div>
	</div>
	
	<!-- -- CHAT PANEL -- -->
	<div id="chatPanel" class="slide-out-div-chat">
		<a class="chatHandle" href="#"></a>
		<div class="sectionBorder">
			<span class="bigtext">Avaya </span> <span class="smalltext">|
				Live Chat</span>
				
		</div>

		<!-- -- CHAT FORM -- -->
		<div id="chatForm">
			<table class="formTable">
                <tr>
                    <td class="formLabel">Name</td>
                    <td class="formField"><input type="text" id="user-chat" value="<%=portalUser.getFirstName() + " " + portalUser.getLastName() %>"></td>
                    <td class="formLabel">Country</td>
                    <td><select id="country-chat" class="formField"></select> </td>
                </tr>

                <tr>
                    <td class="formLabel">E-mail</td>
                    <td class="formField"><input type="text" id="email-chat" value="<%=portalUser.getEmailAddress()%>"></td>
                    
                    <td class="formLabel">Phone</td>
                    <td>
                    <input
                        type="text" id="area-chat" class="areaInput"> <input
                        type="text" id="phone-chat" class="phoneInput" value="<%=portalUser.getPhoneNumber() != null ? portalUser.getPhoneNumber() : "" %>">
                    </td>
                    
                    
                </tr>

                <tr>
                    <td class="formLabel">Contact Type</td>
                    <td class="formField">
                        <select id="contactType">
                            <option value="Chat" selected>Chat</option>
                            <option value="Video">Chat + Video</option>
                            <option value="Voice">Chat + Voice</option>
                        </select>
                    </td>
                    <td class="formLabel">Address</td>
                    <td class="formField"><input type="text" id="address-chat" value="<%=portalUser.getStreetAddress1() != null ? portalUser.getStreetAddress1() : "" %>"></td>
                    
                </tr>

                <tr>
                    <td class="formLabel center" colspan="2"><input
                        type="checkbox" id="transcript-chat" checked="checked"> Email the chat
                        transcript to me</td>
                    <td class="formLabel"></td>
                    <td class="formField center">
                        <button id="openbutton-chat" onclick="chatLogon.gatherDetails()"
                            class="button">Chat Now</button>
                    </td>
                </tr>
            </table>
		</div>

		<!-- -- CHAT INTERFACE -- -->
		<div id="chatInterface">

			<!-- messages -->
			<div id="messages"></div>

			<!-- participants -->
			<div id="usersDiv">
				<div id="participants">
					<div class="hidden">
						<img src="" class="hidden">
					</div>
					<div class="hidden">
						<img src="" class="hidden">
					</div>
					<div class="hidden">
						<img src="" class="hidden">
					</div>
				</div>
			</div>

			<div id="controls">
				<input type="text" id="outmessage" class="chatField" placeholder="Type your message here">
				<button type="button" id="sendbutton-chat"
					onclick="webChat.sendChatMessage()" class="button">Send Msg</button>
				<button type="button" id="closebutton-chat" onclick="webChat.quitChat()"
					class="button">Close</button>

<!-- 				<div id="pagePushDiv" class="pagepush"> -->
<!-- 				p id="hp" class="fold-out-pagepush" -->
<!-- 					<input type="url" class="urlField chatField" id="urlInput" -->
<!-- 						placeholder="http://www.avaya.com"> -->
<!-- 					<button type="button" id="sendurl-chat" -->
<!-- 						onclick="webChat.sendPagePushRequest()" class="button">Send URL</button> -->
				    
<!-- 					<a class="btn btn-xs btn-warning hide" id="startassist" role="button">Video</a> -->
					
<!-- 				</div> -->
				
<!-- 				<div id="escalationDiv" class="escalation"> -->
<!-- 				    <select id="escalationType"> -->
<!-- 				    options will be added in -->
<!-- 				    </select> -->
<!-- 				    <button type="button" id="button-escalate" onclick="webChat.escalate()" class="button">Escalate</button> -->
<!-- 				</div> -->
				
			</div>
			
		</div>
		
		<div id="chatFooter" class="poweredBy">Powered by Avaya Oceana&#x2122;</div>
		<!-- Additional Attributes from SSO -->
		<input type="hidden" id="ssoCountry" value="<%=portalUser.getCountry() != null ? portalUser.getCountry() : "" %>">
		<input type="hidden" id="ssoCity" value="<%=portalUser.getCity() != null ? portalUser.getCity() : "" %>">
		<input type="hidden" id="ssoBpLinkId" value="<%=portalUser.getBpLinkId() != null ? portalUser.getBpLinkId() : "" %>">
		<input type="hidden" id="ssoCompany" value="<%=portalUser.getCompany() != null ? portalUser.getCompany() : "" %>">
		
		<input type="hidden" id="ssoCustomerNumber" value="<%=portalUser.getCustomerNumber() != null ? portalUser.getCustomerNumber() : "" %>">
		<input type="hidden" id="ssoLinkId" value="<%=portalUser.getLinkId() != null ? portalUser.getLinkId() : "" %>">
		<input type="hidden" id="ssoZipcode" value="<%=portalUser.getZipcode() != null ? portalUser.getZipcode() : "" %>">
		<input type="hidden" id="ssoid" value="<%=portalUser.getUserId() != null ? portalUser.getUserId() : "" %>">
		
		<%
		if (portalUser != null && portalUser.getUserType() != null ){
			String userType = "";
			if (portalUser.getUserType().equalsIgnoreCase("A") ){
				userType = "ASSOCIATE";
			} else if(portalUser.getUserType().equalsIgnoreCase("B")) {
				userType = "BUSINESS PARTNER";
			} else if(portalUser.getUserType().equalsIgnoreCase("C")) {
				userType = "CUSTOMER";
			}
		%>
			<input type="hidden" id="ssoUserType" value="<%=userType %>">
		<% } %>
					
		
		<!-- Additional Attributes from SSO ends-->
	</div>
<!--  -->

	<!-- Configuration panel. Remove for production -->
    <div id="configPanel" class="configPanel" title="Configuration">
    
        <div onclick="chatConfig.toggleUrls()" class="configHeader"><span style="text-decoration:underline">Click here</span> to configure URLs</div>
        <div id="urlsListDiv" class="configCollapseDiv">
            <table>
                <tr>
                    <td>Accept self-signed certificates</td>
                    <td><button class="configButton" onclick="chatConfig.loadHelpPage()">Accept certificates</button></td>
                </tr>
	            <tr>
	                <td>Chat URL</td>
	                <td><input type="text" id="webChatUrlInput" placeholder="ws://a.b.c.d/services/websocket/chat" /></td>
	            </tr>
	            <tr>
	                <td>Context Store URL</td>
	                <td><input type="text" id="contextStoreHostInput" placeholder="http://e.f.g.h/services/CSRest/cs/contexts/" /></td>
	            </tr>
	            <tr>
	                <td>Estimated Wait Time URL</td>
	                <td><input type="text" id="ewtUrlInput" placeholder="http://a.b.c.d/services/customer-service/gila/ewt/request" /></td>
	            </tr>
	            <tr>
	                <td>Co-Browsing URL</td>
	                <td><input type="text" id="coBrowseHostInput" placeholder="i.j.l.k"/></td>
	            </tr>
	        </table>
        </div>
        <div class="configHeader" onclick="chatConfig.toggleAttributes()"><span style="text-decoration:underline">Click here</span> to configure attributes</div>
        <div id="attributesListDiv" class="configCollapseDiv" >
            <table id="attributesTable">
                <tr id="newAttributeRow">
                    <td><input id="newAttributeInput" placeholder="Language.English"></td>
                    <td><button class="configButton" onclick="chatConfig.insertNewAttribute()">Add</button></td>
                </tr>
            </table>
        </div>
        
        <div class="configHeader" onclick="chatConfig.toggleConfig()"><span style="text-decoration:underline">Click here</span> to configure workflow and routepoints</div>
        <div id="chatConfigDiv" class="configCollapseDiv">
            <table>
                <tr>
                    <td>Workflow Type</td>
                    <td><input type="text" id="workflowInput" /></td>
                </tr>
                <tr>
                    <td>Routepoint Identifer</td>
                    <td><input type="text" id="routepointInput" /></td>
                </tr>
            </table>
        </div>
        
		<div class="configHeader" onclick="chatConfig.toggleCoBrowse()"><span style="text-decoration:underline">Click here</span> to configure Co-Browse</div>
        <div id="coBrowseConfigDiv" class="configCollapseDiv">
            <table>
                <tr>
                    <td>Co-Browse Enabled</td>
                    <td><input type="checkbox" id="coBrowseEnabledInput"></td>
                </tr>
            </table>
        </div>
        
        <div class="configHeader">
            <button class="configButton" onclick="chatConfig.requestEwt()">Request Estimated Wait Time</button>
            <button class="configButton" onclick="chatConfig.requestEwt()">Initialise Co-Browse Library</button>
            <button class="configButton" onclick="chatConfig.saveWebChatConfig()">Save Configuration</button>
            <button class="configButton" onclick="chatConfig.resetConfig()">Reset</button>
        </div>
    </div>
    

 </div>
  <!-- End of Chat Module -->  
</body>
</html>