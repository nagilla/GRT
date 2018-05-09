package com.grt.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.avaya.grt.mappers.ExpandedSolutionElement;
import com.avaya.grt.mappers.SalProductRegistration;
import com.avaya.grt.mappers.SiteList;
import com.avaya.grt.mappers.SiteRegistration;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.grt.dto.Attachment;
import com.grt.dto.InstallBaseRespDataDto;
import com.grt.dto.LSPRegistrationDto;
import com.grt.dto.RegistrationFormBean;
import com.grt.dto.SALEmailTemplateDto;
import com.grt.dto.TechnicalOrderDetail;
import com.grt.util.SolutionElementCell;
import com.grt.util.WorsheetProcessor;

/**
 * Process the uploaded Material Entry Excel worksheet or generate a new Excel worksheet based on the TechnicalOrderDetail
 *
 * @author Perficient
 *
 */
public class TechnicalOrderDetailWorsheetProcessor extends WorsheetProcessor {

    private final Logger logger = Logger.getLogger(TechnicalOrderDetailWorsheetProcessor.class);
    //  [AVAYA]: 08-23-2011 Add Serial Number to be added to Spreadsheet (Start) -->
    private final String[] HEADER_STRINGS = {"Quantity", "Material Code", "Serial Number"};
    //  [AVAYA]: 08-23-2011 Add Serial Number to be added to Spreadsheet (End) -->
    private final String[] EQR_HEADER_STRINGS = {"Existing Installed Quantity", "Revised Installed Quantity", "Quantity to be Removed", "Active Contract?",
    		"Technically On-Boarded?", "Material Code", "Description", "Product Line", "SE Code", "SEID", "Serial Number", "Warnings/Errors"};
    private final String[] IB_HEADER_STRINGS = {"Material Code", "Installed Quantity", "Description", "Product Line", "SE Code", "SEID", "Serial Number", "Registration Status", "Contract Numbers", "Asset Nickname", "Access Type"};
    private final String[] RV_HEADER_STRINGS = {"Action" ,"Existing Installed Quantity", "Revised Installed Quantity", "Quantity to be Added/Removed", "Active Contract?",
    		"Technically On-Boarded?", "Material Code", "Description", "Product Line", "SE Code", "SEID", "Serialized?","Serial Number", "New Serial Number","Asset Nickname", "Warnings/Errors"};  
    private final String[] EM_HEADER_STRINGS = {"Existing Installed Quantity", "Quantity to be Moved", "Contract","Technical On-Boarded?", "Material Code", "Material Description",
    		"Product Line", "SE Code", "SEID", "Serial Number", "Warnings/Errors"};
    private final String ibExportSheetMessage = "PLEASE NOTE that the Install Base information on this report represents the records on the Sold-To/Functional Location at the specific date and time indicated. Any changes made to the account after that date and time will not be indicated in this report. Thus, the information contained in this report should be used for reference only.";
    
    private final String rvExportSheetMessage= "You must select action to get your row change imported. \n * Update action only pick revised quantity and serial number changes. \n * Quantity for a new row with serial number should be 1 \n * For modifying or assigning new serial number, use column \"New Serial Number\". ";
    
    private final Integer EXCEL_MAX_NO_OF_ROWS = 65536;
    /**
     * Get the TechnicalOrderDetail list based on the uploaded Material Entry Excel worksheet
     * @param fileToParse InputStream
     * @return List<TechnicalOrderDetail>
     * @throws Exception
     */
    /*
    public List<TechnicalOrderDetail> parseWorksheet(InputStream fileToParse) throws Exception {
        logger.debug("Entering parseWorksheet(InputStream fileToParse)");
        List<TechnicalOrderDetail> technicalOrderDtoList = new ArrayList<TechnicalOrderDetail>();
        Workbook wb = WorkbookFactory.create(fileToParse);
        Sheet sheet = wb.getSheetAt(0);
        Iterator<Row> ite = sheet.iterator();
        Row header = ite.next();
        if(!isValidHeader(header, HEADER_STRINGS)) {
        	throw new Exception("Invalid Worksheet");
        }
        while(ite.hasNext()) {
            Row row = ite.next();
            TechnicalOrderDetail technicalOrderDto = new TechnicalOrderDetail();
            Iterator<HSSFCell> cellIte = row.iterator();
            HSSFCell cell1 = cellIte.next();
            HSSFCell cell2 = cellIte.next();
            // [AVAYA]: 08-23-2011 Read Serial Number from uploaded Spreadsheet cell 3 (Start)
            HSSFCell cell3 = cellIte.next();
            // [AVAYA]: 08-23-2011 Read Serial Number from uploaded Spreadsheet cell 3 (End)

            if (cell1 == null || cell2 == null) {
                continue;
            }

            String initialQuantityStr = getStringValue(cell1);
            long initialQuantity = 0;
            try {
            	initialQuantity = Long.parseLong(initialQuantityStr);
            }
            catch(NumberFormatException e) {
                logger.error("NumberFormatException while parsing: " + initialQuantityStr, e);
                continue;
            }
            if(initialQuantity == 0) {
                continue;
            }
            technicalOrderDto.setInitialQuantity(initialQuantity);
        	String materialCode = getStringValue(cell2);
        	if(StringUtils.isEmpty(materialCode)) {
        		continue;
        	}
            technicalOrderDto.setMaterialCode(materialCode);

            // [AVAYA]: 08-23-2011 Read Serial Number from uploaded Spreadsheet cell 3 (Start)
            String serialNumber = getStringValue(cell3);
            // [AVAYA]: 09-20-2011 Check if serial number length is more than 18 characters, do not set to technicalOrderDto (Start)
            if(serialNumber.length() > 18) {
                continue;
            }
            // [AVAYA]: 09-20-2011 Check if serial number length is more than 18 characters, do not set to technicalOrderDto (End)
            technicalOrderDto.setSerialNumber(serialNumber);
            logger.debug("[AVAYA] Initial Quantity " + initialQuantity + " Material Code " + materialCode);
            // [AVAYA]: 08-23-2011 Read Serial Number from uploaded Spreadsheet cell 3 (End)

            technicalOrderDtoList.add(technicalOrderDto);
        }
        logger.debug("Exiting parseWorksheet(InputStream fileToParse)");
        return technicalOrderDtoList;
    }
    */

    public List<TechnicalOrderDetail> parseWorksheet(InputStream fileToParse){
    	try{
        logger.debug("Entering parseWorksheet(InputStream fileToParse) using jakarta-poi-2.5.jar");
        List<TechnicalOrderDetail> technicalOrderDtoList = new ArrayList<TechnicalOrderDetail>();
        //Workbook wb = WorkbookFactory.create(fileToParse);
        /** Create a POIFSFileSystem object**/
        POIFSFileSystem myFileSystem = new POIFSFileSystem(fileToParse);

        /** Create a workbook using the File System**/
         HSSFWorkbook wb = new HSSFWorkbook(myFileSystem);
         /** Get the first sheet from workbook**/
         HSSFSheet sheet = wb.getSheetAt(0);

         /** We now need something to iterate through the cells.**/
         Iterator rowIter = sheet.rowIterator();
         HSSFRow header = (HSSFRow) rowIter.next();
        if(!isValidHeader(header, HEADER_STRINGS)) {
        	throw new Exception("Invalid Worksheet");
        }
        while(rowIter.hasNext()) {
        	HSSFRow row = (HSSFRow)rowIter.next();
            TechnicalOrderDetail technicalOrderDto = new TechnicalOrderDetail();
            //Iterator cellIte = row.cellIterator();
            //HSSFCell cell1 = (HSSFCell)cellIte.next();
           // HSSFCell cell2 = (HSSFCell)cellIte.next();
            // [AVAYA]: 08-23-2011 Read Serial Number from uploaded Spreadsheet cell 3 (Start)
            //HSSFCell cell3 = (HSSFCell)cellIte.next();
            // [AVAYA]: 08-23-2011 Read Serial Number from uploaded Spreadsheet cell 3 (End)
            HSSFCell cell1 = row.getCell((short) 0);
            HSSFCell cell2 = row.getCell((short) 1);
            HSSFCell cell3 = row.getCell((short) 2);
           if (cell1 == null || cell2 == null) {
                continue;
            }
            String initialQuantityStr = getStringValue(cell1);
            long initialQuantity = 0;
            try {
            	if(StringUtils.isNotEmpty(initialQuantityStr)){
            		initialQuantityStr = initialQuantityStr.trim();
            		initialQuantity = Long.parseLong(initialQuantityStr);
            	}
            }
            catch(NumberFormatException e) {
                logger.error("NumberFormatException while parsing: " + initialQuantityStr, e);
                continue;
            }
            if(initialQuantity == 0) {
                continue;
            }
            technicalOrderDto.setInitialQuantity(initialQuantity);
            String materialCode = getStringValue(cell2);
        	if(StringUtils.isEmpty(materialCode)) {
        		continue;
        	}else {
        		materialCode = materialCode.trim();
        	}
        	technicalOrderDto.setMaterialCode(materialCode);
           // [AVAYA]: 08-23-2011 Read Serial Number from uploaded Spreadsheet cell 3 (Start)
            String serialNumber = getStringValue(cell3);
           // [AVAYA]: 09-20-2011 Check if serial number length is more than 18 characters, do not set to technicalOrderDto (Start)
            if(serialNumber != null && serialNumber.trim().length() > 18) {
                continue;
            }else {
            	serialNumber = serialNumber.trim();
            }

            // [AVAYA]: 09-20-2011 Check if serial number length is more than 18 characters, do not set to technicalOrderDto (End)
            technicalOrderDto.setSerialNumber(serialNumber);
            logger.debug("[AVAYA] Initial Quantity " + initialQuantity + " Material Code " + materialCode+" Serial Number "+serialNumber);
            // [AVAYA]: 08-23-2011 Read Serial Number from uploaded Spreadsheet cell 3 (End)
            technicalOrderDtoList.add(technicalOrderDto);
        }
        logger.debug("Exiting parseWorksheet(InputStream fileToParse)");
        return technicalOrderDtoList;
    	}catch(Exception ex){
    		logger.error("ERROR: While parsing excel sheet: " + ex.getMessage());
    		ex.printStackTrace();
        	return null;
        }
    }

    public List<TechnicalOrderDetail> parseWorksheetXLSX(InputStream inp){
    	try{
        logger.debug("Entering parseWorksheet(InputStream fileToParse) using jakarta-poi-2.5.jar");
        List<TechnicalOrderDetail> technicalOrderDtoList = new ArrayList<TechnicalOrderDetail>();
        //Workbook wb = WorkbookFactory.create(fileToParse);
        /** Create a POIFSFileSystem object**/
        //InputStream inp = new FileInputStream(filePath);
        /** Create a workbook using the File System**/
        XSSFWorkbook wb = new XSSFWorkbook(inp);
         /** Get the first sheet from workbook**/
        XSSFSheet sheet = wb.getSheetAt(0);
         /** We now need something to iterate through the cells.**/
         Iterator rowIter = sheet.rowIterator();
         XSSFRow header = (XSSFRow) rowIter.next();
        if(!isValidHeader(header, HEADER_STRINGS)) {
        	throw new Exception("Invalid Worksheet");
        }
        while(rowIter.hasNext()) {
        	XSSFRow row = (XSSFRow)rowIter.next();
            TechnicalOrderDetail technicalOrderDto = new TechnicalOrderDetail();
            //Iterator cellIte = row.cellIterator();
            //HSSFCell cell1 = (HSSFCell)cellIte.next();
           // HSSFCell cell2 = (HSSFCell)cellIte.next();
            // [AVAYA]: 08-23-2011 Read Serial Number from uploaded Spreadsheet cell 3 (Start)
            //HSSFCell cell3 = (HSSFCell)cellIte.next();
            // [AVAYA]: 08-23-2011 Read Serial Number from uploaded Spreadsheet cell 3 (End)
            XSSFCell cell1 = row.getCell((short) 0);
            XSSFCell cell2 = row.getCell((short) 1);
            XSSFCell cell3 = row.getCell((short) 2);
           if (cell1 == null || cell2 == null) {
                continue;
            }
            String initialQuantityStr = getStringValue(cell1);
            long initialQuantity = 0;
            if(StringUtils.isNotEmpty(initialQuantityStr)){
	            try {
	            	initialQuantityStr = initialQuantityStr.trim();
	            	initialQuantity = Long.parseLong(initialQuantityStr);
	            }
	            catch(NumberFormatException e) {
	                logger.error("NumberFormatException while parsing: " + initialQuantityStr, e);
	                continue;
	            }
            }
            if(initialQuantity == 0) {
                continue;
            }
            technicalOrderDto.setInitialQuantity(initialQuantity);
            String materialCode = getStringValue(cell2);
        	if(StringUtils.isEmpty(materialCode)) {
        		continue;
        	}else {
        		materialCode = materialCode.trim();
        	}
        	technicalOrderDto.setMaterialCode(materialCode);
           // [AVAYA]: 08-23-2011 Read Serial Number from uploaded Spreadsheet cell 3 (Start)
            String serialNumber = getStringValue(cell3);
           // [AVAYA]: 09-20-2011 Check if serial number length is more than 18 characters, do not set to technicalOrderDto (Start)
            if(serialNumber != null && serialNumber.trim().length() > 18) {
                continue;
            } else {
            	serialNumber = serialNumber.trim();
            }

            // [AVAYA]: 09-20-2011 Check if serial number length is more than 18 characters, do not set to technicalOrderDto (End)
            technicalOrderDto.setSerialNumber(serialNumber);
            logger.debug("[AVAYA] Initial Quantity " + initialQuantity + " Material Code " + materialCode+" Serial Number "+serialNumber);
            // [AVAYA]: 08-23-2011 Read Serial Number from uploaded Spreadsheet cell 3 (End)
            technicalOrderDtoList.add(technicalOrderDto);
        }
        logger.debug("Exiting parseWorksheet(InputStream fileToParse)");
        return technicalOrderDtoList;
    	}catch(Exception ex){
    		logger.error("ERROR: While parsing excel sheet: " + ex.getMessage());
    		ex.printStackTrace();
        	return null;
        }
    }
    
    public List<TechnicalOrderDetail> parseRecordValidationWorksheet(InputStream fileToParse){
    	try{
    		logger.debug("Entering TechnicalOrderDetailWorsheetProcessor.parseRecordValidationWorksheet(InputStream fileToParse)");
    		List<TechnicalOrderDetail> technicalOrderDtoList = new ArrayList<TechnicalOrderDetail>();
    		//Workbook wb = WorkbookFactory.create(fileToParse);
    		/** Create a POIFSFileSystem object**/
    		POIFSFileSystem myFileSystem = new POIFSFileSystem(fileToParse);

    		/** Create a workbook using the File System**/
    		HSSFWorkbook wb = new HSSFWorkbook(myFileSystem);
    		/** Get the first sheet from workbook**/
    		HSSFSheet sheet = wb.getSheetAt(0);

    		/** We now need something to iterate through the cells.**/
    		Iterator rowIter = sheet.rowIterator();
    		HSSFRow header = null;
    		
    		while(rowIter.hasNext()){
    			header = (HSSFRow) rowIter.next();
    			String headerString=header.getCell((short) 0).getStringCellValue();
    		
    			if(RV_HEADER_STRINGS[0].equalsIgnoreCase(headerString)){
    			break;
    			}
    		
    		}
    		if(!isValidHeader(header, RV_HEADER_STRINGS)) {
    			throw new Exception("Invalid Worksheet");
    		}
    		while(rowIter.hasNext()) {
    			HSSFRow row = (HSSFRow)rowIter.next();
    			TechnicalOrderDetail technicalOrderDto = new TechnicalOrderDetail();
    			//Iterator cellIte = row.cellIterator();
    			//HSSFCell cell1 = (HSSFCell)cellIte.next();
    			// HSSFCell cell2 = (HSSFCell)cellIte.next();
    			// [AVAYA]: 08-23-2011 Read Serial Number from uploaded Spreadsheet cell 3 (Start)
    			//HSSFCell cell3 = (HSSFCell)cellIte.next();
    			// [AVAYA]: 08-23-2011 Read Serial Number from uploaded Spreadsheet cell 3 (End)
    			HSSFCell cell1 = row.getCell((short) 0);
    			HSSFCell cell2 = row.getCell((short) 1);
    			HSSFCell cell3 = row.getCell((short) 2);
    			HSSFCell cell4 = row.getCell((short) 3);
    			HSSFCell cell5 = row.getCell((short) 4);
    			HSSFCell cell6 = row.getCell((short) 5);
    			HSSFCell cell7 = row.getCell((short) 6);
    			HSSFCell cell8 = row.getCell((short) 7);
    			HSSFCell cell9 = row.getCell((short) 8);
    			HSSFCell cell10 = row.getCell((short) 9);
    			HSSFCell cell11 = row.getCell((short) 10);
    			HSSFCell cell12 = row.getCell((short) 11);
    			HSSFCell cell13 = row.getCell((short) 12);
    			HSSFCell cell14 = row.getCell((short) 13);
    			HSSFCell cell15 = row.getCell((short) 14);
    			if (cell1 == null ) {
    				continue;
    			}
    			String actionType = getStringValue(cell1).trim();
    			if (null == actionType || !actionType.equalsIgnoreCase("New") && !actionType.equalsIgnoreCase("Update")) {
    				continue;
    			}
    			if(actionType.equalsIgnoreCase("New")){
    				technicalOrderDto.setActionType(GRTConstants.ACTION_TYPE_A);
    			}
    			long initialQuantity = 0;
    			
    			if(! GRTConstants.ACTION_TYPE_A.equalsIgnoreCase(technicalOrderDto.getActionType())) {
    				String initialQuantityStr = getStringValue(cell2);
    				technicalOrderDto.setInitialQuantity(initialQuantity);
    				if(StringUtils.isNotEmpty(initialQuantityStr)){
    					try {
    						initialQuantityStr = initialQuantityStr.trim();
    						initialQuantity = Long.parseLong(initialQuantityStr);
    						if(initialQuantity < 0) {
    							throw new NumberFormatException();
    						}
    						technicalOrderDto.setInitialQuantity(initialQuantity);
    					}
    					catch(NumberFormatException e) {
    						logger.error("NumberFormatException while parsing: " + initialQuantityStr, e);
    						technicalOrderDto.setInitialQuantity(null);
    					}
    				}
    			} else {
    				String serialNumber = getStringValue(cell14);
        			if(serialNumber != null ) {
        				serialNumber = serialNumber.trim();
        			}
        			technicalOrderDto.setSerialNumber(serialNumber);
    			}
    			
    			String materialCode = getStringValue(cell7);
    			if(StringUtils.isEmpty(materialCode)) {
    				continue;
    			}else {
    				materialCode = materialCode.trim();
    			}
    			technicalOrderDto.setMaterialCode(materialCode);
    			
    			String remainingQuantityStr = getStringValue(cell3);
				long remainingQuantity = -1;
				//technicalOrderDto.setRemainingQuantity(remainingQuantity);
				if(StringUtils.isNotEmpty(remainingQuantityStr)){
					try {
						remainingQuantityStr = remainingQuantityStr.trim();
						remainingQuantity = Long.parseLong(remainingQuantityStr);
						if(remainingQuantity < 0) {
							throw new NumberFormatException();
						}
						technicalOrderDto.setRemainingQuantity(remainingQuantity);
						if(GRTConstants.ACTION_TYPE_A.equalsIgnoreCase(technicalOrderDto.getActionType())) {
							technicalOrderDto.setInitialQuantity(remainingQuantity);
						}
					}
					catch(NumberFormatException e) {
						logger.error("NumberFormatException while parsing: " + remainingQuantityStr, e);
						//technicalOrderDto.setRemainingQuantity(-1L);
						technicalOrderDto.setRemainingQuantity(-1L);
						remainingQuantityStr = null;
						//continue;
					}
				}
				 				
				long revisedQuantity = -1;
				if(null != remainingQuantityStr && StringUtils.isNotEmpty(remainingQuantityStr)) {
					if(remainingQuantity > initialQuantity) {
						revisedQuantity = remainingQuantity - initialQuantity;
					} else if(remainingQuantity <= initialQuantity) {
						revisedQuantity = initialQuantity - remainingQuantity;
					}
					Long revisedQtyLong = new Long(revisedQuantity);
					technicalOrderDto.setRemovedQuantity(revisedQtyLong);
				}
				
    			
    			if(actionType.equalsIgnoreCase("Update")) {
    				technicalOrderDto.setActionType(GRTConstants.ACTION_TYPE_U);
    				
    				String serialNumber = getStringValue(cell13);
        			if(serialNumber != null ) {
        				serialNumber = serialNumber.trim();
        			}
        			technicalOrderDto.setSerialNumber(serialNumber);
    				
    				String newSerialNumber = getStringValue(cell14);
    				if(newSerialNumber != null && StringUtils.isNotEmpty(newSerialNumber)) {
    					newSerialNumber = newSerialNumber.trim();
    					technicalOrderDto.setNewSerialNumber(newSerialNumber);
        				//technicalOrderDto.setSerialNumber(newSerialNumber);		
        				technicalOrderDto.setActionType(GRTConstants.ACTION_TYPE_S);
    				}
    				  				
    				if(revisedQuantity > -1) {
    					technicalOrderDto.setActionType(GRTConstants.ACTION_TYPE_U);
    				}
    				
    				String activeContractExist = getStringValue(cell5);
    				if(activeContractExist != null && ! StringUtils.isEmpty(activeContractExist)) {
    					activeContractExist = activeContractExist.trim();
    				}
    				technicalOrderDto.setActiveContractExist(activeContractExist);
    				
    				String technicallyRegisterable = getStringValue(cell6);
    				if(technicallyRegisterable != null && ! StringUtils.isEmpty(technicallyRegisterable)) {
    					technicallyRegisterable = technicallyRegisterable.trim();
    				}
    				technicalOrderDto.setTechnicallyRegisterable(technicallyRegisterable);
    				
    				String productLine = getStringValue(cell9);
    				if(productLine != null && ! StringUtils.isEmpty(productLine)) {
    					productLine = productLine.trim();
    				}
    				technicalOrderDto.setProductLine(productLine);

    				String solutionElementCode = getStringValue(cell10);
    				if(solutionElementCode != null && ! StringUtils.isEmpty(productLine)) {
    					solutionElementCode = solutionElementCode.trim();
    				}
    				technicalOrderDto.setSolutionElementCode(solutionElementCode);

    				String solutionElementId = getStringValue(cell11);
    				if(solutionElementId != null && ! StringUtils.isEmpty(productLine)) {
    					solutionElementId = solutionElementId.trim();
    				}
    				technicalOrderDto.setSolutionElementId(solutionElementId);

    				String serialized = getStringValue(cell12);
    				if(serialized != null && ! StringUtils.isEmpty(serialized)) {
    					serialized = serialized.trim();
    				}
    				technicalOrderDto.setSerialized(serialized);	
    				
    				String errorDesc = getStringValue(cell15);
    				if(errorDesc != null && ! StringUtils.isEmpty(errorDesc)) {
    					errorDesc = errorDesc.trim();
    				}
    				technicalOrderDto.setErrorDescription(errorDesc);	
    			}			
    			technicalOrderDtoList.add(technicalOrderDto);
    		}
    		logger.debug("Exiting TechnicalOrderDetailWorsheetProcessor.parseRecordValidationWorksheet(InputStream fileToParse)");
    		return technicalOrderDtoList;
    	}catch(Exception ex){
    		logger.error("Exception in TechnicalOrderDetailWorsheetProcessor.parseRecordValidationWorksheet(InputStream inp) : ", ex);
    		ex.printStackTrace();
    		return null;
    	}
    }

    public List<TechnicalOrderDetail> parseRecordValidationWorksheetXLSX(InputStream inp){
    	try{
    		logger.debug("Entering TechnicalOrderDetailWorsheetProcessor.parseRecordValidationWorksheetXLSX(InputStream inp)");
    		List<TechnicalOrderDetail> technicalOrderDtoList = new ArrayList<TechnicalOrderDetail>();
    		//Workbook wb = WorkbookFactory.create(fileToParse);
    		/** Create a POIFSFileSystem object**/
    		//InputStream inp = new FileInputStream(filePath);
    		/** Create a workbook using the File System**/
    		XSSFWorkbook wb = new XSSFWorkbook(inp);
    		/** Get the first sheet from workbook**/
    		XSSFSheet sheet = wb.getSheetAt(0);
    		/** We now need something to iterate through the cells.**/
    		Iterator rowIter = sheet.rowIterator();
    		XSSFRow header = (XSSFRow) rowIter.next();
    		if(!isValidHeader(header, RV_HEADER_STRINGS)) {
    			throw new Exception("Invalid Worksheet");
    		}
    		while(rowIter.hasNext()) {
    			XSSFRow row = (XSSFRow)rowIter.next();
    			TechnicalOrderDetail technicalOrderDto = new TechnicalOrderDetail();
    			XSSFCell cell1 = row.getCell((short) 0);
    			XSSFCell cell2 = row.getCell((short) 1);
    			XSSFCell cell3 = row.getCell((short) 2);
    			XSSFCell cell4 = row.getCell((short) 3);
    			XSSFCell cell5 = row.getCell((short) 4);
    			XSSFCell cell6 = row.getCell((short) 5);
    			XSSFCell cell7 = row.getCell((short) 6);
    			//XSSFCell cell8 = row.getCell((short) 7);
    			XSSFCell cell9 = row.getCell((short) 8);
    			XSSFCell cell10 = row.getCell((short) 9);
    			XSSFCell cell11 = row.getCell((short) 10);
    			XSSFCell cell12 = row.getCell((short) 11);
    			XSSFCell cell13 = row.getCell((short) 12);
    			XSSFCell cell14 = row.getCell((short) 13);
    			XSSFCell cell15 = row.getCell((short) 14);
    			if (cell1 == null ) {
    				continue;
    			}
    			String actionType = getStringValue(cell1).trim();
    			if(null == actionType || !actionType.equalsIgnoreCase("New") && !actionType.equalsIgnoreCase("Update"))
    			{
    				continue;
    			}
    			
    			if(actionType.equalsIgnoreCase("New")){
    				technicalOrderDto.setActionType("A");
    			}
    			
    			String initialQuantityStr = getStringValue(cell2);
    			long initialQuantity = 0;
    			if(StringUtils.isNotEmpty(initialQuantityStr)){
    				try {
    					initialQuantityStr = initialQuantityStr.trim();
    					initialQuantity = Long.parseLong(initialQuantityStr);
    					technicalOrderDto.setInitialQuantity(initialQuantity);
    				}
    				catch(NumberFormatException e) {
    					logger.error("NumberFormatException while parsing: " + initialQuantityStr, e);
    					technicalOrderDto.setInitialQuantity(-1L);
    				}
    			}
    			if(initialQuantity == 0) {
    				//continue;
    			}
    			technicalOrderDto.setInitialQuantity(initialQuantity);
    			
    			String materialCode = getStringValue(cell7);
    			if(StringUtils.isEmpty(materialCode)) {
    				continue;
    			}else {
    				materialCode = materialCode.trim();
    			}
    			technicalOrderDto.setMaterialCode(materialCode);
    			// [AVAYA]: 08-23-2011 Read Serial Number from uploaded Spreadsheet cell 3 (Start)
    			String serialNumber = getStringValue(cell13);
    			// [AVAYA]: 09-20-2011 Check if serial number length is more than 18 characters, do not set to technicalOrderDto (Start)
    			if(serialNumber != null && serialNumber.trim().length() > 18) {
    				continue;
    			} else {
    				serialNumber = serialNumber.trim();
    			}

    			// [AVAYA]: 09-20-2011 Check if serial number length is more than 18 characters, do not set to technicalOrderDto (End)
    			technicalOrderDto.setSerialNumber(serialNumber);
    			//logger.debug("[AVAYA] Initial Quantity " + initialQuantity + " Material Code " + materialCode+" Serial Number "+serialNumber);
    			// [AVAYA]: 08-23-2011 Read Serial Number from uploaded Spreadsheet cell 3 (End)
    			if(actionType.equalsIgnoreCase("Update")) {
    				String newSerialNumber = getStringValue(cell14);
    				if(newSerialNumber != null && newSerialNumber.trim().length() > 18) {
    					continue;
    				} else {
    					newSerialNumber = newSerialNumber.trim();
    					technicalOrderDto.setNewSerialNumber(newSerialNumber);
        				//technicalOrderDto.setSerialNumber(newSerialNumber);		
        				technicalOrderDto.setActionType("S");
    				}
    				
    				
    				String remainingQuantityStr = getStringValue(cell3);
    				long remainingQuantity = 0;
    				if(StringUtils.isNotEmpty(remainingQuantityStr)){
    					try {
    						remainingQuantityStr = remainingQuantityStr.trim();
    						remainingQuantity = Long.parseLong(remainingQuantityStr);
    						technicalOrderDto.setRemainingQuantity(remainingQuantity);
    					}
    					catch(NumberFormatException e) {
    						logger.error("NumberFormatException while parsing: " + remainingQuantityStr, e);
    						technicalOrderDto.setRemainingQuantity(-1L);
    						//continue;
    					}
    				}
    				technicalOrderDto.setRemainingQuantity(remainingQuantity);
    								
    				long revisedQuantity = -1;
    				if(remainingQuantity > initialQuantity) {
    					revisedQuantity = remainingQuantity - initialQuantity;
    				} else if(remainingQuantity <= initialQuantity) {
    					revisedQuantity = initialQuantity - remainingQuantity;
    				}
    				technicalOrderDto.setRemovedQuantity(revisedQuantity);
    				
    				if(revisedQuantity > -1) {
    					technicalOrderDto.setActionType("U");
    				}
    				
    				String activeContractExist = getStringValue(cell5);
    				if(activeContractExist != null && ! StringUtils.isEmpty(activeContractExist)) {
    					activeContractExist = activeContractExist.trim();
    				}
    				technicalOrderDto.setActiveContractExist(activeContractExist);
    				
    				String technicallyRegisterable = getStringValue(cell6);
    				if(technicallyRegisterable != null && ! StringUtils.isEmpty(technicallyRegisterable)) {
    					technicallyRegisterable = technicallyRegisterable.trim();
    				}
    				technicalOrderDto.setTechnicallyRegisterable(technicallyRegisterable);
    				
    				String productLine = getStringValue(cell9);
    				if(productLine != null && ! StringUtils.isEmpty(productLine)) {
    					productLine = productLine.trim();
    				}
    				technicalOrderDto.setProductLine(productLine);

    				String solutionElementCode = getStringValue(cell10);
    				if(solutionElementCode != null && ! StringUtils.isEmpty(productLine)) {
    					solutionElementCode = solutionElementCode.trim();
    				}
    				technicalOrderDto.setSolutionElementCode(solutionElementCode);

    				String solutionElementId = getStringValue(cell11);
    				if(solutionElementId != null && ! StringUtils.isEmpty(productLine)) {
    					solutionElementId = solutionElementId.trim();
    				}
    				technicalOrderDto.setSolutionElementId(solutionElementId);

    				String serialized = getStringValue(cell12);
    				if(serialized != null && ! StringUtils.isEmpty(serialized)) {
    					serialized = serialized.trim();
    				}
    				technicalOrderDto.setSerialized(serialized);
    				
    				String errorDesc = getStringValue(cell15);
    				if(errorDesc != null && ! StringUtils.isEmpty(errorDesc)) {
    					errorDesc = errorDesc.trim();
    				}
    				technicalOrderDto.setErrorDescription(errorDesc);	
    			}		
    			technicalOrderDtoList.add(technicalOrderDto);
    		}
    		logger.debug("Exiting TechnicalOrderDetailWorsheetProcessor.parseRecordValidationWorksheetXLSX(InputStream inp)");
    		return technicalOrderDtoList;
    	}catch(Exception ex){
    		logger.error("Exception in TechnicalOrderDetailWorsheetProcessor.parseRecordValidationWorksheetXLSX(InputStream inp) : ", ex);
    		ex.printStackTrace();
    		return null;
    	}
    }

    /*
	private String getStringValue(HSSFCell cell) {
		String result = "";
		if(HSSFCell.CELL_TYPE_NUMERIC == cell.getCellType()) {
			result = String.valueOf((long)cell.getNumericCellValue());
		}
		else if(HSSFCell.CELL_TYPE_STRING == cell.getCellType()) {
			result = cell.getStringCellValue();
		}
		return result;
	}
	*/
	private String getStringValue(HSSFCell cell) {

		String result = "";
		if(cell != null && cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
			result = String.valueOf((long)cell.getNumericCellValue());
		}
		else if(cell != null && cell.getCellType()== HSSFCell.CELL_TYPE_STRING ) {
			result = cell.getStringCellValue();
		}else if(cell != null && cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			result = "";
		}
		//logger.debug(" Cell Type is "+cell.getCellType());
		return result;
	}
	private String getStringValue(XSSFCell cell) {

		String result = "";
		if(cell != null && cell.getCellType()==XSSFCell.CELL_TYPE_NUMERIC) {
			result = String.valueOf((long)cell.getNumericCellValue());
		}
		else if(cell != null && cell.getCellType()== XSSFCell.CELL_TYPE_STRING ) {
			result = cell.getStringCellValue();
		}else if(cell != null && cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
			result = "";
		}
		//logger.debug(" Cell Type is "+cell.getCellType());
		return result;
	}

    /**
     * Get the TechnicalOrderDetail list based on the uploaded Material Entry Excel worksheet
     * @param filePath String
     * @return List<TechnicalOrderDetail>
     * @throws Exception
     */

    public List<TechnicalOrderDetail> parseWorksheet(String filePath) throws Exception {
        logger.debug("parse worksheet from file using new API: " + filePath);
        InputStream inp = new FileInputStream(filePath);
        List<TechnicalOrderDetail> technicalOrderDtoList = null;
        String fileExtn  = getFileExtension(filePath);
        logger.debug("RegistrationController upload File information::fileExfilePathtn:"+filePath+":fileExtn:"+fileExtn);
    	if (fileExtn != null && fileExtn.equalsIgnoreCase("xlsx")){
    		technicalOrderDtoList = parseWorksheetXLSX(inp);
	     }else if (fileExtn != null && fileExtn.equalsIgnoreCase("xls")){
	      	technicalOrderDtoList = parseWorksheet(inp);
	    }

        //List<TechnicalOrderDetail> technicalOrderDtoList = parseWorksheet(inp);
        inp.close();

        logger.debug("TechnicalOrderDetail list size: " + technicalOrderDtoList.size());
        return technicalOrderDtoList;
    }
    private String getFileExtension(String fName){
        int mid= fName.lastIndexOf(".");
        String ext=fName.substring(mid+1,fName.length());
        return ext;
    }

    /**
     * Generate Excel worksheet in the specific location based on the TechnicalOrderDetail list
     * @param technicalOrderDtoList List<TechnicalOrderDetail>
     * @param filePath String
     * @return filePath String
     * @throws Exception
     */
    public Attachment generateWorksheet(List<TechnicalOrderDetail> technicalOrderDtoList, String filePath){
    	try{
        logger.debug("TechnicalOrderDetail list size is using new API : " + technicalOrderDtoList.size());
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("new sheet");

        HSSFCellStyle headerStyle = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(font);

        // Create a header row.
        HSSFRow headerRow = sheet.createRow(0);
        HSSFCell cell = headerRow.createCell((short) 0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Quantity");

        cell = headerRow.createCell((short) 1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Material Code");

        cell = headerRow.createCell((short) 2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Description");

        // [AVAYA]: 08-23-2011 Add Serial Number header line of spreadsheet (Start)
        cell = headerRow.createCell((short) 3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Serial Number");
        // [AVAYA]: 08-23-2011 Add Serial Number header line of spreadsheet (End)

        int rowIndex = 1;
        for(TechnicalOrderDetail detail : technicalOrderDtoList) {
            if(detail.getInitialQuantity() == null || detail.getInitialQuantity() == 0) {
            	// [AVAYA]: 09-26-2011 log enhancement (Start)
            	logger.debug("detail.getInitialQuantity is null or 0");
            	// [AVAYA]: 09-26-2011 log enhancement (End)
                continue;
            }
            logger.debug("detail.getInitialQuantity is " + detail.getInitialQuantity());
            HSSFRow row = sheet.createRow(rowIndex++);
            cell = row.createCell((short) 0);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(detail.getInitialQuantity());

            logger.debug("detail.getMaterialCode() is " + detail.getMaterialCode());
            cell = row.createCell((short) 1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getMaterialCode());

            logger.debug("detail.getDescription() is " + detail.getDescription());
            cell = row.createCell((short) 2);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getDescription());

            // [AVAYA]: 08-23-2011 Add Serial Number value in the current row of the spreadsheet (Start)
            logger.debug("detail.getDescription() is " + detail.getSerialNumber());
            cell = row.createCell((short) 3);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getSerialNumber());
            // [AVAYA]: 08-23-2011 Add Serial Number value in the current row of the spreadsheet (End)
        }

        logger.debug("autoSizeColumns start");
        sheet.setDefaultColumnWidth((short)0);
        sheet.setDefaultColumnWidth((short)1);
        sheet.setDefaultColumnWidth((short)2);
        // [AVAYA]: 08-23-2011 Add Serial Number value in the current row of the spreadsheet (Start)
        sheet.setDefaultColumnWidth((short)3);
        // [AVAYA]: 08-23-2011 Add Serial Number value in the current row of the spreadsheet (End)
        logger.debug("autoSizeColumns end");
        // [AVAYA]: 09-26-2011 log enhancement (Start)
        logger.debug("about to call generateAttachment");
        // [AVAYA]: 09-26-2011 log enhancement (End)
        Attachment attachment = generateAttachment(wb, filePath);

        return attachment;
    	}catch(Exception ex){
    		logger.error("ERROR: While creating excel sheet: " + ex.getMessage());
    		ex.printStackTrace();
        	return null;
        }
    }

    public void generateEQRWorksheet(List<TechnicalOrderDetail> technicalOrderDtoList, String filePath) throws Exception {
        logger.debug("TechnicalOrderDetail list size using new API: " + technicalOrderDtoList.size());
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Record Validation");
        HSSFCellStyle headerStyle = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(font);
        headerStyle.setWrapText(true);
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        // Create a header row.
        /*HSSFRow headerRow = sheet.createRow(0);
        HSSFCell cell = headerRow.createCell(0, 5);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Registration ID:"+technicalOrderDtoList.get(0).getRegistrationId());

        cell = headerRow.createCell(6, 11);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Sold To:"+technicalOrderDtoList.get(0).getSolutionElementId());*/

        // Create a header row.
        HSSFRow headerRow = sheet.createRow(0);
        HSSFCell cell = headerRow.createCell((short)0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(EQR_HEADER_STRINGS[0]);

        cell = headerRow.createCell((short)1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(EQR_HEADER_STRINGS[1]);

        cell = headerRow.createCell((short)2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(EQR_HEADER_STRINGS[2]);

        cell = headerRow.createCell((short)3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(EQR_HEADER_STRINGS[3]);

        cell = headerRow.createCell((short)4);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(EQR_HEADER_STRINGS[4]);

        cell = headerRow.createCell((short)5);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(EQR_HEADER_STRINGS[5]);

        cell = headerRow.createCell((short)6);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(EQR_HEADER_STRINGS[6]);

        cell = headerRow.createCell((short)7);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(EQR_HEADER_STRINGS[7]);

        cell = headerRow.createCell((short)8);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(EQR_HEADER_STRINGS[8]);

        cell = headerRow.createCell((short)9);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(EQR_HEADER_STRINGS[9]);

        cell = headerRow.createCell((short)10);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(EQR_HEADER_STRINGS[10]);

        cell = headerRow.createCell((short)11);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(EQR_HEADER_STRINGS[11]);
        logger.debug("Header row created...");
        int rowIndex = 1;
        for(TechnicalOrderDetail detail : technicalOrderDtoList) {
            HSSFRow row = sheet.createRow(rowIndex++);
            cell = row.createCell((short)0);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(detail.getInitialQuantity());
            //logger.debug(rowIndex+"  :  "+cell.getNumericCellValue());
            cell = row.createCell((short)1);
            if(detail.getRemainingQuantity()!=null){
            	cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            	cell.setCellValue(detail.getRemainingQuantity());
            } else {
            	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            	cell.setCellValue("");
            }
            //logger.debug(rowIndex+"  :  "+cell.getNumericCellValue());
            cell = row.createCell((short)2);
            if(detail.getRemovedQuantity()!=null){
	            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		        cell.setCellValue(detail.getRemovedQuantity());
            } else {
            	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        cell.setCellValue("");
            }
            //logger.debug(rowIndex+"  :  "+cell.getNumericCellValue());
            cell = row.createCell((short)3);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getActiveContractExist()!=null?detail.getActiveContractExist():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)4);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getTechnicallyRegisterable()!=null?detail.getTechnicallyRegisterable():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)5);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getMaterialCode()!=null?detail.getMaterialCode():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)6);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getDescription()!=null?detail.getDescription():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)7);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getProductLine()!=null?detail.getProductLine():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)8);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getSolutionElementCode()!=null?detail.getSolutionElementCode():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)9);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getSolutionElementId()!=null?detail.getSolutionElementId():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)10);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getSerialNumber()!=null?detail.getSerialNumber():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)11);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getErrorDescription()!=null?detail.getErrorDescription():"");
        }
        logger.debug("After data rows creation completed...");
        sheet.setColumnWidth((short)0, 3000);
        sheet.setColumnWidth((short)1, 3000);
        sheet.setColumnWidth((short)2, 3000);
        sheet.setColumnWidth((short)3, 3000);
        sheet.setColumnWidth((short)4, 3000);
        sheet.setColumnWidth((short)5, 3000);
        sheet.setColumnWidth((short)6, 6000);
        sheet.setColumnWidth((short)7, 5000);
        sheet.setColumnWidth((short)8, 5000);
        sheet.setColumnWidth((short)9, 5000);
        sheet.setColumnWidth((short)10, 3000);
        sheet.setColumnWidth((short)11, 3000);

        // Writing workbook created to FileOutputStream
        FileOutputStream fileOut = new FileOutputStream(new File(filePath));
        logger.debug("After FileOutputStream creation completed...");
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        logger.debug("generateEQRWorksheet ENDS");
        // For opening the created Excel

    }
    
    public void generateRVWorksheet(List<TechnicalOrderDetail> technicalOrderDtoList, String filePath) throws Exception {
        logger.debug("TechnicalOrderDetail list size using new API: " + technicalOrderDtoList.size());
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Record Validation");
        HSSFCellStyle headerStyle = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(font);
        headerStyle.setWrapText(true);
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        // Create a header row.
        /*HSSFRow headerRow = sheet.createRow(0);
        HSSFCell cell = headerRow.createCell(0, 5);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Registration ID:"+technicalOrderDtoList.get(0).getRegistrationId());

        cell = headerRow.createCell(6, 11);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Sold To:"+technicalOrderDtoList.get(0).getSolutionElementId());*/
        
        HSSFRow row1 = sheet.createRow(0);
        HSSFCell cell1 = row1.createCell((short)0);
        cell1.setCellStyle(headerStyle);
        cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell1.setCellValue("Disclaimer");
        HSSFCell cell2 = row1.createCell((short)1);
        cell2.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell2.setCellValue(rvExportSheetMessage);
        
        HSSFCellStyle cs = wb.createCellStyle();
        cs.setWrapText(true);
        cell2.setCellStyle(cs);

        // Create a header row.
        HSSFRow headerRow = sheet.createRow(2);
        //HSSFCell existingCell = headerRow.createCell((short)0);
        //HSSFCell addedCell = headerRow.createCell((short)0);
        HSSFCell cell = headerRow.createCell((short)0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[0]);

        cell = headerRow.createCell((short)1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[1]);

        cell = headerRow.createCell((short)2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[2]);

        cell = headerRow.createCell((short)3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[3]);

        cell = headerRow.createCell((short)4);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[4]);

        cell = headerRow.createCell((short)5);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[5]);

        cell = headerRow.createCell((short)6);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[6]);

        cell = headerRow.createCell((short)7);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[7]);

        cell = headerRow.createCell((short)8);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[8]);

        cell = headerRow.createCell((short)9);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[9]);

        cell = headerRow.createCell((short)10);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[10]);

        cell = headerRow.createCell((short)11);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[11]);
        
        cell = headerRow.createCell((short)12);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[12]);
        
        cell = headerRow.createCell((short)13);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[13]);
        
        cell = headerRow.createCell((short)14);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[14]);
        
        cell = headerRow.createCell((short)15);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[15]);
        logger.debug("Header row created...");
        int rowIndex = 3;
        //Creating the drop down for action.
        
        //int lastRowIndex = rowIndex+technicalOrderDtoList.size();
        int lastRowIndex = EXCEL_MAX_NO_OF_ROWS - rowIndex;
        //Add drop down New and Update from header to last row in excel
        CellRangeAddressList addressList = new CellRangeAddressList(rowIndex - 1, lastRowIndex, 0, 0);
        DVConstraint dvConstraint = DVConstraint.createExplicitListConstraint(new String[] { "New", "Update" });
        DataValidation dataValidation = new HSSFDataValidation(addressList,dvConstraint);
        dataValidation.setSuppressDropDownArrow(false);
        sheet.addValidationData(dataValidation);        
        
        for(TechnicalOrderDetail detail : technicalOrderDtoList) {
            HSSFRow row = sheet.createRow(rowIndex++);
            cell = row.createCell((short)1);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(detail.getInitialQuantity());
            //logger.debug(rowIndex+"  :  "+cell.getNumericCellValue());
            cell = row.createCell((short)2);
            if(detail.getRemainingQuantity()!=null){
            	cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            	cell.setCellValue(detail.getRemainingQuantity());
            } else {
            	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            	cell.setCellValue("");
            }
            //logger.debug(rowIndex+"  :  "+cell.getNumericCellValue());
            cell = row.createCell((short)3);
            if(detail.getRemovedQuantity()!=null){
	            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		        cell.setCellValue(detail.getRemovedQuantity());
		        String strFormula= "IF(ISERROR(C"+rowIndex+"-B"+rowIndex+ "), \"\",C"+rowIndex+"-B"+rowIndex + ")";
	            cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
	            cell.setCellFormula(strFormula);
            } else {
            	cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		        //cell.setCellValue(0);            	
            	String strFormula= "IF(ISERROR(C"+rowIndex+"-B"+rowIndex+ "), \"\",C"+rowIndex+"-B"+rowIndex + ")";
	            cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
	            cell.setCellFormula(strFormula);
            }
            //logger.debug(rowIndex+"  :  "+cell.getNumericCellValue());
            cell = row.createCell((short)4);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getActiveContractExist()!=null?detail.getActiveContractExist():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)5);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getTechnicallyRegisterable()!=null?detail.getTechnicallyRegisterable():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)6);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getMaterialCode()!=null?detail.getMaterialCode():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)7);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getDescription()!=null?detail.getDescription():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)8);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getProductLine()!=null?detail.getProductLine():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)9);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getSolutionElementCode()!=null?detail.getSolutionElementCode():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)10);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getSolutionElementId()!=null?detail.getSolutionElementId():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)11);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getSerialized()!=null?detail.getSerialized():"N");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)12);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getSerialNumber()!=null?detail.getSerialNumber():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)13);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getNewSerialNumber()!=null?detail.getNewSerialNumber():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)14);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getNickName()!=null?detail.getNickName():"");
            
            cell = row.createCell((short)15);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getErrorDescription()!=null?detail.getErrorDescription():"");
        }
        logger.debug("After data rows creation completed...");
        sheet.setColumnWidth((short)0, 3000);
        sheet.setColumnWidth((short)1, 10000);
        sheet.setColumnWidth((short)2, 3000);
        sheet.setColumnWidth((short)3, 3000);
        sheet.setColumnWidth((short)4, 3000);
        sheet.setColumnWidth((short)5, 3000);
        sheet.setColumnWidth((short)6, 6000);
        sheet.setColumnWidth((short)7, 5000);
        sheet.setColumnWidth((short)8, 5000);
        sheet.setColumnWidth((short)9, 5000);
        sheet.setColumnWidth((short)10, 3000);
        sheet.setColumnWidth((short)11, 3000);
        sheet.setColumnWidth((short)12, 3000);
        sheet.setColumnWidth((short)13, 3000);
        sheet.setColumnWidth((short)14, 3000);
        sheet.setColumnWidth((short)15, 3000);

        // Writing workbook created to FileOutputStream
        FileOutputStream fileOut = new FileOutputStream(new File(filePath));
        logger.debug("After FileOutputStream creation completed...");
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        logger.debug("generateRecordValidationWorksheet ENDS");
        // For opening the created Excel

    }
    
    public void generateRVTemplateWorksheet(String filePath) throws Exception {
        logger.debug("TechnicalOrderDetail list size using new API: ");
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Record Validation Template");
        HSSFCellStyle headerStyle = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(font);
        headerStyle.setWrapText(true);
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        // Create a header row.
        /*HSSFRow headerRow = sheet.createRow(0);
        HSSFCell cell = headerRow.createCell(0, 5);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Registration ID:"+technicalOrderDtoList.get(0).getRegistrationId());

        cell = headerRow.createCell(6, 11);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Sold To:"+technicalOrderDtoList.get(0).getSolutionElementId());*/
        
        HSSFRow row1 = sheet.createRow(0);
        HSSFCell cell1 = row1.createCell((short)0);
        cell1.setCellStyle(headerStyle);
        cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell1.setCellValue("Disclaimer");
        HSSFCell cell2 = row1.createCell((short)1);
        cell2.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell2.setCellValue(rvExportSheetMessage);
        
        HSSFCellStyle cs = wb.createCellStyle();
        cs.setWrapText(true);
        cell2.setCellStyle(cs);

        // Create a header row.
        HSSFRow headerRow = sheet.createRow(2);
        HSSFCell cell = headerRow.createCell((short)0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[0]);

        cell = headerRow.createCell((short)1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[1]);

        cell = headerRow.createCell((short)2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[2]);

        cell = headerRow.createCell((short)3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[3]);

        cell = headerRow.createCell((short)4);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[4]);

        cell = headerRow.createCell((short)5);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[5]);

        cell = headerRow.createCell((short)6);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[6]);

        cell = headerRow.createCell((short)7);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[7]);

        cell = headerRow.createCell((short)8);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[8]);

        cell = headerRow.createCell((short)9);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[9]);

        cell = headerRow.createCell((short)10);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[10]);

        cell = headerRow.createCell((short)11);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[11]);
        
        cell = headerRow.createCell((short)12);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[12]);
        
        cell = headerRow.createCell((short)13);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[13]);
        
        cell = headerRow.createCell((short)14);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[14]);
        
        cell = headerRow.createCell((short)15);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(RV_HEADER_STRINGS[15]);
        logger.debug("Header row created...");
        int rowIndex = 3;
        //Creating the drop down for action.
        
        CellRangeAddressList addressList = new CellRangeAddressList(rowIndex, 100, 0, 0);
        DVConstraint dvConstraint = DVConstraint.createExplicitListConstraint(new String[] { "New", "Update" });
        DataValidation dataValidation = new HSSFDataValidation(addressList,dvConstraint);
        dataValidation.setSuppressDropDownArrow(false);
        sheet.addValidationData(dataValidation);   

        for(rowIndex = 3 ; rowIndex < 100;) {
        	HSSFRow row = sheet.createRow(rowIndex++);
        	cell = row.createCell((short)3);

        	cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        	//cell.setCellValue(0);
        	String strFormula= "(C"+rowIndex+"-B"+rowIndex+")";
        	cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        	cell.setCellFormula(strFormula);
        }
        
        logger.debug("After data rows creation completed...");
        sheet.setColumnWidth((short)0, 3000);
        sheet.setColumnWidth((short)1, 10000);
        sheet.setColumnWidth((short)2, 3000);
        sheet.setColumnWidth((short)3, 3000);
        sheet.setColumnWidth((short)4, 3000);
        sheet.setColumnWidth((short)5, 3000);
        sheet.setColumnWidth((short)6, 6000);
        sheet.setColumnWidth((short)7, 5000);
        sheet.setColumnWidth((short)8, 5000);
        sheet.setColumnWidth((short)9, 5000);
        sheet.setColumnWidth((short)10, 3000);
        sheet.setColumnWidth((short)11, 3000);
        sheet.setColumnWidth((short)12, 3000);
        sheet.setColumnWidth((short)13, 3000);
        sheet.setColumnWidth((short)14, 3000);
        sheet.setColumnWidth((short)15, 3000);

        // Writing workbook created to FileOutputStream
        FileOutputStream fileOut = new FileOutputStream(new File(filePath));
        logger.debug("After FileOutputStream creation completed...");
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        logger.debug("generateRecordValidationWorksheet ENDS");
        // For opening the created Excel

    }
    
    //Method to create the Equipment Move export Excel.
    public void generateEquipmentMoveWorksheet(List<TechnicalOrderDetail> technicalOrderDtoList, String filePath) throws Exception {
        logger.debug("TechnicalOrderDetail list size using new API: " + technicalOrderDtoList.size());
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Equipment Move");
        HSSFCellStyle headerStyle = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(font);
        headerStyle.setWrapText(true);
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        // Create a header row.
        HSSFRow headerRow = sheet.createRow(0);
        HSSFCell cell = headerRow.createCell((short)0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(EM_HEADER_STRINGS[0]);

        cell = headerRow.createCell((short)1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(EM_HEADER_STRINGS[1]);

        cell = headerRow.createCell((short)2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(EM_HEADER_STRINGS[2]);

        cell = headerRow.createCell((short)3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(EM_HEADER_STRINGS[3]);

        cell = headerRow.createCell((short)4);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(EM_HEADER_STRINGS[4]);

        cell = headerRow.createCell((short)5);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(EM_HEADER_STRINGS[5]);

        cell = headerRow.createCell((short)6);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(EM_HEADER_STRINGS[6]);

        cell = headerRow.createCell((short)7);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(EM_HEADER_STRINGS[7]);

        cell = headerRow.createCell((short)8);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(EM_HEADER_STRINGS[8]);

        cell = headerRow.createCell((short)9);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(EM_HEADER_STRINGS[9]);

        cell = headerRow.createCell((short)10);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(EM_HEADER_STRINGS[10]);

        logger.debug("Header row created...");
        int rowIndex = 1;
                
        for(TechnicalOrderDetail detail : technicalOrderDtoList) {
            HSSFRow row = sheet.createRow(rowIndex++);
            cell = row.createCell((short)0);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(detail.getInitialQuantity());
            //logger.debug(rowIndex+"  :  "+cell.getNumericCellValue());
           
            //Defect 123
            cell = row.createCell((short)1);
            if(detail.getRemovedQuantity()!=null){
            	cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            	cell.setCellValue(detail.getRemovedQuantity());
            } else {
            	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            	cell.setCellValue("");
            }
            //logger.debug(rowIndex+"  :  "+cell.getNumericCellValue());
            cell = row.createCell((short)2);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getActiveContractExist()!=null?detail.getActiveContractExist():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)3);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getTechnicallyRegisterable()!=null?detail.getTechnicallyRegisterable():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)4);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getMaterialCode()!=null?detail.getMaterialCode():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)5);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getDescription()!=null?detail.getDescription():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)6);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getProductLine()!=null?detail.getProductLine():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)7);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getSolutionElementCode()!=null?detail.getSolutionElementCode():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)8);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getSolutionElementId()!=null?detail.getSolutionElementId():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)9);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getSerialNumber()!=null?detail.getSerialNumber():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)10);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getErrorDescription()!=null?detail.getErrorDescription():"");
        }
        logger.debug("After data rows creation completed...");
        sheet.setColumnWidth((short)0, 3000);
        sheet.setColumnWidth((short)1, 3000);
        sheet.setColumnWidth((short)2, 3000);
        sheet.setColumnWidth((short)3, 3000);
        sheet.setColumnWidth((short)4, 3000);
        sheet.setColumnWidth((short)5, 3000);
        sheet.setColumnWidth((short)6, 6000);
        sheet.setColumnWidth((short)7, 5000);
        sheet.setColumnWidth((short)8, 5000);
        sheet.setColumnWidth((short)9, 5000);
        sheet.setColumnWidth((short)10, 3000);
        
        // Writing workbook created to FileOutputStream
        FileOutputStream fileOut = new FileOutputStream(new File(filePath));
        logger.debug("After FileOutputStream creation completed...");
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        logger.debug("generateEquipmentMoveWorksheet ENDS");
        // For opening the created Excel

    }
    
    public void generateIBWorksheet(RegistrationFormBean actionForm, String filePath, String date) throws Exception {
    	List<TechnicalOrderDetail> technicalOrderDtoList = actionForm.getMaterialEntryList();
        logger.debug("TechnicalOrderDetail list size using new API: " + technicalOrderDtoList.size());
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFCellStyle headerStyle = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(font);
        headerStyle.setWrapText(true);
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        
        HSSFSheet sheet = wb.createSheet("Install Base ");
        // Create a header row.
        /*HSSFRow headerRow = sheet1.createRow(0);
        HSSFCell cell = headerRow.createCell(0, 5);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Registration ID:"+technicalOrderDtoList.get(0).getRegistrationId());

        cell = headerRow.createCell(6, 11);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Sold To:"+technicalOrderDtoList.get(0).getSolutionElementId());*/
        // Create a header row.
        //START - Adding new rows for displaying the new fields as part of View Install Base functionality.
        HSSFRow row1 = sheet.createRow(0);
        HSSFCell cell1 = row1.createCell((short)0);
        cell1.setCellStyle(headerStyle);
        cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell1.setCellValue("First Name");
        HSSFCell cell2 = row1.createCell((short)1);
        cell2.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell2.setCellValue(actionForm.getFirstName());
        
        HSSFRow row2 = sheet.createRow(1);
        cell1 = row2.createCell((short)0);
        cell1.setCellStyle(headerStyle);
        cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell1.setCellValue("Last Name");
        cell2 = row2.createCell((short)1);
        cell2.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell2.setCellValue(actionForm.getLastName());
        
        HSSFRow row3 = sheet.createRow(2);
        cell1 = row3.createCell((short)0);
        cell1.setCellStyle(headerStyle);
        cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell1.setCellValue("Email");
        cell2 = row3.createCell((short)1);
        cell2.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell2.setCellValue(actionForm.getOnsiteEmail());
        
        HSSFRow row4 = sheet.createRow(3);
        cell1 = row4.createCell((short)0);
        cell1.setCellStyle(headerStyle);
        cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell1.setCellValue("Date");
        cell2 = row4.createCell((short)1);
        cell2.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell2.setCellValue(date);
        
        HSSFRow row5 = sheet.createRow(4);
        cell1 = row5.createCell((short)0);
        cell1.setCellStyle(headerStyle);
        cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell1.setCellValue("Disclaimer");
        cell2 = row5.createCell((short)1);
        cell2.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell2.setCellValue(ibExportSheetMessage);
        
        HSSFCellStyle cs = wb.createCellStyle();
        cs.setWrapText(true);
        cell2.setCellStyle(cs);
        
        //END - Adding new rows for displaying the new fields as part of View Install Base functionality.

        HSSFRow headerRow = sheet.createRow(6);
        HSSFCell cell = headerRow.createCell((short)0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(IB_HEADER_STRINGS[0]);

        cell = headerRow.createCell((short)1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(IB_HEADER_STRINGS[1]);

        cell = headerRow.createCell((short)2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(IB_HEADER_STRINGS[2]);

        cell = headerRow.createCell((short)3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(IB_HEADER_STRINGS[3]);

        cell = headerRow.createCell((short)4);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(IB_HEADER_STRINGS[4]);

        cell = headerRow.createCell((short)5);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(IB_HEADER_STRINGS[5]);

        cell = headerRow.createCell((short)6);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(IB_HEADER_STRINGS[6]);

        cell = headerRow.createCell((short)7);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(IB_HEADER_STRINGS[7]);

        cell = headerRow.createCell((short)8);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(IB_HEADER_STRINGS[8]);

        cell = headerRow.createCell((short)9);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(IB_HEADER_STRINGS[9]);
        
        cell = headerRow.createCell((short)10);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(IB_HEADER_STRINGS[10]);
        
        logger.debug("Header row created...");
        int rowIndex = 7;
        for(TechnicalOrderDetail detail : technicalOrderDtoList) {
            HSSFRow row = sheet.createRow(rowIndex++);
            cell = row.createCell((short)0);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(detail.getMaterialCode()!=null?detail.getMaterialCode():"");
            //logger.debug(rowIndex+"  :  "+cell.getNumericCellValue());
            cell = row.createCell((short)1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getInitialQuantity());
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)2);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getDescription()!=null?detail.getDescription():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)3);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getProductLine()!=null?detail.getProductLine():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)4);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getSolutionElementCode()!=null?detail.getSolutionElementCode():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)5);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getSolutionElementId()!=null?detail.getSolutionElementId():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)6);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getSerialNumber()!=null?detail.getSerialNumber():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)7);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getRegStatus()!=null?detail.getRegStatus():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)8);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            // Modified for defect #56
            if(detail.getAgreements() != null)
            	cell.setCellValue(detail.getAgreements().toString()!=null?detail.getAgreements().toString():"");
            //logger.debug(rowIndex+"  :  "+cell.getStringCellValue());
            cell = row.createCell((short)9);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getNickName()!=null?detail.getNickName():"");
            
            cell = row.createCell((short)10);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            //cell.setCellValue(detail.getAgreements().toString()!=null?detail.getAgreements().toString():"");
            cell.setCellValue(detail.getAccessType()!=null?detail.getAccessType():"");
        }
        logger.debug("After data rows creation completed...");
        sheet.setColumnWidth((short)0, 3000);
        //sheet.setColumnWidth((short)1, 3000);
        sheet.setColumnWidth((short)1, 10000);        
        sheet.setColumnWidth((short)2, 3000);
        sheet.setColumnWidth((short)3, 3000);
        sheet.setColumnWidth((short)4, 3000);
        sheet.setColumnWidth((short)5, 3000);
        sheet.setColumnWidth((short)6, 3000);
        sheet.setColumnWidth((short)7, 3000);
        sheet.setColumnWidth((short)8, 3000);
        sheet.setColumnWidth((short)9, 3000);
        sheet.setColumnWidth((short)10, 3000);
        //sheet.setColumnWidth((short)11, 3000);
        //sheet.setColumnWidth((short)12, 3000);
        //sheet.setColumnWidth((short)13, 3000);
        
        
        // Writing workbook created to FileOutputStream
        FileOutputStream fileOut = new FileOutputStream(new File(filePath));
        logger.debug("After FileOutputStream creation completed...");
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        logger.debug("generateIBWorksheet ENDS");
        // For opening the created Excel

    }

    public Attachment generateFinalValidationWorksheet(List<TechnicalOrderDetail> technicalOrderDtoList, String filePath, SiteRegistration siteRegistration) throws Exception {
        logger.debug("TechnicalOrderDetail list size using new API: " + technicalOrderDtoList.size());
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Final Validation");

        HSSFCellStyle headerStyle = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(font);

        // Create a header row.
        HSSFRow headerRow = sheet.createRow(0);
        HSSFCell cell = headerRow.createCell((short) 0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Quantity");

        cell = headerRow.createCell((short) 1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Material Code");

        cell = headerRow.createCell((short) 2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Description");

        cell = headerRow.createCell((short)3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact First Name");

        cell = headerRow.createCell((short)4);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact Last Name");

        cell = headerRow.createCell((short)5);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact Email");

        cell = headerRow.createCell((short)6);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact Phone");
        
        cell = headerRow.createCell((short)7);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requestor Contact First Name");
        
        cell = headerRow.createCell((short)8);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requestor Contact Last Name");
        
        cell = headerRow.createCell((short)9);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requestor Contact Email");
        
        cell = headerRow.createCell((short)10);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requestor Contact Phone");

        int rowIndex = 1;
        for(TechnicalOrderDetail detail : technicalOrderDtoList) {
            /*if(detail.getRemainingQuantity() == null || detail.getRemainingQuantity() == 0) {
                continue;
            }*/
            HSSFRow row = sheet.createRow(rowIndex++);
            cell = row.createCell((short) 0);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(detail.getRemainingQuantity()!=null ? new Double(detail.getRemainingQuantity().toString()):0);

            cell = row.createCell((short) 1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getMaterialCode());

            cell = row.createCell((short) 2);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getDescription());

            if(rowIndex == 2) {
            	logger.debug("OnSite contact FirstName is " + siteRegistration.getOnsiteFirstName());
                cell = row.createCell((short)3);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(siteRegistration.getOnsiteFirstName());
                
                logger.debug("OnSite contact LastName is " + siteRegistration.getOnsiteLastName());
                cell = row.createCell((short)4);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(siteRegistration.getOnsiteLastName());
                
                logger.debug("OnSite contact Email is " + siteRegistration.getOnsiteEmail());
                cell = row.createCell((short)5);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(siteRegistration.getOnsiteEmail());
                
                logger.debug("OnSite contact Phone is " + siteRegistration.getOnsitePhone());
                cell = row.createCell((short)6);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(siteRegistration.getOnsitePhone());
                
                if (siteRegistration.getFirstName()!=null) 
                {
                  logger.debug("Requestor contact FirstName is " + siteRegistration.getFirstName());
                  cell = row.createCell((short)7);
                  cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                  cell.setCellValue(siteRegistration.getFirstName());
                }
                if( siteRegistration.getLastName()!= null) 
                {
                  logger.debug("Requestor contact LastName is " + siteRegistration.getLastName());
                  cell = row.createCell((short)8);
                  cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                  cell.setCellValue(siteRegistration.getLastName());
                }
                if ( siteRegistration.getReportEmailId() != null) 
                {
                  logger.debug("Requestor contact Email is " + siteRegistration.getReportEmailId());
                  cell = row.createCell((short)9);
                  cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                  cell.setCellValue(siteRegistration.getReportEmailId());
                }
                if (siteRegistration.getReportPhone() != null)
                {
                  logger.debug("Requestor contact Phone is " + siteRegistration.getReportPhone());
                  cell = row.createCell((short)10);
                  cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                  cell.setCellValue(siteRegistration.getReportPhone());
                }
            }
        }

        sheet.setDefaultColumnWidth((short)0);
        sheet.setDefaultColumnWidth((short)1);
        sheet.setDefaultColumnWidth((short)2);
        sheet.setDefaultColumnWidth((short)3);
        sheet.setDefaultColumnWidth((short)4);
        sheet.setDefaultColumnWidth((short)5);
        sheet.setDefaultColumnWidth((short)6);
        sheet.setDefaultColumnWidth((short)7);
        sheet.setDefaultColumnWidth((short)8);
        sheet.setDefaultColumnWidth((short)9);
        sheet.setDefaultColumnWidth((short)10);

        Attachment attachment = generateAttachment(wb, filePath);

        return attachment;
    }

    public static void main(String[] args) throws Exception {
        TechnicalOrderDetailWorsheetProcessor processor = new TechnicalOrderDetailWorsheetProcessor();
        List<TechnicalOrderDetail> result = processor.parseWorksheet("test/data/MaterialEntryUpload.xls");
        System.out.println(result.size());

        /*Attachment generatedFile = processor.generateWorksheet(result, "test/data/MaterialEntryUpload1.xls");
        System.out.println(generatedFile.getFileName());
        System.out.println(generatedFile.getFileExtension());
        System.out.println(generatedFile.getFileContents().length);*/
    }


    /**
     * Method to generate the attachment for SAL Migration.
     *
     * Attachment column format
     *
     * Sold To(Site List), Product Code (Site List), Model (Comptibility Matrix), Solution Element ID, Product ID (Alarm Registration), Remote Access(Comptibility Matrix),
     * Transport Alarm(Comptibility Matrix), Administered in SAL GW Y/N(Alarm Registration)
     *
     * @param siteListSet Set contains SiteList
     * @param filePath String
     * @return Attachment file
     * @throws Exception exception
     */
    public Attachment generateSalMigrationWorksheet(Set<SiteList> siteListSet, String filePath) throws Exception {
        logger.debug("TechnicalOrderDetail list size using new API: " + siteListSet.size());
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("SAL Registration");
        boolean alarmRegistration = false;

        HSSFCellStyle headerStyle = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(font);

        // Create a header row.
        HSSFRow headerRow = sheet.createRow(0);
        HSSFCell cell = headerRow.createCell((short) 0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Sold To");

        cell = headerRow.createCell((short) 1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Product (SE Code)");

        cell = headerRow.createCell((short) 2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Model");

        cell = headerRow.createCell((short) 3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Product Id");

        cell = headerRow.createCell((short) 4);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Solution Element ID");

        cell = headerRow.createCell((short) 5);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Remote Access");

        cell = headerRow.createCell((short) 6);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Transport Alarm");

        int rowIndex = 1;
        for(SiteList siteList : siteListSet) {
            HSSFRow row = sheet.createRow(rowIndex++);
            cell = row.createCell((short) 0);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(siteList.getSoldToId());

            cell = row.createCell((short)1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(siteList.getSeCode());

            cell = row.createCell((short)2);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(siteList.getModel());

            cell = row.createCell((short)3);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(siteList.getProductId());

            cell = row.createCell((short)4);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(siteList.getSolutionElementId());

            cell = row.createCell((short)5);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(siteList.getRemoteAccess());

            cell = row.createCell((short)6);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(siteList.getTransportAlarm());
        }

        sheet.setDefaultColumnWidth((short)0);
        sheet.setDefaultColumnWidth((short)1);
        Attachment attachment = generateAttachment(wb, filePath);

        return attachment;
    }


    /**
     * Method to generate the attachment for Sal Universal and Non Universal registration.
     *
     * @param siteList List contains site list
     * @param filePath String
     * @return Attachment
     * @throws Exception exception
     */
    public Attachment generateSalUniversalAndNonUniversalWorksheet(List<SALEmailTemplateDto> salEmailTemplateDtoList, String filePath, boolean isSALUI) throws Exception {
        logger.debug("TechnicalOrderDetail salEmailTemplateDtoList size is: " + salEmailTemplateDtoList.size());
        //  [AVAYA]: 09-12-2011 Log Enhancement (Start)
        logger.debug("TechnicalOrderDetail boolean isSALUI: " + isSALUI);
        //	[AVAYA]: 09-12-2011 Log Enhancement (End)

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("SAL Registration");
        boolean lspFlag = false;
        boolean headerSolutionElementCells = true;
        List<String> solutionElementCodeEntryList = new ArrayList<String>();
        String seCode = null;
        int rowIndex = 1;
        List<SolutionElementCell> solutionElementList = null;

        HSSFCellStyle headerStyle = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(font);

        HSSFCellStyle headerBGColor = wb.createCellStyle();
        font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerBGColor.setFont(font);
        headerBGColor.setBorderTop((short)1);
        headerBGColor.setBorderBottom((short)1);
        headerBGColor.setBorderLeft((short)1);
        headerBGColor.setBorderRight((short)1);
        headerBGColor.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);

        // Create a header row.
        HSSFRow headerRow = sheet.createRow(0);
        HSSFCell cell = headerRow.createCell((short)0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Sold To");

        cell = headerRow.createCell((short)1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Product (SE Code)");

        cell = headerRow.createCell((short)2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Model");

        //Additional Header Cells for SAL Non-UI
        if (!isSALUI) {
            cell = headerRow.createCell((short)3);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("Version");

            cell = headerRow.createCell((short)4);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("SID & MID");

            cell = headerRow.createCell((short)5);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("Root Password");

            cell = headerRow.createCell((short)6);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("RAS Password");

            cell = headerRow.createCell((short)7);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("OS");
        }

        for(SALEmailTemplateDto salEmailTemplateDto : salEmailTemplateDtoList) {
        	seCode = salEmailTemplateDto.getSeCode();

        	HSSFRow row = sheet.createRow(rowIndex++);
            cell = row.createCell((short)0);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(salEmailTemplateDto.getSoldToId());

            cell = row.createCell((short)1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salEmailTemplateDto.getSeCode());

            cell = row.createCell((short)2);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salEmailTemplateDto.getModel());

            //Additional Cells for SAL Non-UI
            if (!isSALUI) {
                cell = row.createCell((short)3);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(salEmailTemplateDto.getVersion());

                cell = row.createCell((short)4);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(salEmailTemplateDto.getMid());

                cell = row.createCell((short)5);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(salEmailTemplateDto.getRootPassword());

                cell = row.createCell((short)6);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(salEmailTemplateDto.getRasPassword());

                cell = row.createCell((short)7);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(salEmailTemplateDto.getOs());
            }

            /*if (salEmailTemplateDto.isLspOnly() && isSALUI) {
                cell = row.createCell(3);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(salEmailTemplateDto.getServerType());

                cell = row.createCell(4);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(salEmailTemplateDto.getGatewayType());

                cell = row.createCell(5);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(salEmailTemplateDto.getModem());

                cell = row.createCell(6);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(salEmailTemplateDto.getAlarmOrientation());

                cell = row.createCell(7);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(salEmailTemplateDto.getNickName());

                cell = row.createCell(8);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(salEmailTemplateDto.getCmVersion());

                cell = row.createCell(9);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(salEmailTemplateDto.getMid());

                if (!lspFlag) {
                    lspFlag = true;
                }
            }*/
        }

        /*if (lspFlag && isSALUI) {
            cell = headerRow.createCell(3);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("Server Type");

            cell = headerRow.createCell(4);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("Select Gateway Type");

            cell = headerRow.createCell(5);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("Modem Dial In #(If SAL, leave blank)");

            cell = headerRow.createCell(6);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("Alarm Orientation");

            cell = headerRow.createCell(7);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("Preferred Nickname");

            cell = headerRow.createCell(8);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("CM Version");

            cell = headerRow.createCell(9);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("Preferred MID#");
        }*/

        //Generate this Solution template only for SAL UI.
        if (isSALUI) {
            //Generate addtional spread sheet
            rowIndex = salEmailTemplateDtoList.size() + 5;
            int aesCount = 0,
            	cmCount = salEmailTemplateDtoList.size(),
            	mbtCount = 0,
            	sbcCount = 0,
            	mmCount = 0,
            	lspQuantity = 0,
            	smgrCount = 0,
            	aacCount = 0,
            	msgCount = 0,
            	psCount = 0,
            	cmmFmCount = 0;

           	// [AVAYA]: 09-19-2011 Setting initial count to 0 for new products (Start)
            int mbtUpgradeToMeCount = 0,
            	csCount = 0,
            	csUpgradeToMeCount = 0,
            	meCount = 0,
            	aaccCount = 0,
            	b5800Count = 0;
            // [AVAYA]: 09-19-2011 Setting initial count to 0 for new products (End)

            for(SALEmailTemplateDto salEmailTemplateDto : salEmailTemplateDtoList) {
            	if ("AES".equalsIgnoreCase(salEmailTemplateDto.getSeCode())) {
            		aesCount += 1;
            	}
            	if ("MBT".equalsIgnoreCase(salEmailTemplateDto.getSeCode())) {
            		mbtCount += 1;
            		if ((salEmailTemplateDto.getLspQuantity() != null) && salEmailTemplateDto.getLspQuantity().length() > 0) {
            			lspQuantity =lspQuantity + Integer.parseInt(salEmailTemplateDto.getLspQuantity());
            		}
            	}
            	if ("MM".equalsIgnoreCase(salEmailTemplateDto.getSeCode())) {
            		mmCount += 1;
            	}
            	if ("SMGR".equalsIgnoreCase(salEmailTemplateDto.getSeCode())) {
            		smgrCount += 1;
            	}
            	if ("AAC".equalsIgnoreCase(salEmailTemplateDto.getSeCode())) {
            		aacCount += 1;
            	}
            	if ("MSG".equalsIgnoreCase(salEmailTemplateDto.getSeCode())) {
            		msgCount += 1;
            	}
            	if ("PS".equalsIgnoreCase(salEmailTemplateDto.getSeCode())) {
            		psCount += 1;
            	}
            	if ("CMM-FM".equalsIgnoreCase(salEmailTemplateDto.getSeCode())) {
            		cmmFmCount += 1;
            	}
            	if ("SBC".equalsIgnoreCase(salEmailTemplateDto.getSeCode())) {
            		sbcCount += 1;
            	}
            	if ((salEmailTemplateDto.getCmTemplate() != null) && salEmailTemplateDto.getCmTemplate().indexOf("Duplex CM Main") >= 0) {
            		cmCount += 1;
            	}

            	// [AVAYA]: 09-19-2011 Incrementing product count by 1 for new products (Start)
            	if ("MBT Upgrade to ME".equalsIgnoreCase(salEmailTemplateDto.getSeCode())) {
            		mbtUpgradeToMeCount += 1;
            		// Similar to MBT,  the product MBT Upgrade to ME also has lspQuantity associated with it.
            		// This is because for both MBT and MBT Upgrade to ME, the UI displays the question
            		// 'Will a new LSP be Installed? If so, how many'
            		// We therefore take into account lspQuantity for product MBT Upgrade to ME.
            		if ((salEmailTemplateDto.getLspQuantity() != null) && salEmailTemplateDto.getLspQuantity().length() > 0) {
            			lspQuantity =lspQuantity + Integer.parseInt(salEmailTemplateDto.getLspQuantity());
            		}
            	}
            	if ("CS".equalsIgnoreCase(salEmailTemplateDto.getSeCode())) {
            		csCount += 1;
            	}
            	if ("CS Upgrade to ME".equalsIgnoreCase(salEmailTemplateDto.getSeCode())) {
            		csUpgradeToMeCount += 1;
            	}
            	if ("ME".equalsIgnoreCase(salEmailTemplateDto.getSeCode())) {
            		meCount += 1;
            	}
            	if ("AACC".equalsIgnoreCase(salEmailTemplateDto.getSeCode())) {
            		aaccCount += 1;
            	}
            	if ("B5800".equalsIgnoreCase(salEmailTemplateDto.getSeCode())) {
            		b5800Count += 1;
            	}
            	// [AVAYA]: 09-19-2011 Incrementing product count by 1 for new products (End)
            }

            // [AVAYA] 09-30-2011 The quantity in generic headerSMCells should not reflect AACC and/or B5800 products - Part 1 (Start)
            int decrementCountForHeaderQty = 0;
            for(SALEmailTemplateDto salEmailTemplateDto : salEmailTemplateDtoList) {
            	if (salEmailTemplateDto.getSeCode().equals("AACC")) {
            		decrementCountForHeaderQty += 1;
            	}

            	if (salEmailTemplateDto.getSeCode().equals("B5800")) {
            		decrementCountForHeaderQty += 1;
            	}
            }
            // [AVAYA] 09-30-2011 The quantity in generic headerSMCells should not reflect AACC and/or B5800 products - Part 1 (End)

            //CM Template
            for(SALEmailTemplateDto salEmailTemplateDto : salEmailTemplateDtoList) {

            	// [AVAYA] 09-30-2011 boolean variables to track AACC and B5800 (Start)
            	boolean isProductAACC = false;
            	boolean isProductB5800 = false;
            	// [AVAYA] 09-30-2011 boolean variables to track AACC and B5800 (End)

            	HSSFRow seResultRow = sheet.createRow(rowIndex++);
            	// [AVAYA]: 09-21-2011 Log Enhancement (Start)
            	logger.debug("CM Template for loop executing for product: " + salEmailTemplateDto.getSeCode());
            	// [AVAYA]: 09-21-2011 Log Enhancement (End)

            	if (salEmailTemplateDto.getSeCode().equals("AACC")) {
            		isProductAACC = true; // [AVAYA] 09-30-2011 headerSMCells should not be generated for AACC and/or B5800 products
            	}

            	if (salEmailTemplateDto.getSeCode().equals("B5800")) {
            		isProductB5800 = true; // [AVAYA] 09-30-2011 headerSMCells should not be generated for AACC and/or B5800 products
            	}

            	if (headerSolutionElementCells && !isProductAACC && !isProductB5800) { // [AVAYA] 09-30-2011 headerSMCells should not be generated for AACC and/or B5800 products
            		logger.debug("generateHeaderSMCells will be invoked for product : " + salEmailTemplateDto.getSeCode());
            		int smHeaderQty = salEmailTemplateDtoList.size();

            		// [AVAYA] 09-30-2011 The quantity in generic headerSMCells should not reflect AACC and/or B5800 products - Part 2 (Start)
            		smHeaderQty = smHeaderQty - decrementCountForHeaderQty;
            		logger.debug("smHeaderQty = " + smHeaderQty);
            		rowIndex = this.generateHeaderSMCells(rowIndex, sheet, headerBGColor, salEmailTemplateDto, smHeaderQty);
            		// [AVAYA] 09-30-2011 The quantity in generic headerSMCells should not reflect AACC and/or B5800 products - Part 2 (End)

            		headerSolutionElementCells = false;
            	}

            	rowIndex+=1;
            	if ("CM".equalsIgnoreCase(salEmailTemplateDto.getSeCode()) && !solutionElementCodeEntryList.contains(salEmailTemplateDto.getSeCode())) {
            		solutionElementCodeEntryList.add(salEmailTemplateDto.getSeCode());

            		seResultRow = sheet.createRow(rowIndex++);

            		HSSFCell cmCell = seResultRow.createCell((short)0);
            		cmCell.setCellStyle(headerBGColor);
            		cmCell.setCellValue("CM");

            		cmCell = seResultRow.createCell((short)2);
            		cmCell.setCellStyle(headerStyle);
            		cmCell.setCellValue("QTY");

            		seResultRow = sheet.createRow(rowIndex++);
            		cmCell = seResultRow.createCell((short)0);
            		cmCell.setCellStyle(headerStyle);
            		cmCell.setCellValue("SP (DOM 0)");

            		cmCell = seResultRow.createCell((short)2);
            		cmCell.setCellStyle(headerStyle);
            		cmCell.setCellValue(cmCount);

                	rowIndex+=1;
                	//LSP of CM Registration
                	rowIndex = generateLSPRegistrationList(rowIndex, sheet, headerStyle, salEmailTemplateDto, salEmailTemplateDto.getSeCode());
            	}

            	if ("AES".equalsIgnoreCase(salEmailTemplateDto.getSeCode()) && !solutionElementCodeEntryList.contains(salEmailTemplateDto.getSeCode())) {
            		solutionElementCodeEntryList.add(salEmailTemplateDto.getSeCode());

            		seResultRow = sheet.createRow(rowIndex++);

            		HSSFCell cmCell = seResultRow.createCell((short)0);
            		cmCell.setCellStyle(headerStyle);
            		cmCell.setCellValue("AES");

            		cmCell = seResultRow.createCell((short)2);
            		cmCell.setCellStyle(headerStyle);
            		cmCell.setCellValue("Qty");

            		seResultRow = sheet.createRow(rowIndex++);
            		cmCell = seResultRow.createCell((short)0);
            		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            		cmCell.setCellValue("VAES");

            		cmCell = seResultRow.createCell((short)2);
            		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            		cmCell.setCellValue(aesCount);
            	}

            	if ("MSG".equalsIgnoreCase(salEmailTemplateDto.getSeCode()) && !solutionElementCodeEntryList.contains(salEmailTemplateDto.getSeCode())) {
            		solutionElementCodeEntryList.add(salEmailTemplateDto.getSeCode());

            		seResultRow = sheet.createRow(rowIndex++);

            		HSSFCell cmCell = seResultRow.createCell((short)0);
            		cmCell.setCellStyle(headerStyle);
            		cmCell.setCellValue("MSG");

            		cmCell = seResultRow.createCell((short)2);
            		cmCell.setCellStyle(headerStyle);
            		cmCell.setCellValue("VAAM");

            		cmCell = seResultRow.createCell((short)3);
            		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            		cmCell.setCellValue(msgCount);
            	}

            	if ("SBC".equalsIgnoreCase(salEmailTemplateDto.getSeCode()) && !solutionElementCodeEntryList.contains(salEmailTemplateDto.getSeCode())) {
            		solutionElementCodeEntryList.add(salEmailTemplateDto.getSeCode());

            		seResultRow = sheet.createRow(rowIndex++);

            		HSSFCell cmCell = seResultRow.createCell((short)(short)0);
            		cmCell.setCellStyle(headerStyle);
            		cmCell.setCellValue("SBC");

            		cmCell = seResultRow.createCell((short)2);
            		cmCell.setCellStyle(headerStyle);
            		cmCell.setCellValue("AASBC");

            		cmCell = seResultRow.createCell((short)3);
            		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            		cmCell.setCellValue(sbcCount);
            	}

            	if ("PS".equalsIgnoreCase(salEmailTemplateDto.getSeCode()) && !solutionElementCodeEntryList.contains(salEmailTemplateDto.getSeCode())) {
            		solutionElementCodeEntryList.add(salEmailTemplateDto.getSeCode());

            		seResultRow = sheet.createRow(rowIndex++);

            		HSSFCell cmCell = seResultRow.createCell((short)0);
            		cmCell.setCellStyle(headerStyle);
            		cmCell.setCellValue("PS");

            		cmCell = seResultRow.createCell((short)2);
            		cmCell.setCellStyle(headerStyle);
            		cmCell.setCellValue("VPSB");

            		cmCell = seResultRow.createCell((short)3);
            		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            		cmCell.setCellValue(psCount);
            	}

            	if ("MM".equalsIgnoreCase(salEmailTemplateDto.getSeCode()) && !solutionElementCodeEntryList.contains(salEmailTemplateDto.getSeCode())) {
            		solutionElementCodeEntryList.add(salEmailTemplateDto.getSeCode());
            		rowIndex = this.generateMMCells(rowIndex, sheet, headerStyle, salEmailTemplateDto, mmCount);
            	}

            	if ("CMM-FM".equalsIgnoreCase(salEmailTemplateDto.getSeCode()) && !solutionElementCodeEntryList.contains(salEmailTemplateDto.getSeCode())) {
            		solutionElementCodeEntryList.add(salEmailTemplateDto.getSeCode());
            		rowIndex = this.generateCMM_FMCells(rowIndex, sheet, headerStyle, salEmailTemplateDto, cmmFmCount);
            	}

            	if ("SMGR".equalsIgnoreCase(salEmailTemplateDto.getSeCode()) && !solutionElementCodeEntryList.contains(salEmailTemplateDto.getSeCode())) {
            		solutionElementCodeEntryList.add(salEmailTemplateDto.getSeCode());
            		rowIndex = this.generateSMGRCells(rowIndex, sheet, headerStyle, salEmailTemplateDto, smgrCount);
            	}

            	if ("AAC".equalsIgnoreCase(salEmailTemplateDto.getSeCode()) && !solutionElementCodeEntryList.contains(salEmailTemplateDto.getSeCode()) && (salEmailTemplateDto.getAacStandard() != null)) {
            		solutionElementCodeEntryList.add(salEmailTemplateDto.getSeCode());
            		rowIndex = this.generateAACStandardCells(rowIndex, sheet, headerStyle, salEmailTemplateDto, aacCount);
            	}

            	if ("MBT".equalsIgnoreCase(salEmailTemplateDto.getSeCode()) && !solutionElementCodeEntryList.contains(salEmailTemplateDto.getSeCode())) {
            		solutionElementCodeEntryList.add(salEmailTemplateDto.getSeCode());
            		salEmailTemplateDto.setLspQuantity(String.valueOf(lspQuantity));
            		rowIndex = this.generateMBTCells(rowIndex, sheet, headerStyle, salEmailTemplateDto, mbtCount);
            	}

            	// [AVAYA]: 09-19-2011 Adding Products to generate worksheets  (Start)
            	if ("MBT Upgrade to ME".equalsIgnoreCase(salEmailTemplateDto.getSeCode()) && !solutionElementCodeEntryList.contains(salEmailTemplateDto.getSeCode())) {
            		solutionElementCodeEntryList.add(salEmailTemplateDto.getSeCode());
            		salEmailTemplateDto.setLspQuantity(String.valueOf(lspQuantity)); // similar to MBT because of UI question 'Will a new LSP be Installed? If so, how many'
            		rowIndex = this.generateMBTUpgradeToMeStandardCells(rowIndex, sheet, headerStyle, salEmailTemplateDto, mbtUpgradeToMeCount);
            	}

            	if ("CS".equalsIgnoreCase(salEmailTemplateDto.getSeCode()) && !solutionElementCodeEntryList.contains(salEmailTemplateDto.getSeCode())) {
            		solutionElementCodeEntryList.add(salEmailTemplateDto.getSeCode());
            		rowIndex = this.generateCSStandardCells(rowIndex, sheet, headerStyle, salEmailTemplateDto, csCount);
            	}

            	if ("CS Upgrade to ME".equalsIgnoreCase(salEmailTemplateDto.getSeCode()) && !solutionElementCodeEntryList.contains(salEmailTemplateDto.getSeCode())) {
            		solutionElementCodeEntryList.add(salEmailTemplateDto.getSeCode());
            		rowIndex = this.generateCSUpgradeToMeStandardCells(rowIndex, sheet, headerStyle, salEmailTemplateDto, csUpgradeToMeCount);
            	}

            	if ("ME".equalsIgnoreCase(salEmailTemplateDto.getSeCode()) && !solutionElementCodeEntryList.contains(salEmailTemplateDto.getSeCode())) {
            		solutionElementCodeEntryList.add(salEmailTemplateDto.getSeCode());
            		rowIndex = this.generateMEStandardCells(rowIndex, sheet, headerStyle, salEmailTemplateDto, meCount);
            	}

            	if ("AACC".equalsIgnoreCase(salEmailTemplateDto.getSeCode()) && !solutionElementCodeEntryList.contains(salEmailTemplateDto.getSeCode())) {
            		solutionElementCodeEntryList.add(salEmailTemplateDto.getSeCode());
            		rowIndex = this.generateAACCStandardCells(rowIndex, sheet, headerStyle, salEmailTemplateDto, aaccCount);
            	}

            	if ("B5800".equalsIgnoreCase(salEmailTemplateDto.getSeCode()) && !solutionElementCodeEntryList.contains(salEmailTemplateDto.getSeCode())) {
            		solutionElementCodeEntryList.add(salEmailTemplateDto.getSeCode());
            		rowIndex = this.generateB5800StandardCells(rowIndex, sheet, headerStyle, salEmailTemplateDto, b5800Count);
            	}
            	// [AVAYA]: 09-19-2011 Adding Products to generate worksheets  (End)

            	rowIndex+=1;
            	if (salEmailTemplateDto.getCmSoftwareSolutionList().size() > 0) {
            		rowIndex = this.generateCMSoftwareCells(rowIndex, sheet, headerStyle, salEmailTemplateDto);
            	}
            	rowIndex+=1;
            	if (salEmailTemplateDto.getEssSoftwareSolutionList().size() > 0) {
            		rowIndex = this.generateESSSoftwareCells(rowIndex, sheet, headerStyle, salEmailTemplateDto);
            	}
            	rowIndex+=1;
            	if (salEmailTemplateDto.getLspSoftwareElementList().size() > 0) {
            		rowIndex = this.generateLSPSoftwareCells(rowIndex, sheet, headerStyle, salEmailTemplateDto);
            	}
            	rowIndex+=1;
            	if (salEmailTemplateDto.getAacSoftwareElementList().size() >0) {
            		rowIndex = this.generateAACPSoftwareCells(rowIndex, sheet, headerStyle, salEmailTemplateDto);
            	}
            }
        }

        sheet.setDefaultColumnWidth((short)0);
        sheet.setDefaultColumnWidth((short)1);


        Attachment attachment = generateAttachment(wb, filePath);

        return attachment;
    }

    /**
     * Method to generate SMGR Cells.
     *
     * @param rowIndex
     * @param sheet
     * @param headerStyle
     * @param salEmailTemplateDto
     */
    public int generateSMGRCells(int rowIndex, HSSFSheet sheet, HSSFCellStyle headerStyle, SALEmailTemplateDto salEmailTemplateDto, int smgrCount) {
		List<SolutionElementCell> solutionElementList = this.getSMGRCells();
		logger.debug("Entering TechnicalOrderDetailWorsheetProcessor : generateSMGRCells");
		HSSFRow seResultRow = sheet.createRow(rowIndex++);
		HSSFCell cmCell = seResultRow.createCell((short)0);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("SMGR");

		cmCell = seResultRow.createCell((short)2);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("QTY");

		for (SolutionElementCell smgrCells : solutionElementList) {
			seResultRow = sheet.createRow(rowIndex++);
    		cmCell = seResultRow.createCell((short)0);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(smgrCells.getColumn1());

    		cmCell = seResultRow.createCell((short)1);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(smgrCells.getColumn2());

    		cmCell = seResultRow.createCell((short)2);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(smgrCount);
		}
		logger.debug("Entering TechnicalOrderDetailWorsheetProcessor : generateSMGRCells");

		return rowIndex;
    }

    /**
     * Method to generate MM Cells.
     *
     * @param rowIndex
     * @param sheet
     * @param headerStyle
     * @param salEmailTemplateDto
     */
    public int generateMMCells(int rowIndex, HSSFSheet sheet, HSSFCellStyle headerStyle, SALEmailTemplateDto salEmailTemplateDto, int count) {
		List<SolutionElementCell> solutionElementList = this.getMMCells();
		logger.debug("Entering TechnicalOrderDetailWorsheetProcessor : generateMMCells");

		HSSFRow seResultRow = sheet.createRow(rowIndex++);
		HSSFCell cmCell = seResultRow.createCell((short)0);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("MM");

		cmCell = seResultRow.createCell((short)2);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("QTY");

		for (SolutionElementCell mmCells : solutionElementList) {
			seResultRow = sheet.createRow(rowIndex++);
    		cmCell = seResultRow.createCell((short)0);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(mmCells.getColumn1());

    		cmCell = seResultRow.createCell((short)1);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(mmCells.getColumn2());

    		cmCell = seResultRow.createCell((short)2);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(count);
		}
		logger.debug("Entering TechnicalOrderDetailWorsheetProcessor : generateMMCells");

		return rowIndex;
    }

    /**
     * Method to generate the Cells for CMM_FM.
     *
     * @param rowIndex
     * @param sheet
     * @param headerStyle
     * @param salEmailTemplateDto
     * @param count
     * @return
     */
    public int generateCMM_FMCells(int rowIndex, HSSFSheet sheet, HSSFCellStyle headerStyle, SALEmailTemplateDto salEmailTemplateDto, int count) {
		List<SolutionElementCell> solutionElementList = this.getCMM_FMCells();
		logger.debug("Entering TechnicalOrderDetailWorsheetProcessor : generateCMM_FMCells");

		HSSFRow seResultRow = sheet.createRow(rowIndex++);
		HSSFCell cmCell = seResultRow.createCell((short)0);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("CMM-FM");

		cmCell = seResultRow.createCell((short)2);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("QTY");

		for (SolutionElementCell cmmCells : solutionElementList) {
			seResultRow = sheet.createRow(rowIndex++);
    		cmCell = seResultRow.createCell((short)0);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(cmmCells.getColumn1());

    		cmCell = seResultRow.createCell((short)1);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(cmmCells.getColumn2());

    		cmCell = seResultRow.createCell((short)2);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(count);
		}
		logger.debug("Entering TechnicalOrderDetailWorsheetProcessor : generateCMM_FMCells");

		return rowIndex;
    }


    /**
     * Method to generate the AAC Cells.
     *
     * @param rowIndex int
     * @param sheet Sheet
     * @param headerStyle CellStyle
     * @param salEmailTemplateDto SALEmailTemplateDto
     * @param count int
     * @return rowIndex int
     */
    public int generateAACStandardCells(int rowIndex, HSSFSheet sheet, HSSFCellStyle headerStyle, SALEmailTemplateDto salEmailTemplateDto, int count) {
		List<SolutionElementCell> solutionElementList = this.getAACStandardCells();
		logger.debug("Entering TechnicalOrderDetailWorsheetProcessor : generateAACStandardCells");

		HSSFRow seResultRow = sheet.createRow(rowIndex++);
		HSSFCell cmCell = seResultRow.createCell((short)2);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("QTY");

		seResultRow = sheet.createRow(rowIndex++);
		cmCell = seResultRow.createCell((short)0);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("ACS");

		cmCell = seResultRow.createCell((short)2);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("QTY");

		for (SolutionElementCell aacCells : solutionElementList) {
			seResultRow = sheet.createRow(rowIndex++);
    		cmCell = seResultRow.createCell((short)0);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(aacCells.getColumn1());

    		cmCell = seResultRow.createCell((short)1);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(aacCells.getColumn2());

    		cmCell = seResultRow.createCell((short)2);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		if (!aacCells.getColumn2().equalsIgnoreCase("ACSWCV") && !aacCells.getColumn2().equalsIgnoreCase("SM")) {
    			cmCell.setCellValue(count);
    		}
		}
		logger.debug("Entering TechnicalOrderDetailWorsheetProcessor : generateAACStandardCells");

		return rowIndex;
    }

    /**
     * Method to generate the software cells for CM.
     *
     * @param rowIndex int
     * @param sheet Sheet
     * @param headerStyle CellStyle
     * @param salEmailTemplateDto SALEmailTemplateDto
     * @return rowIndex int
     */
    public int generateCMSoftwareCells(int rowIndex, HSSFSheet sheet, HSSFCellStyle headerStyle, SALEmailTemplateDto salEmailTemplateDto) {
    	logger.debug("Entering TechnicalOrderDetailWorsheetProcessor : generateCMSoftwareCells()");
		HSSFRow seResultRow = sheet.createRow(rowIndex++);
		HSSFCell cmCell = seResultRow.createCell((short)0);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("CM Software Solution Element");

		cmCell = seResultRow.createCell((short)7);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("CM Hardware Solution Element");
		seResultRow = sheet.createRow(rowIndex++);

		for (List<String> cmList : salEmailTemplateDto.getCmSoftwareSolutionList()) {
			for (int i = 0; i < cmList.size(); i++) {
	    		cmCell = seResultRow.createCell((short)i);
	    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	    		cmCell.setCellValue(cmList.get(i));
			}
		}

		for (List<String> cmHardwareList : salEmailTemplateDto.getCmHardwareElementList()) {
			for (int i=0;i<cmHardwareList.size();i++) {
				int row=i + 7;
	    		cmCell = seResultRow.createCell((short)row);
	    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	    		cmCell.setCellValue(cmHardwareList.get(i));
			}
		}

    	logger.debug("Exiting TechnicalOrderDetailWorsheetProcessor : generateCMSoftwareCells()");

    	return rowIndex;
    }

    /**
     * Method to generate the ESS Software/Hardware list.
     *
     * @param rowIndex int
     * @param sheet Sheet
     * @param headerStyle style
     * @param salEmailTemplateDto SALEmailTemplateDto
     * @return rowIndex int
     */
    public int generateESSSoftwareCells(int rowIndex, HSSFSheet sheet, HSSFCellStyle headerStyle, SALEmailTemplateDto salEmailTemplateDto) {
    	logger.debug("Entering TechnicalOrderDetailWorsheetProcessor : generateESSSoftwareCells()");
		HSSFRow seResultRow = sheet.createRow(rowIndex++);
		HSSFCell cmCell = seResultRow.createCell((short)0);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("ESS Software Solution Element");

		cmCell = seResultRow.createCell((short)7);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("ESS Hardware Solution Element");
		seResultRow = sheet.createRow(rowIndex++);

		for (List<String> essSoftwareList : salEmailTemplateDto.getEssSoftwareSolutionList()) {
			for (int i = 0; i < essSoftwareList.size(); i++) {
	    		cmCell = seResultRow.createCell((short)i);
	    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	    		cmCell.setCellValue(essSoftwareList.get(i));
			}
		}

		for (List<String> essHardwareList : salEmailTemplateDto.getEssHardwareElementList()) {
			for (int i = 0; i < essHardwareList.size(); i++) {
				int row=i+7;
	    		cmCell = seResultRow.createCell((short)row);
	    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	    		cmCell.setCellValue(essHardwareList.get(i));
			}
		}

    	logger.debug("Exiting TechnicalOrderDetailWorsheetProcessor : generateESSSoftwareCells()");

    	return rowIndex;
    }

    /**
     * Method to generate the LSP Software/Hardware list.
     *
     * @param rowIndex int
     * @param sheet Sheet
     * @param headerStyle CellStyle
     * @param salEmailTemplateDto SALEmailTemplateDto
     * @return rowIndex int
     */
    public int generateLSPSoftwareCells(int rowIndex, HSSFSheet sheet, HSSFCellStyle headerStyle, SALEmailTemplateDto salEmailTemplateDto) {
    	logger.debug("Entering TechnicalOrderDetailWorsheetProcessor : generateLSPSoftwareCells()");
		HSSFRow seResultRow = sheet.createRow(rowIndex++);
		HSSFCell cmCell = seResultRow.createCell((short)0);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("LSP Software Solution Element");

		cmCell = seResultRow.createCell((short)7);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("LSP Hardware Solution Element");
		seResultRow = sheet.createRow(rowIndex++);

		for (List<String> lspSoftwareList : salEmailTemplateDto.getLspSoftwareElementList()) {
			for (int i = 0; i < lspSoftwareList.size(); i++) {
	    		cmCell = seResultRow.createCell((short)i);
	    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	    		cmCell.setCellValue(lspSoftwareList.get(i));
			}
		}

		for (List<String> lspHardwareList : salEmailTemplateDto.getLspHardwareElementList()) {
			for (int i = 0; i < lspHardwareList.size(); i++) {
				int row=i+7;
	    		cmCell = seResultRow.createCell((short)row);
	    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	    		cmCell.setCellValue(lspHardwareList.get(i));
			}
		}

    	logger.debug("Exiting TechnicalOrderDetailWorsheetProcessor : generateLSPSoftwareCells()");

    	return rowIndex;
    }

    /**
     * Method to generate the AAC Records.
     *
     * @param rowIndex
     * @param sheet
     * @param headerStyle
     * @param salEmailTemplateDto
     * @return
     */
    public int generateAACPSoftwareCells(int rowIndex, HSSFSheet sheet, HSSFCellStyle headerStyle, SALEmailTemplateDto salEmailTemplateDto) {
    	logger.debug("Entering TechnicalOrderDetailWorsheetProcessor : generateAACPSoftwareCells()");
		HSSFRow seResultRow = sheet.createRow(rowIndex++);
		HSSFCell cmCell = seResultRow.createCell((short)0);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("AAC Software Solution Element");

		cmCell = seResultRow.createCell((short)7);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("AAC Hardware Solution Element");

		for (List<String> aacSoftwareList : salEmailTemplateDto.getAacSoftwareElementList()) {
			seResultRow = sheet.createRow(rowIndex++);
			for (int i = 0; i < aacSoftwareList.size(); i++) {
	    		cmCell = seResultRow.createCell((short)i);
	    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	    		cmCell.setCellValue(aacSoftwareList.get(i));
			}
		}

		//Reset the row index, before proceed the hardware element name
		rowIndex = rowIndex - salEmailTemplateDto.getAacSoftwareElementList().size();
		for (List<String> aacHardwareList : salEmailTemplateDto.getAacHardwareElementList()) {
			seResultRow = sheet.getRow(rowIndex++);
			for (int i = 0; i < aacHardwareList.size(); i++) {
				int row=(short)i + (short)7;
	    		cmCell = seResultRow.createCell((short) row);
	    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	    		cmCell.setCellValue(aacHardwareList.get(i));
			}
		}

    	logger.debug("Exiting TechnicalOrderDetailWorsheetProcessor : generateAACPSoftwareCells()");

    	return rowIndex;
    }

    /**
     * Method to generate Header SM Cells.
     *
     * @param rowIndex
     * @param sheet
     * @param headerStyle
     * @param salEmailTemplateDto
     */
    public int generateHeaderSMCells(int rowIndex, HSSFSheet sheet, HSSFCellStyle headerStyle, SALEmailTemplateDto salEmailTemplateDto, int solutionElementCount) {
		List<SolutionElementCell> solutionElementList = this.getHeaderSMCells();
		logger.debug("Entering TechnicalOrderDetailWorsheetProcessor : generateHeaderSMCells, solutionElementCount: " + solutionElementCount);

		HSSFRow seResultRow = sheet.createRow(rowIndex++);
		HSSFCell cmCell = seResultRow.createCell((short)0);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("SOLUTION ELEMENTS to be Registered");

		cmCell = seResultRow.createCell((short)2);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("QTY");

		for (SolutionElementCell mmCells : solutionElementList) {
			seResultRow = sheet.createRow(rowIndex++);
    		cmCell = seResultRow.createCell((short)0);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(mmCells.getColumn1());

    		cmCell = seResultRow.createCell((short)1);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(mmCells.getColumn2());

    		cmCell = seResultRow.createCell((short)2);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(solutionElementCount);
		}
		logger.debug("Exiting TechnicalOrderDetailWorsheetProcessor : generateHeaderSMCells");

		return rowIndex;
    }

    public int generateMBTCells(int rowIndex, HSSFSheet sheet, HSSFCellStyle headerStyle, SALEmailTemplateDto salEmailTemplateDto, int mbtCount){
		List<SolutionElementCell> solutionElementList = this.getMBTCells();
		logger.debug("Entering TechnicalOrderDetailWorsheetProcessor : generateMBTCells");
		HSSFRow seResultRow = sheet.createRow(rowIndex++);
		HSSFCell cmCell = seResultRow.createCell((short)0);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("MBT");

		cmCell = seResultRow.createCell((short)2);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("QTY");

		for (SolutionElementCell mbtCells : solutionElementList) {
			seResultRow = sheet.createRow(rowIndex++);
    		cmCell = seResultRow.createCell((short)0);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(mbtCells.getColumn1());

    		cmCell = seResultRow.createCell((short)1);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(mbtCells.getColumn2());

    		cmCell = seResultRow.createCell((short)2);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(mbtCount);
		}

		if (salEmailTemplateDto.getMediaService() != null) {
			seResultRow = sheet.createRow(rowIndex++);
    		cmCell = seResultRow.createCell((short)0);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue("Media Services");

    		cmCell = seResultRow.createCell((short)1);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue("VCOB");

    		cmCell = seResultRow.createCell((short)2);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
    		cmCell.setCellValue(String.valueOf(mbtCount));
		}

		if (salEmailTemplateDto.getLspQuantity() != null) {
			seResultRow = sheet.createRow(rowIndex++);
    		cmCell = seResultRow.createCell((short)0);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue("LSP");

    		cmCell = seResultRow.createCell((short)1);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue("RFACM");

    		cmCell = seResultRow.createCell((short)2);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
    		cmCell.setCellValue(salEmailTemplateDto.getLspQuantity());
		}

		if (salEmailTemplateDto.getServerType() != null
				&& salEmailTemplateDto.getHardWareName() != null) {
			seResultRow = sheet.createRow(rowIndex++);
    		cmCell = seResultRow.createCell((short)0);
    		cmCell.setCellStyle(headerStyle);
    		cmCell.setCellValue("Hardware Element");

    		seResultRow = sheet.createRow(rowIndex++);
    		cmCell = seResultRow.createCell((short)1);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(salEmailTemplateDto.getHardWareName());

    		cmCell = seResultRow.createCell((short)2);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(String.valueOf(mbtCount));
		}
		logger.debug("Exiting TechnicalOrderDetailWorsheetProcessor : generateMBTCells");
		rowIndex+=1;
    	//LSP of MBT Registration
		rowIndex = generateLSPRegistrationList(rowIndex, sheet, headerStyle, salEmailTemplateDto, salEmailTemplateDto.getSeCode());
    	rowIndex+=1;

		return rowIndex;
    }

    //  [AVAYA]: 09-19-2011 generate CS Standard Cells (Start)
    /**
     * Method to generate the CS Cells.
     *
     * @param rowIndex int
     * @param sheet Sheet
     * @param headerStyle CellStyle
     * @param salEmailTemplateDto SALEmailTemplateDto
     * @param count int
     * @return rowIndex int
     */
    public int generateCSStandardCells(int rowIndex, HSSFSheet sheet, HSSFCellStyle headerStyle, SALEmailTemplateDto salEmailTemplateDto, int csCount) {
		logger.debug("Entering TechnicalOrderDetailWorsheetProcessor : generateCSStandardCells");
		List<SolutionElementCell> solutionElementList = this.getCSStandardCells();

		HSSFRow seResultRow = sheet.createRow(rowIndex++);
		HSSFCell cmCell = seResultRow.createCell((short)2);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("QTY");

		seResultRow = sheet.createRow(rowIndex++);
		cmCell = seResultRow.createCell((short)0);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("CS");

		cmCell = seResultRow.createCell((short)2);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("QTY");

		for (SolutionElementCell csCells : solutionElementList) {
			seResultRow = sheet.createRow(rowIndex++);

			cmCell = seResultRow.createCell((short)0);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(csCells.getColumn1());

    		cmCell = seResultRow.createCell((short)1);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(csCells.getColumn2());

    		cmCell = seResultRow.createCell((short)2);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
   			cmCell.setCellValue(csCount);
		}

		// Dynamic value supposed to be read from UI 'Pick which Server will be used' question.
		// [AYAYA] 09-27-2011 CS spreadsheet dynamic data needs to come from getServer (Start)
		if (salEmailTemplateDto.getServer() != null) {
			seResultRow = sheet.createRow(rowIndex++);
    		cmCell = seResultRow.createCell((short)0);
    		cmCell.setCellStyle(headerStyle);
    		cmCell.setCellValue("Hardware Element");

    		seResultRow = sheet.createRow(rowIndex++);
    		cmCell = seResultRow.createCell((short)1);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(salEmailTemplateDto.getServer());

    		cmCell = seResultRow.createCell((short)2);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(String.valueOf(csCount));

    		logger.debug("CS Spreadsheet Hardware Element cell value set to: " + salEmailTemplateDto.getServer());
		}
		// [AYAYA] 09-27-2011 CS spreadsheet dynamic data needs to come from getServer (End)
		logger.debug("Exiting TechnicalOrderDetailWorsheetProcessor : generateCSStandardCells");

		return rowIndex;
	}
    //  [AVAYA]: 09-19-2011 generate CS Standard Cells (End)

    //  [AVAYA]: 09-21-2011 generate ME Standard Cells (Start)
    /**
     * Method to generate the CS Cells.
     *
     * @param rowIndex int
     * @param sheet Sheet
     * @param headerStyle CellStyle
     * @param salEmailTemplateDto SALEmailTemplateDto
     * @param count int
     * @return rowIndex int
     */
    public int generateMEStandardCells(int rowIndex, HSSFSheet sheet, HSSFCellStyle headerStyle, SALEmailTemplateDto salEmailTemplateDto, int meCount) {
		logger.debug("Entering TechnicalOrderDetailWorsheetProcessor : generateMEStandardCells");
		List<SolutionElementCell> solutionElementList = this.getMEStandardCells();

		HSSFRow seResultRow = sheet.createRow(rowIndex++);
		HSSFCell cmCell = seResultRow.createCell((short)2);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("QTY");

		seResultRow = sheet.createRow(rowIndex++);
		cmCell = seResultRow.createCell((short)0);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("ME");

		cmCell = seResultRow.createCell((short)2);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("QTY");

		for (SolutionElementCell csCells : solutionElementList) {
			seResultRow = sheet.createRow(rowIndex++);

			cmCell = seResultRow.createCell((short)0);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(csCells.getColumn1());

    		cmCell = seResultRow.createCell((short)1);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(csCells.getColumn2());

    		cmCell = seResultRow.createCell((short)2);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
   			cmCell.setCellValue(meCount);
		}
		logger.debug("Exiting TechnicalOrderDetailWorsheetProcessor : generateMEStandardCells");

		return rowIndex;
	}
    //  [AVAYA]: 09-21-2011 generate ME Standard Cells (End)

    //  [AVAYA]: 09-21-2011 generate CS Upgrade to ME Standard Cells (Start)
    /**
     * Method to generate the CS Cells.
     *
     * @param rowIndex int
     * @param sheet Sheet
     * @param headerStyle CellStyle
     * @param salEmailTemplateDto SALEmailTemplateDto
     * @param count int
     * @return rowIndex int
     */
    public int generateCSUpgradeToMeStandardCells(int rowIndex, HSSFSheet sheet, HSSFCellStyle headerStyle, SALEmailTemplateDto salEmailTemplateDto, int csUpgradeToMeCount) {
		logger.debug("Entering TechnicalOrderDetailWorsheetProcessor : generateCSUpgradeToMeStandardCells");
		List<SolutionElementCell> solutionElementList = this.getCSUpgradeToMeStandardCells();

		HSSFRow seResultRow = sheet.createRow(rowIndex++);
		HSSFCell cmCell = seResultRow.createCell((short)2);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("QTY");

		seResultRow = sheet.createRow(rowIndex++);
		cmCell = seResultRow.createCell((short)0);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("CS Upgrade to ME");

		cmCell = seResultRow.createCell((short)2);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("QTY");

		for (SolutionElementCell csCells : solutionElementList) {
			seResultRow = sheet.createRow(rowIndex++);

			cmCell = seResultRow.createCell((short)0);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(csCells.getColumn1());

    		cmCell = seResultRow.createCell((short)1);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(csCells.getColumn2());

    		cmCell = seResultRow.createCell((short)2);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
   			cmCell.setCellValue(csUpgradeToMeCount);
		}

		// Dynamic value supposed to be read from UI 'Pick which Server will be used' question.
		// [AYAYA] 09-27-2011 CS Upgrade to ME spreadsheet dynamic data needs to come from getServer (Start)
		if (salEmailTemplateDto.getServer() != null) {
			seResultRow = sheet.createRow(rowIndex++);
    		cmCell = seResultRow.createCell((short)0);
    		cmCell.setCellStyle(headerStyle);
    		cmCell.setCellValue("Hardware Element");

    		seResultRow = sheet.createRow(rowIndex++);
    		cmCell = seResultRow.createCell((short)1);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(salEmailTemplateDto.getServer());

    		cmCell = seResultRow.createCell((short)2);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(String.valueOf(csUpgradeToMeCount));

    		logger.debug("CS Upgrade to ME Spreadsheet Hardware Element cell value set to: " + salEmailTemplateDto.getServer());
		}
		// [AYAYA] 09-27-2011 CS Upgrade to ME spreadsheet dynamic data needs to come from getServer (End)
		logger.debug("Exiting TechnicalOrderDetailWorsheetProcessor : generateCSUpgradeToMeStandardCells");

		return rowIndex;
	}
    //  [AVAYA]: 09-21-2011 generate CS Upgrade to ME Standard Cells (End)

    //  [AVAYA]: 09-21-2011 generate MBT Upgrade to ME Standard Cells (Start)
    /**
     * Method to generate the CS Cells.
     *
     * @param rowIndex int
     * @param sheet Sheet
     * @param headerStyle CellStyle
     * @param salEmailTemplateDto SALEmailTemplateDto
     * @param count int
     * @return rowIndex int
     */
    public int generateMBTUpgradeToMeStandardCells(int rowIndex, HSSFSheet sheet, HSSFCellStyle headerStyle, SALEmailTemplateDto salEmailTemplateDto, int mbtUpgradeToMeCount) {
		logger.debug("Entering TechnicalOrderDetailWorsheetProcessor : generateMBTUpgradeToMeStandardCells");
		List<SolutionElementCell> solutionElementList = this.getMBTUpgradeToMeStandardCells();

		HSSFRow seResultRow = sheet.createRow(rowIndex++);
		HSSFCell cmCell = seResultRow.createCell((short)2);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("QTY");

		seResultRow = sheet.createRow(rowIndex++);
		cmCell = seResultRow.createCell((short)0);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("MBT Upgrade to ME");

		cmCell = seResultRow.createCell((short)2);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("QTY");

		for (SolutionElementCell csCells : solutionElementList) {
			seResultRow = sheet.createRow(rowIndex++);

			cmCell = seResultRow.createCell((short)0);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(csCells.getColumn1());

    		cmCell = seResultRow.createCell((short)1);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(csCells.getColumn2());

    		cmCell = seResultRow.createCell((short)2);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
   			cmCell.setCellValue(mbtUpgradeToMeCount);
		}

		// [AYAYA] 09-27-2011 MBT Upgrade to ME spreadsheet dynamic data needs to come from getServer getLspQuantity (Start)
		if (salEmailTemplateDto.getLspQuantity() != null) {
			seResultRow = sheet.createRow(rowIndex++);
    		cmCell = seResultRow.createCell((short)0);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue("LSP");

    		cmCell = seResultRow.createCell((short)1);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue("RFACM");

    		cmCell = seResultRow.createCell((short)2);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
    		cmCell.setCellValue(salEmailTemplateDto.getLspQuantity());

    		logger.debug("MBT Upgrade to ME LSP cell value set to: " + salEmailTemplateDto.getLspQuantity());
		}
		// [AYAYA] 09-27-2011 MBT Upgrade to ME spreadsheet dynamic data needs to come from getServer getLspQuantity (End)

		rowIndex+=1;
    	// [AVAYA] 09-28-2011: MBT Upgrade to ME LSP Registration List (Start)
		rowIndex = generateLSPRegistrationList(rowIndex, sheet, headerStyle, salEmailTemplateDto, salEmailTemplateDto.getSeCode());
    	rowIndex+=1;
    	// [AVAYA] 09-28-2011: MBT Upgrade to ME LSP Registration List (End)

		logger.debug("Exiting TechnicalOrderDetailWorsheetProcessor : generateMBTUpgradeToMeStandardCells");

		return rowIndex;
	}
    //  [AVAYA]: 09-21-2011 generate MBT Upgrade to ME Standard Cells (End))

    //  [AVAYA]: 09-21-2011 generate AACC Standard Cells (Start)
    /**
     * Method to generate the CS Cells.
     *
     * @param rowIndex int
     * @param sheet Sheet
     * @param headerStyle CellStyle
     * @param salEmailTemplateDto SALEmailTemplateDto
     * @param count int
     * @return rowIndex int
     */
    public int generateAACCStandardCells(int rowIndex, HSSFSheet sheet, HSSFCellStyle headerStyle, SALEmailTemplateDto salEmailTemplateDto, int aaccCount) {
		logger.debug("Entering TechnicalOrderDetailWorsheetProcessor : generateAACCStandardCells");
		List<SolutionElementCell> solutionElementList = this.getAACCStandardCells();

		HSSFRow seResultRow = sheet.createRow(rowIndex++);
		HSSFCell cmCell = seResultRow.createCell((short)2);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("QTY");

		seResultRow = sheet.createRow(rowIndex++);
		cmCell = seResultRow.createCell((short)0);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("AACC");

		cmCell = seResultRow.createCell((short)2);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("QTY");

		for (SolutionElementCell csCells : solutionElementList) {
			seResultRow = sheet.createRow(rowIndex++);

			cmCell = seResultRow.createCell((short)0);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(csCells.getColumn1());

    		cmCell = seResultRow.createCell((short)1);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(csCells.getColumn2());

    		cmCell = seResultRow.createCell((short)2);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
   			cmCell.setCellValue(aaccCount);
		}

		logger.debug("Exiting TechnicalOrderDetailWorsheetProcessor : generateAACCStandardCells");

		return rowIndex;
	}
    //  [AVAYA]: 09-21-2011 generate AACC Standard Cells (End)

    //  [AVAYA]: 09-21-2011 generate B5800 Standard Cells (Start)
    /**
     * Method to generate the CS Cells.
     *
     * @param rowIndex int
     * @param sheet Sheet
     * @param headerStyle CellStyle
     * @param salEmailTemplateDto SALEmailTemplateDto
     * @param count int
     * @return rowIndex int
     */
    public int generateB5800StandardCells(int rowIndex, HSSFSheet sheet, HSSFCellStyle headerStyle, SALEmailTemplateDto salEmailTemplateDto, int b5800Count) {
		logger.debug("Entering TechnicalOrderDetailWorsheetProcessor : generateB5800StandardCells");
		List<SolutionElementCell> solutionElementList = this.getB5800StandardCells();

		HSSFRow seResultRow = sheet.createRow(rowIndex++);
		HSSFCell cmCell = seResultRow.createCell((short)(short) 2);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("QTY");

		seResultRow = sheet.createRow(rowIndex++);
		cmCell = seResultRow.createCell((short)(short) 0);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("B5800");

		cmCell = seResultRow.createCell((short)(short) 2);
		cmCell.setCellStyle(headerStyle);
		cmCell.setCellValue("QTY");

		for (SolutionElementCell csCells : solutionElementList) {
			seResultRow = sheet.createRow(rowIndex++);

			cmCell = seResultRow.createCell((short)0);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(csCells.getColumn1());

    		cmCell = seResultRow.createCell((short)1);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		cmCell.setCellValue(csCells.getColumn2());

    		cmCell = seResultRow.createCell((short)2);
    		cmCell.setCellType(HSSFCell.CELL_TYPE_STRING);
   			cmCell.setCellValue(b5800Count);
		}

		logger.debug("Exiting TechnicalOrderDetailWorsheetProcessor : generateB5800StandardCells");
		return rowIndex;
	}
    //  [AVAYA]: 09-21-2011 generate B5800 Standard Cells (End)

    public List<SolutionElementCell> getMBTCells() {
    	List<SolutionElementCell> mbtCellList = new ArrayList<SolutionElementCell>();
    	SolutionElementCell solutionElementCell = null;

    	solutionElementCell = new SolutionElementCell("CM","VCM","1");
    	mbtCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("CMM","VCMM","1");
    	mbtCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("AES","VAES","1");
    	mbtCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("SES","VSES","1");
    	mbtCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("Utility Server","VUS","1");
    	mbtCellList.add(solutionElementCell);

    	return mbtCellList;
    }

    public List<SolutionElementCell> getAACHeaderStandardCells() {
    	List<SolutionElementCell> aacStandardCellList = new ArrayList<SolutionElementCell>();
    	SolutionElementCell solutionElementCell = null;

    	solutionElementCell = new SolutionElementCell("SP (DOM 0)","VSP","1");
    	aacStandardCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("SP (CDOM)","VSPU","1");
    	aacStandardCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("SAL","VSALGW","1");
    	aacStandardCellList.add(solutionElementCell);

    	return aacStandardCellList;
    }

    public List<SolutionElementCell> getAACStandardCells() {
    	List<SolutionElementCell> aacStandardCellList = new ArrayList<SolutionElementCell>();
    	SolutionElementCell solutionElementCell = null;

    	solutionElementCell = new SolutionElementCell("Conf Bridge","ACSCBV","1");
    	aacStandardCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("Client Reg","ACSCRV","1");
    	aacStandardCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("Web Portal","ACSWPV","1");
    	aacStandardCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("Web Conf","ACSWCV","");
    	aacStandardCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("SMGR","SM","");
    	aacStandardCellList.add(solutionElementCell);

    	return aacStandardCellList;
    }


    public List<SolutionElementCell> getMMCells() {
    	List<SolutionElementCell> mmCellList = new ArrayList<SolutionElementCell>();
    	SolutionElementCell solutionElementCell = null;

    	solutionElementCell = new SolutionElementCell("MSS","VMSSR","1");
    	mmCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("MAS","VMAS","1");
    	mmCellList.add(solutionElementCell);

    	return mmCellList;
    }

    public List<SolutionElementCell> getCMM_FMCells() {
    	List<SolutionElementCell> mmCellList = new ArrayList<SolutionElementCell>();
    	SolutionElementCell solutionElementCell = null;

    	solutionElementCell = new SolutionElementCell("CM","VCM","1");
    	mmCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("CMM","VCMM","1");
    	mmCellList.add(solutionElementCell);

    	return mmCellList;
    }

    public List<SolutionElementCell> getHeaderSMCells() {
    	List<SolutionElementCell> smHeaderCellList = new ArrayList<SolutionElementCell>();
    	SolutionElementCell solutionElementCell = null;

    	solutionElementCell = new SolutionElementCell("SP (DOM 0)","VSP","1");
    	smHeaderCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("SP (CDOM)","VSPU","1");
    	smHeaderCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("SAL","VSALGW","1");
    	smHeaderCellList.add(solutionElementCell);

    	return smHeaderCellList;
    }

    public List<SolutionElementCell> getSMGRCells() {
    	List<SolutionElementCell> mmCellList = new ArrayList<SolutionElementCell>();
    	SolutionElementCell solutionElementCell = null;

    	solutionElementCell = new SolutionElementCell("SMGR","SM","1");
    	mmCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("SMGRSAL","SM","1");
    	mmCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("SMGR ELEM","SMELEM","1");
    	mmCellList.add(solutionElementCell);

    	return mmCellList;
    }

    // [AVAYA]: 09-19-2011 get CS Standard Cells (Start)
    public List<SolutionElementCell> getCSStandardCells() {
    	List<SolutionElementCell> csCellList = new ArrayList<SolutionElementCell>();
    	SolutionElementCell solutionElementCell = null;

    	// [AVAYA]: 09-29-211 Remove VSALGW from CS Spreadsheet
    	solutionElementCell = new SolutionElementCell("","VCM","1");
    	csCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","VUS","1");
    	csCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","ASM","1");
    	csCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","SALASM","1");
    	csCellList.add(solutionElementCell);
    	/* [AVAYA]: 09-22-2011 Removing blank line from CS spreadsheet
    	solutionElementCell = new SolutionElementCell("","","");
    	csCellList.add(solutionElementCell);
    	*/
    	solutionElementCell = new SolutionElementCell("","SMELEM","1");
    	csCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","SM","1");
    	csCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","SALSM","1"); // [AVAYA] 09-28-2011: Typo Fix (Start/End)
    	csCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","VPSB","1");
    	csCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","VCMM","1");
    	csCellList.add(solutionElementCell);

    	return csCellList;
    }

    // [AVAYA]: 09-21-2011 get ME Standard Cells (Start)
    public List<SolutionElementCell> getMEStandardCells() {
    	List<SolutionElementCell> meCellList = new ArrayList<SolutionElementCell>();
    	SolutionElementCell solutionElementCell = null;

    	// [AVAYA]: 09-29-211 Remove VSP, VSALGW, VSPU from ME Spreadsheet
    	solutionElementCell = new SolutionElementCell("","VUS","1");
    	meCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","VCM","1");
    	meCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","VCMM","1");
    	meCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","VASM","1");
    	meCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","SALASM","1");
    	meCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","SALSM","1");
    	meCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","SM","1");
    	meCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","SMELEM","1");
    	meCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","VPSB","1");
    	meCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","VAES","1");
    	meCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","AASBC","1");
    	meCellList.add(solutionElementCell);
    	return meCellList;
    }
    // [AVAYA]: 09-21-2011 get ME  Standard Cells (End)

    // [AVAYA]: 09-21-2011 get CS Upgrade to ME Standard Cells (Start)
    public List<SolutionElementCell> getCSUpgradeToMeStandardCells() {
    	List<SolutionElementCell> csUpgradeToMeCellList = new ArrayList<SolutionElementCell>();
    	SolutionElementCell solutionElementCell = null;

    	// [AVAYA]: 09-29-211 Remove VSP, VSALGW, VSPU from CS Upgrade to ME Spreadsheet
    	solutionElementCell = new SolutionElementCell("","VUS","1");
    	csUpgradeToMeCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","VCM","1");
    	csUpgradeToMeCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","VCMM","1");
    	csUpgradeToMeCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","VASM","1");
    	csUpgradeToMeCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","SALASM","1");
    	csUpgradeToMeCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","SALSM","1");
    	csUpgradeToMeCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","SM","1");
    	csUpgradeToMeCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","SMELEM","1");
    	csUpgradeToMeCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","VPSB","1");
    	csUpgradeToMeCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","VAES","1");
    	csUpgradeToMeCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","AASBC","1");
    	csUpgradeToMeCellList.add(solutionElementCell);

    	return csUpgradeToMeCellList;
    }
    //  [AVAYA]: 09-21-2011 get CS Upgrade to ME Standard Cells (End)

    //  [AVAYA]: 09-21-2011 get MBT Upgrade to ME Standard Cells (Start)
    public List<SolutionElementCell> getMBTUpgradeToMeStandardCells() {
    	List<SolutionElementCell> mbtUpgradeToMeCellList = new ArrayList<SolutionElementCell>();
    	SolutionElementCell solutionElementCell = null;

    	solutionElementCell = new SolutionElementCell("","VASM","1");
    	mbtUpgradeToMeCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","SALASM","1");
    	mbtUpgradeToMeCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","SALSM","1");
    	mbtUpgradeToMeCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","SM","1");
    	mbtUpgradeToMeCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","SMELEM","1");
    	mbtUpgradeToMeCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","VPSB","1");
    	mbtUpgradeToMeCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","AASBC","1");
    	mbtUpgradeToMeCellList.add(solutionElementCell);

    	return mbtUpgradeToMeCellList;
    }
    //  [AVAYA]: 09-21-2011 get MBT Upgrade to ME Standard Cells (End)

    //  [AVAYA]: 09-21-2011 get AACC Standard Cells (Start)
    public List<SolutionElementCell> getAACCStandardCells() {
    	List<SolutionElementCell> aaccCellList = new ArrayList<SolutionElementCell>();
    	SolutionElementCell solutionElementCell = null;

    	solutionElementCell = new SolutionElementCell("","ACCMA","1");
    	aaccCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","ACCMS","1");
    	aaccCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","ACCT","1");
    	aaccCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","ACCMM","1");
    	aaccCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","ACCMMO","1");
    	aaccCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","ACCMMW","1");
    	aaccCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","ACCSCE","1");
    	aaccCellList.add(solutionElementCell);
    	solutionElementCell = new SolutionElementCell("","ACCNCC","1");
    	aaccCellList.add(solutionElementCell);

    	return aaccCellList;
    }
    //  [AVAYA]: 09-21-2011 get AACC Standard Cells (End)

    //  [AVAYA]: 09-21-2011 get B5800 Standard Cells (Start)
    public List<SolutionElementCell> getB5800StandardCells() {
    	List<SolutionElementCell> b5800CellList = new ArrayList<SolutionElementCell>();
    	SolutionElementCell solutionElementCell = null;

    	solutionElementCell = new SolutionElementCell("","B5800","1");
    	b5800CellList.add(solutionElementCell);

    	return b5800CellList;
    }
    //  [AVAYA]: 09-21-2011 get B5800 Standard Cells (End)

    /**
     * Method to print the LSP belongs to CM and MBT
     *
     * @param rowIndex int
     * @param sheet Sheet
     * @param headerStyle CellStyle
     * @param salEmailTemplateDto SALEmailTemplateDto
     * @param seCode String
     */
    public int generateLSPRegistrationList(int rowIndex, HSSFSheet sheet, HSSFCellStyle headerStyle, SALEmailTemplateDto salEmailTemplateDto, String seCode){
    	HSSFRow row = sheet.createRow(rowIndex++);
		HSSFCell cell = row.createCell((short)0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue(seCode.toUpperCase() + " - LSP Details ");

        row = sheet.createRow(rowIndex++);
		cell = row.createCell((short)0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Server Type");

        cell = row.createCell((short)1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Select Gateway Type");

        cell = row.createCell((short)2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Modem Dial In #(If SAL, leave blank)");

        cell = row.createCell((short)3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Alarm Orientation");

        cell = row.createCell((short)4);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Preferred Nickname");

        cell = row.createCell((short)5);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("CM Version");

        cell = row.createCell((short)6);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Preferred MID#");

        //Display all the LSP belongs to the SECode (CM or MBT)
		for (LSPRegistrationDto lspRegistration : salEmailTemplateDto.getLspRegistrationList()) {
			if (lspRegistration.getSeCode().equalsIgnoreCase(seCode)) {
				row = sheet.createRow(rowIndex++);
				cell = row.createCell((short)0);
		        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        cell.setCellValue(lspRegistration.getServerType());

		        cell = row.createCell((short)1);
		        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        cell.setCellValue(lspRegistration.getGateway());

		        cell = row.createCell((short)2);
		        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        cell.setCellValue(lspRegistration.getModem());

		        cell = row.createCell((short)3);
		        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        cell.setCellValue(lspRegistration.getAlarmOrientation());

		        cell = row.createCell((short)4);
		        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        cell.setCellValue(lspRegistration.getNickName());

		        cell = row.createCell((short)5);
		        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        cell.setCellValue(lspRegistration.getCmVersion());

		        cell = row.createCell((short)6);
		        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        cell.setCellValue(lspRegistration.getMid());
			}
	    }

		return rowIndex;
    }


    /**
     * Generate Excel worksheet in the specific location based on the TechnicalOrderDetail list
     * @param technicalOrderDtoList List<TechnicalOrderDetail>
     * @param filePath String
     * @return filePath String
     * @throws Exception
     */
    public Attachment generateWorksheetTechRegIPO(SalProductRegistration salProductRegistration) throws Exception {
        logger.debug("Product Registration for TechnicalRegistration ID: " + salProductRegistration.getTechnicalRegistration().getTechnicalRegistrationId());
        logger.debug(" Starting  generateWorksheetTechRegIPO ...");
        String filePath=salProductRegistration.getFilePath();
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("new sheet");

        HSSFCellStyle headerStyle = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(font);

        // Create a header row.
        HSSFRow headerRow = sheet.createRow(0);
        HSSFCell cell = headerRow.createCell((short)0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("SE Code");

        cell = headerRow.createCell((short)1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Product Type");

        cell = headerRow.createCell((short)2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Release String");

        cell = headerRow.createCell((short)3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Service Name");

        cell = headerRow.createCell((short)4);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("ART Error Code");

        cell = headerRow.createCell((short)5);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("ART Error desc");

        cell = headerRow.createCell((short)6);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Sold TO");

        cell = headerRow.createCell((short)7);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("User Id");

        cell = headerRow.createCell((short)8);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Opt Type");

        cell = headerRow.createCell((short)9);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact First Name");

        cell = headerRow.createCell((short)10);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact Last Name");

        cell = headerRow.createCell((short)11);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact Email");

        cell = headerRow.createCell((short)12);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact Phone");
        
        cell = headerRow.createCell((short)13);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requestor Contact First Name");
        
        cell = headerRow.createCell((short)14);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requestor Contact Last Name");
        
        cell = headerRow.createCell((short)15);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requestor Contact Email");
        
        cell = headerRow.createCell((short)16);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requestor Contact Phone");

        int rowIndex = 1;

        TechnicalRegistration technicalRegistration = salProductRegistration.getTechnicalRegistration();

            logger.debug("technicalRegistration.getSolutionElement() is " + technicalRegistration.getSolutionElement());
            HSSFRow row = sheet.createRow(rowIndex++);
            cell = row.createCell((short)0);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalRegistration.getSolutionElement());

            logger.debug("technicalRegistration.getProductCode() is " + technicalRegistration.getProductCode());
            cell = row.createCell((short)1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalRegistration.getProductCode());

            logger.debug("technicalRegistration.getSoftwareRelease() is " + technicalRegistration.getSoftwareRelease());
            cell = row.createCell((short)2);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalRegistration.getSoftwareRelease());

            logger.debug("technicalRegistration.getS.getProductType() is " + "AVAYA_SUPPORT");
            cell = row.createCell((short)3);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue("AVAYA_SUPPORT");

            logger.debug("technicalRegistration.getErrorCode() is " + salProductRegistration.getErrorCode());
            cell = row.createCell((short)4);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getErrorCode());

            logger.debug("salProductRegistration.getErrorDescription() is " + salProductRegistration.getErrorDescription());
            cell = row.createCell((short)5);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getErrorDescription());

            logger.debug("technicalRegistration.getSoldToId() is " + technicalRegistration.getTechnicalOrder().getSiteRegistration().getSoldToId());
            cell = row.createCell((short)6);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalRegistration.getTechnicalOrder().getSiteRegistration().getSoldToId());

            logger.debug("technicalRegistration.getUpdatedBy() is " + technicalRegistration.getCreatedBy());
            cell = row.createCell((short)7);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalRegistration.getCreatedBy());

            logger.debug("salProductRegistration.getOptType()is " + salProductRegistration.getOptType());
            cell = row.createCell((short)8);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getOptType());

            cell = headerRow.createCell((short)9);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getOnsiteContactFirstName());
            
            cell = headerRow.createCell((short)10);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getOnsiteContactLastName());
            
            cell = headerRow.createCell((short)11);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getOnsiteContactEmail());
            
            cell = headerRow.createCell((short)12);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getOnsiteContactPhone());
            
            cell = headerRow.createCell((short)13);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getRequesterContactFirstName());
            
            cell = headerRow.createCell((short)14);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getRequesterContactLastName());
            
            cell = headerRow.createCell((short)15);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getRequesterContactEmail());
            
            cell = headerRow.createCell((short)16);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getRequesterContactPhone());
        
        logger.debug("autoSizeColumns start");
        sheet.setDefaultColumnWidth((short)0);
        sheet.setDefaultColumnWidth((short)1);
        sheet.setDefaultColumnWidth((short)2);
        sheet.setDefaultColumnWidth((short)3);
        sheet.setDefaultColumnWidth((short)4);
        sheet.setDefaultColumnWidth((short)5);
        sheet.setDefaultColumnWidth((short)6);
        sheet.setDefaultColumnWidth((short)7);
        sheet.setDefaultColumnWidth((short)8);
        sheet.setDefaultColumnWidth((short)9);
        sheet.setDefaultColumnWidth((short)10);
        sheet.setDefaultColumnWidth((short)11);
        sheet.setDefaultColumnWidth((short)12);
        sheet.setDefaultColumnWidth((short)13);
        sheet.setDefaultColumnWidth((short)14);
        sheet.setDefaultColumnWidth((short)15);
        sheet.setDefaultColumnWidth((short)16);
        
        logger.debug("autoSizeColumns end");

        logger.debug("about to call generateAttachment");

        Attachment attachment = generateAttachment(wb, filePath);
        logger.debug(" Exiting  generateWorksheetTechRegIPO ...");

        return attachment;
    }


    public Attachment generateWorksheetTechRegIP(SalProductRegistration salProductRegistration) throws Exception {
        logger.debug("Product Registration for TechnicalRegistration ID: " + salProductRegistration.getTechnicalRegistration().getTechnicalRegistrationId());
        logger.debug(" Starting  generateWorksheetTechRegIP ...");
        String filePath=salProductRegistration.getFilePath();
        logger.debug(" filePath ..."+filePath);
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("new sheet");

        HSSFCellStyle headerStyle = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(font);

        // Create a header row.
        HSSFRow headerRow = sheet.createRow(0);
        HSSFCell cell = headerRow.createCell((short)0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("SE Code");

        cell = headerRow.createCell((short)1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Product Type");

        cell = headerRow.createCell((short)2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Access Type");

        cell = headerRow.createCell((short)3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Remote Connectivity ");

        cell = headerRow.createCell((short)4);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("IP Address");

        cell = headerRow.createCell((short)5);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Nick Name");

        cell = headerRow.createCell((short)6);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("SID");

        cell = headerRow.createCell((short)7);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("MID");

        cell = headerRow.createCell((short)8);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("AORIG");

        cell = headerRow.createCell((short)9);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Sold To");

        cell = headerRow.createCell((short)10);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("ART Error Code");

        cell = headerRow.createCell((short)11);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("ART Error Desc");

        cell = headerRow.createCell((short)12);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("User Id");

        cell = headerRow.createCell((short)13);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Opt Type");

        cell = headerRow.createCell((short)14);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact First Name");

        cell = headerRow.createCell((short)15);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact Last Name");

        cell = headerRow.createCell((short)16);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact Email");

        cell = headerRow.createCell((short)17);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact Phone");
        
        cell = headerRow.createCell((short)18);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requestor Contact First Name");
        
        cell = headerRow.createCell((short)19);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requestor Contact Last Name");
        
        cell = headerRow.createCell((short)20);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requestor Contact Email");
        
        cell = headerRow.createCell((short)21);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requestor Contact Phone");

        int rowIndex = 1;

        TechnicalRegistration technicalRegistration = salProductRegistration.getTechnicalRegistration();

            logger.debug("technicalRegistration.getSolutionElement() is " + technicalRegistration.getSolutionElement());
            HSSFRow row = sheet.createRow(rowIndex++);
            cell = row.createCell((short)0);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalRegistration.getSolutionElement());

            logger.debug("technicalRegistration.getProductCode() is " + technicalRegistration.getProductCode());
            cell = row.createCell((short)1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalRegistration.getProductCode());

            logger.debug("technicalRegistration.getAccessType() is " + technicalRegistration.getAccessType());
            cell = row.createCell((short)2);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalRegistration.getAccessType());

            logger.debug("technicalRegistration.getRemoteAccess() is " + technicalRegistration.getRemoteAccess());
            cell = row.createCell((short)3);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalRegistration.getRemoteAccess());

           logger.debug("technicalRegistration.getIpAddress()is " + technicalRegistration.getIpAddress());
            cell = row.createCell((short)4);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalRegistration.getIpAddress());


            logger.debug(" technicalRegistration.getNickname() is " + technicalRegistration.getNickname());
            cell = row.createCell((short)5);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue( technicalRegistration.getNickname());

            logger.debug("technicalRegistration.getSid() is " + technicalRegistration.getSid());
            cell = row.createCell((short)6);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalRegistration.getSid());

            logger.debug("technicalRegistration.getMid() is " + technicalRegistration.getMid());
            cell = row.createCell((short)7);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalRegistration.getMid());

            logger.debug("Angrish: aorig is:[" + salProductRegistration.getAorig() + "]");
            cell = row.createCell((short)8);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getAorig());

            logger.debug("technicalRegistration.getTechnicalOrder().getSiteRegistration().getSoldToId() is " + technicalRegistration.getTechnicalOrder().getSiteRegistration().getSoldToId());
            cell = row.createCell((short)9);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalRegistration.getTechnicalOrder().getSiteRegistration().getSoldToId());

            logger.debug("salProductRegistration.getErrorCode() is " + salProductRegistration.getErrorCode());
            cell = row.createCell((short)10);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getErrorCode());

            logger.debug("salProductRegistration.getErrorDescription() is " + salProductRegistration.getErrorDescription());
            cell = row.createCell((short)11);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getErrorDescription());

            logger.debug("technicalRegistration.getUpdatedBy() is " + technicalRegistration.getCreatedBy());
            cell = row.createCell((short)12);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue( technicalRegistration.getCreatedBy());

            logger.debug("technicalRegistration.getUpdatedBy() is " + technicalRegistration.getCreatedBy());
            cell = row.createCell((short)12);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalRegistration.getCreatedBy());

            logger.debug("salProductRegistration.getOptType() is " + salProductRegistration.getOptType());
            cell = row.createCell((short)13);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getOptType());


            logger.debug("OnSite contact FirstName is " + salProductRegistration.getOnsiteContactFirstName());
            cell = row.createCell((short)14);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getOnsiteContactFirstName());

            logger.debug("OnSite contact LastName is " + salProductRegistration.getOnsiteContactLastName());
            cell = row.createCell((short)15);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getOnsiteContactLastName());

            logger.debug("OnSite contact Email is " + salProductRegistration.getOnsiteContactEmail());
            cell = row.createCell((short)16);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getOnsiteContactEmail());

            logger.debug("OnSite contact Phone is " + salProductRegistration.getOnsiteContactPhone());
            cell = row.createCell((short)17);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getOnsiteContactPhone());
            
            logger.debug("Requestor contact FirstName is " + salProductRegistration.getRequesterContactFirstName());
            cell = row.createCell((short)18);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getRequesterContactFirstName());
            
            logger.debug("Requestor contact LastName is " + salProductRegistration.getRequesterContactLastName());
            cell = row.createCell((short)19);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getRequesterContactLastName());
            
            logger.debug("Requestor contact Email is " + salProductRegistration.getRequesterContactEmail());
            cell = row.createCell((short)20);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getRequesterContactEmail());
            
            logger.debug("Requestor contact Phone is " + salProductRegistration.getRequesterContactPhone());
            cell = row.createCell((short)21);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getRequesterContactPhone());
            
        logger.debug("autoSizeColumns start");
        sheet.setDefaultColumnWidth((short)0);
        sheet.setDefaultColumnWidth((short)1);
        sheet.setDefaultColumnWidth((short)2);
        sheet.setDefaultColumnWidth((short)3);
        sheet.setDefaultColumnWidth((short)4);
        sheet.setDefaultColumnWidth((short)5);
        sheet.setDefaultColumnWidth((short)6);
        sheet.setDefaultColumnWidth((short)7);
        sheet.setDefaultColumnWidth((short)8);
        sheet.setDefaultColumnWidth((short)9);
        sheet.setDefaultColumnWidth((short)10);
        sheet.setDefaultColumnWidth((short)11);
        sheet.setDefaultColumnWidth((short)12);
        sheet.setDefaultColumnWidth((short)13);
        sheet.setDefaultColumnWidth((short)14);
        sheet.setDefaultColumnWidth((short)15);
        sheet.setDefaultColumnWidth((short)16);
        sheet.setDefaultColumnWidth((short)17);
        sheet.setDefaultColumnWidth((short)18);
        sheet.setDefaultColumnWidth((short)19);
        sheet.setDefaultColumnWidth((short)20);
        sheet.setDefaultColumnWidth((short)21);
        
        logger.debug("autoSizeColumns end");

        logger.debug("about to call generateAttachment");

        Attachment attachment = generateAttachment(wb, filePath);
        logger.debug(" Exiting  generateWorksheetTechRegIP ...");

        return attachment;
    }

    public Attachment generateWorksheetTechRegModem(SalProductRegistration salProductRegistration) throws Exception {
        logger.debug("Product Registration for TechnicalRegistration ID: " + salProductRegistration.getTechnicalRegistration().getTechnicalRegistrationId());
        logger.debug(" Starting  generateWorksheetTechRegModem ...");
        String filePath=salProductRegistration.getFilePath();
        logger.debug(" filePath..."+filePath);
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("new sheet");

        HSSFCellStyle headerStyle = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(font);

        // Create a header row.
        HSSFRow headerRow = sheet.createRow(0);
        HSSFCell cell = headerRow.createCell((short)0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("SE Code");

        cell = headerRow.createCell((short)1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Product Type");

        cell = headerRow.createCell((short)2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Access Type");

        cell = headerRow.createCell((short)3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Remote Connectivity ");

        cell = headerRow.createCell((short)4);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Dial Number");

        cell = headerRow.createCell((short)5);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Nick Name");

        cell = headerRow.createCell((short)6);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("SID");

        cell = headerRow.createCell((short)7);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("MID");

        cell = headerRow.createCell((short)8);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("AORIG");

        cell = headerRow.createCell((short)9);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Sold To");

        cell = headerRow.createCell((short)10);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("ART Error Code");

        cell = headerRow.createCell((short)11);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("ART Error Desc");

        cell = headerRow.createCell((short)12);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("User Id");

        cell = headerRow.createCell((short)13);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Opt Type");

        cell = headerRow.createCell((short)14);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact First Name");

        cell = headerRow.createCell((short)15);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact Last Name");

        cell = headerRow.createCell((short)16);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact Email");

        cell = headerRow.createCell((short)17);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact Phone");
        
        cell = headerRow.createCell((short)18);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requestor Contact First Name");
        
        cell = headerRow.createCell((short)19);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requestor Contact Last Name");
        
        cell = headerRow.createCell((short)20);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requestor Contact Email");
        
        cell = headerRow.createCell((short)21);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requestor Contact Phone");

        int rowIndex = 1;

        TechnicalRegistration technicalRegistration = salProductRegistration.getTechnicalRegistration();

            logger.debug("technicalRegistration.getSolutionElementId() is " + technicalRegistration.getSolutionElementId());
            HSSFRow row = sheet.createRow(rowIndex++);
            cell = row.createCell((short)0);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalRegistration.getSolutionElementId());

            logger.debug("technicalRegistration.getProductId() is " + technicalRegistration.getProductId());
            cell = row.createCell((short)1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalRegistration.getProductId());

            logger.debug("technicalRegistration.getAccessType() is " + technicalRegistration.getAccessType());
            cell = row.createCell((short)2);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalRegistration.getAccessType());

            logger.debug("technicalRegistration.getRemoteAccess() is " + technicalRegistration.getRemoteAccess());
            cell = row.createCell((short)3);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalRegistration.getRemoteAccess());

            logger.debug("technicalRegistration.getDialInNumber() is " + technicalRegistration.getDialInNumber());
            cell = row.createCell((short)4);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalRegistration.getDialInNumber());

            logger.debug(" technicalRegistration.getNickname() is " + technicalRegistration.getNickname());
            cell = row.createCell((short)5);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue( technicalRegistration.getNickname());

            logger.debug("technicalRegistration.getSid() is " + technicalRegistration.getSid());
            cell = row.createCell((short)6);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalRegistration.getSid());

            logger.debug("technicalRegistration.getMid() is " + technicalRegistration.getMid());
            cell = row.createCell((short)7);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalRegistration.getMid());

            logger.debug("Angrish: aorig is:[" + salProductRegistration.getAorig() + "]");
            cell = row.createCell((short)8);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getAorig());

            logger.debug("technicalRegistration.getSoldToId() is " + technicalRegistration.getSoldToId());
            cell = row.createCell((short)9);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalRegistration.getSoldToId());

            logger.debug("salProductRegistration is " + salProductRegistration.getErrorCode());
            cell = row.createCell((short)10);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getErrorCode());

            logger.debug("salProductRegistration.getErrorDescription() is " + salProductRegistration.getErrorDescription());
            cell = row.createCell((short)11);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getErrorDescription());

            logger.debug("technicalRegistration.getUpdatedBy() is " + technicalRegistration.getUpdatedBy());
            cell = row.createCell((short)12);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalRegistration.getUpdatedBy());

            logger.debug("salProductRegistration.getOptType() is " + salProductRegistration.getOptType());
            cell = row.createCell((short)13);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getOptType());

            logger.debug("OnSite contact FirstName is " + salProductRegistration.getOnsiteContactFirstName());
            cell = row.createCell((short)14);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getOnsiteContactFirstName());

            logger.debug("OnSite contact LastName is " + salProductRegistration.getOnsiteContactLastName());
            cell = row.createCell((short)15);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getOnsiteContactLastName());

            logger.debug("OnSite contact Email is " + salProductRegistration.getOnsiteContactEmail());
            cell = row.createCell((short)16);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getOnsiteContactEmail());

            logger.debug("OnSite contact Phone is " + salProductRegistration.getOnsiteContactPhone());
            cell = row.createCell((short)17);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getOnsiteContactPhone());
            
            logger.debug("Requestor contact FirstName is " + salProductRegistration.getRequesterContactFirstName());
            cell = row.createCell((short)18);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getRequesterContactFirstName());
            
            logger.debug("Requestor contact LastName is " + salProductRegistration.getRequesterContactLastName());
            cell = row.createCell((short)19);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getRequesterContactLastName());
            
            logger.debug("Requestor contact Email is " + salProductRegistration.getRequesterContactEmail());
            cell = row.createCell((short)20);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getRequesterContactEmail());
            
            logger.debug("Requestor contact Phone is " + salProductRegistration.getRequesterContactPhone());
            cell = row.createCell((short)21);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getRequesterContactPhone());
        
        logger.debug("autoSizeColumns start");
        sheet.setDefaultColumnWidth((short)0);
        sheet.setDefaultColumnWidth((short)1);
        sheet.setDefaultColumnWidth((short)2);
        sheet.setDefaultColumnWidth((short)3);
        sheet.setDefaultColumnWidth((short)4);
        sheet.setDefaultColumnWidth((short)5);
        sheet.setDefaultColumnWidth((short)6);
        sheet.setDefaultColumnWidth((short)7);
        sheet.setDefaultColumnWidth((short)8);
        sheet.setDefaultColumnWidth((short)9);
        sheet.setDefaultColumnWidth((short)10);
        sheet.setDefaultColumnWidth((short)11);
        sheet.setDefaultColumnWidth((short)12);
        sheet.setDefaultColumnWidth((short)13);
        sheet.setDefaultColumnWidth((short)14);
        sheet.setDefaultColumnWidth((short)15);
        sheet.setDefaultColumnWidth((short)16);
        sheet.setDefaultColumnWidth((short)17);
        sheet.setDefaultColumnWidth((short)18);
        sheet.setDefaultColumnWidth((short)19);
        sheet.setDefaultColumnWidth((short)20);
        sheet.setDefaultColumnWidth((short)21);
        
        logger.debug("autoSizeColumns end");

        logger.debug("about to call generateAttachment");

        Attachment attachment = generateAttachment(wb, filePath);
        logger.debug(" Exiting  generateWorksheetTechRegModem ...");

        return attachment;
    }


    public Attachment generateWorksheetTechRegSALSiteList(SalProductRegistration salProductRegistration) throws Exception {
       logger.debug(" Registration for Site ID: " + salProductRegistration.getSiteList().getSiteId());
       logger.debug(" Starting  generateWorksheetTechRegSALSiteList ...");
       String filePath=salProductRegistration.getFilePath();
       logger.debug(" filePath ..."+filePath);
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("new sheet");

        HSSFCellStyle headerStyle = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(font);

        // Create a header row.
        HSSFRow headerRow = sheet.createRow(0);
        HSSFCell cell = headerRow.createCell((short)0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Primary Gateway SEid");

        cell = headerRow.createCell((short)1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Secondary Gateway SEid");

        cell = headerRow.createCell((short)2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("SE Code");

        cell = headerRow.createCell((short)3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Product Type");

        cell = headerRow.createCell((short)4);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Sold To");

        cell = headerRow.createCell((short)5);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("ART Error Code");

        cell = headerRow.createCell((short)6);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("ART Error Description");

        cell = headerRow.createCell((short)7);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("User Id");

        cell = headerRow.createCell((short)8);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Opt Type");

        cell = headerRow.createCell((short)9);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact First Name");

        cell = headerRow.createCell((short)10);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact Last Name");

        cell = headerRow.createCell((short)11);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact Email");

        cell = headerRow.createCell((short)12);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact Phone");
        
        cell = headerRow.createCell((short)13);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requestor Contact First Name");
        
        cell = headerRow.createCell((short)14);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requestor Contact Last Name");
        
        cell = headerRow.createCell((short)15);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requestor Contact Email");
        
        cell = headerRow.createCell((short)16);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requestor Contact Phone");

        int rowIndex = 1;

        SiteList siteList = salProductRegistration.getSiteList();

            //logger.debug("siteList.getPrimarySEID()is " + siteList.getSalMigration().getPrimarySEID());
            HSSFRow row = sheet.createRow(rowIndex++);
            cell = row.createCell((short)0);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue("");//siteList.getSalMigration().getPrimarySEID());

            //logger.debug("siteList.getSecondarySEIDis " + siteList.getSalMigration().getSecondarySEID());
            cell = row.createCell((short)1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue("");//siteList.getSalMigration().getSecondarySEID());

            logger.debug("siteList.getSeId() is " + siteList.getSeCode());
            cell = row.createCell((short)2);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(siteList.getSeCode());

            logger.debug("siteList.getProductType() is " +siteList.getProductId());
            cell = row.createCell((short)3);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(siteList.getProductId());

            logger.debug("siteList.getSoldTo() is " +siteList.getSoldToId());
            cell = row.createCell((short)4);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(siteList.getSoldToId());

            logger.debug("  salProductRegistration.getErrorCode() is " +  salProductRegistration.getErrorCode());
            cell = row.createCell((short)5);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue( salProductRegistration.getErrorCode());

            logger.debug("salProductRegistration.getErrorDescription() is " +  salProductRegistration.getErrorDescription());
            cell = row.createCell((short)6);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getErrorDescription());

            //logger.debug("siteList.getSalMigration().getCreatedBy() is " + siteList.getSalMigration().getCreatedBy());
            cell = row.createCell((short)7);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue("");//siteList.getSalMigration().getCreatedBy());

            logger.debug("salProductRegistration.getOptType() is " + salProductRegistration.getOptType());
            cell = row.createCell((short)8);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getOptType());

            logger.debug("OnSite contact FirstName is " + salProductRegistration.getOnsiteContactFirstName());
            cell = row.createCell((short)9);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getOnsiteContactFirstName());

            logger.debug("OnSite contact LastName is " + salProductRegistration.getOnsiteContactLastName());
            cell = row.createCell((short)10);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getOnsiteContactLastName());

            logger.debug("OnSite contact Email is " + salProductRegistration.getOnsiteContactEmail());
            cell = row.createCell((short)11);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getOnsiteContactEmail());

            logger.debug("OnSite contact Phone is " + salProductRegistration.getOnsiteContactPhone());
            cell = row.createCell((short)12);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getOnsiteContactPhone());
            
            logger.debug("Requestor contact FirstName is " + salProductRegistration.getRequesterContactFirstName());
            cell = row.createCell((short)13);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getRequesterContactFirstName());
            
            logger.debug("Requestor contact LastName is " + salProductRegistration.getRequesterContactLastName());
            cell = row.createCell((short)14);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getRequesterContactLastName());
            
            logger.debug("Requestor contact Email is " + salProductRegistration.getRequesterContactEmail());
            cell = row.createCell((short)15);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getRequesterContactEmail());
            
            logger.debug("Requestor contact Phone is " + salProductRegistration.getRequesterContactPhone());
            cell = row.createCell((short)16);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(salProductRegistration.getRequesterContactPhone());
        
        logger.debug("autoSizeColumns start");
        sheet.setDefaultColumnWidth((short)0);
        sheet.setDefaultColumnWidth((short)1);
        sheet.setDefaultColumnWidth((short)2);
        sheet.setDefaultColumnWidth((short)3);
        sheet.setDefaultColumnWidth((short)4);
        sheet.setDefaultColumnWidth((short)5);
        sheet.setDefaultColumnWidth((short)6);
        sheet.setDefaultColumnWidth((short)7);
        sheet.setDefaultColumnWidth((short)8);
        sheet.setDefaultColumnWidth((short)9);
        sheet.setDefaultColumnWidth((short)10);
        sheet.setDefaultColumnWidth((short)11);
        sheet.setDefaultColumnWidth((short)12);
        sheet.setDefaultColumnWidth((short)13);
        sheet.setDefaultColumnWidth((short)14);
        sheet.setDefaultColumnWidth((short)15);
        sheet.setDefaultColumnWidth((short)16);
        
        logger.debug("autoSizeColumns end");

        logger.debug("about to call generateAttachment");

        Attachment attachment = generateAttachment(wb, filePath);
        logger.debug(" Exiting  generateWorksheetTechRegSALSiteList ...");
        return attachment;
    }

    public Attachment generateFinalValidationErrorWorksheet(List<TechnicalOrderDetail> equipmentRemovalResponseDtoList, String filePath, 
    		SiteRegistration siteRegistration, String orderType) throws Exception {
    	logger.debug("EquipmentRemovalResponseDto list size :" + equipmentRemovalResponseDtoList.size());
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = null;

        if (GRTConstants.TECH_ORDER_TYPE_EM.equalsIgnoreCase(orderType)){ 
        	sheet = wb.createSheet("Equipment Move");
        }else{
        	sheet = wb.createSheet("Equipment Removal");
        }
        
        HSSFCellStyle headerStyle = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(font);

        // Create a header row.
        HSSFRow headerRow = sheet.createRow(0);
        HSSFCell cell = headerRow.createCell(0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Summary Equipment Number");

        cell = headerRow.createCell(1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Material Code");

        cell = headerRow.createCell(2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Material Code Description");

        cell = headerRow.createCell(3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Initial Quantity");

        cell = headerRow.createCell(4);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Revised Quantity");

        cell = headerRow.createCell(5);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("SEID");

        cell = headerRow.createCell(6);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Is Active Contract?");

        cell = headerRow.createCell(7);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("SAP Error Message");

        cell = headerRow.createCell(8);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact First Name");

        cell = headerRow.createCell(9);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact Last Name");

        cell = headerRow.createCell(10);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact Email");

        cell = headerRow.createCell(11);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact Phone");
        
        cell = headerRow.createCell(12);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requester Contact First Name");
        
        cell = headerRow.createCell(13);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requester Contact Last Name");
        
        cell = headerRow.createCell(14);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requester Contact Email");
        
        cell = headerRow.createCell(15);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requester Contact Phone");

        int rowIndex = 1;
        for(TechnicalOrderDetail equipmentRemovalResponseDto : equipmentRemovalResponseDtoList) {
            /*if(equipmentRemovalResponseDto.getFinalQuantity() == null || equipmentRemovalResponseDto.getFinalQuantity() == new BigInteger("0")) {
                continue;
            }*/
            HSSFRow row = sheet.createRow(rowIndex++);
            cell = row.createCell(0);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(equipmentRemovalResponseDto.getSummaryEquipmentNumber());

            cell = row.createCell(1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(equipmentRemovalResponseDto.getMaterialCode());

            cell = row.createCell(2);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(equipmentRemovalResponseDto.getDescription());
            
            cell = row.createCell(3);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(equipmentRemovalResponseDto.getInitialQuantity()!=null ? new Double(equipmentRemovalResponseDto.getInitialQuantity().toString()):0);

            cell = row.createCell(4);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(equipmentRemovalResponseDto.getRemainingQuantity()!=null ? new Double(equipmentRemovalResponseDto.getRemainingQuantity().toString()):0);
            
            cell = row.createCell(5);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(equipmentRemovalResponseDto.getSolutionElementId());

            cell = row.createCell(6);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(equipmentRemovalResponseDto.getActiveContractExist());
            
            cell = row.createCell(7);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(equipmentRemovalResponseDto.getErrorDescription());

            if(rowIndex == 2) {
            	logger.debug("OnSite contact FirstName is " + siteRegistration.getOnsiteFirstName());
                cell = row.createCell(8);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(siteRegistration.getOnsiteFirstName());
                
                logger.debug("OnSite contact LastName is " + siteRegistration.getOnsiteLastName());
                cell = row.createCell(9);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(siteRegistration.getOnsiteLastName());
                
                logger.debug("OnSite contact Email is " + siteRegistration.getOnsiteEmail());
                cell = row.createCell(10);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(siteRegistration.getOnsiteEmail());
                
                logger.debug("OnSite contact Phone is " + siteRegistration.getOnsitePhone());
                cell = row.createCell(11);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(siteRegistration.getOnsitePhone());
                
                logger.debug("Requester contact FirstName is " + siteRegistration.getFirstName());
                cell = row.createCell(12);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(siteRegistration.getFirstName());
                
                logger.debug("Requester contact LastName is " + siteRegistration.getLastName());
                cell = row.createCell(13);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(siteRegistration.getLastName());
                
                logger.debug("Requester contact Email is " + siteRegistration.getReportEmailId());
                cell = row.createCell(14);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(siteRegistration.getReportEmailId());
                
                logger.debug("Requester contact Phone is " + siteRegistration.getReportPhone());
                cell = row.createCell(15);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(siteRegistration.getReportPhone());
            }
        }

        sheet.setDefaultColumnWidth((short)0);
        sheet.setDefaultColumnWidth((short)1);
        sheet.setDefaultColumnWidth((short)2);
        sheet.setDefaultColumnWidth((short)3);
        sheet.setDefaultColumnWidth((short)4);
        sheet.setDefaultColumnWidth((short)5);
        sheet.setDefaultColumnWidth((short)6);
        sheet.setDefaultColumnWidth((short)7);
        sheet.setDefaultColumnWidth((short)8);
        sheet.setDefaultColumnWidth((short)9);
        sheet.setDefaultColumnWidth((short)10);
        sheet.setDefaultColumnWidth((short)11);
        sheet.setDefaultColumnWidth((short)8);
        sheet.setDefaultColumnWidth((short)9);
        sheet.setDefaultColumnWidth((short)10);
        sheet.setDefaultColumnWidth((short)11);

        Attachment attachment = generateAttachment(wb, filePath);

        return attachment;
    }
    
    
    // Worksheet for Equipments with Active Contract
    public Attachment generateActiveContractsWorksheet(List<TechnicalOrderDetail> technicalOrderDetailList, String filePath, String accountName,
    		String requestorName, String requestorEmail, String grtContactName, String grtContactEmail) throws Exception {
    	logger.debug("TechnicalOrderDetail list size :" + technicalOrderDetailList.size());
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Equipment Removal Active Contract");

        HSSFCellStyle headerStyle = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(font);

        // Create a header row.
        HSSFRow headerRow = sheet.createRow(0);
        HSSFCell cell = headerRow.createCell(0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Registration ID");

        cell = headerRow.createCell(1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Summary Equipment Number");

        cell = headerRow.createCell(2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Equipment Number");

        cell = headerRow.createCell(3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Account Name");

        cell = headerRow.createCell(4);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Material Code");

        cell = headerRow.createCell(5);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Material Code Description");

        cell = headerRow.createCell(6);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("SoldTo ID");

        cell = headerRow.createCell(7);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Serial Number");

        cell = headerRow.createCell(8);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Initial Quantity");

        cell = headerRow.createCell(9);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Revised Quantity");

        cell = headerRow.createCell(10);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requester Name");

        cell = headerRow.createCell(11);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requester Email");

        cell = headerRow.createCell(12);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("GRT Notification Contact Name");

        cell = headerRow.createCell(13);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("GRT Notification Contact Email");

        int rowIndex = 1;
        for(TechnicalOrderDetail technicalOrderDetail : technicalOrderDetailList) {
            HSSFRow row = sheet.createRow(rowIndex++);
            cell = row.createCell(0);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(technicalOrderDetail.getRegistrationId());

            cell = row.createCell(1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalOrderDetail.getSummaryEquipmentNumber());

            cell = row.createCell(2);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalOrderDetail.getEquipmentNumber());

            cell = row.createCell(3);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(accountName);

            cell = row.createCell(4);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalOrderDetail.getMaterialCode());

            cell = row.createCell(5);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalOrderDetail.getDescription());
            
            cell = row.createCell(6);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalOrderDetail.getSoldToId());

            cell = row.createCell(7);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalOrderDetail.getSerialNumber());

            cell = row.createCell(8);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalOrderDetail.getInitialQuantity());

            cell = row.createCell(9);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalOrderDetail.getRemainingQuantity());

        	logger.debug("RequestorName is " + requestorName);
            cell = row.createCell(10);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(requestorName);

            logger.debug("RequestorEmail is " + requestorEmail);
            cell = row.createCell(11);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(requestorEmail);

            logger.debug("GRT Notification Contact Name is " + grtContactName);
            cell = row.createCell(12);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(grtContactName);

            logger.debug("GRT Notification Contact Email is " + grtContactEmail);
            cell = row.createCell(13);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(grtContactEmail);
        }

        sheet.setDefaultColumnWidth((short)0);
        sheet.setDefaultColumnWidth((short)1);
        sheet.setDefaultColumnWidth((short)2);
        sheet.setDefaultColumnWidth((short)3);
        sheet.setDefaultColumnWidth((short)4);
        sheet.setDefaultColumnWidth((short)5);
        sheet.setDefaultColumnWidth((short)6);
        sheet.setDefaultColumnWidth((short)7);
        sheet.setDefaultColumnWidth((short)8);
        sheet.setDefaultColumnWidth((short)9);
        sheet.setDefaultColumnWidth((short)10);
        sheet.setDefaultColumnWidth((short)11);
        sheet.setDefaultColumnWidth((short)12);
        sheet.setDefaultColumnWidth((short)13);

        Attachment attachment = generateAttachment(wb, filePath);

        return attachment;
    }
    
    
    // Worksheet for Equipments with Active Contract
    public Attachment generateActiveContractsWorksheetEM(List<TechnicalOrderDetail> technicalOrderDetailList, String filePath, String accountName,
    		String requestorName, String requestorEmail, String grtContactName, String grtContactEmail) throws Exception {
    	logger.debug("TechnicalOrderDetail list size :" + technicalOrderDetailList.size());
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Equipment Move Active Contract");

        HSSFCellStyle headerStyle = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(font);

        // Create a header row.
        HSSFRow headerRow = sheet.createRow(0);
        HSSFCell cell = headerRow.createCell(0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Registration ID");

        cell = headerRow.createCell(1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Summary Equipment Number");

        cell = headerRow.createCell(2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Equipment Number");

        cell = headerRow.createCell(3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Account Name");

        cell = headerRow.createCell(4);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Material Code");

        cell = headerRow.createCell(5);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Material Code Description");

        cell = headerRow.createCell(6);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("From SoldTo ID");
        
        cell = headerRow.createCell(7);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("To SoldTo ID");

        cell = headerRow.createCell(8);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Serial Number");

        cell = headerRow.createCell(9);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Initial Quantity");

        cell = headerRow.createCell(10);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Revised Quantity");

        cell = headerRow.createCell(11);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requester Name");

        cell = headerRow.createCell(12);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requester Email");

        cell = headerRow.createCell(13);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("GRT Notification Contact Name");

        cell = headerRow.createCell(14);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("GRT Notification Contact Email");

        int rowIndex = 1;
        for(TechnicalOrderDetail technicalOrderDetail : technicalOrderDetailList) {
            HSSFRow row = sheet.createRow(rowIndex++);
            cell = row.createCell(0);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(technicalOrderDetail.getRegistrationId());

            cell = row.createCell(1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalOrderDetail.getSummaryEquipmentNumber());

            cell = row.createCell(2);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalOrderDetail.getToEquipmentNumber());

            cell = row.createCell(3);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(accountName);

            cell = row.createCell(4);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalOrderDetail.getMaterialCode());

            cell = row.createCell(5);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalOrderDetail.getDescription());
            
            cell = row.createCell(6);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalOrderDetail.getSoldToId());
            
            cell = row.createCell(7);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalOrderDetail.getToSoldToId());

            cell = row.createCell(8);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalOrderDetail.getSerialNumber());

            cell = row.createCell(9);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalOrderDetail.getInitialQuantity());

            cell = row.createCell(10);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(technicalOrderDetail.getRemainingQuantity());

        	logger.debug("RequestorName is " + requestorName);
            cell = row.createCell(11);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(requestorName);

            logger.debug("RequestorEmail is " + requestorEmail);
            cell = row.createCell(12);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(requestorEmail);

            logger.debug("GRT Notification Contact Name is " + grtContactName);
            cell = row.createCell(13);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(grtContactName);

            logger.debug("GRT Notification Contact Email is " + grtContactEmail);
            cell = row.createCell(14);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(grtContactEmail);
        }

        sheet.setDefaultColumnWidth((short)0);
        sheet.setDefaultColumnWidth((short)1);
        sheet.setDefaultColumnWidth((short)2);
        sheet.setDefaultColumnWidth((short)3);
        sheet.setDefaultColumnWidth((short)4);
        sheet.setDefaultColumnWidth((short)5);
        sheet.setDefaultColumnWidth((short)6);
        sheet.setDefaultColumnWidth((short)7);
        sheet.setDefaultColumnWidth((short)8);
        sheet.setDefaultColumnWidth((short)9);
        sheet.setDefaultColumnWidth((short)10);
        sheet.setDefaultColumnWidth((short)11);
        sheet.setDefaultColumnWidth((short)12);
        sheet.setDefaultColumnWidth((short)13);

        Attachment attachment = generateAttachment(wb, filePath);

        return attachment;
    }

    /**
     * Generate Excel worksheet in the specific location based on the TechnicalOrderDetail list
     * @param technicalOrderDtoList List<TechnicalOrderDetail>
     * @param filePath String
     * @return filePath String
     * @throws Exception
     */
    public Attachment generateWorksheetIBaseErrors(List<InstallBaseRespDataDto> installBaseRespDataDtoList, String regId, String filePath, SiteRegistration siteRegistration) throws Exception {
        logger.debug("Install Base error list for Regitration ID: " + installBaseRespDataDtoList.size());
       // String filePath=ipoProductRegistration.getFilePath();
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("new sheet");

        HSSFCellStyle headerStyle = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(font);

        // Create a header row.
        HSSFRow headerRow = sheet.createRow(0);
        HSSFCell cell = headerRow.createCell((short)0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Registration ID");

        cell = headerRow.createCell((short)1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Material Code");

        cell = headerRow.createCell((short)2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Quantity");

        cell = headerRow.createCell((short)3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Serial Number");

        cell = headerRow.createCell((short)4);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Sold To");

        cell = headerRow.createCell((short)5);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Error Description");

        cell = headerRow.createCell((short)6);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact First Name");

        cell = headerRow.createCell((short)7);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact Last Name");

        cell = headerRow.createCell((short)8);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact Email");

        cell = headerRow.createCell((short)9);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Onsite Contact Phone");
        
        cell = headerRow.createCell((short)10);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requestor Contact First Name");
        
        cell = headerRow.createCell((short)11);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requestor Contact Last Name");
        
        cell = headerRow.createCell((short)12);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requestor Contact Email");
        
        cell = headerRow.createCell((short)13);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Requestor Contact Phone");

        int rowIndex = 1;

        for(InstallBaseRespDataDto installBaseRespDataDto : installBaseRespDataDtoList){
        logger.debug("GRT Registration Id is " + regId);
            HSSFRow row = sheet.createRow(rowIndex++);
            cell = row.createCell((short)0);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(regId);

            logger.debug("installBaseRespDataDto.getMaterialCode() is " + installBaseRespDataDto.getMaterialCode());
            cell = row.createCell((short)1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(installBaseRespDataDto.getMaterialCode());

            logger.debug("installBaseRespDataDto.getQuantity() is " + installBaseRespDataDto.getQuantity());
            cell = row.createCell((short)2);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(installBaseRespDataDto.getQuantity());

            logger.debug("installBaseRespDataDto.getSerialNumber() is " + installBaseRespDataDto.getSerialNumber());
            cell = row.createCell((short)3);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(installBaseRespDataDto.getSerialNumber());

            logger.debug("installBaseRespDataDto.getSoldToId() is " + installBaseRespDataDto.getSoldToId());
            cell = row.createCell((short)4);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(installBaseRespDataDto.getSoldToId());

            logger.debug("installBaseRespDataDto.getErrorDesc() is " + installBaseRespDataDto.getErrorDesc());
            cell = row.createCell((short)5);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(installBaseRespDataDto.getErrorDesc());

            if(rowIndex == 2) {
            	logger.debug("OnSite contact FirstName is " + siteRegistration.getOnsiteFirstName());
                cell = row.createCell((short)6);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(siteRegistration.getOnsiteFirstName());
                
                logger.debug("OnSite contact LastName is " + siteRegistration.getOnsiteLastName());
                cell = row.createCell((short)7);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(siteRegistration.getOnsiteLastName());
                
                logger.debug("OnSite contact Email is " + siteRegistration.getOnsiteEmail());
                cell = row.createCell((short)9);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(siteRegistration.getOnsiteEmail());
                
                logger.debug("OnSite contact Phone is " + siteRegistration.getOnsitePhone());
                cell = row.createCell((short)9);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(siteRegistration.getOnsitePhone());
                
                if (siteRegistration.getFirstName()!=null) 
                {
                  logger.debug("Requestor contact FirstName is " + siteRegistration.getFirstName());
                  cell = row.createCell((short)10);
                  cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                  cell.setCellValue(siteRegistration.getFirstName());
                }
                if(siteRegistration.getLastName() != null) 
                {
                  logger.debug("Requestor contact LastName is " + siteRegistration.getLastName());
                  cell = row.createCell((short)11);
                  cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                  cell.setCellValue(siteRegistration.getLastName());
                }
                if (siteRegistration.getReportEmailId() != null) 
                {
                  logger.debug("Requestor contact Email is " + siteRegistration.getReportEmailId());
                  cell = row.createCell((short)12);
                  cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                  cell.setCellValue(siteRegistration.getReportEmailId());
                }
                if (siteRegistration.getReportEmailId() != null)
                {
                  logger.debug("Requestor contact Phone is " + siteRegistration.getReportPhone());
                  cell = row.createCell((short)13);
                  cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                  cell.setCellValue(siteRegistration.getReportPhone());
                }
            }
        }    
        
        
        logger.debug("autoSizeColumns start");
        sheet.setDefaultColumnWidth((short)0);
        sheet.setDefaultColumnWidth((short)1);
        sheet.setDefaultColumnWidth((short)2);
        sheet.setDefaultColumnWidth((short)3);
        sheet.setDefaultColumnWidth((short)4);
        sheet.setDefaultColumnWidth((short)5);
        sheet.setDefaultColumnWidth((short)6);
        sheet.setDefaultColumnWidth((short)7);
        sheet.setDefaultColumnWidth((short)8);
        sheet.setDefaultColumnWidth((short)9);
        sheet.setDefaultColumnWidth((short)10);
        sheet.setDefaultColumnWidth((short)11);
        sheet.setDefaultColumnWidth((short)12);
        sheet.setDefaultColumnWidth((short)13);
        
        logger.debug("autoSizeColumns end");

        logger.debug("about to call generateAttachment");

        Attachment attachment = generateAttachment(wb, filePath);

        return attachment;
    }

    public Attachment generateWSForTR(List<TechnicalRegistration> trs, SiteRegistration siteRegistration, String filePath, String onsiteContactEmail, String onsiteContactPhone, String onsiteContactFirstName, String onsiteContactLastName) throws Exception {
    	logger.debug("Entering generateWSForTR for Site Registration Id:" + siteRegistration.getRegistrationId());
        Attachment attachment = null;
        try {
	        HSSFWorkbook wb = new HSSFWorkbook();
	        HSSFSheet sheet = wb.createSheet("new sheet");

	        HSSFCellStyle headerStyle = wb.createCellStyle();
	        HSSFFont font = wb.createFont();
	        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        headerStyle.setFont(font);

	        // Create a header row.
	        HSSFRow headerRow = sheet.createRow(0);

	        HSSFCell cell = headerRow.createCell((short)0);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Site Reg Id.");

	        cell = headerRow.createCell((short)1);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("SoldTo Id");

	        cell = headerRow.createCell((short)2);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Customer Name");

	        cell = headerRow.createCell((short)3);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Tech Reg Id");

	        cell = headerRow.createCell((short)4);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Material Code");

	        cell = headerRow.createCell((short)5);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Summary Equipment Number");

	        cell = headerRow.createCell((short)6);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Equipment Number");

	        cell = headerRow.createCell((short)7);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Product Line");

	        cell = headerRow.createCell((short)8);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Group Id");

	        cell = headerRow.createCell((short)9);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Access Type");

	        cell = headerRow.createCell((short)10);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("SE Code");

	        cell = headerRow.createCell((short)11);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Product Type");

	        cell = headerRow.createCell((short)12);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Template");

	        cell = headerRow.createCell((short)13);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Dial-In Number");

	        cell = headerRow.createCell((short)14);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Remote Connectivity");

	        cell = headerRow.createCell((short)15);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("IP Address");

	        cell = headerRow.createCell((short)16);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("SID");

	        cell = headerRow.createCell((short)17);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("MID");

	        cell = headerRow.createCell((short)18);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Primary SAL Gateway ID");

	        cell = headerRow.createCell((short)19);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Secondary SAL Gateway ID");

	        cell = headerRow.createCell((short)20);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Opt Type");

	        cell = headerRow.createCell((short)21);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Software Release");

	        cell = headerRow.createCell((short)22);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Hardware Server");

	        cell = headerRow.createCell((short)23);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Random Password");

	        cell = headerRow.createCell((short)24);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Private IP");

	        cell = headerRow.createCell((short)25);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("seid Of Voice");

	        cell = headerRow.createCell((short)26);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Check SES Router");

	        cell = headerRow.createCell((short)27);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Alarm Id");

	        cell = headerRow.createCell((short)28);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("CM Product");

	        cell = headerRow.createCell((short)29);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("CM Main");

	        cell = headerRow.createCell((short)30);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("CM Main SEID");

	        cell = headerRow.createCell((short)31);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("CM Main SoldTo Id");

	        cell = headerRow.createCell((short)32);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("CM Main SID");

	        cell = headerRow.createCell((short)33);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("SP Release");

	        cell = headerRow.createCell((short)34);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("ART Id");

	        cell = headerRow.createCell((short)35);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("ART Error Code");

	        cell = headerRow.createCell((short)36);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("ART Error Sub Code");

	        cell = headerRow.createCell((short)37);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("ART Error Desc");

	        cell = headerRow.createCell((short)38);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Onsite Contact First Name");

	        cell = headerRow.createCell((short)39);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Onsite Contact Last Name");

	        cell = headerRow.createCell((short)40);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Onsite Contact Email");

	        cell = headerRow.createCell((short)41);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Onsite Contact Phone");

	        cell = headerRow.createCell((short)42);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Username");
	        
	        cell = headerRow.createCell((short)43);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Password");

	        int rowIndex = 1;

	        for(TechnicalRegistration tr : trs){
	            HSSFRow row = sheet.createRow(rowIndex++);
	            cell = row.createCell((short)0);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(siteRegistration.getRegistrationId());

	            cell = row.createCell((short)1);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(siteRegistration.getSoldToId());

	            cell = row.createCell((short)2);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(siteRegistration.getCustomerName());

	            cell = row.createCell((short)3);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getTechnicalRegistrationId());

	            cell = row.createCell((short)4);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getTechnicalOrder().getMaterialCode());

	            cell = row.createCell((short)5);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getSummaryEquipmentNumber());

	            cell = row.createCell((short)6);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getEquipmentNumber());
	            cell = row.createCell((short)7);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getTechnicalOrder().getProductLine());
	            cell = row.createCell((short)8);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getGroupId());
	            cell = row.createCell((short)9);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getAccessType());
	            cell = row.createCell((short)10);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getSolutionElement());
	            cell = row.createCell((short)11);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getProductCode());
	            cell = row.createCell((short)12);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getTemplate());
	            cell = row.createCell((short)13);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getDialInNumber());
	            cell = row.createCell((short)14);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getConnectivity());
	            cell = row.createCell((short)15);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getIpAddress());
	            cell = row.createCell((short)16);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getSid());
	            cell = row.createCell((short)17);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getMid());
	            cell = row.createCell((short)18);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getPrimarySalGWSeid());
	            cell = row.createCell((short)19);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getSecondarySalGWSeid());
	            cell = row.createCell((short)20);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getOperationType());
	            cell = row.createCell((short)21);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getSoftwareRelease());
	            cell = row.createCell((short)22);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getHardwareServer());
	            cell = row.createCell((short)23);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getRandomPassword());
	            cell = row.createCell((short)24);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getPrivateIpAddress());
	            cell = row.createCell((short)25);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getSeidOfVoice());
	            cell = row.createCell((short)26);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getCheckSesEdge());
	            cell = row.createCell((short)27);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getAlarmId());
	            cell = row.createCell((short)28);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.isCmProduct());
	            cell = row.createCell((short)29);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.isCmMain());
	            cell = row.createCell((short)30);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getCmMainSeid());
	            cell = row.createCell((short)31);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getCmMainSoldToId());
	            cell = row.createCell((short)32);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getCmMainSid());
	            cell = row.createCell((short)33);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getSpRelease());
	            cell = row.createCell((short)34);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getArtId());
	            cell = row.createCell((short)35);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getErrorCode());
	            cell = row.createCell((short)36);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getSubErrorCode());
	            cell = row.createCell((short)37);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getErrorDesc());

	            if(rowIndex == 2) {
	                cell = row.createCell((short)38);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellValue(onsiteContactFirstName);

	                cell = row.createCell((short)39);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellValue(onsiteContactLastName);

	                cell = row.createCell((short)40);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellValue(onsiteContactEmail);

	                cell = row.createCell((short)41);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellValue(onsiteContactPhone);
	            }
	            
	            cell = row.createCell((short)42);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getUsername());

	            cell = row.createCell((short)43);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(tr.getPassword());

	        }


	        logger.debug("autoSizeColumns start");
	        sheet.setDefaultColumnWidth((short)0);
	        sheet.setDefaultColumnWidth((short)1);
	        sheet.setDefaultColumnWidth((short)2);
	        sheet.setDefaultColumnWidth((short)3);
	        sheet.setDefaultColumnWidth((short)4);
	        sheet.setDefaultColumnWidth((short)5);
	        sheet.setDefaultColumnWidth((short)6);
	        sheet.setDefaultColumnWidth((short)7);
	        sheet.setDefaultColumnWidth((short)8);
	        sheet.setDefaultColumnWidth((short)9);
	        sheet.setDefaultColumnWidth((short)10);
	        sheet.setDefaultColumnWidth((short)11);
	        sheet.setDefaultColumnWidth((short)12);
	        sheet.setDefaultColumnWidth((short)13);
	        sheet.setDefaultColumnWidth((short)14);
	        sheet.setDefaultColumnWidth((short)15);
	        sheet.setDefaultColumnWidth((short)16);
	        sheet.setDefaultColumnWidth((short)17);
	        sheet.setDefaultColumnWidth((short)18);
	        sheet.setDefaultColumnWidth((short)19);
	        sheet.setDefaultColumnWidth((short)20);
	        sheet.setDefaultColumnWidth((short)21);
	        sheet.setDefaultColumnWidth((short)22);
	        sheet.setDefaultColumnWidth((short)23);
	        sheet.setDefaultColumnWidth((short)24);
	        sheet.setDefaultColumnWidth((short)25);
	        sheet.setDefaultColumnWidth((short)26);
	        sheet.setDefaultColumnWidth((short)27);
	        sheet.setDefaultColumnWidth((short)28);
	        sheet.setDefaultColumnWidth((short)29);
	        sheet.setDefaultColumnWidth((short)30);
	        sheet.setDefaultColumnWidth((short)31);
	        sheet.setDefaultColumnWidth((short)32);
	        sheet.setDefaultColumnWidth((short)33);
	        sheet.setDefaultColumnWidth((short)34);
	        sheet.setDefaultColumnWidth((short)35);
	        sheet.setDefaultColumnWidth((short)36);
	        sheet.setDefaultColumnWidth((short)37);
	        sheet.setDefaultColumnWidth((short)38);
	        sheet.setDefaultColumnWidth((short)39);
	        sheet.setDefaultColumnWidth((short)40);
	        sheet.setDefaultColumnWidth((short)41);
	        sheet.setDefaultColumnWidth((short)42);
	        sheet.setDefaultColumnWidth((short)43);
	        logger.debug("autoSizeColumns end");

	        logger.debug("about to call generateAttachment");

	        attachment = generateAttachment(wb, filePath);
	    } catch(Throwable throwable) {
	    	logger.error("", throwable);
	    } finally {
	    	logger.debug("Exiting generateWSForTR for Site Registration Id:" + siteRegistration.getRegistrationId());
	    }
	        return attachment;
	    }

    public Attachment generateWSForSL(List<SiteList> sls, SiteRegistration siteRegistration, String filePath, String onsiteContactEmail, String onsiteContactPhone, String onsiteContactFirstName, String onsiteContactLastName) throws Exception {
        logger.debug("Entering generateWSForSL for Site Registration Id:" + siteRegistration.getRegistrationId());
        Attachment attachment = null;
        try {
	        HSSFWorkbook wb = new HSSFWorkbook();
	        HSSFSheet sheet = wb.createSheet("new sheet");

	        HSSFCellStyle headerStyle = wb.createCellStyle();
	        HSSFFont font = wb.createFont();
	        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        headerStyle.setFont(font);

	        // Create a header row.
	        HSSFRow headerRow = sheet.createRow(0);

	        HSSFCell cell = headerRow.createCell((short)0);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Site Reg Id.");

	        cell = headerRow.createCell((short)1);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("SoldTo Id");

	        cell = headerRow.createCell((short)2);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Customer Name");

	        cell = headerRow.createCell((short)3);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Tech Reg Id");

	        cell = headerRow.createCell((short)4);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Material Code");

	        cell = headerRow.createCell((short)5);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Summary Equipment Number");

	        cell = headerRow.createCell((short)6);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Equipment Number");

	        cell = headerRow.createCell((short)7);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Product Line");

	        cell = headerRow.createCell((short)8);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Group Id");

	        cell = headerRow.createCell((short)9);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("SEID");

	        cell = headerRow.createCell((short)10);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("SE Code");

	        cell = headerRow.createCell((short)11);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Product Type");

	        cell = headerRow.createCell((short)12);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("SID");

	        cell = headerRow.createCell((short)13);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("MID");

	        cell = headerRow.createCell((short)14);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Primary SAL Gateway ID");

	        cell = headerRow.createCell((short)15);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Secondary SAL Gateway ID");


	        cell = headerRow.createCell((short)16);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Software Release");

	        cell = headerRow.createCell((short)17);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Alarm Id");

	        cell = headerRow.createCell((short)18);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("ART Id");

	        cell = headerRow.createCell((short)19);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("ART Error Code");

	        cell = headerRow.createCell((short)20);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("ART Error Sub Code");

	        cell = headerRow.createCell((short)21);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("ART Error Desc");

	        cell = headerRow.createCell((short)22);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Onsite Contact First Name");

	        cell = headerRow.createCell((short)23);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Onsite Contact Last Name");

	        cell = headerRow.createCell((short)24);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Onsite Contact Email");

	        cell = headerRow.createCell((short)25);
	        cell.setCellStyle(headerStyle);
	        cell.setCellValue("Onsite Contact Phone");


	        int rowIndex = 1;

	        for(SiteList sl : sls){
	            HSSFRow row = sheet.createRow(rowIndex++);
	            cell = row.createCell((short)0);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(siteRegistration.getRegistrationId());

	            cell = row.createCell((short)1);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(siteRegistration.getSoldToId());

	            cell = row.createCell((short)2);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(siteRegistration.getCustomerName());

	            cell = row.createCell((short)3);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(sl.getId());

	            cell = row.createCell((short)4);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(sl.getMaterialCode());

	            cell = row.createCell((short)5);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(sl.getSummaryEquipmentNumber());

	            cell = row.createCell((short)6);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(sl.getEquipmentNumber());
	            cell = row.createCell((short)7);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(sl.getProductLine());
	            cell = row.createCell((short)8);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(sl.getGroupId());
	            cell = row.createCell((short)9);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(sl.getSeId());
	            cell = row.createCell((short)10);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(sl.getSeCode());
	            cell = row.createCell((short)11);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(sl.getProductCode());
	            cell = row.createCell((short)12);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(sl.getSid());
	            cell = row.createCell((short)13);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(sl.getMid());
	            cell = row.createCell((short)14);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(sl.getPrimarySALGatewaySEID());
	            cell = row.createCell((short)15);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(sl.getSecondarySALGatewaySEID());
	            cell = row.createCell((short)16);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(sl.getRelease());
	            cell = row.createCell((short)17);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(sl.getAlarmId());
	            cell = row.createCell((short)18);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(sl.getArtId());
	            cell = row.createCell((short)19);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(sl.getErrorCode());
	            cell = row.createCell((short)20);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(sl.getSubErrorCode());
	            cell = row.createCell((short)21);
	            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(sl.getErrorDesc());

	            if(rowIndex == 2) {
	                cell = row.createCell((short)22);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellValue(onsiteContactFirstName);

	                cell = row.createCell((short)23);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellValue(onsiteContactLastName);

	                cell = row.createCell((short)24);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellValue(onsiteContactEmail);

	                cell = row.createCell((short)25);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellValue(onsiteContactPhone);
	            }
	        }


	        logger.debug("autoSizeColumns start");
	        sheet.setDefaultColumnWidth((short)0);
	        sheet.setDefaultColumnWidth((short)1);
	        sheet.setDefaultColumnWidth((short)2);
	        sheet.setDefaultColumnWidth((short)3);
	        sheet.setDefaultColumnWidth((short)4);
	        sheet.setDefaultColumnWidth((short)5);
	        sheet.setDefaultColumnWidth((short)6);
	        sheet.setDefaultColumnWidth((short)7);
	        sheet.setDefaultColumnWidth((short)8);
	        sheet.setDefaultColumnWidth((short)9);
	        sheet.setDefaultColumnWidth((short)10);
	        sheet.setDefaultColumnWidth((short)11);
	        sheet.setDefaultColumnWidth((short)12);
	        sheet.setDefaultColumnWidth((short)13);
	        sheet.setDefaultColumnWidth((short)14);
	        sheet.setDefaultColumnWidth((short)15);
	        sheet.setDefaultColumnWidth((short)16);
	        sheet.setDefaultColumnWidth((short)17);
	        sheet.setDefaultColumnWidth((short)18);
	        sheet.setDefaultColumnWidth((short)19);
	        sheet.setDefaultColumnWidth((short)20);
	        sheet.setDefaultColumnWidth((short)21);
	        sheet.setDefaultColumnWidth((short)22);
	        sheet.setDefaultColumnWidth((short)23);
	        sheet.setDefaultColumnWidth((short)24);
	        sheet.setDefaultColumnWidth((short)25);
	        logger.debug("autoSizeColumns end");
	        attachment = generateAttachment(wb, filePath);
        } catch(Throwable throwable) {
        	logger.error("", throwable);
        } finally {
        	logger.debug("Exiting generateWSForSL for Site Registration Id:" + siteRegistration.getRegistrationId());
        }
        return attachment;
    }
   
    public Attachment generateWSForAlarmAndConnectivity(List<SiteList> sls, List<TechnicalRegistration> trs, SiteRegistration siteRegistration,String filePath) throws Exception 
    {
        logger.debug("Entering generateWSForSL for Site Registration Id:" + siteRegistration.getRegistrationId());
        Attachment attachment = null;
        try {
              HSSFWorkbook wb = new HSSFWorkbook();
              HSSFSheet sheet = wb.createSheet("new sheet");
      
              HSSFCellStyle headerStyle = wb.createCellStyle();
              HSSFFont font = wb.createFont();
              font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
              headerStyle.setFont(font);
      
              // Create a header row.
              HSSFRow headerRow = sheet.createRow(0);
              
              HSSFCell cell = headerRow.createCell((short)0);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("Site Reg Id.");
      
              cell = headerRow.createCell((short)1);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("SoldTo Id");
      
              cell = headerRow.createCell((short)2);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("Customer Name");
              
              cell = headerRow.createCell((short)3);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("Tech Reg Id");
              
              cell = headerRow.createCell((short)4);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("Material Code");
              
              cell = headerRow.createCell((short)5);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("Summary Equipment Number");
              
              cell = headerRow.createCell((short)6);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("Equipment Number");
              
              cell = headerRow.createCell((short)7);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("Group ID");
              cell = headerRow.createCell((short)8);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("SE Code");
              cell = headerRow.createCell((short)9);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("Model");
              cell = headerRow.createCell((short)10);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("Alarm ID");
              cell = headerRow.createCell((short)11);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("SEID");
              cell = headerRow.createCell((short)12);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("Remote Access");
              cell = headerRow.createCell((short)13);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("Transport Alarm");
              cell = headerRow.createCell((short)14);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("Last Alarm Received Date");
              cell = headerRow.createCell((short)15);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("SAL Gateway SEID");
              cell = headerRow.createCell((short)16);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("Device Status");
              
              cell = headerRow.createCell((short)17);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("Onsite Contact First Name");
              
              cell = headerRow.createCell((short)18);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("Onsite Contact Last Name");
              
              cell = headerRow.createCell((short)19);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("Onsite Contact Email");
              
              cell = headerRow.createCell((short)20);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("Onsite Contact Phone");
              
              cell = headerRow.createCell((short)21);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("System ID (SID)");
              
              cell = headerRow.createCell((short)22);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("Module ID (MID)");
              
              cell = headerRow.createCell((short)23);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("Select for Remote Access");
              
              cell = headerRow.createCell((short)24);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("Select for Alarming");
              
              cell = headerRow.createCell((short)25);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("Activity Log");
              
              //Defect #357
              cell = headerRow.createCell((short)26);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("Error Code");
              
              cell = headerRow.createCell((short)27);
              cell.setCellStyle(headerStyle);
              cell.setCellValue("Error Description");
              
              int rowIndex = 1;
              if(sls != null && sls.size() > 0) {
                    for(SiteList sl : sls){    
                        HSSFRow row = sheet.createRow(rowIndex++);
                        cell = row.createCell((short)0);
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        cell.setCellValue(siteRegistration.getRegistrationId());
                        
                        cell = row.createCell((short)1);
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        cell.setCellValue(siteRegistration.getSoldToId());
                        
                        cell = row.createCell((short)2);
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        //cell.setCellValue(siteRegistration.getCustomerName());
                        cell.setCellValue(siteRegistration.getCompany());
                        
                        cell = row.createCell((short)3);
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        cell.setCellValue(sl.getId());
                        
                        cell = row.createCell((short)4);
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        cell.setCellValue(sl.getMaterialCode());
                      
                      cell = row.createCell((short)5);
                      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                      if(StringUtils.isNotEmpty(sl.getSummaryEquipmentNumber())){
                          if(sl.getSummaryEquipmentNumber().equalsIgnoreCase("null")) {
                                cell.setCellValue("");
                          } else {
                                cell.setCellValue(sl.getSummaryEquipmentNumber());
                          }
				      } else if(StringUtils.isNotEmpty(sl.getEquipmentNumber())){
			              if(sl.getEquipmentNumber().equalsIgnoreCase("null")) {
                                cell.setCellValue("");
                          } else {
                                cell.setCellValue(sl.getEquipmentNumber());
                          }
				      }
                      
                      cell = row.createCell((short)6);
                      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                      cell.setCellValue(sl.getEquipmentNumber());

                      cell = row.createCell((short)7);
                      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                      cell.setCellValue(sl.getGroupId());
                      
                      cell = row.createCell((short)8);
                      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                      cell.setCellValue(sl.getSeCode());
                      
                      cell = row.createCell((short)9);
                      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                      cell.setCellValue(sl.getModel());
                      
                      cell = row.createCell((short)10);
                      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                      cell.setCellValue(sl.getAlarmId());
                      
                      cell = row.createCell((short)11);
                      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                      cell.setCellValue(sl.getSolutionElementId());
                      
                      cell = row.createCell((short)12);
                      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                      cell.setCellValue(sl.getRemoteAccess());
                      
                      cell = row.createCell((short)13);
                      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                      cell.setCellValue(sl.getTransportAlarm());
                      
                      cell = row.createCell((short)14);
                      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                      cell.setCellValue(sl.getDeviceLastAlarmReceivedDate());
                      
                      cell = row.createCell((short)15);
                      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                      //cell.setCellValue(sl.getSalGateWaySeid());
                      cell.setCellValue(truncateCharactersFromData(sl.getSalGatewaySeids()));
                    		  
                      cell = row.createCell((short)16);
                      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                      cell.setCellValue(sl.getDeviceStatus());
                      
                        if(rowIndex == 2) {
                                  cell = row.createCell((short)17);
                                  cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                                  cell.setCellValue(siteRegistration.getOnsiteFirstName());
                                  
                                  cell = row.createCell((short)18);
                                  cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                                  cell.setCellValue(siteRegistration.getOnsiteLastName());
                                  
                                  cell = row.createCell((short)19);
                                  cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                                  cell.setCellValue(siteRegistration.getOnsiteEmail());
                                  
                                  cell = row.createCell((short)20);
                                  cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                                  cell.setCellValue(siteRegistration.getOnsitePhone());
                        }
                        
	                    cell = row.createCell((short)21);
	                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    cell.setCellValue(sl.getSid());
	                    
	                    cell = row.createCell((short)22);
	                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    cell.setCellValue(sl.getMid());

	                    cell = row.createCell((short)23);
	                    cell.setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
	                    cell.setCellValue(sl.isSelectForRemoteAccess());

	                    cell = row.createCell((short)24);
	                    cell.setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
	                    cell.setCellValue(sl.isSelectForAlarming());
	                    
	                    //Begin:  Add Child rows For SAL Migration	   
	                    if(!sl.getExpSolutionElements().isEmpty()){
	                    	for(ExpandedSolutionElement ese : sl.getExpSolutionElements()){
	                    		if( ese.getArtRespCode()!=null && !GRTConstants.TR_ALARM_successCode.equalsIgnoreCase(ese.getArtRespCode()) ){
	                    			HSSFRow childRow = sheet.createRow(rowIndex++);
	                    			cell = childRow.createCell((short)0);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(siteRegistration.getRegistrationId());

	                    			cell = childRow.createCell((short)1);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(siteRegistration.getSoldToId());

	                    			cell = childRow.createCell((short)2);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			//cell.setCellValue(siteRegistration.getCustomerName());
	                    			cell.setCellValue(siteRegistration.getCompany());

	                    			cell = childRow.createCell((short)3);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(sl.getId());

	                    			cell = childRow.createCell((short)4);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(""); // Material Code is Empty

	                    			cell = childRow.createCell((short)5);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			//cell.setCellValue(sl.getSummaryEquipmentNumber());// Value taken from Parent Record since Child do not have this
	                    			if(StringUtils.isNotEmpty(sl.getSummaryEquipmentNumber())){
	                    				if(sl.getSummaryEquipmentNumber().equalsIgnoreCase("null")) {
	                    					cell.setCellValue("");
	                    				} else {
	                    					cell.setCellValue(sl.getSummaryEquipmentNumber());
	                    				}
	                    			} else if(StringUtils.isNotEmpty(sl.getEquipmentNumber())){
	                    				if(sl.getEquipmentNumber().equalsIgnoreCase("null")) {
	                    					cell.setCellValue("");
	                    				} else {
	                    					cell.setCellValue(sl.getEquipmentNumber());
	                    				}
	                    			}

	                    			cell = childRow.createCell((short)6);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(sl.getEquipmentNumber());// Value taken from Parent Record since Child do not have this

	                    			cell = childRow.createCell((short)7);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(sl.getGroupId()); // Value taken from Parent Record since Child do not have this

	                    			cell = childRow.createCell((short)8);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(ese.getSeCode());

	                    			cell = childRow.createCell((short)9);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(ese.getModel());

	                    			cell = childRow.createCell((short)10);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(ese.getAlarmId());

	                    			cell = childRow.createCell((short)11);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(ese.getSeID());

	                    			cell = childRow.createCell((short)12);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(ese.getRemoteAccessEligible());

	                    			cell = childRow.createCell((short)13);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(ese.getTransportAlarmEligible());

	                    			cell = childRow.createCell((short)14);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(ese.getDeviceLastAlarmReceivedDate());

	                    			cell = childRow.createCell((short)15);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(truncateCharactersFromData(sl.getSalGatewaySeids())); // Value taken from Parent as suggested

	                    			cell = childRow.createCell((short)16);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(ese.getDeviceStatus());

	                    			//Below information is not required in Child row since it is already prestnt in Parent Row
	                    			// Check with vishal abt the purpose of the below condition.
	                    			//if(rowIndex == 2) { 
	                    			/*cell = childRow.createCell((short)17);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue(siteRegistration.getOnsiteFirstName());

								cell = childRow.createCell((short)18);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue(siteRegistration.getOnsiteLastName());

								cell = childRow.createCell((short)19);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue(siteRegistration.getOnsiteEmail());

								cell = childRow.createCell((short)20);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue(siteRegistration.getOnsitePhone());*/
	                    			//} Check with vishal abt the purpose of the above condition.

	                    			cell = childRow.createCell((short)21);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(ese.getSid());

	                    			cell = childRow.createCell((short)22);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(ese.getMid());

	                    			cell = childRow.createCell((short)23);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
	                    			cell.setCellValue(ese.isSelectForRemoteAccess());

	                    			cell = childRow.createCell((short)24);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
	                    			cell.setCellValue(ese.isSelectForAlarming());	

	                    		}
	                    		
	                    	}
	                    }
	                    //End: Add Child rows For SAL Migration	                    
                    
	                    cell = row.createCell((short)25);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(sl.getTransactionDetails());
                    }    
              } else if(trs != null && trs.size() > 0) {
                    for(TechnicalRegistration tr : trs){    
                        HSSFRow row = sheet.createRow(rowIndex++);
                        cell = row.createCell((short)0);
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        cell.setCellValue(siteRegistration.getRegistrationId());
                        
                        cell = row.createCell((short)1);
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        cell.setCellValue(siteRegistration.getSoldToId());
                        
                        cell = row.createCell((short)2);
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        //cell.setCellValue(siteRegistration.getCustomerName());
                        cell.setCellValue(siteRegistration.getCompany());
                        
                        cell = row.createCell((short)3);
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        cell.setCellValue(tr.getTechnicalRegistrationId());
                        
                      cell = row.createCell((short)4);
                      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                      cell.setCellValue(tr.getTechnicalOrder().getMaterialCode());
                      
                      cell = row.createCell((short)5);
                      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                      if(StringUtils.isNotEmpty(tr.getSummaryEquipmentNumber())){
                          if(tr.getSummaryEquipmentNumber().equalsIgnoreCase("null")) {
                                cell.setCellValue("");
                          } else {
                                cell.setCellValue(tr.getSummaryEquipmentNumber());
                          }
				      } else if(StringUtils.isNotEmpty(tr.getEquipmentNumber())){
			              if(tr.getEquipmentNumber().equalsIgnoreCase("null")) {
                                cell.setCellValue("");
                          } else {
                                cell.setCellValue(tr.getEquipmentNumber());
                          }
				      }

                      
                      cell = row.createCell((short)6);
                      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                      cell.setCellValue(tr.getEquipmentNumber());

                      cell = row.createCell((short)7);
                      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                      cell.setCellValue(tr.getGroupId());
                      
                      cell = row.createCell((short)8);
                      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                      if(StringUtils.isNotEmpty(tr.getTechnicalOrder().getSolutionElementCode())) {
                    	  cell.setCellValue(tr.getTechnicalOrder().getSolutionElementCode());
                      } else {
                    	  cell.setCellValue(tr.getSolutionElement());
                      }
                      
                      cell = row.createCell((short)9);
                      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                      cell.setCellValue(tr.getModel());
                      
                      cell = row.createCell((short)10);
                      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                      cell.setCellValue(tr.getAlarmId());
                      
                      cell = row.createCell((short)11);
                      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                      cell.setCellValue(tr.getSolutionElementId());
                      
                      cell = row.createCell((short)12);
                      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                      cell.setCellValue(tr.getRemoteAccess());
                      
                      cell = row.createCell((short)13);
                      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                      cell.setCellValue(tr.getTransportAlarm());
                      
                      cell = row.createCell((short)14);
                      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                      cell.setCellValue(tr.getDeviceLastAlarmReceivedDate());
                      
                      cell = row.createCell((short)15);
                      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                      cell.setCellValue(truncateCharactersFromData(tr.getSalGateWaySeids()));
                      
                      cell = row.createCell((short)16);
                      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                      cell.setCellValue(tr.getDeviceStatus());
                      
	                    if(rowIndex == 2) {
	                            cell = row.createCell((short)17);
	                              cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                              cell.setCellValue(siteRegistration.getOnsiteFirstName());
	                              
	                              cell = row.createCell((short)18);
	                              cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                              cell.setCellValue(siteRegistration.getOnsiteLastName());
	                              
	                              cell = row.createCell((short)19);
	                              cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                              cell.setCellValue(siteRegistration.getOnsiteEmail());
	                              
	                              cell = row.createCell((short)20);
	                              cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                              cell.setCellValue(siteRegistration.getOnsitePhone());
	                    }
                        
	                    cell = row.createCell((short)21);
	                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    cell.setCellValue(tr.getSid());
	                    
	                    cell = row.createCell((short)22);
	                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    cell.setCellValue(tr.getMid());

	                    cell = row.createCell((short)23);
	                    cell.setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
	                    cell.setCellValue(tr.isSelectForRemoteAccess());

	                    cell = row.createCell((short)24);
	                    cell.setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
	                    cell.setCellValue(tr.isSelectForAlarming());
                        
	                    //Begin: Add the Child rows
	                    //Defect # 635 Added to 
	                    //if(!tr.getExpSolutionElements().isEmpty()){
	                    if(!tr.getExplodedSolutionElements().isEmpty()){	                    
	                    	for(ExpandedSolutionElement ese : tr.getExplodedSolutionElements()){
	                    		
	                    		//Defect 751 : add only those row that are errored out
	                    		if( ese.getArtRespCode()!=null && !GRTConstants.TR_ALARM_successCode.equalsIgnoreCase(ese.getArtRespCode()) ){
	                    			HSSFRow childRow = sheet.createRow(rowIndex++);
	                    			cell = childRow.createCell((short)0);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(siteRegistration.getRegistrationId());

	                    			cell = childRow.createCell((short)1);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(siteRegistration.getSoldToId());

	                    			cell = childRow.createCell((short)2);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			//cell.setCellValue(siteRegistration.getCustomerName());
	                    			cell.setCellValue(siteRegistration.getCompany());

	                    			cell = childRow.createCell((short)3);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(tr.getTechnicalRegistrationId());

	                    			cell = childRow.createCell((short)4);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(""); // Material Code is Empty

	                    			cell = childRow.createCell((short)5);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			//cell.setCellValue(tr.getSummaryEquipmentNumber());// Value taken from Parent Record since Child do not have this
	                    			if(StringUtils.isNotEmpty(tr.getSummaryEquipmentNumber())){
	                    				if(tr.getSummaryEquipmentNumber().equalsIgnoreCase("null")) {
	                    					cell.setCellValue("");
	                    				} else {
	                    					cell.setCellValue(tr.getSummaryEquipmentNumber());
	                    				}
	                    			} else if(StringUtils.isNotEmpty(tr.getEquipmentNumber())){
	                    				if(tr.getEquipmentNumber().equalsIgnoreCase("null")) {
	                    					cell.setCellValue("");
	                    				} else {
	                    					cell.setCellValue(tr.getEquipmentNumber());
	                    				}
	                    			}

	                    			cell = childRow.createCell((short)6);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(tr.getEquipmentNumber());// Value taken from Parent Record since Child do not have this

	                    			cell = childRow.createCell((short)7);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(tr.getGroupId()); // Value taken from Parent Record since Child do not have this

	                    			cell = childRow.createCell((short)8);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(ese.getSeCode());

	                    			cell = childRow.createCell((short)9);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(ese.getModel());

	                    			cell = childRow.createCell((short)10);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(ese.getAlarmId());

	                    			cell = childRow.createCell((short)11);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(ese.getSeID());

	                    			cell = childRow.createCell((short)12);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(ese.getRemoteAccessEligible());

	                    			cell = childRow.createCell((short)13);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(ese.getTransportAlarmEligible());

	                    			cell = childRow.createCell((short)14);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(ese.getDeviceLastAlarmReceivedDate());

	                    			cell = childRow.createCell((short)15);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(truncateCharactersFromData(ese.getIpAddress()));

	                    			cell = childRow.createCell((short)16);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(ese.getDeviceStatus());

	                    			//Below information is not required in Child row since it is already prestnt in Parent Row
	                    			// Check with vishal abt the purpose of the below condition.
	                    			//if(rowIndex == 2) { 
	                    			/*cell = childRow.createCell((short)17);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue(siteRegistration.getOnsiteFirstName());

								cell = childRow.createCell((short)18);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue(siteRegistration.getOnsiteLastName());

								cell = childRow.createCell((short)19);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue(siteRegistration.getOnsiteEmail());

								cell = childRow.createCell((short)20);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue(siteRegistration.getOnsitePhone());*/
	                    			//} Check with vishal abt the purpose of the above condition.

	                    			cell = childRow.createCell((short)21);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(ese.getSid());

	                    			cell = childRow.createCell((short)22);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			cell.setCellValue(ese.getMid());

	                    			cell = childRow.createCell((short)23);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
	                    			cell.setCellValue(ese.isSelectForRemoteAccess());

	                    			cell = childRow.createCell((short)24);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
	                    			cell.setCellValue(ese.isSelectForAlarming());	

	                    			//Defect #356: add art error and desc
	                    			cell = childRow.createCell((short)26);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			//cell.setCellValue(tr.getErrorCode());
	                    			cell.setCellValue(ese.getArtRespCode());

	                    			cell = childRow.createCell((short)27);
	                    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                    			//cell.setCellValue(tr.getErrorDesc());
	                    			cell.setCellValue(ese.getArtRespMsg());
	                    		}
	                    	}
	                    }
	                    //End: Add the Child rows
	                    
	                    cell = row.createCell((short)25);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(tr.getTransactionDetails());
						
						//Defect #356: add art error and desc
						cell = row.createCell((short)26);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(tr.getErrorCode());

						cell = row.createCell((short)27);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(tr.getErrorDesc());
                    }
              }    
                         
              logger.debug("autoSizeColumns start");
              sheet.setDefaultColumnWidth((short)0);
              sheet.setDefaultColumnWidth((short)1);
              sheet.setDefaultColumnWidth((short)2);
              sheet.setDefaultColumnWidth((short)3);
              sheet.setDefaultColumnWidth((short)4);
              sheet.setDefaultColumnWidth((short)5);
              sheet.setDefaultColumnWidth((short)6);
              sheet.setDefaultColumnWidth((short)7);
              sheet.setDefaultColumnWidth((short)8);
              sheet.setDefaultColumnWidth((short)9);
              sheet.setDefaultColumnWidth((short)10);
              sheet.setDefaultColumnWidth((short)11);
              sheet.setDefaultColumnWidth((short)12);
              sheet.setDefaultColumnWidth((short)13);
              sheet.setDefaultColumnWidth((short)14);
              sheet.setDefaultColumnWidth((short)15);
              sheet.setDefaultColumnWidth((short)16);
              sheet.setDefaultColumnWidth((short)17);
              sheet.setDefaultColumnWidth((short)18);
              sheet.setDefaultColumnWidth((short)19);
              sheet.setDefaultColumnWidth((short)20);
              sheet.setDefaultColumnWidth((short)21);
              sheet.setDefaultColumnWidth((short)22);
              sheet.setDefaultColumnWidth((short)23);
              sheet.setDefaultColumnWidth((short)24);
              sheet.setDefaultColumnWidth((short)25);
              
              logger.debug("autoSizeColumns end");
              attachment = generateAttachment(wb, filePath);
        } catch(Throwable throwable) {
            logger.error("", throwable);
        } finally {
            logger.debug("Exiting generateWSForSL for Site Registration Id:" + siteRegistration.getRegistrationId());
        }
        return attachment;
    }
    
    private String truncateCharactersFromData(String data){
    	String result = "";
    	if(StringUtils.isEmpty(data)){
    		return result;
    	}
    	data = data.replace("+", "##");
		String[] seidArr = data.split("##");
		if(seidArr.length > 0){
			for(int i=0; i<seidArr.length ; i++){
				if(result.length() > 0){
					result += "+";
				}
				result += truncateCharacters(seidArr[i]);
			}
		} else {
			result = data;
		}
		return result;
	}
	
	private String truncateCharacters(String data){
		int index = data.indexOf(">");
		int endIndex = data.indexOf("<", index) > -1?data.indexOf("<", index):data.length();
		if(index > -1){
			return data!=null ? data.substring(index+1,endIndex):"";
		} else {
			return data;
		}
	}
}