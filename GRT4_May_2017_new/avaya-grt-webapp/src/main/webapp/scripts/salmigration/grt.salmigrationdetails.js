/**
 * 
 */

	function validateAssets(){
		//alert("validateAssets called");
		var hasChecked = false;
		/*var sizeStr = document.getElementById("sizeOfAssetsList");
		alert("The sizeStr STRING STRING STRING FROM FORM is ========="+ sizeStr.value);
		var sizeInt = parseInt(sizeStr.value, 10);
		alert("The sizeInt INT INT INT of assets list is========="+ sizeInt);*/
		/*var i=0;
		while(document.getElementById('existingRegisteredAsset'+i+'Select') != null) {
		//while($('#existingRegisteredAsset'+i+'Select').val()){
			//alert("INSIDE FOR LOOP");
			var idOfElement = "existingRegisteredAsset"+i+"Select";
			//alert("Current Element ID=> "+ idOfElement);
			var asset = document.getElementById(idOfElement);
            if (asset.checked) {
	           	hasChecked = true;
	           	break;
            }
			i++;		
		}*/		
		var rows = $("#existingRegisteredAssetsListTable").dataTable().fnGetNodes();
		for(var i=0; i < rows.length; i++) {
			var row = $(rows[i]);

			var checked =	row.find("td:last").find("input").prop("checked");
			if(null != checked) {
				if(checked) {
					var idOfElement = document.getElementById('existingRegisteredAsset'+i+'Select');
					var asset = document.getElementById(idOfElement);
						hasChecked = true;
		           		break;	            
				}
			}
		}
		
	    if (hasChecked == false) {
	    	var selectAsset = $('#selectAsset').val();
	    	var selectAssetCode = $('#selectAssetCode').val();
	    	convertAlertToModelPopUp(selectAssetCode, selectAsset);
	        return false;
	    }
	    
	    var salConfirmAssetMigrationMsg = $("#salConfirmAssetMigrationMsg").val();
	    var salConfirmAssetMigrationMsgCode = $("#salConfirmAssetMigrationMsgCode").val();
	    var callBackMethodStringForOk = "confirmAssetMigrationOK()";
		var callBackMethodStringForCancel ="";
		convertConfirmToModelPopUp(salConfirmAssetMigrationMsgCode, salConfirmAssetMigrationMsg, callBackMethodStringForOk, callBackMethodStringForCancel );
		return false;
		
	    
	    //alert("Leaving validateAssets ");
	    //return true;
	    //var returnFromConfirm = showConfirmation();
	    //alert("Value returned from confirm dialog===>"+ returnFromConfirm);
	    /*if(returnFromConfirm){
	    	var url = document.getElementById('submitSALGatewayMigrationDetails').href;
	    	document.getElementById('salGatewayMigrationDetails').action = url;
	    	document.getElementById('salGatewayMigrationDetails').submit();
	    	$("div.loading-overlay").show();
	    }*/
	}
	function confirmAssetMigrationOK() {
		var url = document.getElementById('submitSALGatewayMigrationDetails').href;
    	document.getElementById('salGatewayMigrationDetails').action = url;
    	document.getElementById('salGatewayMigrationDetails').submit();
    	//window.location =  url;*/
    	$("#submit-overlay").show();
	}
	
	function showConfirmation(){
		//return confirm('Do you want to Submit for Technical Registration?. Click OK to continue otherwise click Cancel');
		var salConfirmAssetMigrationMsg = $("#salConfirmAssetMigrationMsg").val();
		return confirm(salConfirmAssetMigrationMsg);
	}
	
	function sortByField(field, list){
		var sortByCount = document.getElementById('sortByCount').value;
		//alert('Previous :'+document.getElementById('sortBy').value+'   Present  :'+field);
		var sortByScreen = 'SGMD'+list;
		if(document.getElementById('sortByScreen').value == sortByScreen && document.getElementById('sortBy').value == field){
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
		document.getElementById('sortByScreen').value = 'SGMD'+list;
		var url = document.getElementById('dynamicSortByFieldAction').href;
		document.getElementById('salGatewayMigrationList').action=url;
		document.getElementById('salGatewayMigrationList').submit();
	}
	
	function determineSortSelection(){
		var count = document.getElementById('sortByCount').value;
		var field = document.getElementById('sortBy').value;
		var screen = document.getElementById('sortByScreen').value;
		setSortChange(screen, field, count);
	}
	
/*	function showSALPopup(seid, soldTo){
		document.getElementById('salGatewayMigrationList.seid').value = seid;
		document.getElementById('salGatewayMigrationList.soldTo').value = soldTo;
		$('div#showSALPopup').css('display', 'inline-block');
		//showSALGatewayDetails(seid, soldTo);
	}*/

	function showSALGatewayPopup(message) {		
		//var salConcentratorPopup = window.open('', '_blank', 'width=600, height=450, toolbar=no, resizable=yes,menubar=yes');
		//salConcentratorPopup.document.title = 'Recent Devices';
		//salConcentratorPopup.document.write(message);
		
		$('.modal.recent-devices').show();
		$('.modal.recent-devices .content').append(message);
		$('.modal.recent-devices .content table').addClass('basic-table');
	}
	
	function closeSALGatewayPopup() {
		$(".modal.recent-devices").hide();
	}

	/////====================================
	function confirmNewSALGateway(){
		var salEndToEndRegConfirmMsg = $("#salEndToEndRegConfirmMsg").val();
		return confirm(salEndToEndRegConfirmMsg);
	}

	//Validate SoldTo and Seid
	function validateSoldToAndSeid(){
		//alert("inside validateSoldToAndSeid");
		var soldTo = document.getElementById("soldToForAddToList").value;
		//alert("Entered soldto===>"+ soldTo);
		var seid = document.getElementById("salSeIdForAddToList").value;
		//alert("Entered seid===>"+ seid);
		if( (soldTo == null || soldTo == '') && (seid == null || seid == '') ) {
			var emptySTSEID = $("#emptySTSEID").val();
			var emptySTSEIDCode = $("#emptySTSEIDCode").val();
			//convertAlertToModelPopUp("SoldTo and Seid are empty. Please enter soldTo or seid or both.");
			convertAlertToModelPopUp(emptySTSEIDCode, emptySTSEID);
			return false;
		} else if(validateInputs()){

		} else {
			//Submit the form
			return true;
		}
	}

	//////
		function validateSoldToInput(){
			var checkText = document.getElementById('soldToForAddToList').value;
			//alert("i"+checkText+"i");
			RegE = /^(\d{10})\$/;
			/*if(checkText.match(RegE))
				return true;
			else {
				convertAlertToModelPopUp('Invalid SoldToId. Please correct and proceed.');
				return false;
			}*/
			return true;
		}

		function validateSEIDInput(){
			var checkText = document.getElementById('salSeIdForAddToList').value;
			//alert("i"+checkText+"i");
			RegE = /^\((\d{3})\)(\d{3})(\-)(\d{4})\$/;
			/*if(checkText.match(RegE))
				return true;
			else {
				convertAlertToModelPopUp('Invalid SEID format. Please correct and proceed.');
				return false;
			}*/return true;
		}

		function validateInputs(){
			document.getElementById('screenName').value = 'SALGW';
			var soldTo = document.getElementById('soldToForAddToList').value;
			var seid = document.getElementById('salSeIdForAddToList').value;
			if(soldTo == '' && seid == ''){
				var validSTSEID = $("#validSTSEID").val();
				var validSTSEIDCode = $("#validSTSEIDCode").val();
				convertAlertToModelPopUp(validSTSEIDCode, validSTSEID);
				return false;
			} else if(soldTo != '' && validateSoldToInput()){
				return true;
			} else if(seid != '' && validateSEIDInput()){
				return true;
			} else {
				return false;
			}
		}
	////////

	function sortByField(field){
		var sortByCount = document.getElementById('sortByCount').value;
		//alert('Previous :'+document.getElementById('sortBy').value+'	 Present	:'+field);
		if(document.getElementById('sortByScreen').value == 'SGML' && document.getElementById('sortBy').value == field){
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
		document.getElementById('sortByScreen').value = 'SGML';
		var url = document.getElementById('dynamicSortByFieldAction').href;
		document.getElementById('salGatewayMigrationDetails').action=url;
		document.getElementById('salGatewayMigrationDetails').submit();
	}

	function determineSortSelection(){
		var count = document.getElementById('sortByCount').value;
		var field = document.getElementById('sortBy').value;
		var screen = document.getElementById('sortByScreen').value;
		setSortChange(screen, field, count);
	}
	
	function backFromSALMigration(){
		var url = document.getElementById('backFromSALMigrationDetails').href;
		document.getElementById('salGatewayMigrationDetails').action = url;
		document.getElementById('salGatewayMigrationDetails').submit();
		//window.location =  url;
		$("div.loading-overlay").show();
	}
	
	function cancelFromSALMigration(){
		var url = document.getElementById('cancelSALGatewayMigrationList').href;
		document.getElementById('salGatewayMigrationDetails').action = url;
		document.getElementById('salGatewayMigrationDetails').submit();
		//window.location =  url;
		$("div.loading-overlay").show();
	}
	
	function showSALGatewayDetails(seid, soldTo) {
		//convertAlertToModelPopUp('We are processing your request to render Gateway details. A pop-up will appear in few seconds.');
		//var seid =	document.getElementById('salGatewayMigrationList.seid').value;
		//var soldTo = document.getElementById('salGatewayMigrationList.soldTo').value;
		var seid = seid;
		var soldTo = soldTo;
		$("div.loading-overlay").show();
			//dwr.engine.beginBatch();
			//pageflowAccess.getSALGatewayDetails(gatewaySeid, soldToId,
		$.ajax({
			url : _path+'/salmigration/json/getSALGatewayDetails.action?actionForm.gatewaySeid='+seid+'&actionForm.soldToId='+soldTo,
			dataType : 'html',
			success : function(data) {
				if(data) {
					//alert("The value returned from Action class:: "+data);
					showSALGatewayPopup(data);
					$("div.loading-overlay").hide();
				}
			}
		});
			//);
		/*dwr.engine.endBatch({
			parameters: {"jpfScopeID": "<%=jpfScopeID %>"}
		});*/
	}
	
	/*function showMessageToUser(){
		var count = document.getElementById('count').value;
		var errorMessage = document.getElementById('errorMessage').value;
		if(errorMessage != null && errorMessage.length > 0){
			convertAlertToModelPopUp(errorMessage);
			}
		else if(count>0){
			convertAlertToModelPopUp('SAL Gateway Added: '+count);
		}
	}*/
	
	$(function() {
		$('#salGatewayMigrationDetails table#salMigrationDetailsTable').dataTable({"sPaginationType": "full_numbers",
			"autoWidth": false,
			"scrollX":true,
			"sScrollX": "100%",
			"sScrollXInner": "110%",
			"bScrollCollapse": true,
		})
		.columnFilter();
	});
	$(function(){
		$('#salGatewayMigrationDetails table#existingRegisteredAssetsListTable').dataTable({"sPaginationType": "full_numbers",
			"autoWidth": false,
			"scrollX":true,
			"sScrollX": "100%",
			"sScrollXInner": "110%",
			"bScrollCollapse": true,
		})
		.columnFilter();
		
		//showMessageToUser();
	});
	
