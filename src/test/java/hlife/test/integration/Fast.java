package hlife.test.integration;

import com.alibaba.fastjson.JSONObject;

import hlife.base.BaseApi;
import hlife.base.Constants;
import hlife.filtration.URLFiltration;
import hlife.httpclient.HttpClient;
import hlife.parameters.VerificationParameters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;


/**
 * 登录相关
 */
@Test(groups = "Fast")
public class Fast extends BaseApi {
    private HttpClient httpClient = new HttpClient();
    private VerificationParameters verParameters = new VerificationParameters();
    private static Logger log = LoggerFactory.getLogger(Fast.class);
    private String avatar;
    private String access_token;

    /**
     * 验证码登录
     */
    @Test
    public void verifyFast() throws IOException,  InterruptedException {
        verify = URLFiltration.addHost(verify);
        //手机号正确，验证码正确
        HashMap<String, String> params = new HashMap<>();
        params.put("type","1");
        params.put("country_code","86");
        params.put("uuid","00000000-53a8-ae41-ffff-ffffef05ac4a");
        params.put("platform","taiji");
        params.put("username",verParameters.phone);
        params.put("password",verParameters.verificationCode);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(verify,params));
        log.info(rs.toString());
        Reporter.log(rs.toString());
        //check登录成功msg与token是否成功返回
        Assert.assertEquals(rs.getIntValue("status"), Constants.RESPNSE_STATUS_CODE_1,"验证码登录接口访问失败");
        Assert.assertEquals(rs.getString("msg"), "登录成功","验证码登录接口访问失败");

        JSONObject data = rs.getJSONObject("data");

        //判断头像avatar是否可以显示
        avatar= data.getString("avatar");
        if(avatar.equals("")||avatar == null){
            Assert.assertEquals(true, false,"头像不应为空");
        }
        Reporter.log("头像不为空，用例通过");

        int statusCode = httpClient.getStatusCode(httpClient.get(avatar, null));
        Assert.assertEquals(statusCode, Constants.RESPNSE_STATUS_CODE_200,"头像不正确");
        Reporter.log("头像可以成功访问，用例通过");
        //判断access_token不能为空
        access_token = data.getString("access_token");
        if (access_token.equals("")||access_token == null){
            Assert.assertEquals(true, false,"access_token不能为空");
        }

        Reporter.log("access_token不为空，用例通过");
        Thread.sleep(5000);

        //手机号正确，验证码不正确
        params.clear();
        params.put("username","17611111111");
        params.put("password","9988");
        params.put("type","1");
        rs = httpClient.getResponseJson(httpClient.post(verify,params));
        log.info(rs.toString());
        Reporter.log(rs.toString());
        Assert.assertEquals(rs.getIntValue("status"), Constants.RESPNSE_STATUS_CODE_0,"验证码登录接口访问失败");
        Assert.assertEquals(rs.getString("msg"), "验证码不正确","验证码登录接口访问失败");
        Reporter.log("手机号正确，验证码不正确，用例通过");
        Thread.sleep(5000);
        //手机号为空，验证码正确
        params.clear();
        params.put("username","");
        params.put("password","9999");
        params.put("type","1");
        rs = httpClient.getResponseJson(httpClient.post(verify,params));
        log.info(rs.toString());
        Reporter.log(rs.toString());
        Assert.assertEquals(rs.getIntValue("status"), Constants.RESPNSE_STATUS_CODE_0,"验证码登录接口访问失败");
        Assert.assertEquals(rs.getString("msg"), "手机号码不能为空","验证码登录接口访问失败");
        Reporter.log("手机号为空，验证码正确，用例通过");
        Thread.sleep(5000);
        //手机号正确，验证码为空
        params.clear();
        params.put("username","17611111111");
        params.put("password","");
        params.put("type","1");
        rs = httpClient.getResponseJson(httpClient.post(verify,params));
        log.info(rs.toString());
        Reporter.log(rs.toString());
        Assert.assertEquals(rs.getIntValue("status"), Constants.RESPNSE_STATUS_CODE_0,"验证码登录接口访问失败");
        Assert.assertEquals(rs.getString("msg"), "验证码不能为空","验证码登录接口访问失败");
        Reporter.log("手机号正确，验证码为空，用例通过");
        Thread.sleep(5000);
        //手机号不正确，验证码正确
        params.clear();
        params.put("username","176111");
        params.put("password","9999");
        params.put("type","1");
        rs = httpClient.getResponseJson(httpClient.post(verify,params));
        log.info(rs.toString());
        Reporter.log(rs.toString());
        Assert.assertEquals(rs.getIntValue("status"), Constants.RESPNSE_STATUS_CODE_0,"验证码登录接口访问失败");
        Assert.assertEquals(rs.getString("msg"), "手机号码有误","验证码登录接口访问失败");
        Reporter.log("手机号不正确，验证码正确，用例通过");
        Thread.sleep(5000);
    }

    /**
     * 密码登录
     */
    public void pwdFast() throws InterruptedException, IOException {
        verify = URLFiltration.addHost(verify);
        HashMap<String, String> params = new HashMap<>();
        params.put("type","3");
        params.put("username","17611111111");
        params.put("password","123456");
        JSONObject rs = httpClient.getResponseJson(httpClient.post(verify,params));
        log.info(rs.toString());
        Reporter.log(rs.toString());
        //check登录成功msg与token是否成功返回
        Assert.assertEquals(rs.getIntValue("status"), Constants.RESPNSE_STATUS_CODE_1,"验证码登录接口访问失败");
        Assert.assertEquals(rs.getString("msg"), "登录成功","验证码登录接口访问失败");

        JSONObject data = rs.getJSONObject("data");

        //判断头像avatar是否可以显示
        avatar= data.getString("avatar");
        if(avatar.equals("")||avatar == null){
            Assert.assertEquals(true, false,"头像不应为空");
        }
        Reporter.log("头像不为空，用例通过");

        int statusCode = httpClient.getStatusCode(httpClient.get(avatar, null));
        Assert.assertEquals(statusCode, Constants.RESPNSE_STATUS_CODE_200,"头像不正确");
        Reporter.log("头像可以成功访问，用例通过");
        //判断access_token不能为空
        access_token = data.getString("access_token");
        if (access_token.equals("")||access_token == null){
            Assert.assertEquals(true, false,"access_token不能为空");
        }

        Reporter.log("access_token不为空，用例通过");
        Thread.sleep(5000);

        //手机号错误，密码正确
        params.clear();
        params.put("type","3");
        params.put("username","17611");
        params.put("password","123456");
        rs = httpClient.getResponseJson(httpClient.post(verify,params));
        log.info(rs.toString());
        Reporter.log(rs.toString());
        Assert.assertEquals(rs.getIntValue("status"), 0,"手机号错误，密码正确，请求失败");
        Assert.assertEquals(rs.getString("msg"), "登陆失败，账号不存在！","手机号错误，密码正确，返回msg不正确");
        Thread.sleep(5000);

        //手机号为空，密码正确
        // TODO: 2021/8/10 待缺陷修复后 完善逻辑

        Reporter.log("执行手机号正确，密码错误用例");
        params.clear();
        params.put("type","3");
        params.put("username","17611111111");
        params.put("password","123457");
        rs = httpClient.getResponseJson(httpClient.post(verify,params));
        log.info(rs.toString());
        Reporter.log(rs.toString());
        Assert.assertEquals(rs.getIntValue("status"), 0,"手机号正确，密码错误，请求失败");
        Assert.assertEquals(rs.getString("msg"), "密码错误","手机号错误，密码正确，返回msg不正确");
        Thread.sleep(5000);

        Reporter.log("执行手机号正确，密码为空用例");
        params.clear();
        params.put("type","3");
        params.put("username","17611111111");
        params.put("password","");
        rs = httpClient.getResponseJson(httpClient.post(verify,params));
        log.info(rs.toString());
        Reporter.log(rs.toString());
        Assert.assertEquals(rs.getIntValue("status"), 0,"手机号正确，密码为空，请求失败");
        Assert.assertEquals(rs.getString("msg"), "密码不能为空","手机号正确，密码为空，返回msg不正确");
        Thread.sleep(5000);

        Reporter.log("执行手机号正确，密码错误用例");
        params.clear();
        params.put("type","3");
        params.put("username","17611111111");
        params.put("password","123");
        rs = httpClient.getResponseJson(httpClient.post(verify,params));
        log.info(rs.toString());
        Reporter.log(rs.toString());
        Assert.assertEquals(rs.getIntValue("status"), 0,"手机号正确，密码错误，请求失败");
        Assert.assertEquals(rs.getString("msg"), "密码只能为6到20位","手机号正确，密码错误，返回msg不正确");
        Thread.sleep(5000);


    }
}
