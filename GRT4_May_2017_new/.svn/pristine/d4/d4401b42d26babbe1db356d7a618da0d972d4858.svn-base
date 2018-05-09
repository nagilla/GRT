package com.avaya.grt.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


public class DownloadFileDataUtil extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	
	static final long serialVersionUID = 1L;
	//private String filepath = "/tmp/EQR.xls";
	//private String filepath = "/EQR.xls";
	private String filepath = "";
	private static final Logger logger = Logger.getLogger(DownloadFileDataUtil.class);
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String filename = (request.getPathInfo()).substring(1);
		//filepath = filename;
		filepath = "/tmp/"+filename;
		logger.debug("Entering doGet");
		try {
			FileInputStream fileStream = new FileInputStream(new File(filepath));
			if (fileStream != null) {
				byte[] fileBytes = org.apache.poi.util.IOUtils.toByteArray(fileStream);
				response.resetBuffer();
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition", "attachment; filename=\""+filename+"\"");
				response.setContentLength(fileBytes.length);
				ServletOutputStream sout = response.getOutputStream();
				sout.write(fileBytes);
				response.flushBuffer();
				sout.close();
				fileStream.close();
			}
		} catch (Throwable throwable) {
			logger.error("", throwable);
		} finally {
			logger.debug("exiting doGet");
		}
	}
}
