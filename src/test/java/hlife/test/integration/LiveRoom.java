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
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Test(groups = "LiveRoom")
public class LiveRoom extends BaseApi{

    private HttpClient httpClient = new HttpClient();
    private static Logger log = LoggerFactory.getLogger(LiveRoom.class);
    public List<String> live_typeList = new ArrayList<>();
    public String liveId;
    private String uuid ="00000000-258c-468d-ffff-ffffef05ac4a";
    private int in_group;
    private String group_tag;
    private String target_id;
    private String group_name;
    private int is_follow;

    /**
     * 获取直播分类（根据apptype不同返回值不同，分类由后台管理端配置）
     */
    @Test
    public void liveCategoryList() throws IOException {

        HashMap<String, String> header = URLFiltration.addHeader(new HashMap<>());
        HashMap<String,String> params = new HashMap<>();
        params.put("is_hidden","1");
        CloseableHttpResponse post = httpClient.post(liveCategoryList,params,header);
        int statusCode = httpClient.getStatusCode(post);
        JSONObject rs = httpClient.getResponseJson(post);
        log.info(rs.toString());
        Reporter.log(rs.toString());
        Reporter.log("判断code=200\n" +
                "msg=获取直播分类成功\n" +
                "status=1");
        Assert.assertEquals(statusCode, Constants.RESPNSE_STATUS_CODE_200,"获取直播分类接口请求失败");
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
                live_typeList.add(id);
            }
            Reporter.log("判断返回值内包含“关注”");
            Assert.assertEquals(isFollow,false,"");
            Reporter.log("用例通过");
        }
    }

    /**
     * 判断能否开播
     */
    @Test(dependsOnMethods = { "liveCategoryList" })
    public void liveAppCreate() throws IOException, InterruptedException {
        //不能开播，需要实名
        //可以开播，获取信息
        HashMap<String,String> header = URLFiltration.addHeader(new HashMap<>());
        header.put("uuid",uuid);
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",new_access_token);
        JSONObject rs;
        if(!liveAppCreate.contains("api")){
            rs = httpClient.getResponseJson(httpClient.post(liveAppCreate, params, header));
            log.info(rs.toJSONString());
            Reporter.log(rs.toJSONString());
            int status = rs.getIntValue("status");
            Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_Minus_3,"接口请求失败");
            String msg = rs.getString("msg");
            Assert.assertEquals(msg,"抱歉，暂时无法开播！\\r\\n根据国家政策要求，直播要求实名认证和人脸识别","接口返回msg异常");
            JSONObject data = rs.getJSONObject("data");
            String button_text = data.getString("button_text");
            Assert.assertEquals(button_text.equals("前往认证"),true,"认证入口文案不正确");
        }
        Thread.sleep(3000);

        Reporter.log("创建直播成功");
        params.clear();
        params.put("access_token",access_token);
        //rs = httpClient.getResponseJson(httpClient.post(liveAppCreate, params, header));
        rs = httpClient.getResponseJson(httpClient.post(liveAppCreate, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        String msg = rs.getString("msg");
        Assert.assertEquals(msg,"创建直播成功","接口返回msg不正确");
        Thread.sleep(3000);


    }
    /**
     * 创建直播间
     */
    @Test(dependsOnMethods = { "liveAppCreate" })
    public void liveCreate() throws IOException {
        HashMap<String,String> header = URLFiltration.addHeader(new HashMap<>());
        header.put("uuid",uuid);
        HashMap<String,String> params = new HashMap<>();
        params.put("live_setting","{\"BeautyVideoFx\":{\"Strength\":50,\"Whitening\":50},\"captureDevicePosition\":\"front\",\"isMirror\":1,\"is_lu\":0,\"is_send\":1,\"resolution\":\"普清360p\"}");
        params.put("auto_start","1");
        Reporter.log("判断跳转登录");
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveCreate, params,header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_Minus_1,"创建直播接口请求异常"+liveCreate);
        String msg = rs.getString("msg");
        Assert.assertEquals(msg,"请登录","创建直播接口返回msg不正确");

        Reporter.log("直播封面不能为空");
        params.put("access_token",access_token);
        rs = httpClient.getResponseJson(httpClient.post(liveCreate, params,header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"创建直播接口请求异常"+liveCreate);
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"直播封面不能为空","创建直播接口返回msg不正确");

        Reporter.log("直播类型id不能为空");
        params.put("live_cover","taiji/20210823/1629685787500825.jpg");
        rs = httpClient.getResponseJson(httpClient.post(liveCreate, params,header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"创建直播接口请求异常"+liveCreate);
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"直播类型id不能为空","创建直播接口返回msg不正确");

        Reporter.log("直播标题不能为空");
        params.put("live_cid",live_typeList.get(0));
        rs = httpClient.getResponseJson(httpClient.post(liveCreate, params,header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"创建直播接口请求异常"+liveCreate);
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"直播标题不能为空","创建直播接口返回msg不正确");

        Reporter.log("创建直播成功");
        params.put("live_title","自动化测试直播");
        rs = httpClient.getResponseJson(httpClient.post(liveCreate, params,header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"创建直播接口请求异常"+liveCreate);
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"创建直播成功","创建直播接口返回msg不正确");
        Reporter.log("封装直播ID");
        JSONObject data = rs.getJSONObject("data");
        liveId = data.getString("id");
        Assert.assertEquals(liveId.equals(""),false,"直播ID不应为空");

        Reporter.log("判断用户信息");
        JSONObject anchor_data = data.getJSONObject("anchor_data");
        String nick_name = anchor_data.getString("nick_name");
        Assert.assertEquals(nick_name.equals(""),false,"主播昵称不能为空");
        String avatar = anchor_data.getString("avatar");
        int statusCode = httpClient.getStatusCode(httpClient.get(avatar, new HashMap<>()));
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"主播头像显示异常");

        Reporter.log("判断在线用户");
        JSONArray online_data = data.getJSONArray("online_data");
        JSONObject jsonObject;
        for(int i = 0 ;i<online_data.size() ;i++){
            jsonObject = online_data.getJSONObject(i);
            String nick_name1 = jsonObject.getString("nick_name");
            Assert.assertEquals(nick_name1.equals(""),false,"在线用户昵称不能为空");
            avatar = jsonObject.getString("avatar");
            statusCode = httpClient.getStatusCode(httpClient.get(avatar, new HashMap<>()));
            Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"在线用户头像显示异常");
        }

        Reporter.log("判断活动信息");
        JSONObject activity_data = data.getJSONObject("activity_data");
        if(activity_data.size()>0){
            String activity_name = activity_data.getString("activity_name");
            Assert.assertEquals(activity_name.equals(""),false,"活动名称不应为空");
            String cover_pic = activity_data.getString("cover_pic");
            statusCode = httpClient.getStatusCode(httpClient.get(cover_pic, new HashMap<>()));
            Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"活动图片显示异常");
        }

        Reporter.log("判断分享信息");
        JSONObject share = data.getJSONObject("share");
        String url = share.getString("url");
        statusCode = httpClient.getStatusCode(httpClient.get(url, new HashMap<>()));
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"分享地址请求异常");
        String name = share.getString("name");
        Assert.assertEquals(name.equals(""),false,"分享标题不应为空");
        String content = share.getString("content");
        Assert.assertEquals(content.equals(""),false,"分享信息不应为空");
        String image = share.getString("image");
        statusCode = httpClient.getStatusCode(httpClient.get(image, new HashMap<>()));
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"分享图片请求异常");

        Reporter.log("您已有其他直播正在直播中");
        rs = httpClient.getResponseJson(httpClient.post(liveCreate, params,header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"创建直播接口请求异常"+liveCreate);
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"您已有其他直播正在直播中","创建直播接口返回msg不正确");


    }

    /**
     * 异常退出
     * @throws IOException
     */
    @Test(dependsOnMethods = { "liveCreate" })
    public void liveAppCreate1() throws IOException, InterruptedException {
        //异常退出，已开播，继续直播
        //顶号登录，已开播，观看直播
        Reporter.log("顶号登录，已开播，观看直播");
        HashMap<String, String> header = URLFiltration.addHeader(new HashMap<>());
        header.put("uuid","00000000-123c-468d-ffff-ffffef05ac4a");
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        //JSONObject rs = httpClient.getResponseJson(httpClient.post(liveAppCreate, params,header));
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveAppCreate, params,header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_Minus_3,"接口请求失败");
        String msg = rs.getString("msg");
        Assert.assertEquals(msg,"您已有其他直播正在直播中","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        String button_text = data.getString("button_text");
        Assert.assertEquals(button_text.equals("观看直播"),true,"提示文案不正确");
        Thread.sleep(3000);

        Reporter.log("异常退出，已开播，继续直播");
        header.put("uuid",uuid);
        rs = httpClient.getResponseJson(httpClient.post(liveAppCreate, params,header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status1 = rs.getIntValue("status");
        log.info(status1+"------------------");
        Assert.assertEquals(status1,Constants.RESPNSE_STATUS_CODE_Minus_3,"接口请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"您已有其他直播正在直播中","接口返回msg不正确");
        data = rs.getJSONObject("data");
        button_text = data.getString("button_text");
        Assert.assertEquals(button_text.equals("继续直播"),true,"提示文案不正确");
        Thread.sleep(3000);
    }


    /**
     * 获取直播间信息 加入直播间
     */
    @Test(dependsOnMethods = { "liveCreate"})
    public void liveDetail() throws IOException {
        HashMap<String,String> header = URLFiltration.addHeader(new HashMap<>());
        header.put("uuid",uuid);
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("auto","1");
        Reporter.log("直播id不能为空");
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveDetail, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        String msg = rs.getString("msg");
        Assert.assertEquals(msg,"直播id不能为空","接口msg返回失败");

        Reporter.log("获取直播信息成功");
        params.put("live_id",liveId);
        rs = httpClient.getResponseJson(httpClient.post(liveDetail, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"获取直播信息成功","接口msg返回失败");

        Reporter.log("判断用户信息");
        JSONObject data = rs.getJSONObject("data");
        JSONObject anchor_data = data.getJSONObject("anchor_data");
        String nick_name = anchor_data.getString("nick_name");
        Assert.assertEquals(nick_name.equals(""),false,"主播昵称不能为空");
        String avatar = anchor_data.getString("avatar");
        int statusCode = httpClient.getStatusCode(httpClient.get(avatar, new HashMap<>()));
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"主播头像显示异常");

        Reporter.log("判断在线用户");
        JSONArray online_data = data.getJSONArray("online_data");
        JSONObject jsonObject;
        for(int i = 0 ;i<online_data.size() ;i++){
            jsonObject = online_data.getJSONObject(i);
            String nick_name1 = jsonObject.getString("nick_name");
            Assert.assertEquals(nick_name1.equals(""),false,"在线用户昵称不能为空");
            avatar = jsonObject.getString("avatar");
            statusCode = httpClient.getStatusCode(httpClient.get(avatar, new HashMap<>()));
            Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"在线用户头像显示异常");
        }

        Reporter.log("判断活动信息");
        JSONObject activity_data = data.getJSONObject("activity_data");
        if(activity_data.size()>0){
            String activity_name = activity_data.getString("activity_name");
            Assert.assertEquals(activity_name.equals(""),false,"活动名称不应为空");
            String cover_pic = activity_data.getString("cover_pic");
            statusCode = httpClient.getStatusCode(httpClient.get(cover_pic, new HashMap<>()));
            Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"活动图片显示异常");
        }

        Reporter.log("判断分享信息");
        JSONObject share = data.getJSONObject("share");
        String url = share.getString("url");
        statusCode = httpClient.getStatusCode(httpClient.get(url, new HashMap<>()));
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"分享地址请求异常");
        String name = share.getString("name");
        Assert.assertEquals(name.equals(""),false,"分享标题不应为空");
        String content = share.getString("content");
        Assert.assertEquals(content.equals(""),false,"分享信息不应为空");
        String image = share.getString("image");
        statusCode = httpClient.getStatusCode(httpClient.get(image, new HashMap<>()));
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"分享图片请求异常");


    }





    /**
     *是否允许进入直播间
     */
    @Test(dependsOnMethods = { "liveAppCreate1" })
    public void liveStatus() throws IOException {
        Reporter.log("{\n" +
                "    \"status\": -1,\n" +
                "    \"msg\": \"请登录\",\n" +
                "    \"data\": {}\n" +
                "}");
        HashMap<String, String> header = URLFiltration.addHeader(new HashMap<>());
        header.put("uuid",uuid);
        HashMap<String,String> params = new HashMap<>();
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveStatus, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_Minus_1,"接口请求失败");
        Assert.assertEquals(msg,"请登录","接口返回msg不正确");

        Reporter.log("{\n" +
                "    \"status\": 0,\n" +
                "    \"msg\": \"直播id不能为空\",\n" +
                "    \"data\": {}\n" +
                "}");

        params.put("access_token",access_token);
        rs = httpClient.getResponseJson(httpClient.post(liveStatus, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        Assert.assertEquals(msg,"直播id不能为空","接口返回msg不正确");

        Reporter.log("获取直播状态信息成功");
        params.put("live_id",liveId);
        rs = httpClient.getResponseJson(httpClient.post(liveStatus, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取直播状态信息成功","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        int can_join = data.getIntValue("can_join");
        Assert.assertEquals(can_join,Constants.RESPNSE_STATUS_CODE_1,"判断状态不正确");
        String push_url = data.getString("push_url");
        Assert.assertEquals(push_url.equals(""),false,"推流地址不应为空");
        String pull_url = data.getString("pull_url");
        Assert.assertEquals(pull_url.equals(""),false,"下载地址不应为空");


    }



    /**
     * 修改粉丝团信息
     */
    public void fansGroupApply() throws IOException, InterruptedException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        Reporter.log("{\n" +
                "    \"status\": 0,\n" +
                "    \"msg\": \"粉丝团勋章最多3个字\",\n" +
                "    \"data\": {}\n" +
                "}");
        params.put("group_tag","哈哈哈哈");
        JSONObject rs = httpClient.getResponseJson(httpClient.post(fansGroupApply, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        Assert.assertEquals(msg,"粉丝团勋章最多3个字","接口返回msg不正确");
        Thread.sleep(3000);
        Reporter.log("粉丝团群名称最多15个字");
        params.clear();
        params.put("access_token",access_token);
        params.put("group_name","纨绔丶夜的粉丝团对对对鼎折覆餗放松放松");
        rs = httpClient.getResponseJson(httpClient.post(fansGroupApply, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        Assert.assertEquals(msg,"粉丝团群名称最多15个字","接口返回msg不正确");
        Thread.sleep(3000);
        Reporter.log("{\n" +
                "    \"status\": 1,\n" +
                "    \"msg\": \"修改成功\",\n" +
                "    \"data\": {}\n" +
                "}");
        params.clear();
        params.put("access_token",access_token);
        params.put("group_name","纨绔丶夜的粉丝团");
        params.put("join_type","1");
        params.put("group_tag","哈哈哈");
        rs = httpClient.getResponseJson(httpClient.post(fansGroupApply, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"修改成功","接口返回msg不正确");
        Thread.sleep(3000);
        Reporter.log("查看修改后结果是否一致");
        params.clear();
        params.put("access_token",new_access_token);
        String user_id = getUser_id("17611111111");
        params.put("user_id",user_id);
        rs = httpClient.getResponseJson(httpClient.post(fansGroupDetail, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取粉丝团信息成功","接口返回msg不正确");
        //封装in_group
        JSONObject data = rs.getJSONObject("data");
        String group_tag = data.getString("group_tag");
        Assert.assertEquals(group_tag,"哈哈哈","group_tag不一致");
        String group_name = data.getString("group_name");
        Assert.assertEquals(group_name,"纨绔丶夜的粉丝团","粉丝团名称不一致");

    }

    /**
     * 粉丝团详情
     */
    @Test(dependsOnMethods = { "fansGroupApply" })
    public void fansGroupDetail() throws IOException, InterruptedException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",new_access_token);
        String user_id = getUser_id("17611111111");
        params.put("user_id",user_id);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(fansGroupDetail, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取粉丝团信息成功","接口返回msg不正确");
        //封装in_group
        JSONObject data = rs.getJSONObject("data");
        in_group = data.getIntValue("in_group");
        group_tag = data.getString("group_tag");
        target_id = data.getString("target_id");
        group_name = data.getString("group_name");
        Assert.assertEquals(group_name.equals(""),false,"粉丝团名称不能为空");
    }

    /**
     * 我的粉丝团
     */
    @Test(dependsOnMethods = { "fansGroupDetail" })
    public void chatGroupinfo() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("target_id",target_id);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(chatGroupinfo, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取成功","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        Reporter.log("判断粉丝名");
        String name = data.getString("name");
        Assert.assertEquals(name.equals(""),false,"粉丝团名称不能为空");
        Reporter.log("统计人数是否正确");
        JSONArray user = data.getJSONArray("user");
        int num = data.getIntValue("num");
        Assert.assertEquals(num,user.size(),"粉丝团人数不正确");

    }


    /**
     * 根据in_group判断执行
     * 1 执行退群
     * 0 执行加入
     */
    @Test(dependsOnMethods = { "fansGroupDetail" })
    public void QuitOrJoin() throws IOException, InterruptedException {
        if(in_group == 1){
            //进入粉丝群
            chatEnter();
            //获取聊天信息
            chatRecordList();
            //粉丝群详情
            chatEnterInfo();
            //退群
            chatQuit();
            //查看是否退群成功
            fansGroupDetail();
            Assert.assertEquals(in_group,0,"退群失败");
        }else{
            //加入群聊
            charJoin();
            //查看是否退群成功
            fansGroupDetail();
            Assert.assertEquals(in_group,1,"加群失败");
        }
    }

    /**
     * 用户信息
     */
    @Test(dependsOnMethods = { "liveCreate" })
    public void liveUserInfo() throws IOException, InterruptedException {
        String user_id = getUser_id("17611111111");
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",new_access_token);
        params.put("live_id",liveId);
        params.put("user_id",user_id);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveUserInfo, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取直播间用户信息成功","接口返回失败");
        JSONObject data = rs.getJSONObject("data");
        Reporter.log("封装是否关注");
        is_follow = data.getIntValue("is_follow");
        Reporter.log("判断用户可用性");
        String avatar = data.getString("avatar");
        int statusCode = httpClient.getStatusCode(httpClient.get(avatar, new HashMap<>()));
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"用户头像显示异常");
        Reporter.log("判断用户昵称是否显示");
        String nick_name = data.getString("nick_name");
        Assert.assertEquals(nick_name.equals(""),false,"用户昵称不应为空");

    }

    /**
     * 关注/取消关注
     */
    @Test(dependsOnMethods = { "liveUserInfo" })
    public void followIndex() throws IOException, InterruptedException {
        String user_id = getUser_id("17611111111");
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",new_access_token);
        params.put("follow_who",user_id);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(followIndex, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        int old_is_follw = is_follow;
        liveUserInfo();
        switch(old_is_follw){
            case 0:
                Assert.assertEquals(msg,"关注成功","接口请求失败");
                Assert.assertEquals(is_follow,2,"状态变更不正确");
                break;
            case 1:
                Assert.assertEquals(msg,"关注成功","接口请求失败");
                Assert.assertEquals(is_follow,3,"状态变更不正确");
                break;
            case 2:
                Assert.assertEquals(msg,"取消关注成功","接口请求失败");
                Assert.assertEquals(is_follow,0,"状态变更不正确");
                break;
            case 3:
                Assert.assertEquals(msg,"取消关注成功","接口请求失败");
                Assert.assertEquals(is_follow,1,"状态变更不正确");
                break;
        }


    }

    /**
     * 进入粉丝群
     */
    private void chatEnter() throws IOException {
        if(in_group == 0){
            HashMap<String,String> params = new HashMap<>();
            params.put("access_token",new_access_token);
            params.put("channel_name","taiji");
            params.put("target_id",target_id);
            JSONObject rs = httpClient.getResponseJson(httpClient.post(chatEnter, params));
            log.info(rs.toJSONString());
            Reporter.log(rs.toJSONString());
            int status = rs.getIntValue("status");
            String msg = rs.getString("msg");
            Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
            Assert.assertEquals(msg,"获取成功","接口返回msg不正确");
            JSONObject data = rs.getJSONObject("data");
            String name = data.getString("name");
            Assert.assertEquals(name,group_name,"粉丝团名称不匹配");
            JSONArray users = data.getJSONArray("users");
            JSONObject usersObj;
            for(int i = 0 ;i< users.size();i++){
                usersObj = users.getJSONObject(i);
                String avatar = usersObj.getString("avatar");
                int statusCode = httpClient.getStatusCode(httpClient.get(avatar, new HashMap<>()));
                Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"用户头像异常");
                String nick_name = usersObj.getString("nick_name");
                Assert.assertEquals(nick_name.equals(""),false,"用户昵称不应为空");
            }
        }

    }

    /**
     * 获取聊天记录
     */
    private void chatRecordList() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",new_access_token);
        params.put("target_id",target_id);
        params.put("channel_name","taiji");
        JSONObject rs = httpClient.getResponseJson(httpClient.post(chatRecordList, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取聊天记录列表","接口请求msg不正确");
        JSONObject data = rs.getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        Assert.assertEquals(list.size()>0,true,"历史聊天记录未返回");

    }

    /**
     * 粉丝群详情
     */
    private void chatEnterInfo() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",new_access_token);
        params.put("channel_name","taiji");
        params.put("target_id",target_id);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(chatEnterInfo, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取成功","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        String name = data.getString("name");
        Assert.assertEquals(name,group_name,"粉丝团名称不匹配");
        JSONArray users = data.getJSONArray("users");
        JSONObject usersObj;
        for(int i = 0 ;i< users.size();i++){
            usersObj = users.getJSONObject(i);
            String avatar = usersObj.getString("avatar");
            int statusCode = httpClient.getStatusCode(httpClient.get(avatar, new HashMap<>()));
            Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"用户头像异常");
            String nick_name = usersObj.getString("nick_name");
            Assert.assertEquals(nick_name.equals(""),false,"用户昵称不应为空");
        }
    }

    /**
     * 退群
     */
    private void chatQuit() throws IOException, InterruptedException {
        String user_id = getUser_id("17610733700");
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",new_access_token);
        params.put("target_id",target_id);
        params.put("quit_id",user_id);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(chatQuit, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"退群成功","接口返回msg不正确");

    }

    /**
     * 加入群聊
     */
    private void charJoin() throws IOException, InterruptedException {
        String user_id = getUser_id("17610733700");
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",new_access_token);
        params.put("target_id",target_id);
        params.put("users","[\""+user_id+"\"]");
        JSONObject rs = httpClient.getResponseJson(httpClient.post(charJoin, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"加入群聊成功！","接口返回msg不正确");
    }

    /**
     * 获取用户ID
     * @param phone
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
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
     * 关闭直播
     */
    @AfterClass
    public void liveOpera() throws IOException {
        Reporter.log("请登录");
        HashMap<String,String> params = new HashMap<>();
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveOpera, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_Minus_1,"接口请求失败");
        Assert.assertEquals(msg,"请登录","接口返回msg不正确");

        Reporter.log("操作类型错误");
        params.put("access_token",access_token);
        rs = httpClient.getResponseJson(httpClient.post(liveOpera, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        Assert.assertEquals(msg,"操作类型错误","接口返回msg不正确");

        Reporter.log("直播id不能为空");
        params.put("oprea_type","end");
        rs = httpClient.getResponseJson(httpClient.post(liveOpera, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        Assert.assertEquals(msg,"直播id不能为空","接口返回msg不正确");

        Reporter.log("更新直播状态成功");
        params.put("live_id",liveId);
        rs = httpClient.getResponseJson(httpClient.post(liveOpera, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"更新直播状态成功","接口返回msg不正确");

        Reporter.log("直播时长不应为空");
        JSONObject data = rs.getJSONObject("data");
        String times = data.getString("times");
        Assert.assertEquals(times.equals(""),false,"直播时长不应为空");
        Reporter.log("anchor_nick_name不应为空");
        String anchor_nick_name = data.getString("anchor_nick_name");
        Assert.assertEquals(anchor_nick_name.equals(""),false,"anchor_nick_name不应为空");
        Reporter.log("anchor_avatar不应为空，且可访问");
        String anchor_avatar = data.getString("anchor_avatar");
        int statusCode = httpClient.getStatusCode(httpClient.get(anchor_avatar, new HashMap<>()));
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"anchor_avatar头像请求异常");
        Reporter.log("view_count不应为空");
        String view_count = data.getString("view_count");
        Assert.assertEquals(view_count.equals(""),false,"view_count不应为空");
        Reporter.log("look_num不应为空");
        String look_num = data.getString("look_num");
        Assert.assertEquals(look_num.equals(""),false,"look_num不应为空");


        Reporter.log("通过判断能否进入直播间接口确认是否关播成功");
        HashMap<String, String> header = URLFiltration.addHeader(new HashMap<>());
        header.put("uuid",uuid);
        params.clear();
        params.put("access_token",access_token);
        params.put("live_id",liveId);
        rs = httpClient.getResponseJson(httpClient.post(liveStatus, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取直播状态信息成功","接口返回msg不正确");
        data = rs.getJSONObject("data");
        int can_join = data.getIntValue("can_join");
        Assert.assertEquals(can_join,Constants.RESPNSE_STATUS_CODE_0,"关闭直播间失败");
    }
}
