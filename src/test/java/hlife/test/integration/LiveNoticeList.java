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

import javax.print.attribute.HashAttributeSet;
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
    //直播ID
    private String live_id;
    private String date = getDate(7200000);

    /**
     * 发布直播预告
     */
    @Test
    public void createLiveNotice() throws IOException {

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
        log.info("date:"+date);
        log.info("live_start_time:"+live_start_time);
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
        live_id = data.getString("live_id");
        Assert.assertEquals(live_id.equals(""),false,"live_id不应为空");
    }

    /**
     * 预告预约
     */
    @Test(dependsOnMethods = { "liveNoticeDet" })
    public void liveNoticeEnroll() throws IOException, InterruptedException {

        Reporter.log("判断token不为空");
        HashMap<String,String> params = new HashMap<>();
        CloseableHttpResponse post = httpClient.post(liveNoticeEnroll, params);
        JSONObject rs = httpClient.getResponseJson(post);
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        String msg = rs.getString("msg");
        int status = rs.getIntValue("status");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_Minus_1,"预告预约接口请求失败");
        Assert.assertEquals(msg,"请登录","预告预约接口msg不正确");
        Thread.sleep(3000);
        Reporter.log("判断live_id不为空");
        params.clear();
        params.put("access_token",access_token);
        post = httpClient.post(liveNoticeEnroll, params);
        rs = httpClient.getResponseJson(post);
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        msg = rs.getString("msg");
        status = rs.getIntValue("status");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_0,"预告预约接口请求失败");
        Assert.assertEquals(msg,"直播ID必须上传","预告预约接口msg不正确");
        Thread.sleep(3000);
        Reporter.log("判断自己不能预约自己");
        params.clear();
        params.put("access_token",access_token);
        params.put("live_id",live_id);
        post = httpClient.post(liveNoticeEnroll, params);
        rs = httpClient.getResponseJson(post);
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        msg = rs.getString("msg");
        status = rs.getIntValue("status");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_0,"预告预约接口请求失败");
        //TODO 待缺陷13429修复后释放注释
        //Assert.assertEquals(msg,"不能预约自己的预告","预告预约接口msg不正确");
        Thread.sleep(3000);
        Reporter.log("校验code=200\n" +
                "status=1\n" +
                "msg=预约成功，直播开始前十分钟将通过即信通知您观看");
        params.clear();
        params.put("access_token",new_access_token);
        params.put("live_id",live_id);
        post = httpClient.post(liveNoticeEnroll, params);
        rs = httpClient.getResponseJson(post);
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        msg = rs.getString("msg");
        status = rs.getIntValue("status");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_1,"预告预约接口请求失败");
        Assert.assertEquals(msg,"预约成功，直播开始前十分钟将通过即信通知您观看","预告预约接口msg不正确");
        JSONObject data = rs.getJSONObject("data");
        int enroll_status = data.getIntValue("enroll_status");
        Assert.assertEquals(enroll_status,1,"预告预约失败");
        Thread.sleep(3000);
        params.clear();
        params.put("access_token",new_access_token);
        params.put("notice_id",notice_id);
        post = httpClient.post(liveNoticeDet, params);
        rs = httpClient.getResponseJson(post);
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        msg = rs.getString("msg");
        status = rs.getIntValue("status");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_1,"预告详情接口请求失败");
        //Assert.assertEquals(msg,"获取预告详情成功","预告详情接口msg不正确");
        data = rs.getJSONObject("data");
        int enroll_status1 = data.getIntValue("enroll_status");
        Assert.assertEquals(enroll_status,enroll_status1,"预约失败");
    }

    /**
     * 取消预约
     * @throws IOException
     * @throws InterruptedException
     */
    @Test(dependsOnMethods = { "liveNoticeEnroll" })
    public void cancelEnroll() throws IOException, InterruptedException {
        Reporter.log("校验code=200\n" +
                "status=1\n" +
                "msg=取消预约成功");
        HashMap<String,String> params = new HashMap<>();
        params.clear();
        params.put("access_token",new_access_token);
        params.put("live_id",live_id);
        CloseableHttpResponse post = httpClient.post(liveNoticeEnroll, params);
        JSONObject rs = httpClient.getResponseJson(post);
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        String msg = rs.getString("msg");
        int status = rs.getIntValue("status");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_1,"预告预约接口请求失败");
        Assert.assertEquals(msg,"取消预约成功","预告预约接口msg不正确");
        JSONObject data = rs.getJSONObject("data");
        int enroll_status = data.getIntValue("enroll_status");
        Assert.assertEquals(enroll_status,0,"预告预约失败");
        Thread.sleep(3000);
        params.clear();
        params.put("access_token",new_access_token);
        params.put("notice_id",notice_id);
        post = httpClient.post(liveNoticeDet, params);
        rs = httpClient.getResponseJson(post);
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        msg = rs.getString("msg");
        status = rs.getIntValue("status");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_1,"预告详情接口请求失败");
        //Assert.assertEquals(msg,"获取预告详情成功","预告详情接口msg不正确");
        data = rs.getJSONObject("data");
        int enroll_status1 = data.getIntValue("enroll_status");
        Assert.assertEquals(enroll_status,enroll_status1,"预约失败");
    }

    /**
     * 预告广告adv_type=live-notice
     */
    @Test
    public void advList() throws IOException {

        Reporter.log("校验adv_type为空");
        HashMap<String,String> params = new HashMap<>();
        CloseableHttpResponse post = httpClient.post(advList,params);
        int statusCode = httpClient.getStatusCode(post);
        JSONObject rs = httpClient.getResponseJson(post);
        log.info(rs.toString());
        Reporter.log(rs.toString());
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"直播列表广告列表接口请求失败");
        int status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"直播列表广告列表接口请求失败");
        String msg = rs.getString("msg");
        Assert.assertEquals(msg,"广告类型不能为空","直播列表广告列表msg错误");
        Reporter.log("用例通过");

        Reporter.log("校验adv_type不正确");
        params.clear();
        params.put("adv_type","1");
        post = httpClient.post(advList,params);
        statusCode = httpClient.getStatusCode(post);
        rs = httpClient.getResponseJson(post);
        log.info(rs.toString());
        Reporter.log(rs.toString());
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"直播列表广告列表接口请求失败");
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"直播列表广告列表接口请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"广告位不存在","直播列表广告列表msg错误");
        Reporter.log("用例通过");

        Reporter.log("判断code=200\n" +
                "status=1\n" +
                "msg=获取广告列表成功");
        params.clear();
        params.put("adv_type","live-notice");
        post = httpClient.post(advList,params);
        statusCode = httpClient.getStatusCode(post);
        rs = httpClient.getResponseJson(post);
        log.info(rs.toString());
        Reporter.log(rs.toString());
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"直播列表广告列表接口请求失败");
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"直播列表广告列表接口请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"获取广告列表成功","直播列表广告列表msg错误");
        Reporter.log("用例通过");

        Reporter.log("判断广告图片不为空data[i].ad_img[]");
        JSONArray data = rs.getJSONArray("data");
        if(data != null && data.size()>0){
            JSONObject dataObj;
            for (int i = 0;i<data.size();i++){
                dataObj = (JSONObject) data.get(i);
                JSONArray ad_img = dataObj.getJSONArray("ad_img");
                if(ad_img.size()>0 && ad_img != null){
                    for (int j = 0;j< ad_img.size();j++){
                        String img = (String) ad_img.get(j);
                        CloseableHttpResponse closeableHttpResponse = httpClient.get(img, new HashMap<String, String>());
                        int statusCode1 = httpClient.getStatusCode(closeableHttpResponse);
                        Assert.assertEquals(statusCode1,Constants.RESPNSE_STATUS_CODE_200,"广告图片无法显示"+img);
                    }
                } else {
                    Assert.assertEquals(true,false,"广告图片不应为空ad_img");
                }
            }
        }
        Reporter.log("判断广告图片不为空data[i].ad_img[]用例通过");
    }

    /**
     * 推荐预告
     */
    @Test(dependsOnMethods = { "createLiveNotice" })
    public void setLiveNoticeRecommend() throws IOException {
        HashMap<String,String> header = new HashMap<>();
        header.put("Cookie","backend_csrftoken=7078b4d2ef0f0b9290a26c4c16b0b0264a0d3d02374f025b6c84cab18186eb68a%3A2%3A%7Bi%3A0%3Bs%3A17%3A%22backend_csrftoken%22%3Bi%3A1%3Bs%3A32%3A%22J6QSZLMQNA0NiGXPoiO21r6-86EB7DvW%22%3B%7D; advanced-backend=oordrttr8fq3ch6i3j25kavtrg");
        setLiveNoticeRecommend=setLiveNoticeRecommend+"?notice_id="+notice_id;
        CloseableHttpResponse post = httpClient.post(setLiveNoticeRecommend, new HashMap<>(), header);
        int statusCode = httpClient.getStatusCode(post);
        JSONObject rs = httpClient.getResponseJson(post);
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"推荐预告接口请求失败");
        String msg = rs.getString("msg");
        int status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"推荐预告接口请求失败");
        Assert.assertEquals(msg,"设为推荐成功","推荐预告接口msg不正确");

    }
}
