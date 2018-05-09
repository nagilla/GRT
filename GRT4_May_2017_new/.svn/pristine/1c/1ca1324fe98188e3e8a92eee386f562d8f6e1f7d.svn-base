<%@page import="org.apache.commons.lang.StringUtils"%>
<div class="data">
	<div class="row">
		<div class="col">
			<p>
				<span class="title">SoldToId:	</span>
				<input type="text" name="actionForm.soldTo" id="soldToForAddToList" value="${ actionForm.soldTo }"/>
			</p>
		</div>
		<div class="col">
			<p>
				<span class="title">Gateway SEID:	</span>
				<input type="text" name="actionForm.salSeId" id="salSeIdForAddToList" value="${ actionForm.salSeId }"/>
				<input type="button" class="button small gray" value="Add" onclick="return addToListSALGW_Action();">
			</p>
		</div>
		
	</div>
	<div class="row">
		<p>
		<span><span class="title">SAL Gateway Added:</span>&nbsp;<font color="red" >
		<c:set var="errMsgCount" value="${actionForm.count}"/>
		<%
		
		String errMsgCount ="";
		if(errMsgCount.length()>1)
		{
			errMsgCount =""+pageContext.getAttribute("errMsgCount");
			
		}
		
		 %>
		${errMsgCount}
		
		</font></span>
		<span><font color="red">
			<!-- Remove error Code from error message -->
			<c:set var="errMsg" value="${actionForm.errorMessage}"/>
			<%
				String msg = "";
				String errmsg = "" + pageContext.getAttribute("errMsg");
				if(errmsg != null) {
					String[] parts = errmsg.split("###");
					if(parts.length > 1) {
						msg = parts[1];
					} else {
						msg = errmsg;
					}
				} else {
					msg = errmsg;
				}
				if(errMsgCount.length()<4)
				{
					msg = "";
				}
				
				pageContext.setAttribute("errMsg", msg);
				
			%>
			${errMsg}			
		</font></span>
		</p>
		</div>
</div>
