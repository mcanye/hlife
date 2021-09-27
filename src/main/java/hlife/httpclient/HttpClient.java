package hlife.httpclient;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Reporter;
import hlife.utils.PropertiesUtils;
import hlife.utils.fastjson.FastjsonUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 实现HTTP的get,post,put,delete请求方法
 * 
 * @author Mchi
 *
 */
public class HttpClient {
	private static Logger log = LoggerFactory.getLogger(PropertiesUtils.class);
	/**
	 * 发送get请求，url为必填项，其他若无则填写null
	 * 
	 * @param url     请求的路径
	 * @param params  请求的参数
	 * @param headers 请求头
	 * @param cookie  请求cookie
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public CloseableHttpResponse get(String url, HashMap<String, String> params, HashMap<String, String> headers,
			CookieStore cookie) throws ClientProtocolException, IOException {
		// 创建一个可关闭的HttpClient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 添加cookie信息，如果没有需填写null
		if (cookie != null) {
			httpclient = HttpClients.custom().setDefaultCookieStore(cookie).build();
		}
		// 创建一个HttpGet的请求对象
		if (params != null && params.size() > 0) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				url = url + "&" + entry.getKey() + "=" + entry.getValue();
			}
		}
		HttpGet httpget = new HttpGet(url);
		// 加载请求头到httpget对象
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpget.addHeader(entry.getKey(), entry.getValue());
			}
		}
		Reporter.log(url);
		log.info(url);
		// 执行请求,相当于postman上点击发送按钮，然后赋值给HttpResponse对象接收
		CloseableHttpResponse httpResponse = httpclient.execute(httpget);
		return httpResponse;
	}

	/**
	 * 仅有参数的get请求
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public CloseableHttpResponse get(String url, HashMap<String, String> params)
			throws ClientProtocolException, IOException {
		return get(url, params, null, null);
	}

	/**
	 * 带参数，带header的get请求
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public CloseableHttpResponse get(String url, HashMap<String, String> params, HashMap<String, String> headers)
			throws ClientProtocolException, IOException {
		return get(url, params, headers, null);
	}

	/**
	 * 带参数,带cookie的get请求
	 * 
	 * @param url
	 * @param params
	 * @param cookie
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public CloseableHttpResponse get(String url, HashMap<String, String> params, CookieStore cookie)
			throws ClientProtocolException, IOException {
		return get(url, params, null, cookie);
	}

	/**
	 * 发送post请求，url为必填项，其他若无则填null
	 * 
	 * @param url     请求url
	 * @param json    若请求参数为json则填写，若无填写""
	 * @param params  请求参数
	 * @param headers 请求header，用于区分请求方式，与postman相同，填写null，默认为application/x-www-form-urlencoded
	 * @param cookie  若有cookie则填写，若无填null
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public CloseableHttpResponse post(String url, String json, HashMap<String, String> params,
			HashMap<String, String> headers, CookieStore cookie) throws ClientProtocolException, IOException {
		// 创建一个可关闭的HttpClient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url);
		// 添加cookie信息，如果没有需填写null
		if (cookie != null) {
			httpclient = HttpClients.custom().setDefaultCookieStore(cookie).build();
		}
		// 创建一个HttpPost的请求对象
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//		if (params != null && params.size() > 0) {
//			for (Map.Entry<String, String> entry : params.entrySet()) {
//				//url = url +"&"+ entry.getKey() + "=" + entry.getValue();
//				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,"UTF-8");
//				httppost.setEntity(entity);
//			}
//		}
		//设置参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		Iterator iterator = params.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<String,String> elem = (Map.Entry<String, String>) iterator.next();
			list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
			System.out.println(elem.getKey()+":"+elem.getValue());
			Reporter.log(elem.getKey()+":"+elem.getValue());
		}
		if(list.size() > 0) {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			httppost.setEntity(entity);
		}

			// 构造请求体，创建参数队列
			if (json != null && !"".equals(json)) {
				httppost.setEntity(new StringEntity(json));
			}

			// 加载请求头到httppost对象
			if (headers != null && headers.size() > 0) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					httppost.addHeader(entry.getKey(), entry.getValue());
					log.info(entry.getKey()+":"+entry.getValue());
					Reporter.log(entry.getKey()+":"+entry.getValue());
				}
			}
			Reporter.log(url);
			log.info(url);
			// 发送post请求
			CloseableHttpResponse httpResponse = httpclient.execute(httppost);
			return httpResponse;
		}

	/**
	 * 默认状态的post请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public CloseableHttpResponse post(String url, HashMap<String, String> params)
			throws ClientProtocolException, IOException {
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/x-www-form-urlencoded");// postman中有写
		return post(url, "", params, headers, null);
	}
	
	public CloseableHttpResponse post(String url, HashMap<String, String> params,HashMap<String, String> headers)
			throws ClientProtocolException, IOException {
		return post(url, "", params, headers, null);
	}

	/**
	 * json方式发送post
	 * 
	 * @param url
	 * @param json
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public CloseableHttpResponse post(String url, String json) throws ClientProtocolException, IOException {
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");// postman中有写
		return post(url, json, null, headers, null);
	}

	/**
	 * 发送put请求，不常用
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public  CloseableHttpResponse put(String url, Map<String, String> params, Map<String, String> headers)
			throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPut httpput = new HttpPut(url);
		// 构造请求体，创建参数队列
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (params != null && params.size() > 0) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		httpput.setEntity(new UrlEncodedFormEntity(nvps));

		// 加载请求头到httpput对象
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpput.addHeader(entry.getKey(), entry.getValue());
			}
		}
		log.info(url);
		// 发送put请求
		CloseableHttpResponse httpResponse = httpclient.execute(httpput);
		return httpResponse;
	}

	/**
	 * 发送delete请求，几乎不用
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public  CloseableHttpResponse delete(String url) throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpDelete httpdel = new HttpDelete(url);

		// 发送delete请求
		log.info(url);
		CloseableHttpResponse httpResponse = httpclient.execute(httpdel);
		return httpResponse;
	}

	/**
	 * 	获取响应状态码，常用来和TestBase中定义的状态码常量去测试断言使用
	 * 
	 * @param response
	 * @return 返回int类型状态码
	 */
	public  int getStatusCode(CloseableHttpResponse response) {
		int statusCode = response.getStatusLine().getStatusCode();
		return statusCode;
	}
	
	/**
	 * 把字符串转换为JSON对象，get取出【JSONObject.get("name")】
	 * 
	 * @param response, 任何请求返回返回的响应对象 @return， 返回响应体的json格式对象，方便接下来对JSON对象内容解析
	 *        接下来，一般会继续调用TestUtil类下的json解析方法得到某一个json对象的值
	 * @throws ParseException
	 * @throws IOException
	 */
	public  JSONObject getResponseJson(CloseableHttpResponse response){
		
		JSONObject res;
		try {
			res = FastjsonUtils.toJsonObject(EntityUtils.toString(response.getEntity()));
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}


    public <K, V> void getResponseJson(String myServiceHome, HashMap<K, V> kvHashMap, HashMap<String, String> header) {
    }
}
