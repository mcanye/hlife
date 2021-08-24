package hlife.base;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import hlife.filtration.URLFiltration;
import hlife.httpclient.HttpClient;
import hlife.parameters.VerificationParameters;
import hlife.utils.Arith;
import hlife.utils.PropertiesUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


/**
 * 	该类可以当成所有测试类的模板基类，其他需要测试的类继承该类
 * session,token等需要全局使用的均需要在此类中进行定义；若测试需要登录可在本类进行登录
 * 
 * @author Mchi
 *
 */
public class BaseApi {
	private HttpClient httpClient = new HttpClient();
	private static Logger log = LoggerFactory.getLogger(BaseApi.class);
	private VerificationParameters verificationParameters = new VerificationParameters();
	//用户登录token
	protected String access_token;

	//登录接口
	protected String verify;
	//获取全部城市接口
	protected String city;
	//房产列表
	protected String house_list;
	//房产标签接口
	protected String tagList;
	//房产品牌接口
	protected String brandList;

	//判断是否有正在直播的直播间
	protected String liveRecommend;
	//获取直播分类（根据apptype不同返回值不同，分类由后台管理端配置）
	protected String liveCategoryList;
	//广告列表
	protected String advList;
	//获取直播推荐列表
	protected String liveRecommendlist;
	//更改直播排序
	protected String liveCategorySort;
	//直播关注列表接口
	protected String liveFocuslist;
	//直播列表
	protected String liveList;

	//发布预告
	protected String createLiveNotice;
	//预告详情
	protected String liveNoticeDet;
	//推荐预告
	protected String setLiveNoticeRecommend;
	//获取推荐直播预告列表
	protected String liveNoticeRecommendList;
	//直播预告关注列表
	protected String liveNoticeFocuslist;
	//直播预告列表根据live_cid区分类型
	protected String liveNoticeList;
	//预告预约
	protected String liveNoticeEnroll;
	//删除直播预告
	protected String liveNoticeCancel;

	@BeforeClass
	public void setUp() throws  IOException, InterruptedException {

		verify = PropertiesUtils.getConfigValue("verify");
		city = PropertiesUtils.getConfigValue("city");
		house_list = PropertiesUtils.getConfigValue("house_list");
		tagList = PropertiesUtils.getConfigValue("tagList");
		brandList = PropertiesUtils.getConfigValue("brandList");
		liveRecommend = PropertiesUtils.getConfigValue("liveRecommend");
		liveCategoryList = PropertiesUtils.getConfigValue("liveCategoryList");
		advList = PropertiesUtils.getConfigValue("advList");
		liveRecommendlist = PropertiesUtils.getConfigValue("liveRecommendlist");
		liveCategorySort = PropertiesUtils.getConfigValue("liveCategorySort");
		liveFocuslist = PropertiesUtils.getConfigValue("liveFocuslist");
		liveList = PropertiesUtils.getConfigValue("liveList");
		createLiveNotice = PropertiesUtils.getConfigValue("createLiveNotice");
		liveNoticeDet = PropertiesUtils.getConfigValue("liveNoticeDet");
		setLiveNoticeRecommend = PropertiesUtils.getConfigValue("setLiveNoticeRecommend");
		liveNoticeRecommendList = PropertiesUtils.getConfigValue("liveNoticeRecommendList");
		liveNoticeFocuslist = PropertiesUtils.getConfigValue("liveNoticeFocuslist");
		liveNoticeList = PropertiesUtils.getConfigValue("liveNoticeList");
		liveNoticeEnroll = PropertiesUtils.getConfigValue("liveNoticeEnroll");
		liveNoticeCancel = PropertiesUtils.getConfigValue("liveNoticeCancel");
		fast();
	}

	

	private void fast() throws IOException, InterruptedException {
		HashMap<String, String> params = new HashMap<>();
		params.put("type","1");
		params.put("country_code","86");
		params.put("uuid","00000000-53a8-ae41-ffff-ffffef05ac4a");
		params.put("platform","taiji");
		params.put("username",verificationParameters.phone);
		params.put("password",verificationParameters.verificationCode);
		verify = URLFiltration.addHost(verify);
		HashMap<String, String> header = new HashMap<>();
		header = URLFiltration.addHeader(header);
		JSONObject rs = httpClient.getResponseJson(httpClient.post(verify,params,header));
		log.info(rs.toString());
		Reporter.log(rs.toString());
		//check登录成功msg与token是否成功返回
		Assert.assertEquals(rs.getIntValue("status"), Constants.RESPNSE_STATUS_CODE_1,"验证码登录接口访问失败");
		Assert.assertEquals(rs.getString("msg"), "登录成功","验证码登录接口访问失败");

		JSONObject data = rs.getJSONObject("data");
		access_token = data.getString("access_token");
		Thread.sleep(5000);
	}

	/**
	 * 获取日期
	 * @return 年-月-日 时:分
	 */
	protected String getDate(long l){
		Date d = new Date(System.currentTimeMillis()+l);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String format = dateFormat.format(d);
		System.out.println(format);
		return format;
	}

	
}
