<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.grt.dto.TechnicalOrderDetail"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.grt.dto.RegistrationFormBean" %>
<%@ page import="java.util.List"%>
<%@ page import="com.grt.dto.Pager"%>
<%@ include file="/includes/context.jsp" %>

<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/plugins/jquery.dataTables_themeroller.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/views/sal-gateway-details.css" />"/>

<script src='<c:url context="${ _path }" value="/scripts/plugins/jquery_maskedinput.js" />'></script>
<%-- <script src='<c:url context="${ _path }" value="/scripts/salmigration/grt.salmigration.js" />'></script> --%>
<script src='<c:url context="${ _path }" value="/scripts/salmigration/grt.salmigrationdetails.js" />'></script>
<script type="text/javascript"
src="<c:url context="${ _path }" value="/scripts/plugins/jquery.dataTables.min.js" />"></script>
<script type="text/javascript"
src="<c:url context="${ _path }" value="/scripts/plugins/jquery.dataTables.columnFilter.js" />"></script>
<%-- <script type="text/javascript" src='<%=request.getContextPath()+"/dwr/engine.js"%>' /></script>
<script type="text/javascript" src='<%=request.getContextPath()+"/dwr/util.js"%>' /></script>
<script src='dwr/interface/pageflowAccess.js?jpfScopeID=<%=jpfScopeID %>'></script>
<script type="text/javascript" src='<%=request.getContextPath()+ "/js/extremecomponents.js"%>' /></script>
<script type="text/javascript" src='<%=request.getContextPath()+ "/js/cookie.js"%>' /></script>
<script type="text/javascript" src='<%=request.getContextPath()+"/js/CrossBrowserCookieManager/jquery.js"%>'></script>
<script type="text/javascript" src='<%=request.getContextPath()+"/js/CrossBrowserCookieManager/swfobject.js"%>'></script>
<script type="text/javascript" src='<%=request.getContextPath()+"/js/CrossBrowserCookieManager/CrossBrowserCookieManager.js"%>'></script>
<script type="text/javascript" src='<%=request.getContextPath()+"/js/dynamicTable.js"%>'></script>
<script type="text/javascript" src='<%=request.getContextPath()+"/js/srSummaryPopup.js"%>'></script>
<script type="text/javascript" src='<%=request.getContextPath()+"/js/clientValidation.js"%>'></script>
<script type="text/javascript" src='<%=request.getContextPath()+"/dwr/interface/Assembler.js" %>'></script> --%>

<script type="text/javascript">

	/*function validateAssets(){

		alert("validateAssets called");
		var size = document.getElementById("sizeOfAssetsList");
		/////////////
		for (i = 0; i < size; i++ ) {

		}
		/////////////
	 	var hasChecked = false;

		var assets = document.getElementsByName("assets");
		alert("assets Length ===>"+ assets.length);
	    for (var i = 0; i < assets.length; i++) {
             if (assets[i].checked) {
            	hasChecked = true;
            	break;
             }
        }

	    if (hasChecked == false) {
	        alert("Please select at least one Asset.");
	        return false;
	    }

	    return true;
	}*/

	function setSortChange(screen, field, count){
		//alert('screen:'+screen+', field:'+field+', count:'+count);
		if(screen == 'SGMDASGL' || screen == 'SGMDERAL'){
			if(count == 1){
				document.getElementById(field+screen+'Sort').innerHTML = '<netui:image src="/grtWebProject/framework/skins/ptlSales/images/arrow_up.gif"/>';
			} else {
				document.getElementById(field+screen+'Sort').innerHTML = '<netui:image src="/grtWebProject/framework/skins/ptlSales/images/arrow_down.gif"/>';
			}
		} else {
			document.getElementById(field+screen+'Sort').innerHTML = '<netui:image src="/grtWebProject/framework/skins/ptlSales/images/arrow_down.gif"/>';
		}
	}
</script>

<!-- start page content-wrap -->
<div class="content-wrap sal-migration-details">

	<h1 class="page-title">SAL Migration Details</h1>

	<!-- start page content -->
	<div class="content">

		<!-- start salGatewayMigrationDetails -->
		<form action="saveExistingRegisteredAssetsList" id="salGatewayMigrationDetails" enctype="multipart/form-data">

			<input type="hidden" name="actionForm.sortBy" value="${ actionForm.sortBy }"/>
			<input type="hidden" name="actionForm.sortByCount" value="${ actionForm.sortByCount }"/>
			<input type="hidden" name="actionForm.sortByScreen" value="${ actionForm.sortByScreen }"/>
			<input type="hidden" value="${ grtConfig.selectAsset}" id="selectAsset" />			
			<input type="hidden" value="${ grtConfig.emptySTSEID}" id="emptySTSEID" />
			<input type="hidden" value="${ grtConfig.salEndToEndRegConfirmMsg}" id="salEndToEndRegConfirmMsg" />
			<input type="hidden" value="${ grtConfig.validSTSEID}" id="validSTSEID" />
			<input type="hidden" value="${ grtConfig.salConfirmAssetMigrationMsg}" id="salConfirmAssetMigrationMsg" />
			
			<!-- Error Codes -->
			<input type="hidden" value="${grtConfig.ewiMessageCodeMap['selectAsset']}" id="selectAssetCode" />			
			<input type="hidden" value="${grtConfig.ewiMessageCodeMap['emptySTSEID']}" id="emptySTSEIDCode" />
			<input type="hidden" value="${grtConfig.ewiMessageCodeMap['salEndToEndRegConfirmMsg']}" id="salEndToEndRegConfirmMsgCode" />
			<input type="hidden" value="${grtConfig.ewiMessageCodeMap['validSTSEID']}" id="validSTSEIDCode" />
			<input type="hidden" value="${grtConfig.ewiMessageCodeMap['salConfirmAssetMigrationMsg']}" id="salConfirmAssetMigrationMsgCode" />
			
			<!-- start active-sal-wrap -->
			<div id="active-sal-wrap" class="data-table-wrap collapse-box">
				<h2 class="collapse-box-header active">Active SAL Gateway List
					<a href="#">
						<span></span>
					</a>
				</h2>
				<div id="" class="data collapse-box-container" style="display: block;">
					<div id="asgwList">

						<table border="0" width="100%" id="salMigrationDetailsTable" class="basic-table">
						<thead>
							<tr>
								<th width="25" >Material Code&nbsp;<span id="materialCodeSGMDASGLSort"></span></th>
								<th width="50" >Material Code Description&nbsp;<span id="materialDescriptionSGMDASGLSort"></span></th>
								<th width="25" >SE Code&nbsp;<span id="seCodeSGMDASGLSort"></span></th>
								<th width="25" >SEID&nbsp;<span id="seidSGMDASGLSort"></span></th>
								<th width="25" >Sold To/FL of SALGW&nbsp;<span id="soldToSGMDASGLSort"></span></th>
								<th width="25" >Last Updated&nbsp;<span id="gatewayCreatedDateSGMDASGLSort"></span></th>
								<th width="25" ># of Connected Devices&nbsp;<span id="devicesBehindGatewaySGMDASGLSort"></span></th>
								<th width="25" >Primary / Secondary&nbsp;<span id="primaryOrSecondarySGMDASGLSort"></span></th>
							</tr>
							</thead>
							<tbody>
							<c:forEach items="${ actionForm.activeSalGatewayList }" var="item" varStatus="container">
								<tr class="osm_table05_td">
											<td width="25">${item.materialCode}</td>
											<td width="50">${item.materialDescription}</td>
											<td width="25">${item.seCode}</td>
											<td width="25"><a href="#" onclick="showSALGatewayDetails('${item.seid}', '${item.soldTo}')"  >${item.seid}</td>
											<td width="25">${item.soldTo}</td>
											<td width="25">${item.gatewayCreatedDate}</td>
											<td width="25">${item.devicesBehindGateway}</td>
											<td width="25">${item.primaryOrSecondary}</td>
								</tr>
							</c:forEach>

							</tbody>
						</table>
					</div>
				</div>
			</div>
			<!-- end active-sal-wrap -->

			<!-- start assets-wrap -->
			<div id="assets-wrap" class="data-table-wrap collapse-box">
				<h2 class="collapse-box-header active">Existing Registered Assets List
					<a href="#">
						<span></span>
					</a>
				</h2>
				<div id="" class="data collapse-box-container" style="display: block;">
					<table border="0" width="100%"  id="existingRegisteredAssetsListTable">
					 <thead>
						<tr>
							<th  width="10%" >Material Code&nbsp;<span id="materialCodeSGMDERALSort"></span></th>
							<th  width="20%" >Material Code Description&nbsp;<span id="materialCodeDescriptionSGMDERALSort"></span></th>
							<th  width="20%" >Product Line&nbsp;<span id="productLineSGMDERALSort"></span></th>
							<th  width="10%" >SE Code&nbsp;<span id="seCodeSGMDERALSort"></span></th>
							<th  width="10%" >SEID&nbsp;<span id="solutionElementIdSGMDERALSort"></span></th>
							<th  width="10%" >SID&nbsp;<span id="sidSGMDERALSort"></span></th>
							<th  width="10%" >MID&nbsp;<span id="midSGMDERALSort"></span></th>
							<th  width="10%" >Select&nbsp;<span id="selectedSGMDERALSort"></span></th>
						</tr>
						</thead>
						<tbody>
							<c:forEach items="${ actionForm.existingRegisteredAssetsList }" var="item" varStatus="container">
							<tr>
								<td width="10%" align="center" >${item.materialCode}</td>
								<td width="20%" align="center" >${item.materialCodeDescription}</td>
								<td width="20%" align="center" >${item.productLine}</td>
								<td width="10%" align="center" >${item.seCode}</td>
								<td width="10%" align="center" >${item.solutionElementId}</td>
								<td width="10%" align="center" >${item.sid}</td>
								<td width="10%" align="center" >${item.mid}</td>
								<td  width="9%" align="center">
									<input type="checkbox" name="actionForm.existingRegisteredAssetsList[${container.index}].selected" id="existingRegisteredAsset${container.index}Select" value="true"/>
									<input type="hidden" name="actionForm.existingRegisteredAssetsList[${container.index}].selected" value="false"/>
								</td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<!-- end assets-wrap -->

			<!-- start controls -->
			<div class="controls">
				<input type="button" onclick="backFromSALMigration();" value="Back" class="button gray" />
				<a id="backFromSALMigrationDetails" href="<c:url context="${ _path }" value="/salmigration/backFromSALMigrationDetails.action" />" ></a>
				<input type="button" onclick="cancelFromSALMigration();" value="Cancel" class="button gray" />
				<a id="cancelSALGatewayMigrationList" href="<c:url context="${ _path }" value="/salmigration/cancelSALMigrationDetails.action" />" ></a>
				<input type="button" value="Submit" class="button gray"	onclick="return validateAssets();"/>
				<a id="submitSALGatewayMigrationDetails" href="<c:url context="${ _path }" value="/salmigration/saveExistingRegisteredAssetsList.action" />" ></a>
			</div>
			<!-- end controls -->

		</form>
		<!-- end salGatewayMigrationDetails -->

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

<!-- <netui:anchor action="dynamicSortByField" formSubmit="true" tagId="dynamicSortByFieldAction" /> -->


<%-- <td class="osm_text">${container.item.alarmId}</td>
<td class="osm_text">${container.item.primarySALGatewaySEID}</td>
<td class="osm_text">${container.item.secondarySALGatewaySEID}</td> --%>

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
<%-- <netui:checkBox  tagId="existingRegisteredAsset${container.index}Select" disabled="false" dataSource="container.item.selected" /> --%>

<%-- <netui:button value="${bundle.msg.buttonBack}" styleClass="osm_large_button" action="backFromSALMigrationDetails" />&nbsp;
<netui:button value="${bundle.msg.buttonCancel}" styleClass="osm_large_button" action="cancelSALMigrationDetails" />&nbsp;
<netui:button value="${bundle.msg.buttonSubmit}" styleClass="osm_large_button" action="saveExistingRegisteredAssetsList"  onclick="return validateAssets();"   /> --%>

<!-- 	<input type="hidden"  id="salGatewayMigrationList.seid"   value=""/>
	<input type="hidden"  id="salGatewayMigrationList.soldTo"  value=""/> -->
