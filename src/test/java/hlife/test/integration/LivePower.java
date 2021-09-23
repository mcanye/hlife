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

@Test(groups = "LivePower")
public class LivePower extends BaseApi{

    private HttpClient httpClient = new HttpClient();
    private static Logger log = LoggerFactory.getLogger(LivePower.class);
    //默认城市ID
    private String city_id;
    private List<String> houseIdList = new ArrayList<>();
    public List<String> live_typeList = new ArrayList<>();
    private String uuid ="00000000-258c-468d-ffff-ffffef05ac4a";
    private String liveId;
    private String houseIdList_s;
    private int page =1 ;
    private String is_ban;
    private String live_role;
    private String is_manage;
    private String is_block;
    private boolean isin = false;
    private String data_msg;
    private String re_show_id;


    @Test
    public void houseDefaultCity() throws IOException {
        JSONObject rs = httpClient.getResponseJson(httpClient.post(houseDefaultCity, new HashMap<>()));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取默认城市成功","接口返回失败");
        JSONObject data = rs.getJSONObject("data");
        city_id = data.getString("city_id");
        Assert.assertEquals(city_id.equals(""),false,"默认城市ID不应为空");

    }

    /**
     * 获取推荐房产列表
     */
    @Test(dependsOnMethods={"houseDefaultCity"})
    public void house_list() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("type","recommend");
        params.put("city_id",city_id);
        params.put("house_cid","1");
        JSONObject rs = httpClient.getResponseJson(httpClient.post(house_list, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求异常");
        Assert.assertEquals(msg,"获取房产列表成功","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        if(list.size()>0){
            JSONObject listObj;
            for(int i = 0 ;i<list.size();i++){
                listObj = list.getJSONObject(i);
                Reporter.log("判断房产名称不为空");
                String house_name = listObj.getString("house_name");
                Assert.assertEquals(house_name.equals(""),false,"房产名称不应为空");
                Reporter.log("判断房产封面图可正常显示");
                String house_pic = listObj.getString("house_pic");
                int statusCode = httpClient.getStatusCode(httpClient.get(house_pic, new HashMap<>()));
                Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"封面图显示异常");
                Reporter.log("判断价格是否显示");
                String price_range = listObj.getString("price_range");
                if(price_range.equals("")){
                    String house_price = listObj.getString("house_price");
                    Assert.assertEquals(house_price.equals(""),false,"房产价格不能为空");
                }
                String id = listObj.getString("id");
                houseIdList.add(id);
            }
        }

    }

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
     * 创建直播间
     */
    @Test(dependsOnMethods = { "liveCategoryList" })
    public void liveCreate() throws IOException {
        houseIdList_s = houseIdList.toString();
        houseIdList_s = houseIdList_s.replace(" ", "");
        houseIdList_s = houseIdList_s.replace("[", "");
        houseIdList_s = houseIdList_s.replace("]", "");
        HashMap<String,String> header = URLFiltration.addHeader(new HashMap<>());
        header.put("uuid",uuid);
        HashMap<String,String> params = new HashMap<>();
        params.put("live_setting","{\"BeautyVideoFx\":{\"Strength\":50,\"Whitening\":50},\"captureDevicePosition\":\"front\",\"isMirror\":1,\"is_lu\":0,\"is_send\":1,\"resolution\":\"普清360p\"}");
        params.put("auto_start","1");
        params.put("re_type","house");
        params.put("re_id",houseIdList_s);
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
     * 直播商品列表
     * @throws IOException
     */
    @Test(dependsOnMethods = { "liveCreate" })
    public void publicReDataList() throws IOException {
        while (true){
            HashMap<String,String> params = new HashMap<>();
            params.put("access_token",access_token);
            params.put("userId",userIdA);
            params.put("original_id",liveId);
            params.put("original_type","live");
            params.put("re_type","house");
            params.put("re_id",houseIdList_s);
            params.put("page",page+"");
            params.put("pagesize","10");
            JSONObject rs = httpClient.getResponseJson(httpClient.post(publicReDataList, params));
            log.info(rs.toJSONString());
            Reporter.log(rs.toJSONString());
            int status = rs.getIntValue("status");
            String msg = rs.getString("msg");
            Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
            Assert.assertEquals(msg,"获取推广关联信息列表成功","接口返回msg不正确");
            JSONObject data = rs.getJSONObject("data");
            if(data.size()>0){
                JSONObject pager = data.getJSONObject("pager");
                JSONArray list = data.getJSONArray("list");
                if(list.size()>0){
                    JSONObject listObj;
                    for(int i =0;i<list.size();i++){
                        listObj = list.getJSONObject(i);
                        Reporter.log("判断商品图片可用性");
                        String image = listObj.getString("image");
                        int statusCode = httpClient.getStatusCode(httpClient.get(image, new HashMap<>()));
                        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"商品图片显示异常");
                        Reporter.log("判断商品标题显示正常");
                        String title = listObj.getString("title");
                        Assert.assertEquals(title.equals(""),false,"商品标题不能为空");
                        Reporter.log("判断价格显示正常");
                        String price_text = listObj.getString("price_text");
                        Assert.assertEquals(price_text.equals(""),false,"价格不应为空");
                        Reporter.log("判断详情链接可用性");
                        String mobile_url = listObj.getString("mobile_url");
                        int statusCode1 = httpClient.getStatusCode(httpClient.get(mobile_url, new HashMap<>()));
                        Assert.assertEquals(statusCode1,Constants.RESPNSE_STATUS_CODE_200,"商品详情链接异常");

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
     * 设置讲解
     */
    @Test(dependsOnMethods = { "publicReDataList" })
    public void liveReShow() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("live_id",liveId);
        params.put("re_show_id",houseIdList.get(0));
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveReShow, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"设置为讲解成功","接口返回msg不正确");
        liveDetail();
        Assert.assertEquals(re_show_id.equals(houseIdList.get(0)),true,"设置讲解失败");
    }



    /**
     * 取消讲解
     */
    @Test(dependsOnMethods = { "liveReShow" })
    public void liveReHide() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("live_id",liveId);
        params.put("re_show_id",houseIdList.get(0));
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveReHide, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"取消讲解成功","接口返回msg不正确");
        liveDetail();
        Assert.assertEquals(re_show_id.equals("0"),true,"取消讲解失败");
    }



    /**
     * 编辑推广商品
     */
    @Test(dependsOnMethods = { "liveCreate" })
    public void publicReDataEdit() throws IOException {

        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("original_id",liveId);
        params.put("original_type","live");
        params.put("re_type","house");
        params.put("re_id",houseIdList_s);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(publicReDataEdit, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1);
        Assert.assertEquals(msg,"编辑推广关联信息成功","接口返回msg不正确");
    }

    /**
     * 用户加入直播间
     */
    @Test(dependsOnMethods = { "liveCreate"})
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
    }

    /**
     * 主播封禁用户
     */
    @Test(dependsOnMethods = { "liveJoin"})
    public void liveBan() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("live_id",liveId);
        params.put("user_id",userIdB);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveBan, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"禁言用户成功");
        liveDetail();
        Assert.assertEquals(is_ban.equals("1"),true,"直播间详情，禁言用户失败");
        liveBanUser();
        Assert.assertEquals(isin,true,"禁言用户列表添加失败");
        isin = false;
    }

    /**
     * 解除禁言
     */
    @Test(dependsOnMethods = { "liveBan"})
    public void liveUnBan() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("live_id",liveId);
        params.put("user_id",userIdB);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveUnBan, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"取消禁言成功");
        liveDetail();
        Assert.assertEquals(is_ban.equals("0"),true,"直播间详情，取消禁言用户失败");
        liveBanUser();
        Assert.assertEquals(isin,false,"禁言用户列表移除失败");
        isin = false;

    }

    /**
     * 设置管理员
     */
    @Test(dependsOnMethods = { "liveJoin"})
    public void liveManageAdd() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("live_id",liveId);
        params.put("user_id",userIdB);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveManageAdd, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"添加成功");
        liveDetail();
        Assert.assertEquals(live_role.equals("manage"),true,"直播间详情，设置管理员失败");
        Assert.assertEquals(is_manage.equals("1"),true,"直播间详情，设置管理员失败");
        liveManageUser();
        Assert.assertEquals(isin,true,"设置管理员，管理员列表添加失败");
        isin = false;

    }

    /**
     * 撤销管理员
     */
    @Test(dependsOnMethods = { "liveManageAdd"})
    public void liveManageDel() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("live_id",liveId);
        params.put("user_id",userIdB);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveManageDel, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"移除成功");
        liveDetail();
        Assert.assertEquals(live_role.equals("manage"),false,"直播间详情，移除管理员失败");
        Assert.assertEquals(is_manage.equals("0"),true,"直播间详情，移除管理员失败");
        liveManageUser();
        Assert.assertEquals(isin,false,"设置管理员，管理员列表移除失败");
        isin = false;
    }

    /**
     * 踢出直播间
     */
    @Test(dependsOnMethods = { "liveJoin"})
    public void liveBlock() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("live_id",liveId);
        params.put("user_id",userIdB);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveBlock, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"禁封用户成功");
        liveDetail();
        Assert.assertEquals(is_block.equals("1"),true,"直播间详情，踢出直播间失败");
        liveBlockUser();
        Assert.assertEquals(isin,true,"封禁用户，封禁列表添加失败");
        isin = false;
        liveStatus();
        Assert.assertEquals(data_msg,"抱歉，您无法进入当前直播间","封禁状态不正确");
    }




    /**
     * 取消封禁
     */
    @Test(dependsOnMethods = { "liveBlock"})
    public void liveUnBlock() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("live_id",liveId);
        params.put("user_id",userIdB);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveUnBlock, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"取消禁封成功");
        liveDetail();
        Assert.assertEquals(is_block.equals("0"),true,"直播间详情，踢出直播间失败");
        liveBlockUser();
        Assert.assertEquals(isin,false,"封禁用户，封禁列表添加失败");
        isin = false;
        liveStatus();
        Assert.assertEquals(data_msg,"","封禁状态不正确");
    }

    /**
     * 直播详情，判断参数
     */
    private void liveDetail() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",new_access_token);
        params.put("live_id",liveId);
        params.put("auto","1");
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveDetail, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取直播信息成功","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        re_show_id = data.getString("re_show_id");
        is_ban = data.getString("is_ban");
        live_role = data.getString("live_role");
        is_manage = data.getString("is_manage");
        is_block = data.getString("is_block");

    }

    /**
     * 判断封禁
     */
    private void liveStatus() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",new_access_token);
        params.put("live_id",liveId);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveStatus, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取直播状态信息成功","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        data_msg = data.getString("msg");

    }

    /**
     * 封禁列表
     */
    private void liveBlockUser() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("live_id",liveId);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveBlockUser, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取拉黑用户列表成功","接口返回msg不正确");
        JSONArray data = rs.getJSONArray("data");
        if(data.size()>0){
            JSONObject dataObj;
            for(int i = 0 ;i<data.size();i++){
                dataObj = data.getJSONObject(i);
                String id = dataObj.getString("id");
                if(id.equals(userIdB)){
                    isin = true;
                    return;
                }
            }
        }
        isin = false;
    }

    /**
     * 禁言用户列表
     */
    private void liveBanUser() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("live_id",liveId);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveBanUser, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取禁言用户列表成功","接口返回msg不正确");
        JSONArray data = rs.getJSONArray("data");
        if(data.size()>0){
            JSONObject dataObj;
            for(int i = 0 ;i<data.size();i++){
                dataObj = data.getJSONObject(i);
                String id = dataObj.getString("id");
                if(id.equals(userIdB)){
                    isin = true;
                    return;
                }
            }
        }
        isin = false;
    }

    /**
     * 管理员列表
     */
    private void liveManageUser() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("live_id",liveId);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveManageUser, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取管理员用户列表成功","接口返回msg不正确");
        JSONArray data = rs.getJSONArray("data");
        if(data.size()>0){
            JSONObject dataObj;
            for(int i = 0 ;i<data.size();i++){
                dataObj = data.getJSONObject(i);
                String id = dataObj.getString("user_id");
                if(id.equals(userIdB)){
                    isin = true;
                    return;
                }
            }
        }
        isin = false;
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
