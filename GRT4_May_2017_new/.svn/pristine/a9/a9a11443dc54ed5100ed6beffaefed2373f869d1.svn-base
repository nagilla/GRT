package com.grt.util;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class GenericSort implements Comparator<Object>  
{  
      
    private List<String> getters;  
    private boolean ascending;  
      
    public GenericSort(String getter, boolean ascending)  
    {  
        this.getters = new ArrayList<String>();  
        for (String name : getter.split("\\.")) {  
            if (!name.startsWith("get")) {  
                name = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);  
            }  
            this.getters.add(name);  
        }  
        this.ascending = ascending;
    }  
       
    public int compare(Object o1, Object o2) {  
         try {  
                Iterator<String> iter = getters.iterator();  
                while (o1 != null && o2 != null && iter.hasNext()) {  
                    String getter = iter.next();  
                    o1 = o1.getClass().getMethod(getter, new Class[0]).invoke(o1, new Object[0]);  
                    o2 = o2.getClass().getMethod(getter, new Class[0]).invoke(o2, new Object[0]);  
                }  
            } catch (Exception e) {  
                  
                throw new RuntimeException("Cannot compare " + o1 + " with " + o2 + " on " + getters, e);  
            }  
  
            if (ascending) {  
                return (o1 == null) ? -1 : ((o2 == null) ? 1 : ((Comparable<Object>) o1).compareTo(o2));  
            } else {  
                return (o1 == null) ? 1 : ((o2 == null) ? -1 : ((Comparable<Object>) o2).compareTo(o1));  
            }  
    }  
      
}