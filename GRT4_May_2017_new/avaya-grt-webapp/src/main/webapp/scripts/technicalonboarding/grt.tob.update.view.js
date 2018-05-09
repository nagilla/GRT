/**
 * 
 */

$(function() {

	var TOBUpdateView = Backbone.View.extend({

		el : $('#technicalRegistrationForm'),
		
		initialize : function(){
			//Handle UserName Password Functionality
			this.userNamePassFunc();
		},

		//Show the checkbox if the 
		userNamePassFunc : function(){
			var upFlag = this.el.find('#userNamePasswordFlag').val(); //get the username-password flag
			if( upFlag == "block" ){
				//Hide all the username-password related fields
				this.el.find('.userPassDiv').hide();
			}
		},
		
		//Show-hide username password related fields on checkbox state
		showHideUserPasswordFields : function( e ){
			var elem = e.target;
			if( elem  == undefined )
				elem = e.srcElement;
			
			var chckBxState = $(elem).is(':checked');
			if( chckBxState == true ){
				//show the fields
				this.el.find('.userPassDiv').show();
			}else{
				//Hide the fields
				this.el.find('.userPassDiv').hide();
			}
		},
		
		events : {
			'click #modUPcheckBx' : 'showHideUserPasswordFields',//Modify UserName-Password checkbox event
		},

	});

	tobUpdateView = new TOBUpdateView();

});