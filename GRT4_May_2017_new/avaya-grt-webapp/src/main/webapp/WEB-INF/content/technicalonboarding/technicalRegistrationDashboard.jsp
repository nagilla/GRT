<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.grt.dto.TechnicalOrderDetail"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.grt.dto.RegistrationFormBean" %>
<%@ page import="java.util.List"%>
<%@ page import="com.grt.dto.Pager"%>
<%@ include file="/includes/context.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/views/tob-dashboard.css" />"/>
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.3/themes/smoothness/jquery-ui.css">
<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/plugins/jquery.dataTables_themeroller.css" />"/>

<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/plugins/dynamicTable.js" />"></script>
<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/plugins/jquery.dataTables.min.js" />"></script>
<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/plugins/jquery.dataTables.columnFilter.js" />"></script>
<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/technicalonboarding/technicalRegistrationDashboard.js" />"></script>
<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/technicalonboarding/grt.tob.dashboard.view.js" />"></script>

<!-- start page content-wrap -->
<div class="content-wrap tob-dashboard">

	<h1 class="page-title">Technical On-Boarding Dashboard</h1>

	<!-- start page content -->
	<div class="content">

		<!-- start registration summary -->
		<div style="display:none">
			<span id="soldTo">${actionForm.soldToId}</span>
			<span id="registrationId">${actionForm.registrationId}</span>
		</div>
		<%@ include file="../installbase/grtBanner.jsp" %>
		<!-- end registration summary -->


		<!-- start technicalRegistrationForm -->
		<form name="technicalRegistrationForm" id="technicalRegistrationForm" action="technicalRegistrationDashboard" method="post">

			<input type="hidden" id="materialCode" name="actionForm.materialCodeDescription" value="${ actionForm.materialCodeDescription }">
			<input type="hidden" id="seid" name="actionForm.solutionElementId" value="${actionForm.solutionElementId}">
			<input type="hidden" id="indexId" name="actionForm.index" value="${actionForm.index}">
			<input type="hidden" id="sortBy" name="actionForm.sortBy" value="${ actionForm.sortBy }">
			<input type="hidden" id="sortByCount" name="actionForm.sortByCount" value="${ actionForm.sortByCount }">
			<input type="hidden" id="sortByScreen" name="actionForm.sortByScreen" value="${fn:escapeXml(actionForm.sortByScreen ) }">
			<input type="hidden" id="popUpHiddenValue" name="actionForm.popUpHiddenValue" value="${ actionForm.popUpHiddenValue	}">
			<a id="technicalRegistrationUpdateAction" href="<c:url context="${ _path }" value="/technicalonboarding/technicalRegistrationUpdate.action" />" onclick="anchor_submit_form('technicalRegistrationForm','<c:url context="${ _path }" value="/technicalonboarding/technicalRegistrationUpdate.action" />');return false;"></a>
			<a id="technicalRegistrationConfigAction" href="<c:url context="${ _path }" value="/technicalonboarding/technicalRegistrationConfig.action" />" onclick="anchor_submit_form('technicalRegistrationForm','<c:url context="${ _path }" value="/technicalonboarding/technicalRegistrationConfig.action" />');return false;"></a>
			<a id="navigateToTRAction" href="<c:url context="${ _path }" value="/technicalonboarding/navigateToTR.action" />" onclick="anchor_submit_form('technicalRegistrationForm','<c:url context="${ _path }" value="/technicalonboarding/navigateToTR.action" />');return false;"></a>
			<a id="dynamicSortByFieldAction" href="<c:url context="${ _path }" value="/technicalonboarding/dynamicSortByField.action" />" onclick="anchor_submit_form('technicalRegistrationForm','<c:url context="${ _path }" value="/technicalonboarding/dynamicSortByField.action" />');return false;"></a>
			<input type="hidden" id="readyForTRDivFlag" name="actionForm.readyForTRDivFlag" value="${fn:escapeXml(actionForm.readyForTRDivFlag)}">
			<input type="hidden" id="tredListDivFlag" name="actionForm.tredListDivFlag" value="${fn:escapeXml( actionForm.tredListDivFlag)}">
			<input type="hidden" id="tredProdDivFlag" name="actionForm.tredProdDivFlag" value="${fn:escapeXml( actionForm.tredProdDivFlag)}">

			<input type="hidden" id="trRetestFlag" name="actionForm.trRetestDivFlag" value="">
			<a id="technicalRegistrationRetestAction" href="<c:url context="${ _path }" value="/technicalonboarding/technicalRegistrationRetest.action" />"></a>
			<a id="getTechnicalOrderDetails" href="<c:url context="${ _path }" value="/technicalonboarding/getTechnicalOrderDetails.action" />"></a>

			<!-- start modal accessTypeDialog -->
			<div class="modal" id="accessTypeDialog" style="display:none;">
				<div class="modal-overlay">
					<div class="modal-content">
						<a class="close" onclick="closeConfirmAlert()"><i class="fa fa-close errPopupBtn"></i></a>
						<h2 class="title">Attention</h2>
						<div class="content">

							<img class="msg-icon"
								src="<c:url context="${ _path }" value="/styles/images/messages/icon_warning_light_60x60.png" />"
								alt="warning" />
							<div class="msg-txt">
								<h5 id="popUpTopMsg"></h5>

								<div class="" id="AceessType" >
									<div id="popUpDropDown"></div>
									<input type="hidden" id="popUpMaterialCode" value="New" />
									<input type="hidden" id="popUpRemainingQty" value="New" />
									<input type="hidden" id="connectivity" value="New" />
									<input type="hidden" id="sslVpnConnection" value="Ok" />

								</div>
								<p id="msgTR" >${grtConfig.noConnectivityMessage}</p>
								<p id="msgSSLVPN" >SSL/VPN is not supported for 'No Connectivity'.</p>
							</div>
						</div>
						<div class="controls">
							<c:set var="trCancelStr" value="${ actionForm.trCancel }"/>
							<%
							String trCancelStr = (String)pageContext.getAttribute("trCancelStr");
							if(trCancelStr!=null && (trCancelStr.toString().equals("1"))){
							%>
							<input type="button" value="Cancel" class="button gray"	onClick="closeNoConnectivityPopup()" disabled="disabled"/>&nbsp;
							<%} else {%>
								<input type="button" value="Cancel" class="button gray"	onClick="closeNoConnectivityPopup();"/>&nbsp;
							<%}%>
							&nbsp;<input type="button" class="button gray" id="submitAccesstype" onclick="popupDropdownChange()" value="Continue "/>

						</div>
					</div>
				</div>
			</div>
				<!-- end modal accessTypeDialog -->

			<!-- start tob-ready-for-wrap -->
			<div id="tob-ready-for-wrap" class="data-table-wrap collapse-box">
				<h2 class="collapse-box-header" onclick="toggle('readyForTRDiv');">Registrable Products List - Ready for Technical On-Boarding
					<a href="#">
						<span></span>
					</a>
				</h2>
				<div id="readyForTRDivToggleText" class="data collapse-box-container">
					<div id="registrableProdTable">
						<table border="0" width="100%" id="registrableProd1">
							<thead>
								<tr>
									<th width="15%" align="center" >Connectivity</th>
									<th width="15%" align="center" >Initial Qty&nbsp;<span id="initialQtyTOBlist1Sort"></span></th>
									<th width="15%" align="center" >Remaining Qty&nbsp;<span id="remainingQtyTOBlist1Sort"></span></th>
									<th width="15%" align="center" >Material Code&nbsp;<span id="materialCodeTOBlist1Sort"></span></th>
									<th width="25%" align="center" >Material Code Description&nbsp;<span id="materialCodeDescTOBlist1Sort"></span></th>
									<th width="15%" align="center" >Product Line&nbsp;<span id="productLineTOBlist1Sort"></span></th>
									
								</tr>
							</thead>
							<tbody>

							</tbody>
						</table>
					</div>
				</div>
			</div>
			<!-- end tob-ready-for-wrap -->

			<!-- start tob-existing-assets-wrap -->
			<div id="tob-existing-assets-wrap" class="data-table-wrap collapse-box">
				<h2 class="collapse-box-header" onclick="retrieveExistingTOBRecords('tredListDiv');">Existing Registered Assets List
					<a href="#">
						<span></span>
					</a>
				</h2>
				<div id="tredListDivToggleText" class="data collapse-box-container">
					<table border="0" width="100%" id="existingTOBTable">
						<thead>
							<tr>
								<th width="10%" align="center">Update</th>
								<th width="10%" align="center">Retest</th>
								<th width="10%" align="center">SEID&nbsp;<span id="seIdTOBlist2Sort"></span></th>
								<th width="10%" align="center">SE Code&nbsp;<span id="solutionElementTOBlist2Sort"></span></th>
								<th width="10%" align="center">Material Code&nbsp;<span id="materialCodeTOBlist2Sort"></span></th>
								<th width="20%" align="center">Material Code Description&nbsp;<span id="materialCodeDescTOBlist2Sort"></span></th>
								<th width="10%" align="center">Product Line&nbsp;<span id="productLineTOBlist2Sort"></span></th>
								<th width="10%" align="center">SID&nbsp;<span id="sidTOBlist2Sort"></span></th>
								<th width="10%" align="center">MID&nbsp;<span id="midTOBlist2Sort"></span></th>
								<th width="10%" align="center">SALGW(s)&nbsp;<span id="salSeIdPrimarySecondaryTOBlist2Sort"></span></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${ actionForm.registrationSummaryListTRUpdate }" var="item" varStatus="container">
							<tr >
								<td width="10%" align="center" >
									<%
									if(trCancelStr!=null && (trCancelStr).toString().equals("1")){
									%>
									<input type="button" value="Update" class="button gray" onClick="javascript:navigateToUpdate('${item.materialCode}', '${item.seId}');" disabled="true"/>
									<!-- GRT 4.0 : Requirement for Retest Button -->
									<input type="button" value="ReTest" class="button gray" onClick="javascript:navigateToRetest('${item.materialCode}', '${item.seId}');" disabled="true"/>
									<%} else {%>
									<input type="button" title="${item.updateButtonTitle}" value="Update" class="button gray" onClick="javascript:navigateToUpdate('${item.materialCode}', '${item.seId}');" ${item.disableUpdateFlag ? 'disabled' : ''}/>
									<!-- GRT 4.0 : Requirement for Retest Button -->
									<input type="button" value="ReTest" class="button gray" onClick="javascript:navigateToRetest('${item.materialCode}', '${item.seId}');" ${item.disableRetestFlag ? 'disabled' : ''}/>
									<%}%>
								</td>
								<td width="10%" align="center" >${item.seId}</td>
								<td width="10%" align="center" >${item.solutionElement}</td>
								<td width="10%" align="center" >${item.materialCode}</td>
								<td width="20%" align="center" >${item.materialCodeDescription}</td>
								<td width="10%" align="center" >${item.productLine}</td>
								<td width="10%" align="center" >${item.sid}</td>
								<td width="10%" align="center" >${item.mid}</td>
								<td width="10%" align="center" >${item.salSeIdPrimarySecondary}</td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<!-- end tob-existing-assets-wrap -->


			<!-- start tob-ready-process-wrap -->
			<div id="tob-ready-process-wrap" class="data-table-wrap collapse-box">
				<c:if test="${ not empty actionForm.registrationSummaryListRegistrable }">
					<input type="hidden" id="readyToProcessFlag" value="true"/>
				</c:if>
				<h2 class="collapse-box-header" onclick="toggle('tredProdDiv');">Registrable Products -	Ready to Process
					<a href="#">
						<span></span>
					</a>
				</h2>
				<div id="tredProdDivToggleText" class="data collapse-box-container">
					<table border="0" width="100%" id="readyToProcessTable">
						<thead>
							<tr>
								<th width="10%" align="center">Select&nbsp;<span id="processTOBlist3Sort"></span></th>
								<th width="10%" align="center">Action&nbsp;<span id="actionTOBlist3Sort"></span></th>
								<th width="10%" align="center">Material Code&nbsp;<span id="materialCodeTOBlist3Sort"></span></th>
								<th width="30%" align="center">Material Code Description&nbsp;<span id="materialCodeDescTOBlist3Sort"></span></th>
								<th width="10%" align="center">Product Line&nbsp;<span id="productLineTOBlist3Sort"></span></th>
								<th width="10%" align="center">SE Code Description&nbsp;<span id="solutionElementDescTOBlist3Sort"></span></th>
								<th width="10%" align="center">Access Type&nbsp;<span id="accessTypeTOBlist3Sort"></span></th>
								<th width="10%" align="center">Product Template&nbsp;<span id="productTemplateDescTOBlist3Sort"></span></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${ actionForm.registrationSummaryListRegistrable }" var="item" varStatus="container">
							<tr >
								<td width="10%" align="center" >
								<input type="checkbox" name="actionForm.registrationSummaryListRegistrable[${container.index}].process" id="process${container.index}" value="${ item.process }" ${ item.process ? 'checked' : '' } />
								<input type="hidden" name="actionForm.registrationSummaryListRegistrable[${container.index}].process" value="false"/>
								</td>
								<td width="10%" align="center" >${item.action}</td>
								<td width="10%" align="center" ><a href="#" onclick="navigateToTRConfigUpdate(${container.index});">${item.materialCode}</a></td>
								<td width="30%" align="center" >${item.materialCodeDescription}</td>
								<td width="10%" align="center" >${item.productLine}</td>
								<td width="10%" align="center" >${item.solutionElementDesc}</td>
								<td width="10%" align="center" >${item.accessTypes}</td>
								<td width="10%" align="center" >${item.productTemplateDesc}</td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
					<div class="no-data-found">No data available in table</div>
				</div>
			</div>
			<!-- end tob-ready-process-wrap -->


			<!-- start tob-retest-wrap -->
			<div id="tob-retest-wrap" class="data-table-wrap collapse-box">
				<c:if test="${ not empty actionForm.technicalRegRetest }">
					<input type="hidden" id="retestDataFlag" value="true"/>
				</c:if>
				<h2 class="collapse-box-header" onclick="toggle('trRetest');">Ready to re-Test
					<a href="#">
						<span></span>
					</a>
				</h2>
				<div id="trRetestToggleText" class="data collapse-box-container">
					<table border="0" style="width: 2700px" id="trRetestTable" >
						<thead>
							<tr>
								<th style="width: 150px">Select</th>
								<th style="width: 150px">SEID</th>
								<th style="width: 100px">SE Code</th>
								<th style="width: 100px">Alarm ID</th>
								<th style="width: 100px">Material Code</th>
								<!-- <th style="width: 100px">Model</th> -->
								<th style="width: 150px">SID</th>
								<th style="width: 150px">MID</th>
								<th style="width: 100px">Administered?</th>
								<!-- <th style="width: 100px">Remote Access<br>Eligible</th> -->
								<th style="width: 150px">Test Remote Access?</th>
								<th style="width: 100px">Eligible for<br>Alarming?</th>
								<th width="100px">Entitled for<br> Alarming?</th>
								<th style="width: 100px">Test Alarming?</th>
								<th style="width: 100px">Last Alarm Received</th>
								<th style="width: 300px">SALGW(s)</th>
								<th style="width: 100px">Status</th>
							</tr>
						</thead>
						<tbody>
								<c:forEach items="${ actionForm.technicalRegRetest }" var="item" varStatus="container">
									<tr class="osm_table05_td">
										<td>
											<input type="checkbox" name="actionForm.technicalRegRetest[${container.index}].process" id="process${container.index}" value="true" ${ item.process ? 'checked' : '' }  ${ item.checkBoxDisabled ? 'disabled' : '' }/>
											<input type="hidden" name="actionForm.technicalRegRetest[${container.index}].process" value="false"/>
										</td>
										<td class="osm_text">${item.solutionElementId}</td>
										<td class="osm_text">${item.solutionElement} </td>
										<td class="osm_text">${item.alarmId}</td>
										<td class="osm_text">${item.technicalOrder.materialCode}</td>
										<%-- <td class="osm_text">${item.model}</td> --%>
										<td class="osm_text">${item.sid}</td>
										<td class="osm_text">${item.mid}</td>
										<td class="osm_text">${(not empty item.existence and item.existence!="null") ? item.existence : ""}</td>
										<%-- <td class="osm_text">${item.remoteAccess}</td> --%>
										<td class="osm_text" >
											<input type="checkbox" value="true" name="actionForm.technicalRegRetest[${container.index}].selectForRemoteAccess" id="ra${container.index}Select" ${item.remoteAccessCheckBoxDisabled ?'disabled':'' } ${ item.selectForRemoteAccess ? 'checked' : '' } />
											<input type="hidden" name="actionForm.technicalRegRetest[${container.index}].selectForRemoteAccess" value="false"/>
										</td>
										<td class="osm_text">${item.transportAlarm}</td>
										<td>${item.entitledForAlarming}</td>
										<td class="osm_text" >
											<input type="checkbox" value="true" name="actionForm.technicalRegRetest[${container.index}].selectForAlarming" id="ta${container.index}Select" ${item.alarmingCheckBoxDisabled ?'disabled':''} ${ item.selectForAlarming ? 'checked' : '' } />
											<input type="hidden" name="actionForm.technicalRegRetest[${container.index}].selectForAlarming" value="false"/>
										</td>
										<td class="osm_text">${(not empty item.deviceLastAlarmReceivedDate and item.deviceLastAlarmReceivedDate!="null") ? item.deviceLastAlarmReceivedDate : ""}</td>
										<td class="osm_text">${item.primarySalGWSeid}${item.secondarySalGWSeid}</td>
										<td class="osm_text">${item.deviceStatus}</td>
									</tr>
									<c:forEach items="${ item.expSolutionElements }" var="item1" varStatus="container1">
										<tr class="osm_table05_td">
												<td>
												</td>
												<td class="osm_text">${item1.seID}</td>
												<td class="osm_text">${item1.seCode}</td>
												<td class="osm_text">${item1.alarmId}</td>
												<td class="osm_text"> &nbsp;&nbsp;&nbsp; </td>
												<%-- <td class="osm_text">${item1.model}</td> --%>
												
												<td class="osm_text">${item1.sid}</td>
												<td class="osm_text">${item1.mid}</td>
												<td class="osm_text">${(not empty item1.existence and item1.existence!="null") ? item1.existence : ""}</td>
												<td class="osm_text" >
													<input type="checkbox" value="true" name="actionForm.technicalRegRetest[${container.index}].expSolutionElements[${ container1.index }].selectForRemoteAccess"  id="${container.index}childRa${container1.index}Select" ${item1.remoteAccessCheckBoxDisabled?'disabled':''}  ${item1.selectForRemoteAccess?'checked':'' } />
													<input type="hidden" name="actionForm.technicalRegRetest[${container.index}].expSolutionElements[${ container1.index }].selectForRemoteAccess" value="false"/>
												</td>

												<td class="osm_text">${item1.transportAlarmEligible}</td>
												<td>${item.entitledForAlarming}</td>
												<td class="osm_text" >
													<input type="checkbox" value="true" name="actionForm.technicalRegRetest[${container.index}].expSolutionElements[${ container1.index }].selectForAlarming" id="${container.index}childTa${container1.index}Select" ${item1.alarmingCheckBoxDisabled?'disabled':''}  ${ item1.selectForAlarming?'checked':'' } />
													<input type="hidden" name="actionForm.technicalRegRetest[${container.index}].expSolutionElements[${ container1.index }].selectForAlarming" value="false"/>
												</td>												
												<td class="osm_text">${(not empty item1.deviceLastAlarmReceivedDate and item1.deviceLastAlarmReceivedDate!="null" ) ? item1.deviceLastAlarmReceivedDate : ""}</td>
												<td class="osm_text">${item1.primarySalGWSeid}${item1.secondarySalGWSeid}</td>
												<td class="osm_text">${item1.deviceStatus}</td>
										</tr>
									</c:forEach>
								</c:forEach>
						</tbody>
					</table>
					<div class="no-data-found">No data available in table</div>
				</div>
			</div>
			<!-- end tob-retest-wrap -->

			<!-- start controls -->
			<div class="controls">
				<input type="button" onclick="backFromTOBDashBoard();" value="Back" class="button gray" />&nbsp;
				<a id="backFromTOBDashBoardAction" href="<c:url context="${ _path }" value="/technicalonboarding/backFromTechnicalRegistrationSummary.action" />" ></a>

				<%
				if(trCancelStr!=null && (trCancelStr).toString().equals("1")){
				%>
				<input type="button" value="Cancel" class="button gray" onClick="return cancelTRSummary();" disabled="disabled"/>&nbsp;
				<%} else {%>
				<input type="button" value="Cancel" class="button gray" onClick="return cancelHomeAction();"/>&nbsp;
				<%}%>
				<a id="cancelTOBDashBoardAction" href="<c:url context="${ _path }" value="/technicalonboarding/cancelTechnicalRegistrationSummary.action" />" ></a>
				<a id="cancelHomeAction" href="<c:url context="${ _path }" value="/technicalonboarding/cancelRegistrationHome.action" />" ></a>

				<%
				if(trCancelStr!=null && (trCancelStr.toString().equals("1"))){
				%>
				<input type="button" value="Submit" class="button gray" onClick="submitTechnicalRegistrationSummary();" disabled="disabled"/>
				<%} else {%>
				<input type="button" value="Submit" class="button gray" onClick="submitTechnicalRegistrationSummary();"/>
				<%}%>

				<a id="submitTechRegSummary" href="<c:url context="${ _path }" value="/technicalonboarding/submitTechnicalRegistrationSummary.action" />" ></a>

			</div>
			<!-- end controls -->

			<input type="hidden" id="errMessage" value="${ errorMessage }"/>
			<input type="hidden" id="actionFrmMsg" value="${ actionForm.errorMessage }"/>
			<input type="hidden" id="tobDashboardDataError" value="${ tobDashboardDataError }"/>

			<input type="hidden" id="remainingQtyLessThanOne" value="${ grtConfig.remainingQtyLessThanOne }"/>
			<input type="hidden" id="i0020_Technical_On_Boarding" value="${ grtConfig.noConnectivityPopUpMessage }"/>
			<input type="hidden" id="i0021_Technical_On_Boarding" value="${ grtConfig.connectivityPopUpMessage }"/>
			<input type="hidden" id="i0022_Technical_On_Boarding" value="${ grtConfig.cancelActionMsg }"/>
			<input type="hidden" id="i0023_Technical_On_Boarding" value="${ grtConfig.selectRecord }"/>
			<input type="hidden" id="i0024_Technical_On_Boarding" value="${ grtConfig.submitTOBData }"/>
		</form>
		<!-- end technicalRegistrationForm -->
		<!-- start error messages -->
		<input type="hidden" id="alertRemQuanMinOne" value="${grtConfig.alertRemQuanMinOne}" />
		<input type="hidden" id="alertEnterAtlOneRec" value="${grtConfig.alertEnterAtlOneRec}" />
		<input type="hidden" id="cnfrmCancWillDelete" value="${grtConfig.cnfrmCancWillDelete}" />
		<input type="hidden" id="cnfrmSubmitForTOB" value="${grtConfig.cnfrmSubmitForTOB}" />
		<!-- Retest Message -->
		<input type="hidden" value="${grtConfig.ewiMessageCodeMap['salRemoteAccessReqErrMsg']}" id="salRemoteAccessReqErrMsgCode" />
		<input type="hidden" value="${ grtConfig.salRemoteAccessReqErrMsg}" id="salRemoteAccessReqErrMsg" />
		<input type="hidden" value="${ grtConfig.salSeleSeidRequiedMsg}" id="salSeleSeidRequiedMsg" />
		<input type="hidden" value="${grtConfig.ewiMessageCodeMap['salSeleSeidRequiedMsg']}" id="salSeleSeidRequiedMsgCode" />


		<!-- end error messages -->




		<!-- Error Codes -->
		<input type="hidden" id="alertRemQuanMinOneErrorCode" value="${grtConfig.ewiMessageCodeMap['alertRemQuanMinOne']}"/>
		<input type="hidden" id="alertEnterAtlOneRecErrorCode" value="${grtConfig.ewiMessageCodeMap['alertEnterAtlOneRec']}"/>
		<input type="hidden" id="cnfrmCancWillDeleteConfCode" value="${grtConfig.ewiMessageCodeMap['cnfrmCancWillDelete']}"/>
		<input type="hidden" id="cnfrmSubmitForTOBConfCode" value="${grtConfig.ewiMessageCodeMap['cnfrmSubmitForTOB']}"/>


	</div>
	<!-- end page content -->
</div>
<!-- end page content-wrap -->

