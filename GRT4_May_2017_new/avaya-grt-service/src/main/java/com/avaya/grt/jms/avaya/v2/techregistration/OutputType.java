//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.05.13 at 11:47:56 AM IST 
//


package com.avaya.grt.jms.avaya.v2.techregistration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OutputType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OutputType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TechRegId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Sid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Mid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SolutionElements" type="{http://avaya.com/v2/techregistration}NewSEType"/>
 *         &lt;element name="TransactionDetails" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="RegResult" type="{http://avaya.com/v2/techregistration}RegResultType"/>
 *         &lt;element name="InstallScript" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OnBoardingXML" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OutputType", propOrder = {
    "techRegId",
    "sid",
    "mid",
    "solutionElements",
    "transactionDetails",
    "regResult",
    "installScript",
    "onBoardingXML"
})
public class OutputType {

    @XmlElement(name = "TechRegId", required = true)
    protected String techRegId;
    @XmlElement(name = "Sid")
    protected String sid;
    @XmlElement(name = "Mid")
    protected String mid;
    @XmlElement(name = "SolutionElements", required = true)
    protected NewSEType solutionElements;
    @XmlElement(name = "TransactionDetails", required = true)
    protected String transactionDetails;
    @XmlElement(name = "RegResult", required = true)
    protected RegResultType regResult;
    @XmlElement(name = "InstallScript")
    protected String installScript;
    @XmlElement(name = "OnBoardingXML")
    protected Object onBoardingXML;

    /**
     * Gets the value of the techRegId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTechRegId() {
        return techRegId;
    }

    /**
     * Sets the value of the techRegId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTechRegId(String value) {
        this.techRegId = value;
    }

    /**
     * Gets the value of the sid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSid() {
        return sid;
    }

    /**
     * Sets the value of the sid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSid(String value) {
        this.sid = value;
    }

    /**
     * Gets the value of the mid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMid() {
        return mid;
    }

    /**
     * Sets the value of the mid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMid(String value) {
        this.mid = value;
    }

    /**
     * Gets the value of the solutionElements property.
     * 
     * @return
     *     possible object is
     *     {@link NewSEType }
     *     
     */
    public NewSEType getSolutionElements() {
        return solutionElements;
    }

    /**
     * Sets the value of the solutionElements property.
     * 
     * @param value
     *     allowed object is
     *     {@link NewSEType }
     *     
     */
    public void setSolutionElements(NewSEType value) {
        this.solutionElements = value;
    }

    /**
     * Gets the value of the transactionDetails property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionDetails() {
        return transactionDetails;
    }

    /**
     * Sets the value of the transactionDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionDetails(String value) {
        this.transactionDetails = value;
    }

    /**
     * Gets the value of the regResult property.
     * 
     * @return
     *     possible object is
     *     {@link RegResultType }
     *     
     */
    public RegResultType getRegResult() {
        return regResult;
    }

    /**
     * Sets the value of the regResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegResultType }
     *     
     */
    public void setRegResult(RegResultType value) {
        this.regResult = value;
    }

    /**
     * Gets the value of the installScript property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstallScript() {
        return installScript;
    }

    /**
     * Sets the value of the installScript property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstallScript(String value) {
        this.installScript = value;
    }

    /**
     * Gets the value of the onBoardingXML property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getOnBoardingXML() {
        return onBoardingXML;
    }

    /**
     * Sets the value of the onBoardingXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setOnBoardingXML(Object value) {
        this.onBoardingXML = value;
    }

}
