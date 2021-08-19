package hlife.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.util.Properties;


/**
 * 读取系统参数
 * 
 * @author Mchi
 * @date 2019年8月30日
 */
public  class PropertiesUtils {
	private static Logger log = LoggerFactory.getLogger(PropertiesUtils.class);

	private static String configPath = "config.properties";
	private static String paramPath = "param.properties";
	private static String evnPath = "evn.properties";
	/**
	 * 通过指定的key来获取配置文件中对应的value,支持使用默认值
	 * 
	 * @param key
	 * @param defValue
	 * @return
	 */
	private static String getConfigValue(String key, String defValue,String path) {
		String value = null;
		Properties prop = new Properties();
		try {
			prop.load(new InputStreamReader(PropertiesUtils.class.getClassLoader().getResourceAsStream(path), "UTF-8"));   
			value = prop.getProperty(key);
			if (value == null || "".equals(value)) {
				value = defValue;
			}
		} catch (Exception ex) {
			log.warn("Failed to load " + path + "(ingore this file)" + ex.getMessage());
			log.error("读取配置文件出错，请检查配置文件的路径是否正确！错误信息为：" + ex.getMessage());
			value = defValue;
		}
		return value;
	}


	/**
	 * 通过指定key获取config文件中的值
	 * @param key
	 * @return
	 */
	public static String getConfigValue(String key) {
		return getConfigValue(key,null,configPath);
	}
	
	/**
	 * 通过指定key获取param文件中的值
	 * @param key
	 * @return
	 */
	public static String getParamValue(String key) {
		return getConfigValue(key,null,paramPath);
	}
	
	/**
	 * 通过指定key获取evn文件中的值
	 * @param key
	 * @return
	 */
	public static String getEvnValue(String key) {
		return getConfigValue(key,null,evnPath);
	}
	

}
