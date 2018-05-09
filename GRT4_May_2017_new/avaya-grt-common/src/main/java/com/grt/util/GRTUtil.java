package com.grt.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class GRTUtil {

	/**
	 * This method returns empty list, if the list is NULL.
	 * 
	 * @param listObj
	 *            List
	 * @return listObj List
	 */
	public static List<Object> safe(List<Object> listObj) {
		return listObj == null ? Collections.EMPTY_LIST : listObj;
	}

	/**
	 * Method to print the StackTrace to String.
	 * 
	 * @param aThrowable
	 *            Throwable
	 * @return result String
	 */
	public static String getStackTrace(Exception exception) {
		try {
			Writer result = new StringWriter();
			PrintWriter printWriter = new PrintWriter(result);
			exception.printStackTrace(printWriter);
			return result.toString();
		} catch (Exception ex) {
			return "Bad stack trace...";
		}
	}

	public static String appendZerosToMaterialCode(String materialCode){
		int size=0;
		StringBuffer sb = null;
		if(!StringUtils.isEmpty(materialCode)){
			size = materialCode.length();
			sb = new StringBuffer();
			for(;size<18;size++){
				sb.append("0");
			}
			sb.append(materialCode);
		}
		if(sb!=null)
			return sb.toString();
		else
			return materialCode;
	}

	public static String trimPropertytoMaxLength(String property, int length) {
		String returnString = property;
		if (property != null && property.length() > length) {
			returnString = property.substring(0, length - 1);
		}
		return returnString;
	}

	public static List<String> removeDuplicateStringFromList(List<String> objectList){
		List<String> refinedList = new ArrayList<String>();
		if(objectList != null && objectList.size() > 0){
			for(String string : objectList){
				if(!refinedList.contains(string)){
					refinedList.add(string);
				}
			}
		}
		return refinedList;
	}

	public static String formatStringDate( String date, String currentFormat, String reqFormat ){
		String formattedDate = "";
		try{
			SimpleDateFormat sdf = new SimpleDateFormat( currentFormat );
			SimpleDateFormat newFormat = new SimpleDateFormat( reqFormat );
			Date oldDate = sdf.parse(date);
			formattedDate = newFormat.format(oldDate);
		}catch(Exception e){
			e.printStackTrace();
		}
		return formattedDate;
	}
	
	public static Date customDateFormat( Date date, String currentFormat, String reqFormat){
		Date newDate = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat( currentFormat );
			//String dateStr = sdf.format(date);
			SimpleDateFormat newFormat = new SimpleDateFormat( reqFormat );
			String test = newFormat.format(date);
			System.out.println(test);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newDate;
	}
	
	//Put Error Map in Main Map
	public static Map<String, Object> setErrorMsgInMap( String errorMessage ){
		//Handle Errors
		Map<String, Object> ibJsonMap = new HashMap<String, Object>();
		Map<String, Object> errorMap = new HashMap<String, Object>();
		errorMap.put(GRTConstants.BACKEND_ERROR_MSG, errorMessage);
		ibJsonMap.put(GRTConstants.JSON_WRAPPER, errorMap);
		return ibJsonMap;
	}
	
	public static void main(String args[]){
		String s  = "2011-02-08 09:29:30.0";
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSS" );
		
		Date d;
		try {
			d = sdf.parse(s);
			Date res = customDateFormat(d,"yyyy-MM-dd HH:mm:ss.SSS","dd MMM yyyy");
			System.out.println("result"+res);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	
}
