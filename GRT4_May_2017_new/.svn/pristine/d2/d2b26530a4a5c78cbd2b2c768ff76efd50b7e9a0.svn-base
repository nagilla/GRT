<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/includes/context.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<head>

<!--link href="<c:url context="${ _path }"  value="/styles/error.css" />"
	rel="stylesheet" type="text/css"-->
<script type="text/javascript">
	/* spTracking.channel = "error-page";
	spTracking.errorPage404Tracking(); */
	$(document).ready(function(){
		var excepMsg = $("#expStackTrace").val();
		if( excepMsg!=null ){
			var res = excepMsg.substr(0, 500);
		//	$(".expDetail").text(res);
		}
	});
</script>
</head>
<div class="grid_12 error-page">
	<!-- As per Adam's suggestion 723 -->
	<h2>We're sorry but something went wrong with GRT, please try to submit your registration again later.</h2>
	<%-- <c:if test="true">
		these are to help with development, and should be removed for release
		<p style="margin-top: 17px;">Here's my best guess as to what happened.</p>
		<div class="stack-trace">
			<p >${exception}</p>
			 <p>${exceptionStack}</p>
		</div>
	</c:if> --%>
	<!-- Commented Exception Details as suggested by Adam -->
	<%--  <h4 style="font-family: monospace;  font-size: large; margin-top: 17px;">Exception Name: <s:property value="exception" /> </h4>
 
	 <h4 style="font-family: monospace;  font-size: large; margin-top: 17px;">Exception Details: <s:property value="exceptionStack" /></h4> 
	 Exception Details: <div class="expDetail" style="font-family: monospace;  font-size: large; margin-top: 17px;"></div>
	  --%>
	<input type="hidden" id="expStackTrace" value="${exceptionStack}" />
</div>