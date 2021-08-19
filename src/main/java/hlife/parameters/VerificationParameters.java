package hlife.parameters;

import lombok.Data;
import hlife.utils.PropertiesUtils;

@Data
public class VerificationParameters {
	//加密key
	private String key = "69a0b4C41y99J4a38u9c5c335bfe2w";
	
	
	//------------登录-----------//
	//登录的手机号
	public String phone = PropertiesUtils.getParamValue("phone");
	private String merchantPhone = PropertiesUtils.getParamValue("merchantPhone");
	//验证码
	public String verificationCode = PropertiesUtils.getParamValue("verificationCode");
	//设备ID
	private String deviceId =  PropertiesUtils.getParamValue("deviceId");
	
	
	//------------搜索-----------//
	//登录的用户
	private Integer hlifeId ;
	//城市编码
	private String cityCode = "dl";
	//用户的经纬度
	private String longitude = "121.57713";
	private String latitude = "38.896286";
	private String orderType = "1";
	//搜索内容
	private String searchName = PropertiesUtils.getParamValue("exchangeName");
	private String searchNameNew = PropertiesUtils.getParamValue("exchangeNameNew");
	//分页标识
	private int pageNumber = 0;
	//门店ID
	private int storeId ;
	//代金券ID
	private int exchangeId ;
	//代金券售价
	private double price;
	
	
	//----------下预订单---------//
	//益划
	private String logincode;
	//商家版
	private String loginCode_;
	
	//用户益划币金额
	private double amount;
	//订单ID
	private Integer orderId;
	private String orderNum;

	//核销加密二维码串
	private String codeKey;
	
	//商家ID
	private Integer merchantUserId;
	
	
}
