package hlife.test.integration;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.xml.internal.bind.v2.TODO;
import hlife.base.BaseApi;
import hlife.base.Constants;
import hlife.filtration.URLFiltration;
import hlife.httpclient.HttpClient;
import hlife.parameters.VerificationParameters;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;

/**
 * 直播预告相关
 */
@Test(groups = "LiveNoticeList")
public class LiveNoticeList extends BaseApi{
    private HttpClient httpClient = new HttpClient();
    private static Logger log = LoggerFactory.getLogger(LiveNoticeList.class);
    //预告ID
    private String notice_id;
    private String date = getDate(7200000);

    /**
     * 发布直播预告
     */
    @Test
    public void createLiveNotice() throws IOException {
        createLiveNotice = URLFiltration.addHost(createLiveNotice);
        Reporter.log("判断token为空");
        HashMap<String,String> params = new HashMap<>();
        CloseableHttpResponse post = httpClient.post(createLiveNotice, params);
        JSONObject rs = httpClient.getResponseJson(post);
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        String msg = rs.getString("msg");
        int status = rs.getIntValue("status");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_Minus_1,"发布预告接口请求失败");
        Assert.assertEquals(msg,"请登录","发布预告接口msg不正确");

        Reporter.log("判断直播封面不能为空");
        params.clear();
        params.put("access_token",access_token);
        post = httpClient.post(createLiveNotice, params);
        rs = httpClient.getResponseJson(post);
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        msg = rs.getString("msg");
        status = rs.getIntValue("status");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_0,"发布预告接口请求失败");
        Assert.assertEquals(msg,"直播封面不能为空","发布预告接口msg不正确");

        Reporter.log("判断直播封面类型不正确");
        params.clear();
        params.put("access_token",access_token);
        params.put("live_cover","jpg");
        post = httpClient.post(createLiveNotice, params);
        rs = httpClient.getResponseJson(post);
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        msg = rs.getString("msg");
        status = rs.getIntValue("status");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_0,"发布预告接口请求失败");
        Assert.assertEquals(msg,"直播封面类型错误","发布预告接口msg不正确");

        Reporter.log("判断直播类型ID不能为空");
        params.clear();
        params.put("access_token",access_token);
        params.put("live_cover","taiji/20210823/1629685787500825.jpg");
        post = httpClient.post(createLiveNotice, params);
        rs = httpClient.getResponseJson(post);
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        msg = rs.getString("msg");
        status = rs.getIntValue("status");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_0,"发布预告接口请求失败");
        Assert.assertEquals(msg,"直播类型id不能为空","发布预告接口msg不正确");

        Reporter.log("判断直播标题不能为空");
        params.clear();
        params.put("access_token",access_token);
        params.put("live_cover","taiji/20210823/1629685787500825.jpg");
        params.put("live_cid","5");
        post = httpClient.post(createLiveNotice, params);
        rs = httpClient.getResponseJson(post);
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        msg = rs.getString("msg");
        status = rs.getIntValue("status");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_0,"发布预告接口请求失败");
        Assert.assertEquals(msg,"直播标题不能为空","发布预告接口msg不正确");

        Reporter.log("判断预告简介不能为空");
        params.clear();
        params.put("access_token",access_token);
        params.put("live_cover","taiji/20210823/1629685787500825.jpg");
        params.put("live_cid","5");
        params.put("live_title","哈哈");
        params.put("live_start_time",getDate(7200000 ));
        post = httpClient.post(createLiveNotice, params);
        rs = httpClient.getResponseJson(post);
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        msg = rs.getString("msg");
        status = rs.getIntValue("status");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_0,"发布预告接口请求失败");
        Assert.assertEquals(msg,"预告简介不能为空","发布预告接口msg不正确");

        Reporter.log("判断直播开始时间不能为空");
        params.clear();
        params.put("access_token",access_token);
        params.put("live_cover","taiji/20210823/1629685787500825.jpg");
        params.put("live_cid","5");
        params.put("live_title","这是预告标题");
        params.put("introduce","这是预告简介");
        post = httpClient.post(createLiveNotice, params);
        rs = httpClient.getResponseJson(post);
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        msg = rs.getString("msg");
        status = rs.getIntValue("status");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_0,"发布预告接口请求失败");
        Assert.assertEquals(msg,"直播开始时间不能为空","发布预告接口msg不正确");

        Reporter.log("判断宣传图片或视频不为空");
        params.clear();
        params.put("live_start_time",date);
        params.put("live_cover","taiji/20210823/1629685787500825.jpg");
        params.put("live_title","这是预告标题");
        params.put("introduce","这是预告简介");
        params.put("access_token",access_token);
        params.put("live_cid","5");
        post = httpClient.post(createLiveNotice, params);
        rs = httpClient.getResponseJson(post);
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        msg = rs.getString("msg");
        status = rs.getIntValue("status");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_0,"发布预告接口请求失败");
        Assert.assertEquals(msg,"必须上传一张宣传图片或者视频","发布预告接口msg不正确");

        Reporter.log("参数正确，判断code=200\n" +
                "status=1\n" +
                "msg=发布成功");
        params.clear();
        params.put("live_start_time",getDate(7200000 ));
        params.put("live_cover","taiji/20210823/1629685787500825.jpg");
        params.put("live_title","这是预告标题");
        params.put("introduce","这是预告简介");
        params.put("access_token",access_token);
        params.put("live_cid","5");
        params.put("images","[\"taiji\\/20210823\\/1629685787500825.jpg\"]");
        post = httpClient.post(createLiveNotice, params);
        rs = httpClient.getResponseJson(post);
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        msg = rs.getString("msg");
        status = rs.getIntValue("status");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_1,"发布预告接口请求失败");
        Assert.assertEquals(msg,"发布成功","发布预告接口msg不正确");

        JSONObject data = rs.getJSONObject("data");
        String mobile_url = data.getString("mobile_url");
        //截取？后面字符串
        notice_id = mobile_url.substring(mobile_url.indexOf("=") + 1, mobile_url.indexOf("&"));
        Reporter.log("预告ID："+notice_id);
        log.info("预告ID："+notice_id);

    }

    /**
     * 预告详情
     */
    @Test(dependsOnMethods = { "createLiveNotice" })
    public void liveNoticeDet() throws IOException {
        liveNoticeDet = URLFiltration.addHost(liveNoticeDet);
        Reporter.log("判断预告ID不能为空");
        HashMap<String,String> params = new HashMap<>();
        CloseableHttpResponse post = httpClient.post(liveNoticeDet, params);
        JSONObject rs = httpClient.getResponseJson(post);
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        String msg = rs.getString("msg");
        int status = rs.getIntValue("status");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_0,"预告详情接口请求失败");
        Assert.assertEquals(msg,"预告ID不能为空","预告详情接口msg不正确");

        Reporter.log("参数正确，判断code=200\n" +
                "status=1\n" +
                "msg=\"获取预告详情成功\"");
        params.clear();
        params.put("notice_id",notice_id);
        post = httpClient.post(liveNoticeDet, params);
        rs = httpClient.getResponseJson(post);
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        msg = rs.getString("msg");
        status = rs.getIntValue("status");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_1,"预告详情接口请求失败");
        //TODO 待13413缺陷修复后解除注释
        //Assert.assertEquals(msg,"获取预告详情成功","预告详情接口msg不正确");
        JSONObject data = rs.getJSONObject("data");

        Reporter.log("判断预告标题不为空");
        String live_title = data.getString("live_title");
        Assert.assertEquals(live_title,"这是预告标题","预告标题不正确");
        Reporter.log("判断预告简介不为空");
        String introduce = data.getString("introduce");
        Assert.assertEquals(introduce,"这是预告简介","预告简介不正确");
        Reporter.log("判断开播时间不为空");
        String live_start_time = data.getString("live_start_time");
        Assert.assertEquals(live_start_time,date,"开播日期不正确");
        Reporter.log("判断预约状态不为空");
        int enroll_status = data.getIntValue("enroll_status");
        Assert.assertEquals(enroll_status,Constants.RESPNSE_STATUS_CODE_0,"预约状态不正确");
        Reporter.log("判断用户头像可用性");
        String avatar = data.getJSONObject("anchor_data").getString("avatar");
        int statusCode = httpClient.getStatusCode(httpClient.get(avatar, new HashMap<>()));
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"判断用户头像不正确");
        Reporter.log("判断用户昵称不为空");
        String nick_name = data.getJSONObject("anchor_data").getString("nick_name");
        Assert.assertEquals(!nick_name.equals(""),true,"用户昵称不应为空");
        Reporter.log("判断宣传图可用性");
        JSONArray images = data.getJSONArray("images");
        statusCode = httpClient.getStatusCode(httpClient.get(images.getString(0), new HashMap<>()));
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"宣传图片不正确");
        Reporter.log("判断审核成功");
        int is_examination = data.getIntValue("is_examination");
        Assert.assertEquals(is_examination,Constants.RESPNSE_STATUS_CODE_1,"审核状态不正确，应为1");

    }
}
