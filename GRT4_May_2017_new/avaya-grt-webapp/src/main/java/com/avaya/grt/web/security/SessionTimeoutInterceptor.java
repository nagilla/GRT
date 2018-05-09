package com.avaya.grt.web.security;

import java.util.Map;
import org.apache.commons.lang.StringUtils;

import com.grt.util.GRTConstants;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class SessionTimeoutInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Map<String, Object> session = invocation.getInvocationContext().getSession();

		if (session != null && session.containsKey(GRTConstants.CSS_USER_PROFILE)) {

			CSSPortalUser sessionUser = (CSSPortalUser) session
					.get(GRTConstants.CSS_USER_PROFILE);
            
			if (sessionUser == null || StringUtils.isEmpty(sessionUser.getUserId())) {
				
				return Action.LOGIN;
			}
		}
		String result = invocation.invoke();
		return result;
	}
}
