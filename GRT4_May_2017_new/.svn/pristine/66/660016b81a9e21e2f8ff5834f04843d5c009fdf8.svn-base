package com.grt.util;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Utiility class to formatting date.
 * 
 * @author Perficient
 *
 */
public class DateUtil implements Serializable{
	private static final long serialVersionUID = 5084424943615031121L;
	private static final Logger logger = Logger.getLogger(DateUtil.class);
	
	/**
	 * Method to format date into MM/dd/yyyy.
	 * 
	 * @param date Date
	 * @return formattedDate String
	 */
	public static String formatDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String formattedDate = new String("");

		if (date != null) {
			formattedDate = dateFormat.format(date);
		}

		return formattedDate;
	}
	
	public static Date getDateFromDateStr(String dateStr, String datePattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
		Date date = null;
		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			logger.error("error parsing date from date string " + dateStr + " using datePattern " + datePattern);
		}
		return date;
	}
	
	public static Calendar getDateFromString(String dateStr) {
		DateFormat formatter;
		Date date;
		Calendar cal = null;
		try {
			formatter = new SimpleDateFormat("MM/dd/yyyy");
			date = (Date) formatter.parse(dateStr);
			cal = Calendar.getInstance();
			cal.setTime(date);
			logger.debug("Today is " + date);
			logger.debug("Calender date" + cal.getTime());
		} catch (ParseException e) {
			logger.error("error parsing date from date string " + dateStr);
		}
		return cal;
	}
	
	public static Calendar getCurrentDate() {
		DateFormat formatter;
		Date date;
		Calendar cal = null;
		String dateStr = null;
		try {
			formatter = new SimpleDateFormat("MM/dd/yyyy");
			dateStr = formatter.format(new Date());
			date = (Date) formatter.parse(dateStr);
			cal = Calendar.getInstance();
			cal.setTime(date);
			logger.debug("Today is " + date);
			logger.debug("Calender date" + cal.getTime());
		} catch (ParseException e) {
			logger.error("error parsing date from date string " + dateStr);
		}
		return cal;
	}
	/**
	 * Testing method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(getCurrentDate());
	}
}
