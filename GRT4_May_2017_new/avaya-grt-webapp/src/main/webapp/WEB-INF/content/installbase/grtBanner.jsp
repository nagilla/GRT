<%
String iBaseStatus = "";
String subStatus = "";
String SR = "";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!-- start registration summary -->
<div class="registration-site-summary collapse-box">
  <h2 class="collapse-box-header active">Registration Site Summary <a href="#"><span></span></a></h2>
  <div class="data collapse-box-container">
  			<c:set var="onlyFirstColumn" value="${actionForm.firstColumnOnly}"/>
			<c:set var="activeSRColumn" value="${actionForm.actveSRColumnOnly}"/>
			<c:set var="srLabel" value="${actionForm.bannerSrLabel}"/>
			<c:set var="eqmFlag" value="${actionForm.equipmentMoveOnly}"/>
			<c:set var="recValFlag" value="${actionForm.recordValidationOnly}"/>
			<c:set var="TOBflag" value="${actionForm.technicalRegistrationOnly}"/>
    <div class="col">
     <p><span class="title">Registration Name: </span><span>${ fn:escapeXml(actionForm.registrationIdentifier) }</span></p>
      <p><span class="title">Registration ID: </span><span>${ fn:escapeXml(actionForm.registrationId) }</span></p>
		<c:if test="${ actionForm.equipmentMoveOnly == false }">
      	<p><span class="title">Sold To: </span><span id="bannerSoldTo">${ fn:escapeXml(actionForm.soldToId) }</span></p>
      </c:if>
      <p><span class="title">Customer Name: </span><span>${ fn:escapeXml(actionForm.company) }</span></p>
    </div>
    <%
    Boolean TOBflag= (Boolean) pageContext.getAttribute("TOBflag");
     Boolean recValFlag = (Boolean) pageContext.getAttribute("recValFlag");
    Boolean eqmFlag = (Boolean) pageContext.getAttribute("eqmFlag");
    Boolean fcOnly = (Boolean) pageContext.getAttribute("onlyFirstColumn");
    Boolean eqrOnly = (Boolean) pageContext.getAttribute("activeSRColumn");
    String avayaAssociate =  (String )request.getSession().getAttribute(com.grt.util.GRTConstants.IS_AVAYA_ASSOCIATE);
    String srLabl = (String) pageContext.getAttribute("srLabel");
    %>
    <div class="col">
     		 <%	 if( fcOnly==null || eqmFlag ){
					 if(!com.grt.util.GRTConstants.FALSE.equals(srLabl)){
			  %>
    		 	  <p><span class="title">SR#: </span><span> ${actionForm.bannerSrNumber}</span></p>
      	   	 <%
				} }
			 %>
			 
			 <%			             	
			  if( fcOnly==null || eqmFlag || recValFlag || TOBflag){
			 %>	
     			 <p><span class="title">Status: </span><span> ${actionForm.bannerStatus}</span></p>
     			 <p><span class="title">Sub-Status: </span><span> ${actionForm.bannerSubStatus}</span></p>
     		<%}%>	
     		<%				             	
			 if(eqrOnly || eqmFlag){
			%>
     		 <p><span class="title">Active Contract SR#: </span><span> ${actionForm.bannerActiveContractSrNumber}</span></p>
     		<%}%>
    </div>
    <c:if test="${ actionForm.equipmentMoveOnly }">
	    <div class="col">
	      <p><span class="title">FROM Sold To/Functional Location: </span><span>${ fn:escapeXml(actionForm.eqmFromSoldTo) }</span></p>
	      <p><span class="title">TO Sold To/Functional Location: </span><span>${ fn:escapeXml(actionForm.eqmToSoldTo) }</span></p>
	    </div>
    </c:if>
    <div class="col" style="word-break: break-all;">
    	<%				             	
           if((fcOnly==null && !com.grt.util.GRTConstants.FALSE.equals(srLabl))||eqmFlag || recValFlag || TOBflag){
		%>
    	  <p><span class="title">Submitted Date: </span><span>${actionForm.bannerSubmittedDate}</span></p>
	      <p><span class="title">Completed Date: </span><span>${actionForm.bannerCompletedDate}</span></p>
	    <%}%>
	      <p><span class="title">Registration Notes: </span> <span>${ fn:escapeXml(actionForm.registrationNotes) }</span></p>
    </div>
  </div>
  
  
</div>
<!-- end registration summary -->
