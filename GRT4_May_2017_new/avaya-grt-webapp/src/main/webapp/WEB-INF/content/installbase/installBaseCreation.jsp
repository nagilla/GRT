<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.grt.dto.TechnicalOrderDetail"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.grt.dto.RegistrationFormBean"%>
<%@ page import="java.util.List"%>
<%@ page import="com.grt.dto.Pager"%>
<%@ include file="/includes/context.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/views/install-base-creation.css" />"/>
<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/plugins/jquery.dataTables_themeroller.css" />" />

<script type="text/javascript"
	src="<c:url context="${ _path }" value="/scripts/installbase/grt.installbase.creation.view.js" />"></script>
<script type="text/javascript"
	src="<c:url context="${ _path }" value="/scripts/plugins/dynamicTable.js" />"></script>
<script type="text/javascript"
	src="<c:url context="${ _path }" value="/scripts/plugins/jquery.dataTables.min.js" />"></script>
<script type="text/javascript"
	src="<c:url context="${ _path }" value="/scripts/plugins/jquery.dataTables.columnFilter.js" />"></script>

<!-- start install base creation js lib	-->
<script type="text/javascript">
	
	
	function highlightAllRow() {
		//Highlight the rows with error
		var trList = $("#materialEntryListTable tr");
		$.each(trList, function(index, row){
			var visSpanLength = $(row).find('td.errorTd span.errorSpan:visible').length;
			if( visSpanLength > 0 ){
				$(row).addClass('highlight');
			}else{
				$(row).removeClass('highlight');
			}
		});
	}
	/* set vars */
	var existingInstallBase = null;
	var searchResult;

	/*** start toggles, modals ***/

	/* modal - materialCodeSearchPopUp */
	function openSearchPopUp(id, descId, selectionId) {
		document.getElementById('populateFor').value = id;
		document.getElementById('populateDesc').value = descId;
		document.getElementById('rowId').value = selectionId;
		show('materialCodeSearchPopUp');
		show('fade');
		$(document).keypress(function(e) {
			var code = (e.keyCode ? e.keyCode : e.which);
			if (code == 13) {
				e.preventDefault();
				e.stopPropagation();
				document.getElementById('materialCodeSearchButton').click();
			}
		});
	}
	/* modal - materialCodeSearchPopUp */
	function closePopUp() {
		hide('materialCodeSearchPopUp');
		hide('fade');
	}
	/* modal - warningPopup */
	function warning_popup() {
		document.getElementById("warningPopup").style.display = "block";
		document.getElementById("warningmessage").innerHTML =
			"Do You Want to Continue...";
	}
	/* modal - warningPopup */
	function closeWarningPopup() {
		document.getElementById("warningPopup").style.display = "none";
	}
	/* modal - popup-window-ib */
	function show_popup(msg, title) {
		if (title) {
			document.getElementById("popup-window-title-ib").innerHTML = title;
		}
		document.getElementById("popup-window-content-ib").innerHTML = msg;

		var elePopup = document.getElementById("popup-window-ib");
		var scroll = getScroll();
		var divH = getLength(elePopup.style.height, scroll.h);
		var divW = getLength(elePopup.style.width, scroll.w);
		elePopup.style.top = "250px";
		elePopup.style.left = "auto";

	}
	/* modal - popup-window-ib */
	function close_popup_ib() {
			document.getElementById("popup-window-ib").style.display = "none";
		}
		// End: Hour Glass Implementation
	/* modal - popup-wind, shwipoPopup */
	function showpop() {
		document.getElementById("popup-wind").style.display = "block";
	}
	/* modal - popup-wind, shwipoPopup */
	function closePop() {
		document.getElementById("popup-wind").style.display = "none";
	}
	/* modal - popup-wind, shwipoPopup */
	function shwipoPopup(flashowipo) {
		if (flashowipo) { <%
			if (request.getAttribute("registrationForm") != null) {
					request.setAttribute("registrationForm",
					request.getAttribute("registrationForm"));

			} %>

			showpop();
		}
	}
	/* modal - install_message-window */
	function closeInstallBaseMsg() {
		var returnCode = document.getElementById("installBaseReturnCode").value;
		document.getElementById("install_message-window").style.display = "none";
		var str = document.getElementById("installBasefinalmessage").value;
		if (returnCode == "S" || returnCode == "D" || returnCode == "100") {
			$("div.loading-overlay").show();
			var url = document.getElementById('registrationListAction').href;
			document.getElementById('myform').action = url;
			document.getElementById('myform').submit();
		} else if (returnCode == "ME") {
			document.getElementById("installBasefinalmessage").value = "";
			return false;
		} else {
			var url = document.getElementById('installBaseCreationAction').href;
			document.getElementById('myform').action = url;
			document.getElementById('myform').submit();
		}
	}
	/* modal - install_message-window */
	function showMessageToUser() {
		var installBaseMsg = document.getElementById("installBaseMessage");
		var returnCode = document.getElementById("installBaseReturnCode").value;
		if (installBaseMsg.value) {
			if (returnCode == "100") {
				var alertMesgForSysOutage = $("#errorSystemAlertErrorCode").val() + "###" +  $("#errorSystemAlert").val();
				convertAlertToModelPopUpWithCallBackOnClose(alertMesgForSysOutage, "return closeInstallBaseMsg();");
			} else {
				convertAlertToModelPopUpWithCallBackOnClose(installBaseMsg.value, "return closeInstallBaseMsg();");
			}
		}

	}
	/* modal - message-window */
	function showMessage(message) {
		document.getElementById("message-window").style.display = "block";
		document.getElementById("message").innerHTML = message;
	}
	/* modal - message-window */
	function closeMessage() {
		document.getElementById("message-window").style.display = "none";
	}
	/* modal - message-window */
	function showmaterialCodeValidPopup(materialCodeListSize) {
		var message = "";
		var msg = $("#infInfMCodeInProcess").val();
		if (materialCodeListSize > 0) {
			for (var i = 0; i < materialCodeListSize; i++) {
				var mCode = document.getElementById("materialCodeList" + i + "MCode");
				var regId = document.getElementById("materialCodeList" + i + "RegId");
				var grtMsgForMcode = msg.replace("<MCODE>", mCode.value);
				message = message + grtMsgForMcode + regId.value + "<br>";
			}
			showMessage(message);
		}
	}
	/* toggle - helper */
	function hide(elemId) {
		if(document.getElementById(elemId) != null ) {
			document.getElementById(elemId).style.display = 'none';
		}
	}
	/* toggle - helper */
	function show(elemId) {
		if(document.getElementById(elemId) != null ) {
			document.getElementById(elemId).style.display = 'block';
		}
	}
	/* toggle - helper */
	function setVisible(id) {
		if(document.getElementById(id) != null ) {
			document.getElementById(id).style.visibility = "visible";
		}
	}
	/* toggle - helper */
	function setInVisible(id) {
		if(document.getElementById(id) != null ) {
			document.getElementById(id).style.visibility = "hidden";
		}
	}
	/* toggle - extisting intall base table */
	function toggle() {
		var ele = document.getElementById("toggleText");
		var buttons = document.getElementById("toggleButtonsEIB");
		if (ele.style.display == "block") {
			if (buttons != null) {
				buttons.style.display = "none";
			}
		} else {
			if (buttons != null) {
				buttons.style.display = "block";
			}
			if (existingInstallBase == null) {
				var soldTo = document.getElementById("bannerSoldTo").innerHTML;
				show_popup("Retrieving Existing Install Base ", "Please Wait...");
				populateExistingInstallBaseTable(soldTo);

			}
		}
	}
	/* toggle - material entry table */
	function toggleMaterialEntry() {
		var buttons = document.getElementById("toggleButtonsEIB");
		var text = document.getElementById("displayOptions");
		if (ele.style.display == "block") {

		} else {
		
		}
	}
	/* toggle - soir table */
	function toggleSoirData() {
		var soirEle = document.getElementById("soirData");
		var soirButtons = document.getElementById("soirButtons");
		if (soirEle.style.display == "block") {
			soirEle.style.display = "none";
			document.getElementById('SOIRDivToggleSymbol').innerHTML = '+';
			if (soirButtons != null) {
				soirButtons.style.display = "none";
			}
		} else {
			soirEle.style.display = "block";
			document.getElementById('SOIRDivToggleSymbol').innerHTML = '-';
			if (soirButtons != null) {
				soirButtons.style.display = "block";
			}
		}
	}

	function toggleSaveSubmit(disabled) {
		var save = document.getElementById("saveRegistration");
	}

	function enableSaveSubmit(salMigrationOnlyFlag, installBaseCreationReadOnlyFlag) {
		if (salMigrationOnlyFlag) {
			toggleSaveSubmit(false);
			return;
		}
		if (!installBaseCreationReadOnlyFlag && anyRecordForRegistration()) {
			toggleSaveSubmit(false);
			return;
		}
	}

	function showSkipInstallBaseCreationTR(regionAdministrable) {
		if (regionAdministrable) {
			document.getElementById("skipInstallBaseCreationLink").style.display = "block";
		}
	}

	function show_validation_msg_desc_mc(materialCode, desc, matertialExcluded,
		select, exclusionSource) {
		var materialCodeError = document.getElementById(materialCode + "Errors");
		var materialExcludedError = document.getElementById(materialCode +
			"MaterialExcludedError");

		var description = null;
		if (desc != null) {
			description = desc.replace(/^\s+|\s+$/g, '');
		}

		materialCode = document.getElementById(materialCode);
		if (matertialExcluded == "true") {
			select.checked = false;
			select.disabled = true;
			materialCode.style.borderColor = "#0C0480";
			materialCode.style.borderStyle = "solid";
			materialExcludedError.style.display = "inline";
			return;
		} else if (matertialExcluded == "false") {
			if (select.disabled) {
				select.checked = true;
			}
			select.disabled = false;
			materialCode.style.borderColor = "";
			materialCode.style.borderStyle = "";
			materialExcludedError.style.display = "none";
		}
		var installBaseReadOnly = ${installBaseCreationReadOnlyFlag};
		//GRT 4.0 Change : handle undefined
		if (installBaseReadOnly && select!=undefined) {
			select.disabled = true;
		}

		if ((description == null) || (description == '')) {
			materialCode.style.borderColor = "red";
			materialCode.style.borderStyle = "solid";
			if (materialCodeError != null) {
				materialCodeError.style.display = "inline";
			}

		} else {
			materialCode.style.borderColor = "";
			materialCode.style.borderStyle = "";

			if (materialCodeError != null) {
				materialCodeError.style.display = "none";
			}
		}

	}

	function show_validation_msg_desc(materialCode, desc, matertialExcluded, isTR,
		type, exclusionSource) {
		//GRT 4.0 Change : Handle Undefined cases for datatable
		if( document.getElementById(materialCode)!=undefined || materialCode instanceof HTMLElement || materialCode[0] instanceof HTMLElement ){
			if (type == 'MCUL') {
				var materialCodeMcul = null;
				if( ( materialCode instanceof HTMLElement) == false && materialCode[0] instanceof HTMLElement == false ){
					materialCodeMcul = document.getElementById(materialCode);
				}else{
					materialCodeMcul = materialCode[0] == undefined ? materialCode : materialCode[0];
				}
				var selectMcul = document.getElementById(materialCodeMcul.id + "Select");
	
				var materialExcludedErrorHidden1 = document.getElementById(materialCodeMcul.id +
					"MaterialExcludedErrorHidden");
				if (exclusionSource != null) {
					if (exclusionSource == 'PLDS') {
						exclusionSource = $("#infIBFedAutomatic").val();
					} else if(exclusionSource=="DEFECTIVE" || exclusionSource == "NMPC"){
						exclusionSource = $("#defectiveCodeErrMsg").val();
					} else if(exclusionSource== "ESNA" || exclusionSource== 'ESNA'){
						exclusionSource = $("#errEsnaCode").val();
					}
					else {
						exclusionSource = $("#errOffrTCNotQual").val();
					}
					if( materialExcludedErrorHidden1!=null )
						materialExcludedErrorHidden1.innerHTML = exclusionSource;
				}
				//GRT 4.0 Change : undefined case
				if( selectMcul!=undefined ){
					show_validation_msg_desc_mc(materialCode, desc, matertialExcluded,
					selectMcul, exclusionSource);
				}
			}
			//GRT 4.0 Change : 
			if( ( materialCode instanceof HTMLElement) == false && materialCode[0] instanceof HTMLElement == false ){
				materialCode = document.getElementById(materialCode);
			}else{
				materialCode = materialCode[0] == undefined ? materialCode : materialCode[0];
			}
			validateQuantity(materialCode);
			clearMaterialCodeErrors(materialCode, "true");
			var select = document.getElementById(materialCode.id + "Select");
			if (!materialCode.value) {
	
				return;
	
			}
			var materialCodeErrorsMaterialCode = document.getElementById(materialCode.id +
				"ErrorsMaterialCode");
			if(materialCodeErrorsMaterialCode!=undefined )
				materialCodeErrorsMaterialCode.innerHTML = materialCode.value;
			
			var materialCodeDesc = document.getElementById(materialCode.id + "Desc");
			if(desc != null) {
				materialCodeDesc.innerHTML = desc;
			} else {
				materialCodeDesc.innerHTML = "";
			}
			var materialCodeDescHidden = document.getElementById(materialCode.id +
				"DescHidden");
			materialCodeDescHidden.value = desc;
			var materialExclusionHidden = document.getElementById(materialCode.id +
				"ExclusionHidden");
			materialExclusionHidden.value = matertialExcluded;
			var materialExcludedError = document.getElementById(materialCode.id +
				"MaterialExcludedError");
			var installBaseReadOnly = ${installBaseCreationReadOnlyFlag};
	
			//For TR Eligible
			var isTREligible = document.getElementById(materialCode.id + "isTR");
			isTREligible.innerHTML = isTR;
			var isTREligibleHidden = document.getElementById(materialCode.id +
				"isTRHidden");
			isTREligibleHidden.value = isTR;
	
			if (matertialExcluded == "true" || matertialExcluded == true) {
				select.checked = false;
				select.disabled = true;
				materialCode.style.borderColor = "#0C0480";
				materialCode.style.borderStyle = "solid";
				materialExcludedError.style.display = "inline";
			} else if ((matertialExcluded == "false" || matertialExcluded == false) && select!=undefined) {
				if (select.disabled) {
					select.disabled = false;					
				}
				select.checked = true;
				materialCode.style.borderColor = "";
				materialCode.style.borderStyle = "";
				materialExcludedError.style.display = "none";
			}
			//GRT 4.0 Change : handle undefined
			if (installBaseReadOnly && select!=undefined) {
				select.disabled = true;
			}
			if (!desc) {
				clearMaterialCodeErrors(materialCode, "false");
			}
			materialCode = $(materialCode);
			var materialCodeID = materialCode.attr('id');
			highlightRow(materialCodeID);
		}
	}

	/*** end toggles, modals, popups ***/

	/*** start helper methods ***/

	function populateExistingInstallBaseTable(soldTo) {
	
	//Sort
	/* Create an array with the values of all the input boxes in a column */
	jQuery.extend(jQuery.fn.dataTableExt.oSort['dom-text-asc'] = function(x,y) {
		var retVal;
		x = $.trim(x);
		y = $.trim(y);
	 
		if (x==y) retVal= 0;
		else if (x == "" || x == "&nbsp;") retVal=  1;
		else if (y == "" || y == "&nbsp;") retVal=  -1;
		else if (x > y) retVal=  1;
		else retVal = -1;  
	 
		return retVal;
	});
	jQuery.extend( jQuery.fn.dataTableExt.oSort['dom-text-desc'] = function(y,x) {
		var retVal;
		x = $.trim(x);
		y = $.trim(y);
	 
		if (x==y) retVal= 0; 
		else if (x == "" || x == "&nbsp;") retVal=  1;
		else if (y == "" || y == "&nbsp;") retVal=  -1;
		else if (x > y) retVal=  1;
		else retVal = -1; 
	 
		return retVal;
	 });
	
	
	
		$(".loading-overlay").show();
		$.ajax({
			url: _path +
				'/installbase/json/getExistingInstallBaseRecords.action?soldTo=' + soldTo,
			dataType: 'json',
			success: function(data) {

				var flag = ${actionForm.listSizeFlag};
				if (flag == true) { 
				convertAlertToModelPopUp($("#installBaseListSizeMsgWarningCode").val(), $("#installBaseListSizeMsg").val()); 
				}

				existingInstallBase = data;
				$('#existingInstallBaseTable tr').not(':first').remove();
				var html = '';
				for (var i = 0; i < data.length; i++) {
					var mc = data[i].materialCode;
					var initQu = data[i].initialQuantity != null ? data[i].initialQuantity :
						'';
					var desc = data[i].description;
					var prodLine = "";
					if(data[i].productLine != null) {
						prodLine = data[i].productLine;
					}
					var techRegistrable = data[i].technicallyRegisterable;
					html +=
						'<tr align="center"><td>' + initQu +
						'</td><td>' + mc + '</td><td> ' +
						desc + '</td><td>' + prodLine +
						'</td><td>' + techRegistrable + '</td></tr>';
				}
				$('#existingInstallBaseTable tbody').html(html);

				close_popup_ib();
				$("#popup-window").hide();
				$(".loading-overlay").hide();
				//Implement Pagination
				$('#existingInstallBaseTable').dataTable({
						"sPaginationType": "full_numbers",
						"scrollX":true,
						"autoWidth": false,
						"aoColumns": [
						{sDefaultContent: ""},
			            { "sType": "dom-text-numeric", defaultContent: "" },
			            {sDefaultContent: ""},
			          	{ "sType": "dom-text", defaultContent: "" },
			            {sDefaultContent: ""},
						],
						"order": [[ 3, "asc" ], [ 1, "asc" ]],
					}).columnFilter();
			}
		});
	}

	function onMaterrialCodeClick(index) {	
		var populateForId = document.getElementById('populateFor').value;
		var populateDescId = document.getElementById('populateDesc').value;		
		document.getElementById(populateForId).value = searchResult[index].parentMaterialCode;
		document.getElementById(populateDescId).innerHTML = searchResult[index].parentMaterialCodeDescription;
		var descHiddenId = "materialCode" + document.getElementById('rowId').value +
			"DescHidden";
		document.getElementById(descHiddenId).value = searchResult[index].parentMaterialCodeDescription;
		closePopUp();
		//Added for Defect # 539
		$("#"+populateForId).val(searchResult[index].parentMaterialCode);
		validate_material_code(document.getElementById(populateForId));
		//End added for Defect # 539
		//Clear existing errors if any.
		clearMaterialCodeErrors(document.getElementById(populateForId), "true");
		
	}

	function performSearch() {
		var searchText = document.getElementById('searchText').value;
		if ($.trim(searchText).length > 0) {
			$(".loading-overlay").show();
			$("#noResultsId.error-bar").hide();
			$.ajax({
				url: _path + '/installbase/json/getEligibleGroups.action?seCodes=' +
					searchText,
				dataType: 'json',
				success: function(data) {
					searchResult = data;

					$('#resultTable tr').not(':first').remove();
					var html = '';
					for (var i = 0; i < data.length; i++) {
						var isMain = data[i].mainMaterialCode;
						var seCode = data[i].seCode != null ? data[i].seCode : '';
						html += '<tr><td><a href= "javascript:void(0)" onclick =" onMaterrialCodeClick(' +
							i + ')">' + data[i].parentMaterialCode + '</a></td><td>' + data[i].parentMaterialCodeDescription +
							'</td><td>' + seCode + '</td><td>' + data[i].groupDescription +
							'</td></tr>';
					}

					//Destroy Data Table
					var dTExists = $('#resultTable').hasClass('dataTable');
					if (dTExists) {
						$('#resultTable').DataTable().destroy();
					}
					$('#resultTable tbody').html(html);
					$('#resultTable').show();
					//Look UP
					$('#resultTable').dataTable({
							"sPaginationType": "full_numbers",
							"scrollX":true,
							"autoWidth": false
						})
						.columnFilter();
						if (data.length == 0) {
							$("#noResultsId.error-bar").insertAfter("table#resultTable");
							$("#noResultsId.error-bar").show();
						}
					$(".loading-overlay").hide();
				},
				error: function(data) {
					$(".loading-overlay").hide();
				}
			});
		}
	}

	function download_template_upload_file(theFile) {
		var uploadFile = document.getElementById(theFile);
		if (uploadFile.value == "") {
			 window.open('<%=request.getContextPath()%>/others/downloadMaterialEntryUpload.jsp','download'); 
			return false;
		} else {
			//Validation for file format
			var ext = $('#' + theFile).val().split('.').pop().toLowerCase();
			if ($.inArray(ext, ['xlsx', 'xls']) == -1) {
				convertAlertToModelPopUp($("#incorrectFormatErrorCode").val(), $("#incorrectFormat").val());
				return false;
			}

			$("div.loading-overlay").show();
			document.getElementById('myform').action = document.getElementById(
				'uploadAction').href;
			document.getElementById('myform').submit();
			return true;
		}
	}

	function materialEntryVal() {
		var inputs = document.getElementsByName("materialEntry");
		var i;
		for (i = 0; i < inputs.length; i++) {
			if (inputs[i].checked) {
				break;
			}
		}
		var materialEntryVer = document.getElementById('materialEntryVersion' + i);
		if (materialEntryVer != null) {
			var materialEntryVerVal = document.getElementById('materialEntryVersion' + i)
				.value;
			var materialEntryQtyVal = document.getElementById('materialCode' + i +
				'InitialQuantity').value;
			if (materialEntryVerVal == '') {
				//convertAlertToModelPopUp('Select the Material Entry version');
				convertAlertToModelPopUp($("#selMatEntryWarningCode").val(), $("#selMatEntry").val());
				return false;
			} else if (materialEntryQtyVal != '' && parseInt(materialEntryQtyVal) != 1) {
				convertAlertToModelPopUp($("#selectAtlestOneQualityErrorCode").val(), $("#selectAtlestOneQuality").val());
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}

	}

	function anyRecordForRegistration() {
		var materialEntryListSize = <c:out value = "${materialEntryListSize}" /> ;
		var pendingOrdersSize = <c:out value = "${pendingOrdersSize}" /> ;
		for (var i = 0; i < materialEntryListSize; i++) {
			var select1 = document.getElementById("materialCode" + i + "Select");
			if (select1.checked) {
				return true;
			}
		}
		for (var i = 0; i < pendingOrdersSize; i++) {
			var select2 = document.getElementById("pendingOrder" + i + "Select");
			if (select2.checked) {
				return true;
			}
		}
		return false;
	}

	function setSkipInstallBaseCreation() {
		var msg = $("#infIncOrgTrancNo").val();
		var callback = function() {
			var pendingOrdersSize = ${pendingOrdersSize};
			for (var i = 0; i < pendingOrdersSize; i++) {
				var select2 = document.getElementById("pendingOrder" + i + "Select");
				select2.disabled = true;
			}
			var materialEntryListSize = ${materialEntryListSize};
			for (var i = 0; i < materialEntryListSize; i++) {
				var materialCode = document.getElementById("materialCode" + i);
				var quantity = document.getElementById("materialCode" + i +
					"InitialQuantity");
				var select1 = document.getElementById("materialCode" + i + "Select");
				materialCode.disabled = true;
				quantity.disabled = true;
				select1.disabled = true;
			}
			var uploadFile = document.getElementById("uploadFile");
			if (uploadFile != null) {
				uploadFile.disabled = true;
			}
			var uploadBtn = document.getElementById("uploadBtn");
			if (uploadBtn != null) {
				uploadBtn.disabled = true;
			}
			var addBtn = document.getElementById("addBtn");
			if (addBtn != null) {
				addBtn.disabled = true;
			}
			var skipInstallBaseCreation = document.getElementById(
				"skipInstallBaseCreation");
			skipInstallBaseCreation.value = true;
			toggleSaveSubmit(false);
		};
		grtcommonPopup.config({
			title: "Confirmation",
			msg: msg,
			close_callback_func: callback
		});
		grtcommonPopup.show();

	}

	function serialNumDB(materialEntryListSize, salMigrationOnlyFlag, remoteReq) {

		// Setting parameters for DWR call
		var soirSize = ${pendingOrdersSize};
		var allMaterialCodesList = new Array();
		var allSerialNumberList = new Array();
		var allOrderIdList = new Array();
		var allelementIdList = new Array();
		var allIndexList = new Array();
		var count = 0;

		var remote = remoteReq;
		var materialCodesList = new Array();
		var serialNumberList = new Array();
		var orderIdList = new Array();
		for (var mindex = 0; mindex < materialEntryListSize; mindex++) {
			var select = document.getElementById("materialCode" + mindex + "Select");
			var serialNumber = document.getElementById("materialCode" + mindex +
				"SerialNumber");
			if (serialNumber.value != null && serialNumber.value != "" && select.checked) {
				materialCodesList[mindex] = document.getElementById("materialCode" + mindex)
					.value;
				serialNumberList[mindex] = document.getElementById("materialCode" + mindex +
					"SerialNumber").value;
				orderIdList[mindex] = document.getElementById("materialCode" + mindex +
					"orderId").value;

				allMaterialCodesList[count] = document.getElementById("materialCode" +
					mindex).value;
				allSerialNumberList[count] = document.getElementById("materialCode" +
					mindex + "SerialNumber").value;
				allOrderIdList[count] = document.getElementById("materialCode" + mindex +
					"orderId").value;
				allelementIdList[count] = "materialCode";
				allIndexList[count] = mindex;
			} else {
				allMaterialCodesList[count] = null;
				allSerialNumberList[count] = null;
				allOrderIdList[count] = null;
				allelementIdList[count] = "materialCode";
				allIndexList[count] = mindex;
			}
			count = count + 1;
		}

		if (soirSize != null && soirSize > 0) {
			for (var soirindex = 0; soirindex < soirSize; soirindex++) {
				var soirSelect = document.getElementById("pendingOrder" + soirindex + "Select");
				var soirSerialNumber = document.getElementById("pendingOrders" + soirindex + "SerialNumber");
				if (soirSerialNumber!=undefined && soirSerialNumber.value != null && soirSerialNumber.value != "" && soirSelect.checked) {
					allMaterialCodesList[count] = document.getElementById("pendingOrders" + soirindex + "hiddenMc").value;
					allSerialNumberList[count] = document.getElementById("pendingOrders" + soirindex + "SerialNumber").value;
					allOrderIdList[count] = document.getElementById("pendingOrders" + soirindex + "orderId").value;
					allelementIdList[count] = "pendingOrders";
					allIndexList[count] = soirindex;
				} else {
					allMaterialCodesList[count] = null;
					allSerialNumberList[count] = null;
					allOrderIdList[count] = null;
					allelementIdList[count] = "pendingOrders";
					allIndexList[count] = soirindex;
				}
				count = count + 1;
			}
		}

		var duplicateCount = 0;
		/*
		for (var i = 0; i < count; i++) {
			var materialCodeError6 = document.getElementById(allelementIdList[i] +
				allIndexList[i] + "Error6");
			var temp = allelementIdList[i] + allIndexList[i] + "SerialNumber";

			var serialNumber = document.getElementById(allelementIdList[i] +
				allIndexList[i] + "SerialNumber");

			validSerialNumber(serialNumber, materialCodeError6);
			
		}
		*/
		//In case of any errors in selected records throw error
		if(isSelectedRecordsContainsErrors()) {
			convertAlertToModelPopUp($("#notAllValForRegErrorCode").val(), $("#notAllValForReg").val());					
		}
		else if (duplicateCount > 0) {
			close_popup_ib();
			convertAlertToModelPopUp($("#notAllValForRegErrorCode").val(), $("#notAllValForReg").val());
		} else {
			var skipInstallBaseCreation = document.getElementById(
				"skipInstallBaseCreation").value == "true";
			var msg;
			var title = null;
			if (skipInstallBaseCreation) {
				 msg = $("#infSendWrtnReq").val();
				title = "Reminder";
			} else if (salMigrationOnlyFlag) {
				 msg = $("#errRdyToSalMig").val();
			} else if (remote == "Yes") {
				msg = $("#infCnfrmRegOne").val();
			} else if (remote == "No") {
				msg = $("#infCnfrmRegTwo").val();
			} else {
				msg = $("#infCnfrmRegThree").val();
			}

			close_popup_ib();
			$("#warningPopup").show();
			document.getElementById("warningmessage").innerHTML = msg;
			
		}
	}

	function getMaterialVersionMethod(rowId) {
		var matId = "materialCode" + rowId;
		
		if (document.getElementById(matId).value != '') {
			document.getElementById('countId').value = rowId;
			var url = document.getElementById('getMaterialVersionAction').href;
			document.getElementById('myform').action = url;
			document.getElementById('myform').submit();
			return true;
		} else {
			convertAlertToModelPopUp($("#selectAtlestOneMatCodeErrorCode").val(), $("#selectAtlestOneMatCode").val());
			var materialEntryForClear = document.getElementsByName("materialEntry");
			//Clear the User Selection of Radio buttons
			for (var i = 0; i < materialEntryForClear.length; i++) {
				if (materialEntryForClear[i].checked) {
					materialEntryForClear[i].checked = false;
				}
			}
			return false;
		}
	}

	function noEnter(event) {
		if (event && event.keyCode == 13) {
			convertAlertToModelPopUp($("#selCorrectOptionsErrorCode").val(), $("#selCorrectOptions").val());
			return false;
		} else if(event && event.keyCode == 32){
		} else {
			return true;
		}
	}

	function manuallyAddFormSubmit() {
		//Newly Added Code for Submit as per new Architecture
		myform.action = document.getElementById('manuallyAddAction').href;
		myform.submit();
	}

	/*** end helper methods ***/

	/*** start validation ***/

	function checkRequiredFields(materialEntryListSize, salMigrationOnlyFlag) {
		if (!anyRecordForRegistration()) {
			convertAlertToModelPopUp($("#enterValidMatCodeErrorCode").val(), $("#enterValidMatCode").val());
			return false;
		}
		var skipInstallBaseCreation = document.getElementById(
			"skipInstallBaseCreation").value == "true";
		if (skipInstallBaseCreation || salMigrationOnlyFlag) {
			return true;
		}
		var quantityConfirmation = false;
		for (var i = 0; i < materialEntryListSize; i++) {
			var materialCode = document.getElementById("materialCode" + i);
			var quantity = document.getElementById("materialCode" + i +
				"InitialQuantity");
			/*[AVAYA]: 09-13-2011 Add Serial Number label to retrieve data thru checkRequiredFields function (Start)*/
			/*[AVAYA]: 09-14-2011 Removed IntialQuantity reference from line 32 (Start)*/
			var serialNumber = document.getElementById("materialCode" + i +
				"SerialNumber");
			/*[AVAYA]: 09-13-2011 Add Serial Number label to retrieve data thru checkRequiredFields function (End)*/
			var select = document.getElementById("materialCode" + i + "Select");
			var materialCodeDescHidden = document.getElementById(materialCode.id +
				"DescHidden");
			/*[AVAYA]: 09-28-2011 Add logic in if statement to see if serial number is invalid and added validateSerialNumber()method (Start)*/
			validateSerialNumber(materialCode);
			validateQuantity(materialCode);
			validateSerialNumberTest(i);
			var serialValidateFlag = document.getElementById("materialCode" + i +
				"serialValidateFlag");
			if (select.checked && !materialCode.value) {
				convertAlertToModelPopUp($("#uncheckLabelForRegErrorCode").val(), $("#uncheckLabelForReg").val());
				close_popup_ib();
				return false;
			}
			
			if (select.checked && (!materialCode.value || !quantity.value || !isValidQuantity(quantity) || !materialCodeDescHidden.value )) {
				
				convertAlertToModelPopUp($("#notAllValforSelRegErrorCode").val(), $("#notAllValforSelReg").val());
				close_popup_ib();
				return false;
			} 
			
			if(select.checked &&(!isValidSerialNumber(serialNumber) ||serialValidateFlag.value == "Duplicate"))
			{
				convertAlertToModelPopUp("${grtConfig.selectAtlestOneSrNo}");
				close_popup_ib();
				return false;
			}
			
			
			/*[AVAYA]: 09-28-2011 Add logic in if statement to see if serial number is invalid (End)*/
			if (select.checked) {
				var qntyConfirmation = isQuantityMore(quantity.value);
				if (qntyConfirmation) {
					quantityConfirmation = true;
				}
			}
		}
		var proceed = true;
		if (quantityConfirmation) {
			proceed = confirm($("#errLineItemMaxLimCross").val());
			
			
			
		}
		if (!proceed) {
			close_popup_ib();
			return false;
		}

		//Begin Validate SOIR Serial Number and Quantity
		quantityConfirmation = false;
		var k = 0;
		while (document.getElementById('pendingOrders' + k + 'initialQuantity') !=null) {

			var idOfCheckBox = "pendingOrder" + k + "Select";
			var soirRecord = document.getElementById(idOfCheckBox);

			if (soirRecord.checked) {
				var idOfQuantiy = "pendingOrders" + k + "initialQuantity";
				var idOfSerialNum = "pendingOrders" + k + "SerialNumber";

				var quantity = document.getElementById(idOfQuantiy);
				var serialNum = document.getElementById(idOfSerialNum);
				if (!isValidQuantity(quantity)) {
					convertAlertToModelPopUp($("#validQuantityErrorCode").val(), $("#validQuantity").val());
					quantity.focus();
					close_popup_ib();
					return false;
				}
				if (!isValidSerialNumber(serialNum)) {
					convertAlertToModelPopUp($("#selectAtlestOneSrNoErrorCode").val(), $("#selectAtlestOneSrNo").val());
					serialNum.focus();
					close_popup_ib();
					return false;
				}

				if (serialNum.value != null || serialNumber.value != '') {
					if (quantity.value == null || quantity.value == '') {
						convertAlertToModelPopUp($("#quantityNotEmptyErrorCode").val(), $("#quantityNotEmpty").val());
						quantity.focus();
						close_popup_ib();
						return false;
					}
					if (quantity.value != null && serialNum.value != '' && quantity.value > 1) {
						convertAlertToModelPopUp($("#quantitySizeErrorCode").val(), $("#quantitySize").val());
						quantity.focus();
						close_popup_ib();
						return false;
					}
				}

				var qntyConfirmation = isQuantityMore(quantity.value);
				if (qntyConfirmation) {
					quantityConfirmation = true;
				}
			}

			k++;
		}

		proceed = true;
		if (quantityConfirmation) {
			proceed = confirm($("#errSOIRLineItemMaxLimCross").val());
		}
		if (!proceed) {
			close_popup_ib();
			return false;
		}

		return true;
	}

	function checkQuantityLimit(quantity) {

		var totalQuantity = -1;
		if (quantity != null) {
			totalQuantity = parseInt(quantity);
		}

		if (totalQuantity > 999) {
			var confirmYesCallBackFn = "return true;";
			var confirmNoCallBackFn = "return false;"
			convertConfirmToModelPopUp($("#errLineItemMaxLimCrossErrorCode").val(), $("#errLineItemMaxLimCross").val(), confirmYesCallBackFn, confirmNoCallBackFn);
		} else {
			return true;
		}
	}

	function checkMaterialCode(mcode) {
		var materialEntryListSize = '${materialEntryListSize}';
		for (var i = 0; i < materialEntryListSize; i++) {

			var select1 = document.getElementById("materialCode" + i + "Select");
		}

	}

	function vaidateQuantityOnLoad(index) {
		var idOfQuantiy = "pendingOrders" + index + "initialQuantity";
		var quantity = document.getElementById(idOfQuantiy);
		if (quantity.value != null && quantity.value != '') {
			vaidateSerialNumAndQuantity(index, 'QNT');
		}
	}

	function vaidateSerialNumOnLoad(index) {
		var idOfSerialNum = "pendingOrders" + index + "SerialNumber";
		var srNum = document.getElementById(idOfSerialNum);
		if (srNum!=undefined && srNum.value != null && srNum.value != '') {
			vaidateSerialNumAndQuantity(index, 'SN');
		}
	}

	function vaidateSerialNumAndQuantity(index, element) {
		//Begin Validate SOIR Serial Number and Quantity
		var k = index;
		if (document.getElementById('pendingOrders' + k + 'initialQuantity') != null) {

			var idOfCheckBox = "pendingOrder" + k + "Select";
			var soirRecord = document.getElementById(idOfCheckBox);

			var idOfQuantiy = "pendingOrders" + k + "initialQuantity";
			var idOfSerialNum = "pendingOrders" + k + "SerialNumber";

			var quantity = document.getElementById(idOfQuantiy);
			var serialNum = document.getElementById(idOfSerialNum);

			var error4 = document.getElementById("pendingOrders" + k + "Error4");
			var error7 = document.getElementById("pendingOrders" + k + "Error7");
			var error8 = document.getElementById("pendingOrders" + k + "Error8");
			var error9 = document.getElementById("pendingOrders" + k + "Error9");

			if (element == 'QNT') {
				if (!isValidQuantity(quantity)) {

					quantity.style.borderColor = "red";
					quantity.style.borderStyle = "solid";
					error8.style.display = "inline";
					quantity.focus();
					return false;
				} else {
					quantity.style.borderColor = "";
					quantity.style.borderStyle = "";
					error8.style.display = "none";
					quantity.focus();
				}
			}

			if (element == 'SN') {
				if (!isValidSerialNumber(serialNum)) {
					serialNum.style.borderColor = "red";
					serialNum.style.borderStyle = "solid";
					error7.style.display = "inline";
					serialNum.focus();
					return false;
				} else {
					serialNum.style.borderColor = "";
					serialNum.style.borderStyle = "";
					error7.style.display = "none";
				}
			}

			if (serialNum.value != null || serialNum.value != '') {
				if (quantity.value == null || quantity.value == '') {
					quantity.style.borderColor = "red";
					quantity.style.borderStyle = "solid";
					error9.style.display = "inline";
					quantity.focus();
					return false;
				} else {
					quantity.style.borderColor = "";
					quantity.style.borderStyle = "";
					error9.style.display = "none";
				}
				if (quantity.value != null && serialNum.value != '' && quantity.value > 1) {
					quantity.style.borderColor = "red";
					quantity.style.borderStyle = "solid";
					error4.style.display = "inline";
					quantity.focus();
					return false;
				} else {
					quantity.style.borderColor = "";
					quantity.style.borderStyle = "";
					error4.style.display = "none";
				}
			}

		}
		//End Validate SOIR Serial Number and Quantity
	}

	function validateQuantity(materialCode) {
		//GRT 4.0 Change : handle Undefined Case
		if( materialCode!=undefined ){
			//Check whether materialCode is already a DOM Element
			if( (materialCode instanceof HTMLElement) == false ){
				materialCode = document.getElementById(materialCode);
			}
			var select = document.getElementById(materialCode.id + "Select");
			var quantity = document.getElementById(materialCode.id + "InitialQuantity");
			var serialNumber = document.getElementById(materialCode.id + "SerialNumber");
			var materialCodeError2 = document.getElementById(materialCode.id + "Error2");
			var materialCodeError3 = document.getElementById(materialCode.id + "Error3");
			var serializedQty4 = document.getElementById(materialCode.id + "Error4");
			var serializedQty5 = document.getElementById(materialCode.id + "Error5");
			var mcSerialized = document.getElementById(materialCode.id + "Serialized");
			var mcServiceUsage = document.getElementById(materialCode.id + "ServiceUsage");
			var sameMaterialCodeQuantityError = document.getElementById(materialCode.id + "Error7");
			var serialMaterialQuantityError = document.getElementById(materialCode.id + "Error8");
			var serviceUnitMaterialQuantityError = document.getElementById(materialCode.id + "Error9");
			if(quantity.value != null && quantity.value != 'undefined'){
				quantity.value = quantity.value.trim();
				quantity.value = quantity.value.replace(/ /g,'');
			}
			//GRT 4.0 change : handle undefined 
			if (select!=undefined && quantity!=undefined && (!select.checked || !quantity.value)) {
				quantity.style.borderColor = "";
				quantity.style.borderStyle = "";
				materialCodeError2.style.display = "none";
				serializedQty5.style.display = "none";
				serializedQty4.style.display = "none";
				highlightRow(materialCode.id);
				return;
			}
			if (isValidQuantity(quantity)) {
				if( serializedQty4!=undefined )
					serializedQty4.style.display = "none";
				if( serializedQty5!=undefined )
					serializedQty5.style.display = "none";
				if( mcSerialized !=undefined && mcSerialized.value == "Y" && quantity.value == 1) {
					serialNumber.disabled = false;
					$("#" + materialCode.id + "SerialNumber").removeClass('input-disabled');
				} else {
					serialNumber.value = "";
					serialNumber.disabled = true;
					$("#" + materialCode.id + "SerialNumber").addClass('input-disabled');
				}
				if(materialCode!=undefined && quantity!=undefined && materialCode.value == quantity.value){
					quantity.style.borderColor = "red";
					quantity.style.borderStyle = "solid";
					sameMaterialCodeQuantityError.style.display = "inline";
					serialMaterialQuantityError.style.display = "none";
					serviceUnitMaterialQuantityError.style.display = "none";
					toggleSaveSubmit(true);
					highlightRow(materialCode.id);
					return;
				}else{
					quantity.style.borderColor = "";
					quantity.style.borderStyle = "";
					sameMaterialCodeQuantityError.style.display = "none";
					toggleSaveSubmit(true);
					highlightRow(materialCode.id);
				}
				if(mcSerialized!=undefined && mcSerialized.value == "Y" && quantity!=undefined && quantity.value > 9999){
					quantity.style.borderColor = "red";
					quantity.style.borderStyle = "solid";
					serialMaterialQuantityError.style.display = "inline";
					serviceUnitMaterialQuantityError.style.display = "none";
					toggleSaveSubmit(true);
					highlightRow(materialCode.id);
					return;
				}else{
					quantity.style.borderColor = "";
					quantity.style.borderStyle = "";
					serialMaterialQuantityError.style.display = "none";
					toggleSaveSubmit(true);
					highlightRow(materialCode.id);
				}
				if(mcServiceUsage!=undefined && mcServiceUsage.value == "Y" && quantity!=undefined && quantity.value > 499){
					quantity.style.borderColor = "red";
					quantity.style.borderStyle = "solid";
					serviceUnitMaterialQuantityError.style.display = "inline";
					toggleSaveSubmit(true);
					highlightRow(materialCode.id);
					return;
				}else{
					quantity.style.borderColor = "";
					quantity.style.borderStyle = "";
					serviceUnitMaterialQuantityError.style.display = "none";
					toggleSaveSubmit(true);
					highlightRow(materialCode.id);
				}
				if (quantity.value > 1 && isValidSerialNumberNotEmpty(serialNumber)) {
					serialNumber.style.borderColor = "red";
					serialNumber.style.borderStyle = "solid";
					serializedQty5.style.display = "inline";
					if (!isValidSerialNumberNotEmpty(serialNumber)) {
						serialNumber.style.borderColor = "red";
						serialNumber.style.borderStyle = "solid";
						materialCodeError3.style.display = "inline";
					}
					toggleSaveSubmit(true);
				} else {
					quantity.style.borderColor = "";
					quantity.style.borderStyle = "";
					if( materialCodeError2!=undefined )
						materialCodeError2.style.display = "none";
					if (isValidSerialNumberNotEmpty(serialNumber)) {
						serialNumber.style.borderColor = "";
						serialNumber.style.borderStyle = "";
						if( materialCodeError3!=undefined )
							materialCodeError3.style.display = "none";
					}
					toggleSaveSubmit(false);
					highlightRow(materialCode.id);
					return;
				}
			} else {
				quantity.style.borderColor = "red";
				quantity.style.borderStyle = "solid";
				materialCodeError2.style.display = "inline";
				toggleSaveSubmit(true);
			}
			
			//GRT 4.0 Change :
			highlightRow(materialCode.id);
		}		
	}
	
	function highlightRow(childId) {
		var $row = $("#"+childId).closest('tr');
		
		var visSpanLength = $row.find('td.errorTd span.errorSpan:visible').length;
		if( visSpanLength > 0 ){
			$row.addClass('highlight');
		}else{
			$row.removeClass('highlight');
		}
	}

	function validateSerialNumber(materialCode) {
		var select = document.getElementById(materialCode.id + "Select");
		var quantity = document.getElementById(materialCode.id + "InitialQuantity");
		var serialNumber = document.getElementById(materialCode.id + "SerialNumber");
		var materialCodeError3 = document.getElementById(materialCode.id + "Error3");
		var serializedQty4 = document.getElementById(materialCode.id + "Error4");
		var serializedQty5 = document.getElementById(materialCode.id + "Error5");
		var serialNumError6 = document.getElementById(materialCode.id + "Error6");
		if(serialNumber != null && serialNumber!=undefined && serialNumber.value != null && serialNumber.value != 'undefined'){
			serialNumber.value = serialNumber.value.trim();
			serialNumber.value = serialNumber.value.replace(/ /g,'');
		}
		
		if (select!=undefined && serialNumber!=undefined && (!select.checked || !serialNumber.value)) {
			serialNumber.style.borderColor = "";
			serialNumber.style.borderStyle = "";
			materialCodeError3.style.display = "none";
			serializedQty5.style.display = "none";
			serializedQty4.style.display = "none";

			serialNumError6.style.display = "none";
			toggleSaveSubmit(false);
			highlightRow(materialCode.id);
			return;
		}
		if (isValidSerialNumber(serialNumber)) {
			serializedQty4.style.display = "none";
			serializedQty5.style.display = "none";
			serialNumber.style.borderColor = "";
			serialNumber.style.borderStyle = "";
			materialCodeError3.style.display = "none";
			serialNumError6.style.display = "none";
			if (isValidQuantity(quantity) && quantity.value > 1 &&
				isValidSerialNumberNotEmpty(serialNumber)) {
				quantity.style.borderColor = "red";
				quantity.style.borderStyle = "solid";
				serializedQty4.style.display = "inline";
				toggleSaveSubmit(true);
			} else {
				serialNumber.style.borderColor = "";
				serialNumber.style.borderStyle = "";
				materialCodeError3.style.display = "none";
				serialNumError6.style.display = "none";
				toggleSaveSubmit(false);
				highlightRow(materialCode.id);
				return;
			}
		} else {
			if( serialNumber!=undefined ){//GRT 4.0 Change 
				serialNumber.style.borderColor = "red";
				serialNumber.style.borderStyle = "solid";
				materialCodeError3.style.display = "inline";
				toggleSaveSubmit(true);
			}
		}
		//GRT 4.0 Change :
		highlightRow(materialCode.id);
	}

	function validateSerialNumberOnLoad(materialCode) {
		validateSerialNumber(materialCode);
	}

	function validate_material_code(materialCode) {
		
		materialCode = $(materialCode);
		var materialCodeID = materialCode.attr('id');
		var select = $("#" + materialCodeID + "Select");
		if(materialCode.value != null && materialCode.value != 'undefined'){
			materialCode.value = materialCode.value.trim();
			materialCode.value = materialCode.value.replace(/ /g,'');
		}
		if (!materialCode.val()) {
			clearMaterialCodeErrors(materialCode, "true");
			return;
		} else if (!select.is(':checked')) {
			if (!select.is(':disabled')) {
				var materialCodeErrors = $(materialCode.id + "Errors");
				var materialExcludedError = $(materialCode.id + "MaterialExcludedError");
				materialCode.css('border-color', '');
				materialCode.css('border-style', '');
				materialCodeErrors.hide();
				materialExcludedError.hide();
				return;
			}
		}
		$(".loading-overlay").show();
		$.ajax({
			url: _path + '/installbase/json/validateMaterialCode.action?materialCode=' +
				materialCode.val(),
			dataType: 'json',
			success: function(mcArray1) {
				$(".loading-overlay").hide();
				for (var i = 0; i < mcArray1.length; i++) {
					var desc = mcArray1[0];
					var materialExcluded = mcArray1[1];
					var isTREligible = mcArray1[2];
					var exclusionSource = mcArray1[3];
					var serialized = mcArray1[4];
					var serviceUsage = mcArray1[5];
					
				}
				
				if(serviceUsage == "Y"){
					document.getElementById(materialCodeID + "ServiceUsage"). value = "Y";
				} else {
					document.getElementById(materialCodeID + "ServiceUsage"). value = "N";
				}
				//Defect#367. GRT4.0 - Enable serial number field if the material code is serialized and quantity = 1
				var initialQtyEl = document.getElementById(materialCodeID + "InitialQuantity");
				var serialNumEl = document.getElementById(materialCodeID + "SerialNumber");
				if(serialized == "Y"  ) {
					document.getElementById(materialCodeID + "Serialized"). value = "Y";					
					if(initialQtyEl != null && initialQtyEl.value == 1 ) {
						serialNumEl.disabled = false;
						$("#" + materialCodeID + "SerialNumber").removeClass('input-disabled');
					} else {
						serialNumEl.value = "";
						serialNumEl.disabled = true;
						$("#" + materialCodeID + "SerialNumber").addClass('input-disabled');
					}
				} else {
					document.getElementById(materialCodeID + "Serialized"). value = "N";
					serialNumEl.value = "";
					serialNumEl.disabled = true;
					$("#" + materialCodeID + "SerialNumber").addClass('input-disabled');
					
				}
				var materialExcludedErrorHidden = $("#" + materialCodeID +
					"MaterialExcludedErrorHidden");
				if (exclusionSource == 'PLDS') {
					exclusionSource = $("#infIBFedAutomatic").val();
				} else if(exclusionSource=="DEFECTIVE" || exclusionSource == "NMPC"){				
						exclusionSource=$("#defectiveCodeErrMsg").val();
				} else if(exclusionSource== "ESNA" || exclusionSource== 'ESNA'){
					exclusionSource = $("#errEsnaCode").val();
				}
				else {
					exclusionSource = $("#errOffrTCNotQual").val();
				}

				materialExcludedErrorHidden.html(exclusionSource);

				select.checked = true;
				select.disabled = false;
				show_validation_msg_desc(materialCode, desc, materialExcluded,
					isTREligible, "MCME", null);
				//GRT 4.0 Change : 
				highlightRow(materialCodeID);
			}
		});
	}

	function validateSerialNumberTest(mindex) {
		var materialCode = document.getElementById("materialCode" + mindex);
		var serialNumber = document.getElementById("materialCode" + mindex +
			"SerialNumber");
		var select = document.getElementById("materialCode" + mindex + "Select");
		var materialCodeError6 = document.getElementById("materialCode" + mindex +
			"Error6");
		var serialValidateFlag = document.getElementById("materialCode" + mindex +
			"serialValidateFlag");
		var orderId = document.getElementById("materialCode" + mindex + "orderId");
		if (serialNumber.value != null && serialNumber.value != "" && select.checked) {
			var materialEntryListSize = ${materialEntryListSize};
			var mcvalue = materialCode.value;
			var serialvalue = serialNumber.value;
			var mcserialvalue = mcvalue + serialvalue;
			for (var j = 0; j < materialEntryListSize; j++) {
				var selectvalue = document.getElementById("materialCode" + j +
					"SerialNumber").value;
				var selectmcvalue = document.getElementById("materialCode" + j).value;
				var selecttocompare = document.getElementById("materialCode" + j + "Select");
				var selectmcserialvalue = selectmcvalue + selectvalue;

				if (mindex != j && selecttocompare.checked) {
					if (selectmcserialvalue == mcserialvalue) {
						invalidSerialNumber(serialNumber, materialCodeError6);
						serialValidateFlag.value = "Duplicate";
						return false;
					} else {
						serialValidateFlag.value = "";
						validSerialNumber(serialNumber, materialCodeError6);

					}

				}

			}

		} else {
			serialValidateFlag.value = "";
			validSerialNumber(serialNumber, materialCodeError6);
		}

	}

	function isValidQuantity(quantity) {
		var regex = /^\d+$/;
		return regex.test(quantity.value) && quantity.value != 0;
	}

	function isValidSerialNumberNotEmpty(serialNumber) {
		if (serialNumber.value.length == 0) {
			return false;
		} else if (serialNumber.value.length <= 18) {
			var regex = /^[a-zA-Z0-9]+$/;
			return regex.test(serialNumber.value);
		} else {
			return false;
		}
	}

	function isValidQuantityForSerialNumber(quantity, serialNumber) {

		if (isValidSerialNumber(serialNumber) && isValidQuantity(quantity)) {
			return true;
		} else {
			return false;
		}
	}

	function isValidSerialNumber(serialNumber) {
		//GRT 4.0 Change : for undefined
		if( serialNumber ){
			var serialNum = serialNumber.value;
			serialNum = serialNum.replace(/^\s+|\s+$/g, '');
			serialNumber.value = serialNum;
			if (serialNum.length == 0) {
				return true;
			} else if (serialNum.length <= 18) {
				var regex = /^[a-zA-Z0-9]+$/;
				return regex.test(serialNum);
			} else {
				return false;
			}
		}
	}

	function isCoreUnit() {
		var isCoreUnit = document.getElementById('isCoreUnit').value;
		if (isCoreUnit != 'false') {
			return true;
		} else {
			convertAlertToModelPopUp($("#selectAtlestOneCoreUnitErrorCode").val(), $("#selectAtlestOneCoreUnit").val());
			return false;
		}
	}

	function isQuantityMore(quantity) {

		var totalQuantity = -1;
		if (quantity != null) {
			totalQuantity = parseInt(quantity);
		}

		if (totalQuantity > 999) {
			return true;
		} else {
			return false;
		}
	}

	function serialNumberValidation(materialCode, serialNumberFlag) {

		var serialNumber = document.getElementById(materialCode + "SerialNumber");
		var materialCodeError3 = document.getElementById(materialCode + "Error3");
		if (serialNumberFlag == null || serialNumberFlag == "") {
			validSerialNumber(serialNumber, materialCodeError3);
		} else {
			invalidSerialNumber(serialNumber, materialCodeError3)
		}
	}

	function invalidSerialNumber(serialNumber, materialCodeError3) {
		serialNumber.style.borderColor = "red";
		serialNumber.style.borderStyle = "solid";
		materialCodeError3.style.display = "inline";
		toggleSaveSubmit(true);
	}

	function validSerialNumber(serialNumber, materialCodeError3) {
		serialNumber.style.borderColor = "";
		serialNumber.style.borderStyle = "";
		materialCodeError3.style.display = "none";
		toggleSaveSubmit(false);
	}

	/*** end validation ***/

	/*** start select, unselect, clear ***/

	function selectCoreUnit(remoteReq) {
		if (remoteReq == 'No') {
			return true;
		}
		var temp = '0';
		var materialEntryForClear = document.getElementsByName("materialEntry");
		for (var i = 0; i < materialEntryForClear.length; i++) {
			if (materialEntryForClear[i].checked) {
				temp = '1';
				return true;
			}
		}
		if (temp == '0') {
			convertAlertToModelPopUp($("#selectAtlestOneCoreUnitErrorCode").val(), $("#selectAtlestOneCoreUnit").val());
			return false;
		}
	}

	function select_material_exclusion(pendingOrder, matertialExcluded) {
		var pendingselect = document.getElementById(pendingOrder + "Select");
		var pendingMaterialExcluError = document.getElementById(pendingOrder + "materialExcluError");
		var installBaseReadOnly = ${installBaseCreationReadOnlyFlag};
		if (pendingselect != null && pendingselect != "undefined" ) {
			if (matertialExcluded == "true") {
				pendingselect.checked = false;
				pendingselect.disabled = true;
				pendingMaterialExcluError.style.display = "inline";
			} else if (matertialExcluded == "false") {
				if (pendingselect.disabled) {
					pendingselect.checked = true;
				}
				pendingselect.disabled = false;
				pendingMaterialExcluError.style.display = "none";
			}
			if (installBaseReadOnly && pendingselect!=undefined) {
				pendingselect.disabled = true;
			}
		}
	}

	function selectforRegistration(select, materialCode) {
		select = document.getElementById($(select).attr('id'));
		var submitRegistration = document.getElementById("submitRegistration");
		if (select.checked && submitRegistration.disabled) {
			toggleSaveSubmit(false);
		}
		if (!select.checked && !submitRegistration.disabled) {
			if (!anyRecordForRegistration()) {
				toggleSaveSubmit(true);
			}
		}
		if (materialCode) {
			validate_material_code(materialCode);
			validateQuantity(materialCode);
		}
	}

	function sellectAllPendingOrders() {

		var pendingOrdersSize = ${pendingOrdersSize};

		for (var i = 0; i < pendingOrdersSize; i++) {
			var select2 = document.getElementById("pendingOrder" + i + "Select");
			if (!select2.disabled) {
				select2.checked = true;
			}

		}

	}

	function UnsellectAllPendingOrders() {
		var pendingOrdersSize = ${pendingOrdersSize};
		for (var i = 0; i < pendingOrdersSize; i++) {
			var select2 = document.getElementById("pendingOrder" + i + "Select");
			select2.checked = false;

		}

	}

	function sellectAllMeterial() {

		var materialEntryListSize = ${materialEntryListSize};
		var salMigrationOnlyFlag = ${salMigrationOnlyFlag}

		for (var i = 0; i < materialEntryListSize; i++) {

			var select1 = document.getElementById("materialCode" + i + "Select");
			var errors = document.getElementById("materialCode" + i + "Errors");
			var errors2 = document.getElementById("materialCode" + i + "Error2");
			var errors3 = document.getElementById("materialCode" + i + "Error3");
			if (!select1.disabled && errors.style.display == "none" && errors2.style.display ==
				"none" && errors3.style.display == "none") {

				select1.checked = true;
			}
		}



	}

	function sellectAllUnselectAllMeterial(option) {

		var materialEntryListSize = ${materialEntryListSize};
		var salMigrationOnlyFlag = ${salMigrationOnlyFlag}


		for (var i = 0; i < materialEntryListSize; i++) {

			var select1 = document.getElementById("materialCode" + i + "Select");

			if (!select1.disabled && option.checked) {
				select1.checked = true;
			} else {
				select1.checked = false;
			}

		}

	}

	function sellectAllUnselectAllPendingOrders(option) {
		var pendingOrdersSize = ${pendingOrdersSize};
		for (var i = 0; i < pendingOrdersSize; i++) {
			var select1 = document.getElementById("pendingOrder" + i + "Select");

			if (option.checked) {
				select1.checked = true;
			} else {
				select1.checked = false;
			}

		}

	}

	function UnsellectAllMeterial() {

		var materialEntryListSize = ${materialEntryListSize};
		for (var i = 0; i < materialEntryListSize; i++) {
			var select1 = document.getElementById("materialCode" + i + "Select");
			select1.checked = false;
		}

	}

	function selectAndUnselect(option) {
		var materialEntryListSize = ${materialEntryListSize};
		for (var i = 0; i < materialEntryListSize; i++) {
			var materialCode = document.getElementById("materialCode" + i);
			var select = document.getElementById("materialCode" + i + "Select");
			var quantity = document.getElementById("materialCode" + i +
				"InitialQuantity");
			var serialNumber = document.getElementById("materialCode" + i +
				"SerialNumber");
			var materialCodeError2 = document.getElementById("materialCode" + i +
				"Error2");
			var serializedQty5 = document.getElementById("materialCode" + i + "Error5");

			if (option.checked) {
				select.checked = true;
				$.ajax({
					url: _path +
						'/installbase/json/validateMaterialCode.action?materialCode=' +
						materialCode.value,
					dataType: 'json',
					success: function(mcArray1) {
						for (var i = 0; i < mcArray1.length; i++) {
							var desc = mcArray1[0];
							var materialExcluded = mcArray1[1];
							var isTREligible = mcArray1[2];
						}

						var materialCodeErrorsMaterialCode = document.getElementById(
							"materialCode" + i + "ErrorsMaterialCode");
						materialCodeErrorsMaterialCode.value = materialCode.value;

						var materialCodeDesc = document.getElementById("materialCode" + i +
							"Desc");

						materialCodeDesc.value = desc;


						var materialCodeDescHidden = document.getElementById("materialCode" +
							i + "DescHidden");
						materialCodeDescHidden.value = desc;

						var materialExclusionHidden = document.getElementById("materialCode" +
							i + "ExclusionHidden");
						materialExclusionHidden.value = matertialExcluded;


						if (matertialExcluded == "true") {

							select.checked = false;
							select.disabled = true;
						} else {

							select.checked = true;
							select.disabled = false;
						}
						if (!desc) {
							clearMaterialCodeErrors(materialCode, "false");
						}
					}

				});

				//Valiadate quantity

				if (isValidQuantity(quantity)) {
					if (isValidSerialNumber(serialNumber) && quantity.value > 1) {
						serialNumber.style.borderColor = "red";
						serialNumber.style.borderStyle = "solid";
						serializedQty5.style.display = "inline";
						toggleSaveSubmit(true);
					} else {
						quantity.style.borderColor = "";
						quantity.style.borderStyle = "";
						materialCodeError2.style.display = "none";
						toggleSaveSubmit(false);

					}
				} else {
					quantity.style.borderColor = "red";
					quantity.style.borderStyle = "solid";
					materialCodeError2.style.display = "inline";
					toggleSaveSubmit(true);
				}
			} else {

				select.checked = false;
				clearMaterialCodeErrors(materialCode, "true");
				quantity.style.borderColor = "";
				quantity.style.borderStyle = "";
				materialCodeError2.style.display = "none";

			}

		}

	}

	function selectMaterialCode() {
		var inputs = document.getElementsByName("materialEntry");
		var materialEntry = null;
		for (var i = 0; i < inputs.length; i++) {
			if (inputs[i].checked) {
				materialEntry = inputs[i].value;
			}
		}
	}

	function clearMaterialCodeErrors(materialCode, clear) {
		materialCode = $(materialCode);
		var materialCodeID = materialCode.attr('id');
		var materialCodeErrors = $("#" + materialCodeID + "Errors");
		var materialCodeDesc = $("#" + materialCodeID + "Desc");
		var isTREligible = $("#" + materialCodeID + "isTR");
		if (!materialCode.value)
			materialCodeDesc.val('');

		var materialCodeDescHidden = $("#" + materialCodeID + "DescHidden");
		materialCodeDescHidden.value = "";
		var materialExcludedError = $("#" + materialCodeID + "MaterialExcludedError");
		if (clear == "true") {
			
			materialCode.css('border-color', '');
			materialCode.css('border-style', '');
			materialCodeErrors.hide();
			materialExcludedError.hide();
			$("#" + materialCodeID + "Select").attr("disabled", false);
		} else if (clear == "false") {
			
			materialCode.css('border-color', 'red');
			materialCode.css('border-style', 'solid');
			materialCodeErrors.show();
			toggleSaveSubmit(true);
		}
		//GRT 4.0 Change : Highlight row
		highlightRow(materialCodeID);
	}

	/*** end select, unselect, clear ***/
	
	/***START: On Click Save Registartion ***/	
	function saveRegistrationFunc(materialEntryListSize, salMigrationOnlyFlag) {
		if (!checkRequiredFields(materialEntryListSize, salMigrationOnlyFlag)) {
			return;
		} else {
			//In case of any errors in selected records throw error
			if(isSelectedRecordsContainsErrors()) {
				convertAlertToModelPopUp($("#notAllValForRegErrorCode").val(), $("#notAllValForReg").val());					
			}else{
				saveRegistrationInstallBaseCreation();
			}			
		}
	}/***END: On Click Save Registartion ***/

	/*** start submit, save, cancel ***/
	
	function isSelectedRecordsContainsErrors() {
		var errorFlag = false;
		var trList = $("#materialEntryListTable tr");
		$.each(trList, function(index, row){
			var visSpanLength = $(row).find('td.errorTd span.errorSpan:visible').length;
			
			var select = $(row).find("input[id$='Select']").is(':checked');
			
			if( visSpanLength > 0 && select){
				errorFlag = true;
			}
		});
		return errorFlag;	
	}

	function submitRegistrationFunc(materialEntryListSize, salMigrationOnlyFlag,
		remoteReq) {
		if (!anyRecordForRegistration()) {
			
			convertAlertToModelPopUp($("#selAtlestOneMatCodeForSubmitErrorCode").val(), $("#selAtlestOneMatCodeForSubmit").val());
			return;
		} 
		
		window.scrollTo(0, 0);
		
		var remote = remoteReq;
		var ibSrCreated = document.getElementById("ibSrCreated").value == "true";
		var inProcess = document.getElementById("statusId").value == "1002";
		selectMaterialCode();

		if (inProcess && ibSrCreated) {
			grtcommonPopup.config({
				title: 'Warning',
				msg: 'Install Base already submitted'
			});
			grtcommonPopup.show();
			//GRT 4.0 Change :
			highlightAllRow();
			return;
		}


		if (!checkRequiredFields(materialEntryListSize, salMigrationOnlyFlag)) {
			//GRT 4.0 Change :
			highlightAllRow();
			return;
		} else {
			serialNumDB(materialEntryListSize, salMigrationOnlyFlag, remoteReq);
			// return;
		}

		

		
	}

	function submitRegistrationFuncForIPO(materialEntryListSize,
		salMigrationOnlyFlag, remoteReq) {
		if (!anyRecordForRegistration()) {
			/* convertAlertToModelPopUp(
				'Line items cannot be submitted without material codes. Please enter valid material codes or select a different option (Save, Cancel).'
			); */
			convertAlertToModelPopUp($("#selAtlestOneMatCodeForSubmitErrorCode").val(), $("#selAtlestOneMatCodeForSubmit").val());
			return;
		}
		if (selectCoreUnit(remoteReq) && isCoreUnit() && materialEntryVal()) {
			window.scrollTo(0, 0);
			var remote = remoteReq;
			var ibSrCreated = document.getElementById("ibSrCreated").value == "true";
			var inProcess = document.getElementById("statusId").value == "1002";
			selectMaterialCode();

			if (inProcess && ibSrCreated) {
				grtcommonPopup.config({
					title: 'Warning',
					msg: 'Install Base already submitted'
				});
				grtcommonPopup.show();
				//GRT 4.0 Change :
				highlightAllRow();			
				return;
			}


			if (!checkRequiredFields(materialEntryListSize, salMigrationOnlyFlag)) {
				//GRT 4.0 Change :
				highlightAllRow();
				return;
			} else {
				serialNumDB(materialEntryListSize, salMigrationOnlyFlag, remoteReq);
				// return;
			}
		} else {
			return false;
		}
	}

	function installBaseformSubmit() {
		var myform = document.getElementById("myform");
		//Newly Added Code for Submit as per new Architecture
		myform.action = document.getElementById('finalIBSubmitAction').href;
		myform.submit();
		closeWarningPopup();
		show_popup("Submission for Install Base Creation is in progress",
			"Please Wait...");
	}

	function submitInstallBaseCreation() {
		$("#warningPopup").hide();
		$("#submit-overlay").show();
		//Defect#679 add string "Remove" in error desc of unselected material codes
		var trList = $("#materialEntryListTable tr");
		$.each(trList, function(index, row){
			var selectId = $(row).find("input[id$='Select']").attr("id");
			if(selectId != null ) {
				var checkbox = document.getElementById(selectId);
				if(checkbox != null && !checkbox.checked) {					
					$(row).find("input[id$='errorDesc']").val("Remove");
				} 
			}
		});
		
		var url = document.getElementById('validateMaterialCodesAgainstSoldTo').href;
		document.getElementById('myform').action = url;
		document.getElementById('myform').submit();
	}

	function saveRegistrationInstallBaseCreation() {
		$("div.loading-overlay").show();		
		var url = document.getElementById('saveRegistrationHref').href;
		document.getElementById('myform').action = url;
		document.getElementById('myform').submit();
	}

	function cancelConfirm() {
		var confirmYesCallBackFn = "cancelConfirmYes()";
		var confirmNoCallBackFn = "return false;"
		convertConfirmToModelPopUp($("#cnfrmBckToRegListConfCode").val(), $("#cnfrmBckToRegList").val(), confirmYesCallBackFn, confirmNoCallBackFn);
	}
	
	function cancelConfirmYes() {
		var url = document.getElementById('cancelInstallStatus').href;
		document.getElementById('myform').action = url;
		document.getElementById('myform').submit();
	}

	function cancelConfirmation() {
		
		var confirmYesCallBackFn = "cancelConfirmationYes()";
		var confirNoCallBackFn = "return false;"
		convertConfirmToModelPopUp($("#cnfrmBckToGrtConfCode").val(), $("#cnfrmBckToGrt").val(), confirmYesCallBackFn, confirNoCallBackFn);
	}
	
	function cancelConfirmationYes() {
		window.location = _path + "/home-action.action";
		return true;
	}

	//Back from IB
	function backFromIB( actionName ){
		if( actionName == "registrationList" ){		
			actionName = "registrationListAction";
		}
			var url = $('#'+actionName).attr('href');
			document.getElementById('myform').action = url;
			document.getElementById('myform').submit();
	}
	
	/*** end submit, save, cancel ***/

</script>
<!-- end install base creation js lib	-->

<!-- start pagination dependencies -->
<script type="text/javascript">
	//Implement Pagination
	$(document).ready(function() {
		$(".loading-overlay").hide();
		//Sales out inventory
		$('#pendingOrdersTable').dataTable({
				"sPaginationType": "full_numbers",
			"scrollX":true,
				"autoWidth": false
			})
			.columnFilter();
		
	});
</script>
<!-- end pagination dependencies -->

<jsp:include flush="true" page="grtcommon.jsp" />

<!-- start page content-wrap -->
<div class="content-wrap install-base-creation">

	<h1 class="page-title">Install Base Creation</h1>

	<!-- start page content -->
	<div class="content">

		<!-- start registration summary -->
		<%@ include file="grtBanner.jsp"%>
		<!-- end registration summary -->
<a id="downloadfile" href="<c:url context="${ _path }" value="/equipmentmove/downloadfile" />"></a>
		<!-- start existing install base -->
		<div id="eib-wrap" class="data-table-wrap collapse-box">
			<h2 class="collapse-box-header" onclick="toggle();">Existing Install Base
				<a href="#">
					<span></span>
				</a>
			</h2>
			<%
				String exiPagiButtonClicked = (String) request
							.getSession()
							.getAttribute(
									com.grt.util.GRTConstants.EIB_PAGINATION_BUTTON_CLICKED);
					if (null != exiPagiButtonClicked
							&& exiPagiButtonClicked.equals("YES")) {
			%>
			<div id="toggleText" class="data collapse-box-container">
			<% } else { %>
			<div id="toggleText" class="data collapse-box-container" style="display: none">
			<% } %>
				<!-- start dataTables -->
				<table border="0" width="100%" id="existingInstallBaseTable">
					<thead>
						<tr valign="top">
							<th width="16%">Qty&nbsp;<span id="initialQuantityIBEIBSort"></span>
							</th>
							<th width="16%">Material Code&nbsp;<span id="materialCodeIBEIBSort"></span>
							</th>
							<th width="32%">Material Code Description&nbsp;<span id="descriptionIBEIBSort"></span>
							</th>
							<th width="16%">Product Line&nbsp;<span id="productLineIBEIBSort"></span>
							</th>
							<th width="20%">TOB Eligible?&nbsp;<span id="technicallyRegisterableIBEIBSort"></span>
							</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${ actionForm.existingInstallBaseList }"
							var="existingInstallObj">
							<tr align="center">
								<td width="16%">${existingInstallObj.initialQuantity}</td>
								<td width="16%">${existingInstallObj.materialCode}</td>
								<td width="32%">${existingInstallObj.description}</td>
								<td width="16%">${existingInstallObj.productLine}</td>
								<td width="20%">${existingInstallObj.technicallyRegisterable}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<!-- end dataTables -->
			</div>
		</div>
		<!-- end existing install base -->

		<!-- start start sales out inventory report -->
		<div id="soir-wrap" class="data-table-wrap collapse-box">
			<h2 class="collapse-box-header">Sales Out Inventory Report
				<a href="#">
					<span></span>
				</a>
			</h2>
			<%
				String soirPagiButtonClicked = (String) request
							.getSession()
							.getAttribute(
									com.grt.util.GRTConstants.SOIR_PAGINATION_BUTTON_CLICKED);
					if (null != soirPagiButtonClicked
							&& soirPagiButtonClicked.equals("YES")) {
			%>
			<div id="soirData" class="data collapse-box-container">
			<% } else { %>
			<div id="soirData" class="data collapse-box-container" style="display: none">
			<% } %>
				<!-- start dataTables -->
				<table border="0" width="100%" id="pendingOrdersTable">
					<thead>
						<tr valign="top">
							<th width="9%">Select 
								<input type="checkbox"
								id="selectAndUselectAllPending"
								value="${ actionForm.selectAndUnselectAllPending }"
								${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}
								${(actionForm.selectAndUnselectAllPending) ? 'checked' : ''}
								onClick="sellectAllUnselectAllPendingOrders(this)" />&nbsp;<span
								id="selectforRegistrationIBSOIRSort"></span>
							</th>
							<th width="12%">Qty&nbsp;<span
								id="initialQuantityIBSOIRSort"></span>
							</th>
							<th width="15%">Serial Number&nbsp;<span id="serialNumberIBSOIRSort"></span>
							</th>
							<th width="11%">Material Code&nbsp;<span id="materialCodeIBSOIRSort"></span>
							</th>
							<th width="18%">Material Code Description&nbsp;<span id="descriptionIBSOIRSort"></span>
							</th>
							<th width="14%">Product Line&nbsp;<span id="productLineIBSOIRSort"></span>
							</th>
							<th width="9%">TOB Eligible?&nbsp;<span id="technicallyRegisterableIBSOIRSort"></span>
							</th>
							<th width="9%">Warning&nbsp;<span id="errorDescriptionIBSOIRSort"></span>
							</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${actionForm.pendingOrders}" var="item" varStatus="container">

						<c:set var="pendingMaterialExc" value="${item.materialExclusion}"></c:set>
						<c:set var="recQuantity" value="${item.initialQuantity}"></c:set>
						<c:set var="recSerialNum" value="${item.serialNumber}"></c:set>

						<tr align="center">

							<input type="hidden" id="pendingOrders${container.index}orderId" value="${ item.orderId }" />
							<input type="hidden" id="pendingOrders${container.index}hiddenMc" value="${ item.materialCode }" />

							<td width="9%" align="center">
								<input name="actionForm.pendingOrders[${container.index}].selectforRegistration"
								type="checkbox"
								id="pendingOrder${container.index}Select" value="true"
								onChange="selectforRegistration(this)"
								onKeyPress="return noEnter(event);" />
							</td>
							<td width="14%" align="center">
								<input type="text"
								name="actionForm.pendingOrders[${container.index}].initialQuantity"
								id="pendingOrders${container.index}initialQuantity"
								value="${item.initialQuantity}" size="14" maxlength="13"
								${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}
								onChange="vaidateSerialNumAndQuantity( ${container.index} , 'QNT' );"
								onKeyPress="return noEnter(event);" />
							</td>
							<td width="15%" align="center">
								<input type="text"
								name="actionForm.pendingOrders[${container.index}].serialNumber"
								id="pendingOrders${container.index}SerialNumber"
								value="${item.serialNumber}" size="25" maxlength="18"
								${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}
								onChange="vaidateSerialNumAndQuantity( ${container.index} , 'SN' );"
								onKeyPress="return noEnter(event);" />
							</td>
							<td width="12%" align="center">${item.materialCode}</td>
							<td width="18%" align="center">${item.description}</td>
							<td width="14%" align="center">${item.productLine}</td>
							<td width="9%" align="center">${item.technicallyRegisterable}</td>
							<td width="9%">
								<span id="pendingOrder${container.index}errorDescription">
									${item.errorDescription}
								</span>
								<span id="pendingOrder${container.index}materialExcluError" style="display: none;">
									${grtConfig.materialCodeExcluded}
								</span>
								<span id="pendingOrders${container.index}Error4" style="display: none; color: red">
									${grtConfig.quantitySize}
								</span>
								<span id="pendingOrders${container.index}Error6" style="display: none; color: red">
									${grtConfig.invalidSerialNumber}
								</span>
								<span id="pendingOrders${container.index}Error7" style="display: none; color: red">
									${grtConfig.invalidSerialNumber18Char}
								</span>
								<span id="pendingOrders${container.index}Error8" style="display: none; color: red">
									${grtConfig.validQuantity}
								</span>
								<span id="pendingOrders${container.index}Error9" style="display: none; color: red">
									${grtConfig.quantityNotEmpty}
								</span>
							</td>
						</tr>
						<script type="text/javascript">
							select_material_exclusion("pendingOrder${container.index}", "${item.materialExclusion}");
						</script>
						<%
							Long recQnty = (Long) pageContext
											.getAttribute("recQuantity");
									String strQuant = recQnty + "";
									String recSn = (String) pageContext
											.getAttribute("recSerialNum");
									if (recQnty != null && strQuant.trim().length() > 0) {
						%>
						<script type="text/javascript">
							vaidateQuantityOnLoad( ${container.index}	);
						</script>
						<%
							}
									if (recSn != null) {
						%>
						<script type="text/javascript">
							vaidateSerialNumOnLoad( ${container.index} );
						</script>
						<%
							}
						%>
						</c:forEach>
					</tbody>
				</table>
				<!-- end dataTables -->
			</div>
		</div>
		<!-- end start sales out inventory report -->

		<!-- start manual entry table-wrap -->
		<div id="mec-wrap" class="data-table-wrap collapse-box">
			<h2 class="collapse-box-header active">Manual Entry Code
				<a href="#">
					<span></span>
				</a>
			</h2>
			<div id="" class="data collapse-box-container">
				<!-- start manual entry form -->
				<s:form id="myform" enctype="multipart/form-data" method="post">
				
				
					<!-- start error messages -->
		<input type="hidden" id="alertSomethingWrng" value="${grtConfig.alertSomethingWrng}" />
		<input type="hidden" id="installBaseListSizeMsg" value="${grtConfig.installBaseListSizeMsg}" />
		<input type="hidden" id="incorrectFormat" value="${grtConfig.incorrectFormat}" />
		<input type="hidden" id="selMatEntry" value="${grtConfig.selMatEntry}" />
		<input type="hidden" id="selectAtlestOneQuality" value="${grtConfig.selectAtlestOneQuality}" />
		<input type="hidden" id="notAllValForReg" value="${grtConfig.notAllValForReg}" />
		<input type="hidden" id="selectAtlestOneMatCode" value="${grtConfig.selectAtlestOneMatCode}" />
		<input type="hidden" id="selCorrectOptions" value="${grtConfig.selCorrectOptions}" />
		<input type="hidden" id="enterValidMatCode" value="${grtConfig.enterValidMatCode}" />
		<input type="hidden" id="uncheckLabelForReg" value="${grtConfig.uncheckLabelForReg}" />
		<input type="hidden" id="notAllValforSelReg" value="${grtConfig.notAllValforSelReg}" />
		<input type="hidden" id="validQuantity" value="${grtConfig.validQuantity}" />
		<input type="hidden" id="selectAtlestOneSrNo" value="${grtConfig.selectAtlestOneSrNo}" />
		<input type="hidden" id="quantityNotEmpty" value="${grtConfig.quantityNotEmpty}" />
		<input type="hidden" id="quantitySize" value="${grtConfig.quantitySize}" />
		<input type="hidden" id="selectAtlestOneCoreUnit" value="${grtConfig.selectAtlestOneCoreUnit}" />
		<input type="hidden" id="selAtlestOneMatCodeForSubmit" value="${grtConfig.selAtlestOneMatCodeForSubmit}" />


		<input type="hidden" id="errorSystemAlert" value="${grtConfig.errorSystemAlert}"/>
		<input type="hidden" id="infInfMCodeInProcess" value="${grtConfig.infInfMCodeInProcess}"/>
		<input type="hidden" id="infIBFedAutomatic" value="${grtConfig.infIBFedAutomatic}"/>
		<input type="hidden" id="errOffrTCNotQual" value="${grtConfig.errOffrTCNotQual}"/>
			<input type="hidden" id="defectiveCodeErrMsg" value="${grtConfig.defectiveCodeErrMsg}"/>
		<input type="hidden" id="infIncOrgTrancNo" value="${grtConfig.infIncOrgTrancNo}"/>
		<input type="hidden" id="infSendWrtnReq" value="${grtConfig.infSendWrtnReq}"/>
		<input type="hidden" id="errRdyToSalMig" value="${grtConfig.errRdyToSalMig}"/>
		<input type="hidden" id="infCnfrmRegOne" value="${grtConfig.infCnfrmRegOne}"/>
		<input type="hidden" id="infCnfrmRegTwo" value="${grtConfig.infCnfrmRegTwo}"/>
		<input type="hidden" id="infCnfrmRegThree" value="${grtConfig.infCnfrmRegThree}"/>
		<input type="hidden" id="errLineItemMaxLimCross" value="${grtConfig.errLineItemMaxLimCross}"/>
		<input type="hidden" id="errSOIRLineItemMaxLimCross" value="${grtConfig.errSOIRLineItemMaxLimCross}"/>
		<input type="hidden" id="cnfrmBckToGrt" value="${grtConfig.cnfrmBckToGrt}"/>
		<input type="hidden" id="cnfrmBckToRegList" value="${grtConfig.cnfrmBckToRegList}"/>
		<input type="hidden" id="errEsnaCode" value="${grtConfig.errEsnaCode}"/>
		<!-- end error messages -->


		<!-- Error Codes -->
		<input type="hidden" id="alertSomethingWrngErrorCode" value="${grtConfig.ewiMessageCodeMap['alertSomethingWrng']}" />
		<input type="hidden" id="installBaseListSizeMsgWarningCode" value="${grtConfig.ewiMessageCodeMap['installBaseListSizeMsg']}" />
		<input type="hidden" id="incorrectFormatErrorCode" value="${grtConfig.ewiMessageCodeMap['incorrectFormat']}" />
		<input type="hidden" id="selMatEntryWarningCode" value="${grtConfig.ewiMessageCodeMap['selMatEntry']}" />
		<input type="hidden" id="selectAtlestOneQualityErrorCode" value="${grtConfig.ewiMessageCodeMap['selectAtlestOneQuality']}" />
		<input type="hidden" id="notAllValForRegErrorCode" value="${grtConfig.ewiMessageCodeMap['notAllValForReg']}" />
		<input type="hidden" id="selectAtlestOneMatCodeErrorCode" value="${grtConfig.ewiMessageCodeMap['selectAtlestOneMatCode']}" />
		<input type="hidden" id="selCorrectOptionsErrorCode" value="${grtConfig.ewiMessageCodeMap['selCorrectOptions']}" />
		<input type="hidden" id="enterValidMatCodeErrorCode" value="${grtConfig.ewiMessageCodeMap['enterValidMatCode']}" />
		<input type="hidden" id="uncheckLabelForRegErrorCode" value="${grtConfig.ewiMessageCodeMap['uncheckLabelForReg']}" />
		<input type="hidden" id="notAllValforSelRegErrorCode" value="${grtConfig.ewiMessageCodeMap['notAllValforSelReg']}" />
		<input type="hidden" id="validQuantityErrorCode" value="${grtConfig.ewiMessageCodeMap['validQuantity']}" />
		<input type="hidden" id="selectAtlestOneSrNoErrorCode" value="${grtConfig.ewiMessageCodeMap['selectAtlestOneSrNo']}" />
		<input type="hidden" id="quantityNotEmptyErrorCode" value="${grtConfig.ewiMessageCodeMap['quantityNotEmpty']}" />
		<input type="hidden" id="quantitySizeErrorCode" value="${grtConfig.ewiMessageCodeMap['quantitySize']}" />
		<input type="hidden" id="selectAtlestOneCoreUnitErrorCode" value="${grtConfig.ewiMessageCodeMap['selectAtlestOneCoreUnit']}" />
		<input type="hidden" id="selAtlestOneMatCodeForSubmitErrorCode" value="${grtConfig.ewiMessageCodeMap['selAtlestOneMatCodeForSubmit']}" />


		<input type="hidden" id="errorSystemAlertErrorCode" value="${grtConfig.ewiMessageCodeMap['errorSystemAlert']}"/>
		<input type="hidden" id="infInfMCodeInProcessInfoCode" value="${grtConfig.ewiMessageCodeMap['infInfMCodeInProcess']}"/>
		<input type="hidden" id="infIBFedAutomaticInfoCode" value="${grtConfig.ewiMessageCodeMap['infIBFedAutomatic']}"/>
		<input type="hidden" id="errOffrTCNotQualErrorCode" value="${grtConfig.ewiMessageCodeMap['errOffrTCNotQual']}"/>
		<input type="hidden" id="infIncOrgTrancNoInfoCode" value="${grtConfig.ewiMessageCodeMap['infIncOrgTrancNo']}"/>
		<input type="hidden" id="infSendWrtnReqInfoCode" value="${grtConfig.ewiMessageCodeMap['infSendWrtnReq']}"/>
		<input type="hidden" id="errRdyToSalMigErrorCode" value="${grtConfig.ewiMessageCodeMap['errRdyToSalMig']}"/>
		<input type="hidden" id="infCnfrmRegOneInfoCode" value="${grtConfig.ewiMessageCodeMap['infCnfrmRegOne']}"/>
		<input type="hidden" id="infCnfrmRegTwoInfoCode" value="${grtConfig.ewiMessageCodeMap['infCnfrmRegTwo']}"/>
		<input type="hidden" id="infCnfrmRegThreeInfoCode" value="${grtConfig.ewiMessageCodeMap['infCnfrmRegThree']}"/>
		<input type="hidden" id="errLineItemMaxLimCrossErrorCode" value="${grtConfig.ewiMessageCodeMap['errLineItemMaxLimCross']}"/>
		<input type="hidden" id="errSOIRLineItemMaxLimCrossErrorCode" value="${grtConfig.ewiMessageCodeMap['errSOIRLineItemMaxLimCross']}"/>
		<input type="hidden" id="cnfrmBckToGrtConfCode" value="${grtConfig.ewiMessageCodeMap['cnfrmBckToGrt']}"/>
		<input type="hidden" id="cnfrmBckToRegListConfCode" value="${grtConfig.ewiMessageCodeMap['cnfrmBckToRegList']}"/>
		<input type="hidden" id="errEsnaCodeInfoCode" value="${grtConfig.ewiMessageCodeMap['errEsnaCode']}"/>
		
		
		
					
					<c:set var="ipoPopup" value="${ ipoContinueInstallBase }" />
					<a id="getMaterialVersionAction" 
						href="<c:url context="${ _path }" value="/ipoffice/getMaterialVersion.action" />" ></a>
					<a id="registrationListAction"
						href="<c:url context="${ _path }" value="/technicalregsitration/registrationList.action" />"></a>
					<a id="installBaseCreationAction"
						href="<c:url context="${ _path }" value="/installbase/installbasehome.action" />"></a>
					<a id="validateMaterialCodesAgainstSoldTo"
						href="<c:url context="${ _path }" value="/installbase/validateMaterialCodesAgainstSoldTo" />"></a>
					<a id="saveRegistrationHref"
						href="<c:url context="${ _path }" value="/installbase/saveRegistration" />"></a>
					<a id="ipoReg" 
						href="<c:url context="${ _path }" value="/ipoffice/ipoReg.action" />" ></a>
					<a id="newRegistration"
								href="<c:url context="${ _path }" value="/technicalregsitration/newRegistration.action"/>"></a>
					<a id="cancelInstallStatus"
						href="<c:url context="${ _path }" value="/installbase/cancelInstallStatus.action" />"></a>					
								
					<input type="hidden" name="actionForm.userRole"
						value="${ actionForm.userRole }" id="userRole" />
					<input type="hidden" name="actionForm.isCoreUnit" id="isCoreUnit"
						value="${ fn:escapeXml(actionForm.isCoreUnit) }" />
					<input type="hidden" name="actionForm.count"
						value="${ fn:escapeXml(actionForm.count) }" id="countId" />

					<c:forEach items="${ materialCodeList }" var="item">
						<input type="hidden" name="materialCodeList.MCode"
							id="materialCodeList${container.index}MCode" value="" />
						<input type="hidden" name="materialCodeList.RegId"
							id="materialCodeList${container.index}RegId" value="" />
					</c:forEach>

					<input type="hidden" name="actionForm.salMigrationOnly"
						value="${ actionForm.salMigrationOnly }" />
					<input type="hidden" name="materialExclusionFlag" id="materialExc"
						value="${ fn:escapeXml(materialExclusionFlag) }" />

					<c:set var="readOnly" value="${installBaseCreationReadOnlyFlag}" />
					<c:set var="materialExclusion" value="${materialExclusionFlag}" />
					<c:set var="processStepId" value="${actionForm.processStepId}" />
					<c:set var="implementation" value="${actionForm.typeOfImp}" />
					<c:set var="ibaseStatus" value="${actionForm.ibaseStatus}" />
					<c:set var="remoteconn" value="${actionForm.remoteConnectivity}" />
					<c:set var="ipoInventoryNotLoaded" value="${actionForm.ipoInventoryNotLoaded}" />
					<c:set var="form" value="${actionForm.sessionAttr}" />
					<%
						boolean readOnly = ((Boolean) pageContext
									.getAttribute("readOnly"));
							System.out.println("installBaseCreationReadOnlyFlag:"
									+ readOnly);
							if (readOnly) {
								if ("P0003".equals(pageContext
										.getAttribute("processStepId"))
										|| "P0002".equals(pageContext
												.getAttribute("processStepId"))) {
					%>
					<input type="hidden" value="${ fn:escapeXml(actionForm.soldToId) }" id="soldToHidden" />
					<input type="hidden" value="actionForm.registrationId" />
					<input type="hidden" value="actionForm.cuttimendate" />
					<input type="hidden" value="actionForm.processStepId" />
					<input type="hidden" value="actionForm.technicalOrderId" />

					<table>
						<tr>
							<td><a action="productSummary"
								value="Technical<br/> Registration" formSubmit="true" />
							</td>
							<%
								if ("P0003".equals(pageContext
													.getAttribute("processStepId"))) {
							%>
							<td><b>></b></td>
							<td><a action="equipmentRemovalProcess"
								value="Final<br/> Record<br/> Validation" formSubmit="true" />
							</td>
							<% } %>
						</tr>
					</table>

					<% } } %>
					<%-- Keep the New Registration Data in Request Scope --%>
					<%
						if (request.getAttribute("registrationForm") != null) {
								request.setAttribute("registrationForm",
										request.getAttribute("registrationForm"));
							}
					%>

					<!-- start upload dependencies -->
					<c:set var="uploadMessage" value="${uploadMessage}" />
					<%
						if (("display"
									.equals(pageContext.getAttribute("uploadMessage")))) {
					%>
					<script type="text/javascript">
						window.onload = function() {
							convertAlertToModelPopUpServerSideMsg("${actionForm.uploadMessage}");
						}
					</script>
					<% } %>
					<!-- end upload dependencies -->

					<input id="manuallyAddMaterial" type="hidden" value="${manuallyAddMaterial}" />
					<s:token></s:token>

					<table border="0" width="100%" id="materialEntryListTable" class="basic-table">
						<thead>
							<th width="20%">Material Code*</th>
							<th width="20%">Qty*</th>
							<%--	[AVAYA]: 08-09-2011 Add Serial Number label to Service Template (Start) --%>
							<th width="20%">Serial Number</th>
							<%--	[AVAYA]: 08-09-2011 Add Serial Number label to Service Template (End) --%>
							<th width="20%">Material Code Description</th>
							<th width="20%" align="center">TOB Eligible?</th>
							<%
								if ("No".equalsIgnoreCase((String) pageContext
											.getAttribute("ipoInventoryNotLoaded"))) {
							%>
							<th width="">Sourced from Inventory xml</th>
							<%
								}
									int varIndex = 0;
							%>
							<th width="25%">
								Select
								<input type="checkbox" id="selectAndUselectAll"
								value="${actionForm.selectAndUnselectAllMeterial }"
								${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}
								${(actionForm.selectAndUnselectAllMeterial) ? 'checked' : ''}
								onClick="sellectAllUnselectAllMeterial(this)" />
							</th>
							<th width="25%">Warnings</th>
							<%
								if (("Yes".equalsIgnoreCase((String) pageContext
											.getAttribute("ipoInventoryNotLoaded")))
											&& ("Yes".equalsIgnoreCase((String) pageContext
													.getAttribute("remoteconn")))) {
							%>
							<th width="">Select Core Unit</th>
							<th width="">Version</th>
							<%
								}
							%>
						</thead>
						<tbody>
							<c:forEach items="${ actionForm.materialEntryList }" var="item" varStatus="container">
							<tr>
								<input type="hidden" name="actionForm.materialEntryList[${container.index}].errorDescription"  id="materialCode${container.index}errorDesc"/>
								<input type="hidden"
									name="actionForm.materialEntryList[${container.index}].serialized"
									id="materialCode${container.index}Serialized"
									value="${ item.serialized }" />
								<input type="hidden"
									name="actionForm.materialEntryList[${container.index}].serviceUsage"
									id="materialCode${container.index}ServiceUsage"
									value="${ item.serviceUsage }" />
									
								<c:set var="baseUnit" value="${item.isBaseUnit}" />
								<c:set var="materialExc" value="${item.materialExclusion}" />
								<c:set var="mcExclusionSource" value="${item.exclusionSource}" />
								<c:set var="materialCode" value="${item.materialCode}" />
								<%
											String currentItem = (String) pageContext
													.getAttribute("baseUnit");
											if (currentItem != null
													&& currentItem.equalsIgnoreCase("y")) {
								%>
								<td align="center" class="highlightCore">
									<input type="hidden"
									name="actionForm.materialEntryList[${container.index}].orderId"
									id="materialCode${container.index}orderId"
									value="${ item.orderId }" /> <input type="text"
									name="actionForm.materialEntryList[${container.index}].materialCode"
									id="materialCode${container.index}"
									value="${ item.materialCode }" class="materialCodeInput"
									onChange="validate_material_code(this)"
									${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}
									maxlength="18" onKeyPress="return noEnter(event);" color/>									
								</td>
								<td align="center" class="highlightCore">
									<input type="text"
									name="actionForm.materialEntryList[${container.index}].initialQuantity"
									id="materialCode${container.index}InitialQuantity"
									value="${ item.initialQuantity }" size="14"
									${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}
									onChange="validateQuantity(materialCode${container.index})"
									maxlength="13" onKeyPress="return noEnter(event);" />
								</td>
								<td align="center" class="highlightCore">
									<input type="text"
									name="actionForm.materialEntryList[${container.index}].serialNumber"
									id="materialCode${container.index}SerialNumber"
									value="${ item.serialNumber }"
									${(installBaseCreationReadOnlyFlag || item.serialized == 'N') || (item.serialized == 'Y' && item.initialQuantity != 1) ? 'disabled' : ''}
									onChange="validateSerialNumber(materialCode${container.index});"
									onKeyPress="return noEnter(event);" />
									
								</td>
								<td align="center" class="highlightCore">
									<span id="materialCode${container.index}Desc">
										<label>${item.description}"</label>
									</span>
									<input type="hidden"
									name="actionForm.materialEntryList[${container.index}].description"
									id="materialCode${container.index}DescHidden"
									value="${ item.description }" /> <input type="hidden"
									name="actionForm.materialEntryList[${container.index}].materialExclusion"
									id="materialCode${container.index}ExclusionHidden"
									value="${ item.materialExclusion }" /> <input type="hidden"
									name="actionForm.materialEntryList[${container.index}].isBaseUnit"
									id="materialCode${container.index}baseunit"
									value="${ item.isBaseUnit }" /> <input type="hidden"
									name="actionForm.materialEntryList[${container.index}].isSourceIPO"
									id="materialCode${container.index}sourceipo"
									value="${ item.isSourceIPO }" /> <input type="hidden"
									name="actionForm.materialEntryList[${container.index}].serialValidateFlag"
									id="materialCode${container.index}serialValidateFlag"
									value="${ item.serialValidateFlag }" />
								</td>
								<td align="center" class="highlightCore">
									<span id="materialCode${container.index}isTR">
										<label>${item.technicallyRegisterable}</label>
									</span>
									<input type="hidden"
									name="actionForm.materialEntryList[${container.index}].technicallyRegisterable"
									id="materialCode${container.index}isTRHidden"
									value="${ item.technicallyRegisterable }"
									${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} />
								</td>
								<%
									if ("No".equalsIgnoreCase((String) pageContext
														.getAttribute("ipoInventoryNotLoaded"))) {
								%>
								<td align="center" class="highlightCore">
									<input type="checkbox"
									name="actionForm.materialEntryList[${container.index}].sourceipo"
									id="materialCode${container.index}sourceipo"
									value="${item.ipoFlagIbaseJsp}"  disabled="true"
									${(item.ipoFlagIbaseJsp) ? 'checked':''}
									${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}
									onKeyPress="return noEnter(event);" />
								</td>
								<%
									}
								%>
								<td align="center" class="highlightCore">
									<%
										if (readOnly) {
									%>
									<input type="checkbox"
									name="actionForm.materialEntryList[${container.index}].selectforRegistration"
									${(item.selectforRegistration) ? 'checked' : ''}
									value="${ item.selectforRegistration }"
									${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} /> <%
									} else {
								%>
									<input type="checkbox"
									name="actionForm.materialEntryList[${container.index}].selectforRegistration"
									id="materialCode${container.index}Select"
									${(item.selectforRegistration) ? 'checked' : ''}
									value="${ item.selectforRegistration }"
									${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}
									onChange="selectforRegistration(this, materialCode${container.index})"
									onKeyPress="return noEnter(event);" /> <%
									}
								%>
								</td>
								<%
									if ("1003".equals(pageContext
														.getAttribute("ibaseStatus"))) {
								%>
								<td>${item.errorDescription}</td>
								<%
									} else {
								%>
								<td class="highlightCore errorTd">
									<span id="materialCode${container.index}Errors" class="errorSpan" style="display: none; ">
										Material Code
										<span id="materialCode${container.index}ErrorsMaterialCode">
											${item.materialCode}
										</span>
										${grtConfig.invalidMaterialCode}
									</span>
									<span id="materialCode${container.index}Error2" class="errorSpan" style="display: none; ">
										${grtConfig.invalidQuantity}
									</span>
									<span id="materialCode${container.index}Error3" class="errorSpan" style="display: none; ">
										${grtConfig.invalidSerialNumber18Char}
									</span>
									<span id="materialCode${container.index}Error4" class="errorSpan" style="display: none; ">
										${grtConfig.quantitySize}
									</span>
									<span id="materialCode${container.index}Error5" class="errorSpan" style="display: none; ">
										${grtConfig.formaterialcodes}
									</span>
									<span id="materialCode${container.index}Error6" class="errorSpan" style="display: none; ">
										${grtConfig.invalidSerialNumber}
									</span>
									<span id="materialCode${container.index}Error7" class="errorSpan" style="display: none; ">
										${grtConfig.sameMaterialCodeQuantity}
									</span>
									<span id="materialCode${container.index}Error8" class="errorSpan" style="display: none; ">
										${grtConfig.serialMaterialQuantity}
									</span>
									<span id="materialCode${container.index}Error9" class="errorSpan" style="display: none; ">
										${grtConfig.serviceUnitMaterialQuantity}
									</span>
									<span id="materialCode${container.index}MaterialExcludedError" class="errorSpan" style="display: none;">
										${grtConfig.materialCodeExcluded}
									</span>
								</td>
								<%
									if (("Yes".equalsIgnoreCase((String) pageContext
															.getAttribute("ipoInventoryNotLoaded")))
															&& ("Yes"
																	.equalsIgnoreCase((String) pageContext
																			.getAttribute("remoteconn")))) {
								%>
								<td align="center">
									<c:set var="selectedRecord" value="${item.coreUnitSelected}" />
									<%
										String selectedValue = (String) pageContext
																	.getAttribute("selectedRecord");
															if (null != selectedValue
																	&& selectedValue.equals("TRUE")) {
									%>
									<input name="materialEntry"
									onclick="return getMaterialVersionMethod(${container.index});"
									checked="true" type="radio"
									${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}
									onKeyPress="return noEnter(event);" />
									<%
									} else {
									%>
									<input name="materialEntry"
									onclick="return getMaterialVersionMethod(${container.index});"
									type="radio"
									${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}
									onKeyPress="return noEnter(event);" />
									<%
									}
									%>
								</td>
								<td align="center">
									<select id="materialEntryVersion${container.index}"
									${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}
									onKeyPress="return noEnter(event);" name="actionForm.materialEntryList[${container.index}].releaseString">
										<option value="">Choose one</option>
										<c:forEach items="${ item.materialEntryVersion }"
											var="materialVal">
											<c:out value="${ item.materialEntryVersion }">${ item.materialEntryVersion }</c:out>
											<c:if test="${ item.releaseString == materialVal }">
												<option selected value="${materialVal}">${materialVal}</option>
											</c:if>
											<c:if test="${ item.releaseString != materialVal }">
												<option value="${materialVal}">${materialVal}</option>
											</c:if>
										</c:forEach>
									</select>
								</td>
								<%
									}
												}
								%>
								<%
									} else {
								%>
								<td nowrap align="center">
									<input type="hidden"
									name="actionForm.materialEntryList[${container.index}].orderId"
									id="materialCode${container.index}orderId"
									value="${ item.orderId }"
									${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} /> <input
									type="text"
									name="actionForm.materialEntryList[${container.index}].materialCode"
									id="materialCode${container.index}"
									value="${ item.materialCode }" class="materialCodeInput"
									onChange="validate_material_code(this)"
									${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}
									maxlength="18" onKeyPress="return noEnter(event);" />
									<%
									if (!readOnly) {
									%>
									
									<!-- Added for co-browse temporary fix -->
									<input id="validateMcCoBrowse${container.index}" type="button" value="validate MC" class="button gray"
									onclick="validate_material_code(document.getElementById('materialCode'+${container.index}))" style="display : none;"/>
									<!-- Added for co-browse temporary fix ends -->
									
									<input id="lookupInstBase${container.index}" type="button" value="Lookup" class="button gray"
									onclick="openSearchPopUp('materialCode'+${container.index} , 'materialCode'+${container.index}+'Desc',''+${container.index})" />
																		
									<%
										}
									%></td>
								<td align="center">
									<input name="actionForm.materialEntryList[${container.index}].initialQuantity"
									type="text" id="materialCode${container.index}InitialQuantity"
									value="${ item.initialQuantity }" size="14"
									${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}
									onChange="validateQuantity(materialCode${container.index})"
									maxlength="13" onKeyPress="return noEnter(event);" />
								</td>
								<%--	[AVAYA]: 09-13-2011 Add corresponding text space for Serial Number to Service Template (Start) --%>
								<%--	[AVAYA]: 09-14-2011 Removed size reference from line 541 (Start) --%>
								<%--	[AVAYA]: 09-15-2011 Added 'OnChange' code on line 562 (Start) --%>
								<td align="center">
									<input type="text" name="actionForm.materialEntryList[${container.index}].serialNumber"
									id="materialCode${container.index}SerialNumber"
									value="${ item.serialNumber }"
									${(installBaseCreationReadOnlyFlag || item.serialized == 'N') || (item.serialized == 'Y' && item.initialQuantity != 1) ? "disabled class='input-disabled'" : ""}
									onChange="validateSerialNumber(materialCode${container.index});"
									onKeyPress="return noEnter(event);" />
								</td>
								<td align="center">
									<span id="materialCode${container.index}Desc">
										<label>${item.description}</label>
									</span>
									<input type="hidden"
									name="actionForm.materialEntryList[${container.index}].description"
									id="materialCode${container.index}DescHidden"
									value="${ item.description }" /> <input type="hidden"
									name="actionForm.materialEntryList[${container.index}].materialExclusion"
									id="materialCode${container.index}ExclusionHidden"
									value="${ item.materialExclusion }" /> <input type="hidden"
									name="actionForm.materialEntryList[${container.index}].isBaseUnit"
									id="materialCode${container.index}baseunit"
									value="${ item.isBaseUnit }" /> <input type="hidden"
									name="actionForm.materialEntryList[${container.index}].isSourceIPO"
									id="materialCode${container.index}sourceipo"
									value="${ item.isSourceIPO }" /> <input type="hidden"
									name="actionForm.materialEntryList[${container.index}].serialValidateFlag"
									id="materialCode${container.index}serialValidateFlag"
									value="${ item.serialValidateFlag }" />
								</td>
								<td align="center">
									<span id="materialCode${container.index}isTR">
										<label>${item.technicallyRegisterable}</label>
									</span>
									<input type="hidden"
									name="actionForm.materialEntryList[${container.index}].technicallyRegisterable"
									id="materialCode${container.index}isTRHidden"
									value="${ item.technicallyRegisterable }" />
								</td>
								<%
									if ("No".equalsIgnoreCase((String) pageContext
														.getAttribute("ipoInventoryNotLoaded"))) {
								%>
								<td align="center">
									<input type="checkbox"
									name="actionForm.materialEntryList[${container.index}].sourceipo"
									id="materialCode${container.index}sourceipo"
									value="${ item.ipoFlagIbaseJsp }" 
									${(item.ipoFlagIbaseJsp) ? 'checked':''} disabled="true"
									${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}
									onKeyPress="return noEnter(event);" />
								</td>
								<%
									}
								%>
								<td align="center">
									<%
										if (readOnly) {
									%>
									<input value="true" type="checkbox"
									name="actionForm.materialEntryList[${container.index}].selectforRegistration"
									${(item.selectforRegistration) ? 'checked' : ''}
									value="${ item.selectforRegistration }"
									${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} />
									<%
									} else {
									%>
									<input type="checkbox"
									name="actionForm.materialEntryList[${container.index}].selectforRegistration"
									${(item.selectforRegistration) ? 'checked' : ''}
									id="materialCode${container.index}Select"
									value="${ item.selectforRegistration }"
									${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}
									onChange="selectforRegistration(this, materialCode${container.index})"
									onKeyPress="return noEnter(event);" />
									<%
									}
									%>
								</td>
								<%
									if ("1003".equals(pageContext
														.getAttribute("ibaseStatus"))) {
								%>
								<td>${item.errorDescription}</td>
								<%
									} else {
								%>
								<td class="errorTd">
									<span id="materialCode${container.index}Errors" class="errorSpan" style="display: none; ">
										Material Code
										<span id="materialCode${container.index}ErrorsMaterialCode">
											${item.materialCode}
										</span>
										${grtConfig.invalidMaterialCode}
									</span>
									<span id="materialCode${container.index}Error2" class="errorSpan" style="display: none; ">
										${grtConfig.invalidQuantity}
									</span>
									<%--	[AVAYA]: 09-15-2011 Adding code to display Serial Number validation Error (Start) --%>
									<span id="materialCode${container.index}Error3" class="errorSpan" style="display: none; ">
										${grtConfig.invalidSerialNumber18Char}
									</span>
									<%--	[AVAYA]: 09-15-2011 Adding code to display Serial Number validation Error (End) --%>
									<span id="materialCode${container.index}Error4" class="errorSpan" style="display: none; ">
										${grtConfig.quantitySize}
									</span>
									<span id="materialCode${container.index}Error5" class="errorSpan" style="display: none; ">
										${grtConfig.formaterialcodes}
									</span>
									<span id="materialCode${container.index}Error6" class="errorSpan" style="display: none; ">
										${grtConfig.invalidSerialNumber}
									</span>
									<span id="materialCode${container.index}Error7" class="errorSpan" style="display: none; ">
										${grtConfig.sameMaterialCodeQuantity}
									</span>
									<span id="materialCode${container.index}Error8" class="errorSpan" style="display: none; ">
										${grtConfig.serialMaterialQuantity}
									</span>
									<span id="materialCode${container.index}Error9" class="errorSpan" style="display: none; ">
										${grtConfig.serviceUnitMaterialQuantity}
									</span>
									<span id="materialCode${container.index}MaterialExcludedError" class="errorSpan" style="display: none;">
										<span id="materialCode${container.index}MaterialExcludedErrorHidden">
											<%
												if ("PLDS".equalsIgnoreCase((String) pageContext.getAttribute("mcExclusionSource"))) {
											%>
											${grtConfig.prepareTechnicalOrderMaterialExclusionError1}
											<%
												} else if ("KMAT".equalsIgnoreCase((String) pageContext.getAttribute("mcExclusionSource"))) {
											%>
											${grtConfig.prepareTechnicalOrderMaterialExclusionError2}
											<%
												} else if ("NMPC".equalsIgnoreCase((String) pageContext.getAttribute("mcExclusionSource"))) {
											%>
											${grtConfig.prepareTechnicalOrderMaterialExclusionError2}
											<%
												} else if ("DEFECTIVE".equalsIgnoreCase((String) pageContext.getAttribute("mcExclusionSource"))) {
											%>
											${grtConfig.prepareTechnicalOrderMaterialExclusionError2}
											
											<%} %>
										</span>
									</span>
								</td>
								<%
									if (("Yes".equalsIgnoreCase((String) pageContext
															.getAttribute("ipoInventoryNotLoaded")))
															&& ("Yes"
																	.equalsIgnoreCase((String) pageContext
																			.getAttribute("remoteconn")))) {
								%>
								<td align="center">
									<c:set var="selRecord" value="${item.coreUnitSelected}" />
									<%
											String selValue = (String) pageContext
																	.getAttribute("selRecord");
															if (null != selValue && selValue.equals("TRUE")) {
									%>
									<input name="materialEntry"
										onclick="return getMaterialVersionMethod(${container.index});"
										checked="true" type="radio"
										onKeyPress="return noEnter(event);" />
									<%
										} else {
									%>
									<input name="materialEntry"
									onclick="return getMaterialVersionMethod(${container.index});"
									type="radio" onKeyPress="return noEnter(event);" />
									<%
										}
									%>
								</td>
								<td align="center">
									<select id="materialEntryVersion${container.index}"
									${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}
									onKeyPress="return noEnter(event);" name="actionForm.materialEntryList[${container.index}].releaseString">
										<option value="">Choose one</option>
										<c:forEach items="${ item.materialEntryVersion }" var="value">
											<c:if test="${ item.releaseString == value }">
												<option selected value="${value}">${value}</option>
											</c:if>
											<c:if test="${ item.releaseString != value }">
												<option value="${value}">${value}</option>
											</c:if>
										</c:forEach>
									</select>
								</td>
								<%
									} }
								%>
								<%
									}
								%>
							</tr>
							<script type="text/javascript">
								show_validation_msg_desc("materialCode${container.index}", "${item.description}","${item.materialExclusion}", "${item.technicallyRegisterable}", "MCUL", "${item.exclusionSource}"  );
							</script>
							
							<script type="text/javascript">
								validateSerialNumberOnLoad( "materialCode${container.index}" );							
							</script>
							<%
								varIndex++;
							%>
							</c:forEach>
						</tbody>
					</table>

					<!-- start manual entry btns -->
					<div class="controls manual-entry-btns">
						<div class="left-col">
							<input type="button" value=" Manually Add Material Code " id="addBtn"
							class="button gray" onclick="manuallyAddFormSubmit()" />
							<a href="<c:url context="${ _path }" value="/installbase/addMaterialEntry"/>"
							id="manuallyAddAction"></a>
						</div>
						<div class="right-col">
							<input type="file" name="actionForm.theFile" id="uploadFile"
							${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} />
							<input type="button" id="uploadBtn" value="Upload IBASE information"
							class="button gray" onclick="return download_template_upload_file('uploadFile');"
							${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} />
							<a href="<c:url context="${ _path }" value="/installbase/uploadFile"/>"
							id="uploadAction"></a>
							<div id="file.container">&nbsp;</div>
							<div id="filediv" style="display: none;">
								<a href="" id="file_a">uploaded file</a>
							</div>
						</div>
					</div>
					<!-- end manual entry btns -->
	
					<!-- start controls -->
					<div class="controls">

						<input type="hidden" id="skipInstallBaseCreation"
						dataInput="false" value="${ actionForm.skipInstallBaseCreation}" />
						<input type="hidden" id="ibSrCreated"
						name="actionForm.ibSrCreated" value="${ actionForm.ibSrCreated }" />
						<input type="hidden" id="statusId" name="actionForm.statusId"
						value="${ fn:escapeXml(actionForm.statusId) }" /> <input type="hidden"
						name="actionForm.message" value="${ fn:escapeXml(actionForm.message) }"
						id="installBaseMessage" /> <input type="hidden"
						name="actionForm.returnCode" value="${fn:escapeXml(actionForm.returnCode)}"
						id="installBaseReturnCode" />

						<%
							if ((request.getSession().getAttribute("installStatus")) != null
										&& (request.getSession().getAttribute("installStatus"))
												.toString().equalsIgnoreCase("disable")) {
									request.getSession().removeAttribute("installStatus");
						%>

						<input type="button" value="Cancel" class="button gray"
							disabled="disabled" />

						<%
							} else if ((request.getSession().getAttribute("installStatus")) != null
										&& (request.getSession().getAttribute("installStatus"))
												.toString().equalsIgnoreCase("saved")) {
									request.getSession().removeAttribute("installStatus");
						%>

						<input type="button" value="Cancel" class="button gray"
							onClick="return cancelConfirm();"
							${(actionForm.readOnly) ? 'disabled' : ''} />

						<%
							} else {
									request.getSession().removeAttribute("installStatus");
						%>

						<input type="button" value="Cancel" class="button gray"
							onClick="return cancelConfirmation();"
							${(actionForm.readOnly) ? 'disabled' : ''} />

						<%
							}
								String action = null;

								if ((((request.getSession().getAttribute("IBBack")) != null) && (((request
										.getSession().getAttribute("IBBack")).toString())
										.equalsIgnoreCase("RegList")))
										|| ((pageContext.getAttribute("form") != null) && ((pageContext
												.getAttribute("form")).toString()
												.equalsIgnoreCase("RegList")))) {
									action = "registrationList";
								} else if ((((request.getSession().getAttribute("IBBack")) != null) && (((request
										.getSession().getAttribute("IBBack")).toString())
										.equalsIgnoreCase("ipreg")))
										|| ((pageContext.getAttribute("form") != null) && ((pageContext
												.getAttribute("form")).toString()
												.equalsIgnoreCase("ipreg")))) {
									action = "ipoReg";
								} else {
									action = "newRegistration";
								}
								request.getSession().removeAttribute("IBBack");
								pageContext.removeAttribute("form");
						%>

						<input type="button" class="button gray"
							onclick="backFromIB('<%=action%>')" value="Back" />
						
						<input type="button"
							id="saveRegistration" value="Save" class="button gray"
							onclick="return saveRegistrationFunc(${materialEntryListSize}, ${salMigrationOnlyFlag});"
							${installBaseCreationReadOnlyFlag ? 'disabled' : ''} />

						<%
							if (("Yes".equalsIgnoreCase((String) pageContext
										.getAttribute("remoteconn")))) {
						%>

						<input type="button" id="submitRegistration" value="Submit"
							class="button gray"
							onclick="submitRegistrationFuncForIPO(${materialEntryListSize}, ${salMigrationOnlyFlag}, '${actionForm.ipoInventoryNotLoaded}'); return false;"
							${installBaseCreationReadOnlyFlag ? 'disabled' : ''} />

						<% } else { %>

						<input type="button" id="submitRegistration" value="Submit"
							class="button gray"
							onclick="submitRegistrationFunc(${materialEntryListSize}, ${salMigrationOnlyFlag}, '${actionForm.remoteConnectivity}');return false;"
							${installBaseCreationReadOnlyFlag ? 'disabled' : ''} />

						<% } %>

						<script type="text/javascript">
							$(document).ready(function() {
								
								enableSaveSubmit(${salMigrationOnlyFlag}, ${installBaseCreationReadOnlyFlag});
								showSkipInstallBaseCreationTR(${pageFlow.regionAdministrable});
								shwipoPopup(${pageFlow.ipoContinueInstallBase});
								showmaterialCodeValidPopup(${pageFlow.materialCodeListSize});
								showMessageToUser();
								
								
								var flag = ${installBaseCreationReadOnlyFlag};
								if (flag == true) {

									document.getElementById("saveRegistration").className = "button";
									document.getElementById("submitRegistration").className = "button";
									document.getElementById("addBtn").className = "button";
									document.getElementById("addBtn").disabled = true;
									document.getElementById("uploadBtn").className = "button";
									var save = document.getElementById("saveRegistration");
									if (save != null) {
										save.disabled = true;
									} 

								} else {
									document.getElementById("saveRegistration").className = "button gray";
									document.getElementById("submitRegistration").className = "button gray";
									document.getElementById("addBtn").className = "button gray";
									document.getElementById("uploadBtn").className = "button gray";
									document.getElementById("addBtn").disabled = false;
								}
								
							});
						</script>
					</div>
					<!-- end controls -->

				</s:form>
				<script>
				jQuery(document).ready(function($) {
					// cannot wrap table with div tag due to events
					// so using JS to wrap on load to allow scrolling horizontally
					$('#materialEntryListTable').wrap('<div class="basic-table-wrap" />');
				});
				</script>
				<!-- end manual entry form -->
			</div>
		</div>
		<!-- end manual entry table-wrap -->

		<!-- start materialCodeSearchPopUp -->
		<div id="materialCodeSearchPopUp" class="modal" style="display: none;">
			<div class="modal-overlay">
				<div class="modal-content">
					<a class="close" onclick="closePopUp()"><i class="fa fa-close"></i></a>
					<h2 class="title">Material Code Search Look Up</h2>
					<div class="searchHeader">
						Enter SE Code(s) Separated By Commas : <input type="text"
							id="searchText" /> <input type="button" type="button"
							id="materialCodeSearchButton" class="button gray small" value="Search"
							onclick="performSearch()" />
						<div style="float: right"></div>
					</div>
					<div class="content">
						<div>
							<input type="hidden" id="populateFor"> <input type="hidden"
								id="populateDesc"> <input type="hidden" id="rowId">
							<%-- <span id="searchingId"
								style="visibility: hidden; color: green;">Searching..</span> --%>

							<div id="se-wrap" class="data-table-wrap collapse-box">
								<h2 class="collapse-box-header active">
									<a href="#">
										<span></span>
									</a>
								</h2>
								<div id="" class="data collapse-box-container" style="display: block;">
									<table id="resultTable" border="1">
										<thead>
											<tr>
												<th width="10%">Material Code</th>
												<th width="30%">Material Description</th>											
												<th width="25%">SE Code(s)</th>
												<th width="35%">Group Description</th>
												
											</tr>
										</thead>
										<tbody>
										</tbody>
									</table>
									<div id="noResultsId" class="error-bar" style="display: none;">
										<i class="fa fa-exclamation-triangle"></i><span class="error-bar-msg">${grtConfig.materialCodeNotMatchWithSECode}</span>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- end materialCodeSearchPopUp -->

		<!-- start popup-wind -->
		<div id="popup-wind" class="modal" style="display: none;">
			<div class="modal-overlay">
				<div class="modal-content">
					<!-- <a class="close" onclick="closeWarningPopup();"><i class="fa fa-close"></i></a> -->
					<h2 class="title">Attention</h2>
					<p class="content">
						<img class="msg-icon"
							src="<c:url context="${ _path }" value="/styles/images/messages/icon_warning_light_60x60.png" />"
							alt="info" />
							<span class="msg-txt">${grtConfig.validBaseUunit}</span>
					</p>
					<div class="controls">
						<input type="button" class="button gray" onclick="closePop();" action="ipoOnlyIbase" value="Yes">
						<input type="button" class="button gray" onclick="closePop(); return false;" value="No">
					</div>
				</div>
			</div>
		</div>
		<!-- end popup-wind -->

		<!-- start message-window -->
		<div id="message-window" class="modal" style="display: none;">
			<div class="modal-overlay">
				<div class="modal-content">
					<h2 class="title">For soldTo: ${fn:escapeXml(actionForm.soldToId)}</h2>

					<p class="content">
						<img class="msg-icon"
							src="<c:url context="${ _path }" value="/styles/images/messages/icon_warning_light_60x60.png" />"
							alt="info" />
							<span class="msg-txt" id="message">${grtConfig.continueRegistration }</span>
					</p>
					<div class="controls">
						<input type="button" class="button gray" onclick="closeMessage();" action="submitRegistration" value="Yes" />
						<input type="button" class="button gray" onclick="closeMessage(); return false;" value="No" />
					</div>
				</div>
			</div>
		</div>
		<!-- end message-window -->

		<!-- start install_message-window -->
		<div id="install_message-window" class="modal" style="display: none;">
			<div class="modal-overlay">
				<div class="modal-content">
					<h2 class="title">Info</h2>

					<p class="content">
						<img class="msg-icon"
							src="<c:url context="${ _path }" value="/styles/images/messages/icon_info_light_60x60.png" />"
							alt="info" />
							<span class="msg-txt" id="installBasefinalmessage"></span>
					</p>
					<div class="controls">
						<input type="button" class="button gray ibsubmitOk" onclick="javascript:return closeInstallBaseMsg();" value="OK">
					</div>
				</div>
			</div>
		</div>
		<!-- end install_message-window -->

		<!-- start warningPopup -->
		<div id="warningPopup" class="modal" style="display: none;">
			<div class="modal-overlay">
				<div class="modal-content">
					<a class="close" onclick="closeWarningPopup();"><i class="fa fa-close"></i></a>
					<h2 class="title">Confirmation</h2>

					<p  class="content">
						<img class="msg-icon"
							src="<c:url context="${ _path }" value="/styles/images/messages/icon_info_light_60x60.png" />"
							alt="info" />
							<span class="msg-txt" id="warningmessage"></span>
					</p>
					<div class="controls">
						<input id="wrnPopupYesBtnId" type="button" value="Yes" class="button gray" onclick="submitInstallBaseCreation();" />

						<%-- <a id="finalIBSubmitAction" href="<c:url context="${ _path }" value="/installbase/validateMaterialCodesAgainstSoldTo"/>"></a> --%>

						<input id="wrnPopupNoBtnId" type="button" value="No" class="button gray" onclick="closeWarningPopup();" />

					</div>
				</div>
			</div>
		</div>
		<!-- end warningPopup -->

		<!-- start popup-window-ib -->
		<div id="popup-window-ib" class="modal" style="display: none;">
			<div class="modal-overlay">
				<div class="modal-content">
					<!-- <a class="close" onclick="closeWarningPopup();"><i class="fa fa-close"></i></a> -->
					<h2 id="popup-window-title-ib" class="title">Waiting...</h2>
					<p id="popup-window-content-ib" class="content">Loading data</p>
				</div>
			</div>
		</div>
		<!-- end popup-window-ib -->
		


		<!-- End Error Codes -->
	</div>
	<!-- end page content -->
</div>
<!-- end page content-wrap -->
