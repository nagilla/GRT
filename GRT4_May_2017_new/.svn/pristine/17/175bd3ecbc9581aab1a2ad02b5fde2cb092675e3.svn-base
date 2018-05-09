<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/includes/context.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/views/customer-site-registration.css" />"/>
<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/common-flow/grt.locationSelection.js" />"></script>

<script type="text/javascript">
	function viewInstallBase(){
		var soldTo = $('#soldToNumber').val();
		var  soldToId = $.trim(soldTo).split(" ")[0];
		//this.el.find("input#soldToId").val( soldTo  );
		$("input[type=hidden][name=soldTo]").val( soldToId  );
		$("input[type=hidden]#soldToId").val( soldToId  );
		$("div.loading-overlay").show();
		var url = document.getElementById('viewInstallBase').href;
		document.getElementById('customerSiteReg').action=url;
		document.getElementById('customerSiteReg').submit();
	}
</script>

<script type="text/javascript">
	<c:if test="${ requestScope.soldToError ne null }">
	window.onload = function() {
		convertAlertToModelPopUpServerSideMsg("${requestScope.soldToError}");		
	};
	</c:if>
</script>

<a id="viewInstallBase" href="<c:url context="${ _path }" value="/installbase/viewInstallBase.action" />"></a>

<!-- start page content-wrap -->
<div class="content-wrap customer-site-registration" id="locationWrapper">

	<h1 class="page-title">Customer Site Registration</h1>

	<!-- start page content -->
	<div class="content">

		<!-- start customerSiteReg -->
		<form id="customerSiteReg" method="post" class="sold-to-form" action="<c:url context="${ _path}" value="/technicalregsitration/newRegistrationWithSoldToValidation.action"/>" name="customer_site_reg">
			<s:token></s:token>
			<input type="hidden" name="actionForm.userId" value="${fn:escapeXml(actionForm.userId)}" />
			<input type="hidden" name="soldTo" value="" />
			<input type="hidden" name="actionForm.soldToId" value="" id="soldToId"/>
			<input type="hidden" name="actionForm.installbaseRegistrationOnly" value="${fn:escapeXml(actionForm.installbaseRegistrationOnly)}" />
			<input type="hidden" id="filter_registrationId" name="actionForm.filter_registrationId" value="${fn:escapeXml(actionForm.filter_registrationId)}">

			<p>Please enter End Customer Sold To (Functional Location) Number:</p>
			<div class="inputs">
				<label for="soldToNumber">Sold To/Functional Location:</label><input type="text" id="soldToNumber"	class="search-soldto-input" />
			</div>
			
			<!-- start error-message modal -->
			<!-- 
			<c:if test="${ requestScope.soldToError!=null }">				
				<div class="error-message modal" style="display:block;">
					<div class="modal-overlay">
						<div class="modal-content">
							<a class="close"><i class="fa fa-close errPopupBtn"></i></a>
								<p class="content">
									<span><i class="fa fa-exclamation-triangle"></i>The following error occured:</span>
									<span><c:out value="${requestScope.soldToError}" escapeXml="true"/></span>
								</p>
								<div class="controls">
									<a class="button gray errPopupBtn">OK</a>
								</div>
						</div>
					</div>
				</div>
			</c:if> -->			 
			<!-- end error-message modal -->
			
			<div class="controls">
				<c:choose>
					<c:when test="${installBaseCreationReadOnlyFlag}">
						<input type="button" value="Cancel" class="button gray disabled"/>
						<input type="submit" value="Next" class="button gray disabled" onclick="javascript:$('div.loading-overlay').show();"/>
					</c:when>
					<c:when test="${actionForm.viewInstallbaseOnly}">
						<input type="button" value="Cancel" class="cancelBtn button gray"/> &nbsp;&nbsp;
						<input type="button" value="Next" id="csrNext" class="button gray" onclick="javascript:viewInstallBase();"/>
					</c:when>
					<c:otherwise>
						<input type="button" value="Cancel" class="cancelBtn button gray"/>
						<input type="submit" value="Next" id="csrNext" class="button gray" onclick="javascript:$('div.loading-overlay').show();"/>
					</c:otherwise>
				</c:choose>
			</div>

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
