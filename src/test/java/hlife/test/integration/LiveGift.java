package hlife.test.integration;

import com.alibaba.fastjson.JSONArray;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 直播间礼物相关
 */
@Test
public class LiveGift extends BaseApi{
    private HttpClient httpClient = new HttpClient();
    private static Logger log = LoggerFactory.getLogger(LiveGift.class);
    LiveRoom liveRoom = new LiveRoom();
    private int gift_pop;
    private String uuid = "00000000-258c-468d-ffff-ffffef05ac4a";
    private long copperBalance;
    private List<String> giftIdList = new ArrayList<>();
    private List<String> giftPriceList = new ArrayList<>();

    /**
     * 前置条件
     */
    @Test
    public void init() throws IOException {
        //获取分类
        liveRoom.liveCategoryList();
        //创建直播间
        liveRoom.liveCreate();
    }

    /**
     * 详情获取gift_pop字段
     */
    @Test(dependsOnMethods = { "init"})
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
        params.put("live_id",liveRoom.liveId);
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
        params.put("live_id",liveRoom.liveId);
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
        params.put("live_id",liveRoom.liveId);
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

        init();
        Assert.assertEquals(gift_pop,1,"直播礼物弹窗信息失败");
    }

    /**
     * 礼物列表
     */
    @Test(dependsOnMethods = { "init"})
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
            params.put("live_id",liveRoom.liveId);
            rs = httpClient.getResponseJson(httpClient.post(liveGiftSend, params));log.info(rs.toJSONString());
            Reporter.log(rs.toJSONString());
            status = rs.getIntValue("status");
            msg = rs.getString("msg");
            Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
            Assert.assertEquals(msg,"主播不能给自己赠送礼物","接口返回msg不正确");

            Reporter.log("直播礼物id不存在");
            params.clear();
            params.put("access_token",new_access_token);
            params.put("live_id",liveRoom.liveId);
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
            long copperBalance_after = data.getLongValue("copperBalance");
            Assert.assertEquals(copperBalance_after,copperBalance-1,"送出礼物后铜币数不正确");
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
        }
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
                        Assert.assertEquals(copper_price,"1","铜币数不正确");
                        Assert.assertEquals(cash_price,"0.10","现金不正确");
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

            params.clear();
            params.put("access_token",access_token);
            params.put("oprea_type","end");
            params.put("live_id",liveRoom.liveId);
            rs = httpClient.getResponseJson(httpClient.post(liveOpera, params));
            log.info(rs.toJSONString());
            Reporter.log(rs.toJSONString());
            status = rs.getIntValue("status");
            msg = rs.getString("msg");
            Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
            Assert.assertEquals(msg,"更新直播状态成功","接口返回msg不正确");

            Reporter.log("通过判断能否进入直播间接口确认是否关播成功");
            HashMap<String, String> header = URLFiltration.addHeader(new HashMap<>());
            header.put("uuid",uuid);
            params.clear();
            params.put("access_token",access_token);
            params.put("live_id",liveRoom.liveId);
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

    /**
     * 打赏榜
     */
    @Test(dependsOnMethods = { "liveGiftSend"})
    public void liveGiftUser() throws IOException, InterruptedException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("live_id",liveRoom.liveId);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(liveGiftUser, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_200,"接口请求异常");
        Assert.assertEquals(msg,"获取直播打赏用户列表成功","接口返回msg不正确");

        JSONObject data = rs.getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        JSONObject listObj;
        String user_id1 = getUser_id("17610733700");
        boolean istrue = false;
        for(int i =0 ; i<list.size();i++){
            listObj= list.getJSONObject(i);
            String user_id = listObj.getString("user_id");
            if(user_id.equals(user_id1)){
                istrue = true;
                String avatar = data.getString("avatar");
                int statusCode = httpClient.getStatusCode(httpClient.post(avatar, new HashMap<>()));
                Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"用户头像显示异常");
                long silver_amount = data.getLongValue("silver_amount");
                Assert.assertEquals(silver_amount,Integer.parseInt(giftPriceList.get(0))*10*0.3,"铜钱换算银票不正确");
            }
        }
        Assert.assertEquals(istrue,true,"列表没有找到打赏的用户信息");

        params.clear();
        params.put("access_token",access_token);
        params.put("oprea_type","end");
        params.put("live_id",liveRoom.liveId);
        rs = httpClient.getResponseJson(httpClient.post(liveOpera, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"更新直播状态成功","接口返回msg不正确");

        Reporter.log("通过判断能否进入直播间接口确认是否关播成功");
        HashMap<String, String> header = URLFiltration.addHeader(new HashMap<>());
        header.put("uuid",uuid);
        params.clear();
        params.put("access_token",access_token);
        params.put("live_id",liveRoom.liveId);
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
