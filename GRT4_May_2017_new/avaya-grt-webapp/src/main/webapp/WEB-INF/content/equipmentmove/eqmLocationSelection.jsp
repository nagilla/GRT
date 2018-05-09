<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/includes/context.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/views/customer-site-registration.css" />"/>
<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/equipmentmove/grt.eqmsoldto.view.js" />"></script>

<!-- start page content-wrap -->
<div class="content-wrap customer-site-registration" id="locationWrapper">

	<h1 class="page-title">Customer Site Registration</h1>

	<!-- start page content -->
	<div class="content">

		<!-- start customerSiteReg -->
		<form id="eqpMoveSoldToForm" method="post" class="sold-to-form" action="<c:url context="${ _path}" value="/technicalregsitration/newRegistrationWithSoldToValidation.action"/>" name="customer_site_reg">
			<s:token />
			<input type="hidden" name="actionForm.userId" value="${ actionForm.userId }" />
			<input type="hidden" name="actionForm.eqmFromSoldTo" id="fromSoldToHidden" value="" />
			<input type="hidden" name="actionForm.eqmToSoldTo"  id="toSoldToHidden"  value="" />
			<input type="hidden" name="actionForm.soldToId" value="" id="soldToId"/>
			<%
				com.avaya.grt.web.security.CSSPortalUser user = (com.avaya.grt.web.security.CSSPortalUser) (request.getSession().getAttribute("cssUserProfile"));
				if (user != null && user.getUserType().equalsIgnoreCase("A") ){
			%>
				<input type="hidden" id="userType" value="employee" />
				<input type="hidden" id="userRole" name="actionForm.userRole" value="${ actionForm.userRole }"/>
			<%	} %>

			<p>Please enter End Customer Sold To (Functional Location) Number:</p>
			<div class="inputs">
				<label for="fromSoldTo">From Sold To/Functional Location:</label><input type="text" id="fromSoldTo"	class="search-fromsoldto-input" />
			</div>
			<div class="inputs">
				<label for="toSoldTo">To Sold To/Functional Location:</label><input type="text" id="toSoldTo"	class="search-tosoldto-input" />
			</div>

			<!-- Start : Employee User Type Select Popup -->
			<div class="associate-modal modal" id="usertype-popup" style="display:none;">
				<div class="modal-overlay">
					<div class="modal-content">
						<a class="close closeUserTypeImg"><i class="fa fa-close"></i></a>
						<p class="title">From Sold To <span id="fsoldtoMsg"></span></p>
						<p class="title">To Sold To <span id="tsoldtoMsg"></span></p>
						<div class="page-note">
								${ grtConfig.agentUserTypeMsg }
							<p>
								<input type="radio" name="optype" value="BP" checked onclick="checkOption(this.value);"/>
								${ grtConfig.agentBPUserTypeMsg }
							</p>
							<p>
								<input type="radio" name="optype" value="C" onclick="checkOption(this.value);">
								${ grtConfig.agentCustUserTypeMsg }
							</p>
							<div class="controls">
								<input  type="button" value="Proceed" id="agentProceed" class="button gray" onClick="closeusertypePopup();return false;"/>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- End : Employee User Type Select Popup -->

			<div class="controls">
					<input type="button" value="Cancel" class="cancelBtn button gray"/> &nbsp;&nbsp;
					<%
						if (user != null && user.getUserType().equalsIgnoreCase("A") ){
					%>
						<input type="button" id="searchAgent" value="Search" class="button nextBtn gray"  onclick="closeusertypePopup(); return false;" />
						<a id="newRegistrationWithSoldToValidationForAgentAction" href="<c:url context="${ _path }" value="/technicalregsitration/newRegistrationWithSoldToValidationForAgent" />"></a>

					<%} else { %>
						<input type="button" value="Next" id="eqpMoveNext" class="button nextBtn gray" />
						<a id="newRegistrationWithSoldToValidation" href="<c:url context="${ _path}" value="/technicalregsitration/newRegistrationWithSoldToValidation.action"/>"></a>
					<% } %>
			</div>
			
			<!-- Error Messages -->
			<input type="hidden" id="eqmIncorrectFromSoldToErrMsg" value="${grtConfig.eqmIncorrectFromSoldToErrMsg}" />
			<input type="hidden" id="eqmIncorrectToSoldToErrMsg" value="${grtConfig.eqmIncorrectToSoldToErrMsg}" />
			<input type="hidden" id="eqmIncorrectFromAndToSoldToErrMsg" value="${grtConfig.eqmIncorrectFromAndToSoldToErrMsg}" />
			<input type="hidden" id="eqmUnauthorizedSoldToErrMsg" value="${grtConfig.eqmUnauthorizedSoldToErrMsg}" />
			<input type="hidden" id="eqmSameFromAndToSoldToErrMsg" value="${grtConfig.eqmSameFromAndToSoldToErrMsg}" />
			<!-- End Error Messages -->
			
			<!-- Error Codes -->
			<input type="hidden" id="eqmIncorrectFromSoldToErrMsgCode" value="${grtConfig.ewiMessageCodeMap['eqmIncorrectFromSoldToErrMsg']}" />
			<input type="hidden" id="eqmIncorrectToSoldToErrMsgCode" value="${grtConfig.ewiMessageCodeMap['eqmIncorrectToSoldToErrMsg']}" />
			<input type="hidden" id="eqmIncorrectFromAndToSoldToErrMsgCode" value="${grtConfig.ewiMessageCodeMap['eqmIncorrectFromAndToSoldToErrMsg']}" />
			<input type="hidden" id="eqmUnauthorizedSoldToErrMsgCode" value="${grtConfig.ewiMessageCodeMap['eqmUnauthorizedSoldToErrMsg']}" />
			<input type="hidden" id="eqmSameFromAndToSoldToErrMsgCode" value="${grtConfig.ewiMessageCodeMap['eqmSameFromAndToSoldToErrMsg']}" />
			<!-- End Error Codes -->
			
		</form>
		<!-- end customerSiteReg -->

		<div class="page-note">
			<span> ${ grtConfig.leadingZeroesNote } </span>
		</div>

		<div class="page-note">
			<span> ${ grtConfig.soldToAutoNoteMsg } </span>
		</div>
	</div>
	<!-- end page content -->
</div>
<!-- end page content-wrap -->

<%-- ************************************************************* --%>
<%-- ***** BELOW IS DEPRECATED CODE - NEED REVIEW AND REMOVE ***** --%>
<%-- ************************************************************* --%>

<%--
			<c:if test="${ requestScope.soldToError!=null }">
					<input type="hidden" id="soldToValidationErr" value="${ requestScope.soldToError }"/>
			</c:if>
 --%>
