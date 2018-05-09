<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/includes/context.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/views/eqm-process.css" />"/>
<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/plugins/jquery.dataTables_themeroller.css" />"/>
<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/plugins/jquery-ui.css" />"/>

<script type="text/javascript" src="<c:url context="${ _path }"  value="/scripts/plugins/jquery.dataTables.min.js" />"></script>
<script type="text/javascript" src="<c:url context="${ _path }"  value="/scripts/plugins/jquery.dataTables.columnFilter.js" />"></script>
<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/plugins/jquery.datatable.extplugin.js" />"></script>
<script type="text/javascript" src="<c:url context="${ _path }"  value="/scripts/equipmentmove/grt.eqmprocess.js" />"></script>

<style>

.ui-datatable-hor-scroll .ui-datatable-tablewrapper,.scrolling-div .ui-datatable-tablewrapper{
     overflow: auto;
     width: 100%;
     padding-bottom: 5px;}
</style>
	
<script type="text/javascript">
	$( document ).ready(function() {		
		//Adjust column heading on load
		$("#eqmContentTable").dataTable().fnAdjustColumnSizing();
	});
</script>

<!-- start page content-wrap -->
<div class="content-wrap eqm-process">

	<h1 class="page-title">Equipment/Site Move</h1>

	<!-- start page content -->
	<div class="content">
		<div id="equipmentMoveSaveMessage" class="page-note">
			<p>
				<span>${grtConfig.equipmentMoveSaveMessage}</span> <span> ${actionForm.bannerSubmittedDate} </span> <span>${grtConfig.equipmentMoveSaveMessage2}</span>
			</p>
		</div>

		<%@ include file="../installbase/grtBanner.jsp"%>
		
		<!-- start page data loader for defect#100-->
		<div id="loading-overlay-eq-move" class="loading-overlay" style="display: block;">
			<p id="loading-msg" class="loading-msg">Please wait while we fetch your data...</p>
			<img
				src="<c:url context="${ _path }" value="/styles/images/loaders/loader.gif" />"
				alt="loading" />
		</div>
		<!-- end page data loader -->


		<!-- start existing install base -->
		<div id="equip-table-wrap" class="data-table-wrap collapse-box">
			<h2 class="collapse-box-header active">Equipment-Site Data
				<a href="#">
					<span></span>
				</a>
			</h2>
			<div id="" class="data collapse-box-container">

				<!-- start eqmProcessForm -->
				<s:form id="eqmProcessForm" enctype="multipart/form-data" method="post">
					<s:token></s:token>
					<input type="hidden"  id="registrationId" name="actionForm.registrationId" value="${ fn:escapeXml(actionForm.registrationId) }"/>
					<input type="hidden"  id="soldToId" name="actionForm.soldToId" value="${ fn:escapeXml(actionForm.soldToId) }"/>
					<input type="hidden"  id="salMigrationOnly" name="actionForm.salMigrationOnly"  value="${ actionForm.salMigrationOnly }"/>
					<input type="hidden"  id="technicalOrderId" name="actionForm.technicalOrderId"  value="${ actionForm.technicalOrderId }"/>
					<%-- <input type="hidden"  id="materialsAfterExclusionList" name="actionForm.materialsAfterExclusionList"  value="${ actionForm.materialsAfterExclusionList }"/> --%>
					<input type="hidden"  id="saveType" name="actionForm.saveType"  value="${ actionForm.saveType }"/>
					<input type="hidden"  id="validateEnabled" value=""/>
					<input type="hidden"  id="eqmMessage" name="actionForm.message"  value="${ actionForm.message }"/>
					<input type="hidden"  id="eqmReturnCode" name="actionForm.returnCode"  value="${ actionForm.returnCode }"/>
					<a id="registrationListAction" href="<c:url context="${ _path }" value="/technicalregsitration/registrationList.action" />" onclick="anchor_submit_form('viewInstallBaseForm','<c:url context="${ _path }" value="/viewinstallbase/registrationList.action" />');return false;"></a>
					<c:set var="downloadEQMFile" value="${actionForm.eqrMoveFileDownloaded}"/>

					<%
					    boolean download = ((Boolean)pageContext.getAttribute("downloadEQMFile")).booleanValue();
						if (download) {
					%>
						<script>
							window.open('<%=request.getContextPath()%>/files/equipment_move_${actionForm.registrationId}.xls','download', 'height=100, width=100, scrollbars=no');
						</script>
					<%
						}
					%>

					<table id="eqmContentTable" style="overflow-y:hidden;" border="0" cellpadding="5" align="center">
						<thead>
							<tr>
								<th width="5%">Select</th>
								<th width="6%">Existing Qty&nbsp;</th>
								<%-- <th width="6%">Revised Installed Quantity&nbsp;<span id="remainingQuantitySort"></span></th> --%>
								<th width="7%">Qty to Move&nbsp;</th>
								<th width="7%">TOB'ed?&nbsp;</th>
								<th width="10%">SEID&nbsp;</th>
								<th width="6%">SE Code&nbsp;</th>
								<th width="8%">Material Code&nbsp;</th>
								<th width="18%">Material Code Description&nbsp;</th>
								<th width="12%">Product Line&nbsp;</th>
								<th width="7%">Contract?&nbsp;</th>
								<th width="10%">Serial Number&nbsp;</th>
								<th width="8%" class="erroWrap"> Warning&nbsp;</th>
							</tr>
						</thead>
						<tbody>
						    <c:forEach items="${actionForm.materialEntryList}" var="item" varStatus="container">
								<tr title="Pipeline IB Quantity:${item.pipelineIBQuantity} Pipeline EQM Quantity:${item.pipelineEQRMoveQuantity}">
										<td align="center">
											<input type="checkbox" id="deleted${container.index}" name="actionForm.materialEntryList[${container.index}].deleted" ${(item.deleted) ? 'checked' : ''} value="true" onClick="javascript:clearValueOnUncheck(this, ${container.index});" ${(item.exclusionFlag) or (not empty item.errorDescription) or (item.solutionElementCode eq 'VSALGW') ? 'disabled' : ''}/>
										   	<input type="hidden" name="actionForm.materialEntryList[${container.index}].deleted" value="false"/>
										   	<input type="hidden" id="orderId${container.index}" value="${ item.orderId }"/>
										</td>
										<td align="center">${item.initialQuantity}
											<input type="hidden" id="availableQuantity${container.index}" name="actionForm.materialEntryList[${container.index}].initialQuantity" value="${item.initialQuantity }"/>
											<input type="hidden" id="remainingQuantity${container.index}" name="actionForm.materialEntryList[${container.index}].remainingQuantity" value="${item.remainingQuantity}"/>
										</td>

										<td align="center">
											<input type="text" size="5" id="movedQuantity${container.index}" name="actionForm.materialEntryList[${container.index}].removedQuantity" value="${item.removedQuantity}" onchange ="setEquipmentMoveValueInLabel(${container.index+varIndex},this.value,'Quantity')" onBlur="return changeValues(${container.index});" onKeyUp="javascript:numericFilter(this);" ${(item.exclusionFlag) or (not empty item.errorDescription) ? 'disabled' : ''}/>
											<label id="lblmovedQuantity${container.index}" class="hiddenLable">${item.removedQuantity}</label>
										</td>
										
										<td align="center">${item.technicallyRegisterable}
											<%-- <input type="hidden" id="techReg${container.index}" name="actionForm.materialEntryList[${container.index}].technicallyRegisterable" value="${item.technicallyRegisterable}"> --%>
										</td>
										<td align="left">${item.solutionElementId}
											<!--input type="hidden" id="seId${container.index}" name="actionForm.materialEntryList[${container.index}].solutionElementId" value="${item.solutionElementId}"/-->
										</td>
										<td align="left">${item.solutionElementCode}
										</td>
										<td align="left">${item.materialCode}
										</td>
										<td align="left" title="${item.description}">${item.description}
											<!--input type="hidden" id="description${container.index}" name="actionForm.materialEntryList[${container.index}].description" value="${item.description}"/-->
										</td>
										<td align="left">${item.productLine}
										</td>
										<td align="center">${item.activeContractExist}
											<input type="hidden" id="activeContract${container.index}" name="actionForm.materialEntryList[${container.index}].activeContractExist" value="${item.activeContractExist}"/>
										</td>
										<td align="left">${item.serialNumber}
													
										</td>
										<td align="left" class="erroWrap">
										
										<c:choose>
										    <c:when test="${item.excludedMaestroOrNortel}">
										       <a target="_blank" style="color: #FFC600; text-decoration: none;" href='https://support.avaya.com/ext/index?page=content&id=FAQ107254'>		
													${item.errorDescription}  For more information, click here.
												</a>
										    </c:when>    
										    <c:otherwise>
										        ${item.errorDescription}
										    </c:otherwise>
										</c:choose>										
										
										<i id="errorExclamation${container.index}" style="display:none" class="fa fa-exclamation-triangle"></i> <span id="errorDescription${container.index}"></span>
											<input type="hidden" id="existingErrorDescription${container.index}" name="actionForm.materialEntryList[${container.index}].errorDescription" value="${item.errorDescription}"/>
										</td>
									</tr>
							</c:forEach>
						<tbody>
					</table>

					<div class="select-remove-wrap">
						<input type="checkbox" id="selectAll" value="actionForm.selectAndUnselectAllMeterial" onClick="javascript:selectNunselect(this);" ${actionForm.readOnly ? 'disabled':''}/>
						<span>Select/Unselect All for Move</span>
					</div>
					
					
					<!-- Error Messages -->
										
					<input type="hidden" id="eqmAssignedQtyErrMsg" value="${grtConfig.eqmAssignedQtyErrMsg}" />	
					<input type="hidden" id="eqmSeletOneEqpErrMsg1" value="${grtConfig.eqmSeletOneEqpErrMsg1}" />	
					<input type="hidden" id="eqmSeletOneEqpErrMsg2" value="${grtConfig.eqmSeletOneEqpErrMsg2}" />	
					<input type="hidden" id="eqmActiveContrsctSelErrMsg" value="${grtConfig.eqmActiveContrsctSelErrMsg}" />	
					<input type="hidden" id="eqmRequestSuccessMsg" value="${ fn:escapeXml(grtConfig.eqmRequestSuccessMsg)}" />	
					<input type="hidden" id="eqmOutageMsg" value="${grtConfig.eqmOutageMsg}" />	
					<input type="hidden" id="eqmCancelToHomePageErrMsg" value="${grtConfig.eqmCancelToHomePageErrMsg}" />	
					<input type="hidden" id="eqmCancelToRegPageErrMsg" value="${grtConfig.eqmCancelToRegPageErrMsg}" />	
					<input type="hidden" id="eqmReqSubmitConfMsg" value="${grtConfig.eqmReqSubmitConfMsg}" />	
					<input type="hidden" id="eqmQtyLessThanZeroErrMsg" value="${grtConfig.eqmQtyLessThanZeroErrMsg}" />	
					<input type="hidden" id="eqmQtyGreaterThanExisErrMsg" value="${grtConfig.eqmQtyGreaterThanExisErrMsg}" />	
					<input type="hidden" id="eqmZeroQtyErrMsg" value="${grtConfig.eqmZeroQtyErrMsg}" />	
					<input type="hidden" id="eqmSaveSuccMsg" value="${grtConfig.eqmSaveSuccMsg}" />	
					
					<!--  End Error Messages -->
					
					
					<!--  Error Codes -->
										
					<input type="hidden" id="eqmAssignedQtyErrMsgCode" value="${grtConfig.ewiMessageCodeMap['eqmAssignedQtyErrMsg']}" />	
					<input type="hidden" id="eqmSeletOneEqpErrMsg1Code" value="${grtConfig.ewiMessageCodeMap['eqmSeletOneEqpErrMsg1']}" />	
					<input type="hidden" id="eqmSeletOneEqpErrMsg2Code" value="${grtConfig.ewiMessageCodeMap['eqmSeletOneEqpErrMsg2']}" />	
					<input type="hidden" id="eqmActiveContrsctSelErrMsgCode" value="${grtConfig.ewiMessageCodeMap['eqmActiveContrsctSelErrMsg']}" />	
					<input type="hidden" id="eqmRequestSuccessMsgCode" value="${grtConfig.ewiMessageCodeMap['eqmRequestSuccessMsg']}" />	
					<input type="hidden" id="eqmOutageMsgCode" value="${grtConfig.ewiMessageCodeMap['eqmOutageMsg']}" />	
					<input type="hidden" id="eqmCancelToHomePageErrMsgCode" value="${grtConfig.ewiMessageCodeMap['eqmCancelToHomePageErrMsg']}" />	
					<input type="hidden" id="eqmCancelToRegPageErrMsgCode" value="${grtConfig.ewiMessageCodeMap['eqmCancelToRegPageErrMsg']}" />	
					<input type="hidden" id="eqmReqSubmitConfMsgCode" value="${grtConfig.ewiMessageCodeMap['eqmReqSubmitConfMsg']}" />	
					<input type="hidden" id="eqmQtyLessThanZeroErrMsgCode" value="${grtConfig.ewiMessageCodeMap['eqmQtyLessThanZeroErrMsg']}" />	
					<input type="hidden" id="eqmQtyGreaterThanExisErrMsgCode" value="${grtConfig.ewiMessageCodeMap['eqmQtyGreaterThanExisErrMsg']}" />	
					<input type="hidden" id="eqmZeroQtyErrMsgCode" value="${grtConfig.ewiMessageCodeMap['eqmZeroQtyErrMsg']}" />	
					<input type="hidden" id="eqmSaveSuccMsgCode" value="${grtConfig.ewiMessageCodeMap['eqmSaveSuccMsg']}" />	
					
					<!-- End Error Codes -->

				</s:form>
				<!-- end eqmProcessForm -->

			</div>
		</div>
		<!-- end existing install base -->

		<!-- start controls -->
		<div class="controls">
			<%
			String eqrStatus = (String)request.getSession().getAttribute("EQR_MOVEStatus");
			System.out.println("EquipmentRemovalProcess JSP --> EQRStatus:" + eqrStatus);
			if( eqrStatus!=null && (eqrStatus.equalsIgnoreCase("inprocess") || eqrStatus.equalsIgnoreCase("cancelled"))){
			%>
				<input type="button" value="Cancel" class="button gray" disabled ="disabled" />
			<%
			} else if(eqrStatus != null && eqrStatus.equalsIgnoreCase("saved")){
			%>
				<input type="button" value="Cancel" class="button gray" id="backToHomePage" onClick="return cancelConfirmation();"/>
			<%
			} else {
			%>
				<input type="button" value="Cancel" class="button gray" id="backToHomePage" onClick="return cancelConfirmation();"/>
			<%
			}
			request.getSession().removeAttribute("EQR_MOVEStatus");
			%>
			<% if(request.getSession().getAttribute("eqrMoveFlag") != null && request.getSession().getAttribute("eqrMoveFlag").equals("0") ) { %>
			<input type="button" value="Back" class="button gray"  id="backToLocationPage" onclick="backToRegistrationList()" />
			<% } else { %>
			<input type="button" value="Back" class="button gray" id="backToLocationPage" onclick="backToLocation()" />
			<% } %>
			<%-- <input type="button" value="Cancel" class="button gray" id="backToHomePage" onClick="return cancelConfirmation();"/> --%>
			<input type="button" value="Save" class="button gray" id="saveEQM" onClick="return updateValues('save');" ${(actionForm.readOnly) ? 'disabled' : ''}/>
			<input type="button" value="Export" class="button gray" id="exportEQM" onClick="return exportValidatedRecord();"/>
			<input type="button" value="Submit" class="button gray" id="submitEQM" onClick="return updateValues('submit');" ${(actionForm.readOnly) ? 'disabled' : ''}/>

			<a id="backToHomeLocationAction" href="<c:url context="${ _path }" value="/home/home-action.action"/>"></a>
			<a id="saveEQMProcessAction" href="<c:url context="${ _path }" value="/equipmentmove/saveEquipmentMoveProcess.action"/>"></a>
			<a id="exportEQMProcessAction" href="<c:url context="${ _path }" value="/equipmentmove/exportEquipmentMoveProcess.action"/>"></a>
			<a id="backToRegistrationList" href="<c:url context="${ _path }" value="/technicalonboarding/backFromTRDetails" />"></a>
			<%-- <a id="backToLocationAction" href="<c:url context="${ _path }" value="/recordvalidation/recordValidationOnly.action"/>"></a> --%>
			<a id="backToLocationAction" href="<c:url context="${ _path }" value="/equipmentmove/equipmentMoveOnly.action"/>"></a>
			<a id="newRegistrationWithSoldToValidationForAgentAction" href="<c:url context="${ _path }" value="/technicalregsitration/newRegistrationWithSoldToValidationForAgent" />"></a>
			<a id="newRegistrationWithSoldToValidation" href="<c:url context="${ _path}" value="/technicalregsitration/newRegistrationWithSoldToValidation.action"/>"></a>
		
		</div>
		<!-- end controls -->

	</div>
	<!-- end page content -->
</div>
<!-- end page content-wrap -->
<input type="hidden" id="eqmStatusShow" value="<%=eqrStatus%>" />
<script>
jQuery(document).ready(function($) {
$("#equipmentMoveSaveMessage").hide();
var status="<%=eqrStatus%>";
if(status=="saved"){$("#equipmentMoveSaveMessage").show();}
	// cannot wrap table with div tag due to events
	// so using JS to wrap on load to allow scrolling horizontally
	$('.select-remove-wrap').appendTo('.dataTables_scroll');

});
</script>

<%-- ************************************************************* --%>
<%-- ***** BELOW IS DEPRECATED CODE - NEED REVIEW AND REMOVE ***** --%>
<%-- ************************************************************* --%>
<%-- <%
String eqrStatus = (String)request.getSession().getAttribute("EQRStatus");
System.out.println("EquipmentRemovalProcess JSP --> EQRStatus:" + eqrStatus);
if( eqrStatus!=null && (eqrStatus.equalsIgnoreCase("inprocess") || eqrStatus.equalsIgnoreCase("cancelled"))){
%>
	<input type="button" value="Cancel" class="osm_large_button" disabled ="true"/>
<%
} else if(eqrStatus != null && eqrStatus.equalsIgnoreCase("saved")){
%>
	<input type="button" value="Cancel" class="osm_large_button" id="backToHomePage" onClick="return cancelConfirm();"/>
<%
} else {
%>
	<input type="button" value="Cancel" class="osm_large_button" id="backToHomePage" onClick="return cancelConfirmation();"/>
<%
}
request.getSession().removeAttribute("EQRStatus");
%>
<% if(request.getSession().getAttribute("eqrFlag") != null && request.getSession().getAttribute("eqrFlag").equals("0") ) { %>
<input type="button" value="Back" class="osm_large_button"  id="backToLocationPage" />
<% } else { %>
<input type="button" value="Back" class="osm_large_button" id="backToLocationPage" />
<% } %> --%>

<%-- <td title="Enter Revised Installed Quantity" align="center">
	<input type="text" id="remainingQuantity${container.index}" name="actionForm.materialEntryList[${container.index}].remainingQuantity" value="${item.remainingQuantity}" size="5" onBlur="return changeValues(${container.index});" onKeyUp="javascript:numericFilter(this);" ${(item.exclusionFlag) or (item.errorDescription) ? 'disabled' : ''}/>
</td> --%>
