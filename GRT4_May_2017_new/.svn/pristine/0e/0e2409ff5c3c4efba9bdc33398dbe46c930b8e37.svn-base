package com.grt.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import com.avaya.grt.mappers.Region;
import com.grt.dto.BusinessPartner;
import com.grt.dto.Result;
import com.grt.dto.ServiceDetails;
import com.grt.dto.TokenRedemptionResponse;
import com.grt.dto.TokenResponseTypeDto;
import com.grt.dto.Account;

/**
 * Form bean for the Token Redemption.
 * 
 * 
 */

public class TokenFormBean implements Serializable
{
    private static final long         serialVersionUID = 1L;
    private String                    locale;
    private String                    tokenNumber;
    private String                    distiSoldToID;
    private String                    distiSoldToName;
    private List<ServiceDetails>      tokenServiceDetails;
    private String                    bpLinkID;
    private String                    serviceTerm;
    private String                    tokenNumberFlag;
    private String                    tokenDetailsFlag;
    private String                    bplinkdetailsFlag;
    private String                    shipToCreationFlagForBP;
    private String                    shipToCreationFlagForNonBP;
    private String                    contractDateFlag;
    private String                    finalScreenFlag;
    private String                    searchFlag;
    private String                    searchDataFlag;
    private String                    tokenRedemptionFinalFlag;
    private String                    selectedAccount;
    private String                    shipTo;
    private String                    name;
    private String                    address1;
    private String                    address2;
    private String                    city;
    private String                    state;
    private String                    country;
    private String                    replaceContract;
    private String                    contractStartDate;
    private List<Result>              searchResult;
    private String                    selectedAccountNumber;
    private String                    endCustomerName;
    private String                    endCustomerStreetName;
    private String                    endCustomerCity;
    private String                    endCustomerOtherProvince;
    private String                    endCustomerPostalCode;
    private String                    endCustomerState;
    private String                    endCustomerProvince;
    private String                    endCustomerCountry;
    private String                    endCustomerEmail;
    private String                    endCustomerPhone;
    private String                    shipToName;
    private boolean                   bplinkidRequired;
	//Added for gift 2.0
	private boolean                   superUser;
    private SortedMap<String, String> countryList;
    private SortedMap<String, String> stateList;
    private SortedMap<String, String> provinceList;
    private Map<String, String>       regionList;
    private TokenResponseTypeDto      tokenDetails;
    private List<BusinessPartner>     attachedBp;
    private BusinessPartner           selectedBp;
    private TokenRedemptionResponse   tokenResponse;
    private String					  region;
    private String                    additionalInfo;
    private String                    additionalEmailIds;
    private Account                   endCoustomerAccount;
    private Account                   distributorAccount;
    private List<String>              cxpSoldToList;
    private String                    usStore;
    private String                    contractSubmitedDate;
    private String                    endCustomerShipto;
    private String                    bpName;
    
    private Map<Long, Region>              currentCountryRegion;
    private Map<String, Map<Long, Region>> regionMap;
    private long                           selectedRegionId;
    private String                         regionValue;
    private String                         regionId;
    private Region                         selectedRegion;

// Added for gift 2.0
    List<Contract> contracts ;
    private String selectedContractNumber;
    private String cancelledContractStartDate;
    private String cancelledContractEndDate;
    
    public String getCancelledContractEndDate() {
		return cancelledContractEndDate;
	}

	public void setCancelledContractEndDate(String cancelledContractEndDate) {
		this.cancelledContractEndDate = cancelledContractEndDate;
	}

	public String getCancelledContractStartDate() {
		return cancelledContractStartDate;
	}

	public void setCancelledContractStartDate(String cancelledContractStartDate) {
		this.cancelledContractStartDate = cancelledContractStartDate;
	}

	public String getSelectedContractNumber() {
		return selectedContractNumber;
	}

	public void setSelectedContractNumber(String selectedContractNumber) {
		this.selectedContractNumber = selectedContractNumber;
	}

    public String getBpName()
    {
        return bpName;
    }

    public void setBpName(String bpName)
    {
        this.bpName = bpName;
    }

    public String getContractSubmitedDate()
    {
        return contractSubmitedDate;
    }

    public void setContractSubmitedDate(String contractSubmitedDate)
    {
        this.contractSubmitedDate = contractSubmitedDate;
    }

    public String getUsStore()
    {
        return usStore;
    }

    public void setUsStore(String usStore)
    {
        this.usStore = usStore;
    }

    public Account getDistributorAccount()
    {
        return distributorAccount;
    }

    public void setDistributorAccount(Account distributorAccount)
    {
        this.distributorAccount = distributorAccount;
    }

    public void setEndCoustomerAccount(Account endCoustomerAccount)
    {
        this.endCoustomerAccount = endCoustomerAccount;
    }

    public Account getEndCoustomerAccount()
    {
        return endCoustomerAccount;
    }

    public void setFinalScreenFlag(String finalScreenFlag)
    {
        this.finalScreenFlag = finalScreenFlag;
    }

    public String getFinalScreenFlag()
    {
        return finalScreenFlag;
    }

    public TokenRedemptionResponse getTokenResponse()
    {
        return tokenResponse;
    }

    public void setTokenResponse(TokenRedemptionResponse tokenResponse)
    {
        this.tokenResponse = tokenResponse;
    }

    public List<BusinessPartner> getAttachedBp()
    {
        return attachedBp;
    }

    public void setAttachedBp(List<BusinessPartner> attachedBp)
    {
        this.attachedBp = attachedBp;
    }

    public TokenResponseTypeDto getTokenDetails()
    {
        return tokenDetails;
    }

    public void setTokenDetails(TokenResponseTypeDto tokenDetails)
    {
        this.tokenDetails = tokenDetails;
    }

    public SortedMap<String, String> getCountryList()
    {
        return countryList;
    }

    public void setCountryList(SortedMap<String, String> countryList)
    {
        this.countryList = countryList;
    }

    public SortedMap<String, String> getProvinceList()
    {
        return provinceList;
    }

    public void setProvinceList(SortedMap<String, String> provinceList)
    {
        this.provinceList = provinceList;
    }

    public SortedMap<String, String> getStateList()
    {
        return stateList;
    }

    public void setStateList(SortedMap<String, String> stateList)
    {
        this.stateList = stateList;
    }

    public String getContractStartDate()
    {
        return contractStartDate;
    }

    public void setContractStartDate(String contractStartDate)
    {
        this.contractStartDate = contractStartDate;
    }

    public String getSelectedAccount()
    {
        return selectedAccount;
    }

    public void setSelectedAccount(String selectedAccount)
    {
        this.selectedAccount = selectedAccount;
    }

    public String getBplinkdetailsFlag()
    {
        return bplinkdetailsFlag;
    }

    public void setBplinkdetailsFlag(String bplinkdetailsFlag)
    {
        this.bplinkdetailsFlag = bplinkdetailsFlag;
    }

    public String getTokenDetailsFlag()
    {
        return tokenDetailsFlag;
    }

    public void setTokenDetailsFlag(String tokenDetailsFlag)
    {
        this.tokenDetailsFlag = tokenDetailsFlag;
    }

    public String getTokenNumberFlag()
    {
        return tokenNumberFlag;
    }

    public void setTokenNumberFlag(String tokenNumberFlag)
    {
        this.tokenNumberFlag = tokenNumberFlag;
    }

    public String getServiceTerm()
    {
        return serviceTerm;
    }

    public void setServiceTerm(String serviceTerm)
    {
        this.serviceTerm = serviceTerm;
    }

    public String getBpLinkID()
    {
        return bpLinkID;
    }

    public void setBpLinkID(String bpLinkID)
    {
        this.bpLinkID = bpLinkID;
    }

    public String getDistiSoldToID()
    {
        return distiSoldToID;
    }

    public void setDistiSoldToID(String distiSoldToID)
    {
        this.distiSoldToID = distiSoldToID;
    }

    public String getDistiSoldToName()
    {
        return distiSoldToName;
    }

    public void setDistiSoldToName(String distiSoldToName)
    {
        this.distiSoldToName = distiSoldToName;
    }

    public String getTokenNumber()
    {
        return tokenNumber;
    }

    public void setTokenNumber(String tokenNumber)
    {
        this.tokenNumber = tokenNumber;
    }

    public List<ServiceDetails> getTokenServiceDetails()
    {
        return tokenServiceDetails;
    }

    public void setTokenServiceDetails(List<ServiceDetails> tokenServiceDetails)
    {
        this.tokenServiceDetails = tokenServiceDetails;
    }

    public String getLocale()
    {
        return locale;
    }

    public void setLocale(String locale)
    {
        this.locale = locale;
    }

    public String getShipToCreationFlagForBP()
    {
        return shipToCreationFlagForBP;
    }

    public void setShipToCreationFlagForBP(String shipToCreationFlagForBP)
    {
        this.shipToCreationFlagForBP = shipToCreationFlagForBP;
    }

    public String getShipToCreationFlagForNonBP()
    {
        return shipToCreationFlagForNonBP;
    }

    public void setShipToCreationFlagForNonBP(String shipToCreationFlagForNonBP)
    {
        this.shipToCreationFlagForNonBP = shipToCreationFlagForNonBP;
    }

    public String getShipTo()
    {
        return shipTo;
    }

    public void setShipTo(String shipTo)
    {
        this.shipTo = shipTo;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getAddress1()
    {
        return address1;
    }

    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    public String getAddress2()
    {
        return address2;
    }

    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    public String getReplaceContract()
    {
        return replaceContract;
    }

    public void setReplaceContract(String replaceContract)
    {
        this.replaceContract = replaceContract;
    }

    public String getContractDateFlag()
    {
        return contractDateFlag;
    }

    public void setContractDateFlag(String contractDateFlag)
    {
        this.contractDateFlag = contractDateFlag;
    }

    public String getSearchDataFlag()
    {
        return searchDataFlag;
    }

    public void setSearchDataFlag(String searchDataFlag)
    {
        this.searchDataFlag = searchDataFlag;
    }

    public String getSearchFlag()
    {
        return searchFlag;
    }

    public void setSearchFlag(String searchFlag)
    {
        this.searchFlag = searchFlag;
    }

    public String getEndCustomerCity()
    {
        return endCustomerCity;
    }

    public void setEndCustomerCity(String endCustomerCity)
    {
        this.endCustomerCity = endCustomerCity;
    }

    public String getEndCustomerCountry()
    {
        return endCustomerCountry;
    }

    public void setEndCustomerCountry(String endCustomerCountry)
    {
        this.endCustomerCountry = endCustomerCountry;
    }

    public String getEndCustomerEmail()
    {
        return endCustomerEmail;
    }

    public void setEndCustomerEmail(String endCustomerEmail)
    {
        this.endCustomerEmail = endCustomerEmail;
    }

    public String getEndCustomerName()
    {
        return endCustomerName;
    }

    public void setEndCustomerName(String endCustomerName)
    {
        this.endCustomerName = endCustomerName;
    }

    public String getEndCustomerOtherProvince()
    {
        return endCustomerOtherProvince;
    }

    public void setEndCustomerOtherProvince(String endCustomerOtherProvince)
    {
        this.endCustomerOtherProvince = endCustomerOtherProvince;
    }

    public String getEndCustomerPhone()
    {
        return endCustomerPhone;
    }

    public void setEndCustomerPhone(String endCustomerPhone)
    {
        this.endCustomerPhone = endCustomerPhone;
    }

    public String getEndCustomerPostalCode()
    {
        return endCustomerPostalCode;
    }

    public void setEndCustomerPostalCode(String endCustomerPostalCode)
    {
        this.endCustomerPostalCode = endCustomerPostalCode;
    }

    public String getEndCustomerState()
    {
        return endCustomerState;
    }

    public void setEndCustomerState(String endCustomerState)
    {
        this.endCustomerState = endCustomerState;
    }

    public String getEndCustomerStreetName()
    {
        return endCustomerStreetName;
    }

    public void setEndCustomerStreetName(String endCustomerStreetName)
    {
        this.endCustomerStreetName = endCustomerStreetName;
    }

    public List<Result> getSearchResult()
    {
        return searchResult;
    }

    public void setSearchResult(List<Result> searchResult)
    {
        this.searchResult = searchResult;
    }

    public String getSelectedAccountNumber()
    {
        return selectedAccountNumber;
    }

    public void setSelectedAccountNumber(String selectedAccountNumber)
    {
        this.selectedAccountNumber = selectedAccountNumber;
    }


    public String getShipToName()
    {
        return shipToName;
    }

    public void setShipToName(String shipToName)
    {
        this.shipToName = shipToName;
    }


    public String getTokenRedemptionFinalFlag()
    {
        return tokenRedemptionFinalFlag;
    }

    public void setTokenRedemptionFinalFlag(String tokenRedemptionFinalFlag)
    {
        this.tokenRedemptionFinalFlag = tokenRedemptionFinalFlag;
    }

    public String getEndCustomerProvince()
    {
        return endCustomerProvince;
    }

    public void setEndCustomerProvince(String endCustomerProvince)
    {
        this.endCustomerProvince = endCustomerProvince;
    }

    public boolean isBplinkidRequired()
    {
        return bplinkidRequired;
    }

    public void setBplinkidRequired(boolean bplinkidRequired)
    {
        this.bplinkidRequired = bplinkidRequired;
    }

    public BusinessPartner getSelectedBp()
    {
        return selectedBp;
    }

    public void setSelectedBp(BusinessPartner selectedBp)
    {
        this.selectedBp = selectedBp;
    }

    public String getAdditionalInfo()
    {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo)
    {
        this.additionalInfo = additionalInfo;
    }

    public void setCxpSoldToList(List<String> cxpSoldToList)
    {
        this.cxpSoldToList = cxpSoldToList;
    }

    public List<String> getCxpSoldToList()
    {
        return cxpSoldToList;
    }

    public String getEndCustomerShipto()
    {
        return endCustomerShipto;
    }

    public void setEndCustomerShipto(String endCustomerShipto)
    {
        this.endCustomerShipto = endCustomerShipto;
    }

    public Map<Long, Region> getCurrentCountryRegion()
    {
        return currentCountryRegion;
    }

    public void setCurrentCountryRegion(Map<Long, Region> currentCountryRegion)
    {
        this.currentCountryRegion = currentCountryRegion;
    }

    public String getRegionId()
    {
        return regionId;
    }

    public void setRegionId(String regionId)
    {
        this.regionId = regionId;
    }

    public Map<String, Map<Long, Region>> getRegionMap()
    {
        return regionMap;
    }

    public void setRegionMap(Map<String, Map<Long, Region>> regionMap)
    {
        this.regionMap = regionMap;
    }

    public String getRegionValue()
    {
        return regionValue;
    }

    public void setRegionValue(String regionValue)
    {
        this.regionValue = regionValue;
    }

    public Region getSelectedRegion()
    {
        return selectedRegion;
    }

    public void setSelectedRegion(Region selectedRegion)
    {
        this.selectedRegion = selectedRegion;
    }

    public long getSelectedRegionId()
    {
        return selectedRegionId;
    }

    public void setSelectedRegionId(long selectedRegionId)
    {
        this.selectedRegionId = selectedRegionId;
    }

	public String getAdditionalEmailIds() {
		return additionalEmailIds;
	}

	public void setAdditionalEmailIds(String additionalEmailIds) {
		this.additionalEmailIds = additionalEmailIds;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Map<String, String> getRegionList() {
		return regionList;
	}

	public void setRegionList(Map<String, String> regionList) {
		this.regionList = regionList;
	}
// Added for gift card 2.0
	public List<Contract> getContracts() {
		return contracts;
	}

	public void setContracts(List<Contract> contracts) {
		this.contracts = contracts;
	}

	public boolean isSuperUser() {
		return superUser;
	}

	public void setSuperUser(boolean superUser) {
		this.superUser = superUser;
	}

}
