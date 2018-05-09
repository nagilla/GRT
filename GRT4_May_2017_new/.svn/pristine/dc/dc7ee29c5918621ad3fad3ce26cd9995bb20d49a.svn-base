<%@page import="java.util.Date"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/includes/context.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE>
<html>
<head>
	<%
		//For avoiding caching issue
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", 0); // Proxies.
		response.setHeader("X-Powered-By","NONE");
		String filepath=application.getRealPath("/others/MaterialEntryUpload.xls");
		session.setAttribute("downloadFilePath", filepath);
	%>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="X-Frame-Options" content="deny">

	<title>Avaya GRT <decorator:title/> </title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache, no-store, must-revalidate">
	<meta http-equiv="expires" content="0">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

	<!-- New UI Code -->
	<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/main.css" />"/>
	<link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.css">
	<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/framework/common.css"/>"/>

	<!-- Plugins -->
	<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/plugins/jquery-1.7.1.min.js" />"></script>
	<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/plugins/jquery-ui-1.8.16.custom.min.js" />"></script>
	<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/plugins/jquery.autocomplete.js" />"></script>
	<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/plugins/underscore-min.js" />"></script>
	<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/plugins/backbone.js" />"></script>
	<script type="text/javascript" src="https://avaya.conversive.com/CLA_API/v2/js/json2-min.js"></script>
	<script type="text/javascript" src="https://avaya.conversive.com/avaya/js/propop.conversive.combined.js"></script>
	
	<!-- search functionality changes -->
	<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/avaya-search-list.css" />"/>
	<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/common-flow/grt.search.js" />"></script>
	
	<!-- Chat Phase 2 -->
	
	<%-- <%
		/* the presence of parameter key indicates that the page is being co-browsed. */
	    if (request.getParameter("key") != null && !request.getParameter("key").trim().isEmpty()) {
	       %>
	       	<link href="<c:url context="${ _path }" value="/styles/live-chat/jquery-ui.min.css" />" rel="stylesheet" type="text/css" />
			<script src="<c:url context="${ _path }" value="/scripts/live-chat/jquery.min.js"/>"></script>
			<script src="<c:url context="${ _path }" value="/scripts/live-chat/jquery-ui.min.js"/>"></script>
			
	       <%
	    } 
	%> --%>
	
	<script type="text/javascript">
		
		//Function to check whether the page is being loaded in iframe
		function inIframe() {
		    try {
		        return window.self !== window.top;
		    } catch (e) {
		        return true;
		    }
		}
		if(inIframe()) {
			//alert("hdhdh");
			//$.getScript('<c:url context="${ _path }" value="/scripts/live-chat/jquery-ui.min.js"/>');
			
			/* $('head').append('<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/live-chat/jquery-ui.min.js"/>"/>');
			
			$('head').append('<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/live-chat/jquery-ui.min.js"/>"/>'); */
			
			$('head').append('<link href="<c:url context="${ _path }" value="/styles/live-chat/jquery-ui.min.css" />" rel="stylesheet" type="text/css" />');
			
			/* $.ajax({
		        url: "/grt/scripts/live-chat/jquery-ui.min.js",
		        dataType: "script",
		        async: false,           // <-- This is the key
		        success: function () {
		            alert(" all good...");
		        },
		        error: function () {
		        	alert("Could not load script ");
		            throw new Error("Could not load script " + script);
		        }
		    }); */
		    
		    document.write("<script src='/grt/scripts/live-chat/jquery.min.js' type='text/javascript'>"+"</"+"script>");
			document.write("<script src='/grt/scripts/live-chat/jquery-ui.min.js' type='text/javascript'>"+"</"+"script>");
		}
	</script>
	
	<!-- CoBrowse dependencies -->
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/LAB.min.js"/>"></script>
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/coBrowse.js"/>"></script>
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/coBrowseUI.js"/>"></script>
		
	<link href="<c:url context="${ _path }" value="/styles/live-chat/cbe.css" />" rel="stylesheet" type="text/css" />
	<link href="<c:url context="${ _path }" value="/styles/live-chat/cobrowseUI.css" />" rel="stylesheet" type="text/css" />
	<script src="<c:url context="${ _path }" value="/scripts/live-chat/jquery.tabSlideOut.v1.3.js"/>"></script>	
	
	<!-- Chat Phase 2 Ends -->
	
	<script type="text/javascript">
		var _port = (window.location.port != undefined && window.location.port != '') ? ':' + window.location.port : '';
		var _path = ("${ _path }" == "/") ? window.location.protocol + "//" + window.location.hostname + _port : "${ _path }";
	</script>

	<script type="text/javascript">

		//Defence for clickjacking
		try {
			if (top.location.hostname != self.location.hostname) throw 1;
		} catch (e) {
			top.location.href = self.location.href;
		}

		function logoutUser() {
			var url = _path + "/technicalregsitration/logout.action";
			window.location = url;
		}

		function onPropopLoad () {
			try {
				//propop.setCallback(function () {$('.can-chat').show(); conversiveModal.show(); spTracking.eventChat('event11','chat window displayed');});
				//TODO: 1. show underlay and Ask for confirmation. 2. populate this dynamic data.
				<c:if test="${sessionScope.askavauser.userType eq 'B' or sessionScope.askavauser.userType eq 'b'}" >
				propop.ssoData = {
						"emailAddress":"${sessionScope.askavauser.emailAddress}", "firstName":"${sessionScope.askavauser.firstName}",
					 	"lastName":"${sessionScope.askavauser.lastName}", "companyName":"${sessionScope.askavabpuser.soldToName}",
							"companyId":"", "city":"${sessionScope.askavabpuser.city}",
							"country":"${sessionScope.askavabpuser.country}", "phone":"${sessionScope.askavauser.phoneNumber}",
						"partnerLinkID":"${sessionScope.askavauser.bpLinkId}","userID":"${sessionScope.askavauser.userId}",
						"userType" : "BUSINESS_PARTNER",
						"defaultSoldTo" : "${sessionScope.askavabpuser.soldToId}",
						"customerSegment" : "",
							"topClient" : "",
							"capsuleTeam" : "",
							"securityRestricted" : "",
							"marketingProductId" : ""
				};
				</c:if>

				<c:if test="${sessionScope.askavauser.userType eq 'C' or sessionScope.askavauser.userType eq 'c'}" >
				propop.ssoData = {
						"emailAddress":"${sessionScope.askavauser.emailAddress}", "firstName":"${sessionScope.askavauser.firstName}",
					 	"lastName":"${sessionScope.askavauser.lastName}", "companyName":"${sessionScope.askavacoustomeruser.name}",
							"companyId":"", "city":"${sessionScope.askavacoustomeruser.primaryAccountCity}",
							"country":"${sessionScope.askavacoustomeruser.primaryAccountCountry}", "phone":"${sessionScope.askavacoustomeruser.phoneNumber}",
						"partnerLinkID":"","userID":"${sessionScope.askavauser.userId}",
						"userType" : "CUSTOMER",
						"defaultSoldTo" : "${sessionScope.askavacoustomeruser.soldToNumber}",
						"customerSegment" : "",
							"topClient" : "",
							"capsuleTeam" : "",
							"securityRestricted" : "",
							"marketingProductId" : ""
				};
				</c:if>
			} catch (error) {}
		}

		function onPropopUnLoad () {
			try {
					propop.reset();

			} catch (error) {}
		}

		function showAskAva()	{
			<c:if test="${sessionScope.askavauser.userType eq 'A' or sessionScope.askavauser.userType eq 'a'}">
				alert('This functionality is not available for Avaya Associates.');
				return false;
			</c:if>
				propop.openWindowReactive();
		}

		function createSuccFailPopup( msg, href ){
			$(".succFailMsg span.succFailTxt").text(msg);
			$(".succFailMsg").show();
			$(".succFailOkBtn").attr('href', href);
		}

		function closeSuccFailPopup() {
			 $(".succFailMsg").hide();
			 $(".loading-overlay").show();
		}

		function closeAlert() {
			$(".alertMsg").hide();
		}
		
		function writeToLogFile(msg) {
			var url = "<c:url context="${ _path }" value="/commonUtil/addAlertLogs.action" />";
			$.ajax({
				type: "POST",
				url: url,
				data: { logMessage: msg },
				success: success,
				error: function(errorThrown ) {
					console.log(errorThrown );
                }
			});
		}
		
		function success() {
			//alert("Log Success");
		}
		//TODO Remove
		function convertAlertToModelPopUpOld( msg ){
			$(".alertMsg span.alertMsgTxt").text(msg);
			$(".alertMsg").show();
		}
		
		function convertAlertToModelPopUp(msgKey, msg) {
			var refNoAndModuleArr = msgKey.split("_");
			var refNo = refNoAndModuleArr[0];
			var msgType = refNo.split("-")[0];
			//var moduleName = refNoAndModuleArr[1];
			//var dispMessage = msgArr[1];
			if(msgType == "ERR") {
				writeToLogFile(msgKey+"###"+msg);
				convertAlertToErrorModelPopUp(refNo, msg);
			} else if(msgType == "WRN") {
				writeToLogFile(msgKey+"###"+msg);
				convertAlertToWarnModelPopUp(refNo, msg);
			} else if(msgType == "INF") {
				writeToLogFile(msgKey+"###"+msg);
				convertAlertToInfoModelPopUp(msg);
			}
			//TODO added for backward compatibility.
			else {
				convertAlertToModelPopUpOld(msgKey);
			}
		}
		//Chat Phase 2 Method
		function showLiveChatDisabledMsg(){
			var disabledCode = $('#alertliveChatDisabledCode').val();
			var disabledMsg = $('#alertliveChatDisabledMsg').val();
			
			convertAlertToModelPopUp(disabledCode, disabledMsg);
		}
		
		function convertAlertToInfoModelPopUp( msg ){
			$(".infoMsg span.infoMsgTxt").html(msg);
			$(".infoMsg").show();
		}
		
		function closeInfoAlert() {
			$(".infoMsg").hide();
		}
		
		function convertAlertToWarnModelPopUp(refNo, msg) {
		
			$(".warningMsg span.warningMsgTxt").html(msg);
			$(".warningMsg").show();
		}
		
		function closeWarnAlert() {
			$(".warningMsg").hide();
		}
		
		function convertAlertToErrorModelPopUp(refNo, msg ){
			$(".errorMsg span.errorMsgTxt").html(msg+ " " +refNo);
			$(".errorMsg").show();
		}
		
		function closeErrorAlert() {
			$(".errorMsg").hide();
		}
		
		function convertAlertToModelPopUpServerSideMsg(msg) {
			if(msg.length > 0) {
				// write the message to log file
				writeToLogFile(msg);
				var msgArr = msg.split("###");
				if(msgArr && msgArr.length > 1) {
					var refNoAndModule = msgArr[0];
					var refNoAndModuleArr = refNoAndModule.split("_");
					var refNo = refNoAndModuleArr[0];
					var msgType = refNo.split("-")[0];
					//var msgType = msgTypeAndCodeArr[0];
					
					//var moduleName = refNoAndModuleArr[1];
					var dispMessage = msgArr[1];
					if(msgType == "ERR") {
						convertAlertToErrorModelPopUp(refNo, dispMessage);
					} else if(msgType == "WRN") {
						convertAlertToWarnModelPopUp(refNo, dispMessage);
					} else if(msgType == "INF") {
						convertAlertToInfoModelPopUp(dispMessage);
					}
				}
			}
		}
		 
		//This alert Info for alert and redirection
		function convertAlertToModelPopUpWithRedirect( msg, href ){		
			if(msg.length > 0) {
				// write the message to log file
				writeToLogFile(msg);
				var msgArr = msg.split("###");
				if(msgArr && msgArr.length > 1) {
					var refNoAndModule = msgArr[0];
					var refNoAndModuleArr = refNoAndModule.split("_");
					var refNo = refNoAndModuleArr[0];
					var msgType = refNo.split("-")[0];
					
					var dispMessage = msgArr[1];
					$(".alertOkBtn").attr('href', href);
					if(msgType == "ERR") {
						$(".alertErrorMsgOkBtn").attr('href', href);
						$(".errorMsg .close").attr('href', href);
						convertAlertToErrorModelPopUp(refNo, dispMessage);
					} else if(msgType == "WRN") {
						$(".alertWarningMsgOkBtn").attr('href', href);
						$(".warningMsg .close").attr('href', href);
						convertAlertToWarnModelPopUp(refNo, dispMessage);
					} else if(msgType == "INF") {
						$(".alertInfoMsgOkBtn").attr('href', href);
						$(".infoMsg .close").attr('href', href);
						convertAlertToInfoModelPopUp(dispMessage);
					}
				}
			}
		}
		//This alert Info for alert and redirection ends here
		
		//This alert Info for alert and callback Javascript function
		function convertAlertToModelPopUpWithCallBackOnClose(msg, callBackFuncName ){		
			if(msg.length > 0) {
				// write the message to log file
				writeToLogFile(msg);
				var msgArr = msg.split("###");
				if(msgArr && msgArr.length > 1) {
					var refNoAndModule = msgArr[0];
					var refNoAndModuleArr = refNoAndModule.split("_");
					var refNo = refNoAndModuleArr[0];
					var msgType = refNo.split("-")[0];
					
					var dispMessage = msgArr[1];
					if(msgType == "ERR") {
						var callBackFinal = 'closeErrorAlert(); '+callBackFuncName;
						$(".alertErrorMsgOkBtn").attr('onclick', callBackFinal);
						$(".errorMsg .close").attr('onclick', callBackFinal);
						convertAlertToErrorModelPopUp(refNo, dispMessage);
					} else if(msgType == "WRN") {
						var callBackFinal = 'closeWarnAlert(); '+callBackFuncName;
						$(".alertWarningMsgOkBtn").attr('onclick', callBackFinal);
						$(".warningMsg .close").attr('onclick', callBackFinal);
						convertAlertToWarnModelPopUp(refNo, dispMessage);
					} else if(msgType == "INF") {
						var callBackFinal = 'closeInfoAlert(); '+callBackFuncName;
						$(".alertInfoMsgOkBtn").attr('onclick', callBackFinal);
						$(".infoMsg .close").attr('onclick', callBackFinal);
						convertAlertToInfoModelPopUp(dispMessage);
					}
				}
			}
		}
		//This alert Info for alert and callback Javascript function ends here
		
		//Confirm box		
		function convertConfirmToModelPopUp(msgKey, msg, callBackMethodForOk, callBackMethodForCancel ) {
			writeToLogFile(msgKey+"###"+msg);
			var refNoAndModuleArr = msgKey.split("_");
			var refNo = refNoAndModuleArr[0];
			$(".confirmMsg span.confirmMsgTxt").html(msg+ " " +refNo);
			$(".confirmMsg").show();
			$(".confirmMsg .alertOkBtn").attr("onclick", "closeConfirmAlert();" + callBackMethodForOk);
			$(".confirmMsg .alertCancelBtn").attr("onclick", "closeConfirmAlert();" + callBackMethodForCancel);
		}

		function closeConfirmAlert() {
			$(".confirmMsg").hide();			
		}
		//Confirm box ends here
		
		$(document).ready(function() {
			/* collapse box */
			var collapseBox = $('.collapse-box .collapse-box-header');

			collapseBox.click(function(e){
				e.preventDefault();
				var thisBox = $(this),
						speed = 200;
				// toggle container
				thisBox.next('.collapse-box-container').slideToggle(speed);
				// set header active state
				if(thisBox.hasClass('active') === true) {
					thisBox.removeClass('active');
				} else if(collapseBox.hasClass('active') === false) {
					thisBox.addClass('active');
				} else {
					thisBox.addClass('active');
				}
			});


			//Disable button remove css
			 $('input[type="submit"]').each(function() {
			    var readonly = $(this).is(':disabled');

			    if(readonly) { // this is readonly
			    	$(this).removeClass('gray');
			    }
			});
			$('input[type="button"]').each(function() {
				var readonly = $(this).is(':disabled');
				
				if(readonly) { // this is readonly
					$(this).removeClass('gray');
				}
			});
			$(document).click(function() {
				if($("#searchListVisible").val() == "True") {
					$("#searchListVisible").val("False");
					$("#autoCompletetop").hide();
				}
			});
			
			$('#autoCompletetop').click(function(event) {
			    event.stopPropagation();
			});
		});
		
		//Phase 2 : Chat in new Window
		$(function() {
			
			
			/* setInterval(function() {
				console.log('checking local storage chat flag...' + localStorage.getItem("onChat"));
				if (localStorage.getItem("onChat") !== 'undefined' && localStorage.getItem("onChat") == "true" ) {
						//$('#chatInProgress').html('<blink>Chat In-Progress</blink>');
					$('#chatPanel').hide();
					$('.chatHandle').hide();
					window.open('', 'Oceana Chat', '');
				} else {
					//$('#chatInProgress').text('Chat NOT active'); 
					$('#chatPanel').show();
					$('.chatHandle').show();
				}
			},3000); */

			$('.slide-out-div-chat').tabSlideOut({
		        tabHandle : '.chatHandle', // CSS class of the tab element
		        pathToTabImage : '/grt/images/live-chat/LiveChat2.jpg', // path to the image for the tab
		        imageHeight : '111px', // height of tab image
		        imageWidth : '25px', // width of tab image (Optionally can be set using css)
		        tabLocation : 'right', // can be top, right, bottom, or left
		        speed : 300, // speed of animation
		        action : 'click', // options: 'click' or 'hover', action to trigger animation
		        topPos : '150px', // position from the top (use if tabLocation is left or right)
		        leftPos : '0px', // position from left (use if tabLocation is bottom or top)
		        fixedPosition : true
		    // options: true makes it stick(fixed position) on scroll
		    });		
			
			$(".chatHandle").click(function(){
				// hiding chat panel
				//$('#chatPanel').hide();
				//$('.chatHandle').hide();
				
				// check if chat is in progress		
				console.log('checking local storage chat flag...' + localStorage.getItem("onChat"));
				if (localStorage.getItem("onChat") !== 'undefined' && localStorage.getItem("onChat") == "true" ) {
					//$('#chatInProgress').html('<blink>Chat In-Progress</blink>');
					//$('#chatPanel').hide();
					//$('.chatHandle').hide();
					//Fix local storage issue.
					var glbl_popupHandler = window.open("", "Oceana Chat", 
					"toolbar=0,scrollbars=0,directory=0,location=0,statusbar=0,menubar=0,resizable=1,width=590,height=433,left=570,top=120");
					//check whether the window already opened or not
					if(glbl_popupHandler.location.href === 'about:blank') {
						//If not opened, update the url
						glbl_popupHandler.location.href = "/grt/commonUtil/liveChat.action";
						var timer = setInterval(function() {
							if (glbl_popupHandler.closed) {
								console.log('window IS closed');
								$('#chatPanel').show();
								$('.chatHandle').show();
								clearInterval(timer);								
								localStorage.setItem("onChat", "false");
							} 
						},1000);
					}
					glbl_popupHandler.focus();
				} else {
					//$('#chatInProgress').text('Chat NOT active'); 
					// this open a popup and load live-chat.jsp on it
					var glbl_popupHandler = window.open("/grt/commonUtil/liveChat.action", "Oceana Chat", 
							"toolbar=0,scrollbars=0,directory=0,location=0,statusbar=0,menubar=0,resizable=1,width=590,height=433,left=570,top=120");
	                glbl_popupHandler.focus();
					var timer = setInterval(function() {
					if (glbl_popupHandler.closed) {
						console.log('window IS closed');
						$('#chatPanel').show();
						$('.chatHandle').show();
						clearInterval(timer);
						
						localStorage.setItem("onChat", "false");
					} 
					},1000);
					
					//window.open("http://localhost:8080/grt/commonUtil/liveChat.action","GRT Chat", 
	    			//"toolbar=0,scrollbars=1,directory=0,location=0,statusbar=0,menubar=0,resizable=1,width=605,height=450,left = 0,top = 0");
				}
			});
			
			
			//check whether the page is being loaded in iframe
			if(inIframe()) {
				//Hide Live Chat button
				 $(".chatHandle").hide();
			}
			
		});
	 </script>

	<decorator:head/>

</head>
<body>
	<!-- START :: Tealium code -->
	<script type="text/javascript">
		(function(a, b, c, d) {
			a = '//tags.tiqcdn.com/utag/avaya/main/prod/utag.js';
			b = document;
			c = 'script';
			d = b.createElement(c);
			d.src = a;
			d.type = 'text/java' + c;
			d.async = true;
			a = b.getElementsByTagName(c)[0];
			a.parentNode.insertBefore(d, a);
		})();
	</script>
	<!-- END  ::  Tealium code -->
	
	<!-- start header -->
	<div class="header">
		<div class="clearfix"></div>
		<div class="inner-wrap">
			<div class="left-col">
				<div class="top-bar">
					<div class="content">
						<div class="search">
							<form name="avayaSearchForm" method="get" id="avayaSearchForm" onsubmit="return validateGoogleSearchBoxForm(this);">
								<input type="text" id="q" maxlength="100" name="q" data-index="0" placeholder="Search" onkeyup="xmlhttpPost(this, device)" onfocus="this.value=''" autocomplete="off">
								<input type="hidden" name="site" value="all_usa">
								<input type="hidden" name="avayaSearchRedirectUrl" id="avayaSearchRedirectUrl" value="">
								<button type="submit"><i class="fa fa-search"></i></button>
								<div class="butlerbar-dropdowns">
									<div class="autoComplete-list" id="autoCompletetop">
										
									</div>
								</div>
							</form>
							<!-- <form search-form>
								<input type="text" placeholder="Search Avaya Support" />
								<button type="submit"><i class="fa fa-search"></i></button>
							</form> -->
						</div>
						<%
							com.avaya.grt.web.security.CSSPortalUser portalUser = (com.avaya.grt.web.security.CSSPortalUser) request
							.getSession().getAttribute(com.grt.util.GRTConstants.CSS_USER_PROFILE);

							if (portalUser != null) {
								String grtSuggestionsUrl = (String)request.getSession().getAttribute(com.grt.util.GRTConstants.GRT_SUGGESTIONS_URL_FOR_UI);
								String grtHelpUrl = (String)request.getSession().getAttribute(com.grt.util.GRTConstants.GRT_HELP_URL);
						%>
						<ul>
							<li onClick="">
								Welcome <%=portalUser.getFirstName() + " "
										+ portalUser.getLastName() %> 		<i class="fa fa-caret-down"></i>
								<div class="dropdown">
									<div class="content">
										<ul>
											<li><a href="<c:url context="${ _path }" value="/technicalregsitration/logout.action" />">Logout</a></li>
										</ul>
									</div>
								</div>
							</li>
							<li><a href="http://avaya.com" target="_blank">Avaya.com</a></li>
							<li onClick="">
								Support <i class="fa fa-caret-down"></i>
								<div class="dropdown">
									<div class="content">
										<ul>
											<c:forEach items="${ grtConfig.supportMenu }" var="smenu">
												<li><a href="${ smenu.link }">${smenu.title}</a></li>
											</c:forEach>
										</ul>
									</div>
								</div>
							</li>
						</ul>
					</div>
				</div>
				<div class="bottom-bar">
					<div class="content">
						<div class="logo">
							<% pageContext.setAttribute("time",new Date().getTime()); %>
							<a href="<c:url context="${ _path }" value="/home-action"> <c:param name="reqid" value="${time}"/>	</c:url>">
							<span class="image">
							</span> <span class="text">Global Registration Tool</span></a>
						</div>
						<div class="menu">
							<ul>
								<li><a href="<c:url context="${ _path }" value="/home-action"> <c:param name="reqid" value="${time}"/>	</c:url>">
										HOME</a></li>
								<li><a href="<%=grtSuggestionsUrl%>" target="_blank">Suggestions</a></li>
								<li><a href="<%=grtHelpUrl%>">Registration Help</a></li>
								<li><div id="showCoBrowse"><a href="#" id="showCoBrowseLink" style="display:none" enable-cobrowse="false">Co-Browse</a></div></li>
							</ul>
						</div>
					</div>
				</div>
			</div>
			<%-- 	<img src="<c:url context="${ _path }" value="/images/global/ava.jpg" />" alt="Ask Ava"/> --%>
			<div class="right-col" onclick="javascript:showAskAva();"></div>
		</div>
		<div class="clearfix"></div>
		<%
			}
		%>
	</div>
	<!-- end header -->

	<!-- start page-wrap	-->
	<div class="page-wrap">
		<decorator:body/>
		<input type="hidden" value="<%=filepath %>" />
	</div>
	<!-- end page-wrap -->

	<!-- Live Chat Integration : Enable chat based on configuration for each user -->	
	<%	
	String chatOnAssociates = portalUser.getChatConfigurations().get("ENABLE_CHAT_FOR_ASSOCIATES");
	String chatOnBusinessPartner = portalUser.getChatConfigurations().get("ENABLE_CHAT_FOR_BUSINESS_PARTNER");
	String chatOnCustomer = portalUser.getChatConfigurations().get("ENABLE_CHAT_FOR_CUSTOMER");
	
	if (portalUser != null && portalUser.getUserType() != null 
		&& ( (portalUser.getUserType().equalsIgnoreCase("B") && "ON".equalsIgnoreCase(chatOnBusinessPartner))
				|| ( portalUser.getUserType().equalsIgnoreCase("C")  && "ON".equalsIgnoreCase(chatOnCustomer) )
						|| ( portalUser.getUserType().equalsIgnoreCase("A")  && "ON".equalsIgnoreCase(chatOnAssociates)  )  ) ){
	%>
		<%-- %@ include file="/decorators/live-chat.jsp" %> --%>
		<div id="chatPanel" class="slide-out-div-chat">
			<a class="chatHandle" href="#"></a>
		</div>		
		
		<!-- COBROWSE CONTROLS -->
	<div id="coBrowseDialog" style="display: none" title="Co-Browse Session">
		<div id="coBrowseText"></div>
	</div>

	<div id="requestControlDialog" style="display: none" title="Request Control">
		<div id="requestControlText"></div>
	</div>
		
		
	<%	}
	else{%>
		 <div class="chatPanelDisabled">
	<a class="chatHandleDisabled" href="#" onclick="javascript:showLiveChatDisabledMsg();" ></a>
	</div>
	
	<%	} %> 
	
	<!-- Live Chat Integration : ends -->
	
	<!-- start global loader	-->
	<div class="loader" style="display:none;">
		<div class="content-wrap">
			<div class="content">
				<img src="<c:url context="${ _path }" value="/styles/images/logos/avaya.svg" />" alt="Avaya Engage" class="logo" />
				<p class="message">Initializing Global Registration Tool</p>
				<img src="<c:url context="${ _path }" value="/styles/images/loaders/loader-lightgray.gif" />" alt="loader" class="loader-image" />
				<p class="error-message">There was a problem during the loading process. Please try again later.</p>
			</div>
		</div>
	</div>
	<!-- end global loader	-->

	<!-- start page data loader -->
	<div id="loading-overlay" class="loading-overlay" style="display: none;">
		<p id="loading-msg" class="loading-msg">Please wait while we process your request...</p>
		<img
			src="<c:url context="${ _path }" value="/styles/images/loaders/loader.gif" />"
			alt="loading" />
	</div>
	<!-- end page data loader -->
	<!-- start data submission loader -->
	<div id="submit-overlay" class="submit-overlay" style="display: none;">
		<p id="submit-msg" class="loading-msg">Please wait while we process your request...</p>
		<img
			src="<c:url context="${ _path }" value="/styles/images/loaders/loader.gif" />"
			alt="loading" />
	</div>

	<!-- end data submission loader -->
	<!-- start modal alertMsg -->
	<div class="modal alertMsg" style="display:none;">
		<div class="modal-overlay">
			<div class="modal-content">
				<a class="close" onclick="closeAlert()"><i class="fa fa-close errPopupBtn"></i></a>
				<h2 class="title">Attention</h2>
				<p class="content">
					<img class="msg-icon"
						src="<c:url context="${ _path }" value="/styles/images/messages/icon_warning_light_60x60.png" />"
						alt="warning" />
					<span class="msg-txt alertMsgTxt"></span>
				</p>
				<div class="controls">
					<a id="alertOkBtnId" class="button gray alertOkBtn"	onclick="closeAlert()" >OK</a>
				</div>
			</div>
		</div>
	</div>
	<!-- end modal alertMsg -->
	<!-- start modal infoMsg -->
	<div class="modal infoMsg" style="display:none;">
		<div class="modal-overlay">
			<div class="modal-content">
				<a class="close" onclick="closeInfoAlert()"><i class="fa fa-close errPopupBtn"></i></a>
				<h2 class="title">Info</h2>
				<p class="content">
					<img class="msg-icon"
						src="<c:url context="${ _path }" value="/styles/images/messages/icon_info_light_60x60.png" />"
						alt="info" />
					<span class="msg-txt infoMsgTxt"></span>
				</p>
				<div class="controls">
					<a id="alertInfoMsgOkBtnId" class="button gray alertInfoMsgOkBtn" onclick="closeInfoAlert()" >OK</a>
				</div>
			</div>
		</div>
	</div>
	<!-- end modal infoMsg -->
	<!-- start modal warningMsg -->
	<div class="modal warningMsg" style="display:none;">
		<div class="modal-overlay">
			<div class="modal-content">
				<a class="close" onclick="closeWarnAlert()"><i class="fa fa-close errPopupBtn"></i></a>
				<h2 class="title">Attention</h2>
				<p class="content">
					<img class="msg-icon"
						src="<c:url context="${ _path }" value="/styles/images/messages/icon_warning_light_60x60.png" />"
						alt="warning" />
					<span class="msg-txt warningMsgTxt"></span>
				</p>
				<div class="controls">
					<a id="alertWarningMsgOkBtnId" class="button gray alertWarningMsgOkBtn" onclick="closeWarnAlert()" >OK</a>
				</div>
			</div>
		</div>
	</div>
	<!-- end modal warningMsg -->
	<!-- start modal confirmMsg -->
	<div class="modal confirmMsg" style="display:none;">
		<div class="modal-overlay">
			<div class="modal-content">
				<a class="close" onclick="closeConfirmAlert()"><i class="fa fa-close errPopupBtn"></i></a>
				<h2 class="title">Confirmation</h2>
				<p class="content">
					<img class="msg-icon"
						src="<c:url context="${ _path }" value="/styles/images/messages/icon_warning_light_60x60.png" />"
						alt="warning" />
					<span class="msg-txt confirmMsgTxt"></span>
				</p>
				<div class="controls">
					<a id="alertConfirmOkBtnId" class="button gray alertOkBtn" onclick="closeConfirmAlert()" >OK</a>
					<a id="alertConfirmCancelBtnId" class="button gray alertCancelBtn" onclick="closeConfirmAlert()" >Cancel</a>
				</div>
			</div>
		</div>
	</div>
	<!-- end modal confirmMsg -->
	<!-- start modal errorMsg -->
	<div class="modal errorMsg" style="display:none;">
		<div class="modal-overlay">
			<div class="modal-content">
				<a class="close" onclick="closeErrorAlert()"><i class="fa fa-close errPopupBtn"></i></a>
				<h2 class="title">Error</h2>
				<p class="content">
					<img class="msg-icon"
						src="<c:url context="${ _path }" value="/styles/images/messages/icon_error_light_60x60.png" />"
						alt="error" />
					<span class="msg-txt errorMsgTxt"></span>
				</p>
				<div class="controls">
					<a id="alertErrorMsgOkBtnId" class="button gray alertErrorMsgOkBtn" onclick="closeErrorAlert()" >OK</a>
				</div>
			</div>
		</div>
	</div>

	<!-- end modal errorMsg -->
	

	<div class="modal infoMsgRedirect" style="display:none;">
	<!-- start alert Info for alert and redirection -->
		<div class="modal-overlay">
			<div class="modal-content">
				<a class="close" onclick="clickAlertRedirectOkBtn()"><i class="fa fa-close errPopupBtn"></i></a>
				<h2 class="title">Info</h2>
				<p class="content">
					<img class="msg-icon"
						src="<c:url context="${ _path }" value="/styles/images/messages/icon_info_light_60x60.png" />"
						alt="info" />
					<span class="msg-txt infoMsgTxt"></span>
				</p>
				<div class="controls">
					<a id="alertInfoRedirectOkBtnId" class="button gray alertRedirectOkBtn" onclick="closeConvertAlertToModelPopUpInfoWithRedirect()" >OK</a>
				</div>
			</div>
		</div>
	</div>
	<!-- end alert Info for alert and redirection -->



	<!-- start Success/Failure Msg with custom redirection link -->
	<div class="modal succFailMsg" style="display:none;">
		<div class="modal-overlay">
			<div class="modal-content">
				<h2 class="title">Info</h2>
				<p class="content">
					<img class="msg-icon"
						src="<c:url context="${ _path }" value="/styles/images/messages/icon_info_light_60x60.png" />"
						alt="info" />
					<span class="msg-txt succFailTxt"></span>
				</p>
				<div class="controls">
					<a id="succFailOkBtnId" class="button gray succFailOkBtn" onclick="closeSuccFailPopup()" >OK</a>
				</div>
			</div>
		</div>
	</div>

	<!-- end Success/Failure Msg with custom redirection link  -->

	<!-- start footer -->
	<div class="footer">
		<div class="content">
			<ul>
				<c:forEach items="${ grtConfig.footerLinks }" var="fmenu">
				<li><a href="${ fmenu.link }" target="_blank">${fmenu.title}</a></li>
				</c:forEach>
			</ul>
			<p class="address">${grtConfig.address}</p>
			<p class="copyright">${grtConfig.copyRight}</p>
		</div>
	</div>
	<!-- end footer -->


	<!-- Start error message -->
	<input type="hidden" id="alertToDateGrtFromDate" value="${grtConfig.alertToDateGrtFromDate}" />
	<input type="hidden" id="alertEnterToDate" value="${grtConfig.alertEnterToDate}" />
	<input type="hidden" id="alertEnterFromDate" value="${grtConfig.alertEnterFromDate}" />
	<input type="hidden" id="alertEnterToFromDate" value="${grtConfig.alertEnterToFromDate}" />
	<!-- Error message when live chat is disabled -->
	<input type="hidden" id="alertliveChatDisabledMsg" value="${grtConfig.liveChatDisabled}" />
	<!-- end error messages -->


	<!-- Error Codes -->
	<input type="hidden" id="alertToDateGrtFromDateErrorCode" value="${grtConfig.ewiMessageCodeMap['alertToDateGrtFromDate']}" />
	<input type="hidden" id="alertEnterToDateErrorCode" value="${grtConfig.ewiMessageCodeMap['alertEnterToDate']}" />
	<input type="hidden" id="alertEnterFromDateErrorCode" value="${grtConfig.ewiMessageCodeMap['alertEnterFromDate']}" />
	<input type="hidden" id="alertEnterToFromDateErrorCode" value="${grtConfig.ewiMessageCodeMap['alertEnterToFromDate']}" />
	<!-- End Error Codes -->

	<!-- Error message when live chat is disabled -->
	<input type="hidden" id="alertliveChatDisabledCode" value="${grtConfig.ewiMessageCodeMap['liveChatDisabled']}" />

	<input type="hidden" id="searchListVisible" value=""/>
</body>
</html>