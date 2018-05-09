/**
 * webChatSocket.js
 *
 * Copyright 2015 Avaya Inc. All Rights Reserved.
 *
 * Usage of this source is bound to the terms described in
 * licences/License.txt
 *
 * Avaya - Confidential & Proprietary. Use pursuant to your signed agreement or
 * Avaya Policy
 */

/*
 * \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
 *
 * The following section defines the WebSocket and it's interactions
 *
 * \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
 */

var chatSocket = {

    // referencing the following variables as shorthand
    response : chatConfig.writeResponseClassResponse,
    sent : chatConfig.writeResponseClassSent,
    retries : chatConfig.totalNumberOfRetries,
    maxRetries : chatConfig.maxNumberOfRetries,

    base : AvayaClientServices.Base,

    /**
     * Open the socket.
     */
    openSocket : function() {
        'use strict';
        webChat.participants = avayaGlobal.getEl('participants');
        avayaGlobal.log.info('Opening the WebSocket');

        // Ensures only one connection is open at a time
        if (webSocket !== undefined && (webSocket.readyState !== WebSocket.CLOSED)) {
            avayaGlobal.log.warn('WebSocket is already opened');
            return;
        }

        clearTimeout(chatConfig.reconnectionTimeout);

        // Create a new instance of the WebSocket using the specified url
        webSocket = new WebSocket(links.webChatUrl);
        //attach event handlers
        webSocket.onopen = chatSocket.handleOpen;
        webSocket.onmessage = chatSocket.handleMessage;
        webSocket.onerror = chatSocket.handleError;
        webSocket.onclose = function(event) {

            avayaGlobal.log.debug("Websocket closed with code " + event.code);

            // disable the controls upon close
            webChat.disableControls(true);

            // If it is an expected/graceful close, do not attempt to reconnect.
            // Don't attempt reconnect if we haven't connected successfully before
            if (!chatConfig.previouslyConnected || chatConfig.dontRetryConnection || event.code === 1000 ||
                    event.code === 1005) {
                chatSocket.handleClose(event);
            } else {
                chatSocket.reconnect();
            }
        };

    },

    /**
     * Close the websocket. Disable the controls, clear the users, and let the user know that the chat is over.
     */
    handleClose : function() {
        'use strict';
        avayaGlobal.log.info('Definitely closing the WebSocket.');
     //   webChat.writeResponse('Connection closed, chat has ended', chatSocket.response);
	//	webChat.writeResponse('Connection closed. Thank you for using Oceana GRT chat. Please take some time to fill out the survey', chatSocket.response);      
	    webChat.writeResponse('Connection closed. Thank you for using Oceana GRT chat.', chatSocket.response);       	
		webChat.writeResponse('After closing the window, please take some time to fill out the survey.', chatSocket.response);      
	  webChat.disableControls(true);
        webChat.outMessage.textContent = '';
        webChat.clearAllTimeouts();
        webChat.updateUsers();
        
        //Open survey url on chat close. Phase 1
        /*window.open("http://survey.webengage.com/ws/~177p0a0", "_blank");*/
        /*setTimeout(function() {
        	window.open("http://survey.webengage.com/ws/~177p0a0","GRT Chat WebEngage survey", 
        			"toolbar=0,scrollbars=1,directory=0,location=0,statusbar=0,menubar=0,resizable=1,width=835,height=598,left = 10,top = 10");
        	
        }, 2000);*/
    },

    /**
     * Open the WebSocket. The user's controls will be disabled if there are no agents,
     * and the ping timer will start.
     * @param event
     */
    handleOpen : function(event) {
        'use strict';
        avayaGlobal.log.debug(event);

        // if there are agents in the chat, enable the controls
        if (Object.keys(webChat.users).length <= 0) {
            avayaGlobal.log.debug('No users in room, disabling controls');
            webChat.disableControls(true);
        } else {
            avayaGlobal.log.debug('Agents already in chat, enabling controls');
            webChat.disableControls(false);
        }

        // set up the ping mechanism here.
        webChat.pingInterval = setInterval(function() {
            webChat.sendPing();
        }, chatConfig.pingTimer);
        webChat.timeouts.push(webChat.pingInterval);
        webChat.chatLogin(webChat.g_user, webChat.g_email, webChat.g_account, webChat.g_phone);
    },

    handleMessage : function(event) {
        'use strict';
        var msg = JSON.parse(event.data), body = msg.body;

        // Handle the message according to the type and method.
        // Notifications are in their own method to reduce complexity.
        if (msg.type === chatConfig.messageTypeNotification) {
            webChat.handleNotification(msg);
        } else if (msg.type === chatConfig.messageTypeError) {
            webChat.writeResponse('An error occurred ' + body.code + ' (' + body.errorMessage + ')',
                    chatSocket.response);
        } else if (msg.type === chatConfig.messageTypeAck) {
            // Nothing to do for acks
        } else if (msg.type === chatConfig.messageTypeNewChatAck) {
            // if a newChatAcknowledgement has been received,
            // then use it to initialise the guid, webOnHold variables, etc.
            webChat.guid = body.guid;
            // enable the controls and let the user know that their request
            // has been approved
            webChat.disableControls(false);
            chatSocket.resetConnectionAttempts();
            webChat.writeResponse('Chat request approved', chatSocket.response);
        } else {
            throw new TypeError('Unknown message type:\n' + msg);
        }
        
        //Phase 2 : if there are agents in the chat, enable co-browse links
        if (Object.keys(webChat.users).length <= 0 || body.agentId === 'AvayaAutomatedResource') {
        	$("#showCoBrowseLink").hide();
        } else {
        	//show co-browse if co-browse is enabled for current URL.
            if($("#showCoBrowseLink").attr("enable-cobrowse") != undefined && $("#showCoBrowseLink").attr("enable-cobrowse") == "true") {
            	$("#showCoBrowseLink").show();
            	$("#showCoBrowse").show();
            }
        }
    },

    /**
     * Log errors to the console and alert the user that an error has occurred.
     * @param event
     */
    handleError : function(event) {
        'use strict';
        avayaGlobal.log.error("WebSocket error", event);
        webChat.writeResponse('A connection error has occurred', chatSocket.response);
    },

    /**
     * Attempt to reconnect the webSocket.
     */
    reconnect : function() {
        'use strict';
        if (webSocket.readyState !== WebSocket.OPEN) {
            chatConfig.reconnectionTimeout = setTimeout(function() {
                if (chatSocket.retries <= chatSocket.maxRetries) {
                    webChat.writeResponse('Attempting to reconnect...', chatSocket.response);
                    clearTimeout(chatConfig.reconnectionTimeout);
                    chatSocket.retries++;
                    chatSocket.openSocket();
                } else {
                    webChat.writeResponse('Unable to reconnect, chat has ended', chatSocket.response);
                }
            }, chatConfig.retryInterval);
        }
    },

    /**
     * Reset the number of connection attempts after a successful login.
     */
    resetConnectionAttempts : function() {
        'use strict';
        chatConfig.totalNumberOfRetries = 0;
        clearTimeout(chatConfig.reconnectionTimeout);
    },

    /**
     * Reset the WebSocket.
     */
    resetWebSocket : function() {
        'use strict';
        webChat.initCalled = false;
        chatConfig.previouslyConnected = false;
        chatConfig.totalNumberOfRetries = 0;
        webSocket = undefined;
    }, 

    /**
     * Send a message over the WebSocket
     * @param outMsg as a JSON object
     */
    sendMessage : function(outMsg) {
        'use strict';
        if (webSocket != undefined && webSocket !== null) {
            webSocket.send(JSON.stringify(outMsg));
        }
    }
};
