/**
 * 
 */

$(function() {
	var RegistrationListModel = Backbone.Model.extend({

		initialize : function(){
				this.set({
					regListData : [],
					pageNo : 0,
					totalDispRec : 0
				});
		},
		
		//Update the email id
		updateEmailId : function( registrationId, onsiteEmail ){
			//Show the loader
			$("div.loading-overlay").show();
			$.ajax({
				url : _path+'/technicalregsitration/json/updateEmail.action?registrationId='+registrationId+'&onsiteEmail='+onsiteEmail,
				dataType : 'json',
				success : function( data ){
					convertAlertToModelPopUpServerSideMsg(data);
					//Hide the loader
					$("div.loading-overlay").hide();
				},
	            error : function(data){
	           	 //Hide the loader
	           	 $("div.loading-overlay").hide();
	            }
			});
		},
		
		//Email Onboarding file to user specified mail
		emailOnBoardingFile : function( registrationId, emailId ){
			//Show the loader
			$("div.loading-overlay").show();
			$.ajax({
				url : _path+'/technicalregsitration/json/emailOnBoardingFile.action?registrationId='+registrationId+'&emailId='+emailId,
				dataType : 'json',
				success : function( data ){
					convertAlertToModelPopUpServerSideMsg(data);
					//Hide the loader
					$("div.loading-overlay").hide();
				},
				error : function(data){
		           	 //Hide the loader
		           	 $("div.loading-overlay").hide();
		        }
			});
		},
	});

	window.regListModel = new RegistrationListModel();
});