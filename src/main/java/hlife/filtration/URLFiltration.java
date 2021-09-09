package hlife.filtration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.annotations.VisibleForTesting;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.testng.annotations.Test;
import hlife.base.Constants;
import hlife.httpclient.HttpClient;
import hlife.utils.PropertiesUtils;
import hlife.utils.md5.RSAUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * url过滤器，添加host，签名等
 * 
 * @author Mchi
 *
 */
public class URLFiltration {

	/**
	 * 过滤url 拼接对应服务器路径
	 * 
	 * @param url
	 * @return
	 */
	public static String addHost(String url) {
		String BASEURL_API = PropertiesUtils.getEvnValue("BASEURL_API");
		String BASEURL_4001 = PropertiesUtils.getEvnValue("BASEURL_4001");
		String BASEURL_PASSPORT = PropertiesUtils.getEvnValue("BASEURL_PASSPORT");
		if (!"".equals(url)) {
			if (url.contains("union") == true) {
				url = BASEURL_API + url ;
			}
			if(url.contains("admin") == true){
				url = BASEURL_4001 + url ;
			}
			if(url.contains("v2")==true || url.contains("v1")==true){
				url = BASEURL_PASSPORT + url ;
			}
		}
		return url;
	}

	/**
	 * 添加header
	 */
	public static HashMap<String,String> addHeader(HashMap<String,String> header){
		String apptype = PropertiesUtils.getEvnValue("apptype");
		String clientkey = PropertiesUtils.getParamValue("clientkey");
		String sign = PropertiesUtils.getParamValue("sign");
		String timestamp = PropertiesUtils.getParamValue("timestamp");
		header.put("apptype",apptype);
		header.put("clientkey",clientkey);
		header.put("sign",sign);
		header.put("timestamp",timestamp);
		return header;
	}

	/**
	 * url加密
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static HashMap<String,String> getURLEncoder(HashMap<String,String> params) throws UnsupportedEncodingException {
		HashMap<String,String> newParams = new HashMap<>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			//System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
			String value = URLEncoder.encode(entry.getValue(), "utf-8");
			newParams.put(entry.getKey(),value);
		}
		return newParams;
	}

}
