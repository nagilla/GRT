<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="com.grt.dto.TechnicalOrderDetail"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.grt.dto.RegistrationFormBean" %>
<%@ page import="java.util.List"%>
<%@ page import="com.grt.dto.Pager"%>
<%@ include file="/includes/context.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/views/record-validation.css" />"/>
<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/plugins/jquery.dataTables_themeroller.css" />"/>
<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/plugins/jquery-ui.css" />"/>

<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/plugins/jquery.dataTables.min.js" />"></script>
<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/plugins/jquery.dataTables.columnFilter.js" />"></script>
<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/plugins/jquery.datatable.extplugin.js" />"></script>
<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/plugins/dynamicTable.js" />"></script>
<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/recordValidation/grt.recordvalidation.js" />"></script>

<script type="text/javascript">

	<c:if test="${not empty actionForm.uploadMessage}">
		window.onload = function() {
			convertAlertToModelPopUpServerSideMsg("${actionForm.uploadMessage}");
		};
	</c:if>

	$( document ).ready(function() {


		var rows = $("#recordValidationTable").dataTable().fnGetNodes();
		for(var i=0; i < rows.length; i++) {
			var row = $(rows[i]);
			var errorDesc = row.find("td").find("input[id^=errorDescription]").attr("value");
			var highlight =	row.attr("highlight");
			if(highlight == "true" || (null != errorDesc && errorDesc.trim() != "")) {
				row.addClass( 'highlight' );
				var checkBox = row.find("td:first-child").find("input");
				checkBox.attr("checked",false);
				checkBox.attr("disabled",true);
				row.find("i[id^='exclamation']").show();
			}
		}

		//Focus on Material Code
		if($("#materialCode0") != null && $("#materialCode0").is("input") != null ){
			$("#materialCode0").focus();
		}
		hideLoadingOverlay();
		$("#loading-overlay-rec-val").hide();


		showMessageToUser();
		var formReadOnly = document.getElementById("formReadOnly").value;
		if(formReadOnly != null && formReadOnly != "true") {
			validateButtonStatus();
		}


		//If No data available in table Disable Export
		if($("#recordValidationTable").dataTable().fnSettings().aoData.length == 0 ) {
			$("#exportRecordValidation").attr("disabled", true);
			$("#exportRecordValidation").removeClass("gray");


			$("#validateRecordValidation").attr("disabled", true);
			$("#validateRecordValidation").removeClass("gray");
		}
		//Adjust column heading on load
		$("#recordValidationTable").dataTable().fnAdjustColumnSizing();
	});

	/* modal - materialCodeSearchPopUp */
	function openSearchPopUp(id, descId, selectionId) {
		document.getElementById('populateFor').value = id;
		document.getElementById('populateDesc').value = descId;
		document.getElementById('rowId').value = selectionId;
		show('materialCodeSearchPopUp');
		//show('fade');
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
		//hide('fade');
	}

	/* toggle - helper */
	function hide(elemId) {
		document.getElementById(elemId).style.display = 'none';
	}
	/* toggle - helper */
	function show(elemId) {
		document.getElementById(elemId).style.display = 'block';
	}
	/* toggle - helper */
	function setVisible(id) {
		document.getElementById(id).style.visibility = "visible";
	}
	/* toggle - helper */
	function setInVisible(id) {
		document.getElementById(id).style.visibility = "hidden";
	}

	function performSearch() {
		var searchText = document.getElementById('searchText').value;
		//setVisible('searchingId');
		if ($.trim(searchText).length > 0) {
			$(".loading-overlay").show();
			$("#noResultsId.error-bar").hide();
			$.ajax({
				url: _path + '/installbase/json/getEligibleGroups.action?seCodes=' +
					searchText,
				dataType: 'json',
				success: function(data) {
					searchResult = data;
					//setInVisible('searchingId');

					$('#resultTable tr').not(':first').remove();
					var html = '';
					for (var i = 0; i < data.length; i++) {
						var isMain = data[i].mainMaterialCode;
						var seCode = data[i].seCode != null ? data[i].seCode : '';
						html += '<tr><td>' + seCode + '</td><td>' + data[i].groupDescription +
							'</td>' +
							'<td><a href= "javascript:void(0)" onclick =" onMaterrialCodeClick(' +
							i + ')">' + data[i].parentMaterialCode + '</a></td><td>' + data[i].parentMaterialCodeDescription +
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
	function onMaterrialCodeClick(index) {

		//populateForId is the id of material code input tag
		var populateForId = document.getElementById('populateFor').value;
		var populateDescId = document.getElementById('populateDesc').value;
		document.getElementById(populateForId).value = searchResult[index].parentMaterialCode;
		//Check whether material code is valid(ie, it should present in existing list)
		if(isMaterialCodeExisting(searchResult[index].parentMaterialCode.trim())) {

			document.getElementById(populateDescId).innerHTML = searchResult[index].parentMaterialCodeDescription;
			var descHiddenId = "materialCode" + document.getElementById('rowId').value +
				"DescHidden";
			document.getElementById(descHiddenId).value = searchResult[index].parentMaterialCodeDescription;

			closePopUp();
			document.getElementById(populateForId + "Errors").style.display = "none";
			//setting hidden desc empty
			$("#"+populateForId).parent().parent().find('input:hidden[id^=errorDescription]').attr("value","");


			//Claer existsing field
			document.getElementById(populateForId + "ExistingError").innerHTML = "";
			//Remove Highlight
			clearHighlight(document.getElementById(populateForId));
		} else {
			document.getElementById(populateDescId).innerHTML = "";
			var descHiddenId = "materialCode" + document.getElementById('rowId').value +
				"DescHidden";
			document.getElementById(descHiddenId).value = "";

			closePopUp();
			//setting hidden error message to empty
			$("#"+populateForId).parent().parent().find('input:hidden[id^=errorDescription]').attr("value","");

			//Clear existing field
			document.getElementById(populateForId + "ExistingError").innerHTML = "";
		}


		//Validate serial Number and quantity
		var serialNoId = $("#"+populateForId).parent().parent().find("input[id^=serialNumber]").attr("id");
		var materialCodeElement = document.getElementById(populateForId);
		validate_material_code(materialCodeElement);

	}


	/* modal - materialCodeSearchPopUp ends here*/





	var recordValidateTable;
	function clearValueOnUncheck(object, item){
		if(!object.checked){
			document.getElementById('remainingQuantity'+item).value='';
			document.getElementById('quantityRemoved'+item).innerHTML = '';
			document.getElementById('hidQuantityRemoved'+item).value = '';
		}
	}

	function changeSerialized(item){
		var existingSerialNumber = document.getElementById('hidSerialNumber'+item).value;
		var updatedSerialNumber = document.getElementById('serialNumber'+item).value;
		if(existingSerialNumber != updatedSerialNumber) {
				document.getElementById('newSerialNumber'+item).value = existingSerialNumber;
				document.getElementById("validateRecordValidation").disabled = true;
				document.getElementById('validateEnabled').value = 'N';
		} else {
			document.getElementById('newSerialNumber'+item).value = '';
			validateButtonStatus();
		}
	}

	function changeValues(item){
		var revised = document.getElementById('remainingQuantity'+item).value;
		var existing = "0";
		if(document.getElementById('availableQuantity'+item) !=null) {
			existing = document.getElementById('availableQuantity'+item).value;
		} else {
			existing = $("#remainingQuantity"+item).parent().prev().find("input").attr("value");
		}
		if(parseInt(revised) < 0){
			convertAlertToModelPopUp('Revised Quantity cannot be less than Zero.');
			document.getElementById('remainingQuantity'+item).value='';
			document.getElementById('quantityRemoved'+item).innerHTML = '';
			document.getElementById('hidQuantityRemoved'+item).value = '';
			return false;
		} else if(revised.trim() == ''){
			document.getElementById('quantityRemoved'+item).innerHTML = '';
			document.getElementById('hidQuantityRemoved'+item).value = '';
			validateButtonStatus();
		} else if(parseInt(revised) >= parseInt(existing)){
			var newVal=parseInt(revised)-parseInt(existing);
			if(newVal == 0) {
				document.getElementById('quantityRemoved'+item).innerHTML =newVal;
			} else {
				document.getElementById('quantityRemoved'+item).innerHTML ='&#43;'+newVal;
			}
			document.getElementById('hidQuantityRemoved'+item).value = newVal; //'&#43;'+newVal;
			document.getElementById("validateRecordValidation").disabled = true;
			document.getElementById('validateEnabled').value = 'N';
			$("#validateRecordValidation").removeClass("gray");
			return false;
		}
   	 	else{
			var newVal=parseInt(existing)-parseInt(revised);
			document.getElementById('quantityRemoved'+item).innerHTML ='&#45;'+newVal;
			document.getElementById('hidQuantityRemoved'+item).value =newVal;//'&#45;'+newVal;
			document.getElementById("validateRecordValidation").disabled = true;
			document.getElementById('validateEnabled').value = 'N';
			$("#validateRecordValidation").removeClass("gray");
		}
	}

	function validateButtonStatus(){


		var rows = $("#recordValidationTable").dataTable().fnGetNodes();
		for(var i=0; i < rows.length; i++) {
			var row = $(rows[i]);
			var index = row.find("td").find("input[id^=deleted]").attr("id").replace("deleted","").trim();

			if(row.find('#hidQuantityRemoved'+index).val() != '') {
				$("#validateRecordValidation").attr("disabled", true);
				$("#validateRecordValidation").removeClass("gray");
				$('#validateEnabled').attr("value", "N");
				return;
			}
			if(row.find('#serialNumber'+index) != null && row.find('#hidSerialNumber'+index) != null){
				var existingSNo = row.find('#hidSerialNumber'+index).val();
				var updatedSNo = row.find('#serialNumber'+index).val();
				if(existingSNo != null && existingSNo != updatedSNo) {
					$("#validateRecordValidation").attr("disabled", true);
					$("#validateRecordValidation").removeClass("gray");
					$('#validateEnabled').attr("value", "N");
					return;
				}

			}
			if(row.find('td:nth-child(2)').text().trim() == "NA" ) {
				$("#validateRecordValidation").attr("disabled", true);
				$("#validateRecordValidation").removeClass("gray");
				$('#validateEnabled').attr("value", "N");
				return;
			}


		}


		$("#validateRecordValidation").attr("disabled", false);
		$("#validateRecordValidation").addClass("gray");
		$('#validateEnabled').attr("value", "Y");


	}
	
	function isSerialNumberChanged(serialNumberId) {
		var index =  serialNumberId.replace("serialNumber","").trim();
		if($('#'+serialNumberId) != null && $('#hid'+serialNumberId) != null){
			var existingSNo = $('#hidSerialNumber'+index).val();
			var updatedSNo = $('#'+serialNumberId).val();
			if(existingSNo != null && existingSNo != updatedSNo) {				
				return true;
			}
		}
		return false;
	}

	function selectNunselect(obj){

		var rows = $("#recordValidationTable").dataTable().fnGetNodes();
		if(obj.checked){
			for(var i=0; i < rows.length; i++) {
				var checkBox = $(rows[i]).find("td:first-child").find("input");

				if(checkBox.attr("disabled") == '' || checkBox.attr("disabled") == null || checkBox.attr("disabled") == "false") {
					checkBox.attr("checked",true);
				}
			}
		} else {
			for(var i=0; i < rows.length; i++) {
				var checkBox = $(rows[i]).find("td:first-child").find("input");

				checkBox.attr("checked",false);
			}
		}

		return false;// to be removed
	}


	function updateValues(saveType){
		var checkFlag = 0;
		var mcWithActive = '';
		var rows = $("#recordValidationTable").dataTable().fnGetNodes();
		for(var i=0; i < rows.length; i++) {
			var row = $(rows[i]);


			var checked =	row.find("td:first").find("input").prop("checked");
			if(null != checked) {
				if(checked) {
					checkFlag++;
					//Need to get verification as there may be cases to update only serial numbers.
					var index = row.find("input[id^=deleted]").attr("id").replace("deleted","").trim();
					var existingSerialNumber = row.find('#hidSerialNumber'+index).val();
					var updatedSerialNumber = row.find('#serialNumber'+index).val();
					//If there is no serial numbers, set it as empty string
					if( null == existingSerialNumber || null == updatedSerialNumber ) {
						existingSerialNumber = "";
						updatedSerialNumber = "";
					}


					var remainingQuantity = row.find("td").find("input[id^='remainingQuantity']").attr("value");
					if(null!= remainingQuantity && remainingQuantity == ''){
						//If serial Number is not updated, Revised Quantity is mandatory
						if(existingSerialNumber == updatedSerialNumber ) {
							convertAlertToModelPopUp($("#emptyRevisedQuantityMsgCode").val(), $("#emptyRevisedQuantityMsg").val());
							return false;
						}


					}

					//Find Active contract element in which id start with activeContract)
					var activeContract = row.find("td").find("input[id^='activeContract']").attr("value");
					if(null != activeContract && activeContract != ''
						&& activeContract == 'Yes'){
						mcWithActive = mcWithActive + activeContract + ", ";
					}
				}

			}
		}


		document.getElementById('saveType').value = saveType;
		if(checkFlag == 0){
			convertAlertToModelPopUp( $("#selectOneEquipmentMsgCode").val(), $("#selectOneEquipmentMsg").val().replace(":saveType", saveType) );
			return false;
		} else if(saveType == 'submit'){
			var submitConfirmMsg = $("#submitConfirmMsg").val();
			var submitConfirmMsgCode = $("#submitConfirmMsgCode").val();
			var callBackMethodStringForOk = "submitRecordValidationForm('recordValidationForm' , 'saveRecordValidationAction', 'recordValidationTable', 'submitLoadingTextMsg')";
			var callBackMethodStringForCancel ="";
			if(mcWithActive != ''){
				//Defect : Confirm msg for active contract & normal records should be same
				convertConfirmToModelPopUp(submitConfirmMsgCode, submitConfirmMsg, callBackMethodStringForOk, callBackMethodStringForCancel );
				return false;
			}
			convertConfirmToModelPopUp(submitConfirmMsgCode, submitConfirmMsg, callBackMethodStringForOk, callBackMethodStringForCancel );
			return false;
		} else if(saveType == 'save'){
			var message = $("#saveLoadingTextMsg").val();
			showLoadingOverlay(message);
			document.getElementById('recordValidationForm').action = document.getElementById('saveRecordValidationAction').href;
			$('#recordValidationTable').dataTable().fnDestroy();
			document.getElementById('recordValidationForm').submit();
		}
	}


	function submitRecordValidationForm(formId , actionUrl, dataTableId, loadingOverlayMsgId) {
		if(loadingOverlayMsgId != null && loadingOverlayMsgId != "") {
			var message = $("#" + loadingOverlayMsgId).val();
			showLoadingOverlay(message);
		} else {
			showLoadingOverlay();
		}
		if(dataTableId != null && dataTableId != "") {
			$('#' + dataTableId).dataTable().fnDestroy();
		}
		document.getElementById(formId).action = document.getElementById(actionUrl).href;
		document.getElementById(formId).submit();
	}


	function showLoadingOverlay(message){
		if(message != null && message != "") {
			document.getElementById("loading-msg").innerHTML = message;
		}
		$("#loading-overlay").show();
	}
	function hideLoadingOverlay(){
		$("#loading-overlay").hide();
	}
	//For Validate button click
	function selectValidate(saveType){
		document.getElementById('saveType').value = saveType;


		var confirmValidateTextMsg = $("#confirmValidateTextMsg").val();
		var confirmValidateTextMsgCode = $("#confirmValidateTextMsgCode").val();
		var callBackMethodStringForOk = "submitRecordValidationForm('recordValidationForm' , 'saveRecordValidationAction', '', '')";
		var callBackMethodStringForCancel ="";
		convertConfirmToModelPopUp(confirmValidateTextMsgCode, confirmValidateTextMsg, callBackMethodStringForOk, callBackMethodStringForCancel );
		return false;
	}

	function exportValidatedRecord() {
		document.getElementById('recordValidationForm').action = document.getElementById('exportRecordValidationAction').href;
		document.getElementById('recordValidationForm').submit();
	}

	function addManuallyAddedRecord() {
		document.getElementById('recordValidationForm').action = document.getElementById('addManuallyAddedAction').href;
		document.getElementById('recordValidationForm').submit();
	}

	// Begin: Hour Glass Implementation
	function show_popup(msg, title) {
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
		var cancelConfirmMsgHome = $("#cancelConfirmMsgHome").val();
		var cancelConfirmMsgHomeCode = $("#cancelConfirmMsgHomeCode").val();
		var callBackMethodStringForOk = "submitRecordValidationForm('recordValidationForm' , 'backToHomeLocationAction', '', '')";
		var callBackMethodStringForCancel ="";
		convertConfirmToModelPopUp(cancelConfirmMsgHomeCode, cancelConfirmMsgHome, callBackMethodStringForOk, callBackMethodStringForCancel );
	}

	function cancelConfirm() {
		if(confirm($("#cancelConfirmMsgRegList").val())) {
	 		return true;
	 	} else {
			return false;
		}
	}

	function backToLocation() {
		document.getElementById('recordValidationForm').action = document.getElementById('backToLocationAction').href;
		document.getElementById('recordValidationForm').submit();
	}

	function showMessageToUser(){
		var eqrMessage = document.getElementById('eqrMessage').value;
		var returnCode = document.getElementById('eqrReturnCode').value;
		if(eqrMessage != null && eqrMessage.length > 0){
			//Error Code : 14 Validation success code
			if(returnCode == 'S'){
				eqrMessage = $("#RVSuccessMessageCode").val() + "###" + $("#RVSuccessMessage").val();
			}
			if(returnCode == '100'){
				eqrMessage = $("#RVErrorMessage").val() + "###" + $("#RVErrorMessage").val();

			}
			if(returnCode == 'S' || returnCode == '100' || returnCode == '14'){
				var url = document.getElementById('registrationListAction').href;
				convertAlertToModelPopUpWithRedirect(eqrMessage, url);
			} else{
				convertAlertToModelPopUpServerSideMsg(eqrMessage);
			}
		}
	}

	function numericFilter(txb) {
		 txb.value = txb.value.replace(/[^0-9]/ig, "");
	}

	function sortByField(field){
		var sortByCount = document.getElementById('sortByCount').value;
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
		document.getElementById('equipmentRemovalForm').action=url;
		document.getElementById('equipmentRemovalForm').submit();
	}

	function determineSortSelection(){
		var count = document.getElementById('sortByCount').value;
		var field = document.getElementById('sortBy').value;
		var screen = document.getElementById('sortByScreen').value;
		setSortChange(screen, field, count);
	}

	function setSortChange(screen, field, count){
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


	function download_template_upload_file_Cancel(theFile) {
		$(".confirmMsg .alertCancelBtn").html("Cancel");
		$(".confirmMsg .alertOkBtn").html("Ok");
		$(".confirmMsg .close").attr("onclick", "closeConfirmAlert();");
		revertOnScreenChanges();
		showLoadingOverlay("Record Validation file upload is in progress. Please Wait..." );
		document.getElementById('recordValidationForm').action = document.getElementById('uploadAction').href;
		document.getElementById('recordValidationForm').submit();
		return true;
	}
	
	function download_template_upload_file_ok() {
		$(".confirmMsg .alertCancelBtn").html("Cancel");
		$(".confirmMsg .alertOkBtn").html("Ok");
		$(".confirmMsg .close").attr("onclick", "closeConfirmAlert();");
		return true;
	}
	
	function download_template_upload_file(theFile) {
		var uploadFile = document.getElementById(theFile);
		if(document.getElementById('validateEnabled').value == 'N') {
			if(uploadFile.value == "") {				
				//Defect#800
				convertAlertToModelPopUp($("#nofileChosenForUploadErrMsgCode").val(), $("#nofileChosenForUploadErrMsg").val());
				return false;
			} else {

				var downloadTemplateMsg = $("#downloadTemplateMsg").val();
				var downloadTemplateMsgCode = $("#downloadTemplateMsgCode").val();
				var callBackMethodStringForOk = "download_template_upload_file_ok()";
				var callBackMethodStringForCancel ="download_template_upload_file_Cancel('uploadFile')";
				convertConfirmToModelPopUp(downloadTemplateMsgCode, downloadTemplateMsg, callBackMethodStringForOk, callBackMethodStringForCancel );
				//Change Ok and cancel to import and cancel.
				$(".confirmMsg .alertCancelBtn").html("Import");
				$(".confirmMsg .alertOkBtn").html("Cancel");
				$(".confirmMsg .close").attr("onclick", "closeConfirmAlert();" + callBackMethodStringForOk);
				
			}			
		} else {
			if(uploadFile.value == "") {
				//Defect#800
				convertAlertToModelPopUp($("#nofileChosenForUploadErrMsgCode").val(), $("#nofileChosenForUploadErrMsg").val());
				return false;
			} else {
				showLoadingOverlay("Record Validation file upload is in progress. Please Wait..." );
				document.getElementById('recordValidationForm').action = document.getElementById('uploadAction').href;
				document.getElementById('recordValidationForm').submit();
				return true;
			}
		}
	}

	function revertOnScreenChanges(){
		//convertAlertToModelPopUp('Inside revertOnScreenChanges');
		var i=0;
		while(document.getElementById('hidQuantityRemoved'+i) != null){
			if(document.getElementById('hidQuantityRemoved'+i).value != '') {
				document.getElementById('remainingQuantity'+i).value='';
				document.getElementById('quantityRemoved'+i).innerHTML = '';
				document.getElementById('hidQuantityRemoved'+i).value = '';
			}
			if(document.getElementById('serialNumber'+i) != null && document.getElementById('newSerialNumber'+i) != null){
				var existingSNo = document.getElementById('hidSerialNumber'+i).value;
				var updatedSNo = document.getElementById('serialNumber'+i).value;
				if(existingSNo != updatedSNo) {
					document.getElementById('serialNumber'+i).value = existingSNo;
					document.getElementById('newSerialNumber'+i).value = '';
				}
			}
			i++;
		}
	}

	function noEnter(event) {
		if(event && event.keyCode == 13){
			convertAlertToModelPopUp($("#validOptionMsgCode").val(), $("#validOptionMsg").val());
			return false;
		} else {
			return true;
		}
	}

	function checkQuantityLimit(quantity){

		var totalQuantity = -1;
		if(quantity != null){
			totalQuantity = parseInt(quantity);
		}

		if( totalQuantity > 999 ){
			if(confirm($("#largeQuantityErrorMsg").val())) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	function isQuantityMore(quantity){

		var totalQuantity = -1;
		if(quantity != null){
			totalQuantity = parseInt(quantity);
		}

		if( totalQuantity > 999 ){
			return true;
		} else {
			return false;
		}
	}

	function toggleSaveSubmit(disabled) {
		var save = document.getElementById("saveRegistration");
		if(save != null){
			save.disabled=disabled;
		}
		var submit = document.getElementById("submitRegistration");
		if(submit != null){
			submit.disabled=disabled;
		}
	}

	function manuallyAddFormSubmit(){
		//Newly Added Code for Submit as per new Architecture
		document.getElementById('recordValidationForm').action	= document.getElementById('manuallyAddAction').href;
		document.getElementById('recordValidationForm').submit();
	}

	function show_validation_msg_desc(materialCode, desc ,matertialExcluded, isTR, type, exclusionSource ) {
			if ( type == 'MCUL' ){
			var materialCodeMcul = document.getElementById($(materialCode).attr('id'));
			var selectMcul = document.getElementById(materialCodeMcul.id + "Select");

			var materialExcludedErrorHidden1 = document.getElementById(materialCodeMcul.id + "MaterialExcludedErrorHidden");
			if(exclusionSource != null){
				if(exclusionSource == 'PLDS'){
					exclusionSource = "Install Base automatically fed from PLDS; move to technical onboarding or manage in PLDS.";//'${bundle.msg.exclusionSourcePLDS}';
				} 	else if(exclusionSource == "NMPC"){
					exclusionSource = "This material code is not eligible for Install Base Creation.";//'${bundle.msg.exclusionSourcePLDS}';
				} else{
					exclusionSource = "Offer/Tracking Codes do not qualify for Services Install Base and thus cannot be registered.";//'${bundle.msg.exclusionSourceKMAT}';
				}
				materialExcludedErrorHidden1.value = exclusionSource;
			}
			show_validation_msg_desc_mc(materialCode, desc, matertialExcluded, selectMcul, exclusionSource );
		}
		materialCode = document.getElementById($(materialCode).attr('id'));
		clearMaterialCodeErrors(materialCode, "true");
		var select = document.getElementById(materialCode.id + "Select");
		if(!materialCode.value) {

			return;

		}
		var materialCodeDesc = document.getElementById(materialCode.id + "Desc");
			materialCodeDesc.innerHTML	= desc;
		var materialCodeDescHidden = document.getElementById(materialCode.id + "DescHidden");
			materialCodeDescHidden.value = desc;
		var materialExclusionHidden = document.getElementById(materialCode.id + "ExclusionHidden");
			materialExclusionHidden.value = matertialExcluded;
		var materialExcludedError = document.getElementById(materialCode.id + "MaterialExcludedError");
		var installBaseReadOnly =	${installBaseCreationReadOnlyFlag};

		//For TR Eligible
		var isTREligible = document.getElementById(materialCode.id + "isTR");
		if(isTR=="Y") {
			isTREligible.value = "Yes";
			document.getElementById(materialCode.id + "isTRdisplay").innerHTML = "Yes";
		} else {
			isTREligible.value = isTR;
			document.getElementById(materialCode.id + "isTRdisplay").innerHTML = isTR;
		}

		if(matertialExcluded == "true"){
			desc = "";
			select.checked =false;
			select.disabled =true;
			materialCode.style.borderColor="#0C0480";
			materialCode.style.borderStyle="solid";
			materialExcludedError.style.display="inline";

		} else if(matertialExcluded){
			desc = "";
			select.checked =false;
			select.disabled =true;
			materialCode.style.borderColor="#0C0480";
			materialCode.style.borderStyle="solid";
			materialExcludedError.style.display="inline";
		} else if(matertialExcluded == "false"){
			if(select.disabled){
				select.checked =true;
			}
			select.disabled =false;
			materialCode.style.borderColor="";
			materialCode.style.borderStyle="";
			materialExcludedError.style.display="none";
		} else if(!matertialExcluded){
			if(select.disabled){
				select.checked =true;
			}
			select.disabled =false;
			materialCode.style.borderColor="";
			materialCode.style.borderStyle="";
			materialExcludedError.style.display="none";
		}
		if(installBaseReadOnly){
			select.disabled =true;
		}
		if(!desc || (matertialExcluded == "true" || matertialExcluded == true) ) {
			clearMaterialCodeErrors(materialCode, "false");
		}
	}

	function show_validation_msg_desc_mc(materialCode, desc, matertialExcluded, select, exclusionSource ){
		var materialCodeError = document.getElementById(materialCode + "Errors");
		var materialExcludedError = document.getElementById( materialCode + "MaterialExcludedError" );

		var description = null;
		if(desc != null){
			description = desc.replace(/^\s+|\s+$/g,'');
		}

		materialCode = document.getElementById( $(materialCode).attr('id') );
		if(matertialExcluded == "true"){
			select.checked =false;
			select.disabled =true;
			materialCode.style.borderColor="#0C0480";
			materialCode.style.borderStyle="solid";
			materialExcludedError.style.display="inline";
			return;
		}else if(matertialExcluded == "false"){
			if(select.disabled){
				select.checked =true;
			}
			select.disabled =false;
			materialCode.style.borderColor="";
			materialCode.style.borderStyle="";
			materialExcludedError.style.display="none";
		}
		var installBaseReadOnly =	${installBaseCreationReadOnlyFlag};
		if(installBaseReadOnly){
			select.disabled =true;
		}

		if((description == null) || (description	== '' )){
			materialCode.style.borderColor="red";
			materialCode.style.borderStyle="solid";
			if(materialCodeError != null) {
				materialCodeError.style.display = "inline";
			}

		} else {
			materialCode.style.borderColor="";
			materialCode.style.borderStyle="";

			if(materialCodeError != null) {
				materialCodeError.style.display = "none";
			}
		}
	}

	function clearMaterialCodeErrors(materialCode, clear) {
		materialCode = $(materialCode);
		var materialCodeID = materialCode.attr('id');
		var materialCodeErrors = $("#"+ materialCodeID + "Errors");
		var materialCodeDesc = $("#"+ materialCodeID + "Desc");
		var isTREligible = $("#"+ materialCodeID +	"isTR");

		var materialExcludedErrorHidden = $("#"+materialCodeID + "MaterialExcludedErrorHidden");
		if(!materialCode.value)
			materialCodeDesc.val('');

		var materialCodeDescHidden =	$("#"+ materialCodeID +	"DescHidden");
		materialCodeDescHidden.value = "";
		var materialExcludedError =	$("#"+ materialCodeID +	"MaterialExcludedError");
		var mcElementForHighlight = document.getElementById(materialCode.attr("id"));
		if(clear == "true") {
			materialCode.css('border-color','');
			materialCode.css('border-style','');
			materialCodeErrors.hide();
			materialExcludedError.hide();
			//Clear Hidden error message field

			$("#"+materialCodeID).parent().parent().find('input:hidden[id^=errorDescription]').attr("value","");
			clearHighlight(mcElementForHighlight);
		}
		else if(clear == "false") {
			var errorDesc = "";
			materialCode.css('border-color','red');
			materialCode.css('border-style','solid');
			if(materialExcludedErrorHidden.text().trim()!="") {
				errorDesc = materialExcludedErrorHidden.text().trim();
				materialExcludedError.show();
			} else {
				materialCodeErrors.show();
				errorDesc = $("#invalidMaterialCodeError").val().replace(":materialCode", materialCode.val());
			}

			$("#"+materialCodeID).parent().parent().find('input:hidden[id^=errorDescription]').attr("value",errorDesc);
			highlightRow(mcElementForHighlight);
		}
	}

	function validate_material_code(materialCode) {
			materialCode = $(materialCode);
			var materialCodeID = materialCode.attr('id');
			var select = $( "#"+materialCodeID + "Select");

			document.getElementById(materialCodeID + "ExistingError").innerHTML="";
			$("#"+materialCodeID + "MaterialExcludedErrorHidden").html("");

			if(!isMaterialCodeExisting(materialCode.attr("value").trim())) {
				show_validation_msg_desc(materialCode, "","","", "MCME", null );
				return;
			}

			if(!materialCode.val()) {
				clearMaterialCodeErrors(materialCode, "false");
				return;
			} else if(!select.is(':checked') ){
				if(!select.is(':disabled')){
					var materialCodeErrors = $(materialCode.id + "Errors");
					var materialExcludedError = $(materialCode.id + "MaterialExcludedError");
					materialCode.css('border-color','');
					materialCode.css('border-style','');
					materialCodeErrors.hide();
					materialExcludedError.hide();
				}
			}
			$.ajax({
				url : _path+'/installbase/json/validateMaterialCode.action?materialCode='+materialCode.val(),
				dataType : 'json',
				success : function( mcArray1 ){
					for(var i=0;i<mcArray1.length;i++){
						var desc = mcArray1[0];
						var materialExcluded = mcArray1[1];
						var isTREligible = mcArray1[2];
						var exclusionSource = mcArray1[3];
						var serialized = mcArray1[4];
					}
					var materialExcludedErrorHidden = $("#"+materialCodeID + "MaterialExcludedErrorHidden");
					if(exclusionSource == 'PLDS'){
						exclusionSource = $("#infIBFedAutomatic").val();
					} else if(exclusionSource == 'KMAT') {
						exclusionSource = $("#errOffrTCNotQual").val();						
					} else if(exclusionSource== "ESNA" || exclusionSource== 'ESNA'){
						exclusionSource = $("#errEsnaCode").val();
					}else if(exclusionSource=="DEFECTIVE" || exclusionSource=="NMPC"){				
						exclusionSource=$("#defectiveCodeErrMsg").val();
					}

					materialExcludedErrorHidden.html( exclusionSource );
					//For Manual Entry isTREligible always No
					isTREligible = "";
					select.checked = true;
					select.disabled = false;
					show_validation_msg_desc(materialCode, desc,materialExcluded,isTREligible, "MCME", null );

					//Validate serial Number and quantity
					var serialNoId = $("#"+materialCodeID).parent().parent().find("input[id^=serialNumber]").attr("id");
					var materialCodeElement = document.getElementById(materialCodeID);

					//Check whether materialCode is serialized
					var serialNumId = "serialNumber" + materialCodeID.replace("materialCode","");
					if(serialized == "Y") {						
						$("#"+materialCodeID+"Serialized").html("Y");						
						document.getElementById( "serializedserialNumber"+materialCodeID.replace("materialCode","") ).value = "Y";
						var remainingQtyId = "remainingQuantity" + materialCodeID.replace("materialCode","");
						var quantity = document.getElementById(remainingQtyId);
						if(quantity != null && isValidQuantity(quantity) && quantity.value == 1) {	
							$("#"+serialNoId).attr("value","");
							$("#" + materialCodeID + "SerialNumSpan").show();
						} else {
							$("#" + materialCodeID + "SerialNumSpan").hide();
							$("#"+serialNoId).attr("value","");
						}

					} else {
						$("#"+materialCodeID+"Serialized").html("N");
						document.getElementById( "serializedserialNumber"+materialCodeID.replace("materialCode","") ).value = "N";
						$("#" + materialCodeID + "SerialNumSpan").hide();
						$("#"+serialNoId).attr("value","");

					}

					validateManualEntry(serialNoId, materialCodeElement);
				}
			});

	}

	function isMaterialCodeSerialized(materialCodeID) {
		var materialCodeElement = $("#"+materialCodeID);
		var materialCode = materialCodeElement.val();
		var serialized = "N";
		var rows = $("#recordValidationTable").dataTable().fnGetNodes();

		for(var i=0; i < rows.length; i++) {
			//Getting nth child
			var mcElement = $(rows[i]).find("td:nth-of-type(7)").find("input");
			if(mcElement.attr("value") == materialCode) {
				var serializedserialNumberID = "serializedserialNumber" + mcElement.attr("id").replace("materialCode","");
				var serializedserialNumber = document.getElementById(serializedserialNumberID);
				if(null != serializedserialNumber && serializedserialNumber.value.trim() =="Y") {
					serialized = "Y";
				}
			}
		}
		if(serialized == "N") {
			return false;
		}
		return true;


	}

	function selectforRegistration(select, materialCode) {
		select = document.getElementById($(select).attr('id'));
		var submitRegistration = document.getElementById("submitRegistration");
		if(select.checked && submitRegistration.disabled) {
			toggleSaveSubmit(false);
		}
		if(!select.checked && !submitRegistration.disabled) {
			if(!anyRecordForRegistration()) {
				toggleSaveSubmit(true);
			}
		}
		if(materialCode) {
			validate_material_code(materialCode);
			validateQuantity(materialCode);
		}
	}



	function validateQuantity(materialCode) {
		materialCode = document.getElementById($(materialCode).attr('id'));
		var select = document.getElementById(materialCode.id + "Select");
		var quantity =	document.getElementById(materialCode.id + "InitialQuantity");
		var serialNumber =	document.getElementById(materialCode.id + "SerialNumber");
		var materialCodeError2 =	document.getElementById(materialCode.id + "Error2");
		var materialCodeError3 =	document.getElementById(materialCode.id + "Error3");
		var serializedQty4 =	document.getElementById(materialCode.id + "Error4");
		var serializedQty5 =	document.getElementById(materialCode.id + "Error5");
		if(!select.checked || !quantity.value) {
			quantity.style.borderColor="";
				quantity.style.borderStyle="";
				materialCodeError2.style.display="none";
				serializedQty5.style.display="none";
				serializedQty4.style.display="none";
				return;
		}
		if(isValidQuantity(quantity)) {
			serializedQty4.style.display="none";
				serializedQty5.style.display="none";
			 	if(quantity.value>1 && isValidSerialNumberNotEmpty(serialNumber)){
					serialNumber.style.borderColor="red";
					serialNumber.style.borderStyle="solid";
					serializedQty5.style.display="inline";
					if(!isValidSerialNumberNotEmpty(serialNumber)){
						serialNumber.style.borderColor="red";
					serialNumber.style.borderStyle="solid";
						materialCodeError3.style.display="inline";
					}
					toggleSaveSubmit(true);
				}else{
				quantity.style.borderColor="";
				quantity.style.borderStyle="";
					materialCodeError2.style.display="none";
					if(isValidSerialNumberNotEmpty(serialNumber)){
					serialNumber.style.borderColor="";
					serialNumber.style.borderStyle="";
					materialCodeError3.style.display="none";
				}
					toggleSaveSubmit(false);
					return;
				}
		}
		else {
			quantity.style.borderColor="red";
			quantity.style.borderStyle="solid";
				materialCodeError2.style.display="inline";
			toggleSaveSubmit(true);
		}
	}

	/*	[AVAYA]: 09-15-2011 Adding function to validate Serial Number (Start) */
	/*	[AVAYA]: 09-15-2011 Adding function to isValidSerialNumber (Start) */
	/*	[AVAYA]: 09-16-2011 Modified function isValidSerialNumber to add regular expression for serial number validation */
	/*	[AVAYA]: 09-16-2011 Modified function isValidSerialNumber to add .value for serialNumber */
	function isValidSerialNumber(serialNumber) {
	var serialNum = serialNumber.value;
	serialNum = serialNum.replace(/^\s+|\s+$/g,'');
	serialNumber.value = serialNum;
	if(serialNum.length == 0 ) {
		return true;
	} else if(serialNum.length <= 18) {
			var regex = /^[a-zA-Z0-9]+$/ ;
		return regex.test(serialNum);
	} else {
		return false;
	}

	}

	function validateSerialNumber(serialNumberID,materialCode) {

		var valid = true;
		var quantity = document.getElementById(materialCode.id + "InitialQuantity");
		var serialNumber = document.getElementById(serialNumberID);
		var materialCodeError3 = document.getElementById(materialCode.id + "Error3");
		var serializedQty4 = document.getElementById(materialCode.id + "Error4");
		var serializedQty5 = document.getElementById(materialCode.id + "Error5");
		var serialNumError6 = document.getElementById(materialCode.id + "Error6");

		if(!serialNumber.value) {
			serialNumber.style.borderColor="";
				serialNumber.style.borderStyle="";
				materialCodeError3.style.display="none";
				serializedQty5.style.display = "none";
				serializedQty4.style.display = "none";

				serialNumError6.style.display="none";
				toggleSaveSubmit(false);
		}
		else if(isValidSerialNumber(serialNumber)) {
				serializedQty4.style.display="none";
				serializedQty5.style.display="none";
				serialNumber.style.borderColor="";
				serialNumber.style.borderStyle="";
				materialCodeError3.style.display="none";
				serialNumError6.style.display="none";
				if(isValidQuantity(quantity)&& quantity.value >1 && isValidSerialNumberNotEmpty(serialNumber)){
		 			quantity.style.borderColor="red";
					quantity.style.borderStyle="solid";
					serializedQty4.style.display="inline";
					toggleSaveSubmit(true);
					valid = false;
				}
				//Check for duplicate serial Number
				else if(isSerialNumberDuplicate(serialNumber)) {
					serialNumber.style.borderColor="red";
					serialNumber.style.borderStyle="solid";
					serialNumError6.style.display="inline";
					toggleSaveSubmit(true);
					valid = false;
				}else{
					serialNumber.style.borderColor="";
					serialNumber.style.borderStyle="";
					materialCodeError3.style.display="none";
					serialNumError6.style.display="none";
					toggleSaveSubmit(false);
				}

		}
		else {
			serialNumber.style.borderColor="red";
			serialNumber.style.borderStyle="solid";
				materialCodeError3.style.display="inline";
					toggleSaveSubmit(true);
					valid = false;
		}
		if(!valid) {
			highlightRow(serialNumber);
		} else {
			clearHighlight(serialNumber);
		}
	}
	function isMaterialCodeExisting(materialCode){
		var rows = $("#recordValidationTable").dataTable().fnGetNodes();

		for(var i=0; i < rows.length; i++) {
			//Getting nth child
			var mc = $(rows[i]).find("td:nth-of-type(7)").find("input").attr("value");
			if(mc == materialCode) {
				return true;
			}
		}
		return false;

	}

	function validateManualEntry(serialNumberID,materialCode) {

		var valid = true;
		var errorDescription = "";
		//Clear serial number mandatory error.
		document.getElementById(serialNumberID + "NewError").innerHTML = "";
		var remainingQtyId = "remainingQuantity" + materialCode.id.replace("materialCode","");
		var quantity = document.getElementById(remainingQtyId);

		var serialNumber = document.getElementById(serialNumberID);
		var materialCodeError3 = document.getElementById(materialCode.id + "Error3");
		var serializedQty4 = document.getElementById(materialCode.id + "Error4");
		var serializedQty5 = document.getElementById(materialCode.id + "Error5");
		var serialNumError6 = document.getElementById(materialCode.id + "Error6");
		//Clear existing errors
		document.getElementById(materialCode.id + "ExistingError").innerHTML="";

		var materialExcludedErrorHidden = $("#"+materialCode.id + "MaterialExcludedErrorHidden");
		var materialExcludedError = document.getElementById(materialCode.id + "MaterialExcludedError");

		var materialCodeError2 =	document.getElementById(materialCode.id + "Error2");
		var materialCodeErrors =	document.getElementById(materialCode.id + "Errors");

		if(materialCodeErrors.style.display != "none") {
			errorDescription = errorDescription + $("#invalidMaterialCodeError").val().replace(":materialCode", document.getElementById(materialCode.id).value);
			valid = false;
		}
		//Check for KMAT Error
		else if(materialExcludedError.style.display != "none" && materialExcludedErrorHidden.text().trim()!="") {
			errorDescription = errorDescription + materialExcludedErrorHidden.text().trim();
			valid = false;
		}

		else if(!isMaterialCodeExisting(document.getElementById(materialCode.id).value.trim()) || $("#"+materialCode.id+"Desc").text().trim() == "") {
			materialCodeErrors.style.display = "inline";

			errorDescription = errorDescription + $("#invalidMaterialCodeError").val().replace(":materialCode", document.getElementById(materialCode.id).value);
			valid = false;
		} else {
			materialCodeErrors.style.display = "none";
			materialExcludedError.style.display = "none";
			materialExcludedErrorHidden.text("");
		}
		
		var serializedIndicator = document.getElementById("serialized"+serialNumber.id).value.trim();
		if(serializedIndicator == "Y" || serializedIndicator == "y") {				
			//Defect#367- commenting as serial nummber is not mandatory
			//If Quantity is 1, enable serial number fieled, else hide it.
			if(quantity != null && isValidQuantity(quantity) && quantity.value == 1) {				
				$("#" + materialCode.id + "SerialNumSpan").show();
			} else {					
				$("#" + materialCode.id + "SerialNumSpan").hide();
				$("#"+serialNumber.id).attr("value","");
			}
		}

		if(!quantity.value) {
			quantity.style.borderColor="";
			quantity.style.borderStyle="";
			materialCodeError2.style.display="none";
			serializedQty5.style.display="none";
			serializedQty4.style.display="none";

			quantity.style.borderColor="red";
			quantity.style.borderStyle="solid";
			materialCodeError2.style.display="inline";
			errorDescription = errorDescription + document.getElementById("invalidQuantityError").value.trim();
			valid = false;
		} else {
				if(isValidQuantity(quantity)) {
					serializedQty4.style.display="none";
					serializedQty5.style.display="none";
					if(quantity.value > 1 && isValidSerialNumberNotEmpty(serialNumber)){
						serialNumber.style.borderColor="red";
						serialNumber.style.borderStyle="solid";
						serializedQty5.style.display="inline";

						errorDescription = errorDescription + document.getElementById("formaterialcodesError").value.trim();

						if(!isValidSerialNumberNotEmpty(serialNumber)){
							serialNumber.style.borderColor="red";
							serialNumber.style.borderStyle="solid";
							materialCodeError3.style.display="inline";

							errorDescription = errorDescription + document.getElementById("invalidSerialNumber18CharError").value.trim();
						}
						valid = false;
					}else{
						quantity.style.borderColor="";
						quantity.style.borderStyle="";
						materialCodeError2.style.display="none";
						if(isValidSerialNumberNotEmpty(serialNumber)){
							serialNumber.style.borderColor="";
							serialNumber.style.borderStyle="";
							materialCodeError3.style.display="none";
						}
					}

				} else {
					quantity.style.borderColor="red";
					quantity.style.borderStyle="solid";
					materialCodeError2.style.display="inline";
					errorDescription = errorDescription + document.getElementById("invalidQuantityError").value.trim();
					valid = false;
				}
		}

		if(!serialNumber.value) {
			serialNumber.style.borderColor="";
				serialNumber.style.borderStyle="";
				materialCodeError3.style.display="none";
				serializedQty5.style.display = "none";
				serializedQty4.style.display = "none";

				serialNumError6.style.display="none";
				toggleSaveSubmit(false);
		}
		else if(isValidSerialNumber(serialNumber)) {
				serializedQty4.style.display="none";
				serializedQty5.style.display="none";
				serialNumber.style.borderColor="";
				serialNumber.style.borderStyle="";
				materialCodeError3.style.display="none";
				serialNumError6.style.display="none";
				if(isValidQuantity(quantity)&& quantity.value >1 && isValidSerialNumberNotEmpty(serialNumber)){
		 			quantity.style.borderColor="red";
					quantity.style.borderStyle="solid";
					serializedQty4.style.display="inline";
					toggleSaveSubmit(true);

					errorDescription = errorDescription + document.getElementById("quantitySizeError").value.trim();
					valid = false;
				}
				//Check for duplicate serial Number
				else if(isSerialNumberDuplicate(serialNumber)) {
					serialNumber.style.borderColor="red";
					serialNumber.style.borderStyle="solid";
					serialNumError6.style.display="inline";
					toggleSaveSubmit(true);
					errorDescription = errorDescription + document.getElementById("invalidSerialNumberError").value.trim();
					valid = false;
				}else{
					serialNumber.style.borderColor="";
					serialNumber.style.borderStyle="";
					materialCodeError3.style.display="none";
					serialNumError6.style.display="none";
					toggleSaveSubmit(false);
				}

		}
		else {
			serialNumber.style.borderColor="red";
			serialNumber.style.borderStyle="solid";
			materialCodeError3.style.display="inline";
			toggleSaveSubmit(true);
			errorDescription = errorDescription + document.getElementById("invalidSerialNumber18CharError").value.trim();
			valid = false;
		}
		if(!valid) {
			//Put the current error messages to a hidden variable
			$("#errorDescription" + serialNumber.id.replace("serialNumber","")).attr("value", errorDescription);
			highlightRow(serialNumber);
		} else {
			$("#errorDescription" + serialNumber.id.replace("serialNumber","")).attr("value", "");
			clearHighlight(serialNumber);
		}
	}

	function highlightRow(coulmnID) {
		var col = $("#"+coulmnID.id);
		var parent = col.parent();
		while(!parent.is("tr")) {
			parent = parent.parent();
		}

		if(parent && parent != null) {
			parent.addClass( 'highlight' );
			var checkBox = parent.find("td:first-child").find("input");
			checkBox.attr("checked",false);
			checkBox.attr("disabled",true);
			$("#recordValidationTable").dataTable().fnAdjustColumnSizing();
			parent.find("i[id^='exclamation']").show();
		}
	}

	function clearHighlight(coulmnID) {
		var col = $("#"+coulmnID.id);
		var parent = col.parent();
		while(!parent.is("tr")) {
			parent = parent.parent();
		}

		if(parent && parent != null) {
			parent.removeClass( 'highlight' );
			var checkBox = parent.find("td:first-child").find("input");
			checkBox.attr("checked",true);
			checkBox.attr("disabled",false);
			parent.find("i[id^='exclamation']").hide();
		}
	}


	function isSerialNumberDuplicate(serialNumber) {
		serialNumber = serialNumber.value.trim();
		var count = 0;
		var rows = $("#recordValidationTable").dataTable().fnGetNodes();
			for(var i=0; i < rows.length; i++) {
			var row = $(rows[i]);

			var existingSerialNo =	row.find("td").find("input[id^='serialNumber']").attr("value");
			if(null != existingSerialNo) {
				existingSerialNo = existingSerialNo.trim();
				if(serialNumber == existingSerialNo) {
					count++;
					if(count > 1){
						return true;
					}
				}

			}
			}
		return false;

	}


	function validateExistingEntry(serialNumber, quantity,techReg,serialize,availableQty) {

		var valid = true;
		var errorDescription = "";
		//Clear existing errors
		document.getElementById(serialNumber.id + "ExistingError").innerHTML="";
		document.getElementById(serialNumber.id + "NewError").innerHTML="";

		if(!quantity.value) {
			//do nothing
		} else {
				if(!isValidQuantityForExixtingRec(quantity)) {
					errorDescription = errorDescription + document.getElementById("invalidQuantityError").value.trim();
					valid = false;

				}
		}
		

		if(serialize.value == "Y" && availableQty.value < quantity.value)
		{
			errorDescription = $("#revisedQtyErrForSerializedAsset").val();
			valid = false;
		}
		else if(techReg.value == "Yes" && quantity.value > 1)
		{
			errorDescription = $("#revisedQtyErrForTechOnBorded").val();
			valid = false;

		} else if(availableQty.value == quantity.value) {
			errorDescription = $("#revisedQutyEqlExistQtyErrMsg").val();
			valid = false;
		}
		else if(!isSerialNumberChanged(serialNumber.id)) {			
			//do nothing
		}
		else if(!serialNumber.value) {
			var serializedIndicator = document.getElementById("serialized"+serialNumber.id).value.trim();
			if(serializedIndicator == "Y" || serializedIndicator == "y") {
				errorDescription = errorDescription + document.getElementById("blankSerialNumberError").value.trim();
				valid = false;
			}
		} else if($("#"+serialNumber.id).attr("type") == "hidden")	{
			//do nothing in case of serial number not editable
		}
		else if(isValidSerialNumber(serialNumber)) {

				if(isValidQuantityForExixtingRec(quantity)&& quantity.value >1 && isValidSerialNumberNotEmpty(serialNumber)){
					errorDescription = errorDescription + document.getElementById("quantitySizeError").value.trim();
					valid = false;
				}
				//Check for duplicate serial Number
				else if(isSerialNumberDuplicate(serialNumber)) {
					errorDescription = errorDescription + document.getElementById("invalidSerialNumberError").value.trim();
					valid = false;
				}else{
					//do nothing
				}

		}
		else {
			errorDescription = errorDescription + document.getElementById("invalidSerialNumber18CharError").value.trim();
			valid = false;
		}
		if(!valid) {
			//Put the current error messages to a hidden variable
			$("#errorDescription" + serialNumber.id.replace("serialNumber","")).attr("value", errorDescription);
			document.getElementById(serialNumber.id + "NewError").innerHTML = errorDescription;
			highlightRow(serialNumber);
		} else {
			$("#errorDescription" + serialNumber.id.replace("serialNumber","")).attr("value", "");
			document.getElementById(serialNumber.id + "NewError").innerHTML = "";
			clearHighlight(serialNumber);
		}
	}



	function serialNumberValidation(materialCode,serialNumberFlag){
		var serialNumber = document.getElementById(materialCode + "SerialNumber");
		var materialCodeError3 = document.getElementById(materialCode + "Error3");
		if(serialNumberFlag ==null || serialNumberFlag==""){
			validSerialNumber(serialNumber,materialCodeError3);
		}
		else{
			invalidSerialNumber(serialNumber,materialCodeError3)
		}
	}

	function isValidQuantity(quantity) {
		var regex = /^\d+$/;
		return regex.test(quantity.value) && quantity.value != 0;
	}
	function isValidQuantityForExixtingRec(quantity) {
		var regex = /^\d+$/;
		return regex.test(quantity.value);
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

	function invalidSerialNumber(serialNumber,materialCodeError3){
	serialNumber.style.borderColor="red";
	serialNumber.style.borderStyle="solid";
	materialCodeError3.style.display="inline";
	toggleSaveSubmit(true);
	}

	function validSerialNumber(serialNumber,materialCodeError3){
	serialNumber.style.borderColor="";
	serialNumber.style.borderStyle="";
	materialCodeError3.style.display="none";
	toggleSaveSubmit(false);
	}

	function sellectAllUnselectAllMeterial(option) {

			var materialEntryListSize = 1;	//EMPTY VALUES
			for (var i = 0; i < materialEntryListSize; i++) {

						 var select1 = document.getElementById("materialCode" + i + "Select");

						 if (!select1.disabled && option.checked) {
									 select1.checked = true;
						 } else {
									 select1.checked = false;
						 }

			}
	}

</script>

<!-- start page content-wrap -->
<div class="content-wrap record-validation">

	<h1 class="page-title">Record Validation</h1>

	<!-- start page content -->
	<div class="content">

		<div id="validatedStatusMsg" class="page-note">
			<p>
				<span>${grtConfig.validatedStatusMsg}</span>
			</p>
		</div>


		<!-- start page data loader for defect#100-->
		<div id="loading-overlay-rec-val" class="loading-overlay" style="display: block;">
			<p id="loading-msg" class="loading-msg">Please wait while we process your request...</p>
			<img
				src="<c:url context="${ _path }" value="/styles/images/loaders/loader.gif" />"
				alt="loading" />
		</div>
		<!-- end page data loader -->

		<!-- start registration summary -->
		<%@ include file="../installbase/grtBanner.jsp" %>
		<!-- end registration summary -->


		<!-- start record validation wrap -->
		<div id="rv-wrap" class="data-table-wrap collapse-box">
			<h2 class="collapse-box-header active">Record Validation
				<a href="#">
					<span></span>
				</a>
			</h2>
			<div id="productListTableDiv" class="data collapse-box-container">

				<!-- start recordValidationForm -->
				<s:form id="recordValidationForm" enctype="multipart/form-data"	method="post">
					<s:token></s:token>
					<input type="hidden"	id="registrationId" name="actionForm.registrationId" value="${ fn:escapeXml(actionForm.registrationId) }"/>
					<input type="hidden"	id="soldToId" name="actionForm.soldToId" value="${ fn:escapeXml(actionForm.soldToId) }"/>
					<input type="hidden"	id="salMigrationOnly" name="actionForm.salMigrationOnly"	value="${ actionForm.salMigrationOnly }"/>
					<input type="hidden"	id="technicalOrderId" name="actionForm.technicalOrderId"	value="${ actionForm.technicalOrderId }"/>
					<input type="hidden"	id="saveType" name="actionForm.saveType"	value="${ fn:escapeXml(actionForm.saveType) }"/>
					<input type="hidden"	id="validateEnabled" value=""/>
					<input type="hidden"	id="eqrMessage" name="actionForm.message" value="${ actionForm.message }"/>
					<input type="hidden"	id="eqrReturnCode" name="actionForm.returnCode" value="${ actionForm.returnCode }"/>
					<input type="hidden"	id="sortBy" name="actionForm.sortBy" value="${ fn:escapeXml(actionForm.sortBy) }"/>
					<input type="hidden"	id="sortByCount" name="actionForm.sortByCount"	value="${ actionForm.sortByCount }"/>
					<input type="hidden"	id="sortByScreen" name="actionForm.sortByScreen"	value="${ actionForm.sortByScreen }"/>
					<input type="hidden"	id="formReadOnly" value="${actionForm.readOnly}"/>


					<a id="downloadfile" href="<c:url context="${ _path }" value="/recordvalidation/downloadfile" />"></a>
					<a id="registrationListAction" href="<c:url context="${ _path }" value="/recordvalidation/toRegistrationList.action" />"></a>
					<a id="dynamicSortByFieldAction" href="<c:url context="${ _path }" value="/viewinstallbase/dynamicSortByField.action" />" onclick="anchor_submit_form('viewInstallBaseForm','<c:url context="${ _path }" value="/viewinstallbase/dynamicSortByField.action" />');return false;"></a>
					<c:set var="readOnly" value="${installBaseCreationReadOnlyFlag}"/>
					<c:set var="downloadEQRFile" value="${actionForm.eqrFileDownloaded}"/>
					<c:set var="downloadEQRFileTemplate" value="${actionForm.eqrFileTemplateDownloaded}"/>
					<%
						boolean download = ((Boolean)pageContext.getAttribute("downloadEQRFile")).booleanValue();
						boolean downloadTemplate = ((Boolean)pageContext.getAttribute("downloadEQRFileTemplate")).booleanValue();
						if (download) {
					%>
						<script>
							window.open('<%=request.getContextPath()%>/files/RecordValidation_${actionForm.soldToId}.xls','download', 'height=100, width=100, scrollbars=no');
						</script>
					<%
						} else if(downloadTemplate) {
					%>
						<script>
							window.open('<%=request.getContextPath()%>/files/RecordValidationTemplate.xls','download', 'height=100, width=100, scrollbars=no');
						</script>
					<%
						}
					%>
					<%
					 	boolean readOnly = ((Boolean)pageContext.getAttribute("readOnly")).booleanValue();
					%>

					<!-- start record validation table -->
					<table border="0" cellpadding="5" style="overflow-y:hidden;" id="recordValidationTable" align="center">
						<thead>
							<tr>
								<th width="5%">Select</th>
								<th width="6%">Existing Qty&nbsp;<span id="initialQuantitySort"></span></th>
								<th width="6%">Updated Qty&nbsp;<span id="remainingQuantitySort"></span></th>
								<th width="7%">Qty Added/Removed&nbsp;<span id="removedQuantitySort"></span></th>
								<th width="7%">Contract?&nbsp;<span id="activeContractExistSort"></span></th>
								<th width="7%">TOB'ed?&nbsp;<span id="technicallyRegisterableSort"></span></th>
								<th width="8%">Material Code&nbsp;<span id="materialCodeSort"></span></th>
								<th width="18%">Material Code Description&nbsp;<span id="descSort"></span></th>
								<th width="12%">Product Line&nbsp;<span id="productLineSort"></span></th>
								<th width="6%">SE Code&nbsp;<span id="solutionElementCodeSort"></span></th>
								<th width="10%">SEID&nbsp;<span id="solutionElementIdSort"></span></th>
								<th width="10%">Serialized?&nbsp;<span id="serializedSort"></span></th>
								<th width="10%">Serial Number&nbsp;<span id="serialNumberSort"></span></th>
								<th width="10%">Asset Nickname&nbsp;<span id="assetNicknameSort"></span></th>
								<th width="8%"> Warning&nbsp;<span id="errorDescSort"></span></th>
							</tr>
						</thead>
						<tbody>
							<% int varIndex = 0; %>
							<c:forEach items="${ actionForm.manuallyAddedMaterialEntryList }" var="item" varStatus="container">
							<tr highlight="${item.errorFlag}">
								<c:set var="baseUnit" value="${itemObj.isBaseUnit}"/>
								<c:set var="materialExc" value="${itemObj.materialExclusion}"/>
								<c:set var="mcExclusionSource" value="${itemObj.exclusionSource}"/>
								<c:set var="materialCode" value="${itemObj.materialCode}"/>
								<%
								//TechnicalOrderDetail item = (TechnicalOrderDetail) pageContext.getAttribute("itemObj");
								String currentItem = (String) pageContext.getAttribute("baseUnit");
								if(currentItem!=null &&currentItem.equalsIgnoreCase("y")){
								%>
								<td align="center">
									<input type="checkbox" id="deleted${container.index}" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].deleted" value="true" onClick="javascript:clearValueOnUncheck(this, ${container.index});" ${(item.exclusionFlag) or (not empty item.errorDescription) ? 'disabled' : ''}/>
									<input type="hidden" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].deleted" value="false"/>															
								</td>
								<td align="center" >
									<input type="hidden" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].orderId" id="materialCode${container.index}orderId" value="${ item.orderId }"/>
									<input type="text" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].materialCode" id="materialCode${container.index}"	value="${ item.materialCode }" onChange="validate_material_code(this)" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} maxlength="18"	onKeyPress="return noEnter(event);" />
								</td>
								<td align="center">
									<input type="text" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].initialQuantity" id="materialCode${container.index}InitialQuantity" value="${ item.initialQuantity }"	size="14" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} onChange="validateQuantity(materialCode${container.index})" maxlength="13"	onKeyPress="return noEnter(event);" />
								</td>
								<td align="center">
									<input type="text" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].serialNumber" id="materialCode${container.index}SerialNumber" value="${ item.serialNumber }"	${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} onChange="validateSerialNumber(materialCode${container.index});"	onKeyPress="return noEnter(event);"	/>
								</td>
								<td align="center">
									<span id="materialCode${container.index}Desc">
										<label>${item.description}"</label>
									</span>
									<input type="hidden" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].description" id="materialCode${container.index}DescHidden" value="${ item.description }"/>
									<input type="hidden" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].materialExclusion" id="materialCode${container.index}ExclusionHidden" value="${ item.materialExclusion }"/>
									<input type="hidden" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].isBaseUnit" id="materialCode${container.index}baseunit" value="${ item.isBaseUnit }"/>
									<input type="hidden" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].isSourceIPO" id="materialCode${container.index}sourceipo" value="${ item.isSourceIPO }"/>
									<input type="hidden" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].serialValidateFlag" id="materialCode${container.index}serialValidateFlag" value="${ item.serialValidateFlag }"/>
								</td>
								<td align="center">
									<span id="materialCode${container.index}isTR">
										<label>${item.technicallyRegisterable}"</label>
									</span>
								 	<input type="hidden" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].technicallyRegisterable" id="materialCode${container.index}isTRHidden" value="${ item.technicallyRegisterable }" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}/>
								</td>
								<% if("No".equalsIgnoreCase((String)pageContext.getAttribute("ipoInventoryNotLoaded"))) { %>
								<td align="center">
									<input type="checkbox" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].sourceipo" id="materialCode${container.index}sourceipo" value="${item.ipoFlagIbaseJsp}" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}	 onKeyPress="return noEnter(event);"	/>
								</td>
								<% } %>
								<td align="center">
									<%if(readOnly){ %>
									<input type="checkbox" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].selectforRegistration" ${(item.selectforRegistration) ? 'checked' : ''} value="${ item.selectforRegistration }" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} />
									<% } else {%>
									<input type="checkbox" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].selectforRegistration" id="materialCode${container.index}Select" ${(item.selectforRegistration) ? 'checked' : ''} value="${ item.selectforRegistration }" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} onChange="selectforRegistration(this, materialCode${container.index})"	onKeyPress="return noEnter(event);" />
									<% }%>
								</td>
								<td align="left">${item.nickName} </td>
								<%if("1003".equals(pageContext.getAttribute("ibaseStatus"))) { %>
								<td class="erroWrap" >${item.errorDescription}</td>
								<%}else{ %>
								<td align="center" class="erroWrap" >
									<i id="exclamation${container.index}" style="display:none" class="fa fa-exclamation-triangle"></i>
									<span id="materialCode${container.index}ErrorsMaterialCode">
									</span>
									<span id="materialCode${container.index}Errors" style="display:none;">
										&nbsp;${grtConfig.RVInvalidMC}
									</span>
									<span id="materialCode${container.index}Error2" style="display:none;">
										${grtConfig.invalidQuantity}
									</span>
									<span id="materialCode${container.index}Error3" style="display:none;">
										${grtConfig.invalidSerialNumber18Char}
									</span>
									<span id="materialCode${container.index}Error4" style="display:none;">
										${grtConfig.quantitySize}
									</span>
									<span id="materialCode${container.index}Error5" style="display:none;">
										${grtConfig.formaterialcodes}
									</span>
									<span id="materialCode${container.index}Error6" style="display:none;">
										${grtConfig.invalidSerialNumber}
									</span>
									<span id="materialCode${container.index}MaterialExcludedError" style="display:none; color:#0C0480">
										${grtConfig.materialCodeExcluded}
									</span>
								</td>
								<% if(("Yes".equalsIgnoreCase((String)pageContext.getAttribute("ipoInventoryNotLoaded"))) && ("Yes".equalsIgnoreCase((String)pageContext.getAttribute("remoteconn")))) { %>
								<td align="center">
									<input id="selectedRecord" value="${item.coreUnitSelected}"/>
									<%
									String selectedValue = (String) pageContext.getAttribute("selectedRecord");
									if (null != selectedValue && selectedValue.equals("TRUE")){
									%>
									<input name="materialEntry" onclick="return getMaterialVersionMethod(${container.index});" checked ="true" type="radio" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}	onKeyPress="return noEnter(event);"	/>
									<%}else{ %>
									<input name="materialEntry" onclick="return getMaterialVersionMethod(${container.index});"	type="radio" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}	onKeyPress="return noEnter(event);" />
									<%}%>
								</td>
								<td align="center">
									<select id="materialEntryVersion${container.index}" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}	onKeyPress="return noEnter(event);" >
										<option value="" >Choose one</option>
										<c:forEach items="item.materialEntryVersion" var="materialVal">
										<c:if test="${ item.releaseString == materialVal }">
										<option selected value="${materialVal}">${materialVal}</option>
										</c:if>
										<c:if test="${ item.releaseString != materialVal }">
										<option value="${materialVal}">${materialVal}</option>
										</c:if>
										</c:forEach>
									</select>
								</td>
								<% } } %>
								<%}else{ %>
								<td align="center">
									<input type="checkbox" id="deleted${container.index}" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].deleted" ${(item.deleted) ? 'checked' : ''} value="true" onClick="javascript:clearValueOnUncheck(this, ${container.index});" ${(item.exclusionFlag) or (not empty item.errorDescription) ? 'disabled' : ''}/>
									<input type="hidden" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].deleted" value="false"/>
								</td>
								<td align="center">
									<label>NA</label>
									<input name="actionForm.manuallyAddedMaterialEntryList[${container.index}].initialQuantity" type="hidden" id="materialCode${container.index}InitialQuantity" value="0" size="5" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} onChange="validateManualEntry('serialNumber'+${container.index},materialCode${container.index});" maxlength="13"	onKeyPress="return noEnter(event);"	/>
								</td>
								<!-- Revised installed Quantity -->
								<td title="Enter Revised Installed Quantity" align="center">
									<input type="text" id="remainingQuantity${container.index}" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].remainingQuantity" value="${item.remainingQuantity}" size="5" onBlur="validateManualEntry('serialNumber'+${container.index},materialCode${container.index}); changeValues(${container.index});" onKeyUp="javascript:numericFilter(this);" ${(item.exclusionFlag) or (item.errorDescription) ? 'disabled' : ''}/>
								</td>
								<!-- Quantity to be added or removed -->
								<td align="center">
									<span id="quantityRemoved${container.index}"><c:set var="signNeg" value="${item.removedQuantity != null  && item.removedQuantity != 0 ? '-' : ''}"/>${item.remainingQuantity > 0 ? '+' : signNeg}${item.removedQuantity}</span>
									<input type="hidden" id="hidQuantityRemoved${container.index}" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].removedQuantity" value="${item.removedQuantity}"/>
								</td>
								<!-- Active Contract List -->
								<td align="center">
									<input type="hidden" id="activeContract${container.index}" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].activeContractExist" value=""/>
								</td>
								<!-- Technical On Boarding Select Box -->
								<td align="center">
									<span id="materialCode${container.index}isTRdisplay">
										${item.technicallyRegisterable}
									</span>
									<input type="hidden" id="materialCode${container.index}isTR" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].technicallyRegisterable" value="${item.technicallyRegisterable}"/>
									<span style="display:none">
										<%if(readOnly){ %>
										<input value="true" type="checkbox" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].selectforRegistration" ${(item.selectforRegistration) ? 'checked' : ''} value="${ item.selectforRegistration }" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} />
										<% } else {%>
										<input value="true" type="checkbox" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].selectforRegistration" ${(item.selectforRegistration) ? 'checked' : ''} id="materialCode${container.index}Select" value="${ item.selectforRegistration }" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} onChange="selectforRegistration(this, materialCode${container.index})"	onKeyPress="return noEnter(event);" />
										<%} %>
									<span>
								</td>
								<!-- Material Code entry-->
								<td nowrap align="center">
									<input type="hidden" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].orderId" id="materialCode${container.index}orderId" value="${ item.orderId }" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}/>
									<input type="text" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].materialCode" id="materialCode${container.index}" value="${ item.materialCode }" onChange="validate_material_code(this)"
									${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} maxlength="18"	onKeyPress="return noEnter(event);" />
									<%if(!readOnly) {%>
									<input type="button" type="button" value="Lookup" class="button gray small"
									onclick = "openSearchPopUp('materialCode'+${container.index} , 'materialCode'+${container.index}+'Desc',''+${container.index})"/>
									<%}%>
								</td>
								<!-- Description -->
								<td align="center">
									<span id="materialCode${container.index}Desc">
										<label>${item.description}</label>
									</span>
									<input type="hidden" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].description" id="materialCode${container.index}DescHidden" value="${ item.description }"/>
									<input type="hidden" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].materialExclusion" id="materialCode${container.index}ExclusionHidden" value="${ item.materialExclusion }"/>
									<input type="hidden" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].isBaseUnit" id="materialCode${container.index}baseunit" value="${ item.isBaseUnit }"/>
									<input type="hidden" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].isSourceIPO" id="materialCode${container.index}sourceipo" value="${ item.isSourceIPO }"/>
									<input type="hidden" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].serialValidateFlag" id="materialCode${container.index}serialValidateFlag" value="${ item.serialValidateFlag }"/>
								</td>
								<!-- Product Line, SE Code, SEID, Serialized -->
								<td align="left">${item.productLine} </td>
								<td align="left">${item.solutionElementCode} </td>
								<td align="left">${item.solutionElementId} </td>
								<td align="left">
									<span id="materialCode${container.index}Serialized">${item.serialized} </span>
									<input type="hidden" id="serializedserialNumber${container.index}" value="${item.serialized}" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].serialized" />
								</td>
								<!--	Serial Number -->
								<td align="center">
									<span id="materialCode${container.index}SerialNumSpan" style="display:${item.serialized =='Y' && item.initialQuantity == 1 ? 'block' : 'none' }">
										<input type="text" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].serialNumber" id="serialNumber${container.index}" value="${ item.serialNumber }" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''} onChange="validateManualEntry('serialNumber'+${container.index},materialCode${container.index});"	onKeyPress="return noEnter(event);" />
									<span>
								</td>
								<td align="left">${item.nickName}

								</td>
								<%if("1003".equals(pageContext.getAttribute("ibaseStatus"))) { %>
								<td class="erroWrap" >${item.errorDescription}</td>
								<%}else{ %>
								<td class="erroWrap" >
									<i id="exclamation${container.index}" style="display:none" class="fa fa-exclamation-triangle"></i>
									<span id="materialCode${container.index}ExistingError">										
										<c:choose>
										    <c:when test="${item.excludedMaestroOrNortel}">
										       <a target="_blank" style="color: #FFC600; text-decoration: none;" href='https://support.avaya.com/ext/index?page=content&id=FAQ107254'>		
													${grtConfig.rvNortelMaestroWithContractErrMsg}  For more information, click here.
												</a>
										    </c:when>    
										    <c:otherwise>
										        ${item.errorDescription} 
										    </c:otherwise>
										</c:choose>										
										
									</span>
									<span id="serialNumber${container.index}NewError">
									</span>
									<span id="materialCode${container.index}Errors" style="display:none;">
										&nbsp;${grtConfig.RVInvalidMC};
									</span>
									<span id="materialCode${container.index}Error2" style="display:none;">
										${grtConfig.invalidQuantity}
									</span>
									<span id="materialCode${container.index}Error3" style="display:none;">
										${grtConfig.invalidSerialNumber18Char}
									</span>
									<span id="materialCode${container.index}Error4" style="display:none;">
										${grtConfig.quantitySize}
									</span>
									<span id="materialCode${container.index}Error5" style="display:none;">
										${grtConfig.formaterialcodes}
									</span>
									<span id="materialCode${container.index}Error6" style="display:none;">
										${grtConfig.invalidSerialNumber}
									</span>
									<span id="materialCode${container.index}MaterialExcludedError" style="display:none;">
										<span id="materialCode${container.index}MaterialExcludedErrorHidden">
										<% if("PLDS".equalsIgnoreCase((String)pageContext.getAttribute("mcExclusionSource"))) { %>
												${grtConfig.prepareTechnicalOrderMaterialExclusionError1}
										<% } else if("NMPC".equalsIgnoreCase((String)pageContext.getAttribute("mcExclusionSource"))) { %>
												${grtConfig.defectiveCodeErrMsg}
										<% } else if("KMAT".equalsIgnoreCase((String)pageContext.getAttribute("mcExclusionSource"))) { %>
												${grtConfig.prepareTechnicalOrderMaterialExclusionError2}
										<% } %>
										</span>
									</span>
									
									<c:choose>
										<c:when test="${item.excludedMaestroOrNortel}">
											<input type="hidden" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].errorDescription" id="errorDescription${container.index}" value="${grtConfig.rvNortelMaestroWithContractErrMsg}" />											
										</c:when>    
									   <c:otherwise>
										    <input type="hidden" name="actionForm.manuallyAddedMaterialEntryList[${container.index}].errorDescription" id="errorDescription${container.index}" value="${ item.errorDescription }" />
									   </c:otherwise>
									</c:choose>		
									
								</td>
								<% if(("Yes".equalsIgnoreCase((String)pageContext.getAttribute("ipoInventoryNotLoaded"))) && ("Yes".equalsIgnoreCase((String)pageContext.getAttribute("remoteconn")))) { %>
								<td align="center">
									<input id="selRecord" value="${item.coreUnitSelected}"/>
									<%
									String selValue = (String) pageContext.getAttribute("selRecord");
									if (null != selValue && selValue.equals("TRUE")){
									%>
									<input name="materialEntry"	 onclick="return getMaterialVersionMethod(${container.index});" checked ="true" type="radio"	onKeyPress="return noEnter(event);" />
									<%}else{ %>
									<input name="materialEntry"	 onclick="return getMaterialVersionMethod(${container.index});" type="radio"	onKeyPress="return noEnter(event);" />
									<%}%>
								</td>
								<td align="center">
									<select id="materialEntryVersion${container.index}" ${(installBaseCreationReadOnlyFlag) ? 'disabled' : ''}	onKeyPress="return noEnter(event);" >
										<option value="" >Choose one</option>
										<%-- <netui-data:repeater value="container.item.materialEntryVersion">
										<netui-data:repeaterItem>
										<option value="${container.item}">${container.item}</option>
										</netui-data:repeaterItem>
										</netui-data:repeater>	--%>
										<c:forEach items="item.materialEntryVersion" var="value">
										<c:if test="${ item.releaseString == value }">
										<option selected value="${value}">${value}</option>
										</c:if>
										<c:if test="${ item.releaseString != value }">
										<option value="${value}">${value}</option>
										</c:if>
										</c:forEach>
									</select>
								</td>
								<% } } %>
								<%} %>
							</tr>
							<% varIndex++; %>
							</c:forEach>
							<% pageContext.setAttribute("varIndex", varIndex); %>
							<c:forEach items="${actionForm.materialEntryList}" var="item" varStatus="container">
							<tr title="Pipeline IB Quantity:${item.pipelineIBQuantity} Pipeline EQR Quantity:${item.pipelineEQRQuantity}" highlight="${item.errorFlag}">
								<td align="center">
									<!--${container.index+varIndex} -->
									<input type="checkbox" id="deleted${container.index+varIndex}" name="actionForm.materialEntryList[${container.index}].deleted" value="true" ${(item.deleted) ? 'checked' : ''} onClick="javascript:clearValueOnUncheck(this, ${container.index+varIndex});" ${(item.exclusionFlag) or (not empty item.errorDescription) ? 'disabled' : ''}/>
									<input type="hidden" name="actionForm.materialEntryList[${container.index}].deleted" value="false"/>
								</td>
								<td align="center">
									${item.initialQuantity}
								</td>
									<!-- moved out of above td as part of sort fix for initial quantity start-->
									<input type="hidden" id="availableQuantity${container.index+varIndex}" name="actionForm.materialEntryList[${container.index}].initialQuantity" value="${item.initialQuantity }"/>
									<label id="lblavailableQuantity${container.index+varIndex}" class="hiddenLable" onchange ="setValueInLabel(${container.index+varIndex},this.value,'Existing')" >${item.materialCode}</label>
									<!-- moved out of above td as part of sort fix for initial quantity end -->
								<td title="Enter Revised Installed Quantity" align="center">
									<span class="hiddenLable" id="revisedQty${container.index+varIndex}">${item.remainingQuantity}</span>
									<input type="text" id="remainingQuantity${container.index+varIndex}"	onchange ="setValueInLabel(${container.index+varIndex},this.value,'Quantity')" name="actionForm.materialEntryList[${container.index}].remainingQuantity" value="${item.remainingQuantity}" size="5" onBlur="validateExistingEntry(serialNumber${container.index+varIndex},remainingQuantity${container.index+varIndex},techReg${container.index+varIndex},serializedserialNumber${container.index+varIndex},availableQuantity${container.index+varIndex}); changeValues(${container.index+varIndex});" onKeyUp="javascript:numericFilter(this);" ${(item.exclusionFlag) or (not empty item.errorDescription) ? 'disabled' : ''}/>
									<label id="lblremainingQuantity${container.index+varIndex}" class="hiddenLable" >${item.remainingQuantity}</label>

								</td>
								<td align="center">
									<span id="quantityRemoved${container.index+varIndex}"><c:set var="signNeg" value="${item.removedQuantity != null && item.removedQuantity != 0 ? '-' : ''}"/>${item.remainingQuantity >= item.initialQuantity && item.removedQuantity != 0 ? '+' : signNeg}${item.removedQuantity} </span>
									<input type="hidden" id="hidQuantityRemoved${container.index+varIndex}" name="actionForm.materialEntryList[${container.index}].removedQuantity" value="${item.removedQuantity}"/>
								</td>
								<td align="center">
									${item.activeContractExist}
									<input type="hidden" id="activeContract${container.index+varIndex}" name="actionForm.materialEntryList[${container.index}].activeContractExist" value="${item.activeContractExist}"/>
								</td>
								<td align="center">
									${item.technicallyRegisterable}
									<input type="hidden" id="techReg${container.index+varIndex}" name="actionForm.materialEntryList[${container.index}].technicallyRegisterable" value="${item.technicallyRegisterable}"/>
								</td>
								<td align="left"> ${item.materialCode}
									<label id="lblmaterialCode${container.index+varIndex}" class="hiddenLable" onchange ="setValueInLabel(${container.index+varIndex},this.value,'Material')" >${item.materialCode}</label>
									<input type="hidden" id="materialCode${container.index+varIndex}" name="actionForm.materialEntryList[${container.index}].materialCode" value="${item.materialCode}">
								</td>
								<td align="left" title="${item.description}"> ${item.description} </td>
								<td align="left"> ${item.productLine} </td>
								<td align="left"> ${item.solutionElementCode} </td>
								<td align="left"> ${item.solutionElementId} </td>
								<td align="left"> ${item.serialized}
									<input type="hidden" id="serializedserialNumber${container.index+varIndex}" name="actionForm.materialEntryList[${container.index}].serialized" value="${item.serialized}"/>
								</td>
								<c:choose>
								<c:when test="${item.initialQuantity == 1 && item.serialized != 'N'}">
								<td align="left">
									<input type="text" id="serialNumber${container.index+varIndex}" name="actionForm.materialEntryList[${container.index}].serialNumber" value="${item.serialNumber}" onchange ="setValueInLabel(${container.index+varIndex},this.value,'Serial')"  onblur="validateExistingEntry(serialNumber${container.index+varIndex},remainingQuantity${container.index+varIndex},techReg${container.index+varIndex},serializedserialNumber${container.index+varIndex},availableQuantity${container.index+varIndex}); validateButtonStatus();"	${(item.exclusionFlag) or (item.errorDescription) ? 'disabled' : ''}	/>
									<label id="lblhidSerialNumber${container.index+varIndex}" class="hiddenLable"	>${item.serialNumber}</label>
									<input type="hidden" id="hidSerialNumber${container.index+varIndex}" value="${item.originalSerialNumber}">
									<input type="hidden" id="newSerialNumber${container.index+varIndex}" name="actionForm.materialEntryList[${container.index}].newSerialNumber" value="">
								</td>
								</c:when>
								<c:otherwise>
								<td align="left"> ${item.serialNumber}
									<input type="hidden" id="serialNumber${container.index+varIndex}" name="actionForm.materialEntryList[${container.index}].serialNumber" value="${item.serialNumber}"/>
								</td>
								</c:otherwise>
								</c:choose>
								<td align="left">${item.nickName} </td>
								<td align="left" class="erroWrap" >
									<i id="exclamation${container.index+varIndex}" style="display:none" class="fa fa-exclamation-triangle"></i>
									<span id="serialNumber${container.index+varIndex}ExistingError">
										<c:choose>
										    <c:when test="${item.excludedMaestroOrNortel}">
										       <a target="_blank" style="color: #FFC600; text-decoration: none;" href='https://support.avaya.com/ext/index?page=content&id=FAQ107254'>		
													${grtConfig.rvNortelMaestroWithContractErrMsg}  For more information, click here.
												</a>
										    </c:when>    
										    <c:otherwise>
										        ${item.errorDescription}
										    </c:otherwise>
										</c:choose>
									</span>
									<span id="serialNumber${container.index+varIndex}NewError">
									</span>
									<c:choose>
										<c:when test="${item.excludedMaestroOrNortel}">
											<input type="hidden" id="errorDescription${container.index+varIndex}" name="actionForm.materialEntryList[${container.index}].errorDescription" value="${grtConfig.rvNortelMaestroWithContractErrMsg}" />
										</c:when>    
									   <c:otherwise>
										    <input type="hidden" id="errorDescription${container.index+varIndex}" name="actionForm.materialEntryList[${container.index}].errorDescription" value="${item.errorDescription}" />
									   </c:otherwise>
									</c:choose>		
								</td>
							</tr>
							</c:forEach>
						<tbody>
					</table>
					<!-- end record validation table -->

					<!-- start manual entry btns -->
					<div class="controls manual-entry-btns" id="manulaEntryDiv">
						<span class="left-col">
							<input type="checkbox" id="selectAll" value="actionForm.selectAndUnselectAllMeterial" onClick="javascript:selectNunselect(this);" />
							<span>Select/Unselect All Records</span>
						</span>
						<span class="right-col">

							Upload &nbsp;&nbsp;
							<input type="file" name="actionForm.theFile" id="uploadFile" ${(actionForm.readOnly) ? 'disabled' : ''}/>
							<input type="button" id="uploadBtn" value="Upload Record Validation information" class="button gray" onclick="return download_template_upload_file('uploadFile');" ${(actionForm.readOnly) ? 'disabled' : ''} />
							<input type="button" value="Manual Entry" id="addBtn" class="button gray" onclick="manuallyAddFormSubmit()" ${(actionForm.readOnly) ? 'disabled' : ''} />
							<a href="<c:url context="${ _path }" value="/recordvalidation/addMaterialEntry"/>" id="manuallyAddAction" ></a>
							<a href="<c:url context="${ _path }" value="/recordvalidation/uploadFile"/>" id="uploadAction" ></a>
							<div id="file.container">&nbsp;</div>
							<div id="filediv" style="display:none;">
								<a href="" id="file_a">uploaded file</a>
							</div>

						</span>
					</div>
					<!-- end manual entry btns -->

				</s:form>
				<!-- end recordValidationForm -->
				<script>
					jQuery(document).ready(function($) {
						// move entry buttons into table area
						$('#recordValidationTable_wrapper .dataTables_scroll').append($('.manual-entry-btns'));
					});
				</script>

			</div>

		</div>
		<!-- end record validation wrap -->

		<!-- start recordValidationSummary wrap -->
		<div>
			<!-- start summary-table-wrap -->
			<div id="summary-table-wrap" class="data-table-wrap collapse-box">
				<h2 class="collapse-box-header ">Record Validation summary
					<a href="#">
						<span></span>
					</a>
				</h2>
				<div id="" class="data collapse-box-container">
					<table border="0" cellpadding="5" id="recordValidationSummaryTable" align="center">
						<thead>
							<tr>
								<th width="7%">Qty&nbsp;<span id="technicallyRegisterableSort"></span></th>
								<th width="6%">Material Code&nbsp;<span id="initialQuantitySort"></span></th>
								<th width="7%">Material Code Description&nbsp;<span id="activeContractExistSort"></span></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${actionForm.summaryMap}" var="tods" varStatus="container">
							<tr>
								<td><a>${tods.key.quantity}</a></td>
								<td><a>${tods.key.materialCode}</a></td>
								<td><a>${tods.key.description}</a></td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<!-- end summary-table-wrap -->

			<div id="summaryDetailedView"></div>

			<!-- start hiddensummaryDetails -->
			<div id="hiddensummaryDetails" style="display:none">
				<c:forEach items="${actionForm.summaryMap}" var="tods" varStatus="container">
				<!-- start summary-table-wrap with ID -->
				<div id="summary-table-wrap-${tods.key.materialCode}" class="unique-tables data-table-wrap collapse-box">
					<h2 class="collapse-box-header">Record Validation Summary Details - ${tods.key.materialCode}
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div id="" class="data collapse-box-container" >
						<table id="summaryTable${tods.key.materialCode}" >
							<thead>
								<tr>
									<th width="6%">Existing Qty&nbsp;<span id="initialQuantitySort"></span></th>
									<th width="7%">Contract?&nbsp;<span id="activeContractExistSort"></span></th>
									<th width="7%">TOB'ed?&nbsp;<span id="technicallyRegisterableSort"></span></th>
									<th width="8%">Material Code&nbsp;<span id="materialCodeSort"></span></th>
									<th width="18%">Material Code Description&nbsp;<span id="descSort"></span></th>
									<th width="12%">Product Line&nbsp;<span id="productLineSort"></span></th>
									<th width="6%">SE Code&nbsp;<span id="solutionElementCodeSort"></span></th>
									<th width="10%">SEID&nbsp;<span id="solutionElementIdSort"></span></th>
									<th width="10%">Serialized?&nbsp;<span id="serializedSort"></span></th>
									<th width="10%">Serial Number&nbsp;<span id="serialNumberSort"></span></th>
									<th width="10%">Asset Nickname&nbsp;<span id="assetNicknameSort"></span></th>
									<th width="8%">Warning&nbsp;<span id="errorDescSort"></span></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${tods.value}" var="item" varStatus="container">
								<tr>
									<td align="center"> ${item.initialQuantity} </td>
									<td align="center"> ${item.activeContractExist} </td>
									<td align="center"> ${item.technicallyRegisterable} </td>
									<td align="left"> ${item.materialCode} </td>
									<td align="left" title="${item.description}"> ${item.description} </td>
									<td align="left"> ${item.productLine} </td>
									<td align="left"> ${item.solutionElementCode} </td>
									<td align="left"> ${item.solutionElementId} </td>
									<td align="left"> ${item.serialized} </td>
									<td align="left"> ${item.serialNumber} </td>
									<td align="left"> ${item.nickName} </td>
									<td align="left"> ${item.errorDescription} </td>
								</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
				<!-- end summary-table-wrap with ID -->
				</c:forEach>
			</div>
			<!-- end hiddensummaryDetails -->
		</div>

		<script>
			// script for recordValidationSummary tables
			$(document).ready(function() {
				$("#summary-table-wrap").find(".collapse-box-header").click();

				var recordValidationSummaryTable = $('#recordValidationSummaryTable').dataTable({"sPaginationType": "full_numbers", "autoWidth": false,"scrollX":true,"columns": [{defaultContent: ""}, { "orderDataType": "dom-text-numeric", defaultContent: "" },{defaultContent: ""}],"order": [[1, "asc" ]] });
				recordValidationSummaryTable.fnAdjustColumnSizing();

				$("#summary-table-wrap").find(".collapse-box-header").click();

				$('#recordValidationSummaryTable tbody').on('click', 'tr', function () {
					//Defect #920 : fixed - get the value for Material code from second column (due to reodering changes)
					var mc = $('td', this).parent().find("td:nth-child(2)").text().trim();
					$("#summaryDetailedView").html( $("#hiddensummaryDetails #summary-table-wrap-" + mc).clone());
					$('#summaryDetailedView #summaryTable' + mc).dataTable().fnDestroy();
					$('#summaryDetailedView #summaryTable' + mc).dataTable({"sPaginationType": "full_numbers", "autoWidth": false,"scrollX":true });
					$("#summary-table-wrap").find(".collapse-box-header").click();


					var collapseBoxUnique = $("#summaryDetailedView #summary-table-wrap-" + mc +'.collapse-box .collapse-box-header');

					collapseBoxUnique.click(function(e){
						e.preventDefault();
						var thisUniqueBox = $(this),
								speed = 200;
						// toggle container
						thisUniqueBox.next('.collapse-box-container').slideToggle(speed);
						// set header active state
						if(thisUniqueBox.hasClass('active') === true) {
							thisUniqueBox.removeClass('active');
						} else if(thisUniqueBox.hasClass('active') === false) {
							thisUniqueBox.addClass('active');
						} else {
							thisUniqueBox.addClass('active');
						}
					});

					$("#summaryDetailedView #summary-table-wrap-" + mc).find(".collapse-box-header").click();
					$('#summaryDetailedView #summaryTable' + mc).dataTable().fnAdjustColumnSizing();

				});
			});
		</script>
		<!-- end recordValidationSummary wrap -->

		<!-- start controls -->
		<div class="controls">
			<%
			String eqrStatus = (String)request.getSession().getAttribute("EQRStatus");
			System.out.println("EquipmentRemovalProcess JSP --> EQRStatus:" + eqrStatus);
			if( eqrStatus!=null && (eqrStatus.equalsIgnoreCase("inprocess") || eqrStatus.equalsIgnoreCase("cancelled"))){
			%>
			<input type="button" value="Cancel" class="button gray" disabled ="true"/>
			<%
			} else if(eqrStatus != null && eqrStatus.equalsIgnoreCase("saved")){
			%>
			<input type="button" value="Cancel" class="button gray" id="backToHomePage" onClick="return cancelConfirmation();"/>
			<%
			} else {
			%>
			<input type="button" value="Cancel" class="button gray" id="backToHomePage" onClick="return cancelConfirmation();" ${(actionForm.readOnly) ? 'disabled' : ''}/>
			<%
			}
			request.getSession().removeAttribute("EQRStatus");
			%>
			<% if(request.getSession().getAttribute("eqrFlag") != null && request.getSession().getAttribute("eqrFlag").equals("0") ) { %>
			<input type="button" value="Back" class="button gray"	id="backToLocationPage" onclick="backToRegistrationList()" />
			<% } else { %>
			<!-- Back Button Change Defect 253 -->
			<input type="button" onclick="backFromRecordValidation();" value="Back" class="button gray" />&nbsp;
			<a id="backFromRecordValidation" href="<c:url context="${ _path }" value="/recordvalidation/backFromRecordValidation.action" />" ></a>
			<!-- Back Button Change Defect 253 -->
			<% } %>




			<input type="button" value="Save" class="button gray" id="saveRecordValidation" onClick="return updateValues('save');" ${(actionForm.readOnly) ? 'disabled' : ''}/>
			<input type="button" value="Submit" class="button gray" id="saveRecordValidation" onClick="return updateValues('submit');" ${(actionForm.readOnly) ? 'disabled' : ''}/>
			<input type="button" value="Validate" class="button gray" id="validateRecordValidation" onClick="return selectValidate('validate');" ${(actionForm.validateDisabled) || actionForm.readOnly || varIndex > 0 ? 'disabled' : ''}/>
			<input type="button" value="Export" class="button gray" id="exportRecordValidation" onClick="return exportValidatedRecord();"/>
			<!-- <input type="button" value="Add" class="button gray" id="addRecordValidation" onClick="return addManuallyAddedRecord();" ${(actionForm.readOnly) ? 'disabled' : ''} /> -->
			<a id="backToHomeLocationAction" href="<c:url context="${ _path }" value="/home/home-action.action"/>"></a>
			<a id="backToLocationAction" href="<c:url context="${ _path }" value="/recordvalidation/recordValidationOnly.action"/>"></a>
			<a id="saveRecordValidationAction" href="<c:url context="${ _path }" value="/recordvalidation/saveEquipmentRemovalProcess.action"/>"></a>
			<a id="exportRecordValidationAction" href="<c:url context="${ _path }" value="/recordvalidation/exportRecordValidationProcess.action"/>"></a>
			<a id="addManuallyAddedAction" href="<c:url context="${ _path }" value="/recordvalidation/addManuallyAddedRecords.action"/>"></a>
			<a id="backToRegistrationList" href="<c:url context="${ _path }" value="/technicalonboarding/backFromTRDetails" />"></a>
			<input type="hidden" id="ValidateStatus" value="${actionForm.status}" />
			<script type="text/javascript">
				$(document).ready(function() {
				var Vindex=${varIndex};
				
				var validateD=${actionForm.validateDisabled};
				var readOnly=${actionForm.readOnly};
				
				if(Vindex<=0 && validateD==false && readOnly==false)
				{
					$('#validateRecordValidation').attr('disabled' , false);
					$('#validateRecordValidation').addClass('gray');
				}
					$("#validatedStatusMsg").hide();
					var flag = ${actionForm.validateDisabled};
					var status=$("#ValidateStatus").val();
					var recFlag=${actionForm.equipmentRegistrationOnly};

						if(flag==false && recFlag==true && status=="Validated")
							$("#validatedStatusMsg").show();
				});
				//showMessageToUser();
			</script>
		</div>
		<!-- end controls -->

		<!-- start error messages -->
		<input type="hidden" id="blankSerialNumberError" value="${grtConfig.blankSerialNumberErrorMsg}" />
		<input type="hidden" id="invalidMaterialCodeError" value="${grtConfig.RVInvalidMC}" />
		<input type="hidden" id="invalidQuantityError" value="${grtConfig.invalidQuantity}" />
		<input type="hidden" id="invalidSerialNumber18CharError" value="${grtConfig.invalidSerialNumber18Char}" />
		<input type="hidden" id="quantitySizeError" value="${grtConfig.quantitySize}" />
		<input type="hidden" id="formaterialcodesError" value="${grtConfig.formaterialcodes}" />
		<input type="hidden" id="invalidSerialNumberError" value="${grtConfig.invalidSerialNumber}" />
		<input type="hidden" id="prepareTechnicalOrderMaterialExclusionError1" value="${grtConfig.prepareTechnicalOrderMaterialExclusionError1}" />
		<input type="hidden" id="prepareTechnicalOrderMaterialExclusionError2" value="${grtConfig.prepareTechnicalOrderMaterialExclusionError2}" />
		<input type="hidden" id="emptyRevisedQuantityMsg" value="${grtConfig.emptyRevisedQuantityMsg}" />
		<input type="hidden" id="selectOneEquipmentMsg" value="${grtConfig.selectOneEquipmentMsg}" />
		<input type="hidden" id="mcWithActiveRemovalWarning" value="${grtConfig.mcWithActiveRemovalWarning}" />
		<input type="hidden" id="submitConfirmMsg" value="${grtConfig.submitConfirmMsg}" />
		<input type="hidden" id="saveLoadingTextMsg" value="${grtConfig.saveLoadingTextMsg}" />
		<input type="hidden" id="submitLoadingTextMsg" value="${grtConfig.submitLoadingTextMsg}" />
		<input type="hidden" id="confirmValidateTextMsg" value="${grtConfig.confirmValidateTextMsg}" />
		<input type="hidden" id="cancelConfirmMsgHome" value="${grtConfig.cancelConfirmMsgHome}" />
		<input type="hidden" id="cancelConfirmMsgRegList" value="${grtConfig.cancelConfirmMsgRegList}" />
		<input type="hidden" id="downloadTemplateMsg" value="${grtConfig.downloadTemplateMsg}" />
		<input type="hidden" id="validOptionMsg" value="${grtConfig.validOptionMsg}" />
		<input type="hidden" id="largeQuantityErrorMsg" value="${grtConfig.largeQuantityErrorMsg}" />




		<input type="hidden" id="RVSuccessMessage" value="${ fn:escapeXml(grtConfig.RVSuccessMessage)}" />
		<input type="hidden" id="RVErrorMessage" value="${grtConfig.RVErrorMessage}" />
		<input type="hidden" id="revisedQtyErrForSerializedAsset" value="${grtConfig.revisedQtyErrForSerializedAsset}" />
		<input type="hidden" id="revisedQtyErrForTechOnBorded" value="${grtConfig.revisedQtyErrForTechOnBorded}" />
		<input type="hidden" id="nofileChosenForUploadErrMsg" value="${grtConfig.nofileChosenForUploadErrMsg}" />
		
		<input type="hidden" id="infIBFedAutomatic" value="${grtConfig.infIBFedAutomatic}" />
		<input type="hidden" id="errOffrTCNotQual" value="${grtConfig.errOffrTCNotQual}" />
		<input type="hidden" id="defectiveCodeErrMsg" value="${grtConfig.defectiveCodeErrMsg}" />
		<input type="hidden" id="revisedQutyEqlExistQtyErrMsg" value="${grtConfig.revisedQutyEqlExistQtyErrMsg}" />
		<input type="hidden" id="errEsnaCode" value="${grtConfig.errEsnaCode}"/>
		
		
		




		<!-- end error messages -->


		<!-- Error Codes -->
		<input type="hidden" id="blankSerialNumberErrorCode" value="${grtConfig.ewiMessageCodeMap['blankSerialNumberErrorMsg']}" />
		<input type="hidden" id="invalidMaterialCodeErrorCode" value="${grtConfig.ewiMessageCodeMap['RVInvalidMC']}" />
		<input type="hidden" id="invalidQuantityErrorCode" value="${grtConfig.ewiMessageCodeMap['invalidQuantity']}" />
		<input type="hidden" id="invalidSerialNumber18CharErrorCode" value="${grtConfig.ewiMessageCodeMap['invalidSerialNumber18Char']}" />
		<input type="hidden" id="quantitySizeErrorCode" value="${grtConfig.ewiMessageCodeMap['quantitySize']}" />
		<input type="hidden" id="formaterialcodesErrorCode" value="${grtConfig.ewiMessageCodeMap['formaterialcodes']}" />
		<input type="hidden" id="invalidSerialNumberErrorCode" value="${grtConfig.ewiMessageCodeMap['invalidSerialNumber']}" />
		<input type="hidden" id="prepareTechnicalOrderMaterialExclusionError1Code" value="${grtConfig.ewiMessageCodeMap['prepareTechnicalOrderMaterialExclusionError1']}" />
		<input type="hidden" id="prepareTechnicalOrderMaterialExclusionError2Code" value="${grtConfig.ewiMessageCodeMap['prepareTechnicalOrderMaterialExclusionError2']}" />
		<input type="hidden" id="emptyRevisedQuantityMsgCode" value="${grtConfig.ewiMessageCodeMap['emptyRevisedQuantityMsg']}" />
		<input type="hidden" id="selectOneEquipmentMsgCode" value="${grtConfig.ewiMessageCodeMap['selectOneEquipmentMsg']}" />
		<input type="hidden" id="mcWithActiveRemovalWarningCode" value="${grtConfig.ewiMessageCodeMap['mcWithActiveRemovalWarning']}" />
		<input type="hidden" id="submitConfirmMsgCode" value="${grtConfig.ewiMessageCodeMap['submitConfirmMsg']}" />
		<input type="hidden" id="saveLoadingTextMsgCode" value="${grtConfig.ewiMessageCodeMap['saveLoadingTextMsg']}" />
		<input type="hidden" id="submitLoadingTextMsgCode" value="${grtConfig.ewiMessageCodeMap['submitLoadingTextMsg']}" />
		<input type="hidden" id="confirmValidateTextMsgCode" value="${grtConfig.ewiMessageCodeMap['confirmValidateTextMsg']}" />
		<input type="hidden" id="cancelConfirmMsgHomeCode" value="${grtConfig.ewiMessageCodeMap['cancelConfirmMsgHome']}" />
		<input type="hidden" id="cancelConfirmMsgRegListCode" value="${grtConfig.ewiMessageCodeMap['cancelConfirmMsgRegList']}" />
		<input type="hidden" id="downloadTemplateMsgCode" value="${grtConfig.ewiMessageCodeMap['downloadTemplateMsg']}" />
		<input type="hidden" id="validOptionMsgCodeCode" value="${grtConfig.ewiMessageCodeMap['validOptionMsg']}" />
		<input type="hidden" id="largeQuantityErrorMsgCode" value="${grtConfig.ewiMessageCodeMap['largeQuantityErrorMsg']}" />
		



		<input type="hidden" id="RVSuccessMessageCode" value="${grtConfig.ewiMessageCodeMap['RVSuccessMessage']}" />
		<input type="hidden" id="RVErrorMessageCode" value="${grtConfig.ewiMessageCodeMap['RVErrorMessage']}" />
		<input type="hidden" id="revisedQtyErrForSerializedAssetCode" value="${grtConfig.ewiMessageCodeMap['revisedQtyErrForSerializedAsset']}" />
		<input type="hidden" id="revisedQtyErrForTechOnBordedCode" value="${grtConfig.ewiMessageCodeMap['revisedQtyErrForTechOnBorded']}" />
		
		<input type="hidden" id="nofileChosenForUploadErrMsgCode" value="${grtConfig.ewiMessageCodeMap['nofileChosenForUploadErrMsg']}" />

		<!-- End Error Codes -->
	</div>
	<!-- end page content -->
</div>
<!-- end page content-wrap -->

<!-- start materialCodeSearchPopUp -->
<div id="materialCodeSearchPopUp" class="modal" style="display: none;">
	<div class="modal-overlay">
		<div class="modal-content">
			<a class="close" onclick="closePopUp()"><i class="fa fa-close"></i></a>
			<h2 class="title">Material Code Search Look Up</h2>
			<div class="searchHeader">
				Enter SE Code(s) Separated By Commas: <input type="text"
					id="searchText" /> <input type="button" type="button"
					id="materialCodeSearchButton" class="button gray small" value="Search"
					onclick="performSearch()" />
				<div style="float: right"></div>
			</div>
			<div class="content">
				<div>
					<input type="hidden" id="populateFor"> <input type="hidden"
						id="populateDesc"> <input type="hidden" id="rowId">

					<div id="se-wrap" class="data-table-wrap collapse-box">
						<h2 class="collapse-box-header active">
							<a href="#">
								<span></span>
							</a>
						</h2>
						<div id="" class="data collapse-box-container">
							<table id="resultTable" border="1">
								<thead>
									<tr>
										<th width="25%">SE Code(s)</th>
										<th width="35%">Group Description</th>
										<th width="10%">Material Code</th>
										<th width="30%">Material Description</th>
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




<%-- ************************************************************* --%>
<%-- ***** BELOW IS DEPRECATED CODE - NEED REVIEW AND REMOVE ***** --%>
<%-- ************************************************************* --%>

<!-- Omniture Test
<script language="JavaScript" type="text/javascript" src="/grtWebProject/framework/skins/support/js/s_code.js"></script>
<script language="JavaScript" type="text/javascript" src="/grtWebProject/framework/skins/support/js/p_code.js"></script> -->
<%--
<!--body onLoad="javascript:determineSortSelection();"-->
<%--

<th width="450"><input type="button" id="selectAllMeterial" value="Select All" class="button gray" onclick="sellectAllMeterial();return false;"/></th>
						<th width="450"><input type="button" id="UnselectAllMeterial" value="UnselectAll" class="button gray" onclick="UnsellectAllMeterial();return false;"/></th>
						<table>
							<tr>
								<td>
									<!--%@include file="/jsp/regSummaryHyperLink.jsp"%-->
								</td>
							</tr>
						</table>

 --%>
 <!--form name="recordValidationForm" id="recordValidationForm" action="exportRecordValidationProcess.action" method="post"-->
 <!-- @include file="/com/grt/registration/registrationSteps/productStepStatus.jsp"%-->
 <!--	p colspan="9" class="osm_text_error" height="20" align="left"><netui:label value="${actionForm.message}"/></p>-->
 <!--input type="hidden" id="orderId${container.index}" name="item.orderId" value="${ item.orderId} "/-->
