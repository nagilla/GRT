function setRegId(){
	document.getElementById('filter_registrationId').value = document.getElementById('regId').innerHTML;
}

/*function show_popup(regId) {	
		document.getElementById("dialog-underlay").style.display = "block";
		document.getElementById("popup-window").style.left = "auto";
		document.getElementById("regId").innerHTML = regId;
	}
	
	function closePopup() {
		document.getElementById("dialog-underlay").style.display = "none";
		document.getElementById("popup-window").style.left = "-1000em";
	}*/

	/*function getFirstSiteRegistrationIdIfStatusIsInProgress() {
		dwr.engine.beginBatch();
		pageflowAccess.getFirstSiteRegistrationIdIfStatusIsInProgress(document.getElementById('soldToStorer').value,
			function(data) {
				if(data) {
					show_popup(data);
				} else {
					document.getElementById("newRegistrationNavigator").click();
				} 
			}
		);
		dwr.engine.endBatch({
			parameters: {"jpfScopeID": "<%=jpfScopeID %>"}
		}); 
	}*/
	

/*$(function() {

		var SelectSoldTo = Backbone.View.extend({

			el : $('#customerSiteReg'),
			initialize : function() {
				var self = this;
				this.el.find('.search-soldto-input').autocomplete(self.options);
			},
		
			 START :: Sold To Autocomplete 
			options : {
				//delay : 200,
				minLength : 2,
				source : function(request, response) {
						var self = this;
						var soldTo = $("input.search-soldto-input").val();
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
					var soldToId = ui.item.value.split(" ");
					$("input[type=hidden][name=soldTo]").val( soldToId[0]  );
					$("input[type=hidden][name=actionForm.soldToId]").val( soldToId[0]  );
				},
				open : function(event, ui) {
					var data = $(this).data('autocomplete');
					data.menu.element.css({
						// 'z-index' : '996',
						'width' : '225px'
					}).addClass('global-auto-complete').find('li').each(function() {
						var a = $(this).find('a');
						a.html(a.text().replace(/\[<b>\]/g, '<strong>').replace(/\[<\/b>\]/g, '</strong>'));
					});
				}	
			},
			
			 END :: Sold To Autocomplete 
			
		});			
					
		
		window.selectSoldTo = new SelectSoldTo({});
		
});*/

		


