package hlife.test.integration;

import com.alibaba.fastjson.JSONObject;
import hlife.base.BaseApi;
import hlife.base.Constants;
import hlife.filtration.URLFiltration;
import hlife.httpclient.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;

/**
 * 个人主页相关
 */
@Test(groups = "MinePage")
public class MinePage extends BaseApi{

    private HttpClient httpClient = new HttpClient();
    private static Logger log = LoggerFactory.getLogger(MinePage.class);

    /**
     * 生活服务页
     */
    @Test
    public void myServiceHome() throws IOException {
        HashMap<String, String> header = URLFiltration.addHeader(new HashMap<>());
        JSONObject rs = httpClient.getResponseJson(httpClient.post(myServiceHome, new HashMap<>(), header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取用户信息成功","接口返回msg失败");
    }

}
