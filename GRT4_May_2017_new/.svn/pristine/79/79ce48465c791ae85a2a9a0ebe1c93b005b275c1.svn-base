<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.Map"%>
<%@page import="com.avaya.grt.mappers.Region"%>
<%@page import="com.avaya.grt.web.util.PageCache"%>


<%@page import="java.util.Set"%>
<%@ include file="/includes/context.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/views/account-creation.css" />"/>
<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/plugins/jquery.dataTables_themeroller.css" />" />

<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/plugins/jquery.dataTables.min.js" />"></script>
<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/plugins/jquery.dataTables.columnFilter.js" />"></script>
<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/plugins/jquery.datatable.extplugin.js" />"></script>
<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/plugins/dynamicTable.js" />"></script>
<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/account/validatestring.js" />"></script>

<script type="text/javascript">


	
	<c:if test="${requestScope.addressMessage ne null }">
	window.onload = function() {
		convertAlertToModelPopUpServerSideMsg("${requestScope.addressMessage}");
	};
	</c:if>

	<c:if test="${requestScope.jurisdictionError eq '1' }">
	window.onload = function() {
		convertAlertToModelPopUp(invalidCountryCityPostalAccErrMsgCode, invalidCountryCityPostalAccErrMsg);
	};
	</c:if>
	
	<c:if test="${requestScope.bpLinkIdError ne null }">
	window.onload = function() {
		convertAlertToModelPopUpServerSideMsg("${requestScope.bpLinkIdError}");
	};
	</c:if>
	
	var invalidTokenAccErrMsg = "${grtConfig.invalidTokenAccErrMsg}";
	var searchAccErrMsg = "${grtConfig.searchAccErrMsg}";
	var invalidCountryCityPostalAccErrMsg = "${grtConfig.invalidCountryCityPostalAccErrMsg}";
	var mandatoryBPLinkId = "${grtConfig.mandatoryBPLinkId}";
	var tokenLengthErrMsg = "${grtConfig.tokenLengthErrMsg}";
	var errorPleaseInsert = "${grtConfig.errorPleaseInsert}";
	var upto = "${grtConfig.upto}";
	var chars = "${grtConfig.chars}";
	var vatLengthErrMsg = "${grtConfig.vatLengthErrMsg}";
	var tokenValidateAgainErrMsg = "${grtConfig.tokenValidateAgainErrMsg}";
	var invalidSoldToErrMsg = "${grtConfig.invalidSoldToErrMsg}";
	var mandatoryFnLnEmailPhone = "${grtConfig.mandatoryFnLnEmailPhone}";
	var FnLnLengthErrMsg = "${grtConfig.fnLnLengthErrMsg}";
	var emailLengthErrMsg = "${grtConfig.emailLengthErrMsg}";
	var invalidEmailErrMsg = "${grtConfig.invalidEmailErrMsg}";
	var phoneLength16ErrMsg = "${grtConfig.phoneLength16ErrMsg}";
	var internationalPhNoErrMsg = "${grtConfig.internationalPhNoErrMsg}";
	var FnLengthErrMsg = "${grtConfig.fnLengthErrMsg}";
	var LnLengthErrMsg = "${grtConfig.lnLengthErrMsg}";
	var phoneLength10ErrMsg = "${grtConfig.phoneLength10ErrMsg}";
	var mandatoryNameStreetCityCountryErrMsg = "${grtConfig.mandatoryNameStreetCityCountryErrMsg}";
	var accountNameLengthErrMsg = "${grtConfig.accountNameLengthErrMsg}";
	var cityLengthErrMsg = "${grtConfig.cityLengthErrMsg}";
	var nameAlphaNumnericErrMsg = "${grtConfig.nameAlphaNumnericErrMsg}";
	var streetAlphaNumericErrMsg = "${grtConfig.streetAlphaNumericErrMsg}";
	var potsalAlphaNumericErrMsg = "${grtConfig.potsalAlphaNumericErrMsg}";
	var mandatoryAccNameStreetCityPostalErrMsg = "${grtConfig.mandatoryAccNameStreetCityPostalErrMsg}";

	var streetLengthErrMsg = "${grtConfig.streetLengthErrMsg}";

	var postalLengthErrMsg = "${grtConfig.postalLengthErrMsg}";
	var mandatoryReasonForAccCreation = "${grtConfig.mandatoryReasonForAccCreation}";
	var reasonForAccCreateMinLengthErrMsg = "${grtConfig.reasonForAccCreateMinLengthErrMsg}";
	var reasonForAccCreateMaxLengthErrMsg = "${grtConfig.reasonForAccCreateMaxLengthErrMsg}";
	var selectAtlestOneRecord = "${grtConfig.selectAtlestOneRecord}";
	var enterAtlestOneEmail = "${grtConfig.enterAtlestOneEmailMsg}";
	var waitAccountCreateMsg = "${grtConfig.waitAccountCreateMsg}";
	var waitSendEmailMsg = "${grtConfig.waitSendEmailMsg}";
	var noneOfabove = "${grtConfig.noneOfabove}";
	var existingAccount = "${grtConfig.existingAccount}";
	var cancelProcessMsg = "${grtConfig.cancelProcessMsg}";
	var validationEmailInvalid = "${grtConfig.validationEmailInvalid}";
	var mandatoryToken = "${grtConfig.mandatoryToken}";
	var tokenAlphaNumericErrMsg = "${grtConfig.tokenAlphaNumericErrMsg}";
	var waitWhileFetchingMsg = "${grtConfig.waitWhileFetchingMsg}";
	var waitValidateTokenMsg = "${grtConfig.waitValidateTokenMsg}";

	
	//Error Codes
	var invalidTokenAccErrMsgCode = "${grtConfig.ewiMessageCodeMap['invalidTokenAccErrMsg']}";
	var searchAccErrMsgCode = "${grtConfig.ewiMessageCodeMap['searchAccErrMsg']}";
	var invalidCountryCityPostalAccErrMsgCode = "${grtConfig.ewiMessageCodeMap['invalidCountryCityPostalAccErrMsg']}";
	var mandatoryBPLinkIdCode = "${grtConfig.ewiMessageCodeMap['mandatoryBPLinkId']}";
	var tokenLengthErrMsgCode = "${grtConfig.ewiMessageCodeMap['tokenLengthErrMsg']}";
	var errorPleaseInsertCode = "${grtConfig.ewiMessageCodeMap['errorPleaseInsert']}";
	var uptoCode = "${grtConfig.ewiMessageCodeMap['upto']}";
	var charsCode = "${grtConfig.ewiMessageCodeMap['chars']}";
	var vatLengthErrMsgCode = "${grtConfig.ewiMessageCodeMap['vatLengthErrMsg']}";
	var tokenValidateAgainErrMsgCode = "${grtConfig.ewiMessageCodeMap['tokenValidateAgainErrMsg']}";
	var invalidSoldToErrMsgCode = "${grtConfig.ewiMessageCodeMap['invalidSoldToErrMsg']}";
	var mandatoryFnLnEmailPhoneCode = "${grtConfig.ewiMessageCodeMap['mandatoryFnLnEmailPhone']}";
	var FnLnLengthErrMsgCode = "${grtConfig.ewiMessageCodeMap['fnLnLengthErrMsg']}";
	var emailLengthErrMsgCode = "${grtConfig.ewiMessageCodeMap['emailLengthErrMsg']}";
	var invalidEmailErrMsgCode = "${grtConfig.ewiMessageCodeMap['invalidEmailErrMsg']}";
	var phoneLength16ErrMsgCode = "${grtConfig.ewiMessageCodeMap['phoneLength16ErrMsg']}";
	var internationalPhNoErrMsgCode = "${grtConfig.ewiMessageCodeMap['internationalPhNoErrMsg']}";
	var FnLengthErrMsgCode = "${grtConfig.ewiMessageCodeMap['fnLengthErrMsg']}";
	var LnLengthErrMsgCode = "${grtConfig.ewiMessageCodeMap['lnLengthErrMsg']}";
	var phoneLength10ErrMsgCode = "${grtConfig.ewiMessageCodeMap['phoneLength10ErrMsg']}";
	var mandatoryNameStreetCityCountryErrMsgCode = "${grtConfig.ewiMessageCodeMap['mandatoryNameStreetCityCountryErrMsg']}";
	var accountNameLengthErrMsgCode = "${grtConfig.ewiMessageCodeMap['accountNameLengthErrMsg']}";
	var cityLengthErrMsgCode = "${grtConfig.ewiMessageCodeMap['cityLengthErrMsg']}";
	var nameAlphaNumnericErrMsgCode = "${grtConfig.ewiMessageCodeMap['nameAlphaNumnericErrMsg']}";
	var streetAlphaNumericErrMsgCode = "${grtConfig.ewiMessageCodeMap['streetAlphaNumericErrMsg']}";
	var potsalAlphaNumericErrMsgCode = "${grtConfig.ewiMessageCodeMap['potsalAlphaNumericErrMsg']}";
	var mandatoryAccNameStreetCityPostalErrMsgCode = "${grtConfig.ewiMessageCodeMap['mandatoryAccNameStreetCityPostalErrMsg']}";

	var streetLengthErrMsgCode = "${grtConfig.ewiMessageCodeMap['streetLengthErrMsg']}";

	var postalLengthErrMsgCode = "${grtConfig.ewiMessageCodeMap['postalLengthErrMsg']}";
	var mandatoryReasonForAccCreationCode = "${grtConfig.ewiMessageCodeMap['mandatoryReasonForAccCreation']}";
	var reasonForAccCreateMinLengthErrMsgCode = "${grtConfig.ewiMessageCodeMap['reasonForAccCreateMinLengthErrMsg']}";
	var reasonForAccCreateMaxLengthErrMsgCode = "${grtConfig.ewiMessageCodeMap['reasonForAccCreateMaxLengthErrMsg']}";
	var selectAtlestOneRecordCode = "${grtConfig.ewiMessageCodeMap['selectAtlestOneRecord']}";
	var enterAtlestOneEmailCode = "${grtConfig.ewiMessageCodeMap['enterAtlestOneEmailMsg']}";
	var waitAccountCreateMsgCode = "${grtConfig.ewiMessageCodeMap['waitAccountCreateMsg']}";
	var waitSendEmailMsgCode = "${grtConfig.ewiMessageCodeMap['waitSendEmailMsg']}";
	var noneOfaboveCode = "${grtConfig.ewiMessageCodeMap['noneOfabove']}";
	var existingAccountCode = "${grtConfig.ewiMessageCodeMap['existingAccount']}";
	var cancelProcessMsgCode = "${grtConfig.ewiMessageCodeMap['cancelProcessMsg']}";
	var validationEmailInvalidCode = "${grtConfig.ewiMessageCodeMap['validationEmailInvalid']}";
	var mandatoryTokenCode = "${grtConfig.ewiMessageCodeMap['mandatoryToken']}";
	var tokenAlphaNumericErrMsgCode = "${grtConfig.ewiMessageCodeMap['tokenAlphaNumericErrMsg']}";
	var waitWhileFetchingMsgCode = "${grtConfig.ewiMessageCodeMap['waitWhileFetchingMsg']}";
	var waitValidateTokenMsgCode = "${grtConfig.ewiMessageCodeMap['waitValidateTokenMsg']}";


	function showLoadingOverlay(message){
		document.getElementById("loading-msg").innerHTML = message;
		$("#loading-overlay").show();
	}
	function hideLoadingOverlay(){
		$("#loading-overlay").hide();
	}

	function trim(myString) {
		return myString.replace(/^\s+|\s+$/g, "");
	}

	function tokenNextButton() {
		var obj1 = document.getElementById('bplinkId');
		var userType = document.getElementById('userType').value;
		if (userType == "B" && (obj1 == null || trim(obj1.value).length == 0)) {
			convertAlertToModelPopUp(mandatoryBPLinkIdCode, mandatoryBPLinkId);
			return false;
		}
		showLoadingOverlay(waitWhileFetchingMsg);

		var url = document.getElementById('nextAccountCreationAction').href;
		document.getElementById('accountCreationForm').action = url;
		document.getElementById('accountCreationForm').submit();
	}

	function goBack(currentdiv, backDiv) {

		if (document.getElementById(currentdiv)) {
			document.getElementById(currentdiv).style.display = "none";
		}
		if (document.getElementById(backDiv)) {
			document.getElementById(backDiv).style.display = "block";
		}
	}
	function goBackToStep1(currentdiv, backDiv) {

		if (document.getElementById(currentdiv)) {
			document.getElementById(currentdiv).style.display = "none";
		}
		if (document.getElementById(backDiv)) {
			document.getElementById(backDiv).style.display = "block";
		}
		if (document.getElementById(backDiv+"Table")) {
			document.getElementById(backDiv+"Table").style.display = "block";
		}
	}

	function tokenCancelButton() {
		var url = document.getElementById('cancelAccountCreationAction').href;
		document.getElementById('accountCreationForm').action = url;
		document.getElementById('accountCreationForm').submit();
	}

	function tokenBackButton() {
		var url = document.getElementById('tokenBackButtonAction').href;
		document.getElementById('accountCreationForm').action = url;
		document.getElementById('accountCreationForm').submit();
	}

	function tokenValidateButton() {
		var obj1 = document.getElementById('tokenNumber');

		if (obj1 == null || trim(obj1.value).length == 0) {
			convertAlertToModelPopUp(mandatoryTokenCode, mandatoryToken);
			return false;
		}

		if (isAlphaNumeric(obj1.value) == false) {
			convertAlertToModelPopUp(tokenAlphaNumericErrMsgCode, tokenAlphaNumericErrMsg);
			return false;
		}

		if (validateExistenceWithSize(obj1, 23)) {
			showLoadingOverlay(waitValidateTokenMsg);
			var url = document.getElementById('tokenVerificationAction').href;
			document.getElementById('accountCreationForm').action = url;
			document.getElementById('accountCreationForm').submit();
		} else {
			convertAlertToModelPopUp(tokenLengthErrMsgCode, tokenLengthErrMsg);
			return false;
		}

	}

	function validateExistenceWithSize(textobject, name, limit) {
		if (textobject.value.length > limit) {
			convertAlertToModelPopUp(errorPleaseInsertCode, errorPleaseInsert + textobject.value
					+ ' (' + upto + ' ' + limit + ' ' + chars + ').');
			return false;
		}
		return true;
	}

	function countryChange() {
		var e = document.getElementById("countrySearch");
		updateRegion(e.options[e.selectedIndex].value);
	}

	function provinceOther() {
		var e = document.getElementById("endCustomerProvince");
		var strUser = e.options[e.selectedIndex].text;
		if (strUser == "Other") {
			document.getElementById("OtherProvince").style.display = "block";

		} else {
			document.getElementById("OtherProvince").style.display = "none";
		}
	}

	function isAlphaNumeric(value) {
		var pattern = /^[a-zA-Z0-9 \&\-\_\.]*$/;
		return (value.search(pattern) != -1);
	}

	function isEmailValid(value) {
		var atpos = value.indexOf("@");
		var dotpos = value.lastIndexOf(".");
		if (atpos < 1 || dotpos < 1) {
			return false;
		}
	}

	function submitShipToForSap() {

		var euflag = document.getElementById('euFlag');
		if (euflag != null
				&& (euflag.value != 'false' || euflag.value != 'False')) {
			var vat = document.getElementById('vatNumber');
			if (vat != null) {
				vat.value = trim(vat.value);
			}
			if (vat != null && (vat.value.length > 20)) {
				convertAlertToModelPopUp(vatLengthErrMsgCode, vatLengthErrMsg);
				return false;
			}
		}

		var radioRedeem = document
				.getElementsByName("actionForm.accountFormBean.redeem");
		var redeemvalue = "Yes";
		if (redeemvalue == "Yes") {
			var token = document.getElementById('tokenNumberSubmissionFlag');
			if (token != null && (token.value.length > 0)) {
			} else {
				convertAlertToModelPopUp(searchAccErrMsgCode, searchAccErrMsg);
				return false;
			}

			if ((document.getElementById('tokenNumber').value) != (document
					.getElementById('tokenNumberSubmissionFlag').value)) {
				convertAlertToModelPopUp(tokenValidateAgainErrMsgCode, tokenValidateAgainErrMsg);
				return false;
			}
		}

		if (redeemvalue == "No") {
			var soldToFlag = document.getElementById('soldToIdSubmissionFlag');
			if (soldToFlag != null && (soldToFlag.value.length > 0)) {
			} else {
				convertAlertToModelPopUp(invalidSoldToErrMsgCode, invalidSoldToErrMsg);
				return false;
			}
		}

		var obj1 = document.getElementById('contactFirstName');
		var obj2 = document.getElementById('contactLastName');
		var obj3 = document.getElementById('endCustomerEmail');
		var obj4 = document.getElementById('endCustomerPhone');
		if ((obj1 == null || trim(obj1.value).length == 0)
				|| (obj2 == null || trim(obj2.value).length == 0)
				|| (obj3 == null || trim(obj3.value).length == 0)
				|| (obj4 == null || trim(obj4.value).length == 0)) {
			convertAlertToModelPopUp(mandatoryFnLnEmailPhoneCode, mandatoryFnLnEmailPhone);
			return false;
		}

		if (obj1.value.length > 35 || obj2.value.length > 35) {
			convertAlertToModelPopUp(FnLnLengthErrMsgCode, FnLnLengthErrMsg);
			return false;
		}

		if (obj3.value.length > 241) {
			convertAlertToModelPopUp(emailLengthErrMsgCode, emailLengthErrMsg);
			return false;
		}

		if (isEmailValid(obj3.value) == false) {
			convertAlertToModelPopUp(invalidEmailErrMsgCode, invalidEmailErrMsg);
			return false;
		}

		/* if (obj4.value.length > 16) {
			convertAlertToModelPopUp(phoneLength16ErrMsgCode, phoneLength16ErrMsg);
			return false;
		}

		var countryforphone = document.getElementById('countryforphone').innerHTML;
		if (countryforphone == 'USA'
				|| countryforphone.toUpperCase() == 'CANADA') {
			if (/^[0-9]*$/.test(obj4.value) == false || obj4.value.length != 10) {
				convertAlertToModelPopUp(phoneLength10ErrMsgCode, phoneLength10ErrMsg);
				return false;
			}
		} else {
			if (/^[0-9]*$/.test(obj4.value) == false || obj4.value.length < 7) { // MaxLength is 16
				convertAlertToModelPopUp(internationalPhNoErrMsgCode, internationalPhNoErrMsg);
				return false;
			}
		} */
		showLoadingOverlay(waitWhileFetchingMsg);

		var url = document.getElementById('accountCreationConfirmationAction').href;
		document.getElementById('accountCreationForm').action = url;
		document.getElementById('accountCreationForm').submit();
	}

	function submitSoldToForSap() {
		var radios = document
				.getElementsByName("actionForm.accountFormBean.usGovernmentAcount");
		for ( var i = 0; i < radios.length; i++) {
			if (radios[i].checked) {
				var value = radios[i].value;
			}
		}

		var radioRedeem = document
				.getElementsByName("actionForm.accountFormBean.redeem");
		var redeemvalue = "Yes";
		if (redeemvalue == "Yes") {
			var token = document.getElementById('tokenNumberSubmissionFlag');
			if (token != null && (token.value.length > 0)) {
			} else {
				convertAlertToModelPopUp(invalidTokenAccErrMsgCode, invalidTokenAccErrMsg);
				return false;
			}

			if ((document.getElementById('tokenNumber').value) != (document
					.getElementById('tokenNumberSubmissionFlag').value)) {
				convertAlertToModelPopUp(tokenValidateAgainErrMsgCode, tokenValidateAgainErrMsg);
				return false;
			}
		}

		var obj1 = document.getElementById('contactFirstName');
		var obj2 = document.getElementById('contactLastName');
		var obj3 = document.getElementById('endCustomerEmail');
		var obj4 = document.getElementById('endCustomerPhone');
		if ((obj1 == null || trim(obj1.value).length == 0)
				|| (obj2 == null || trim(obj2.value).length == 0)
				|| (obj3 == null || trim(obj3.value).length == 0)
				|| (obj4 == null || trim(obj4.value).length == 0)) {
			convertAlertToModelPopUp(mandatoryFnLnEmailPhoneCode, mandatoryFnLnEmailPhone);
			return false;
		}

		if (obj1.value.length > 35) {
			convertAlertToModelPopUp(FnLengthErrMsgCode, FnLengthErrMsg);
			return false;
		}

		if (obj2.value.length > 35) {
			convertAlertToModelPopUp(LnLengthErrMsgCode, LnLengthErrMsg);
			return false;
		}

		if (obj3.value.length > 241) {
			convertAlertToModelPopUp(emailLengthErrMsgCode, emailLengthErrMsg);
			return false;
		}

		if (isEmailValid(obj3.value) == false) {
			convertAlertToModelPopUp(invalidEmailErrMsgCode, invalidEmailErrMsg);
			return false;
		}

		/* if (obj4.value.length > 16) {
			convertAlertToModelPopUp(phoneLength16ErrMsgCode, phoneLength16ErrMsg);
			return false;
		}

		var countryforphone = document.getElementById('countryforphone').innerHTML;
		if (countryforphone == 'USA'
				|| countryforphone.toUpperCase() == 'CANADA') {
			if (/^[0-9]*$/.test(obj4.value) == false || obj4.value.length != 10) {
				convertAlertToModelPopUp(phoneLength10ErrMsgCode, phoneLength10ErrMsg);
				return false;
			}
		} else {
			if (/^[0-9]*$/.test(obj4.value) == false || obj4.value.length < 7) { //MaxLength is 16
				convertAlertToModelPopUp(internationalPhNoErrMsgCode, internationalPhNoErrMsg);
				return false;
			}
		} */

		showLoadingOverlay(waitAccountCreateMsg);

		var url = document.getElementById('accountCreationConfirmationAction').href;
		document.getElementById('accountCreationForm').action = url;
		document.getElementById('accountCreationForm').submit();
	}

	function doSearch() {
		var obj1 = document.getElementById('endCustomerName');
		var obj2 = document.getElementById('city');
		var obj3 = document.getElementById('countrySearch');

		var obj4 = document.getElementById('endCustomerStreetName');
		var obj5 = document.getElementById('endCustomerPostalCode');

		if ((obj1 == null || trim(obj1.value).length == 0)
				|| (obj2 == null || trim(obj2.value).length == 0)
				|| (obj3 == null || trim(obj3.value).length == 0)
				|| (obj4 == null || trim(obj4.value).length == 0)) {
			convertAlertToModelPopUp(mandatoryNameStreetCityCountryErrMsgCode, mandatoryNameStreetCityCountryErrMsg);
			return false;
		}

		if (obj1.value.length > 80) {
			convertAlertToModelPopUp(accountNameLengthErrMsgCode, accountNameLengthErrMsg);
			return false;
		}

		if (obj2.value.length > 40) {
			convertAlertToModelPopUp(cityLengthErrMsgCode, cityLengthErrMsg);
			return false;
		}

		if (isAlphaNumeric(obj1.value) == false) {
			convertAlertToModelPopUp(nameAlphaNumnericErrMsgCode, nameAlphaNumnericErrMsg);
			return false;
		}

		/* if (isAlphaNumeric(obj4.value) == false) {
			convertAlertToModelPopUp(streetAlphaNumericErrMsgCode, streetAlphaNumericErrMsg);
			return false;
		} */

		if (isAlphaNumeric(obj5.value) == false) {
			convertAlertToModelPopUp(potsalAlphaNumericErrMsgCode, potsalAlphaNumericErrMsg);
			return false;
		}

		showLoadingOverlay(waitWhileFetchingMsg);

		var url = document.getElementById('doFuzzySearch').href;
		document.getElementById('accountCreationForm').action = url;
		document.getElementById('accountCreationForm').submit();
	}

	function showShipToSoldTo() {
		var radios = document
				.getElementsByName("actionForm.accountFormBean.selectedAccountNumber");
		var accno = document.getElementById("selectedAccountNumber");
		if (radios.length > 0) {
			var accountSelected = false;
			for ( var i = 0; i < radios.length; i++) {
				if (radios[i].checked) {
					accountSelected = true;
					var value = radios[i].value;
					if (value == noneOfabove) {
						var obj1 = document.getElementById('endCustomerName');
						var obj2 = document
								.getElementById('endCustomerStreetName');
						var obj3 = document.getElementById('city');
						var obj4 = document
								.getElementById('endCustomerPostalCode');
						var obj5 = document
								.getElementById('endCustomerCountry');
						if ((obj1 == null || trim(obj1.value).length == 0)
								|| (obj2 == null || trim(obj2.value).length == 0)
								|| (obj3 == null || trim(obj3.value).length == 0)
								|| (obj4 == null || trim(obj4.value).length == 0)
								|| (obj5 == null || trim(obj5.value).length == 0)) {
							convertAlertToModelPopUp(mandatoryAccNameStreetCityPostalErrMsgCode, mandatoryAccNameStreetCityPostalErrMsg);
							return false;
						}

						if (isAlphaNumeric(obj1.value) == false) {
							convertAlertToModelPopUp(nameAlphaNumnericErrMsgCode, nameAlphaNumnericErrMsg);
							return false;
						}

						if (isAlphaNumeric(obj2.value) == false) {
							convertAlertToModelPopUp(streetAlphaNumericErrMsgCode, streetAlphaNumericErrMsg);
							return false;
						}

						/* if (isAlphaNumeric(obj4.value) == false) {
							convertAlertToModelPopUp(potsalAlphaNumericErrMsgCode, potsalAlphaNumericErrMsg);
							return false;
						} */

						if (obj1.value.length > 80) {
							convertAlertToModelPopUp(accountNameLengthErrMsgCode, accountNameLengthErrMsg);
							return false;
						}

						if (obj2.value.length > 60) {
							convertAlertToModelPopUp(streetLengthErrMsgCode, streetLengthErrMsg);
							return false;
						}

						if (obj3.value.length > 40) {
							convertAlertToModelPopUp(cityLengthErrMsgCode, cityLengthErrMsg);
							return false;
						}

						/* if (obj4.value.length > 10) {
							convertAlertToModelPopUp(postalLengthErrMsgCode, postalLengthErrMsg);
							return false;
						} */

						var obj6 = document
								.getElementById('accountCreationReason');

						if (obj6 != null && trim(obj6.value).length == 0) {
							convertAlertToModelPopUp(mandatoryReasonForAccCreationCode, mandatoryReasonForAccCreation)
							return false;
						} else if (obj6 != null && trim(obj6.value).length < 10) {
							convertAlertToModelPopUp(reasonForAccCreateMinLengthErrMsgCode, reasonForAccCreateMinLengthErrMsg)
							return false;
						} else if (obj6 != null
								&& trim(obj6.value).length > 4000) {
							convertAlertToModelPopUp(reasonForAccCreateMaxLengthErrMsgCode, reasonForAccCreateMaxLengthErrMsg)
							return false;
						}

						showLoadingOverlay(waitWhileFetchingMsg);

						var url = document.getElementById('showShipToSoldTo').href;
						document.getElementById('accountCreationForm').action = url;
						document.getElementById('accountCreationForm').submit();
					} else {
						var message = existingAccount;
						var messageCode = existingAccount;
						
						var callBackMethodStringForOk = "existingAccountConfOK()";
						var callBackMethodStringForCancel ="";
						convertConfirmToModelPopUp(messageCode, message, callBackMethodStringForOk, callBackMethodStringForCancel );
						return false;
					}
				}
			}
			if (!accountSelected) {
				convertAlertToModelPopUp(selectAtlestOneRecordCode, selectAtlestOneRecord);
				return false;
			}
		} else {
			var obj1 = document.getElementById('endCustomerName');
			var obj2 = document.getElementById('endCustomerStreetName');
			var obj3 = document.getElementById('city');
			var obj4 = document.getElementById('endCustomerPostalCode');
			var obj5 = document.getElementById('endCustomerCountry');
			if ((obj1 == null || trim(obj1.value).length == 0)
					|| (obj2 == null || trim(obj2.value).length == 0)
					|| (obj3 == null || trim(obj3.value).length == 0)
					|| (obj4 == null || trim(obj4.value).length == 0)
					|| (obj5 == null || trim(obj5.value).length == 0)) {
				convertAlertToModelPopUp(mandatoryAccNameStreetCityPostalErrMsgCode, mandatoryAccNameStreetCityPostalErrMsg);
				return false;
			}

			if (isAlphaNumeric(obj1.value) == false) {
				convertAlertToModelPopUp(nameAlphaNumnericErrMsgCode, nameAlphaNumnericErrMsg);
				return false;
			}

			if (isAlphaNumeric(obj2.value) == false) {
				convertAlertToModelPopUp(streetAlphaNumericErrMsgCode, streetAlphaNumericErrMsg);
				return false;
			}

			/* if (isAlphaNumeric(obj4.value) == false) {
				convertAlertToModelPopUp(potsalAlphaNumericErrMsgCode, potsalAlphaNumericErrMsg);
				return false;
			} */

			if (obj1.value.length > 80) {
				convertAlertToModelPopUp(accountNameLengthErrMsgCode, accountNameLengthErrMsg);
				return false;
			}

			if (obj2.value.length > 60) {
				convertAlertToModelPopUp(streetLengthErrMsgCode, streetLengthErrMsg);
				return false;
			}

			if (obj3.value.length > 40) {
				convertAlertToModelPopUp(cityLengthErrMsgCode, cityLengthErrMsg);
				return false;
			}

			/* if (obj4.value.length > 10) {
				convertAlertToModelPopUp(postalLengthErrMsgCode, postalLengthErrMsg);
				return false;
			} */

			var url = document.getElementById('showShipToSoldTo').href;
			document.getElementById('accountCreationForm').action = url;
			document.getElementById('accountCreationForm').submit();
		}
	}

	function existingAccountConfOK() {
		showLoadingOverlay(waitWhileFetchingMsg);
		var url = document.getElementById('accountCreationConfirmationAction').href;
		document.getElementById('accountCreationForm').action = url;
		document.getElementById('accountCreationForm').submit();
	}
						
	function validateShipToIdBP() {
		document.getElementById('soldtoIdBP').value = document
				.getElementById('cxpSoldTo').value.trim().split(" ")[0];

		var url = document.getElementById('validateShipToId').href;
		document.getElementById('accountCreationForm').action = url;
		document.getElementById('accountCreationForm').submit();
	}

	function validateShipToId() {
		var url = document.getElementById('validateShipToId').href;
		document.getElementById('accountCreationForm').action = url;
		document.getElementById('accountCreationForm').submit();
	}

	function show_popup(regId) {
		document.getElementById("dialog-underlay").style.display = "block";
		document.getElementById("popup-window").style.left = "auto";
		document.getElementById("regId").innerHTML = regId;
	}

	function show_popup(regId) {
		document.getElementById("dialog-underlay").style.display = "block";
		document.getElementById("popup-window").style.left = "auto";
		document.getElementById("regId").innerHTML = regId;
	}

	function closePopup() {
		document.getElementById("dialog-underlay").style.display = "none";
		document.getElementById("popup-window").style.left = "-1000em";
	}

	function provinceOther() {
		var e = document.getElementById("endCustomerProvince");
		var strUser = e.options[e.selectedIndex].text;
		if (strUser == "Other") {
			document.getElementById("OtherProvince").style.display = "block";

		} else {
			document.getElementById("OtherProvince").style.display = "none";
		}
	}
	
	function cancelProcess() {		
		var callBackMethodStringForOk = "cancelProcessOK()";
		var callBackMethodStringForCancel ="";
		convertConfirmToModelPopUp(cancelProcessMsgCode, cancelProcessMsg, callBackMethodStringForOk, callBackMethodStringForCancel );
		
	}
	function cancelProcessOK() {
		showLoadingOverlay(waitWhileFetchingMsg);
		var url = document.getElementById('cancelAccountCreationAction').href;
		document.getElementById('accountCreationForm').action = url;
		document.getElementById('accountCreationForm').submit();
	}
	

	function homePage() {
		showLoadingOverlay(waitWhileFetchingMsg);
		var url = document.getElementById('cancelAccountCreationAction').href;
		document.getElementById('accountCreationForm').action = url;
		document.getElementById('accountCreationForm').submit();
	}

	function finalScreenBack() {

		document.getElementById("finalSubmit").style.display = "none";
		document.getElementById("fuzzySerch").style.display = "block";
		document.getElementById("accountaddress").style.display = "block";
	}

	var countryMap = new Object();



	<%for (Map.Entry<String, Map<Long, Region>> countryMap : PageCache.regionMap.entrySet()) {
				out.print("var regions = new Array();");
				if (!countryMap.getKey().equalsIgnoreCase("US")) {
					out.print("var region=new Object();");
					out.print("region.regionId='';");
					out.print("region.region='';");
					out.print("region.description='---- Select ----';");
					out.print("region.countryCode='';");
					out.print("regions.push(region);");
				}

				for (Map.Entry<Long, Region> regionMap : countryMap.getValue()
						.entrySet()) {
					out.print("var region=new Object();");
					out.print("region.regionId=\""
							+ regionMap.getValue().getRegionId() + "\";");
					out.print("region.region=\""
							+ regionMap.getValue().getRegion() + "\";");
					out.print("region.description=\""
							+ regionMap.getValue().getDescription() + "\";");
					out.print("region.countryCode=\""
							+ regionMap.getValue().getCountryCode() + "\";");
					out.print("regions.push(region);");
				}
				out.print("countryMap[\"" + countryMap.getKey()
						+ "\"]=regions;");
	}%>



	function updateRegion(country) {

		var regionselect = document.getElementById("region");
		var length = regionselect.options.length;
		for (i = 0; i < length; i++) {
			regionselect.options[i] = null;
		}
		regionselect.options.length = 0;
		var regions = countryMap[country];

		if (regions == null || regions.length == 0) {
			document.getElementById("otherRegionSelect").style.display = "none";
		} else {
			document.getElementById("otherRegionSelect").style.display = "";
			for ( var i = 0; i < regions.length; i++) {
				regionselect.add(new Option(regions[i].description,
						regions[i].regionId));
			}
		}

	}

	function sendEmailConfirmation() {
		var email = document.getElementById('emailNotification').value;
		if (validateRequired(email, enterAtlestOneEmailCode + "###" + enterAtlestOneEmail)
				&& splitMails(email)) {
			showLoadingOverlay(waitSendEmailMsg);
			var url = document.getElementById('sendEmailConfirmationAction').href;
			document.getElementById('accountCreationForm').action = url;
			document.getElementById('accountCreationForm').submit();
		} else {
			return false;
		}
	}

	// Function to split Multiple E-mails by semi-colons
	function splitMails(val) {
		var str = '';
		var mails = val.split(';');
		for (i = 0; i < mails.length; i++) {
			var str = mails[i].replace(/\s/g, "");
			if ((str) != '') {
				if (!validateEmailId(str)) {
					convertAlertToModelPopUp(validationEmailInvalidCode, validationEmailInvalid + str);
					return false;
				}
			}

		}
		return true;
	}

	//Function to validate email id.
	function validateEmailId(mail) {
		/** This change for: GRT will allow email id contain _ or - **/
		/** Change start ***/
		var reg = /^([_a-zA-Z0-9-]+[_|\_|\.]?)*[_a-zA-Z0-9-]+@([a-zA-Z0-9-]+[_|\_|\.]?)*[a-zA-Z0-9-]+\.[a-zA-Z]{2,9}$/;
		/** Change end **/
		if (!reg.test(mail)) {
			return false;
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

	function numericFilter(txb) {
		txb.value = txb.value.replace(/[^0-9]/ig, "");
	}
</script>


<!-- start page content-wrap -->
<div class="content-wrap account-creation">

	<h1 class="page-title">End Customer Account Creation${bundle.msg.endCustomerAccount}</h1>

	<!-- start content -->
	<div class="content">

		<!-- start accountCreationForm -->
		<s:form action="begin" id="accountCreationForm" method="post" >
			<s:token></s:token>
			<a id="nextAccountCreationAction" href="<c:url context="${ _path }" value="/account/nextAccountCreation.action"/>"></a>
			<a id="cancelAccountCreationAction" href="<c:url context="${ _path }" value="/home/home-action.action"/>"></a>
			<a id="tokenBackButtonAction" href="<c:url context="${ _path }" value="/account/tokenBackButton.action"/>"></a>
			<a id="tokenVerificationAction" href="<c:url context="${ _path }" value="/account/tokenVerification.action"/>"></a>
			<a id="validateShipToId" href="<c:url context="${ _path }" value="/account/validateShipToId.action"/>"></a>
			<a id="showShipToSoldTo" href="<c:url context="${ _path }" value="/account/showShipToSoldTo.action"/>"></a>
			<a id="doFuzzySearch" href="<c:url context="${ _path }" value="/account/doFuzzySearch.action"/>"></a>
			<a id="showSoldToId" href="<c:url context="${ _path }" value="/account/showSoldToId.action"/>"></a>
			<a id="accountCreationConfirmationAction" href="<c:url context="${ _path }" value="/account/accountCreationConfirmation.action"/>"></a>
			<a id="sendEmailConfirmationAction" href="<c:url context="${ _path }" value="/account/sendEmailConfirmation.action"/>"></a>

			<input type="hidden" id="soldToStorer" value="${actionForm.accountFormBean.shipToId}" />


			${requestScope.accountCreationError}

			<!-- start accountcreation -->
			<div id="accountcreation" style="display:${actionForm.accountFormBean.acountCreationFlag};">
				<div id="account-creation-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">End Customer Account Creation${bundle.msg.endCustomerAccount}
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div class="data collapse-box-container">

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Please select the Country for account creation${bundle.msg.selectCountry}</span>
									<select name="actionForm.accountFormBean.country" id="country">
										<c:forEach var="entry" items="${actionForm.accountFormBean.countryList}">
										<c:choose>
										<c:when test="${actionForm.accountFormBean.country eq entry.key}">
										<option class="required" value="${entry.key}" selected="selected">${entry.value}</option>
										</c:when>
										<c:when test="${actionForm.accountFormBean.country eq null and entry.key eq 'US'}">
										<option class="required" value="${entry.key}" selected="selected">${entry.value}</option>
										</c:when>
										<c:when test="${fn:length(entry.key) gt 0 and fn:length(entry.value) gt 0}">
										<option class="required" value="${entry.key}">${entry.value}</option>
										</c:when>
										</c:choose>
										</c:forEach>
									</select>
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">
										<c:choose>
											<c:when test="${actionForm.accountFormBean.userType eq 'B'}">
											Please enter/modify the BPLinkID${bundle.msg.enterBPlinkId}*
											</c:when>
											<c:otherwise>
											Please enter/modify the BPLinkID${bundle.msg.enterBPlinkId}
											</c:otherwise>
										</c:choose>
									</span>
									<input type="text" name="actionForm.accountFormBean.bplinkId" id="bplinkId" value="${fn:escapeXml(actionForm.accountFormBean.bplinkId) }" />
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">
										Will this account be used for redeeming a token?${bundle.msg.isRedeemingToken}
									</span>
									<input type="radio" name="actionForm.accountFormBean.redeem" value="Yes" disabled="" checked=""><span>Yes</span>
									<input type="radio" name="actionForm.accountFormBean.redeem" value="No" disabled="" ><span>No</span>
								</p>
							</div>
						</div>

						<input type="hidden" name="actionForm.accountFormBean.userType" id="userType" value="${fn:escapeXml(actionForm.accountFormBean.userType) }" />
						<!-- ${requestScope.bpLinkIdError} -->

					</div>
				</div>

				<!-- start controls -->
				<div class="controls">
					<input type="button" class="button gray" value="Cancel" onclick="javascript:cancelProcess();" />
					<input type="button" class="button gray" value="Next" onclick="javascript:tokenNextButton();" />
				</div>
				<!-- end controls -->

			</div>
			<!-- end accountcreation -->


			<!-- start accountaddress -->
			<div id="accountaddress" style="display: ${actionForm.accountFormBean.accountAddressFlag};">
				<div id="account-address-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">Account address
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div class="data collapse-box-container">

						<div class="row">
							<div class="col">
								<p>
									<span class="title">
										Name${bundle.msg.name}*
									</span>
									<input type="text" value="${fn:escapeXml(actionForm.accountFormBean.endCustomerName)}" name="actionForm.accountFormBean.endCustomerName" id="endCustomerName" maxlength="80" size="60" class="required" />
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">
										Street Name${bundle.msg.streetName}*
									</span>
									<input type="text" value="${fn:escapeXml(actionForm.accountFormBean.endCustomerStreetName)}" name="actionForm.accountFormBean.endCustomerStreetName" id="endCustomerStreetName" maxlength="60" size="60" class="required" />
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">
										City${bundle.msg.city}*
									</span>
									<input type="text" value="${fn:escapeXml(actionForm.accountFormBean.endCustomerCity)}" name="actionForm.accountFormBean.endCustomerCity" id="city" maxlength="40" size="40" class="required" />
							</div>
						</div>

						<!-- start regional select -->
						<div class="row">
							<c:choose>
							<c:when test="${actionForm.accountFormBean.currentCountryRegion ne null || fn:length(actionForm.accountFormBean.currentCountryRegion) gt 0}">
							<div class="col" id="otherRegionSelect">
							</c:when>
							<c:otherwise>
							<div class="col" style="display:none;" id="otherRegionSelect">
							</c:otherwise>
							</c:choose>
								<p>
									<span class="title">
										State/Province${bundle.msg.stateprov}
									</span>
									<select name="actionForm.accountFormBean.selectedRegionId" id="region" class="required">
										<c:if test="${actionForm.accountFormBean.endCustomerCountry ne 'US' and actionForm.accountFormBean.endCustomerCountry ne 'USA'}">
										<option class="required" value="">---- Select ----</option>
										</c:if>
										<c:forEach var="entry" items="${actionForm.accountFormBean.currentCountryRegion}">
										<c:if test="${actionForm.accountFormBean.selectedRegionId eq entry.key}">
										<option class="required" value="${entry.key}" selected="selected">${entry.value.description}</option>
										</c:if>
										<c:if test="${actionForm.accountFormBean.selectedRegionId ne entry.key}"></c:if>
										<option class="required" value="${entry.key}">${entry.value.description}</option>
										</c:forEach>
									</select>
								</p>
							</div>
							<div class="col">
								<p>
									<span class="title">
										Zip / Postal Code${bundle.msg.zip}
									</span>
									<input type="text"
										name="actionForm.accountFormBean.endCustomerPostalCode"
										value="${fn:escapeXml(actionForm.accountFormBean.endCustomerPostalCode)}"
										id="endCustomerPostalCode" maxlength="10" size="12"
										class="required" />
								</p>
							</div>
						</div>
						<!-- end regional select -->

						<div class="row">
							<div class="col">
								<p>
									<span class="title">
										Country${bundle.msg.country}*
									</span>
									<select name="actionForm.accountFormBean.endCustomerCountry" id="countrySearch" onChange="javascript:countryChange();">
										<c:forEach var="entry" items="${actionForm.accountFormBean.countryList}">
										<c:choose>
										<c:when test="${actionForm.accountFormBean.endCustomerCountry eq entry.key}">
										<option value="${entry.key}" selected="selected">${entry.value}</option>
										</c:when>
										<c:when test="${actionForm.accountFormBean.endCustomerCountry eq null and entry.key eq 'US'}">
										<option value="${entry.key}" selected="selected">${entry.value}</option>
										</c:when>
										<c:when test="${fn:length(entry.key) gt 0 and fn:length(entry.value) gt 0}">
										<option value="${entry.key}">${entry.value}</option>
										</c:when>
										</c:choose>
										</c:forEach>
									</select>
								</p>
							</div>
						</div>

						<c:choose>
						<c:when test="${requestScope.jurisdictionError eq '1'}">
						<div>Please provide a valid Country, City, Postal Code value combination and retry${bundle.msg.jurisdiction}</div>
						</c:when>
						</c:choose>
						<input type="hidden" name="endCustomerCountry" id="endCustomerCountry" value="${actionForm.accountFormBean.endCustomerCountry}">


					</div>

					<c:if test="${requestScope.searchError ne null }">
						<div class="error-bar" style="display: block;">
							<i class="fa fa-exclamation-triangle"></i>
							<span class="error-bar-msg">
								${requestScope.searchError}							
							</span>
						</div>

					</c:if>
				</div>

				<!-- start fuzzySerch -->
				<div id="fuzzySerch" style="display: ${actionForm.accountFormBean.fuzzySearchFlag};">

					<!-- start similarRecordFound -->
					<div id="fuzzy-wrap" class="data-table-wrap collapse-box">
						<h2 class="collapse-box-header active">Similar Records
							<span class="legend">
								<span class="best">Best Match</span>
								<span class="nearest">Nearest Match</span>
								<span class="related">Related Match</span>
							</span>
							<a href="#">
								<span></span>
							</a>
						</h2>
						<div id="fuzzy-box" class="data collapse-box-container">

							<c:choose>
							<c:when test="${fn:length(actionForm.accountFormBean.searchResult) gt 0}">

							<table width="100%" border="0" class="fuzzy-class basic-table">
								<thead>
									<tr>
										<th>Select (optional)${bundle.msg.selecRadioButton}</th>
										<th>Account${bundle.msg.account}</th>
										<th>Name${bundle.msg.name}</th>
										<th>Street${bundle.msg.street}</th>
										<th>City${bundle.msg.city}</th>
										<th>State/Province${bundle.msg.stateprov}</th>
										<th>Zip / Postal Code${bundle.msg.zip}</th>
										<th>Site Country${bundle.msg.country}</th>
									</tr>
								</thead>
								<tbody>
									<c:set var="showTextArea" value="false" />
									<c:forEach items="${ actionForm.accountFormBean.searchResult }"	var="item">
									<c:if test="${item.score gt grtConfig.serchMidRange}">
										<c:set var="showTextArea" value="true" />
									</c:if>
									<c:set var="color" value="#98CD97" />
									<c:if test="${item.score le grtConfig.serchMidRange}">
										<c:set var="color" value="#CCCD5F" />
									</c:if>
									<c:if test="${item.score le grtConfig.searchLowRange}">
										<c:set var="color" value="#CE6564" />
									</c:if>
									<tr style="background-color:${color}">
										<td	align="center" width="5%">
											<input type="radio" name="actionForm.accountFormBean.selectedAccountNumber" id="selectedAccountNumber" value="${item.SAPId}:${item.name}:${item.street}:${item.city}:${item.region}:${item.zip}:${item.country}" />
										</td>
										<td >${item.SAPId }</td>
										<td >${item.name }</td>
										<td >${item.street }</td>
										<td >${item.city }</td>
										<td >${item.region }</td>
										<td >${item.zip }</td>
										<td >${item.country }</td>
									</tr>
									</c:forEach>

									<tr>
										<td align="center">
											<input type="radio" name="actionForm.accountFormBean.selectedAccountNumber" value="None of the above" />
										</td>
										<c:choose>
										<c:when test="${showTextArea}">
										<td colspan="3">Please tell us, why you still want to continue with new account creation${bundle.msg.continueaccount}:</td>
										<td colspan="4">
											<textarea id="accountCreationReason" name="actionForm.accountFormBean.accountCreationReason" value="${actionForm.accountFormBean.accountCreationReason}" ></textarea>
										</td>
										</c:when>
										<c:otherwise>
										<td colspan="7">None of the above${bundle.msg.noneOfabove}</td>
										</c:otherwise>
										</c:choose>
									</tr>
								</tbody>
							</table>

							<div class="page-note address-note">
								<p>
									<span>Please review and select an existing account if displayed. If record is not displayed, Choose No Record Found and continue to Create a new Account. <br />Before continuing, please read our <a href="/grtWebProject/download/policy.html" target="_blank">policy</a> for duplicate records</span>
								</p>
							</div>

							</c:when>
							<c:otherwise>

							<div class="error-bar" style="display: block;">
								<i class="fa fa-exclamation-triangle"></i>
								<span class="error-bar-msg">
									No matching records found, click next to continue with creation of account.${bundle.msg.norecordsfoundcreation}
									<input type="hidden" id="selectedAccountNumber" />
								</span>
							</div>

							</c:otherwise>
							</c:choose>

						</div>
					</div>
					<!-- end similarRecordFound -->

				</div>
				<!-- end fuzzySerch -->

				<!-- start controls -->
				<div class="controls address-buttons">
					<input type="button" value="Back" class="button gray" onClick="goBackToStep1('accountaddress','accountcreation');javascript:goBack('fuzzySerch','accountcreation'); " />
					<input type="button" value="Search" class="button gray" onclick="doSearch();" />
					<input type="button" value="Cancel" class="button gray" onclick="cancelProcess();" />
					<c:if test="${actionForm.accountFormBean.fuzzySearchFlag eq 'block'}">
					<input type="button" class="button gray" value="Next" onclick="showShipToSoldTo();" />
					</c:if>
				</div>
				<!-- end controls -->

			</div>
			<!-- start accountaddress -->

			<!-- start tokennumber1 -->
			<div id="tokennumber1" style="display:${actionForm.accountFormBean.tokenNumberFlag};">

				<!--div>${requestScope.tokenSearchError}</div-->

				<!-- start similarRecordFound -->
					<div id="token-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active" id="l_account8">Account creation for Standalone account${bundle.msg.accountcreationfortoken}
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div id="token-box" class="data collapse-box-container">

						<input type="hidden" name="actionForm.accountFormBean.tokenNumberSubmissionFlag" id="tokenNumberSubmissionFlag" value="${fn:escapeXml(actionForm.accountFormBean.tokenNumberSubmissionFlag)}" />
						<input type="hidden" name="actionForm.accountFormBean.EUFlag" id="euFlag" value="${fn:escapeXml(actionForm.accountFormBean.EUFlag)}" />

						<div class="row">
							<div class="col">
								<p>
									<span id="l_account5" class="title">BP Name${bundle.msg.bpName}</span>
									${actionForm.accountFormBean.bpName}
								</p>
							</div>
							<div class="col">
								<p>
									<span id="l_account5" class="title">Country${bundle.msg.country}</span>
									${actionForm.accountFormBean.countryList[actionForm.accountFormBean.endCustomerCountry]}
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span id="l_account5" class="title">BP Link ID${bundle.msg.bpLinkId}</span>
									${actionForm.accountFormBean.bplinkId}
								</p>
							</div>
							<div class="col">
								<p>
									<span class="title">Enter Token Number*</span>
									<input type="text" name="actionForm.accountFormBean.tokenNumber" id="tokenNumber" value="${fn:escapeXml(actionForm.accountFormBean.tokenNumber)}" maxlength="23" />
									<input type="button" class="button gray small" value="Validate" onclick="javascript:tokenValidateButton();" />
								</p>
							</div>
						</div>
					</div>
					<!-- start error msgs -->
					<c:if test="${requestScope.tokenSearchMsg != null}">
					<div class="error-bar" style="display: block;">
						<i class="fa fa-exclamation-triangle"></i><span class="error-bar-msg">${requestScope.tokenSearchMsg}</span>
					</div>
					</c:if>
					<c:if test="${requestScope.tokenSearchError != null}">
					<div class="error-bar" style="display: block;">
						<i class="fa fa-exclamation-triangle"></i><span class="error-bar-msg">${requestScope.tokenSearchError}</span>
					</div>
					</c:if>
					<!-- end error msgs -->
				</div>
			</div>
			<!-- end tokennumber1 -->

			<!-- start shiptocreationforBP -->
			<c:if test="${actionForm.accountFormBean.shipToCreationFlagForBP eq 'block'}">
			<div id="shiptocreationforBP" style="display: ${actionForm.accountFormBean.shipToCreationFlagForBP};">
				<div id="soldto-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">SoldTo details
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div class="data collapse-box-container">

						<input type="hidden" name="actionForm.accountFormBean.soldToIdSubmissionFlag" id="soldToIdSubmissionFlag" />

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Please select soldTo id for association of this account</span>
									<input type="text" style="display: none;" name="actionForm.accountFormBean.soldToId" id="soldtoIdBP" />
									<div class='soldTo'>
										<input type="text" name="actionForm.accountFormBean.soldToId" class="soldTo" id="cxpSoldTo" value="${ item.orderId }" size="60" />
									</div>
								</p>
							</div>
						</div>

					</div>
				</div>

				<div>${requestScope.shipToMsg}</div>
				<div>${requestScope.shipToError}</div>
				${requestScope.soldToError}
			</div>

			<!-- start controls -->
			<div class="controls">
				<input type="button" onClick="javascript:validateShipToIdBP();" value="Validate" />
			</div>
			<!-- end controls -->
			</c:if>
			<!-- end shiptocreationforBP -->

			<!-- start shiptocreationforNonBP -->
			<c:if test="${actionForm.accountFormBean.shipToCreationFlagForNonBP eq 'block'}">
			<div id="shiptocreationforNonBP" style="display: ${actionForm.accountFormBean.shipToCreationFlagForNonBP};">
				<div id="soldto-non-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">SoldTo details
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div id="" class="collapse-box-container">

						<input type="hidden" name="actionForm.accountFormBean.soldToIdSubmissionFlag" id="soldToIdSubmissionFlag" />

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Please select soldTo id for association of this account</span>
									<input type="text" name="actionForm.accountFormBean.soldToId" id="soldtoIdBP" />
								</p>
							</div>
						</div>

					</div>
				</div>

				<div>${requestScope.shipToMsg}</div>
				<div>${requestScope.shipToError}</div>
				<div>${requestScope.soldToError}</div>

			</div>

			<!-- start controls -->
			<div class="controls">
				<input type="button" class="button gray" onClick="javascript:validateShipToId();" value="Validate" />
			</div>
			<!-- end controls -->
			</c:if>
			<!-- end shiptocreationforNonBP -->

			<!-- start finalSubmit -->
			<div id="finalSubmit" style="display: ${actionForm.accountFormBean.shipTosoldToFlag};">
				<div id="final-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">SoldTo details
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div class="data collapse-box-container">

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Name</span>
									${ actionForm.accountFormBean.endCustomerName}
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Street Name</span>
									${actionForm.accountFormBean.endCustomerStreetName }
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<c:choose>
									<c:when test="${actionForm.accountFormBean.sapBox eq 'I'}">
									<span class="title">Province</span>
									</c:when>
									<c:otherwise>
									<span class="title">State / Province</span>
									</c:otherwise>
									</c:choose>
									${actionForm.accountFormBean.regionValue}
								</p>
							</div>
							<div class="col">
								<p>
									<span class="title">City</span>
									${fn:escapeXml(actionForm.accountFormBean.endCustomerCity)}
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Zip / Postal Code</span>
									${fn:escapeXml(actionForm.accountFormBean.endCustomerPostalCode)}
								</p>
							</div>
							<div class="col">
								<p>
									<span class="title">Country</span>
									<span id="countryforphone">${actionForm.accountFormBean.countryList[actionForm.accountFormBean.endCustomerCountry]}</span>
								</p>
							</div>
						</div>

						<c:if test="${actionForm.accountFormBean.EUFlag eq 'True'}">
						<div class="row">
							<div class="col">
								<p>
									<span class="title">VAT No</span>
									<input type="text" name="actionForm.accountFormBean.vatNumber" maxlength="20" size="20" id="vatNumber" />
								</p>
							</div>
						</div>
						</c:if>

					</div>
				</div>

				<div id="final-2-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">Contact
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div class="data collapse-box-container">

						<div class="row">
							<div class="col">
								<p>
									<span class="title">First Name*</span>
									<input type="text" name="actionForm.accountFormBean.contactFirstName" value="${fn:escapeXml(actionForm.accountFormBean.contactFirstName) }" maxlength="35" size="35" id="contactFirstName" />
								</p>
							</div>
							<div class="col">
								<p>
									<span class="title">Last Name*</span>
									<input type="text" name="actionForm.accountFormBean.contactLastName" value="${fn:escapeXml(actionForm.accountFormBean.contactLastName) }" maxlength="35" size="35" id="contactLastName" />
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Email Address*</span>
									<input type="text" name="actionForm.accountFormBean.contactEmail" value="${fn:escapeXml(actionForm.accountFormBean.contactEmail) }" maxlength="241" size="60" id="endCustomerEmail" />
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Phone Number*(For US/Canada, please enter 10 digit phone number)</span>
									<input type="text" name="actionForm.accountFormBean.contactPhone" value="${fn:escapeXml(actionForm.accountFormBean.contactPhone) }" maxlength="16" size="16" id="endCustomerPhone" />
								</p>
							</div>
						</div>

						<c:if test="${actionForm.accountFormBean.sapBox eq 'B'}">
						<div class="row">
							<div class="col">
								<p>
									<span class="title">Account</span>
								</p>
								<p><input type="radio" name="actionForm.accountFormBean.usGovernmentAcount" id="siaccount" value="This is a Service Integrator (SI)" >This is a Service Integrator (SI)</input></p>
								<p><input type="radio" name="actionForm.accountFormBean.usGovernmentAcount" id="spaccount" value="This is a Service Provider (SP)"	>This is a Service Provider (SP)</input></p>
								<p><input type="radio" name="actionForm.accountFormBean.usGovernmentAcount" id="govtaccount" value="This is a US Government account"	>This is a US Government account</input></p>
								<p><input type="radio" name="actionForm.accountFormBean.usGovernmentAcount" id="others" value="Other"	checked>Other</input></p>
							</div>
						</div>
						</c:if>

					</div>
				</div>

				<!-- start controls -->
				<div class="controls">
					<input type="button" class="button gray" value="Back" onClick="javascript:tokenNextButton();" />
					<c:if test="${actionForm.accountFormBean.sapBox eq 'I'}">
					<input type="button" class="button gray" value="Submit" onclick="javascript:submitShipToForSap();" />
					</c:if> <c:if test="${actionForm.accountFormBean.sapBox eq 'B'}">
					<input type="button" class="button gray" value="Submit" onclick="javascript:submitSoldToForSap();" />
					</c:if>
					<input type="button" class="button gray" value="Cancel" onclick="javascript:cancelProcess();" />
				</div>
				<!-- end controls -->

			</div>
			<!-- end finalSubmit -->

			<!-- start confirmation -->
			<div id="confirmation" style="display:${actionForm.accountFormBean.confirmationFlag};">

				<!-- start confirm-wrap -->
				<div id="confirm-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">Confirmation
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div id="confirm-box" class="data collapse-box-container">

						<c:choose>
						<c:when test="${actionForm.accountFormBean.responseCode ne '1000'}">

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Error Description</span>
									<span>${actionForm.accountFormBean.responseDescription}</span>
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<input type="button" value="Home" onclick="javascript:homePage()" />
								</p>
							</div>
						</div>

						</c:when>
						<c:otherwise>
						<c:choose>
						<c:when test="${actionForm.accountFormBean.selectedAccountNumber eq 'None of the above'}">

						<div class="row">
							<div class="col">
								<p>
									<span style="text-transform: none !important">Congratulations<span style="font-size: 25px;">,</span> the account has been successfully created!</span>
									<span class="title" style="text-transform: none">Please wait 20 minutes before registering any equipment at this site</span> <span style="text-transform: none !important">to prevent any system failures.</span>
								</p>
							</div>
						</div>

						

						<div class="row">
							<c:if test="${actionForm.accountFormBean.sapBox eq 'I'}">
							<div class="col">
								<p>
									<span class="title">
										ShipToId${bundle.msg.shipToId}
									</span>
									<span>${actionForm.accountFormBean.shipToId}</span>
								</p>
							</div>
							</c:if>
							<c:if test="${actionForm.accountFormBean.sapBox eq 'B'}">
							<div class="col">
								<p>
									<span class="title">
										SoldToId${bundle.msg.soldToId}
									</span>
									<span>${fn:escapeXml(actionForm.accountFormBean.soldToId)}</span>
								</p>
							</div>
							</c:if>
						</div>

						</c:when>
						<c:otherwise>

						<div class="row">
							<div class="col">
								<p>
									<span>An existing Customer Account has been selected.</span>
								</p>
							</div>
						</div>
						
						<div class="row">
							<div class="col">
								<p>
									<span class="title">
										Account Id
									</span>
									<span>${actionForm.accountFormBean.accountIdForUpdate}</span>
								</p>
							</div>
						</div>
						
						</c:otherwise>
						</c:choose>
						

						<div class="row">
							
							<div class="col">
								<p>
									<span class="title">
										Name
									</span>
									<span>${actionForm.accountFormBean.endCustomerName}</span>
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">
										Street Name
									</span>
									<span>${fn:escapeXml(actionForm.accountFormBean.endCustomerStreetName)}</span>
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">
										City
									</span>
									<span>${fn:escapeXml(actionForm.accountFormBean.endCustomerCity)}</span>
								</p>
							</div>

							<c:choose>
							<c:when test="${actionForm.accountFormBean.sapBox eq 'I'}">
							<div class="col">
								<p>
									<span class="title">Province</span>
									<span>${actionForm.accountFormBean.regionValue}</span>
								</p>
							</div>
							</c:when>
							<c:otherwise>
							<div class="col">
								<p>
									<span class="title">State / Province</span>
									<span>${actionForm.accountFormBean.regionValue}</span>
								</p>
							</div>
							</c:otherwise>
							</c:choose>
									
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">
										Zip / Postal Code
									</span>
									<span>${fn:escapeXml(actionForm.accountFormBean.endCustomerPostalCode)}</span>
								</p>
							</div>
							<div class="col">
								<p>
									<span class="title">
										Country${bundle.msg.country}
									</span>
									<span id="countryforphone">${actionForm.accountFormBean.countryList[actionForm.accountFormBean.endCustomerCountry]}</span>
								</p>
							</div>
						</div>

						<c:if test="${actionForm.accountFormBean.bplinkId ne null and fn:length(actionForm.accountFormBean.bplinkId) > 0}">

						<div class="row">
							<div class="col">
								<p>
									<span class="title">
										BP Link ID
									</span>
									
									<span style="text-transform: none !important"> ${actionForm.accountFormBean.bplinkId} has been given access to this account for the duration of the Token you redeemed. This access applies to product registration, Token redemption and Siebel access.</span>
								</p>
							</div>
						</div>

						</c:if>

						<c:if test="${actionForm.accountFormBean.emailNotificationStatus eq 'Yes'}">
						<div class="row">
							<div class="col">
								<p>
									<span class="title">Description</span>
									<span>${actionForm.accountFormBean.responseDescription}</span>
								</p>
							</div>
						</div>
						</c:if>

					</div>
				</div>
				<!-- end confirm-wrap -->

				<!-- start controls -->
				<div id="emailDiv" class="controls" style="display:${actionForm.accountFormBean.emailNotificationFlag};">
					<span class="title">Email Address</span>
					<!--tl:toolTip id="emailToolTip"
					message="${bundle.msg.emailToolTip}"></tl:toolTip-->
					<input type="text" id="emailNotification" name="actionForm.accountFormBean.emailNotification" maxlength="200" />
					<input type="button" value="Send Email" class="button gray" onClick="javascript:sendEmailConfirmation();" />
					<input type="button" value="Print" class="button gray" onClick="window.print();"	/>
					<input type="button" value="Home" class="button gray" onclick="javascript:homePage()"	/>
				</div>
				<!-- end controls -->

				</c:otherwise>
				</c:choose>

			</div>
			<!-- end confirmation -->

		</s:form>
		<!-- end accountCreationForm -->

	</div>
	<!-- end content -->
</div>
<!-- end content-wrap -->

<script>
	jQuery(document).ready(function($) {
		// move entry buttons into table area
		var theMainForm = $('#accountCreationForm');

		$('#fuzzy-box').append($('table.fuzzy-class'));
		$('table.fuzzy-class').wrap('<div class="basic-table-wrap" />');
		$('#accountaddress').append($('.address-note'));
		$('#accountaddress').append($('.address-buttons'));

		$(theMainForm).append($('#tokennumber1'));
		$(theMainForm).append($('#finalSubmit'));
		$(theMainForm).append($('#confirmation'));
		
	});
</script>
<%-- ************************************************************* --%>
<%-- ***** BELOW IS DEPRECATED CODE - NEED REVIEW AND REMOVE ***** --%>
<%-- ************************************************************* --%>
<%-- <table id="accountcreationTable" height="25%" width="100%" border="0" style="display:${actionForm.accountFormBean.acountCreationFlag};">
</table> --%>
