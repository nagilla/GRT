/**
 * @license global.js
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
 * \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
 * Holds functions and variables that are accessible across the entire site.
 * Import this before any other script, if not using a concatenated file.
 * \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
 */

var avayaGlobal = {

    browserWarning : 'Your browser does not support required features for this website. The earliest supported versions are Internet Explorer 10, Firefox 11, Chrome 31, Safari 7.1 and Opera 12.1',
    language : 'en_GB',

    /**
     * The logger. By default, uses the browser console.
     */
    log : null,

    /**
     * Create a new logger, using the console.
     * @return AvayaClient.Base.Logger
     */
    makeLogger : function() {
        'use strict';
        console.info("Creating a logger");
        var logger = AvayaClientServices.Base.Logger;
        if (logger !== null) {
            logger.addLogger(console);
        } else {
            console.warn("AvayaClientServices were null!");
            logger = console;
        }
        return logger;
    },

    /**
     * Creates an option for a select.
     * @param textContent
     * @param value
     * @returns {___anonymous1400_1411}
     */
    addSelectOption : function(textContent, value) {
        'use strict';
        var selectOption = document.createElement('option');
        selectOption.textContent = textContent;
        selectOption.value = value;
        return selectOption;
    },

    /**
     * Adds international dialling codes to the specified menu.
     * @param menu
     */
    addCountryCodes : function(menu) {
        'use strict';
        this.log.info('Adding dialling codes to ' + menu.id);
        var countries = codes.countries;
        for ( var entry in countries) {
            if (countries.hasOwnProperty(entry)) {
                var country = countries[entry];
                menu.appendChild(this.addSelectOption(country.name + ' (' + country.code + ')', country.code));
            }
        }
    },

    /**
     * Checks that a browser supports WebSockets, XMLHttpRequests and JSON.
     */
    detectBrowserSupport : function() {
        'use strict';
        if (!WebSocket || !XMLHttpRequest || !JSON) {
            window.alert(this.browserWarning);
        }
    },

    /**
     * Returns the element with the specified ID
     * TODO: replace with ElementCache
     * @param el
     * @returns {Element}
     */
    getEl : function(el) {
        'use strict';
        return document.getElementById(el);
    },

    /**
     * Initialises all elements related to the chat.
     * TODO: replace with ElementCache
     */
    gatherElements : function() {
        'use strict';
        avayaGlobal.log.info('Gathering elements');
        chatLogon.contactTypeMenu = this.getEl('contactType');
        chatLogon.openChatButton = this.getEl('openbutton-chat');
        webChat.sendButton = this.getEl('sendbutton-chat');
        webChat.closeButton = this.getEl('closebutton-chat');
        webChat.messages = this.getEl('messages');
        webChat.outMessage = this.getEl('outmessage');
        webChat.participants = this.getEl('participants');
        webChat.pagePushDiv = this.getEl('pagePushDiv');
        webChat.videoPanel = this.getEl('videoPanel');
        webChat.chatVideo = this.getEl('chatVideo');
        chatLogon.contactTypeMenu.onchange = chatLogon.updateChatType;
    },

    /**
     * Get an object from session storage.
     * @param key
     * @returns the resulting pair
     */
    getSessionStorage : function(key) {
        'use strict';
        return sessionStorage.getItem(key);
    },

    /**
     * Checks that a string is empty.
     * @param string
     * @returns {Boolean} true if string has length of zero
     */
    isStringEmpty : function(string) {
        'use strict';
        return (string.length === 0);
    },

    /**
     * Sets a specified key-value pair in session storage.
     * @param key
     * @param value
     */
    setSessionStorage : function(key, value) {
        'use strict';
        sessionStorage.setItem(key, value);
    },

    /**
     * Clears session storage.
     */
    clearSessionStorage : function() {
        'use strict';
        this.setSessionStorage('user', '');
        this.setSessionStorage('phone', '');
        this.setSessionStorage('email', '');
        this.setSessionStorage('account', '');
        this.setSessionStorage('skillset', '');
        this.setSessionStorage('subject', '');
        this.setSessionStorage('attributes', '');
    },

    /**
     * Creates and returns a JSON object for a particular value
     * @param value
     * @returns {JSON}
     */
    createJson : function(value) {
        'use strict';
        var msg = {
            'skillset' : value
        };
        return JSON.stringify(msg);
    },

    /**
     * Gets the value of path parameter from a URL by name.
     *
     * attribution:
     *     http://stackoverflow.com/questions/901115/how-can-i-get-query-string-values-in-javascript?page=1&tab=votes#tab-top
     *
     * @param name
     * @param url
     * @returns the parameter value
     */
    getParameterByName : function(name, url) {
        'use strict';
        if (!url) {
            url = window.location.href;
        }

        name = name.replace(/[\[\]]/g, '\\$&');
        var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'), results = regex.exec(url);

        if (!results || !results[2]) {
            return null;
        }

        return decodeURIComponent(results[2].replace(/([\+%]20)+/g, ' '));
    },

    /**
     * Alternative version of getParameterByName that doesn't use regexes.
     * @param name
     * @param url
     * @returns the parameter value
     */
    getParameter : function(name, url) {
        'use strict';
        var index = url.indexOf(name + '=');
        if (index < 0) {
            return null;
        }

        // find the index of the next parameter, if there is more than one
        var nextKeyIndex = url.indexOf('?', index);
        var param = url.substring(index, nextKeyIndex < 0 ? url.length : nextKeyIndex);
        param = param.replace(name + '=', '');

        // this automatically replaces %20 with an actual space
        return decodeURIComponent(param);
    },

    /**
     * Checks if a JSON object is empty. The constructor condition is due to the fact that
     * Object.keys(new Date()).length returns 0, but this is invalid:
     * http://stackoverflow.com/a/32108184/3861543
     * @param json
     * @returns {Boolean}
     */
    isJsonEmpty : function(json) {
        'use strict';
        return (Object.keys(json).length === 0 && json.constructor === Object);
    },

    /**
     * Detach all children from the specified element
     * @param element
     */
    detachChildren : function(element) {
        'use strict';
        while (element.firstChild) {
            element.removeChild(element.firstChild);
        }
    },

    /**
     * Find multiple occurences of a string inside another. Adapted from the answer at:
     * http://stackoverflow.com/a/3410557/3861543
     * @param {String} targetString - the string you wish to find
     * @param {String} str - the string to search inside
     * @param {Boolean} caseSensitive - if false, upper/lower case doesn't matter 
     */
    getIndicesOf : function(targetString, str, caseSensitive) {
        'use strict';
        var targetStringLength = targetString.length;
        if (targetStringLength === 0) {
            avayaGlobal.log.warn('Search string is empty!');
            return;
        }
        var startIndex = 0, index, indices = [];
        if (!caseSensitive) {
            str = str.toLowerCase();
            targetString = targetString.toLowerCase();
        }
        while ((index = str.indexOf(targetString, startIndex)) > -1) {
            indices.push(index);
            startIndex = index + targetStringLength;
        }
        return indices;
    }
};

// the following is called if the file is present on the page
$(function() {
    'use strict';
    avayaGlobal.detectBrowserSupport();
    avayaGlobal.log = avayaGlobal.makeLogger();
});
