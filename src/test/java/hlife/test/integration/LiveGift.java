package hlife.test.integration;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import hlife.base.BaseApi;
import hlife.base.Constants;
import hlife.filtration.URLFiltration;
import hlife.httpclient.HttpClient;
import org.apache.commons.codec.language.bm.Lang;
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


/**
 * 直播间礼物相关
 */
@Test(groups = "LiveGift")
public class LiveGift extends BaseApi{
    private HttpClient httpClient = new HttpClient();
    private static Logger log = LoggerFactory.getLogger(LiveGift.class);
    public List<String> live_typeList = new ArrayList<>();
    public String liveId;
    private int gift_pop;
    private String uuid = "00000000-258c-468d-ffff-ffffef05ac4a";
    private long copperBalance;
    private List<String> giftIdList = new ArrayList<>();
    private List<String> giftPriceList = new ArrayList<>();
    private int page = 1 ;
    private boolean istrue;

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
     * 详情获取gift_pop字段
     */
    @Test(dependsOnMethods = { "liveCreate"})
    public void liveDetail() throws IOException {
        HashMap<String,String> header = URLFiltration.addHeader(new HashMap<>());
        //header.put("uuid",uuid);
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",new_access_token);
        params.put("auto","1");
        Reporter.log("直播id不能为空");
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveDetail, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
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

        JSONObject data = rs.getJSONObject("data");
        gift_pop = data.getIntValue("gift_pop");

    }

    /**
     * 直播礼物弹窗
     */
    @Test(dependsOnMethods = { "liveDetail"})
    public void liveGiftPopInfo() throws IOException {
        if(gift_pop == 0){
            Reporter.log("未弹出，执行直播礼物弹窗接口");
            HashMap<String,String> params = new HashMap<>();
            params.put("access_token",new_access_token);
            JSONObject rs = httpClient.getResponseJson(httpClient.post(liveGiftPopInfo, params));
            log.info(rs.toJSONString());
            Reporter.log(rs.toJSONString());
            int status = rs.getIntValue("status");
            String msg = rs.getString("msg");
            Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
            Assert.assertEquals(msg,"获取直播礼物弹窗信息成功","接口返回msg不正确");

            Reporter.log("详情页判断是否变更状态");
            liveDetail();
            Assert.assertEquals(gift_pop,1,"直播礼物弹窗信息失败");
        }

    }

    /**
     * 执行关播
     */
    @Test(dependsOnMethods = { "liveGiftPopInfo"})
    public void liveOpera() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("oprea_type","end");
        params.put("live_id",liveId);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveOpera, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"更新直播状态成功","接口返回msg不正确");

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
        JSONObject data = rs.getJSONObject("data");
        int can_join = data.getIntValue("can_join");
        Assert.assertEquals(can_join,Constants.RESPNSE_STATUS_CODE_0,"关闭直播间失败");


    }

    /**
     * 强制关播
     * @throws IOException
     */
    @AfterClass
    public void liveOpera1() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("oprea_type","end");
        params.put("live_id",liveId);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveOpera, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"更新直播状态成功","接口返回msg不正确");

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
        JSONObject data = rs.getJSONObject("data");
        int can_join = data.getIntValue("can_join");
        Assert.assertEquals(can_join,Constants.RESPNSE_STATUS_CODE_0,"关闭直播间失败");


    }

    /**
     * 再次开播确认变更状态
     */
    @Test(dependsOnMethods = { "liveOpera"})
    public void againCreate() throws IOException {
        liveCreate();
        liveDetail();
        Assert.assertEquals(gift_pop,1,"直播礼物弹窗信息失败");

    }

    /**
     * 礼物列表
     */
    @Test(dependsOnMethods = { "againCreate"})
    public void liveGiftList() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",new_access_token);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveGiftList, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取直播礼物列表成功","接口返回");
        JSONObject data = rs.getJSONObject("data");
        Reporter.log("封装用户铜币数");
        copperBalance = data.getLongValue("copperBalance");
        JSONArray list = data.getJSONArray("list");
        JSONObject listObj;
        for(int i = 0;i<list.size();i++){
            listObj = list.getJSONObject(i);
            String gift_name = listObj.getString("gift_name");
            Reporter.log(gift_name);
            Assert.assertEquals(gift_name.equals(""),false,"礼物名称不能为空");
            Reporter.log("礼物图标可用性");
            String gift_icon = listObj.getString("gift_icon");
            int statusCode = httpClient.getStatusCode(httpClient.get(gift_icon, new HashMap<>()));
            Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"礼物图标显示异常");
            String copper_price = listObj.getString("copper_price");
            Assert.assertEquals(copper_price.equals(""),false,"价格不应为空");
            String id = listObj.getString("id");
            Assert.assertEquals(id.equals(""),false,"礼物ID不能为空");
            giftIdList.add(id);
            giftPriceList.add(copper_price);
        }
    }

    /**
     * 赠送礼物
     */
    @Test(dependsOnMethods = { "liveGiftList"})
    public void liveGiftSend() throws IOException, InterruptedException {
        if(copperBalance>=Long.parseLong(giftPriceList.get(0))){
            HashMap<String,String> params = new HashMap<>();
            Reporter.log("请登录");
            JSONObject rs = httpClient.getResponseJson(httpClient.post(liveGiftSend, params));
            log.info(rs.toJSONString());
            Reporter.log(rs.toJSONString());
            int status = rs.getIntValue("status");
            String msg = rs.getString("msg");
            Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_Minus_1,"接口请求失败");
            Assert.assertEquals(msg,"请登录","接口返回msg不正确");

            Reporter.log("直播id不能为空");
            params.put("access_token",access_token);
            rs = httpClient.getResponseJson(httpClient.post(liveGiftSend, params));log.info(rs.toJSONString());
            Reporter.log(rs.toJSONString());
            status = rs.getIntValue("status");
            msg = rs.getString("msg");
            Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
            Assert.assertEquals(msg,"直播id不能为空","接口返回msg不正确");

            Reporter.log("主播不能给自己赠送礼物");
            params.put("live_id",liveId);
            rs = httpClient.getResponseJson(httpClient.post(liveGiftSend, params));log.info(rs.toJSONString());
            Reporter.log(rs.toJSONString());
            status = rs.getIntValue("status");
            msg = rs.getString("msg");
            Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
            Assert.assertEquals(msg,"主播不能给自己赠送礼物","接口返回msg不正确");

            Reporter.log("直播礼物id不存在");
            params.clear();
            params.put("access_token",new_access_token);
            params.put("live_id",liveId);
            rs = httpClient.getResponseJson(httpClient.post(liveGiftSend, params));log.info(rs.toJSONString());
            Reporter.log(rs.toJSONString());
            status = rs.getIntValue("status");
            msg = rs.getString("msg");
            Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
            Assert.assertEquals(msg,"直播礼物id不存在","接口返回msg不正确");

            Reporter.log("给主播送礼物成功");
            params.put("gift_id",giftIdList.get(0));
            rs = httpClient.getResponseJson(httpClient.post(liveGiftSend, params));
            log.info(rs.toJSONString());
            Reporter.log(rs.toJSONString());
            status = rs.getIntValue("status");
            msg = rs.getString("msg");
            Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
            Assert.assertEquals(msg,"给主播送礼物成功","接口返回msg不正确");
            JSONObject data = rs.getJSONObject("data");
            Reporter.log("判断铜币余额是否正确");
            long l = payQueryActBalance();
            long copperBalance_after = data.getLongValue("copperBalance");
            Assert.assertEquals(copperBalance_after,l,"送出礼物后铜币数不正确");
            Reporter.log("判断礼物ID是否一致");
            String gift_id = data.getString("gift_id");
            Assert.assertEquals(gift_id,giftIdList.get(0),"礼物ID不正确");
            Reporter.log("判断礼物数量是否正确");
            int gift_num = data.getIntValue("gift_num");
            Assert.assertEquals(gift_num,1,"礼物数量不正确");
            Reporter.log("判断礼物图标可用性");
            String gift_icon = data.getString("gift_icon");
            int statusCode = httpClient.getStatusCode(httpClient.get(gift_icon, new HashMap<>()));
            Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"礼物图标显示异常");
            liveGiftUser();
            Assert.assertEquals(istrue,true,"打赏榜没有找到用户");
        }
    }

    private long payQueryActBalance() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",new_access_token);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(payQueryActBalance, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取铜币充值金额套餐成功","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        long copperBalance_after = data.getLongValue("copperBalance");
        return copperBalance_after;
    }

    /**
     * 充值页面
     * @throws IOException
     */
    @Test(dependsOnMethods = { "liveGiftList"})
    public void payCopperRechargePackage() throws IOException {
        if(copperBalance<Long.parseLong(giftPriceList.get(0))){
            HashMap<String,String> params = new HashMap<>();
            params.put("access_token",new_access_token);
            JSONObject rs = httpClient.getResponseJson(httpClient.post(payCopperRechargePackage, params));
            log.info(rs.toJSONString());
            Reporter.log(rs.toJSONString());
            int status = rs.getIntValue("status");
            String msg = rs.getString("msg");
            Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
            Assert.assertEquals(msg,"获取铜币充值金额套餐成功","接口返回msg不正确");
            JSONObject data = rs.getJSONObject("data");
            JSONArray list = data.getJSONArray("list");
            JSONObject listObj ;
            for(int i = 0;i<list.size();i++){
                listObj = list.getJSONObject(i);
                String copper_price = listObj.getString("copper_price");
                String cash_price = listObj.getString("cash_price");
                switch(i)
                {
                    case 0 :
                        if(payCopperRechargePackage.contains("api.")){
                            Assert.assertEquals(copper_price,"60","铜币数不正确");
                            Assert.assertEquals(cash_price,"6.00","现金不正确");
                        }else {
                            Assert.assertEquals(copper_price,"1","铜币数不正确");
                            Assert.assertEquals(cash_price,"0.10","现金不正确");
                        }
                        break;
                    case 1 :
                        Assert.assertEquals(copper_price,"120","铜币数不正确");
                        Assert.assertEquals(cash_price,"12.00","现金不正确");
                        break;
                    case 2 :
                        Assert.assertEquals(copper_price,"300","铜币数不正确");
                        Assert.assertEquals(cash_price,"30.00","现金不正确");
                        break;
                    case 3 :
                        Assert.assertEquals(copper_price,"600","铜币数不正确");
                        Assert.assertEquals(cash_price,"60.00","现金不正确");
                        break;
                    case 4 :
                        Assert.assertEquals(copper_price,"1180","铜币数不正确");
                        Assert.assertEquals(cash_price,"118.00","现金不正确");
                        break;
                    case 5 :
                        Assert.assertEquals(copper_price,"5880","铜币数不正确");
                        Assert.assertEquals(cash_price,"588.00","现金不正确");
                        break;
                }
            }
            //执行关播
            Reporter.log("执行关播");
            log.info("执行关播");
            liveOpera();
        }
    }

    /**
     * 打赏榜
     */

    private void liveGiftUser() throws IOException, InterruptedException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("live_id",liveId);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveGiftUser, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求异常");
        Assert.assertEquals(msg,"获取直播打赏用户列表成功","接口返回msg不正确");

        JSONObject data = rs.getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        JSONObject listObj;
        String user_id1 = getUser_id("17610733700");
        istrue = false;
        for(int i =0 ; i<list.size();i++){
            listObj= list.getJSONObject(i);
            String user_id = listObj.getString("user_id");
            if(user_id.equals(user_id1)){
                istrue = true;
                String avatar = listObj.getString("avatar");
                int statusCode = httpClient.getStatusCode(httpClient.post(avatar, new HashMap<>()));
                Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"用户头像显示异常");
                long silver_amount = listObj.getLongValue("silver_amount");
                int j = (int) (Integer.parseInt(giftPriceList.get(0))*10*0.3);
                Assert.assertEquals(silver_amount,j,"铜钱换算银票不正确");
            }
        }

        //执行关播
        Reporter.log("执行关播");
        log.info("执行关播");
        liveOpera();

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
     * 在线用户列表
     */
    @Test(dependsOnMethods = { "liveJoin"})
    public void liveOnlineUser() throws IOException, InterruptedException {
        String user_id = getUser_id("17610733700");
        while (true){
            HashMap<String,String> params = new HashMap<>();
            params.put("access_token",access_token);
            params.put("live_id",liveId);
            params.put("pagesize","20");
            params.put("page",page+"");
            JSONObject rs = httpClient.getResponseJson(httpClient.post(liveOnlineUser, params));
            log.info(rs.toJSONString());
            Reporter.log(rs.toJSONString());
            int status = rs.getIntValue("status");
            String msg = rs.getString("msg");
            Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
            Assert.assertEquals(msg,"获取直播在线用户列表成功","接口返回msg不正确");

            JSONObject data = rs.getJSONObject("data");
            JSONObject pager = data.getJSONObject("pager");
            JSONArray list = data.getJSONArray("list");
            JSONObject listObj;
            boolean isin = false;
            for(int i = 0 ;i<list.size();i++){
                listObj = list.getJSONObject(i);
                String user_id1 = listObj.getString("user_id");
                if(user_id.equals(user_id1)){
                    isin =true;
                }
                Reporter.log("用户头像的可用性");
                String avatar = listObj.getString("avatar");
                int statusCode = httpClient.getStatusCode(httpClient.get(avatar, new HashMap<>()));
                Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"用户头像显示异常");
                Reporter.log("显示昵称");
                String nick_name = listObj.getString("nick_name");
                Assert.assertEquals(nick_name.equals(""),false,"昵称不应为空");
                int is_follow = listObj.getIntValue("is_follow");
                Assert.assertEquals(is_follow>=0,true,"需要显示关注");

            }
            Assert.assertEquals(isin,true,"加入直播间，列表中没有显示对应用户信息");

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
     * 用户离开直播间
     */
    @Test(dependsOnMethods = { "liveOnlineUser"})
    public void liveQuit() throws IOException {

        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",new_access_token);
        params.put("live_id",liveId);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveQuit, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"离开直播间成功","接口返回msg不正确");

    }

    /**
     * 判断是否列表移除
     */
    @Test(dependsOnMethods = { "liveQuit"})
    public void isDel() throws IOException, InterruptedException {
        String user_id = getUser_id("17610733700");
        while (true){
            HashMap<String,String> params = new HashMap<>();
            params.put("access_token",access_token);
            params.put("live_id",liveId);
            params.put("pagesize","20");
            params.put("page",page+"");
            JSONObject rs = httpClient.getResponseJson(httpClient.post(liveOnlineUser, params));
            log.info(rs.toJSONString());
            Reporter.log(rs.toJSONString());
            int status = rs.getIntValue("status");
            String msg = rs.getString("msg");
            Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
            Assert.assertEquals(msg,"获取直播在线用户列表成功","接口返回msg不正确");

            JSONObject data = rs.getJSONObject("data");
            JSONObject pager = data.getJSONObject("pager");
            JSONArray list = data.getJSONArray("list");
            JSONObject listObj;
            boolean isin = false;
            for(int i = 0 ;i<list.size();i++){
                listObj = list.getJSONObject(i);
                String user_id1 = listObj.getString("user_id");
                if(user_id.equals(user_id1)){
                    isin =true;
                }
                Reporter.log("用户头像的可用性");
                String avatar = listObj.getString("avatar");
                int statusCode = httpClient.getStatusCode(httpClient.get(avatar, new HashMap<>()));
                Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"用户头像显示异常");
                Reporter.log("显示昵称");
                String nick_name = listObj.getString("nick_name");
                Assert.assertEquals(nick_name.equals(""),false,"昵称不应为空");
                int is_follow = listObj.getIntValue("is_follow");
                Assert.assertEquals(is_follow>=0,true,"需要显示关注");

            }
            Assert.assertEquals(isin,false,"离开直播间，列表中没有移除对应用户信息");

            int pagesize= Integer.parseInt(pager.getString("pagesize"));
            if (list.size()<pagesize){
                page=1;
                break;
            }else {
                page+=1;
            }
        }
    }





    @Test
    public void remind(){
        Assert.assertEquals(true,false,"每间隔1天，判断gift_pop是否走等于0的逻辑liveGiftPopInfo方法");
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
}
