/**
 * webChatUI_Custom.js<br>
 * This is Kiko custom support functions
 * Copyright 2015 Avaya Inc. All Rights Reserved.<br>
 * Usage of this source is bound to the terms described in licences/License.txt<br>
 * <br>
 * Avaya - Confidential & Proprietary.<br>
 * Use pursuant to your signed agreement or Avaya Policy<br>
 * <br>
 * This file contains UI code. By default, it uses jQuery methods; replace with your own code.
 */

function sendAutoText(msg) {
	autoMessage = {'apiVersion' : '1.0','type' : 'request','body' : {'method' : 'newMessage','message' : msg}};
	chatSocket.sendMessage(autoMessage);
}





openPopupHelper = function(x) {
	console.log('Opening popup , sender input param = ' + x)
	switch (x) {
		case 1:
		console.log('input 1');
			window.open("https://support.avaya.com/ext/index?page=content&id=FAQ101925","popup", "toolbar=0,scrollbars=1,directory=0,location=0,statusbar=0,menubar=0,resizable=1,width=835,height=598,left = 270,top = 70");
			break;
		case 2:
		console.log('input 2');
			
			autoMessage = {'apiVersion' : '1.0','type' : 'request','body' : {'method' : 'newMessage','message' : '_grtshowMe_RegProcess_And_Tools'}};
			
			chatSocket.sendMessage(autoMessage);
			setTimeout(function() {
			var objDiv = document.getElementById("messages");
			objDiv.scrollTop = objDiv.scrollHeight;
			},1000);
			break;
		case 3:
		console.log('input 3');
			window.open("https://grt.avaya.com/grt/account/accountCreation.action?reqid=1482285958002","popup", "toolbar=0,scrollbars=1,directory=0,location=0,statusbar=0,menubar=0,resizable=1,width=835,height=598,left = 270,top = 70");
			break;

		case 4:
		console.log('input 4');
			var myDate = new Date();
			$('#messages').append('<p class="response" style="color: rgb(255, 255, 255);">Ava ( '+getTimeAMPM(myDate)+' ): If you would like, you can wait to speak with a live agent on Ask AVA at a later time. For more immediate help, please search the full list of <a class="noagentsavail" href="#" onclick="openPopupHelper(1)"> Frequently asked</a> questions (FAQs) or open a ticket with Avaya IT department using the directions in this <a class="noagentsavail" href="https://support.avaya.com/ext/index?page=content&id=FAQ105904" target="_blank">article</a>.</p>');
			setTimeout(function() {
			var objDiv = document.getElementById("messages");
			objDiv.scrollTop = objDiv.scrollHeight;
			},1000);
			// send no agents available to chatbot conversation and data reports
                if (global_chatbot_token != "notoken" && global_chatbot_token.length > 7){
					sendToConvDataRepV2('con_no_agents_avail', 'no agents available');
				}
				//
			break;
		default:
		break;
		
	}
	
}

getMeOceanaLiveChatAgent = function() {
	var autoMessage = {
                'apiVersion' : '1.0',
               'type' : 'request',
               'body' : {
                   'method' : 'newMessage',
                   'message' : '_needGRTLiveAgent'
              }
            };
			avayaGlobal.log.info('sendingAutoText - _needGRTLiveAgent');
			chatSocket.sendMessage(autoMessage);
			
			var objDiv = document.getElementById("messages");
			objDiv.scrollTop = objDiv.scrollHeight;
	
}
// 04-10-2017
var  visitor_engaged = {customer_escalated:false, agent_engaged: false, clickedCloseBtn: false };

var global_chatbot_token = "notoken";
grtWelcomeMessage = function() {
	
			var autoMessage = {
                'apiVersion' : '1.0',
               'type' : 'request',
               'body' : {
                   'method' : 'newMessage',
                   'message' : '_grtWelcome'
              }
            };
			avayaGlobal.log.info('sendingAutoText - _grtWelcome');
			chatSocket.sendMessage(autoMessage);
			// get token, send data to chabot reps
			// orig setTimeout(function() {
			// orig getTokenServerSide();
			// orig },3000);
			//
			// kiko new here get cbot token, send data to chatbot reps
			var convAndDataReportEmail = (sessionStorage.getItem('email') != null && sessionStorage.getItem('email').length > 0 ) ? sessionStorage.getItem('email') : "empty_email";
			getTokenServerSideV2();
			setTimeout(function() {
				avayaGlobal.log.debug('Chatbot sess. token: ' + global_chatbot_token);
				 if (global_chatbot_token != "notoken" && global_chatbot_token.length > 7){
				sendToConvDataRepV2('con_visitor_email', convAndDataReportEmail);
				 }
				
			}, 3000);
			
			
			
}


loadWorkflowHelpers = function() {
				
			$('#messages').append('<p class="response"><input type="button" class="button ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" value="See GRT FAQs list" onclick="openPopupHelper(1);"/></p>');
			$('#messages').append('<p class="response"><input type="button" class="button ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" value="Show me GRT Registration Process and Tool" onclick="openPopupHelper(2);"/></p>');
			$('#messages').append('<p class="response"><input type="button" class="button ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" value="Go to Account Creation" onclick="openPopupHelper(3);"/></p>');
			//autoMessage = {'apiVersion' : '1.0','type' : 'request','body' : {'method' : 'newMessage','message' : '_grthelpkeywords'}};
			//chatSocket.sendMessage(autoMessage);
			setTimeout(function() {
			var objDiv = document.getElementById("messages");
			objDiv.scrollTop = objDiv.scrollHeight;
			},2000);
			
}




mutationObserverTest = function() {
    var changesCaptured = [];
    avayaGlobal.log.info('*** MutationObserver started');
    var alertSent = false;
	var genericReceived = false;
    var MutationObserver = window.MutationObserver || window.WebKitMutationObserver || window.MozMutationObserver;
	
    var list = document.querySelector('#messages');
	
    var observer = new MutationObserver(function (mutations) {
        mutations.forEach(function (mutation) {
            if (mutation.type === 'childList') {
                var list_values = [].slice.call(list.children)
                            .map(function (node) {

                                avayaGlobal.log.info('RAW node.innerHTML: ' + node.innerHTML);

								
//								avayaGlobal.log.info('Change detected on messages, Which class (attribute) ? : ' + node.hasAttributes());
                                                                avayaGlobal.log.info('Change detected on messages, with class: ' + node.getAttribute("class") );
                                                                // 04-10-2017
                                                                // escalated interaction report column for chatbot - on demand
							   		if (  !visitor_engaged.agent_engaged && !visitor_engaged.customer_escalated && node.hasAttributes() && node.getAttribute("class") == "response" && node.innerHTML.indexOf('Getting GRT live chat agent') > -1 ) {
										if (global_chatbot_token != "notoken" && global_chatbot_token.length > 7){
										sendToConvDataRepV2('con_visitor_escalated', 'yes');
										avayaGlobal.log.info('W_con_visitor_escalated_Yes');
										}
									visitor_engaged.customer_escalated = true;
									avayaGlobal.log.info('visitor_engaged.customer_escalated true');
							   		}
							    	// END: escalated interaction report column for chatbot
								
								 // escalated interaction report column for chatbot - chatbot by itself
							   		if ( !visitor_engaged.agent_engaged && !visitor_engaged.customer_escalated && node.hasAttributes() && node.getAttribute("class") == "response" && node.innerHTML.indexOf('Escalating to a live agent') > -1 ) {
										if (global_chatbot_token != "notoken" && global_chatbot_token.length > 7){
											avayaGlobal.log.info('W_con_visitor_chatbot_escalated_Yes');
											sendToConvDataRepV2('con_visitor_chatbot_escalated', 'yes');
										}
										visitor_engaged.customer_escalated = true;
										avayaGlobal.log.info('visitor_engaged.customer_escalated true');
							   		}
							    // END 04-10-2017: escalated interaction report column for chatbot  
							    // 

								// removing 'An agent has joined the chat' for chatbat when session starts
								if ( node.hasAttributes() && node.getAttribute("class") == "response" && node.innerHTML.indexOf('An agent has joined the chat') > -1) {
								if ($('#messages p:contains("An agent has joined the chat")').prev().length > 0 && $('#messages p:contains("An agent has joined the chat")').prev().text() == "Login request received and approved") {
                                                              		  node.innerHTML = '';
                                                              	  }
                                }
                                          
                                                                // removing Agent has left the chat when chatbot exit - this confuse the customer
                                                                if ( node.hasAttributes() && node.getAttribute("class") == "response" && node.innerHTML.indexOf('Agent has left the chat') > -1) {
                                                                        node.innerHTML = '';
									clearInterval(idleInterval);
                                                                                                                                
                                                                     }

								if (node.hasAttributes() && node.getAttribute("class") == "sent") {
									node.setAttribute("class", 'sent fromcustcustbubble');
									
									// display sso data on demand 
									if (node.innerHTML.indexOf('_grtvars') > -1) {
										var allSessionStorageHTML = '<ol>';
										for (var i = 0; i < sessionStorage.length; i++){
											
											avayaGlobal.log.info('SESS STO: ' + sessionStorage.key(i) + "|" + sessionStorage.getItem(sessionStorage.key(i) ));
											allSessionStorageHTML += '<li>Key: '+sessionStorage.key(i)+' Value: '+sessionStorage.getItem(sessionStorage.key(i))+'</li>';
											
											
										}
										var ssoVarsPopup = window.open("", "SSO Data passed", "toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, width=780, height=200, top="+(screen.height-400)+", left="+(screen.width-840)); 
										ssoVarsPopup.document.body.innerHTML = allSessionStorageHTML;
										node.innerHTML = node.innerHTML.replace('_grtvars','displaying sso data');
										
										
									}
								}
							if (node.hasAttributes() && node.getAttribute("class") == "response") {
									
								if (node.innerHTML.indexOf('Ava') > -1) {
									avayaGlobal.log.info('incoming AVA message...');
									$("p.response:contains('Ava')").css("color", "white");
								}
								
							
								if (node.innerHTML.indexOf('how can we assist you today') > -1) {
										node.innerHTML = '<p>(^_^)</p>';
										var autoMessage = {'apiVersion' : '1.0','type' : 'request','body' : {'method' : 'newMessage','message' : '_grt_hello_welcome' } };
										chatSocket.sendMessage(autoMessage);
										
									
									}
									if (node.innerHTML.indexOf('ok...') > -1) {
										node.innerHTML = '<p>(^_^)</p>';
										var autoMessage = {'apiVersion' : '1.0','type' : 'request','body' : {'method' : 'newMessage','message' : '_needGRTLiveAgent' } };
										chatSocket.sendMessage(autoMessage);
										
									
									}
									
								if (node.innerHTML.indexOf('To further assist you') > -1 || node.innerHTML.indexOf('helpcenter') > -1 ) {
									
									node.innerHTML = '<p>(^_^)</p>';
									console.log('Offering generic response....');
									genericReceived = true;
									 var autoMessage = {'apiVersion' : '1.0','type' : 'request','body' : {'method' : 'newMessage','message' : '_grt_generic_response' } };
										avayaGlobal.log.info('sendingAutoText - _grt_generic_response');
										chatSocket.sendMessage(autoMessage);
										
										
								} else {
								
														avayaGlobal.log.info('checking for incoming JS Object...');
														var temp_nodeInnerHTML = node.innerHTML.split("):").pop();
														if (temp_nodeInnerHTML.length > 0) {
														
														}
														if (temp_nodeInnerHTML.charAt(0) == "{" || temp_nodeInnerHTML.charAt(1) == "{") {
															
															
															 var objVar = temp_nodeInnerHTML;
															
															 var receivedObjEval = eval('(' + objVar + ')');
															var myDate = new Date();
															
															var newHTML = '<p>Ava ( '+getTimeAMPM(myDate)+' ) :</p><br><ul class="fromcustbubble">';
															//var newHTML = '<ul>';

															var sectionTitleSet = false;
														$.each(receivedObjEval, function(key, value) {
															if (typeof(value) == 'object') {
																	
																	if (!sectionTitleSet) {
																		if (value.description) {
																			newHTML += '<li>'+value.description+'<ol>';
																		} else {
																				newHTML += '<li><ol>';
																		}
																		sectionTitleSet = true;
																	}

																	
																	if (value.type == "link") {
																			if (value.linktextlocation == 'pre') {
																			newHTML += '<li><a href="https://'+value.url+'" onclick="sendAutoText(\'' + 'Clicked: ' + 'url: https://' + value.url + '\')" target="_blank">'+value.pretext+'</a>'+value.middletext + value.posttext+'</li>';           
																			}
																			if (value.linktextlocation == 'middle') {
																			newHTML += '<li>'+value.pretext+'<a href="https://'+value.url+'" onclick="sendAutoText(\'' + 'Clicked: ' + 'url: https://' + value.url + '\')" target="_blank">'+value.middletext+'</a>'+value.posttext+'</li>';
																			}
																			if (value.linktextlocation == 'post') {
																			newHTML += '<li>'+value.pretext+value.middletext+'<a href="https://'+value.url+'" onclick="sendAutoText(\'' + 'Clicked: ' + 'url: https://' + value.url + '\')" target="_blank">'+value.posttext+'</a></li>';
																		     }
																		}

																	if (value.type == "function") {
																		//console.log('MASSAGE FUNCTION with ' + value.url + "|" + value.onclickFunctionName  + "|" + value.pretext + "|" + value.posttext + "|" + value.linktext );
																		//newHTML += '<li>'+value.pretext+'<a href="'+value.url+'" onclick="'+value.onclickFunctionName+'">'+value.linktext+'</a>'+value.posttext+'</li>';
																		if (value.linktextlocation == 'pre') {
																			newHTML += '<li><a href="'+value.url+'" onclick="'+value.onclickFunctionName+'">'+value.pretext+'</a>'+value.middletext+value.posttext+'</li>';	
																		}
																		 if (value.linktextlocation == 'middle') {
																			newHTML += '<li>'+value.pretext+'<a href="'+value.url+'" onclick="'+value.onclickFunctionName+'">'+value.middletext+'</a>'+value.posttext+'</li>';
																		}
																		 if (value.linktextlocation == 'post') {
																			newHTML += '<li>'+value.pretext + value.middletext+'<a href="'+value.url+'" onclick="'+value.onclickFunctionName+'">'+value.posttext+'</a></li>';
																		}
																	
																	}
																	if (value.type == "sendinput") {
																		var sendInputWithQ = "'"+value.inputString.trim()+"'";
																		//console.log('MASSAGE SENDINPUT with: '+ value.url + "|" + sendInputWithQ + "|" + value.pretext + "|" + value.posttext + "|" + value.linktext );
																		//newHTML += '<li>'+value.pretext+value.posttext+'<a href="'+value.url+'" onclick="sendAutoText('+sendInputWithQ+')">'+value.linktext+'</a>'+'</li>';
																		if (value.linktextlocation == 'pre') {
																			newHTML += '<li><a href="'+value.url+'" onclick="sendAutoText('+sendInputWithQ+')">'+value.pretext+'</a>'+value.middletext+value.posttext+'</li>';	
																		}
																		 if (value.linktextlocation == 'middle') {
																			newHTML += '<li>'+value.pretext+'<a href="'+value.url+'" onclick="sendAutoText('+sendInputWithQ+')">'+value.middletext+'</a>'+value.posttext+'</li>';
																		}
																		 if (value.linktextlocation == 'post') {
																			newHTML += '<li>'+value.pretext+value.middletext+'<a href="'+value.url+'" onclick="sendAutoText('+sendInputWithQ+')">'+value.posttext+'</a></li>';
																		}
																		
																		
																		
																	}
																	if (value.type == "text") {
																
																		newHTML += '<p>'+value.textString+'</p>';
																	}
																	if (value.type == "textlist") {
																		//console.log('MASSAGE LISTTEXT with: '+ value.listItem1 + '|' + value.listItem2 + '|' + value.listItem3 + '|' + value.listItem4 + '|' + value.listItem5 + '|' + value.listItem6 );
																		if (value.listItem1) 
																			newHTML += '<li>'+value.listItem1+'</li>';
																		if (value.listItem2)
																			newHTML += '<li>'+value.listItem2+'</li>';
																		if (value.listItem3)
																			newHTML += '<li>'+value.listItem3+'</li>';
																		if (value.listItem4)
																			newHTML += '<li>'+value.listItem4+'</li>';
																		if (value.listItem5)
																			newHTML += '<li>'+value.listItem5+'</li>';
																		if (value.listItem6)
																			newHTML += '<li>'+value.listItem1+'</li>';
																		
																	}
																
																
																} else 	{
																	//console.log('Outer 1: KEY: ' + key + ' VALUE: ' + value);
																	}
																	//console.log('Outer 2 : KEY: ' + key + ' VALUE: ' + value);
																	console.log('DONE observer-replace 2');
															});
														console.log('NEWHTML: ' + newHTML);

														newHTML += '</ol></li></ul>';
														node.innerHTML = newHTML;
														

									
									
														}
														
												}
								
								
                                    if ($.inArray("Changed_" + node.innerHTML, changesCaptured) == -1) {
                                        changesCaptured.push("Changed_" + node.innerHTML);
                                        
                                        alertSent = true;
										
                                    } else {
                                        
                                    }
									
                                
		}
						
                           return node.innerHTML;
                            }) // .map func

                                            .filter(function (s) {
                                                if (s === '<br />') {
                                                    return false;
                                                }
                                                else {
                                                    return true;
                                                }
                                            }); // filter
			console.log('DONE observer-replace 7');
            } // if (mutation.type === 'childList')
				console.log('DONE observer-replace 8');
			    fastScroll(1000);
        }); // mutations.forEach(function (mutation)
    }); // var observer = new MutationObserver(function (mutations)

    observer.observe(list, {
        attributes: true,
        childList: true,
        characterData: true
    });


console.log('exit mutation observer function');
console.log('DONE observer-replace 9');
}    // func close

// stop observer
function stopObserver() {
	observer.disconnect();
	
}
// get time as am pm - 12hr format
function getTimeAMPM(date){
	var hours = date.getHours();
  var minutes = date.getMinutes();
  var seconds = date.getSeconds();
  var ampm = hours >= 12 ? 'PM' : 'AM';
  hours = hours % 12;
  hours = hours ? hours : 12; // the hour '0' should be '12'
  minutes = minutes < 10 ? '0'+minutes : minutes;
   seconds = seconds < 10 ? '0'+seconds : seconds;
 // var strTime = hours + ':' + minutes + ' ' + ampm;
   var strTime = hours + ':' + minutes + ':' + seconds + ' ' + ampm;
  return strTime;

}
// smooth scrolling
function smoothScroll(delay) {
	console.log('SmoothScroll in: '+ delay);
	setTimeout(function() {
	$('#messages').get(0).scrollIntoView();
	}, delay);
	
}
// fast scroll
function fastScroll(delay) {
	console.log('Fast Scroll in: '+ delay);
	setTimeout(function() {
	var objDiv = document.getElementById("messages");
	objDiv.scrollTop = objDiv.scrollHeight;
	}, delay);
	//setTimeout(function() {
	

	//$('#messages p:last-child').effect("highlight",{easing: "linear", color:"#686868"},8000)
	//},delay + 500);
	
	
}
// This runs on page load.

// set timeout for mouse clicks or send inputString
// reset timer is mouse click or send input
// if timer expire and no clicks or send input a help message is displayed

var idleTimer = 0;
var idleTimerEnd = 0;
var idleInterval;
$(function() {
	idleInterval = setInterval(innactivityTimerIncrement, 30000); // 1 min
	// zero timer if mouse click on messages or send new input
	$('#messages').click(function(e) {
		idleTimer = 0;
		
	});
	$('#messages').keydown(function(e) {
		idleTimer = 0;
		
	});
	
});

function innactivityTimerIncrement() {
	idleTimer = idleTimer + 1;
	idleTimerEnd = idleTimerEnd + 1;
	if (idleTimer > 2) { // 2 mins 
	autoMessage = {'apiVersion' : '1.0','type' : 'request','body' : {'method' : 'newMessage','message' : '_grtInnactivityHelpers'}};
	 chatSocket.sendMessage(autoMessage);
	
	var audio = new Audio('/grt/audio/live-chat/inactivityAlert.mp3');
	audio.play();
	idleTimer = 0;
	console.log('what is idleInterval?' + idleInterval);
	if (idleTimerEnd > 3) { console.log('clearInterval timer INACTIVITY interval '); clearInterval(idleInterval); }
	}
}

//Code piece shared by Kiko for during cobrowse UAT.
// send chatbot conversation and data report user info
// get chatbot session token
getTokenServerSide = function() {
	var ak = webChat.ak;
	var code_chatbotUser = (sessionStorage.getItem('user') != null && sessionStorage.getItem('user').length > 0 ) ? sessionStorage.getItem('user') : "emptyUser";
	console.log('----> getting chatbot token using ak: ' + ak)
	try {
		        var script = document.createElement('script');
		        script.setAttribute('src', "https://p15.conversive.com/avaya/agentAvailProxy.aspx?f=chatbotGetToken&cb=getTokenSSCB&chatbotid=" + ak  + "&chatbotuser=" + code_chatbotUser +"&");
		        document.getElementsByTagName('head')[0].appendChild(script);
		    } catch (cbotEx) {
		        console.log("Get chatbot token from SS FAILED: " + cbotEx.toString());

    		}

}

// callback for get session token associated with chatbot
getTokenSSCB = function(retData) {
	try {
	var tokenStartPos = retData["chatbottoken"].indexOf('"t":"');
	var tokenEndPos = retData["chatbottoken"].indexOf('"', tokenStartPos+5);
	var cb_token = retData["chatbottoken"].substring(tokenStartPos+5, tokenEndPos);
	console.log("----> TOKEN: " + cb_token);
	}
	catch (chatBotSubsToken) {
		console.log("ERROR on substr token from response: " + chatBotSubsToken.toString());
	}

	var code_chatbotAcct = '4';
	var code_chatbotToken = cb_token;
	var code_chatbotColumnName = "con_visitor_email";
	console.log('----> What is sessionStorage.getItem(email)' + sessionStorage.getItem('email') + " What is sessionStorage.getItem('email').length: " + sessionStorage.getItem('email').length );
	
	var code_chatbotVisitorEmailValue = (sessionStorage.getItem('email') != null && sessionStorage.getItem('email').length > 0 ) ? sessionStorage.getItem('email') : "empty_email";
	


	
	
// send request to add chatbot report data	
	try {
		console.log("----> Trying Chatbot to REP, data EMAIL: "+ code_chatbotVisitorEmailValue);
	        var script = document.createElement('script');
	        script.setAttribute('src', "https://p15.conversive.com/avaya/agentAvailProxy.aspx?f=chatbotRegisterToRep&cb=chatbotToRepCB&chatbottoken=" + code_chatbotToken + "&chatbotaccount="+ code_chatbotAcct + "&chatbotcolname="+code_chatbotColumnName+ "&chatbotcolvalue="+code_chatbotVisitorEmailValue+ "&");
	        document.getElementsByTagName('head')[0].appendChild(script);
	    } catch (chatBotToRepEx) {
	        console.log("ERROR on adding chatbotdata to rep: " + chatBotToRepEx.toString());

			}
}	
	
chatbotToRepCB = function(od) {
	 console.log("----> ChatbotToRep CB returned: " + od["chatbotFromRep"] );
}



// version 2 chatbot conv and data report
	
	
	
getTokenServerSideV2 = function() {
	var ak = webChat.ak;
	var code_chatbotUser = (sessionStorage.getItem('user') != null && sessionStorage.getItem('user').length > 0 ) ? sessionStorage.getItem('user') : "emptyUser";
	console.log('V2 ----> getting chatbot token using ak: ' + ak)
	try {
		        var script = document.createElement('script');
		        script.setAttribute('src', "https://p15.conversive.com/avaya/agentAvailProxy.aspx?f=chatbotGetToken&cb=getTokenSSCBV2&chatbotid=" + ak  + "&chatbotuser=" + code_chatbotUser +"&");
		        document.getElementsByTagName('head')[0].appendChild(script);
		    } catch (cbotEx) {
		        console.log("V2 Get chatbot token from SS FAILED: " + cbotEx.toString());
				
    		}
			

}	




getTokenSSCBV2 = function(retData) {
	try {
	var tokenStartPos = retData["chatbottoken"].indexOf('"t":"');
	var tokenEndPos = retData["chatbottoken"].indexOf('"', tokenStartPos+5);
	var cb_token = retData["chatbottoken"].substring(tokenStartPos+5, tokenEndPos);
	console.log("V2 ----> before returning TTTOKEN: " + cb_token);
	global_chatbot_token =  cb_token;
	
	}
	catch (chatBotSubsToken) {
		console.log("V2 ERROR on substr token from response: " + chatBotSubsToken.toString());
		
		
	}

}

	
	
sendToConvDataRepV2 = function(colname, colvalue) {	
	
	var code_chatbotAcct = '4';
	var code_chatbotColumnName = colname;
	var code_chatbotColValue = colvalue;
	
// send request to add chatbot report data	
	try {
		console.log("V2 ----> Trying Chatbot to REP, data : "+ code_chatbotColValue);
	        var script = document.createElement('script');
	        script.setAttribute('src', "https://p15.conversive.com/avaya/agentAvailProxy.aspx?f=chatbotRegisterToRep&cb=chatbotToRepCBV2&chatbottoken=" + global_chatbot_token + "&chatbotaccount="+ code_chatbotAcct + "&chatbotcolname="+code_chatbotColumnName+ "&chatbotcolvalue="+code_chatbotColValue+ "&");
	        document.getElementsByTagName('head')[0].appendChild(script);
	    } catch (chatBotToRepEx) {
	        console.log("V2 ERROR on adding chatbotdata to rep: " + chatBotToRepEx.toString());

			}
}
//
//	
chatbotToRepCBV2 = function(od) {
	 console.log("----> V2 ChatbotToRep CBV2 returned: " + od["chatbotFromRep"] );
}	
	

	
	
	
// end version 2