package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CastUtil {
	private static final Logger logger = LoggerFactory.getLogger(CastUtil.class);
	
	public static String castString(Object obj){
		return castString(obj,"");
	}
	public static String castString(Object obj,String defaultValue){
		return obj==null?String.valueOf(obj):defaultValue;
	}
	public static long castLong(Object obj){
		return castLong(obj,0L);
	}
	public static long castLong(Object obj,long defaultValue){
		long value = defaultValue;
		if (null != obj){
			String strValue = castString(obj);
			value = Long.parseLong(strValue);
		}
		return value;
	}
	public static int castInt(Object obj){
		return castInt(obj,0);
	}
	public static int castInt(Object obj,int defaultValue){
		int value = defaultValue;
		if (null != obj) {
			String strValue = castString(obj);
			if (StringUtil.isNotEmpty(strValue)) {
				try {
					value = Integer.parseInt(strValue);
				} catch (NumberFormatException e) {
					logger.error("Can't cast string to integer");
				}
			}
		}
		return value;
	}
	public static boolean castBoolean(Object obj){
		return castBoolean(obj,false);
	}
	public static boolean castBoolean(Object obj,boolean defaultValue){
		Boolean value = defaultValue;
		if (null != obj){
			value = Boolean.parseBoolean(castString(obj));		
		}
		return value;
	}
}
