<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/includes/context.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/views/site-contact-validation.css" />"/>

<script src='<c:url context="${ _path }" value="/scripts/plugins/jquery_maskedinput.js" />'></script>
<script type="text/javascript" src='<%=request.getContextPath()+"/scripts/plugins/dynamicTable.js"%>'></script>
<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/common-flow/grt.newregistration.view.js" />"></script>
<% 
	//For avoiding caching issue
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
	response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
	response.setDateHeader("Expires", 0); // Proxies.
%>
<!-- start page content-wrap -->
<div class="content-wrap site-contact-validation">

	<h1 class="page-title">Site Contact Validation</h1>

	<!-- start page content -->
	<div clas="content">

		<form id="saveSiteRegistrationForm" action="<c:url context="${_path}" value="/technicalregsitration/saveSiteRegistration.action"/>" method="post">
			<s:token></s:token>
			<input type="hidden" value="${ actionForm.poentered }" name="actionForm.poentered" id="poentered" />
			<input type="hidden" value="${ fn:escapeXml(actionForm.userRole) }" name="actionForm.userRole" id="userRole" />
			
			<!-- Error Messages -->
			<input type="hidden" id="scvInvalidPhoneNoErrMsg" value="${grtConfig.scvInvalidPhoneNoErrMsg}" />
			<input type="hidden" id="scvPurchaseOrderReqErrMsg" value="${grtConfig.scvPurchaseOrderReqErrMsg}" />
			<input type="hidden" id="scvEmptyFirstNameErrMsg" value="${grtConfig.scvEmptyFirstNameErrMsg}" />
			<input type="hidden" id="scvEmptyFirstLastErrMsg" value="${grtConfig.scvEmptyFirstLastErrMsg}" />
			<input type="hidden" id="scvInvalidEmailErrMsg" value="${grtConfig.scvInvalidEmailErrMsg}" />
			<input type="hidden" id="scvEmptyNotifEmailErrMsg" value="${grtConfig.scvEmptyNotifEmailErrMsg}" />
			<input type="hidden" id="scvInvalidDate" value="${grtConfig.scvInvalidDate}" />
			<input type="hidden" id="scvCutoverDateErrMsg" value="${grtConfig.scvCutoverDateErrMsg}" />
			<input type="hidden" id="scvMaxCharErrMsg" value="${grtConfig.scvMaxCharErrMsg}" />			
			<input type="hidden" id="scvTypedTextMsg" value="${grtConfig.scvTypedTextMsg}" />			
			<input type="hidden" id="scvZipLengthErrMsg" value="${grtConfig.scvZipLengthErrMsg}" />
			<input type="hidden" id="scvInvalidCharInZipErrMsg" value="${grtConfig.scvInvalidCharInZipErrMsg}" />
			<input type="hidden" id="scvZipCodeFormaterrMsg" value="${grtConfig.scvZipCodeFormaterrMsg}" />
			<input type="hidden" id="scvEmptyGrtNotiPhNoErrMsg" value="${grtConfig.scvEmptyGrtNotiPhNoErrMsg}" />
			<input type="hidden" id="scvEmptyPhNoErrMsg" value="${grtConfig.scvEmptyPhNoErrMsg}" />
			<input type="hidden" id="scvInvalidGrtNotiPhNoErrMsg" value="${grtConfig.scvInvalidGrtNotiPhNoErrMsg}" />
			<input type="hidden" id="scvInvalidPhNoErrMsg" value="${grtConfig.scvInvalidPhNoErrMsg}" />			
			
			<!--  Error codes -->

			<input type="hidden" id="scvInvalidPhoneNoErrMsgCode" value="${grtConfig.ewiMessageCodeMap['scvInvalidPhoneNoErrMsg']}" />
			<input type="hidden" id="scvPurchaseOrderReqErrMsgCode" value="${grtConfig.ewiMessageCodeMap['scvPurchaseOrderReqErrMsg']}" />
			<input type="hidden" id="scvEmptyFirstNameErrMsgCode" value="${grtConfig.ewiMessageCodeMap['scvEmptyFirstNameErrMsg']}" />
			<input type="hidden" id="scvEmptyFirstLastErrMsgCode" value="${grtConfig.ewiMessageCodeMap['scvEmptyFirstLastErrMsg']}" />
			<input type="hidden" id="scvInvalidEmailErrMsgCode" value="${grtConfig.ewiMessageCodeMap['scvInvalidEmailErrMsg']}" />
			<input type="hidden" id="scvEmptyNotifEmailErrMsgCode" value="${grtConfig.ewiMessageCodeMap['scvEmptyNotifEmailErrMsg']}" />
			<input type="hidden" id="scvInvalidDateCode" value="${grtConfig.ewiMessageCodeMap['scvInvalidDate']}" />
			<input type="hidden" id="scvCutoverDateErrMsgCode" value="${grtConfig.ewiMessageCodeMap['scvCutoverDateErrMsg']}" />
			<input type="hidden" id="scvMaxCharErrMsgCode" value="${grtConfig.ewiMessageCodeMap['scvMaxCharErrMsg']}" />
			<input type="hidden" id="scvTypedTextMsgCode" value="${grtConfig.ewiMessageCodeMap['scvTypedTextMsg']}" />			
			<input type="hidden" id="scvZipLengthErrMsgCode" value="${grtConfig.ewiMessageCodeMap['scvZipLengthErrMsg']}" />
			<input type="hidden" id="scvInvalidCharInZipErrMsgCode" value="${grtConfig.ewiMessageCodeMap['scvInvalidCharInZipErrMsg']}" />
			<input type="hidden" id="scvZipCodeFormaterrMsgCode" value="${grtConfig.ewiMessageCodeMap['scvZipCodeFormaterrMsg']}" />
			<input type="hidden" id="scvEmptyGrtNotiPhNoErrMsgCode" value="${grtConfig.ewiMessageCodeMap['scvEmptyGrtNotiPhNoErrMsg']}" />
			<input type="hidden" id="scvEmptyPhNoErrMsgCode" value="${grtConfig.ewiMessageCodeMap['scvEmptyPhNoErrMsg']}" />
			<input type="hidden" id="scvInvalidGrtNotiPhNoErrMsgCode" value="${grtConfig.ewiMessageCodeMap['scvInvalidGrtNotiPhNoErrMsg']}" />
			<input type="hidden" id="scvInvalidPhNoErrMsgCode" value="${grtConfig.ewiMessageCodeMap['scvInvalidPhNoErrMsg']}" />
			
			<div class="form">
				<div class="first-col">
						<h5>Sold To Location</h5>
						<div class="input-row">
							<label>Registration Id</label>
							<input type="text" value="${ fn:escapeXml(actionForm.registrationId) }" readonly class="disabled-form" />
							<input type="hidden" name="actionForm.registrationId" value="${ fn:escapeXml(actionForm.registrationId) }" />
						</div>
						<div class="input-row">
							<label>Sold To</label>
							<input type="text" value="${ fn:escapeXml(actionForm.soldToId) }" readonly class="disabled-form"/>
							<input type="hidden" name="actionForm.soldToId" value="${ fn:escapeXml(actionForm.soldToId) }" />
						</div>
						<div class="input-row">
							<label>Company Name</label>
							<input type="text" value="${ fn:escapeXml(actionForm.company) }" readonly class="disabled-form"/>
							<input type="hidden" name="actionForm.company" value="${ fn:escapeXml(actionForm.company) }" id="company" />
						</div>
						<div class="input-row">
							<label>Phone Number</label>
							<input type="text" value="${ fn:escapeXml(actionForm.companyPhone) }" readonly class="disabled-form"/>
							<input type="hidden" id="companyPhoneNo" name="actionForm.companyPhone" value="${ fn:escapeXml(actionForm.companyPhone) }" />
						</div>
						<div class="input-row">
							<label>Site Country</label>
							<input type="text" value="${actionForm.siteCountry}" readonly class="disabled-form"/>
							<input type="hidden" name="actionForm.siteCountry" value="${ actionForm.siteCountry }" />
						</div>
						<div class="input-row">
							<label>Address Line 1</label>
							<input type="text" value="${actionForm.address1}" readonly class="disabled-form"/>
							<input type="hidden" name="actionForm.address1" value="${ actionForm.address1 }" />
						</div>
						<div class="input-row">
							<label>Address Line 2</label>
							<input type="text" value="${actionForm.address2}" readonly class="disabled-form"/>
							<input type="hidden" name="actionForm.address2" value="${ actionForm.address2 }" />
						</div>
						<div class="input-row">
							<label>City</label>
							<input type="text" value="${fn:escapeXml(actionForm.city)}" readonly class="disabled-form"/>
							<input type="hidden" name="actionForm.city" value="${ fn:escapeXml(actionForm.city) }" />
						</div>
						<div class="input-row">
							<label>State</label>
							<input type="text"	name="actionForm.state" value="${actionForm.state}" readonly class="disabled-form"/>
						</div>
						<div class="input-row">
							<label>Zip/Postal Code</label>
							<input type="text" value="${actionForm.zip}" readonly class="disabled-form"/>
							<input type="hidden" name="actionForm.zip" value="${ actionForm.zip }" />
						</div>
						<div class="input-row">
							<label>Registration Name <i class="fa fa-question-circle" title="Registration name is an optional field that can be used to help easily identify your registration"></i></label>
							<input type="text" id="registrationIdentifier"	name="actionForm.registrationIdentifier"	value="${ fn:escapeXml(actionForm.registrationIdentifier) }" size="30" maxlength="30" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} onKeyPress="return noEnter(event);"/>
						</div>
				</div>
				<div class="second-col">
						<h5>Reported By</h5>
						<div class="input-row">
							<label>First Name</label>
							<input type="text" value="${actionForm.firstName}" readonly class="disabled-form"/>
							<input type="hidden" name="actionForm.firstName" value="${actionForm.firstName}" id="firstName"/>
						</div>
						<div class="input-row">
							<label>Last Name</label>
							<input type="text" value="${actionForm.lastName}" readonly class="disabled-form"/>
							<input type="hidden" name="actionForm.lastName" id="lastName" value="${ actionForm.lastName }"/>
						</div>
						<div class="input-row">
							<label>Phone Number</label>
							<input type="text" id="onsitePhone" name="actionForm.reportedPhone" value="${ actionForm.reportedPhone}" size="20" maxlength="19" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}
								onKeyPress="return noEnter(event);" onKeyUp="copyPhone();" />
						</div>
						<div class="input-row">
							<label>Email Address</label>
							<input type="text" value="${ actionForm.reportedEmail }" readonly class="disabled-form"/>
							<input type="hidden" name="actionForm.reportedEmail" id="emailId" value="${ actionForm.reportedEmail }"/>
						</div>
						<div class="input-row">
							<label>Do You Want To Receive Email Notifications?</label>
							<select id="sendMail" name="actionForm.sendMail" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}>
								<option ${ (actionForm.sendMail == "Y") ? 'selected' : ''	} value="Y" >Y</option>
								<option ${ (actionForm.sendMail == "N") ? 'selected' : ''	} value="N" >N</option>
					 			 </select>
						</div>
					<h5>GRT Notification Contact</h5>
					<!-- <font color="red"><b>Note: The Phone Number field is no longer required below and the 'Same as Above' option is automatically selected.</b></font> -->
							<div class="input-row radio-group">
								<input type="radio" name="addressRadio" checked="checked" id="sameAsAbove" value="Same as Above" onClick="copysiteaddress();" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} onKeyPress="return noEnter(event);"/>
								<span class="sameAsAboveTxt">Same as Above</span>
								<input type="radio" name="addressRadio" id="enterManually" value="Enter Manually" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} onKeyPress="return noEnter(event);"/>
								<span>Enter Manually</span>
							</div>

							<div class="enter-manually" ng-show="enterManually == true">
								<div class="input-row">
									<label>First Name*</label>
									<input type="text" name="actionForm.onsiteFirstname" id="onsiteFirstName" value="${ actionForm.onsiteFirstname }"
										size="40" maxlength="50" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} onKeyPress="return noEnter(event);"/>
								</div>
								<div class="input-row">
									<label>Last Name*</label>
									<input type="text" name="actionForm.onsiteLastname" id="onsiteLastName" value="${actionForm.onsiteLastname}" size="40" maxlength="50"
										${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} onKeyPress="return noEnter(event);"/>
								</div>
								<div class="input-row">
									<label>Phone Number</label>
									<input type="text" id="sitePhone" name="actionForm.onsitePhone" value="${actionForm.onsitePhone}" size="20" maxlength="19"
										${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} onKeyPress="return noEnter(event);"/>
								</div>
								<div class="input-row">
									<label>Email Address* <i class="fa fa-question-circle" title="You can enter more than one email address by using a semicolon as separator"></i></label>
									<input type="text"	name="actionForm.onsiteemail" id="onsiteEmailId" value="${actionForm.onsiteemail}" size="40" maxlength="145"
										${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} onKeyPress="return noEnter(event);"/>
								</div>
							</div>
					</div>
						<div class="third-col">
							<h5>Registration Notes</h5>
							<textarea id="registrationNotes" name="actionForm.registrationNotes" rows="3" cols = "30" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} onblur="javascript:textLimit(this,4000);" onKeyPress="return noEnter(event);">${ fn:escapeXml(actionForm.registrationNotes) }</textarea>
						</div>

						<c:set var="userType" value="${sessionScope.cssUserProfile.userType}"/>
						<c:set var="region" value="${actionForm.region}"/>
					 <%if("NAR".equals(pageContext.getAttribute("region")) &&
							("B".equals(pageContext.getAttribute("userType")) || "BP".equals(pageContext.getAttribute("userType")))) { %>
						<div class="fourth-col" style="display:none;" id="AuthTr">
							<label class="osm_text" >Authorization Number</label>
								<label id="expediteRefNoDisplayField"></label>
								<input type="hidden" value="${ actionForm.expediteRefNo }" name="actionForm.expediteRefNo"
				 				id="expediteRefNo" />
						</div>
					<% } %>
			</div>

			<div class="controls">
					<input type="button" class="button gray homeBtn" value="Home"/>
				<input type="button" class="button gray" value="Back" id="backToLocationPage"/>
				<c:choose>
					<c:when	test="${ actionForm.installbaseRegistrationOnly }">
						<a id="backToLocationAction" href="<c:url context="${ _path }" value="/installbase/installbasehome"/>"></a>
						<a id="saveSiteRegAction" href="<c:url context="${ _path }" value="/technicalregsitration/saveSiteRegistration.action"/>"></a>
					</c:when>
					<c:when	test="${ actionForm.technicalRegistrationOnly }">
						<a id="backToLocationAction" href="<c:url context="${ _path }" value="/technicalonboarding/technicalRegistrationOnly"/>"></a>
						<a id="saveSiteRegAction" href="<c:url context="${ _path }" value="/technicalregsitration/saveSiteRegistrationTOB.action"/>"></a>
					</c:when>
					<c:when	test="${ actionForm.recordValidationOnly == true }">
						<a id="backToLocationAction" href="<c:url context="${ _path }" value="/recordvalidation/recordValidationOnly.action"/>"></a>
						<a id="saveSiteRegAction" href="<c:url context="${ _path }" value="/recordvalidation/saveSiteRegistration.action"/>"></a>
					</c:when>
					<c:when	test="${ actionForm.equipmentMoveOnly }">
						<a id="backToLocationAction" href="<c:url context="${ _path }" value="/equipmentmove/equipmentMoveOnly.action"/>"></a>
						<a id="saveSiteRegAction" href="<c:url context="${ _path }" value="/equipmentmove/saveSiteRegistration.action"/>"></a>
					</c:when>
					<c:when	test="${ actionForm.fullRegistrationOnly }">
						<%
						com.avaya.grt.web.security.CSSPortalUser user = (com.avaya.grt.web.security.CSSPortalUser) (request.getSession().getAttribute("cssUserProfile"));
						if (user != null && user.getUserType().equalsIgnoreCase("C") ){
						%>
							<a id="backToLocationAction" href="<c:url context="${ _path }" value="/technicalonboarding/technicalRegistrationOnly"/>"></a>
							<a id="saveSiteRegAction" href="<c:url context="${ _path }" value="/technicalregsitration/saveSiteRegistrationTOB.action"/>"></a>
						<%	}else if(user != null){ %>
							<a id="backToLocationAction" href="<c:url context="${ _path }" value="/installbase/installbasehome"/>"></a>
							<a id="saveSiteRegAction" href="<c:url context="${ _path }" value="/technicalregsitration/saveSiteRegistration.action"/>"></a>
						<%	} %>
					</c:when>
					<c:when	test="${ actionForm.salMigrationOnly }">
						<a id="backToLocationAction" href="<c:url context="${ _path }" value="/salmigration/salMigrationOnly"/>"></a>
						<a id="saveSiteRegAction" href="<c:url context="${ _path }" value="/salmigration/saveSiteRegistration.action"/>"></a>
					</c:when>
					<c:when	test="${ actionForm.ipoRegistration }">
						<a id="backToLocationAction" href="<c:url context="${ _path }" value="/ipoffice/ipoRegistration"/>"></a>
						<a id="saveSiteRegAction" href="<c:url context="${ _path }" value="/ipoffice/saveSiteRegistration.action"/>"></a>
					</c:when>
				</c:choose>
				<input type="button" value="Reset" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} class="button gray" onClick="resetAuth(); return false;"/>
				<input type="button" class="button gray" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} value="Next" id="scvNextBtn"	onClick="return validate();"/>
			</div>
			<div style="display:none;">
				<!-- <a id="viewPOa" action="purchaseOrder" />
				<a id="viewEPN" action="epnSurvey" /> -->
			</div>
			<c:if test="${ bufferOverFlowMsg != null }">
				<input type="hidden" value="${ fn:escapeXml(bufferOverFlowMsg) }" id="bufferOverFlowMsg">
			</c:if>
		</form>

	</div>
	<!-- end page content -->
</div>
<!-- end page content-wrap -->
