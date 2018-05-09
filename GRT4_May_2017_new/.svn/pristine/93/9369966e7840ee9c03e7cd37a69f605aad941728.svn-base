<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/includes/context.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/views/customer-site-registration-emp.css" />"/>
<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/common-flow/agent-location-selection.css" />"/>

<script type="text/javascript" src="<c:url context="${ _path }"  value="/scripts/common-flow/grt.agentlocationSelection.js" />"></script>
<script type="text/javascript">
	<c:if test="${ requestScope.soldToError ne null }">
	window.onload = function() {
		convertAlertToModelPopUpServerSideMsg("${requestScope.soldToError}");		
	};
	</c:if>
</script>


<a id="viewInstallBase" href="<c:url context="${ _path }" value="/installbase/viewInstallBase.action" />"></a>

<div class="pageWrapper">
	<div class="associate-modal modal" id="usertype-popup" style="display:none;">
				<div class="modal-overlay">
					<div class="modal-content">
						<a class="close closeUserTypeImg"><i class="fa fa-close"></i></a>
						<p class="title">Sold To <span id="userMessage"></span></p>
						<div class="content">
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
								<input  type="button" value="Proceed" class="button gray" onClick="closeusertypePopup();getFirstSiteRegistrationIdIfStatusIsInProgress(); return false;"/>
							</div>
						</div>
					</div>
				</div>
	</div>
	
	<div class="content-wrap customer-site-registration">
	
			<h1 class="page-title">Customer Site Registration</h1>
			<div class="content-wrap">
				<div class="content">
					
				<form action="<c:url context="${_path}" value="/technicalregsitration/newRegistration.action"/>" id="myForm" method="post">
					<s:token></s:token>
					<input type="hidden" name="actionForm.userId" value="${ fn:escapeXml(actionForm.userId) }" >
					<input type="hidden" id="filter_registrationId" name="actionForm.filter_registrationId" value="${ fn:escapeXml(actionForm.filter_registrationId) }">
					<input type="hidden" id="userRole" name="actionForm.userRole" value="${ fn:escapeXml(actionForm.userRole) }"/>
					<input type="hidden" id="installbaseRegistrationOnly" name="actionForm.installbaseRegistrationOnly"  value="${ fn:escapeXml(actionForm.installbaseRegistrationOnly) }"/>
					<input type="hidden" id="viewInstallbaseOnly" name="actionForm.viewInstallbaseOnly"  value="${ fn:escapeXml(actionForm.viewInstallbaseOnly) }"/>
					<a id="newRegistrationWithSoldToValidationForAgentAction" href="<c:url context="${ _path }" value="/technicalregsitration/newRegistrationWithSoldToValidationForAgent" />"></a>
					<p>Please enter End Customer Sold To (Functional Location) Number:</p>
					<!-- <form class="sold-to-form"> -->
						<div class="inputs">
							<label for="soldToNumber">Sold To/Functional Location:</label>
							<c:choose> 
								<c:when test="${installBaseCreationReadOnlyFlag}">
									<input class="search-soldto-input" type="text" value="actionForm.soldToId" onKeyUp="clearErrorMessage();" onblur="setEnableNextButton(this)" maxlength="20" disabled  onKeyPress="return noEnter(event);"/>
								</c:when>
								<c:otherwise>
									<input class="search-soldto-input" type="text" id="agentSoldToStorer" name="actionForm.soldToId" onKeyUp="clearErrorMessage();" onblur="setEnableNextButton(this)" maxlength="20" onKeyPress="return noEnter(event);"/>
								</c:otherwise>
							</c:choose>
						</div>
						<!-- 
						<c:if test="${ requestScope.soldToError!=null }">
							<div class="error-message modal" style="display:block;">
								<div class="modal-overlay">
									<div class="modal-content">
										<a class="close"><i class="fa fa-close errPopupBtn"></i></a>
										<div class="content">
											<p><span><i class="fa fa-exclamation-triangle"></i>The following error occured:</span> <span><c:out value="${requestScope.soldToError}" escapeXml="true"/></span></p>
											<div class="controls">
												<a class="button gray errPopupBtn">OK</a>
											</div>
										</div>
									</div>
								</div>
							</div>
						</c:if>
						 -->
						<div class="buttons">
							<input type="button" value="Cancel" class="button gray" />
							<c:choose> 
								<c:when test="${installBaseCreationReadOnlyFlag}">
									<input type="button" id="searchAgent" value="Search" disabled="disabled" class="button" onclick="getFirstSiteRegistrationIdIfStatusIsInProgress(); return false;" disabled/>
								</c:when>
								<c:when test="${actionForm.viewInstallbaseOnly}">
									<input type="button" id="searchAgent" value="Search" class="button gray" onclick="viewinstallbase(); return false;"/>
								</c:when>		
								<c:otherwise>
									<input type="button" id="searchAgent" value="Search" class="button gray" onclick="getFirstSiteRegistrationIdIfStatusIsInProgress(); return false;"/>
								</c:otherwise>		
							</c:choose>	
						</div>
						<br/>
						<br/>
					</form>
					<!-- </form> -->
					
						<div>
							<span class="pageNote"> ${grtConfig.leadingZeroesNote } </span>
						</div>
				</div>
			</div>
		
	</div>
</div>
