<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.grt.dto.TechnicalOrderDetail"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.grt.dto.RegistrationFormBean" %>
<%@ page import="java.util.List"%>
<%@ page import="com.grt.dto.Pager"%>
<%@ include file="/includes/context.jsp" %>

<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/views/sal-gateway-migration.css" />"/>
<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/plugins/jquery.dataTables_themeroller.css" />" />

<script src='<c:url context="${ _path }" value="/scripts/plugins/jquery_maskedinput.js" />'></script>
<script src='<c:url context="${ _path }" value="/scripts/salmigration/grt.salmigration.js" />'></script>
<script type="text/javascript"
	src="<c:url context="${ _path }" value="/scripts/plugins/jquery.dataTables.min.js" />"></script>
<script type="text/javascript"
	src="<c:url context="${ _path }" value="/scripts/plugins/jquery.dataTables.columnFilter.js" />"></script>

<script type="text/javascript">
	jQuery(function($) {
		$.mask.definitions['~']='[+-]';
		$('#salSeIdForAddToList').mask('(999)999-9999');
 });
</script>
<script type="text/javascript">
function setSortChange(screen, field, count){
		//alert('screen:'+screen+', field:'+field+', count:'+count);
		if(screen == 'SGML'){
			if(count == 1){
				document.getElementById(field+'Sort').innerHTML = '<netui:image src="/grtWebProject/framework/skins/ptlSales/images/arrow_up.gif"/>';
			} else {
				document.getElementById(field+'Sort').innerHTML = '<netui:image src="/grtWebProject/framework/skins/ptlSales/images/arrow_down.gif"/>';
			}
		} else {
			document.getElementById(field+'Sort').innerHTML = '<netui:image src="/grtWebProject/framework/skins/ptlSales/images/arrow_down.gif"/>';
		}
	}
	
function addToListSALGW_Action(){
	if(validateInputs()){
		submitForm('addToListSALGWAction');
	}
}

function submitForm(actionName){
	var url = document.getElementById(actionName).href;
	document.getElementById('salGatewayMigrationList').action = url;
	document.getElementById('salGatewayMigrationList').submit();
}
	
</script>

<!-- start page content-wrap -->
<div class="content-wrap sal-gateway-migration">

	<h1 class="page-title">SAL Gateway Migration List</h1>

	<!-- start page content -->
	<div class="content">

		<%@ include file="../installbase/grtBanner.jsp"%>

		<!-- start salGatewayForm -->
		<form action="nextForSal" id="salGatewayMigrationList" enctype="multipart/form-data" >

			<input type="hidden" name="actionForm.primary" value="${ actionForm.primary }" id="primarySALGateway" />
			<input type="hidden" name="actionForm.secondary" value="${ actionForm.secondary }" id="secondarySALGateway" />
			<input type="hidden" name="actionForm.gatewaySeid" value="${ actionForm.gatewaySeid }" id="gatewaySeid" />
			<input type="hidden" name="actionForm.screenName" value="${ actionForm.screenName }" id="screenName" />
			<input type="hidden" name="actionForm.sortBy" value="${fn:escapeXml(actionForm.sortBy)}" id="sortBy" />
			<input type="hidden" name="actionForm.sortByCount" value="${ actionForm.sortByCount }" id="sortByCount" />
			<input type="hidden" name="actionForm.sortByScreen" value="${fn:escapeXml(actionForm.sortByScreen)}" id="sortByScreen" />
			<input type="hidden"  id="errorMessage" name="actionForm.errorMessage"  value="${ actionForm.errorMessage }"/>
			<input type="hidden"  id="count" name="actionForm.count"  value="${ actionForm.count }"/>
			<input type="hidden"  id="salGatewayMigrationList.seid"   value=""/>	
			<input type="hidden"  id="salGatewayMigrationList.soldTo"  value=""/>	
			
			<input type="hidden" value="${ grtConfig.salRequired}" id="salRequired" />
			<input type="hidden" value="${ grtConfig.duplicateGateway}" id="duplicateGateway" />
			<input type="hidden" value="${ grtConfig.emptySTSEID}" id="emptySTSEID" />
			<input type="hidden" value="${ grtConfig.invalidST}" id="invalidST" />
			<input type="hidden" value="${ grtConfig.invalidSEID}" id="invalidSEID" />
			<input type="hidden" value="${ grtConfig.validSTSEID}" id="validSTSEID" />
			<input type="hidden" value="${ grtConfig.gatewayAdded}" id="gatewayAdded" />			
			<input type="hidden" value="${ grtConfig.salEndToEndRegConfirmMsg}" id="salEndToEndRegConfirmMsg" />
			
			<!-- Error Coded -->			
			<input type="hidden" value="${grtConfig.ewiMessageCodeMap['salRequired']}" id="salRequiredCode" />
			<input type="hidden" value="${grtConfig.ewiMessageCodeMap['duplicateGateway']}" id="duplicateGatewayCode" />
			<input type="hidden" value="${grtConfig.ewiMessageCodeMap['emptySTSEID']}" id="emptySTSEIDCode" />
			<input type="hidden" value="${grtConfig.ewiMessageCodeMap['invalidST']}" id="invalidSTCode" />
			<input type="hidden" value="${grtConfig.ewiMessageCodeMap['invalidSEID']}" id="invalidSEIDCode" />
			<input type="hidden" value="${grtConfig.ewiMessageCodeMap['validSTSEID']}" id="validSTSEIDCode" />
			<input type="hidden" value="${grtConfig.ewiMessageCodeMap['gatewayAdded']}" id="gatewayAddedCode" />						
			<input type="hidden" value="${grtConfig.ewiMessageCodeMap['salEndToEndRegConfirmMsg']}" id="salEndToEndRegConfirmMsgCode" />
			
			<a id="createNewSALGatewayAction" href="<c:url context="${ _path }" value="/salmigration/createNewSALGateway.action"/>"></a>
			<a id="addToListSALGWAction" href="<c:url context="${ _path }" value="/salmigration/addToList.action" />"></a>

			<!-- start addToList wrap -->
			<div id="add-sal-wrap" class="data-table-wrap collapse-box">
				<h2 class="collapse-box-header active">SAL Gateway
					<a href="#">
						<span></span>
					</a>
				</h2>
				<div id="" class="collapse-box-container">

					<%@ include file="../technicalonboarding/addToList.jsp" %>

				</div>
			</div>
			<!-- end addToList wrap -->

			<!-- start existingSALData wrapper -->
			<div id="sal-wrap" class="data-table-wrap collapse-box">
				<h2 class="collapse-box-header active">Existing SAL Gateway List
					<a href="#">
						<span></span>
					</a>
				</h2>
				<div id="" class="data collapse-box-container">

					<!-- start existingSALData -->
					<div id="existingSALData">
						<table border="0" id="salMigrationTable" class="basic-table">
							<thead>
								<tr>
									<th width="5%" style="word-wrap: break-word;" >Select Primary</th>
									<th width="5%" style="word-wrap: break-word;" >Select Secondary</th>
									<th width="10%">SEID&nbsp;<span id="seidSort"></span></th>
									<th width="10%">SE Code&nbsp;<span id="seCodeSort"></span></th>
									<th width="10%">Sold To/FL of SALGW&nbsp;<span id="soldToSort"></span></th>
									<th width="10%"># of Connected Devices&nbsp;<span id="devicesBehindGatewaySort"></span></th>
									<th width="10%">Material Code&nbsp;<span id="materialCodeSort"></span></th>
									<th width="30%">Material Code Description&nbsp;<span id="materialDescriptionSort"></span></th>
									<th width="10%">Created&nbsp;<span id="gatewayCreatedDateSort"></span></th>
								</tr>
							</thead>
							<tbody>
							<c:forEach items="${ actionForm.salGatewayMigrationList }" var="item" varStatus="container">
								<tr>
									<c:set var="currentSeid" value="${item.seid}"/>
									<%
										String seId = (String) pageContext.getAttribute("currentSeid");
										String primarySeid =	(String )request.getSession().getAttribute(com.grt.util.GRTConstants.PRIMARY_SE_ID);
										if(seId !=null && seId.equalsIgnoreCase(primarySeid)){
									%>
									<td width="5%" align="center" >
										<input name="primary" value="${item.seid}" type="radio"	checked="true"	/>
									</td>
									<%} else { %>
									<td width="5%" align="center" >
										<input name="primary" value="${item.seid}" type="radio"	 />
									</td>
									<% }
										 String secondarySeid =	(String )request.getSession().getAttribute(com.grt.util.GRTConstants.SECONDARY_SE_ID);
										 if(seId !=null && seId.equalsIgnoreCase(secondarySeid)){
									%>
									<td width="4%" align="center" ><input name="secondary" value="${item.seid}" type="radio"	checked="true"	/></td>
									<% } else { %>
									<td width="4%" align="center" ><input name="secondary" value="${item.seid}" type="radio"	/></td>
									<%} %>
									<td width="10%" align="center" ><a href="#" onclick="showSALGatewayDetails('${item.seid}', '${item.soldTo}')">${item.seid}</a></td>
									<td width="10%" align="center" >${item.seCode}</td>
									<td width="10%" align="center" >${item.soldTo}</td>
									<td width="10%" align="center" >${item.devicesBehindGateway}</td>
									<td width="10%" align="center" >${item.materialCode}</td>
									<td width="30%" align="center" >${item.materialDescription}</td>
									<td width="10%" align="center" >${item.gatewayCreatedDate}</td>
									
								</tr>
							</c:forEach>	

							<%
							request.getSession().removeAttribute(com.grt.util.GRTConstants.PRIMARY_SE_ID);
							request.getSession().removeAttribute(com.grt.util.GRTConstants.SECONDARY_SE_ID);
							%>
							</tbody>
						</table>
					</div>
					<!-- end existingSALData -->

					<table width="100%" style="margin: 10px;">
						<tr align="center" class="salMigTabBtn">
							<!-- <td>No standalone SAL gateway found or do you need to create new gateway?</td> -->
							<td><p>Click CREATE NEW SAL GATEWAY below to start an End to End registration for a new SAL Gateway.</p></td>
						</tr>
						<tr align="center">
							<td align="center">
								<input type="button" id="createNewSALGateWay" class="button gray" value="Create New SAL Gateway"	onclick="return confirmNewSALGateway();" />
							</td>
						</tr>
					</table>

				</div>
			</div>
			<!-- end existingSALData wrapper -->

			<!-- start controls -->
			<div class="controls">
				<%-- <input type="button" class="button gray" value="${bundle.msg.buttonNext}" onClick="validate()" /> --%>
				<%-- <a id="backToLocationAction" href="<c:url context="${ _path }" value="/installbase/viewInstallBaseOnly.action"/>"></a> --%>
				<input type="button" onclick="backFromSALMigration();" value="Back" class="button gray" />&nbsp;
				<a id="backFromSALGatewayMigrationList" href="<c:url context="${ _path }" value="/salmigration/backFromSALGatewayMigrationList.action" />" ></a>
				
				<input type="button" onclick="cancelFromSALMigration();" value="Cancel" class="button gray" />&nbsp;
				<a id="cancelSALGatewayMigrationList" href="<c:url context="${ _path }" value="/salmigration/cancelSALMigrationDetails.action" />" ></a>
				
				<!-- <input type="button" id="backBtn" value="Back" class="button gray" /> -->
				<!-- <input type="button" id="cancelBtn"	value="Cancel" class="button gray" /> -->
				<input type="button" class="button gray" value="Next" onClick="validate()"/>
				 <a id="submitSALGatewayMigrationList" href="<c:url context="${ _path }" value="/salmigration/nextForSal.action" />" ></a>
			</div>
			<!-- end controls -->

		</form>
		<!-- end salGatewayForm -->
		


	</div>
	<!-- end page content -->
</div>
<!-- end page content-wrap -->

<!-- start modal recent-devices messages -->
<div class="modal recent-devices" style="display:none;">
	<div class="modal-overlay">
		<div class="modal-content">
			<a class="close" onclick="closeSALGatewayPopup()"><i class="fa fa-close errPopupBtn"></i></a>
			<h2 class="title">Info</h2>
			<p class="content">
				<img class="msg-icon"
					src="<c:url context="${ _path }" value="/styles/images/messages/icon_info_light_60x60.png" />"
					alt="info" />
				<span class="msg-txt"></span>
			</p>
			<div class="controls"></div>
		</div>
	</div>
</div>
<!-- end modal recent-devices messages -->

<%-- ************************************************************* --%>
<%-- ***** BELOW IS DEPRECATED CODE - NEED REVIEW AND REMOVE ***** --%>
<%-- ************************************************************* --%>

<%-- <input type="button" class="button gray" value="${bundle.msg.buttonNext}" onClick="validate()" /> --%>

			<!-- <table border="0" width="100%">
				<tr>
					<th width="10%">Sold To (FL) of SAL Gateway&nbsp;<span id="soldToSort"></span></th>
					<th width="10%">MaterialCode/Hardware&nbsp;<span id="materialCodeSort"></span></th>
					<th width="30%">Material Description&nbsp;<span id="materialDescriptionSort"></span></th>
					<th width="10%">SE Code&nbsp;<span id="seCodeSort"></span></th>
					<th width="10%">SEID&nbsp;<span id="seidSort"></span></th>
					<th width="10%">Date Created&nbsp;<span id="gatewayCreatedDateSort"></span></th>
					<th width="10%">Devices Connected&nbsp;<span id="devicesBehindGatewaySort"></span></th>
					<th width="5%" style="word-wrap: break-word;" >Select as Primary</th>
					<th width="5%" style="word-wrap: break-word;" >Select as Secondary</th>
				</tr>
			</table>
 -->
<!-- start modal alert messages -->
<%-- 	<div id="showSALPopup" class="modal alertMsg" style="display:none;">
	<div class="modal-overlay">
		<div class="modal-content">
			<h2 class="title"></h2>
			<p class="content">
				<span class="succFailTxt">We are processing your request to render Gateway details. A pop-up will appear in few seconds.</span>
			</p>
			<div class="controls">
				<a class="button gray succFailOkBtn" onclick="closeSuccFailPopup();showSALGatewayDetails('${item.seid}', '${item.soldTo}')" >OK</a>
			</div>
		</div>
	</div>
</div> --%>
<!-- end modal alert messages -->
<%-- 	<div id="showSALPopup" class="modal succFailMsg" style="display:none;">
	<div class="modal-overlay">
		<div class="modal-content">
			<h2 class="title">Alert</h2>
			<p class="content">
				<span class="succFailTxt">We are processing your request to render Gateway details. A pop-up will appear in few seconds.</span>
			</p>
			<div class="controls">
				<a class="button gray alertOkBtn"	onclick="closeSuccFailPopup();showSALGatewayDetails('${item.seid}', '${item.soldTo}')" >OK</a>
			</div>
		</div>
	</div>
</div> --%>
<%-- <input type="button" class="button gray" value="${bundle.msg.buttonNext}" onClick="validate()" /> --%>
<%-- <a id="backToLocationAction" href="<c:url context="${ _path }" value="/installbase/viewInstallBaseOnly.action"/>"></a> --%>
<!-- <input type="button" id="backBtn" value="Back" class="button gray"	/> -->
<!-- <input type="button" id="cancelBtn"	value="Cancel" class="button gray" /> -->
