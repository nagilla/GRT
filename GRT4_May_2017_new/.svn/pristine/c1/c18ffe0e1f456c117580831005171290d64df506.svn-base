//Error Messages 

var eqmIncorrectFromSoldToErrMsg;
var eqmIncorrectToSoldToErrMsg;
var eqmIncorrectFromAndToSoldToErrMsg;
var eqmUnauthorizedSoldToErrMsg;
var eqmSameFromAndToSoldToErrMsg;

//Error Codes
var eqmIncorrectFromSoldToErrMsgCode;
var eqmIncorrectToSoldToErrMsgCode;
var eqmIncorrectFromAndToSoldToErrMsgCode;
var eqmUnauthorizedSoldToErrMsgCode;
var eqmSameFromAndToSoldToErrMsgCode;

$( document ).ready(function() {
	//Error Messages 

	eqmIncorrectFromSoldToErrMsg = $("#eqmIncorrectFromSoldToErrMsg").val();
	eqmIncorrectToSoldToErrMsg = $("#eqmIncorrectToSoldToErrMsg").val();
	eqmIncorrectFromAndToSoldToErrMsg = $("#eqmIncorrectFromAndToSoldToErrMsg").val();
	eqmUnauthorizedSoldToErrMsg = $("#eqmUnauthorizedSoldToErrMsg").val();
	eqmSameFromAndToSoldToErrMsg = $("#eqmSameFromAndToSoldToErrMsg").val();

	//Error Codes
	eqmIncorrectFromSoldToErrMsgCode = $("#eqmIncorrectFromSoldToErrMsgCode").val();
	eqmIncorrectToSoldToErrMsgCode = $("#eqmIncorrectToSoldToErrMsgCode").val();
	eqmIncorrectFromAndToSoldToErrMsgCode = $("#eqmIncorrectFromAndToSoldToErrMsgCode").val();
	eqmUnauthorizedSoldToErrMsgCode = $("#eqmUnauthorizedSoldToErrMsgCode").val();
	eqmSameFromAndToSoldToErrMsgCode = $("#eqmSameFromAndToSoldToErrMsgCode").val();

});

$(function(){
	var EqmSoldToSelect = Backbone.View.extend({
		el : $("#locationWrapper"),
		
		initialize : function() {
			var self = this;
			
			var loadingOverlay = $(".loading-overlay");
			loadingOverlay.hide(); //hide loader by default
		
			//Initialize the autocomplete
			if( this.isEmployee() == false ){ //Sold to autocomplete is only for BP and Customer
				this.el.find('.search-fromsoldto-input').keyup(this.searchFromSoldToOptions);
				this.el.find('.search-tosoldto-input').keyup(this.searchToSoldToOptions);
			}
		},
		
		closeErrorPopup : function(){
			this.el.find('.error-message').hide();
		},
		
		setSoldToValue : function(){
			var fromSoldTo = $.trim(this.el.find('#fromSoldTo').val()).split(" ")[0];
			var toSoldTo = $.trim(this.el.find('#toSoldTo').val()).split(" ")[0];
			this.el.find("input#fromSoldToHidden").val( $.trim(fromSoldTo) );
			this.el.find("input#toSoldToHidden").val( $.trim(toSoldTo) );
			return true;
		},
		
		redirectToHomePage : function(){
			window.location.href = _path+"/home-action.action";
		},
		
		checkSoldToCountryAndSubmit : function(){
			var self = this;
			self.closeUserTypePopup();
			//Get the soldto
			self.setSoldToValue();
			var fromSoldTo =  self.el.find("input#fromSoldToHidden").val();
			var toSoldTo = self.el.find("input#toSoldToHidden").val();
			//Sold to validation
			if( fromSoldTo == toSoldTo ){
				convertAlertToModelPopUp(eqmSameFromAndToSoldToErrMsgCode, eqmSameFromAndToSoldToErrMsg);
				return false;
			}
			if( self.isEmployee() == false ){ //For Employee it is already checked in other method
				if( self.sameSoldToValidation() == false ){
				     return false;	
				}
			}
			
			$("div.loading-overlay").show();
			$.ajax({
				url : _path+'/equipmentmove/json/isSameCountry',
				dataType : 'json',
				type: 'post',
				data : {'actionForm.eqmFromSoldTo':fromSoldTo, 'actionForm.eqmToSoldTo':toSoldTo},
				success : function( data ){
					if( data!=undefined && data.isSameCountry=="fromError" ){
						convertAlertToModelPopUp(eqmIncorrectFromSoldToErrMsgCode, eqmIncorrectFromSoldToErrMsg);
						$("div.loading-overlay").hide();
						return false;
					}else if( data!=undefined && data.isSameCountry=="toError" ){
						convertAlertToModelPopUp(eqmIncorrectToSoldToErrMsgCode, eqmIncorrectToSoldToErrMsg);
						$("div.loading-overlay").hide();
						return false;
					}
					else{
					if( data!=undefined && data.isSameCountry=="Ok" ){
						$("#soldToId").val( fromSoldTo ); //set sold to action form
						if( self.isEmployee() ){ //Employee Flow
							self.proceedToSCVForAgent();
						}else{ //Customer & BP Flow
							self.proceedToSCV();
						}
					}
					if(data!=undefined && data.isSameCountry=="FromTo" ){
						convertAlertToModelPopUp(eqmUnauthorizedSoldToErrMsgCode, eqmUnauthorizedSoldToErrMsg);
						$("div.loading-overlay").hide();
					}
					if(data!=undefined && data.isSameCountry=="From" ){
						convertAlertToModelPopUp(eqmIncorrectFromSoldToErrMsgCode, eqmIncorrectFromSoldToErrMsg);
						$("div.loading-overlay").hide();
					}
					if(data!=undefined && data.isSameCountry=="To" ){
						convertAlertToModelPopUp(eqmIncorrectToSoldToErrMsgCode, eqmIncorrectToSoldToErrMsg);
						$("div.loading-overlay").hide();
					}
					if(data!=undefined && data.isSameCountry=="FromAuth" ){
						var soldTo=$("#fromSoldTo").val();
						convertAlertToModelPopUp(eqmIncorrectFromAndToSoldToErrMsgCode, soldTo+' '+eqmIncorrectFromAndToSoldToErrMsg);
						$("div.loading-overlay").hide();
					}
					if(data!=undefined && data.isSameCountry=="ToAuth" ){
						var soldTo=$("#toSoldTo").val();
						convertAlertToModelPopUp(eqmIncorrectFromAndToSoldToErrMsgCode, soldTo+' '+eqmIncorrectFromAndToSoldToErrMsg);
						$("div.loading-overlay").hide();
					}
					}
				},
	            error : function(data){
	            	//Hide the loader
	            	$("div.loading-overlay").hide();
	            	return false;
	            }
			});
		},
		
		//Submit the page and proceed to site contact validation for BP and Customers
		proceedToSCV : function() {
			this.submitFormWithUrl('newRegistrationWithSoldToValidation');
		},
		
		//Submit the page and proceed to site contact validation for Employee
		proceedToSCVForAgent : function(){
			this.submitFormWithUrl( 'newRegistrationWithSoldToValidationForAgentAction' );
		},
		
		//Generic method for form submit
		submitFormWithUrl : function( urlID ){
			if( urlID!=undefined ){
				var url = document.getElementById( urlID ).href;
				document.getElementById('eqpMoveSoldToForm').action=url;
				document.getElementById('eqpMoveSoldToForm').submit();
			}
		},
		
		closeUserTypePopup : function(){
			this.el.find("#usertype-popup").hide();
		},
		
		isEmployee : function(){
			var userType = this.el.find('input#userType');
			//Initialize the autocomplete
			if( userType == undefined || userType.val()!='employee' ){ 
				return false;
			}
			return true;
		},
		
		sameSoldToValidation : function(){
			//Get the soldto
			this.setSoldToValue();
			var fromSoldTo = $.trim($("input#fromSoldToHidden").val());
			var toSoldTo = $.trim($("input#toSoldToHidden").val());
			//Sold to validation
			if( fromSoldTo == toSoldTo ){
				convertAlertToModelPopUp(eqmSameFromAndToSoldToErrMsgCode, eqmSameFromAndToSoldToErrMsg);
				return false;
			}
			return true;
		},
		
		searchFromSoldToOptions : function() {
			searchSoldToOptions($("#fromSoldTo"), 'fromSoldTo');
		},
		
		searchToSoldToOptions : function() {
			searchSoldToOptions($("#toSoldTo"), 'toSoldTo');
		},
		
		events : {
			'click .errPopupBtn' : 'closeErrorPopup',
			'click input[type=button].cancelBtn' : 'redirectToHomePage',
			'click input#eqpMoveNext' : 'checkSoldToCountryAndSubmit',
			'click input#agentProceed' : 'checkSoldToCountryAndSubmit',
			'click .closeUserTypeImg' : 'closeUserTypePopup',
			'click #searchAgent'   : 'checkSoldToCountryAndSubmit'
		}
		
	});
	
	eqmSoldToSelect = new EqmSoldToSelect();
	
});

function show_popup(regId) {	
	document.getElementById("dialog-underlay").style.display = "block";
	document.getElementById("popup-window").style.left = "auto";
	document.getElementById("regId").innerHTML = regId;
}

function closePopup() {
	document.getElementById("dialog-underlay").style.display = "none";
	document.getElementById("popup-window").style.left = "-1000em";
}

/* START : Employee UserType Selection Popup */
function show_usertypepopup() {	
	if( eqmSoldToSelect.sameSoldToValidation() == false ){
	     return false;	
	}
	document.getElementById("usertype-popup").style.display = "block";
	document.getElementById("fsoldtoMsg").innerHTML = "  "+document.getElementById("fromSoldTo").value;
	document.getElementById("tsoldtoMsg").innerHTML = "  "+document.getElementById("toSoldTo").value;
}

function closeusertypePopup() {
	$("usertype-popup").hide();
	//Show Loading Popup
	$('div.customer-site-registration div.loading-overlay').show();
}
/* END : Employee UserType Selection Popup */

//Set the user role value for employee
function checkOption(option){
	if(option=='BP'){		
		document.getElementById('userRole').value="BP"; 
	}
	else{
		document.getElementById('userRole').value="C"; 
	}

}

$(document).ready(function(){
 /* START :  Enable/Disable Next Button */
	$("#fromSoldTo, #toSoldTo").keyup(function(){
		toggleNextBtn();
	});

	$("form#eqpMoveSoldToForm").mouseover(function(){
		toggleNextBtn();
	});
	
	function toggleNextBtn(){
		//Chat Phase 2 : Commenting due to co-browse issue on agent as keyup events are not propagated.
		/*var fromSoldTo = $.trim($('#fromSoldTo').val());
		var toSoldTo = $.trim($('#toSoldTo').val());
		if( fromSoldTo!="" && toSoldTo!="" ){
			$('.nextBtn').removeAttr('disabled');
			$('.nextBtn').addClass('gray');
			$('.nextBtn').removeClass('disabled');
		}else{
			$('.nextBtn').attr('disabled',true);
			$('.nextBtn').removeClass('gray');
			$('.nextBtn').addClass('disabled');
		}*/
	}
/* END :  Enable/Disable Next Button */
});

function searchSoldToOptions(soldToField, soldToType) {
	var soldTo = soldToField.val();
	if(soldTo && soldTo.length >= 2 ) {
		var url = _path + "/technicalregsitration/json/getCxpSoldToList.action?soldTo="+soldTo;
		$.ajax({
			url: url,
			dataType: 'json',
			headers: {
				'Content-type': 'application/x-www-form-urlencoded'
			},
			success: function (data, textStatus, xhr) {
				soldToCallBackFunction(xhr, soldToType);
			},
			error: function (xhr, textStatus, errorThrown) {
				console.log(errorThrown);
			}
		});
	} else {
		var elementRemove = $("ul.ui-autocomplete");
		if(elementRemove) {
			$(elementRemove).remove();
		}
	}
}

function soldToCallBackFunction(http_request, soldToType) {
	if (http_request.readyState == 4) {
		if (http_request.status == 200) {
			var responceString = http_request.responseText;
			var dataStart = responceString.indexOf('["');
			var dataEnd = responceString.indexOf('"]');
			var searchData = responceString.substring(dataStart + 1, dataEnd + 1);
			var HTMLdata = "";
			if(soldToType == 'fromSoldTo') {
				HTMLdata = '<ul class="ui-autocomplete ui-menu ui-widget ui-widget-content ui-corner-all global-auto-complete" role="listbox" aria-activedescendant="ui-active-menuitem" style="z-index: 1; top: -333px; left: 265px; display: block; position: relative; width: 225px;">';
			} else {
				HTMLdata = '<ul class="ui-autocomplete ui-menu ui-widget ui-widget-content ui-corner-all global-auto-complete" role="listbox" aria-activedescendant="ui-active-menuitem" style="z-index: 1; top: -294px; left: 249px; display: block; position: relative; width: 225px;">';
			}
			if(searchData.length > 0) {
				var searchDataArray = searchData.split('","'); // remove the double quotes for first and last array
				searchDataArray[0] = searchDataArray[0].replace('"', '');
				searchDataArray[searchDataArray.length - 1] = searchDataArray[searchDataArray.length - 1].replace('"', '');
				
				for (var i = 0; i < searchDataArray.length; i++) {
					HTMLdata = HTMLdata	+ '<li class="ui-menu-item" role="menuitem"><a class="ui-corner-all" tabindex="-1" onclick="setSelectedSoldToValue(\'' + searchDataArray[i] + '\', \'' + soldToType + '\')">' + searchDataArray[i] + '</a></li>';
				}
			} else {
				HTMLdata = HTMLdata	+ '<li class="ui-menu-item" role="menuitem"><a class="ui-corner-all" tabindex="-1">No Results Found</a></li>';
			}
			HTMLdata = HTMLdata + "</ul>";
			
			if(soldToType == 'fromSoldTo') {
				var elementRemove = $("ul.ui-autocomplete");
				if(elementRemove) {
					$(elementRemove).remove();
				}
				$('body').append(HTMLdata);
			} else {
				var elementRemove = $("ul.ui-autocomplete");
				if(elementRemove) {
					$(elementRemove).remove();
				}
				$('body').append(HTMLdata);
			}
			
		} else {
			//do nothing
		}
	}
}

function setSelectedSoldToValue(t, soldToType) {
	if(soldToType == 'fromSoldTo') {
		$("#fromSoldTo").val(t);
		$(".ui-autocomplete").hide();
	} else {
		$("#toSoldTo").val(t);
		$(".ui-autocomplete").hide();
	}
}

$(document).click(function() {
	$(".ui-autocomplete").hide();
});