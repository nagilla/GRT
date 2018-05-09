/**
 *  CUSTOM PLUGINS FOR DATATABLE
 */

//Numbers with HTML Plugin
jQuery.fn.dataTableExt.aTypes.unshift( function ( sData )
{
    sData = typeof sData.replace == 'function' ?
        sData.replace( /<[\s\S]*?>/g, "" ) : sData;
    sData = $.trim(sData);
 
    var sValidFirstChars = "0123456789-";
    var sValidChars = "0123456789.";
    var Char;
    var bDecimal = false;
 
    /* Check for a valid first char (no period and allow negatives) */
    Char = sData.charAt(0);
    if (sValidFirstChars.indexOf(Char) == -1)
    {
        return null;
    }
 
    /* Check all the other characters are valid */
    for ( var i=1 ; i<sData.length ; i++ )
    {
        Char = sData.charAt(i);
        if (sValidChars.indexOf(Char) == -1)
        {
            return null;
        }
 
        /* Only allowed one decimal place... */
        if ( Char == "." )
        {
            if ( bDecimal )
            {
                return null;
            }
            bDecimal = true;
        }
    }
 
    return 'num-html';
} );
 
/* Create an array with the values of all the input boxes in a column, parsed as numbers */
$.fn.dataTable.ext.order['dom-text-numeric'] = function  ( settings, col )
{	var sortArray = settings.aaSorting[0];
	var orderType  = sortArray[1];
    return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
    	//if( orderType == 'desc' && $('input', td).val()=="") return Number.POSITIVE_INFINITY;
    	if( $('input', td).val()!=undefined ){
    		return $('input', td).val() * 1;
    	}else{
    		return $(td).text() * 1;
    	}
    } );	
};
 
/* Create an array with the values of all the checkboxes in a column */
$.fn.dataTable.ext.order['dom-checkbox'] = function  ( settings, col )
{
    return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
        return $('input', td).prop('checked') ? '1' : '0';
    } );
};