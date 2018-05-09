/**
 * @license coBrowse.js<br>
 * Copyright 2015 Avaya Inc. All Rights Reserved.<br>
 * <br>
 * Usage of this source is bound to the terms described in licences/License.txt<br>
 * <br>
 * Avaya - Confidential & Proprietary. <br>
 * Use pursuant to your signed agreement or Avaya Policy
 */

var coBrowseUI = {

    // Store the listeners interested in UI events.
    listeners : [],

    targetDocument : null,
    targetDiv : null,

    isIframed : false,

    // UI element IDs.
    proactiveJoinDialogId : '#proactiveJoinDialog',
    proactiveJoinTextId : 'proactiveJoinText',
    proactiveJoinNameInputId : 'proactiveJoinNameInput',
    proactiveJoinSessionKeyInputId : 'proactiveJoinSessionKeyInput',

    requestControlDialogId : '#requestControlDialog',
    requestControlTextId : 'requestControlText',

    coBrowseDialogId : '#coBrowseDialog',
    coBrowseTextId : 'coBrowseText',

    errorDialogId : '#errorDialog',
    errorTextId : 'errorText',

    joinButtonId : 'join-button',
    pauseButtonId : 'pause-button',
    stopButtonId : 'stop-button',
    closeButtonId : 'close-button',
    grantButtonId : 'grant-button',
    denyButtonId : 'deny-button',
    revokeButtonId : 'revoke-button',

    statusImageId : '#statusImage',
    statusImage : '<img src="/grt/images/live-chat/cobrowse/green.png" id="statusImage" class="status-image" />',
    redStatus : '/grt/images/live-chat/cobrowse/red.png',
    greenStatus : '/grt/images/live-chat/cobrowse/green.png',
    orangeStatus : '/grt/images/live-chat/cobrowse/orange.png',

    coBrowseIframeWrapper : '.AvayaCoBrowseWrapper',
    coBrowseIframeId : '#cobrowse',

    showCoBrowseLinkDivId : '#showCoBrowse',

    setup : function() {
        'use strict';
        
        console.log('setup');

        // Get the top most document.
        try {
            coBrowseUI.targetDocument = window.top.document;
            coBrowseUI.isIframed = window.top !== window.self;
        } catch (e) { // May be thrown in an iFrame on some browsers if in an iFrame.
            coBrowseUI.targetDocument = parent.document; // Assumes iFrame is one level deep.
            coBrowseUI.isIframed = true;
        }

        // Work around for issue with jQuery getting unloaded from the 
        // iFrame and leaving broken dialogs.
        window.addEventListener('unload', coBrowseUI.closeHangingDialogs);

        coBrowseUI.targetDiv = coBrowseUI.targetDocument.body;
        
        // Check for the required DIVs on each page, if they exist already tear
        // them down and recreate them to ensure we have a clean state.
        try {
            console.log('Creating required dialogs.');
            
            //Chat Phase 2  Commenting to avoid proactive dialog
            var element = $(coBrowseUI.proactiveJoinDialogId, coBrowseUI.targetDocument).length;
            //Phase 2 : Commenting to disable the client initiated co-browse dialog box
                    /*if (element === 0) {
                coBrowseUI.addProactiveJoinDialog();
            }*/
            
            element = $(coBrowseUI.requestControlDialogId, coBrowseUI.targetDocument).length;
            if (element === 1) {
                // Already exists, tear it down and recreate.
                $(coBrowseUI.requestControlDialogId, coBrowseUI.targetDocument).remove();
            }
            coBrowseUI.addRequestControlDialog();
            
            element = $(coBrowseUI.coBrowseDialogId, coBrowseUI.targetDocument).length;
            if (element === 1) {
                // Already exists, tear it down and recreate.
                $(coBrowseUI.coBrowseDialogId, coBrowseUI.targetDocument).remove();
            }
            coBrowseUI.addCoBrowseDialog();
            
            element = $(coBrowseUI.errorDialogId, coBrowseUI.targetDocument).length;
            if (element === 1) {
                // Already exists, tear it down and recreate.
                $(coBrowseUI.errorDialogId, coBrowseUI.targetDocument).remove();
            }
            coBrowseUI.addErrorDialog();
            
            console.log('Finished creating required dialogs');
        } catch (err) {
            console.error('Could not create the required dialogs.');
            console.error(err);
        }
    },

    addListener : function(listener) {
      'use strict';

      if (listener) {
          coBrowseUI.listeners.push(listener);
      }
    },

    addProactiveJoinDialog : function() {
        'use strict';

        var proactiveJoinDialog = coBrowseUI.targetDocument.createElement('div');
        proactiveJoinDialog.id = coBrowseUI.proactiveJoinDialogId.substring(1);

        var proactiveJoinText = coBrowseUI.targetDocument.createElement('div');
        proactiveJoinText.id = coBrowseUI.proactiveJoinTextId;

        proactiveJoinDialog.appendChild(proactiveJoinText);
        coBrowseUI.targetDiv.appendChild(proactiveJoinDialog);
    },

    addRequestControlDialog : function() {
        'use strict';

        var requestControlDialog = coBrowseUI.targetDocument.createElement('div');
        requestControlDialog.id = coBrowseUI.requestControlDialogId.substring(1);

        var requestControlText = coBrowseUI.targetDocument.createElement('div');
        requestControlText.id = coBrowseUI.requestControlTextId;

        requestControlDialog.appendChild(requestControlText);
        coBrowseUI.targetDiv.appendChild(requestControlDialog);
    },

    addCoBrowseDialog : function() {
        'use strict';

        var coBrowseDialog = coBrowseUI.targetDocument.createElement('div');
        coBrowseDialog.id = coBrowseUI.coBrowseDialogId.substring(1);

        var coBrowseText = coBrowseUI.targetDocument.createElement('div');
        coBrowseText.id = coBrowseUI.coBrowseTextId;

        coBrowseDialog.appendChild(coBrowseText);
        coBrowseUI.targetDiv.appendChild(coBrowseDialog);
    },

    addErrorDialog : function() {
        'use strict';

        var errorDialog = coBrowseUI.targetDocument.createElement('div');
        errorDialog.id = coBrowseUI.errorDialogId.substring(1);

        var errorText = coBrowseUI.targetDocument.createElement('div');
        errorText.id = coBrowseUI.coBrowseTextId;

        errorDialog.appendChild(errorText);
        coBrowseUI.targetDiv.appendChild(errorDialog);
    },

    cleanUp : function() {
        'use strict';
        
        console.log('cleanUp');

        if (coBrowseUI.checkDialogOpen(coBrowseUI.requestControlDialogId) === true) {
            coBrowseUI.destroyDialog(coBrowseUI.requestControlDialogId);
        }

        if (coBrowseUI.checkDialogOpen(coBrowseUI.coBrowseDialogId) === true) {
            coBrowseUI.destroyDialog(coBrowseUI.coBrowseDialogId);
        }

        //Phase 2 : Commenting to disable the client initiated co-browse dialog box
        /*if (!coBrowseUI.isIframed) {
         coBrowseUI.createProactiveJoinDialog(true);
         }*/
        
        // Remove the dialog DIVs.
        try {
            console.log('Removing dialogs to ensure clean state.');
            
            var element = $(coBrowseUI.requestControlDialogId, coBrowseUI.targetDocument).length;
            if (element === 1) {
                $(coBrowseUI.requestControlDialogId, coBrowseUI.targetDocument).remove();
            }
            element = $(coBrowseUI.coBrowseDialogId, coBrowseUI.targetDocument).length;
            if (element === 1) {
                $(coBrowseUI.coBrowseDialogId, coBrowseUI.targetDocument).remove();
            }
            element = $(coBrowseUI.errorDialogId, coBrowseUI.targetDocument).length;
            if (element === 1) {
                $(coBrowseUI.errorDialogId, coBrowseUI.targetDocument).remove();
            }
        } catch (err) {
            console.error('Could not remove dialog DIVs!');
            console.error(err);
        }

        coBrowseUI.fireCleanUp();
    },

    // Event triggers
    fireCleanUp : function() {
        'use strict';
        
        console.log('fireCleanUp');

        coBrowseUI.listeners.forEach(function(listener) {
            if (listener.onCoBrowseUICleanUp !== undefined) {
                listener.onCoBrowseUICleanUp(coBrowseUI);
            }
        });
    },

    fireJoinRequest : function() {
        'use strict';
        
        console.log('fireJoinRequest');

        var name = $('#' + coBrowseUI.proactiveJoinNameInputId, coBrowseUI.targetDocument).val();
        var coBrowseKey = $('#' + coBrowseUI.proactiveJoinSessionKeyInputId, coBrowseUI.targetDocument).val();

        // Basic check of input.
        if (!name) {
            coBrowseUI.createInfoDialog('Invalid Input', 'Please enter a name to join the Co-Browse session with.', coBrowseUI.closeInfoDialog, true);
            return;
        }
        if (!coBrowseKey) {
            coBrowseUI.createInfoDialog('Invalid Input', 'Please enter a Co-Browse Session Key.', coBrowseUI.closeInfoDialog, true);
            return;
        } else {
            // Get rid of any white spaces to make matching easier.
            coBrowseKey = coBrowseKey.replace(/\s+/g, '');

            var regex = /\b\d{8}\b/;
            var result = coBrowseKey.match(regex);

            if(!result) {
                coBrowseUI.createInfoDialog('Invalid Input', 'Please enter a valid Co-Browse Session Key (e.g. "XXXX XXXX").' , coBrowseUI.closeInfoDialog, true);
                return;
            }

            if (coBrowseKey[4] !== ' ') {
                // They forgot the space in the key.
                coBrowseKey = [coBrowseKey.slice(0, 4), ' ', coBrowseKey.slice(4)].join('');
            }
        }

        coBrowseUI.listeners.forEach(function(listener) {
            if (listener.onCoBrowseUIJoinRequest !== undefined) {
                listener.onCoBrowseUIJoinRequest(coBrowseUI, name, coBrowseKey);
            }
        });
    },

    fireCoBrowseUIJoinFailure : function() {
        'use strict';
        
        console.log('fireCoBrowseUIJoinFailure');

        coBrowseUI.closeDialog(coBrowseUI.errorDialogId);

        coBrowseUI.listeners.forEach(function(listener) {
            if (listener.onCoBrowseUIJoinFailure !== undefined) {
                listener.onCoBrowseUIJoinFailure(coBrowseUI);
            }
        });
    },

    // Subscribed Co-Browse events.
    onCoBrowseReady : function(source) {
      'use strict';
      
      console.log('onCoBrowseReady');
    },

    onCoBrowseConnection : function(source) {
        'use strict';

        console.log('onCoBrowseConnection');

        var data = source.coBrowseInstance.getCustomerData();
        if (data.cobrowseState !== undefined) {
            // Close all possible open dialogs.
            coBrowseUI.closeDialog(coBrowseUI.errorDialogId);
            coBrowseUI.closeDialog(coBrowseUI.coBrowseDialogId);
            coBrowseUI.closeDialog(coBrowseUI.requestControlDialogId);

            // Rebuild the user interface between pages.
            if (data.agentControlState === "pending") {
                coBrowseUI.createRequestControlDialog(true);
            } else if (data.agentControlState === "true") {
                coBrowseUI.createRevokeControlDialog(true);
            } else if (data.cobrowseState === "Paused") {
                coBrowseUI.onCoBrowsePauseSuccess(source);
                coBrowseUI.openDialog(coBrowseUI.coBrowseDialogId);
            } else { // Default state.
                coBrowseUI.openDialog(coBrowseUI.coBrowseDialogId);
            }
        }
   },
   
   onCoBrowseConnectionFailure : function(source, error) {
        'use strict';

        console.log('onCoBrowseConnectionFailure');

        coBrowseUI.closeInfoDialog(); // Close it if already open, check performed in closeInfoDialog.

        // Let the user know of the failure.
        coBrowseUI.createInfoDialog('Co-Browse Connection', 'Interrupted, attempting reconnect.', coBrowseUI.closeInfoDialog, true);
   },

    onCoBrowseSessionClose : function(source, title, text) {
        'use strict';
        
        console.log('onCoBrowseSessionClose');
		
        //Phase 2 : Fix for removing hanging dialog when co-browse session closed by agent.
        coBrowseUI.closeHangingDialogs();
		
        coBrowseUI.closeDialog(coBrowseUI.coBrowseDialogId);
        coBrowseUI.createCloseDialog(title, text, true);
    },

    onCoBrowseControlRequest : function(source) {
        'use strict';
        
        console.log('onCoBrowseControlRequest');

        coBrowseUI.closeDialog(coBrowseUI.coBrowseDialogId);
        coBrowseUI.createRequestControlDialog(true);
    },

    onCoBrowseControlRelease : function(source) {
        'use strict';
        
        console.log('onCoBrowseControlRelease');

        coBrowseUI.closeDialog(coBrowseUI.requestControlDialogId);
        coBrowseUI.openDialog(coBrowseUI.coBrowseDialogId);
    },

    onCoBrowseJoinSuccess : function(source) {
        'use strict';
        
        console.log('onCoBrowseJoinSuccess');

        if (!coBrowseUI.isIframed) {
            coBrowseUI.closeDialog(coBrowseUI.proactiveJoinDialogId);
        }

        coBrowseUI.createPauseStopDialog(true);
    },

    onCoBrowseJoinFailure : function(source, error) {
        'use strict';
        
        console.log('onCoBrowseJoinFailure');

        coBrowseUI.createInfoDialog('Join Failed', error + '.', coBrowseUI.fireCoBrowseUIJoinFailure, true);
    },

    onCoBrowseGrantControlSuccess : function(source) {
        'use strict';
        
        console.log('onCoBrowseGrantControlSuccess');

        coBrowseUI.closeDialog(coBrowseUI.requestControlDialogId);
        coBrowseUI.createRevokeControlDialog(true);
    },

    onCoBrowseGrantControlFailure : function(source, error) {
        'use strict';
        
        console.log('onCoBrowseGrantControlFailure');

        coBrowseUI.createInfoDialog('Grant Control Failed', error + '.', coBrowseUI.closeInfoDialog, true);
    },

    onCoBrowseDenyControlSuccess : function(source) {
        'use strict';
        
        console.log('onCoBrowseDenyControlSuccess');

        coBrowseUI.closeDialog(coBrowseUI.requestControlDialogId);
        coBrowseUI.openDialog(coBrowseUI.coBrowseDialogId);
    },

    onCoBrowseDenyControlFailure : function(source, error) {
        'use strict';
        
        console.log('onCoBrowseDenyControlFailure');

        coBrowseUI.createInfoDialog('Deny Control Failed', error + '.', coBrowseUI.closeInfoDialog, true);
    },

    onCoBrowseRevokeControlSuccess : function(source) {
        'use strict';
        
        console.log('onCoBrowseRevokeControlSuccess');

        coBrowseUI.closeDialog(coBrowseUI.requestControlDialogId);
        coBrowseUI.openDialog(coBrowseUI.coBrowseDialogId);
    },

    onCoBrowseRevokeControlFailure : function(source, error) {
        'use strict';
        
        console.log('onCoBrowseRevokeControlFailure');

        coBrowseUI.createInfoDialog('Revoke Control Failed', error + '.', coBrowseUI.closeInfoDialog, true);
    },

    onCoBrowsePauseSuccess : function(source) {
        'use strict';
        
        console.log('onCoBrowsePauseSuccess');

      $('#' + coBrowseUI.pauseButtonId, coBrowseUI.targetDocument).button('option', 'label', 'Resume');
      $(coBrowseUI.statusImageId, coBrowseUI.targetDocument).attr('src', coBrowseUI.orangeStatus);
    },

    onCoBrowsePauseFailure : function(source, error) {
        'use strict';
        
        console.log('onCoBrowsePauseFailure');

        coBrowseUI.createInfoDialog('Pause Failed', error + '.', coBrowseUI.closeInfoDialog, true);
    },

    onCoBrowseResumeSuccess : function(source) {
        'use strict';
        
        console.log('onCoBrowseResumeSuccess');

      $('#' + coBrowseUI.pauseButtonId, coBrowseUI.targetDocument).button('option', 'label', 'Pause');
      $(coBrowseUI.statusImageId, coBrowseUI.targetDocument).attr('src', coBrowseUI.greenStatus);
    },

    onCoBrowseResumeFailure : function(source, error) {
        'use strict';
        
        console.log('onCoBrowseResumeFailure');

        coBrowseUI.createInfoDialog('Resume Failed', error + '.', coBrowseUI.closeInfoDialog, true);
    },

    onCoBrowseStopSuccess : function(source) {
        'use strict';
        
        console.log('onCoBrowseStopSuccess');

        coBrowseUI.cleanUp();
    },

    onCoBrowseStopFailure : function(source, error) {
        'use strict';
        
        console.log('onCoBrowseStopFailure');

        coBrowseUI.createInfoDialog('Stop Failed', error + '.', coBrowseUI.closeInfoDialog, true);
    },

    // Dialog functions.
    closeInfoDialog : function() {
        'use strict';

        coBrowseUI.closeDialog(coBrowseUI.errorDialogId);
    },

    createInfoDialog : function(title, text, callback, autoOpen) {
        'use strict';
        
        if ($(coBrowseUI.errorDialogId, coBrowseUI.targetDocument).length === 0) {
            coBrowseUI.addErrorDialog();
        }

        var isModal = true;
        var buttons = [];
        buttons.push(coBrowseUI.createButton(coBrowseUI.closeButtonId, 'Close', callback.bind(coBrowseUI)));

        coBrowseUI.showDialog(coBrowseUI.errorDialogId, text, title, isModal, buttons, coBrowseUI.errorDialogId, null, null, false, autoOpen);
    },

    createProactiveJoinDialog : function(autoOpen) {
        'use strict';

        var text = '';
        var title = 'Join Co-browse';
        var isModal = false;
        var buttons = [];
        buttons.push(coBrowseUI.createButton(coBrowseUI.joinButtonId, 'Join', coBrowseUI.fireJoinRequest.bind(coBrowseUI)));

        var proactiveJoinNameInput = coBrowseUI.targetDocument.createElement('input');
        proactiveJoinNameInput.id = coBrowseUI.proactiveJoinNameInputId;
        proactiveJoinNameInput.type = 'text';
        proactiveJoinNameInput.size = 20;
        proactiveJoinNameInput.placeholder = 'Your Name';
        proactiveJoinNameInput.className = 'join-field';

        var proactiveJoinSessionKeyInput = coBrowseUI.targetDocument.createElement('input');
        proactiveJoinSessionKeyInput.id = coBrowseUI.proactiveJoinSessionKeyInputId;
        proactiveJoinSessionKeyInput.type = 'text';
        proactiveJoinSessionKeyInput.size = 20;
        proactiveJoinSessionKeyInput.placeholder = 'SESSION KEY';
        proactiveJoinSessionKeyInput.className = 'join-field';

        var children = [proactiveJoinNameInput, proactiveJoinSessionKeyInput];

        coBrowseUI.showDialog(coBrowseUI.proactiveJoinDialogId, text, title, isModal, buttons, coBrowseUI.proactiveJoinDialogId, null, children, true, autoOpen);
    },

    createPauseStopDialog: function (autoOpen) {
        'use strict';
        
        if ($(coBrowseUI.coBrowseDialogId, coBrowseUI.targetDocument).length === 0) {
            coBrowseUI.addCoBrowseDialog();
        }

        var text = 'Co-Browsing is in progress.';
        var title = 'Connected to Co-Browse';
        var isModal = false;
        var buttons = [];
        buttons.push(coBrowseUI.createButton(coBrowseUI.pauseButtonId, 'Pause', coBrowse.togglePause.bind(coBrowse)));
        buttons.push(coBrowseUI.createButton(coBrowseUI.stopButtonId, 'Stop', coBrowse.stopSession.bind(coBrowse)));

        coBrowseUI.showDialog(coBrowseUI.coBrowseDialogId, text, title, isModal, buttons, coBrowseUI.coBrowseDialogId, coBrowseUI.statusImage, null, false, autoOpen);
    },

	createCloseDialog : function(title, text, autoOpen) {
		'use strict';
                
                if ($(coBrowseUI.coBrowseDialogId, coBrowseUI.targetDocument).length === 0) {
                    coBrowseUI.addCoBrowseDialog();
                }

		var isModal = true;
		var buttons = [];
		buttons.push(coBrowseUI.createButton(coBrowseUI.closeButtonId, 'Close', coBrowseUI.cleanUp.bind(coBrowseUI)));

		coBrowseUI.showDialog(coBrowseUI.coBrowseDialogId, text, title, isModal, buttons, coBrowseUI.coBrowseDialogId, coBrowseUI.statusImage, null, false, autoOpen);
	},

	createRequestControlDialog : function(autoOpen) {
		'use strict';
                
                if ($(coBrowseUI.requestControlDialogId, coBrowseUI.targetDocument).length === 0) {
                    coBrowseUI.addRequestControlDialog();
                }

		var text = 'The Agent would like to control your web page.';
		var title = 'Co-Browse Control Request';
		var isModal = true;
		var buttons = [];
		buttons.push(coBrowseUI.createButton(coBrowseUI.grantButtonId, 'Grant', coBrowse.grantControl.bind(coBrowse)));
		buttons.push(coBrowseUI.createButton(coBrowseUI.denyButtonId, 'Deny', coBrowse.denyControl.bind(coBrowse)));

		coBrowseUI.showDialog(coBrowseUI.requestControlDialogId, text, title, isModal, buttons, coBrowseUI.requestControlDialogId, null, null, false, autoOpen);
	},

	createRevokeControlDialog : function(autoOpen) {
		'use strict';

                if ($(coBrowseUI.requestControlDialogId, coBrowseUI.targetDocument).length === 0) {
                    coBrowseUI.addRequestControlDialog();
                }

		var text = 'The agent is currently in control of the co-browse.';
		var title = 'Co-Browse Control';
		var isModal = false;
		var buttons = [];
		buttons.push(coBrowseUI.createButton(coBrowseUI.revokeButtonId, 'Revoke', coBrowse.revokeControl.bind(coBrowse)));

		coBrowseUI.showDialog(coBrowseUI.requestControlDialogId, text, title, isModal, buttons, coBrowseUI.requestControlDialogId, null, null, false, autoOpen);
	},

	showDialog : function(dialog, text, title, isModal, buttons, hiddenId, statusImage, children, isCloseable, autoOpen) {
		'use strict';
                
                var activeElement = coBrowseUI.targetDocument.activeElement;

		var width = '250px';
		if (dialog === coBrowseUI.proactiveJoinDialogId) {
		    width = 'auto';
		}

		 $(dialog, coBrowseUI.targetDocument).text(text);
		 $(dialog, coBrowseUI.targetDocument).dialog({
			 autoOpen : autoOpen,
		     width: width,
		     resizable: false,
		     dialogClass: 'cbe-container',
		     title: title,
			 modal: false,
			 closeOnEscape: false,
			 create: function () {
			     if(!isCloseable) {
			         $(this, coBrowseUI.targetDocument).dialog('widget').find('.ui-dialog-titlebar-close').remove();
			     }

                 if (statusImage) {
                     $(this, coBrowseUI.targetDocument).parent().children(".ui-dialog-titlebar").prepend(statusImage);
                 }

				 //$(this, coBrowseUI.targetDocument).dialog('widget').attr('id', hiddenId);
			 },
			 open: function() {
                             $(this).blur();
			     $(this, coBrowseUI.targetDocument).dialog('widget').position({ my: 'center', at: 'center', of: window });
                             
                             if (activeElement) {
                                console.log('Resetting focus to: ' + activeElement.id);
                                activeElement.focus();
                            }
			 },
			 buttons: buttons
		 });

		 if (children) {
	        children.forEach(function(child) {
	            $(dialog, coBrowseUI.targetDocument).append(child);
	        });
		 }
	},

	checkDialogOpen : function(dialog) {
	    'use strict';

	    try {
	        return $(dialog, coBrowseUI.targetDocument).hasClass("ui-dialog-content") && $(dialog, window.parent.document).dialog('isOpen') === true;
	    } catch (ex) { // Thrown if the dialog hasn't been initialised for some reason.
                console.log('Dialog is not open: ' + dialog);
	        return false;
	    }
	},

    openDialog: function (dialog) {
        'use strict';

        console.log('Opening dialog: ' + dialog);

        if ($(dialog, coBrowseUI.targetDocument).length === 0) {
            if (dialog === coBrowseUI.coBrowseDialogId) {
                coBrowseUI.addCoBrowseDialog();
                coBrowseUI.createPauseStopDialog(true);
            } else if (dialog === coBrowseUI.requestControlDialogId) {
                coBrowseUI.addRequestControlDialog();
                coBrowseUI.createRequestControlDialog(true);
            }
        } else {
            if (coBrowseUI.checkDialogOpen(dialog)) {
                console.log('Dialog is already open: ' + dialog);
                return; // Already open.
            }

            try {
                $(dialog, coBrowseUI.targetDocument).dialog('open');
            } catch (err) {
                // Looking for jQuery UI issues.
                console.error('Could not open dialog: ' + dialog);
                console.error(err);
            }
        }
    },

    closeDialog: function (dialog) {
        'use strict';

        console.log('Closing dialog: ' + dialog);

        // Completely remove the dialog from the DOM to work around the
        // breaking jQuery dialog issue.
        try {
            $(dialog, coBrowseUI.targetDocument).remove();
        } catch (err) {
            console.warn('Could not remove dialog from the DOM: ' + dialog);
        }
    },

    destroyDialog: function (dialog) {
        'use strict';

        console.log('Destroying dialog: ' + dialog);

        // Completely remove the dialog from the DOM to work around the
        // breaking jQuery dialog issue.
        try {
            $(dialog, coBrowseUI.targetDocument).remove();
        } catch (err) {
            console.warn('Could not remove dialog from the DOM: ' + dialog);
        }
    },

    createButton : function (id, text, click) {
            'use strict';

            return {
                    id: id,
                    text: text,
                    click: click
            };
    },

    /**
     * Show the Co-browse iFrame.
     * @param url
     */
    showCoBrowseIframe : function(url) {
        'use strict';
        $(coBrowseUI.coBrowseIframeWrapper, coBrowseUI.targetDocument).hide();
        $(coBrowseUI.coBrowseIframeId, coBrowseUI.targetDocument).attr('src', url);
        $(coBrowseUI.coBrowseIframeId, coBrowseUI.targetDocument).show();
    },

    /**
     * Hide the Co-Browsing iFrame.
     */
    hideCoBrowseIframe : function() {
        'use strict';
        $(coBrowseUI.coBrowseIframeId, coBrowseUI.targetDocument).hide();
        $(coBrowseUI.coBrowseIframeId, coBrowseUI.targetDocument).attr('src', '');
        $(coBrowseUI.coBrowseIframeWrapper, coBrowseUI.targetDocument).show();
    },

    /**
     * Show the Co-Browse div link for toggling the proactive dialog.
     */
    showCoBrowseLinkDiv : function() {
        'use strict';
        $(coBrowseUI.showCoBrowseLinkDivId, coBrowseUI.targetDocument).show();
    },

    /**
     * Hide the Co-Browse div link for toggling the proactive dialog.
     */
    hideCoBrowseLinkDiv : function() {
        'use strict';
        $(coBrowseUI.showCoBrowseLinkDivId, coBrowseUI.targetDocument).hide();
    }, // OCPROVIDER-2089, don't forget this comma.
	
    // START OCPROVIDER-2089
    closeHangingDialogs: function () {
        'use strict';

        console.log('closeHangingDialogs');

        var dialogList = [
            coBrowseUI.errorDialogId,
            coBrowseUI.coBrowseDialogId, 
            coBrowseUI.requestControlDialogId];

        var size = dialogList.length;

        for (var i = 0; i < size; i++) {
            coBrowseUI.destroyDialog(dialogList[i]);
        }
    },
    // END OCPROVIDER-2089
	
    onCoBrowseRebuild: function (source) {
        'use strict';

        console.log('onCoBrowseRebuild');

        // Reset the state of these dialogs.
        coBrowseUI.createPauseStopDialog(false);

        coBrowseUI
                .createInfoDialog(
                        'Co-Browse Status',
                        'Reconnection in progress.',
                        coBrowseUI.closeInfoDialog, true);
    }
	
};

$(function() {
    'use strict';
    coBrowseUI.setup();
});
