<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.grt.dto.TechnicalOrderDetail"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.grt.dto.RegistrationFormBean" %>
<%@ page import="java.util.List"%>
<%@ page import="com.grt.dto.Pager"%>
<%@ include file="/includes/context.jsp" %>


<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/views/sal-step-b.css" />"/>
<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/plugins/jquery.dataTables_themeroller.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/plugins/jquery-ui.css" />"/>

<script type="text/javascript"
	src="<c:url context="${ _path }" value="/scripts/plugins/jquery.dataTables.min.js" />"></script>
<script type="text/javascript"
	src="<c:url context="${ _path }" value="/scripts/plugins/jquery.dataTables.columnFilter.js" />"></script>
<script type="text/javascript"
	src="<c:url context="${ _path }" value="/scripts/plugins/dynamicTable.js" />"></script>
<script type="text/javascript"
	src="<c:url context="${ _path }" value="/scripts/salmigration/grt.sal.stepb.js" />"></script>
	
<script type="text/javascript">
	function cancelAction()
	{
		$("div.loading-overlay").show();
		document.getElementById('salStepB').action = document.getElementById('techOrderUrl').href;
		document.getElementById('salStepB').submit();
		return false;
	}
</script>

<!-- start page content-wrap -->
<div class="content-wrap sal-step-b">

	<h1 class="page-title">SAL Connectivity and Alarm Testing</h1>

	<!-- start page content -->
	<div class="content">

		<!-- start registration summary -->
		<div class="registration-site-summary collapse-box">
			<h2 class="collapse-box-header active">Registration Site Summary <a href="#"><span></span></a></h2>

			<div class="data collapse-box-container">
				<div class="col">
					<p><span class="title">Registration ID:  </span>${actionForm.registrationId}</p>
				</div>
				<div class="col">
					<p><span class="title">Sold To/FL: </span>${actionForm.soldToId}</p>
				</div>
				<div class="col">
					<p><span class="title">Customer Name: </span>${actionForm.company}</p>
				</div>
			</div>
		</div>
		<!-- end registration summary -->

		<!-- start salStepB -->
		<form action="submitSalAlarmConnectivityDetails" id="salStepB" method="post">

			<!-- start SAL step B  -->
			<div id="exist-install-table-wrap" class="data-table-wrap collapse-box">
				<h2 class="collapse-box-header active">Existing Install Base
					<a href="#">
						<span></span>
					</a>
				</h2>
				<div class="data collapse-box-container">
					<!-- start salStepBTable  -->
					<div id="productListTableDiv" class="basic-table-wrap">
						<table id="salStepBTable" class="basic-table">
								<tr>
									<th width="25">Material Code</th>
									<th width="25">SeCode</th>
									<th width="50">Model</th>
									<th width="25">Product ID (Alarm ID)</th>
									<th width="25">SEID</th>
									<th width="25">System ID (SID)</th>
									<th width="25">Module ID (MID)</th>
									<th width="25">Administered?</th>
									<th width="25">Remote Access Eligible</th>
									<th width="25">Select for Remote Access</th>
									<th width="25">Transport Alarm Eligible</th>
									<th width="25">Select for Alarming</th>
									<th width="25">Last Alarm Received Date</th>
									<th width="25">SAL Gateway SEId</th>
									<th width="25">Status</th>
									<th width="25">Entitled For Alarming</th>
								</tr>

							<c:forEach items="${ actionForm.selectedSALRegSummaryList }" var="item" varStatus="container">
								<tr>
									<td>${item.technicalOrder.materialCode}</td>
									<td>${item.solutionElement} </td>
									<td>${item.model}</td>
									<td>${item.alarmId}</td>
									<td>${item.solutionElementId}</td>
									<td>${item.sid}</td>
									<td>${item.mid}</td>
									<td>${(not empty item.existence and item.existence!="null") ? item.existence : ""}</td>
									<td>${item.remoteAccess}</td>
									<td  >
										<input type="checkbox" value="true" name="actionForm.selectedSALRegSummaryList[${container.index}].selectForRemoteAccess" id="ra${container.index}Select" ${item.remoteAccessCheckBoxDisabled ?'disabled':'' } ${ item.selectForRemoteAccess ? 'checked' : '' } />
										<input type="hidden" name="actionForm.selectedSALRegSummaryList[${container.index}].selectForRemoteAccess" value="false"/>
									</td>

									<td>${item.transportAlarm}</td>
									<td  >
										<input type="checkbox" value="true" name="actionForm.selectedSALRegSummaryList[${container.index}].selectForAlarming" id="ta${container.index}Select" ${item.alarmingCheckBoxDisabled ?'disabled':''} ${ item.selectForAlarming ? 'checked' : '' } />
										<input type="hidden" name="actionForm.selectedSALRegSummaryList[${container.index}].selectForAlarming" value="false"/>
									</td>

									<td>${(not empty item.deviceLastAlarmReceivedDate and item.deviceLastAlarmReceivedDate!="null") ?item.deviceLastAlarmReceivedDate:""}</td>
									<td>${item.primarySalGWSeid}${item.secondarySalGWSeid}</td>
									<td>
										<c:if test="${ item.selectForRemoteAccess or item.selectForAlarming }">
											${item.stepBStatus.statusShortDescription}
										</c:if>
									</td>
									<td>${item.entitledForAlarming}</td>
								</tr>

								<c:forEach items="${ item.expSolutionElements }" var="item1" varStatus="container1">
									<tr>
											<td> &nbsp;&nbsp;&nbsp; </td>
											<td>${item1.seCode}</td>
											<td>${item1.model}</td>
											<td>${item1.alarmId}</td>
											<td>${item1.seID}</td>

											<td>${item1.sid}</td>
											<td>${item1.mid}</td>
											<td>${(not empty item1.existence and item1.existence!="null") ? item1.existence : ""}</td>

											<td>${item1.remoteAccessEligible}</td>
											<td  >
												<input type="checkbox" value="true" name="actionForm.selectedSALRegSummaryList[${container.index}].expSolutionElements[${ container1.index }].selectForRemoteAccess"  id="${container.index}childRa${container1.index}Select" ${item1.remoteAccessCheckBoxDisabled?'disabled':''}  ${item1.selectForRemoteAccess?'checked':'' } />
												<input type="hidden" name="actionForm.selectedSALRegSummaryList[${container.index}].expSolutionElements[${ container1.index }].selectForRemoteAccess" value="false"/>
											</td>

											<td>${item1.transportAlarmEligible}</td>
											<td  >
												<input type="checkbox" value="true" name="actionForm.selectedSALRegSummaryList[${container.index}].expSolutionElements[${ container1.index }].selectForAlarming" id="${container.index}childTa${container1.index}Select" ${item1.alarmingCheckBoxDisabled?'disabled':''}  ${ item1.selectForAlarming?'checked':'' } />
												<input type="hidden" name="actionForm.selectedSALRegSummaryList[${container.index}].expSolutionElements[${ container1.index }].selectForAlarming" value="false"/>
											</td>

											<td>${not empty item1.deviceLastAlarmReceivedDate and item1.deviceLastAlarmReceivedDate!="null" ?item.deviceLastAlarmReceivedDate:""}</td>
											<td>${item1.primarySalGWSeid}${item1.secondarySalGWSeid}</td>
											<td>${item1.retestStatus}</td>
											<td> &nbsp;&nbsp;&nbsp; </td>											
									</tr>
								</c:forEach>
							</c:forEach>

							<c:forEach items="${ actionForm.selectedSALMigrationSummaryList }" var="item" varStatus="container">
								<tr>
									<td>${item.materialCode}</td>
									<td>${item.seCode} </td>
									<td>${item.model}</td>
									<td>${item.alarmId}</td>
									<td>${item.solutionElementId}</td>
									<td>${item.sid}</td>
									<td>${item.mid}</td>
									<td>${(not empty item.existence and item.existence!="null") ? item.existence : ""}</td>
									<td>${item.remoteAccess}</td>
									<td>
										<input type="checkbox" value="true" name="actionForm.selectedSALMigrationSummaryList[${container.index}].selectForRemoteAccess" id="ra${container.index}Select" ${item.remoteAccessCheckBoxDisabled?'disabled':''} ${ item.selectForRemoteAccess ?'checked':'' } />
										<input type="hidden" name="actionForm.selectedSALMigrationSummaryList[${container.index}].selectForRemoteAccess" value="false"/>
									</td>
									<td>${item.transportAlarm}</td>
									<td>
										<input type="checkbox" value="true" name="actionForm.selectedSALMigrationSummaryList[${container.index}].selectForAlarming"  id="ta${container.index}Select" ${item.alarmingCheckBoxDisabled?'disabled':''} ${ item.selectForAlarming ?'checked':'' }/>											<input type="hidden" name="actionForm.selectedSALMigrationSummaryList[${container.index}].selectForRemoteAccess" value="false"/>
										<input type="hidden" name="actionForm.selectedSALMigrationSummaryList[${container.index}].selectForAlarming" value="false"/>
									</td>
									<td>${(not empty item.deviceLastAlarmReceivedDate and item.deviceLastAlarmReceivedDate!="null") ?item.deviceLastAlarmReceivedDate:""}</td>
	    							<td>${item.primarySALGatewaySEID}&nbsp;+&nbsp;${item.secondarySALGatewaySEID}</td>
									<td>
										<c:if test="${ item.selectForRemoteAccess or item.selectForAlarming }">
											${item.stepBStatus.statusShortDescription}
										</c:if>
									</td>
									<td> &nbsp;&nbsp;&nbsp; </td>
								</tr>

							 	<c:forEach items="${ item.expSolutionElements }" var="item1" varStatus="container1">
									<tr>
										<td> &nbsp;&nbsp;&nbsp; </td>
										<td>${item1.seCode}</td>
										<td>${item1.model}</td>
										<td>${item1.alarmId}</td>
										<td>${item1.seID}</td>

										<td>${item1.sid}</td>
										<td>${item1.mid}</td>
										<td>${(not empty item1.existence and item1.existence!="null") ? item1.existence : ""}</td>
										<td>${item1.remoteAccessEligible}</td>
										<td>
											<input type="checkbox" value="true" name="actionForm.selectedSALMigrationSummaryList[${container.index}].expSolutionElements[${ container1.index }].selectForRemoteAccess" id="${container.index}childRa${container1.index}Select" ${item1.remoteAccessCheckBoxDisabled?'disabled':''}  ${ item1.selectForRemoteAccess ?'checked':'' }  />
											<input type="hidden" name="actionForm.selectedSALMigrationSummaryList[${container.index}].expSolutionElements[${ container1.index }].selectForRemoteAccess" value="false"/>
										</td>

										<td>${item1.transportAlarmEligible}</td>
										<td>
											<input type="checkbox" value="true" name="actionForm.selectedSALMigrationSummaryList[${container.index}].expSolutionElements[${ container1.index }].selectForAlarming"  id="${container.index}childTa${container1.index}Select" ${item1.alarmingCheckBoxDisabled ? 'disabled':''}  ${ item1.selectForAlarming ?'checked':'' } />
											<input type="hidden" name="actionForm.selectedSALMigrationSummaryList[${container.index}].expSolutionElements[${ container1.index }].selectForAlarming" value="false"/>
										</td>

										<td>${not empty item1.deviceLastAlarmReceivedDate and item1.deviceLastAlarmReceivedDate!="null" ?item.deviceLastAlarmReceivedDate:""}</td>
										<td>${item1.primarySalGWSeid}&nbsp;+&nbsp;${item1.secondarySalGWSeid}</td>
										<td>${item1.retestStatus}</td>
										<td> &nbsp;&nbsp;&nbsp; </td>								
									</tr>
								</c:forEach>
							</c:forEach>
						</table>
					</div>
					<!-- end salStepBTable -->
				</div>
			</div>
			<!-- end SAL step B  -->

			<%
			String detailView = (String)request.getSession().getAttribute(com.grt.util.GRTConstants.STEPB_DETAIL_VIEW);
			request.getSession().removeAttribute(com.grt.util.GRTConstants.STEPB_DETAIL_VIEW);
			if(com.grt.util.GRTConstants.TRUE.equals(detailView)){
			%>

			<div class="page-note">
				<p>Please Note: The details displayed represent the status at the time of submission and does not represent live data.  To exit this screen, please select the Cancel button.</p>
			</div>

			<% } %>

			<!-- sart controls -->
			<div class="controls">
				<input type="button" value="cancel" class="button gray" onclick=" cancelAction()" />&nbsp;
				<%
				if(com.grt.util.GRTConstants.TRUE.equals(detailView)){
				%>
				&nbsp;
				<%} else { %>
				<input type="button" value="submit" class="button gray" onClick="return processBeforeSubmit()"/>
				<% }%>
			</div>
			<!-- end controls -->
			
			<!-- Messages -->
			<input type="hidden" value="${ grtConfig.salSomethingWrongErrMsg}" id="salSomethingWrongErrMsg" />
			<input type="hidden" value="${ grtConfig.salRemoteAccessReqErrMsg}" id="salRemoteAccessReqErrMsg" />
			<input type="hidden" value="${ grtConfig.salSeleSeidRequiedMsg}" id="salSeleSeidRequiedMsg" />
			<input type="hidden" value="${ grtConfig.salValidateConnectivityConfirmMsg}" id="salValidateConnectivityConfirmMsg" />
			
			<!-- Error Codes -->
			
			<input type="hidden" value="${grtConfig.ewiMessageCodeMap['salSomethingWrongErrMsg']}" id="salSomethingWrongErrMsgCode" />
			<input type="hidden" value="${grtConfig.ewiMessageCodeMap['salRemoteAccessReqErrMsg']}" id="salRemoteAccessReqErrMsgCode" />
			<input type="hidden" value="${grtConfig.ewiMessageCodeMap['salSeleSeidRequiedMsg']}" id="salSeleSeidRequiedMsgCode" />
			<input type="hidden" value="${grtConfig.ewiMessageCodeMap['salValidateConnectivityConfirmMsg']}" id="salValidateConnectivityConfirmMsgCode" />
			
		</form>
		<!-- end salStepB form -->

		<input type="hidden" id="message" value="${ message }"/>
		<a href="<c:url context="${ _path }" value="/technicalonboarding/getTechnicalOrderDetails.action" />" id="techOrderUrl"></a>
		<a id="submitSalAlarmConnectivityDetailsAction" href="<c:url context="${ _path }" value="/technicalonboarding/submitSalAlarmConnectivityDetails.action"/>"></a>	

	</div>
	<!-- end page content -->
</div>
<!-- end page content-wrap -->
