package org.smart4j.framework.util;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArrayUtil {
	public static final Logger logger = LoggerFactory.getLogger(ArrayUtil.class);
	
	public static boolean isEmpty(Object[] array){
		return ArrayUtils.isEmpty(array);
	}
	public static boolean isNotEmpty(Object[] array){
		return ArrayUtils.isNotEmpty(array);
	}
}
