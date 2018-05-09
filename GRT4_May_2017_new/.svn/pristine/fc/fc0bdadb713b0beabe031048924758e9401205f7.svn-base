<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%-- <div id="dialog-underlay" style="opacity:0.2; filter:alpha(opacity=20); position: absolute; display: none; width: 100%; height: 100%; top: 0; right: 0; bottom: 0; left: 0;"></div>

<div id="popup-window" class="dialog" style="left: -1000em;">
	<div class="dialog-container">
		<div class="gb-tc"></div>
		<div class="gb">
			<div class="dialog-title-bar">
				<h2><span id="popup-window-title">Waiting...</span></h2>
				<div class="dialog-close-button" style="cursor: pointer;" onclick="grtcommonPopup.close();"></div>
			</div>
			<div id="popup-window-content" class="dialog-content">
			</div>
			<div class="fixer"></div>
		</div>
		<div class="gb-bc"></div>

	</div>
</div> --%>

<script type="text/javascript">
function Popup(){
    this.title;
    this.msg;
    this.timeout;
    this.timeout_callback_func;
    this.close_callback_func;
	//config
	//{title: title, msg: msg, timeout:timeout, timeout_callback_func:timeout_callback_func, close_callback_func:close_callback_func}
    this.config = function(config) {
    	if(!config) {
    		return;
    	}
    	this.title = config.title;
    	this.msg = config.msg;
    	this.timeout = config.timeout;
    	this.timeout_callback_func = config.timeout_callback_func;
    	this.close_callback_func = config.close_callback_func;
    };
    this.show = function(){
		document.getElementById("dialog-underlay").style.display = "block";
		if(this.title) {
			document.getElementById("popup-window-title").innerHTML = this.title;
		}
		document.getElementById("popup-window-content").innerHTML = this.msg;

		var elePopup = document.getElementById("popup-window");
		var scroll = _getScroll();
		var divH = _getLength(elePopup.style.height, scroll.h);
		var divW = _getLength(elePopup.style.width, scroll.w);
		elePopup.style.top = (scroll.t + (scroll.ch - divH)/2) + "px";
		//elePopup.style.left = (scroll.l + (scroll.cw - divW)/2) + "px";
		elePopup.style.left = "auto";

		if(this.timeout_callback_func) {
			if(!this.timeout) {
				// Set time out for 5s
				this.timeout = 5000;
			}
			setTimeout(this.timeout_callback_func, this.timeout);
		}
	};
    this.close = function() {
		document.getElementById("dialog-underlay").style.display = "none";
		document.getElementById("popup-window").style.left = "-1000em";
		if(this.close_callback_func) {
			this.close_callback_func();
			this.close_callback_func = function() {
				//convertAlertToModelPopUp("close_callback_func not configured");
				convertAlertToModelPopUp($("#closeFunNotConfiguredErrorCode").val(), $("#icloseFunNotConfigured").val()); 
			};
		}
    };

	function _getScroll() {
		var t, l, w, cw, h, ch;
		if (document.documentElement && document.documentElement.scrollTop) {
			t = document.documentElement.scrollTop;
			l = document.documentElement.scrollLeft;
			w = document.documentElement.scrollWidth;
			cw = document.documentElement.clientWidth;
			h = document.documentElement.scrollHeight;
			ch = document.documentElement.clientHeight;
		} else if (document.body && document.body.scrollTop) {
			t = document.body.scrollTop;
			l = document.body.scrollLeft;
			w = document.body.scrollWidth;
			cw = document.body.clientWidth;
			h = document.body.scrollHeight;
			ch = document.body.clientHeight;
		}
		if( typeof( window.innerWidth ) == 'number' ) {
    		//Non-IE
			cw = window.innerWidth;
			ch = window.innerHeight;
		}

		return {t:t, l:l, w:w, cw:cw, h:h, ch:ch};
	};

	function _getLength(divLen, windowLen) {
		if(divLen.indexOf("px") != -1) {
			return divLen.substring(0, divLen.length-2);
		}
		else if(divLen.indexOf("%") != -1) {
			return divLen.substring(0, divLen.length-1) * windowLen / 100;
		}
		else if(divLen.length == 0){
			return 0;
		}
		else {
		 	return divLen;
		}
	};

}

var grtcommonPopup = new Popup();

</script>
		<div>
		<input type="hidden" id="closeFunNotConfigured" value="${grtConfig.closeFunNotConfigured}" />
		<!-- end error messages -->
		
		<!-- Error Codes -->
		<input type="hidden" id="closeFunNotConfiguredErrorCode" value="${grtConfig.ewiMessageCodeMap['closeFunNotConfigured']}" />
		</div>
		
