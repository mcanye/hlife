package hlife.base;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import hlife.filtration.URLFiltration;
import hlife.httpclient.HttpClient;
import hlife.parameters.VerificationParameters;
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
	protected String userIdA;
	protected String userIdB;

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
	public String advList;
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
	//用户主页信息
	protected String friendHome;
	//粉丝关注列表
	protected String followList;
	//修改用户信息
	protected String myModifymyinfo;




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
	//引导送礼物弹窗
	protected String liveGiftPopInfo;
	//礼物列表
	protected String liveGiftList;
	//赠送礼物
	protected String liveGiftSend;
	//打赏榜
	protected String liveGiftUser;
	//充值页面
	protected String payCopperRechargePackage;
	//用户信息
	protected String liveUserInfo;
	//关注/取消关注
	protected String followIndex;
	//在线用户列表
	protected String liveOnlineUser;
	//用户离开直播间
	protected String liveQuit;
	//用户加入直播间
	protected String liveJoin;
	//主播开启聊天室
	protected String liveOpenRtcFunc;
	//观众申请连麦
	protected String chatroomApply;
	//主播查看申请连麦用户列表
	protected String chatroomApplyList;
	//查看邀请连麦列表
	protected String chatroomInviteList;
	//邀请连麦
	protected String chatroomInvite;
	//同意连麦
	protected String chatroomUserAgree;
	//管理连麦列表
	protected String chatroomManagement;
	//管理用户
	protected String chatroomManage;
	//全员静音，解除静音
	protected String chatroomMuteAll;
	//主播同意连麦申请
	protected String chatroomAgree;
	//关闭连麦权限
	protected String chatroomUserCloseMute;
	//关闭聊天室
	protected String liveCloseRtcFunc;
	//获取默认城市
	protected String houseDefaultCity;
	//直播商品列表
	protected String publicReDataList;
	//设置讲解
	protected String liveReShow;
	//取消讲解
	protected String liveReHide;
	//编辑推广信息
	protected String publicReDataEdit;
	//主播禁言用户
	protected String liveBan;
	//禁言用户列表
	protected String liveBanUser;
	//解除禁言
	protected String liveUnBan;
	//设置管理员
	protected String liveManageAdd;
	//管理员列表
	protected String liveManageUser;
	//撤销管理员
	protected String liveManageDel;
	//踢出直播间
	protected String liveBlock;
	//封禁用户列表
	protected String liveBlockUser;
	//取消封禁
	protected String liveUnBlock;
	//个人主页栏目统一接口
	protected String friendMenuInfo;
	//个人主页咨询接口
	protected String newsList;
	//个人主页侧边栏
	protected String myHome;
	//我的资产
	protected String payQueryActBalance;
	//会员攻略
	protected String distributeUpgradeStrategy;
	//会员信息
	protected String distributeMemberInfo;
	//我的粉丝团
	protected String chatGroupinfo;
	//我的联系人
	protected String myContacts;
	//收藏即信聊天
	protected String chatRecordcollect;
	//收藏列表
	protected String myfavorite;
	//删除收藏即信
	protected String chatRecordnocollect;
	//小视频列表
	protected String videoList;
	//统一收藏接口
	protected String publicsFavorite;
	//自修课列表
	protected String courseSelfStudyList;
	//自修课详情
	protected String courseSelfStudyDetail;
	//可报备楼盘
	protected String houseStoreShareList;
	//获取下级信息
	protected String houseSubMembers;
	//报备
	protected String houseReport;
	//房产报备我的客户列表
	protected String houseReportList;


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
		friendHome = PropertiesUtils.getConfigValue("friendHome");
		friendHome = URLFiltration.addHost_BASEURL_API(friendHome);
		followList = PropertiesUtils.getConfigValue("followList");
		followList = URLFiltration.addHost_BASEURL_API(followList);
		myModifymyinfo = PropertiesUtils.getConfigValue("myModifymyinfo");
		myModifymyinfo = URLFiltration.addHost_BASEURL_API(myModifymyinfo);
		friendMenuInfo = PropertiesUtils.getConfigValue("friendMenuInfo");
		friendMenuInfo = URLFiltration.addHost_BASEURL_API(friendMenuInfo);
		newsList = PropertiesUtils.getConfigValue("newsList");
		newsList = URLFiltration.addHost_BASEURL_API(newsList);
		myHome = PropertiesUtils.getConfigValue("myHome");
		myHome = URLFiltration.addHost_BASEURL_API(myHome);
		payQueryActBalance = PropertiesUtils.getConfigValue("payQueryActBalance");
		payQueryActBalance = URLFiltration.addHost_BASEURL_API(payQueryActBalance);
		distributeUpgradeStrategy = PropertiesUtils.getConfigValue("distributeUpgradeStrategy");
		distributeUpgradeStrategy = URLFiltration.addHost_BASEURL_API(distributeUpgradeStrategy);
		distributeMemberInfo = PropertiesUtils.getConfigValue("distributeMemberInfo");
		distributeMemberInfo = URLFiltration.addHost_BASEURL_API(distributeMemberInfo);
		chatGroupinfo = PropertiesUtils.getConfigValue("chatGroupinfo");
		chatGroupinfo = URLFiltration.addHost_BASEURL_API(chatGroupinfo);
		myContacts = PropertiesUtils.getConfigValue("myContacts");
		myContacts = URLFiltration.addHost_BASEURL_API(myContacts);
		chatRecordcollect = PropertiesUtils.getConfigValue("chatRecordcollect");
		chatRecordcollect = URLFiltration.addHost_BASEURL_API(chatRecordcollect);
		myfavorite = PropertiesUtils.getConfigValue("myfavorite");
		myfavorite = URLFiltration.addHost_BASEURL_API(myfavorite);
		chatRecordnocollect = PropertiesUtils.getConfigValue("chatRecordnocollect");
		chatRecordnocollect = URLFiltration.addHost_BASEURL_API(chatRecordnocollect);
		videoList = PropertiesUtils.getConfigValue("videoList");
		videoList = URLFiltration.addHost_BASEURL_API(videoList);
		publicsFavorite = PropertiesUtils.getConfigValue("publicsFavorite");
		publicsFavorite = URLFiltration.addHost_BASEURL_API(publicsFavorite);
		courseSelfStudyList = PropertiesUtils.getConfigValue("courseSelfStudyList");
		courseSelfStudyList = URLFiltration.addHost_BASEURL_API(courseSelfStudyList);
		courseSelfStudyDetail = PropertiesUtils.getConfigValue("courseSelfStudyDetail");
		courseSelfStudyDetail = URLFiltration.addHost_BASEURL_API(courseSelfStudyDetail);
		houseStoreShareList = PropertiesUtils.getConfigValue("houseStoreShareList");
		houseStoreShareList = URLFiltration.addHost_BASEURL_API(houseStoreShareList);
		houseSubMembers = PropertiesUtils.getConfigValue("houseSubMembers");
		houseSubMembers = URLFiltration.addHost_BASEURL_API(houseSubMembers);
		houseReport = PropertiesUtils.getConfigValue("houseReport");
		houseReport = URLFiltration.addHost_BASEURL_API(houseReport);
		houseReportList = PropertiesUtils.getConfigValue("houseReportList");
		houseReportList = URLFiltration.addHost_BASEURL_API(houseReportList);



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
		liveGiftPopInfo = PropertiesUtils.getConfigValue("liveGiftPopInfo");
		liveGiftPopInfo = URLFiltration.addHost_BASEURL_API(liveGiftPopInfo);
		liveGiftList = PropertiesUtils.getConfigValue("liveGiftList");
		liveGiftList = URLFiltration.addHost_BASEURL_API(liveGiftList);
		liveGiftSend = PropertiesUtils.getConfigValue("liveGiftSend");
		liveGiftSend = URLFiltration.addHost_BASEURL_API(liveGiftSend);
		liveGiftUser = PropertiesUtils.getConfigValue("liveGiftUser");
		liveGiftUser = URLFiltration.addHost_BASEURL_API(liveGiftUser);
		payCopperRechargePackage = PropertiesUtils.getConfigValue("payCopperRechargePackage");
		payCopperRechargePackage = URLFiltration.addHost_BASEURL_API(payCopperRechargePackage);
		liveUserInfo = PropertiesUtils.getConfigValue("liveUserInfo");
		liveUserInfo = URLFiltration.addHost_BASEURL_API(liveUserInfo);
		followIndex = PropertiesUtils.getConfigValue("followIndex");
		followIndex = URLFiltration.addHost_BASEURL_API(followIndex);
		liveOnlineUser = PropertiesUtils.getConfigValue("liveOnlineUser");
		liveOnlineUser = URLFiltration.addHost_BASEURL_API(liveOnlineUser);
		liveQuit = PropertiesUtils.getConfigValue("liveQuit");
		liveQuit = URLFiltration.addHost_BASEURL_API(liveQuit);
		liveJoin = PropertiesUtils.getConfigValue("liveJoin");
		liveJoin = URLFiltration.addHost_BASEURL_API(liveJoin);
		liveOpenRtcFunc = PropertiesUtils.getConfigValue("liveOpenRtcFunc");
		liveOpenRtcFunc = URLFiltration.addHost_BASEURL_API(liveOpenRtcFunc);
		chatroomApply = PropertiesUtils.getConfigValue("chatroomApply");
		chatroomApply = URLFiltration.addHost_BASEURL_API(chatroomApply);
		chatroomApplyList = PropertiesUtils.getConfigValue("chatroomApplyList");
		chatroomApplyList = URLFiltration.addHost_BASEURL_API(chatroomApplyList);
		chatroomInviteList = PropertiesUtils.getConfigValue("chatroomInviteList");
		chatroomInviteList = URLFiltration.addHost_BASEURL_API(chatroomInviteList);
		chatroomInvite = PropertiesUtils.getConfigValue("chatroomInvite");
		chatroomInvite = URLFiltration.addHost_BASEURL_API(chatroomInvite);
		chatroomUserAgree = PropertiesUtils.getConfigValue("chatroomUserAgree");
		chatroomUserAgree = URLFiltration.addHost_BASEURL_API(chatroomUserAgree);
		chatroomManagement = PropertiesUtils.getConfigValue("chatroomManagement");
		chatroomManagement = URLFiltration.addHost_BASEURL_API(chatroomManagement);
		chatroomManage = PropertiesUtils.getConfigValue("chatroomManage");
		chatroomManage = URLFiltration.addHost_BASEURL_API(chatroomManage);
		chatroomMuteAll = PropertiesUtils.getConfigValue("chatroomMuteAll");
		chatroomMuteAll = URLFiltration.addHost_BASEURL_API(chatroomMuteAll);
		chatroomAgree = PropertiesUtils.getConfigValue("chatroomAgree");
		chatroomAgree = URLFiltration.addHost_BASEURL_API(chatroomAgree);
		chatroomUserCloseMute = PropertiesUtils.getConfigValue("chatroomUserCloseMute");
		chatroomUserCloseMute = URLFiltration.addHost_BASEURL_API(chatroomUserCloseMute);
		liveCloseRtcFunc = PropertiesUtils.getConfigValue("liveCloseRtcFunc");
		liveCloseRtcFunc = URLFiltration.addHost_BASEURL_API(liveCloseRtcFunc);
		houseDefaultCity = PropertiesUtils.getConfigValue("houseDefaultCity");
		houseDefaultCity = URLFiltration.addHost_BASEURL_API(houseDefaultCity);
		publicReDataList = PropertiesUtils.getConfigValue("publicReDataList");
		publicReDataList = URLFiltration.addHost_BASEURL_API(publicReDataList);
		liveReShow = PropertiesUtils.getConfigValue("liveReShow");
		liveReShow = URLFiltration.addHost_BASEURL_API(liveReShow);
		liveReHide = PropertiesUtils.getConfigValue("liveReHide");
		liveReHide = URLFiltration.addHost_BASEURL_API(liveReHide);
		publicReDataEdit = PropertiesUtils.getConfigValue("publicReDataEdit");
		publicReDataEdit = URLFiltration.addHost_BASEURL_API(publicReDataEdit);
		liveBan = PropertiesUtils.getConfigValue("liveBan");
		liveBan = URLFiltration.addHost_BASEURL_API(liveBan);
		liveBanUser = PropertiesUtils.getConfigValue("liveBanUser");
		liveBanUser = URLFiltration.addHost_BASEURL_API(liveBanUser);
		liveUnBan = PropertiesUtils.getConfigValue("liveUnBan");
		liveUnBan = URLFiltration.addHost_BASEURL_API(liveUnBan);
		liveManageAdd = PropertiesUtils.getConfigValue("liveManageAdd");
		liveManageAdd = URLFiltration.addHost_BASEURL_API(liveManageAdd);
		liveManageUser = PropertiesUtils.getConfigValue("liveManageUser");
		liveManageUser = URLFiltration.addHost_BASEURL_API(liveManageUser);
		liveManageDel = PropertiesUtils.getConfigValue("liveManageDel");
		liveManageDel = URLFiltration.addHost_BASEURL_API(liveManageDel);
		liveBlock = PropertiesUtils.getConfigValue("liveBlock");
		liveBlock = URLFiltration.addHost_BASEURL_API(liveBlock);
		liveBlockUser = PropertiesUtils.getConfigValue("liveBlockUser");
		liveBlockUser = URLFiltration.addHost_BASEURL_API(liveBlockUser);
		liveUnBlock = PropertiesUtils.getConfigValue("liveUnBlock");
		liveUnBlock = URLFiltration.addHost_BASEURL_API(liveUnBlock);


		access_token = fast(verificationParameters.phone);
		new_access_token = fast(verificationParameters.newPhone);
		userIdA = getUser_id(verificationParameters.phone);
		userIdB = getUser_id(verificationParameters.newPhone);
	}

	

	public String fast(String phone) throws IOException, InterruptedException {
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

	private String getUser_id(String phone) throws IOException, InterruptedException {
		HashMap<String, String> params = new HashMap<>();
		params.put("type","1");
		params.put("country_code","86");
		params.put("uuid","00000000-53a8-ae41-ffff-ffffef05ac4a");
		params.put("platform","taiji");
		params.put("username",phone);
		params.put("password","9999");

		HashMap<String, String> header = new HashMap<>();
		header = URLFiltration.addHeader(header);
		JSONObject rs = httpClient.getResponseJson(httpClient.post(verify,params,header));
		log.info(rs.toString());
		Reporter.log(rs.toString());
		//check登录成功msg与token是否成功返回
		Assert.assertEquals(rs.getIntValue("status"), Constants.RESPNSE_STATUS_CODE_1,"验证码登录接口访问失败");
		Assert.assertEquals(rs.getString("msg"), "登录成功","验证码登录接口访问失败");
		Thread.sleep(5000);
		return rs.getJSONObject("data").getString("id");
	}

	/**
	 * 获取日期
	 * @return 年-月-日 时:分
	 */
	public String getDate(long l){
		Date d = new Date(System.currentTimeMillis()+l);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String format = dateFormat.format(d);
		System.out.println(format);
		return format;
	}

	public void loading() throws InterruptedException {
		Thread.sleep(3000);
	}
}
