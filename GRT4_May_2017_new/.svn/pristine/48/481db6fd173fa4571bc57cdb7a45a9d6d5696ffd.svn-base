<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.List"%>
<%@ include file="/includes/context.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
%>
<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/views/tob-details.css" />"/>
<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/plugins/jquery.dataTables_themeroller.css" />" />

<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/technicalonboarding/technicalRegistrationDetails.js" />"></script>

<!-- start page content-wrap -->
<div class="content-wrap tob-details">

	<h1 class="page-title">Technical On-Boarding Details</h1>

	<!-- start page content -->
	<div class="content">

		<!-- start registration summary -->
		<%@ include file="../installbase/grtBanner.jsp" %>
		<!-- end registration summary -->

		<!-- start tecRegDetails -->
		<form name="tecRegDetails" id="tecRegDetails" action="submitTrDetails" method="post" enctype="multipart/form-data">

			<input type="hidden" id="trdSeid" name="actionForm.trdSeid" value="${ actionForm.trdSeid }">
			<input type="hidden" id="trId" name="actionForm.technicalRegistrationId" value="${ actionForm.technicalRegistrationId }">
			<input type="hidden" id="trdIndex" name="actionForm.index" value="${ actionForm.index }">
			<input type="hidden" id="trdType" name="actionForm.trDetailType" value="${ actionForm.trDetailType }">
			<input type="hidden" id="trdUiOperation" name="actionForm.uiOperation" value="${ actionForm.uiOperation }">
			<input type="hidden" id="returnCode" name="actionForm.returnCode" value="${ actionForm.returnCode }">
			<input type="hidden" id="message" name="actionForm.message" value="${ actionForm.message }">

			<a id="backToRegistrationList" href="<c:url context="${ _path }" value="/technicalonboarding/backFromTRDetails" />"></a>
			<a id="submitTrDetails" href="<c:url context="${ _path }" value="/technicalonboarding/submitTrDetails" />"></a>
			<a id="loadSalStepBDetailsAction" href="<c:url context="${ _path }" value="/technicalonboarding/loadSalStepBDetails"/>"
			onclick="anchor_submit_form('tecRegDetails','<c:url context="${ _path }" value="/technicalonboarding/loadSalStepBDetails"/>');return false;"></a>

			<!-- Technical Registration Detail Table -->
			<c:set var="nonSalDetails" value="${actionForm.nonSalList}"/>
			<c:set var="salDetails" value="${actionForm.salList}"/>
			<c:set var="salMigratinDetails" value="${actionForm.salMigrationList}"/>
			<c:set var="seidCreationFailureFlag" value="${actionForm.seidCreationFailureFlag}"/>
			<c:set var="retestDetailsFlag" value="${actionForm.retestListFlag}"/>

			<%
			Boolean nonSal = (Boolean) pageContext.getAttribute("nonSalDetails");
			Boolean sal = (Boolean) pageContext.getAttribute("salDetails");
			Boolean salMigration = (Boolean) pageContext.getAttribute("salMigratinDetails");
			Boolean seidCreationFailureFlag = (Boolean) pageContext.getAttribute("seidCreationFailureFlag");
			Boolean retestDetailsFlag = (Boolean) pageContext.getAttribute("retestDetailsFlag");

			String registratonType = (String)request.getSession().getAttribute(com.grt.util.GRTConstants.REGISTRATION_TYPE);
			if ( nonSal != null	&& nonSal.booleanValue()) {
			%>

			<!-- start tob reg detail -->
			<div id="tech-wrap" class="data-table-wrap collapse-box">
				<h2 class="collapse-box-header active">Technical Registration Detail
					<a href="#">
						<span></span>
					</a>
				</h2>
				<div class="data collapse-box-container">

					<div class="basic-table-wrap">
						<table class="basic-table">
							<thead>
								<tr>
									<th style="width: 100px" align="center" ></th>
									<th style="width: 100px" align="center" ></th>
									<th style="width: 100px" align="center" >Error</th>
									<th style="width: 150px" align="center" >SEID</th>
									<th style="width: 100px" align="center" >SE Code</th>
									<th style="width: 100px" align="center" >Material Code</th>
									<th style="width: 250px" align="center" >Material Code Description</th>
									<th style="width: 100px" align="center" >Access Type</th>
									<th style="width: 150px" align="center" >Access Detail</th>
									<th style="width: 100px" align="center" >SID</th>
									<th style="width: 100px" align="center" >MID</th>
									<th style="width: 100px" align="center" >Status</th>
									<th style="width: 150px" align="center" >Submitted</th>
									<th style="width: 150px" align="center" >Completed</th>
									<th style="width: 125px" align="center" >SR#</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${ actionForm.technicalRegistrationDetailList }" var="item" varStatus="container">
								<c:set var="troubleShootURL" value="${item.troubleShootURL}"/>
								<tr>
									<c:set var="nonSalstatusDesc" value="${item.status.statusDescription}"/>
									<c:set var="nsalOpType" value="${item.operationType}"/>
									<c:set var="stepASubmittedDate" value="${item.stepASubmittedDate}"/>
									<c:set var="stepAReSubmittedDate" value="${item.stepAReSubmittedDate}"/>
									<c:set var="stepACompletedDate" value="${item.stepACompletedDate}"/>
									<td	style="width: 100px" align="center" ><input type="button" class="button gray"  ${item.updateButtonStatus ? 'disabled' :''} value="Update"	onClick="submitTrDetails('${item.technicalRegistrationId}',	'${container.index}', 'NONSAL', 'Update')" /></td>
									<td	style="width: 95px" align="center" ><input type="button"	class="button gray"  ${item.submitButtonStatus ? 'disabled' :''} value="Submit"	 onClick="submitTrDetails('${item.technicalRegistrationId}',	'${container.index}', 'NONSAL', 'Submit')" /></td>
									<%
										String troubleShootURL = (String) pageContext.getAttribute("troubleShootURL");
										if(troubleShootURL!=null && troubleShootURL.length() > 0 ){
									%>
									<td	style="width: 100px" align="center"	title="${item.errorDesc}" ><a href="${item.troubleShootURL}" target="_blank" >${item.errorCode}</a></td>
									<%
										} else {
									%>
									<td	style="width: 100px" align="center" title="${item.errorDesc}" >${item.errorCode}</td>
									<%
										}
									%>
									<td style="width: 150px" align="center" >${item.solutionElementId}</td>
									<td style="width: 100px" align="center" >${item.solutionElement}</td>
									<td style="width: 100px" align="center" >${item.technicalOrder.materialCode}</td>
									<td style="width: 250px" align="center" >${item.technicalOrder.description}</td>
									<td style="width: 100px" align="center" >${item.uiAccessType}</td>
									<td style="width: 150px" align="center" >${item.connectionDetail}</td>
									<td style="width: 100px" align="center" >${item.sid}</td>
									<td style="width: 100px" align="center" >${item.mid}</td>
									<%
										String nonSalStatus = (String) pageContext.getAttribute("nonSalstatusDesc");
										String nonSalOpType = (String) pageContext.getAttribute("nsalOpType");
										if(nonSalStatus !=null && nonSalStatus.equalsIgnoreCase("Completed") && !(com.grt.util.GRTConstants.DU.equals(nonSalOpType))){
									%>
									<td style="width: 100px" align="center" ><a href="#" onClick="showStatusPopup('${item.technicalRegistrationId}')" style="color:#d72726">${item.status.statusDescription}</a></td>
									<%} else { %>
									<td style="width: 100px" align="center" >${item.status.statusDescription}</td>
									<%}
										if(pageContext.getAttribute("stepAReSubmittedDate") != null) {
									%>
									<td style="width: 150px" align="center" ><%= sdf.format(pageContext.getAttribute("stepAReSubmittedDate"))%></td>
									<%} else if (pageContext.getAttribute("stepASubmittedDate") != null){
									%>
									<td style="width: 150px" align="center" ><%= sdf.format(pageContext.getAttribute("stepASubmittedDate"))%></td>
									<%} else { %>
									<td style="width: 150px" align="center" >&nbsp;</td>
									<%}%>
									<td style="width: 150px" align="center" >
									<% if(pageContext.getAttribute("stepACompletedDate") != null){ %><%= sdf.format(pageContext.getAttribute("stepACompletedDate"))%><%} %>
									</td>
									<td style="width: 125px" align="center" ><a href="#" onClick="showPopup('${actionForm.soldToId}', '${item.strSiebelSRNo}')"><span style="color: red;">${item.strSiebelSRNo}</span></a></td>
									
								</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>

				</div>
			</div>
			<!-- end tob reg detail -->

			<%
			}
			if (sal != null	&& sal.booleanValue() ) {
			%>

			<!-- start sal summary -->
			<div id="sal-wrap" class="data-table-wrap collapse-box">
				<h2 class="collapse-box-header active">SAL On-Boarding Summary
					<a href="#">
						<span></span>
					</a>
				</h2>
				<div class="data collapse-box-container">

					<div class="basic-table-wrap">
						<table class="basic-table">
							<thead>
								<tr>
									<th style="width: 100px">Select</th>
									<th style="width: 100px"></th>
									<th style="width: 100px"></th>
									<th style="width: 100px">Error</th>
									<th style="width: 150px">SEID</th>
									<th style="width: 100px">SE Code</th>
									<th style="width: 100px">Alarm ID</th>
									<th style="width: 100px">Material Code</th>
									<th style="width: 250px">Material Code Description</th>
									<th style="width: 100px">Model</th>
									<th style="width: 300px">SALGW(s)</th>
									<th style="width: 100px">SEID Creation Status</th>
									<th style="width: 150px">SEID Creation Submitted</th>
									<th style="width: 150px">SEID Creation Completed</th>
									<th style="width: 100px">SEID Creation SR#</th>
									<th style="width: 100px">Eligible for <br>Alarming?</th>
									<th style="width: 100px">Connectivity/Alarming Status</th>
									<th style="width: 150px">Connectivity/Alarming Submitted</th>
									<th style="width: 150px">Connectivity/Alarming Completed</th>
									<th style="width: 100px">Connectivity/Alarming SR#</th>
									<th style="width: 100px"></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${ actionForm.salRegistrationSummaryList }" var="item" varStatus="container">
								<c:set var="troubleShootURL" value="${item.troubleShootURL}"/>
								<tr>
									<c:set var="statusDesc" value="${item.status.statusDescription}"/>
									<c:set var="stepBstatusDesc" value="${item.stepBStatus.statusDescription}"/>
									<c:set var="opType" value="${item.operationType}"/>
									<c:set var="stepASubmittedDate1" value="${item.stepASubmittedDate}"/>
									<c:set var="stepAReSubmittedDate1" value="${item.stepAReSubmittedDate}"/>
									<c:set var="stepACompletedDate1" value="${item.stepACompletedDate}"/>
									<c:set var="stepBSubmittedDate1" value="${item.stepBSubmittedDate}"/>
									<c:set var="stepBCompletedDate1" value="${item.stepBCompletedDate}"/>
									
									<td style="width: 100px" align="center" >
										<input type="checkbox" class="alarmChckBx" id="sal${container.index}Select" name="actionForm.salRegistrationSummaryList[${container.index}].selectForAlarmAndConnectivity" value="true"	${item.checkBoxDisabled ? 'disabled' : ''} ${ item.selectForAlarmAndConnectivity ? 'checked' : ''}/>
										<input type="hidden" name="actionForm.salRegistrationSummaryList[${container.index}].selectForAlarmAndConnectivity" value="false"/>
									</td>
									<td	style="width: 100px" align="center" ><input type="button" class="button gray" value="Update"	${item.updateButtonStatus ? 'disabled' :''}	onClick="submitTrDetails('${item.technicalRegistrationId}',	'${container.index}', 'SAL', 'Update')" /></td>
									<td	style="width: 95px" align="center" ><input type="button" class="button gray" value="Submit"	${item.submitButtonStatus ? 'disabled' :''}	onClick="submitTrDetails('${item.technicalRegistrationId}',	'${container.index}', 'SAL', 'Submit')" /></td>
									<%
										String troubleShootURL = (String) pageContext.getAttribute("troubleShootURL");
										if(troubleShootURL!=null && troubleShootURL.length() > 0 ){
									%>
									<td	style="width: 100px" align="center"	title="${item.errorDesc}" ><a href="${item.troubleShootURL}" target="_blank" >${item.errorCode}</a></td>
									<%
										} else {
									%>
									<td	 style="width: 100px" align="center" title="${item.errorDesc}" >${item.errorCode}</td>
									<%
										}
									%>
									
									<td style="width: 140px" align="center" ><span id="sal${container.index}SEID">${item.solutionElementId}</span></td>
									<td style="width: 95px" align="center" >${item.solutionElement}</td>
									<td style="width: 100px" align="center" >${item.productId}</td>
									<td style="width: 96px" align="center" >${item.technicalOrder.materialCode}</td>
									<td style="width: 240px" align="center" >${item.technicalOrder.description}</td>
									<td style="width: 95px" align="center" >${item.model}</td>
									<td style="width: 285px" align="center" >${item.salGateWaySeid}</td>
									<%
										String statusDescription = (String) pageContext.getAttribute("statusDesc");
										String operationType = (String) pageContext.getAttribute("opType");
										if(statusDescription !=null && statusDescription.equalsIgnoreCase("Completed") &&	!(com.grt.util.GRTConstants.DU.equals(operationType))){
									%>
									<td style="width: 100px" align="center" ><a href="#" onClick="showStatusPopup('${item.technicalRegistrationId}')" style="color:#d72726">${item.status.statusDescription}</a></td>
									<%} else { %>
									<td style="width: 100px" align="center" >${item.status.statusDescription}</td>
									<% }
										if(pageContext.getAttribute("stepAReSubmittedDate1") != null) {
									%>
									<td style="width: 150px" align="center" ><%= sdf.format(pageContext.getAttribute("stepAReSubmittedDate1"))%></td>
									<%} else if (pageContext.getAttribute("stepASubmittedDate1") != null){
									%>
									<td style="width: 150px" align="center" ><%= sdf.format(pageContext.getAttribute("stepASubmittedDate1"))%></td>
									<%} else { %>
									<td style="width: 150px" align="center" >&nbsp;</td>
									<% }%>
									<td style="width: 150px" align="center" >
									<% if(pageContext.getAttribute("stepACompletedDate1") != null){%><%= sdf.format(pageContext.getAttribute("stepACompletedDate1"))%><%} %>
									</td>
									<td style="width: 100px" align="center" ><a href="#" onClick="showPopup('${actionForm.soldToId}', '${item.strSiebelSRNo}')"><span style="color: red;">${item.strSiebelSRNo}</span></a></td>
									<td style="width: 95px" align="center" >${item.transportAlarm}</td>
									<td style="width: 100px" align="center" >${item.stepBStatus.statusDescription}</td>
									<td style="width: 150px" align="center" >
									<% if(pageContext.getAttribute("stepBSubmittedDate1") != null){ %><%= sdf.format(pageContext.getAttribute("stepBSubmittedDate1"))%><%} %>
									</td>
									<td style="width: 150px" align="center" >
									<% if(pageContext.getAttribute("stepBCompletedDate1") != null){ %><%= sdf.format(pageContext.getAttribute("stepBCompletedDate1"))%><%} %>
									</td>
									<td style="width: 100px" align="center" ><a href="#" onClick="showPopup('${actionForm.soldToId}', '${item.strStepBSRNo}')"><span style="color: red;">${item.strStepBSRNo}</span></a></td>
									
									<%
									String stepBStatusDesc = (String) pageContext.getAttribute("stepBstatusDesc");
										if("Completed".equalsIgnoreCase(stepBStatusDesc) || "In Process".equalsIgnoreCase(stepBStatusDesc)){
										%>
											<td	style="width: 100px" align="center" >
												<input type="button" class="button gray" value="Detail"	${item.detailButtonStatus ? 'disabled' :''}	onClick="submitTrDetails('${item.technicalRegistrationId}',	'${container.index}', 'SAL', 'Detail')" />
											</td>
										<%
										} else if("Awaiting Information".equalsIgnoreCase(stepBStatusDesc)){
										%>
											<td	style="width: 100px" align="center" >
												<input type="button" class="button gray" value="Detail"	onClick="submitTrDetails('${item.technicalRegistrationId}',	'${container.index}', 'SAL', 'Detail')" />
											</td>
										<%
										}else{%>
											<td	style="width: 100px" align="center" >
												&nbsp;
											</td>
										<%}
									%>
								</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>

				</div>
			</div>
			<!-- end sal summary -->

			<%
			}
			if (salMigration != null	&& salMigration.booleanValue() ) {
			%>

			<!-- start sal-third summary -->
			<div id="sal-third-wrap" class="data-table-wrap collapse-box">
				<h2 class="collapse-box-header active">SAL Migration Summary
					<a href="#">
						<span></span>
					</a>
				</h2>
				<div class="data collapse-box-container">

					<div class="basic-table-wrap">
						<table class="basic-table">
							<thead>
								<tr>
									<th style="width: 100px">Select</th>
									<th style="width: 100px"></th>
									<th style="width: 100px"></th>
									<th style="width: 100px">Error</th>
									<th style="width: 150px">SEID </th>
									<th style="width: 100px">SE Code</th>
									<th style="width: 100px">Alarm ID</th>
									<th style="width: 100px">Material Code</th>
									<th style="width: 250px">Material Code Description</th>
									<th style="width: 150px">Model</th>
									<th style="width: 300px">SALGW(s)</th>
									<th style="width: 100px">SEID Creation Status</th>
									<th style="width: 150px">SEID Creation Submitted</th>
									<th style="width: 150px">SEID Creation Completed</th>
									<th style="width: 100px">SEID Creation SR#</th>
									<!-- <th style="width: 100px">Remote Access<br>Eligible</th> -->
									<th style="width: 100px">Eligible for <br> Alarming?</th>
									<th style="width: 100px">Connectivity/Alarming Status</th>
									<th style="width: 150px">Connectivity/Alarming Submitted</th>
									<th style="width: 150px">Connectivity/Alarming Completed</th>
									<th style="width: 100px">Connectivity/Alarming SR#</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${ actionForm.salMigrationSummaryList }" var="item" varStatus="container">
								<c:set var="troubleShootURL" value="${item.troubleShootURL}"/>
								<tr style="width: 2650px" >
									<c:set var="salMigrationStatus" value="${item.status.statusDescription}"/>
									<c:set var="stepASubmittedDate2" value="${item.stepASubmittedDate}"/>
									<c:set var="stepACompletedDate2" value="${item.stepACompletedDate}"/>
									<c:set var="stepBSubmittedDate2" value="${item.stepBSubmittedDate}"/>
									<c:set var="stepBCompletedDate2" value="${item.stepBCompletedDate}"/>
									<c:set var="salStepBstatusDesc" value="${item.stepBStatus.statusDescription}"/>
									
									<td style="width: 100px" align="center" >
										<input type="checkbox" class="alarmChckBx" id="sal${container.index}Select" name="actionForm.salMigrationSummaryList[${container.index}].selectForAlarmAndConnectivity" value="true" ${item.checkBoxDisabled ?'disabled' :''} ${ item.selectForAlarmAndConnectivity ? 'checked' : '' } />
										<input type="hidden" name="actionForm.salMigrationSummaryList[${container.index}].selectForAlarmAndConnectivity" value="false"/>
									</td>
									<%
										String stepBStatusDesc = (String) pageContext.getAttribute("salStepBstatusDesc");
										if("Completed".equalsIgnoreCase(stepBStatusDesc) || "In Process".equalsIgnoreCase(stepBStatusDesc)){
										%>
											<td style="width: 100px" align="center" ><input type="button" class="button gray" value="Detail"	${item.detailButtonStatus ? 'disabled' : ''}	onClick="submitTrDetails('${item.id}',	'${container.index}', 'SALMigration', 'Detail')" /></td>
										<%
										} else if("Awaiting Information".equalsIgnoreCase(stepBStatusDesc)){
										%>
											<td style="width: 100px" align="center" ><input type="button" class="button gray" value="Detail" onClick="submitTrDetails('${item.id}',	'${container.index}', 'SALMigration', 'Detail')" /></td>
										<%
										}else{%>
											<td style="width: 100px" align="center" ><input type="button" class="button gray" value="Detail"	${item.detailButtonStatus ? 'disabled' : ''}	onClick="submitTrDetails('${item.id}',	'${container.index}', 'SALMigration', 'Detail')" /></td>
										<%}
									%>
									<td	style="width: 95px" align="center" ><input type="button" class="button gray" value="Submit"	${item.submitButtonStatus ? 'disabled' : ''}	onClick="submitTrDetails('${item.id}',	'${container.index}', 'SALMigration', 'Submit')" /></td>
									<%
										String troubleShootURL = (String) pageContext.getAttribute("troubleShootURL");
										if(troubleShootURL!=null && troubleShootURL.length() > 0 ){
									%>
									<td style="width: 100px" align="center"	title="${item.errorDesc}" ><a href="${item.troubleShootURL}" target="_blank" >${item.errorCode}</a></td>
									<%
										} else {
									%>
									<td style="width: 100px" align="center"	title="${item.errorDesc}" >${item.errorCode}</td>
									<%
										}
									%>
									<td style="width: 140px" align="center" >${item.solutionElementId}</td>
									<td style="width: 96px" align="center" >${item.seCode}</td>
									<td style="width: 97px" align="center" >${item.productId}</td>
									<td style="width: 96px" align="center" >${item.materialCode}</td>
									<td style="width: 233px" align="center"	>${item.materialCodeDescription}</td>
									<td style="width: 141px" align="center" >${item.model}</td>
									<td style="width: 278px" align="center" >${item.salGateWaySeid}</td>
									<td style="width: 96px" align="center" >${item.status.statusDescription}</td>
									<% if (pageContext.getAttribute("stepASubmittedDate2") != null){
									%>
									<td style="width: 150px" align="center" ><%= sdf.format(pageContext.getAttribute("stepASubmittedDate2"))%></td>
									<%} else { %>
									<td style="width: 150px" align="center" >&nbsp;</td>
									<% }%>
									<td style="width: 150px" align="center" >
									<% if(pageContext.getAttribute("stepACompletedDate2") != null){%><%= sdf.format(pageContext.getAttribute("stepACompletedDate2"))%><%} %>
									</td>
									<td style="width: 96px" align="center" ><a href="#" onClick="showPopup('${actionForm.soldToId}', '${item.strSiebelSRNo}')"><span style="color: red;">${item.strSiebelSRNo}</span></a></td>
									
									<td style="width: 96px" align="center" >${item.transportAlarm}</td>
									
									<td style="width: 100px" align="center" >${item.stepBStatus.statusDescription}</td>
									<td style="width: 150px" align="center" >
									<% if(pageContext.getAttribute("stepBSubmittedDate2") != null){ %><%= sdf.format(pageContext.getAttribute("stepBSubmittedDate2"))%><%} %>
									</td>
									<td style="width: 150px" align="center" >
									<% if(pageContext.getAttribute("stepBCompletedDate2") != null){ %><%= sdf.format(pageContext.getAttribute("stepBCompletedDate2"))%><%} %>
									</td>
									<td style="width: 100px" align="center" ><a href="#" onClick="showPopup('${actionForm.soldToId}', '${item.strStepBSRNo}')"><span style="color: red;">${item.strStepBSRNo}</span></a></td>
									
								</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>

				</div>
			</div>
			<!-- end sal-third summary -->

			<% } if (retestDetailsFlag != null	&& retestDetailsFlag.booleanValue()) {
			%>
					<!-- GRT 4.0 Retest Table	-->

			<!-- start re-test summary -->
			<div id="retest-wrap" class="data-table-wrap collapse-box">
				<h2 class="collapse-box-header active">Re-Test Summary
					<a href="#">
						<span></span>
					</a>
				</h2>
				<div class="data collapse-box-container">

					<div class="basic-table-wrap">
						<table class="basic-table">
							<thead>
								<tr>
									<th style="width: 100px">Material Code</th>
									<th style="width: 100px">SE Code</th>
									<th style="width: 100px">Model</th>
									<th style="width: 100px">Alarm ID</th>
									<th style="width: 150px">SEID</th>
									<th style="width: 150px">SID</th>
									<th style="width: 150px">MID</th>
									<th style="width: 100px">Eligible for <br>Remote Access?</th>
									<th style="width: 150px">Test Remote Access?</th>
									<th style="width: 100px">Eligible for<br> Alarming?</th>
									<th style="width: 100px">Test Alarming?</th>
									<th style="width: 100px">Last Alarm Received</th>
									<th style="width: 300px">SALGW(s)</th>
									<th style="width: 100px">Status</th>
									<th style="width: 150px">SR#</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${ actionForm.retestTRList }" var="item" varStatus="container">
								<tr>
									<td>${item.technicalOrder.materialCode}</td>
									<td>${item.solutionElement} </td>
									<td>${item.model}</td>
									<td>${item.alarmId}</td>
									<td>${item.solutionElementId}</td>
									<td>${item.sid}</td>
									<td>${item.mid}</td>
									<td>${item.remoteAccess}</td>
									<td >
										${item.selectForRemoteAccess ? 'Y':'N'}
									</td>
									<td>${item.transportAlarm}</td>
									<td >
										${item.selectForAlarming ? 'Y':'N'}
									</td>
									<td>${(not empty item.deviceLastAlarmReceivedDate and item.deviceLastAlarmReceivedDate!="null") ? item.deviceLastAlarmReceivedDate : ""}</td>
									<td>${item.primarySalGWSeid}${item.secondarySalGWSeid}</td>
									<td>
										<c:if test="${ item.selectForRemoteAccess or item.selectForAlarming or item.stepBStatus.statusShortDescription=='Cancelled' }">
											${item.stepBStatus.statusShortDescription}
										</c:if>
									</td>
									<td>
										<c:choose>
											<c:when test="${item.stepBSRRequest.siebelSRNo!=null}">
												<a href="#" onClick="showPopup('${actionForm.soldToId}', '${item.stepBSRRequest.siebelSRNo}')"><span style="color: red;">${item.stepBSRRequest.siebelSRNo}</span></a>
											</c:when>
											<c:otherwise>
											    <a href="#" onClick="showPopup('${actionForm.soldToId}', '${item.srRequest.siebelSRNo}')"><span style="color: red;">${item.srRequest.siebelSRNo}</span></a>
											</c:otherwise>
										</c:choose>
										
									</td>
								</tr>
								<c:forEach items="${ item.explodedSolutionElements }" var="item1" varStatus="container1">
								<tr>
									<td> &nbsp;&nbsp;&nbsp; </td>
									<td>${item1.seCode}</td>
									<td>${item1.model}</td>
									<td>${item1.alarmId}</td>
									<td>${item1.seID}</td>
									<td>${item1.sid}</td>
									<td>${item1.mid}</td>
									<td>${item1.remoteAccessEligible}</td>
									<td >
										${ item1.selectForRemoteAccess ? 'Y':'N' }
									</td>
									<td>${item1.transportAlarmEligible}</td>
									<td >
										${ item1.selectForAlarming ? 'Y':'N' }
									</td>
									<td>${(not empty item1.deviceLastAlarmReceivedDate and item1.deviceLastAlarmReceivedDate!="null")? item1.deviceLastAlarmReceivedDate : ""}</td>
									<td>${item1.primarySalGWSeid}${item1.secondarySalGWSeid}</td>
									<td>${item1.retestStatus}</td>
									<td></td>
								</tr>
								</c:forEach>
								</c:forEach>
							</tbody>
						</table>
					</div>

				</div>
			</div>
			<!-- end re-test summary -->

			<% } %>

			<%
			if (seidCreationFailureFlag != null && seidCreationFailureFlag.booleanValue()) {
			%>
			<div class="error-bar" style="display: block;">
				<i class="fa fa-exclamation-triangle"></i>
				<p class="error-bar-msg">
					<span>Action Required:</span> An error occurred while processing your record building request. You can
					change the information submitted by pressing the "Update" button and resubmit the request
					or if all data is correct you can simply press the "Submit" button. This request will not be
					processed without one of these options selected.
				</p>
				<p class="error-bar-msg">
					If an error occurs on your second submission, a service request will automatically be created for
					an Avaya engineer to resolve the issues and process your request. You will receive an email
					notifying you of the details. (Note: the SR will only be created upon second submission).
				</p>
			</div>

			<% } %>

			<div class="page-note" ${actionForm.skipStepB == 'none' ? 'style="display:none"' : ''}>
				<p><span>Please Note:</span> Any SEID that was created manually outside of GRT cannot be submitted for Connectivity/Alarm testing in GRT. This situation is detailed as exception #3 in this KB article describing <a target="\_blank\" href=\"https://support.avaya.com/ext/index?page=content&id=FAQ101876\">SAL registration scenarios not supported by GRT</a>.
				</p>
				<p>To test these manually created SEID's for connectivity and alarming, please submit the <a target="\_blank\" href=\"https://support.avaya.com/css/P8/documents/100076044\">Technical Onboarding Exception Form</a> you received to <a href=\"mailto:salfinalreg@avaya.com\">salfinalreg@avaya.com</a>.
				</p>
			</div>

			<div class="page-note">
				<p><span>Please Note:</span> Alarming will only be configured if you are contractually entitled to alarming at the time of registration.
				</p>
			</div>

			<!-- start controls -->
			<div class="controls">
				<input type="button" value="Back" class="button gray" onclick="backToRegistrationList()" />&nbsp;&nbsp;
				<%
				String acDisabled = (String)request.getSession().getAttribute(com.grt.util.GRTConstants.ALARM_AND_CONNECTIVITY_DISABLED);
				if(	 (sal != null	&& sal.booleanValue())	||	(salMigration != null && salMigration.booleanValue()) ){
					if(com.grt.util.GRTConstants.TRUE.equalsIgnoreCase(acDisabled)){
				%>
				<input type="button" id="salAlarmButton" value="SAL Alarm & Connectivity" class="button disabled"	disabled	onclick="return validateSalMigrationAndRegistration();" />
				<% } else { %>
				<input type="button" id="salAlarmButton" value="SAL Alarm & Connectivity" class="button gray"	onclick="return validateSalMigrationAndRegistration();" />
				<% } } %>
				<script type="text/javascript">
				$(document).ready(function(){
						showMessageToUser();
				});
				</script>
			</div>
			<!-- end controls -->

			<!-- start statusPopup -->
			<div id="statusPopup" class="modal" style="display:none">
				<div class="modal-overlay">
					<div class="modal-content">
						<a class="close" onclick="closePopup3();"><i class="fa fa-close"></i></a>
						<h2 class="title">Completed Status Info</h2>

						<p class="content">
							<img class="msg-icon"
								src="<c:url context="${ _path }" value="/styles/images/messages/icon_info_light_60x60.png" />"
								alt="warning" />
							<span class="msg-txt" id="complettionDetails"></span>
						</p>
					</div>
				</div>
			</div>
			<!-- end statusPopup -->

			<!-- start srSummaryPopup -->
			<div id="srSummaryPopup" class="modal" style="display: none;">
				<div class="modal-overlay">
					<div class="modal-content">
						<a class="close" onclick="closeSummaryPopup();"><i class="fa fa-close"></i></a>
						<h2 class="title">SR Summary</h2>
						<p class="content">
							<table width="100%" cellpadding="10" cellspacing="10">
								<tr>
									<td width="35%">SR #</td>
									<td id="srSummarySrNumber">
									</td>
								</tr>
								<tr>
									<td width="35%">Description:</td>
									<td id="srSummaryDescription" >
										${actionForm.srDescription}
									</td>
								</tr>
								<tr>
									<td width="35%">Owner:</td>
									<td id="srSummaryOwner" >
										${actionForm.srOwnerName}
									</td>
								</tr>
								<tr>
									<td valign="top" width="35%">Completion Narrative:</td>
									<td >
										<textarea id="srCompletionNarrative" rows="3"	cols = "20" disabled="true"	>
											${actionForm.srCompletionNarrative}
										</textarea>
									</td>
								</tr>
								<tr>
									<td width="35%">Note to Avaya</td>
									<td >
										<input id="srSummarySrNote" class="required"	maxlength="150"/>
										<div style="display:none" id="accountLocation" ></div>
									</td>
								</tr>
								<tr>
									<td align="center" colspan="2">
										<input type="button" class="button gray" value="Submit" onclick="saveSrSummaryInfo()" />
									</td>
								</tr>
							</table>
						</p>
					</div>
				</div>
			</div>
			<!-- end srSummaryPopup -->

			<!-- start managedElementsRemainderPopUp -->
			<div class="modal managedEl" style="display:none;">
				<div class="modal-overlay">
					<div class="modal-content">
						<h2 class="title">Acknowledgement</h2>
						<p class="content">
							<img class="msg-icon"
								src="<c:url context="${ _path }" value="/styles/images/messages/icon_warning_light_60x60.png" />"
								alt="warning" />
							<span class="msg-txt">${grtConfig.managedElementsRemainder} this <a target="_blank" href="${grtConfig.managedElementsRemainderKB}">KB article</a>.</span>
						</p>
						<div class="controls">
							<a class="button gray alertOkBtn"	onclick="closePopUp()" >OK</a>
						</div>
					</div>
				</div>
			</div>
			<!-- end managedElementsRemainderPopUp -->


		</form>
		<!-- end tecRegDetails -->

		<!-- start error msg -->
		<input type="hidden" id="alertSellOneRecForSALMig" value="${grtConfig.alertSellOneRecForSALMig}" />

		<input type="hidden" id="alertInfoRetCode" value="${grtConfig.alertInfoRetCode}" />
		<input type="hidden" id="cnfrmNavigationToSALAlarm" value="${grtConfig.cnfrmNavigationToSALAlarm}" />
		<!-- end error messages -->


		<!-- Error Codes -->
		<input type="hidden" id="alertSellOneRecForSALMigErrorCode" value="${grtConfig.ewiMessageCodeMap['alertSellOneRecForSALMig']}" />
		<input type="hidden" id="alertInfoRetCodeInfCode" value="${grtConfig.ewiMessageCodeMap['alertInfoRetCode']}" />
		<input type="hidden" id="cnfrmNavigationToSALAlarmConfCode" value="${grtConfig.ewiMessageCodeMap['cnfrmNavigationToSALAlarm']}" />

	</div>
	<!-- end page content -->
</div>
<!-- end page content-wrap -->
