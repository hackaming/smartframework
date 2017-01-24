package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
	private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	public static <T> String toJson(T obj){
		String json;
		try{
			json = OBJECT_MAPPER.writeValueAsString(obj);
		} catch(Exception e){
			logger.error("convert POJO to JSON failure",e);
			throw new RuntimeException();
		}
		return json;
	}
	public static <T> T fromJson(String json,Class<T> type){
		T pojo;
		try{
			pojo = OBJECT_MAPPER.readValue(json, type);
		} catch(Exception e){
			logger.error("Can't convert JSON to POJO",e);
			throw new RuntimeException();
		}
		return pojo;
	}

}
