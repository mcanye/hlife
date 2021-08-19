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
import java.util.HashMap;

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
		if (!"".equals(url)) {
			if (url.contains("union") == true) {
				url = BASEURL_API + url ;
			}
		}
		return url;
	}

	/**
	 * 添加header
	 */
	public static HashMap<String,String> addHeader(HashMap<String,String> header){
		String apptype = PropertiesUtils.getEvnValue("apptype");
		header.put("apptype",apptype);
		return header;
	}
}
