<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String filename = "MaterialEntryUpload.xls";
String filepath = request.getContextPath()+"/downloads/";
String fullPath = filepath+filename;
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
<script>
	document.location = '<%=fullPath %>';
</script>
</body>
</html>
