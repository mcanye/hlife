package hlife.test.integration;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import hlife.base.BaseApi;
import hlife.base.Constants;
import hlife.httpclient.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;

/**
 * 收藏相关
 */
@Test(groups = "Collection")
public class Collection extends BaseApi{

    private HttpClient httpClient = new HttpClient();
    private static Logger log = LoggerFactory.getLogger(MinePage.class);
    //粉丝团ID
    private String group_target_id;
    //聊天记录ID
    private String msg_uid;
    //聊天内容
    private String text;
    //封装后的body
    private String body;
    //收藏ID
    private String id;
    //小视频ID
    private String video_id;
    //自修课ID
    private String selfStud_id;
    //自修课单课ID
    private String selfStud_only_id;


    @Test
    public void friendHome() throws IOException, InterruptedException {
        HashMap<String, String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("user_id", userIdA);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(friendHome, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_1, "接口请求失败");
        Assert.assertEquals(msg, "获取用户信息成功", "接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        group_target_id = data.getString("group_target_id");
    }

/*    *//**
     * 获取即信聊天记录
     *//*
    @Test(dependsOnMethods = { "friendHome"})
    public void chatRecordList() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("channel_name","taiji");
        params.put("target_id",group_target_id);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(chatRecordList, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取聊天记录列表","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for(int i = 0 ;i<list.size();i++){
            JSONObject jsonObject = list.getJSONObject(i);
            msg_uid = jsonObject.getString("jsonObject");
            JSONObject message_content = jsonObject.getJSONObject("message_content");
            text = message_content.getString("content");
            JSONObject user = message_content.getJSONObject("user");
            if(user.size()>0){
                String headerUrl = user.getString("portrait");
                String id = user.getString("id");
                String name = user.getString("name");
                if(!headerUrl.equals("")){
                    JSONObject obj = new JSONObject();
                    obj.put("text",text);
                    obj.put("headerUrl",headerUrl);
                    obj.put("id",id);
                    obj.put("name",name);
                    body = obj.toString();
                    break;
                }
            }

        }

    }

    *//**
     * 收藏即信聊天记录
     * @throws IOException
     *//*
    @Test(dependsOnMethods = { "chatRecordList"})
    public void chatRecordcollect() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("body",body);
        params.put("record_id",msg_uid);
        params.put("type","1");
        params.put("target_id",group_target_id);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(chatRecordcollect, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"收藏成功","接口返回msg不正确");
        Reporter.log("判断是否收藏成功");
        boolean isin = myfavorite("chat");
        Assert.assertEquals(isin,true,"收藏失败");
    }

    *//**
     * 删除即信聊天
     *//*
    @Test(dependsOnMethods = { "chatRecordcollect"})
    public void chatRecordnocollect() throws IOException {
        HashMap<String,String> params  = new HashMap<>();
        params.put("access_token",access_token);
        params.put("id",id);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(chatRecordnocollect, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"已删除","接口返回msg不正确");
        Reporter.log("判断是否取消收藏成功");
        boolean isin = myfavorite("chat");
        Assert.assertEquals(isin,false,"取消收藏失败");
    }*/

    /**
     * 小视频列表
     */
    @Test
    public void videoList() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("city","全国");
        params.put("uuid","00000000-258c-468d-ffff-ffffef05ac4a");
        params.put("refresh","1");
        JSONObject rs = httpClient.getResponseJson(httpClient.post(videoList, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取成功","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        JSONObject jsonObject = list.getJSONObject(1);
        video_id = jsonObject.getString("id");

    }

    /**
     * 收藏小视频/取消收藏
     */
    @Test(dependsOnMethods = { "videoList"})
    public void collectionVideo() throws IOException, InterruptedException {
        publicsFavorite("video",video_id);
        loading();
        boolean video = myfavorite("video");
        Assert.assertEquals(video,true,"收藏失败");
        loading();
        publicsFavorite("video",video_id);
        loading();
        video = myfavorite("video");
        Assert.assertEquals(video,false,"取消收藏失败");
    }

    /**
     * 自修课列表
     */
    @Test
    public void courseSelfStudyList() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(courseSelfStudyList, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取成功","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        JSONObject jsonObject = list.getJSONObject(0);
        selfStud_id = jsonObject.getString("id");
    }

    /**
     * 自修课详情列表 获取单课ID
     */
    @Test(dependsOnMethods = { "courseSelfStudyList"})
    public void courseSelfStudyDetail() throws IOException {
        courseSelfStudyDetail = courseSelfStudyDetail+selfStud_id;
        JSONObject rs = httpClient.getResponseJson(httpClient.post(courseSelfStudyDetail, new HashMap<>()));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取自修课详情列表","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        JSONObject jsonObject = list.getJSONObject(1);
        selfStud_only_id = jsonObject.getString("id");
        Assert.assertEquals(selfStud_only_id.equals(""),false,"ID不能为空");
    }

    /**
     * 收藏自修课/取消收藏自修课
     */
    @Test(dependsOnMethods = { "courseSelfStudyDetail"})
    public void collectionSelfd() throws IOException, InterruptedException {
        publicsFavorite("selfd",selfStud_only_id);
        loading();
        boolean isin = myfavorite("selfd");
        Assert.assertEquals(isin,true,"收藏自修课失败");
        loading();
        publicsFavorite("selfd",selfStud_only_id);
        loading();
        isin = myfavorite("selfd");
        Assert.assertEquals(isin,false,"取消收藏自修课失败");
    }

    /**
     * 统一收藏接口
     */
    private void publicsFavorite(String type,String id) throws IOException {
        String new_publicsFavorite = publicsFavorite;
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("type",type);
        new_publicsFavorite = new_publicsFavorite+id;
        JSONObject rs = httpClient.getResponseJson(httpClient.post(new_publicsFavorite, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
    }

    /**
     * 收藏列表
     * @param type
     * @return
     * @throws IOException
     */
    private boolean myfavorite(String type) throws IOException {
        boolean isin = false;
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("type",type);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(myfavorite, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取成功","接口请求msg不正确");
        JSONObject data = rs.getJSONObject("data");
        if(type.equals("chat")){
            if(data.size()>0){
                JSONArray list = data.getJSONArray("list");
                if(list.size()>0){
                    JSONObject list_obj;
                    for (int i = 0;i<list.size();i++) {
                        list_obj = list.getJSONObject(i);
                        String content = list_obj.getString("content");
                        if(content.equals(text)){
                            id = list_obj.getString("id");
                            isin = true;
                            return isin;
                        }
                    }
                }
            }
        }

        if(type.equals("video")){
            if(data.size()>0){
                JSONArray list = data.getJSONArray("list");
                if(list.size()>0){
                    JSONObject list_obj;
                    for (int i = 0;i<list.size();i++) {
                        list_obj = list.getJSONObject(i);
                        String id = list_obj.getString("id");
                        if(id.equals(video_id)){
                            isin = true;
                            return isin;
                        }
                    }
                }
            }
        }

        if(type.equals("selfd")){
            if(data.size()>0){
                JSONArray list = data.getJSONArray("list");
                if(list.size()>0){
                    JSONObject list_obj;
                    for (int i = 0;i<list.size();i++) {
                        list_obj = list.getJSONObject(i);
                        String id = list_obj.getString("id");
                        if(id.equals(selfStud_only_id)){
                            isin = true;
                            return isin;
                        }
                    }
                }
            }
        }


        return isin;
    }

}
