function submitTrDetails(trId, index, type, uiOperation){
		document.getElementById("trId").value = trId;
		document.getElementById("trdIndex").value = index;
		document.getElementById("trdType").value = type;
		document.getElementById("trdUiOperation").value = uiOperation;
		var url = document.getElementById('submitTrDetails').href;
		document.getElementById('tecRegDetails').action = url;
		document.getElementById("tecRegDetails").submit();

		$("div.loading-overlay").show();
		
		

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

	function validateSalMigrationAndRegistration () {
		var hasChecked = false;
		var i=0;
		var seid = "";
		var alertKey=$('#alertSellOneRecForSALMigErrorCode').val();
		var alertMsg=$('#alertSellOneRecForSALMig').val();
		
		while(document.getElementById('sal'+i+'Select') != null) {
			//convertAlertToModelPopUp("INSIDE FOR LOOP");
			var idOfElement = "sal"+i+"Select";
			var seidElement = "sal"+i+"SEID";
			//convertAlertToModelPopUp("Current Element ID=> "+ idOfElement);
			var sal = document.getElementById(idOfElement);
            if (sal.checked) {
	           	hasChecked = true;
	           	break;
            }
			i++;
		}

		if (hasChecked == false) {
	        convertAlertToModelPopUp(alertKey, alertMsg);
	        return false;
		 } else {
			
		    	$(".managedEl").show();
				/* //show('fade');
				document.getElementById('managedElementsRemainderPopUp').style.display='block';
				if(document.getElementById('fade')!='undefined' && document.getElementById('fade')!=null)
				document.getElementById('fade').style.display='block'; */
				//return false;
	    }

	    // var returnFromConfirm = showConfirmation();
	    //convertAlertToModelPopUp("Value returned from confirm dialog===>"+ returnFromConfirm);
	    // return returnFromConfirm;
	    
	    return true;
	}

	function hide(elemId) {
		document.getElementById(elemId).style.display='none';
	}
	function show(elemId) {
		document.getElementById(elemId).style.display='block';
	}

	function closePopUp() {
		  $(".managedEl").hide();
		
		$("div.loading-overlay").show();
		var url = document.getElementById('loadSalStepBDetailsAction').href;
		document.getElementById('tecRegDetails').action=url;
		document.getElementById('tecRegDetails').submit();
	}

	function showConfirmation() {
		//return confirm('Do you want to navigate to SAL Alarm and Connectivity testing screen?. Click OK to continue otherwise click Cancel');
		return confirm($('#cnfrmNavigationToSALAlarm').val());
	}

	function showSALGatewayPopup(message) {
		var salConcentratorPopup = window.open('', '_blank', 'width=600, height=450, titlebar=yes, toolbar=no, resizable=yes,menubar=yes');
		salConcentratorPopup.document.title = 'Recent Devices';
		salConcentratorPopup.document.write(message);
	}

	function showSALGatewayDetails(seid, soldTo) {
		//convertAlertToModelPopUp("seid =========>"+seid);
		//convertAlertToModelPopUp("soldTo =======>"+soldTo);
	  	var gatewaySeid =  seid;
		var soldToId = soldTo;
			/* dwr.engine.beginBatch();
			pageflowAccess.getSALGatewayDetails(gatewaySeid, soldToId, */
		$.ajax({
				url : _path+'/technicalonboarding/json/getSALGatewayDetails.action?gatewaySeid='+seid+'&soldToId='+soldTo,
				dataType : 'json',
				success : function(data) {
				if(data) {
					//convertAlertToModelPopUp("The value returned from Action class:: "+data);
					showSALGatewayPopup(data);
				}
			}
		});
	}

	function showMessageToUser(){
		//convertAlertToModelPopUp('Show results after submission...');
		var message = document.getElementById('message').value;
		var returnCode = document.getElementById('returnCode').value;
		var alertKeyRetCode=$('#alertInfoRetCodeInfCode').val();
		var alertMsgRetCode=$('#alertInfoRetCode').val() ;
		alertMsgRetCode = alertMsgRetCode + "-" + returnCode ; 
		//convertAlertToModelPopUp('TRDetails Return Code:'+returnCode);
		if(message != null && message.length > 0){
			//convertAlertToModelPopUp('Message is not empty..');
			convertAlertToModelPopUp("ERR",message);
			if(returnCode == 'success'){
				convertAlertToModelPopUp(alertKeyRetCode, alertMsgRetCode);
				var url = document.getElementById('loadSalStepBDetailsAction').href;
				document.getElementById('tecRegDetails').action=url;
				document.getElementById('tecRegDetails').submit();
			}
		}
	}

	function closePopup2() {
		document.getElementById("dialog-underlay").style.display = "none";
		document.getElementById("srSummaryPopup").style.left = "-1000em";
	}

	function closePopup3() {
		document.getElementById("dialog-underlay").style.display = "none";
		document.getElementById("statusPopup").style.left = "-1000em";
	}

	function backToRegistrationList() {
		var url = document.getElementById('backToRegistrationList').href;
		document.getElementById('tecRegDetails').action = url;
		document.getElementById('tecRegDetails').submit();
		$("div.loading-overlay").show();
	}
	function closePopup3()
	{
		$("#statusPopup").hide();
	}
	function showStatusPopup(techRegId) {
		
		
		$.ajax({
			url : _path+'/technicalonboarding/getCompletionDetails.action?actionForm.technicalRegistrationId='+techRegId,
			dataType : 'html',
			success : function(data) {
			
				document.getElementById("complettionDetails").innerHTML = data;
				$("#statusPopup").show();
			}
		});
	}
	
	$(function() {
		
		 $('input[type="submit"]').each(function() {
		    var readonly = $(this).is(':disabled');
		    
		    if(readonly) { // this is readonly
		    	$(this).removeClass('gray');
		    }
		}); 
		 $('input[type="button"]').each(function() {
			    var readonly = $(this).is(':disabled');
			   
			    if(readonly) { // this is readonly
			    	$(this).removeClass('gray');
			    }
			}); 
		 
		 //Disable Sal Alarm & Connectivity Checkbox if no eligible records are present Defect# 306
		 var totalAlarmLength = $("input[type=checkbox].alarmChckBx").length;
		 var disableAlarmLength = $("input[type=checkbox].alarmChckBx:disabled").length;
		 if(  totalAlarmLength == disableAlarmLength ){
			 $("#salAlarmButton").attr('disabled','disabled');
			 $("#salAlarmButton").removeClass('gray');
		 }
	});
	
	/**
	 * These functions are copied from srSummaryPopup.js and clientValidation.js of GRT 3 as missing in our files
	 */
	function closeSummaryPopup() {
		$("#srSummaryPopup").hide();
	}

	function showPopup(accountLocation, srNumber) {
		document.getElementById("accountLocation").innerHTML = accountLocation;
		document.getElementById("srSummarySrNumber").innerHTML = srNumber;
		
		//var data1 ='actionForm.srNumber='+srNumber;
		
		$.ajax({
			url : _path+'/technicalregsitration/getSrSummaryInfo.action?actionForm.srNumber='+srNumber,
			//data: data1,
			//dataType: 'json',
			//contentType: 'application/json',
			type: 'POST',
			//success: function(data) {
			success: function (data) {
				if(data != 'undefined' && data != null && data.length > 0) {
					var dataArray = data.split(" ::");
					if(dataArray.length > 1) {
						document.getElementById("srSummaryDescription").innerHTML = dataArray[0];
						document.getElementById("srSummaryOwner").innerHTML = dataArray[1];
						document.getElementById("srCompletionNarrative").innerHTML = dataArray[2];
					}
				}
				$("#srSummaryPopup").show();
			},
			error: function(ex) {
				$("#srSummaryPopup").show();
			}
		});
	}

	function closePopup() {
		document.getElementById("dialog-underlay").style.display = "none";
		document.getElementById("srSummaryPopup").style.left = "-1000em";
	}

	function showSalMigrationPopup(siteListId) {
		document.getElementById("dialog-underlay").style.display = "block";
		
		dwr.engine.beginBatch();
		pageflowAccess.getCompletionDetailsForSalMigration(siteListId,
			function(data) {
		        document.getElementById("complettionDetails").innerHTML = data;
		    }
		);
		document.getElementById("statusPopup").style.left = "auto";
		document.getElementById("statusPopup").style.top = "120px";
		 
		dwr.engine.endBatch({
		    parameters: {"jpfScopeID": jpfScopeID}
		});
	}

	function saveSrSummaryInfo() {
		var srNumber = document.getElementById("srSummarySrNumber").innerHTML;
		var srNote   = document.getElementById("srSummarySrNote").value;
		var srAccountLocation = document.getElementById("accountLocation").innerHTML;
		
		var data1 ='actionForm.srNumber='+srNumber+'&actionForm.srNote='+srNote+'&actionForm.accountLocation='+srAccountLocation;
		
		$.ajax({
			url : _path+'/technicalregsitration/saveSrSummaryInfo.action?'+data1,
			//data: data1,
			//dataType: 'json',
			//contentType: 'application/json',
			type: 'POST',
			success: function(data) {
				$("#srSummaryPopup").hide();
			},
			error: function(ex) {
				$("#srSummaryPopup").hide();
			}
		});
	}