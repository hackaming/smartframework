package org.smart4j.framework.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.util.ReflectionUtil;

public class BeanHelper {
	private static final Logger logger = LoggerFactory.getLogger(BeanHelper.class);
	private static final Map<Class<?>,Object> BEAN_MAP = new HashMap<Class<?>,Object>();
	
	static{
		Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
		for (Class<?>cls : beanClassSet){
			Object obj = ReflectionUtil.newInstance(cls);
			BEAN_MAP.put(cls, obj);
		}
	}
	
	public static Map<Class<?>,Object> getBeanMap(){
		return BEAN_MAP;
	}
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> cls){
		if (!BEAN_MAP.containsKey(cls)){
			throw new RuntimeException("Can't get beay by class:" + cls);
		}
		return (T) BEAN_MAP.get(cls);
	}
	public static void setBean(Class<?> cls, Object obj){
		BEAN_MAP.put(cls, obj);
	}

}
