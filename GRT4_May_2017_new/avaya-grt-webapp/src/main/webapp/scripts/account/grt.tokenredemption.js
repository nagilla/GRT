/**
 * 
 */

$(function(){
	var loadingOverlay = $(".loading-overlay");
	loadingOverlay.hide(); //hide loader by default
	var TokenRedemption = Backbone.View.extend({
		el : $(".token-redemption"),
		
		initialize : function() {
			var self = this;
			this.el.find('#shiptocreationforBP .autocompleteBp').autocomplete(self.options);
		},
		
		/* START :: Sold To Autocomplete */
		options : {
			//delay : 200,
			minLength : 2,
			source : function(request, response) {
					var self = this;
					var soldTo = $("#shiptocreationforBP .autocompleteBp").val();
					self.menu.element.find('.ui-menu-item').stop().animate({
						'opacity' : 0
					}, 'slow');
					$.ajax({
						url : _path + "/technicalregsitration/json/getCxpSoldToList.action?soldTo="+soldTo,
						dataType : 'json',
						success : function(data) {
							response( data );
						}
					});
				},

			select : function(e, ui, category, el) {
				/*var soldToId = ui.item.value.split(" ");
				$("input[type=hidden][name=soldTo]").val( soldToId[0]  );
				$("input[type=hidden]#soldToId").val( soldToId[0]  );*/
				//if( soldToValue!="" ){
				$('#csrNext').removeAttr('disabled');
				$('#csrNext').addClass('gray');
				/*}else{
					$('#searchAgent').attr('disabled',true);
					$('#searchAgent').removeClass('gray');
				}*/
			},
			open : function(event, ui) {
				var data = $(this).data('autocomplete');
				data.menu.element.css({
					// 'z-index' : '996',
					'width' : '430px'
				}).addClass('global-auto-complete').find('li').each(function() {
					var a = $(this).find('a');
					a.html(a.text().replace(/\[<b>\]/g, '<strong>').replace(/\[<\/b>\]/g, '</strong>'));
				});
			}	
		},
		
		/* END :: Sold To Autocomplete */
	});
	
	tokenRedemption = new TokenRedemption();
	
});
