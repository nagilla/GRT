/**
 * @license contextStore.js<br>
 * Copyright 2015 Avaya Inc. All Rights Reserved.<br>
 * <br>
 * Usage of this source is bound to the terms described in licences/License.txt<br>
 * <br>
 * Avaya - Confidential & Proprietary. <br>
 * Use pursuant to your signed agreement or Avaya Policy
 */

var contextStore = {

    contextId : '',

    attributes : {
        'Language' : [ 'English' ]
    },

    /**
     * Temporary attributes are used to hold things while requesting a context ID.
     */
    tempAttributes : {},

    requestingAttributes : false,

    /**
     * Work Assignment does not work with more than 10 attributes per context ID in total. This limit includes the attributes here
     * and those used to log into the chat.
     * Reduce this to match the default chat attributes in webChatLogon.js, e.g. if you have five attributes as defaults
     * in webChatLogon.js, lower this to 5.
     */
    maxAttributes : 10,
    totalAttributes : 0,

    csConfig : null,
    isGuest : true,
    csClient : null,
    csInstance : null,

    priority : 5,

    /**
     * Issue a GET request to Context Store to get the current attributes.
     */
    getAttributes : function() {
        'use strict';
        if (contextStore.contextId === '') {
            avayaGlobal.log.warn('Cannot retrieve attributes if contextId is empty!');
            return;
        }
        contextStore.requestingAttributes = true;

        contextStore.csInstance.getContext(contextStore.contextId, '', '', 'web').done(function(response) {
            var json = response;
            var attr = json.data;
            contextStore.attributes = attr;
            contextStore.requestingAttributes = false;

            if (!avayaGlobal.isJsonEmpty(contextStore.tempAttributes)) {
                avayaGlobal.log.warn('Need to add temporary attributes here!');
                contextStore.addTemporaryAttributes(attr);
            }
        }).fail(function(response) {
            // if it fails to get the context, create a new one
            contextStore.createContext();
        });

    },

    /**
     * Add the temporary attributes to the attributes returned from Context Store.
     * @param attr attributes from Context Store.
     */
    addTemporaryAttributes : function(attr) {
        'use strict';
        for ( var key in contextStore.tempAttributes) {

            avayaGlobal.log.debug('Examining ' + key);

            // check if this key already exists
            if (attr[key] !== undefined) {
                avayaGlobal.log.debug('The key ' + key +
                        ' exists in the temp attributes and CS attributes. Temp value ' +
                        contextStore.tempAttributes[key] + '; actual value ' + attr[key]);

                // merge the arrays and add back into the attribute
                var result = contextStore.mergeAttributeArrays(attr[key], contextStore.tempAttributes[key]);
                contextStore.attributes[key] = result;
                avayaGlobal.log.info(contextStore.attributes);
            } else {
                attr[key] = contextStore.tempAttributes[key];
            }
        }

        console.info(contextStore.attributes);

        // update the attributes, and reset the old ones
        contextStore.updateAttributes();
        contextStore.tempAttributes = {};
    },

    /**
     * Merge the attribute arrays.
     * @param csArray - the values associated in Context Store with an attribute
     * @param tempArray - the values associcated locally with an attribute
     * @returns a merged array without duplicates.
     */
    mergeAttributeArrays : function(csArray, tempArray) {
        'use strict';
        var result = csArray;
        for (var i = 0; i < tempArray.length; i++) {
            var tmp = tempArray[i];
            avayaGlobal.log.debug('Checking if ' + tmp + ' exists in CS attributes');
            if (csArray.indexOf(tmp) < 0) {
                avayaGlobal.log.debug('The value ' + tmp + ' does not exist in the array');
                csArray.push(tmp);
            }
        }
        return result;
    },

    /**
     * Update the attributes by issuing a PUT request to ContextStore. If the
     * contextId hasn't been set, it will not send.
     */
    updateAttributes : function() {
        'use strict';

        if (contextStore.contextId === '') {
            avayaGlobal.log.warn('Cannot update attributes without a context ID!');
            return;
        }

        // create the request data from the attributes
        var json = {
            'DataCenter' : 'Primary',
            'CustomerId' : '1',
            'AgentId' : '',
            'Strategy' : 'Most Idle',
            'ResourceMap' : {},
            'ServiceMap' : {
                '1' : {
                    'attributes' : contextStore.attributes,
                    'priority' : 5,
                    'rank' : null,
                    'resourceCount' : null
                }
            }
        };

        // upsert using the context ID. There is no done() method needed - it just returns an empty string if successful
        contextStore.csInstance.upsertContext(contextStore.contextId, JSON.stringify(json), '', '', 'web', 3600, false)
                .fail(function(response) {
                    avayaGlobal.log.error(response);
                });
    },

    /**
     * Save the context ID to the browser's session storage
     */
    saveContextId : function() {
        'use strict';
        avayaGlobal.log.info('context ID is now ' + contextStore.contextId);
        avayaGlobal.setSessionStorage('contextId', contextStore.contextId);
    },

    /**
     * Load the contextId.
     * @return contextId from sessionStorage
     */
    loadContextId : function() {
        'use strict';
        return avayaGlobal.getSessionStorage('contextId');
    },

    /**
     * Add or update an attribute.
     * @param newAttribute in format Attribute.Value e.g. Language.English
     */
    addAttribute : function(newAttribute) {
        'use strict';

        if (contextStore.totalAttributes >= contextStore.maxAttributes) {
            avayaGlobal.log.warn('OCPROVIDER-1026: cannot have more than 10 attributes in total');
            return;
        }

        // get the attribute ID (e.g. Language)
        var array = newAttribute.split('.');
        var attributeId = array.shift();

        if (contextStore.contextId === '' || contextStore.requestingAttributes) {
            avayaGlobal.log.warn('Cannot update attributes if context ID is empty!');
            contextStore.tempAttributes[attributeId] = array;
            return;
        }

        // check if the attribute exists.
        var attributeExists = contextStore.attributes.hasOwnProperty(attributeId);
        var oldArray;
        if (!attributeExists) {
            avayaGlobal.log.debug('The attribute ' + attributeId + ' does not exist');
            oldArray = [];
        } else {
            oldArray = contextStore.attributes[attributeId];
        }

        // add the new attribute to the current ones, if it doesn't exist
        if (oldArray.indexOf(array[0]) < 0) {
            oldArray.push(array[0]);

            // should now be e.g. 'Language' : ['English', 'French']
            contextStore.attributes[attributeId] = oldArray;
            avayaGlobal.log.debug('Attributes are now ' + JSON.stringify(contextStore.attributes));
            contextStore.updateAttributes();
            contextStore.totalAttributes++;
            avayaGlobal.setSessionStorage('totalAttributes', contextStore.totalAttributes);
        }

    },

    /**
     * Initialise the context.
     */
    initialise : function() {
        'use strict';

        var totalAttr = avayaGlobal.getSessionStorage('totalAttributes');
        contextStore.totalAttributes = (totalAttr !== null ? Number.parseInt(totalAttr) : 0);
        avayaGlobal.log.debug('Total attributes: ' + contextStore.totalAttributes);

        // create connection config here
        var conf = new AvayaDataStoreClient.Config.CsConfiguration();
        conf.serverInfo.hostName = links.contextStoreHost;
        conf.serverInfo.isSecure = links.secureContextStore;
        conf.enabled = true;
        var client = new AvayaDataStoreClient(conf);
        contextStore.csInstance = client.createDataStoreService(conf);
        contextStore.csClient = client;
        contextStore.csConfig = conf;
        client.registerlogger(avayaGlobal.log);

        // check if the context ID is empty or null. If it is, create a context;
        // otherwise, get the attributes
        var contextId = contextStore.loadContextId();
        if (contextId === null || contextId === '') {
            avayaGlobal.log.info('Context ID is null');
            contextStore.createContext();
        } else {
            avayaGlobal.log.info('Context ID is ' + contextStore.contextId);
            contextStore.contextId = contextId;
            contextStore.getAttributes();
        }
    },

    /**
     * Clears the context ID. Resets the total attribuites as well.
     */
    clearContextId : function() {
        'use strict';
        contextStore.contextId = "";
        contextStore.saveContextId();
        avayaGlobal.setSessionStorage('totalAttributes', 0);
    },

    /**
     * Set the context ID.
     * @param newId
     */
    setContextID : function(newId) {
        'use strict';
        contextStore.contextId = newId;
        contextStore.saveContextId();
    },

    /**
     * Create a new Context.
     */
    createContext : function() {
        'use strict';
        if (contextStore.csInstance === null || avayaGlobal.isJsonEmpty(contextStore.attributes)) {
            avayaGlobal.log.warn('Cannot create context if instance is null or attributes are empty!');
            return;
        }

        avayaGlobal.log.info('Creating a context');
        var data = {
            'contextId' : contextStore.contextId,
            'data' : {
                'DataCenter' : 'Primary',
                'CustomerId' : '1',
                'AgentId' : '',
                'Strategy' : 'Most Idle',
                'ResourceMap' : {},
                'ServiceMap' : {
                    '1' : {
                        'attributes' : contextStore.attributes,
                        'priority' : 5,
                        'rank' : null,
                        'resourceCount' : null
                    }
                }
            },
            //'groupId' : '',
            //'tenantId' : '',
            'persistToEDM' : false
        };

        // create the new context. Ignore the session ID, route ID, lease, rules or shortid for now
        contextStore.csInstance.createContext(JSON.stringify(data), '', '', 'web', '', false, false).done(
                function(response) {
                    // override the done() function to retrieve the context ID
                    // only needed if the context ID isn't set already
                    if (contextStore.contextId === '' || contextStore.contextId === null) {
                        var json = JSON.parse(response);
                        contextStore.contextId = json.data.contextId;
                        contextStore.saveContextId();
                    }
                });
    }
};

/**
 * Upon loading the page, check if the contextId has been set. If it hasn't,
 * request one.
 */

$(function() {
    'use strict';

    if (avayaGlobal.log === null) {
        avayaGlobal.log = avayaGlobal.makeLogger();
    }
    //    contextStore.setContextID('aabbccddeeffgg');
    contextStore.initialise();
});