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

	function searchNext() {

		var radios = document.getElementsByName("actionForm.accountFormBean.selectedSAPId");
		var checked = false;
		for ( var i = 0; i < radios.length; i++) {
			if (radios[i].checked) {
				checked = true;
				break;
			}
		}

		if (checked) {
			showLoadingOverlay(waitWhileFetchingMsg);
			submitForm('validateAccountForUpdate');
		} else {
			convertAlertToModelPopUp(pleaseSelectARecordMsgCode, pleaseSelectARecordMsg);
		}
	}

	function isAlphaNumeric(value) {
		var pattern = /^[a-zA-Z0-9]*$/;
		return (value.search(pattern) != -1);
	}

	function validateSoldToIdNonBP() {
		var obj1 = document.getElementById('cxpSoldTo');
		if (obj1 == null || trim(obj1.value).length == 0) {
			convertAlertToModelPopUp(emptySoldToErrorCode, emptySoldToError);
			return false;
		} else {
			submitForm('validateSoldtoIdForUpdate');
		}
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

	function submitForm(formName) {
		if(formName == "updateAccount") {
			showLoadingOverlay(waitAccountUpdateMsg);
		} else {
			showLoadingOverlay(waitWhileFetchingMsg);
		}
		var url = document.getElementById(formName).href;
		document.getElementById('accountCreationForm').action = url;
		document.getElementById('accountCreationForm').submit();
	}

	function countryChange() {
		var e = document.getElementById("country");
		updateRegion(e.options[e.selectedIndex].value);
	}

	function doSearch() {

		var obj1 = document.getElementById('endCustomerName');
		var obj2 = document.getElementById('city');
		var obj4 = document.getElementById('country').options[document
				.getElementById('country').selectedIndex].value;
		if ((obj1 == null || trim(obj1.value).length == 0)
				|| (obj2 == null || trim(obj2.value).length == 0)
				|| (obj4 == null || trim(obj4).length == 0)) {
			convertAlertToModelPopUp(requiredNameCityCountryErrorCode, requiredNameCityCountryError);
			return false;
		}

		if (obj1.value.length > 80) {
			convertAlertToModelPopUp(accountNameLengthErrorCode, accountNameLengthError);
			return false;
		}

		if (obj2.value.length > 40) {
			convertAlertToModelPopUp(cityLengthErrorCode, cityLengthError);
			return false;
		}

		submitForm('doFuzzySearch');
		return true;
	}

	function doNext() {
		var obj1 = document.getElementById('cxpSoldTo');
		if (obj1 == null || trim(obj1.value).length == 0) {
			convertAlertToModelPopUp(emptySoldToErrorCode, emptySoldToError);
			return false;
		} else {
			document.getElementById('soldToStorer').value = document
					.getElementById('cxpSoldTo').value.trim().split(" ")[0];
			submitForm('validateSoldtoIdForUpdate');
			return true;
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
	function doSubmit() {
		var obj1 = document.getElementById('name');
		var obj2 = document.getElementById('primaryAccountStreetAddress');
		var obj3 = document.getElementById('primaryAccountStreetAddress2');
		var obj4 = document.getElementById('primaryAccountCity');
		var obj6 = document.getElementById('primaryAccountPostalCode');
		var obj7 = document.getElementById('phoneNumber');
		var obj8 = document.getElementById('faxNumber');

		var obj9 = document.getElementById('emailId');

		var contactName = document.getElementById('contactName');
		var error = false;
		var country = document.getElementById('primaryAccountCountry1').value;
		var obj15 = document.getElementById('primaryAccountState');

		if ((obj1 == null || trim(obj1.value).length == 0)
				|| (obj2 == null || trim(obj2.value).length == 0)
				|| (obj4 == null || trim(obj4.value).length == 0)
				|| (obj6 == null || trim(obj6.value).length == 0)
				|| (obj7 == null || trim(obj7.value).length == 0)
				|| (obj9 == null || trim(obj9.value).length == 0)
				|| (contactName == null || trim(contactName.value).length == 0)) {
			error = true;
		}

		if (error) {
			var errormsg = mandatoryAccNameStreetCityError;
			if (country == 'USA' || country == 'US') {
				errormsg = errormsg + stateOrProvince;
			}
			errormsg = errormsg
					+ mandatorPhoneEmailNameError;
			convertAlertToModelPopUp(mandatoryAccNameStreetCityErrorCode, errormsg);
			return false;
		}

		if (obj1.value.length > 80) {
			convertAlertToModelPopUp(accountNameLengthErrorCode, accountNameLengthError);
			return false;
		}

		if (obj2.value.length > 30) {
			convertAlertToModelPopUp(streetLengthErrorCode, streetLengthError);
			return false;
		}

		if (obj3.value.length > 30) {
			convertAlertToModelPopUp(street2LengthErrorCode, street2LengthError);
			return false;
		}

		if (obj4.value.length > 40) {
			convertAlertToModelPopUp(cityLengthErrorCode, cityLengthError);
			return false;
		}

		if (obj6.value.length > 10) {
			convertAlertToModelPopUp(zipCodeLengthErrorCode, zipCodeLengthError);
			return false;
		}

		if (obj7.value.length > 30) {
			convertAlertToModelPopUp(phoneNumLengthErrorCode, phoneNumLengthError);
			return false;
		}

		if (document.getElementById('countryforphone').innerHTML == 'USA'
				|| document.getElementById('countryforphone').innerHTML == 'US') {

			if (obj7.value.length > 10) {
				convertAlertToModelPopUp(phoneNoMax10ErrorCode, phoneNoMax10Error);
				return false;
			}

			if (/^[0-9]*$/.test(obj7.value) == false) {
				convertAlertToModelPopUp(invalidPhoneNumberErrorCode, invalidPhoneNumberError);
				return false;
			}
		}

		if (obj8.value.length > 30) {
			convertAlertToModelPopUp(faxLengthErrorCode, faxLengthError);
			return false;
		}

		if (obj9.value.length > 240) {
			convertAlertToModelPopUp(emailLengthErrorCode, emailLengthError);
			return false;
		}

		if (obj15 != null && obj15.value.length > 24) {
			convertAlertToModelPopUp(stateLengthErrorCode, stateLengthError);
			return false;
		}

		submitForm('updateAccount')
		return true;
	}

	function doReset() {

		submitForm('populateAccountForUpdate')
		return true;
	}

	
	
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
	
	/*Data table for fuzzy search	*/
	
	$(function() {

		$('#fuzzySearchTable').dataTable({"sPaginationType": "full_numbers",
			"autoWidth": false,
			"scrollX":true,
			"sScrollX": "100%",
			"sScrollXInner": "110%",
			"bScrollCollapse": true,
		})
		.columnFilter();
		//Persisting search value after export
		
	});