/**
 * 
 */

	function validateipo(filein){
		//var remotecon = document.getElementById("remotecon").value;
		//if(remotecon == ""){
			//alert("Remote Connectivity Required,Choose Yes or No");
			//return false;
		//}
		document.getElementById('ipoInstallForm').action = document.getElementById('ipoInstall').href;
		document.getElementById('ipoInstallForm').submit();
		$("div.loading-overlay").show();
		return true;
	}

	function popUp()
	{
		var homeUrl = $("#begin").attr('href');
		createSuccFailPopup('You have select to cancel this registration. You will be taken back to the GRT Home page and all data entered will be lost.',homeUrl);
	}

	function enableNewRowDiv(flag){
		if(flag == 'Yes'){
			document.getElementById('additionalInfoDiv').style.display = 'block';
		} else {
			document.getElementById('ipoAccessTypeSslvpn').checked = false;
			document.getElementById('ipoAccessTypeOther').checked = false;
			document.getElementById('ipoAccessTypeTokenRedemptOnly').checked = false;
			document.getElementById('additionalInfoDiv').style.display = 'none';
		}
	}
	
	function backFromIPOffice(){
		var url = document.getElementById('backFromIPOffice').href;
		document.getElementById('ipoInstallForm').action = url;
		document.getElementById('ipoInstallForm').submit();
		//window.location =  url;
		$("div.loading-overlay").show();
	}
	
	function cancelIPOffice(){
		var url = document.getElementById('cancelIPOffice').href;
		document.getElementById('ipoInstallForm').action = url;
		document.getElementById('ipoInstallForm').submit();
		//window.location =  url;
		$("div.loading-overlay").show();
	}
	
	//Jquery Code
	$(function(){
		//Back button click event
		$("#backToRegistration").click(function(){
			document.getElementById('newRegistration').action = document.getElementById('newRegistration').href;
			document.getElementById('ipoInstallForm').submit();
			$("div.loading-overlay").show();
		});
	});
	