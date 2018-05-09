/**
 * 
 */

$(function(){
	var loadingOverlay = $(".loading-overlay");
	loadingOverlay.hide(); //hide loader by default
	var LocationSelection = Backbone.View.extend({
		el : $("#locationWrapper"),
		
		initialize : function() {
			var self = this;
			this.el.find('.search-soldto-input').autocomplete(self.options);
		},
		
		/* START :: Sold To Autocomplete */
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
					'width' : '225px'
				}).addClass('global-auto-complete').find('li').each(function() {
					var a = $(this).find('a');
					a.html(a.text().replace(/\[<b>\]/g, '<strong>').replace(/\[<\/b>\]/g, '</strong>'));
				});
			}	
		},
		
		/* END :: Sold To Autocomplete */
		
		closeErrorPopup : function(){
			this.el.find('.error-message').hide();
		},
		
		setSoldToValue : function(){
			var soldTo = $.trim(this.el.find('#soldToNumber').val()).split(" ")[0];
			soldTo = $.trim(soldTo);
			this.el.find("input#soldToId").val( soldTo  );
			$("input[type=hidden][name=soldTo]").val( soldTo  );
			$("input[type=hidden]#soldToId").val( soldTo  );
			
			//Show Loading Popup
			loadingOverlay.show();
			return true;
		},
		
		redirectToHomePage : function(){
			window.location.href = _path+"/home-action.action";
		},
		
		events : {
			'click .errPopupBtn' : 'closeErrorPopup',
			'click #csrNext' : 'setSoldToValue',
			'click input[type=button].cancelBtn' : 'redirectToHomePage',
			'submit form': 'setSoldToValue'
		}
		
	});
	
	locationSelection = new LocationSelection();
	
});

function show_popup(regId) {	
	document.getElementById("dialog-underlay").style.display = "block";
	document.getElementById("popup-window").style.left = "auto";
	document.getElementById("regId").innerHTML = regId;
}

function closePopup() {
	document.getElementById("dialog-underlay").style.display = "none";
	document.getElementById("popup-window").style.left = "-1000em";
}


//GRT 4.0 Changes
/*function setEnableNextButton(val)
{

	if(val.value!="")
	{
		document.getElementById("csrNext").className = "button gray";
		document.getElementById("csrNext").disabled = false;
	}else{
		document.getElementById("csrNext").className = "button";
		document.getElementById("csrNext").disabled = true;
	}
	
}*/

//Chat Phase 2 : Enable next button by default
/*$(document).ready(function(){
	$("#soldToNumber").keyup(function(){
		var soldToValue = $(this).val();
		if( $.trim(soldToValue)!="" ){
			$('#csrNext').removeAttr('disabled');
			$('#csrNext').addClass('gray');
			$('#csrNext').removeClass('disabled');
		}else{
			$('#csrNext').attr('disabled',true);
			$('#csrNext').removeClass('gray');
			$('#csrNext').addClass('disabled');
		}
	});

	$("form#myForm").mouseover(function(){
		var soldToValue = $('#soldToNumber').val();
		if( $.trim(soldToValue)!="" ){
			$('#csrNext').removeAttr('disabled');
			$('#csrNext').addClass('gray');
			$('#csrNext').removeClass('disabled');
		}else{
			$('#csrNext').attr('disabled',true);
			$('#csrNext').removeClass('gray');
			$('#csrNext').addClass('disabled');
		}
	});
});*/