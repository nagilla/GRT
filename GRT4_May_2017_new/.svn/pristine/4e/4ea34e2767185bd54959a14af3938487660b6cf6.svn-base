function saveOrUpdate(flag, rowId, alertId) {
	var dateTime = "";
	document.getElementById("addAlert").style.display='block';
	document.getElementById("alertId").value = alertId;
	if(flag == 'add'){
		document.getElementById("requiredAction").value = "add";
		document.getElementById("alert").value = "";
		document.getElementById("sDate").value = "";
		document.getElementById("sTime").value = "00:00:00";
		document.getElementById("eDate").value = "";
		document.getElementById("eTime").value = "00:00:00";
		document.getElementById("startDate").value = "";
		document.getElementById("endDate").value = "";
	} else if(flag == 'update'){
		document.getElementById("requiredAction").value = "update";
		document.getElementById("alert").value = document.getElementById("hiddenMessage"+rowId).value;
		dateTime = document.getElementById("startDate"+rowId).innerHTML.split(" ");
		document.getElementById("sDate").value = dateTime[0];
		document.getElementById("sTime").value = dateTime[1];
		dateTime = document.getElementById("endDate"+rowId).innerHTML.split(" ");
		document.getElementById("eDate").value = dateTime[0];
		document.getElementById("eTime").value = dateTime[1];
	}
}

function saveOrUpdateAlerts() {
	var message = document.getElementById("alert").value;
	var startdate = document.getElementById("sDate").value;
	var endDate = document.getElementById("eDate").value;
	var startTime = document.getElementById("sTime").value;
	var endTime = document.getElementById("eTime").value;
	var currentdateTime = getCurrentDateString();
	var currentDate = currentdateTime[0];
	var currentTime = currentdateTime[1];
	if(message == "") {
		var errorMsgKey = document.getElementById("alertMessageBlankMsgKey").value;
		var errorMsg = document.getElementById("alertMessageBlankMsg").value;
		convertAlertToModelPopUp(errorMsgKey, errorMsg);
		//alert('Please enter alert message.');
		return false;
	} else if(!textLimit(document.getElementById("alert"),4000)) {
		var errorMsgKey = document.getElementById("alertMessageLenghtOverMsgKey").value;
		var errorMsg = document.getElementById("alertMessageLenghtOverMsg").value + " "+document.getElementById("alert").value.length;
		convertAlertToModelPopUp(errorMsgKey, errorMsg);
		//alert('You cannot enter more than 4000 characters. Characters entered:'+field.value.length);
		//alert('More than 4000 characters');
		return false;
	} else if(!isValid(message)) {
		var errorMsgKey = $("#alertSpecialCharErrorMsgKey").val();
		var errorMsg = $("#alertSpecialCharErrorMsg").val();
		convertAlertToModelPopUp(errorMsgKey, errorMsg);
		return false;
	}
	if(startdate == "") {
		var errorMsgKey = document.getElementById("alertStartDateBlankMsgKey").value;
		var errorMsg = document.getElementById("alertStartDateBlankMsg").value;
		convertAlertToModelPopUp(errorMsgKey, errorMsg);
		//alert('Please enter start date.');
		return false;
	}
	if(endDate == "") {
		var errorMsgKey = document.getElementById("alertEndDateBlankMsgKey").value;
		var errorMsg = document.getElementById("alertEndDateBlankMsg").value;
		convertAlertToModelPopUp(errorMsgKey, errorMsg);
		//alert('Please enter end date.');
		return false;
	}
	if(parseDate(currentDate, currentTime)!= null && parseDate(startdate, startTime)!= null && validateTime(currentTime) != null && validateTime(startTime) != null){
		if(parseDate(currentDate, currentTime) > parseDate(startdate, startTime)) {
			var errorMsgKey = document.getElementById("alertStartDateLessCurrDateMsgKey").value;
			var errorMsg = document.getElementById("alertStartDateLessCurrDateMsg").value;
			convertAlertToModelPopUp(errorMsgKey, errorMsg);
			//alert('Start Date cannot be less than Current Date.');
			return false;
		}
	}
	if(parseDate(startdate, startTime)!= null && parseDate(endDate, endTime)!= null && validateTime(startTime) != null && validateTime(endTime) != null){
		if(parseDate(startdate, startTime) <= parseDate(endDate, endTime)){
			document.getElementById("startDate").value = startdate + " " + startTime + " " + "EST";
			document.getElementById("endDate").value = endDate + " " + endTime + " " + "EST";
			document.getElementById('alertsList').action = document.getElementById('updateAnnouncementAction').value;
			document.getElementById('alertsList').submit();
		} else {
			var errorMsgKey = document.getElementById("alertStartDateGreaterEndDateMsgKey").value;
			var errorMsg = document.getElementById("alertStartDateGreaterEndDateMsg").value;
			convertAlertToModelPopUp(errorMsgKey, errorMsg);
			//alert('Start Date cannot be greater than End Date.');
			return false;
		}
	} else {
		var errorMsgKey = document.getElementById("alertDatesNotValidMsgKey").value;
		var errorMsg = document.getElementById("alertDatesNotValidMsg").value;
		convertAlertToModelPopUp(errorMsgKey, errorMsg);
		//alert('Please verify Start Date and End Date.');
		return false;
	}
}

function cancelSaveOrUpdate() {
	document.getElementById("alert").value = "";
	document.getElementById("sDate").value = "";
	document.getElementById("sTime").value = "00:00:00";
	document.getElementById("eDate").value = "";
	document.getElementById("eTime").value = "00:00:00";
	document.getElementById("addAlert").style.display = 'none';
}

function deleteAlert(alertId, rowId) {
	var warnMsgKey = document.getElementById("alertDeleteConfirmationMsgKey").value;
	var warnMsg = document.getElementById("alertDeleteConfirmationMsg").value + " " + document.getElementById("message"+rowId).innerHTML;
	//if(confirm("Are you sure to delete the alert: "+ document.getElementById("message"+rowId).innerHTML)) {
	//document.getElementById("alertId").value = alertId;
	
	var succesCallBack = "deleteConfirmed('"+alertId+"')";
	var failedCallBack = "deleteNotConfirmed()";
	convertConfirmToModelPopUp(warnMsgKey, warnMsg, succesCallBack, failedCallBack);
}

function deleteConfirmed(alertId) {
	document.getElementById("alertId").value = alertId;
	document.getElementById("requiredAction").value = "delete";
	document.getElementById('alertsList').action = document.getElementById('updateAnnouncementAction').value;
	document.getElementById('alertsList').submit();
}

function deleteNotConfirmed() {
	return false;
}

function parseDate(str, time){
  var dateDelim = "/";
  str = str.substr(0,2) + dateDelim + str.substr(2,2) + dateDelim + str.substr(4,str.length-4);
  var t = str.match(/^(\d{2})\/(\d{2})\/(\d{4})$/);
  var ts = time.match(/^(\d{2}):(\d{2}):(\d{2})$/);
  if(t!==null && ts != null){
    var m=+t[1], d=+t[2], y=+t[3], h=+ts[1], mi=+ts[2], s=+ts[3];
    var date = new Date(y,m-1,d,h,mi,s,0);
    if(date.getFullYear()===y && date.getMonth()===m-1){
      return date;   
    }
  }
  return null;
}

function getCurrentDateString() {
	var currentTime = document.getElementById('currentTime').innerHTML;
   	var today = new Array();
   	today[0] = currentTime.substr(0,8);
   	today[1] = currentTime.substr(9,currentTime.length);
   	return today;
}

function validateTime(obj) {
    var timeValue = obj;
    if(timeValue == "" || timeValue.indexOf(":")<0) {
    	var errorMsgKey = document.getElementById("alertInvalidTimeFormatMsgKey").value;
		var errorMsg = document.getElementById("alertInvalidTimeFormatMsg").value;
		convertAlertToModelPopUp(errorMsgKey, errorMsg);
        //alert("Invalid Time format");
        return null;
    } else {
        var sHours = timeValue.split(':')[0];
        var sMinutes = timeValue.split(':')[1];
		var sSeconds = timeValue.split(':')[2];
		if(validateMinutesAndSeconds(sHours, 23) && validateMinutesAndSeconds(sMinutes, 59) && validateMinutesAndSeconds(sSeconds, 59)) {
			obj = validateMinutesAndSeconds(sHours, 23) + ":" + validateMinutesAndSeconds(sMinutes, 59) + ":" + validateMinutesAndSeconds(sSeconds, 59); 
		} else {
			return null;
		}
    }
    return obj;
}

function validateMinutesAndSeconds(source, maxValue) {
	if(source == "" || isNaN(source) || parseInt(source) > maxValue) {
		var errorMsgKey = document.getElementById("alertInvalidTimeFormatMsgKey").value;
		var errorMsg = document.getElementById("alertInvalidTimeFormatMsg").value;
		convertAlertToModelPopUp(errorMsgKey, errorMsg);
		//alert("Invalid Time format");
		return false;
	} else if(parseInt(source) == 0)
		source = "00";
	else if (source <10)
		source = "0"+source;
	return source;
}
function textLimit(field, maxlen) {
	if(field.value.length > maxlen-1) {
		//alert('You cannot enter more than 4000 characters. Characters entered:'+field.value.length);
		return false;
	} else {
		return true;
	}
}

function goToHome() {
	window.history.back();
}

function isValid(str) {
	 return !/[~`!#$%\^&*+=\-\[\]\\';,/{}|\\":<>\?]/g.test(str);
}