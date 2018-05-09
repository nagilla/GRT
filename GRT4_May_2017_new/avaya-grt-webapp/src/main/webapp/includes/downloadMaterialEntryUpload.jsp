<%@page import="java.net.URLDecoder"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String filename = "MaterialEntryUpload.xls";
String filepath = request.getContextPath()+"/others/";
String fullPath = filepath+filename;

String fileName = URLDecoder.decode(fullPath, "ISO8859_1");//Change if required
response.setContentType("application/x-msdownload");            
response.setHeader("Content-disposition", "attachment; filename="+ fileName);

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Downloading Material Entry Template</title>
</head>
<body>
	<table width="100%">
		<tr>
			<td colspan="2" align="center"><h1 class="osm_pageTitle">Downloading Material Entry Template</h1></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
	</table>
<%-- <script>
	document.location = '<%=fullPath %>';
</script> --%>
</body>
</html>

