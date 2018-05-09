package com.grt.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException; 
import java.io.InputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
/*
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
*/
import com.grt.dto.Attachment;

public abstract class WorsheetProcessor {
	
    private final Logger logger = Logger.getLogger(getClass());
    
    public Attachment generateAttachment(HSSFWorkbook wb, String filePath) throws FileNotFoundException, IOException {
        // Make dir and write the output to a file
    	// [AVAYA]: 09-26-2011 Log Enhancement (Start)
    	logger.debug("Entering generateAttachment using new API :" + filePath);
    	// [AVAYA]: 09-26-2011 Log Enhancement (End)
        makeDirIfNotExist(filePath);

        FileOutputStream fileOut = new FileOutputStream(filePath);
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        logger.debug("Generate worksheet file: " + filePath);

        // Read the output a file back and set it to attachment and return
        Attachment attachment = new Attachment();

        if(filePath != null && filePath.split("/").length != 0 ) {
            String[] filenames = filePath.split("/");
            String filename = filenames[filenames.length-1];
            attachment.setFileName(filename);

            int last = filePath.lastIndexOf(".");
            if(last != -1) {
                String fileExtension = filePath.substring(last);
                attachment.setFileExtension(fileExtension);
            }
        }

        File f = new File(filePath);
        InputStream inp = new FileInputStream(f);
        byte[] fileContents = new byte[inp.available()];
        inp.read(fileContents);
        attachment.setFileContents(fileContents);
        inp.close();
        boolean success = f.delete();
        if (success){
            logger.debug("Generated worksheet file deleted: " + filePath);
        } else {
            logger.debug("Generated worksheet file deletion failed: " + filePath);
        }
        return attachment;
    }

    private Boolean makeDirIfNotExist(String filePath) {
        int lastSlash = filePath.lastIndexOf("/");
        if(lastSlash == -1) {
            return false;
        }
        String fileDir = filePath.substring(0, lastSlash);
        File file = new File(fileDir);
        if(file.exists()) {
            return true;
        }
        return file.mkdir();
    }

	protected boolean isValidHeader(HSSFRow header, String[] headerStrings) {
		
	    //Iterator cellIte = header.cellIterator();	    
		for(int i = 0; i < headerStrings.length; i++) {	
			//HSSFCell cell = (HSSFCell) cellIte.next();
			String headerString=header.getCell((short) i).getStringCellValue();
			logger.debug(headerStrings[i]+ ":::"+headerString);
			if(!headerStrings[i].equalsIgnoreCase(headerString)) {
				return false;
			}
		}
		return true;
	}
    protected boolean isValidHeader(XSSFRow header, String[] headerStrings) {    	
	    //Iterator cellIte = header.cellIterator();	    
		for(int i = 0; i < headerStrings.length; i++) {	
			//HSSFCell cell = (HSSFCell) cellIte.next();			
			String headerString=header.getCell((short) i).getStringCellValue();
			logger.debug(headerStrings[i]+ ":::"+headerString);
			if(!headerStrings[i].equalsIgnoreCase(headerString)) {
				return false;
			}
		}		
		return true;
	}
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

}
