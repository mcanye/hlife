package hlife.base;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.ClientProtocolException;
import org.bouncycastle.crypto.agreement.srp.SRP6Client;
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
	protected String new_access_token;



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

	//礼品卡列表
	protected String coursecardList;
	//礼品卡详情
	protected String coursecardDetail;
	//课程卡合作单位
	protected String coursecardCooperation;
	//发票参数校验
	protected String cardorderVerifyInvoice;
	//发票抬头列表
	protected String invoicesListtitle;
	//创建发票抬头
	protected String invoicesCreatetitle;
	//删除发票抬头
	protected String invoicesDeletetitle;
	//我的卡包
	protected String cardorderList;
	//礼品卡下订单
	protected String cardorderCreateOrder;
	//取消订单
	protected String cardorderCancel;
	//删除订单
	protected String cardorderDelete;

	//个人主页相关
	//生活服务页
	protected String myServiceHome;


	//直播间相关
	//创建直播间
	protected String liveCreate;
	//获取直播间信息 加入直播间
	protected String liveDetail;
	//能否创建直播
	protected String liveAppCreate;
	//开播，关播，继续直播
	protected String liveOpera;
	//是否允许进入直播间
	protected String liveStatus;
	//粉丝团详情
	protected String fansGroupDetail;
	//修改粉丝团信息
	protected String fansGroupApply;
	//进入粉丝群
	protected String chatEnter;
	//获取聊天记录
	protected String chatRecordList;
	//粉丝群详情
	protected String chatEnterInfo;
	//退群
	protected String chatQuit;
	//加入群聊
	protected String charJoin;


	@BeforeClass
	public void setUp() throws  IOException, InterruptedException {

		verify = PropertiesUtils.getConfigValue("verify");
		verify = URLFiltration.addHost_BASEURL_API(verify);
		city = PropertiesUtils.getConfigValue("city");
		city = URLFiltration.addHost_BASEURL_API(city);
		house_list = PropertiesUtils.getConfigValue("house_list");
		house_list = URLFiltration.addHost_BASEURL_API(house_list);
		tagList = PropertiesUtils.getConfigValue("tagList");
		tagList = URLFiltration.addHost_BASEURL_API(tagList);
		brandList = PropertiesUtils.getConfigValue("brandList");
		brandList = URLFiltration.addHost_BASEURL_API(brandList);
		liveRecommend = PropertiesUtils.getConfigValue("liveRecommend");
		liveRecommend = URLFiltration.addHost_BASEURL_API(liveRecommend);
		liveCategoryList = PropertiesUtils.getConfigValue("liveCategoryList");
		liveCategoryList = URLFiltration.addHost_BASEURL_API(liveCategoryList);
		advList = PropertiesUtils.getConfigValue("advList");
		advList = URLFiltration.addHost_BASEURL_API(advList);
		liveRecommendlist = PropertiesUtils.getConfigValue("liveRecommendlist");
		liveRecommendlist = URLFiltration.addHost_BASEURL_API(liveRecommendlist);
		liveCategorySort = PropertiesUtils.getConfigValue("liveCategorySort");
		liveCategorySort = URLFiltration.addHost_BASEURL_API(liveCategorySort);
		liveFocuslist = PropertiesUtils.getConfigValue("liveFocuslist");
		liveFocuslist = URLFiltration.addHost_BASEURL_API(liveFocuslist);
		liveList = PropertiesUtils.getConfigValue("liveList");
		liveList = URLFiltration.addHost_BASEURL_API(liveList);
		createLiveNotice = PropertiesUtils.getConfigValue("createLiveNotice");
		createLiveNotice = URLFiltration.addHost_BASEURL_API(createLiveNotice);
		liveNoticeDet = PropertiesUtils.getConfigValue("liveNoticeDet");
		liveNoticeDet = URLFiltration.addHost_BASEURL_API(liveNoticeDet);
		setLiveNoticeRecommend = PropertiesUtils.getConfigValue("setLiveNoticeRecommend");
		setLiveNoticeRecommend = URLFiltration.addHost_BASEURL_4001(setLiveNoticeRecommend);
		liveNoticeRecommendList = PropertiesUtils.getConfigValue("liveNoticeRecommendList");
		liveNoticeRecommendList = URLFiltration.addHost_BASEURL_API(liveNoticeRecommendList);
		liveNoticeFocuslist = PropertiesUtils.getConfigValue("liveNoticeFocuslist");
		liveNoticeFocuslist = URLFiltration.addHost_BASEURL_API(liveNoticeFocuslist);
		liveNoticeList = PropertiesUtils.getConfigValue("liveNoticeList");
		liveNoticeList = URLFiltration.addHost_BASEURL_API(liveNoticeList);
		liveNoticeEnroll = PropertiesUtils.getConfigValue("liveNoticeEnroll");
		liveNoticeEnroll = URLFiltration.addHost_BASEURL_API(liveNoticeEnroll);
		liveNoticeCancel = PropertiesUtils.getConfigValue("liveNoticeCancel");
		liveNoticeCancel = URLFiltration.addHost_BASEURL_API(liveNoticeCancel);
		coursecardList = PropertiesUtils.getConfigValue("coursecardList");
		coursecardList = URLFiltration.addHost_BASEURL_API(coursecardList);
		coursecardDetail = PropertiesUtils.getConfigValue("coursecardDetail");
		coursecardDetail = URLFiltration.addHost_BASEURL_API(coursecardDetail);
		coursecardCooperation = PropertiesUtils.getConfigValue("coursecardCooperation");
		coursecardCooperation = URLFiltration.addHost_BASEURL_API(coursecardCooperation);
		cardorderVerifyInvoice = PropertiesUtils.getConfigValue("cardorderVerifyInvoice");
		cardorderVerifyInvoice = URLFiltration.addHost_BASEURL_API(cardorderVerifyInvoice);
		invoicesListtitle = PropertiesUtils.getConfigValue("invoicesListtitle");
		invoicesListtitle = URLFiltration.addHost_BASEURL_PASSPORT(invoicesListtitle);
		invoicesCreatetitle = PropertiesUtils.getConfigValue("invoicesCreatetitle");
		invoicesCreatetitle = URLFiltration.addHost_BASEURL_PASSPORT(invoicesCreatetitle);
		invoicesDeletetitle = PropertiesUtils.getConfigValue("invoicesDeletetitle");
		invoicesDeletetitle = URLFiltration.addHost_BASEURL_PASSPORT(invoicesDeletetitle);
		cardorderList = PropertiesUtils.getConfigValue("cardorderList");
		cardorderList = URLFiltration.addHost_BASEURL_API(cardorderList);
		cardorderCreateOrder = PropertiesUtils.getConfigValue("cardorderCreateOrder");
		cardorderCreateOrder = URLFiltration.addHost_BASEURL_API(cardorderCreateOrder);
		cardorderCancel = PropertiesUtils.getConfigValue("cardorderCancel");
		cardorderCancel = URLFiltration.addHost_BASEURL_API(cardorderCancel);
		cardorderDelete = PropertiesUtils.getConfigValue("cardorderDelete");
		cardorderDelete = URLFiltration.addHost_BASEURL_API(cardorderDelete);
		myServiceHome = PropertiesUtils.getConfigValue("myServiceHome");
		myServiceHome = URLFiltration.addHost_BASEURL_API(myServiceHome);
		liveCreate = PropertiesUtils.getConfigValue("liveCreate");
		liveCreate = URLFiltration.addHost_BASEURL_API(liveCreate);
		liveDetail = PropertiesUtils.getConfigValue("liveDetail");
		liveDetail = URLFiltration.addHost_BASEURL_API(liveDetail);
		liveAppCreate = PropertiesUtils.getConfigValue("liveAppCreate");
		liveAppCreate = URLFiltration.addHost_BASEURL_API(liveAppCreate);
		liveOpera = PropertiesUtils.getConfigValue("liveOpera");
		liveOpera = URLFiltration.addHost_BASEURL_API(liveOpera);
		liveStatus = PropertiesUtils.getConfigValue("liveStatus");
		liveStatus = URLFiltration.addHost_BASEURL_API(liveStatus);
		fansGroupDetail = PropertiesUtils.getConfigValue("fansGroupDetail");
		fansGroupDetail = URLFiltration.addHost_BASEURL_API(fansGroupDetail);
		fansGroupApply = PropertiesUtils.getConfigValue("fansGroupApply");
		fansGroupApply = URLFiltration.addHost_BASEURL_API(fansGroupApply);
		chatEnter = PropertiesUtils.getConfigValue("chatEnter");
		chatEnter = URLFiltration.addHost_BASEURL_PASSPORT(chatEnter);
		chatRecordList = PropertiesUtils.getConfigValue("chatRecordList");
		chatRecordList = URLFiltration.addHost_BASEURL_PASSPORT(chatRecordList);
		chatEnterInfo = PropertiesUtils.getConfigValue("chatEnterInfo");
		chatEnterInfo = URLFiltration.addHost_BASEURL_PASSPORT(chatEnterInfo);
		chatQuit = PropertiesUtils.getConfigValue("chatQuit");
		chatQuit = URLFiltration.addHost_BASEURL_PASSPORT(chatQuit);
		charJoin = PropertiesUtils.getConfigValue("charJoin");
		charJoin = URLFiltration.addHost_BASEURL_PASSPORT(charJoin);
		access_token = fast(verificationParameters.phone);
		new_access_token = fast(verificationParameters.newPhone);

	}

	

	protected String fast(String phone) throws IOException, InterruptedException {
		HashMap<String, String> params = new HashMap<>();
		params.put("type","1");
		params.put("country_code","86");
		params.put("uuid","00000000-53a8-ae41-ffff-ffffef05ac4a");
		params.put("platform","taiji");
		params.put("username",phone);
		params.put("password",verificationParameters.verificationCode);

		HashMap<String, String> header = new HashMap<>();
		header = URLFiltration.addHeader(header);
		JSONObject rs = httpClient.getResponseJson(httpClient.post(verify,params,header));
		log.info(rs.toString());
		Reporter.log(rs.toString());
		//check登录成功msg与token是否成功返回
		Assert.assertEquals(rs.getIntValue("status"), Constants.RESPNSE_STATUS_CODE_1,"验证码登录接口访问失败");
		Assert.assertEquals(rs.getString("msg"), "登录成功","验证码登录接口访问失败");
		Thread.sleep(5000);
		return rs.getJSONObject("data").getString("access_token");

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
