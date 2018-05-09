<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ include file="/includes/context.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script src='<c:url context="${ _path }"  value="/scripts/plugins/jquery_maskedinput.js" />'></script>

<script type="text/javascript" src='<%=request.getContextPath()+"/scripts/plugins/dynamicTable.js"%>'></script>
<script type="text/javascript"	src="<c:url context="${ _path }"  value="/scripts/installbase/registration.js" />"></script>
<script type="text/javascript"	src="<c:url context="${ _path }"  value="/scripts/installbase/grt.newregistration.view.js" />"></script>
<script type="text/javascript"	src="<c:url context="${ _path }"  value="/scripts/installbase/grt.newregistration.model.js" />"></script>

<div id="dialog-underlay" style="opacity:0.2; filter:alpha(opacity=20); position: absolute; display: none; width: 100%; height: 100%; top: 0; right: 0; bottom: 0; left: 0;"></div>
<div id="popup-window" class="dialog" style="left: -1000em;">
	<div class="dialog-container">
		<div class="gb-tc"></div>
		<div class="gb">
			<div class="dialog-title-bar">
				<h2><span id="popup-window-title">Waiting...</span></h2>
			</div>
			<div class="dialog-content" id="popup-window-content">Loading data
			</div>
			<div class="fixer"></div>
		</div>
		<div class="gb-bc"></div>
	</div>
</div>

<div id="errorDiv" />
<form id="saveSiteRegistrationForm" action="<c:url context="${_path}" value="/technicalregsitration/saveSiteRegistration.action"/>" method="post">
	<s:token />
	<a action="saveSiteRegistration" formSubmit="true" id="saveSiteRegistrationAnchor" />
	<input type="hidden" value="${ actionForm.poentered }" name="actionForm.poentered" id="poentered" />
	<input type="hidden" value="${ actionForm.userRole }" name="actionForm.userRole" id="userRole" />
	
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
	
	<table width="100%">
		<tr>
			<td colspan="2" align="center"><h1 class="osm_pageTitle">Site Contact Validation</h1></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
	</table>
	<table height="100%" width="100%" class="osm_table05" border="0">
		<tr>
			<th colspan="2" class="osm_table05_th" height="20">Sold To Location</th>
		</tr>
		<tr class="osm_table05_td">
			<td class="osm_text" width="25%">Registration ID</td>
			<td>${fn:escapeXml(actionForm.registrationId)}<input type="hidden" name="actionForm.registrationId" value="${fn:escapeXml(actionForm.registrationId)}" /></td>
		</tr>
		<tr class="osm_table05_td">
			<td class="osm_text" width="25%">Sold To</td>
			<td>${actionForm.soldToId}<input type="hidden" name="actionForm.soldToId" value="${fn:escapeXml(actionForm.soldToId)}" /></td>
		</tr>
		<tr class="osm_table05_td">
			<td class="osm_text">Company Name</td>
			<td>${actionForm.company}<input type="hidden" name="actionForm.company" value="${ actionForm.company }" id="company" /></td>
		</tr>
		<tr class="osm_table05_td">
			<td class="osm_text">Phone Number</td>
			<td>${actionForm.companyPhone}
				<input type="hidden" id="companyPhoneNo" name="actionForm.companyPhone" value="${ actionForm.companyPhone }" /></td>
		</tr>
		<tr class="osm_table05_td">
			<td class="osm_text">Site Country</td>
			<td>${actionForm.siteCountry}<input type="hidden" name="actionForm.siteCountry" value="${ actionForm.siteCountry }" /></td>
		</tr>
		<tr class="osm_table05_td">
			<td class="osm_text">Address Line1</td>
			<td>${actionForm.address1}<input type="hidden" name="actionForm.address1" value="${ actionForm.address1 }" /></td>
		</tr>
		<tr class="osm_table05_td">
			<td class="osm_text">Address Line2</td>
			<td>${actionForm.address2}<input type="hidden" name="actionForm.address2" value="${ actionForm.address2 }" /></td>
		</tr>
		<tr class="osm_table05_td">
			<td class="osm_text">City</td>
			<td>${actionForm.city}<input type="hidden" name="actionForm.city" value="${ fn:escapeXml(actionForm.city) }" /></td>
		</tr>
		<tr class="osm_table05_td">
			<td class="osm_text">State / Province</td>
			<td><input type="hidden" id="stateSelectHidden"  name="actionForm.state" value="${actionForm.state}"/>
			<select id="stateSelect" disabled>
				<option value="Alabama">Alabama</option>
				<option value="Alaska" >Alaska</option>
				<option value="Arizona" >Arizona</option>
				<option value="Arkansas" >Arkansas</option>
				<option value="California" >California</option>
				<option value="Colorado" >Colorado</option>
				<option value="Connecticut" >Connecticut</option>
				<option value="Delaware" >Delaware</option>
				<option value="District of Columbia" >District of Columbia"</option>
				<option value="Florida" >Florida</option>
				<option value="Georgia" >Georgia</option>
				<option value="Hawaii" >Hawaii</option>
				<option value="Idaho" >Idaho</option>
				<option value="North Carolina" >North Carolina</option>
				<option value="Illinois" >Illinois</option>
				<option value="Indiana" >Indiana</option>
				<option value="Iowa" >Iowa</option>
				<option value="Kansas" >Kansas</option>
				<option value="Kentucky" >Kentucky</option>
				<option value="Louisiana" >Louisiana</option>
				<option value="Maine" >Maine</option>
				<option value="Maryland"> Maryland</option>
				<option value="Massachusetts" >Massachusetts</option>
				<option value="Michigan" >Michigan</option>
				<option value="Minnesota" >Minnesota</option>
				<option value="Mississippi">Mississippi</option>
				<option value="Missouri" >Missouri</option>
				<option value="Montana" >Montana</option>
				<option value="Nebraska" >Nebraska</option>
				<option value="Nevada" >Nevada</option>
				<option value="New Hampshire" >New Hampshire</option>
				<option value="New Jersey" >New Jersey</option>
				<option value="New Mexico" >New Mexico</option>
				<option value="New York" >New York</option>
				<option value="North Dakota" >North Dakota</option>
				<option value="Ohio" >Ohio</option>
				<option value="Oklahoma" >Oklahoma</option>
				<option value="Oregon" >Oregon</option>
				<option value="Pennsylvania" >Pennsylvania</option>
				<option value="Rhode Island" >Rhode Island</option>
				<option value="South Carolina">South Carolina</option>
				<option value="South Dakota" >South Dakota</option>
				<option value="Tennessee" >Tennessee</option>
				<option value="Texas" >Texas</option>
				<option value="Utah" >Utah</option>
				<option value="Vermont" >Vermont</option>
				<option value="Virginia" >Virginia</option>
				<option value="Washington" >Washington</option>
				<option value="West Virginia" >West Virginia</option>
				<option value="Wisconsin" >Wisconsin</option>
				<option value="Wyoming" >Wyoming</option>
			</select></td>
		</tr>
		<tr class="osm_table05_td">
			<td class="osm_text">Zip / Postal Code</td>
			<td>${actionForm.zip}<input type="hidden" name="actionForm.zip" value="${ actionForm.zip }" /></td>
		</tr>
		<tr class="osm_table05_td">
			<td class="osm_table05_td">Registration Name
				<tl:toolTip id="registrationNameToolTip" message="Registration name is an optional field that can be used to help easily identify your registration"></tl:toolTip>
			</td>
			<td>
				 <input type="text" id="registrationIdentifier" name="actionForm.registrationIdentifier" value="${ actionForm.registrationIdentifier }" size="30" maxlength="30" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} onKeyPress="return noEnter(event);"/>
			</td>
		</tr>
		<tr>
			<th colspan="2" height="20">&nbsp;</th>
		</tr>
		<tr>
			<th colspan="2" height="20">&nbsp;</th>
		</tr>
		<tr>
			<th colspan="2" class="osm_table05_th" height="20">Reported By</th>
		</tr>
		<tr class="osm_table05_td">
			<td class="osm_text" width="25%">First Name</td>
			<td>
				${actionForm.firstName}
				<input type="hidden" name="actionForm.firstName" value="${actionForm.firstName}" id="firstName"/>
			</td>
		</tr>
		<tr class="osm_table05_td">
			<td class="osm_text" width="25%">Last Name</td>
			<td>
				${actionForm.lastName}
				<input type="hidden" name="actionForm.lastName" id="lastName" value="${ actionForm.lastName }"/>
			</td>
		</tr>
		<tr class="osm_table05_td">
			<td class="osm_text">Phone</td>
			<td><input type="text" id="onsitePhone" name="actionForm.reportedPhone" value="${ actionForm.reportedPhone}" size="20" style="width: 170px" maxlength="19" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} 
			onKeyPress="return noEnter(event);" onKeyUp="copyPhone();" /></td>
		</tr>
		<tr class="osm_table05_td">
			<td class="osm_text">Email Address</td>
			<td>
				${actionForm.reportedEmail}
				<input type="hidden" name="actionForm.reportedEmail" id="emailId" value="${ actionForm.reportedEmail }"/>
			</td>
		</tr>
		<%-- send mail flag --%>
		<tr class="osm_table05_td">
			<td class="osm_text">Do you want to receive email notifications?</td>
			<td><select id="sendMail" name="actionForm.sendMail" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}>
					<option ${ (actionForm.sendMail == "Y") ? 'selected' : ''  } value="Y" >Y</option>
					<option ${ (actionForm.sendMail == "N") ? 'selected' : ''  } value="N" >N</option>
			    </select>
			</td>
		</tr>
		<tr>
			<th colspan="2" height="20">&nbsp;</th>
		</tr>
		<tr class="osm_table05_td">
			<th colspan="2" class="osm_table05_th" height="20">GRT Notification Contact&nbsp;&nbsp;&nbsp;
				<font color="red"><b>Note: The Phone Number field is no longer required below and the 'Same as Above' option is automatically selected.</b></font>
			</th>
		</tr>
		<tr class="osm_table05_td">
			<td class="osm_text" width="25%" colspan="2"><%-- <netui:radioButtonGroup value="actionForm.useSameAddress"
				defaultValue="Same as Above" disabled="${pageFlow.installBaseCreationReadOnlyFlag}"> --%>
				<input type="radio" name="addressRadio" id="sameAsAbove" value="Same as Above" onClick="copysiteaddress();" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} onKeyPress="return noEnter(event);">Same as Above</>
				<input type="radio" checked="checked" name="addressRadio" id="enterManually" value="Enter Manually" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} onKeyPress="return noEnter(event);">Enter Manually</>
			<%-- </netui:radioButtonGroup> --%></td>
		</tr>
		
			
		<tr class="osm_table05_td">
				
			<td class="osm_text" width="25%">First Name*</td>
			<td><input type="text" name="actionForm.onsiteFirstname" id="onsiteFirstName" value="${ actionForm.onsiteFirstname }" size="40" maxlength="50" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} onKeyPress="return noEnter(event);"/></td>
		</tr>
		<tr class="osm_table05_td">
			<td class="osm_text" width="25%">Last Name*</td>
			<td><input type="text" name="actionForm.onsiteLastname" id="onsiteLastName" value="${actionForm.onsiteLastname}" size="40" maxlength="50" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} onKeyPress="return noEnter(event);"/></td>
		</tr>
		<tr class="osm_table05_td">
			<td class="osm_text">Phone</td>
			<td><input type="text" id="sitePhone" name="actionForm.onsitePhone" value="${actionForm.onsitePhone}" size="20" style="width: 170px" maxlength="19" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} onKeyPress="return noEnter(event);"/></td>
		</tr>
		<tr class="osm_table05_td">
			<td class="osm_text">Email*
				<tl:toolTip id="emailToolTip" message="Can enter more than one Email Address with semicolon as separator"></tl:toolTip>
			</td>
			<td><input type="text"  name="actionForm.onsiteemail" id="onsiteEmailId" value="${actionForm.onsiteemail}" size="40" maxlength="145"  ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} onKeyPress="return noEnter(event);"/></td>
		</tr>
		
		<tr class="osm_table05_td">
			<td class="osm_table05_td">Registration Notes</td>
			<td><textarea id="registrationNotes" name="actionForm.registrationNotes" rows="3" cols = "30" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} onblur="javascript:textLimit(this,4000);" onKeyPress="return noEnter(event);">
				${actionForm.registrationNotes}
			</textarea></td>
		</tr>

		<tr>
			<th colspan="2" height="20">&nbsp;</th>
		</tr>	
			
		
		<c:set var="userType" value="${sessionScope.cssUserProfile.userType}"/>
		<c:set var="region" value="${actionForm.region}"/> 
		
		 <%if("NAR".equals(pageContext.getAttribute("region")) && 
				("B".equals(pageContext.getAttribute("userType")) || "BP".equals(pageContext.getAttribute("userType")))) { %>   
<%--			<tr class="osm_table05_td">--%>
<%--				<td class="osm_text" width="25%">${bundle.msg.expedite}</td>--%>
<%--				<td><select value="actionForm.expedaite" onChange="displayAuthFieldIfNecessary();openCreditCardInfo(this.value)"--%>
<%--					defaultValue="No" id="expedaiteSelect" >--%>
<%--					<option value="No" >${bundle.msg.optionNo}</option>--%>
<%--					<option value="Yes" >${bundle.msg.optionYes}</option>--%>
<%--				</select> <input type="button" value="${bundle.msg.buttonViewPO}" class="osm_large_button" action="purchaseOrder" />--%>
<%--				</td>--%>
<%--			</tr>--%>
		
		<tr class="osm_table05_td" style="display:none;" id="AuthTr" >
			<td class="osm_text" >Authorization Number</td>
			<td>               
				<input type="text" id="expediteRefNoDisplayField" value=""/>
				<input type="hidden" value="${ actionForm.expediteRefNo }" name="actionForm.expediteRefNo"
 				id="expediteRefNo" />
			</td>
		</tr>
		<% } %>
	</table>
	<table width="100%">
		<tr>
			<td align="center">
<%--				<input type="button" class="osm_large_button" disabled="${pageFlow.installBaseCreationReadOnlyFlag}" action="begin" value="${bundle.msg.buttonHome}"/> --%>
				<input type="button" class="osm_large_button homeBtn" value="Home"/>
				<input type="button" class="osm_large_button" value="Back" id="backToLocationPage"/>	
				<c:choose>
					<c:when	test="${ actionForm.installbaseRegistrationOnly }">
						<a id="backToLocationAction" href="<c:url context="${ _path }" value="/installbase/installbasehome"/>"></a>
						<a id="saveSiteRegAction" href="<c:url context="${ _path }" value="/technicalregsitration/saveSiteRegistration.action"/>"></a>
					</c:when>
					<c:when	test="${ actionForm.installbaseRegistrationOnly == false }">
						<a id="backToLocationAction" href="<c:url context="${ _path }" value="/technicalonboarding/technicalRegistrationOnly"/>"></a>
						<a id="saveSiteRegAction" href="<c:url context="${ _path }" value="/technicalregsitration/saveSiteRegistrationTOB.action"/>"></a>
					</c:when>
				</c:choose>
				<input type="button" value="Reset" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} class="osm_large_button" onClick="resetAuth(); return false;"/> 
				<input type="button" class="osm_large_button" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} value="Next" id="scvNextBtn"  onClick="return validate();"/>
			</td>
		</tr>
	</table>
	<div style="display:none;">
		<a id="viewPOa" action="purchaseOrder" />
		<a id="viewEPN" action="epnSurvey" />
	</div>
	<%-- <script>
		initPage();
	</script> --%>
</form>


	


<%-- Omniture Test
<script language="JavaScript" type="text/javascript" src="/grtWebProject/framework/skins/support/js/s_code.js"></script>
<script language="JavaScript" type="text/javascript" src="/grtWebProject/framework/skins/support/js/p_code.js"></script> --%>