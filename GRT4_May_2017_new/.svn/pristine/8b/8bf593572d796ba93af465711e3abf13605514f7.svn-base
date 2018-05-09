/* validatestring.js 63.1.2.1 of 02/15/10 */
/* @(#)validatestring.js	63.1.2.1 */
// JScript source code

function validateExistence( textobject, name ) {
	// This function returns true is the specified object contain
	// more than an empty string
	if ( textobject.value == "" ) {
        alert( 'Error: \nPlease enter a token for ' + name + '.' );
        return false;
	} else {
			return true;
	}
}

function validateExistenceWithSize( textobject, name, limit ) {
	if ( !validateExistence( textobject, name ) ) {
		return false;
	}
	if ( textobject.value.length > limit ) {
		alert( 'Error: \nPlease insert a valid ' + name + ' (upto ' + limit + ' chars).' );
		return false;
	}
	return true;
}

function validateExistenceWithForcedSize( textobject, name, limit ) {
	if ( !validateExistence( textobject, name ) ) {
		return false;
	}
	if ( textobject.value.length != limit ) {
		alert( 'Error: \nPlease insert a valid ' + name + ' (must be ' + limit + ' characters).' );
		return false;
	}
	return true;
}
