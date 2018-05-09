<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.grt.dto.TechnicalOrderDetail"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.grt.dto.RegistrationFormBean" %>
<%@ page import="java.util.List"%>
<%@ page import="com.grt.dto.Pager"%>
<%@ include file="/includes/context.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/views/view-install-base.css" />"/>
<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/plugins/jquery.dataTables_themeroller.css" />"/>
<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/plugins/jquery-ui.css" />"/>

<script type="text/javascript"
	src="<c:url context="${ _path }" value="/scripts/plugins/jquery.dataTables.min.js" />"></script>
<script type="text/javascript"
	src="<c:url context="${ _path }" value="/scripts/plugins/jquery.dataTables.columnFilter.js" />"></script>
<script type="text/javascript"
	src="<c:url context="${ _path }" value="/scripts/plugins/dynamicTable.js" />"></script>
<script type="text/javascript"
	src="<c:url context="${ _path }" value="/scripts/installbase/grt.viewinstallbase.js" />"></script>

<script type="text/javascript">

	//If No data available in table Disable Export 
	$( document ).ready(function() {	
		if($("#productListTable").dataTable().fnSettings().aoData.length == 0 ) {
			$("#exportBtn").attr("disabled", true);
			$("#exportBtn").removeClass("gray");
		}
		
	});
	function clearValueOnUncheck(object, item){
		//alert('Item:'+ item +'	 Object checked:'+object.checked);
		if(!object.checked){
			document.getElementById('remainingQuantity'+item).value='';
			document.getElementById('quantityRemoved'+item).innerHTML = '';
			document.getElementById('hidQuantityRemoved'+item).value = '';
		}
	}

	function changeValues(item){
		var revised = document.getElementById('remainingQuantity'+item).value;
		var existing = document.getElementById('availableQuantity'+item).value;
		//alert('revised	:'+revised + 'existing	:'+existing);
		if(parseInt(revised) < 0){
			convertAlertToModelPopUp('Revised Quantity cannot be less than Zero.');
			document.getElementById('remainingQuantity'+item).value='';
			document.getElementById('quantityRemoved'+item).innerHTML = '';
			document.getElementById('hidQuantityRemoved'+item).value = '';
			return false;
		} else if(revised.trim() == ''){
			document.getElementById('quantityRemoved'+item).innerHTML = '';
			document.getElementById('hidQuantityRemoved'+item).value = '';
		} else if(parseInt(revised) >= parseInt(existing)){
			convertAlertToModelPopUp('Revised Quantity cannot be greater than or equal to Existing Quantity.');
			document.getElementById('remainingQuantity'+item).value='';
			document.getElementById('quantityRemoved'+item).innerHTML = '';
			document.getElementById('hidQuantityRemoved'+item).value = '';
			return false;
		} else{
			//alert('Calculating removed qty...');
			document.getElementById('quantityRemoved'+item).innerHTML = parseInt(existing)-parseInt(revised);
			document.getElementById('hidQuantityRemoved'+item).value = parseInt(existing)-parseInt(revised);
		}
	}

	function selectNunselect(obj){
		var i=0;
		if(obj.checked){
			while(document.getElementById('deleted'+i) != null){
				document.getElementById('deleted'+i).checked = true;
				i++;
			}
		} else {
			while(document.getElementById('deleted'+i) != null){
				document.getElementById('deleted'+i).checked = false;
				document.getElementById('remainingQuantity'+i).value = '';
				document.getElementById('quantityRemoved'+i).innerText = '';
				document.getElementById('hidQuantityRemoved'+i).value = '';
				i++;
			}
		}
		return false;// to be removed
	}

	function updateValues(saveType){
		var checkFlag = 0;
		var i=0;
		var mcWithActive = '';
		while(document.getElementById('deleted'+i) != null){
			if(document.getElementById('deleted'+i).checked){
				checkFlag++;
				if(document.getElementById('remainingQuantity'+i).value == ''){
					close_popup()
					convertAlertToModelPopUp('Updated quantity value is not assigned for one or more equipments selected for removal.');
					return false;
				}
				if(document.getElementById('activeContract'+i).value != ''
					&& document.getElementById('activeContract'+i).value == 'Yes'){
					mcWithActive = mcWithActive + document.getElementById('materialCode'+i).value + ", ";
				}
			}
			i++;
		}
		document.getElementById('saveType').value = saveType;
		if(checkFlag == 0){
			convertAlertToModelPopUp('You must select equipment for removal to '+saveType+' this request.');
			return false;
		} else if(saveType == 'submit'){
			if(mcWithActive != ''){
				if(confirm('Equipment/s selected '+ mcWithActive +' for removal have active contract associated with it, click OK to continue and Cancel to modify selection.')){
					show_popup( "Submit for Equipment Removal is in progress", "Please Wait..." );
					return true;
				} else {
					return false;
				}
			}
			if(confirm('Equipment/s selected for removal, click OK to continue and Cancel to modify selection.')){
				show_popup( "Submit for Equipment Removal is in progress", "Please Wait..." );
				return true;
			} else {
				return false;
			}
		}
	}

	// Begin: Hour Glass Implementation
	function show_popup(msg, title) {

		document.getElementById("dialog-underlay").style.display = "block";
		if(title) {
			document.getElementById("popup-window-title").innerHTML = title;
		}
		document.getElementById("popup-window-content").innerHTML = msg;

		var elePopup = document.getElementById("popup-window");
		var scroll = getScroll();
		var divH = getLength(elePopup.style.height, scroll.h);
		var divW = getLength(elePopup.style.width, scroll.w);
		elePopup.style.top = (scroll.t + (scroll.ch - divH)/2) + "px";
		elePopup.style.left = "auto";

	}

	function close_popup(){
		document.getElementById("dialog-underlay").style.display = "none";
		document.getElementById("popup-window").style.left = "-1000em";
	}
	// End: Hour Glass Implementation

	function cancelConfirmation(){
		if(confirm('Cancel on this registration will be taken back to the GRT Home page and all data entered will be lost. Are you sure you would like to cancel this registration?')){
			return true;
		} else {
			return false;
		}
	}

	function cancelConfirm() {
		if(confirm("Cancel on this registration will take you back to the Registration List and all data entered will be lost. Are you sure you would like to cancel this registration?")) {
	 		return true;
	 	} else {
			return false;
		}
	}

	function showMessageToUser(){
		//alert('Show results after submission...');
		var eqrMessage = document.getElementById('eqrMessage').value;
		var returnCode = document.getElementById('eqrReturnCode').value;
		//alert('EQR Return Code:'+returnCode);
		if(eqrMessage != null && eqrMessage.length > 0){
			//alert('Message is not empty..');
			close_popup();
			if(returnCode == 'S'){
				eqrMessage = 'Your Equipment Removal request is submitted, you will receive notification email(s).';
			}
			if(returnCode == '100'){
				eqrMessage = 'System Outage Alert - There is currently a system outage preventing the submission of your request.	The System Administrator has been notified to resolve this issue.	Your registration has been saved and will need to be resubmitted once the outage has been resolved. A notification will be added to the Announcement section of the GRT Home page once system has been restored.	 We apologize for the inconvenience.';
			}
			convertAlertToModelPopUp(eqrMessage);
			if(returnCode == 'S' || returnCode == '100'){
				//alert('Return Code is :'+returnCode);
				var url = document.getElementById('registrationListAction').href;
				document.getElementById('viewInstallBaseForm').action=url;
				document.getElementById('viewInstallBaseForm').submit();
				show_popup( "Navigation to Registration List is in progress", "Please Wait..." );
			}
			//document.getElementById("dialog-underlay").style.display = "block";
			//document.getElementById("eqr-window").style.left = "auto";
			//document.getElementById("eqrfinalmessage").innerHTML = eqrMessage.value;
		}
	}

	function numericFilter(txb) {
		 txb.value = txb.value.replace(/[^0-9]/ig, "");
	}

	function sortByField(field){
		var sortByCount = document.getElementById('sortByCount').value;
		//alert('Previous :'+document.getElementById('sortBy').value+'	 Present	:'+field);
		if(document.getElementById('sortByScreen').value == 'EQR' && document.getElementById('sortBy').value == field){
			if(sortByCount == 1) {
				sortByCount = 0;
			} else {
				sortByCount = 1;
			}
		} else {
			sortByCount = 0;
		}
		document.getElementById('sortByCount').value = sortByCount;
		document.getElementById('sortBy').value = field;
		document.getElementById('sortByScreen').value = 'EQR';
		var url = document.getElementById('dynamicSortByFieldAction').href;
		document.getElementById('viewInstallBaseForm').action=url;
		document.getElementById('viewInstallBaseForm').submit();
	}

	function determineSortSelection(){
		var count = document.getElementById('sortByCount').value;
		var field = document.getElementById('sortBy').value;
		var screen = document.getElementById('sortByScreen').value;
		setSortChange(screen, field, count);
	}

	function setSortChange(screen, field, count){
		//alert('screen:'+screen+', field:'+field+', count:'+count);
		if(screen == 'EQR'){
			if(count == 1){
				document.getElementById(field+'Sort').innerHTML = '<netui:image src="/grtWebProject/framework/skins/ptlSales/images/arrow_up.gif"/>';
			} else {
				document.getElementById(field+'Sort').innerHTML = '<netui:image src="/grtWebProject/framework/skins/ptlSales/images/arrow_down.gif"/>';
			}
		} else {
			document.getElementById(field+'Sort').innerHTML = '<netui:image src="/grtWebProject/framework/skins/ptlSales/images/arrow_down.gif"/>';
		}
	}
	
	$(function() {

		var NewRegistration = Backbone.View.extend({

			el : $('#viewInstallBaseForm'),

			redirectToLocationPage : function(){
					 var href	= this.el.find('#backToLocationAction').attr('href');
					 window.location = href;
			},

			events : {

				'click #backToLocationPage' : 'redirectToLocationPage'
			}

		});

		newRegistration = new NewRegistration();
		/*
		//form Submit
		$( "#saveSiteRegistrationForm" ).submit(function() {
			var fullSoldTo = $("#saveSiteRegistrationForm input[name=actionForm.soldToId]").val();
			var soldToId = '';
			if( fullSoldTo ){
				soldToId = fullSoldTo.split(' ')[0];
			}
			 $("#saveSiteRegistrationForm input[name=actionForm.soldToId]").val(soldToId);
		});*/

	});

	$(document).ready(function() {
		//determineSortSelection();
	});
</script>
<script type="text/javascript">
	$(document).ready(function(){
		var flag=${actionForm.listSizeFlag};
		var msg=${grtConfig.installBaseListSizeMsg};
		if(flag==true)convertAlertToModelPopUp(msg);
	});
</script>
<script type="text/javascript">
	//showMessageToUser();
	$(".loading-overlay").hide();
	function show_loader(){
		$(".loading-overlay").show();
	}
	function saveDTSearchVal(){
		//Add dt param
		var value = $("div#productListTable_filter input").val();
		$("#dtSearchVal").val(value);
	}
</script>

<!-- start page content-wrap -->
<div class="content-wrap view-install-base">

	<h1 class="page-title">View Install Base</h1>

	<!-- start page content -->
	<div class="content">

		<!-- start registration summary -->
		<%@ include file="grtViewIBBanner.jsp" %>
		<!-- end registration summary -->

		<!-- start viewInstallBaseForm -->
		<form name="viewInstallBaseForm" id="viewInstallBaseForm" action="generateIBWorkSheet.action" method="post">

			<input type="hidden"	id="registrationId" name="actionForm.registrationId" value="${fn:escapeXml(actionForm.registrationId)}"/>
			<input type="hidden"	id="soldToId" name="actionForm.soldToId" value="${fn:escapeXml(actionForm.soldToId) }"/>
			<input type="hidden"	id="salMigrationOnly" name="actionForm.salMigrationOnly"	value="${fn:escapeXml(actionForm.salMigrationOnly)}"/>
			<input type="hidden"	id="technicalOrderId" name="actionForm.technicalOrderId"	value="${ fn:escapeXml(actionForm.technicalOrderId) }"/>
			<input type="hidden"	id="materialsAfterExclusionList" name="actionForm.materialsAfterExclusionList"	value="${ fn:escapeXml(actionForm.materialsAfterExclusionList) }"/>
			<input type="hidden"	id="saveType" name="actionForm.saveType"	value="${ fn:escapeXml(actionForm.saveType) }"/>
			<input type="hidden"	id="eqrMessage" name="actionForm.message"	value="${ fn:escapeXml(actionForm.message) }"/>
			<input type="hidden"	id="eqrReturnCode" name="actionForm.returnCode"	value="${ fn:escapeXml(actionForm.returnCode) }"/>
			<input type="hidden"	id="sortBy" name="actionForm.sortBy" value="${ fn:escapeXml(actionForm.sortBy) }"/>
			<input type="hidden"	id="sortByCount" name="actionForm.sortByCount"	value="${ fn:escapeXml(actionForm.sortByCount) }"/>
			<input type="hidden"	id="sortByScreen" name="actionForm.sortByScreen"	value="${ fn:escapeXml(actionForm.sortByScreen) }"/>

			<a id="registrationListAction" href="<c:url context="${ _path }" value="/viewinstallbase/registrationList.action" />" onclick="anchor_submit_form('viewInstallBaseForm','<c:url context="${ _path }" value="/viewinstallbase/registrationList.action" />');return false;"></a>
			<a id="dynamicSortByFieldAction" href="<c:url context="${ _path }" value="/viewinstallbase/dynamicSortByField.action" />" onclick="anchor_submit_form('viewInstallBaseForm','<c:url context="${ _path }" value="/viewinstallbase/dynamicSortByField.action" />');return false;"></a>

			<c:set var="downloadIBViewFile" value="${actionForm.IBViewFileDownloaded}"/>
			<%
				boolean download = ((Boolean)pageContext.getAttribute("downloadIBViewFile")).booleanValue();

				if (download) {
			%>
				<script>
					window.open('<%=request.getContextPath()%>/files/${actionForm.IBViewFileName}','download', 'height=100, width=100, scrollbars=no');
				</script>
			<%
				}
			%>

			<!-- start existing install base -->
			<div class="data-table-wrap collapse-box">
				<h2 class="collapse-box-header active">Existing Install Base
					<a href="#">
						<span></span>
					</a>
				</h2>
				<div class="data collapse-box-container">
					<!-- start productListTableDiv -->
					<div id="productListTableDiv">
						<table id="productListTable" class="main-data" border="1" cellpadding="5" align="center">
							<thead>
								<tr>
									<!--th width="5%">Select Record</th-->
									<td width="6%">Qty&nbsp;<span id="initialQuantitySort"></span></td>
									<td width="10%">SEID&nbsp;<span id="solutionElementIdSort"></span></td>
									<td width="6%">SE Code&nbsp;<span id="solutionElementCodeSort"></span></td>
									<td width="8%">Material Code&nbsp;<span id="materialCodeSort"></span></td>
									<!--td width="7%">Active Contract?&nbsp;<span id="activeContractExistSort"></span></td>
									<td width="7%">Technically On-Boarded?&nbsp;<span id="technicallyRegisterableSort"></span></td-->
									<td width="18%">Material Code Description&nbsp;<span id="descSort"></span></td>
									<td width="12%">Product Line&nbsp;<span id="productLineSort"></span></td>
									<td width="10%">Serial Number&nbsp;<span id="serialNumberSort"></span></td>
									<td width="8%"> Asset Nickname&nbsp;<span id="errorDescSort"></span></td>
									<td width="8%"> Registration Status&nbsp;<span id="errorDescSort"></span></td>
									<td width="8%"> Contract Numbers(s)&nbsp;<span id="errorDescSort"></span></td>
									<td width="8%"> Access Type&nbsp;<span id="errorDescSort"></span></td>
								</tr>
							</thead>
							
							<!-- Added escapeXml for defect #44  --> 
							<tbody>
								<c:forEach items="${actionForm.materialEntryList}" var="item" varStatus="container">
									<tr title="Pipeline IB Quantity:${item.pipelineIBQuantity} Pipeline EQR Quantity:${item.pipelineEQRQuantity}">

										<td align="center">
											${fn:escapeXml(item.initialQuantity)}
										</td>
										<td align="left">
											${fn:escapeXml(item.solutionElementId)}
										</td>
										<td align="left">
											${fn:escapeXml(item.solutionElementCode)}
										</td>
										<td align="left">
											${fn:escapeXml(item.materialCode)}
										</td>
										<td align="left" title="${item.description}">
											${fn:escapeXml(item.description)}
										</td>
										<td align="left">
											${fn:escapeXml(item.productLine)}
										</td>
										<td align="left">
											${fn:escapeXml(item.serialNumber)}
										</td>
										<td align="left">
											${fn:escapeXml(item.nickName)}
										</td>
										<td align="left">
											${fn:escapeXml(item.regStatus)}
										</td>
										<td align="left">																				
												<%-- ${fn:escapeXml(item.agreements)} --%>
												<c:choose>
													<c:when test="${item.toggleMore eq true}">
														<div class="more">
															<span>${fn:escapeXml(item.toggleAgreementsList)}</span>
															<span class="toggle" onclick="more('${item.agreements}')"> More</span>
														</div>
													</c:when>
												<c:otherwise>
													${fn:escapeXml(item.agreements)}
												</c:otherwise>
												</c:choose>										
										</td>
										<td align="left">
											${fn:escapeXml(item.accessType)}
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>

					</div>
					<!-- end productListTableDiv -->
				</div>
			</div>
			<!-- end existing install base -->

			<div id="viewinstallbasebtndiv" class="controls">
				<input type="button" class="button gray" value="Back" id="backToLocationPage" />
				<a id="backToLocationAction" href="<c:url context="${ _path }" value="/installbase/viewInstallBaseOnly.action"/>"></a>
				<input id="exportBtn" type="Submit" value="Export" class="button gray"	onClick="show_loader(); saveDTSearchVal();" />
			</div>
			<input type="hidden" id="dtSearchVal" name="dtSearchVal" value="${fn:escapeXml(dtSearchVal)}"/>
			
		</form>
		<!-- end viewInstallBaseForm -->
	</div>
	<!-- start page content -->
</div>
<!-- start page content-wrap -->
<!-- start modal alert messages -->
	<div id="showAgreement" class="modal alertMsg" style="display:none;">
		<div class="modal-overlay">
			<div class="modal-content">
				<a class="close" onclick="closeAlert()"><i class="fa fa-close errPopupBtn"></i></a>
				<h2 class="title">Attention</h2>
				<p class="content">
					<img class="msg-icon"
						src="<c:url context="${ _path }" value="/styles/images/messages/icon_warning_light_60x60.png" />"
						alt="info" />
					<span class="msg-txt agreemntMsgTxt"></span>
				</p>
				<div class="controls">
					<a class="button gray alertOkBtn"	onclick="closeAlert()" >OK</a>
				</div>
			</div>
		</div>
	</div>
	<!-- end modal alert messages -->

<%-- ************************************************************* --%>
<%-- ***** BELOW IS DEPRECATED CODE - NEED REVIEW AND REMOVE ***** --%>
<%-- ************************************************************* --%>

<%-- <link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/plugins/jquery.dataTables.css" />"/> --%>
<%-- <link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/views/install-base.css" />"/> --%>
<%-- <link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/installbasecreation/view-install-base.css" />"/> --%>
<%-- <link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/viewinstallbase/install_base_creation_1.css" />"/> --%>
<%-- <link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/viewinstallbase/main.css" />"/> --%>

<!-- <body> -->
<!-- Omniture Test
<script language="JavaScript" type="text/javascript" src="/grtWebProject/framework/skins/support/js/s_code.js"></script>
<script language="JavaScript" type="text/javascript" src="/grtWebProject/framework/skins/support/js/p_code.js"></script> -->

<!--div id="popup-window" class="dialog" style="left: -1000em;" >
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
</div-->

<!-- @include file="/com/grt/registration/registrationSteps/productStepStatus.jsp"%-->
<!-- <body onLoad="javascript:determineSortSelection();"> -->
<!--div class="clearfix" id="box7"-->
<!-- 	<div class="clearfix" id="FirstRow">
		<p id="text8">
			View Install Base
		</p>
	 </div>

<br/>	 -->

<!--	Banner -->
<!-- %@ include file="/pages/registration/grtBanner.jsp" %-->
	 <!--	Banner -->

<!--	p colspan="9" class="osm_text_error" height="20" align="left"><netui:label value="${actionForm.message}"/></p>-->
<%
//String eqrStatus = (String)request.getSession().getAttribute("EQRStatus");
//System.out.println("EquipmentRemovalProcess JSP --> EQRStatus:" + eqrStatus);
//if( eqrStatus!=null && (eqrStatus.equalsIgnoreCase("inprocess") || eqrStatus.equalsIgnoreCase("cancelled"))){
%>
	<!--input type="button" value="Cancel" class="osm_large_button" disabled ="true"/-->
<%
//} else if(eqrStatus != null && eqrStatus.equalsIgnoreCase("saved")){
%>
	<!--input type="button" value="Cancel" class="osm_large_button" onClick="return cancelConfirm();"/-->
<%
//} //else {
%>
	<!--input type="button" value="Cancel" class="osm_large_button" onClick="return cancelConfirmation();"/-->
<%
//}
//request.getSession().removeAttribute("EQRStatus");
%>
<%
	//if(request.getSession().getAttribute("eqrFlag") != null && request.getSession().getAttribute("eqrFlag").equals("0") ) {
%>
<!-- input type="button" value="Back" class="osm_large_button"  /-->
<%
//} else {
%>
<!-- input type="button" value="Back" class="osm_large_button" /-->
<%
//}
%>

<!--
<td align="center"-->
	<!--input type="hidden" id="orderId${container.index}" name="item.orderId" value="${ item.orderId }"/-->
	<!--input type="checkbox" name="actionForm.materialEntryList[${container.index}].deleted" id="deleted${container.index}" onClick="javascript:clearValueOnUncheck(this, ${container.index});" disabled="${item.exclusionFlag}"/>
</td>
-->
<!--input type="hidden" id="availableQuantity${container.index}" name="item.initialQuantity" value="${item.initialQuantity }"/-->
<!--input type="hidden" id="description${container.index}" name="item.description" value="${item.description}"/-->
<!--input type="hidden" id="productLine${container.index}" name="item.productLine" value="${item.productLine}"/-->
<!--input type="hidden" id="seCode${container.index}" name="item.solutionElementCode" value="${item.solutionElementCode}"/-->
<!--input type="hidden" id="seId${container.index}" name="item.solutionElementId" value="${item.solutionElementId}"/-->
<!--input type="hidden" id="serialNo${container.index}" name="item.serialNumber" value="${item.serialNumber}"/-->
<!--input type="hidden" id="errorDescription${container.index}" name="item.errorDescription" value="${item.errorDescription}"/-->
<!--input type="hidden" id="errorDescription${container.index}" name="item.errorDescription" value="${item.errorDescription}"/-->
<!--input type="hidden" id="errorDescription${container.index}" name="item.errorDescription" value="${item.errorDescription}"/-->
<!--input type="hidden" id="errorDescription${container.index}" name="item.errorDescription" value="${item.errorDescription}"/-->
