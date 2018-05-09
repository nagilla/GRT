
var existingTobRecords = null;
var registrableProd = null;
var registrableProdDataTable=null;

function retrieveExistingTOBRecords(value) {
	var ele = document.getElementById(value+"ToggleText");
	var text = document.getElementById(value);
	if(ele.style.display == "block") {
    	//Nothing
  	}
	else {
		if(existingTobRecords==null) {
				   var soldTo = document.getElementById("soldTo").textContent;
				   var registrationId = document.getElementById("registrationId").innerHTML;
		    	   //show_popup( "Retrieving Existing Install Base ", "Please Wait..." );
		    	   populateExistingTobTable(soldTo,registrationId);

		}
	}
}

//GRT 4.0 Chnages
function populateRegistrableProductList(soldTo,registrationId)
{
	
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
	
	
	
	$("div.loading-overlay").show();
	toggleEnable("No");
  	$.ajaxSetup({ cache: false });
    $.ajax({
		url : _path+'/technicalonboarding/json/getRegistrationSummaryListDetail.action?soldToNumber='+soldTo+'&registrationId='+registrationId,
		dataType : 'json',
		success : function(data) {
			try {
			registrableProd=data;
			$('#registrableProd1 tr').not(':first').remove();
			var headData = '';
			var trStart = '<tr>';
			var tdStart = '<td align="center" >';
		 	var tdEnd = '</td>';
			for(var count=0; count<data.length;count++)
			{
				var materialCode=data[count].materialCode!=null?data[count].materialCode:'';
				var materialCodeDescription=data[count].materialCodeDescription!=null?data[count].materialCodeDescription:'';
				var productLine=data[count].productLine!=null?data[count].productLine:'';
				var initialQty=data[count].initialQty!=null?data[count].initialQty:'';
				var remainingQty=data[count].remainingQty!=null?data[count].remainingQty:'';
				var connectivity='<input type="radio" name="noConnectivity'+count+'" value="Yes" class="connectivtyChk" onclick="showConnectivityPopUp(this.value, \''+materialCode+'\', '+remainingQty+','+count+');" > Yes <input type="radio" class="connectivtyChk" onclick="showConnectivityPopUp(this.value, \''+materialCode+'\', '+remainingQty+','+count+');" name="noConnectivity'+count+'" value="No Connectivity"> No ';
				var accessType='Access Type &nbsp;&nbsp;<select style="width:60px;" id="accessType'+count+'"';
				accessType+='name="actionForm.registrationSummaryList['+count+'].accessType" ';
				accessType +=' onChange="return navigateToTRConfig(this.value, \''+materialCode+'\', '+remainingQty+');"';
				accessType +=' onClick="populateAccessType(this, \''+materialCode+'\');">';
				accessType +='</select>';
				headData +=trStart+tdStart+connectivity+tdEnd+
				tdStart+initialQty+tdEnd+
				tdStart+remainingQty+tdEnd+
				tdStart+materialCode+tdEnd+
				tdStart+materialCodeDescription+tdEnd+
				tdStart+productLine+tdEnd+
				'</tr>';

			}
			$('#registrableProd1 tbody').html(headData);
			$(".loading-overlay").hide();
			$('#registrableProd1').dataTable().fnDestroy();
						registrableProdDataTable = $('#registrableProd1')
								.dataTable({
									"sPaginationType" : "full_numbers",
									"scrollX" : true,
									"autoWidth" : false,
									"columns": [
										          
										           
										           { "sType": "dom-text", sDefaultContent: "" },
										          
										           {sDefaultContent: ""},
										           {sDefaultContent: ""},
										           { "sType": "dom-text-numeric", sDefaultContent: "" },
										           {sDefaultContent: ""},
										           { "sType": "dom-text", sDefaultContent: "" },
										       ],
									"order" : [ [ 5, "asc" ],[3,"asc"] ],
								}).columnFilter();
			registrableProdDataTable.fnDraw();

			 }catch(e) {
	            	//close_popup();
	            	$(".loading-overlay").hide();
	            }
		}
	});
}



function populateExistingTobTable(soldTo , registrationId) {
	
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
			$.ajaxSetup({ cache: false });
				$.ajax({
					url : _path+'/technicalonboarding/json/getExistingTOBRecords.action?soldToNumber='+soldTo+'&registrationId='+registrationId,
					dataType : 'json',
					cache : false,
					success : function(data) {
			                         try {
			                            existingTobRecords = data;
										$('#existingTOBTable tr').not(':first').remove();
										var html = '';
										var trStart = '<tr>';
										var tdStart = '<td width="10%" align="center" >';
									 	var tdEnd = '</td>';
									 	for(var i = 0; i < data.length; i++) {
									 	  var mc = data[i].materialCode!=null?data[i].materialCode:'';
									 	  var mcDesc = data[i].materialCodeDescription!=null?data[i].materialCodeDescription:'';
									 	  var prodLine = data[i].productLine!=null?data[i].productLine:'';
									 	  var seElem = data[i].solutionElement!=null?data[i].solutionElement:'';
									      var seId = data[i].seId!=null?data[i].seId:'';;
									      var sId = data[i].sid!=null?data[i].sid:'';
									      var mId = data[i].mid!=null?data[i].mid:'';
									      var salSeIdPrimarySecondary = data[i].salSeIdPrimarySecondary!=null?data[i].salSeIdPrimarySecondary:'';
									      var updateTitle = data[i].updateButtonTitle!=null?data[i].updateButtonTitle:'Update';
									      var disabled = (data[i].disableUpdateFlag==true)?'disabled="true"':'';
										  var updateBtnClass = (data[i].disableRetestFlag==true)?'"button disabled"':'"button gray"';
										  var updateButton=	'<input type="button" title="'+updateTitle+
										                    '" value="Update" class='+updateBtnClass+'onClick="javascript:navigateToUpdate(\''+
										                  mc+'\', \''+seId+'\');"  '+disabled+'/>';

										  var retestDisabled = (data[i].disableRetestFlag==true)?'disabled="true"':'';
										  var retestBtnClass = (data[i].disableRetestFlag==true)?'"button disabled"':'"button gray"';
										  var reTestButton=	'<input type="button" title="ReTest" value="ReTest" class='+retestBtnClass+' onClick="javascript:navigateToRetest(\''+
						                  mc+'\', \''+seId+'\');"  '+retestDisabled+'/>';
										  html += trStart+
								              tdStart+updateButton+tdEnd+
								              tdStart+reTestButton+tdEnd+
								              tdStart+seId+tdEnd+
								              tdStart+seElem+tdEnd+
								              tdStart+mc+tdEnd+
								              tdStart+mcDesc+tdEnd+
								              tdStart+prodLine+tdEnd+
								              tdStart+sId+tdEnd+
								              tdStart+mId+tdEnd+
								              tdStart+salSeIdPrimarySecondary+tdEnd+
								              '</tr>';
									    }
									 		$('#existingTOBTable tbody').html(html);
											$(".loading-overlay").hide();
							            	//Create the datatable for the existing tob records
								            $('#existingTOBTable').dataTable({
												"sPaginationType" : "full_numbers",
												"scrollX" : true,
												"autoWidth" : false,
												"columns": [
													           {sDefaultContent: "" },
													           {sDefaultContent: ""},
													           {sDefaultContent: ""},
													           { "sType": "dom-text", sDefaultContent: "" },
													           { "sType": "dom-text-numeric", sDefaultContent: "" },
													           {sDefaultContent: ""},
													           { "sType": "dom-text", sDefaultContent: "" },
													           {sDefaultContent: ""},
													           {sDefaultContent: ""},
													           {sDefaultContent: ""},
													       ],
												"order" : [ [ 6, "asc" ],[ 3, "asc" ], [ 4, "asc" ] ]
											}).columnFilter();
							            }catch(e) {
							            	//close_popup();
							            	$(".loading-overlay").hide();
							            }
			                         }
				});
		}

function populateAccessType(elem,materialCode) {
	var elemSize = elem.options.length;

	if(elemSize<=0)
	{
	$("div.loading-overlay").show();
	  if(elem != null || elem!=undefined){
	       if(elem.options.length >1) {
	         return;
	       }
  			$.ajax({
					url : _path+'/technicalonboarding/json/getEligibleAccessTypes.action?materialCode='+materialCode,
					dataType : 'json',
					success : function(data) {
			                           searchResult = data;
			                           if(data.length == 0) return;
			                           var optStr = "";
									 	for(var i = 0; i < data.length; i++) {
										    elem.options[i]=new Option(data[i],data[i]);
									    }

									 	$("div.loading-overlay").hide();

			                        }
  			});
	  }
		}
}

function populateAccessTypeForPopUp(materialCode,count) {

	$("div.loading-overlay").show();

  			$.ajax({
					url : _path+'/technicalonboarding/json/getEligibleAccessTypes.action?materialCode='+materialCode,
					dataType : 'json',
					success : function(data) {
			                           searchResult = data;
			                           if(data.length == 0) return;
			                           var optStr ='Access Type&nbsp;&nbsp;<select style="width:100px;" id="accessTypeDropDown" onchange="toggleEnable(this.value)"';
			                           optStr+=' name="actionForm.registrationSummaryList['+count+'].accessType" >';

			                          	for(var i = 0; i < data.length; i++) {
			                          	//Defect 140
			                          		if(data[i]=="SSL/VPN"){
			                          			$('#sslVpnConnection').val("Error");
			                          			}
									 		optStr+="<option value='"+data[i]+"'>"+data[i]+"</option>";
										   }
									 	optStr +='</select>';

									 	$('#popUpDropDown').html(optStr);

									 	$("div.loading-overlay").hide();
									 	//Defect 140
										$("#msgSSLVPN").hide();
									 	$("#accessTypeDialog").show();
			                        }
  			});
	  }



function toggle(value) {
	var ele = document.getElementById(value+"ToggleText");
	var text = document.getElementById(value);
	if(ele.style.display == "block") {
  	}
	else {
		var soldTo = document.getElementById("soldTo").textContent;
		var registrationId = document.getElementById("registrationId").innerHTML;

		if(registrableProd==null && value=="readyForTRDiv") {//Load only for 1st div
			populateRegistrableProductList(soldTo,registrationId);
		}
		if(value=="tredProdDiv") {//Load only for 1st div
			//reconstruct  data table
			var zeroData = $("#readyToProcessTable").dataTable().fnSettings().aoData.length;
			if(zeroData===0) {
					$('#readyToProcessTable_wrapper').hide();
					$('#tob-ready-process-wrap .no-data-found').show();
			} else {
				$('#tob-ready-process-wrap .no-data-found').hide();
			}
		}

		if(value=="trRetest") {
			//Load only for 1st div
			//reconstruct  data table
			var zeroData = $("#trRetestTable").dataTable().fnSettings().aoData.length;
			if(zeroData===0) {
					$('#trRetestTable_wrapper').hide();
					$('#tob-retest-wrap .no-data-found').show();
			} else {
				$('#tob-retest-wrap .no-data-found').hide();
			}
		}
	}

}
	function navigateToUpdate(materialCode, seid){
		document.getElementById('materialCode').value = materialCode;
		document.getElementById('seid').value = seid;
		var url=document.getElementById('technicalRegistrationUpdateAction').href;
		document.getElementById('technicalRegistrationForm').action=url;
        document.getElementById('technicalRegistrationForm').submit();
	}

	function navigateToTRConfig(accessType, materialCode, remainingQty) {

			if(parseInt(remainingQty) > 0){
				$(".loading-overlay").show();
				document.getElementById('materialCode').value = materialCode;
				var url=document.getElementById('technicalRegistrationConfigAction').href;
				document.getElementById('technicalRegistrationForm').action=url;
		        document.getElementById('technicalRegistrationForm').submit();
		        return true;
	        } else {
	        	closeNoConnectivityPopup();
	        	var alertKey=$('#alertRemQuanMinOneErrorCode').val();
				var alertMsg=$('#alertRemQuanMinOne').val();
	        	convertAlertToModelPopUp(alertKey, alertMsg);
	        	return false;
	        }

	}
	//<!-- GRT 4.0 Change -->
	function toggleEnable(val) {
		var connectivity=$('#connectivity').val();
		if(connectivity=="Yes" && val!=""){
			$("#submitAccesstype").prop('disabled', false);
			$("#submitAccesstype").removeClass('disabled').addClass('gray');
		}else{
			$("#submitAccesstype").prop('disabled', true);
			$("#submitAccesstype").addClass('disabled').removeClass('gray');
		}
		var accessType=$('#connectivity').val();
		if(accessType=="No Connectivity")
       {
			$("#submitAccesstype").prop('disabled', false);
			$("#submitAccesstype").removeClass('disabled').addClass('gray');
       }
		//Defect 140
		$("#msgSSLVPN").hide();
		if(val=="SSL/VPN" && connectivity !="Yes")
		{
		  $("#msgSSLVPN").show();
		  $("#submitAccesstype").prop('disabled', true);
		  $("#submitAccesstype").addClass('disabled').removeClass('gray');
		}
    }

	function showConnectivityPopUp(accessType, materialCode, remainingQty,count)
	{
			if(accessType=="Yes"){
				$("#submitAccesstype").prop('disabled', true);
				$("#submitAccesstype").addClass('disabled').removeClass('gray');
				$("#msgTR").hide();

				document.getElementById("popUpTopMsg").innerHTML = "You have opted for connectivity. Please select the access type";
			}
			else{
				$("#submitAccesstype").prop('disabled', false);
				$("#submitAccesstype").removeClass('disabled').addClass('gray');
				$("#msgTR").show();
				document.getElementById("popUpTopMsg").innerHTML = "You may continue without entering any connection details, however, please consider selecting an optional access type. This connection data will be saved for possible future use, in the event you decide to enable access at a later date.";
			}

			$('#popUpMaterialCode').val(materialCode);
			$('#popUpRemainingQty').val(remainingQty);
			$('#connectivity').val(accessType);
			var select = document.getElementById('accessTypeDropDown');
			for (var option in select){
			    select.remove(option);
			}

			$('#sslVpnConnection').val("Ok");

			populateAccessTypeForPopUp(materialCode,count);

	}

		function popupDropdownChange()
		{

			//$("#accessTypeDialog").dialog('close');
			$('#iframeDiv').contents().find("#accessTypeDialog").dialog('close');

			var accessType=$('#accessTypeDropDown').val();
			var materialCode=$('#popUpMaterialCode').val();
			var remainingQty=$('#popUpRemainingQty').val();
			var connectivity=$('#connectivity').val();

			if(accessType==""){
				$('#accessTypeDropDown').val(" ");
				accessType=" ";
			}

			if(connectivity=="No Connectivity"){

				if(parseInt(remainingQty) > 0){
					document.getElementById('materialCode').value = materialCode+",No Connectivity,"+accessType;
					var url=document.getElementById('technicalRegistrationConfigAction').href;
					document.getElementById('technicalRegistrationForm').action=url;
			        document.getElementById('technicalRegistrationForm').submit();
			        $(".loading-overlay").show();
			        return true;
		        } else {
		        	closeNoConnectivityPopup();
		        	var alertKey=$('#alertRemQuanMinOneErrorCode').val();
					var alertMsg=$('#alertRemQuanMinOne').val();
		        	convertAlertToModelPopUp(alertKey, alertMsg);
		        	return false;
		        }

			}else{
				navigateToTRConfig(accessType, materialCode, remainingQty);
			}



		}

		function closeNoConnectivityPopup()
		{
			$('.connectivtyChk').each(function() { //loop through each checkbox
                this.checked = false; //deselect all checkboxes with class "checkbox1"
            });
			$("#accessTypeDialog").hide();
		}

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

	function cancelTRSummary(){
		var confirmYesCallBackFn = "cancelConfirmTRSSummary()";
		var confirmNoCallBackFn = "return false;"
		convertConfirmToModelPopUp($("#cnfrmCancWillDeleteConfCode").val(), $("#cnfrmCancWillDelete").val(), confirmYesCallBackFn, confirmNoCallBackFn);
	}
	function cancelConfirmTRSSummary() {


		var url = document.getElementById("cancelTOBDashBoardAction");
		window.location = url;

		return true;
	}
	function cancelHomeAction(){
		var confirmYesCallBackFn = "cancelConfirmYes()";
		var confirmNoCallBackFn = "return false;"
		convertConfirmToModelPopUp($("#cnfrmCancWillDeleteConfCode").val(), $("#cnfrmCancWillDelete").val(), confirmYesCallBackFn, confirmNoCallBackFn);
	}


	function cancelConfirmYes() {
		var url = document.getElementById("cancelHomeAction");
		window.location = url;

		return true;
	}
	function confirmSubmit(){
		var checkFlag = 0;
		var i=0;
		var alertKey=$('#alertEnterAtlOneRecErrorCode').val();
		var alertMsg=$('#alertEnterAtlOneRec').val();


		//Check for TOB records
		var readyToProcessRows = $("#readyToProcessTable").dataTable().fnGetNodes();
		if(readyToProcessRows != undefined) {	
			for(var i=0; i < readyToProcessRows.length; i++) {
				var row = $(readyToProcessRows[i]);
				var checkbox =	row.find('input[type=checkbox][id^=process]');
				if( checkbox!=undefined && checkbox.is(':checked') ){
					checkFlag++;
				}
			}
		}
		
		/*START : handle retest scenario*/
		
		var retestRows = $("#trRetestTable").dataTable().fnGetNodes();
		var retestSelected = false;
		var retestRecFlag = true;
		if( retestRows!=undefined ){
			for(var i=0; i < retestRows.length; i++) {
				var row = $(retestRows[i]);
				var checkbox =	row.find('input[type=checkbox][id^=process]');
				if( checkbox!=undefined && checkbox.is(':checked') ){
					retestSelected = true;
					checkFlag++;
				}
			}
			if( retestRows!=undefined && retestSelected ){
				retestRecFlag = processBeforeSubmit();
				
			}
		}
		/*END : handle retest scenario*/


		if(checkFlag == 0){
			convertAlertToModelPopUp(alertKey, alertMsg);
			return false;
		} else if(retestRecFlag) {
			var confirmYesCallBackFn = "cancelConfirmSubmit()";
			var confirmNoCallBackFn = "return false;"
			convertConfirmToModelPopUp($("#cnfrmSubmitForTOBConfCode").val(), $("#cnfrmSubmitForTOB").val(), confirmYesCallBackFn, confirmNoCallBackFn);	
		}
	}
	function cancelConfirmSubmit() {
		$(".loading-overlay").show();
		var url=document.getElementById('submitTechRegSummary').href;
		document.getElementById('technicalRegistrationForm').action=url;
        document.getElementById('technicalRegistrationForm').submit();
		return true;
	}
	function submitTechnicalRegistrationSummary()
	{
		if(confirmSubmit())
		{
			 $("#trRetestTable").dataTable().fnDestroy();
			var url=document.getElementById('submitTechRegSummary').href;
			document.getElementById('technicalRegistrationForm').action=url;
	        document.getElementById('technicalRegistrationForm').submit();
		}
	}

	function navigateToTRConfigUpdate(index){
		document.getElementById('indexId').value = index;
		var url=document.getElementById('navigateToTRAction').href;
		document.getElementById('technicalRegistrationForm').action=url;
        document.getElementById('technicalRegistrationForm').submit();
	}

	function sortByField(field, list){
		var sortByCount = document.getElementById('sortByCount').value;
		var sortByScreen = 'TOB'+list;
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
		document.getElementById('sortByScreen').value = 'TOB'+list;
		var url = document.getElementById('dynamicSortByFieldAction').href;
		document.getElementById('technicalRegistrationForm').action=url;
		document.getElementById('technicalRegistrationForm').submit();
	}

	function determineSortSelection(){
		var count = document.getElementById('sortByCount').value;
		var field = document.getElementById('sortBy').value;
		var screen = document.getElementById('sortByScreen').value;
		setSortChange(screen, field, count);
	}

	function setSortChange(screen, field, count){
		if(screen == 'TOBlist1' || screen == 'TOBlist2' || screen == 'TOBlist3'){
			if(count == 1){
				document.getElementById(field+screen+'Sort').innerHTML = '<netui:image src="/grtWebProject/framework/skins/ptlSales/images/arrow_up.gif"/>';
			} else {
				document.getElementById(field+screen+'Sort').innerHTML = '<netui:image src="/grtWebProject/framework/skins/ptlSales/images/arrow_down.gif"/>';
			}
		} else {
			document.getElementById(field+screen+'Sort').innerHTML = '<netui:image src="/grtWebProject/framework/skins/ptlSales/images/arrow_down.gif"/>';
		}
	}

	//**Custom Written <!-- GRT 4.0 Change -->
	function backFromTOBDashBoard(){
		var url = document.getElementById("backFromTOBDashBoardAction").href;
		window.location =  url;
	}

//	<!-- GRT 4.0 Change -->
	$(function() {
		//Show Popup if message is not null
		var msg = $("input[type=hidden]#errMessage").val();
		var url = $("a#getTechnicalOrderDetails").attr('href');
		var actionFormErrMsg =  $("input[type=hidden]#actionFrmMsg").val();
		var tobDashboardDataError = $("input[type=hidden]#tobDashboardDataError").val();
		if( msg!=undefined && msg != ""){
			convertAlertToModelPopUpWithRedirect(msg, url);
		}else if( tobDashboardDataError!=undefined && tobDashboardDataError != ""){
			convertAlertToModelPopUpWithRedirect(tobDashboardDataError, url);
		}else if( actionFormErrMsg!=undefined && actionFormErrMsg!="" ){
			/*Commented below code for QA defect - Due to error msg merging*/
			if(actionFormErrMsg.indexOf('###') > 0) {
				convertAlertToModelPopUpServerSideMsg(actionFormErrMsg);
			} else {
				convertAlertToModelPopUp(actionFormErrMsg);
			}
		}

	  });
//	<!-- GRT 4.0 Change -->
	function CloseThisForm()
    {
		$("#accessTypeDialog").dialog('close');
    }

	//Re-Test Button Functionality
	function navigateToRetest(materialCode, seid){
		$("div.loading-overlay").show(); //show the loader
		document.getElementById('materialCode').value = materialCode;
		document.getElementById('seid').value = seid;
			var url=document.getElementById('technicalRegistrationRetestAction').href;
		document.getElementById('technicalRegistrationForm').action=url;
        document.getElementById('technicalRegistrationForm').submit();
	}

	$( document ).ready(function(){
		$("div.loading-overlay").hide();
	});


	//Retest validations
	function processBeforeSubmit(){
		var atLeastOneSelected = false;
		var i=0;
		var j=0;

		while(document.getElementById('ra'+i+'Select') != null) {

			var idOfRaCheckBox = "ra"+i+"Select";
			var idOfTaCheckBox = "ta"+i+"Select";
			var seid = document.getElementById(idOfRaCheckBox);


			var salRemoteAccessReqErrMsg = $("#salRemoteAccessReqErrMsg").val();
			var salRemoteAccessReqErrMsgCode = $("#salRemoteAccessReqErrMsgCode").val();


	        if (seid.checked) {
	           	atLeastOneSelected = true;
	        } else {
	         	var alarm = document.getElementById(idOfTaCheckBox);
	         	if(alarm.checked){
	         		convertAlertToModelPopUp(salRemoteAccessReqErrMsgCode, salRemoteAccessReqErrMsg);
	         		return false;
	         	}
	        }
	        // Verify the Child Records
	        while(document.getElementById( i + 'childRa'+ j +'Select' ) != null){
	        	var idOfRaChild = i + "childRa" + j + "Select";
	        	var idOdTaChild = i + "childTa" + j + "Select";
	        	var childSeid = document.getElementById(idOfRaChild);
	            if (childSeid.checked) {
		           	atLeastOneSelected = true;
	            } else {
	            	var childAlarm = document.getElementById(idOdTaChild);
	             	if(childAlarm.checked){
	             		convertAlertToModelPopUp(salRemoteAccessReqErrMsgCode, salRemoteAccessReqErrMsg);
	             		return false;
	             	}
	            }
	            j++;
	        }

	        j=0;

			i++;
		}

	    if (atLeastOneSelected == false) {
	    	var salSeleSeidRequiedMsg = $("#salSeleSeidRequiedMsg").val();
	    	var salSeleSeidRequiedMsgCode = $("#salSeleSeidRequiedMsgCode").val();
	        convertAlertToModelPopUp(salSeleSeidRequiedMsgCode, salSeleSeidRequiedMsg);
	        return false;
	    } 
	    
	    return true;
	}
