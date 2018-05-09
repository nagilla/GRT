/**
 * 
 * Dynamic Table JS
 * 
 */

function addLoadEvent(func) {

	var oldonload = window.onload;

	if (typeof window.onload != 'function') {

		window.onload = func;

	} else {

		window.onload = function() {

			if (oldonload) {

				oldonload();

			}

			func();

		}

	}

}

function buildTable(form) {
	document.getElementById("dialog-underlay").style.display = "block";
	document.getElementById("updateEmailPopup").style.left = "auto";
	document.getElementById("infoContent").innerHTML = "Loading data...";
	document.getElementById("popupCloseBtn").style.display = "none";
	saveCookieToFlash('assembler');
	var parameterMap = getParameterMap(form);
	Assembler.getTable(parameterMap, userType, userId, isSuperUser,bpLinkId,prFlag, showTable);
}

function buildTableNoCookie(form) {

	var parameterMap = getParameterMap(form);
	Assembler.getTable(parameterMap, userType, userId, isSuperUser,bpLinkId,prFlag, showTable);
}
function buildHeader() {
	document.getElementById("dialog-underlay").style.display = "block";
	document.getElementById("updateEmailPopup").style.left = "auto";
	document.getElementById("infoContent").innerHTML = "Loading data...";
	document.getElementById("popupCloseBtn").style.display = "none";
	Assembler.getTable(null, userType, userId, isSuperUser,bpLinkId,prFlag, initTable);

}

function getListByRegistrationId(){
	document.getElementById("dialog-underlay").style.display = "block";
	document.getElementById("updateEmailPopup").style.left = "auto";
	document.getElementById("infoContent").innerHTML = "Loading data...";
	document.getElementById("popupCloseBtn").style.display = "none";
	Assembler.getTable(null, userType, userId, isSuperUser,bpLinkId,prFlag, filterTableById);
}

function filterTableById(table){
	document.getElementById('tableDiv').innerHTML = table;
	changeTableHead();
	var form = 'assembler';
	if(document.forms[form]){
		document.forms.assembler.assembler_f_RegistrationID.value = document.getElementById('filter_registrationId').value;
		buildTableNoCookie(form);
	}
}

function initTable(table) {
	document.getElementById('tableDiv').innerHTML = table;
	changeTableHead();
	CrossBrowserCookieManager.LoadCookie();
		var parameterMap = getMapFromCookie('assembler');
		if (parameterMap == null) {
			buildTableNoCookie('assembler');
		} else {
			Assembler.getTable(parameterMap, userType, userId, isSuperUser,bpLinkId,prFlag,
					showTable);
		}

}

function showTable(table) {
	document.getElementById('tableDiv').innerHTML = table;
	changeTableHead();
	document.getElementById("dialog-underlay").style.display = "none";
	document.getElementById("updateEmailPopup").style.left = "-1000em";
}
/*<!-- This function is added for Technical Registration -->*/

//function techRegDetail(ownedByCurrentUserFlag, registrationId, processStepId, statusId, soldToId) {
	//if (ownedByCurrentUserFlag) {
	//	redirection(false, registrationId, processStepId, statusId, soldToId, "an");
//	} else {
	//	document.getElementById("takeOwnershipBtn").onclick = function() {
	//		document.getElementById("takeOwnershipOfRequestPopup").style.left = "-1000em";
	//		redirection(true, registrationId, processStepId, statusId, soldToId, "an");
	//	};
	//	showTakeOwnershipOfRequestPopupHref();
//	}
//} 
function directA(ownedByCurrentUserFlag, registrationId, processStepId,
		statusId, soldToId) {
	if (ownedByCurrentUserFlag) {
		redirection(false, registrationId, processStepId, statusId, soldToId, "an");
	} else {
		document.getElementById("takeOwnershipBtn").onclick = function() {
			document.getElementById("takeOwnershipOfRequestPopup").style.left = "-1000em";
			redirection(true, registrationId, processStepId, statusId, soldToId, "an");
		};
		showTakeOwnershipOfRequestPopupHref();
	}
}

function navigate( source, status,registrationId, soldTo ) {
	var callback = function(){ 
		var anchor = document.getElementById("reg").getElementsByTagName("a")[0];
		
		var href = anchor.href;
		href = href.replace("an_value_000", registrationId);
		if(source == 'IB'){
			href = href.replace("an_value_001", true);
			href = href.replace("an_value_002", false);
			href = href.replace("an_value_003", false);
		}
		if(source == 'TR'){
			href = href.replace("an_value_001", false);
			href = href.replace("an_value_002", true);
			href = href.replace("an_value_003", false);
		}
		if(source == 'EQR'){
			href = href.replace("an_value_001", false);
			href = href.replace("an_value_002", false);
			href = href.replace("an_value_003", true);
		}
		href = href.replace("an_value_004", status);
		href = href.replace("an_value_005", soldTo);
		
		document.location = href;
		
	}
	show_popup("Loading data", "Waiting...", callback);
}

function techReg(ownedByCurrentUserFlag, registrationId, processStepId, statusId, soldToId, ele) {
	if (ownedByCurrentUserFlag) {
		redirection(false, registrationId, processStepId, statusId, soldToId, "techReg");
	} else {
		document.getElementById("takeOwnershipBtn").onclick = function() {
			document.getElementById("takeOwnershipOfRequestPopup").style.left = "-1000em";
			redirection(true, registrationId, processStepId, statusId, soldToId, "techReg");
		};
		showTakeOwnershipOfRequestPopup(ele);
	}
}

function redirection(changeOwnership, registrationId, processStepId, statusId,
		soldToId, linkId) {
		var callback = function(){ 
		var anchor = document.getElementById(linkId).getElementsByTagName("a")[0];
		var href = anchor.href;
		href = href.replace("an_value_000", registrationId);
		href = href.replace("an_value_001", processStepId);
		href = href.replace("an_value_002", statusId);
		href = href.replace("an_value_003", soldToId);
		href = href.replace("an_value_004", changeOwnership);			
		document.location = href;
						
	}
	show_popup("Loading data", "Waiting...", callback);
}


function show_popup(msg, title, callback) {	
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
	// Set time out for 3s
	setTimeout(callback, 3000);
}


function superUpdateStatus(registrationId, processStepId, statusId, soldToId) {
	var anchor = document.getElementById("superUpdateStatus")
			.getElementsByTagName("a")[0];
	var href = anchor.href;
	href = href.replace("an_value_000", registrationId);
	href = href.replace("an_value_001", processStepId);
	href = href.replace("an_value_002", statusId);
	href = href.replace("an_value_003", soldToId);
	document.location = href;

}

function ipoFile(registrationId, soldToId) {
	var anchor = document.getElementById("ipoFile")
			.getElementsByTagName("a")[0];
	var href = anchor.href;
	href = href.replace("an_value_000", registrationId);
	href = href.replace("an_value_001", soldToId);
	document.location = href;

}


function showTakeOwnershipOfRequestPopup(ele) {
	document.getElementById("dialog-underlay").style.display = "block";
	var elePopup = document.getElementById("takeOwnershipOfRequestPopup");
	elePopup.style.left = "auto"
	if(ele) {
			elePopup.style.top = (ele.parentNode.offsetTop) + "px"; 		
	}
}

function showTakeOwnershipOfRequestPopupHref() {
	document.getElementById("dialog-underlay").style.display = "block";
	var elePopup = document.getElementById("takeOwnershipOfRequestPopup");
	elePopup.style.left = "auto"
	elePopup.style.top = "120px"; 		
}

function closeTakeOwnershipOfRequestPopup() {
	document.getElementById("dialog-underlay").style.display = "none";
	document.getElementById("takeOwnershipOfRequestPopup").style.left = "-1000em";
}





function getLength(divLen, windowLen) {	
	if(divLen.indexOf("px") != -1) { 
		return divLen.substring(0, divLen.length-2);
	}
	else if(divLen.indexOf("%") != -1) {		
		return divLen.substring(0, divLen.length-1) * windowLen / 100;
	}
	else if(divLen.length == 0){
		return 0;
	}
	else {
	 	return divLen;
	}
}

function getScroll()
{	
	var t, l, w, cw, h, ch;	
	if (document.documentElement && document.documentElement.scrollTop) {	
		t = document.documentElement.scrollTop;		
		l = document.documentElement.scrollLeft;		
		w = document.documentElement.scrollWidth;		
		cw = document.documentElement.clientWidth;		
		h = document.documentElement.scrollHeight;		
		ch = document.documentElement.clientHeight;	
	} else if (document.body) {		
		t = document.body.scrollTop;		
		l = document.body.scrollLeft;		
		w = document.body.scrollWidth;	
		cw = document.body.clientWidth;			
		h = document.body.scrollHeight;		
		ch = document.body.clientHeight;	
	}	
	return {t:t, l:l, w:w, cw:cw, h:h, ch:ch};
}

/**
 * 
 * 
 */
function setRegistrationInfo(registrationId, processStepId, statusId) {

}

/**
 * 
 */
function changeTableHead() {
	var tds = document.getElementById('assembler').getElementsByTagName('td');
	for (i = 0; i < tds.length; i++) {
		var tdstr = tds[i].innerHTML;
		if (tdstr.indexOf('Active Related S R') != -1) {
			tds[i].innerHTML = tds[i].innerHTML.replace('Active Related S R',
					'SR# IBC/EQR');
			tds[i].title = 'Sort By SR# IBC/EQR';
		}
		if (tdstr.indexOf('Sold T O') != -1) {
			tds[i].innerHTML = tds[i].innerHTML.replace('Sold T O', 'SoldTO/FL');
			tds[i].title = 'Sort By SoldTO/FL';
		}
		if (tdstr.indexOf('Grt Notification Name') != -1) {
			tds[i].innerHTML = tds[i].innerHTML.replace('Grt Notification Name', 'GRT Notification Name');
			tds[i].title = 'Sort By GRT Notification Name';
		}
		if (tdstr.indexOf('Grt Notification Email') != -1) {
			tds[i].innerHTML = tds[i].innerHTML.replace('Grt Notification Email', 'GRT Notification Email');
			tds[i].title = 'Sort By GRT Notification Email';
		}
	}
}


function updateEmail(id, registrationId) {
	var onsiteEmail = document.getElementById(id).value;
	if (splitMails(onsiteEmail) == false) {
		return false;
	}
	document.getElementById("dialog-underlay").style.display = "block";
	document.getElementById("updateEmailPopup").style.left = "auto";
	document.getElementById("updateEmailPopup").style.top = "120px";
	document.getElementById("infoContent").innerHTML = "Your request is being processed.";
	document.getElementById("popupCloseBtn").style.display = "none";
	Assembler.updateEmail(registrationId, onsiteEmail, userId, afterUpdate);

}

// Function to split Multiple E-mails by semi-colons
function splitMails(val) {
	var str = '';
	var mails = val.split(';');
	for (i = 0; i < mails.length; i++) {
		var str = mails[i].replace(/\s/g, "");
		if ((str) != '') {
			if (!validateEmailId(str)) {
				alert('Invalide E-mail Address' + str);
				return false;
			}
		}

	}
	return true;
}

// Function to validate email id.
function validateEmailId(mail) {
	//var reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	var reg = /^([_a-zA-Z0-9-]+[_|\_|\.]?)*[_a-zA-Z0-9-]+@([a-zA-Z0-9-]+[_|\_|\.]?)*[a-zA-Z0-9-]+\.[a-zA-Z]{2,9}$/; 
	if (!reg.test(mail)) {
		return false;
	}
	return true;
}

function afterUpdate(data) {
	document.getElementById("infoContent").innerHTML = "Update Successfully";
	document.getElementById("popupCloseBtn").style.display = "";
}

function closeUpdateEmailPopup() {
	document.getElementById("dialog-underlay").style.display = "none";
	document.getElementById("updateEmailPopup").style.left = "-1000em";
	buildTable('assembler');
}

function popError(errorString, exception) {
	alert("Error message is: " + errorString + " - Error Details: "
			+ dwr.util.toDescriptiveString(exception, 2));
}
