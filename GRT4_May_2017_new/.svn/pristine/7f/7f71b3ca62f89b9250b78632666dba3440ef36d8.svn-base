/**
 * 
 */
package com.grt.util;

/**
 * @author Deepa_Vadakkath
 *
 */
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class AutoCompleteCache implements Serializable{
	private static final long serialVersionUID = 1136714232484898762L;
	//Log log = LogFactory.getLog(this.getClass());
	private List<String> dataList = null;
	private String cacheParam = new String("");

	public void setCache(List<String> dataList) {
		this.dataList = dataList;
	}

	public boolean hasCache() {
		return dataList != null;
	}

	public List<String> getFilteredDataList(String reg){ 
		return filterDataList(reg);
	}

	private List<String> filterDataList(String reg) {
		List<String> filteredDataList = new LinkedList<String>();
		if( dataList!=null ){
			for (String data : dataList) {
				if (isMatchString(data, reg)) {
					filteredDataList.add(data);
				}
			}
		}

		return filteredDataList;
	}

	public List<String> getDataList() {
		return dataList;
	}

	private boolean isMatchString(String str, String reg){
		boolean isMatch = false;
		if (StringUtils.isNotEmpty(reg) && StringUtils.isNotEmpty(str)) {
			/*String regEx= "^" + reg;   
	        Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
	        Matcher m = p.matcher(str);
	        isMatch = m.find();*/
			isMatch = str.toUpperCase().contains(reg.toUpperCase().trim());
		}

		return isMatch;
	}

	public String getCacheParam() {
		return cacheParam;
	}

	public void setCacheParam(String cacheParam) {
		this.cacheParam = cacheParam;
	}
}