/**
 * 
 */
package com.grt.util;

import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.engine.TypedValue;

/**
 * @author administrator
 *
 */
public class StatusLikeExpression implements Criterion{ 
	private static final long serialVersionUID = 1L;
	private String propertyName;
	private String value;
	private boolean flag;

	public StatusLikeExpression(String propertyName, String value, boolean flag) {
	    this.propertyName = propertyName;
	    this.value = value;
	    this.flag = flag;
	}

	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
	    String[] columns = criteriaQuery.getColumnsUsingProjection(criteria, propertyName);
	    if (columns.length != 1) { 
	        throw new HibernateException("Like may only be used with single-column properties");
	    }
	    if(flag){
	    	return "lower(" + columns[0] + ") like ?";
	    } else {
	    	return "lower(" + columns[0] + ") not like ?";
	    }
	}

	public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
		 TypedValue[] tyr = new TypedValue[] { new TypedValue(new org.hibernate.type.StringType(),
	            MatchMode.START.toMatchString(value), EntityMode.POJO) };
	    return new TypedValue[] { new TypedValue(new org.hibernate.type.StringType(),
	            MatchMode.START.toMatchString(value), EntityMode.POJO) };
	}


}
