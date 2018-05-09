<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- <%-- <%@taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%> --%>  -->

<%@ page import="com.grt.util.GRTConstants"%>

<%@ include file="/includes/context.jsp" %>

<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/views/home.css" />"/>
<%-- <link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/common-flow/registration-home.css" />"/> --%>

 <script type="text/javascript" src="<c:url context="${ _path }"  value="/scripts/common-flow/grt.registrationhome.view.js" />"></script> 

<style>
/* #toggleText div.gb {padding-top:0;} */
</style>


<!-- Start Page -->
            <div class="home-page">
                <table>
                    <thead></thead>
                    <tfoot></tfoot>
                    <tbody>
                       <%--  <tr>
                                <h5>Recent Activity</h5>
                                <c:forEach items="${ graphImages }" var="graphs">
									<td colspan="3"><div class="graphImgDiv"><img src="${graphs.value}" alt="graphs"/></div></td>
								</c:forEach> 
                        </tr> --%>
                        <tr>
                            <td>
                            
                            <% pageContext.setAttribute("time",new Date().getTime()); %>
                            <div class="nav-col-left">
	                                <h5 class="heading1">Create A New Registration</h5>
	                                <c:set var="role" value="${actionForm.userRole}"/>
	                                <ul>
	                                    <li>
	                                       <a class="button flat" href="<c:url context="${ _path }" value="/endtoend/endtoendhome.action"><c:param name="reqid" value="${time}"/></c:url>">End to End Registration</a>
	                                    </li>
	                                    <%				             	
											String userRole = (String) pageContext.getAttribute("role");
											if (! com.grt.util.GRTConstants.DIRECT_CUSTOMER.equalsIgnoreCase(userRole)) {
										%>
											<li>
												<a class="button flat" href="<c:url context="${ _path }" value="/installbase/installbasehome.action" ><c:param name="reqid" value="${time}"/></c:url>">Install Base Creation Only</a>
				 							</li>
										<%}%> 
											<li ><a class="button flat" href="<c:url context="${ _path }" value="/technicalonboarding/technicalRegistrationOnly.action"><c:param name="reqid" value="${time}"/></c:url>">Technical Onboarding Only</a></li>
											<li ><a class="button flat" href="<c:url context="${ _path }" value="/recordvalidation/recordValidationOnly.action" ><c:param name="reqid" value="${time}"/></c:url>">Record Validation Only</a></li>
											<li ><a class="button flat" href="<c:url context="${ _path }" value="/equipmentmove/equipmentMoveOnly.action" ><c:param name="reqid" value="${time}"/></c:url>">Equipment/Site Move Only</a></li>
										    <li ><a class="button flat" href="<c:url context="${ _path }" value="/salmigration/salMigrationOnly.action" ><c:param name="reqid" value="${time}"/></c:url>">SAL Migration Only</a></li>
											
	                                </ul>
                                </div>
                            	<div class="nav-col-right">
                                <h5 class="heading1">Registration Management</h5>
                                <ul>
                                    <li>
                                        <a class="button flat" href="<c:url context="${ _path }" value="/technicalregsitration/registrationList.action" ><c:param name="reqid" value="${time}"/></c:url>">Update Existing Registration</a>
                                    </li>
                                   <%
										String rssOrEpnServeyUrl = (String)request.getSession().getAttribute(com.grt.util.GRTConstants.GRT_RSS_OR_EPN_SERVEY_URL_FOR_UI);
										if (rssOrEpnServeyUrl != null) {
									%>
									<li>
										<a class="button flat" target="_blank"  href="<%=rssOrEpnServeyUrl%>" >CM Main/Remote Survey (EPN Survey)</a>
									</li>
									<% } %>
                                    <% 
										String megaUser = (String)request.getAttribute(GRTConstants.GRT_MEGA_USER);
										if(megaUser != null && megaUser.equals("1")){ %>
										<li><a class="button flat" href="<c:url context="${ _path }" value="/administrator/viewAnnouncements.action" ><c:param name="reqid" value="${time}"/></c:url>">Administrator</a></li>
									<% } %>
									<li><a class="button flat" href="<c:url context="${ _path }" value="/account/accountUpdatePageLoad.action" ><c:param name="reqid" value="${time}"/></c:url>">Account Update</a></li>
									<li><a class="button flat" href="<c:url context="${ _path }" value="/installbase/viewInstallBaseOnly.action" ><c:param name="reqid" value="${time}"/></c:url>">View Install Base</a></li>
                                </ul>
                               
                                	<%				             	
										com.avaya.grt.web.security.CSSPortalUser user = (com.avaya.grt.web.security.CSSPortalUser) (request.getSession().getAttribute("cssUserProfile"));
										if (user != null && ( user.getUserType().equalsIgnoreCase("A") || user.getUserType().equalsIgnoreCase("B") )){
									 %>
									  <h5 class="heading1 ipossHead">IP Office Registrations ONLY</h5>
									 <ul>
										<li><a class="button flat" href="<c:url context="${ _path }" value="/account/accountCreation.action" ><c:param name="reqid" value="${time}"/></c:url>">Account Creation</a></li>
										<li><a class="button flat" href="<c:url context="${ _path }" value="/ipoffice/ipoRegistration.action" ><c:param name="reqid" value="${time}"/></c:url>">IP Office Equipment Registration</a></li>
										<li><a class="button flat" href="<c:url context="${ _path }" value="/token/tokenRedemption.action" ><c:param name="reqid" value="${time}"/></c:url>">IPOSS Token Redemption</a></li>
									 </ul>
									<% }%>
                           </div>
                            </td>
                            <td>
                            	<div class="collapse-box">
                                    <!-- Header for Section -->
                                    <p class="accordion-header collapse-box-header">Pending Registrations <i class="fa fa-minus"></i></p>
                                    <!-- Announcements Section Only -->
                                    <div class="collapse-box-container" style="font-weight: normal;">
	                                          <c:forEach items="${ graphImages }" var="graphs">
													<div><p><img src="${graphs.value}" alt="graphs"/></p>	</div>
											  </c:forEach> 
                                    </div>
                                    <!-- end Annoucements Section Only -->
                                 </div>   
                            
                                <div class="collapse-box">
                                    <!-- Header for Section -->
                                    <p class="accordion-header collapse-box-header">Announcements <i class="fa fa-minus"></i></p>
                                    <!-- Announcements Section Only -->
                                    <div class="collapse-box-container" style="font-weight: normal;">
                                    	
	                                        <c:forEach items="${ actionForm.alertsList }" var="container" varStatus="rowCounter">
	                                         <div class="annoucement">
												<p>
													${container.message} 
												</p>	
											 </div>	
											</c:forEach>
										
                                    </div>
                                    <!-- end Annoucements Section Only -->
                                 </div>   
                                 <!-- all other sections -->
                                 <div class="collapse-box">   
                                 	<p class="accordion-header collapse-box-header">Training <i class="fa fa-minus"></i></p>
                                    <div class="collapse-box-container">
                                        <ul class="">
                                            <li>
                                                <a target="_blank" href="https://support.avaya.com/registration">GRT training, videos and documentation</a>
                                            </li>
                                            <li>
                                                <a target="_blank" href="https://support.avaya.com/ext/index?page=content&id=FAQ101925">GRT Frequently Asked Questions</a>
                                            </li>
                                            
                                            <li>
                                            <a target="_blank" href=" https://support.avaya.com/ext/index?page=content&id=FAQ106254">GRT 101 Training</a>
                                            </li>
                                        </ul>
                                    </div>
                                  </div>
                                  <div class="collapse-box">   
                                    <p class="accordion-header collapse-box-header">Help and Policies <i class="fa fa-minus"></i></p>
                                    <div class="collapse-box-container">
                                        <ul class="">
                                            <li>
                                                <a target="_blank" href="https://support.avaya.com/ext/index?page=content&id=FAQ105904">How to get help with GRT questions</a>
                                            </li>
                                            <li>
                                                <a target="_blank" href="https://support.avaya.com/ext/index?page=content&id=FAQ101876">Technical Onboarding scenarios not supported in GRT</a>
                                            </li>
                                        </ul>
                                    </div>
                                  </div>
                                  <div class="collapse-box">   
                                  	<p class="accordion-header collapse-box-header">Tools and Reports <i class="fa fa-minus"></i></p>
                                    <div class="collapse-box-container">
                                        <ul class="">
                                            <li>
                                                <a target="_blank" href="https://avaya.uservoice.com/forums/190607-global-registration-tool-grt-">Provide comments and suggestions to Avaya</a>
                                            </li>
                                            <li>
                                               <a target="_blank" href="https://acsbi.avaya.com">ACSBI Reporting</a>
                                            </li>
                                        </ul>
                                    </div>
                                 	 </div>
                                    <!-- end all other sections -->
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        <!-- End Page -->
