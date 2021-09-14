package hlife.filtration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.annotations.VisibleForTesting;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.testng.Reporter;
import org.testng.annotations.Test;
import hlife.base.Constants;
import hlife.httpclient.HttpClient;
import hlife.utils.PropertiesUtils;
import hlife.utils.md5.RSAUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * url过滤器，添加host，签名等
 * 
 * @author Mchi
 *
 */
public class URLFiltration {

	/**
	 * 添加BASEURL_API域名
	 * @param url
	 * @return
	 */
	public static String addHost_BASEURL_API(String url) {
		String BASEURL_API = PropertiesUtils.getEvnValue("BASEURL_API");
		if (!"".equals(url)) {
				url = BASEURL_API + url ;
		}
		return url;
	}

	/**
	 * 添加BASEURL_4001域名
	 * @param url
	 * @return
	 */
	public static String addHost_BASEURL_4001(String url) {
		String BASEURL_4001 = PropertiesUtils.getEvnValue("BASEURL_4001");
		if (!"".equals(url)) {
				url = BASEURL_4001 + url ;
		}
		return url;
	}

	/**
	 * 添加BASEURL_PASSPORT域名
	 * @param url
	 * @return
	 */
	public static String addHost_BASEURL_PASSPORT(String url) {
		String BASEURL_PASSPORT = PropertiesUtils.getEvnValue("BASEURL_PASSPORT");
		if (!"".equals(url)) {
				url = BASEURL_PASSPORT + url ;
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
		Iterator iterator = header.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<String,String> elem = (Map.Entry<String, String>) iterator.next();
			System.out.println(elem.getKey()+":"+elem.getValue());
			Reporter.log(elem.getKey()+":"+elem.getValue());
			Reporter.log("--------header--------");
		}
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
