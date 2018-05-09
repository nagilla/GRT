/**
 * 
 */

$(function() {
	
	var alertKeyForSmthngWrng=$('#alertSomethingWrngErrorCode').val();
	var alertMsgForSmthngWrng=$('#alertSomethingWrng').val();
	
	var InstallBaseCreation = Backbone.View.extend({

		el : $('#saveSiteRegistrationForm'),

		createInstallBase : function() {
			$.ajax({
				url : _path + "" ,
				dataType : 'json',
				success : function( data, ui ) {
					
				},
				error : function( error ){
					convertAlertToModelPopUp(alertKeyForSmthngWrng, alertMsgForSmthngWrng);
				}
			});
		},

		events : {
			'click .scvNextBtn' : 'createInstallBase',
		},

	});

	installBaseCreation = new InstallBaseCreation();

	//Chat Phase 2 : show validate material code button only when loaded on iframe(co-browse)
	function inIframe() {
	    try {
	        return window.self !== window.top;
	    } catch (e) {
	        return true;
	    }
	}
	if(inIframe()) {
		$('input[id^="validateMcCoBrowse"]').show();
	}
	
	
});