package hlife.utils.md5;

import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 加密方法
 * @author Mchi
 *
 */
public class EncryptUtil {
	static AES aes = new AES();
	public  static long nowSecs;
	
	/**
	 * 小程序加密方法
	 * 
	 * @param param
	 * @return
	 */
	public static String getEncryptKey(String param) {
		
		nowSecs= Calendar.getInstance().getTimeInMillis();
		byte[] b = (nowSecs+param).getBytes();
		String key = aes.encrypt(b);
		
		return key;
		
	}
	
	/**
	 * IOS加密
	 * 时间戳+终端类型+手机号+逗号+请求路径+后台versionCode
	 * @param param
	 * @return
	 */
	public static String getEncryptKey(String phone,String url,String versionCode) {
		
		nowSecs= Calendar.getInstance().getTimeInMillis();
		String prototype = nowSecs + "2" + phone +"," + url + versionCode;
		byte[] b = prototype.getBytes();
		String key = aes.encrypt(b);
		
		return key;
		
	}
	
	public static String getDecryptKey(String key) {
		
		return aes.decrypt(key);
		
	}
	
	
	@Test
	public void test111() throws UnsupportedEncodingException {
		long i =1 ;
		System.out.println((int)(i*10*0.3));
	}

	/**
	 * Unicode转中文
	 * @param str 中文
	 * @return
	 */
	public static String unicodeToString(String str) {

		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(str);
		char ch;
		while (matcher.find()) {
			ch = (char) Integer.parseInt(matcher.group(2), 16);
			str = str.replace(matcher.group(1), ch + "");
		}
		return str;
	}
}
