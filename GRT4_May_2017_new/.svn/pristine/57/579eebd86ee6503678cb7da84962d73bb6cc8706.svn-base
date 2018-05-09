/**
 * Handle UI Operations on TOB Dashboard View
 */
/* Create an array with the values of all the checkboxes in a column */
$.fn.dataTable.ext.order['dom-checkbox'] = function  ( settings, col )
{
    return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
        return $('input', td).prop('checked') ? '1' : '0';
    } );
};

$(function() {

	var TOBDashBoardView = Backbone.View.extend({

		el : $('#technicalRegistrationForm'),

		initialize : function(){
			//Initialize the datatable functionality
			this.createDataTableForPage();

			//Handle collapse box
			this.showCollapseDiv();
		},

		createDataTableForPage : function(){
			//Destroy Data Table
			var regProd = $('.registrableProd').hasClass('dataTable');
			if(regProd){
				$('.registrableProd').DataTable().destroy();
			}
			//Create Datatable for Registerable Products List
			this.el.find('.registrableProd').dataTable({"sPaginationType": "full_numbers","autoWidth": false})
			 .columnFilter();

			var readyToProcess = $('#readyToProcessTable').hasClass('dataTable');
			if(readyToProcess){
				$('#readyToProcessTable').DataTable().destroy();
			}

			//Create Datatable for Registerable Products List - Ready To Process
			this.el.find('#readyToProcessTable').dataTable({"sPaginationType": "full_numbers","scrollX":true,"autoWidth": false,"order": [[ 0, "desc" ]],
				"aoColumns": [
				 {defaultContent: ""},
		           {defaultContent: ""},
		           {defaultContent: ""},
		           {defaultContent: ""},
		           {defaultContent: ""},
		           {defaultContent: ""},
		           {defaultContent: ""},
		           { "sSortDataType": "dom-checkbox" }
			]

			});
			 //.columnFilter();
			 $("#readyToProcessTable").dataTable().fnDraw();
       $('#tob-ready-process-wrap .no-data-found').hide();

			//DataTable for Retest

			 var retestTable= $("#trRetestTable").dataTable({
				 "scrollX":true,
				 "sPaginationType": "full_numbers",
				 "aoColumns": [
				     null,
				     null,
				     null,
				     null,
				     null,
				     null,
				     null,
				     null,
				     null,
				     null,
				     null,
				     null,
				     null,
				     null,
				     null
				 ]}).columnFilter();

		},

		showCollapseDiv : function(){
			//Check ready to process data
			if( $("#readyToProcessFlag").length > 0 && $("#readyToProcessFlag").val() == "true" ){
				this.el.find('#tredProdDivToggleText').show();
				this.el.find('div#tob-ready-process-wrap h2.collapse-box-header').addClass('active');
				//Fix for issue 691 - START
				this.el.find('#readyForTRDivToggleText').show();
				this.el.find('div#tob-ready-for-wrap h2.collapse-box-header').addClass('active');
				//Fix for issue 691 - END
				
				var soldTo = document.getElementById("soldTo").textContent;
				var registrationId = document.getElementById("registrationId").innerHTML;

				//if(registrableProd==null && value=="readyForTRDiv") {//Load only for 1st div
					populateRegistrableProductList(soldTo,registrationId);
				//}
			}

			//Ready to Re-test div
			if( $("#retestDataFlag").length > 0 && $("#retestDataFlag").val() == "true" ){
				this.el.find('#trRetestToggleText').show();
				this.el.find('div#tob-retest-wrap h2.collapse-box-header').addClass('active');
			}
		},

		events : {
		},

	});

	tobDashBoardView = new TOBDashBoardView();

		$('#trRetestTable').dataTable().fnDestroy();
		 var retestTable= $("#trRetestTable").dataTable({
			 "scrollX":true,
			 "sPaginationType": "full_numbers",
			 "aoColumns": [
			     null,
			     null,
			     null,
			     null,
			     null,
			     null,
			     null,
			     null,
			     null,
			     null,
			     null,
			     null,
			     null,
			     null,
			     null
			 ]}).columnFilter();
		 retestTable.fnDraw();
     $('#tob-retest-wrap .no-data-found').hide();

		 $('#readyToProcessTable').dataTable().fnDestroy();
		 var resdyToProcessTable= $('#readyToProcessTable').dataTable({"sPaginationType": "full_numbers","scrollX":true,"autoWidth": false,
				"aoColumns": [
				 {defaultContent: ""},
		           {defaultContent: ""},
		           {defaultContent: ""},
		           {defaultContent: ""},
		           {defaultContent: ""},
		           {defaultContent: ""},
		           {defaultContent: ""},
		           { "sSortDataType": "dom-checkbox" }
			]

			});
		 resdyToProcessTable.fnDraw();

});
