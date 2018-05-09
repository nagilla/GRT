<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/includes/context.jsp"%>

<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/views/registration-list.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/plugins/jquery.dataTables_themeroller.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/plugins/jquery-ui.css" />" />

<script type="text/javascript"
	src="<c:url context="${ _path }" value="/scripts/plugins/jquery.dataTables.min.js" />"></script>
<script type="text/javascript"
	src="<c:url context="${ _path }" value="/scripts/plugins/jquery.dataTables.columnFilter.js" />"></script>
<script type="text/javascript"
	src="<c:url context="${ _path }" value="/scripts/common-flow/grt.registrationList.model.js" />"></script>
<script type="text/javascript"
	src="<c:url context="${ _path }" value="/scripts/common-flow/grt.registrationList.view.js" />"></script>
<script type="text/javascript"
	src="<c:url context="${ _path }" value="/scripts/installbase/registration.js" />"></script>
<script type="text/javascript">
	<c:if test="${ requestScope.regListBPLinkIdError ne null }">
	window.onload = function() {
		convertAlertToModelPopUpServerSideMsg("${requestScope.regListBPLinkIdError}");		
	};
	</c:if>
</script>

<!-- start page content-wrap -->
<div class="content-wrap registration-list">

	<h1 class="page-title">Registration List</h1>

	<!-- start page content -->
	<div class="content">
		<input type="hidden" value="${ grtConfig.regSelectedCancRegErrMsg}" id="regSelectedCancRegErrMsg" />
		<input type="hidden" value="${ grtConfig.regInvalidEmailErrMsg}" id="regInvalidEmailErrMsg" />
		<!-- code -->
		<input type="hidden" value="${grtConfig.ewiMessageCodeMap['regSelectedCancRegErrMsg']}" id="regSelectedCancRegErrMsgCode" />
		<input type="hidden" value="${grtConfig.ewiMessageCodeMap['regInvalidEmailErrMsg']}" id="regInvalidEmailErrMsgCode" />


		<div id="registrationContent">

			<input type="hidden" id="regTypeList" value="${ regTypeList }" /> <input
				type="hidden" id="statusList" value="${ statusList }" /> <input
				type="hidden" id="maxRegListSize" value="${ maxRegListSize }" /> <input
				type="hidden" id="dispListSize" value="${ regListRowNum }" />
			<input type="hidden" class="hidden_reg_id" value=""/>
			<input type="hidden" class="filterValue" id="filterValue" value="ASC"/>
			<div class="page-note">
			 	<%
				com.avaya.grt.web.security.CSSPortalUser user = (com.avaya.grt.web.security.CSSPortalUser) (request.getSession().getAttribute("cssUserProfile"));%>
			 	<%--	if (user != null && ( user.getUserType().equalsIgnoreCase("A"))){ 
						<p>To filter the registrations in this list, type one or more values in the text field(s) above each column and press Enter.</p>
				<%} else if (user != null && ( user.getUserType().equalsIgnoreCase("B"))){ %>
						<p>Filtering Logic Updated: In absence of user entered filters, registration/s will now automatically be filtered for the logged in requester’s name.</p>
						<p>HINT: The more specific your filtering criteria (SoldTo, Registration ID, etc), the faster your list results will appear.</p>
				<%} %> --%>
				
				<p>You can filter the registrations in this list by typing values in the text fields or using the filtering options above each column.</p>

				<p>To remove any existing filters, click the CLEAR FILTERS button below.</p>

				<div class="controls">
					<input type="checkbox" id="excludeCancelledStatus" value="exCanStatus"
						checked /><span class="exCancelledTitle">Exclude Cancelled Registrations</span>
					<input type="button" value="Clear Filters" class="button gray" id="clearFilters" />
				</div>

			</div>

			<!-- start existing install base -->
			<div class="data-table-wrap collapse-box">
				<h2 class="collapse-box-header active">Registration List
					<a href="#">
						<span></span>
					</a>
				</h2>
				<div class="data collapse-box-container">
					<table id="registrationlist" class="regListTable display compact" style="width: 100%">
						<thead>
							<tr>
								<td>Registration ID#</td>
								<td>Submitted By</td>
								<td>Registration Name</td>
								<td>Notification Name</td>
								<td>Notification Email</td>
								<td>Date Created</td>
								<td>Date Last Updated</td>
								<td>Last Updated By</td>
								<td>Registration Type</td>
								<td>Install Base Creation Status</td>
								<td>Technical Onboarding Status</td>
								<td>Record Validation Status</td>
								<td>Equipment Move Status</td>
								<td>Sold To/FL</td>
								<td>Customer Name</td>
								<td>SR#</td>
								<td></td>
							</tr>
							<tr>
								<td>Registration ID#</td>
								<td>Submitted By</td>
								<td>Registration Name</td>
								<td>Notification Name</td>
								<td>Notification Email</td>
								<td>Date Created</td>
								<td>Date Last Updated</td>
								<td>Last Updated By</td>
								<td>Registration Type</td>
								<td>Install Base Creation Status</td>
								<td>Technical Onboarding Status</td>
								<td>Record Validation Status</td>
								<td>Equipment Move Status</td>
								<td>Sold To/FL</td>
								<td>Customer Name</td>
								<td>SR#</td>
								<td></td>
							</tr>
						</thead>
					</table>
				</div>
			</div>

		</div>


		<% pageContext.setAttribute("time",new Date().getTime()); %>


		<div class="controls">
			<a href="<c:url context="${ _path }"	value="/home-action.action"> <c:param name="reqid" value="${time}"/>	</c:url>"
				class="button gray homebtn"> Home </a>
		</div>

		<input type="hidden" id="registrationId_f" value="${ actionForm.registrationId }" />
		<c:set var="requesterName" value="${ requesterName }"/>
		<%
			if (user != null && ( user.getUserType().equalsIgnoreCase("B"))){ %>
				<input type="hidden" id="requesterName_f" value="${ requesterName }" />
		<% } %>

		<!-- start modal email-window -->
		<div id="email-window" class="modal" style="display: none;">
			<div class="modal-overlay">
				<div class="modal-content">
					<a class="close" onclick="closeEmailPopup();"><i class="fa fa-close"></i></a>
					<h2 class="title">On-boarding File Request</h2>
					<p class="content">
						<img class="msg-icon"
							src="<c:url context="${ _path }" value="/styles/images/messages/icon_warning_light_60x60.png" />"
							alt="info" />
						<span class="msg-txt">Enter Email ID for on-boarding file request.
							<br><input type="text" id="userEmail" />
						</span>
					</p>
					<div class="controls">
						<input type="button" class="button gray" value="Yes" id="sendEmail" />
					</div>
				</div>
			</div>
		</div>
		<!-- end modal email-window -->

		<!-- start modal srSummaryPopup -->
		<div id="srSummaryPopup" class="modal" style="display: none;">
			<div class="modal-overlay">
				<div class="modal-content">
					<a class="close" onclick="closeSummaryPopup();"><i class="fa fa-close"></i></a>
					<h2 class="title">SR Summary</h2>

					<div class="content">
						<img class="msg-icon"
							src="<c:url context="${ _path }" value="/styles/images/messages/icon_warning_light_60x60.png" />"
							alt="info" />
						<div class="msg-txt">
							<table class="modal-summary" width="100%" cellpadding="10" cellspacing="10">
								<tr>

									<td>SR#: </td>
									<td id="srSummarySrNumber">
									</td>
								</tr>
								<tr>
									<td>Description: </td>
									<td id="srSummaryDescription" >
										${actionForm.srDescription}
									</td>
								</tr>
								<tr>

									<td>Owner: </td>
									<td id="srSummaryOwner" >
										${actionForm.srOwnerName}
									</td>
								</tr>
								<tr>
									<td>Completion Narrative: </td>

									<td>
										<textarea id="srCompletionNarrative" rows="3"	cols = "20" disabled="true"	>
											${actionForm.srCompletionNarrative}
										</textarea>
									</td>
								</tr>
								<tr>
									<td>Note to Avaya: </td>

									<td>
										<input id="srSummarySrNote" class="required"	maxlength="150"/>
										<div style="display:none" id="accountLocation" ></div>
									</td>
								</tr>


							</table>
						</div>
					</div>
					<div class="controls">
						<input type="button" class="button gray" value="Submit" onclick="saveSrSummaryInfo()" />




					</div>
				</div>
			</div>
		</div>
		<!-- end modal srSummaryPopup -->

	</div>
	<!-- end page content -->

</div>
<!-- end page content-wrap -->

<%-- ************************************************************* --%>
<%-- ***** BELOW IS DEPRECATED CODE - NEED REVIEW AND REMOVE ***** --%>
<%-- ************************************************************* --%>
