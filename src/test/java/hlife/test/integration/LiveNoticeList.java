package hlife.test.integration;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import hlife.base.BaseApi;
import hlife.base.Constants;
import hlife.filtration.URLFiltration;
import hlife.httpclient.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    //直播分类
    private List<String> live_typeMap = new ArrayList<>();
    private String date = getDate(7200000);
    private int page = 1;
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
        params.put("live_start_time",date);
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
        params.put("live_start_time",date);
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

        Assert.assertEquals(msg,"获取预告详情成功","预告详情接口msg不正确");
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
        header.put("Cookie","backend_csrftoken=a9991e44fc121542e6f26815014197a2fc3fc2b0be734f492d6f20ea47e50878a%3A2%3A%7Bi%3A0%3Bs%3A17%3A%22backend_csrftoken%22%3Bi%3A1%3Bs%3A32%3A%226KwX4UuE9wsieI08UQyO3JbSRdwgS35O%22%3B%7D; advanced-backend=5a4amu6jtdch5ldme43djgv5uj");
        header.put("Connection","keep-alive");
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

    /**
     *预告推荐列表
     */
    @Test(dependsOnMethods = { "setLiveNoticeRecommend" })
    public void liveNoticeRecommendList() throws IOException {
        Reporter.log("判断code=200\n" +
                "status=1\n" +
                "msg=获取推荐直播列表成功");
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        CloseableHttpResponse post = httpClient.post(liveNoticeRecommendList, params);
        int statusCode = httpClient.getStatusCode(post);
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"预告推荐列表请求失败");
        JSONObject rs = httpClient.getResponseJson(post);
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"预告推荐列表请求失败");
        String msg = rs.getString("msg");
        Assert.assertEquals(msg,"获取推荐直播列表成功","预告推荐列表返回msg不正确");
        JSONArray data = rs.getJSONArray("data");
        for(int i = 0; i<data.size();i++){
            JSONObject dataObj = data.getJSONObject(i);
            JSONArray list = dataObj.getJSONArray("list");
            Reporter.log("判断每个分类推荐的直播间满足（0,5]条件");
            Assert.assertEquals(list.size()>0 && list.size()<=5,true,"每个分类推荐的直播间不满足（0,5]条件");
            Reporter.log("判断分类名称不为空");
            String live_cname = dataObj.getString("live_cname");
            Assert.assertEquals(live_cname.equals(""),false,"分类名称不应为空");

            for (int j = 0;j<list.size();j++){
                Reporter.log("判断预告标题不为空");
                JSONObject jsonObject = list.getJSONObject(j);
                String live_title = jsonObject.getString("live_title");
                Assert.assertEquals(live_title.equals(""),false,"预告标题不应为空");
                Reporter.log("判断直播预告封面图可正确显示");
                String live_cover = jsonObject.getString("live_cover");
                Assert.assertEquals(live_cover.equals(""),false,"直播预告封面图不应为空");
                statusCode = httpClient.getStatusCode(httpClient.get(live_cover, new HashMap<>()));
                Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"直播预告封面图不正确");
                Reporter.log("判断开播时间不为空");
                long live_start_time = jsonObject.getLongValue("live_start_time");
                Assert.assertEquals(live_start_time>0,true,"开播时间不正确");
                Reporter.log("判断预约状态不为空");
                int enroll_status = jsonObject.getIntValue("enroll_status");
                Assert.assertEquals(enroll_status==0||enroll_status==1,true,"预约状态不正确");
                Reporter.log("判断预告简介不为空");
                String introduce = jsonObject.getString("introduce");
                Assert.assertEquals(introduce.equals(""),false,"预告简介不应为空");
            }

        }
    }

    /**
     * 获取直播分类（根据apptype不同返回值不同，分类由后台管理端配置）
     */
    @Test(dependsOnMethods = { "createLiveNotice" })
    public void liveCategoryList() throws IOException {

        HashMap<String, String> header = URLFiltration.addHeader(new HashMap<String, String>());
        HashMap<String,String> params = new HashMap<>();
        CloseableHttpResponse post = httpClient.post(liveCategoryList,params,header);
        int statusCode = httpClient.getStatusCode(post);
        JSONObject rs = httpClient.getResponseJson(post);
        log.info(rs.toString());
        Reporter.log(rs.toString());
        Reporter.log("判断code=200\n" +
                "msg=获取直播分类成功\n" +
                "status=1");
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"获取直播分类接口请求失败");
        int status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"获取直播分类接口请求失败");
        String msg = rs.getString("msg");
        Assert.assertEquals(msg,"获取直播分类成功","获取直播分类接口msg错误");
        JSONArray data = rs.getJSONArray("data");
        if (data.size()>0 && data != null){
            JSONObject data_obj;
            boolean isFollow = false;
            for (int i = 0 ;i<data.size();i++){
                Reporter.log("判断data["+i+"].cate_name不为空");
                data_obj = (JSONObject) data.get(i);
                String cate_name = data_obj.getString("cate_name");
                if (cate_name.equals("") || cate_name == null){
                    Assert.assertEquals(true,false,"分类名称不应为空cate_name");
                }
                if (cate_name.equals("关注")){
                    isFollow = true;
                }
                Reporter.log("用例通过");
                Reporter.log("判断data["+i+"].id不为空");
                String id = data_obj.getString("id");
                if(id.equals("") || id == null){
                    Assert.assertEquals(true,false,"分类标识不应为空id");
                }
                Reporter.log("用例通过");
                Reporter.log("封装键值对");
                live_typeMap.add(id);
            }
            Reporter.log("判断返回值内包含“关注”");
            Assert.assertEquals(isFollow,true,"");
            Reporter.log("用例通过");
        }
    }

    /**
     * 直播预告关注列表
     */
    @Test(dependsOnMethods = { "createLiveNotice" })
    public void liveNoticeFocuslist() throws IOException {
        Reporter.log("检验token为空");
        HashMap<String,String> params = new HashMap<>();
        CloseableHttpResponse post = httpClient.post(liveNoticeFocuslist, params);
        JSONObject rs = httpClient.getResponseJson(post);
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        String msg = rs.getString("msg");
        int status = rs.getIntValue("status");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_Minus_1,"预告预约接口请求失败");
        Assert.assertEquals(msg,"请登录","预告预约接口msg不正确");

        while (true){
            Reporter.log("校验code=200\n" +
                    "status=1\n" +
                    "msg=获取关注直播列表成功");
            params.clear();
            params.put("access_token",new_access_token);
            params.put("page",page+"");
            params.put("pagesize","10");
            post = httpClient.post(liveNoticeFocuslist, params);
            rs = httpClient.getResponseJson(post);
            Reporter.log(rs.toJSONString());
            log.info(rs.toJSONString());
            msg = rs.getString("msg");
            status = rs.getIntValue("status");
            Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_1,"预告预约接口请求失败");
            Assert.assertEquals(msg,"获取推荐直播列表成功","预告预约接口msg不正确");
            JSONObject data = rs.getJSONObject("data");

            JSONObject pager = data.getJSONObject("pager");
            JSONArray list = data.getJSONArray("list");
            for(int i=0;i<list.size();i++){
                Reporter.log("判断预告标题不为空");
                JSONObject jsonObject = list.getJSONObject(i);
                String live_title = jsonObject.getString("live_title");
                Assert.assertEquals(live_title.equals(""),false,"预告标题不应为空");
                Reporter.log("判断直播预告封面图可正确显示");
                String live_cover = jsonObject.getString("live_cover");
                Assert.assertEquals(live_cover.equals(""),false,"直播预告封面图不应为空");
                int statusCode = httpClient.getStatusCode(httpClient.get(live_cover, new HashMap<>()));
                Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"直播预告封面图不正确");
                Reporter.log("判断开播时间不为空");
                long live_start_time = jsonObject.getLongValue("live_start_time");
                Assert.assertEquals(live_start_time>0,true,"开播时间不正确");
                Reporter.log("判断预约状态不为空");
                int enroll_status = jsonObject.getIntValue("enroll_status");
                Assert.assertEquals(enroll_status==0||enroll_status==1,true,"预约状态不正确");
                Reporter.log("判断预告简介不为空");
                String introduce = jsonObject.getString("introduce");
                Assert.assertEquals(introduce.equals(""),false,"预告简介不应为空");
            }

            Reporter.log("检测分页");
            Assert.assertEquals(list.size()<=10,true,"分页不正确");
            int pagesize= Integer.parseInt(pager.getString("pagesize"));
            if (list.size()<pagesize){
                page=1;
                break;
            }else {
                page+=1;
            }
        }

    }

    /**
     * 预告列表type=2
     */
    @Test(dependsOnMethods = { "liveCategoryList" })
    public void liveNoticeList() throws IOException {
        for(int i = 1;i<live_typeMap.size();i++){

            while (true){
                Reporter.log("校验code=200\n" +
                        "status=1\n" +
                        "msg=获取直播列表成功");
                HashMap<String,String> params = new HashMap<>();
                params.put("live_cid",live_typeMap.get(i));
                params.put("access_token",access_token);
                params.put("type","2");
                params.put("page",page+"");
                params.put("pagesize","10");
                HashMap<String, String> header = URLFiltration.addHeader(new HashMap<>());
                CloseableHttpResponse post = httpClient.post(liveNoticeList, params, header);
                int statusCode = httpClient.getStatusCode(post);
                JSONObject rs = httpClient.getResponseJson(post);
                Reporter.log(rs.toJSONString());
                log.info(rs.toJSONString());
                Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"预告列表错误");
                int status = rs.getIntValue("status");
                String msg = rs.getString("msg");
                Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"预告列表接口请求失败");
                Assert.assertEquals(msg,"获取直播预告列表成功","预告列表接口请求失败");
                JSONObject data = rs.getJSONObject("data");
                JSONObject pager = data.getJSONObject("pager");
                JSONArray list = data.getJSONArray("list");
                if(list.size()>0 && list != null ){
                    for(int j=0;j<list.size();j++){
                        Reporter.log("判断预告标题不为空");
                        JSONObject jsonObject = list.getJSONObject(j);
                        String live_title = jsonObject.getString("live_title");
                        Assert.assertEquals(live_title.equals(""),false,"预告标题不应为空");
                        Reporter.log("判断直播预告封面图可正确显示");
                        String live_cover = jsonObject.getString("live_cover");
                        Assert.assertEquals(live_cover.equals(""),false,"直播预告封面图不应为空");
                        statusCode = httpClient.getStatusCode(httpClient.get(live_cover, new HashMap<>()));
                        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"直播预告封面图不正确");
                        Reporter.log("判断开播时间不为空");
                        long live_start_time = jsonObject.getLongValue("live_start_time");
                        Assert.assertEquals(live_start_time>0,true,"开播时间不正确");
                        Reporter.log("判断预约状态不为空");
                        int enroll_status = jsonObject.getIntValue("enroll_status");
                        Assert.assertEquals(enroll_status==0||enroll_status==1,true,"预约状态不正确");
                        Reporter.log("判断预告简介不为空");
                        String introduce = jsonObject.getString("introduce");
                        Assert.assertEquals(introduce.equals(""),false,"预告简介不应为空");
                    }
                }

                int pagesize= Integer.parseInt(pager.getString("pagesize"));
                if (list.size()<pagesize){
                    page=1;
                    break;
                }else {
                    page+=1;
                }
            }


        }



    }

    /**
     * 个人主页预告列表type=1
     */
    @Test(dependsOnMethods = { "liveNoticeList" })
    public void liveNoticeList_mine(){

        //TODO 自己个人主页 待bug13469修复后编写
        //TODO 查看他人个人主页待bug13469修复后编写
    }

    /**
     * 删除预告
     */
    @Test(dependsOnMethods = { "liveNoticeList_mine" })
    public void liveNoticeCancel() throws IOException {
        Reporter.log("参数正确，判断code=200\n" +
                "status=1\n" +
                "msg=\"取消直播预告成功\"");
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("notice_id",notice_id);
        CloseableHttpResponse post = httpClient.post(liveNoticeCancel, params);
        int statusCode = httpClient.getStatusCode(post);
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"删除预告接口请求失败");
        JSONObject rs = httpClient.getResponseJson(post);
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"删除预告接口请求失败");
        String msg = rs.getString("msg");
        Assert.assertEquals(msg,"取消直播预告成功","删除预告接口msg不正确");

    }

}
