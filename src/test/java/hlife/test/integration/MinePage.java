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
 * 个人主页相关
 */
@Test(groups = "MinePage")
public class MinePage extends BaseApi {

    private HttpClient httpClient = new HttpClient();
    private static Logger log = LoggerFactory.getLogger(MinePage.class);
    private String sex;
    private List<String> menu_type_list = new ArrayList<>();
    private String house_id;
    private String nick_name;
    private String phone;
    private String date = getDate(432000000);

    /**
     * 生活服务页
     */
    @Test
    public void myServiceHome() throws IOException {
        HashMap<String, String> header = new HashMap<>();
        header.put("apptype","hilife");
        JSONObject rs = httpClient.getResponseJson(httpClient.post(myServiceHome, new HashMap<>(), header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_1, "接口请求失败");
        Assert.assertEquals(msg, "获取用户信息成功", "接口返回msg失败");
        JSONObject data = rs.getJSONObject("data");
        Reporter.log("判断用户头像可用性");
        String avatar = data.getString("avatar");
        int statusCode = httpClient.getStatusCode(httpClient.get(avatar, new HashMap<>()));
        Assert.assertEquals(statusCode, Constants.RESPNSE_STATUS_CODE_200, "用户头像显示异常");
        Reporter.log("判断活动");
        JSONArray activity = data.getJSONArray("activity");
        if (activity.size() > 0) {
            JSONObject activity_obj;
            for (int i = 1; i < activity.size(); i++) {
                activity_obj = activity.getJSONObject(i);
                Reporter.log("判断活动名称");
                String activity_name = activity_obj.getString("activity_name");
                Assert.assertEquals(activity_name.equals(""), false, "");
                Reporter.log("判断活动链接可否访问");
                String activity_url = activity_obj.getString("activity_url");
                int statusCode1 = httpClient.getStatusCode(httpClient.get(activity_url, new HashMap<>()));
                Assert.assertEquals(statusCode1, Constants.RESPNSE_STATUS_CODE_200, "活动链接无法访问");
            }
        }
        Reporter.log("判断健康频道");
        JSONObject health_channel = data.getJSONObject("health_channel");
        if (health_channel.size() > 0) {
            String menu_name1 = health_channel.getString("menu_name");
            Assert.assertEquals(menu_name1.equals("健康频道"), true, "健康频道名称不正确");
            JSONArray sub_list = health_channel.getJSONArray("sub_list");
            JSONObject sub_list_obj;
            for (int i = 0; i < sub_list.size(); i++) {
                sub_list_obj = sub_list.getJSONObject(i);
                Reporter.log("判断子分类名称不为空");
                String menu_name = sub_list_obj.getString("menu_name");
                Assert.assertEquals(menu_name.equals(""), false, "");
                Reporter.log("子分类图片可用性");
                String menu_icon = sub_list_obj.getString("menu_icon");
                int statusCode1 = httpClient.getStatusCode(httpClient.get(menu_icon, new HashMap<>()));
                Assert.assertEquals(statusCode1, Constants.RESPNSE_STATUS_CODE_200, "子分类图片显示异常：" + menu_name);
            }
        }
        Reporter.log("判断房产频道");
        JSONObject house_channel = data.getJSONObject("house_channel");
        if (house_channel.size() > 0) {
            String menu_name1 = house_channel.getString("menu_name");
            Assert.assertEquals(menu_name1.equals("房产频道"), true, "健康频道名称不正确");
            JSONArray sub_list = house_channel.getJSONArray("sub_list");
            JSONObject sub_list_obj;
            for (int i = 0; i < sub_list.size(); i++) {
                sub_list_obj = sub_list.getJSONObject(i);
                Reporter.log("判断子分类名称不为空");
                String menu_name = sub_list_obj.getString("menu_name");
                Assert.assertEquals(menu_name.equals(""), false, "");
                Reporter.log("子分类图片可用性");
                String menu_icon = sub_list_obj.getString("menu_icon");
                int statusCode1 = httpClient.getStatusCode(httpClient.get(menu_icon, new HashMap<>()));
                Assert.assertEquals(statusCode1, Constants.RESPNSE_STATUS_CODE_200, "子分类图片显示异常：" + menu_name);
            }
        }
        Reporter.log("判断医药械频道");
        JSONObject medical_channel = data.getJSONObject("medical_channel");
        if (medical_channel.size() > 0) {
            String menu_name1 = medical_channel.getString("menu_name");
            Assert.assertEquals(menu_name1.equals("医药械频道"), true, "健康频道名称不正确");
            JSONArray sub_list = medical_channel.getJSONArray("sub_list");
            JSONObject sub_list_obj;
            for (int i = 0; i < sub_list.size(); i++) {
                sub_list_obj = sub_list.getJSONObject(i);
                Reporter.log("判断子分类名称不为空");
                String menu_name = sub_list_obj.getString("menu_name");
                Assert.assertEquals(menu_name.equals(""), false, "");
                Reporter.log("子分类图片可用性");
                String menu_icon = sub_list_obj.getString("menu_icon");
                int statusCode1 = httpClient.getStatusCode(httpClient.get(menu_icon, new HashMap<>()));
                Assert.assertEquals(statusCode1, Constants.RESPNSE_STATUS_CODE_200, "子分类图片显示异常：" + menu_name);
            }
        }
//        Reporter.log("判断旅游频道");
//        JSONObject travel_channel = data.getJSONObject("travel_channel");
//        if (travel_channel.size() > 0) {
//            String menu_name1 = travel_channel.getString("menu_name");
//            Assert.assertEquals(menu_name1.equals("旅游频道"), true, "健康频道名称不正确");
//            JSONArray sub_list = travel_channel.getJSONArray("sub_list");
//            JSONObject sub_list_obj;
//            for (int i = 0; i < sub_list.size(); i++) {
//                sub_list_obj = sub_list.getJSONObject(i);
//                Reporter.log("判断子分类名称不为空");
//                String menu_name = sub_list_obj.getString("menu_name");
//                Assert.assertEquals(menu_name.equals(""), false, "");
//                Reporter.log("子分类图片可用性");
//                String menu_icon = sub_list_obj.getString("menu_icon");
//                int statusCode1 = httpClient.getStatusCode(httpClient.get(menu_icon, new HashMap<>()));
//                Assert.assertEquals(statusCode1, Constants.RESPNSE_STATUS_CODE_200, "子分类图片显示异常：" + menu_name);
//            }
//        }
        Reporter.log("判断商城频道");
        JSONObject mall_channel = data.getJSONObject("mall_channel");
        if (mall_channel.size() > 0) {
            String menu_name1 = mall_channel.getString("menu_name");
            Assert.assertEquals(menu_name1.equals("商城频道"), true, "健康频道名称不正确");
            JSONArray sub_list = mall_channel.getJSONArray("sub_list");
            JSONObject sub_list_obj;
            for (int i = 0; i < sub_list.size(); i++) {
                sub_list_obj = sub_list.getJSONObject(i);
                Reporter.log("判断子分类名称不为空");
                String menu_name = sub_list_obj.getString("menu_name");
                Assert.assertEquals(menu_name.equals(""), false, "");
                Reporter.log("子分类图片可用性");
                String menu_icon = sub_list_obj.getString("menu_icon");
                int statusCode1 = httpClient.getStatusCode(httpClient.get(menu_icon, new HashMap<>()));
                Assert.assertEquals(statusCode1, Constants.RESPNSE_STATUS_CODE_200, "子分类图片显示异常：" + menu_name);
            }
        }
    }

    /**
     * 用户主页信息
     */
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
        Reporter.log("封装性别");
        sex = data.getString("sex");
        Reporter.log("判断用户头像可用性");
        String avatar = data.getString("avatar");
        int statusCode = httpClient.getStatusCode(httpClient.get(avatar, new HashMap<>()));
        Assert.assertEquals(statusCode, Constants.RESPNSE_STATUS_CODE_200, "用户头像图片显示异常");
        Reporter.log("判断背景图可用性");
        String bg_pic = data.getString("bg_pic");
        statusCode = httpClient.getStatusCode(httpClient.get(bg_pic, new HashMap<>()));
        Assert.assertEquals(statusCode, Constants.RESPNSE_STATUS_CODE_200, "背景图图片显示异常");
        Reporter.log("判断用户昵称可显示");
        String nick_name = data.getString("nick_name");
        Assert.assertEquals(nick_name.equals(""), false, "用户昵称不能为空");
        Reporter.log("判断账号可显示");
        String username = data.getString("username");
        Assert.assertEquals(username.equals(""), false, "账号不能为空");
        Reporter.log("判断关注数是否正确");
        String follow_count = data.getString("follow_count");
        String count = followList(userIdA, "follow");
        Assert.assertEquals(follow_count,count,"关注数与关注列表总数不一致");
        loading();
        Reporter.log("判断粉丝数是否正确");
        String fans_count = data.getString("fans_count");
        count = followList(userIdA, "fans");
        Assert.assertEquals(fans_count,count,"粉丝数与关注列表总数不一致");
        loading();
        Reporter.log("封装列表模块");
        JSONArray menu_list = data.getJSONArray("menu_list");
        if(menu_list.size()>0){
            JSONObject menu_list_obj;
            for(int i = 0 ; i <menu_list.size();i++ ){
                menu_list_obj = menu_list.getJSONObject(i);
                String menu_type = menu_list_obj.getString("menu_type");
                menu_type_list.add(menu_type);
            }
        }
    }

    /**
     * 修改用户信息
     */
    @Test(dependsOnMethods = { "friendHome"})
    public void myModifymyinfo() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        if(sex.equals(1)){
            sex ="2";
            params.put("sex","2");

        }else {
            sex = "1";
            params.put("sex","1");
        }
        JSONObject rs = httpClient.getResponseJson(httpClient.post(myModifymyinfo, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求成功");
        JSONObject data = rs.getJSONObject("data");
        Assert.assertEquals(data.size()>0,true,"接口请求失败");

        Reporter.log("请求个人主页接口，判断信息是否修改成功");
        params.clear();
        params.put("access_token",access_token);
        params.put("user_id", userIdA);
        rs = httpClient.getResponseJson(httpClient.post(friendHome, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_1, "接口请求失败");
        Assert.assertEquals(msg, "获取用户信息成功", "接口返回msg不正确");
        data = rs.getJSONObject("data");
        Reporter.log("封装性别");
        String sex1 = data.getString("sex");
        Assert.assertEquals(sex1,sex,"修改信息成功");
    }

    /**
     * 个人主页栏目统一接口
     * 菜单类型 blog动态 video小视频 news文章资讯 course培训课程
     * mall商品橱窗 organization主页机构 activity活动（弃用） live直播 house房产
     */
    @Test
    public void friendMenuInfo() throws IOException {
        Reporter.log("check动态接口");
        HashMap<String, String> blog = getParams("blog");
        JSONObject rs = httpClient.getResponseJson(httpClient.post(friendMenuInfo, blog));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取动态成功","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        if(data.size()>0){
            JSONArray list = data.getJSONArray("list");
            if(list.size()>0){
                JSONObject list_obj;
                for(int i =0;i<list.size();i++){
                    list_obj = list.getJSONObject(i);
                    Reporter.log("判断封面图");
                    String union_pic = list_obj.getString("union_pic");
                    int statusCode = httpClient.getStatusCode(httpClient.get(union_pic, new HashMap<>()));
                    Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"动态封面图显示异常");
                    JSONArray pics = list_obj.getJSONArray("pics");
                    if(pics.size()>0){
                        JSONObject pice_obj;
                        for(int j=0;j<pics.size();j++){
                            pice_obj = pics.getJSONObject(i);
                            String url = pice_obj.getString("url");
                            int statusCode1 = httpClient.getStatusCode(httpClient.get(url, new HashMap<>()));
                            Assert.assertEquals(statusCode1,Constants.RESPNSE_STATUS_CODE_200,"动态图片显示异常");
                        }
                    }
                }
            }
        }

        Reporter.log("check小视频接口");
        HashMap<String, String> video = getParams("video");
        video.put("page","1");
        video.put("pagesize","100");
        rs = httpClient.getResponseJson(httpClient.post(friendMenuInfo, video));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取小视频列表成功","接口返回msg不正确");
        data = rs.getJSONObject("data");
        if(data.size()>0){
            JSONArray list = data.getJSONArray("list");
            if(list.size()>0){
                JSONObject list_obj;
                for(int i = 0 ; i< list.size();i++){
                    list_obj = list.getJSONObject(i);
                    String play_nums = list_obj.getString("play_nums");
                    Assert.assertEquals(play_nums.equals(""),false,"点播数不应为空");
                    JSONObject video1 = list_obj.getJSONObject("video");
                    String cover = video1.getString("cover");
                    int statusCode = httpClient.getStatusCode(httpClient.get(cover, new HashMap<>()));
                    Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"视频封面图显示异常");
                    String download = video1.getString("download");
                    statusCode = httpClient.getStatusCode(httpClient.get(download, new HashMap<>()));
                    Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"视频播放异常");
                    JSONObject music = list_obj.getJSONObject("music");
                    String music1 = music.getString("music");
                    statusCode = httpClient.getStatusCode(httpClient.get(music1, new HashMap<>()));
                    Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"音乐播放失败");
                    String thumb = music.getString("thumb");
                    statusCode = httpClient.getStatusCode(httpClient.get(thumb, new HashMap<>()));
                    Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"音乐图片无法显示");
                }
            }
        }

        Reporter.log("check直播列表");
        HashMap<String, String> live = getParams("live");
        live.put("page","1");
        live.put("pagesize","100");
        rs = httpClient.getResponseJson(httpClient.post(friendMenuInfo, live));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取直播列表成功","接口返回msg不正确");
        data = rs.getJSONObject("data");
        if(data.size()>0){
            JSONArray list = data.getJSONArray("list");
            if(list.size()>0){
                JSONObject list_obj;
                for(int i = 0;i<list.size();i++){
                    list_obj = list.getJSONObject(i);
                    Reporter.log("查看封面图");
                    JSONObject live_cover = list_obj.getJSONObject("live_cover");
                    String url = live_cover.getString("url");
                    int statusCode = httpClient.getStatusCode(httpClient.get(url, new HashMap<>()));
                    Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"直播封面图，无法显示");
                    Reporter.log("点播数不能为空");
                    String play_count = list_obj.getString("play_count");
                    Assert.assertEquals(play_count.equals(""),false,"点播数不能为空"+i);
                    Reporter.log("判断时长不为空");
                    String video_duration = list_obj.getString("video_duration");
                    Assert.assertEquals(video_duration.equals(""),false,"时长不能为空"+i);
                    Reporter.log("判断直播标题可显示");
                    String live_title = list_obj.getString("live_title");
                    Assert.assertEquals(live_title.equals(""),false,"直播标题不能为空");
                    Reporter.log("判断创建日期可以显示");
                    String created_at = list_obj.getString("created_at");
                    Assert.assertEquals(created_at.equals(""),false,"创建日期不能为空");
                }
            }
        }

        Reporter.log("check商品列表");
        HashMap<String, String> mall = getParams("mall");
        mall.put("page","1");
        mall.put("pagesize","100");
        rs = httpClient.getResponseJson(httpClient.post(friendMenuInfo, mall));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取商品列表成功","接口返回msg不正确");
        data = rs.getJSONObject("data");
        if(data.size()>0){
            JSONArray list = data.getJSONArray("list");
            if(list.size()>0){
                JSONObject list_obj;
                for(int i = 0;i<list.size();i++){
                    list_obj = list.getJSONObject(i);
                    Reporter.log("判断封面图");
                    String image = list_obj.getString("image");
                    int statusCode = httpClient.getStatusCode(httpClient.get(image, new HashMap<>()));
                    Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"封面图不能为空");
                    Reporter.log("判断标题正常显示");
                    String title = list_obj.getString("title");
                    Assert.assertEquals(title.equals(""),false,"标题不能为空");
                    Reporter.log("判断价格");
                    String price_text = list_obj.getString("price_text");
                    Assert.assertEquals(price_text.equals(""),false,"价格不能为空");
                }
            }
        }
    }

    /**
     * 个人主页咨询列表
     */
    @Test
    public void newsList() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("type","my");
        JSONObject rs = httpClient.getResponseJson(httpClient.post(newsList, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取成功","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        if(data.size()>0){
            JSONArray list = data.getJSONArray("list");
            if(list.size()>0){
                JSONObject list_obj;
                for(int i =0;i<list.size();i++){
                    list_obj = list.getJSONObject(i);
                    Reporter.log("判断标题可以显示");
                    String title = list_obj.getString("title");
                    Assert.assertEquals(title.equals(""),false,"标题不能为空"+i);
                    Reporter.log("判断封面图可以显示");
                    JSONArray cover = list_obj.getJSONArray("cover");
                    for(int j = 0 ;j<cover.size();j++){
                        String s = cover.getString(j);
                        int statusCode = httpClient.getStatusCode(httpClient.get(s, new HashMap<>()));
                        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"咨询图片显示异常");
                    }
                    Reporter.log("判断点赞数正常显示");
                    String num_praise = list_obj.getString("num_praise");
                    Assert.assertEquals(num_praise.equals(""),false,"点赞数不能为空");
                    Reporter.log("判断用户昵称正常显示");
                    JSONObject user_info = list_obj.getJSONObject("user_info");
                    String nick_name = user_info.getString("nick_name");
                    Assert.assertEquals(nick_name.equals(""),false,"用户昵称不能为空");

                }
            }
        }



    }

    private HashMap<String,String> getParams(String menu_type){
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("user_id",userIdA);
        params.put("menu_type",menu_type);
        return params;
    }

    /**
     * 个人主页侧边栏
     */
    @Test
    public void myHome() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(myHome, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取用户信息成功","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        Reporter.log("判断名片可用性");
        String qrcode_url = data.getString("qrcode_url");
        int statusCode = httpClient.getStatusCode(httpClient.get(qrcode_url, new HashMap<>()));
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"名片访问异常");
        Reporter.log("判断购物车");
        String shoppingCartAddress = data.getString("shoppingCartAddress");
        statusCode = httpClient.getStatusCode(httpClient.get(shoppingCartAddress, new HashMap<>()));
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"购物车访问异常");
    }

    /**
     * 会员攻略
     */
    @Test
    public void distributeUpgradeStrategy() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(distributeUpgradeStrategy, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取会员升级攻略成功","接口返回msg不正确");
        JSONArray data = rs.getJSONArray("data");
        Assert.assertEquals(data.size(),3,"返回数目不正确");
    }

    /**
     * 会员信息
     */
    @Test
    public void distributeMemberInfo() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(distributeMemberInfo, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取注册会员信息成功","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        Reporter.log("判断用户昵称可以显示");
        String nick_name = data.getString("nick_name");
        Assert.assertEquals(nick_name.equals(""),false,"用户昵称不应为空");
        Reporter.log("判断用户头像可以显示");
        String avatar = data.getString("avatar");
        int statusCode = httpClient.getStatusCode(httpClient.get(avatar, new HashMap<>()));
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"用户头像访问失败");
        Reporter.log("会员级别可以显示");
        String role_name = data.getString("role_name");
        Assert.assertEquals(role_name.equals(""),false,"会员级别不能为空");
        Reporter.log("判断我的会员");
        JSONArray member = data.getJSONArray("member");
        JSONObject sub_register = member.getJSONObject(0);
        String menu_name = sub_register.getString("menu_name");
        Assert.assertEquals(menu_name,"我的注册会员","我的会员返回文案不正确：0");
        JSONObject sub_super = member.getJSONObject(1);
        menu_name = sub_super.getString("menu_name");
        Assert.assertEquals(menu_name,"我的超级会员","我的会员返回文案不正确：1");
        JSONObject team_super = member.getJSONObject(2);
        menu_name = team_super.getString("menu_name");
        Assert.assertEquals(menu_name,"团队超级会员","我的会员返回文案不正确：2");
        JSONObject team_all = member.getJSONObject(3);
        menu_name = team_all.getString("menu_name");
        Assert.assertEquals(menu_name,"团队全部会员","我的会员返回文案不正确：3");
        Reporter.log("升级合伙人赚更多佣金！");
        String upgrade_tip = data.getString("upgrade_tip");
        Assert.assertEquals(upgrade_tip,"升级合伙人赚更多佣金！","会员说明入口文案不正确");
        Reporter.log("预估收益区域");
        JSONArray profit = data.getJSONArray("profit");
        JSONObject this_distribute = profit.getJSONObject(0);
        menu_name = this_distribute.getString("menu_name");
        Assert.assertEquals(menu_name,"今日推广收益","预估收益返回文案不正确：0");
        JSONObject this_platform = profit.getJSONObject(1);
        menu_name = this_platform.getString("menu_name");
        Assert.assertEquals(menu_name,"今日平台奖励","预估收益返回文案不正确：1");
        JSONObject this_rebate = profit.getJSONObject(2);
        menu_name = this_rebate.getString("menu_name");
        Assert.assertEquals(menu_name,"今日返利收益","预估收益返回文案不正确：2");
        JSONObject this_total = profit.getJSONObject(3);
        menu_name = this_total.getString("menu_name");
        Assert.assertEquals(menu_name,"今日全部收益","预估收益返回文案不正确：3");
        Reporter.log("订单区域");
        JSONArray order = data.getJSONArray("order");
        JSONObject this_buy = order.getJSONObject(0);
        menu_name = this_buy.getString("menu_name");
        Assert.assertEquals(menu_name,"今日购买订单","预估收益返回文案不正确：0");
        JSONObject this_share = order.getJSONObject(1);
        menu_name = this_share.getString("menu_name");
        Assert.assertEquals(menu_name,"今日推广订单","预估收益返回文案不正确：1");
        Reporter.log("特别说明：按房产相应规则可直接升级合伙人");
        String special_tip = data.getString("special_tip");
        Assert.assertEquals(special_tip,"特别说明：按房产相应规则可直接升级合伙人","底部文案不正确");
    }

    /**
     * 我的联系人
     */
    @Test
    public void myContacts() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(myContacts, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取我的联系人成功","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        if(data.size()>0){
            JSONArray list = data.getJSONArray("list");
            if(list.size()>0){
                JSONObject list_obj;
                for(int i = 0 ;i< list.size();i++){
                    list_obj = list.getJSONObject(i);
                    Reporter.log("判断用户名");
                    String username = list_obj.getString("username");
                    Assert.assertEquals(username.equals(""),false,"用户名不能为空");
                    Reporter.log("判断昵称");
                    String nick_name = list_obj.getString("nick_name");
                    Assert.assertEquals(nick_name.equals(""),false,"昵称不能为空");
                    Reporter.log("用户头像正常显示");
                    String avatar = list_obj.getString("avatar");
                    int statusCode = httpClient.getStatusCode(httpClient.get(avatar, new HashMap<>()));
                    Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"用户头像显示失败");
                }
            }
        }
    }


    /**
     * 可报备楼盘
     */
    @Test
    public void houseStoreShareList() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(houseStoreShareList, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取可报备楼盘列表","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        if(data.size()>0){
            JSONArray list = data.getJSONArray("list");
            if(list.size()>0){
                house_id = list.getJSONObject(0).getString("id");
            }
        }
    }

    /**
     * 获取下级信息
     */
    @Test(dependsOnMethods = { "houseStoreShareList"})
    public void houseSubMembers() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(houseSubMembers, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取经纪人下级会员成功","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        JSONObject jsonObject = list.getJSONObject(0);
        nick_name = jsonObject.getString("nick_name");
        phone = jsonObject.getString("phone");
    }

    /**
     * 房产报备
     */
    @Test(dependsOnMethods = { "houseSubMembers"})
    public void houseReport() throws IOException, InterruptedException {
        String currentTime= System.currentTimeMillis()+"";
        String substring = currentTime.substring(currentTime.length() - 8);
        JSONObject user = new JSONObject();
        user.put("phone","176"+substring);
        user.put("true_name","哈哈哈");
        String userlist = "["+user.toJSONString()+"]";
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("user_list",userlist);
        params.put("house_id",house_id);
        params.put("house_time",date);
        params.put("user_permission","house_sales");
        params.put("arrive_name","111");
        params.put("arrive_phone","17612345678");
        params.put("remarks","JJ5了快");
        JSONObject rs = httpClient.getResponseJson(httpClient.post(houseReport, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"报备信息已提交成功","接口返回msg不正确");
        loading();
        houseReportList();
    }

    /**
     * 房产我的客户列表
     */
    private void houseReportList() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("type","sales");
        JSONObject rs = httpClient.getResponseJson(httpClient.post(houseReportList, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取房产报备列表成功","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        JSONObject list_obj;
        boolean isin =false;
        for(int i =0;i<list.size();i++){
            list_obj = list.getJSONObject(i);
            String house_time = list_obj.getString("house_time");
            System.out.println(date);
            if(house_time.equals(date)){
                System.out.println("------------");
                isin = true;
            }
        }

        Assert.assertEquals(isin,true,"报备添加失败");
    }

    /**
     * 粉丝/关注列表
     * @param type follow关注 fans粉丝
     * @return 总数量
     */
    private String followList(String userId,String type) throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("user_id",userId);
        params.put("type",type);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(followList, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取成功","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        JSONObject pager = data.getJSONObject("pager");
        String count = pager.getString("count");
        Assert.assertEquals(count.equals(""),false,"总数不应为空");
        return count;
    }
}
