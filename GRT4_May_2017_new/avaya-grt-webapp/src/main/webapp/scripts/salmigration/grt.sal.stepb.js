/**
 * 
 */

$(function() {
	//Show Popup if message is not null
	var msg = $("input[type=hidden]#message").val();
	var url = $("a#techOrderUrl").attr('href');
	if( msg!=undefined && msg != ""){
	  createSuccFailPopup(msg, url);
	}
	  
	var SALStepB = Backbone.View.extend({

		el : $('#saveSiteRegistrationForm'),

		createInstallBase : function() {
			$.ajax({
				url : _path + "" ,
				dataType : 'json',
				success : function( data, ui ) {
					
				},
				error : function( error ){
					var salSomethingWrongErrMsg = $("#salSomethingWrongErrMsg").val();
					var salSomethingWrongErrMsgCode = $("#salSomethingWrongErrMsgCode").val();
					convertAlertToModelPopUp(salSomethingWrongErrMsgCode, salSomethingWrongErrMsg);
				}
			});
		},

		events : {
			'click .scvNextBtn' : 'createInstallBase',
		},

	});

	salStepB = new SALStepB();

});

function showSALGatewayPopup(message) {
	var salConcentratorPopup = window.open('', '_blank', 'width=600, height=450, toolbar=no, resizable=yes,menubar=yes');
	//salConcentratorPopup.document.title = 'Recent Devices';
	salConcentratorPopup.document.write(message);
}

function showSALGatewayDetails(seid, soldTo) {
  	var gatewaySeid =  seid;
	var soldToId = soldTo;
		$.ajax({
			url : _path+'/technicalonboarding/json/getSALGatewayDetails.action?gatewaySeid='+gatewaySeid+'&soldToId='+soldToId,
			dataType : 'json',
			success : function(data) {
				if(data) {
					showSALGatewayPopup(data);
				}
			}
		});
}

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
    } else {
    	// User selected atleast one SEID
    	var salValidateConnectivityConfirmMsg = $("#salValidateConnectivityConfirmMsg").val();
    	var salValidateConnectivityConfirmMsgCode = $("#salValidateConnectivityConfirmMsgCode").val();        
    	var callBackMethodStringForOk = "confirmValidateConnectivityOK()";
		var callBackMethodStringForCancel ="";
		convertConfirmToModelPopUp(salValidateConnectivityConfirmMsgCode, salValidateConnectivityConfirmMsg, callBackMethodStringForOk, callBackMethodStringForCancel );
		return false;
    	
    	/*var userInput = confirm(salValidateConnectivityConfirmMsg);
        if(userInput == false){
        	return false;
        }*/
    }
    
    $("div.loading-overlay").show();
    return true;
}

function confirmValidateConnectivityOK() {
	$("div.loading-overlay").show();
	document.getElementById('salStepB').action = document.getElementById('submitSalAlarmConnectivityDetailsAction').href;
	document.getElementById('salStepB').submit();
	return false;
}

function cancelOnClick()
{
	$("div.loading-overlay").show();
}
