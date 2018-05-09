/**
 * @license coBrowse.js<br>
 * Copyright 2015 Avaya Inc. All Rights Reserved.<br>
 * <br>
 * Usage of this source is bound to the terms described in licences/License.txt<br>
 * <br>
 * Avaya - Confidential & Proprietary. <br>
 * Use pursuant to your signed agreement or Avaya Policy
 */

var coBrowse = {

    coBrowseInstance : null,
	isSecure : true,
	logger : null,
	listeners : null,
	
	// Session storage keys.
	sessionKey : "coBrowse.sessionKey",
    inCoBrowse : "coBrowse.inCoBrowse",
    isPaused : "coBrowse.isPaused",
    agentInControl : "coBrowse.agentInControl",
	inControlRequest : "coBrowse.inControlRequest",

	init : function(jsPath, coBrowseSDKPath, genericLogger, listeners, hostName) {
	    'use strict';

	    coBrowse.logger = genericLogger;
	    coBrowse.listeners = listeners;
		
		// Setup the session storage if needed.
		if (coBrowse.getSessionItem(coBrowse.inCoBrowse) === null) {
            coBrowse.setSessionItem(coBrowse.inCoBrowse, "false");
        }
        if (coBrowse.getSessionItem(coBrowse.isPaused) === null) {
            coBrowse.setSessionItem(coBrowse.isPaused, "false");
        } 
        if (coBrowse.getSessionItem(coBrowse.agentInControl) === null) {
            coBrowse.setSessionItem(coBrowse.agentInControl, "false");
        }

	    $LAB.script(coBrowseSDKPath + 'sock.js')
	            .script(coBrowseSDKPath + 'Base64.js').script(
	                    coBrowseSDKPath + 'md5.js').script(
	                    coBrowseSDKPath + 'jquery_path.js').script(
	                    coBrowseSDKPath + 'AvayaCoBrowseClientServices.min.js')
	            .wait(function() {
	                coBrowse.configureInstance(hostName);
	            });
	},
	
	reset : function() {
		'use strict';
      
        // Reset the state for the next Co-Browse session.
        coBrowse.setSessionItem(coBrowse.inCoBrowse, "false");
        coBrowse.setSessionItem(coBrowse.isPaused, "false");
		coBrowse.setSessionItem(coBrowse.inControlRequest, "false");
        coBrowse.setSessionItem(coBrowse.agentInControl, "false");
    },
	
    getSessionItem : function(key) {
		'use strict';
        
        if (typeof(Storage) !== 'undefined') {
            return sessionStorage.getItem(key);
        } else {
            coBrowse.logger.error('No session storage availabe for Co-Browse.');
            return null;
        } 
    },
    
   setSessionItem : function(key, value) {
		'use strict';
        
        if (typeof(Storage) !== 'undefined') {
	    sessionStorage.setItem(key, value);
        } else {
            coBrowse.logger.error('No session storage availabe for Co-Browse.');
        }
    },
	
    rebuild : function() {
        'use strict'; 
        
        // Let any listeners know that they need to rebuild their internal states.
        coBrowse.listeners.forEach(function(listener) {
            if (listener.onCoBrowseRebuild !== undefined) {
				listener.onCoBrowseRebuild(coBrowse);
			}
		});
    },

	configureInstance : function(hostName) {
	    'use strict';

        coBrowse.logger.info("Successfully loaded all dependencies for coBrowse.");

        var config = new AvayaCoBrowseClientServices.Config.CoBrowseConfiguration();
        coBrowse.logger.info('AVCOBROWSE Engine (Customer) Load Complete');
        coBrowse.logger.info('[jQuery]: ' + $().jquery);
        config.serverInfo.hostName = hostName;
        config.serverInfo.port = coBrowse.isSecure ? '443' : '80';
        config.serverInfo.isSecure = coBrowse.isSecure;
        config.enabled = true;

        var coBrowseCustomerClientService = new AvayaCoBrowseClientServices();
        coBrowseCustomerClientService.registerLogger(coBrowse.logger);

        coBrowse.coBrowseInstance = coBrowseCustomerClientService.createCustomerCobrowse(config);

        coBrowse.listeners.forEach(function(listener) {
            if (listener.onCoBrowseReady !== undefined) {
                listener.onCoBrowseReady(coBrowse);
            }
        });
	},

	registerAPICallbacks : function() { // NOSONAR cannot be simplified any further
		'use strict';

		var instance = coBrowse.coBrowseInstance;

		instance.addOnConnectionResetCallback(function(evt) {
			if (evt.resume) {
				coBrowse.rebuild();
			}
		});

		instance.addOnConnectionCallback(function(evt) {
		    coBrowse.logger.info('Connected to Co-Browse with ' + evt.agent);

            coBrowse.listeners.forEach(function(listener) {
                if (listener.onCoBrowseConnection !== undefined) {
                    listener.onCoBrowseConnection(coBrowse);
                }
            });
		});

            instance.addOnConnectionFailureCallback(function (evt) {
                coBrowse.logger.info('Failed to connect to Co-Browse ' + evt.msg);
                coBrowse.logger.log(evt);

                coBrowse.listeners.forEach(function (listener) {
                    if (listener.onCoBrowseConnection !== undefined) {
                        listener.onCoBrowseConnectionFailure(coBrowse, evt.msg);
                    }
                });
            });

		instance.addOnSessionCloseCallback(function(evt) {
		    coBrowse.logger.info('Co-Browse session closed.');
			
			coBrowse.reset();

			var str = evt.msg.split(':');
			var title = '';
			var text = '';

			if (str.length > 0) {
				if (str[0] === 'inactive_timeout') {
					title = 'Inactive Session by Agent';
				} else if (str[0] === 'session_timeout') {
					title = 'Session Timeout by System';
				} else {
					title = 'Session Closed by the Agent';
				}
				//Phase 2 : Updating Message
				//text = str[1];
				text = "Co-browse has been closed, use the window to continue chatting.";
				
			}

            coBrowse.listeners.forEach(function(listener) {
                if (listener.onCoBrowseSessionClose !== undefined) {
                    listener.onCoBrowseSessionClose(coBrowse, title, text);
                }
            });
		});

		instance.addOnControlRequestCallback(function(evt) {
		    coBrowse.logger.info('Received Control request from ' + evt.agent);
			
			coBrowse.setSessionItem(coBrowse.inControlRequest, "true");
            coBrowse.listeners.forEach(function(listener) {
                if (listener.onCoBrowseControlRequest !== undefined) {
                    listener.onCoBrowseControlRequest(coBrowse);
                }
            });
		});

		instance.addOnControlReleaseCallback(function(evt) {
		    coBrowse.logger.info('Agent ' + evt.agent + ' has released Control.');
			
			coBrowse.setSessionItem(coBrowse.agentInControl, "false");

            coBrowse.listeners.forEach(function(listener) {
                if (listener.onCoBrowseControlRelease !== undefined) {
                    listener.onCoBrowseControlRelease(coBrowse);
                }
            });
		});

		instance.addOnfireInactivityCallback(function(evt) {
		    coBrowse.logger.warn('addOnfireInactivityCallback is unimplemented!');
		});

		instance
				.addOnStopInactivityTimerCallback(function(evt) {
				    coBrowse.logger.warn('addOnStopInactivityTimerCallback is unimplemented!');
				});
	},

	joinSession : function(displayName, coBrowseKey, hideElements) {
	    'use strict';

        try {
            if (coBrowse.coBrowseInstance) {
                coBrowse.logger.info('Successfully Initilized');
                coBrowse.logger.info('Joining session with key: ' + coBrowseKey);

                var len = hideElements.length;
                for (var i = 0; i < len; i++) {
                    var element = hideElements[i];
                    coBrowse.logger.info('Hiding element with ID ' + element + '.');
                    coBrowse.coBrowseInstance.addElementIDToRemove(element);
                }

                coBrowse.registerAPICallbacks();
                coBrowse.coBrowseInstance.start();

                displayName = displayName === '' ? 'Customer' : displayName;

				var previousKey = coBrowse.getSessionItem(coBrowse.sessionKey);
				if (previousKey !== coBrowseKey) { // Is it a different session from last session?
					coBrowse.setSessionItem(coBrowse.sessionKey, coBrowseKey);
					coBrowse.coBrowseInstance.joinSession(displayName, coBrowseKey)
						.then(coBrowse.onJoinSuccess.bind(coBrowse), coBrowse.onJoinFailure);
				} else {
					// Wait for the onConnection to fire to deal with a session reconnect.
				}
            }
        } catch (e) {
            coBrowse.logger.error(e);
        }
    },

	stopSession : function() {
		'use strict';

		coBrowse.logger.info('Attempting to stop the Co-Browse session.');
		coBrowse.coBrowseInstance.logoutSession().then(
				coBrowse.onStopSuccess.bind(coBrowse), coBrowse.onStopFailure);
	},

	togglePause : function() {
		'use strict';

		if (coBrowse.getSessionItem(coBrowse.isPaused) === "false") {
		    coBrowse.logger.info('Attempting to pause the Co-Browse session.');
			coBrowse.coBrowseInstance.pause().then(coBrowse.onPauseSuccess.bind(coBrowse),
					coBrowse.onPauseFailure);
		} else {
		    coBrowse.logger.info('Attempting to resume the Co-Browse session.');
			coBrowse.coBrowseInstance.resume().then(
					coBrowse.onResumeSuccess.bind(coBrowse), coBrowse.onResumeFailure);
		}
	},

	grantControl : function(evt) {
		'use strict';

		coBrowse.logger.info('Co-Browse Control request for ' + evt.agent + ' granted.');
		coBrowse.coBrowseInstance.grantControl().then(
				coBrowse.onGrantControlSuccess.bind(coBrowse),
				coBrowse.onGrantControlFailure);
	},

	denyControl : function(evt) {
		'use strict';

		coBrowse.logger.info('Co-Browse Control request for ' + evt.agent + ' denied.');
		coBrowse.coBrowseInstance.denyControl()
				.then(coBrowse.onDenyControlSuccess.bind(coBrowse),
						coBrowse.onDenyControlFailure);
	},

	revokeControl : function(evt) {
		'use strict';

		coBrowse.logger.info('Co-Browse Revoke Control request for ' + evt.agent + ' initiated.');
		coBrowse.coBrowseInstance.revokeControl().then(
				coBrowse.onRevokeControlSuccess.bind(coBrowse),
				coBrowse.onRevokeControlFailure);
	},

	onJoinSuccess : function(data) {
		'use strict';
		
		coBrowse.setSessionItem(coBrowse.inCoBrowse, "true");

		coBrowse.logger.info('Join session successful.');
		coBrowse.listeners.forEach(function(listener) {
            if (listener.onCoBrowseJoinSuccess !== undefined) {
                listener.onCoBrowseJoinSuccess(coBrowse);
            }
        });
	},

	onJoinFailure : function(error) {
		'use strict';

		var errorMessage = error.getMessage();
		
		// START OCPROVIDER-2089
		var exp = "/Customer session with same ID already in progress/";
		if (errorMessage.search(exp)) {
			// Likely to be moving between Co-Browse pages, not an actual error.
			coBrowse.onJoinSuccess(null);
			return;
		}
		// END OCPROVIDER-2089
		
		coBrowse.logger.info('Join session failed ' + errorMessage);
		coBrowse.listeners.forEach(function(listener) {
            if (listener.onCoBrowseJoinFailure !== undefined) {
                listener.onCoBrowseJoinFailure(coBrowse, errorMessage);
            }
        });
	},

	onGrantControlSuccess : function(data) {
		'use strict';
		
		coBrowse.setSessionItem(coBrowse.inControlRequest, "false");
		coBrowse.setSessionItem(coBrowse.agentInControl, "true");

		coBrowse.logger.info('Grant control successful.');
        coBrowse.listeners.forEach(function(listener) {
            if (listener.onCoBrowseGrantControlSuccess !== undefined) {
                listener.onCoBrowseGrantControlSuccess(coBrowse);
            }
        });
	},

	onGrantControlFailure : function(error) {
		'use strict';

		var errorMessage = error.getMessage();
		coBrowse.logger.info('Unable to grant control, reason ' + errorMessage);
        coBrowse.listeners.forEach(function(listener) {
            if (listener.onCoBrowseGrantControlFailure !== undefined) {
                listener.onCoBrowseGrantControlFailure(coBrowse, errorMessage);
            }
        });
	},

	onDenyControlSuccess : function(data) {
		'use strict';
		coBrowse.setSessionItem(coBrowse.inControlRequest, "false");

		coBrowse.logger.info('Deny control successful.');
        coBrowse.listeners.forEach(function(listener) {
            if (listener.onCoBrowseDenyControlSuccess !== undefined) {
                listener.onCoBrowseDenyControlSuccess(coBrowse);
            }
        });
	},

	onDenyControlFailure : function(error) {
		'use strict';

		var errorMessage = error.getMessage();
		coBrowse.logger.info('Unable to deny control, reason ' + errorMessage);
        coBrowse.listeners.forEach(function(listener) {
            if (listener.onCoBrowseDenyControlFailure !== undefined) {
                listener.onCoBrowseDenyControlFailure(coBrowse, errorMessage);
            }
        });
	},

	onRevokeControlSuccess : function(data) {
		'use strict';

		coBrowse.logger.info('Revoke control successful.');
		
		coBrowse.setSessionItem(coBrowse.agentInControl, "false");
		
        coBrowse.listeners.forEach(function(listener) {
            if (listener.onCoBrowseRevokeControlSuccess !== undefined) {
                listener.onCoBrowseRevokeControlSuccess(coBrowse);
            }
        });
	},

	onRevokeControlFailure : function(error) {
		'use strict';

		var errorMessage = error.getMessage();
		coBrowse.logger.info('Unable to revoke control, reason ' + errorMessage);
        coBrowse.listeners.forEach(function(listener) {
            if (listener.onCoBrowseRevokeControlFailure !== undefined) {
                listener.onCoBrowseRevokeControlFailure(coBrowse, errorMessage);
            }
        });
	},

	onPauseSuccess : function(data) {
		'use strict';

		coBrowse.logger.info('Paused co-browse successfully.');
		
		coBrowse.setSessionItem(coBrowse.isPaused, "true");
		
        coBrowse.listeners.forEach(function(listener) {
            if (listener.onCoBrowsePauseSuccess !== undefined) {
                listener.onCoBrowsePauseSuccess(coBrowse);
            }
        });
	},

	onPauseFailure : function(error) {
		'use strict';

		var errorMessage = error.getMessage();
		coBrowse.logger.info('Unable to pause co-browse, reason ' + errorMessage);
        coBrowse.listeners.forEach(function(listener) {
            if (listener.onCoBrowsePauseFailure !== undefined) {
                listener.onCoBrowsePauseFailure(coBrowse, errorMessage);
            }
        });
	},

	onResumeSuccess : function(data) {
		'use strict';

		coBrowse.logger.info('Resumed co-browse successfully.');
		
		coBrowse.setSessionItem(coBrowse.isPaused, "false");
		
        coBrowse.listeners.forEach(function(listener) {
            if (listener.onCoBrowseResumeSuccess !== undefined) {
                listener.onCoBrowseResumeSuccess(coBrowse);
            }
        });
	},

	onResumeFailure : function(error) {
		'use strict';

		var errorMessage = error.getMessage();
		coBrowse.logger.info('Unable to resume co-browse, reason ' + errorMessage);
        coBrowse.listeners.forEach(function(listener) {
            if (listener.onCoBrowseResumeFailure !== undefined) {
                listener.onCoBrowseResumeFailure(coBrowse, errorMessage);
            }
        });
	},

	onStopSuccess : function(data) {
		'use strict';
		
		coBrowse.reset();

        coBrowse.listeners.forEach(function(listener) {
            if (listener.onCoBrowseStopSuccess !== undefined) {
                listener.onCoBrowseStopSuccess(coBrowse);
            }
        });
	},

	onStopFailure : function(error) {
		'use strict';

		var errorMessage = error.getMessage();
		coBrowse.logger.info('Unable to stop co-browse, reason ' + errorMessage);
        coBrowse.listeners.forEach(function(listener) {
            if (listener.onCoBrowseStopFailure !== undefined) {
                listener.onCoBrowseStopFailure(coBrowse, errorMessage);
            }
        });
	}
};