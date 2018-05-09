package com.avaya.grt.mappers;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class Region implements Serializable
{
    private static final long serialVersionUID = 9149277972522747182L;

    private long              regionId;
    private String            region;
    private String            description;
    private String            countryCode;
    private String            siebeldescription;
    
    public String getCountryCode()
    {
        return countryCode;
    }

    public void setCountryCode(String countryCode)
    {
        this.countryCode = countryCode;
    }
    
    public String getSiebeldescription() {
		return siebeldescription;
	}

	public void setSiebeldescription(String siebeldescription) {
		this.siebeldescription = siebeldescription;
	}

	public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

	public String getRegion() {
		String regex = "[0-9]+";
		if (StringUtils.isNotEmpty(region) && region.length() == 1
				&& region.matches(regex)) {
			return "0" + region;
		}
		return region;
	}

    public void setRegion(String region)
    {
        this.region = region;
    }

    public long getRegionId()
    {
        return regionId;
    }

    public void setRegionId(long regionId)
    {
        this.regionId = regionId;
    }


}
