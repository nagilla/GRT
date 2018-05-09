<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.grt.util.GRTConstants"%>
<%@page import="java.util.Map"%>
<%@page import="com.avaya.grt.mappers.Region"%>
<%@page import="com.avaya.grt.web.util.PageCache"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.grt.dto.RegistrationFormBean"%>
<%@ page import="java.util.List"%>
<%@ page import="com.grt.dto.Pager"%>
<%@ page import="java.lang.Double"%>
<%@ page import="com.avaya.grt.web.action.GrtConfig"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ include file="/includes/context.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/views/account-update.css" />"/>
<link rel="stylesheet" type="text/css" href="<c:url context="${ _path }" value="/styles/plugins/jquery.dataTables_themeroller.css" />" />

<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/plugins/jquery.dataTables.min.js" />"></script>
<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/plugins/jquery.dataTables.columnFilter.js" />"></script>
<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/plugins/jquery.datatable.extplugin.js" />"></script>
<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/plugins/dynamicTable.js" />"></script>
<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/account/grt.accountUpdate.js" />"></script>

<script type="text/javascript" src="<c:url context="${ _path }" value="/scripts/account/validatestring.js" />"></script>

<script type="text/javascript">
// December Release Item - State drop showing wrong state in Account Update screen
	/*<c:if test="${actionForm.accountFormBean.contactsFlag eq 'block'}">
		//To Sort the states in alphabetical order
		$(document).ready( function() {
			var my_options = $("#primaryAccountStateSelect option");
			var selected = $("#primaryAccountStateSelect").val(); 

			my_options.sort(function(a,b) {
				if (a.text > b.text) return 1;
				else if (a.text < b.text) return -1;
				else return 0
			})

			$("#primaryAccountStateSelect").empty().append( my_options );
				
		});
	</c:if>	*/
		
	<c:if test="${requestScope.accountUpdated and requestScope.updateResponse ne null }">
	window.onload = function() {
		var url = document.getElementById('cancelAccountCreationAction').href;
		convertAlertToModelPopUpWithRedirect("${requestScope.updateResponse}", url);		
	};
	</c:if>
	<c:if test="${requestScope.accountUpdated eq null and requestScope.updateResponse ne null }">
	window.onload = function() {
		convertAlertToModelPopUpServerSideMsg("${requestScope.updateResponse}");
	};
	</c:if>
	
	<c:if test="${requestScope.accountUpdated eq null and requestScope.updateResponse eq null and requestScope.soldToError ne null }">
	window.onload = function() {
		convertAlertToModelPopUpServerSideMsg("${requestScope.soldToError}");
	};
	</c:if>
	
	<c:if test="${requestScope.updateerror ne null}">
	window.onload = function() {
		convertAlertToModelPopUpServerSideMsg("${requestScope.updateerror}");
	};
						
	</c:if>
	
	var countryMap = new Object();
	<%for (Map.Entry<String, Map<Long, Region>> countryMap : PageCache.regionMap.entrySet()) {
				out.print("var regions = new Array();");
				if (!countryMap.getKey().equalsIgnoreCase("US")) {
					out.print("var region=new Object();");
					out.print("region.regionId='';");
					out.print("region.region='';");
					out.print("region.description='---- Select ----';");
					out.print("region.countryCode='';");
					out.print("regions.push(region);");
				}

				for (Map.Entry<Long, Region> regionMap : countryMap.getValue()
						.entrySet()) {
					out.print("var region=new Object();");
					out.print("region.regionId=\""
							+ regionMap.getValue().getRegionId() + "\";");
					out.print("region.region=\""
							+ regionMap.getValue().getRegion() + "\";");
					out.print("region.description=\""
							+ regionMap.getValue().getDescription() + "\";");
					out.print("region.countryCode=\""
							+ regionMap.getValue().getCountryCode() + "\";");
					out.print("regions.push(region);");
				}
				out.print("countryMap[\"" + countryMap.getKey()
						+ "\"]=regions;");
	}%>

	
	var pleaseSelectARecordMsg = "${grtConfig.pleaseSelectARecordMsg}";

	var emptySoldToError = "${grtConfig.emptySoldToError}";
	var requiredNameCityCountryError = "${grtConfig.requiredNameCityCountryError}";
	var accountNameLengthError = "${grtConfig.accountNameLengthError}";
	var cityLengthError = "${grtConfig.cityLengthError}";
	var mandatoryAccNameStreetCityError = "${grtConfig.mandatoryAccNameStreetCityError}";
	var mandatorPhoneEmailNameError = "${grtConfig.mandatorPhoneEmailNameError}";
	var streetLengthError = "${grtConfig.streetLengthError}";

	var zipCodeLengthError = "${grtConfig.zipCodeLengthError}";
	var phoneNumLengthError = "${grtConfig.phoneNumLengthError}";
	var invalidPhoneNumberError = "${grtConfig.invalidPhoneNumberError}";
	var faxLengthError = "${grtConfig.faxLengthError}";
	var emailLengthError = "${grtConfig.emailLengthError}";
	var stateLengthError = "${grtConfig.stateLengthError}";
	var cancelProcessMsg = "${grtConfig.cancelProcessMsg}";
	var waitWhileFetchingMsg = "${grtConfig.waitWhileFetchingMsg}";

	var stateOrProvince = "${grtConfig.stateOrProvince}";
	var street2LengthError = "${grtConfig.street2LengthError}";
	var phoneNoMax10Error = "${grtConfig.phoneNoMax10Error}";

	var waitAccountUpdateMsg = "${grtConfig.waitAccountUpdateMsg}";
	
	
	//Error Codes	
	var pleaseSelectARecordMsgCode = "${grtConfig.ewiMessageCodeMap['pleaseSelectARecordMsg']}";

	var emptySoldToErrorCode = "${grtConfig.ewiMessageCodeMap['emptySoldToError']}";
	var requiredNameCityCountryErrorCode = "${grtConfig.ewiMessageCodeMap['requiredNameCityCountryError']}";
	var accountNameLengthErrorCode = "${grtConfig.ewiMessageCodeMap['accountNameLengthError']}";
	var cityLengthErrorCode = "${grtConfig.ewiMessageCodeMap['cityLengthError']}";
	var mandatoryAccNameStreetCityErrorCode = "${grtConfig.ewiMessageCodeMap['mandatoryAccNameStreetCityError']}";
	var mandatorPhoneEmailNameErrorCode = "${grtConfig.ewiMessageCodeMap['mandatorPhoneEmailNameError']}";
	var streetLengthErrorCode = "${grtConfig.ewiMessageCodeMap['streetLengthError']}";

	var zipCodeLengthErrorCode = "${grtConfig.ewiMessageCodeMap['zipCodeLengthError']}";
	var phoneNumLengthErrorCode = "${grtConfig.ewiMessageCodeMap['phoneNumLengthError']}";
	var invalidPhoneNumberErrorCode = "${grtConfig.ewiMessageCodeMap['invalidPhoneNumberError']}";
	var faxLengthErrorCode = "${grtConfig.ewiMessageCodeMap['faxLengthError']}";
	var emailLengthErrorCode = "${grtConfig.ewiMessageCodeMap['emailLengthError']}";
	var stateLengthErrorCode = "${grtConfig.ewiMessageCodeMap['stateLengthError']}";
	var cancelProcessMsgCode = "${grtConfig.ewiMessageCodeMap['cancelProcessMsg']}";
	var waitWhileFetchingMsgCode = "${grtConfig.ewiMessageCodeMap['waitWhileFetchingMsg']}";

	var stateOrProvinceCode = "${grtConfig.ewiMessageCodeMap['stateOrProvince']}";
	var street2LengthErrorCode = "${grtConfig.ewiMessageCodeMap['street2LengthError']}";
	var phoneNoMax10ErrorCode = "${grtConfig.ewiMessageCodeMap['phoneNoMax10Error']}";

	var waitAccountUpdateMsgCode = "${grtConfig.ewiMessageCodeMap['waitAccountUpdateMsg']}"; 
	

	
	

	$(function(){
		var loadingOverlay = $("#loading-overlay");
		loadingOverlay.hide(); //hide loader by default
		var LocationSelection = Backbone.View.extend({
			el : $("#locationWrapper"),

			initialize : function() {
				var self = this;
				this.el.find('.search-soldto-input').autocomplete(self.options);
			},

			/* START :: Sold To Autocomplete */
			options : {
				minLength : 2,
				source : function(request, response) {
						var self = this;
						var soldTo = $("input.search-soldto-input").val();
						self.menu.element.find('.ui-menu-item').stop().animate({
							'opacity' : 0
						}, 'slow');
						$.ajax({
							url : _path + "/technicalregsitration/json/getCxpSoldToList.action?soldTo="+soldTo,
							dataType : 'json',
							success : function(data) {
								response( data );
							}
						});
					},

				select : function(e, ui, category, el) {
					$('#csrNext').removeAttr('disabled');
					$('#csrNext').addClass('gray');
				},
				open : function(event, ui) {
					var data = $(this).data('autocomplete');
					data.menu.element.css({
						'width' : '225px'
					}).addClass('global-auto-complete').find('li').each(function() {
						var a = $(this).find('a');
						a.html(a.text().replace(/\[<b>\]/g, '<strong>').replace(/\[<\/b>\]/g, '</strong>'));
					});
				}
			},

			/* END :: Sold To Autocomplete */

			closeErrorPopup : function(){
				this.el.find('.error-message').hide();
			},

			setSoldToValue : function(){
				var soldTo = $.trim(this.el.find('#soldToNumber').val()).split(" ")[0];
				soldTo = $.trim(soldTo);
				this.el.find("input#soldToId").val( soldTo  );
				$("input[type=hidden][name=soldTo]").val( soldTo  );
				$("input[type=hidden]#soldToId").val( soldTo  );

				return true;
			},

			redirectToHomePage : function(){
				window.location.href = _path+"/home-action.action";
			},

			events : {
				'click .errPopupBtn' : 'closeErrorPopup',
				'click #csrNext' : 'setSoldToValue',
				'click input[type=button].cancelBtn' : 'redirectToHomePage'
			}

		});

		locationSelection = new LocationSelection();

	});

</script>

<!-- start content-wrap -->
<div class="content-wrap account-update" id="locationWrapper">

	<h1 class="page-title">Account Update</h1>

	<!-- start content -->
	<div class="content">

		<!-- start accountCreationForm -->
		<s:form id="accountCreationForm" enctype="multipart/form-data" action="begin" method="post">
			<a id="showShipToSoldTo" href="<c:url context="${ _path }" value="/account/showShipToSoldTo.action"/>"></a>
			<a id="showSoldToId" href="<c:url context="${ _path }" value="/account/showSoldToId.action"/>"></a>
			<a id="showFuzzySearch" href="<c:url context="${ _path }" value="/account/showFuzzySearch.action"/>"></a>
			<a id="doFuzzySearch" href="<c:url context="${ _path }" value="/account/doFuzzySearchForUpdate.action"/>"></a>
			<a id="populateAccountForUpdate"  href="<c:url context="${ _path }" value="/account/populateAccountForUpdate.action"/>"></a>
			<a id="cancelAccountCreationAction" href="<c:url context="${ _path }" value="/home/home-action.action"/>"></a>
			<a id="validateAccountForUpdate" href="<c:url context="${ _path }" value="/account/validateAccountForUpdate.action"/>"></a>
			<a id="validateSoldtoIdForUpdate" href="<c:url context="${ _path }" value="/account/acoountUpdate.action"/>"></a>
			<a id="showAccountUpdate"  href="<c:url context="${ _path }" value="/account/showAccountUpdate.action"/>"></a>
			<a id="updateAccount" href="<c:url context="${ _path }" value="/account/updateAccount.action"/>"></a>
			<input type="hidden" name="actionForm.soldToId" value="${ fn:escapeXml(actionForm.accountFormBean.soldToId) }" id="soldToStorer" />


			<c:if test="${actionForm.accountFormBean.shipToCreationFlagForBP eq 'block'}">
			<!-- start shiptocreationforBP -->
			<div id="shiptocreationforBP" style="display: ${actionForm.accountFormBean.shipToCreationFlagForBP};">
				<div id="soldto-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">SoldTo details
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div class=" collapse-box-container">

						<div class="data">
							<div class="row">
								<div class="col">
									<p>
										<span class="title">Enter Sold To/Functional Location </span>

										<input type="text" id="cxpSoldTo"
											name="actionForm.accountFormBean.soldToId"
											value="${ actionForm.accountFormBean.soldToId }"
											class="search-soldto-input" /> 
										<span class="title"> or if unknown </span>
										<input type="button"
											class="button gray small"
											onClick="document.getElementById('cxpSoldTo').value = document.getElementById('cxpSoldTo').value.trim().split(' ')[0]; javascript:submitForm('showFuzzySearch');"
											value="Lookup" />
											
									</p>
								</div>
							</div>
						</div>

					</div>
				</div>

				<!-- start controls -->
				<div class="controls">
					<input type="button" class="button gray" value="Cancel" onclick="javascript:cancelProcess();" />
					<input type="button" id="csrNext" class="button gray" onclick="document.getElementById('cxpSoldTo').value = document.getElementById('cxpSoldTo').value.trim().split(' ')[0];javascript:doNext();" value="Next" />
				</div>
				<!-- end controls -->

			</div>
			<!-- end shiptocreationforBP -->
			</c:if>

			<c:if test="${actionForm.accountFormBean.shipToCreationFlagForNonBP eq 'block'}">
			<!-- start shiptocreationforNonBP -->
			<div id="shiptocreationforNonBP" style="display: ${actionForm.accountFormBean.shipToCreationFlagForNonBP};">
				<div id="soldto-non-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">SoldTo details
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div id="" class="collapse-box-container">

						<div class="data">
							<div class="row">
								<div class="col">
									<p>
										<span class="title">Enter Sold To/Functional Location </span>
										<input type="text"
											name="actionForm.accountFormBean.soldToId"
											id="cxpSoldTo"
											value="${ actionForm.accountFormBean.soldToId }" />  
										<span class="title"> or if unknown </span> 
										<input type="button" class="button gray small"
											onClick="javascript:submitForm('showFuzzySearch');"
											value="Lookup" />
									</p>
								</div>
							</div>
						</div>

					</div>
					<!-- start error msgs -->
					<c:if test="${requestScope.soldToError}">
					<div class="error-bar" style="display: block;">
						<i class="fa fa-exclamation-triangle"></i><span class="error-bar-msg">${requestScope.soldToError}</span>
					</div>
					</c:if>
					<c:if test="${requestScope.noresult}">
					<div class="error-bar" style="display: block;">
						<i class="fa fa-exclamation-triangle"></i><span class="error-bar-msg">No record Found.</span>
					</div>
					</c:if>
					<!-- end error msgs -->
				</div>

				<!-- sart controls -->
				<div class="controls">
					<input type="button" class="button gray" value="Cancel" onclick="javascript:cancelProcess();" />
					<input type="button" class="button gray" onclick="javascript:validateSoldToIdNonBP();" value="Next" />
				</div>
				<!-- end controls -->

			</div>
			<!-- end shiptocreationforNonBP -->
			</c:if>

			<c:if test="${actionForm.accountFormBean.searchFlag eq 'block'}">
			<!-- start accountaddress -->
			<div id="accountaddress" style="display: ${actionForm.accountFormBean.searchFlag};">

				<div id="address-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">Account address
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div id="" class="data collapse-box-container">

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Name*</span>
									<input type="text"
										name="actionForm.accountFormBean.endCustomerName"
										id="endCustomerName"
										class="required"
										value="${actionForm.accountFormBean.endCustomerName}"
										maxlength="80" size="60"/>
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Street Name</span>
									<input type="text"
										name="actionForm.accountFormBean.endCustomerStreetName"
										id="endCustomerStreetName"
										class="required"
										value="${fn:escapeXml(actionForm.accountFormBean.endCustomerStreetName)}"
										maxlength="60" size="60"/>
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">City*</span>
									<input type="text"
										name="actionForm.accountFormBean.endCustomerCity"
										id="city"
										value="${fn:escapeXml(actionForm.accountFormBean.endCustomerCity)}"
										class="required"
										maxlength="40" size="40"/>
								</p>
							</div>
						</div>

						<!-- start regional select -->
						<c:choose>
						<c:when test="${actionForm.accountFormBean.currentCountryRegion ne null || fn:length(actionForm.accountFormBean.currentCountryRegion) gt 0}">
						<div style="display: block;" id="otherRegionSelect">
						</c:when>

						<c:otherwise>
						<div style="display: none;" id="otherRegionSelect">
						</c:otherwise>
						</c:choose>

							<div class="row">
								<div class="col">
									<p>
										<span class="title">State/Province</span>
										<select
											name="actionForm.accountFormBean.selectedRegionId"
											id="region" class="required">
											<option class="required" value="">---- Select ----</option>
											<c:forEach var="entry" items="${actionForm.accountFormBean.currentCountryRegion}">
												<c:if test="${actionForm.accountFormBean.selectedRegionId eq entry.key}">
													<option class="required" value="${entry.key}" selected="selected">${entry.value.description}</option>
												</c:if>
												<c:if test="${actionForm.accountFormBean.selectedRegionId ne entry.key}"></c:if>
												<option class="required" value="${entry.key}">${entry.value.description}</option>
											</c:forEach>
										</select>
									</p>
								</div>
								<div class="col">
									<p>
										<span class="title">Zip / Postal Code</span>
										<input type="text"
											name="actionForm.accountFormBean.endCustomerPostalCode"
											id="endCustomerPostalCode"
											class="required"
											value="${fn:escapeXml(actionForm.accountFormBean.endCustomerPostalCode)}"
											maxlength="10" size="12"/>
									</p>
								</div>
							</div>

							<div class="row">
								<div class="col">
									<p>
										<span class="title">Site Country*</span>
										<select
											name="Registrationwlw-select_key:{actionForm.accountFormBean.endCustomerCountry}"
											id="country" onChange="javascript:countryChange();"
											class="required">
											<c:forEach var="entry" items="${actionForm.accountFormBean.countryList}">
												<c:choose>
													<c:when test="${actionForm.accountFormBean.endCustomerCountry eq entry.key}">
														<option class="required" value="${entry.key}"
															selected="selected">${entry.value}</option>
													</c:when>
													<c:when
														test="${actionForm.accountFormBean.endCustomerCountry eq null and entry.key eq 'US'}">
														<option class="required" value="${entry.key}"
															selected="selected">${entry.value}</option>
													</c:when>
													<c:when
														test="${fn:length(entry.key) gt 0 and fn:length(entry.value) gt 0}">
														<option class="required" value="${entry.key}">${entry.value}</option>
													</c:when>
												</c:choose>
											</c:forEach>
										</select>
									</p>
								</div>
							</div>

						</div>
						<!-- end regional select -->

					</div>
					<!-- start error msgs -->
					<c:if test="${requestScope.soldtosearcherror ne null }">
					<div class="error-bar" style="display: block;">
						<i class="fa fa-exclamation-triangle"></i><span class="error-bar-msg">${requestScope.soldtosearcherror}</span>
					</div>
					</c:if>
					<c:if test="${requestScope.searchError ne null }">
					<div class="error-bar" style="display: block;">
						<i class="fa fa-exclamation-triangle"></i><span class="error-bar-msg">${requestScope.searchError}</span>
					</div>
					</c:if>
					<c:if test="${requestScope.noresult}">
					<div class="error-bar" style="display: block;">
						<i class="fa fa-exclamation-triangle"></i><span class="error-bar-msg">No record Found.</span>
					</div>
					</c:if>
					<!-- end error msgs -->
				</div>

				<c:if test="${fn:length(actionForm.accountFormBean.searchResult) gt 0 && actionForm.accountFormBean.searchDataFlag != 'none'}">
				<!-- start fuzzySerch -->
				<div id="fuzzySerch" style="display: ${actionForm.accountFormBean.searchDataFlag};">

					<!-- start similarRecordFound -->
					<div id="fuzzy-wrap" class="data-table-wrap collapse-box">
						<h2 class="collapse-box-header active">Similar Records found
							<span class="legend">
								<span class="best">Best Match</span>
								<span class="nearest">Nearest Match</span>
								<span class="related">Related Match</span>
							</span>
							<a href="#">
								<span></span>
							</a>
						</h2>
						<div id="fuzzy-box" class="data collapse-box-container">

							<!-- start fuzzySearchTable -->
							<table border="1" width="100%" align="center" class="fuzzy-class basic-table" >
								<thead>
									<tr>
										<th align="left">Select (optional)</th>
										<th align="left">Account</th>
										<th align="left">Name</th>
										<th align="left">Street</th>
										<th align="left">City</th>
										<th align="left">State/Province</th>
										<th align="left">Zip / Postal Code</th>
										<th align="left">Country</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${ actionForm.accountFormBean.searchResult }"	var="item">
									<c:set var="color" value="#98CD97" />
									<c:if test="${item.score le grtConfig.serchMidRange}">
										<c:set var="color" value="#CCCD5F" />
									</c:if>
									<c:if test="${item.score le grtConfig.searchLowRange}">
										<c:set var="color" value="#CE6564" />
									</c:if>

									<tr style="background-color:${color}">
										<td align="center" width="15%">
											<input type="radio" name="actionForm.accountFormBean.selectedSAPId" id="enterManually" value="${item.SAPId}" />
										</td>
										<td  align="left">${item.accountNumber }</td>
										<td  align="left">${item.name }</td>
										<td  align="left">${item.street }</td>
										<td  align="left">${item.city }</td>
										<td  align="left">${item.region}</td>
										<td  align="left">${item.zip }</td>
										<td  align="left">${item.country }</td>
									</tr>
									</c:forEach>

								</tbody>
							</table>
							<!-- end fuzzySearchTable -->

						</div>
					</div>
					<!-- end similarRecordFound -->

				</div>
				<!-- end fuzzySerch -->
				</c:if>

				<!-- start controls -->
				<div id="address-btns" class="controls">
					<input type="button" class="button gray" value="Back" onClick="javascript:submitForm('showAccountUpdate');" />
					<input type="button" class="button gray" value="Cancel" onclick="javascript:cancelProcess();" />
					<input type="button" class="button gray" value="Search" onclick="javascript:doSearch();" />
					<c:if test="${actionForm.accountFormBean.searchDataFlag eq 'block'}">
					<input type="button" class="button gray" value="Next" onclick="javascript:searchNext();" />
					</c:if>
				</div>
				<!-- end controls -->

			</div>
			<!-- end accountaddress -->
			</c:if>

			<c:if test="${actionForm.accountFormBean.contactsFlag eq 'block'}">
			<!-- start t1 -->
			<div id="t1" style="display: ${actionForm.accountFormBean.contactsFlag};">
				<div id="t-wrap" class="data-table-wrap collapse-box">
					<h2 class="collapse-box-header active">Acount Details
						<a href="#">
							<span></span>
						</a>
					</h2>
					<div id="" class="data collapse-box-container">

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Account Name*</span>
									<input type="text"
										name="actionForm.accountFormBean.updatedAccount.name"
										id="name"
										value="${actionForm.accountFormBean.originalAccount.name}"
										maxlength="80" size="60"/>
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Street Name*</span>
									<input type="text"
										name="actionForm.accountFormBean.updatedAccount.primaryAccountStreetAddress"
										id="primaryAccountStreetAddress"
										value="${actionForm.accountFormBean.originalAccount.primaryAccountStreetAddress}"
										maxlength="30" size="30"/>
								</p>
							</div>
							<div class="col">
								<p>
									<span class="title">Street Address 2</span>
									<input type="text"
										name="actionForm.accountFormBean.updatedAccount.primaryAccountStreetAddress2"
										id="primaryAccountStreetAddress2"
										value="${actionForm.accountFormBean.originalAccount.primaryAccountStreetAddress2}"
										maxlength="30" size="30"/>
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">City*</span>
									<input type="text"
										name="actionForm.accountFormBean.updatedAccount.primaryAccountCity"
										id="primaryAccountCity"
										value="${actionForm.accountFormBean.originalAccount.primaryAccountCity}"
										maxlength="40" size="40"/>
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">State / Province
										<c:if test="${actionForm.accountFormBean.originalAccount.primaryAccountCountry eq 'USA' or actionForm.accountFormBean.originalAccount.primaryAccountCountry eq 'US'}">*</c:if>
									</span>
									<select
										name="actionForm.accountFormBean.updatedAccount.primaryAccountState"
										id="primaryAccountStateSelect" class="required"
										title="${actionForm.accountFormBean.originalAccount.primaryAccountState }">
										<c:if test="${actionForm.accountFormBean.originalAccount.primaryAccountCountry ne 'US' and actionForm.accountFormBean.originalAccount.primaryAccountCountry ne 'USA'}">
										<option class="required" value="">---- Select ----</option>
										</c:if>
										<c:forEach var="entry" items="${actionForm.accountFormBean.currentCountryRegion}">
											<c:if test="${actionForm.accountFormBean.selectedRegionId eq entry.key}">
											<option class="required"
												value="${entry.value.siebeldescription}##${entry.value.description}"
												selected="selected">${entry.value.description}</option>
											</c:if>
											<c:if test="${actionForm.accountFormBean.selectedRegionId ne entry.key}"></c:if>
											<option class="required"
												value="${entry.value.siebeldescription}##${entry.value.description}">${entry.value.description}</option>
										</c:forEach>
									</select>
								</p>
							</div>
							<div class="col">
								<p>
									<span class="title">Zip / Postal Code*</span>
									<input type="text"
										name="actionForm.accountFormBean.updatedAccount.primaryAccountPostalCode"
										id="primaryAccountPostalCode"
										value="${actionForm.accountFormBean.originalAccount.primaryAccountPostalCode}"
										maxlength="10" size="10"/>
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Phone Number*</span>
									<input type="text"
										name="actionForm.accountFormBean.updatedAccount.phoneNumber"
										id="phoneNumber"
										value="${actionForm.accountFormBean.originalAccount.phoneNumber}"
										maxlength="30" size="30"/>
								</p>
							</div>
							<div class="col">
								<p>
									<span class="title">Fax</span>
									<input type="text"
										name="actionForm.accountFormBean.updatedAccount.faxNumber"
										id="faxNumber"
										value="${actionForm.accountFormBean.originalAccount.faxNumber}"
										maxlength="30" size="30"/>
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Email Address*</span>
									<input type="text"
										name="actionForm.accountFormBean.updatedAccount.emailId"
										id="emailId"
										value="${actionForm.accountFormBean.originalAccount.emailId}"
										maxlength="60" size="60"/>
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Contact Name*</span>
									<input type="text"
										name="actionForm.accountFormBean.updatedAccount.contactName"
										id="contactName"
										value="${actionForm.accountFormBean.originalAccount.contactNameForDisplay}"
										maxlength="60" size="60"/>
								</p>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<p>
									<span class="title">Country*</span>
									<span id="countryforphone">${fn:escapeXml(actionForm.accountFormBean.updatedAccount.primaryAccountCountry)}</span>
								</p>
							</div>
						</div>

						<input type="hidden"
							name="actionForm.accountFormBean.updatedAccount.primaryAccountCountry"
							value="${fn:escapeXml(actionForm.accountFormBean.updatedAccount.primaryAccountCountry)}"
							id="primaryAccountCountry1" />

					</div>

				</div>

				<div class="page-note">
					<p><span>For Avaya to serve you better, Please ensure that your phone and email are correct.</span></p>
				</div>

				<!-- start controls -->
				<div class="controls">
					<c:choose>
					<c:when test="${requestScope.accountUpdated}">
					<input type="button" class="button gray" value="Back" disabled="true" />
					<input type="button" class="button gray" value="Reset" disabled="true" />
					<input type="button"  class="button gray" value="Submit" disabled="true" />
					<input type="button" class="button gray" value="Home" onClick="javascript:submitForm('cancelAccountCreationAction');" />
					</c:when>
					<c:otherwise>
					<input type="button" class="button gray" value="Back" onClick="javascript:submitForm('showAccountUpdate');" />
					<input type="button" class="button gray" value="Cancel" onclick="javascript:cancelProcess();" />
					<input type="button" class="button gray" value="Reset" onclick="javascript:doReset();" />
					<input type="button" class="button gray" value="Submit" onclick="javascript:doSubmit();" />
					</c:otherwise>
					</c:choose>
				</div>
				<!-- end controls -->

			</div>
			<!-- end t1 -->
			</c:if>

		</s:form>
		<!-- end accountCreationForm -->

	</div>
	<!-- end content -->
</div>
<!-- end content-wrap -->

<script>
	jQuery(document).ready(function($) {
		// move entry buttons into table area
		$('#fuzzy-box').append($('table.fuzzy-class'));
		$('table.fuzzy-class').wrap('<div class="basic-table-wrap" />');
		$('.page-wrap').append($('#address-btns'));
	});
</script>
<%-- ************************************************************* --%>
<%-- ***** BELOW IS DEPRECATED CODE - NEED REVIEW AND REMOVE ***** --%>
<%-- ************************************************************* --%>


<!--<netui-data:declareBundle name="msg" bundlePath="messages.grt"></netui-data:declareBundle-->
<%--
	String jpfScopeID = DesktopPresentationContext
			.getDesktopPresentationContext(request)
			.getDefinitionLabel()
			+ "_"
			+ PortletPresentationContext.getPortletPresentationContext(
					request).getInstanceLabel();
--%>
