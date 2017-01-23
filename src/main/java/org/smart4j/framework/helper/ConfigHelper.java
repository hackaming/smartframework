package org.smart4j.framework.helper;

import java.util.Properties;

import org.smart4j.framework.ConfigConstant;
import org.smart4j.framework.util.PropsUtil;

public final class ConfigHelper {
	private static final Properties CONFIG_PROPS=PropsUtil.loadProps(ConfigConstant.CONFIG_FIle);
}
