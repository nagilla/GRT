<%@page import="com.avaya.grt.web.action.AvayaCommonUtilAction"%>
<%

String redirectUrl = session.getAttribute("avayaSearchRedirectUrl") != null ? (String)session.getAttribute("avayaSearchRedirectUrl") : "";

if(redirectUrl != null && !redirectUrl.equals("")) {
	response.sendRedirect(redirectUrl);
} else {
	out.println("Redirect URL is not found in session...");
}
%>