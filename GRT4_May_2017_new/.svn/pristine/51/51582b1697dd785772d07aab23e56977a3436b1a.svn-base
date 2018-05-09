
<% 

	String path = request.getContextPath(); 
	if(path.length() == 0) path = "/";
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	
	
	//store for JSTL to use 
	request.setAttribute("_path",path); 
	request.setAttribute("_basepath",basePath); 
			
	// figuring out what these are for testing (John)
	String requestUri = request.getRequestURI();
	String contextPath = request.getContextPath();
	String servletPath = request.getServletPath();
	String pathInfo = request.getPathInfo();
	String queryString = request.getQueryString();
	
	//queryString = com.glg.avaya.util.StringUtils.urlEncode(queryString);

	request.setAttribute("_requestUri", requestUri);
	request.setAttribute("_contextPath", contextPath);
	request.setAttribute("_servletPath", servletPath);
	request.setAttribute("_pathInfo", pathInfo);
	request.setAttribute("_queryString", queryString);
	
	String section = "";
	if (servletPath != null && !"".equals(servletPath)) {
		String[] split = servletPath.split("/");
		if (split.length > 1)
			section = split[split.length - 2];
	}

	request.setAttribute("_section", section);		
			
%>