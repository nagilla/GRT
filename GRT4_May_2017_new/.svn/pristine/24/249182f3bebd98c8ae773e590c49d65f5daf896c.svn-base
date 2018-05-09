/**
 * webChat.js Copyright 2015 Avaya Inc. All Rights Reserved. Usage of this
 * source is bound to the terms described in licences/License.txt Avaya -
 * Confidential & Proprietary. Use pursuant to your signed agreement or Avaya
 * Policy
 */

/*
 * \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
 * This section defines the client end of the WebSocket chat.
 * \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
 */

var webChat = {

    // particular elements in the page
    sendButton : null,
    closeButton : null,
    messages : null,
    outMessage : null,
    pagePushDiv : null,

    // video/voice related
    videoPanel : null,
    chatVideo : null,
    device : null,
    clientPlatform : null,
    user : null,
    session : null,

    participants : null,
    users : {},

    guid : null,
    ak : null,
    webOnHoldComfortGroups : null,
    webOnHoldURLs : null,

    webOnHoldInterval : null,
    pingInterval : null,

    // isTyping variable currently not used, Planned for future release
    isTyping : false,
    typeOut : null,
    activeAgentTypeOut : null,
    passiveAgentTypeOut : null,
    supervisorTypeOut : null,
    usersTable : null,
    lastIsTypingSent : 0,
    timeBetweenMsgs : 10000,

    initCalled : false,
    autoOpen : false,

    timeouts : [],

    g_email : '',
    g_user : '',
    g_account : '',
    g_phone : {},
    g_country : '',

    // maximum wait time in minutes
    maxWaitTime : 10,
    chatAvailableMsg : 'The chat is available. Your estimated wait time is {0} minutes. Click the "Live Chat" tab to open the chat',
    chatPossibleMsg : 'The chat is available. Click the "Live Chat" tab to open the chat.',
    chatNotAvailableMsg : 'The chat is not currently available. Please try again later',
    noAgentsAvailableMsg : 'No agents are currently available. Please try again later',

    // priority ranges from 1 (max) to 10 (min)
    priority : 5,

    // syntax for the estimated wait time request. The Channel attribute is hard-coded to Chat.
    ewtRequest : {
        'serviceMap' : {
            '1' : {
                'attributes' : {
                    'Channel' : [ 'Chat' ]
                },
                'priority' : 5
            },
        }
    },

    workflowType : '',
    routePointIdentifier : 'Default',

    // CoBrowse related.
    coBrowseEnabled : true,
    coBrowseReady : false,
    coBrowseIframe : null,
    coBrowseKey : '',
    isPagePushKey : false,
    coBrowseOceanaPath : '/grt/scripts/live-chat/',
    coBrowseSDKPath : '/grt/scripts/live-chat/',
    hideElements : [ 'chatPanel', 'hidePauseStop', 'hideRequestControl' ],
    //Phase 2 : pass chatStartTimestamp to survey url
    chatStartTimestamp : '',

    /**
     * Add a timer to an array for the Web-On-Hold messages.
     * @param array
     */
    addTimerToArray : function(array) {
        'use strict';
        if (array === undefined) {
            return;
        }

        for (var p = 0; p < array.length; p++) {
            var obj = array[p];
            obj.lastCalled = new Date().valueOf();
            obj.currentSequence = 0;
        }
    },

    /**
     * Append a specified link to the chat transcript.
     * @param url
     * @param urlDestination
     * @param openAutomatically -
     *                false for Web-On-Hold URLs, but URLs from an agent may be
     *                changed
     *                @param parentElement - the parent element
     */
    appendLink : function(url, urlDestination, openAutomatically, parentElement) {
        'use strict';
        var link = document.createElement('a');
        link.href = url;
        link.text = url;
        link.target = ('_' + urlDestination);
        parentElement.appendChild(link);

        // Scroll to bottom of the messages div to show new messages
        webChat.messages.scrollTop = webChat.messages.scrollHeight;

        if (openAutomatically) {
            window.open(link);
        }
    },

    /**
     * Log the user into the chat.
     * @param user
     * @param email
     * @param account
     * @param skillset
     * @param phone
     */
    chatLogin : function(user, email, subject, phone) {
        'use strict';
        var wantsEmail = avayaGlobal.getEl('transcript-chat').checked;
        var custAddress = avayaGlobal.getEl('address-chat');
        var channelAttribute = avayaGlobal.getEl('contactType').value;
        avayaGlobal.log.debug('Channel attribute is ' + channelAttribute);
        avayaGlobal.log.debug('Chat attributes are ' + JSON.stringify(chatLogon.attributes));

        // if the user didn't specify a valid email address, they can't
        // receive a transcript.
        if (avayaGlobal.isStringEmpty(email)) {
            wantsEmail = false;
        }
        var calledParty = window.location.href;
        var msg;
        if (!chatConfig.previouslyConnected) {
            msg = {
                'apiVersion' : '1.0',
                'type' : 'request',
                'body' : {
                    'method' : 'requestChat',
                    'deviceType' : navigator.userAgent,
                    'routePointIdentifer' : webChat.routePointIdentifier,
                    'workFlowType' : webChat.workflowType,
                    'requestTranscript' : wantsEmail,
                    'workRequestId' : avayaGlobal.getSessionStorage('contextId'),
                    'calledParty' : calledParty,
                    'intrinsics' : {
                        'channelAttribute' : channelAttribute,
                        'attributes' : chatLogon.attributes,
                        'email' : email,
                        'name' : user,
                        'country' : phone.country,
                        'area' : phone.area,
                        'phoneNumber' : phone.phone,
                        'customFields' : [ {
                            'title' : 'address',
                            'value' : custAddress.value
                        } ]
                    }
                }
            };
            webChat.writeResponse('Sending Login Details', chatConfig.writeResponseClassSent);
        } else {
            msg = {
                'apiVersion' : '1.0',
                'type' : 'request',
                'body' : {
                    'method' : 'renewChat',
                    'guid' : webChat.guid,
                    'authenticationKey' : webChat.ak,
                }
            };
        }

        chatSocket.sendMessage(msg);

        // if the user doesn't have a name, give them a default one.
        // This is specific to the UI - the server will assign a default label when
        // passing messages to the agent.
        if (webChat.g_user === '') {
            webChat.g_user = chatConfig.defaultUsername;
        }
    },

    /**
     * Clears all timeouts on the page.
     */
    clearAllTimeouts : function() {
        'use strict';
        for (var i = 0; i < webChat.timeouts.length; i++) {
            clearTimeout(webChat.timeouts[i]);
        }
    },

    /**
     * Clears the customer's typing timeout.
     * @param obj
     */
    clearTypingTimeout : function(obj) {
        'use strict';
        if (obj) {
            clearTimeout(obj);
        }
    },

    /**
     * Toggles the controls.
     * @param isDisabled
     */
    disableControls : function(isDisabled) {
        'use strict';
        avayaGlobal.log.debug('Disabling controls: ' + isDisabled);
        webChat.sendButton.disabled = isDisabled;
        webChat.outMessage.disabled = isDisabled;
        if (isDisabled === false) {
            webChat.outMessage.addEventListener('keydown', webChat.onType);
        } else {
            webChat.outMessage.removeEventListener('keydown', webChat.onType);
        }
    },

    /**
     * Handles notification messages.
     * @param message
     */
    handleNotification : function(message) { // NOSONAR: too complex
        // for Sonar, but cannot be reduced further
        'use strict';
        var body = message.body, method = body.method;
        if (method === chatConfig.jsonMethodRequestChat) {
            webChat.notifyRequestChat(body);
        } else if (method === chatConfig.jsonMethodRouteCancel) {
            webChat.notifyRouteCancel();
        } else if (method === chatConfig.jsonMethodRequestNewParticipant) {
            webChat.notifyNewParticipant(body);
        } else if (method === chatConfig.jsonMethodRequestIsTyping) {
            webChat.notifyIsTyping(body);
        } else if (method === chatConfig.jsonMethodRequestNewMessage) {
            webChat.notifyNewMessage(body);
        } else if (method === chatConfig.jsonMethodRequestCloseConversation) {
            webChat.notifyCloseConversation();
        } else if (method === chatConfig.jsonMethodRequestParticipantLeave) {
            webChat.notifyParticipantLeave(body);
        } else if (method === chatConfig.jsonMethodRequestNewPushMessage) {
            webChat.notifyNewPagePushMessage(body);
        } else if (method === chatConfig.jsonMethodRequestNewCoBrowseSessionKeyMessage) {
            //Phase 2 : Commenting the method call to disable the key push feature on the Customer side
        	//webChat.notifyNewCoBrowseSessionKeyMessage(body);
        } else if (method === chatConfig.jsonMethodPing) {
            // do nothing with pings. They just confirm that the
            // WebSocket is open.
        } else {
            throw new TypeError('Received notification with unknown method: '.concat(method));
        }
    },

    /**
     * Initialise the chat.
     */
    initChat : function() {
        'use strict';

        // if the chat has already opened, don't bother opening it
        if (webChat.initCalled) {
            return;
        }

        webChat.pagePushDiv = avayaGlobal.getEl('pagePushDiv');

        webChat.g_user = avayaGlobal.getSessionStorage('user');
        webChat.g_email = avayaGlobal.getSessionStorage('email');
        webChat.g_account = avayaGlobal.getSessionStorage('account');
        var phone = avayaGlobal.getSessionStorage('phone');
        webChat.g_phone = JSON.parse(phone);
        webChat.g_country = avayaGlobal.getSessionStorage('country');
        webChat.g_subject = avayaGlobal.getSessionStorage('subject');

        // this is sometimes undefined at this point.
        if (webChat.outMessage === undefined) {
            webChat.outMessage = avayaGlobal.getEl('outmessage');
        }

        webChat.disableControls(true);
        chatSocket.openSocket();
        webChat.initCalled = true;
    },

    /**
     * Notify the user that the chat is closed.
     * @param body
     */
    notifyCloseConversation : function() {
        'use strict';
        // Server will close the websocket
        avayaGlobal.clearSessionStorage();
        if (chatConfig.resetOnClose) {
            webChat.resetChat();
        }
    },

    /**
    * Reset the chat.
    */
    resetChat : function() {
        'use strict';
        avayaGlobal.log.info('Resetting chat');
        avayaGlobal.detachChildren(webChat.messages);
     	//chatUI.resetChatUI();
        webChat.updateUsers();
        webChat.clearAllTimeouts();
        webChat.ak = null;
        webChat.guid = null;
        webChat.lastIsTypingSent = 0;
       /* window.open("http://survey.webengage.com/ws/~177p0a0","GRT Chat WebEngage survey", 
        			"toolbar=0,scrollbars=1,directory=0,location=0,statusbar=0,menubar=0,resizable=1,width=835,height=598,left = 10,top = 10");*/
        //To track chat session active or not.
		localStorage.setItem("onChat", "false");
        chatSocket.resetWebSocket();
 		window.close();
    },

    /**
     * Notify the user that an agent's typing status has changed.
     * @param body
     */
    notifyIsTyping : function(body) {
        'use strict';
        var isAgentTyping = body.isTyping;

        if (isAgentTyping === true) {
            var agent = webChat.users[body.agentId];
            agent.isTyping = isAgentTyping;
            webChat.updateTypingCell(agent, true);

            var agentTypeOut;
            if (agent.type === 'active_participant') {
                agentTypeOut = chatConfig.activeAgentTypeOut;
            } else if (agent.type === 'passive_participant') {
                agentTypeOut = chatConfig.passiveAgentTypeOut;
            } else {
                agentTypeOut = chatConfig.supervisorTypeOut;
            }

            if (agentTypeOut !== undefined) {
                webChat.clearTypingTimer(agentTypeOut);
            }

            agentTypeOut = setTimeout(function() {
                if (webChat.users !== undefined) {
                    agent.isTyping = false;
                    webChat.updateTypingCell(agent, false);
                }
            }, chatConfig.agentTypingTimeout);
            webChat.timeouts.push(agentTypeOut);
        }
    },

    /**
     * Notify the user of a new chat message.
     * @param body
     */
    notifyNewMessage : function(body) {
        'use strict';
        var date = new Date(body.timestamp);
        var message = chatConfig.newMessageText;
        message = message.replace('{0}', body.displayName);
        message = message.replace('{1}', date.toLocaleTimeString());
        message = message.replace('{2}', body.message);
        webChat.writeResponse(message, chatConfig.writeResponseClassResponse);
    },

    /**
     * Notify the user of a new page push message
     * @param body
     */
    notifyNewPagePushMessage : function(body) {
        'use strict';
        var sentDate = new Date(body.timestamp), url = body.pagePushURL, destination = body.pagePushDestination, name = body.displayName;
        avayaGlobal.log.debug('URL is ' + url);

        // Check for Co-Browse enabled.
        var sessionKey = avayaGlobal.getParameterByName('key', body.pagePushURL);
        var messagePlaceholder;

        if (sessionKey !== null) {
            avayaGlobal.log.debug('Recieved Co-Browse page push request for session ' + sessionKey + '.');
            webChat.coBrowseKey = sessionKey;
            webChat.isPagePushKey = true;
            coBrowseUI.showCoBrowseIframe(url);

            // show the Co-Browsing notification
            messagePlaceholder = chatConfig.coBrowseMessageText;
            messagePlaceholder = messagePlaceholder.replace('{0}', name);
            messagePlaceholder = messagePlaceholder.replace('{1}', sentDate.toLocaleTimeString());
            webChat.writeResponse(messagePlaceholder, chatConfig.writeResponseClassResponse);
            
            //Phase 2 : Minimize chat window on page push
            if( !( $("#chatPanel").hasClass("open") || $("#chatPanel").hasClass("close")) ) {
            	$('.slide-out-div-chat').tabSlideOut({
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
    		    });	
            	//Trigger click event to open live chat slide out window after page push
            	$(".chatHandle").click();
            	//Maximize live chat window after page push.
            	window.moveTo(0, 0);
            	window.resizeTo(screen.width, screen.height);
            } else {
            	//Tab slide-out already active : update css
            	if($("#chatPanel") != null) {
            		$("#chatPanel").css("top", "150px");
            	} else if(window != null && window.parent != null && window.parent.$("#chatPanel") != null) {
            		window.parent.$("#chatPanel").css("top", "150px");
            	}
            	window.parent.window.moveTo(0, 0);
            	window.parent.window.resizeTo(screen.width, screen.height);
            	
            }
            
            
           // $(".chatHandle").hide();
            
        } else {
            // show the Page-Push notification
            messagePlaceholder = chatConfig.pagePushMessageText;
            messagePlaceholder = messagePlaceholder.replace('{0}', name);
            messagePlaceholder = messagePlaceholder.replace('{1}', sentDate.toLocaleTimeString());
            webChat.writeResponse(messagePlaceholder, chatConfig.writeResponseClassResponse);
            webChat.appendLink(url, destination, chatConfig.autoOpen, webChat.messages);
        }
    },

    /**
     * Notify the user of a new co-browse session key message
     * @param body
     */
    notifyNewCoBrowseSessionKeyMessage : function(body) {
        'use strict';

        // TODO: check format of the key.
        var coBrowseKey = body.sessionKey;

        webChat.joinKeyPushCoBrowse(coBrowseKey);
    },

    /**
     * Notify the user that an agent has joined the chat.
     * @param body
     */
    notifyNewParticipant : function(body) {
        'use strict';

        // suppress chatbot notifications
        if (!(body.agentId === 'AvayaAutomatedResource' && chatConfig.suppressChatbotPresence)) {
            webChat.writeResponse('An agent has joined the chat', chatConfig.writeResponseClassResponse);
        }
        var agents = body.participants;
        webChat.updateUsers(agents);
        clearInterval(webChat.webOnHoldInterval);
        webChat.disableControls(false);
    },

    /**
     * Notify the user that an agent has left the chat.
     * @param body
     */
    notifyParticipantLeave : function(body) {
        'use strict';

        // suppress chatbot notifications
        if (!(body.agentId === 'AvayaAutomatedResource' && chatConfig.suppressChatbotPresence)) {
            webChat.writeResponse('Agent has left the chat', chatConfig.writeResponseClassResponse);
        }
        // Server will close the websocket if endChatFlag is true
        if (body.endChatFlag === false) {
            // if there is only one user left (i.e. the customer),
            // play the webOnHold messages
            if (Object.keys(body.participants).length === 0) {
                avayaGlobal.log.trace('Only the customer remains in the room.');
                webChat.disableControls(true);
                webChat.startOnHoldMessages();
                webChat.participants.textString = '';
            }
        }

        var agents = body.participants;
        webChat.updateUsers(agents);
    },

    /**
     * Notifies the user that they have entered the chat.
     * @param body
     */
    notifyRequestChat : function(body) {
        'use strict';

        avayaGlobal.log.debug(body);
        webChat.guid = body.guid;
        webChat.g_user = body.intrinsics.name;
        webChat.ak = body.authenticationKey;

        if (chatSocket.retries > 0) {
            chatSocket.resetConnectionAttempts();
        }

        // Send up the Co-Browse status of this page.
        var coBrowseEnabled = typeof coBrowse !== 'undefined' && webChat.coBrowseEnabled;
        webChat.sendCoBrowseStatus(coBrowseEnabled);

        // if the customer has already been connected, don't play the on
        // hold messages
        if (!chatConfig.previouslyConnected) {
            webChat.writeResponse('Login request received and approved', chatConfig.writeResponseClassResponse);
            chatConfig.previouslyConnected = true;
            webChat.webOnHoldComfortGroups = body.webOnHoldComfortGroups;
            webChat.webOnHoldURLs = body.webOnHoldURLs;
            webChat.addTimerToArray(webChat.webOnHoldComfortGroups);
            webChat.addTimerToArray(webChat.webOnHoldURLs);
            webChat.startOnHoldMessages();
            
            //Chat Pahse 2 :: Store chat start time
            webChat.chatStartTimestamp = new Date().toLocaleString();
            //TODO: placed in here initially, there may be another - better - spot to place the below
            setTimeout(function() {
            grtWelcomeMessage();
            },2000);
            // calling mutationObserver - to react to changes in dom - filtered by incoming classname
            mutationObserverTest();
        } else {
            webChat.writeResponse('Successfully reconnected', chatConfig.writeResponseClassResponse);
        }
    },

    /**
     * Notifies the user that no agent could be found to route the chat to.
     */
    notifyRouteCancel : function() {
        'use strict';
      //  webChat.writeResponse('No suitable agents could be found', chatConfig.writeResponseClassResponse);
	  webChat.writeResponse('Thank you for using Ask Ava for help with  GRT or product registration-related questions. Unfortunately, no live agent is available to assist, but there are still ways to get help.', chatConfig.writeResponseClassResponse);
          openPopupHelper(4);
    },

    /**
     * Notifies the user that a video or voice chat is being opened.
     * @param body
     */
    notifyVideoOrVoice : function(body) {
        'use strict';
        avayaGlobal.log.debug(body);
    },

    /**
     * Called when the customer starts typing.
     * @param event
     */
    onType : function(event) {
        'use strict';
        if (event.keyCode === 13) {
            webChat.sendChatMessage();
        } else {
            webChat.startTypingTimer();
        }
    },

    /**
     * Play the Web-On-Hold messages inside a specific array.
     * @param array
     *                of Web-On-Hold comfort messages or URLs.
     */
    playOnHoldMessage : function(array) {
        'use strict';
        var currentDate = new Date(), currentTime = currentDate.valueOf();
        for (var i = 0; i < array.length; i++) {
            var group = array[i];
            var deltaTime = (currentTime - group.lastCalled) / 1000;
            if (deltaTime >= group.delay || deltaTime >= group.holdTime) {
                var currentMsg;

                // if this has a urls array, it's a WebOnHold URL
                // otherwise, it's a comfort message
                if (group.urls !== undefined) {
                    currentMsg = group.urls[group.currentSequence];
                    webChat.writeResponse('Please visit this link: ', chatConfig.writeResponseClassResponse);
                    webChat.appendLink(currentMsg.url, '_blank', false, webChat.messages);

                } else {
                    currentMsg = group.messages[group.currentSequence];
                    webChat.writeResponse(currentMsg.message, chatConfig.writeResponseClassResponse);
                }
                group.lastCalled = currentTime;
                group.currentSequence++;
                if ((group.numberOfMessages !== undefined && group.currentSequence >= group.numberOfMessages) ||
                        (group.urls !== undefined && group.currentSequence >= group.urls.length)) {
                    group.currentSequence = 0;
                }
            }
        }
    },

    /**
     * Sends a close chat request to the server.
     */
    quitChat : function() {
        'use strict';
        // Prevent reconnect attempts if customer clicks 'Close' while chat is reconnecting
        chatConfig.dontRetryConnection = true;
        if (webSocket !== null && webSocket.readyState === webSocket.OPEN) {
            var closeRequest = {
                'apiVersion' : '1.0',
                'type' : 'request',
                'body' : {
                    'method' : 'closeConversation'
                }
            };
            
            webChat.writeResponse('Close request sent', chatConfig.writeResponseClassSent);
            // 04-10-2017
            sendAutoText("Customer clicked close button");
	    				// START: for chatbot reports - if customer leave while waiting for agent
	    				if (visitor_engaged.customer_escalated) {
	    					if (global_chatbot_token != "notoken" && global_chatbot_token.length > 7){
	    					avayaGlobal.log.info('W_customer_dropped_while_waiting_for_agent set visitor_engaged.clickedCloseBtn = true');
	    					sendToConvDataRepV2('customer_dropped_while_waiting_for_agent', 'yes');
	    					visitor_engaged.clickedCloseBtn = true;
	    					}
	    				}
				// END: for chatbot reports - if customer leave while waiting for agent
            chatSocket.sendMessage(closeRequest);
        }
    },

    /**
     * Sends a chat message to the server. If the message box is empty, nothing
     * is sent.
     */
    sendChatMessage : function() {
        'use strict';
        var text = webChat.outMessage.value;

        if (!avayaGlobal.isStringEmpty(text)) {

            // get the timestamp and add to the transcript message. The server adds the timestamp
            // when it receives the message, so there is no need to send it.
            var timestamp = new Date().toLocaleTimeString();
            var transcriptMessage = chatConfig.newMessageText;
            transcriptMessage = transcriptMessage.replace('{0}', webChat.g_user);
            transcriptMessage = transcriptMessage.replace('{1}', timestamp);
            transcriptMessage = transcriptMessage.replace('{2}', text);
            webChat.writeResponse(transcriptMessage, chatConfig.writeResponseClassSent);

            var message = {
                'apiVersion' : '1.0',
                'type' : 'request',
                'body' : {
                    'method' : 'newMessage',
                    'message' : text
                }
            };
            chatSocket.sendMessage(message);
            webChat.outMessage.value = '';
        }
    },

    /**
     * Lets the agents know that the customer is typing.
     * @param isUserTyping
     */
    sendIsTyping : function(isUserTyping) {
        'use strict';
        var isTypingMessage = {
            'apiVersion' : '1.0',
            'type' : 'request',
            'body' : {
                'method' : 'isTyping',
                'isTyping' : isUserTyping
            }
        };

        // update lastisTypingSent timestamp
        webChat.lastIsTypingSent = Date.now();

        chatSocket.sendMessage(isTypingMessage);
    },

    /**
     * Lets the agent know if the customer's client is enabled for Co-Browse.
     * @param isEnabled
     */
    sendCoBrowseStatus : function(isEnabled) {
        'use strict';
        var coBrowseStatusMessage = {
            'apiVersion' : '1.0',
            'type' : 'request',
            'body' : {
                'method' : 'coBrowseStatus',
                'isEnabled' : isEnabled
            }
        };

        chatSocket.sendMessage(coBrowseStatusMessage);
    },

    /**
     * Sends a page push request to the agents.
     */
    sendPagePushRequest : function() {
        'use strict';

        // get the URL, and then trim whitespace from it
        var url = avayaGlobal.getEl('urlInput').value.trim();
        if (avayaGlobal.isStringEmpty(url)) {
            webChat.writeResponse('Please enter a link to send it', chatConfig.writeResponseClassResponse);
            return;
        }

        var ppr = {
            'apiVersion' : '1.0',
            'type' : 'request',
            'body' : {
                'method' : 'newPushPageMessage',
                'pagePushURL' : url,
                'pagePushDestination' : 'newTab'
            }
        };

        webChat.writeResponse('Your link has been sent', chatConfig.writeResponseClassSent);
        avayaGlobal.getEl('urlInput').value = '';
        chatSocket.sendMessage(ppr);
    },

    /**
     * Ping the WebSocket to see if it is still open on both ends. JavaScript
     * doesn't have an API for this, so this is a workaround.
     */
    sendPing : function() {
        'use strict';
        var ping = {
            'apiVersion' : '1.0',
            'type' : 'request',
            'body' : {
                'method' : 'ping'
            }
        };
        chatSocket.sendMessage(ping);
    },

    /**
     * Start playing the on hold messages, if either the messages or the URLs are defined.
     */
    startOnHoldMessages : function() {
        'use strict';
        avayaGlobal.log.info('Starting the On Hold messages');
        var onHoldUrlsDefined = (webChat.webOnHoldUrls !== undefined);
        var onHoldMessagesDefined = (webChat.webOnHoldComfortGroups !== undefined);

        if (!onHoldUrlsDefined && !onHoldMessagesDefined) {
            avayaGlobal.log.warn('On Hold messages are not defined!');
        } else {
            webChat.webOnHoldInterval = setInterval(function() {
                if (webChat.webOnHoldURLs !== undefined) {
                    webChat.playOnHoldMessage(webChat.webOnHoldURLs);
                }

                if (webChat.webOnHoldComfortGroups !== undefined) {
                    webChat.playOnHoldMessage(webChat.webOnHoldComfortGroups);
                }

            }, chatConfig.webOnHoldTimer);
            webChat.timeouts.push(webChat.webOnHoldInterval);
        }
    },

    /**
     * Start the customer's typing timer.
     */
    startTypingTimer : function() {
        'use strict';
        var isTypingTimer = Date.now();
        var timerExpiryTime = webChat.lastIsTypingSent + webChat.timeBetweenMsgs;

        if (isTypingTimer >= timerExpiryTime) {
            webChat.sendIsTyping(true);
        }

    },

    /**
     * Update the typing image for a specific agent.
     * @param agent
     * @param isTyping
     */
    updateTypingCell : function(agent, isTyping) {
        'use strict';
        var image = agent.image;
        if (agent.agentType === 'active_participant' || agent.agentType === 'passive_participant') {
            image.src = isTyping === true ? chatConfig.agentTypingImage : chatConfig.agentImage;
        } else {
            image.src = isTyping === true ? chatConfig.supervisorTypingImage : chatConfig.supervisorImage;
        }

        image.nextSibling.textContent = isTyping === true ? agent.name.concat(' is typing') : agent.name;
    },

    /**
     * Update the users section when an agent leaves or joins.
     * @param agents
     */
    updateUsers : function(agents) {
        'use strict';
        webChat.users = {};

        // hide the children immediately, and then reshow them
        for (var x = 0; x < webChat.participants.children.length; x++) {
            var child = webChat.participants.children[x], image = child.getElementsByTagName('img')[0];
            child.className = 'hidden';
            image.className = 'hidden';
            image.src = '';
            // clear the TextNode after this - i.e. removes the text
            // after the image
            image.nextSibling.textContent = '';
        }

        if (agents !== undefined) {
            for (var i = 0; i < agents.length; i++) {
                var agent = agents[i];
                avayaGlobal.log.debug('Adding agent with id ' + agent.id + ' and name ' + agent.name);
		// 04-10-2017
		// send agent id to chatbot conversation and data reports
		                if (global_chatbot_token != "notoken" && global_chatbot_token.length > 7){
							sendToConvDataRepV2('con_agent_join', agent.id + " " + agent.name);
							visitor_engaged.agent_engaged = true ;
							visitor_engaged.customer_escalated = false;
							avayaGlobal.log.info('W_con_agent_join set visitor_engaged.agent_engaged = true visitor_engaged.customer_escalated = false');
							
							
				}

                var div = webChat.participants.children[i];
                div.className = 'user';
                // find the first image in the div
                var typingImage = div.getElementsByTagName('img')[0];
                typingImage.src = (agent.type === 'supervisor') ? chatConfig.supervisorImage : chatConfig.agentImage;
                typingImage.className = '';

                // should be the first TextNode in the div after the
                // image which enters it into the div just after the image
                typingImage.nextSibling.textContent = agent.name;

                webChat.users[agent.id] = {
                    name : agent.name,
                    isTyping : false,
                    agentType : agent.type,
                    image : typingImage
                };
            }
        }
    },

    /**
     * Writes the specified text out to the transcript box and includes an
     * autoscroll mechanism.
     * @param text
     * @param msgClass
     */
    writeResponse : function(text, msgClass) {
        'use strict';
        var paragraph = document.createElement('p');
        paragraph.className = msgClass;

        // check for URLs
        var urlIndices = avayaGlobal.getIndicesOf('http', text);
        if (urlIndices.length > 0) {
            var firstPart = text.substring(0, urlIndices[0]);
            var lastPart;
            var spans = [ firstPart ];
            for (var i = 0; i < urlIndices.length; i++) {
                var urlIndex = urlIndices[i];

                // find where the URL ends - either at a space, or at the end of the message
                var spaceIndex = text.indexOf(' ', urlIndex);
                if (spaceIndex === -1) {
                    spaceIndex = text.length;
                }
                var tmpText = text.substring(urlIndex, spaceIndex);
                var link;
                // Find the last character. I would use tmpText.endsWith, but the unit tests fail that (since it's experimental).
                var lastChar = tmpText.slice(-1);
                if (lastChar === '.' || lastChar === ',' || lastChar === '\'' || lastChar === '"' || lastChar === '}' ||
                        lastChar === ']' || lastChar === ')') { // NOSONAR cannot be reduced
                    avayaGlobal.log.debug('The link ' + tmpText + ' contains an invalid character at the end');
                    tmpText = tmpText.slice(0, -1);
                    spaceIndex -= 1;
                    avayaGlobal.log.debug('The link is now ' + tmpText);
                }
                link = tmpText;
                spans.push(link);

                // if there is text after the URL, insert it here
                if (i === (urlIndices.length - 1)) {
                    lastPart = text.substring(spaceIndex);
                    spans.push(lastPart);
                } else {
                    spans.push(text.substring(spaceIndex, urlIndices[i + 1]));
                }
            }

            // create a series of spans to hold everything,
            // and then append to the paragraph
            for (var j = 0; j < spans.length; j++) {
                var span = document.createElement('span');
                if (spans[j].indexOf('http') >= 0) {
                    webChat.appendLink(spans[j], 'newTab', false, span);
                } else {
                    span.textContent = spans[j];
                }
                paragraph.appendChild(span);
            }

        } else {
            // if there isn't a URL, just add the textContent to the paragraph. Much simpler :)
            paragraph.textContent = text;
        }

        webChat.messages.appendChild(paragraph);
        webChat.messages.scrollTop = webChat.messages.scrollHeight;
    },

    /**
     * Initialise the video.
     * TODO: blocked until we know how AMV are going to implement
     */
    initialiseVideo : function() {
        'use strict';
    },

    /**
     * Open the video/voice panel.
     */
    openVideoPanel : function() {
        'use strict';
        $('#videoPanel').dialog();
    },

    /**
     * Toggle video or voice.
     * TODO: blocked until we know how AMV are going to implement video.
     * @param showVideo
     *          true if showing video, false if using voice-only
     */
    toggleVideo : function(showVideo) {
        'use strict';
        if (showVideo) {
            // toggle the video. Log message to make Sonar shut up.
            avayaGlobal.log.debug('Showing video');
        } else {
            // hide the video. Log message to make Sonar shut up.
            avayaGlobal.log.debug('Hiding video');
        }
    },

    /**
     * Mute the video
     * @param mute
     *          true if the video is to be muted
     */
    muteVideo : function(mute) {
        'use strict';
        webChat.chatVideo.muted = mute;
    },

    /**
     * Utility function to set the workflow type for routing.
     * @param newWorkflowType
     */
    setWorkflowType : function(newWorkflowType) {
        'use strict';
        webChat.workflowType = newWorkflowType;
    },

    /**
     * Gather the required chat elements
     */
    gatherChatElements : function() {
        'use strict';
        avayaGlobal.log.info('Gathering chat elements');
        var find = avayaGlobal.getEl;
        chatLogon.contactTypeMenu = find('contactType');
        chatLogon.openChatButton = find('openbutton-chat');
        webChat.sendButton = find('sendbutton-chat');
        webChat.closeButton = find('closebutton-chat');
        webChat.messages = find('messages');
        webChat.outMessage = find('outmessage');
        webChat.participants = find('participants');
        webChat.pagePushDiv = find('pagePushDiv');
        webChat.videoPanel = find('videoPanel');
        webChat.chatVideo = find('chatVideo');
        webChat.coBrowseIframe = find('cobrowse');
        chatLogon.contactTypeMenu.onchange = chatLogon.updateChatType;
        avayaGlobal.addCountryCodes(find('country-chat'));
    },

    /**
     * Add an attribute to the estimated wait time
     * @param group: a String which contains the id of the group (e.g. '1').
     * @param attribute: a String which contains the attribute (e.g. 'Language.English')
     */
    addEwtAttribute : function(group, attribute) {
        'use strict';
        avayaGlobal.log.debug('Adding ' + attribute + ' to group ' + group);
        if (group === '' || attribute === '') {
            avayaGlobal.log.warn('Invalid parameters! {' + group + '}, {' + attribute + '}');
            return;
        }

        var groupArray = webChat.ewtRequest.serviceMap[group];

        // split the attribute into the key and value
        var tempArray = attribute.split('.');
        var key = tempArray[0];
        var value = tempArray[1];

        // check if this group exists
        if (groupArray !== undefined) {

            // if the attribute is not already part of the group, add the new attribute
            if (groupArray.attributes.hasOwnProperty(key) && groupArray.attributes[key].indexOf(value) < 0) {
                groupArray.attributes[key].push(value);
            } else {
                groupArray.attributes[key] = [ value ];
            }
        } else {
            // if the attribute does not exist,
            // create the new group as a JSON object with an attributes object and default priority
            webChat.ewtRequest.serviceMap[group] = {
                'attributes' : {},
                'priority' : webChat.priority
            };
            webChat.ewtRequest.serviceMap[group].attributes[key] = [ value ];
        }
        avayaGlobal.log.debug(webChat.ewtRequest.serviceMap);
    },

    /**
     * Remove an attribute from the EWT service map.
     * @param {String} group: a String containing the group ID (e.g. '1')
     * @param {String} attribute: a String containing the attribute (e.g. 'Language.English')
     */
    removeEwtAttribute : function(group, attribute) {
        'use strict';
        avayaGlobal.log.debug('Removing ' + attribute + ' from group ' + group);
        if (group === '' || attribute === '') {
            avayaGlobal.log.warn('Invalid parameters! {' + group + '}, {' + attribute + '}');
            return;
        }
        var groupObject = webChat.ewtRequest.serviceMap[group];

        // split the attribute into the key and value
        var tempArray = attribute.split('.');
        var key = tempArray[0];
        var value = tempArray[1];

        // check if the attributes actually exists in the service map. If it does, remove it.
        if (groupObject !== undefined && groupObject.attributes.hasOwnProperty(key)) {
            var array = groupObject.attributes[key];
            var index = array.indexOf(value);
            if (index >= 0) {
                array = array.splice(index, 1);
                webChat.ewtRequest.serviceMap[group].attributes[key] = array;
            }
        }
        avayaGlobal.log.debug(webChat.ewtRequest.serviceMap);
    },

    /**
     * Change the priority of an attribute group.
     * @param {String} group
     * @param {Number} priority
     */
    changeGroupPriority : function(group, priority) {
        'use strict';
        if (group === '') {
            avayaGlobal.log.warn('Invalid parameters! {' + group + '}');
            return;
        }

        // make sure that the priority is actually a number, and catch it if it isn't
        if (priority === undefined || typeof priority !== 'number') {
            avayaGlobal.log.warn('Priority is invalid, using the default (5)');
            priority = 5;
        }

        var groupObject = webChat.ewtRequest.serviceMap[group];
        if (groupObject !== undefined) {
            groupObject.priority = priority;
        }
        avayaGlobal.log.debug(groupObject);
    },

    /**
     * Remove a specific EWT attribute group.
     * @param {String} group: the group ID.
     */
    removeEwtAttributeGroup : function(group) {
        'use strict';
        if (group === '') {
            avayaGlobal.log.warn('Invalid paremter! {' + group + '}');
            return;
        }

        if (webChat.ewtRequest.serviceMap.hasOwnProperty(group)) {
            delete webChat.ewtRequest.serviceMap[group];
        }
    },

    /**
     * Request the estimated wait time. The attributes here use the Context Store format instead of the chat format
     * (see the webChatLogon.js file)
     */
    requestEwt : function() {
        'use strict';

        // reset the attributes - only Channel.Chat is ever hard-coded in.
        webChat.ewtRequest.serviceMap['1'].attributes = {
            //'Channel' : [ 'Chat' ]
            //Phase 1 Change
            'GRT_Chat' : [ 'agent' ],
            'priority' : webChat.priority
        };

        // get the webChatLogon attributes and convert to map
        var attributes = chatLogon.attributes;
        for (var i = 0; i < attributes.length; i++) {
            var attr = attributes[i];
            var array = attr.split('.'), key = array[0], value = array[1];
            var attrArray;
            if (Object.keys(webChat.ewtRequest.serviceMap['1'].attributes).indexOf(key) < 0) {
                attrArray = [ value ];
                webChat.ewtRequest.serviceMap['1'].attributes[key] = attrArray;
            } else {
                attrArray = webChat.ewtRequest.serviceMap['1'].attributes[key];
                if (attrArray.indexOf(value) < 0) {
                    attrArray.push(value);
                }
            }
        }

        // send the request
        var request = new XMLHttpRequest();
        request.open('POST', links.estimatedWaitTimeUrl);
        request.setRequestHeader('Content-Type', 'application/json');
        request.onreadystatechange = function() {
            if (request.readyState === 4) {
                if (request.status === 200) {

                    // if the response is empty, log an error.
                    if (request.response === '') {
                        avayaGlobal.log.error('EWT response is empty!');
                        return;
                    }

                    // use only the first for now. Replace this with the ID you wish to use
                    var mapId = '1';
                    webChat.parseEwtResponse(request.response, mapId);
                } else {
                    if (request.response === '') {
                        avayaGlobal.log.error('EWT response is empty!');
                    } else {
                        avayaGlobal.log.warn(request.response);
                    }
                }
            }
        };
        request.send(JSON.stringify(webChat.ewtRequest));
    },

    /**
     * Parse the Estimated Wait Time response.
     * @param {Object} response: the response as a JSON object.
     * @param {String} serviceMapID: the ID of the perferred group
     */
    parseEwtResponse : function(response, serviceMapId) {
        'use strict';
        var json = JSON.parse(response);
        var key = serviceMapId;
        avayaGlobal.log.info(json);
        webChat.parseServiceMap(json.serviceMetricsResponseMap[key]);
    },

    /**
     * Parse the specified service map for the estimated wait time.
     * @param {Object} map
     */
    parseServiceMap : function(map) {
        'use strict';

        // check if the EWT is defined here
        var alertMsg = webChat.chatPossibleMsg;
        var chatAvailable = true;
        var metrics = map.metrics;
        if (metrics !== undefined) {
            var waitTime = parseInt(metrics.EWT);
            if (waitTime < 0) {
                alertMsg = webChat.noAgentsAvailableMsg;
                chatAvailable = false;
            } else if (waitTime > webChat.maxWaitTime) {
                alertMsg = webChat.chatNotAvailableMsg;
                chatAvailable = false;
            } else {
                alertMsg = webChat.chatAvailableMsg.replace('{0}', waitTime);
                chatAvailable = true;
            }
        }

        if (!chatAvailable) {
            chatUI.hideChatPanel();
        }

        chatUI.showAlert(alertMsg);
    },

    // Subscribed coBrowse iFrame events.
    onLoadCoBrowseIframe : function() {
        'use strict';

        var contentWindow = webChat.coBrowseIframe.contentWindow;
        var iFramedCoBrowse = contentWindow.coBrowse;
        var iFramedCoBrowseUI = contentWindow.coBrowseUI;

        if (iFramedCoBrowse !== undefined && iFramedCoBrowseUI !== undefined) {
            iFramedCoBrowseUI.addListener(webChat);
            iFramedCoBrowse.init(webChat.coBrowseOceanaPath, webChat.coBrowseSDKPath, avayaGlobal.log, [ webChat,
                    iFramedCoBrowseUI ], links.coBrowseHost);
					
			// START OCPROVIDER-2089
			contentWindow.onbeforeunload = webChat.onBeforeUnloadCoBrowseIframe;
			// END OCPROVIDER-2089
        }
    },

    // Subscribed coBrowse events.
    onCoBrowseReady : function(source) {
        'use strict';

        if (webChat.isPagePushKey) {
            // Came from the iFrame handle it differently.
            if (webChat.coBrowseKey) {
                webChat.joinPagePushCoBrowse();
            }
        } else {
            // Came from the Co-Browse on this page.
            webChat.coBrowseReady = true;
			
			//Phase 2 : Commenting to disable the client initiated co-browse dialog box
            /*coBrowseUI.createProactiveJoinDialog();
            coBrowseUI.showCoBrowseLinkDiv();*/
        }
    },

    onCoBrowseSessionClose : function(source, title, text) {
        'use strict';

        webChat.coBrowseKey = '';
    },

    // Subscribed coBrowseUI events.
    onCoBrowseUIJoinRequest : function(source, name, coBrowseKey) {
        'use strict';

        coBrowse.joinSession(name, coBrowseKey, webChat.hideElements);
        coBrowseUI.hideCoBrowseLinkDiv();
    },

    onCoBrowseUIJoinFailure : function(source) {
        'use strict';

        if (webChat.coBrowseKey) {
            if (webChat.isPagePushKey) {
                webChat.cleanUpPagePushCoBrowse();
            }
        }
    },

    onCoBrowseUICleanUp : function(source) {
        'use strict';

        if (webChat.isPagePushKey) {
            webChat.cleanUpPagePushCoBrowse();
        }

        coBrowseUI.showCoBrowseLinkDiv();
    },

    // CoBrowse related functions.
    initCoBrowse : function() {
        'use strict';
        coBrowse.init(webChat.coBrowseOceanaPath, webChat.coBrowseSDKPath, avayaGlobal.log, [ webChat, coBrowseUI ],
                links.coBrowseHost);
    },

    joinPagePushCoBrowse : function() {
        'use strict';

        
        // Hide join dialog for other CoBrowse on this page.
        //Chat Phase 2 : commenting due to UI error
        //coBrowseUI.closeDialog(coBrowseUI.proactiveJoinDialogId);
        //coBrowseUI.hideCoBrowseLinkDiv();

        var iFramedCoBrowse = webChat.coBrowseIframe.contentWindow.coBrowse;
        if (iFramedCoBrowse) {
            iFramedCoBrowse.joinSession(webChat.g_user, webChat.coBrowseKey, webChat.hideElements);
        } else {
            // TODO: Handle error.
        }
    },

    joinKeyPushCoBrowse : function(coBrowseKey) {
        'use strict';

        coBrowse.joinSession(webChat.g_user, coBrowseKey, webChat.hideElements);
        coBrowseUI.hideCoBrowseLinkDiv();
    },

    cleanUpPagePushCoBrowse : function() {
        'use strict';

        webChat.isPagePushKey = false;
        coBrowseUI.hideCoBrowseIframe();
        //Phase 2 : Commenting to disable the client initiated co-browse dialog box
		//coBrowseUI.createProactiveJoinDialog();
        
      //Phase 2 : Minimize chat window on co-browse session close.
        if(window != null && window.parent != null && window.parent.$("#chatPanel") != null) {
        	window.parent.$("#chatPanel").css("top", "0px");
        	parent.window.resizeTo(600, 490);
        	parent.window.moveTo(570, 120);
        	//If chat window is slide in, make it slide out
        	if( $("#chatPanel").hasClass("close") ) {
        		$(".chatHandle").click();
        	}
        }
        
    },
	
	// START OCPROVIDER-2089
	onBeforeUnloadCoBrowseIframe : function() {
		'use strict';
		
		var contentWindow = webChat.coBrowseIframe.contentWindow;
        var iFramedCoBrowse = contentWindow.coBrowse;
        var iFramedCoBrowseUI = contentWindow.coBrowseUI;

        if (iFramedCoBrowse !== undefined && iFramedCoBrowseUI !== undefined) {
			iFramedCoBrowseUI.closeHangingDialogs();
        }
	}
	// END OCPROVIDER-2089
};

// the following will be called only if this file is present
$(function() {
    'use strict';
    chatConfig.loadWebChatConfig();
    webChat.gatherChatElements();
    webChat.requestEwt();
    chatLogon.saveAttributeCount();

    // Set Co-Browse iFrame onLoad handler.
    webChat.coBrowseIframe.onload = webChat.onLoadCoBrowseIframe;

    // Setup CoBrowse instance on this page.
    coBrowseUI.addListener(webChat);
    webChat.initCoBrowse();

    /*window.onbeforeunload = function() {
        // Can't override default message due to security restrictions
        // so the value returned here doesn't really matter.
        return "You're about to end your session, are you sure?";
    };*/
});
