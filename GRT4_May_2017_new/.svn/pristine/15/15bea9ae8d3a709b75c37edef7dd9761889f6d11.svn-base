jQuery(function($) {
	$("div.loading-overlay").hide(); //hide loader
	$.mask.definitions['~'] = '[+-]';
	$('#date').mask('99/99/9999');
	$('#phone').mask('(999) 999-9999');
	$('#phoneext').mask("(999) 999-9999? ext. 99");
	$("#tin").mask("99-9999999");
	$("#ssn").mask("999-99-9999");
	$("#product").mask("a*-999-a999", {
		placeholder : " ",
		completed : function() {
			var scvTypedTextMsg = $("#scvTypedTextMsg").val();
			var scvTypedTextMsgCode = $("#scvTypedTextMsgCode").val();
			convertAlertToModelPopUp(scvTypedTextMsgCode, scvTypedTextMsg + this.val());
		}
	});
	$("#eyescript").mask("~9.99 ~9.99 999");

	// $('#cuttimendate').mask('99/99/9999');
	// $('#onsitePhone').mask("+999 (999) 999-9999? ext. 99");
	// $('#sitePhone').mask("+999 (999) 999-9999? ext. 99");
	/* Select the state in the dropdown */
	var defaultState = $("#stateSelectHidden").val();
	if( !defaultState ) defaultState = "Maryland";
	$('#stateSelect option[value='+ defaultState +']').attr("selected","selected");

});

// Declaring required variables
var digits = "0123456789";
// non-digit characters which are allowed in phone numbers
var phoneNumberDelimiters = "()- ";
// characters which are allowed in international phone numbers
// (a leading + is OK)
var validWorldPhoneChars = phoneNumberDelimiters + "+";
// Minimum no of digits in an international phone no.
var minDigitsInIPhoneNumber = 10;
function copysiteaddress() {
	document.getElementById("onsiteFirstName").value = document
			.getElementById("firstName").value;
	document.getElementById("onsiteLastName").value = document
			.getElementById("lastName").value;
	document.getElementById("sitePhone").value = document
			.getElementById("onsitePhone").value;
	document.getElementById("onsiteEmailId").value = document
			.getElementById("emailId").value;
}
function copyPhone() {
	var sameAsAb = document.getElementById('sameAsAbove').checked;
	if (sameAsAb) {
		document.getElementById("sitePhone").value = document
				.getElementById("onsitePhone").value;
	}
}
function validateZIP(field) {
	var valid = "0123456789-";
	var hyphencount = 0;

	if (field.length != 5 && field.length != 10) {
		var scvZipLengthErrMsg = $("#scvZipLengthErrMsg").val();
		var scvZipLengthErrMsgCode = $("#scvZipLengthErrMsgCode").val();
		convertAlertToModelPopUp(scvZipLengthErrMsgCode, scvZipLengthErrMsg);
		return false;
	}
	for (var i = 0; i < field.length; i++) {
		temp = "" + field.substring(i, i + 1);
		if (temp == "-")
			hyphencount++;
		if (valid.indexOf(temp) == "-1") {
			var scvInvalidCharInZipErrMsg = $("#scvInvalidCharInZipErrMsg").val();
			var scvInvalidCharInZipErrMsgCode = $("#scvInvalidCharInZipErrMsgCode").val();
			convertAlertToModelPopUp(scvInvalidCharInZipErrMsgCode, scvInvalidCharInZipErrMsg);
			return false;
		}
		if ((hyphencount > 1)
				|| ((field.length == 10) && "" + field.charAt(5) != "-")) {
			var scvZipCodeFormaterrMsg = $("#scvZipCodeFormaterrMsg").val();
			var scvZipCodeFormaterrMsgCode = $("#scvZipCodeFormaterrMsgCode").val();
			convertAlertToModelPopUp(scvZipCodeFormaterrMsgCode, scvZipCodeFormaterrMsg);
			return false;
		}
	}
	return true;
}

function isInteger(s) {
	var i;
	for (i = 0; i < s.length; i++) {
		// Check that current character is number.
		var c = s.charAt(i);
		if (((c < "0") || (c > "9")))
			return false;
	}
	// All characters are numbers.
	return true;
}
function trim(s) {
	var i;
	var returnString = "";
	// Search through string's characters one by one.
	// If character is not a whitespace, append to returnString.
	for (i = 0; i < s.length; i++) {
		// Check that current character isn't whitespace.
		var c = s.charAt(i);
		if (c != " ")
			returnString += c;
	}
	return returnString;
}
function stripCharsInBag(s, bag) {
	var i;
	var returnString = "";
	// Search through string's characters one by one.
	// If character is not in bag, append to returnString.
	for (i = 0; i < s.length; i++) {
		// Check that current character isn't whitespace.
		var c = s.charAt(i);
		if (bag.indexOf(c) == -1)
			returnString += c;
	}
	return returnString;
}

function checkInternationalPhone(strPhone) {
	// var bracket=3
	// strPhone=trim(strPhone)
	// if(strPhone.indexOf("+")>1) return false
	// if(strPhone.indexOf("-")!=-1)bracket=bracket+1
	// if(strPhone.indexOf("(")!=-1 && strPhone.indexOf("(")>bracket)return
	// false
	// var brchr=strPhone.indexOf("(")
	// if(strPhone.indexOf("(")!=-1 && strPhone.charAt(brchr+2)!=")")return
	// false
	// if(strPhone.indexOf("(")==-1 && strPhone.indexOf(")")!=-1)return false
	// s=stripCharsInBag(strPhone,validWorldPhoneChars);
	// return (isInteger(s) && s.length >= minDigitsInIPhoneNumber);
	strPhone = strPhone.replace(/(-|\(|\)|\+|\s|ext.)/g, "");
	var reg1 = /^[0-9]+$/;
	return reg1.test(strPhone);
}

function validatePhoneNumber(Phone, flag) {
	if ((Phone == null) || (Phone == "")) {
		if (flag == 0) {
			var scvEmptyGrtNotiPhNoErrMsg = $("#scvEmptyGrtNotiPhNoErrMsg").val();
			var scvEmptyGrtNotiPhNoErrMsgCode = $("#scvEmptyGrtNotiPhNoErrMsgCode").val();
			convertAlertToModelPopUp(scvEmptyGrtNotiPhNoErrMsgCode, scvEmptyGrtNotiPhNoErrMsg);
		} else {
			var scvEmptyPhNoErrMsg = $("#scvEmptyPhNoErrMsg").val();
			var scvEmptyPhNoErrMsgCode = $("#scvEmptyPhNoErrMsgCode").val();
			convertAlertToModelPopUp(scvEmptyPhNoErrMsgCode, scvEmptyPhNoErrMsg);
		}
		return false;
	}
	if (checkInternationalPhone(Phone) == false) {
		if (flag == 0) {
			var scvInvalidGrtNotiPhNoErrMsg = $("#scvInvalidGrtNotiPhNoErrMsg").val()
			var scvInvalidGrtNotiPhNoErrMsgCode = $("#scvInvalidGrtNotiPhNoErrMsgCode").val()
			convertAlertToModelPopUp(scvInvalidGrtNotiPhNoErrMsgCode, scvInvalidGrtNotiPhNoErrMsg);
		} else {
			var scvInvalidPhNoErrMsg = $("#scvInvalidPhNoErrMsg").val();
			var scvInvalidPhNoErrMsgCode = $("#scvInvalidPhNoErrMsgCode").val();
			convertAlertToModelPopUp(scvInvalidPhNoErrMsgCode, scvInvalidPhNoErrMsg);
		}
		return false;
	}
	return true;
}

function openCreditCardInfo(value) {
	if (value == 'Yes') {
		var url = '<%=request.getContextPath()%>/pages/registration/creditCardRegistration.portlet';
		window.open(url, "newRegistrationWindow",
				'width=600,height=200,location=no,menubar=no,resizable=no');
	} else {
		try {
			document.getElementById("expediteRefNo").value = "";
			document.getElementById("expediteDiv").style.visibility = "hidden";
		} catch (e) {
		}
	}
}

function phonenumber(inputtxt) {
	  var phoneno = /^\+?([0-9]{2})\)?[-. ]?([0-9]{4})[-. ]?([0-9]{4})$/;
	  if(inputtxt!=""){
		  if(inputtxt.match(phoneno)) {
		    return true;
		  }  
		  else {  
			  var scvInvalidPhoneNoErrMsg = $("#scvInvalidPhoneNoErrMsg").val();
			  var scvInvalidPhoneNoErrMsgCode = $("#scvInvalidPhoneNoErrMsgCode").val();
			  convertAlertToModelPopUp(scvInvalidPhoneNoErrMsgCode, scvInvalidPhoneNoErrMsg);
		    
		    return false;
		  }
	  }else{
		  return true;
	  }
	}

// Validate mandatory fields.
function validate() {
	var emailId = document.getElementById("emailId").value;
	var onsiteEmailId = document.getElementById("onsiteEmailId").value;
	// var phone = document.getElementById("companyPhoneNo").value;
	// var zipCode = document.getElementById("ZipCheck").value;
	var onsitePhone = document.getElementById("onsitePhone").value;
	var sitePhone = document.getElementById("sitePhone").value;
	var firstName = document.getElementById('firstName').value;
	var lastName = document.getElementById('lastName').value;
	var onsiteFirstName = document.getElementById('onsiteFirstName').value;
	var onsiteLastName = document.getElementById('onsiteLastName').value;
	// var cuttimendate = document.getElementById('cuttimendate').value;
	var registrationNotes = document.getElementById('registrationNotes').value;
	var registrationIdentifier = document
			.getElementById('registrationIdentifier').value;
	var notesObject = document.getElementById('registrationNotes');
	var maxlen = 4000;
	// document.getElementById("companyPhoneNo").value = replaceChars(phone) ;
	var scvEmptyNotifEmailErrMsg = $("#scvEmptyNotifEmailErrMsg").val();
	var scvEmptyNotifEmailErrMsgCode = $("#scvEmptyNotifEmailErrMsgCode").val();
	var scvEmptyNotifEmailErrMsgCodeCombined = scvEmptyNotifEmailErrMsgCode + "###" + scvEmptyNotifEmailErrMsg;
	if (validateRequired(onsiteEmailId, scvEmptyNotifEmailErrMsgCodeCombined)
			&& splitMails(onsiteEmailId)
			&& validateNames(firstName, lastName, onsiteFirstName,
					onsiteLastName)
			&& checkExpedaite()
			&& textLimit(notesObject, maxlen)) { // &&checkDate(cuttimendate)
													// && validateZIP(zipCode))
													// {
		/*if(phonenumber(onsitePhone) && phonenumber(sitePhone)){
			document.getElementById("onsitePhone").value = replaceChars(onsitePhone);
			document.getElementById("sitePhone").value = replaceChars(sitePhone);
		}else{
			return false;
		}*/
		//Restructure
		var url = document.getElementById('saveSiteRegAction').href;
		document.getElementById("saveSiteRegistrationForm").action = url;
		$("div.loading-overlay").show();
		document.getElementById("saveSiteRegistrationForm").submit();
		
		//Show Popup
		//$("div.loading-overlay").show();
	} else {
		// alert("Invalid Data ...........Coming to Else block.....Should not
		// submit");
		return false;
	}
	
	

}

// Begin: Hour Glass Implementation
function show_popup(msg, title) {
	/*document.getElementById("dialog-underlay").style.display = "block";
	if (title) {
		document.getElementById("popup-window-title").innerHTML = title;
	}
	document.getElementById("popup-window-content").innerHTML = msg;

	var elePopup = document.getElementById("popup-window");
	var scroll = getScroll();
	var divH = getLength(elePopup.style.height, scroll.h);
	var divW = getLength(elePopup.style.width, scroll.w);
	elePopup.style.top = (scroll.t + (scroll.ch - divH) / 2) + "px";
	elePopup.style.left = "auto";*/
	//Custom implementation for New UI
	
}

function close_popup() {
	document.getElementById("dialog-underlay").style.display = "none";
	document.getElementById("popup-window").style.left = "-1000em";
}
// End: Hour Glass Implementation

function checkExpedaite() {
	var scvPurchaseOrderReqErrMsg = $("#scvPurchaseOrderReqErrMsg").val();
	var scvPurchaseOrderReqErrMsgCode = $("#scvPurchaseOrderReqErrMsgCode").val();
	// Expidite is available only for BP and Customer
	if (document.getElementById('expedaiteSelect')) {
		var expedaiteSelect = document.getElementById('expedaiteSelect').value;
		var expediteRefNo = document.getElementById('expediteRefNo').value;
		var poentered = document.getElementById('poentered').value;
		if (expedaiteSelect == 'Yes') {
			if (poentered == 'true' || replaceChars(expediteRefNo) != '') {

			} else {
				convertAlertToModelPopUp(scvPurchaseOrderReqErrMsgCode, scvPurchaseOrderReqErrMsg);
				return false;
			}
		}
	}
	return true;

}

function replaceChars(val) {
	return val.replace(/(-|\(|\)|\s|ext.)/g, "");
}

// Validate names fields
function validateNames(firstName, lastName, onsiteFirstName, onsiteLastName) {
	// if(firstName==''){alert("${bundle.msg.validationFirstNameRequired}");
	// return false;}
	// if(lastName==''){alert("${bundle.msg.validationLastNameRequired}");
	// return false;}
	if (isValueEmptyAfterTrimed(onsiteFirstName)) {
		var scvEmptyFirstNameErrMsg = $("#scvEmptyFirstNameErrMsg").val();
		var scvEmptyFirstNameErrMsgCode = $("#scvEmptyFirstNameErrMsgCode").val();
		convertAlertToModelPopUp(scvEmptyFirstNameErrMsgCode, scvEmptyFirstNameErrMsg);
		return false;
	}
	if (isValueEmptyAfterTrimed(onsiteLastName)) {
		var scvEmptyFirstLastErrMsg = $("#scvEmptyFirstLastErrMsg").val();
		var scvEmptyFirstLastErrMsgCode = $("#scvEmptyFirstLastErrMsgCode").val();
		convertAlertToModelPopUp(scvEmptyFirstLastErrMsgCode, scvEmptyFirstLastErrMsg);
		return false;
	}
	return true;
}

// Function to split Multiple E-mails by semi-colons
function splitMails(val) {
	var str = '';
	var mails = val.split(';');
	for (i = 0; i < mails.length; i++) {
		var str = mails[i].replace(/\s/g, "");
		if ((str) != '') {
			if (!validateEmailId(str)) {
				var scvInvalidEmailErrMsg = $("#scvInvalidEmailErrMsg").val();
				var scvInvalidEmailErrMsgCode = $("#scvInvalidEmailErrMsgCode").val();
				convertAlertToModelPopUp(scvInvalidEmailErrMsgCode, scvInvalidEmailErrMsg + ' ' + str);
				return false;
			}
		}

	}
	return true;
}

function validateRequired(value, errMsg) {
	if (isValueEmptyAfterTrimed(value)) {
		convertAlertToModelPopUpServerSideMsg(errMsg);
		return false;
	}
	return true;
}

function isValueEmptyAfterTrimed(value) {
	return value.replace(/(^\s*)|(\s*$)/g, '') == "";
}

// Function to validate email id.
function validateEmailId(mail) {
	/** This change for: GRT will allow email id contain _ or - * */
	/** Change start ** */
	var reg = /^([_a-zA-Z0-9-]+[_|\_|\.]?)*[_a-zA-Z0-9-]+@([a-zA-Z0-9-]+[_|\_|\.]?)*[a-zA-Z0-9-]+\.[a-zA-Z]{2,9}$/;
	// var reg
	// =^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$
	/** Change end * */
	if (!reg.test(mail)) {
		return false;
	}
	return true;
}
function formatPhoneNumber(input) {
	var strPhone = input.value;
	if (!strPhone.match("[0-9]{3}-[0-9]{3}-[0-9]{4}")) {
		strPhone = strPhone.replace(/(-|\(|\)|\+)/g, "");
		if ((strPhone.length == 10) && strPhone.match("[0-9]{10}")) {
			input.value = (strPhone.substring(0, 3) + '-'
					+ strPhone.substring(3, 6) + '-' + strPhone
					.substring(6, 11));
		}
	}
}

function initPage() {
	var msg = 'Please enter data for EPNSurvey';
	checkExpedaiteSelect();
	/*
	 * <%if("Y".equalsIgnoreCase(String.valueOf(request.getAttribute("needEPN")))){%>
	 * alert(msg); <%}%>
	 */
	displayAuthFieldIfNecessary();
	if (document.getElementById("expediteRefNoDisplayField") != null
			&& document.getElementById("expediteRefNo") != null) {
		document.getElementById("expediteRefNoDisplayField").innerHTML = document
				.getElementById("expediteRefNo").value;
	}
}

function displayAuthFieldIfNecessary() {
	var expiredValue = '';
	if (document.getElementById("expediteRefNo") != null
			&& document.getElementById("expediteRefNo").value != 'undefined') {
		expiredValue = document.getElementById("expediteRefNo").value.replace(
				/\s/g, "");
	}
	var AuthTrObj = document.getElementById('AuthTr');
	if (expiredValue != ''
			&& document.getElementById("expedaiteSelect").value == "Yes") {
		if (AuthTrObj) {
			AuthTrObj.style.display = '';
		}
	} else {
		if (AuthTrObj != null) {
			AuthTrObj.style.display = 'none';
		}
	}
}

function checkExpedaiteSelect() {
	try {
		var expedaiteSelect = document.getElementById('expedaiteSelect').value;
		if (expedaiteSelect == 'Y') {
			document.getElementById('AuthTr').style.display = '';
		}
	} catch (e) {
	}
}

function checkDate(val) {
	var scvInvalidDate = $("#scvInvalidDate").val();
	var scvInvalidDateCode = $("#scvInvalidDateCode").val();
	var a = val.split('/');
	if (a.length < 3) {
		convertAlertToModelPopUp(scvInvalidDateCode, scvInvalidDate);
		return false
	}
	var year = a[2];
	var month = a[0];
	var date = a[1];

	var dd = new Date(year, month - 1, date);
	var tomorrow = new Date(year, month - 1, new Number(date) + 1);
	var now = new Date();
	var resMonth = dd.getMonth() + 1;
	var resDate = dd.getDate();

	if (year > 1899 && month == resMonth && date == resDate) {
	} else {
		convertAlertToModelPopUp(scvInvalidDateCode, scvInvalidDate);
		return false
	}
	if (now > tomorrow) {
		var scvCutoverDateErrMsg = $("#scvCutoverDateErrMsg").val();
		var scvCutoverDateErrMsgCode = $("#scvCutoverDateErrMsgCode").val();
		convertAlertToModelPopUp(scvCutoverDateErrMsgCode , scvCutoverDateErrMsg);
	}
	return true;

}

function resetAuth() {
	if (document.getElementById("AuthTr") != null)
		document.getElementById("AuthTr").style.display = 'none';
	// GRT 3.0 - Reset data
	document.getElementById('registrationIdentifier').value = '';
	document.getElementById('onsitePhone').value = '';
	document.getElementById('sendMail').value = 'Y';
	document.getElementById('onsiteFirstName').value = '';
	document.getElementById('onsiteLastName').value = '';
	document.getElementById('sitePhone').value = '';
	document.getElementById('onsiteEmailId').value = '';
	document.getElementById('registrationNotes').value = '';
	document.getElementById('sameAsAbove').checked = false;
	document.getElementById('enterManually').checked = true;
}

function gotoEPN() {
	var url = document.getElementById('viewEPN').href;
	var form = document.getElementById('saveSiteRegistrationForm');
	form.action = url;
	form.submit();
}

function textLimit(field, maxlen) {
	if (field.value.length > maxlen - 1) {
		var scvMaxCharErrMsg = $("#scvMaxCharErrMsg").val();
		var scvMaxCharErrMsgCode = $("#scvMaxCharErrMsgCode").val();
		convertAlertToModelPopUp(scvMaxCharErrMsgCode, scvMaxCharErrMsg	+ field.value.length);
		return false;
	} else {
		return true;
	}
}

function noEnter(event) {
	if (event && event.keyCode == 13) {
		return false;
	} else {
		return true;
	}
}


$(function() {

	var NewRegistration = Backbone.View.extend({

		el : $('#saveSiteRegistrationForm'),

		goToInstallBaseCreation : function() {
			window.location = _path + ''; // TODO action for the next page
		},
		
		redirectToHomePage : function(){
			window.location = _path+"/home-action.action";
		},
			
		redirectToLocationPage : function(){
			   var href	= this.el.find('#backToLocationAction').attr('href');
			   window.location = href;
		},
				
		events : {
			'click .scvNextBtn' : 'goToInstallBaseCreation',
			'click .homeBtn' : 'redirectToHomePage',
			'click .scvNextBtn' : 'goToInstallBaseCreation',
			'click #backToLocationPage' : 'redirectToLocationPage'
		}

	});

	newRegistration = new NewRegistration();
	/*
	//form Submit
	$( "#saveSiteRegistrationForm" ).submit(function() {
		var fullSoldTo = $("#saveSiteRegistrationForm input[name=actionForm.soldToId]").val();
		var soldToId = '';
		if( fullSoldTo ){
			soldToId = fullSoldTo.split(' ')[0];
		}
		 $("#saveSiteRegistrationForm input[name=actionForm.soldToId]").val(soldToId);
	});*/
	
	
	$('input').each(function() {
	    var readonly = $(this).prop("readonly");
	    if(readonly==true) { // this is readonly
	    	 $(this).addClass('readOnlyText');
	    }
	});
	
	
	//Show buffer overflow message
	var bufferOvFlwMsg = $("#bufferOverFlowMsg").val();
	if( bufferOvFlwMsg!=null && bufferOvFlwMsg.length > 0 ){
		convertAlertToModelPopUp( bufferOvFlwMsg );
	}
	
});