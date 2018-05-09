//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b26-ea3 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.27 at 11:51:48 AM EDT 
//


package com.avaya.grt.jms.siebel.xml.assetupdate.jms;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.avaya.grt.jms.siebel.xml.assetupdate.jms.SiebelMessage;
import com.avaya.grt.jms.siebel.xml.assetupdate.jms.SiebelMessage.ListOfAvayaGrtRegistration;
import com.avaya.grt.jms.siebel.xml.assetupdate.jms.SiebelMessage.ListOfAvayaGrtRegistration.AvayaGrtRegistration;


/**
 * <p>Java class for SiebelMessage element declaration.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;element name="SiebelMessage">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="ListOfAvayaGrtRegistration">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="AvayaGrtRegistration" maxOccurs="unbounded">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="AssetNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                               &lt;element name="GRTSentFlag" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                               &lt;element name="MaterialCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                               &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                               &lt;element name="RegistrationId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                               &lt;element name="SerialNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;/sequence>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/sequence>
 *         &lt;attribute name="IntObjectFormat" type="{http://www.w3.org/2001/XMLSchema}string" />
 *         &lt;attribute name="IntObjectName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *         &lt;attribute name="MessageId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *         &lt;attribute name="MessageType" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "listOfAvayaGrtRegistration"
})
@XmlRootElement(name = "SiebelMessage")
public class SiebelMessage {

    @XmlElement(name = "ListOfAvayaGrtRegistration", namespace = "http://www.siebel.com/xml/AVAYA%20GRT%20Registration")
    protected ListOfAvayaGrtRegistration listOfAvayaGrtRegistration;
    @XmlAttribute(name = "IntObjectFormat")
    protected String intObjectFormat;
    @XmlAttribute(name = "IntObjectName")
    protected String intObjectName;
    @XmlAttribute(name = "MessageId")
    protected String messageId;
    @XmlAttribute(name = "MessageType")
    protected String messageType;

    /**
     * Gets the value of the listOfAvayaGrtRegistration property.
     * 
     * @return
     *     possible object is
     *     {@link ListOfAvayaGrtRegistration }
     *     
     */
    public ListOfAvayaGrtRegistration getListOfAvayaGrtRegistration() {
        return listOfAvayaGrtRegistration;
    }

    /**
     * Sets the value of the listOfAvayaGrtRegistration property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListOfAvayaGrtRegistration }
     *     
     */
    public void setListOfAvayaGrtRegistration(ListOfAvayaGrtRegistration value) {
        this.listOfAvayaGrtRegistration = value;
    }

    /**
     * Gets the value of the intObjectFormat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIntObjectFormat() {
        return intObjectFormat;
    }

    /**
     * Sets the value of the intObjectFormat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIntObjectFormat(String value) {
        this.intObjectFormat = value;
    }

    /**
     * Gets the value of the intObjectName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIntObjectName() {
        return intObjectName;
    }

    /**
     * Sets the value of the intObjectName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIntObjectName(String value) {
        this.intObjectName = value;
    }

    /**
     * Gets the value of the messageId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Sets the value of the messageId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageId(String value) {
        this.messageId = value;
    }

    /**
     * Gets the value of the messageType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * Sets the value of the messageType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageType(String value) {
        this.messageType = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="AvayaGrtRegistration" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="AssetNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="GRTSentFlag" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="MaterialCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="RegistrationId" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="SerialNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "avayaGrtRegistration"
    })
    public static class ListOfAvayaGrtRegistration {

        @XmlElement(name = "AvayaGrtRegistration", namespace = "http://www.siebel.com/xml/AVAYA%20GRT%20Registration")
        protected List<AvayaGrtRegistration> avayaGrtRegistration;

        /**
         * Gets the value of the avayaGrtRegistration property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the avayaGrtRegistration property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAvayaGrtRegistration().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link AvayaGrtRegistration }
         * 
         * 
         */
        public List<AvayaGrtRegistration> getAvayaGrtRegistration() {
            if (avayaGrtRegistration == null) {
                avayaGrtRegistration = new ArrayList<AvayaGrtRegistration>();
            }
            return this.avayaGrtRegistration;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="AssetNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="GRTSentFlag" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="MaterialCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="RegistrationId" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="SerialNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "assetNumber",
            "grtSentFlag",
            "materialCode",
            "quantity",
            "registrationId",
            "serialNumber"
        })
        public static class AvayaGrtRegistration {

            @XmlElement(name = "AssetNumber", namespace = "http://www.siebel.com/xml/AVAYA%20GRT%20Registration")
            protected String assetNumber;
            @XmlElement(name = "GRTSentFlag", namespace = "http://www.siebel.com/xml/AVAYA%20GRT%20Registration")
            protected String grtSentFlag;
            @XmlElement(name = "MaterialCode", namespace = "http://www.siebel.com/xml/AVAYA%20GRT%20Registration")
            protected String materialCode;
            @XmlElement(name = "Quantity", namespace = "http://www.siebel.com/xml/AVAYA%20GRT%20Registration")
            protected String quantity;
            @XmlElement(name = "RegistrationId", namespace = "http://www.siebel.com/xml/AVAYA%20GRT%20Registration")
            protected String registrationId;
            @XmlElement(name = "SerialNumber", namespace = "http://www.siebel.com/xml/AVAYA%20GRT%20Registration")
            protected String serialNumber;

            /**
             * Gets the value of the assetNumber property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAssetNumber() {
                return assetNumber;
            }

            /**
             * Sets the value of the assetNumber property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAssetNumber(String value) {
                this.assetNumber = value;
            }

            /**
             * Gets the value of the grtSentFlag property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getGRTSentFlag() {
                return grtSentFlag;
            }

            /**
             * Sets the value of the grtSentFlag property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setGRTSentFlag(String value) {
                this.grtSentFlag = value;
            }

            /**
             * Gets the value of the materialCode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getMaterialCode() {
                return materialCode;
            }

            /**
             * Sets the value of the materialCode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setMaterialCode(String value) {
                this.materialCode = value;
            }

            /**
             * Gets the value of the quantity property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getQuantity() {
                return quantity;
            }

            /**
             * Sets the value of the quantity property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setQuantity(String value) {
                this.quantity = value;
            }

            /**
             * Gets the value of the registrationId property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getRegistrationId() {
                return registrationId;
            }

            /**
             * Sets the value of the registrationId property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setRegistrationId(String value) {
                this.registrationId = value;
            }

            /**
             * Gets the value of the serialNumber property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getSerialNumber() {
                return serialNumber;
            }

            /**
             * Sets the value of the serialNumber property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setSerialNumber(String value) {
                this.serialNumber = value;
            }

        }

    }

}