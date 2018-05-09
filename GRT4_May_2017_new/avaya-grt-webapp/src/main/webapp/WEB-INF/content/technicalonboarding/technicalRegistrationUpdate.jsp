<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/includes/context.jsp" %>

<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/views/tob-update.css" />"/>
<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/plugins/jquery.dataTables_themeroller.css" />" />

<script src='<c:url context="${ _path }" value="/scripts/plugins/jquery_maskedinput.js" />'></script>
<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/plugins/dynamicTable.js" />"></script>
<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/technicalonboarding/grt.tob.update.view.js" />"></script>

<script type="text/javascript">
	jQuery(function($) {
		$.mask.definitions['~']='[+-]';
		$('#salSeIdForAddToList').mask('(999)999-9999');
	});
</script>

<script type="text/javascript">
	function populateTRConfig(groupId){
		document.getElementById('groupId').value = groupId;
		var url=document.getElementById('populateTRConfigAction').href;
		document.getElementById('technicalRegistrationForm').action=url;
		document.getElementById('technicalRegistrationForm').submit();
	}

	function showSALGatewayPopup(message) {
		var salConcentratorPopup = window.open('', '_blank', 'width=600, height=450,toolbar=no,resizable=yes,menubar=yes');
		salConcentratorPopup.document.write(message);
	}

	function showSALGatewayDetails(seid, soldTo) {
			var gatewaySeid =	seid;
		var soldToId = soldTo;
			//dwr.engine.beginBatch();
			$.ajax({
				url : _path+'/technicalonboarding/json/getSALGatewayDetails.action?gatewaySeid='+gatewaySeid+"&soldToId="+soldToId,
				dataType : 'json',
				success : function( data ){
					if(data) {
						showSALGatewayPopup(data);
					}
				}
			});
	}

	/* Enable/Disable IP Address or Dial In Number based on accessType selection */
	function enableIPAddressOrDialIn(object){
		//convertAlertToModelPopUp('AccessType :'+object.value);
		if(object.value == 'RASIP'){
			//document.getElementById('dialIn').value = '';
			document.getElementById('ipAddrDiv').style.display = 'block';
			document.getElementById('dialInDiv').style.display = 'none';
		} else if(object.value == 'Modem'){
			//document.getElementById('avayaIP').value = '';
			document.getElementById('ipAddrDiv').style.display = 'none';
			document.getElementById('dialInDiv').style.display = 'block';
		} else {
			document.getElementById('ipAddrDiv').style.display = 'none';
			document.getElementById('dialInDiv').style.display = 'none';
		}
	}

	/* Validating IP Address String */
	function validateIPAddr(ipAddr){
		var validIP = false;
		var theNum = "";
		var ipParts = ipAddr.split(".");
		if(ipParts.length==4){
			for(i=0;i<4;i++){
				theNum = parseInt(ipParts[i]);
				if(theNum >= 0 && theNum <= 255){}
				else{break;}
			}
			if(i==4)validIP=true;
		}
		return validIP;
	}

	/* Processing validations before submit */
	function processBeforeSubmit(){
		//convertAlertToModelPopUp('Entered processBeforeSubmit');
		//show_popup("Loading data", "Waiting...");
		var index = 0;
		var primary = "";
		var secondary = "";
		var checkText = "";
		var RegE = "";
		
		var userName = document.getElementById('username').value;
		var password = document.getElementById('password').value;
		var userNamePasswordFlag = document.getElementById('userNamePasswordFlag').value;
		//GRT 4.0 Change
		/* if (userNamePasswordFlag == 'block' && (userName == '' || password == '')) {
			close_popup();
			convertAlertToModelPopUp('Please enter value for username and password');
			return false;
		} */
		var retypePassword = document.getElementById('retypePassword').value;
		if (userNamePasswordFlag == 'block' && (retypePassword != password)) {
			close_popup();
			//convertAlertToModelPopUp('Password and ReType Password value must be same');
			convertAlertToModelPopUp($("#alertPswrdRetypePwdSameErrorCode").val(), $("#alertPswrdRetypePwdSame").val());
			
			return false;
		}
		if (userNamePasswordFlag == 'block' && (userName != '' || password != '') ) {
			close_popup();
			//if(confirm(' Please note it is often useful to retest connectivity/alarming after updating product login/password information, to ensure that connectivity/alarming is still enabled correctly. ')){
			if(confirm($("#cnfrmRetestAlalrmConn").val())){
				return true;
			} else {
				return false;
			}
		}
		
		
		if(document.getElementById('accessType') != null && document.getElementById('accessType').value != ''){
			if(document.getElementById('accessType').value == 'RASIP') {
				if(document.getElementById('avayaIP') != null
					&& document.getElementById('avayaIP').value == ''){
					close_popup();
					//convertAlertToModelPopUp('IP Address cannot be empty when AccessType is RASIP.');
					convertAlertToModelPopUp($("#alertIPNotEmpWhenAccTypRASIPErrorCode").val(), $("#alertIPNotEmpWhenAccTypRASIP").val());
					return false;
				} else {
					checkText = document.getElementById('avayaIP').value;
						if(validateIPAddr(checkText))
							return true;
						else {
							close_popup();
							//convertAlertToModelPopUp('Invalid IP Address. Please correct and proceed.');
							convertAlertToModelPopUp($("#alertInvIpAddrErrorCode").val(), $("#alertInvIpAddr").val());
							return false;
						}
				}
			} else if (document.getElementById('accessType').value == 'Modem') {
				if(document.getElementById('dialIn') != null
					&& document.getElementById('dialIn').value == ''){
					close_popup();
					//convertAlertToModelPopUp('Dial In Number cannot be empty when AccessType is Modem.');
					convertAlertToModelPopUp($("#alertEnterDialInForModemErrorCode").val(), $("#alertEnterDialInForModem").val());
					return false;
				} else {
					checkText = document.getElementById('dialIn').value;
					checkText = checkText.replace(/^\s+|\s+$/g,'');
					document.getElementById('dialIn').value = checkText;
					RegE = /^(\+\d+) (\d+)( ?)(\d+)\$/;
					RegE1 = /^(\d{10})\$/;
						if(checkText.match(RegE))
							return true;
						else if(checkText.match(RegE1))
							return true;
						else {
							close_popup();
							//convertAlertToModelPopUp('Invalid DialIn Number. Please correct and proceed.');
							convertAlertToModelPopUp($("#alertInvalidDialInErrorCode").val(), $("#alertInvalidDialIn").val());
							return false;
						}
				}
			} else if(document.getElementById('accessType').value == 'SAL'){
				if(document.getElementsByName('primary') != null){
					while(primary.length == 0 && document.getElementsByName('primary')[index] != null){
						if(document.getElementsByName('primary')[index].checked){
							primary = document.getElementsByName('primary')[index].value;
							//convertAlertToModelPopUp('Primary:	'+primary);
							document.getElementById('primarySeid').value = primary;
						}
						index ++;
					}
				}
				/*if(primary == ''){
					close_popup();
					convertAlertToModelPopUp('If AccessType is SAL, Primary SAL is mandatory.');
					return false;
				}*/
				index = 0;
				if(document.getElementsByName('secondary') != null){
					while(secondary.length == 0 && document.getElementsByName('secondary')[index] != null){
						if(document.getElementsByName('secondary')[index].checked){
							secondary = document.getElementsByName('secondary')[index].value;
							//convertAlertToModelPopUp('secondary:	'+secondary);
							document.getElementById('secondarySeid').value = secondary;
						}
						index ++;
					}
				}
			}
		} else {
			close_popup();
			//convertAlertToModelPopUp('Please select AccessType.');
			convertAlertToModelPopUp($("#alertEnterAccTypeErrorCode").val(), $("#alertEnterAccType").val());
			return false;
		}
		return true;		
	}

	function show_popup(msg, title) {
		/* document.getElementById("dialog-underlay").style.display = "block";
		if(title) {
			document.getElementById("popup-window-title").innerHTML = title;
		}
		document.getElementById("popup-window-content").innerHTML = msg;

		var elePopup = document.getElementById("popup-window");
		var scroll = getScroll();
		var divH = getLength(elePopup.style.height, scroll.h);
		var divW = getLength(elePopup.style.width, scroll.w);
		elePopup.style.top = (scroll.t + (scroll.ch - divH)/2) + "px";
		elePopup.style.left = "auto"; */
	}

	function close_popup(){
		/* document.getElementById("dialog-underlay").style.display = "none";
		document.getElementById("popup-window").style.left = "-1000em"; */
	}

	/* Validate radio button selection */
	function validateSelection(selObj, num){
						//convertAlertToModelPopUp('Sel :'+selObj+'	num:'+num);
						//convertAlertToModelPopUp(selObj.name);
						if(selObj.name == 'primary'){
									if(document.getElementsByName('secondary')[num].checked){
												//convertAlertToModelPopUp('One Gateway cannot be selected as both primary and secondary.');
												convertAlertToModelPopUp($("#alertNoCommonGatewayErrorCode").val(), $("#alertNoCommonGateway").val());
												document.getElementsByName('secondary')[num].checked = false;
												return false;
									}
						} else if(selObj.name == 'secondary'){
									var i=0;
									var flag = 0;
									while(document.getElementsByName('primary')[i] != null){
												if(document.getElementsByName('primary')[i].checked){
														flag = 1;
															break;
												}
												i++;
									}
									if(flag == 0){
												//convertAlertToModelPopUp('Please select primary SAL Gateway and then proceed for secondary SAL Gateway selection.');
												convertAlertToModelPopUp($("#alertSelPriThanSecGtwayErrorCode").val(), $("#alertSelPriThanSecGtway").val());
												document.getElementsByName('secondary')[num].checked = false;
												return false;
									}
									//convertAlertToModelPopUp(document.getElementsByName('p')[num].checked);
									if(document.getElementsByName('primary')[num].checked){
												//convertAlertToModelPopUp('One Gateway cannot be selected as both primary and secondary.');
												convertAlertToModelPopUp($("#alertNoCommonGatewayErrorCode").val(), $("#alertNoCommonGateway").val());
												document.getElementsByName('secondary')[num].checked = false;
												return false;
									}
						}
						return true;
			}

			function resetSALGatewaySelected(){
		var i=0;
		//if(confirm('Reset on primary SAL Gateway will reset the secondary SAL Gateway also (If applicable). Are you sure to reset both the SAL Gateways?')){
		if(confirm($("#cnfrmResetGateway").val())){		
			while(document.getElementsByName('primary')[i] != null){
				document.getElementsByName('primary')[i].checked = false;
				i++;
			}
			document.getElementById('primarySeid').value = '';
			resetSecondarySALGateway();
		}
		}

		function resetSecondarySALGateway(){
			var i=0;
			while(document.getElementsByName('secondary')[i] != null){
			document.getElementsByName('secondary')[i].checked = false;
			i++;
		}
		document.getElementById('secondarySeid').value = '';
		}

		function validateSoldToInput(){
			var checkText = document.getElementById('soldToForAddToList').value;
			RegE = /^(\d{10})\$/;
		if(checkText.match(RegE))
				return true;
			else {
				//convertAlertToModelPopUp('Invalid SoldToId. Please correct and proceed.');
				convertAlertToModelPopUp($("#alertInvalidSoldToErrorCode").val(), $("#alertInvalidSoldTo").val());
				return false;
			}
		}

		function validateSEIDInput(){
			var checkText = document.getElementById('salSeIdForAddToList').value;
			RegE = /^\((\d{3})\)(\d{3})(\-)(\d{4})\$/;
		if(checkText.match(RegE))
				return true;
			else {
				//convertAlertToModelPopUp('Invalid SEID format. Please correct and proceed.');
				convertAlertToModelPopUp($("#alertInvalidSeIdFormatErrorCode").val(), $("#alertInvalidSeIdFormat").val());
				return false;
			}
		}

		function validateInputs(){
			document.getElementById('screenName').value = 'TOBCONFIGUPDATE';
			var soldTo = document.getElementById('soldToForAddToList').value;
			var seid = document.getElementById('salSeIdForAddToList').value;
			if(soldTo == '' && seid == ''){
				//convertAlertToModelPopUp('Please enter either valid SoldTo or SEID or both as input(s).');
				convertAlertToModelPopUp($("#alertEnterValidSoldToOrSeIdErrorCode").val(), $("#alertEnterValidSoldToOrSeId").val());
				return false;
			} else if(soldTo != '' && validateSoldToInput()){
				return true;
			} else if(seid != '' && validateSEIDInput()){
				return true;
			} else {
				return false;
			}
		}

		function validateAFID(obj){
			var authId = obj.value.length;
			if (authId > 0 && authId < 10) {
				//convertAlertToModelPopUp('AFID has to be a 10 digit number');
				convertAlertToModelPopUp($("#alertAFIDMaxTenDigErrorCode").val(), $("#alertAFIDMaxTenDig").val());
				return false;
			}
		}

		function numericFilter(txb) {
			 txb.value = txb.value.replace(/[^0-9]/ig, "");
		}
		
		function submitForm(actionName){
			var url = document.getElementById(actionName).href;
			document.getElementById('technicalRegistrationForm').action = url;
			document.getElementById('technicalRegistrationForm').submit();
		}
		
		function processFormSubmit(submitVal){
			if(processBeforeSubmit()){
				if(submitVal=="Submit"){
					submitForm('updateAction');
				}else if(submitVal=="Add"){
					submitForm('addAction');
				}
			}
		}
		
		function addToListSALGW_Action(){
			if(validateInputs()){
				submitForm('addToListSALGWAction');
			}
		}

</script>

<!-- start page content-wrap -->
<div class="content-wrap tob-update">

	<h1 class="page-title">Technical On-Boarding Configuration Update</h1>

	<!-- start page content -->
	<div class="content">

		<!-- start registration summary -->
		<div class="registration-site-summary collapse-box">
			<h2 class="collapse-box-header active">Registration Site Summary <a href="#"><span></span></a></h2>

			<div class="data collapse-box-container">
				<div class="col">
					<p><span class="title">Registration ID:	</span><span>${actionForm.registrationId}</span></p>
					<p><span class="title">Material Code(Description):	</span><span>${actionForm.technicalRegistrationSummary.materialCode}(${actionForm.technicalRegistrationSummary.materialCodeDescription})</span></p>
				</div>
				<div class="col">
					<p><span class="title">Sold To/FL: </span><span>${actionForm.soldToId}</span></p>
					<p><span class="title">Access Type: </span><span>${actionForm.technicalRegistrationSummary.accessType}</span></p>
				</div>
				<div class="col">
					<p><span class="title">Customer Name: </span><span>${actionForm.company}</span></p>
					<p><span class="title">SEID: </span><span>${actionForm.technicalRegistrationSummary.seId}</span></p>
				</div>
			</div>
		</div>
		<!-- end registration summary -->

		<!-- start technicalRegistrationForm -->
		<form name="technicalRegistrationForm" action="<c:url context="${ _path }" value="/technicalonboarding/addRegistrableProductsReadyToProcess.action" />" id="technicalRegistrationForm" method="post">
			<input type="hidden" id="groupId" name="actionForm.groupId" value="${ actionForm.groupId }">
			<a id="populateTRConfigAction" href="<c:url context="${ _path }" value="/technicalonboarding/populateTRConfig.action" />" ></a>
			<input type="hidden" id="primarySeid" name="actionForm.primary" value="${actionForm.primary}">
			<input type="hidden" id="secondarySeid" name="actionForm.secondary" value="${actionForm.secondary }">
			<input type="hidden" id="screenName" name="actionForm.screenName" value="${ actionForm.screenName }">
			<input type="hidden" id="userNamePasswordFlag" name="actionForm.userNamePasswordFlag" value="${ actionForm.userNamePasswordFlag }">
			<a id="cancelAction" href="<c:url context="${ _path }" value="/technicalonboarding/technicalRegistrationDashboard.action" />"></a>
			<a id="addAction" href="<c:url context="${ _path }" value="/technicalonboarding/addRegistrableProductsReadyToProcess.action" />"></a>
			<a id="updateAction" href="<c:url context="${ _path }" value="/technicalonboarding/updateRegistrableProductsReadyToProcess.action" />"></a>
			<a id="addToListSALGWAction" href="<c:url context="${ _path }" value="/technicalonboarding/addToList.action" />"></a>

			<!-- start tob-config-table-wrap -->
			<div>
				<div id="tob-config-table-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">Product configuration data
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div id="" class="collapse-box-container">

						<div class="data">
							<div class="row">
								<div class="col">
									<p>
										<span class="title">Group:	</span>
										<select name="actionForm.technicalRegistrationSummary.groupId"
										onChange="populateTRConfig(this.value);" >
										<c:forEach items="${ actionForm.groupIds }" var="eat">
										<option ${ ( eat.key == actionForm.technicalRegistrationSummary.groupId ) ? 'selected' : '' } value="${ eat.key }">${ eat.value }</option>
										</c:forEach>
										</select>
									</p>
								</div>
								<div class="col">
									<p>
										<span class="title">Product:	</span>
										${actionForm.technicalRegistrationSummary.trConfig.productTypeDescription}
									</p>
									<p>
										<span class="title">Solution Element: </span>
										${actionForm.technicalRegistrationSummary.trConfig.seCodeDescription}
									</p>
								</div>
								<div class="col" id="templateDiv" ${actionForm.templateFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">Template: </span>
										${actionForm.technicalRegistrationSummary.trConfig.templateDescription}
									</p>
								</div>
							</div>
						</div>

					</div>
				</div>
			</div>
			<!-- end tob-config-table-wrap -->

			<!-- start tob-sal-table-wrap -->
			<div ${actionForm.salMigrationFlag == 'none' ? 'style="display:none"' : ''}>
				<div id="tob-sal-table-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">SAL Gateway
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div id="productRegistrationDivId" class="collapse-box-container">

						<%@ include file="addToList.jsp" %>

						<div id="existingSALData" class="basic-table-wrap">
							<table class="basic-table">
								<thead>
									<tr>
										<th width="16%">Sold To (FL) of SAL Gateway</th>
										<th width="16%">Material Code</th>
										<th width="16%">SE Code</th>
										<th width="16%">SEID</th>
										<th width="16%">Primary	&nbsp;&nbsp;<input type="button" class="button disabled small" value="Reset" onClick="javascript:resetSALGatewaySelected(); return false;" disabled="true"/></th>
										<th width="20%">Secondary	&nbsp;&nbsp;<input type="button" class="button disabled small" value="Reset" onClick="javascript:resetSecondarySALGateway(); return false;" disabled="true"/></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${ actionForm.salGatewayMigrationList }" var="item" varStatus="container">
									<tr>
										<c:set var="currentSeid" value="${item.seid}"/>
										<td>${item.soldTo}</td>
										<td>${item.materialCode}</td>
										<td>${item.seCode}</td>
										<td><a href="#" onclick="showSALGatewayDetails('${item.seid}', '${item.soldTo}')"	>${item.seid}</a></td>
										<%
															String seId = (String) pageContext.getAttribute("currentSeid");
											String primarySeid =	(String )request.getSession().getAttribute(com.grt.util.GRTConstants.PRIMARY_SE_ID);
											if(seId !=null && seId.equalsIgnoreCase(primarySeid)){
										%>
										<td><input name="primary" value="${item.seid}" type="radio" id="primaryseidradio${container.index}" onclick="javascript:validateSelection(this, '${container.index}');" checked="true" /></td>
										<%} else { %>
										<td><input name="primary" value="${item.seid}" type="radio" id="primaryseidradio${container.index}" onclick="javascript:validateSelection(this, '${container.index}');"  /></td>
										<% }
											String secondarySeid =	(String )request.getSession().getAttribute(com.grt.util.GRTConstants.SECONDARY_SE_ID);
											if(seId !=null && seId.equalsIgnoreCase(secondarySeid)){
										%>
										<td><input name="secondary" value="${item.seid}" type="radio" id="secondatyseidradio${container.index}" onclick="javascript:validateSelection(this, '${container.index}');" checked="true"  /></td>
										<% } else { %>
										<td><input name="secondary" value="${item.seid}" type="radio" id="secondatyseidradio${container.index}" onclick="javascript:validateSelection(this, '${container.index}');"  /></td>
										<%} %>
									</tr>
									</c:forEach>
									<%
									request.getSession().removeAttribute(com.grt.util.GRTConstants.PRIMARY_SE_ID);
									request.getSession().removeAttribute(com.grt.util.GRTConstants.SECONDARY_SE_ID);
									%>
								</tbody>
							</table>
						</div>

					</div>
				</div>
				<div class="error-bar" ${actionForm.errorMessage ? 'style="display:block"' : 'style="display:none"'}>
					<i class="fa fa-exclamation-triangle"></i><span class="error-bar-msg">${actionForm.errorMessage}</span>
				</div>
			</div>
			<!-- end tob-sal-table-wrap -->

			<!-- start tob-product-table-wrap -->
			<div>
				<div id="tob-product-table-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">Product registration data
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div class="collapse-box-container">

						<div class="data">
							<div class="row">
								<div class="col" id="accessTypeDiv" ${actionForm.accessTypeFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">Access Type:	</span>
										<select id="accessType" name="actionForm.technicalRegistrationSummary.accessType"
										onChange="enableIPAddressOrDialIn(this);">
										<c:forEach items="${ actionForm.accessTypes }" var="accType">
										<option ${ ( accType.key == actionForm.technicalRegistrationSummary.accessType ) ? 'selected' : '' } value="${accType.key }">${accType.value }</option>
										</c:forEach>
										</select>
									</p>
								</div>
								<div class="col" id="swReleaseDiv" ${actionForm.swReleaseFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">Software Release: </span>
										<select name="actionForm.technicalRegistrationSummary.softwareRelease">
											<c:forEach items="${ actionForm.releasesMap }" var="rel">
											<option ${ ( rel.key == actionForm.technicalRegistrationSummary.softwareRelease ) ? 'selected' : '' } value="${rel.key }">${rel.value }</option>
											</c:forEach>
										</select>
									</p>
								</div>
								<div class="col" id="connectivityDiv" ${actionForm.connectivityFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">Connectivity:	</span>
										<select name="actionForm.technicalRegistrationSummary.connectivity" id="connectivity" onchange="onConnectivityChange(this)">
											<c:forEach items="${ actionForm.connectivities }" var="conn">
												<option ${ ( conn == actionForm.technicalRegistrationSummary.connectivity ) ? 'selected' : '' } value="${conn }">${conn }</option>
											</c:forEach>
										</select>
									</p>
								</div>
								<%-- <div class="col" id="Alarm_Origination" ${actionForm.alarmOriginationFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">Alarm Origination: </span>
										<select name="actionForm.technicalRegistrationSummary.alarmOrigination">
										<c:forEach items="${ actionForm.alarmOriginations }" var="alarm">
											<option ${ ( alarm == actionForm.technicalRegistrationSummary.alarmOrigination ) ? 'selected' : '' } value="${alarm }">${alarm }</option>
										</c:forEach>
										</select>
									</p>
								</div> --%>
							</div>
						</div>

						<div class="data">
							<div class="row">
								<div class="col" id="ipAddrDiv" ${actionForm.ipAddrFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">IP Address: </span>
										<input type="text" name="actionForm.technicalRegistrationSummary.ipAddress" id="avayaIP" />
									</p>
								</div>
							</div>
						</div>

						<div class="data">
							<div class="row">
								<div class="col" id="dialInDiv" ${actionForm.dialInFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">Dial-in Number: </span>
										<tl:toolTip id="outboundCallingPrefixToolTip" message="${bundle.msg.dialInNumberToolTip}"></tl:toolTip>
										<input type="text" name="actionForm.technicalRegistrationSummary.dialInNumber" value="${actionForm.technicalRegistrationSummary.dialInNumber }" id="dialIn">
									</p>
								</div>
							</div>
						</div>

						<div class="data">
							<div class="row">
								<div class="col" id="authFileDiv" ${actionForm.authFileIDFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">Authentication File ID (Optional):	</span>
										<input value="${ actionForm.technicalRegistrationSummary.authFileID }" type="text" name="actionForm.technicalRegistrationSummary.authFileID" id="avayaFID" maxlength="10" onkeyup="javascript:numericFilter(this);" onblur="javascript:validateAFID(this);">
									</p>
								</div>
							</div>
						</div>

						<div class="data">
							<div class="row">
								<div class="col" id="modifyUPdiv userPassDiv" ${actionForm.userNamePasswordFlag == 'none' ? 'style="display:none"' : ''} >
									<p>
										<span class="title">Modify Username/Password:	</span>
										<input type="checkbox" id="modUPcheckBx" />
									</p>
								</div>
							</div>
							<div class="row">
								<div class="col userPassDiv" id="usernameDiv" ${actionForm.userNamePasswordFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">User Name:	</span>
										<input type="text" name="actionForm.technicalRegistrationSummary.userName" id="username" value=""/>
									</p>
								</div>
								<div class="col userPassDiv" id="passwordDiv" ${actionForm.userNamePasswordFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">Password:	</span>
										<input type="password" name="actionForm.technicalRegistrationSummary.password" id="password" value="">
									</p>
								</div>
								<div class="col userPassDiv" id="reTypePassDiv" ${actionForm.userNamePasswordFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">Retype Password:	</span>
										<input type="password" id="retypePassword"/>
									</p>
								</div>
							</div>
							<div class="row">
								<div class="col">
									<p>
										<span class="title">Asset Nick Name:	</span>
										<input type="text" name="actionForm.technicalRegistrationSummary.nickName" value="${actionForm.technicalRegistrationSummary.nickName }" id="nickName">
									</p>
								</div>
							</div>
						</div>

					</div>

					<div class="error-bar" id="sidMidDiv" ${actionForm.sidMidErrorFlag == 'none' ? 'style="display:none"' : ''}>
						<i class="fa fa-exclamation-triangle"></i><span class="error-bar-msg">${actionForm.sidMidStatus}</span>
					</div>

				</div>
			</div>
			<!-- end tob-product-table-wrap -->

			<!-- start controls -->
			<div class="controls">
				<input type="button" class="button gray" value="Cancel" onClick="return submitForm('cancelAction');">
				<!--				<netui:button value="${bundle.msg.buttonAdd}" styleClass="osm_large_button" action="addRegistrableProductsReadyToProcess" onClick="return processBeforeSubmit();"/>&nbsp;&nbsp;-->
				<%
				String isFromTrObDetail = (String)request.getSession().getAttribute(com.grt.util.GRTConstants.FROM_TR_OB_DETAIL);
				if(com.grt.util.GRTConstants.TRUE.equalsIgnoreCase(isFromTrObDetail)){
				%>
				<input type="button" class="button gray" value="Submit" onclick="return processFormSubmit('Submit');">
				<% } else { %>
				<input type="button" class="button gray" value="Add" onclick="return processFormSubmit('Add');">
				<% } %>
			</div>
			<!-- end controls -->

		</form>
		<!-- end technicalRegistrationForm -->
		<!-- Error Msg start -->
		<input type="hidden" id="alertInvIpAddr" value="${grtConfig.alertInvIpAddr}" />	
		<input type="hidden" id="alertEnterDialInForModem" value="${grtConfig.alertEnterDialInForModem}" />
		<input type="hidden" id="alertInvalidDialIn" value="${grtConfig.alertInvalidDialIn}" />
		<input type="hidden" id="alertIPNotEmpWhenAccTypRASIP" value="${grtConfig.alertIPNotEmpWhenAccTypRASIP}" />
		<input type="hidden" id="alertEnterAccType" value="${grtConfig.alertEnterAccType}" />
		<input type="hidden" id="alertPswrdRetypePwdSame" value="${grtConfig.alertPswrdRetypePwdSame}" />
		<input type="hidden" id="alertSelPriThanSecGtway" value="${grtConfig.alertSelPriThanSecGtway}" />
		<input type="hidden" id="alertInvalidSoldTo" value="${grtConfig.alertInvalidSoldTo}" />
		<input type="hidden" id="alertInvalidSeIdFormat" value="${grtConfig.alertInvalidSeIdFormat}" />
		<input type="hidden" id="alertEnterValidSoldToOrSeId" value="${grtConfig.alertEnterValidSoldToOrSeId}" />
		<input type="hidden" id="alertAFIDMaxTenDig" value="${grtConfig.alertAFIDMaxTenDig}" />
		<input type="hidden" id="alertNoCommonGateway" value="${grtConfig.alertNoCommonGateway}" />
		<input type="hidden" id="cnfrmResetGateway" value="${grtConfig.cnfrmResetGateway}" />
		<input type="hidden" id="cnfrmRetestAlalrmConn" value="${grtConfig.cnfrmRetestAlalrmConn}" />
		
		<!-- Error code start -->
		<input type="hidden" id="alertInvIpAddrErrorCode" value="${grtConfig.ewiMessageCodeMap['alertInvIpAddr']}"/>
		<input type="hidden" id="alertEnterDialInForModemErrorCode" value="${grtConfig.ewiMessageCodeMap['alertEnterDialInForModem']}"/>
		<input type="hidden" id="alertInvalidDialInErrorCode" value="${grtConfig.ewiMessageCodeMap['alertInvalidDialIn']}"/>
		<input type="hidden" id="alertIPNotEmpWhenAccTypRASIPErrorCode" value="${grtConfig.ewiMessageCodeMap['alertIPNotEmpWhenAccTypRASIP']}"/>
		<input type="hidden" id="alertEnterAccTypeErrorCode" value="${grtConfig.ewiMessageCodeMap['alertEnterAccType']}"/>
		<input type="hidden" id="alertPswrdRetypePwdSameErrorCode" value="${grtConfig.ewiMessageCodeMap['alertPswrdRetypePwdSame']}"/>
		<input type="hidden" id="alertSelPriThanSecGtwayErrorCode" value="${grtConfig.ewiMessageCodeMap['alertSelPriThanSecGtway']}"/>
		<input type="hidden" id="alertInvalidSoldToErrorCode" value="${grtConfig.ewiMessageCodeMap['alertInvalidSoldTo']}"/>
		<input type="hidden" id="alertInvalidSeIdFormatErrorCode" value="${grtConfig.ewiMessageCodeMap['alertInvalidSeIdFormat']}"/>
		<input type="hidden" id="alertEnterValidSoldToOrSeIdErrorCode" value="${grtConfig.ewiMessageCodeMap['alertEnterValidSoldToOrSeId']}"/>
		<input type="hidden" id="alertAFIDMaxTenDigErrorCode" value="${grtConfig.ewiMessageCodeMap['alertAFIDMaxTenDig']}"/>
		<input type="hidden" id="alertNoCommonGatewayErrorCode" value="${grtConfig.ewiMessageCodeMap['alertNoCommonGateway']}"/>
		<input type="hidden" id="cnfrmResetGatewayConfCode" value="${grtConfig.ewiMessageCodeMap['cnfrmResetGateway']}"/>
		<input type="hidden" id="cnfrmRetestAlalrmConnConfCode" value="${grtConfig.ewiMessageCodeMap['cnfrmRetestAlalrmConn']}"/>
		
		
	</div>
	<!-- end page content -->
</div>
<!-- end page content-wrap -->
