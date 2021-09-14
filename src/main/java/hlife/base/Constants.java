package hlife.base;

/**
 * 常量类
 * @author Mchi
 *
 */
public class Constants {
	//不要在代码里写死状态码，用常量写出来，方便每一个TestNG测试用例去调用去断言
	public static final int RESPNSE_STATUS_CODE_200 = 200;
	public static final int RESPNSE_STATUS_CODE_201 = 201;
	public static final int RESPNSE_STATUS_CODE_204 = 204;
	public static final int RESPNSE_STATUS_CODE_404 = 404;
	public static final int RESPNSE_STATUS_CODE_500 = 500;
	public static final int RESPNSE_STATUS_CODE_1 = 1;
	public static final int RESPNSE_STATUS_CODE_Minus_1 = -1;
	public static final int RESPNSE_STATUS_CODE_Minus_3 = -3;
	public static final int RESPNSE_STATUS_CODE_0= 0;

	//登录成功
	public static final int RESPNSE_STATUS_CODE_3501 = 3501;
	
	//url请求来源和版本号  必传
	public static final String FROM_VERSION_CODE = "from=2&versionCode=142";
	
}
