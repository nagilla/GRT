/**
 * @license
 * links.js
 * Copyright 2015 Avaya Inc. All Rights Reserved. Usage of
 * this source is bound to the terms described in licences/License.txt
 *
 * Avaya - Confidential & Proprietary. Use pursuant to your signed
 * agreement or Avaya Policy
 */

/*
 * This section contains all the urls. This file will NOT be minified during the
 * build for ease of modification. Replace the following URLS with the hostname or SIP IP address
 * of the server or proxy.
 */

var links = {

		// new FP1 environment
		/*  webChatUrl : 'wss://10.134.36.68/services/websocket/chat',
		  coBrowseHost : '10.134.36.67',
		  contextStoreHost : '10.134.36.66',
		  estimatedWaitTimeUrl : 'https://10.134.36.68/services/CustomerControllerService/gila/ewt/request',
		*/

		// New  Prod  environment
		/*webChatUrl :'wss://52.14.121.134/services/websocket/chat',
		coBrowseHost : '52.14.121.134',
		contextStoreHost : 'https://52.14.121.134/services/CSRest/cs/contexts/',
		estimatedWaitTimeUrl : 'https://52.14.121.134/services/customer-service/gila/ewt/request',
		*/

		// new prod environment fqdn
		webChatUrl :'wss://oceana.avaya.com/services/websocket/chat',
	     	coBrowseHost : 'oceana.avaya.com',
		//coBrowseHost : 'workspaces.mustang.avaya.com',
		contextStoreHost : 'https://oceana.avaya.com/services/CSRest/cs/contexts/',
		estimatedWaitTimeUrl : 'https://oceana.avaya.com/services/customer-service/gila/ewt/request',
		  
		//test environment
/*  webChatUrl : 'wss://135.122.73.211/services/websocket/chat',
    coBrowseUrl : '135.122.73.237',
    contextStoreUrl : 'https://135.122.73.200/services/CSRest/cs/contexts/',
    estimatedWaitTimeUrl : 'https://135.122.73.211/services/CustomerControllerService/gila/ewt/request'
*/
  	
    //FP1 environment
	/*webChatUrl : 'ws://10.134.47.95/services/websocket/chat',
	coBrowseHost : '10.134.42.79',
	contextStoreHost : '10.134.47.193',
	estimatedWaitTimeUrl : 'http://10.134.47.95/services/CustomerControllerService/gila/ewt/request',
		*/
	/**
	 * If set to true, the Context Store SDK will use a secure connection
	 */
	secureContextStore : true,


    	//Production environment
/*    webChatUrl : 'wss://192.168.119.142/services/websocket/chat',
    coBrowseUrl : '192.168.119.142',
    contextStoreUrl : '192.168.119.139',
    estimatedWaitTimeUrl : 'http://192.168.119.142/services/customer-service/gila/ewt/request'

*/

    	//Production Reverse Proxy environment
 /*   webChatUrl : 'wss://ec2-34-196-76-248.compute-1.amazonaws.com/services/websocket/chat',
	coBrowseUrl : 'ec2-34-196-76-248.compute-1.amazonaws.com',
    contextStoreUrl : 'https://ec2-34-196-76-248.compute-1.amazonaws.com/services/CSRest/cs/contexts/',
    estimatedWaitTimeUrl : 'https://ec2-34-196-76-248.compute-1.amazonaws.com/services/customer-service/gila/ewt/request'
*/
    	//Production Reverse Proxy environment
	/*webChatUrl :'wss://oceana-sp1.avaya.com/services/websocket/chat',
    coBrowseUrl : 'oceana-sp1.avaya.com',
    contextStoreUrl : 'https://oceana-sp1.avaya.com/services/CSRest/cs/contexts/',
    estimatedWaitTimeUrl : 'https://oceana-sp1.avaya.com/services/customer-service/gila/ewt/request'*/

        //Production Inner Revese Proxy environment
   /* webChatUrl : 'wss://awsedp142s.avayaaws.com/services/websocket/chat',
    coBrowseUrl : 'awsedp142s.avayaaws.com',
    contextStoreUrl : 'https://awsedp139s.avayaaws.com/services/CSRest/cs/contexts/',
    estimatedWaitTimeUrl : 'https://awsedp142s.avayaaws.com/services/customer-service/gila/ewt/request'
*/

//Production AWS FP1  environment
/*    webChatUrl :'wss://oceana.avaya.com/services/websocket/chat',
    coBrowseUrl : 'oceana.avaya.com',
    contextStoreUrl : 'https://oceana.avaya.com/services/CSRest/cs/contexts/',
    estimatedWaitTimeUrl : 'https://oceana.avaya.com/services/customer-service/gila/ewt/request'
*/

};
