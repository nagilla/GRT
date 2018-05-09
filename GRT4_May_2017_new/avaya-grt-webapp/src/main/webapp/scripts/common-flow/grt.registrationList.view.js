/**
 * JS to handle UI logic on Registration List
 */
$(function() {
	var regListTable = '';
	var firstTimeLoad = true;
	var RegistrationListView = Backbone.View.extend({

		el : $('#registrationContent'),

		initialize : function(){
			//Get values for registration type and status multiselect filters
			this.populateValuesForMultiselectOptions();

			//Initialize the datatable plugin
			this.createRegistrationListDataTable();
			
			var orderFalg=false;
			setTimeout(function(){
				//console.log($(".regListTable td.sorting")[0]);
				orderFalg=true;
				$($(".regListTable td.sorting")[0]).click();
			}, 150);

			/* Code to trigger event on first time load */
			//Set the dafault filter if any
	       	 var registrationId = $("#registrationId_f").val();
	       	 var requesterName = "";
	       	 if(  $("#requesterName_f")!=undefined &&  $("#requesterName_f").length > 0 ){
	       		requesterName = $("#requesterName_f").val();
	       	 }
	       	 //trigger custom enter event
       		 var event = jQuery.Event('keyup');
       		 event.keyCode = 13;
	       	 if ( registrationId != "" ){
	       		 //set the filter
	       		/* $("span.filter_column input[rel=0]:first").val(registrationId);
	       		 $("span.filter_column input[rel=0]:first").removeClass("search_init");
	       		 $("span.filter_column input[rel=0]:first").trigger(event);*/
	       	 }else if( requesterName!="" ){
	       		 //set the filter
	       		 $("span.filter_column input[rel=1]:first").val(requesterName);
	       		 $("span.filter_column input[rel=1]:first").removeClass("search_init");
	       		 $("span.filter_column input[rel=1]:first").trigger(event); //trigger event for requester name
	       	 }

		},

		//create the datatable
		createRegistrationListDataTable : function(){
			//Initialize the sorting and filtering functionality
			 onResize('none');
			$.datepicker.regional[""].dateFormat = 'mm/dd/yy';
            $.datepicker.setDefaults($.datepicker.regional['']);
            var url = _path+"/technicalregsitration/json/registrationListJson";
            regListTable = this.el.find('#registrationlist').dataTable(
					{
						"sPaginationType": "full_numbers",
						//"bProcessing": true,
						"bServerSide": true,
						"sAjaxSource": url,
						"scrollX":true,
						"sScrollX": "100%",
						"sScrollXInner": "110%",
						"bScrollCollapse": true,
						"stateSave": true,
						"bStateSave"		: true,
						/*"fnStateSaveParams": function (oSettings, oData) {
						      console.log(JSON.stringify(oData));
						      oData.loggedInUser = 'test';
						},
						"fnStateLoadParams" : function(oSettings, oData) {
							console.log(oData.loggedInUser);
						},*/
						"fnServerData": function ( sSource, aoData, fnCallback ) {
							var newMap = {};
							var doubleCancel = false;

							//Handling for 1st time data loading
							if( firstTimeLoad ){
								newMap['firstTimeLoad'] = "yes";
							}

							//Add Custom Value for Exclude Cancelled Status Checkbox
							newMap['filterValue'] = $("#filterValue").val();
							var isExCanStatus = $("#excludeCancelledStatus").is(':checked');
							if( isExCanStatus ){
								newMap['exCancelledStatus_f'] = "yes";
							}else{
								newMap['exCancelledStatus_f'] = null;
							}

							 //Convert AoData to simple Map
							$.each( aoData, function( index, map ) {
								if( map.value == "~" ) map.value="";
							
							    newMap[map.name] = map.value;
							    //Defect #830 : Don't show cancelled popup
							    /*if( map.value == 'Cancelled' && isExCanStatus ){
							    	doubleCancel = true;
							    	var regSelectedCancRegErrMsg = $("#regSelectedCancRegErrMsg").val();
							    	var regSelectedCancRegErrMsgCode = $("#regSelectedCancRegErrMsgCode").val();
							    	convertAlertToModelPopUp(regSelectedCancRegErrMsgCode, regSelectedCancRegErrMsg);
							    	return false;
							    }*/
							});
							if( doubleCancel ){
								return false;
							}

							var tempArr = [];
						    tempArr.push( newMap );
							var obj = { dataTableParams : newMap };
							$("#filterValue").val("ASC");
							//Show the loader

							if( firstTimeLoad || orderFalg ){
								$("#filterValue").val("DESC");
							}
							orderFalg=false;
							if( firstTimeLoad == false ){
								//Make the ajax call to get data
								$("div.loading-overlay").show();
								$.ajax({
						            "url": url,
						            "type": "POST",
						            "contentType" : 'application/json',
						            "cache": false,
						            "data" : JSON.stringify(obj),
						             "success" :function(json){
						            	
						            	 if(json.iTotalRecords<=0){
						            		 onResize('none');
							            	}else{
						            		 onResize('block');
						            	 }
						            	 
						            	 fnCallback(json);
						            	 //Hide the loader
						            	 $("div.loading-overlay").hide();
						             },
						             "error" : function(data){
						            	 //Hide the loader
						            	 $("div.loading-overlay").hide();
						             }
								});
							}else{
								 fnCallback({"iTotalDisplayRecords":0,"iTotalRecords":0,"aaData":[],"sEcho":1});
							}

							//Reset the flag
							firstTimeLoad = false;
						}
						,
						/* Warning: Don't change the order of the columns.. if needed please modify this js
						 * Here column index starts with 0,1,...n
						 *  */
					    "aoColumns": [
					            { "mData": "registrationId" },
					            { "mData": "fullName" },
					            { "mData": "registrationIdentifier" },
					            { "mData": "grtNotificationName" },
					            { "mData": "requestEmailUIElement" },
					            { "mData": "createdDate",
					              "mRender": function(data, type, full) {
						            	  //custom function for formatting dates
						                  return regListView.formatJSONDate(data);
						          }
					            },
					            { "mData": "computedUpdateDate",
					              "mRender": function(data, type, full) {
					            	  //custom function for formatting dates
					                     return regListView.formatJSONDate(data);
					              }
					            },
					            { "mData": "computedUpdateBy" },
					            { "mData": "registrationTypeDesc" },
					            { "mData": "installBaseStatusUIElement" },
					            { "mData": "techRegStatusUIElement" },
					            { "mData": "finalRVStatusUIElement" },
					            { "mData": "eqrMoveUIElement" },
					            { "mData": "soldTo",
					            	"mRender": function(data, type, full) {
						            	  //custom function for formatting dates
						                     return encodeURI(data);
						              }
					            },
					            { "mData": "requestingCompany" },
					            { "mData": "activeSRUIElement" },
					            { "mData": "allButtonUIElement" }
					     ],
					}
			  )
			 .columnFilter(
					 { 	 sPlaceHolder: "head:before",
						 aoColumns: [ { type: "text" },
							             { type: "text" },
							             { type: "text" },
							             { type: "text" },
							             { type: "text" },
							             { type: "date-range" },
				                         { type: "date-range"  },
				                         { type: "text" },
				                         { type: "checkbox", values: regListModel.get('regTypeArray')},
				                         { type: "checkbox", values: regListModel.get('statusArray')},
				                         { type: "checkbox", values: regListModel.get('statusArray')},
				                         { type: "checkbox", values: regListModel.get('statusArray')},
				                         { type: "checkbox", values: regListModel.get('statusArray')},
				                         { type: "text" },
				                         { type: "text" },
				                         { type: "text" },
				                         null
							           ]

					 }
			 );
		},

		//Format the date for display
		formatJSONDate : function( dateInput ) {
			   if (dateInput === null) {
			     return '';
			   }
			     dateInput = dateInput.split(/[T-]/);
			     var currYear = dateInput[0];
			     var currMonth = dateInput[1];
			     var currDay = dateInput[2];
			     return currMonth + '/' + currDay + '/' + currYear;
			   return dateInput;
		},

		//Populate the value for multiselect registration type and 3 statuses
		populateValuesForMultiselectOptions : function(){
			//For Status Multiselect
			var statusStr = this.el.find('#statusList').val();
			var statusArr = this.getArrayFromStr( statusStr );
			regListModel.set({'statusArray': statusArr});


			//For Registration Type Multiselect
			var regStr = this.el.find('#regTypeList').val();
			var regArr = this.getArrayFromStr( regStr );
			regListModel.set({'regTypeArray': regArr});
		},

		//Genric Method to convert string "['a','b','c']" to array
		getArrayFromStr : function( strVal ){
			var array = [];
			if( strVal != undefined ){
				strVal = strVal.substring(1,strVal.length-1);
				array = $.map(strVal.split(","), $.trim);
			}
			return array;
		},

		//Update the email
		updateEmail : function(e) {
			var anchorElem = this.getElementFromEvent(e);
			var onsiteEmail = $(anchorElem).parents('td').find('input').val();
			//var registrationId = $(anchorElem).parents('td').siblings('td.registrationIdTd').text();
			var registrationId = $(anchorElem).parents('td').find('input').attr('id').split('_')[1];
			if (this.splitMails(onsiteEmail) == false) {
				return false;
			}
			regListModel.updateEmailId( registrationId, onsiteEmail );
		},

		//Email Onboarding file to user
		/*emailOnBoardFile : function(e) {
			$('#email-window').hide();
			//get the email entered by the user
			var emailId = $('input#userEmail').val();

			//var btnElem = this.getElementFromEvent(e);
			var registrationId = this.el.find(".hidden_reg_id").val();
			//var registrationId = $(btnElem).parents('td').siblings('td:first').text();
			regListModel.emailOnBoardingFile( registrationId, emailId );
		},*/

		//Show Email popup for onboarding button click
		showEmailPopup : function(e){
			var onbButtonElem = this.getElementFromEvent(e);
			$('input#userEmail').val('');
			//get the registration id for the row
			var registrationId = $(onbButtonElem ).parents('td').siblings('td:first').text();
			this.el.find(".hidden_reg_id").val( registrationId );
			$('#email-window').show();
		},

		// Function to split Multiple E-mails by semi-colons
		splitMails : function(val) {
			var mails = val.split(';');
			for (var i = 0; i < mails.length; i++) {
				var str = mails[i].replace(/\s/g, "");
				if ((str) != '') {
					if (!this.validateEmailId(str)) {
						var regInvalidEmailErrMsg = $("#regInvalidEmailErrMsg").val();
				    	var regInvalidEmailErrMsgCode = $("#regInvalidEmailErrMsgCode").val();
						convertAlertToModelPopUp(regInvalidEmailErrMsgCode, regInvalidEmailErrMsg + " " + str);
						return false;
					}
				}

			}
			return true;
		},

		//Exclude Cancelled status check
		excludeCanSatusFilter : function(e){
			if( regListTable != '' || regListTable != undefined ){
				  regListTable.fnDraw();
			}
		},
		//Clear all the column filters
		clearAllFilters : function(e){
			if( regListTable != '' || regListTable != undefined ){
			  //regListTable.fnFilter('');
			  var oSettings = regListTable.fnSettings();
			  for(var iCol = 0; iCol < oSettings.aoPreSearchCols.length; iCol++) {
			    oSettings.aoPreSearchCols[ iCol ].sSearch = '';
			  }
			  //Reset the column filters
			  $('#registrationContent .filter_column input[type=text]').val('');
			  $('#registrationContent .filter_column input[type=text]').blur();
			  //clear the multiselect checkbox filter
			  $('#registrationlistInstallBaseCreationStatus-flt-toggle .checkbox_filter').removeAttr('checked');
			  $('#registrationlistTechnicalOnboardingStatus-flt-toggle .checkbox_filter').removeAttr('checked');
			  $('#registrationlistRecordValidationStatus-flt-toggle .checkbox_filter').removeAttr('checked');
			  $('#registrationlistFinalValidationStatus-flt-toggle .checkbox_filter').removeAttr('checked');
			  $('#registrationlistEquipmentMoveStatus-flt-toggle .checkbox_filter').removeAttr('checked');
			  $('#registrationlistRegistrationType-flt-toggle .checkbox_filter').removeAttr('checked');
			  regListTable.fnDraw();
			}
		},

		// Function to validate email id.
		 validateEmailId : function(mail) {
			//var reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
			var reg = /^([_a-zA-Z0-9-]+[_|\_|\.]?)*[_a-zA-Z0-9-]+@([a-zA-Z0-9-]+[_|\_|\.]?)*[a-zA-Z0-9-]+\.[a-zA-Z]{2,9}$/;
			if (!reg.test(mail)) {
				return false;
			}
			return true;
		},

		getElementFromEvent : function(e){
			var elem = e.srcElement;
			if( elem == undefined ){
				elem = e.target;
			}
			return elem;
		},

		events : {
			'click .updateEmailLink' :  'updateEmail',
			'click .emailOnBoardFileBtn' : 'showEmailPopup',
			//'click #sendEmail' : 'emailOnBoardFile',
			'click #excludeCancelledStatus' : 'excludeCanSatusFilter',
			'click #clearFilters' : 'clearAllFilters'
		}

	});

	window.regListView = new RegistrationListView();
	
	
	/* On Boarding File functionality */
	$("#sendEmail").click(function(){
		$('#email-window').hide();
		//get the email entered by the user
		var emailId = $('input#userEmail').val();

		//var btnElem = this.getElementFromEvent(e);
		var registrationId = $(".hidden_reg_id").val();
		//var registrationId = $(btnElem).parents('td').siblings('td:first').text();
		regListModel.emailOnBoardingFile( registrationId, emailId );
	});
	
	//Add id to attribute Id to all input filter filed. Co-browse issue Fix	
	$(".regListTable tr:first-child").find('td').each(function(index) {
		var inputElement = $(this).find('input');
		if(inputElement != null && (inputElement.attr("id") == null || inputElement.attr("id") =="")) {
			inputElement.attr("id","regListCoBrowse_"+index);
		} 
	});
	
});


var $style = $("<style type='text/css'>").appendTo('head'); 

function onResize(val) {
    var css = "\
        .regListTable td:last-child,\
		#registrationList td:last-child{\
			display:"+val+";}";

    $style.html(css);
}

function closeEmailPopup(){
	$('#email-window').hide();
}
