package com.grt.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


import com.grt.dto.TechnicalOrderDetail;

public class TechnicalOrderDetailUtil {
	

	public static void main(String[] args) {
		List<TechnicalOrderDetail> test = new ArrayList<TechnicalOrderDetail>();
		TechnicalOrderDetail o = new TechnicalOrderDetail();
		o.setMaterialCode("123");
		o.setInitialQuantity(10L);
		o.setSerialNumber(null);
		test.add(o);
		
		TechnicalOrderDetail o2 = new TechnicalOrderDetail();
		o2.setMaterialCode("1234");
		o2.setInitialQuantity(10L);
		o2.setSerialNumber(null);
		test.add(o2);
		

		TechnicalOrderDetail o3 = new TechnicalOrderDetail();
		o3.setMaterialCode("12");
		o3.setInitialQuantity(10L);
		o3.setSerialNumber(null);
		test.add(o3);
		
		TechnicalOrderDetail o4 = new TechnicalOrderDetail();
		o4.setMaterialCode("12");
		o4.setInitialQuantity(10L);
		o4.setSerialNumber("123456_V");
		test.add(o4);
		
		TechnicalOrderDetail o5 = new TechnicalOrderDetail();
		o5.setMaterialCode("0235");
		o5.setInitialQuantity(10L);
		o5.setSerialNumber(null);
		test.add(o5);
		
		TechnicalOrderDetail o6 = new TechnicalOrderDetail();
		o6.setMaterialCode("0233");
		o6.setInitialQuantity(10L);
		o6.setSerialNumber(null);
		test.add(o6);
		
		//Added by Velpuri on 0908
		TechnicalOrderDetail o7 = new TechnicalOrderDetail();
		o7.setMaterialCode("0235");
		o7.setInitialQuantity(10L);
		o7.setSerialNumber("87654321_V");
		test.add(o7);
		
		TechnicalOrderDetail o8 = new TechnicalOrderDetail();
		o8.setMaterialCode("0235");
		o8.setInitialQuantity(20L);
		o8.setSerialNumber(null);
		test.add(o8);
		
		test = TechnicalOrderDetailUtil.summingQuantityGoupedByMaterialcode(test );
		
		for(TechnicalOrderDetail t : test) {
			System.out.println(t.getMaterialCode());
			System.out.println(t.getInitialQuantity());
		}
	}
	
	private TechnicalOrderDetailUtil() {
	}
	
	/** Summing the quantity of initialQuantity grouped by materialCode
	 * 
	 * @param details List<TechnicalOrderDetail>
	 * @return result List<TechnicalOrderDetail>
	 */
	public static List<TechnicalOrderDetail> summingQuantityGoupedByMaterialcode(List<TechnicalOrderDetail> details) {
        Logger logger = Logger.getLogger(TechnicalOrderDetailUtil.class);
		Map<String, TechnicalOrderDetail> detailMap = new HashMap<String, TechnicalOrderDetail>();
		for(TechnicalOrderDetail detail : details) {
			if(detail != null && detail.getSelectforRegistration()){
				String key = detail.getMaterialCode();
				/**  [AVAYA]: 09-09-2011 creating a new unique key to allow duplicates in the TreeMap(Start) **/
				//String mapKey = detail.getMaterialCode()+detail.getSerialNumber()+detail.getInitialQuantity();
				String mapKey = detail.getMaterialCode()+detail.getSerialNumber();
				/**  [AVAYA]: 09-09-2011 creating a new unique key to allow duplicates in the TreeMap(End) **/
				if(StringUtils.isEmpty(mapKey)) {
					continue;
				}
				TechnicalOrderDetail original = detailMap.get(mapKey);
				if(original == null) {
					/**  [AVAYA]: 09-09-2011 creating a new unique key to allow duplicates in the TreeMap(Start) **/
					detailMap.put(mapKey, detail);
					/**  [AVAYA]: 09-09-2011 creating a new unique key to allow duplicates in the TreeMap(End) **/
				}
				else {				
	
					
					/*if(original.getInitialQuantity() != null) {
						sum = original.getInitialQuantity();
					}
					*//**  [AVAYA]: 08-26-2011 Removed logic that adds Quantity for specific Material Code (Start) **//*
					
					if(detail.getInitialQuantity() != null) {
						sum += detail.getInitialQuantity();
					}
					*/
					long sum = 0;				
					/*if(detail.getInitialQuantity() != null) {
						sum += detail.getInitialQuantity();
					}*/
					sum = (original.getInitialQuantity() + detail.getInitialQuantity());
					/**  [AVAYA]: 08-26-2011 Removed logic that adds Quantity for specific Material Code (End) **/
					if(original.getOrderId() != null || detail.getOrderId() == null) {
						original.setInitialQuantity(sum);
					}
					else {
						detail.setInitialQuantity(sum);
						/**  [AVAYA]: 09-09-2011 creating a new unique key to allow duplicates in the TreeMap(Start) **/
						detailMap.put(mapKey, detail);
						/**  [AVAYA]: 09-09-2011 creating a new unique key to allow duplicates in the TreeMap(End) **/
						logger.debug("&*&*&*&*&*&*&*&*&*&*&*Adding values"+mapKey+detail.getInitialQuantity());
					}
					
				}
			}
		}
		return new ArrayList<TechnicalOrderDetail>(detailMap.values());
	}
}