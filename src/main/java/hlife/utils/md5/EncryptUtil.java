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
		//String s  = "GluZWNsYXNzLmtMTBPSDRmQT0=VodWFhcHAuY29tL2VfYWRWaWRlbzE1Njc0OTIxMjk4ODgubXA0P2U9MTU3MTMwMDE3NSZ0b2tlbj0tNlZlb1VEc2MyVFRJdEpQMjZFR2dBWjVsNHY2YzJkN1VvRWhzbWVEOnZBckZMRkxMVnFha3ZTNHYycUQaHR0cDovL29ub";
	//	String s = "7,8,5";
		//String decrypt = EncryptUtil.getDecryptKey(s);
//		byte[] b = s.getBytes();
//		String encrypt = aes.encrypt(b);
//		String urlString = URLEncoder.encode("7,8,5", "utf-8");
//		System.out.println(urlString);
//		long l = System.currentTimeMillis();
//		l = l+7200000;
//		Date d = new Date(l);
//		SimpleDateFormat sbf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		System.out.println(sbf.format(d));
		String s = "\\u8bbe\\u4e3a\\u63a8\\u8350\\u6210\\u529f";
		String s1 = unicodeToString(s);
		System.out.println(s1);
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
