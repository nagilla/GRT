package com.avaya.grt.jms.siebel.xml.service20request_2.jms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceResponse", propOrder = {
    "listOfServiceResponseDto"
})
public class ServiceResponse {

	 @XmlElement(name = "ListOfAvayaServiceResponse")
	    protected ListOfAvayaServiceResponse listOfServiceResponseDto;
	 
		public ListOfAvayaServiceResponse getListOfServiceResponseDto() {
			return listOfServiceResponseDto;
		}
		public void setListOfServiceResponseDto(
				ListOfAvayaServiceResponse listOfServiceResponseDto) {
			this.listOfServiceResponseDto = listOfServiceResponseDto;
		}
}
