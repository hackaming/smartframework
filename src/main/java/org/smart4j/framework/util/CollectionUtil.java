package org.smart4j.framework.util;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollectionUtil {
	private static final Logger logger = LoggerFactory.getLogger(CollectionUtil.class);
	public static boolean isEmpty(Collection <?> collection){
		return CollectionUtils.isEmpty(collection);
	}
	public static boolean isNotEmpty(Collection<?> collection){
		return CollectionUtils.isNotEmpty(collection);
	}
	public static boolean isEmpty(Map<?,?> map){
		return MapUtils.isEmpty(map);
	}
	public static boolean isNotEmpty(Map<?,?> map){
		return MapUtils.isNotEmpty(map);
	}

}
