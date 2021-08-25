package hlife.parameters;

import lombok.Data;
import hlife.utils.PropertiesUtils;

@Data
public class VerificationParameters {

	
	
	//------------登录-----------//
	//登录的手机号
	public String phone = PropertiesUtils.getParamValue("phone");
	public String newPhone = PropertiesUtils.getParamValue("newPhone");
	//验证码
	public String verificationCode = PropertiesUtils.getParamValue("verificationCode");


	
	
}
