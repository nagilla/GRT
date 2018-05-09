//Error Messages										
var eqmAssignedQtyErrMsg;
var eqmSeletOneEqpErrMsg1;
var eqmSeletOneEqpErrMsg2;
var eqmActiveContrsctSelErrMsg;
var eqmRequestSuccessMsg;
var eqmOutageMsg;
var eqmCancelToHomePageErrMsg;
var eqmCancelToRegPageErrMsg;
var eqmReqSubmitConfMsg;
var eqmQtyLessThanZeroErrMsg;
var eqmQtyGreaterThanExisErrMsg;
var eqmZeroQtyErrMsg;
var eqmSaveSuccMsg;
					
//Error Codes
var eqmAssignedQtyErrMsgCode;
var eqmSeletOneEqpErrMsg1Code;
var eqmSeletOneEqpErrMsg2Code;
var eqmActiveContrsctSelErrMsgCode;
var eqmRequestSuccessMsgCode;
var eqmOutageMsgCode;
var eqmCancelToHomePageErrMsgCode;
var eqmCancelToRegPageErrMsgCode;
var eqmReqSubmitConfMsgCode;
var eqmQtyLessThanZeroErrMsgCode;
var eqmQtyGreaterThanExisErrMsgCode;
var eqmZeroQtyErrMsgCode;
var eqmSaveSuccMsgCode;



$( document ).ready(function() {
	//Error Messages 
									
	eqmAssignedQtyErrMsg = $("#eqmAssignedQtyErrMsg").val();	
	eqmSeletOneEqpErrMsg1 = $("#eqmSeletOneEqpErrMsg1").val();	
	eqmSeletOneEqpErrMsg2 = $("#eqmSeletOneEqpErrMsg2").val();	
	eqmActiveContrsctSelErrMsg = $("#eqmActiveContrsctSelErrMsg").val();	
	eqmRequestSuccessMsg = $("#eqmRequestSuccessMsg").val();	
	eqmOutageMsg = $("#eqmOutageMsg").val();	
	eqmCancelToHomePageErrMsg = $("#eqmCancelToHomePageErrMsg").val();	
	eqmCancelToRegPageErrMsg = $("#eqmCancelToRegPageErrMsg").val();	
	eqmReqSubmitConfMsg = $("#eqmReqSubmitConfMsg").val();	
	eqmQtyLessThanZeroErrMsg = $("#eqmQtyLessThanZeroErrMsg").val();	
	eqmQtyGreaterThanExisErrMsg = $("#eqmQtyGreaterThanExisErrMsg").val();	
	eqmZeroQtyErrMsg = $("#eqmZeroQtyErrMsg").val();	
	eqmSaveSuccMsg = $("#eqmSaveSuccMsg").val();	
						
	//Error Codes
	eqmAssignedQtyErrMsgCode = $("#eqmAssignedQtyErrMsgCode").val();	
	eqmSeletOneEqpErrMsg1Code = $("#eqmSeletOneEqpErrMsg1Code").val();	
	eqmSeletOneEqpErrMsg2Code = $("#eqmSeletOneEqpErrMsg2Code").val();	
	eqmActiveContrsctSelErrMsgCode = $("#eqmActiveContrsctSelErrMsgCode").val();	
	eqmRequestSuccessMsgCode = $("#eqmRequestSuccessMsgCode").val();	
	eqmOutageMsgCode = $("#eqmOutageMsgCode").val();	
	eqmCancelToHomePageErrMsgCode = $("#eqmCancelToHomePageErrMsgCode").val();	
	eqmCancelToRegPageErrMsgCode = $("#eqmCancelToRegPageErrMsgCode").val();	
	eqmReqSubmitConfMsgCode = $("#eqmReqSubmitConfMsgCode").val();	
	eqmQtyLessThanZeroErrMsgCode = $("#eqmQtyLessThanZeroErrMsgCode").val();	
	eqmQtyGreaterThanExisErrMsgCode = $("#eqmQtyGreaterThanExisErrMsgCode").val();	
	eqmZeroQtyErrMsgCode = $("#eqmZeroQtyErrMsgCode").val();	
	eqmSaveSuccMsgCode = $("#eqmSaveSuccMsgCode").val();

});
					


function clearValueOnUncheck(object, item){
	if(!object.checked){
		document.getElementById('movedQuantity'+item).value='';
		document.getElementById('remainingQuantity'+item).value = '';
	}
}

function changeValues(item){
	var revised = document.getElementById('movedQuantity'+item).value;
	var existing = document.getElementById('availableQuantity'+item).value;
	if(parseInt(revised) < 0){
		showErrorDescription(item, eqmQtyLessThanZeroErrMsg);
		errorDescription
		document.getElementById('movedQuantity'+item).value='';
		return false;
	} else if(revised.trim() == ''){
		clearErrorDescription(item);
		//set chekbox unchecked
		$("#deleted"+item).attr("checked",false);
	} else if(parseInt(revised) > parseInt(existing)){
		showErrorDescription(item, eqmQtyGreaterThanExisErrMsg);
		document.getElementById('movedQuantity'+item).value='';
		return false;
	} else if( parseInt(revised)  == 0){
		showErrorDescription(item, eqmZeroQtyErrMsg);
		document.getElementById('movedQuantity'+item).value='';
		return false;
	} else if( parseInt(revised)  <= parseInt(existing)){ //Update the value of remaining qty
		document.getElementById('remainingQuantity'+item).value = parseInt(existing)-parseInt(revised);
		clearErrorDescription(item);
	}
}
function showErrorDescription(item,msg) {
	document.getElementById("errorDescription"+item).innerHTML = msg;
	disableCheckbox(item);
	var col = $("#"+"errorDescription"+item);
	var parent = col.parent();
	while(!parent.is("tr")) {
		parent = parent.parent();
	}
	if(parent && parent != null) {
		parent.addClass( 'highlight' );
		$("#eqmContentTable").dataTable().fnAdjustColumnSizing();
		
	}
	
	$("#errorExclamation"+item).show();
}
function clearErrorDescription(item) {
	document.getElementById("errorDescription"+item).innerHTML = "";
	enableCheckbox(item);
	var col = $("#"+"errorDescription"+item);
	var parent = col.parent();
	while(!parent.is("tr")) {
		parent = parent.parent();
	}
	if(parent && parent != null) {
		parent.removeClass( 'highlight' );
	}
	$("#errorExclamation"+item).hide();
}
function disableCheckbox(item) {
	var checkBox = $("#deleted"+item);
	checkBox.attr("checked",false);
	checkBox.attr("disabled",true);
}

function enableCheckbox(item) {
	var checkBox = $("#deleted"+item);
	checkBox.attr("checked",true);
	checkBox.attr("disabled",false);
}


//Select/Unselect All For Removal Method
function selectNunselect(obj){
	var rows = $("#eqmProcessForm table#eqmContentTable").dataTable().fnGetNodes();
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
	
	return false;// to be moved
}

function updateValues(saveType){
	var checkFlag = 0;
	var mcWithActive = '';
	var rows = $('#eqmProcessForm table#eqmContentTable').dataTable().fnGetNodes();
    for(var i=0; i < rows.length; i++) {
		var row = $(rows[i]);

		var checked =  row.find("td:first").find("input").prop("checked");    
		if(null != checked) {
			if(checked) {
				checkFlag++;
				//Need to get verification as there may be cases to update only serial numbers.
				var remainingQuantity = row.find("td").find("input[id^='movedQuantity']").attr("value");
				if(null!= remainingQuantity && remainingQuantity == ''){
					convertAlertToModelPopUp(eqmAssignedQtyErrMsgCode, eqmAssignedQtyErrMsg);
					return false;
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
		convertAlertToModelPopUp(eqmSeletOneEqpErrMsg1Code, eqmSeletOneEqpErrMsg1 + ' ' +saveType+' '+eqmSeletOneEqpErrMsg2);
		return false;
	} else if(saveType == 'submit'){
		if(mcWithActive != ''){
			var callBackMethodStringForOk = "submitEqmProcessForm('eqmProcessForm' , 'saveEQMProcessAction', '#eqmProcessForm table#eqmContentTable')";
			var callBackMethodStringForCancel ="";
			convertConfirmToModelPopUp(eqmActiveContrsctSelErrMsgCode, eqmActiveContrsctSelErrMsg, callBackMethodStringForOk, callBackMethodStringForCancel );
			return false;			
		}		
		
		var callBackMethodStringForOk = "submitEqmProcessForm('eqmProcessForm' , 'saveEQMProcessAction', '#eqmProcessForm table#eqmContentTable')";
		var callBackMethodStringForCancel ="";
		convertConfirmToModelPopUp(eqmReqSubmitConfMsgCode, eqmReqSubmitConfMsg, callBackMethodStringForOk, callBackMethodStringForCancel );
		return false;
	} else if(saveType == 'save'){
		$("div.loading-overlay").show();
		document.getElementById('eqmProcessForm').action = document.getElementById('saveEQMProcessAction').href;
		$('#eqmProcessForm table#eqmContentTable').dataTable().fnDestroy();
		document.getElementById('eqmProcessForm').submit();		
	}
}

function submitEqmProcessForm(formId , actionUrl, dataTableId) {
	$("#submit-overlay").show();
	
	if(dataTableId != null && dataTableId != "") {
		$(dataTableId).dataTable().fnDestroy();
	}			
	
	document.getElementById('eqmProcessForm').action = document.getElementById(actionUrl).href;
	document.getElementById('eqmProcessForm').submit();	
}

function cancelConfirm() {
	if(confirm(eqmCancelToRegPageErrMsg)) {
 		return true;
 	} else {
		return false;
	}
}

function showMessageToUser(){
	var eqrMessage = document.getElementById('eqmMessage').value;
	var returnCode = document.getElementById('eqmReturnCode').value;
	if(eqrMessage != null && eqrMessage.length > 0){
		if(returnCode == 'S'){
			eqrMessage = eqmRequestSuccessMsgCode + "###" + eqmRequestSuccessMsg;
		}
		if(returnCode == '100'){
			eqrMessage = eqmOutageMsgCode + "###" + eqmOutageMsg;
		}
		if(returnCode == 'S' || returnCode == '100'){
			var url = document.getElementById('registrationListAction').href;
			convertAlertToModelPopUpWithRedirect(eqrMessage, url);
		}else{
			convertAlertToModelPopUpServerSideMsg(eqrMessage);
		}
	}else if ( returnCode == "0" ){ //Redirect to registration list
		var regLink = $('a#registrationListAction').attr('href');
		convertAlertToModelPopUpWithRedirect(eqmSaveSuccMsgCode + "###" +eqmSaveSuccMsg, regLink);
	}
}

function numericFilter(txb) {
   txb.value = txb.value.replace(/[^0-9]/ig, "");
}

function cancelConfirmation(){
	
	var callBackMethodStringForOk = "submitEqmProcessForm('eqmProcessForm' , 'backToHomeLocationAction', '', '')";
	var callBackMethodStringForCancel ="";
	convertConfirmToModelPopUp(eqmCancelToHomePageErrMsgCode, eqmCancelToHomePageErrMsg, callBackMethodStringForOk, callBackMethodStringForCancel );
	return false;
}

function exportValidatedRecord() {
	$("div.loading-overlay").show();
	document.getElementById('eqmProcessForm').action = document.getElementById('exportEQMProcessAction').href;
	document.getElementById('eqmProcessForm').submit();
}

/* GRT 4.0 CODE */
$(function() {
	
	$.fn.dataTable.ext.order['dom-text'] = function  ( settings, col )
	{
	    return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
	    	if( $('input', td).val()!=undefined ){//Input box is present
	    		return $('input', td).val();
	    	}else{
	    		return $(td).text().trim();
	    	}
	    } );
	};
	
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
	
	$("div.loading-overlay").hide();
	var eqmDtable = $('#eqmProcessForm table#eqmContentTable').dataTable({"sPaginationType": "full_numbers",
	"autoWidth": false,
	"scrollX":true,
	"columns": [
				{ "orderDataType": "dom-checkbox", "sDefaultContent": "" },
				{ "orderDataType": "numeric", sDefaultContent: "" },
				{ "orderDataType": "numeric", sDefaultContent: "" },
				{ "orderDataType": "numeric", sDefaultContent: "" },
				{sDefaultContent: ""},
				{"sType" : "dom-text", sDefaultContent: ""},
				{"sType" : "dom-text-numeric", sDefaultContent: ""},
				{sDefaultContent: ""},
				{ "sType": "dom-text", "sDefaultContent": "" },
				{sDefaultContent: ""},
				{ "orderDataType": "dom-text-numeric", "defaultContent": "" },
				{sDefaultContent: ""},
				   
		       ],
	"order": [[ 8, "asc" ], [ 5, "asc" ], [ 6, "asc" ]],
	       "fnInitComplete": function (oSettings, json) {
	    	   $("#loading-overlay-eq-move").hide();
	       }
	});
	//Highlight PLDS/KMAT items	
	var rows = eqmDtable.dataTable().fnGetNodes();
	for(var i=0; i < rows.length; i++) {
		var row = $(rows[i]);
		var index =  row.find("td").find("input[id^=deleted]").attr("id").replace("deleted","").trim();		
		var errorDesc = row.find('#existingErrorDescription'+index).val().trim();
		if(null != errorDesc && errorDesc.trim() != "") {
			row.addClass( 'highlight' );
			var checkBox = row.find("td:first-child").find("input");
			checkBox.attr("checked",false);
			checkBox.attr("disabled",true);
		}
	}
	
	showMessageToUser();
	
	//
	 $('input[type="search"]').each(function() {
		 $(this).on('click',function(){
			 eqmDtable.api().draw(false);
			 $('#eqmContentTable_wrapper #eqmContentTable_filter').find("input[type='search']").focus();
			 
		 });
	 });
	 
	 $(".hiddenLable").hide(); 
	 
	 $('form#eqmProcessForm').on('mouseover','td.erroWrap',function(){
		 	var spanElem = $(this).text();
		    var titleText = $.trim(spanElem);
	        this.setAttribute( 'title', titleText);
	    });
});

function setEquipmentMoveValueInLabel(index,val,type)
{
	if(type=="Quantity")
		$("#lblmovedQuantity"+index).text(val);
	
	 $(".hiddenLable").hide(); 
}

function backToRegistrationList() {
	var url = document.getElementById('backToRegistrationList').href;
	document.getElementById('eqmProcessForm').action = url;
	document.getElementById('eqmProcessForm').submit();
	$("div.loading-overlay").show();
}

function backToLocation() {
		document.getElementById('eqmProcessForm').action = document.getElementById('newRegistrationWithSoldToValidationForAgentAction').href;
		document.getElementById('eqmProcessForm').submit();
}
