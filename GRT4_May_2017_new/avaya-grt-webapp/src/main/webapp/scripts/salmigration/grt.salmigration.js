/**
 * 
 */

function validate() {
		validatePrimaryAnadSecondary();
		/////document.getElementById("salGatewayMigrationList").submit();

	}

	function validatePrimaryAnadSecondary() {
		var inputs = document.getElementsByName("primary");
		var selectedPrimarySeid = null;
		var selectedSecondarySeid = null;
		for (var i = 0; i < inputs.length; i++) {
			if (inputs[i].checked) {
				document.getElementById("primarySALGateway").value = inputs[i].value;
				//alert("Primary SEID selected is =====> "+ document.getElementById("primarySALGateway").value );
				selectedPrimarySeid = inputs[i].value;
			}
		}

		var secondaryInputs = document.getElementsByName("secondary");
		for (var i = 0; i < secondaryInputs.length; i++) {
			if (secondaryInputs[i].checked) {
				document.getElementById("secondarySALGateway").value = secondaryInputs[i].value;
				//alert("Secondary SEID selected is =====> "+ document.getElementById("secondarySALGateway").value );
				selectedSecondarySeid = secondaryInputs[i].value;
			}
		}

		if (selectedPrimarySeid == null) {
			var salRequired = $('#salRequired').val();
			var salRequiredCode = $('#salRequiredCode').val();
			convertAlertToModelPopUp(salRequiredCode, salRequired);
		} else if (selectedPrimarySeid == selectedSecondarySeid) {
			var duplicateGateway = $('#duplicateGateway').val();
			var duplicateGatewayCode = $('#duplicateGatewayCode').val();
			convertAlertToModelPopUp(duplicateGatewayCode, duplicateGateway);
			///Begin Clear the User Selection of Radio buttons
			var primaryInputsForClear = document.getElementsByName("primary");
			for (var i = 0; i < primaryInputsForClear.length; i++) {
				if (primaryInputsForClear[i].checked) {
					primaryInputsForClear[i].checked = false;
				}
			}
			document.getElementById("primarySALGateway").value = null;
		 	var secondaryInputsForClear = document.getElementsByName("secondary");
			for (var i = 0; i < secondaryInputsForClear.length; i++) {
				if (secondaryInputsForClear[i].checked) {
					secondaryInputsForClear[i].checked = false;
				}
			}
			document.getElementById("secondarySALGateway").value = null;
			///End Clear the User Selection of Radio buttons
		} else {//TO BE CHANGED
			//document.getElementById("salGatewayMigrationList").submit();
			var url = document.getElementById('submitSALGatewayMigrationList').href;
			document.getElementById('salGatewayMigrationList').action = url;
			document.getElementById('salGatewayMigrationList').submit();
			//window.location =  url;*/
			$("div.loading-overlay").show();
		}
	}

	/*
	function showSALGatewayDetails(seCode) {
			alert("The value selected ===>"+ seCode);
			document.getElementById("gatewaySeid").value = seCode;
			//var url = '<%=request.getContextPath()%>/pages/registration/creditCardRegistration.portlet';
			//var url = '<%=request.getContextPath()%>/showSALGatewayDetails';
		var url = 'showSALGatewayDetails';
			window.open(url,"SALGateWayDetails",'width=600,height=200,location=no,menubar=no,resizable=yes');
	}
	*/
	/////====================================
	/*function closeSALGatewayPopup() {
		document.getElementById("dialog-underlay").style.display = "none";
		document.getElementById("email-window").style.left = "-1000em";
	}*/
	
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

	/////====================================
	function confirmNewSALGateway(){
		var salEndToEndRegConfirmMsg = $("#salEndToEndRegConfirmMsg").val();
		var salEndToEndRegConfirmMsgCode = $("#salEndToEndRegConfirmMsgCode").val();
		
		var callBackMethodStringForOk = "confirmNewSALGatewayOK()";
		var callBackMethodStringForCancel ="";
		convertConfirmToModelPopUp(salEndToEndRegConfirmMsgCode, salEndToEndRegConfirmMsg, callBackMethodStringForOk, callBackMethodStringForCancel );
		return false;
		/*confirm(salEndToEndRegConfirmMsg);
		if(confirm){
			$("div.loading-overlay").hide();
		}*/
	}
	function confirmNewSALGatewayOK() {
		document.getElementById('salGatewayMigrationList').action = document.getElementById('createNewSALGatewayAction').href;
		document.getElementById('salGatewayMigrationList').submit();
		return false;
	}

	//Validate SoldTo and Seid
	function validateSoldToAndSeid(){
		//alert("inside validateSoldToAndSeid");
		var soldTo = document.getElementById("soldToForAddToList").value;
		//alert("Entered soldto===>"+ soldTo);
		var seid = document.getElementById("salSeIdForAddToList").value;
		//alert("Entered seid===>"+ seid);
		if( (soldTo == null || soldTo == '') && (seid == null || seid == '') ) {
			var emptySTSEID = $('#emptySTSEID').val();
			var emptySTSEIDCode = $('#emptySTSEIDCode').val();
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
				var invalidST = $('#invalidST').val();
				convertAlertToModelPopUp(invalidST);
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
				var invalidSEID = $('#invalidSEID').val();
				convertAlertToModelPopUp(invalidSEID);
				return false;
			}*/return true;
		}

		function validateInputs(){
			document.getElementById('screenName').value = 'SALGW';
			var soldTo = document.getElementById('soldToForAddToList').value;
			var seid = document.getElementById('salSeIdForAddToList').value;
			if(soldTo == '' && seid == ''){
				var validSTSEID = $('#validSTSEID').val();
				var validSTSEIDCode = $('#validSTSEIDCode').val();
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
		document.getElementById('salGatewayMigrationList').action=url;
		document.getElementById('salGatewayMigrationList').submit();
	}

	function determineSortSelection(){
		var count = document.getElementById('sortByCount').value;
		var field = document.getElementById('sortBy').value;
		var screen = document.getElementById('sortByScreen').value;
		setSortChange(screen, field, count);
	}
	
	function backFromSALMigration(){
		var url = document.getElementById('backFromSALGatewayMigrationList').href;
		document.getElementById('salGatewayMigrationList').action = url;
		document.getElementById('salGatewayMigrationList').submit();
		//window.location =  url;
		$("div.loading-overlay").show();
	}
	
	function cancelFromSALMigration(){
		var url = document.getElementById('cancelSALGatewayMigrationList').href;
		document.getElementById('salGatewayMigrationList').action = url;
		document.getElementById('salGatewayMigrationList').submit();
		//window.location =  url;
		$("div.loading-overlay").show();
	}
	
	function showMessageToUser(){
		var count = document.getElementById('count').value;
		var errorMessage = document.getElementById('errorMessage').value;
		if(errorMessage != null && errorMessage.length > 0){
			convertAlertToModelPopUpServerSideMsg(errorMessage);
		}
		else if(count>0){
			var gatewayAdded = $('#gatewayAdded').val();
			var gatewayAddedCode = $('#gatewayAddedCode').val();
			convertAlertToModelPopUp(gatewayAddedCode, gatewayAdded+count);
		}else if(count != null && count.length > 0){
			convertAlertToModelPopUpServerSideMsg(count);
		}
	}
	
	$(function() {
		
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
		
		$('#salGatewayMigrationList table#salMigrationTable').dataTable({"sPaginationType": "full_numbers",
			"autoWidth": false,
			"scrollX":true,
			"sScrollX": "100%",
			"sScrollXInner": "110%",
			"bScrollCollapse": true,
			"order": [[ 3, "asc" ],[ 6, "asc" ]]
		})
		.columnFilter();
		
		showMessageToUser();
	});
	
