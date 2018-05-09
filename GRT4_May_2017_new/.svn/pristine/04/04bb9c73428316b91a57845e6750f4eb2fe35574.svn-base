/**
 * webChatUI.js<br>
 * Copyright 2015 Avaya Inc. All Rights Reserved. Usage of
 * this source is bound to the terms described in licences/License.txt<br>
 * <br>
 * Avaya - Confidential & Proprietary. Use pursuant to your signed
 * agreement or Avaya Policy.<br>
 * <br>
 * Handles the UI side of the web chat library, which is based around jQuery UI. Replace the following with your own UI code, if you prefer.
 */

var chatUI = {

    /**
     * Spawns an alert window with the specified message
     * @param msg
     */
    alertWindow : function(msg) {
        'use strict';
        $('#alertDialog').dialog();
        $('#alertDialog').text(msg);
    },

    /**
     * Hide the chat panel.
     */
    hideChatPanel : function() {
        'use strict';
        $('#chatPanel').hide();
    },

    /**
     * Show the chat panel.
     */
    showChatPanel : function() {
        'use strict';
        $('#chatPanel').show();
    },

    /**
     * Open the video/voice panel.
     */
    openVideoPanel : function() {
        'use strict';
        $('#videoPanel').dialog();
    },

    /**
     * Change the textContent of the specified element.
     * @param el
     * @param newText
     */
    changeElementText : function(el, newText) {
        'use strict';
        $(el).text(newText);
    },

    /**
     * Changes the chat panel to chat mode.
     */
    changeToChatMode : function() {
        'use strict';
        $('#chatForm').hide();
        $('#chatInterface').show();
    // orig $('#chatInterface').height('333px');
                                $('#chatInterface').height('363px');
      // orig   $('#chatPanel').height('363px');
                  $('#chatPanel').height('423px');

    },

    /**
     * Changes the chat panel to login mode.
     */
    changeToLoginMode : function() {
        'use strict';
        $('#chatInterface').hide();
        $('#chatForm').show();
        $('#chatForm').height('123px');
        $('#chatPanel').height('177px');
    },

    /**
     * Changes the chat panel to video mode. This is still a TODO.
     */
    changeToVideoMode : function() {
        'use strict';
        avayaGlobal.log.error('Video mode not supported yet!');
    },

    /**
     * Create and show an alert
     * @param msg
     */
    showAlert : function(msg) {
        'use strict';
        $('#alertDialog').dialog({
            draggable : false,
            resizeable : false,
            buttons : {
                OK : function() {
                    $(this).dialog('close');
                }
            }
        }).text(msg);
    },

    /**
     * Request confirmation using a jQuery window.
     * @param msg
     */
    requestConfirmation : function(msg) {
        'use strict';
        chatUI.showConfirm(msg).then(function(answer) {
            if (answer) {
                return true;
            }
        });
    },

    /**
     * Performs the
     * @param msg
     * @returns
     */
    showConfirm : function(msg) {
        'use strict';
        var result = $.Deferred();
        $('#alertDialog').dialog({
            draggable : false,
            resizeable : false,
            modal : true,
            buttons : {
                'Yes' : function() {
                    result.resolve(true);
                    $(this).dialog('close');
                },
                'No' : function() {
                    result.resolve(false);
                    $(this).dialog('close');
                }
            }
        }).text(msg);
        return result.promise();
    },

    returnConfirmResult : function(result) {
        'use strict';
        return result;
    }
};

// This runs on page load.
$(function() {
    // Configure the sliding tab for the chat.
    //Phase 2 :: Commenting as chat loads as a popup.
    /*$('.slide-out-div-chat').tabSlideOut({
        tabHandle : '.chatHandle', // CSS class of the tab element
        pathToTabImage : '/grt/images/live-chat/LiveChat2.jpg', // path to the image for the tab
        imageHeight : '111px', // height of tab image
        imageWidth : '25px', // width of tab image (Optionally can be set using css)
        tabLocation : 'right', // can be top, right, bottom, or left
        speed : 300, // speed of animation
        action : 'click', // options: 'click' or 'hover', action to trigger animation
        topPos : '150px', // position from the top (use if tabLocation is left or right)
        leftPos : '0px', // position from left (use if tabLocation is bottom or top)
        fixedPosition : true
    // options: true makes it stick(fixed position) on scroll
    });*/

    $('#liveChatLink').click(function(event) {
        $('#chatDialog').dialog();
        event.preventDefault();
    });

    $('#videoLink').click(function(event) {
        $('#videoPanel').dialog();
        event.preventDefault();
    });

    $('#configLink').click(function(event) {
        $('#configPanel').dialog({
            width : 400,
            'resize' : 'auto'
        });
        event.preventDefault();
    });

    $('#showCoBrowseLink').click(function(event) {
    	//Phase 2 Change 
    	coBrowseUI.setup();
    	
        if(!coBrowseUI.checkDialogOpen(coBrowseUI.proactiveJoinDialogId)) {
            coBrowseUI.createProactiveJoinDialog();
        }
        event.preventDefault();
    });

    // show a tooltip for the configuration panel
    //$('#configLink').tooltip();

   // Set jQuery UI button
   // $('.button').button();
    
    //Chat Phase 1 : On click of live chat button chat starts
    //Phase 2 : Commenting as chat loads as a popup.
    /*$(".chatHandle").click(function(){
        $("#openbutton-chat").click();
    });*/
    
});