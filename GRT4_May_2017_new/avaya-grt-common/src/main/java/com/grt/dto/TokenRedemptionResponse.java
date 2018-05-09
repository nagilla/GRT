package com.grt.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Calendar;

public class TokenRedemptionResponse implements Serializable
{

    private static final long serialVersionUID = 1L;
    private String            code;
    private String            description;
    private BigInteger            contractNumber;
    private Calendar          contractStartDate;

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public void setContractNumber(BigInteger contractNumber)
    {
        this.contractNumber = contractNumber;
    }

    public BigInteger getContractNumber()
    {
        return contractNumber;
    }

    public Calendar getContractStartDate()
    {
        return contractStartDate;
    }

    public void setContractStartDate(Calendar contractStartDate)
    {
        this.contractStartDate = contractStartDate;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }


}
