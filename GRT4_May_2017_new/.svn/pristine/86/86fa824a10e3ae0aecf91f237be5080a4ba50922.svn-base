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
  

<script type="text/javascript">
<!-- Keep the New Registration Data in Request Scope -->
	<%
		if (request.getAttribute("registrationForm") != null) {
			request.setAttribute("registrationForm", request.getAttribute("registrationForm"));	
		}
	%>
	
	
	function validateipo(filein){
		//var remotecon = document.getElementById("remotecon").value;
		//if(remotecon == ""){
			//alert("Remote Connectivity Required,Choose Yes or No");
			//return false;
		//}
		document.getElementById('ipoInstallForm').action = document.getElementById('ipoInstall').href;
		document.getElementById('ipoInstallForm').submit();
		return true;
	}
	
	function popUp()
	{
		alert('You have select to cancel this registration. You will be taken back to the GRT Home page and all data entered will be lost.');
		
		return true;
	}
	
	function enableNewRowDiv(flag){
		if(flag == 'Yes'){
			document.getElementById('additionalInfoDiv').style.display = 'block';
		} else {
			document.getElementById('ipoAccessTypeSslvpn').checked = false;
			document.getElementById('ipoAccessTypeOther').checked = false;
			document.getElementById('ipoAccessTypeTokenRedemptOnly').checked = false;
			document.getElementById('additionalInfoDiv').style.display = 'none';
		}
	}
</script>

<s:form id="ipoInstallForm" enctype="multipart/form-data" action="begin" method="post">
<!--netui:anchor action="ipoInstall" formSubmit="true" tagId="ipoInstallAnchor" /-->
<input type="hidden" name="actionForm.poentered" value="${ actionForm.poentered }" id="poentered" />
<a id="begin" href="<c:url context="${ _path }" value="/home/home-action.action"/>"></a>
<a id="ipoInstall" href="<c:url context="${ _path }" value="/account/ipoInstall"/>"></a>
<a id="newRegistration" href="<c:url context="${ _path }" value="/account/ipoRegistrationInformation.action"/>"></a>
<input type="hidden" name="actionForm.userRole" value="${ actionForm.userRole }" id="userRole" />	
<table width="100%">
		<tr>
			<td colspan="2" align="center"><h1 class="osm_pageTitle">IP Office Registration Information</h1></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
</table>

  	<!--  Banner -->
	<%@ include file="../installbase/grtBanner.jsp"%>
   	<!--  Banner -->
	
<table height="100%" width="100%" class="osm_table05" border="0">
         
 		<tr>
			<th colspan="2" class="osm_table05_th" height="20">Technical On-Boarding</th>
		</tr>
		
		<tr class="osm_table05_td">
			<td class="osm_text" >Would you like to establish Remote Connectivity for the IP Office?</td>
			<td><select id="remotecon" onChange="enableNewRowDiv(this.value);" disabled="${pageFlow.installBaseCreationReadOnlyFlag}"> 
			 	<option value=""  disabled="${pageFlow.installBaseCreationReadOnlyFlag}">Choose one</option>
			 	<option value="Yes" disabled="${pageFlow.installBaseCreationReadOnlyFlag}"/>
		      	<option value="No" disabled="${pageFlow.installBaseCreationReadOnlyFlag}" />
		    </select>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<div id="additionalInfoDiv" style="display:none;">
					  	<input type="radio" id="ipoAccessTypeSslvpn" value="SSL/VPN ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}"/>
						<input type="radio" id="ipoAccessTypeOther" value="Other ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}"/>
						<input type="radio" id="ipoAccessTypeTokenRedemptOnly" value="Token Redemption Only ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}"/>
				</div>
			</td>
		</tr>
		<tr>
			<th colspan="2" class="osm_table05_th" height="20"><s:property value="getText('fileuploadlabel')" />${bundle.msg.fileuploadlabel}</th>
		</tr>
		<tr class="osm_table05_td">
			<td class="osm_text">Upload Inventory xml File
				<p>To register the IP Office without inventory file, click on the Next button below to manually enter the material codes for install base creation. </p>
			</td>
			<td>				
				<input type="file" name="actionForm.ipoFile" id="uploadFile" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}/>
				<input type="button" id="uploadBtn" value="Browse" class="button gray" ${(actionForm.readOnly) ? 'disabled' : ''} />
				<div style="color:red" >
				${requestScope.schemaError}
				</div>
			</td>
		</tr>
         
          
         <tr>
			<th colspan="2" class="osm_table05_th" height="20"><s:property value="getText('companyInformation')" />${bundle.msg.companyInformation}</th>
		</tr>


         <tr class="osm_table05_td">
			<td class="osm_text" ><s:property value="getText('typeofinstallation')" />${bundle.msg.typeofinstallation}</td>
			<td><select id="typeofinstl" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}"> 
			    <option value="" >Choose one</option>
			    <option value="newIPofficeNewLocation" ><s:property value="getText('newIPofficeNewLocation')" />${bundle.msg.newIPofficeNewLocation}</option>
			    <option value="newIPofficeExistingLocation" ><s:property value="getText('newIPofficeExistingLocation')" />${bundle.msg.newIPofficeExistingLocation}</option>
			    <option value="upgradeExistingIPOffice" ><s:property value="getText('upgradeExistingIPOffice')" />${bundle.msg.upgradeExistingIPOffice}</option>
			    <option value="migrateBCMtoIPO" ><s:property value="getText('migrateBCMtoIPO')" />${bundle.msg.migrateBCMtoIPO}</option>
			    <option value="migrateNorstartoIPO" ><s:property value="getText('migrateNorstartoIPO')" />${bundle.msg.migrateNorstartoIPO}</option>
			    <option value="migrateACStoIPO" ><s:property value="getText('migrateACStoIPO')" />${bundle.msg.migrateACStoIPO}</option>
			    <option value="migrateIntegraltoIPO" ><s:property value="getText('migrateIntegraltoIPO')" />${bundle.msg.migrateIntegraltoIPO}</option>
			</select>
			</td>
		</tr>
		
		<tr class="osm_table05_td">
			<td class="osm_text" ><s:property value="getText('')" />${bundle.msg.industry}</td>
			  <td>
			  	<select id="indstry" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}"> 
				    <option value="" >Choose one</option>
				    <option value="agricultureConstMining" ><s:property value="getText('agricultureConstMining')" />${bundle.msg.agricultureConstMining}</option>
				    <option value="educationalServices" ><s:property value="getText('educationalServices')" />${bundle.msg.educationalServices}</option>
				    <option value="financialServicesFinIns" ><s:property value="getText('financialServicesFinIns')" />${bundle.msg.financialServicesFinIns}</option>
				    <option value="financialServicesRealEst" ><s:property value="getText('financialServicesRealEst')" />${bundle.msg.financialServicesRealEst}</option>
				    <option value="government" ><s:property value="getText('government')" />${bundle.msg.government}</option>
				    <option value="healthServices" ><s:property value="getText('healthServices')" />${bundle.msg.healthServices}</option>
				    <option value="hospitality" ><s:property value="getText('hospitality')" />${bundle.msg.hospitality}</option>
				    <option value="InfoandComm" ><s:property value="getText('InfoandComm')" />${bundle.msg.InfoandComm}</option>
				    <option value="manufacturing" ><s:property value="getText('manufacturing')" />${bundle.msg.manufacturing}</option>
				    <option value="otherServices" ><s:property value="getText('otherServices')" />${bundle.msg.otherServices}</option>
				    <option value="proffesionalServices" ><s:property value="getText('proffesionalServices')" />${bundle.msg.proffesionalServices}</option>
				    <option value="retail" ><s:property value="getText('retail')" />${bundle.msg.retail}</option>
				    <option value="utilitiesandTransp" ><s:property value="getText('utilitiesandTransp')" />${bundle.msg.utilitiesandTransp}</option>
				    <option value="wholesale" ><s:property value="getText('wholesale')" />${bundle.msg.wholesale}</option>
				    <option value="other" ><s:property value="getText('other')" />${bundle.msg.other}</option>
				</select>
			</td>
		</tr>
		
		<tr class="osm_table05_td">
			<td class="osm_text" ><s:property value="getText('employeesalllocation')" />${bundle.msg.employeesalllocation}</td>
			<td><select id="employeesallloc" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}> 
			 <option value="" >Choose one</option>
			   <option value="1-19" />
		       <option value="20-29" />
		       <option value="30-49" />
		       <option value="50-99" />
		       <option value="100-249" />
		       <option value="250-999" />
		       <option value="1000 or over" />    
			</select>
			</td>
		</tr>
		
		<tr class="osm_table05_td">
			<td class="osm_text" ><s:property value="getText('employeesthislocation')" />${bundle.msg.employeesthislocation}</td>
			<td><select id="employeesthisloc" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}> 
			 <option value="" >Choose one</option>
			   <option value="1-19" />
		       <option value="20-29" />
		       <option value="30-49" />
		       <option value="50-99" />
		       <option value="100-249" />
		       <option value="250 or over" />
		    </select>
			</td>
		</tr>
		
		<tr class="osm_table05_td">
			<td class="osm_text" ><s:property value="getText('totallocations')" />${bundle.msg.totallocations}</td>
			<td><select id="totalloc" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}> 
			 <option value="">Choose one</option>
			  <option value="1" />
		      <option value="2-4" />
		      <option value="5-9" />
		      <option value="10-14" />
		      <option value="15-19" />
		      <option value="20-24" />
		      <option value="25-32" />
		      <option value="33 or over" />
		    </select>
			</td>
		</tr>
		
		
		
	
		<tr>
			<th colspan="2" height="20">&nbsp;</th>
		</tr>
		
			
</table>
<div class="controls">
		
				<input type="button" class="button gray" value="Cancel" onClick="return popUp();" disabled="${pageFlow.installBaseCreationReadOnlyFlag}"/>
				<input type="button" class="button gray" value="Back"/>
				<input type="button" class="button gray" id="uploadBtn" onClick="return validateipo('uploadFile');" value="Next" />
				
	</div>
	<script>
		var ipoFlag = document.getElementById('remotecon').value;
		enableNewRowDiv(ipoFlag);
	</script>
</s:form>