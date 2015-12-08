package com.jiuyi.vggle.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description 系统配置参数读取类
 * @author zhb
 * @createTime 2015年4月25日
 */
public class SysCfg {
	// 用于存于配置参数
    private static Map<String, String> map;

	// 静态块一次性加载系统配置参数到map中
    public static void init() {
        map = new ConcurrentHashMap<String, String>();
		Properties prop = new Properties();
		InputStream in = SysCfg.class.getResourceAsStream("/syscfg.properties");
		try {
			prop.load(in);
			for (Object key : prop.keySet()) {
				map.put(key.toString(), prop.getProperty(key.toString()).trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    /**
     * @description 根据参数名获取字符串参数值
     * @param key
     * @return
     */
    public static String getString(String key) {
        String value = map.get(key);
        if (value == null) {
            return value;
        }
        return value.trim();
	}

    /**
     * @description 根据参数名获取整型参数值
     * @param key
     * @return
     */
    public static int getInt(String key) {
        String value = map.get(key);
        if (value == null) {
            return 0;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * @description 根据参数名获取整型参数值
     * @param key
     * @return
     */
    public static long getLong(String key) {
        String value = map.get(key);
        if (value == null) {
            return 0;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }
}