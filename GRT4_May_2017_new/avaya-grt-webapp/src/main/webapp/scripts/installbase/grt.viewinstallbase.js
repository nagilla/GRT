/**
 *
 */

$(function() {

	jQuery.extend(jQuery.fn.dataTableExt.oSort['dom-text-asc'] = function(x,y) {
		var retVal;
		x = $.trim(x);
		y = $.trim(y);
	 
		if (x==y) retVal= 0;
		else if (x == "" || x == "&nbsp;") retVal=  1;
		else if (y == "" || y == "&nbsp;") retVal=  -1;
		else if (x > y) retVal=  1;
		else retVal = -1;  
	 
		return retVal;
	});
	jQuery.extend( jQuery.fn.dataTableExt.oSort['dom-text-desc'] = function(y,x) {
		var retVal;
		x = $.trim(x);
		y = $.trim(y);
	 
		if (x==y) retVal= 0; 
		else if (x == "" || x == "&nbsp;") retVal=  1;
		else if (y == "" || y == "&nbsp;") retVal=  -1;
		else if (x > y) retVal=  1;
		else retVal = -1; 
	 
		return retVal;
	 });
	

	$('#productListTableDiv table#productListTable').dataTable({"sPaginationType": "full_numbers",
		"autoWidth": false,
		"scrollX":true,
		"sScrollX": "100%",
		"sScrollXInner": "110%",
		"bScrollCollapse": true,
		"aoColumns": [
			            
			            {sDefaultContent: ""},
			            {sDefaultContent: ""},
			            {"sType" : "dom-text", sDefaultContent: ""},
			            {"sType" : "dom-text-numeric", sDefaultContent: ""},
			            
			            {sDefaultContent: ""},
			            {"sType" : "dom-text", sDefaultContent: ""},
			            {sDefaultContent: ""},
			            {sDefaultContent: ""},
			            {sDefaultContent: ""},
			            {sDefaultContent: ""},
			            {sDefaultContent: ""},
		],
		"order": [[ 5, "asc" ], [ 2, "asc" ], [ 3, "asc" ]]
	})
	.columnFilter();
	//Persisting search value after export
	var oldSearchVal = $('#dtSearchVal').val();
	if( oldSearchVal!="" || oldSearchVal!=null ){
		var viewIBTable = $('#productListTableDiv table#productListTable').DataTable();
		viewIBTable.search( oldSearchVal ).draw(); //redraw the table
	}
});

function more( agreements ){
	var agreements = agreements;
	$('span.agreemntMsgTxt').text(agreements);
	$('div#showAgreement').css('display', 'inline-block');
	//convertAlertToModelPopUp(agreements);
}