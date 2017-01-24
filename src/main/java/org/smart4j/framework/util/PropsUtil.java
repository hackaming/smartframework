package org.smart4j.framework.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropsUtil {
	private static final Logger logger = LoggerFactory.getLogger(PropsUtil.class);

	public static Properties loadProps(String filename) {
		Properties props = null;
		InputStream is = null;
		try {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
			if (null == is){
				throw new FileNotFoundException();
			}
			props = new Properties();
			props.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return props;
	}
	public static String getString(Properties props,String key){
		return getString(props,key,"");
	}
	public static String getString(Properties props,String key,String defaultValue){
		String value = defaultValue;
		if (props.containsKey(key)){
			value = props.getProperty(key);
		}
		return value;
	}
	
	public static int getInt(Properties props,String key){
		return getInt(props,key,0);
	}
	
	public  static int getInt(Properties props,String key,int DefaultValue){
		int value = DefaultValue;
		if (props.containsKey(key)){
			value = CastUtil.castInt(props.getProperty(key));
		}
		return value;
	}
	public static boolean getBoolean(Properties props,String key){
		return getBoolean(props,key,false);
	}
	public static boolean getBoolean(Properties props,String key, Boolean defaultValue){
		Boolean value = defaultValue;
		if (props.containsKey(key)){
			value = CastUtil.castBoolean(props.getProperty(key));
		}
		return value;
	}
}
