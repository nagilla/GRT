/**
 * webChatLogon.js
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
 * This section defines the functionality for the logon window
 *
 * \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
 */

var chatLogon = {

    /**
    * The attributes for the chat. There is a known issue where Work Assignment does not accept any contact
    * whose contextId has more than 10 attributes associated with it. Those attributes include this array *AND*
    */
    //	attributes : [ 'Location.Inhouse' ],
  	//  attributes : [ 'GRT_Chat.dev' ],
		attributes : [ 'GRT_Chat.agent' ],
	//	attributes : [ 'Location.Inhouse', 'Language.English' ],

    // workaround for ED having issues with more than 10 attributes per context ID.
    attributeCount : 1,
    maxAttributes : 10,

    contactTypeMenu : null,
    openChatButton : null,

    /**
     * Logs the customer into the chat.
     * @param name
     * @param phone
     * @param email
     * @param subject
     */
    logon : function(name, phone, email, subject) {
        'use strict';
        // if the email address is empty or otherwise invalid, alert the user.
        // if they accept this, then continue
        var confirm = true;
        var isValid = chatLogon.parseEmail(email);
        if (isValid === false) {
            confirm = window
                    .confirm('You may use the chat without a valid email, but you will not be able to receive transcripts of the conversation');
        }

        if (confirm === true) {

            // if the customer's email was invalid, and they went ahead,
            // reset it
            if (isValid === false) {
                avayaGlobal.log.warn('Email was invalid, resetting');
                email = '';
                avayaGlobal.setSessionStorage('email', email);
            }

            chatUI.changeToChatMode();
            webChat.initChat();
        }
    },

    /**
     * Parses an email to check that it is valid. Will allow strings, aliases using '+',
     * or multiple dots (as long as these are not consecutive or the last character before '@').
     * @param emailToParse
     * @returns true if email is valid
     */
    parseEmail : function(emailToParse) {
        'use strict';
        // this regex allows any alphanumeric character
        var re = /((\w\+\w+)|(\w+)|(\w\.\w))+@(\w+\.\w+)/;
        var possible = re.test(emailToParse);
        // the second condition removes any double-dots, as these are invalid
        return (possible && emailToParse.indexOf('..') < 0);
    },

    /**
     * Parses an email to check that it is valid without using a regex. The email is invalid if any of the following are true:
     *  - There is no '@' symbol.
     *  - There is no '.' symbol after the '@' symbol.
     *  - The address includes consecutive dots (..)
     *  - The address includes spaces.
     *  - The address contains a dot just before the '@' symbol (.@)
     * @param emailToParse
     * @returns {Boolean} true if the email is vald
     */
    parseEmailManually : function(emailToParse) {
        'use strict';
        var atIndex = emailToParse.indexOf('@');
        var dotIndex = emailToParse.indexOf('.', atIndex);
        if (atIndex < 0 || dotIndex < (atIndex + 2) || emailToParse.includes('..') || emailToParse.includes(' ') ||
                emailToParse.includes('.@')) {
            return false;
        } else {
            return true;
        }
    },

    /**
     * Gathers the customer's details before logging them in.
     */
    gatherDetails : function() {
        'use strict';
                                // in case of a Connection closed' message at the beginining of chat session
                                // just remove the output - not needed in here
                                if (document.getElementById("messages").innerHTML.indexOf('Connection closed') > -1) {
                                                document.getElementById("messages").innerHTML = '';
                                }

        var l_user = avayaGlobal.getEl('user-chat').value, l_country = avayaGlobal.getEl('country-chat').value, l_email = avayaGlobal
                .getEl('email-chat').value, l_subject = avayaGlobal.getEl('subject-chat');
        
        var ssoCountry = avayaGlobal.getEl('ssoCountry').value;
        var ssoCity = avayaGlobal.getEl('ssoCity').value;
        var ssoBpLinkId = avayaGlobal.getEl('ssoBpLinkId').value;
        var ssoCompany = avayaGlobal.getEl('ssoCompany').value;
        var ssoCustomerNumber = avayaGlobal.getEl('ssoCustomerNumber').value;
        var ssoLinkId = avayaGlobal.getEl('ssoLinkId').value;
        var ssoZipcode = avayaGlobal.getEl('ssoZipcode').value;
        
        var ssoAddress = avayaGlobal.getEl('address-chat').value + ', '+ ssoCity + ', ' + ssoZipcode;
        
        var ssoUserType = avayaGlobal.getEl('ssoUserType').value;
        
        

        avayaGlobal.log.info('Gathering customer details');

        // store the phone details in a JSON object for convenience
        var l_phone = {
            'country' : avayaGlobal.getEl('country-chat').value,
            'area' : avayaGlobal.getEl('area-chat').value,
            'phone' : avayaGlobal.getEl('phone-chat').value
        };

        avayaGlobal.setSessionStorage('user', l_user);
        avayaGlobal.setSessionStorage('phone', JSON.stringify(l_phone));
        avayaGlobal.setSessionStorage('country', l_country);
        avayaGlobal.setSessionStorage('email', l_email);
        avayaGlobal.setSessionStorage('subject', l_subject);
        //set additional fields
        avayaGlobal.setSessionStorage('ssoCountry', ssoCountry);
        avayaGlobal.setSessionStorage('ssoBpLinkId', ssoBpLinkId);
        avayaGlobal.setSessionStorage('ssoCompany', ssoCompany);
        avayaGlobal.setSessionStorage('ssoCustomerNumber', ssoCustomerNumber);
        avayaGlobal.setSessionStorage('ssoLinkId', ssoLinkId);        
        avayaGlobal.setSessionStorage('ssoAddress', ssoAddress);
        avayaGlobal.setSessionStorage('ssoUserType', ssoUserType);
        
        
        //Adding ContextStorage attributes
//        contextStore.addAttribute('user' + l_user);
//        contextStore.addAttribute('phone.' + avayaGlobal.getEl('phone-chat').value);
        //contextStore.addAttribute('country.' + l_country);
//        contextStore.addAttribute('email.' + l_email);
        //contextStore.addAttribute('subject.' + l_subject);
//        contextStore.addAttribute('ssoCountry.' + ssoCountry);
//        contextStore.addAttribute('ssoBpLinkId.' + ssoBpLinkId);
//        contextStore.addAttribute('ssoCompany.' + ssoCompany);
//        contextStore.addAttribute('ssoCustomerNumber.' + ssoCustomerNumber);
//        contextStore.addAttribute('ssoLinkId.' + ssoLinkId);
//        contextStore.addAttribute('ssoAddress.' + ssoAddress);
//        contextStore.addAttribute('ssoUserType.' + ssoUserType);
      //Adding ContextStorage attributes ends

avayaGlobal.log.info('calling chatLogon.logon method with values: l_user='+l_user+',l_phone='+l_phone+', l_email='+l_email+' l_subject='+l_subject);

        chatLogon.logon(l_user, l_phone, l_email, l_subject);
    },

    /**
     * Verifies that the phone number is valid using a regex.
     * @param number
     * @returns {Boolean} true if the phone number is valid
     */
    verifyPhone : function(number) {
        'use strict';

        // this should allow calls from any country in the format 00xxx yyy zzzzzzz
        var re = /^[\+\d][\s\d]+$/;
        return re.test(number);
    },
    /**
     * Update the text content of the chat button to reflect the current contact type.
     * This is just "eye candy".
     */
    updateChatType : function() {
        'use strict';
        var type = chatLogon.contactTypeMenu.value;
        if (type.toLowerCase() === 'chat') {
            chatUI.changeElementText(chatLogon.openChatButton, 'Chat Now');
        } else {
            chatUI.changeElementText(chatLogon.openChatButton, 'Call Now');
        }
    },

    /**
     * Add an attribute to the chat logon attributes. This does <em>not</em> affect Context Store.
     * @param newAttribute is a String that represents the attribute.
     */
    addAttribute : function(newAttribute) {
        'use strict';
        if (chatLogon.attributes.indexOf(newAttribute) < 0 && chatLogon.attributeCount < chatLogon.maxAttributes) {
            chatLogon.attributes.push(newAttribute);
            chatLogon.attributeCount++;
            chatLogon.saveAttributeCount();
        }
    },

    /**
     * Removes the specified attribute from the chat attributes. This does <em>not</em> affect Context Store.
     * @param oldAttribute is a String that represents the attribute to remove.
     */
    removeAttribute : function(oldAttribute) {
        'use strict';
        var index = chatLogon.attributes.indexOf(oldAttribute);
        chatLogon.attributes.splice(index, 1);
        if (chatLogon.attributeCount > 0) {
            chatLogon.attributeCount--;
            chatLogon.saveAttributeCount();
        }
    },

    /**
     * Save the total attribute count. Part of a workaround for OCPROVIDER-1206
     */
    saveAttributeCount : function() {
        'use strict';
        avayaGlobal.setSessionStorage('chatAttrCount', chatLogon.attributeCount);
    },

    loadAttributeCount : function() {
        'use strict';
        var attr = avayaGlobal.getSessionStorage('totalAttributes');
        chatLogon.attributeCount = attr !== null ? Number.parseInt(attr) : 0;
    }
};
