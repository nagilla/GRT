<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.grt.dto.TechnicalOrderDetail"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.grt.dto.RegistrationFormBean" %>
<%@ page import="java.util.List"%>
<%@ page import="com.grt.dto.Pager"%>
<%@ include file="/includes/context.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/views/tob-config.css" />"/>
<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/plugins/jquery.dataTables_themeroller.css" />" />

<script src='<c:url context="${ _path }"	value="/scripts/plugins/jquery_maskedinput.js" />'></script>
<script type="text/javascript">
	jQuery(function($) {
		$.mask.definitions['~']='[+-]';
		$('#cmMainSeid').mask('(999)999-9999');
		$('#salSeIdForAddToList').mask('(999)999-9999');
		$('#seidOfVoicePortal').mask('(999)999-9999');
		$('#auxMCMainSEID').mask('(999)999-9999');
		$("div.loading-overlay").hide();
	});
</script>
<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/plugins/dynamicTable.js" />"></script>
<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/technicalonboarding/grt.tob.update.view.js" />"></script>

<script type="text/javascript">
	function hide(elemId) {
		if( document.getElementById(elemId)!=null ){
			document.getElementById(elemId).style.display='none';
		}
	}
	function show(elemId) {
		if( document.getElementById(elemId)!=null ){
			document.getElementById(elemId).style.display='block';
		}
	}
	function closePopUp() {
		hide('errorMessagePopUp');
		hide('fade');
	}
	function lssOrESSLowerVersionNoteClosePopUp() {
		hide('lssOrESSLowerVersionNotePopUp');
		hide('fade');
	}
	function populateTRConfig(grpId){

		// document.getElementById('groupId').value = grpId;
		var url=document.getElementById('populateTRConfigAction').href;
		document.getElementById('technicalRegistrationForm').action=url;
				document.getElementById('technicalRegistrationForm').submit();
	}

	function populateTRConfigOnGroupId(grpId){
		$("div.loading-overlay").show();
		document.getElementById('lssOrESSLowerVersionFlag').value = '';
		document.getElementById('upgradedMainCMuseRFASIDFlag').value = '';
		hide('errorMessagePopUp');
		hide('fade');
		document.getElementById('groupId').value = grpId;
		var url=document.getElementById('populateTRConfigOnGroupIdAction').href;
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
		$.ajax({
				url : _path+'/technicalonboarding/json/getSALGatewayDetails.action?gatewaySeid='+seid+'&soldToId='+soldTo,
				dataType : 'json',
				success : function(data) {
				if(data) {
					showSALGatewayPopup(data);
				}
			}
		});
	}
	function getRadioValue(id) {
		return document.getElementById(id).checked;
	}
	function enableInfoDiv(value){
		if(value == 'yes'){
			document.getElementById('cmMainInfoDiv').style.display = 'none';
			document.getElementById('isMainSeidFlag').value = 'none';
			document.getElementById('sid').disabled = false;
			document.getElementById('cmMainSeid').value = '';
			document.getElementById('message').value = '';
			document.getElementById('mainDiv').style.display = 'none';
		} else {
			document.getElementById('cmMainInfoDiv').style.display = 'block';
			document.getElementById('isMainSeidFlag').value = 'block';
		}
		show('salGateWayDivId');
		show('productRegistrationDivId');
		document.getElementById('addButton').style.visibility='visible';
		$("div.loading-overlay").show();
		var url=document.getElementById('populateTRConfigAction').href;
		document.getElementById('technicalRegistrationForm').action=url;
			 	document.getElementById('technicalRegistrationForm').submit();

	}
	
	function enableAUXMCMainSEIDDiv(value){
		if(value == 'yes'){
			document.getElementById('auxMCMainSEID').value = '';
			
		}else{
			document.getElementById('auxMCMainSEID').value = '';
		}
		$("div.loading-overlay").show();
		var url=document.getElementById('populateTRConfigAction').href;
		document.getElementById('technicalRegistrationForm').action=url;
		document.getElementById('technicalRegistrationForm').submit();
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

	function validateSidandMid(){
		// Set Primary and Secondary SEIDs to form properties before validating SIDnMID. To retain the SEID selection.
		setPrimaryAndSecondarySEIDs();
		if(document.getElementById('sid').value == ''){
				convertAlertToModelPopUp($("#alertEnterSIdErrorCode").val(), $("#alertEnterSId").val());
				return false;
		}
		if(document.getElementById('mid').value == ''){
				convertAlertToModelPopUp($("#alertEnterMIdErrorCode").val(), $("#alertEnterMId").val());
				return false;
		}
		if(!validateSID(document.getElementById('sid'))){
			return false;
		}
		if((document.getElementById('highAvailablity').value == 'true') && (document.getElementById('mid').value != '1')){
			convertAlertToModelPopUp($("#alertEnterMidAsOneForHAErrorCode").val(), $("#alertEnterMidAsOneForHA").val());
			return false;
		}
		if((document.getElementById('productType').value == 'radvsn') && (document.getElementById('mid').value != '1')){
			convertAlertToModelPopUp($("#alertEnterMidAsOneForRadvisionErrorCode").val(), $("#alertEnterMidAsOneForRadvision").val());
			return false;
		}
		if(document.getElementById('isMainSeidFlag').value == 'block'){
			if(document.getElementById('readOnly').value
				&& document.getElementById('mid').value == '1'){
				convertAlertToModelPopUp($("#aletEnterMidForRemSidErrorCode").val(), $("#aletEnterMidForRemSid").val());
				return false;
			}
		}
		if((document.getElementById('mainYes').checked) && (document.getElementById('mid').value != '1')
			&& (document.getElementById('cmProduct').value == 'true')){
			//convertAlertToModelPopUp('MID has to be 1 for main.');
			convertAlertToModelPopUp($("#aletEnterMidForMainErrorCode").val(), $("#aletEnterMidForMain").val());
			return false;
		}
		
		submitForm('validateSIDMIDAction');
		
	}

	function processBeforeSubmit(submitVal){
		var failedSeidNotNull = ${actionForm.failedSeidIsNotNull};
		$("div.loading-overlay").show();
		var accType=$("#popUpHiddenValue").val();
		var cmFlag = document.getElementById('cmMainFlag').value;
				var checkText = "";
				var RegE = "";
				
				if(document.getElementById('softwareReleaseFlag').value == 'block'){					
					if(document.getElementById('softwareRelease_Select') != null && document.getElementById('softwareRelease_Select').value == 'Choose'){
						$("div.loading-overlay").hide();
						convertAlertToModelPopUp($("#alertSoftwareReleaseErrorCode").val(), $("#alertSoftwareReleaseErrorMsg").val());
						return false;
					}
				}
				//AUXMC Check
				
				var auxMCFlag = document.getElementById('auxMCShowFlag').value;
				
				if(auxMCFlag != null && auxMCFlag == 'block'){
					if(!getRadioValue('auxMCYes') && !getRadioValue('auxMCNo')){
						$("div.loading-overlay").hide();
						convertAlertToModelPopUp($("#alertSelectAUXMCTypeErrorCode").val(), $("#alertSelectAUXMCType").val());
						return false;
					}
					if(document.getElementById('auxMCMainSEIDRequiredFlag').value == 'true'){
						if (document.getElementById('auxMCMainSEID').value == ''){
							$("div.loading-overlay").hide();
							convertAlertToModelPopUp($("#alertEnterAUXMCMainSEIDErrorCode").val(), $("#alertEnterAUXMCMainSEID").val());
							return false;
						}
						if ((document.getElementById('tempAUXMCMainSEID').value != document.getElementById('auxMCMainSEID').value)) {
							$("div.loading-overlay").hide();
							convertAlertToModelPopUp($("#alertValAUXMCMainSEIDErrorCode").val(), $("#alertValAUXMCMainSEID").val());
							return false;
						}
						if(document.getElementById('auxMCMainSEIDErrorFlag').value == 'block'){
							$("div.loading-overlay").hide();
							convertAlertToModelPopUp($("#alertClrErrorErrorCode").val(), $("#alertClrError").val());
							return false;
						}
					}
				}
				
				// Not allowing the user till Error would get cleared only when, if user enters a valid SeID or he selects cmMain Yes radio button.
				if(document.getElementById('isMainSeidFlag').value == 'block'){
					if(document.getElementById('message').value != '' && accType!="No Connectivity"){
						$("div.loading-overlay").hide();
						convertAlertToModelPopUp($("#alertClrCmErrErrorCode").val(), $("#alertClrCmErr").val());
						return false;
					}
					if(document.getElementById('cmMainsoldTo').value == '' && cmFlag != null && cmFlag == 'block' && accType!="No Connectivity"){
						$("div.loading-overlay").hide();
						convertAlertToModelPopUp($("#alertEnterValMainSeidErrorCode").val(), $("#alertEnterValMainSeid").val());
						return false;
					}
				}

				if(!failedSeidNotNull){
				if(document.getElementById('sidMidRequiredFlag').value == 'true'){
					if (document.getElementById('sid').value == '' || document.getElementById('mid').value ==''){
						$("div.loading-overlay").hide();
						convertAlertToModelPopUp($("#alertEnterSidMidErrorCode").val(), $("#alertEnterSidMid").val());
						return false;
					}
					if ((document.getElementById('tempSid').value != document.getElementById('sid').value)
					|| (document.getElementById('tempMid').value != document.getElementById('mid').value)) {
						$("div.loading-overlay").hide();
						convertAlertToModelPopUp($("#alertValSidMidErrorCode").val(), $("#alertValSidMid").val());
						return false;
					}
				}
				if(document.getElementById('sidMidErrorFlag').value == 'block'){
					$("div.loading-overlay").hide();
					convertAlertToModelPopUp($("#alertClrErrorErrorCode").val(), $("#alertClrError").val());
					return false;
				}
				}

				if(document.getElementById('seidOfVoicePortalFlag').value == 'block'
					&& document.getElementById('seidOfVoicePortal').value == '' && accType!="No Connectivity" ){
					$("div.loading-overlay").hide();
					convertAlertToModelPopUp($("#alertSeIdNotEmpForVPErrorCode").val(), $("#alertSeIdNotEmpForVP").val());
					return false;
				}

				if(document.getElementById('privateIpEligibleFlag').value == 'block' && accType!="No Connectivity"){
						if(document.getElementById('privateIP') != null
							&& document.getElementById('privateIP').value == '' && accType!="No Connectivity"){
							if(document.getElementById('productType').value.toUpperCase() != 'IPOFC'){
								$("div.loading-overlay").hide();
								convertAlertToModelPopUp($("#alertPvtIPNotEmptyErrorCode").val(), $("#alertPvtIPNotEmpty").val());
								if(submitVal=="Submit")
									updateTechnicalRegistrationConfig();
								else
								return false;
							}
						} else {
							checkText = document.getElementById('privateIP').value;
								if(validateIPAddr(checkText)) {
									//return true;
								} else {
									$("div.loading-overlay").hide();
									convertAlertToModelPopUp($("#alertEntCorrPvtIPErrorCode").val(), $("#alertEntCorrPvtIP").val());
									return false;
								}
						}
				}
				
				if(document.getElementById('accessType') != null && document.getElementById('accessType').value != '' ){
					if(document.getElementById('accessType').value == 'RASIP' ) {
						if(document.getElementById('ipAddress') != null
							&& document.getElementById('ipAddress').value == '' && accType!="No Connectivity" ){
							$("div.loading-overlay").hide();
							convertAlertToModelPopUp($("#alertIpNotEmpForAccTypIPErrorCode").val(), $("#alertIpNotEmpForAccTypIP").val());
							return false;
						} else {
							checkText = document.getElementById('ipAddress').value;
								if(validateIPAddr(checkText))
									if(submitVal=="Submit")
										updateTechnicalRegistrationConfig();
									else
									return true;
								else {
									$("div.loading-overlay").hide();
									convertAlertToModelPopUp($("#alertInvIpAddrErrorCode").val(), $("#alertInvIpAddr").val());
									return false;
								}
						}
					} else if (document.getElementById('accessType').value == 'Modem') {
						if(document.getElementById('dialIn') != null
							&& document.getElementById('dialIn').value == '' && accType!="No Connectivity" ){
							$("div.loading-overlay").hide();
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
									if(submitVal=="Submit")
										updateTechnicalRegistrationConfig();
									else
									return true;
								else if(checkText.match(RegE1))
									if(submitVal=="Submit")
										updateTechnicalRegistrationConfig();
									else
									return true;
								else {
									$("div.loading-overlay").hide();
									//convertAlertToModelPopUp('Invalid DialIn Number. Please correct and proceed.');
									convertAlertToModelPopUp($("#alertInvalidDialInErrorCode").val(), $("#alertInvalidDialIn").val());
									return false;
								}
						}
					} else if(document.getElementById('accessType').value == 'SAL'){
						setPrimaryAndSecondarySEIDs();
						var primary = document.getElementById('primarySeid').value;
						var secondary = document.getElementById('secondarySeid').value;
						var spProduct = document.getElementById('spProduct').value;
						var seCode = document.getElementById('seCode').value.toUpperCase();
						var specialNote = document.getElementById('specialNote').value.toUpperCase();
						var productType = document.getElementById('productType').value.toUpperCase();
						if (seCode == 'VCM' && specialNote == 'CAAS' && productType == 'SPTEM'&& primary == '' && secondary == '' && accType!="No Connectivity" ) {
							$("div.loading-overlay").hide();
							convertAlertToModelPopUp($("#alertEnterSEIdForCaasErrorCode").val(), $("#alertEnterSEIdForCaas").val());
								return false;
						} else if( spProduct == 'none' && primary == '' && secondary == '' && (seCode != 'SALGW' && seCode != 'VSALGW' && accType!="No Connectivity"  )){
							$("div.loading-overlay").hide();
							convertAlertToModelPopUp($("#alertEnterSEIdForNonSysPrdErrorCode").val(), $("#alertEnterSEIdForNonSysPrd").val());
								return false;
						} 
					}
				} else {
					$("div.loading-overlay").hide();
					if(accType!="No Connectivity"){
					convertAlertToModelPopUp($("#alertEnterAccTypeErrorCode").val(), $("#alertEnterAccType").val());
					return false;
					}
				}
				var userName = document.getElementById('username').value;
				var password = document.getElementById('password').value;
				var userNamePasswordFlag = document.getElementById('userNamePasswordFlag').value;
				if (userNamePasswordFlag == 'block' && (userName == '' || password == '' )) {
					$("div.loading-overlay").hide();
					if(accType!="No Connectivity"){
					convertAlertToModelPopUp($("#alertEntUsrNamPwdErrorCode").val(), $("#alertEntUsrNamPwd").val());
					return false;
					}
				}
				
			if(submitVal=="Submit"){
				updateTechnicalRegistrationConfig();
			}
			return true;
	}
	
	function addAction(submitVal){
		if(processBeforeSubmit(submitVal)){
			submitForm('addAction');
		}
	}

	function setPrimaryAndSecondarySEIDs(){
		var primary = "";
		var secondary = "";
		var index = 0;
		if(document.getElementsByName('primary') != null){
			while(primary.length == 0 && document.getElementsByName('primary')[index] != null){
				if(document.getElementsByName('primary')[index].checked){
					primary = document.getElementsByName('primary')[index].value;
					//alert('Primary:	'+primary);
					document.getElementById('primarySeid').value = primary;
				}
				index ++;
			}
		}
		
		index = 0;
		if(document.getElementsByName('secondary') != null){
			while(secondary.length == 0 && document.getElementsByName('secondary')[index] != null){
				if(document.getElementsByName('secondary')[index].checked){
					secondary = document.getElementsByName('secondary')[index].value;
					//alert('secondary:	'+secondary);
					document.getElementById('secondarySeid').value = secondary;
				}
				index ++;
			}
		}
	}

	function show_popup(msg, title) {
			}

	function close_popup(){
		document.getElementById("dialog-underlay").style.display = "none";
		
	}

	/* Validate radio button selection */
	function validateSelection(selObj, num){
						if(selObj.name == 'primary'){
									if(document.getElementsByName('secondary')[num].checked){
												convertAlertToModelPopUp($("#alertSameGtwayNtAllForPriAndSecErrorCode").val(), $("#alertSameGtwayNtAllForPriAndSec").val());
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
												convertAlertToModelPopUp($("#alertSelPriThanSecGtwayErrorCode").val(), $("#alertSelPriThanSecGtway").val());
												document.getElementsByName('secondary')[num].checked = false;
												return false;
									}
									if(document.getElementsByName('primary')[num].checked){
												convertAlertToModelPopUp($("#alertSameGtwayNtAllForPriAndSecErrorCode").val(), $("#alertSameGtwayNtAllForPriAndSec").val());
												document.getElementsByName('secondary')[num].checked = false;
												return false;
									}
						}
						return true;
			}

			function resetSALGatewaySelected(){
		var i=0;
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
				convertAlertToModelPopUp($("#alertInvalidSoldToErrorCode").val(), $("#alertInvalidSoldTo").val());
				return false;
			}
		}

		function validateSEIDInput(checkText){
			RegE = /^\((\d{3})\)(\d{3})(\-)(\d{4})\$/;
		if(checkText.match(RegE))
				return true;
			else {
				convertAlertToModelPopUp($("#alertInvalidSeIdFormatErrorCode").val(), $("#alertInvalidSeIdFormat").val());
				return false;
			}
		}

		function validateInputs(){
			document.getElementById('screenName').value = 'TOBCONFIG';
			var soldTo = document.getElementById('soldToForAddToList').value;
			var seid = document.getElementById('salSeIdForAddToList').value;
			if(soldTo == '' && seid == ''){
				convertAlertToModelPopUp($("#alertEnterValidSoldToOrSeIdErrorCode").val(), $("#alertEnterValidSoldToOrSeId").val());
				return false;
			} else if(soldTo != '' && validateSoldToInput()){
				return true;
			} else if(seid != '' && validateSEIDInput(seid)){
				return true;
			} else {
				return false;
			}
		}

		function validateCMMain(){
			var seid = document.getElementById('cmMainSeid').value;
			if(seid == ''){
				convertAlertToModelPopUp($("#alertInvalidSeIdAsInputErrorCode").val(), $("#alertInvalidSeIdAsInput").val());
				return false;
			} else if(validateSEIDInput(seid)){
				//submit the form
				$("div.loading-overlay").show();
				var url=document.getElementById('validateCMMainAction').href;
				document.getElementById('technicalRegistrationForm').action=url;
				document.getElementById('technicalRegistrationForm').submit();
				return true;
			} else {
				return false;
			}
		}		
		
		function validateAUXMCMAINSEID(){
			var seid = document.getElementById('auxMCMainSEID').value;
			if(seid == ''){
				convertAlertToModelPopUp($("#alertInvalidSeIdAsInputErrorCode").val(), $("#alertInvalidSeIdAsInput").val());
				return false;
			} else if(validateSEIDInput(seid)){
				//submit the form
				$("div.loading-overlay").show();
				var url=document.getElementById('validateAUXMCMainSEIDAction').href;
				document.getElementById('technicalRegistrationForm').action=url;
				document.getElementById('technicalRegistrationForm').submit();
				return true;
			} else {
				return false;
			}
		}

		function numericFilter(txb) {
			 txb.value = txb.value.replace(/[^0-9]/ig, "");
		}

		function alphaNumericFilter(txb) {
			 txb.value = txb.value.replace(/[^a-z0-9]/ig, "");
		}

		function validateSID(obj){
			var flag = 0;
			var RegExpr1 = /^\T(\d{10})\$/;
			var RegExpr2 = /^\d{5}\$/;
			var RegExpr3 = /^\d+$/;
			var sid = obj.value;
			if(sid != ''){
				if(sid.length == 10 && sid.substring(0,5) == "99900" && sid.substring(5, 11).match(RegExpr2) ) {
					flag = 1;
				} else if(sid.length == 11 && sid.match(RegExpr1)) {
					flag = 1;
				} else if((sid.length != 10 && sid.length != 11) &&	sid.match(RegExpr3) && sid >= 899){
					flag = 1;
				} else {
					convertAlertToModelPopUp($("#alertInvalidSIdFormatErrorCode").val(), $("#alertInvalidSIdFormat").val());
					return false;
				}
				if(flag == 1){
					var mid = document.getElementById('mid');
					if(mid.value != null && mid.value.length > 0){
						if(validateMID(mid)){
							return true;
						} else {
							return false;
						}
					} else {
						return true;
					}
				}
		}
		}

		function validateAFID(obj){
			var authId = obj.value.length;
			if (authId > 0 && authId < 10) {
				convertAlertToModelPopUp($("#alertAFIDMaxTenDigErrorCode").val(), $("#alertAFIDMaxTenDig").val());
				return false;
			}
			document.getElementById('sidMidErrorFlag').value = 'None';
			return true;
		}

		function validateMID(obj){
			var mid = obj.value;
			var sid = document.getElementById('sid').value;
			if(sid != '' && mid != ''){
				if(mid == 0){
					convertAlertToModelPopUp($("#alertMidNotZeroErrorCode").val(), $("#alertMidNotZero").val());
					return false;
				}
				if((sid.length == 10 || sid.length == 11) && (mid >= 1 && mid <= 99999)){
					return true;
				}
				else if(sid.length <= 11 && mid.length > 0) {
					return true;
				} else {
					convertAlertToModelPopUp($("#alertInvalidMIdErrorCode").val(), $("#alertInvalidMId").val());
					return false;
				}
			}
		}


		//GRT 4.0 Chnages
		$(function(){
			var accType=$("#popUpHiddenValue").val();
			$("#accessType").attr("disabled", false);
		 	 $("#connectivity").attr("disabled", false);
		 	 $("#alarmOrigination").attr("disabled", false);
		 	 $("#noConnectivityMessage").hide();
			if(accType=="No Connectivity"){
				 $("#noConnectivityMessage").show();
				$("#accessType").attr("disabled", "disabled");
		 	 $("#connectivity").attr("disabled", "disabled");
		 	 $("#alarmOrigination").attr("disabled", "disabled");
			}


			$("#userNamePassword").hide();


			if($("#modUPcheckBx").is(':checked')) {
			$("#userNamePassword").show();
			};
		});
		function updateTechnicalRegistrationConfig(){
			var url=document.getElementById('updateTechnicalRegistrationConfig').href;
			document.getElementById('technicalRegistrationForm').action=url;
			document.getElementById('technicalRegistrationForm').submit();
		}


		function showUserNamePassword(val)
		{
				$("#userNamePassword").hide();


			if($("#modUPcheckBx").is(':checked')==true)
			{
				$("#userNamePassword").show();
			}
		}
		
		function addToListSALGW_Action(){
			if(validateInputs()){
				submitForm('addToListSALGWAction');
			}
		}
		
		function submitForm(actionName){
			var url = document.getElementById(actionName).href;
			document.getElementById('technicalRegistrationForm').action = url;
			document.getElementById('technicalRegistrationForm').submit();
		}
		
</script>

<!-- start page content-wrap -->
<div class="content-wrap tob-config">

	<h1 class="page-title">Technical On-Boarding Configuration</h1>

	<!-- start page content -->
	<div class="content">

		<!-- start registration summary -->
		<div class="registration-site-summary collapse-box">
			<h2 class="collapse-box-header active">Registration Site Summary <a href="#"><span></span></a></h2>

			<div class="data collapse-box-container">
				<div class="col">
					<p><span class="title">Registration ID:	</span><span>${actionForm.registrationId}</span></p>
					<p><span class="title">Material Code(Description):	</span><span>${actionForm.technicalRegistrationSummary.materialCode}</span></p>
					<p><span class="title">Registration Name: </span><span>${actionForm.registrationIdentifier}</span></p>
				</div>
				<div class="col" style="word-break: break-all;">
					<p><span class="title">Sold To/FL: </span><span>${actionForm.soldToId}</span></p>
					<p><span class="title">Remaining Quantity:	</span><span>${actionForm.technicalRegistrationSummary.remainingQty}</span></p>
					<p><span class="title">Registration Notes: </span><span>${actionForm.registrationNotes}</span></p>
				</div>
				<div class="col">
					<p><span class="title">Customer Name: </span><span>${actionForm.company}</span></p>
					<p><span class="title">Access Type:	</span><span>${actionForm.bannerAccessType}</span></p>
				</div>
			</div>
		</div>
		<!-- end registration summary -->

		<!-- start technicalRegistrationForm -->
		<form id="technicalRegistrationForm" action="<c:url context="${ _path }" value="/technicalonboarding/technicalRegistrationConfig.action" />" method="post">
			<a id="updateTechnicalRegistrationConfig" href="<c:url context="${ _path }" value="/technicalonboarding/updateTechnicalRegistrationConfig.action" />"></a>
			<input type="hidden" id="groupId" name="actionForm.technicalRegistrationSummary.groupId" value="${ fn:escapeXml(actionForm.technicalRegistrationSummary.groupId ) }"/>
			<a id="populateTRConfigAction" href="<c:url context="${ _path }" value="/technicalonboarding/populateTRConfiguration.action" />"
			onclick="anchor_submit_form('technicalRegistrationForm','<c:url context="${ _path }" value="/technicalonboarding/populateTRConfiguration.action" />');return false;"></a>
			<a id="populateTRConfigOnGroupIdAction" href="<c:url context="${ _path }" value="/technicalonboarding/populateTRConfigForConfig.action" />"
			 onclick="anchor_submit_form('technicalRegistrationForm','<c:url context="${ _path }" value="/technicalonboarding/populateTRConfigForConfig.action" />');return false;"></a>
			<input type="hidden" id="primarySeid" name="actionForm.primary" value="${ actionForm.primary	}">
			<input type="hidden" id="popUpHiddenValue" name="actionForm.popUpHiddenValue" value="${ actionForm.popUpHiddenValue	}">
			<input type="hidden" id="secondarySeid" name="actionForm.secondary" value="${fn:escapeXml(actionForm.secondary)}">
			<input type="hidden" id="isMainSeidFlag" name="actionForm.isMainSeidFlag" value="${ actionForm.isMainSeidFlag }">
			<input type="hidden" id="cmMainFlag" name="actionForm.cmMainFlag" value="${ actionForm.cmMainFlag }">
			<input type="hidden" id="readOnly" name="actionForm.readOnly" value="${ actionForm.readOnly }">
			<input type="hidden" id="doesProductSupportAlarm" name="actionForm.alarmEligible" value="${ actionForm.alarmEligible }">
			<input type="hidden" id="screenName" name="actionForm.screenName" value="${fn:escapeXml(actionForm.screenName)}">
			<input type="hidden" id="message" name="actionForm.message" value="${ actionForm.message }">
			<%-- <input type="hidden" id="trSID" name="actionForm.technicalRegistrationSummary.sid" value="${ actionForm.technicalRegistrationSummary.sid }">
			<input type="hidden" id="trMID" name="actionForm.technicalRegistrationSummary.mid" value="${ actionForm.technicalRegistrationSummary.mid }"> --%>
			<input type="hidden" id="sidMidErrorFlag" name="actionForm.sidMidErrorFlag" value="${ actionForm.sidMidErrorFlag }">
			<input type="hidden" id="sidMidRequiredFlag" name="actionForm.sidMidRequiredFlag" value="${ actionForm.sidMidRequiredFlag }">
			<input type="hidden" id="tempSid" name="actionForm.tempSid" value="${ actionForm.tempSid }">
			<input type="hidden" id="tempMid" name="actionForm.tempMid" value="${ actionForm.tempMid }">
			<input type="hidden" id="spProduct" name="actionForm.spReleaseFlag" value="${ actionForm.spReleaseFlag }">
			<input type="hidden" id="cmMainsoldTo" name="actionForm.technicalRegistrationSummary.cmMainsoldTo" value="${ actionForm.technicalRegistrationSummary.cmMainsoldTo }">
			<input type="hidden" id="privateIpEligibleFlag" name="actionForm.privateIpEligibleFlag" value="${ actionForm.privateIpEligibleFlag }">
			<input type="hidden" id="seidOfVoicePortalFlag" name="actionForm.seidOfVoicePortalFlag" value="${ actionForm.seidOfVoicePortalFlag }">
			<input type="hidden" id="seCode" name="actionForm.trConfig.seCode" value="${ fn:escapeXml(actionForm.trConfig.seCode) }">
			<input type="hidden" id="productType" name="actionForm.trConfig.productType" value="${fn:escapeXml(actionForm.trConfig.productType)}">
			<input type="hidden" id="highAvailablity" name="actionForm.highAvailablity" value="${ actionForm.highAvailablity }">
			<input type="hidden" id="cmProduct" name="actionForm.technicalRegistrationSummary.cmProduct" value="${ actionForm.technicalRegistrationSummary.cmProduct }">
			<input type="hidden" id="specialNote" name="actionForm.specialNote" value="${ actionForm.specialNote }"/>
			<input type="hidden" id="userNamePasswordFlag" name="actionForm.userNamePasswordFlag" value="${ actionForm.userNamePasswordFlag }">
			<input type="hidden" id="lssOrESSLowerVersionFlag" name="actionForm.lssOrESSLowerVersionFlag" value="${ actionForm.lssOrESSLowerVersionFlag }"/>
			<input type="hidden" id="upgradedMainCMuseRFASIDFlag" name="actionForm.upgradedMainCMuseRFASIDFlag" value="${ actionForm.upgradedMainCMuseRFASIDFlag }"/>
			<input type="hidden" id="softwareReleaseFlag" name="actionForm.softwareReleaseFlag" value="${ actionForm.softwareReleaseFlag }"/>
			<input type="hidden" id="auxMCShowFlag" name="actionForm.technicalRegistrationSummary.auxMCShowFlag" value="${ actionForm.technicalRegistrationSummary.auxMCShowFlag }"/>
			<input type="hidden" id="auxMCMainSEIDRequiredFlag" name="actionForm.auxMCMainSEIDRequiredFlag" value="${ actionForm.auxMCMainSEIDRequiredFlag }"/>
			<input type="hidden" id="tempAUXMCMainSEID" name="actionForm.temp_auxMCMainSEID" value="${ actionForm.temp_auxMCMainSEID }">
			<input type="hidden" id="auxMCMainSEIDErrorFlag" name="actionForm.auxMCMainSEIDErrorFlag" value="${ actionForm.auxMCMainSEIDErrorFlag }">
			<input type="hidden" id="auxMCSid" name="actionForm.technicalRegistrationSummary.auxMCSid" value="${ actionForm.technicalRegistrationSummary.auxMCSid }"/>
			<input type="hidden" id="auxMCMid" name="actionForm.technicalRegistrationSummary.auxMCMid" value="${ actionForm.technicalRegistrationSummary.auxMCMid }"/>
			<a id="validateCMMainAction" href="<c:url context="${ _path }" value="/technicalonboarding/validateCMMain.action" />"></a>
			<a id="addToListSALGWAction" href="<c:url context="${ _path }" value="/technicalonboarding/addToList.action" />"></a>
			<a id="validateSIDMIDAction" href="<c:url context="${ _path }" value="/technicalonboarding/validateSIDMIDQuery.action" />"></a>
			<a id="cancelAction" href="<c:url context="${ _path }" value="/technicalonboarding/technicalRegistrationDashboard.action" />"></a>
			<a id="addAction" href="<c:url context="${ _path }" value="/technicalonboarding/addTechnicalRegistrationConfig.action" />"></a>
			<a id="validateAUXMCMainSEIDAction" href="<c:url context="${ _path }" value="/technicalonboarding/validateAUXMCMainSEID.action" />"></a>


			<!-- start tob-config-table-wrap -->
			<div id="sectionTwo">
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
										<span class="title">Group:	<a href="${bundle.msg.groupIdKB}" target="_blank">${bundle.msg.whatIsThisTitle}</a>
										<tl:toolTip id="groupIdToolTip" message="${bundle.msg.groupIdToolTip}"></tl:toolTip></span>
										<select name="Registrationwlw-select_key:{actionForm.technicalRegistrationSummary.groupId}"
										onChange="populateTRConfigOnGroupId(this.value)"	${actionForm.tobResubmitFlag ? 'disabled' : ''}>
										<c:forEach items="${ actionForm.groupIds }" var="eat">
										<option ${ ( eat.key eq actionForm.technicalRegistrationSummary.groupId ) ? 'selected' : '' } value="${ eat.key }">${ eat.value }</option>
										</c:forEach>
										</select>

									</p>
								</div>
								<div class="col">
									<p>
										<span class="title">SE Code Preview: </span>
										<span>${actionForm.technicalRegistrationSummary.seCodePreview}</span>
										</p>
										<p>
										<a href="https://support.avaya.com/kb/ext/FAQ104959" target="_blank" class="hyper-link">(What is This?)</a>
										<tl:toolTip id="seCodePreviewToolTip" message="${bundle.msg.SECodePreviewToolTip}"></tl:toolTip>

									</p>
									<p>
										<span class="title">Product:	</span>
										<span>${actionForm.trConfig.productTypeDescription}</span>

									</p>
								</div>
								<div class="col">
									<p>
										<span class="title">Solution Element:	</span>
										<span>${actionForm.trConfig.seCodeDescription}</span>
									</p>
									<p>
										<span class="title">Template: </span>
										<span>${actionForm.trConfig.templateDescription}</span>
									</p>
								</div>
							</div>
						</div>

					</div>
				</div>
			</div>
			<!-- end tob-config-table-wrap -->
			
			<!-- start auxmc-table-wrap -->
			
			<div id="auxMCDiv" style="display:${actionForm.technicalRegistrationSummary.auxMCShowFlag};" >
				<div id="tob-cmain-table-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">Auxiliary Material Code
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div id="" class=" collapse-box-container" style="display: block;">
						<div class="data">
							<div class="row">
								<div class="col">
									<p>
										<span class="title">How do you want to Register this?	</span>
										<input type="radio" name="actionForm.technicalRegistrationSummary.isAuxMCSEIDFlag"
										id="auxMCYes" value="Yes" onclick="javascript:enableAUXMCMainSEIDDiv('yes');"
										${ actionForm.technicalRegistrationSummary.isAuxMCSEIDFlag == 'Yes' ? 'checked' :'' }>
										<span>Register as part of an existing solution</span>
										<input type="radio" name="actionForm.technicalRegistrationSummary.isAuxMCSEIDFlag"
										id="auxMCNo" value="No" onclick="javascript:enableAUXMCMainSEIDDiv('no');"
										${ actionForm.technicalRegistrationSummary.isAuxMCSEIDFlag == 'No' ? 'checked' :'' }>
										<span>Register as a stand-alone solution</span>
									</p>
								</div>
							</div>
						</div>
						<div id="auxMCMainSEIDDiv" style="display:${actionForm.auxMCMainSEIDShowFlag};">
							<div class="data">
								<div class="row">
									<div class="col">
										<p>
											<span class="title">Virtual Communication Manager (VCM) SEID:	</span>
											<input type="text" id="auxMCMainSEID" name="actionForm.technicalRegistrationSummary.auxMCMainSEID" 
											value="${ actionForm.technicalRegistrationSummary.auxMCMainSEID }"/>
											<input type="button" value="Validate" class="button gray small" onClick="return validateAUXMCMAINSEID();"/>
										</p>
									</div>
								</div>
							</div>
							<div id="auxMCMainSEIDErrorDiv" style="display:${actionForm.auxMCMainSEIDErrorFlag};">
								<div class="data">
									<div class="row">
										<div class="col">
											<div class="error-bar">
												<i class="fa fa-exclamation-triangle"></i><span	class="error-bar-msg">${actionForm.auxMCMainSEIDErrorMsg}</span>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						
					</div>
					
					
				</div>
			</div>
			
			<!-- end auxmc-table-wrap -->

			<!-- start tob-cmain-table-wrap -->
			<div id="cmMainDiv" style="display:${actionForm.cmMainFlag};" >
				<div id="tob-cmain-table-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">CM Product
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div id="" class=" collapse-box-container" style="display: block;">

						<div class="data">
							<div class="row">
								<div class="col">
									<p>
										<span class="title">Is it a main CM?	</span>
										<input type="radio" name="actionForm.technicalRegistrationSummary.mainCM"
										id="mainYes" value="Yes" onclick="javascript:enableInfoDiv('yes');"
										${ actionForm.technicalRegistrationSummary.mainCM == 'Yes' ? 'checked' :'' }>
										<span>Yes</span>
										<input type="radio" name="actionForm.technicalRegistrationSummary.mainCM"
										id="mainNo" value="No" onclick="javascript:enableInfoDiv('no');"
										${ actionForm.technicalRegistrationSummary.mainCM == 'No' ? 'checked' :'' }>
										<span>No</span>
									</p>
								</div>


							</div>
						</div>
<div id="cmMainInfoDiv" style="display:${actionForm.isMainSeidFlag};">
					<div class="data">
							<div class="row">
						<div class="col">


									<p>
										<span class="title">Enter Main SEID:	</span>
										<input type="text" id="cmMainSeid" name="actionForm.solutionElementId"
														value="${fn:escapeXml(actionForm.solutionElementId)}" ${actionForm.failedSeidIsNotNull?'disabled':''} />
										<input type="button" value="Validate" class="button gray small" onClick="return validateCMMain();"
											${actionForm.failedSeidIsNotNull?'disabled':''} />
									</p>
								</div>
								</div>
								</div>
								<div id="mainDiv" style="display:${actionForm.cmMainErrorFlag};">
									<div class="data">
										<div class="row">
											<div class="col">
												<div class="error-bar">
													<i class="fa fa-exclamation-triangle"></i><span	class="error-bar-msg">${actionForm.message}</span>
												</div>
											</div>
										</div>
									</div>
								</div>
							<div id="mainDiv1" style="display:${actionForm.mainSeIdFlag};">
								<div class="data">
									<div class="row">
										<div class="col">
											<p>
												<span class="title">Sold To: </span> <span>${actionForm.technicalRegistrationSummary.cmMainsoldTo}</span>
											</p>
										</div>
										<div class="col">
											<p>
												<span class="title">Material Code Description: </span> <span>${actionForm.technicalRegistrationSummary.cmMainMaterialCodeDesc}</span>
											</p>
										</div>
										<div class="col">
											<p>
												<span class="title">SID: </span> <span>${actionForm.technicalRegistrationSummary.sid}</span>
											</p>
										</div>
									</div>
								</div>

								<div class="data">
									<div class="row">
										<div class="col">
											<p>
												<span class="title">Pick the remote device type: </span> <input
													name="actionForm.technicalRegistrationSummary.remoteDeviceType"
													type="radio" value="LSP" id="lsp" checked /><span>LSP</span>
												<input
													name="actionForm.technicalRegistrationSummary.remoteDeviceType"
													type="radio" value="ESS" id="ess" /><span>ESS</span> <input
													name="actionForm.technicalRegistrationSummary.remoteDeviceType"
													type="radio" value="Gateway" id="gateway" /><span>Gateway</span>
											</p>
										</div>
									</div>
								</div>
							</div>
</div>
					</div>




				</div>
			</div>
			<!-- end tob-cmain-table-wrap -->

			<!-- start tob-sal-table-wrap -->
			<div id="salGateWayDivId" style="display:${actionForm.salMigrationFlag};">
				<div id="tob-sal-table-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">SAL Gateway Configuration details
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div id="" class=" collapse-box-container">

						<%@ include file="addToList.jsp" %>

						<div class="basic-table-wrap">
							<table class="basic-table">
								<thead>
									<tr>
										<th width="16%">Sold To/FL of SALGW</th>
										<th width="16%">Material Code</th>
										<th width="16%">SE Code</th>
										<th width="16%">SEID</th>
										<th width="16%">Primary&nbsp;&nbsp;<input type="submit" class="button gray small" value="Reset" onClick="javascript:resetSALGatewaySelected(); return false;"/></th>
										<th width="20%">Secondary&nbsp;&nbsp;<input type="submit"	class="button gray small" value="Reset" onClick="javascript:resetSecondarySALGateway(); return false;"/></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${ actionForm.salGatewayMigrationList }" var="item" varStatus="container">
										<tr>
											<c:set var="currentSeid" value="${item.seid}"/>
											<td width="16%" align="center" >${item.soldTo}</td>
											<td width="16%" align="center" >${item.materialCode}</td>
											<td width="16%" align="center" >${item.seCode}</td>
											<td width="16%" align="center" ><a href="#" onclick="showSALGatewayDetails('${item.seid}', '${item.soldTo}')"	>${item.seid}</a></td>
											<%
												String seId = (String) pageContext.getAttribute("currentSeid");
												String primarySeid =	(String )request.getSession().getAttribute(com.grt.util.GRTConstants.PRIMARY_SE_ID);
												if(seId !=null && seId.equalsIgnoreCase(primarySeid)){
											%>
											<td width="16%" align="center" ><input name="primary" value="${item.seid}" type="radio" onclick="javascript:validateSelection(this, '${container.index}');" checked="true"	/></td>
											<%} else { %>
											<td width="16%" align="center" ><input name="primary" value="${item.seid}" type="radio" onclick="javascript:validateSelection(this, '${container.index}');"	/></td>
											<% }
												String secondarySeid =	(String )request.getSession().getAttribute(com.grt.util.GRTConstants.SECONDARY_SE_ID);
												if(seId !=null && seId.equalsIgnoreCase(secondarySeid)){
											%>
											<td width="19%" align="center" ><input name="secondary" value="${item.seid}" type="radio" onclick="javascript:validateSelection(this, '${container.index}');" checked="true"	/></td>
											<% } else { %>
											<td width="19%" align="center" ><input name="secondary" value="${item.seid}" type="radio" onclick="javascript:validateSelection(this, '${container.index}');" /></td>
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
			</div>
			<!-- end tob-sal-table-wrap -->

			<!-- start tob-product-table-wrap -->
			<div id="productRegistrationDivId" style="display:${actionForm.productConfigurationFlag};">
				<div id="tob-product-table-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">Product configuration details
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div id="" class="collapse-box-container">

						<div class="data">
							<div class="row">
								<div class="col">
									<p>
										<span class="title">Access Type:	</span>
										<select id="accessType" name="actionForm.technicalRegistrationSummary.accessType"
										onChange="populateTRConfig(this.value);">
											<c:forEach items="${ actionForm.eligibleAccessTypesList }" var="accType">
											<option ${ ( accType == actionForm.technicalRegistrationSummary.accessType ) ? 'selected' : '' } value="${accType }">${accType }</option>
											</c:forEach>
										</select>
									</p>
								</div>
								<div class="col" id="softwareReleaseDiv" ${actionForm.softwareReleaseFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">Software Release: </span>
										<select name="actionForm.technicalRegistrationSummary.softwareRelease" id="softwareRelease_Select">
											<c:forEach items="${ actionForm.softwareRelease }" var="rel">
												<option ${ ( rel == actionForm.technicalRegistrationSummary.softwareRelease ) ? 'selected' : '' } value="${rel }">${rel }</option>
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
								<div class="col" id="Alarm_Origination" ${actionForm.alarmOriginationFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">Alarm Origination: </span>
										<select name="actionForm.technicalRegistrationSummary.alarmOrigination" id="alarmOrigination">
											<c:forEach items="${ actionForm.alarmOriginations }" var="alarm">
												<option ${ ( alarm == actionForm.technicalRegistrationSummary.alarmOrigination ) ? 'selected' : '' } value="${alarm }">${alarm }</option>
											</c:forEach>
										</select>
									</p>
								</div>
							</div>
						</div>

						<div class="data">
							<div class="row">
								<div class="col" id="spReleaseDiv" ${actionForm.spReleaseFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">System Platform Release:	</span>
										<select name="actionForm.technicalRegistrationSummary.systemProductRelease">
											<c:forEach items="${ actionForm.spRelease }" var="spRel">
												<option ${ ( spRel == actionForm.technicalRegistrationSummary.systemProductRelease ) ? 'selected' : '' } value="${spRel }">${spRel }</option>
											</c:forEach>
										</select>
									</p>
								</div>
								<div class="col" id="ipAddressDiv" ${actionForm.ipAddressFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">IP Address: </span>
										<input type="text" id="ipAddress" name="actionForm.technicalRegistrationSummary.ipAddress" value="${ actionForm.technicalRegistrationSummary.ipAddress }"/>
									</p>
								</div>
							</div>
						</div>

						<div class="data">
							<div class="row">
								<div class="col" id="outboundPrefixDiv" ${actionForm.outboundPrefixFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">Outbound prefix:	</span>
										<tl:toolTip id="outboundPrefixTooltip" message="${bundle.msg.outboundPrefixTooltip}"></tl:toolTip>
										<input type="text" id="outboundPrefix" name="actionForm.technicalRegistrationSummary.outboundPrefix" value="${ actionForm.technicalRegistrationSummary.outboundPrefix }"/>
									</p>
								</div>
								<div class="col" id="dialInNumberDiv" ${actionForm.dialInFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">Dial-in Number: </span>
										<tl:toolTip id="dialInNumberToolTip" message="${bundle.msg.dialInNumberToolTip}"></tl:toolTip>
										<input type="text" id="dialIn" name="actionForm.technicalRegistrationSummary.dialInNumber" value="${ actionForm.technicalRegistrationSummary.dialInNumber }"/>
									</p>
								</div>
							</div>
						</div>

						<div class="data">
							<div class="row" id="sidMidDiv" ${actionForm.sidMidShowFlag == 'none' ? 'style="display:none"' : ''}>
								<div class="col">
									<c:set var="highAvailablity" value="${actionForm.highAvailablity}"/>
									<p>
										<span class="title">
											<%
												Boolean highAvailablity= (Boolean) pageContext.getAttribute("highAvailablity");
												if (highAvailablity) {
											%>
												Please enter SID of main:
											<% } else { %>
												SID:
											<% } %>
										</span>
										<tl:toolTip id="SIDToolTip" message="${bundle.msg.SIDToolTip}"></tl:toolTip>
										<input type="text" id="sid" name="actionForm.technicalRegistrationSummary.sid" value="${ actionForm.technicalRegistrationSummary.sid }" maxlength="11" onBlur="javascript:validateSID(this);" onKeyUp="javascript:alphaNumericFilter(this);" ${actionForm.tobResubmitFlag ? 'disabled' : ''}/>
									</p>
								</div>
								<div class="col">
									<p>
										<span class="title">MID:	</span>
										<tl:toolTip id="MIDToolTip" message="${bundle.msg.MIDToolTip}"></tl:toolTip>
										<input type="text" value="${ actionForm.technicalRegistrationSummary.mid }" name="actionForm.technicalRegistrationSummary.mid" id="mid" maxlength="5" onkeyup="javascript:numericFilter(this);" onblur="javascript:validateMID(this);"	${actionForm.tobResubmitFlag ? 'disabled' : ''}/>
										<input type="button" ${actionForm.failedSeidIsNotNull ? 'disabled':''} class="button gray small" value="Validate SID/MID" onclick="return validateSidandMid();"/>
									</p>
								</div>
							</div>
						</div>

						<div class="data">
							<div class="row" id="randomPasswordDiv" ${actionForm.randomPasswordFlag == 'none' ? 'style="display:none"' : ''}>
								<div class="col" >
									<p>
										<span class="title">Do you want random password generated?	</span>
										<input type="radio" value="Yes" ${ actionForm.technicalRegistrationSummary.randomPassword == 'Yes' ? 'checked' :'' }
										name="actionForm.technicalRegistrationSummary.randomPassword"/><span>Yes</span>
										<input type="radio" value="No"	${ actionForm.technicalRegistrationSummary.randomPassword == 'No' ? 'checked' :'' }
										name="actionForm.technicalRegistrationSummary.randomPassword"/><span>No</span>
										<input type="radio" value="Default"	${ (actionForm.technicalRegistrationSummary.randomPassword != null || actionForm.technicalRegistrationSummary.randomPassword != 'Yes' && actionForm.technicalRegistrationSummary.randomPassword != 'No') ? 'checked' :'' }
										name="actionForm.technicalRegistrationSummary.randomPassword"/>
										<span>Default</span>
									</p>
								</div>
							</div>
						</div>

						<div class="data">
							<div class="row">
								<div class="col" id="privateIpDiv" ${actionForm.privateIpEligibleFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">Private IP address:	</span>
										<input type="text" id="privateIP" name="actionForm.technicalRegistrationSummary.privateIP" value="${ actionForm.technicalRegistrationSummary.privateIP }"/>
									</p>
								</div>
							</div>
						</div>

						<div class="data">
							<div class="row">
								<div class="col" id="seidOfVoicePortalDiv" ${actionForm.seidOfVoicePortalFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">SEID of Voice/Experience Portal Management Server:	</span>
										<tl:toolTip id="voiceofPortalSEIDToolTip" message="${bundle.msg.voiceofPortalSEIDToolTip}"></tl:toolTip>
										<input type="text" id="seidOfVoicePortal" name="actionForm.technicalRegistrationSummary.seidOfVoicePortal" value="${ actionForm.technicalRegistrationSummary.seidOfVoicePortal }"/>
									</p>
								</div>
							</div>
						</div>

						<div class="data">
							<div class="row">
								<div class="col" id="sampBoardDiv" ${actionForm.sampBoardUpgradedFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">Is SAMP board upgraded to 2.2.3?	</span>
										<input type="radio" value="Yes" name="actionForm.technicalRegistrationSummary.sampBoardUpgraded" ${ actionForm.technicalRegistrationSummary.sampBoardUpgraded == "Yes" ? 'checked' :'' }/><span>Yes</span>
										<input type="radio" value="No"	name="actionForm.technicalRegistrationSummary.sampBoardUpgraded" ${ actionForm.technicalRegistrationSummary.sampBoardUpgraded == "No" || actionForm.technicalRegistrationSummary.sampBoardUpgraded == null ? 'checked' :'' }/><span>No</span>
									</p>
								</div>
							</div>
						</div>

						<div class="data">
							<div class="row">
								<div class="col" id="checkIfSESEdgeDiv" ${actionForm.checkIfSESEdgeFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">Check if SES Edge will Use Core Router Feature:	</span>
										<input type="radio" value="Yes" name="actionForm.technicalRegistrationSummary.checkIfSESEdge" ${ actionForm.technicalRegistrationSummary.checkIfSESEdge == "Yes" ? 'checked' :'' }/><span>Yes</span>
										<input type="radio" value="No" name="actionForm.technicalRegistrationSummary.checkIfSESEdge" ${ actionForm.technicalRegistrationSummary.checkIfSESEdge == "No" ? 'checked' :'' }/><span>No</span>
									</p>
								</div>
							</div>
						</div>

						<div class="data">
							<div class="row">
								<div class="col" id="authFileDiv" ${actionForm.authFileIDFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">Authentication File ID (Optional):	</span>
										<tl:toolTip id="AuthIdToolTip" message="${bundle.msg.authToolTip}"></tl:toolTip>
										<input type="text" id="authFileId" name="actionForm.technicalRegistrationSummary.authFileID" value="${ actionForm.technicalRegistrationSummary.authFileID }" maxlength="10" onBlur="javascript:validateAFID(this);" onKeyUp="javascript:numericFilter(this);"/>
									</p>
								</div>
							</div>
						</div>

						<div class="data">
							<div class="row">
								<div class="col" id="hardwareServerDiv" ${actionForm.hardwareServerFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">Gateway:	</span>
										<select id="hardwareServer" style="width:60px">
											<c:forEach items="${ actionForm.hardwareServer }" var="hs">
											<option ${ ( hs == actionForm.technicalRegistrationSummary.hardwareServer ) ? 'selected' : '' } value="${ hs }">${hs }</option>
											</c:forEach>
										</select>
									</p>
								</div>
							</div>
						</div>

						<div class="data">
							<div class="row">
								<div class="col" id="modifyUPdiv" ${actionForm.userNamePasswordFlag == 'none' ? 'style="display:none"' : ''} >
									<p>
										<span class="title">Modify Username/Password:	</span>
										<input type="checkbox" id="modUPcheckBx" onclick="showUserNamePassword(this.value);" />
									</p>
								</div>
							</div>
							<div class="row" id="userNamePassword">
								<div class="col" id="usernameDiv" ${actionForm.userNamePasswordFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">User Name:	</span>
										<input type="text" name="actionForm.technicalRegistrationSummary.userName" id="username" value="${ actionForm.technicalRegistrationSummary.userName }"/>
									</p>
								</div>
								<div class="col" id="passwordDiv" ${actionForm.userNamePasswordFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">Password:	</span>
										<input type="password" name="actionForm.technicalRegistrationSummary.password" id="password" value="${ actionForm.technicalRegistrationSummary.password }">
									</p>
								</div>
								<div class="col" id="reTypePassDiv" ${actionForm.userNamePasswordFlag == 'none' ? 'style="display:none"' : ''}>
									<p>
										<span class="title">Retype Password:	</span>
										<input type="password" id="retypePassword"/>
									</p>
								</div>
							</div>
						</div>

					</div>

					<div class="error-bar" id="sidMidDiv" ${actionForm.sidMidErrorFlag == 'none' ? 'style="display:none"' : ''}>
						<i class="fa fa-exclamation-triangle"></i><span class="error-bar-msg">${actionForm.sidMidStatus}</span>
					</div>
					<div class="error-bar" id="sidMidDiv1" ${actionForm.sidMidValidatedFlag == 'none' ? 'style="display:none"' : ''}>
						<i class="fa fa-exclamation-triangle"></i><span class="error-bar-msg">${actionForm.sidMidValidated}</span>
					</div>
					<div class="error-bar" id="sidMidDiv2" ${actionForm.duplicateSidMidFlag == 'none' ? 'style="display:none"' : ''}>
						<i class="fa fa-exclamation-triangle"></i><span class="error-bar-msg">This SID/MID combination already exists for a record in Ready to Process section of On-boarding Dashboard.</span>
					</div>

				</div>
			</div>
			<!-- end tob-product-table-wrap -->

			<!-- start No Connectivity Message -->
			<div id="noConnectivityMessage"  class="page-note">
				<span>${grtConfig.noConnectivityMessage}</span>
			</div>
			<!-- end No Connectivity Message -->

			<!-- start controls -->
			<div class="controls">
				<input type="button" value="Cancel" class="button gray" onClick="return submitForm('cancelAction');"/>&nbsp;&nbsp;
				<%
					String isFromTrObDetail = (String) request.getSession().getAttribute(com.grt.util.GRTConstants.FROM_TR_OB_DETAIL);
					if(com.grt.util.GRTConstants.TRUE.equalsIgnoreCase(isFromTrObDetail)){
				%>
				<input type="button" value="Submit" class="button gray" onClick="return processBeforeSubmit('Submit');"/>
				<% } else { %>
				 <input type="button" value="Add" style="visibility: visible;" id="addButton" class="button gray" onClick="return addAction('Add');" ${actionForm.readOnlyForBlankGrpId ? 'disabled':''}/>
				<%} %>
			</div>
			<!-- end controls -->

			<!-- start errorMessagePopUp -->
			<div id="errorMessagePopUp" class="modal succFailMsg" style="display:none;">
				<div class="modal-overlay">
					<div class="modal-content">
						<h2 class="title">Attention</h2>

						<p id="messageContent" class="content">
							<img class="msg-icon"
								src="<c:url context="${ _path }" value="/styles/images/messages/icon_warning_light_60x60.png" />"
								alt="info" />
							<span class="msg-txt">${bundle.msg.upgradedMainCMuseRFASIDNote}</span>
						</p>
						<div class="controls">
							<input type="button" class="button gray" onclick ="closePopUp();" value="OK"/>
						</div>
					</div>
				</div>
			</div>
			<!-- end errorMessagePopUp -->

			<!-- start lssOrESSLowerVersionNotePopUp -->
			<div id="lssOrESSLowerVersionNotePopUp" class="modal succFailMsg" style="display:none;">
				<div class="modal-overlay">
					<div class="modal-content">
						<h2 class="title">Attention</h2>

						<p id="messageContent" class="content">
							<img class="msg-icon"
								src="<c:url context="${ _path }" value="/styles/images/messages/icon_warning_light_60x60.png" />"
								alt="info" />
							<span class="msg-txt">${bundle.msg.lssOrESSLowerVersionNote}</span>
						</p>
						<div class="controls">
							<input type="button" class="button gray" onclick ="lssOrESSLowerVersionNoteClosePopUp();" value="OK"/>
						</div>
					</div>



				</div>
			</div>
			<!-- end lssOrESSLowerVersionNotePopUp -->

			<script>
				function onConnectivityChange(con) {
					var connectivity = con.value;
					var doesProductSupportAlarm = document.getElementById('doesProductSupportAlarm').value;
					if(doesProductSupportAlarm=="false") {
						return;
					}
					if(connectivity=="Yes") {
						show('Alarm_Origination');
					} else {
							hide('Alarm_Origination');
					}
				}
			</script>

			<script>
				$(document).ready(function(){
				var cmFlag = document.getElementById('cmMainFlag').value;
					if(getRadioValue('mainYes') || getRadioValue('mainNo') ) {
						if(cmFlag != null && cmFlag == 'block'){
							 show('salGateWayDivId');
							 show('productRegistrationDivId');
							 if( document.getElementById('addButton')!=undefined )
							 	document.getElementById('addButton').style.visibility="visible";
						 }
					} else if(cmFlag != null && cmFlag == 'block'){
						hide('salGateWayDivId');
						hide('productRegistrationDivId');
					 	document.getElementById('addButton').style.visibility='hidden';
				}
				var lssOrESSLowerVersionFlag = document.getElementById('lssOrESSLowerVersionFlag').value;
				var upgradedMainCMuseRFASIDFlag = document.getElementById('upgradedMainCMuseRFASIDFlag').value;


				if(lssOrESSLowerVersionFlag.length > 0){
					var lssOrESSLowerVersionNoteMsg = document.getElementById('lssOrESSLowerVersionNoteMsg').value;
					var lssOrESSLowerVersionNoteCode = document.getElementById('lssOrESSLowerVersionNoteCode').value;
					convertAlertToModelPopUp(lssOrESSLowerVersionNoteCode, lssOrESSLowerVersionNoteMsg);
				} else if(upgradedMainCMuseRFASIDFlag.length > 0){
					var msg = document.getElementById('upgradedMainCMuseRFASIDNoteMsg').value;
					var code = document.getElementById('upgradedMainCMuseRFASIDNoteCode').value;
					convertAlertToModelPopUp(code, msg);
				}
				});
			</script>
		</form>
		<!-- end technicalRegistrationForm -->


	<!-- start error messages -->
		<input type="hidden" id="alertEnterSId" value="${grtConfig.alertEnterSId}" />
		<input type="hidden" id="alertEnterMId" value="${grtConfig.alertEnterMId}" />
		<input type="hidden" id="alertEnterMidAsOneForHA" value="${grtConfig.alertEnterMidAsOneForHA}" />
		<input type="hidden" id="alertEnterMidAsOneForRadvision" value="${grtConfig.alertEnterMidAsOneForRadvision}" />
		<input type="hidden" id="aletEnterMidForRemSid" value="${grtConfig.aletEnterMidForRemSid}" />
		<input type="hidden" id="aletEnterMidForMain" value="${grtConfig.aletEnterMidForMain}" />
		<input type="hidden" id="alertClrCmErr" value="${grtConfig.alertClrCmErr}" />
		<input type="hidden" id="alertEnterValMainSeid" value="${grtConfig.alertEnterValMainSeid}" />
		<input type="hidden" id="alertEnterSidMid" value="${grtConfig.alertEnterSidMid}" />
		<input type="hidden" id="alertValSidMid" value="${grtConfig.alertValSidMid}" />
		<input type="hidden" id="alertClrError" value="${grtConfig.alertClrError}" />
		<input type="hidden" id="alertSeIdNotEmpForVP" value="${grtConfig.alertSeIdNotEmpForVP}" />
		<input type="hidden" id="alertPvtIPNotEmpty" value="${grtConfig.alertPvtIPNotEmpty}" />
		<input type="hidden" id="alertEntCorrPvtIP" value="${grtConfig.alertEntCorrPvtIP}" />
		<input type="hidden" id="alertIpNotEmpForAccTypIP" value="${grtConfig.alertIpNotEmpForAccTypIP}" />
		<input type="hidden" id="alertInvIpAddr" value="${grtConfig.alertInvIpAddr}" />
		<input type="hidden" id="alertEnterDialInForModem" value="${grtConfig.alertEnterDialInForModem}" />
		<input type="hidden" id="alertInvalidDialIn" value="${grtConfig.alertInvalidDialIn}" />
		<input type="hidden" id="alertEnterSEIdForCaas" value="${grtConfig.alertEnterSEIdForCaas}" />
		<input type="hidden" id="alertEnterSEIdForNonSysPrd" value="${grtConfig.alertEnterSEIdForNonSysPrd}" />
		<input type="hidden" id="alertEnterAccType" value="${grtConfig.alertEnterAccType}" />
		<input type="hidden" id="alertEntUsrNamPwd" value="${grtConfig.alertEntUsrNamPwd}" />
		<input type="hidden" id="alertSameGtwayNtAllForPriAndSec" value="${grtConfig.alertSameGtwayNtAllForPriAndSec}" />
		<input type="hidden" id="alertSelPriThanSecGtway" value="${grtConfig.alertSelPriThanSecGtway}" />
		<input type="hidden" id="alertInvalidSoldTo" value="${grtConfig.alertInvalidSoldTo}" />
		<input type="hidden" id="alertInvalidSeIdFormat" value="${grtConfig.alertInvalidSeIdFormat}" />
		<input type="hidden" id="alertEnterValidSoldToOrSeId" value="${grtConfig.alertEnterValidSoldToOrSeId}" />
		<input type="hidden" id="alertInvalidSeIdAsInput" value="${grtConfig.alertInvalidSeIdAsInput}" />
		<input type="hidden" id="alertInvalidSIdFormat" value="${grtConfig.alertInvalidSIdFormat}" />
		<input type="hidden" id="alertAFIDMaxTenDig" value="${grtConfig.alertAFIDMaxTenDig}" />
		<input type="hidden" id="alertMidNotZero" value="${grtConfig.alertMidNotZero}" />
		<input type="hidden" id="alertInvalidMId" value="${grtConfig.alertInvalidMId}" />
		<input type="hidden" id="cnfrmResetGateway" value="${grtConfig.cnfrmResetGateway}" />

		<input type="hidden" id="lssOrESSLowerVersionNoteMsg" value="${ fn:escapeXml(grtConfig.lssOrESSLowerVersionNote) }" />
		<input type="hidden" id="upgradedMainCMuseRFASIDNoteMsg" value="${ fn:escapeXml(grtConfig.upgradedMainCMuseRFASIDNote)}" />
		<input type="hidden" id="alertSoftwareReleaseErrorMsg" value="${grtConfig.alertSoftwareReleaseErrorMsg}" />
		<input type="hidden" id="alertValAUXMCMainSEID" value="${grtConfig.alertValAUXMCMainSEID}" />
		<input type="hidden" id="alertEnterAUXMCMainSEID" value="${grtConfig.alertEnterAUXMCMainSEID}" />
		<input type="hidden" id="alertSelectAUXMCType" value="${grtConfig.alertSelectAUXMCType}" />
		<!-- end error messages -->




		<!-- Error Codes -->
		<input type="hidden" id="alertEnterSIdErrorCode" value="${grtConfig.ewiMessageCodeMap['alertEnterSId']}"/>
		<input type="hidden" id="alertEnterMIdErrorCode" value="${grtConfig.ewiMessageCodeMap['alertEnterMId']}"/>
		<input type="hidden" id="alertEnterMidAsOneForHAErrorCode" value="${grtConfig.ewiMessageCodeMap['alertEnterMidAsOneForHA']}"/>
		<input type="hidden" id="alertEnterMidAsOneForRadvisionErrorCode" value="${grtConfig.ewiMessageCodeMap['alertEnterMidAsOneForRadvision']}"/>
		<input type="hidden" id="aletEnterMidForRemSidErrorCode" value="${grtConfig.ewiMessageCodeMap['aletEnterMidForRemSid']}"/>
		<input type="hidden" id="aletEnterMidForMainErrorCode" value="${grtConfig.ewiMessageCodeMap['aletEnterMidForMain']}"/>
		<input type="hidden" id="alertClrCmErrErrorCode" value="${grtConfig.ewiMessageCodeMap['alertClrCmErr']}"/>
		<input type="hidden" id="alertEnterValMainSeidErrorCode" value="${grtConfig.ewiMessageCodeMap['alertEnterValMainSeid']}"/>
		<input type="hidden" id="alertEnterSidMidErrorCode" value="${grtConfig.ewiMessageCodeMap['alertEnterSidMid']}"/>
		<input type="hidden" id="alertValSidMidErrorCode" value="${grtConfig.ewiMessageCodeMap['alertValSidMid']}"/>
		<input type="hidden" id="alertClrErrorErrorCode" value="${grtConfig.ewiMessageCodeMap['alertClrError']}"/>
		<input type="hidden" id="alertSeIdNotEmpForVPErrorCode" value="${grtConfig.ewiMessageCodeMap['alertSeIdNotEmpForVP']}"/>
		<input type="hidden" id="alertPvtIPNotEmptyErrorCode" value="${grtConfig.ewiMessageCodeMap['alertPvtIPNotEmpty']}"/>
		<input type="hidden" id="alertEntCorrPvtIPErrorCode" value="${grtConfig.ewiMessageCodeMap['alertEntCorrPvtIP']}"/>
		<input type="hidden" id="alertIpNotEmpForAccTypIPErrorCode" value="${grtConfig.ewiMessageCodeMap['alertIpNotEmpForAccTypIP']}"/>
		<input type="hidden" id="alertInvIpAddrErrorCode" value="${grtConfig.ewiMessageCodeMap['alertInvIpAddr']}"/>
		<input type="hidden" id="alertEnterDialInForModemErrorCode" value="${grtConfig.ewiMessageCodeMap['alertEnterDialInForModem']}"/>
		<input type="hidden" id="alertInvalidDialInErrorCode" value="${grtConfig.ewiMessageCodeMap['alertInvalidDialIn']}"/>
		<input type="hidden" id="alertEnterSEIdForCaasErrorCode" value="${grtConfig.ewiMessageCodeMap['alertEnterSEIdForCaas']}"/>
		<input type="hidden" id="alertEnterSEIdForNonSysPrdErrorCode" value="${grtConfig.ewiMessageCodeMap['alertEnterSEIdForNonSysPrd']}"/>
		<input type="hidden" id="alertEnterAccTypeErrorCode" value="${grtConfig.ewiMessageCodeMap['alertEnterAccType']}"/>
		<input type="hidden" id="alertEntUsrNamPwdErrorCode" value="${grtConfig.ewiMessageCodeMap['alertEntUsrNamPwd']}"/>
		<input type="hidden" id="alertSameGtwayNtAllForPriAndSecErrorCode" value="${grtConfig.ewiMessageCodeMap['alertSameGtwayNtAllForPriAndSec']}"/>
		<input type="hidden" id="alertSelPriThanSecGtwayErrorCode" value="${grtConfig.ewiMessageCodeMap['alertSelPriThanSecGtway']}"/>
		<input type="hidden" id="alertInvalidSoldToErrorCode" value="${grtConfig.ewiMessageCodeMap['alertInvalidSoldTo']}"/>
		<input type="hidden" id="alertInvalidSeIdFormatErrorCode" value="${grtConfig.ewiMessageCodeMap['alertInvalidSeIdFormat']}"/>
		<input type="hidden" id="alertEnterValidSoldToOrSeIdErrorCode" value="${grtConfig.ewiMessageCodeMap['alertEnterValidSoldToOrSeId']}"/>
		<input type="hidden" id="alertInvalidSeIdAsInputErrorCode" value="${grtConfig.ewiMessageCodeMap['alertInvalidSeIdAsInput']}"/>
		<input type="hidden" id="alertInvalidSIdFormatErrorCode" value="${grtConfig.ewiMessageCodeMap['alertInvalidSIdFormat']}"/>
		<input type="hidden" id="alertAFIDMaxTenDigErrorCode" value="${grtConfig.ewiMessageCodeMap['alertAFIDMaxTenDig']}"/>
		<input type="hidden" id="alertMidNotZeroErrorCode" value="${grtConfig.ewiMessageCodeMap['alertMidNotZero']}"/>
		<input type="hidden" id="alertInvalidMIdErrorCode" value="${grtConfig.ewiMessageCodeMap['alertInvalidMId']}"/>
		<input type="hidden" id="cnfrmResetGatewayConfCode" value="${grtConfig.ewiMessageCodeMap['cnfrmResetGateway']}"/>
		<input type="hidden" id="lssOrESSLowerVersionNoteCode" value="${grtConfig.ewiMessageCodeMap['lssOrESSLowerVersionNote']}"/>
		<input type="hidden" id="upgradedMainCMuseRFASIDNoteCode" value="${grtConfig.ewiMessageCodeMap['upgradedMainCMuseRFASIDNote']}"/>
		<input type="hidden" id="alertSoftwareReleaseErrorCode" value="${grtConfig.ewiMessageCodeMap['alertSoftwareReleaseErrorMsg']}"/>
		<input type="hidden" id="alertValAUXMCMainSEIDErrorCode" value="${grtConfig.ewiMessageCodeMap['alertValAUXMCMainSEID']}"/>
		<input type="hidden" id="alertEnterAUXMCMainSEIDErrorCode" value="${grtConfig.ewiMessageCodeMap['alertEnterAUXMCMainSEID']}"/>
		<input type="hidden" id="alertSelectAUXMCTypeErrorCode" value="${grtConfig.ewiMessageCodeMap['alertSelectAUXMCType']}"/>


	</div>
	<!-- end page content -->
</div>
<!-- end page content-wrap -->

