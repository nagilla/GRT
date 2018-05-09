/**
 * 
 */

// surprizingly trim function is not supported in IE8 and above...
if (typeof String.prototype.trim !== 'function') {
	String.prototype.trim = function() {
		return this.replace(/^\s+|\s+$/g, '');
	};
}

var device = "";
if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
	device = "mobile";
} else {
	device = "desktop";
}

// this function is taken from existing website
function validateGoogleSearchBoxForm(t) {
	var tval = t.q.value.trim();
	// blank value for individual countries
	// var qObj=document.getElementById("q");
	if (tval == "" || tval == "Search") {
		// second condition if for IE8 & IE9
		var GlobalLocation = document.URL;
		var Cala_US = GlobalLocation.indexOf("/cala/en-us");
		var Cala_BR = GlobalLocation.indexOf("/cala/pt-br");
		var Cala_MX = GlobalLocation.indexOf("/cala/es-mx");
		if (Cala_US > 1) {
			alert("Por favor escriba un termino especifico de busqueda e intente nuevamente.");
			// document.base.q.focus();
			return false;
		} else if (Cala_BR > 1) {
			alert("Por favor digite um termo específico e tente novamente.");
			// qObj.focus();
			t.q.focus();
			return false;
		} else if (Cala_MX > 1) {
			alert("Por favor escriba un termino especifico de busqueda e intente nuevamente.");
			// qObj.focus();
			t.q.focus();
			return false;
		} else {
			alert("Please type a specific search term and try again.");
			// qObj.focus();
			t.q.focus();
			return false;
		}
	}

	if (tval.length < 2) {
		alert("Could not process your search.\n Please enter a search term at least 2 characters long and try again.");
		t.q.focus();
		return false;
	}

	if (tval.length > 200) {
		alert("The search text you've submitted is too long (more than 250 characters).\n Please shorten your query and try again.");
		t.q.focus();
		return false;
	}
	
	submitUsingQTopBar(tval);
}

function submitUsingQTopBar(t) {
	var urlToRedirect = "http://www.avaya.com/usa/search-results/?site=all_usa&q="+t;
	//window.open(t, '_self');
	document.getElementById("avayaSearchRedirectUrl").value = urlToRedirect;
	document.getElementById('avayaSearchForm').action = _path+"/commonUtil/redirectToAvayaSearch.action";
	document.getElementById('avayaSearchForm').submit();
}

/*document.onkeydown = checkKey;
var keyevent;
function checkKey(e) {
	keyevent = e || window.event;
	if (keyevent.keyCode == '13') {
		// submitUsingQTopBar();
	}
}*/

function xmlhttpPost(t, d) {
	var url = _path+"/commonUtil/avayaSearchList.action";
	var endUrl = 'http://www.avaya.com/search/suggest?site=all_usa&client=avaya_frontend&access=p&output=xml_no_dtd&filter=0&format=os&getfields=BusinessNeed.Industry.AvayaKNOVA:SupportGoals.google:lastmodified.BusinessSize.BusinessRole.SiteSection&q='+ encodeURI(t.value);
	$.ajax({
		url: url,
		type: 'POST',
		data: { searchUrl: endUrl },
		dataType: 'json',
		headers: {
			//'Access-Control-Allow-Origin' : 'Yes',
			'Content-type': 'application/x-www-form-urlencoded'
		},
		success: function (data, textStatus, xhr) {
			callBackFunction(xhr);
			$("#autoCompletetop").show();
		},
		error: function (xhr, textStatus, errorThrown) {
			console.log(errorThrown);
		}
	});
}

function callBackFunction(http_request) {
	if (http_request.readyState == 4) {
		if (http_request.status == 200) {
			var responceString = http_request.responseText;
			var dataStart = responceString.indexOf('["');
			var dataEnd = responceString.indexOf('"]');
			var searchData = responceString.substring(dataStart + 1, dataEnd + 1);
			var searchDataArray = searchData.split('","'); // remove the double quotes for first and last array
			searchDataArray[0] = searchDataArray[0].replace('"', '');
			searchDataArray[searchDataArray.length - 1] = searchDataArray[searchDataArray.length - 1].replace('"', '');
			var HTMLdata = "<ul>";
			if (device == "desktop") {
				for (var i = 0; i < searchDataArray.length; i++) {
					//var qStr = "http://www.avaya.com/usa/search-results/?site=all_usa&q=";
					//qStr = qStr + searchDataArray[i];
					HTMLdata = HTMLdata	+ '<li><a href="#" onclick="submitUsingQTopBar(\'' + searchDataArray[i] + '\')">' + searchDataArray[i] + '</a></li>';
				}
			} else {
				for (var i = 0; i < searchDataArray.length; i++) {
					HTMLdata = HTMLdata + '<li><a href="http://www.avaya.com/usa/search-results/?site=all_usa&q=' + searchDataArray[i] + '">' + searchDataArray[i] + '</a></li>';
				}
			}
			HTMLdata = HTMLdata + "</ul>";
			$("#searchListVisible").val("True");
			document.getElementById("autoCompletetop").innerHTML = HTMLdata;
		} else {
			// alert(http_request.responseText);
			// alert('ERROR: AJAX request status = ' + http_request.status);
		}
	}
}
