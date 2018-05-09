<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="com.grt.util.GRTConstants"%>
<%@ page import="com.grt.util.GRTConstants"%>

<%@page import="java.util.Map"%>
<%@page import="com.avaya.grt.mappers.Region"%>
<%@page import="com.avaya.grt.web.util.PageCache"%>
<%--@page import="com.grt.util.PageCache"--%>
<%@ include file="/includes/context.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/views/token-redemption.css" />"/>
<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/plugins/jquery.dataTables_themeroller.css" />" />

<script type="text/javascript" src='<%=request.getContextPath()+"/scripts/validatestring.js"%>'></script>
<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/account/grt.tokenredemption.js" />"></script>

<script type="text/javascript">

	function trim (myString)
	{
			return myString.replace(/^\s+|\s+$/g, "");
	}

	function show(id) {
	  	     document.getElementById(id).style.display = "";
	}
	function hide(id) {
	  	     document.getElementById(id).style.display = "none";
	}
	function mask() {
		$(".loading-overlay").show();
	}
	function unMask() {
		$(".loading-overlay").hide();
	}

	function submitForm(formName){
		mask();
		var url=document.getElementById(formName).href;
		document.getElementById('tokenRedemptionForm').action=url;
		document.getElementById('tokenRedemptionForm').submit();
	}

function enable(id) {
         document.getElementById(id).disabled=  false;
	}
	function disable(id) {
         document.getElementById(id).disabled=  true;
	}
	function showAdditionalInfo() {
	    show("additionalinfo");
 		show("chooseRegion");	   
	    show("selectContractToCancel");
	    var superUser = document.getElementById('superUserId').value;
	     if(superUser == "false") {
	     	disable("redeemTokenId");
	        mask();
	        var shipTo = document.getElementById('soldToStorer').value;
  	        populateContractData(shipTo);
  	     } else {
  	     	enable("redeemTokenId");
  	     	document.getElementById('selectContractToCancel').style.display = 'none';
  	     }
	}
	var contractList;
	var haveValidContractToReplace = false;
	
	function selectContract(contractNum, contractStartDate,contractEndDate) {
		// show error when contract can not be cancelled.
		var radioButtonId = "contractRadio_"+contractNum;
		var allowSelection = document.getElementById(radioButtonId).getAttribute('allowSelection');
		if(allowSelection=="false") {
			var errorMsgKey = $('#tokenContractNotReplacableErrMsgKey').val();
			var errorMsg = $('#tokenContractNotReplacableErrMsg').val();
			convertAlertToModelPopUp(errorMsgKey, errorMsg);
			document.getElementById(radioButtonId).checked = false;
			disable('redeemTokenId');
	    } else {
	    	document.getElementById('cancelledContractNumber').value = contractNum;
	    	document.getElementById('cancelledContractStartDate').value = contractStartDate;
	    	document.getElementById('cancelledContractEndDate').value = contractEndDate;
	       	enable('redeemTokenId');
	    }
	}
	
	function populateContractData(shipTo) {
	
		var url = _path+"/token/json/findContracts.action?actionForm.tokenFormBean.shipTo="+shipTo;
		$.ajax({
			url: url,
			type: 'POST',
			dataType: 'json',
			headers: {
				'Content-type': 'application/x-www-form-urlencoded'
			},
			success: function (data, textStatus, xhr) {
				populateResults(data);
			},
			error: function (xhr, textStatus, errorThrown) {
				unMask();
				console.log(errorThrown);
			}
		});
	}
	
	function populateResults(data) {
		contractList = data;
		if(contractList!=null && contractList.length > 0)  {
			//do nothing
		} else {
			disableRedeemToken();
			return false;
		}
		document.getElementById('contractListTable').innerHTML = '';
		var html = '<tbody><thead><tr><th align="left" width="20%">Select for Cancellation</th><th align="left" width="30%">Contract Number</th>'+
					'<th align="left" width="25%">Start Date</th><th align="left" width="25%">End Date</th></tr></thead>';
		if(data != null){
			for(var i = 0; i < data.length; i++) {
				if(data[i] != null){
					if(data[i].returnCode == "4" || data[i].returnCode == "3") {
						disableRedeemToken();
						return false;
					} else if(data[i].returnCode == "1"){
						unMask();
						var errorMsg = $('#tokenExcpFindingContractsErrMsg').val();
						document.getElementById('contractsDiv').innerText = errorMsg;
						disable('redeemTokenId');
						return false;
					}
					if(data[i].allowReplace==true) {
						haveValidContractToReplace=true;
					}
					var contractNum = data[i].contractNumber;
					var contractNumStr = "'"+contractNum+"','"+data[i].contractStartDateStr+"','"+data[i].contractEndDateStr+"'";
					var onClick = ' onclick="selectContract('+contractNumStr+');" ';
					
					html += '<tr><td width="20%">'
							+'<input type="radio" id="contractRadio_'+contractNum+'" allowSelection="'+data[i].allowReplace+'" '+onClick+' value="'+contractNum+'" /></td>'
							+'<td width="25%">'+contractNum+'</td><td width="15%">'
							+ data[i].contractStartDateStr + '</td><td width="15%">'
							+ data[i].contractEndDateStr + '</td></tr>';
					
				} else {
					disableRedeemToken();
					return false;
				}
			}
		} else {
			disableRedeemToken();
			return false;
		}
		html += '</tbody>';
		document.getElementById('contractListTable').innerHTML = html;
		unMask();
	}
	
	function disableRedeemToken(){
		unMask();
		var errorMsg = $('#tokenNoContractsFoundErrMsg').val();
		document.getElementById('contractsDiv').innerText = errorMsg;
		disable('redeemTokenId');
	}
	
	function hideAdditionalInfo() {
	
		var warnMsgKey = document.getElementById("tokenContractReplaceNoConfirmMsgKey").value;
		var warnMsg = document.getElementById("tokenContractReplaceNoConfirmMsg").value;
		var succesCallBack = "hideAdditionalInfoYes()";
		var failedCallBack = "hideAdditionalInfoNo()";
		convertConfirmToModelPopUp(warnMsgKey, warnMsg, succesCallBack, failedCallBack);
	}
	
	function hideAdditionalInfoYes() {	
		enable("redeemTokenId");
		hide("additionalinfo");
		hide("chooseRegion");
	    hide("selectContractToCancel");
	    document.getElementById('cancelledContractNumber').value = '';
	    document.getElementById('cancelledContractStartDate').value = '';
    	document.getElementById('cancelledContractEndDate').value = '';
		if (document.getElementById("additionalInfobox").value != null) {
			document.getElementById("additionalInfobox").value = '';
		}
	}
	
	function hideAdditionalInfoNo() {
		document.getElementById("addInfoYes").checked = true;
	}

	function redeemToken() {
		var emailIds = document.getElementById('additionalEmailIds').value;
		var region = document.getElementById('selectedRegion').value;
		if(region == '' && document.getElementById("addInfoYes").checked){
			alert('Please choose an appropriate region to send the contract cancellation information to the Avaya operations team.');
			return false;
		}
		if(emailIds != '' && !splitMails(emailIds)){
			return false;
		}
		var obj1 = document.getElementById("additionalInfobox");
		var radios = document.getElementsByName("actionForm.tokenFormBean.replaceContract");
		var additionalInfo = false;
		for (var i = 0; i < radios.length; i++) {
			if (radios[i].checked) {
				var value = radios[i].value;
				if (value == "Yes") {
 					additionalInfo = true;
				} else if (value == "No") {
					additionalInfo = false;
				}
				break;
			}
		}
		if ( additionalInfo && (obj1 == null || obj1.value.length > 400)) {
			var errorMsgKey = $('#tokenAdditionInfoLengthErrorKey').val();
			var errorMsg = $('#tokenAdditionInfoLengthError').val();
			convertAlertToModelPopUp(errorMsgKey, errorMsg);
			return false;
		}
		else
		{
			mask();
			var url=document.getElementById('redeemToken').href;
			document.getElementById('tokenRedemptionForm').action=url;
			document.getElementById('tokenRedemptionForm').submit();
		}
	}
	
	function cancelProcess() {
		var warnMsgKey = $('#tokenCancelWarningMsgKey').val();
		var warnMsg = $('#tokenCancelWarningMsg').val();
		
		var succesCallBack = "submitForm('cancelTokenRedemption')";
		var failedCallBack = "return false;";
		convertConfirmToModelPopUp(warnMsgKey, warnMsg, succesCallBack, failedCallBack);
	}
	
	function goBack (currentdiv, backDiv ) {

		if (document.getElementById(currentdiv)) {
			 document.getElementById(currentdiv).style.display = "none";
		}
		if (document.getElementById(backDiv)) {
			document.getElementById(backDiv).style.display = "block";
		}
	}
	
	function countryChange(){
		var e = document.getElementById("country");
		updateRegion(e.options[e.selectedIndex].value);
	}

	function selectBp() {
		var radios = document.getElementsByName("actionForm.tokenFormBean.selectedAccount");
		var checked = false;
		for (var i = 0; i < radios.length; i++) {
			if (radios[i].checked) {
				checked = true;
				break;
			}
		}

		if (checked){
			submitForm('showSoldToId');
		} else {
			var errorMsgKey = $('#tokenSelectAccountErrorKey').val();
			var errorMsg = $('#tokenSelectAccountError').val();
			convertAlertToModelPopUp(errorMsgKey, errorMsg);
		}
	}

	function searchNext() {
		var radios = document.getElementsByName("actionForm.tokenFormBean.selectedAccountNumber");
		var checked = false;
		for (var i = 0; i < radios.length; i++) {
			if (radios[i].checked) {
				checked = true;
				break;
			}
		}

		if (checked) {
			submitForm('validateSelectedRecord');
		} else {
			var errorMsgKey = $('#tokenSelectRecordErrorKey').val();
			var errorMsg = $('#tokenSelectRecordError').val();
			convertAlertToModelPopUp(errorMsgKey, errorMsg);
		}
	}

	function additionalinfo(){
		var radios = document.getElementsByName("actionForm.tokenFormBean.replaceContract");
		for (var i = 0; i < radios.length; i++) {
			if (radios[i].checked) {
				var value = radios[i].value;
				if (value == "Yes") {
					document.getElementById("additionalinfo").style.display = "";
					document.getElementById("chooseRegion").style.display = "";
				} else if (value == "No") {
					document.getElementById("additionalinfo").style.display = "none";
					document.getElementById("chooseRegion").style.display = "none";
					if (document.getElementById("additionalInfobox").value != null) {
	 					document.getElementById("additionalInfobox").value = '';
	 				}
				}
				break;
			}
		}
	}
	
	function doSearch() {
		var obj1=document.getElementById('endCustomerName');
		var obj3=document.getElementById('city');
		var obj4=document.getElementById('country').options[document.getElementById('country').selectedIndex].value;
		if ( (obj1 == null || trim(obj1.value).length == 0)
				|| (obj3 == null || trim(obj3.value).length == 0)
				|| (obj4 == null || trim(obj4).length == 0) ) {
			
			var errorMsgKey = $('#tokenReqNameCityCountryErrorKey').val();
			var errorMsg = $('#tokenReqNameCityCountryError').val();
			convertAlertToModelPopUp(errorMsgKey, errorMsg);
			return false;
		}
		submitForm('doFuzzySearch');
		return true;
	}

	function tokenValidateButton() {
		var obj1=document.getElementById('tokenNumber');
		if ( (obj1 == null || trim(obj1.value).length == 0)) {
			var errorMsgKey = $('#tokenBlankErrorKey').val();
			var errorMsg = $('#tokenBlankError').val();
			convertAlertToModelPopUp(errorMsgKey, errorMsg);
			return false;
		}
		if ( isAlphaNumeric(obj1.value) == false ) {
			var errorMsgKey = $('#tokenAlphaNumericErrorKey').val();
			var errorMsg = $('#tokenAlphaNumericError').val();
			convertAlertToModelPopUp(errorMsgKey, errorMsg);
			return false;
		}

		if (obj1.value.length > 23) {
			var errorMsgKey = $('#tokenExcessLengthErrorKey').val();
			var errorMsg = $('#tokenExcessLengthError').val();
			convertAlertToModelPopUp(errorMsgKey, errorMsg);
			return false;
		}
		submitForm('validateToken');
	}

	function validateExistenceWithSize( textobject, name, limit ) {
		if ( textobject.value.length > limit ) {
			var errorMsgKey = $('#tokenValidObjectLengthErrorKey').val();
			var errorMsg = $('#tokenValidObjectLengthError').val();
			errorMsg = errorMsg.replace("<OBJECTVALUE>", name);
			errorMsg = errorMsg.replace("<LIMIT>", limit); 
			convertAlertToModelPopUp(errorMsgKey, errorMsg);
			
			return false;
		}
		return true;
	}

	function checkBpLinkId(){
		var val = document.getElementById('bplinkID1').value;

		if (val==null || trim(val) == "") {
			var errorMsgKey = $('#tokenBPLinkIdErrorKey').val();
			var errorMsg = $('#tokenBPLinkIdError').val();
			convertAlertToModelPopUp(errorMsgKey, errorMsg);
			return false;
		} else {
			submitForm('validateBpLinkID');
		}
		return true;
	}

	function validateSoldToIdBP(){
		var obj1 = document.getElementById('cxpSoldTo');
		if 	(obj1 == null || trim(obj1.value).length == 0) {
			var errorMsgKey = $('#tokenEndCustomerAccNumberErrorKey').val();
			var errorMsg = $('#tokenEndCustomerAccNumberError').val();
			convertAlertToModelPopUp(errorMsgKey, errorMsg);
			return false;
		} else {
			document.getElementById('soldToStorer').value = document.getElementById('cxpSoldTo').value.trim().split(" ")[0];
			var soldToVal = document.getElementById('cxpSoldTo').value.trim().split(" ")[0];
			document.getElementById('cxpSoldTo').value = soldToVal;
			submitForm('validateShipToId');
		}
	}

	function validateSoldToIdNonBP() {
		var obj1 = document.getElementById('cxpSoldTo');
		if 	(obj1 == null || trim(obj1.value).length == 0) {
			var errorMsgKey = $('#tokenEndCustomerAccNumberErrorKey').val();
			var errorMsg = $('#tokenEndCustomerAccNumberError').val();
			convertAlertToModelPopUp(errorMsgKey, errorMsg);
			return false;
		} else {
			submitForm('validateShipToId');
	 	}
	}
	
	function isAlphaNumeric(value) {
		var pattern = /^[a-zA-Z0-9]*$/;
		return (value.search(pattern) != -1);
	}
	
	var countryMap = new Object();
		<%
		for (Map.Entry<String, Map<Long, Region>> countryMap : PageCache.regionMap.entrySet() ) {
			out.print("var regions = new Array();");
			out.print("var region=new Object();");
			out.print("region.regionId='';");
			out.print("region.region='';");
			out.print("region.description='---- Select ----';");
			out.print("region.countryCode='';");
			out.print("regions.push(region);");

			for ( Map.Entry<Long, Region> regionMap : countryMap.getValue().entrySet()) {
				out.print("var region=new Object();");
				out.print("region.regionId=\""+ regionMap.getValue().getRegionId()+"\";");
				out.print("region.region=\""+ regionMap.getValue().getRegion()+"\";");
				out.print("region.description=\""+ regionMap.getValue().getDescription()+"\";");
				out.print("region.countryCode=\""+ regionMap.getValue().getCountryCode()+"\";");
				out.print("regions.push(region);");
			}
			out.print("countryMap[\""+countryMap.getKey()+"\"]=regions;");
		}
		%>

	function updateRegion(country) {

		var regionselect = document.getElementById("region");
		var length = regionselect.options.length;
		for (i = 0; i < length; i++) {
			regionselect.options[i] = null;
		}
		regionselect.options.length = 0;
		var regions = countryMap[country];

		if (regions == null || regions.length == 0) {
			document.getElementById("otherRegionSelect").style.display = "none";
		} else {
			document.getElementById("otherRegionSelect").style.display = "";
			for (var i = 0; i < regions.length; i++) {
						regionselect.add(new Option(regions[i].description , regions[i].regionId));
			}
		}
	}

	// Function to split Multiple E-mails by semi-colons
	function splitMails(val){
		var str = '';
		var mails = val.split(';');
		for(i=0; i<mails.length; i++){
			var str = mails[i].replace(/\s/g, "");
			if((str)!=''){
				if (!validateEmailId(str)){
				alert('${bundle.msg.validationEmailInvalid} ' +str);
				return false;
				}
			}
		}
		return true;
	}

	//Function to validate email id.
	function validateEmailId(mail){
	/** This change for: GRT will allow email id contain _ or - **/
	/** Change start ***/
		var reg =	/^([_a-zA-Z0-9-]+[_|\_|\.]?)*[_a-zA-Z0-9-]+@([a-zA-Z0-9-]+[_|\_|\.]?)*[a-zA-Z0-9-]+\.[a-zA-Z]{2,9}$/;
	/** Change end **/
		if (!reg.test(mail)){
			return false;
		}
		return true;
	}

</script>


<!-- start page content-wrap -->
<div class="content-wrap token-redemption">

	<h1 class="page-title">Token Redemption</h1>

	<!-- start content -->
	<div class="content">

		<!-- start tokenRedemptionForm -->
		<form action="begin" id="tokenRedemptionForm" method="post">
			<a id="validateToken" href="<c:url context="${ _path }" value="/token/validateToken.action" />"></a>
			<a id="validateBpLinkID" href="<c:url context="${ _path }" value="/token/validateBpLinkID.action" />"></a>
			<a id="cancelTokenRedemption" href="<c:url context="${ _path }" value="/home/home-action.action" />"></a>
			<a id="showSoldToId" href="<c:url context="${ _path }" value="/token/showSoldToId.action" />"></a>
			<a id="showFuzzySearch" href="<c:url context="${ _path }" value="/token/showFuzzySearch.action" />"></a>
			<a id="doFuzzySearch" href="<c:url context="${ _path }" value="/token/doFuzzySearch.action" />"></a>
			<a id="redeemToken" href="<c:url context="${ _path }" value="/token/redeemToken.action" />"></a>
			<a id="validateSelectedRecord" href="<c:url context="${ _path }" value="/token/validateSelectedRecord.action" />"></a>
			<a id="begin" href="<c:url context="${ _path }" value="/token/begin.action" />"></a>
			<a id="lookupBackButton" href="<c:url context="${ _path }" value="/token/lookupBackButton.action" />"></a>
			<a id="validateShipToId" href="<c:url context="${ _path }" value="/token/validateShipToId.action" />"></a>

			<input type="hidden" id="soldToStorer" value="${ actionForm.tokenFormBean.shipTo }" />
			
			<!-- start tokenNumber1 1/5 -->
			<div id="tokenNumber1" style="display: ${actionForm.tokenFormBean.tokenNumberFlag};">

				<div id="token-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">Step 1 of 5 Token Number
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div class="data collapse-box-container">

						<div class="row">
							<div class="col">
								<p>
									<span class="title">
										Enter Token Number *
									</span>
									<input type="text" name="actionForm.tokenFormBean.tokenNumber" value="${fn:escapeXml(actionForm.tokenFormBean.tokenNumber)}"	id="tokenNumber" size="40" maxlength="23" />								</p>
							</div>
						</div>

					</div>
					<!-- start error msgs -->
					<c:if test="${requestScope.soldToError ne null}">
						<script type="text/javascript">
							window.onload= function() {
								convertAlertToModelPopUpServerSideMsg("${requestScope.soldToError}");
							}
						</script>
						<%-- <div class="error-bar" style="display: block;">
							<i class="fa fa-exclamation-triangle"></i><span class="error-bar-msg">${requestScope.soldToError}</span>
						</div> --%>
					</c:if>
					<c:if test="${requestScope.tokenSearchError ne null}">
						<script type="text/javascript">
							window.onload= function() {
								convertAlertToModelPopUpServerSideMsg("${requestScope.tokenSearchError}");
							}
						</script>
				
					</c:if>
					<!-- end error msgs -->
				</div>

				<!-- start controls -->
				<div class="controls">
					<input type="button" value="Cancel" class="button gray" onclick="javascript:cancelProcess();" />
					<input type="button" value="Next" class="button gray" onclick="javascript:tokenValidateButton();" />
				</div>
				<!-- end controls -->

			</div>
			<!-- end tokenNumber1 -->

			<!-- start tokenDetails 2/5 -->
			<div id="tokenDetails" style="display: ${actionForm.tokenFormBean.tokenDetailsFlag};">

				<div id="token-detail-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">Step 2 of 5 Token Details
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div class="collapse-box-container">

						<div class="data">
							<div class="row">
								<div class="col">
									<p>
										<span class="title">
											Token Number
										</span>
										${fn:escapeXml(actionForm.tokenFormBean.tokenNumber)}
									</p>
								</div>
								<div class="col">
									<p>
										<span class="title">
											Service term
										</span>
										${actionForm.tokenFormBean.serviceTerm}
									</p>
								</div>
							</div>

							<div class="row">
								<div class="col">
									<p>
										<span class="title">
											Enter BP Link ID /Co-Delivery BP Link Id<c:if test="${actionForm.tokenFormBean.bplinkidRequired}"> *</c:if>
										</span>
										<input type="text" name="actionForm.tokenFormBean.bpLinkID" value="${fn:escapeXml(actionForm.tokenFormBean.bpLinkID)}"	id="bplinkID1" />
									</p>
								</div>
							</div>
						</div>

					</div>
					<!-- start error msgs -->
					<c:if test="${requestScope.bplinkerror ne null}">
						<script type="text/javascript">
							window.onload= function() {
								convertAlertToModelPopUpServerSideMsg("${requestScope.bplinkerror}");
							}
						</script>
					</c:if>
					<!-- end error msgs -->
				</div>

				<!-- start token-service-wrap -->
				<div id="token-service-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">Service Details
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div class="collapse-box-container">

						<div class="basic-table-wrap">
							<table colspan="3" width="70%"	border="1" class="basic-table">
								<thead>
									<tr>
										<th width="25%" align="left">Service description</th>
										<th width="30%" align="left">T-Code service material/ Material Code/ Hardware</th>
										<th width="45%" align="left">Quantity</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${actionForm.tokenFormBean.tokenServiceDetails}" var="item">
									<tr>
										<td align="left" width="25%">${item.serviceCodeDescription}</td>
										<td align="left" width="30%">${item.serviceCode}/ ${item.materialCode}/ ${item.materialCodeDescription}</td>
										<td align="left" width="45%">${item.quantity}</td>
									</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>

					</div>
				</div>
				<!-- end token-service-wrap -->

				<!-- start controls -->
				<div class="controls">
					<input type="button" value="Back" class="button gray" onclick="submitForm('begin');" />
					<input type="button" value="Cancel" class="button gray" onclick="cancelProcess();" />
					<c:choose>
					<c:when test="${actionForm.tokenFormBean.bplinkidRequired}">
					<input type="button" value="Next" class="button gray" onclick="javascript:checkBpLinkId(); " />
					</c:when>
					<c:otherwise>
					<input type="button" value="Next" class="button gray" onclick="javascript:submitForm('validateBpLinkID');" />
					</c:otherwise>
					</c:choose>
				</div>
				<!-- end controls -->

			</div>
			<!-- end tokenDetails -->

			<!-- start bplinkdetails 2/5 -->
			<div id="bplinkdetails" style="display: ${actionForm.tokenFormBean.bplinkdetailsFlag};">

				<div id="bp-detail-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">Step 2 of 5 BP Link Details
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div class=" collapse-box-container">

						<div class="data">
							<div class="row">
								<div class="col">
									<p>
										<span class="title">
											Token Number
										</span>
										${fn:escapeXml(actionForm.tokenFormBean.tokenNumber)}
									</p>
								</div>
								<div class="col">
									<p>
										<span class="title">
											Distributor
										</span>
										${actionForm.tokenFormBean.distiSoldToName}
									</p>
								</div>
							</div>

							<div class="row">
								<div class="col">
									<p>
										<span class="title">Service term</span>
										${actionForm.tokenFormBean.serviceTerm}
									</p>
								</div>
								<div class="col">
									<p>
										<span class="title">BP Link ID</span>
										<input type="text" name="actionForm.tokenFormBean.bpLinkID" value="${fn:escapeXml(actionForm.tokenFormBean.bpLinkID)}" disabled="true" />									</p>
								</div>
							</div>

						</div>

					</div>

					<!-- start error msgs -->
					<c:if test="${requestScope.medallevelerror ne null}">
						<script type="text/javascript">
							window.onload= function() {
								convertAlertToModelPopUpServerSideMsg("${requestScope.medallevelerror}");
							}
						</script>
					</c:if>
					<!-- end error msgs -->

				</div>

				<!-- start bp-services-wrap -->
				<div id="bp-services-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">Step 2 of 5 BP Link Details
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div class=" collapse-box-container">
						<div class="data basic-table-wrap">
							<table colspan="3" align="center" width="100%" border="0" class="basic-table">
								<thead>
									<tr>
										<th width="25%">Service description</th>
										<th width="30%">T-Code service material/ Material Code/ Hardware</th>
										<th width="45%">Quantity</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${actionForm.tokenFormBean.tokenServiceDetails}" var="item">
									<tr>
										<td align="left" width="25%">${item.serviceCodeDescription}</td>
										<td align="left" width="30%">${item.serviceCode}/ ${item.materialCode}/ ${item.materialCodeDescription}</td>
										<td align="left" width="45%">${item.quantity}</td>
									</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<!-- start bp-services-wrap -->

				<!-- start bp-detail-address-wrap -->
				<div id="bp-detail-address-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">BP Link Address Details
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div class=" collapse-box-container">

						<div class="data basic-table-wrap">
							<table colspan="2" width="100%"	border="0" class="basic-table">
								<thead>
									<tr>
										<th align="left">Select</th>
										<th align="left">Account Id</th>
										<th align="left">BP Account Name</th>
										<th align="left">Address</th>
										<th align="left">City</th>
										<th align="left">State / Province</th>
										<th align="left">Country</th>
										<th align="left">Zip/Postal Code</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="item" items="${actionForm.tokenFormBean.attachedBp}">
									<tr>
										<td align="center" ><input type="radio" id="radioSelect_${item.soldToId}" name="actionForm.tokenFormBean.selectedAccount" value="${item.soldToId}" /> </td>
										<td>${item.soldToId } </td>
										<td>${item.soldToName } </td>
										<td>${item.streetAddress } </td>
										<td>${item.city } </td>
										<td>${item.state}</td>
										<td>${item.country }</td>
										<td>${item.zip }</td>
									</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>

					</div>
				</div>
				<!-- start bp-detail-address-wrap -->

				<!-- start controls -->
				<div class="controls">
					<input type="button" class="button gray" value="Back" onclick="javascript:goBack('bplinkdetails','tokenDetails')" />
					<input type="button" class="button gray" value="Cancel" onclick="javascript:cancelProcess();" />
					<input type="button" class="button gray" value="Next"	onclick=" javascript:selectBp();" />
				</div>
				<!-- end controls -->

			</div>
			<!-- end bplinkdetails -->

			<!-- start shiptocreationforBP 3/5 -->
			<c:if test="${actionForm.tokenFormBean.shipToCreationFlagForBP eq 'block'}">
			<div id="shiptocreationforBP" style="display: ${actionForm.tokenFormBean.shipToCreationFlagForBP};">

				<div id="shipto-bp-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">Step 3 of 5 Business Partner Details
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div class="data collapse-box-container">

						<div class="row">
							<div class="col">
								<p>
									<span class="title">
										Business Partner
									</span>
									BPName ${actionForm.tokenFormBean.bpName}
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">
										Enter End Customer Account Number
									</span>
									<input type="text" class="soldTo autocompleteBp" name="actionForm.tokenFormBean.shipTo" value="" id="cxpSoldTo" size="60" />
									<input type="button" class="button gray small"	onClick="document.getElementById('soldToStorer').value = document.getElementById('cxpSoldTo').value.trim().split(' ')[0]; javascript:submitForm('showFuzzySearch');" value="Lookup" />
								</p>
							</div>
						</div>

					</div>

					<!-- start error msgs -->
					<c:if test="${requestScope.countryUSError ne null}">
						<script type="text/javascript">
							window.onload= function() {
								convertAlertToModelPopUpServerSideMsg("${requestScope.countryUSError}");
							}
						</script>
					</c:if>
					<c:if test="${requestScope.countryNonUSError ne null}">
						<script type="text/javascript">
							window.onload= function() {
								convertAlertToModelPopUpServerSideMsg("${requestScope.countryNonUSError}");
							}
						</script>
					</c:if>
					<c:if test="${requestScope.soldToError ne null}">
						<script type="text/javascript">
							window.onload= function() {
								convertAlertToModelPopUpServerSideMsg("${requestScope.soldToError}");
							}
						</script>
					</c:if>
					<!-- end error msgs -->

				</div>

				<!-- start controls -->
				<div class="controls">
					<input type="button" class="button gray" value="Back" onClick="javascript:submitForm('lookupBackButton');" />
					<input type="button" class="button gray" value="Cancel" onclick="javascript:cancelProcess();" />
					<input type="button" class="button gray"	value="Next" onClick="javascript:validateSoldToIdBP();" />
				</div>
				<!-- end controls -->

			</div>
			</c:if>
			<!-- end shiptocreationforBP -->

			<!-- start shiptocreationforNonBP 3/5 -->
			<c:if test="${actionForm.tokenFormBean.shipToCreationFlagForNonBP eq 'block'}">
			<div id="shiptocreationforNonBP" style="display: ${actionForm.tokenFormBean.shipToCreationFlagForNonBP};">

				<div id="shipto-nonbp-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">Step 3 of 5 Business Partner Details
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div class="data collapse-box-container">

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Business Partner</span>
									${actionForm.tokenFormBean.bpName}
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Enter End Customer Account Number</span>
									<input type="text" name="actionForm.tokenFormBean.shipTo"	id="cxpSoldTo"/>
									<input type="button" class="button gray small" onClick="submitForm('showFuzzySearch');" value="Lookup" />
								</p>
							</div>
						</div>

					</div>

					<!-- start error msgs -->
					<c:if test="${requestScope.countryUSError ne null}">
						<script type="text/javascript">
							window.onload= function() {
								convertAlertToModelPopUpServerSideMsg("${requestScope.countryUSError}");
							}
						</script>
					</c:if>
					<c:if test="${requestScope.countryNonUSError ne null}">
						<script type="text/javascript">
							window.onload= function() {
								convertAlertToModelPopUpServerSideMsg("${requestScope.countryNonUSError}");
							}
						</script>
					</c:if>
					<c:if test="${requestScope.soldToError ne null}">
						<script type="text/javascript">
							window.onload= function() {
								convertAlertToModelPopUpServerSideMsg("${requestScope.soldToError}");
							}
						</script>
					</c:if>
					<!-- end error msgs -->

				</div>

				<!-- start controls -->
				<div class="controls">
					<input type="button" class="button gray" value="Back" onClick="submitForm('lookupBackButton');" />
					<input type="button" class="button gray" value="Cancel" onclick="cancelProcess();" />
					<input type="button" class="button gray" value="Next"	onClick="validateSoldToIdNonBP();" />
				</div>
				<!-- end controls -->

			</div>
			</c:if>
			<!-- end shiptocreationforNonBP -->

			<!-- start contractdate 4/5 -->
			<div id="contractdate" style="display:	${actionForm.tokenFormBean.contractDateFlag};">

				<div id="contract-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">Step 4 of 5 Business Partner Details
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div class="data collapse-box-container">

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Token Number</span>
									${fn:escapeXml(actionForm.tokenFormBean.tokenNumber)}
								</p>
							</div>
							<div class="col">
								<p>
									<span class="title">Distributor</span>
									${actionForm.tokenFormBean.distiSoldToName}
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Service term</span>
									${actionForm.tokenFormBean.serviceTerm}
								</p>
							</div>
							<div class="col">
								<p>
									<span class="title">Contract Start Date</span>
									${actionForm.tokenFormBean.contractStartDate }
								</p>
							</div>
						</div>

					</div>

					<!-- start error msgs -->
					<c:if test="${requestScope.redeemTokenError ne null}">
						<script type="text/javascript">
							window.onload= function() {
								convertAlertToModelPopUpServerSideMsg("${requestScope.redeemTokenError}");
							}
						</script>
					</c:if>
					<!-- end error msgs -->

				</div>

				<!-- start contract-service-wrap -->
				<div id="contract-service-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">Service Details
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div class="data collapse-box-container">

						<div class="basic-table-wrap">
							<table colspan="3" align="center" height="10%" width="100%" border="0" class="basic-table">
								<thead>
									<tr>
										<th width="25%" align="left" >Service description</th>
										<th width="30%" align="left">T-Code service material/ Material Code/ Hardware</th>
										<th width="45%" align="left">Quantity</th>
									</tr>
								</thead>
								<tbody>
								<c:forEach items="${actionForm.tokenFormBean.tokenServiceDetails}" var="item">
								<tr>
									<td align="left" width="25%" align="left">${item.serviceCodeDescription}</td>
									<td align="left" width="30%" align="left">${item.serviceCode}/ ${item.materialCode}/ ${item.materialCodeDescription}</td>
									<td align="left" width="45%" align="left">${item.quantity}</td>
								</tr>
								</c:forEach>
								</tbody>
							</table>
						</div>

					</div>
				</div>
				<!-- end contract-service-wrap -->

				<c:if test="${actionForm.tokenFormBean.selectedBp ne null}">
				<!-- start contract-bplink-wrap -->
				<div id="contract-bplink-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">BP Link Details
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div class="data collapse-box-container">

						<div class="basic-table-wrap">

							<table colspan="2" height="10%" width="100%"	border="0" class="basic-table">
								<thead>
									<tr>
										<th align="left" width="25%">BP Link ID</th>
										<th align="left" width="30%">Account Id</th>
										<th align="left" width="45%">Account Name</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td width="25%">${fn:escapeXml(actionForm.tokenFormBean.bpLinkID)} </td>
										<td width="30%">${actionForm.tokenFormBean.selectedBp.soldToId } </td>
										<td width="45%">${actionForm.tokenFormBean.selectedBp.soldToName } </td>
									</tr>
								</tbody>
							</table>
						</div>

					</div>
				</div>
				<!-- end contract-bplink-wrap -->
				</c:if>

				<!-- start contract-address-wrap -->
				<div id="contract-address-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">Account Details
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div class="data collapse-box-container">

						<div class="row">
							<div class="col">
								<p>
									<span class="title">End Customer Account Number</span>
									${actionForm.tokenFormBean.endCoustomerAccount.soldToNumber }
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Name</span>
									${actionForm.tokenFormBean.endCoustomerAccount.name }
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Address</span>
									${actionForm.tokenFormBean.endCoustomerAccount.primaryAccountStreetAddress }
								</p>
							</div>
							<div class="col">
								<p>
									<span class="title">City</span>
									${actionForm.tokenFormBean.endCoustomerAccount.primaryAccountCity }
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">State / Province</span>
									${actionForm.tokenFormBean.endCoustomerAccount.primaryAccountState }
								</p>
							</div>
							<div class="col">
								<p>
									<span class="title">Zip/Postal Code</span>
									${actionForm.tokenFormBean.endCoustomerAccount.primaryAccountPostalCode }
								</p>
							</div>
							<div class="col">
								<p>
									<span class="title">Country </span>
									${actionForm.tokenFormBean.endCoustomerAccount.primaryAccountCountry }
								</p>
							</div>
						</div>

						<div class="row">
							<c:if test="${actionForm.tokenFormBean.tokenDetails.requesterSalesOrg eq 'B001'}">
							<div class="col">
								<p>
									<span class="title">Is this installation for Government account?</span>
									<input type="radio" name="actionForm.tokenFormBean.usStore" value="Yes" />Yes
									<input type="radio" name="actionForm.tokenFormBean.usStore"	value="No" checked />No
								</p>
							</div>
							</c:if>
							<div class="col">
								<p>
									<span class="title">Do you want to replace  the existing contract?</span>
									<input type="radio" name="actionForm.tokenFormBean.replaceContract" value="Yes" id="addInfoYes" onClick="showAdditionalInfo();" /> Yes
									<input type="radio" name="actionForm.tokenFormBean.replaceContract" value="No" id="addInfoNo" onClick="hideAdditionalInfo();" checked />No
								</p>
							</div>
						</div>

						<div class="row" style="display:none;" id="additionalinfo">
							<div class="col">
								<p>
									<span class="title">Additional information</span>
								</p>
							</div>
							<div class="col">
								<p>
									<textarea name="actionForm.tokenFormBean.additionalInfo" id="additionalInfobox" rows="4" > </textarea>
								</p>
							</div>
						</div>
						<!--for existing contract info  -->
						<div class="row" style="display:none;" id="selectContractToCancel">
							<div class="col">
								<p>
									<span class="title">Existing Contracts</span>
								</p>
							</div>
							<div class="col" id="contractsDiv">
								<p>
									<span class="title">Here are the contract details for this location. Please contact your customer or OEFC for more information on the contract before you proceed to cancel one. If you are sure of the contract you need to cancel and you select the appropriate one, then Termination Fees may apply. Contact OEFC for applicable fee information.</span>
									<div class="basic-table-wrap">
										<table height="10%" width="75%" border="0" class="basic-table" id="contractListTable">
											<tbody>
											<thead>
												<tr>
													<th align="left" width="20%">Select for Cancellation</th>
													<th align="left" width="30%">Contract Number</th>
													<th align="left" width="25%">Start Date</th>
													<th align="left" width="25%">End Date</th>
												</tr>
											</thead>
											<c:forEach items="${actionForm.tokenFormBean.contracts }" var="container" varStatus="rowCounter">
												<tr>
													<td width="20%">
														<input type="radio" id="contractRadio_${container.contractNumber}" value="${container.contractNumber}" allowSelection="${container.allowReplace}" onclick="selectContract('${container.contractNumber}','${container.contractStartDateStr}','${container.contractEndDateStr}' );" />
												    </td>
													<td width="30%">${container.contractNumber}</td>
													<td width="25%">${container.contractStartDateStr}</td>
													<td width="25%">${container.contractEndDateStr}</td>
												</tr>
											</c:forEach>
											</tbody>
										</table>
									</div>
								</p>
							</div>
						</div>
						<div class="row" style="display:none;" id="chooseRegion">
							<div class="col">
								<p>
									<span class="title">Please choose the region</span>
								</p>
							</div>
							<div class="col">
								<p>
									<select name="actionForm.tokenFormBean.region" id="selectedRegion" class="required" >
										<option class="required" value="">Choose</option>
										<c:forEach var="entry" items="${actionForm.tokenFormBean.regionList}">
											<option class="required" value="${entry.key}">${entry.value}</option>
										</c:forEach>
									</select>
								</p>
							</div>
						</div>

					</div>
				</div>
				<!-- end contract-address-wrap -->

				<!-- start contract-email-wrap -->
				<div id="contract-email-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">Email Notification
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div class="data collapse-box-container">

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Additional Email Id(s) to send Token Redemption notification</span>
									<input type="text" name="actionForm.tokenFormBean.additionalEmailIds" id="additionalEmailIds" size="80"/>
								</p>
							</div>
						</div>

					</div>
				</div>
				<!-- end contract-email-wrap -->

				<!-- start controls -->
				<div class="controls" title="${bundle.msg.emailToolTip}">
					<input type="button" class="button gray" value="Back" onClick="submitForm('showSoldToId');" />
					<input type="button" class="button gray" value="Cancel" onclick="cancelProcess();" />
					<input type="button" class="button gray" id="redeemTokenId" value="Redeem Token" onclick="redeemToken();" />
				</div>
				<!-- end controls -->

			</div>
			<!-- end contractdate -->

			<!-- start finalScreen1 -->
			<div id="finalScreen1" style='display: ${actionForm.tokenFormBean.finalScreenFlag};'>

				<!-- start final-wrap -->
				<div id="final-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">FinalStep 5 of 5
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div class="data collapse-box-container">

						<div class="row">
							<div class="col">
								<p>
									Congratulations. You have successfully activated IPOSS contract using Token: ${actionForm.tokenFormBean.tokenNumber}
								</p>
							</div>
						</div>
						<div class="row">
							<div class="col">
								<p>
									<span class="title">Contract Number</span>
									${actionForm.tokenFormBean.tokenResponse.contractNumber}
								</p>
							</div>
						</div>

					</div>
				</div>
				<!-- end final-wrap -->

				<!-- start final-2-wrap -->
				<div id="final-2-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">Details
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div class="data collapse-box-container">

						<div class="basic-table-wrap">
							<table	height="100%" width="100%" border="0"class="basic-table">
								<thead>
									<tr>
										<th align="left">Distributor/Contract Party</th>
										<th align="left">Business Partner</th>
										<th align="left">End Customer</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>
											${actionForm.tokenFormBean.distributorAccount.name}<br>
											${actionForm.tokenFormBean.distributorAccount.primaryAccountStreetAddress }<br>
											${actionForm.tokenFormBean.distributorAccount.primaryAccountCity }<br>
											${actionForm.tokenFormBean.distributorAccount.primaryAccountState } ${actionForm.tokenFormBean.distributorAccount.primaryAccountCountry}
										</td>
										<td>
											${actionForm.tokenFormBean.selectedBp.soldToName }<br>
											${actionForm.tokenFormBean.selectedBp.streetAddress }<br>
											${actionForm.tokenFormBean.selectedBp.city }<br>
											${actionForm.tokenFormBean.selectedBp.state } ${actionForm.tokenFormBean.selectedBp.country }

										</td>
										<td>
											${actionForm.tokenFormBean.endCoustomerAccount.name }<br>
											${actionForm.tokenFormBean.endCoustomerAccount.primaryAccountStreetAddress }<br>
											${actionForm.tokenFormBean.endCoustomerAccount.primaryAccountCity }<br>
											${actionForm.tokenFormBean.endCoustomerAccount.primaryAccountState } ${actionForm.tokenFormBean.endCoustomerAccount.primaryAccountCountry}
										</td>
									</tr>
								</tbody>
							</table>
						</div>

					</div>
				</div>
				<!-- end final-2-wrap -->

				<!-- start final-3-wrap -->
				<div id="final-3-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">Contract Information
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div class="data collapse-box-container">

						<div class="basic-table-wrap">
							<table	height="100%" width="100%" border="0" class="basic-table">
								<thead>
									<tr>
										<th width="25%">Service description</th>
										<th width="30%">T-Code service material/ Material Code/ Hardware</th>
										<th width="45%">Quantity</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${actionForm.tokenFormBean.tokenServiceDetails}" var="item">
									<tr>
										<td align="left" width="25%">${item.serviceCodeDescription}</td>
										<td align="left" width="30%">${item.serviceCode}/ ${item.materialCode}/ ${item.materialCodeDescription}</td>
										<td align="left" width="45%">${item.quantity}</td>
									</tr>
									</c:forEach>

								</tbody>
							</table>
						</div>

					</div>
				</div>
				<!-- end final-3-wrap -->

				<!-- start final-4-wrap -->
				<div id="final-4-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">Token Redemption Information
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div class="data collapse-box-container">

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Contract Activate On</span>
									${actionForm.tokenFormBean.contractStartDate }
								</p>
							</div>
							<div class="col">
								<p>
									<span class="title">Token redeemed date</span>
									${actionForm.tokenFormBean.contractSubmitedDate }
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Contract Activated By</span>
									${requestScope.username }
								</p>
							</div>
							<div class="col">
								<p>
									<span class="title">BP Link ID</span>
									${fn:escapeXml(actionForm.tokenFormBean.bpLinkID) }
								</p>
							</div>
							<div class="col">
								<p>
									<span class="title">Sold To/Functional Location</span>
									${actionForm.tokenFormBean.endCoustomerAccount.soldToNumber }
								</p>
							</div>
						</div>

					</div>
				</div>
				<!-- end final-4-wrap -->

				<!-- start controls -->
				<div class="controls">
					<input type="button" class="button gray" value="Print" onClick="window.print();" />
					<input type="button" class="button gray" value="Home" onClick="javascript:submitForm('cancelTokenRedemption');" />
				</div>
				<!-- end controls -->

			</div>
			<!-- end finalScreen1 -->

			<!-- start accountaddress 3/5 -->
			<div id="accountaddress" style="display: ${actionForm.tokenFormBean.searchFlag};">

				<div id="address-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">Account Address
						<span>3 of 5</span>
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div class="data collapse-box-container">

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Name*</span>
									<input type="text" name="actionForm.tokenFormBean.endCustomerName" value="${actionForm.tokenFormBean.endCustomerName}" maxlength="80" size="60" id="endCustomerName" class="required"/>
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Street Name</span>
									<input type="text" name="actionForm.tokenFormBean.endCustomerStreetName" value="${fn:escapeXml(actionForm.tokenFormBean.endCustomerStreetName)}" maxlength="60" size="60" id="endCustomerStreetName" class="required"/>
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">City*</span>
									<input type="text" name="actionForm.tokenFormBean.endCustomerCity" value="${actionForm.tokenFormBean.endCustomerCity}" id="city" maxlength="40" size="40" class="required" />
								</p>
							</div>
						</div>

						<div class="row">

							<c:choose>
							<c:when test="${actionForm.tokenFormBean.currentCountryRegion ne null || fn:length(actionForm.tokenFormBean.currentCountryRegion) gt 0}">
							<div class="col" style="display:table-cell;" id="otherRegionSelect">
							</c:when>
							<c:otherwise>
							<div class="col" style="display: none;" id="otherRegionSelect">
							</c:otherwise>
							</c:choose>

								<p>
									<span class="title">State/Province</span>
									<select name="actionForm.tokenFormBean.selectedRegionId" id="region" class="required" >
										<option class="required" value="-1">---- Select ----</option>
										<c:forEach var="entry" items="${actionForm.tokenFormBean.currentCountryRegion}">
										<c:if test="${actionForm.tokenFormBean.selectedRegionId eq entry.key}">
										<option class="required" value="${entry.key}" selected="selected">${entry.value.description}</option>
										</c:if>
										<c:if test="${actionForm.tokenFormBean.selectedRegionId ne entry.key}"></c:if>
										<option class="required" value="${entry.key}">${entry.value.description}</option>
										</c:forEach>
									</select>
								</p>
							</div>
							<div class="col">
								<p>
									<span class="title">Zip/Postal Code</span>
									<input type="text" name="actionForm.tokenFormBean.endCustomerPostalCode" value="${fn:escapeXml(actionForm.tokenFormBean.endCustomerPostalCode)}" id="endCustomerPostalCode" size="12" class="required" />
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Country* </span>
									<select name="actionForm.tokenFormBean.endCustomerCountry" id="country" onChange="javascript:countryChange();" class="required" >
										<c:forEach var="entry" items="${actionForm.tokenFormBean.countryList}">
										<c:choose>
										<c:when test="${actionForm.tokenFormBean.endCustomerCountry eq entry.key}">
										<option class="required" value="${entry.key}" selected="selected">${entry.value}</option>
										</c:when>
										<c:when test="${actionForm.tokenFormBean.endCustomerCountry eq null and entry.key eq 'US'}">
										<option class="required" value="${entry.key}" selected="selected">${entry.value}</option>
										</c:when>
										<c:when test="${fn:length(entry.key) gt 0 and fn:length(entry.value) gt 0}">
										<option class="required" value="${entry.key}">${entry.value}</option>
										</c:when>
										</c:choose>
										</c:forEach>
									</select>
								</p>
							</div>
						</div>

					</div>

					<!-- start error msgs -->
					<c:if test="${requestScope.countryUSError ne null}">
						<script type="text/javascript">
							window.onload= function() {
								convertAlertToModelPopUpServerSideMsg("${requestScope.countryUSError}");
							}
						</script>
					</c:if>
					<c:if test="${requestScope.countryNonUSError ne null}">
						<script type="text/javascript">
							window.onload= function() {
								convertAlertToModelPopUpServerSideMsg("${requestScope.countryNonUSError}");
							}
						</script>
					</c:if>
					<c:if test="${requestScope.soldtosearcherror ne null}">
						<script type="text/javascript">
							window.onload= function() {
								convertAlertToModelPopUpServerSideMsg("${requestScope.soldtosearcherror}");
							}
						</script>
					</c:if>
					<c:if test="${requestScope.searchError ne null}">
						<script type="text/javascript">
							window.onload= function() {
								convertAlertToModelPopUpServerSideMsg("${requestScope.searchError}");
							}
						</script>
					</c:if>
					<!-- end error msgs -->

				</div>

				<!-- start fuzzySerch -->
				<c:if test="${fn:length(actionForm.tokenFormBean.searchResult) gt 0}">
				<div id="fuzzySerch" style="display: ${actionForm.tokenFormBean.searchDataFlag};">

					<!-- start fuzzy-wrap -->
					<div id="fuzzy-wrap" class="data-table-wrap collapse-box">
						<h2 class="collapse-box-header active">Similar Records
							<span class="legend">
								<span class="best">Best Match</span>
								<span class="nearest">Nearest Match</span>
								<span class="related">Related Match</span>
							</span>
							<a href="#">
								<span></span>
							</a>
						</h2>
						<div id="fuzzy-box" class="data collapse-box-container">

							<table width="100%" align="center" class="fuzzy-class basic-table">
								<thead>
									<tr>
										<th>Select *</th>
										<th>Account </th>
										<th>Name} </th>
										<th>Street </th>
										<th>City </th>
										<th>State/Province </th>
										<th>Zip/Postal Code </th>
										<th>Country </th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${ actionForm.tokenFormBean.searchResult }"	var="item">
									<c:set var="color" value="#98CD97"/>
									<c:if test="${item.score le grtConfig.serchMidRange}">
										<c:set var="color" value="#CCCD5F"/>
									</c:if>
									<c:if test="${item.score le grtConfig.searchLowRange}">
										<c:set var="color" value="#CE6564"/>
									</c:if>
									<tr style="background-color:${color}">
										<td	bgcolor="${color}" align="center" width="15%">
											<input type="radio" name="actionForm.tokenFormBean.selectedAccountNumber"	value="${item.accountNumber}" />
										</td>
										<td	bgcolor="${color}">${item.accountNumber } </td>
										<td	bgcolor="${color}">${item.name } </td>
										<td	bgcolor="${color}">${item.street } </td>
										<td	bgcolor="${color}">${item.city } </td>
										<td	bgcolor="${color}">${item.region}</td>
										<td	bgcolor="${color}">${item.zip }</td>
										<td	bgcolor="${color}">${item.country }</td>
									</tr>
									</c:forEach>

								</tbody>
							</table>

						</div>
			
					</div>
					<!-- end fuzzy-wrap -->

				</div>
				</c:if>
				<!-- end fuzzySerch -->
	<c:if test="${requestScope.noresult}">
		<div class="error-bar" style="display: block;">
			<i class="fa fa-exclamation-triangle"></i> <span
				class="error-bar-msg"> No record Found. </span>
		</div>
	</c:if>
	<!-- start controls -->
				<div class="controls">
					<input type="button"	class="button gray"value="Back" onClick="javascript:submitForm('showSoldToId'); " />
					<input type="button"	class="button gray" value="Cancel" onclick="javascript:cancelProcess();" />
					<input type="button" class="button gray" value="Search" onclick="javascript:doSearch();" />
					<c:if test="${actionForm.tokenFormBean.searchDataFlag eq 'block'}">
						<input type="button" class="button gray" value="Next" onclick="javascript:searchNext();" />
					</c:if>
				</div>
				<!-- end controls -->

			</div>
			<!-- end accountaddress -->	
<input type="hidden" id="superUserId" name="actionForm.tokenFormBean.superUser" value="${actionForm.tokenFormBean.superUser}" />
<input type="hidden" id="cancelledContractStartDate" name="actionForm.tokenFormBean.cancelledContractStartDate" value="${fn:escapeXml(actionForm.tokenFormBean.cancelledContractStartDate)}"/>
<input type="hidden" id="cancelledContractEndDate" name="actionForm.tokenFormBean.cancelledContractEndDate" value="${fn:escapeXml(actionForm.tokenFormBean.cancelledContractEndDate)}"/>
<input type="hidden" id="cancelledContractNumber" name="actionForm.tokenFormBean.selectedContractNumber" value="${fn:escapeXml(actionForm.tokenFormBean.selectedContractNumber)}"/>
		</form>
		<!-- end tokenRedemptionForm -->

	</div>
	<!-- end content -->
</div>
<!-- end page content-wrap -->


<%-- ************************************************************* --%>
<%-- ***** BELOW IS DEPRECATED CODE - NEED REVIEW AND REMOVE ***** --%>
<%-- ************************************************************* --%>

<%-- <!--		 <script type='text/javascript' >
				function cxpSoldTo_autoComplete(event, eleInput){
					cxpSoldTo_loadData(event, eleInput);
				}

				function cxpSoldTo_loadData(event, eleInput){
						if (isUpKeyPressed(event) || isDownKeyPressed(event)) {
								dealWithUpDownKey(event, eleInput);
						} else if (!isEnterKeyPressed(event)) {
							 var data = getData(eleInput.value);
	 										 scallback(data, eleInput, event);
						}
				}

				function cxpSoldTo_validateInput(eleInput){

						var data = getData(eleInput.value);
								if (data.length == 1) {
								hideAutoCompleteErrMsg(eleInput);
								}
								else {
									 // generateAutoCompleteErrMsgForBusinessPartner(eleInput);
							 }
				}

				function getData(reg)
				{
					reg	= reg.toUpperCase();
					var data = new Array();
	 					<c:forEach	items="${actionForm.tokenFormBean.cxpSoldToList}" var="value" varStatus="count" begin="0">
	 					data[${count.index}] = "${value}";</c:forEach>
					var data1 = [];
					for(var i=0; i < data.length; i++)
					{
						var dataValue = data[i];
						if (dataValue != null)
						{
						var value = data[i].toUpperCase();
						if (value.indexOf(reg) !=-1)
						{
		 						data1.push(data[i]);
		 					}
						}
					}
					return data1;
				}
</script> -->

	<!--div id="token-popup" class="dialog" style="left: -1000em;">
		<div class="dialog-container">
			<div class="gb-tc"></div>
			<div class="gb">
				<div class="dialog-title-bar">
					<table width="100%">
						<tr>
							<td width="50%" align="right">Please wait.<td>
							<td id="userMessage" width="40%" style="font-weight: bold;"></td>
							<td><div	class="dialog-close-button" style="cursor: pointer; margin-top: 0px;"></div></td>
						</tr>
					</table>
				</div>
				<div class="dialog-content">

					 <table width="100%">
						<tr>
							<td align="center">
								Token redemption process is in process, Please wait and do not refresh or close the browser.
							</td>
						</tr>
						<tr>
							<td>
							</td>
						</tr>
					</table>
				</div>
				<div class="fixer"></div>
			</div>
			<div class="gb-bc"></div>
		</div>
	</div-->
	<div id="dialog-underlay" style="opacity:0.2; filter:alpha(opacity=20); position: absolute; display: none; width: 100%; height: 100%; top: 0; right: 0; bottom: 0; left: 0;"></div>

	--%>
<input type="hidden" id="tokenIvalidError" value="${grtConfig.tokenIvalidError }" />
<input type="hidden" id="tokenIvalidErrorKey" value="${grtConfig.ewiMessageCodeMap['tokenIvalidError'] }" />
<input type="hidden" id="tokenExpiredError" value="${grtConfig.tokenExpiredError }" />
<input type="hidden" id="tokenExpiredErrorKey" value="${grtConfig.ewiMessageCodeMap['tokenExpiredError'] }" />
<input type="hidden" id="tokenRedeemedError" value="${grtConfig.tokenRedeemedError }" />
<input type="hidden" id="tokenRedeemedErrorKey" value="${grtConfig.ewiMessageCodeMap['tokenRedeemedError'] }" />
<input type="hidden" id="tokenUnauthorizeToRedeemError" value="${grtConfig.tokenUnauthorizeToRedeemError }" />
<input type="hidden" id="tokenUnauthorizeToRedeemErrorKey" value="${grtConfig.ewiMessageCodeMap['tokenUnauthorizeToRedeemError'] }" />
<input type="hidden" id="tokenAdditionInfoLengthError" value="${grtConfig.tokenAdditionInfoLengthError }" />
<input type="hidden" id="tokenAdditionInfoLengthErrorKey" value="${grtConfig.ewiMessageCodeMap['tokenAdditionInfoLengthError'] }" />
<input type="hidden" id="tokenCancelWarningMsg" value="${grtConfig.tokenCancelWarningMsg }" />
<input type="hidden" id="tokenCancelWarningMsgKey" value="${grtConfig.ewiMessageCodeMap['tokenCancelWarningMsg'] }" />
<input type="hidden" id="tokenSelectAccountError" value="${grtConfig.tokenSelectAccountError }" />
<input type="hidden" id="tokenSelectAccountErrorKey" value="${grtConfig.ewiMessageCodeMap['tokenSelectAccountError'] }" />
<input type="hidden" id="tokenSelectRecordError" value="${grtConfig.tokenSelectRecordError }" />
<input type="hidden" id="tokenSelectRecordErrorKey" value="${grtConfig.ewiMessageCodeMap['tokenSelectRecordError'] }" />
<input type="hidden" id="tokenReqNameCityCountryError" value="${grtConfig.tokenReqNameCityCountryError }" />
<input type="hidden" id="tokenReqNameCityCountryErrorKey" value="${grtConfig.ewiMessageCodeMap['tokenReqNameCityCountryError'] }" />
<input type="hidden" id="tokenBlankError" value="${grtConfig.tokenBlankError }" />
<input type="hidden" id="tokenBlankErrorKey" value="${grtConfig.ewiMessageCodeMap['tokenBlankError'] }" />
<input type="hidden" id="tokenAlphaNumericError" value="${grtConfig.tokenAlphaNumericError }" />
<input type="hidden" id="tokenAlphaNumericErrorKey" value="${grtConfig.ewiMessageCodeMap['tokenAlphaNumericError'] }" />
<input type="hidden" id="tokenExcessLengthError" value="${grtConfig.tokenExcessLengthError }" />
<input type="hidden" id="tokenExcessLengthErrorKey" value="${grtConfig.ewiMessageCodeMap['tokenExcessLengthError'] }" />
<input type="hidden" id="tokenValidObjectLengthError" value="${grtConfig.tokenValidObjectLengthError }" />
<input type="hidden" id="tokenValidObjectLengthErrorKey" value="${grtConfig.ewiMessageCodeMap['tokenValidObjectLengthError'] }" />
<input type="hidden" id="tokenBPLinkIdError" value="${grtConfig.tokenBPLinkIdError }" />
<input type="hidden" id="tokenBPLinkIdErrorKey" value="${grtConfig.ewiMessageCodeMap['tokenBPLinkIdError'] }" />
<input type="hidden" id="tokenEndCustomerAccNumberError" value="${grtConfig.tokenEndCustomerAccNumberError }" />
<input type="hidden" id="tokenEndCustomerAccNumberErrorKey" value="${grtConfig.ewiMessageCodeMap['tokenEndCustomerAccNumberError'] }" />
<input type="hidden" id="tokenValidatingError" value="${grtConfig.tokenValidatingError }" />
<input type="hidden" id="tokenValidatingErrorKey" value="${grtConfig.ewiMessageCodeMap['tokenValidatingError'] }" />

<input type="hidden" id="tokenBPNotAuthorisedToRedeemErrMsg" value="${grtConfig.tokenBPNotAuthorisedToRedeemErrMsg }" />
<input type="hidden" id="tokenBPNotAuthorisedToRedeemErrMsgKey" value="${grtConfig.ewiMessageCodeMap['tokenBPNotAuthorisedToRedeemErrMsg'] }" />
<input type="hidden" id="tokenContractNotReplacableErrMsg" value="${grtConfig.tokenContractNotReplacableErrMsg }" />
<input type="hidden" id="tokenContractNotReplacableErrMsgKey" value="${grtConfig.ewiMessageCodeMap['tokenContractNotReplacableErrMsg'] }" />
<input type="hidden" id="tokenExcpFindingContractsErrMsg" value="${grtConfig.tokenExcpFindingContractsErrMsg }" />
<input type="hidden" id="tokenExcpFindingContractsErrMsgKey" value="${grtConfig.ewiMessageCodeMap['tokenExcpFindingContractsErrMsg'] }" />
<input type="hidden" id="tokenNoContractsFoundErrMsg" value="${grtConfig.tokenNoContractsFoundErrMsg }" />
<input type="hidden" id="tokenNoContractsFoundErrMsgKey" value="${grtConfig.ewiMessageCodeMap['tokenNoContractsFoundErrMsg'] }" />
<input type="hidden" id="tokenContractReplaceNoConfirmMsg" value="${grtConfig.tokenContractReplaceNoConfirmMsg }" />
<input type="hidden" id="tokenContractReplaceNoConfirmMsgKey" value="${grtConfig.ewiMessageCodeMap['tokenContractReplaceNoConfirmMsg'] }" />