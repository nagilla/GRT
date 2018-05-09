	
	var pageChange=false;
	
	$.fn.dataTableExt.oApi.fnStandingRedraw = function(oSettings) {
		//redraw to account for filtering and sorting
		// concept here is that (for client side) there is a row got inserted at the end (for an add)
		// or when a record was modified it could be in the middle of the table
		// that is probably not supposed to be there - due to filtering / sorting
		// so we need to re process filtering and sorting
		// BUT - if it is server side - then this should be handled by the server - so skip this step
		if(oSettings.oFeatures.bServerSide === false){
			var before = oSettings._iDisplayStart;
			oSettings.oApi._fnReDraw(oSettings);
			//iDisplayStart has been reset to zero - so lets change it back
			oSettings._iDisplayStart = before;
			oSettings.oApi._fnCalculateEnd(oSettings);
		}
		  
		//draw the 'current' page
		oSettings.oApi._fnDraw(oSettings);
	};
	
	$(function() {

	/* Create an array with the values of all the input boxes in a column */
	$.fn.dataTable.ext.order['dom-text'] = function  ( settings, col )
	{
	    return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
	    	if( $('input', td).val()!=undefined ){//Input box is present
	    		return $('input', td).val();
	    	}else{
	    		return $(td).text().trim();
	    	}
	    } );
	};
	
	$.fn.dataTable.ext.order['dom-text-numeric'] = function  ( settings, col )
	{
	    return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
	    	if( $('input', td).val()!=undefined ){//Input box is present
	    		return ($('input', td).val().trim() * 1);
	    	}else{
	    		return ($(td).text().trim() * 1);
	    	}
	    } );
	};

	$.fn.dataTable.ext.order['dom-text-numeric-qtyAddRemove'] = function  ( settings, col )
	{
	    return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {				
				if( $(td).text().trim() != ""){
					return ($(td).text().trim() * 1);
				}else{
					return 999999999; // Any highest integer
				}				
	    } );
	};
	
	$.fn.dataTable.ext.order['dom-text-numeric-updatedQty'] = function  ( settings, col )
	{
	    return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
	    		if($('input', td).val() != undefined && $('input', td).val().trim() != "") {
					return ($('input', td).val().trim() * 1);
				}
				else return 999999999; // Any highest integer
	    } );
	};
	
	$.fn.dataTable.ext.order['dom-textOnly'] = function  ( settings, col )
	{
	    return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
	    	if($(td).text().trim() != "") {
				return ($(td).text().trim());
			} else return "~";  // highest ASCII char position: 127
	    } );
	};
	
	$.fn.dataTable.ext.order['dom-textOrTexbox'] = function  ( settings, col )
	{
	    return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {				
				var text = ""
				if( $("input[type!='hidden']", td).val()!=undefined && $("input[type!='hidden']", td).val().trim() != ""){//Input box is present
					text = ($("input[type!='hidden']", td).val().trim());
				}else{
					text = ($(td).text().trim());
				}		
				if(text != "") return text;
				else return "~"; // Any highest ASCII char position: 127
	    } );
	};
	
	//Sort
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

	 
	recordValidateTable =$('#productListTableDiv table#recordValidationTable').dataTable({"sPaginationType": "full_numbers",
	"autoWidth": false,
	"scrollX":true,
	
	"columns": [
		           { "orderDataType": "dom-checkbox", sDefaultContent: "" },
		           { "orderDataType": "dom-text-numeric", defaultContent: "" },
		           { "orderDataType": "dom-text-numeric-updatedQty", defaultContent: "" },
		           { "orderDataType": "dom-text-numeric-qtyAddRemove", defaultContent: "" },
		           { "orderDataType": "dom-textOnly", defaultContent: "" },
		           { "orderDataType": "dom-textOnly",defaultContent: ""},			   
		           { "orderDataType": "dom-textOrTexbox", defaultContent: "" },
		           
				   { "orderDataType": "dom-textOnly", defaultContent: ""},
		           { "sType": "dom-text", sDefaultContent: "" },
		           { "sType": "dom-text", sDefaultContent: "" },				   
		           { "sType": "dom-text", sDefaultContent: ""},				   
		           { sDefaultContent: ""},				   
				   { "orderDataType": "dom-textOrTexbox", defaultContent: "" },
				   { "sType": "dom-text", sDefaultContent: ""},
				   { "orderDataType": "dom-textOnly", defaultContent: "" },
		       ],
	"order": []
	});
	
	
		 $('input[type="submit"]').each(function() {
			    var readonly = $(this).is(':disabled');
			    
			    if(readonly) { // this is readonly
			    	$(this).removeClass('gray');
			    }
			}); 
			 $('input[type="button"]').each(function() {
				    var readonly = $(this).is(':disabled');
				   
				    if(readonly) { // this is readonly
				    	$(this).removeClass('gray');
				    }
				}); 
			 $('input[type="search"]').each(function() {
				 $(this).on('click',function(){
					 recordValidateTable.api().draw(false);
						//for Search
						$('#recordValidationTable_wrapper .dataTables_scroll').append(manulaEntryDiv);
						$('#recordValidationTable_wrapper #recordValidationTable_filter').find("input[type='search']").focus();
				 });
			 });
			 
			 $(".hiddenLable").hide(); 
			 
			
			 
			 $('div#productListTableDiv').on('mouseover','td.erroWrap',function(){
				    var spanElem = $(this).find('span:visible');
				    var titleText = "";
				    $.each(spanElem, function(index, elem){
				    	titleText += $.trim($(elem).text());
				    });
			        this.setAttribute( 'title', titleText);
			    });
			 
	});
	
	
	function backToRegistrationList() {
		var url = document.getElementById('backToRegistrationList').href;
		document.getElementById('recordValidationForm').action = url;
		document.getElementById('recordValidationForm').submit();
		$("div.loading-overlay").show();
	}
	function setValueInLabel(index,val,type)
	{
		if(type=="Quantity")
			$("#lblremainingQuantity"+index).text(val);
		if(type=="Serial")
			$("#lblhidSerialNumber"+index).text(val);
		if(type=="Material")
			$("#lblmaterialCode"+index).text(val);
		if(type=="Existing")
			$("#lblavailableQuantity"+index).text(val);
	
	}
	
	/*Back Button Change Defect 253*/
	function backFromRecordValidation(){
		var url = document.getElementById('backFromRecordValidation').href;
		document.getElementById('recordValidationForm').action = url;
		document.getElementById('recordValidationForm').submit();
		$("div.loading-overlay").show();
	}
	/*Back Button Change Defect 253*/
	
	