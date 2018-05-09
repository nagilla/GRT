function closeSummaryPopup()
{
	$("#srSummaryPopup").hide();
}

function showPopup(accountLocation, srNumber) {	
	document.getElementById("accountLocation").innerHTML = accountLocation;	
	document.getElementById("srSummarySrNumber").innerHTML = srNumber;
	
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

function saveSrNote() {
	document.getElementById("srSummaryPopup").style.left = "-1000em";
	var srNumber = document.getElementById("srSummarySrNumber").innerHTML;
	var srNote   = document.getElementById("srSummarySrNote").value;
	var srAccountLocation = document.getElementById("accountLocation").innerHTML;
	dwr.engine.beginBatch();
	pageflowAccess.saveSrSummaryInfo(srNumber, srNote, srAccountLocation,
		function(data) {
	        document.getElementById("dialog-underlay").style.display = "none";
	    }
	);
	dwr.engine.endBatch({
	    parameters: {"jpfScopeID": jpfScopeID}
	});
}

function showStatusPopup(techRegId) {
	//alert("Inside showStatusPopup() function");	
	//alert("The value XX YYYY of install Script techRegId =======>"+ techRegId);
	document.getElementById("dialog-underlay").style.display = "block";
	
	dwr.engine.beginBatch();
	//alert("Just before the dwr call");
	pageflowAccess.getCompletionDetails(techRegId,
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

function showSalMigrationPopup(siteListId) {
	//alert("Inside showStatusPopup() function");	
	//alert("The value XX YYYY of install Script techRegId =======>"+ techRegId);
	document.getElementById("dialog-underlay").style.display = "block";
	
	dwr.engine.beginBatch();
	//alert("Just before the dwr call");
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

//saveSrSummaryInfo-{srNumber:'1-2201753691',srNote:'Description',accountLocation:'Pune'};
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
		error: function() {
			$("#srSummaryPopup").hide();
		}
	});
}

 function superUpdateStatus(registrationId, processStepId, statusId, soldToId) {	
	// var callback = function(){ 
	 $("div.loading-overlay").show();
	 var d = new Date()
	 var anchor = _path+'/technicalregsitration/updateSiteRegistrationBySuperUser.action?actionForm.registrationId='+registrationId+'&reqId='+d.getTime();

	 window.location.href = anchor;
	// }show_popup("Saving data", "Waiting...", callback);

	}
 
 
 function navigate( source, status,registrationId, soldTo ) {
	  
	 var subPath=null;
	 var installbaseRegistrationOnly= true;
	 var technicalRegistrationOnly= true;
	 var equipmentRegistrationOnly= true;
	 var equipmentMoveOnly=true;
	 if (source == 'IB') {
		installbaseRegistrationOnly = true;
		technicalRegistrationOnly = false;
		equipmentRegistrationOnly = false;
		equipmentMoveOnly=false;
		subPath= _path+'/technicalregsitration/displayRegistration.action';
		
	}
	if (source == 'TR') {
		installbaseRegistrationOnly = false;
		technicalRegistrationOnly = true;
		equipmentRegistrationOnly = false;
		equipmentMoveOnly=false;
		subPath= _path+'/technicalregsitration/displayRegistrationTOB.action';
	}
	if (source == 'EQR') {
		installbaseRegistrationOnly = false;
		technicalRegistrationOnly = false;
		equipmentRegistrationOnly = true;
		equipmentMoveOnly=false;
		subPath= _path+'/technicalregsitration/displayRegistration.action';
	}
	if(source=='EQR_MOVE')
	{
		installbaseRegistrationOnly = false;
		technicalRegistrationOnly = false;
		equipmentRegistrationOnly = false;
		equipmentMoveOnly=true;
		subPath= _path+'/technicalregsitration/displayRegistration.action';
		
	}
	$("div.loading-overlay").show();
	var anchor = subPath+'?actionForm.registrationId='+registrationId+'&actionForm.soldToId='+soldTo+'&actionForm.status='+status+'&actionForm.source='+source+'&actionForm.installbaseRegistrationOnly='+installbaseRegistrationOnly+'&actionForm.technicalRegistrationOnly='+technicalRegistrationOnly+'&actionForm.equipmentRegistrationOnly='+equipmentRegistrationOnly+'&actionForm.equipmentMoveOnly='+equipmentMoveOnly;
	document.location = anchor;
			
		}

 function show_popup(msg, title) {
		document.getElementById("dialog-underlay").style.display = "block";
		if(title) {
			document.getElementById("popup-window-title-ib").innerHTML = title;
		}
		document.getElementById("popup-window-content-ib").innerHTML = msg;
		
		var elePopup = document.getElementById("popup-window-ib");
		var scroll = getScroll();
		var divH = getLength(elePopup.style.height, scroll.h);
		var divW = getLength(elePopup.style.width, scroll.w);
		// elePopup.style.top = (scroll.t + (scroll.ch - divH)/2) + "px";
		elePopup.style.top = "250px";
		elePopup.style.left = "auto";
		
	}
