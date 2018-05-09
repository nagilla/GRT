function show_popup(regId) {	
	//document.getElementById("dialog-underlay").style.display = "block";
	document.getElementById("popup-window").style.left = "auto";
	document.getElementById("regId").innerHTML = regId;
}

function closePopup() {
	//document.getElementById("dialog-underlay").style.display = "none";
	document.getElementById("popup-window").style.left = "-1000em";
}
function show_usertypepopup() {	

	//document.getElementById("dialog-underlay").style.display = "block";
	//document.getElementById("usertype-popup").style.left = "auto";
	document.getElementById("usertype-popup").style.display = "block";
	document.getElementById("userMessage").innerHTML="  "+document.getElementById("agentSoldToStorer").value;
}

function closeusertypePopup() {
	//document.getElementById("dialog-underlay").style.display = "none";
	document.getElementById("usertype-popup").style.left = "-1000em";
	
	//Show Loading Popup
	$('div.customer-site-registration div.loading-overlay').show();
}

function checkOption(option){

	if(option=='BP'){		
		document.getElementById('userRole').value="BP"; 
		//document.getElementById('almId').disabled=true; 
	}
	else{
		document.getElementById('userRole').value="C"; 
		//document.getElementById('almId').disabled=false; 
	}

}

function getFirstSiteRegistrationIdIfStatusIsInProgress() {
	var radioObj = document.forms[0].elements['optype'];
	if( radioObj == undefined ) radioObj = $("input[type=radion][name=optype]"); //For New UI
	var radioValue = "";
	for(var i=0;i<radioObj.length;i++){
		if(radioObj[i].checked){
			document.getElementById('userRole').value = radioObj[i].value;    
		}
	}
	//alert('User role :'+document.getElementById('userRole').value);
	//alert('Registration Type :'+document.getElementById('installbaseRegistrationOnly').value);
	//alert(document.getElementById('viewInstallbaseOnly').value);
	var soldToValue = $('#agentSoldToStorer').val();
	soldToValue = $.trim(soldToValue);
	$('#agentSoldToStorer').val(soldToValue);//set the sold-to after trimming
	$("div.loading-overlay").show();
	var url = document.getElementById('newRegistrationWithSoldToValidationForAgentAction').href;
	document.getElementById('myForm').action=url;
	document.getElementById('myForm').submit();
}

function viewinstallbase() {
	if(document.getElementById('viewInstallbaseOnly').value == 'true') {
			$("div.loading-overlay").show();
			var url = document.getElementById('viewInstallBase').href;
			document.getElementById('myForm').action=url;
			document.getElementById('myForm').submit();
			//alert('Install Base Creation is not supported for Direct Customer.');
	}
}

function noEnter(event) {
	if(event && event.keyCode == 13){
		return false;
	} else {
		return true;
	}
}

function clearErrorMessage(){
	//document.getElementById('errorDiv').innerHTML = '';
}

$(function(){
	$('div.customer-site-registration div.loading-overlay').hide(); //hide loader by default
	var AgentLocationSelection = Backbone.View.extend({
		el : $(".pageWrapper"),
		
		closeErrorPopup : function(){
			this.el.find('.error-message').hide();
		},
		
		closeUserTypePopup : function(){
			this.el.find("#usertype-popup").hide();
		},
		
		redirectToHomePage : function(){
			window.location.href = _path+"/home-action.action";
		},
		
		events : {
			'click .errPopupBtn' : 'closeErrorPopup',
			'click .closeUserTypeImg' : 'closeUserTypePopup',
			'click input[type=button][value=Cancel]' : 'redirectToHomePage'
		}
		
	});
	
	agentlocationSelection = new AgentLocationSelection();
	
});

//GRT 4.0 Changes
function setEnableNextButton(val)
{
	//Chat Phase 2 : Enable next button by default
	/*if(val.value!="")
	{
		document.getElementById("searchAgent").className = "button gray";
		document.getElementById("searchAgent").disabled = false;
	}else{
		document.getElementById("searchAgent").className = "button";
		document.getElementById("searchAgent").disabled = true;
	}*/
	
}

$(document).ready(function(){
	$("#agentSoldToStorer").keyup(function(){
		var soldToValue = $(this).val();
		//Chat Phase 2 : Enable next button by default
		/*if( $.trim(soldToValue)!="" ){
			$('#searchAgent').removeAttr('disabled');
			$('#searchAgent').addClass('gray');
		}else{
			$('#searchAgent').attr('disabled',true);
			$('#searchAgent').removeClass('gray');
		}*/
	});
	
/*	$("#agentSoldToStorer").mousemove(function(){
		var soldToValue = $(this).val();
		if( soldToValue!="" ){
			$('#searchAgent').removeAttr('disabled');
			$('#searchAgent').addClass('gray');
		}else{
			$('#searchAgent').attr('disabled',true);
			$('#searchAgent').removeClass('gray');
		}
	});
*/	
	$("form#myForm").mouseover(function(){
		var soldToValue = $('#agentSoldToStorer').val();
		//Chat Phase 2 : Enable next button by default
		/*if( $.trim(soldToValue)!="" ){
			$('#searchAgent').removeAttr('disabled');
			$('#searchAgent').addClass('gray');
		}else{
			$('#searchAgent').attr('disabled',true);
			$('#searchAgent').removeClass('gray');
		}*/
	});
});