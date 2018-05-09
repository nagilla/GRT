/**
 * webChatConfig.js
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
 * This section configures the Web Chat.
 *
 * \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
 */

// the WebSocket is globally accessible - there should only ever be one on the page?
var webSocket;

var chatConfig = {
    totalNumberOfRetries : 0,
    maxNumberOfRetries : 200,
    retryInterval : 3000,
    // If set to true, if the Websocket connection fails we will not attempt to re-establish the session
    dontRetryConnection : false,
    reconnectionTimeout : null,
    previouslyConnected : false,

    // if set to true, the chatbot will not announce its arrival/departure
   // suppressChatbotPresence : false,

    // if set to true, the chat will close the panel, etc. when finished
    resetOnClose : true,

    // message types
    messageTypeNewChatAck : 'newChatAcknowledgement',
    messageTypeAck : 'acknowledgement',
    messageTypeError : 'error',
    messageTypeNotification : 'notification',

    // notification messages are further broken down
    jsonMethodRequestChat : 'requestChat',
    jsonMethodRouteCancel : 'routeCancel',
    jsonMethodRequestNewParticipant : 'newParticipant',
    jsonMethodRequestIsTyping : 'isTyping',
    jsonMethodRequestNewMessage : 'newMessage',
    jsonMethodRequestCloseConversation : 'closeConversation',
    jsonMethodRequestParticipantLeave : 'participantLeave',
    jsonMethodRequestNewPushMessage : 'newPushPageMessage',
    jsonMethodRequestNewCoBrowseSessionKeyMessage : 'newCoBrowseSessionKeyMessage',
    jsonMethodPing : 'ping',

    // placeholder for new message. Format is displayName (timestamp): messageText
    newMessageText : '{0} ({1}): {2}',

    // placeholder for page push messages. {0} represents agent display name, {1} is the timestamp
    pagePushMessageText : '{0} sent the following URL at ({1}): ',
    coBrowseMessageText : '{0} initiated a Co-Browsing session at ({1})',

    // if set to true, the chatbot will not announce its arrival/departure
    suppressChatbotPresence : false,

    // how frequently the pings are sent
    pingTimer : 5000,

    // Configures how often Web-On-Hold messages are played
    webOnHoldTimer : 3000,

    typingTimeout : 10000,
    agentTypingTimeout : 3000,

    // default username for if the customer doesn't enter a name.
    defaultUsername : 'Customer',

    // CSS classes that distinguish customer from agent messages
    writeResponseClassResponse : 'response',
    writeResponseClassSent : 'sent',

    // links to various images
    agentImage : '/grt/images/live-chat/agent.png',
    agentTypingImage : '/grt/images/live-chat/agent_typing.png',
    supervisorImage : '/grt/images/live-chat/supervisor.png',
    supervisorTypingImage : '/grt/images/live-chat/supervisor_typing.png',

    /**
     * Configure from localStorage for testing.
     */
    loadWebChatConfig : function() {
        'use strict';
        var find = avayaGlobal.getEl;
        var settings = localStorage.getItem('chatSettings');
        var json = JSON.parse(settings);

        // if the settings aren't null, use them
        if (json !== null) {
            avayaGlobal.log.debug('Using old settings');

            var urls = json.urls;
            webChat.workflowType = json.workflowType;
            webChat.routePointIdentifier = json.routePointIdentifier;
            links.webChatUrl = urls.webChatUrl;
            links.estimatedWaitTimeUrl = urls.estimatedWaitTimeUrl;
            links.contextStoreHost = urls.contextStoreHost;
            links.coBrowseHost = urls.coBrowseHost;
            avayaGlobal.log.debug(json.attributes);
            chatLogon.attributes = json.attributes;

            if (json.coBrowseEnabled !== undefined) {
                webChat.coBrowseEnabled = json.coBrowseEnabled;
            }
        }

        // update the settings now
        find('webChatUrlInput').value = links.webChatUrl;
        find('contextStoreHostInput').value = links.contextStoreHost;
        find('ewtUrlInput').value = links.estimatedWaitTimeUrl;
        find('coBrowseHostInput').value = links.coBrowseHost;
        find('workflowInput').value = webChat.workflowType;
        find('routepointInput').value = webChat.routePointIdentifier;
        for (var i = 0; i < chatLogon.attributes.length; i++) {
            chatConfig.addAttributeEntry(chatLogon.attributes[i]);
        }
        find('coBrowseEnabledInput').checked = webChat.coBrowseEnabled;

    },

    /**
     * Save the WebChatConfig to localStorage for testing.
     */
    saveWebChatConfig : function() {
        'use strict';
        avayaGlobal.log.debug('Saving config');
        var find = avayaGlobal.getEl;
        links.webChatUrl = find('webChatUrlInput').value;
        links.contextStoreHost = find('contextStoreHostInput').value;
        links.estimatedWaitTimeUrl = find('ewtUrlInput').value;
        links.coBrowseHost = find('coBrowseHostInput').value;
        webChat.workflowType = find('workflowInput').value;
        webChat.routePointIdentifier = find('routepointInput').value;
        webChat.coBrowseEnabled = find('coBrowseEnabledInput').checked;

        // save the details to a JSON object
        var details = {
            'urls' : {
                'webChatUrl' : links.webChatUrl,
                'contextStoreHost' : links.contextStoreHost,
                'estimatedWaitTimeUrl' : links.estimatedWaitTimeUrl,
                'coBrowseHost' : links.coBrowseHost
            },
            'attributes' : chatLogon.attributes,
            'workflowType' : webChat.workflowType,
            'routePointIdentifier' : webChat.routePointIdentifier,
            'coBrowseEnabled' : webChat.coBrowseEnabled
        };
        localStorage.setItem('chatSettings', JSON.stringify(details));
        chatUI.showAlert('Configuration saved');
    },

    /**
     * Reset the configuration.
     */
    resetConfig : function() {
        'use strict';
        localStorage.setItem('chatSettings', null);
        chatUI.showAlert('Configuration reset. Please reload the page');
    },

    /**
     * Loads the help page
     */
    loadHelpPage : function() {
        'use strict';
        window.open('certs.html');
    },

    toggleUrls : function() {
        'use strict';
        $('#urlsListDiv').toggle('fold');
        chatConfig.resizeConfigPanel();
    },

    toggleAttributes : function() {
        'use strict';
        $('#attributesListDiv').toggle('fold');
        chatConfig.resizeConfigPanel();
    },

    toggleConfig : function() {
        'use strict';
        $('#chatConfigDiv').toggle('fold');
        chatConfig.resizeConfigPanel();
    },

    toggleCoBrowse : function() {
        'use strict';
        $('#coBrowseConfigDiv').toggle('fold');
        chatConfig.resizeConfigPanel();
    },

    /**
     * Resize the config panel.
     */
    resizeConfigPanel : function() {
        'use strict';
        $('#configPanel').dialog({
            width : 400,
            height : 'auto'
        });
    },

    /**
     * Add a new row to the attributes table. These are automatically added into the chat attributes.
     * @param attribute
     */
    addAttributeEntry : function(attribute) {
        'use strict';
        avayaGlobal.log.debug('Adding ' + attribute + ' to the table');
        if (avayaGlobal.isStringEmpty(attribute)) {
            avayaGlobal.log.warn('Cannot have empty attributes!');
            return;
        }

        // create the elements
        var table = avayaGlobal.getEl('attributesTable');
        var lastRow = avayaGlobal.getEl('newAttributeRow');
        var newRow = document.createElement('tr');
        var cell1 = document.createElement('td');
        var cell2 = document.createElement('td');
        var input = document.createElement('input');
        var button = document.createElement('button');

        // workaround for JavaScript scope problems when adding onclick
        var tmp = chatConfig;
        button.onclick = function() {
            tmp.removeAttributeEntry(table, newRow);
        };
        button.textContent = 'Remove';
        button.className = 'configButton';
        input.value = attribute;
        input.disabled = true;
        cell1.appendChild(input);
        cell2.appendChild(button);
        newRow.appendChild(cell1);
        newRow.appendChild(cell2);
        table.appendChild(newRow);
        chatLogon.addAttribute(attribute);
    },

    /**
     * Insert a new attribute. This is called from the HTML page.
     */
    insertNewAttribute : function() {
        'use strict';
        var newAttr = avayaGlobal.getEl('newAttributeInput').value;
        avayaGlobal.log.debug('Adding ' + newAttr);

        if (chatLogon.attributes.indexOf(newAttr) >= 0) {
            avayaGlobal.log.warn('The attribute ' + newAttr + ' already exists!');
            return;
        }

        chatConfig.addAttributeEntry(newAttr);
    },

    /**
     * Removes the specified entry from the table.
     * @param table
     * @param entry
     */
    removeAttributeEntry : function(table, entry) {
        'use strict';
        var attribute = entry.children[0].children[0].value;
        avayaGlobal.log.debug('Removing ' + attribute);
        chatLogon.removeAttribute(attribute);
        table.removeChild(entry);
    },

    /**
     * Utility function to request the EWT manually.
     */
    requestEwt : function() {
        'use strict';
        webChat.requestEwt();
    }
};
