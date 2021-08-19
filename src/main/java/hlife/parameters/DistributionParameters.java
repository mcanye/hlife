package hlife.parameters;

import lombok.Data;
import hlife.utils.PropertiesUtils;

@Data
/**
 * 分销链参数
 * @author Mchi
 * @data 2019年9月12日15:36:00
 */
public class DistributionParameters {
//https://v3.tangcupaigu.hlife.net/hlife-user/v1
	///user/distribution/recordDistributionByhlifeId?
	//hlifeId=3104235&fromhlifeId=678532&
	//adownerId=3792&distributionId=4959&distributionType=8&versionCode=66&from=4
	private String hlifeId = PropertiesUtils.getParamValue("hlifeId");
	private String fromhlifeId = PropertiesUtils.getParamValue("fromhlifeId");
	//adownerId 商家id
	private String adownerId = PropertiesUtils.getParamValue("adownerId");
	//分销来源ID
	private String distributionId;
	private String distributionType = "8";
	
	//门店ID
	private String storeId;
}
