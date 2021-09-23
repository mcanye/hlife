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
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Test(groups = "LiveRtcFunc")
public class LiveRtcFunc extends BaseApi{
    private HttpClient httpClient = new HttpClient();
    private static Logger log = LoggerFactory.getLogger(LiveRoom.class);
    private List<String> live_typeList = new ArrayList<>();
    private String liveId;
    private String uuid ="00000000-258c-468d-ffff-ffffef05ac4a";
    private String userIdA ;
    private String userIdB ;
    private boolean isin = false;
    //所有人禁言解除禁言 0 开启 1关闭
    private int muteAll;
    //用户麦克风权限开关 0 关闭 1开启
    private int enableAudio;
    //用户视频权限开关 0 关闭 1开启
    private int enableVideo;



    /**
     * 获取直播分类（根据apptype不同返回值不同，分类由后台管理端配置）
     */
    @Test
    public void liveCategoryList() throws IOException, InterruptedException {

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
        if (data.size()>0 ){
            JSONObject data_obj;
            boolean isFollow = false;
            for (int i = 0 ;i<data.size();i++){
                Reporter.log("判断data["+i+"].cate_name不为空");
                data_obj = (JSONObject) data.get(i);
                String cate_name = data_obj.getString("cate_name");
                if (cate_name.equals("")){
                    Assert.assertEquals(true,false,"分类名称不应为空cate_name");
                }
                if (cate_name.equals("关注")){
                    isFollow = true;
                }
                Reporter.log("用例通过");
                Reporter.log("判断data["+i+"].id不为空");
                String id = data_obj.getString("id");
                if(id.equals("")){
                    Assert.assertEquals(true,false,"分类标识不应为空id");
                }
                Reporter.log("用例通过");
                Reporter.log("封装键值对");
                live_typeList.add(id);
            }
            Reporter.log("判断返回值内包含“关注”");
            Assert.assertEquals(isFollow,false,"");
            Reporter.log("用例通过");
            userIdA = getUser_id("17611111111");
            userIdB = getUser_id("17610733700");
        }
    }

    /**
     * 创建直播间
     */
    @Test(dependsOnMethods = { "liveCategoryList" })
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
        int statusCode = httpClient.getStatusCode(httpClient.get(avatar,new HashMap<>()));
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
     * 主播开启聊天室
     */
    @Test(dependsOnMethods = { "liveCreate" })
    public void liveOpenRtcFunc() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("userId",userIdA);
        params.put("liveId",liveId);
        params.put("liveFunc","1");
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveOpenRtcFunc, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"开启成功","接口msg返回不正确");

    }

    /**
     * 用户加入直播间
     */
    @Test(dependsOnMethods = { "liveOpenRtcFunc"})
    public void liveJoin() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",new_access_token);
        params.put("live_id",liveId);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveJoin, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"进入直播间成功","接口请求msg返回不正确");
        chatroomInviteList();
    }

    /**
     * 邀请用户连麦
     */
    @Test(dependsOnMethods = { "liveJoin"})
    public void chatroomInvite() throws IOException {
        Reporter.log("邀请用户连麦");
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("liveId",liveId);
        params.put("userId",userIdB);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(chatroomInvite, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求异常");
        Assert.assertEquals(msg,"操作成功","接口返回msg不正确");
    }

    /**
     * 同意连麦
     */
    @Test(dependsOnMethods = { "chatroomInvite"})
    public void chatroomUserAgree() throws IOException, InterruptedException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",new_access_token);
        params.put("liveId",liveId);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(chatroomUserAgree, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求异常");
        Assert.assertEquals(msg,"操作成功","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        Assert.assertEquals(data.size()>0,true,"连麦成功后，未返回对应response数据");

        loading();
        rs = httpClient.getResponseJson(httpClient.post(chatroomUserAgree, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求异常");
        Assert.assertEquals(msg,"您已在聊天室","连麦失败");

        chatroomInviteList();
        Assert.assertEquals(isin,false,"用户同意连麦，没有在邀请列表中移除");
        chatroomManagement();
        Assert.assertEquals(isin,true,"用户同意连麦，没有在邀请列表中移除");
    }

    /**
     * 管理用户
     */
    @Test(dependsOnMethods = { "chatroomUserAgree"})
    public void chatroomManage() throws IOException, InterruptedException {
        //audio禁音,video禁视频,getout踢出
        Reporter.log("管理麦克风 视频权限");
        Reporter.log("关闭麦克风");
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("liveId",liveId);
        params.put("userId",userIdB);
        params.put("operateType","audio");
        JSONObject rs = httpClient.getResponseJson(httpClient.post(chatroomManage, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"声音已关闭","接口返回msg不正确");

        loading();
        Reporter.log("关闭视频");
        params.clear();
        params.put("access_token",access_token);
        params.put("liveId",liveId);
        params.put("userId",userIdB);
        params.put("operateType","video");
        rs = httpClient.getResponseJson(httpClient.post(chatroomManage, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"视频已关闭","接口返回msg不正确");
        Reporter.log("查看修改后状态");
        loading();
        chatroomManagement();
        Assert.assertEquals(enableAudio,0,"声音修改失败");
        Assert.assertEquals(enableVideo,0,"视频修改失败");
        loading();

        Reporter.log("开启麦克风");
        params.clear();
        params.put("access_token",access_token);
        params.put("liveId",liveId);
        params.put("userId",userIdB);
        params.put("operateType","audio");
        rs = httpClient.getResponseJson(httpClient.post(chatroomManage, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"声音已开启","接口返回msg不正确");

        loading();

        Reporter.log("开启视频");
        params.clear();
        params.put("access_token",access_token);
        params.put("liveId",liveId);
        params.put("userId",userIdB);
        params.put("operateType","video");
        rs = httpClient.getResponseJson(httpClient.post(chatroomManage, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"视频已开启","接口返回msg不正确");
        chatroomManagement();
        Assert.assertEquals(enableAudio,1,"声音修改失败");
        Assert.assertEquals(enableVideo,1,"视频修改失败");
    }

    /**
     * 所有人静音
     */
    @Test(dependsOnMethods = { "chatroomManage"})
    public void chatroomMuteAll() throws IOException, InterruptedException {
        Reporter.log("全员静音");
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("liveId",liveId);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(chatroomMuteAll, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"全员静音成功","接口返回msg不正确");
        Reporter.log("查看修改后状态");
        chatroomManagement();
        Assert.assertEquals(muteAll,1,"声音修改失败");

        loading();
        Reporter.log("全员开启静音");

        rs = httpClient.getResponseJson(httpClient.post(chatroomMuteAll, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"全员开启声音成功","接口返回msg不正确");
        Reporter.log("查看修改后状态");
        chatroomManagement();
        Assert.assertEquals(muteAll,0,"声音修改失败");

    }

    /**
     * 踢出用户
     */
    @Test(dependsOnMethods = { "chatroomMuteAll"})
    public void chatroomManage_getout() throws IOException, InterruptedException {
        Reporter.log("踢出用户连麦");
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("liveId",liveId);
        params.put("userId",userIdB);
        params.put("operateType","getout");
        JSONObject rs = httpClient.getResponseJson(httpClient.post(chatroomManage, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"已关闭用户","接口返回msg不正确");

        chatroomManagement();
        Assert.assertEquals(isin,false,"踢出用户成功后,管理列表中没有移除对应用户");
    }

    /**
     * 观众申请连麦
     */
    @Test(dependsOnMethods = { "chatroomManage_getout"})
    public void chatroomApply() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",new_access_token);
        params.put("liveId",liveId);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(chatroomApply, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求异常");
        Assert.assertEquals(msg,"操作成功","接口返回msg异常");

    }

    /**
     * 主播查看申请连麦用户列表
     */
    @Test(dependsOnMethods = { "chatroomApply"})
    public void chatroomApplyList() throws IOException {
        Reporter.log("主播查看申请连麦用户列表");
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("liveId",liveId);
        params.put("page","1");
        JSONObject rs = httpClient.getResponseJson(httpClient.post(chatroomApplyList, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求异常");
        Assert.assertEquals(msg,"获取申请列表成功","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        JSONObject listObj ;
        isin = false;
        for(int i = 0 ; i<list.size();i++){
            listObj = list.getJSONObject(i);
            String userId = listObj.getString("userId");
            if(userId.equals(userIdB)){
                isin = true;
            }
            Reporter.log("判断用户头像可用性");
            String avatar = listObj.getString("avatar");
            int statusCode = httpClient.getStatusCode(httpClient.get(avatar, new HashMap<>()));
            Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"用户头像显示异常");
            Reporter.log("判断用户昵称是否显示");
            String nickName = listObj.getString("nickName");
            Assert.assertEquals(nickName.equals(""),false,"用户昵称未显示");

        }
        Reporter.log("判断用户申请连麦，在申请列表中可否显示");
        Assert.assertEquals(isin,true,"用户申请连麦，没有在申请列表中显示");

    }

    /**
     * 主播同意连麦申请
     */
    @Test(dependsOnMethods = { "chatroomApplyList"})
    public void chatroomAgree() throws IOException, InterruptedException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("liveId",liveId);
        params.put("userId",userIdB);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(chatroomAgree, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"操作成功","接口返回msg不正确");

        loading();
        rs = httpClient.getResponseJson(httpClient.post(chatroomAgree, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        Assert.assertEquals(msg,"该用户已在聊天室","连麦失败");

        Reporter.log("查看列表是否移除用户");
        params.clear();
        params.put("access_token",access_token);
        params.put("liveId",liveId);
        params.put("page","1");
        rs = httpClient.getResponseJson(httpClient.post(chatroomApplyList, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求异常");
        Assert.assertEquals(msg,"获取申请列表成功","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        JSONObject listObj ;
        isin = false;
        for(int i = 0 ; i<list.size();i++){
            listObj = list.getJSONObject(i);
            String userId = listObj.getString("userId");
            if(userId.equals(userIdB)){
                isin = true;
            }
            Reporter.log("判断用户头像可用性");
            String avatar = listObj.getString("avatar");
            int statusCode = httpClient.getStatusCode(httpClient.get(avatar, new HashMap<>()));
            Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"用户头像显示异常");
            Reporter.log("判断用户昵称是否显示");
            String nickName = listObj.getString("nickName");
            Assert.assertEquals(nickName.equals(""),false,"用户昵称未显示");

        }
        Reporter.log("判断用户申请连麦，在申请列表中可否显示");
        Assert.assertEquals(isin,false,"连麦成功后，没有移除用户");

        Reporter.log("判断管理列表是否添加");
        chatroomManagement();
        Assert.assertEquals(isin,true,"连麦用户管理列表没有添加用户");
    }

    /**
     * 用户断开连麦
     */
    @Test(dependsOnMethods = { "chatroomAgree"})
    public void chatroomUserCloseMute() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("liveId",liveId);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(chatroomUserCloseMute, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"删除成功","接口返回");
        chatroomManagement();
        Assert.assertEquals(isin,false,"用户断开连麦后，管理列表没有移除用户");
        chatroomInviteList();
        Assert.assertEquals(isin,true,"用户断开连麦后，邀请列表没有添加用户");
    }

    /**
     * 关闭聊天室
     */
    @Test(dependsOnMethods = { "chatroomUserCloseMute"})
    public void liveCloseRtcFunc() throws IOException {
        Reporter.log("关闭聊天室");
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("userId",userIdA);
        params.put("liveId",liveId);
        params.put("liveFunc","1");
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveCloseRtcFunc, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"关闭成功","接口msg请求失败");

        params.clear();
        params.put("access_token",new_access_token);
        params.put("liveId",liveId);
        rs = httpClient.getResponseJson(httpClient.post(chatroomApply, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求异常");
        Assert.assertEquals(msg,"主播未开启聊天室","关闭聊天室失败");

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

    /**
     * 获取用户ID
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
     * 主播查看邀请连麦用户列表
     */
    private void chatroomInviteList() throws IOException {
        Reporter.log("主播查看邀请连麦用户列表");
        isin = false;
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("liveId",liveId);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(chatroomInviteList, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求异常");
        Assert.assertEquals(msg,"获取用户列表成功","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        JSONObject listObj ;

        for(int i = 0 ; i<list.size();i++){
            listObj = list.getJSONObject(i);
            String userId = listObj.getString("userId");
            if(userId.equals(userIdB)){
                isin = true;
            }
            Reporter.log("判断用户头像可用性");
            String avatar = listObj.getString("avatar");
            int statusCode = httpClient.getStatusCode(httpClient.get(avatar, new HashMap<>()));
            Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"用户头像显示异常");
            Reporter.log("判断用户昵称是否显示");
            String nickName = listObj.getString("nickName");
            Assert.assertEquals(nickName.equals(""),false,"用户昵称未显示");

        }
        Reporter.log("判断用户进入直播间，在邀请连麦列表中可否显示");

    }

    /**
     * 管理连麦列表
     */
    private void chatroomManagement() throws IOException {
        isin = false;
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("liveId",liveId);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(chatroomManagement, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"查询成功","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        muteAll = data.getIntValue("muteAll");
        JSONArray list = data.getJSONArray("list");
        JSONObject listObj;
        for(int i = 0 ; i<list.size(); i++){
            listObj = list.getJSONObject(i);
            String userId = listObj.getString("userId");
            if(userId.equals(userIdB)){
                isin = true;
                Reporter.log("判断用户头像可用性");
                String avatar = listObj.getString("avatar");
                int statusCode = httpClient.getStatusCode(httpClient.get(avatar, new HashMap<>()));
                Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"用户头像显示异常");
                Reporter.log("判断用户昵称是否显示");
                String nickName = listObj.getString("nickName");
                Assert.assertEquals(nickName.equals(""),false,"用户昵称未显示");
                enableAudio = listObj.getIntValue("enableAudio");
                enableVideo = listObj.getIntValue("enableVideo");
            }
        }
    }
}
