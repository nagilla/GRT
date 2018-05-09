<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat,java.util.*"%>
<%-- <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.grt.util.GRTConstants"%>
<%@ include file="/includes/context.jsp" %>
 <% response.setHeader("Cache-Control","no-cache"); //HTTP 1.1 
 response.setHeader("Pragma","no-cache"); //HTTP 1.0 
 response.setDateHeader ("Expires", 0); //prevents caching at the proxy server  
%>
<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/views/alerts.css" />"/>

<script type="text/javascript" src="<c:url context="${ _path }"  value="/scripts/admin/grt.adminhome.view.js" />"></script>
<div>
<s:form name="alertsList" id="alertsList" method="post">
<s:token></s:token>
	<table width="100%">
		<tr>
			<td colspan="2" align="center"><h1 class="">Announcements</h1></td>
		</tr>
		<%-- <tr>
			<td colspan="2" colspan="9" class="osm_text_error" height="20" align="left">&nbsp;${adminAlertForm.message}</td>
		</tr> --%>
	</table>
	<table border="0">
		<tr>
			<td align="right"><b>Current Date/Time:</b>&nbsp;<span id="currentTime"><%=new SimpleDateFormat("MMddyyyy HH:mm:ss").format(new Date()) %></span>&nbsp;&nbsp;&nbsp;</td>
		</tr>
		<tr>
			<td>
			<!-- <div class="CSS_Table_Example"> -->
			<div class="box-table">
				<table>
					<tr>
						<th width="250">Start Date Time</th>
						<th width="250">End Date Time</th>
						<th width="1000">Message</th>
						<th width="250">Update</th>
					</tr>
					<c:forEach items="${adminAlertForm.alertsList }" var="container" varStatus="rowCounter">
						<tr>
							<td>
								<span id="startDate${rowCounter.count}">${container.startDate}</span>&nbsp;EST
							</td>
							<td>
								<span id="endDate${rowCounter.count}">${container.endDate}</span>&nbsp;EST
							</td>
							<td>
								<span id="message${rowCounter.count}">${container.message}</span>
							<input type="hidden" id="hiddenMessage${rowCounter.count}" value="${container.message }"/>
							</td>
							<td>
								<input type="button" value="Update" onClick="saveOrUpdate('update', ${rowCounter.count}, ${container.alertId});"/>
								<input type="button" value="Delete" onClick="deleteAlert(${container.alertId}, ${rowCounter.count});"/>
							</td>
						</tr>
					</c:forEach>
				</table>
			</div>
			<!-- </div> -->
			</td>
		</tr>
	</table>
	<table width="100%">
		<tr>
			<td colspan="2" align="center">
				<DIV>
					<input type="button" value="Add" onClick="saveOrUpdate('add','','');"/>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="button" value="Back" onclick="goToHome();"/>
				</DIV>
			</td>
		</tr>
	</table>
	<DIV style="display: none;" id="addAlert">
		<table border="0" align="center">
		<tr>
			<td>
				<table width="40%">
					<tr>
						<td width="250">Message</td>
						<td><textArea name="adminAlertForm.message" cols="30" id="alert" rows="4" onblur="javascript:textLimit(this,4000)"></textArea></td>
					</tr>
					<tr>
						<td width="250">Start Date Time*</td>
						<td>
							<input type="text" name="adminAlertForm.sDate" id="sDate" value="" size="10" maxlength="8">&nbsp;
							<input type="text" name="adminAlertForm.sTime" id="sTime" value="00:00:00" size="10" maxlength="8">&nbsp;EST
						</td>
					</tr>
					<tr>
						<td width="1000">End Date Time*</td>
						<td>
							<input type="text" name="adminAlertForm.eDate" id="eDate" value="" size="10" maxlength="8">&nbsp;
							<input type="text" name="adminAlertForm.eTime" id="eTime" value="00:00:00" size="10" maxlength="8">&nbsp;EST
						</td>
					</tr>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr>
						<td align="right"><input type="button" value="Save" onClick="saveOrUpdateAlerts();"/>&nbsp;&nbsp;</td>
						<td align="left">&nbsp;&nbsp;<input type="button" value="Cancel" onClick="cancelSaveOrUpdate();"/></td>
					</tr>
					<tr><td colspan="9" class="osm_text_error" height="20" align="left">Note: Start Date and End Date formats should be as : mmddyyyy hh:mi:ss EST</td></tr>
				</table>
			</td>
		</tr>
	</table>
	</DIV>
	<input type="hidden" id="updateAnnouncementAction" value="<c:url context="${_path}" value="/administrator/updateAnnouncement.action"/>"/>
	
	<input type="hidden" name="adminAlertForm.alertId" id="alertId" value=""/>
	<input type="hidden" id="startDate" value=""/>
	<input type="hidden" id="endDate" value=""/>
	<input type="hidden" name="adminAlertForm.requiredAction" id="requiredAction" value=""/>
	
	<input type="hidden" name="alertMessageBlankMsgKey" id="alertMessageBlankMsgKey" value="${grtConfig.ewiMessageCodeMap['alertMessageBlankMsg']}"/>
	<input type="hidden" name="alertMessageBlankMsg" id="alertMessageBlankMsg" value="${grtConfig.alertMessageBlankMsg}"/>
	<input type="hidden" name="alertMessageLenghtOverMsgKey" id="alertMessageLenghtOverMsgKey" value="${grtConfig.ewiMessageCodeMap['alertMessageLenghtOverMsg']}"/>
	<input type="hidden" name="alertMessageLenghtOverMsg" id="alertMessageLenghtOverMsg" value="${grtConfig.alertMessageLenghtOverMsg}"/>
	<input type="hidden" name="alertStartDateBlankMsgKey" id="alertStartDateBlankMsgKey" value="${grtConfig.ewiMessageCodeMap['alertStartDateBlankMsg']}"/>
	<input type="hidden" name="alertStartDateBlankMsg" id="alertStartDateBlankMsg" value="${grtConfig.alertStartDateBlankMsg}"/>
	<input type="hidden" name="alertEndDateBlankMsgKey" id="alertEndDateBlankMsgKey" value="${grtConfig.ewiMessageCodeMap['alertEndDateBlankMsg']}"/>
	<input type="hidden" name="alertEndDateBlankMsg" id="alertEndDateBlankMsg" value="${grtConfig.alertEndDateBlankMsg}"/>
	<input type="hidden" name="alertDatesNotValidMsgKey" id="alertDatesNotValidMsgKey" value="${grtConfig.ewiMessageCodeMap['alertDatesNotValidMsg']}"/>
	<input type="hidden" name="alertDatesNotValidMsg" id="alertDatesNotValidMsg" value="${grtConfig.alertDatesNotValidMsg}"/>
	
	<input type="hidden" name="alertStartDateLessCurrDateMsgKey" id="alertStartDateLessCurrDateMsgKey" value="${grtConfig.ewiMessageCodeMap['alertStartDateLessCurrDateMsg']}"/>
	<input type="hidden" name="alertStartDateLessCurrDateMsg" id="alertStartDateLessCurrDateMsg" value="${grtConfig.alertStartDateLessCurrDateMsg}"/>
	<input type="hidden" name="alertStartDateGreaterEndDateMsgKey" id="alertStartDateGreaterEndDateMsgKey" value="${grtConfig.ewiMessageCodeMap['alertStartDateGreaterEndDateMsg']}"/>
	<input type="hidden" name="alertStartDateGreaterEndDateMsg" id="alertStartDateGreaterEndDateMsg" value="${grtConfig.alertStartDateGreaterEndDateMsg}"/>
	<input type="hidden" name="alertDeleteConfirmationMsgKey" id="alertDeleteConfirmationMsgKey" value="${grtConfig.ewiMessageCodeMap['alertDeleteConfirmationMsg']}"/>
	<input type="hidden" name="alertDeleteConfirmationMsg" id="alertDeleteConfirmationMsg" value="${grtConfig.alertDeleteConfirmationMsg}"/>
	<input type="hidden" name="alertInvalidTimeFormatMsgKey" id="alertInvalidTimeFormatMsgKey" value="${grtConfig.ewiMessageCodeMap['alertInvalidTimeFormatMsg']}"/>
	<input type="hidden" name="alertInvalidTimeFormatMsg" id="alertInvalidTimeFormatMsg" value="${grtConfig.alertInvalidTimeFormatMsg}"/>
	
	<input type="hidden" name="alertSpecialCharErrorMsgKey" id="alertSpecialCharErrorMsgKey" value="${grtConfig.ewiMessageCodeMap['alertSpecialCharErrorMsg']}"/>
	<input type="hidden" name="alertSpecialCharErrorMsg" id="alertSpecialCharErrorMsg" value="${grtConfig.alertSpecialCharErrorMsg}"/>
	
</s:form>
</div>