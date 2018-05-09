$(function() {

	var RegistrationHome = Backbone.View.extend({

		el : $('.home-page'),

		//Handling collapsable contents
		showHideContents : function(e){
			
			var domElement = e.srcElement;
			if(domElement==undefined){
				domElement = e.target;
			}
			
			/*var contentBox = $(domElement).parents('div.collapse-box').find('.collapse-box-container');
			if( contentBox.is(':visible') )
				$(domElement).parents('div.collapse-box').find('.collapse-box-container').hide();
			else
				$(domElement).parents('div.collapse-box').find('.collapse-box-container').show();*/
			
			//Change the image
			var imgElement = $(domElement).parents('div.collapse-box').find('i');
			var classFlag = imgElement.hasClass('fa fa-minus');
			if( classFlag ){
				imgElement.removeClass('fa fa-minus');
				imgElement.addClass('fa fa-plus');
			}else{
				imgElement.removeClass('fa fa-plus');
				imgElement.addClass('fa fa-minus');
			}
		},
				
		events : {
			//Announcements Header Events
			'click .collapse-box-header' : 'showHideContents'
		}

	});

	regHome = new RegistrationHome();
	
});