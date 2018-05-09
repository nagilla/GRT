<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@page import="java.util.Map"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.grt.dto.RegistrationFormBean"%>
<%@ page import="java.util.List"%>
<%@ page import="com.grt.dto.Pager"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ include file="/includes/context.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/views/ip-office.css" />"/>
<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/plugins/jquery.dataTables_themeroller.css" />" />
<script type="text/javascript" src="<c:url context="${ _path }"  value="/scripts/ipoffice/ipoInstall.js" />"></script>

	<%
		if (request.getAttribute("registrationForm") != null) {
			request.setAttribute("registrationForm", request.getAttribute("registrationForm"));
		}
	%>

<!-- start page content-wrap -->
<div class="content-wrap ip-office">

	<h1 class="page-title">IP Office Registration Information</h1>

	<!-- start page content -->
	<div class="content">

		<%@ include file="../installbase/grtBanner.jsp"%>

		<!-- start salGatewayForm -->
		<form id="ipoInstallForm" enctype="multipart/form-data" action="ipoInstall" method="post">
		<%-- <s:form id="ipoInstallForm" enctype="multipart/form-data" action="ipoInstall" method="post"> --%>
			<a id="begin" href="<c:url context="${ _path }" value="/home/home-action.action"/>"></a>
			<a id="ipoInstall" href="<c:url context="${ _path }" value="/ipoffice/ipoInstall.action"/>"></a>
			<a id="newRegistration" href="<c:url context="${ _path }" value="/technicalregsitration/newRegistration.action"/>"></a>
			<input type="hidden" name="actionForm.userRole" value="${ actionForm.userRole }" id="userRole" />
			<input type="hidden" name="actionForm.poentered" value="${ actionForm.poentered }" id="poentered" />
		
			<!-- start ip-tob-wrap wrapper -->
			<div id="ip-tob-wrap" class="data-table-wrap collapse-box">
				<h2 class="collapse-box-header active">Technical On-Boarding
					<a href="#">
						<span></span>
					</a>
				</h2>
				<div id="" class="data collapse-box-container" style="display: block; padding: 10px 20px !important;">

					<table height="100%" width="100%" border="0" style="max-height: 75px !important">
						<tbody>
						<!-- <tr>
							<th colspan="2" height="20">Technical On-Boarding</th>
						</tr> -->

						<tr>
							<td style="min-width: 520px;">Would you like to establish Remote Connectivity for the IP Office?</td>
							<td>
								 <select id="remotecon" name="actionForm.remoteConnectivity" onChange="enableNewRowDiv(this.value);" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}>
									<option value="">Choose one</option>
									<option value="Yes"  ${ actionForm.remoteConnectivity =='Yes' ? 'selected' : '' }>Yes</option>
						  			<option value="No"  ${ actionForm.remoteConnectivity =='No' ? 'selected' : '' }>No</option>
						   		 </select>
							</td>
						</tr>

						<tr>
							<td>&nbsp;</td>
							<td>
								<div id="additionalInfoDiv" style="display:none;">
									  	<input type="radio" checked id="ipoAccessTypeSslvpn" name="actionForm.ipoAccessType" value="SSL/VPN" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}/><span>SSL/VPN</span>
										<input type="radio" id="ipoAccessTypeOther" name="actionForm.ipoAccessType" value="Other" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}/><span>Other</span>
										<input type="radio" id="ipoAccessTypeTokenRedemptOnly" name="actionForm.ipoAccessType" value="Token Redemption Only" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}/><span>Token Redemption Only</span>
								</div>
							</td>
						</tr>
						</tbody>
					</table>

				</div>
			</div> 
			<!-- end ip-tob-wrap wrap -->

			<!-- start ip-upload-wrap wrapper -->
			<div id="ip-upload-wrap" class="data-table-wrap collapse-box">
				<h2 class="collapse-box-header active">File Upload(Optional)
					<a href="#">
						<span></span>
					</a>
				</h2>
				<div id="" class="data collapse-box-container" style="display: block; padding: 10px 20px !important;">

					<table height="100%" width="100%" border="0"   style="max-height: 75px !important">
						<tr>
							<td style="max-width: 400px;">Upload Inventory xml File
								<p style="margin-top: 10px">To register the IP Office without inventory file, click on the Next button below to manually enter the material codes<br> for install base creation. </p>
							</td>
							<td>
								<input type="file" name="actionForm.ipoFile" id="uploadFile" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}/>
								<%-- <input type="button" id="uploadBtn" value="Browse" class="button gray" ${(actionForm.readOnly) ? 'disabled' : ''} /> --%>
								<div style="color:red; margin-top: 8px" >${requestScope.schemaError}</div>
							</td>
						</tr>
					</table>

				</div>
			</div>
			<!-- end ip-upload-wrap wrap -->

			<!-- start ip-company-wrap wrapper -->
			<div id="ip-company-wrap" class="data-table-wrap collapse-box">
				<h2 class="collapse-box-header active"><%-- <s:property value="getText('companyInformation')" /> --%>
						Company Information <div style="text-transform: initial;  display: inline-block;">
								(This data is optional and is only used by the IP Office Product House )</div>
					<a href="#">
						<span></span>
					</a>
				</h2>
				<div id="" class="data collapse-box-container"   style="display: block; padding: 10px 20px !important;">

					<table height="100%" width="100%" border="0" style="max-height: 145px;">

				    <tr>
							<td style=" min-width: 582px;">Type of Installation</td>
							<td>
								<select id="typeofinstl" class="selectWidth" name="actionForm.typeOfInstallation" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}>
								    <option value="" >Choose one</option>
								    <option value="newIPofficeNewLocation" >New IP Office installation (new location, no current communication solution)</option>
								    <option value="newIPofficeExistingLocation" >New IP Office installation (existing location, replacing a non-Avaya communication solution)</option>
								    <option value="upgradeExistingIPOffice" >Upgrading of existing  IP Office system</option>
								    <option value="migrateBCMtoIPO" >Migrating from BCM to IP Office</option>
								    <option value="migrateNorstartoIPO" >Migrating from Norstar to IP Office</option>
								    <option value="migrateACStoIPO" >Migrating from Partner ACS to IP Office</option>
								    <option value="migrateIntegraltoIPO" >Migrating from Integral to IP Office</option>
								</select>
							</td>
						</tr>

						<tr>
							<td>Industry (Verticals)</td>
							  <td>
							  	<select id="indstry" class="selectWidth" name="actionForm.industry" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}>
								    <option value="" >Choose one</option>
								    <option value="agricultureConstMining" ${ actionForm.industry == '' }>Agriculture, Construction and Mining</option>
								    <option value="educationalServices" >Educational Services</option>
								    <option value="financialServicesFinIns" >Financial Services-Finance, Insurance and Leasing</option>
								    <option value="financialServicesRealEst" >Financial Services-Real Estate</option>
								    <option value="government" >Government</option>
								    <option value="healthServices" >Health Services</option>
								    <option value="hospitality" >Hospitality</option>
								    <option value="InfoandComm" >Information and Communications</option>
								    <option value="manufacturing" >Manufacturing</option>
								    <option value="otherServices" >Other Services</option>
								    <option value="proffesionalServices" >Professional Services</option>
								    <option value="retail" >Retail</option>
								    <option value="utilitiesandTransp" >Utilities and Transportation</option>
								    <option value="wholesale" >Wholesale</option>
								    <option value="other" >Other</option>
								</select>
							</td>
						</tr>

						<tr>
							<td>Total number of Company Employees (all Locations)</td>
							<td>
								<select id="employeesallloc" class="selectWidth" name="actionForm.employeesAllLocation" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}>
									<option value="" >Choose one</option>
									<option value="1-19">1-19</option>
									<option value="20-29">20-29</option>
									<option value="30-49">30-49</option>
									<option value="50-99">50-99</option>
									<option value="100-249">100-249</option>
									<option value="250-999">250-999</option>
									<option value="1000 or over">1000 or over</option>
								</select>
							</td>
						</tr>

						<tr>
							<td>Total number of Company Employees (this Location)</td>
							<td><select id="employeesthisloc" class="selectWidth" name="actionForm.employeesThisLocation" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}>
							 <option value="" >Choose one</option>
							   <option value="1-19">1-19</option>
						       <option value="20-29">20-29</option>
						       <option value="30-49">30-49</option>
						       <option value="50-99">50-99</option>
						       <option value="100-249">100-249</option>
						       <option value="250 or over">250 or over</option>
						    </select>
							</td>
						</tr>

						<tr>
							<td>Total number of Company Locations</td>
							<td><select id="totalloc" class="selectWidth" name="actionForm.totalLocations" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}>
							 <option value="">Choose one</option>
							  <option value="1">1</option>
						      <option value="2-4">2-4</option>
						      <option value="5-9">5-9</option>
						      <option value="10-14">10-14</option>
						      <option value="15-19">15-19</option>
						      <option value="20-24">20-24</option>
						      <option value="25-32">25-32</option>
						      <option value="33 or over">33 or over</option>
						    </select>
							</td>
						</tr>

					</table>

				</div>
			</div>
			<!-- end ip-company-wrap wrap -->

			<!-- start controls -->
			<div class="controls">
				<%-- <input type="button" class="button gray" value="Cancel" onClick="return popUp();" ${installBaseCreationReadOnlyFlag ? 'disabled' : ''}/>
				<input type="button" class="button gray" value="Back" id="backToRegistration"/>
				<input type="button" class="button gray" id="uploadBtn" onClick="return validateipo('uploadFile');" value="Next" /> --%>
				
				<input type="button" onclick="backFromIPOffice();" value="Back" class="button gray" />&nbsp;
				<a id="backFromIPOffice" href="<c:url context="${ _path }" value="/ipoffice/backFromIPOffice.action" />" ></a>
				
				<input type="button" onclick="cancelIPOffice();" value="Cancel" class="button gray" />&nbsp;
				<a id="cancelIPOffice" href="<c:url context="${ _path }" value="/ipoffice/cancelIPOffice.action" />" ></a>
				
				<input type="button" class="button gray" value="Next" onClick="validateipo()"/>
				 <a id="submitIPOffice" href="<c:url context="${ _path }" value="/ipoffice/ipoInstall.action" />" ></a>
			</div>
			<!-- end controls -->

			<script>
				var ipoFlag = document.getElementById('remotecon').value;
				enableNewRowDiv(ipoFlag);
			</script>

		</form>
		<!-- end salGatewayForm -->

	</div>
	<!-- end page content -->
</div>
<!-- end page content-wrap -->
