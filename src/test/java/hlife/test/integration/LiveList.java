package hlife.test.integration;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import hlife.base.BaseApi;
import hlife.base.Constants;
import hlife.filtration.URLFiltration;
import hlife.httpclient.HttpClient;
import hlife.parameters.VerificationParameters;
import hlife.utils.PropertiesUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 直播列表相关
 */
@Test(groups = "LiveList")
public class LiveList extends BaseApi {
    private HttpClient httpClient = new HttpClient();
    private VerificationParameters verParameters = new VerificationParameters();
    private static Logger log = LoggerFactory.getLogger(LiveList.class);
    private List<String> live_typeMap = new ArrayList<>();
    private List<String> liveRecommendIdList = new ArrayList<>();
    private List<String> after_liveRecommendIdList = new ArrayList<>();
    private int page = 1;


    /**
     * 判断是否有正在直播的直播间
     */
    @Test
    public void liveRecommend() throws IOException {

        CloseableHttpResponse post = httpClient.post(liveRecommend, new HashMap<String, String>());
        int statusCode = httpClient.getStatusCode(post);
        JSONObject rs = httpClient.getResponseJson(post);
        log.info(rs.toString());
        Reporter.log(rs.toString());
        Reporter.log("判断code=200\n" +
                "msg=获取直播分类成功\n" +
                "status=1");
        Assert.assertEquals(statusCode, Constants.RESPNSE_STATUS_CODE_200,"判断是否有正在直播的直播间接口请求失败");
        int status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"判断是否有正在直播的直播间接口请求失败");
        String msg = rs.getString("msg");
        Assert.assertEquals(msg,"获取直播推荐成功","判断是否有正在直播的直播间接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        Reporter.log("用例通过");
        Reporter.log("判断data.live_id 不等于空");
        String live_id = data.getString("live_id");
        if (live_id.equals("") || live_id == null){
            Assert.assertEquals(true,false,"live_id不应为空");
        }
        Reporter.log("用例通过");
    }

    /**
     * 获取直播分类（根据apptype不同返回值不同，分类由后台管理端配置）
     */
    @Test
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
     * 直播列表广告列表（adv_type=live）
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
        params.put("adv_type","live");
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
     * 获取直播推荐列表
     */
    public void liveRecommendlist() throws IOException {

        Reporter.log("校验code=200\n" +
                "status=1\n" +
                "msg=获取关注直播列表成功");
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        CloseableHttpResponse post = httpClient.post(liveRecommendlist,params);
        int statusCode = httpClient.getStatusCode(post);
        JSONObject rs = httpClient.getResponseJson(post);
        log.info(rs.toString());
        Reporter.log(rs.toString());
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"获取直播推荐列表接口请求失败");
        int status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"获取直播推荐列表接口请求失败");
        String msg = rs.getString("msg");
        Assert.assertEquals(msg,"获取推荐直播列表成功","获取直播推荐列表接口msg错误");
        Reporter.log("用例通过");
        JSONArray data = rs.getJSONArray("data");
        if(data!=null && data.size()>0){
            JSONObject dataObj;
            liveRecommendIdList.clear();
            for (int i = 0 ;i<data.size();i++){
                dataObj = data.getJSONObject(i);
                String live_cname = dataObj.getString("live_cname");
                Reporter.log("判断分类名称不为空");
                Assert.assertEquals(!live_cname.equals("")&&live_cname!=null,true,"分类名称不应为空");
                Reporter.log("判断每个分类推荐的直播间满足（0,5]条件");
                JSONArray list = dataObj.getJSONArray("list");
                Assert.assertEquals(list.size()>0 && list.size()<=5,true,"分类推荐数量不正确："+list.size());
                Reporter.log("判断分类查询结果正确");
                liveRecommendIdList.add(dataObj.getString("id"));
                System.out.println(liveRecommendIdList.toString());
                for (int j = 0;j<list.size();j++){
                    JSONObject listObj = list.getJSONObject(j);
                    String live_cname1 = listObj.getString("live_cname");
                    Assert.assertEquals(live_cname1,live_cname,"返回的直播间不属于对应分类");
                    Reporter.log("判断热度返回值不为空");
                    String view_count = listObj.getString("view_count");
                    Assert.assertEquals(!view_count.equals("") && view_count != null,true,"热度不应为空view_count");
                    Reporter.log("判断直播标题不为空");
                    String live_title = listObj.getString("live_title");
                    Assert.assertEquals(!live_title.equals("") && live_title != null,true,"直播标题不应为空live_title");
                    Reporter.log("判断直播封面图可以正确显示");
                    String url = listObj.getJSONObject("live_cover").getString("url");
                    int statusCode1 = httpClient.getStatusCode(httpClient.get(url, new HashMap<String, String>()));
                    Assert.assertEquals(statusCode1,Constants.RESPNSE_STATUS_CODE_200,"直播封面图无法显示" + url);
                    Reporter.log("判断用户头像可以正确显示");
                    JSONObject anchor_data = listObj.getJSONObject("anchor_data");
                    String avatar = anchor_data.getString("avatar");
                    int statusCode2 = httpClient.getStatusCode(httpClient.get(avatar, new HashMap<String, String>()));
                    Assert.assertEquals(statusCode2,Constants.RESPNSE_STATUS_CODE_200,"用户头像无法显示"+avatar);
                    Reporter.log("判断用户昵称可以正确显示");
                    String nick_name = anchor_data.getString("nick_name");
                    Assert.assertEquals(!nick_name.equals("") && nick_name != null,true,"用户昵称不应为空nick_name");
                }
            }
        }
    }

    /**
     * 更改直播排序
     */
    @Test(dependsOnMethods = { "liveRecommendlist" })
    public void liveCategorySort() throws IOException {

        Reporter.log("校验access_token为空");
        HashMap<String,String> params = new HashMap<>();
        CloseableHttpResponse post = httpClient.post(liveCategorySort, params);
        int statusCode = httpClient.getStatusCode(post);
        JSONObject rs = httpClient.getResponseJson(post);
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"请求失败");
        int status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_Minus_1,"请求失败");
        String msg = rs.getString("msg");
        Assert.assertEquals(msg,"请登录","返回msg不正确");



        Reporter.log("校验items为空");
        params.clear();
        params.put("access_token",access_token);
        params.put("type","0");
        post = httpClient.post(liveCategorySort, params);
        statusCode = httpClient.getStatusCode(post);
        rs = httpClient.getResponseJson(post);
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"请求失败");
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"请选择项目","返回msg不正确");

        Reporter.log("参数正确，校验是否修改成功");
        //打乱顺序
        Collections.shuffle(liveRecommendIdList);
        System.out.println("----------");
        System.out.println(liveRecommendIdList.toString());
        for (int i = 0 ;i<liveRecommendIdList.size();i++){
            after_liveRecommendIdList.add(liveRecommendIdList.get(i));
        }
        System.out.println(after_liveRecommendIdList.toString());
        String s = after_liveRecommendIdList.toString();
        System.out.println(s);
        s = s.replace(" ", "");
        s = s.replace("[", "");
        s = s.replace("]", "");
        //String items = URLEncoder.encode(s, "utf-8");
        params.clear();
        params.put("access_token",access_token);
        params.put("type","0");
        params.put("items",s);
        post = httpClient.post(liveCategorySort, params);
        statusCode = httpClient.getStatusCode(post);
        rs = httpClient.getResponseJson(post);
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"请求失败");
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"更改直播分类排序成功","返回msg不正确");

        liveRecommendlist();
        System.out.println(after_liveRecommendIdList.toString());
        System.out.println(liveRecommendIdList.toString());
        Assert.assertEquals(after_liveRecommendIdList.toString(),liveRecommendIdList.toString(),"更改直播分类失败");

    }

    /**
     * 直播关注列表接口
     */
    @Test
    public void liveFocuslist() throws IOException {

        Reporter.log("校验access_token为空");
        HashMap<String,String> params = new HashMap<>();
        CloseableHttpResponse post = httpClient.post(liveFocuslist, params);
        int statusCode = httpClient.getStatusCode(post);
        JSONObject rs = httpClient.getResponseJson(post);
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"请求失败");
        int status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_Minus_1,"请求失败");
        String msg = rs.getString("msg");
        Assert.assertEquals(msg,"请登录","返回msg不正确");

        while (true){
            Reporter.log("校验code=200\n" +
                    "status=1\n" +
                    "msg=获取关注直播列表成功");
            params.clear();
            params.put("access_token",access_token);
            params.put("pagesize","10");
            params.put("page",page+"");
            post = httpClient.post(liveFocuslist, params);
            statusCode = httpClient.getStatusCode(post);
            rs = httpClient.getResponseJson(post);
            Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"请求失败");
            status = rs.getIntValue("status");
            Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"请求失败");
            msg = rs.getString("msg");
            Assert.assertEquals(msg,"获取关注直播列表成功","返回msg不正确");
            JSONObject data = rs.getJSONObject("data");
            if(data!=null && data.size()>0){
                JSONObject pager = data.getJSONObject("pager");
                JSONArray list = data.getJSONArray("list");
                Reporter.log("校验分页");
                Assert.assertEquals(list.size()<=10,true,"分页超过预期限制"+list.size());
                for (int i = 0; i< list.size();i++){
                    JSONObject listObj = list.getJSONObject(i);
                    Reporter.log("判断分类名称不为空");
                    String live_cname = listObj.getString("live_cname");
                    Assert.assertEquals(!live_cname.equals(""),true,"分类名称不应为空");
                    Reporter.log("判断热度返回值不为空");
                    String view_count = listObj.getString("view_count");
                    Assert.assertEquals(!view_count.equals(""),true,"热度不应为空view_count");
                    Reporter.log("判断直播标题不为空");
                    String live_title = listObj.getString("live_title");
                    Assert.assertEquals(!live_title.equals(""),true,"直播标题不应为空live_title");
                    Reporter.log("判断直播封面图可以正确显示");
                    String url = listObj.getJSONObject("live_cover").getString("url");
                    int statusCode1 = httpClient.getStatusCode(httpClient.get(url, new HashMap<String, String>()));
                    Assert.assertEquals(statusCode1,Constants.RESPNSE_STATUS_CODE_200,"直播封面图无法显示" + url);
                    Reporter.log("判断用户头像可以正确显示");
                    JSONObject anchor_data = listObj.getJSONObject("anchor_data");
                    String avatar = anchor_data.getString("avatar");
                    int statusCode2 = httpClient.getStatusCode(httpClient.get(avatar, new HashMap<String, String>()));
                    Assert.assertEquals(statusCode2,Constants.RESPNSE_STATUS_CODE_200,"用户头像无法显示"+avatar);
                    Reporter.log("判断用户昵称可以正确显示");
                    String nick_name = anchor_data.getString("nick_name");
                    Assert.assertEquals(!nick_name.equals(""),true,"用户昵称不应为空nick_name");
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
     * 直播列表
     */
    @Test(dependsOnMethods = { "liveCategoryList" })
    public void liveList() throws IOException {

        for (int j = 1;j< live_typeMap.size();j++){
            String live_cid = live_typeMap.get(j);
            while (true){
                HashMap<String,String> params = new HashMap<>();
                params.clear();
                params.put("live_cid",live_cid);
                params.put("pagesize","10");
                params.put("live_process","1");
                params.put("is_recommend","0");
                params.put("page",page+"");
                Reporter.log("校验code=200\n" +
                        "status=1\n" +
                        "msg=获取直播列表成功");
                CloseableHttpResponse post = httpClient.post(liveList, params);
                int statusCode = httpClient.getStatusCode(post);
                JSONObject rs = httpClient.getResponseJson(post);
                log.info(rs.toJSONString());
                Reporter.log(rs.toJSONString());
                Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"请求失败");
                int status = rs.getIntValue("status");
                Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"请求失败");
                String msg = rs.getString("msg");
                Assert.assertEquals(msg,"获取直播列表成功","返回msg不正确");
                JSONObject data = rs.getJSONObject("data");
                Reporter.log(rs.toJSONString());
                if(data!=null && data.size()>0){
                    JSONObject pager = data.getJSONObject("pager");
                    JSONArray list = data.getJSONArray("list");
                    Reporter.log("校验分页");
                    Assert.assertEquals(list.size()<=10,true,"分页超过预期限制"+list.size());
                    for (int i = 0; i< list.size();i++){
                        JSONObject listObj = list.getJSONObject(i);
                        Reporter.log("判断分类名称不为空");
                        String live_cname = listObj.getString("live_cname");
                        Assert.assertEquals(!live_cname.equals(""),true,"分类名称不应为空");
                        Reporter.log("判断热度返回值不为空");
                        String view_count = listObj.getString("view_count");
                        Assert.assertEquals(!view_count.equals(""),true,"热度不应为空view_count");
                        Reporter.log("判断直播标题不为空");
                        String live_title = listObj.getString("live_title");
                        Assert.assertEquals(!live_title.equals(""),true,"直播标题不应为空live_title");
                        Reporter.log("判断直播封面图可以正确显示");
                        String url = listObj.getJSONObject("live_cover").getString("url");
                        int statusCode1 = httpClient.getStatusCode(httpClient.get(url, new HashMap<String, String>()));
                        Assert.assertEquals(statusCode1,Constants.RESPNSE_STATUS_CODE_200,"直播封面图无法显示" + url);
                        Reporter.log("判断用户头像可以正确显示");
                        JSONObject anchor_data = listObj.getJSONObject("anchor_data");
                        String avatar = anchor_data.getString("avatar");
                        int statusCode2 = httpClient.getStatusCode(httpClient.get(avatar, new HashMap<String, String>()));
                        Assert.assertEquals(statusCode2,Constants.RESPNSE_STATUS_CODE_200,"用户头像无法显示"+avatar);
                        Reporter.log("判断用户昵称可以正确显示");
                        String nick_name = anchor_data.getString("nick_name");
                        Assert.assertEquals(!nick_name.equals(""),true,"用户昵称不应为空nick_name");
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



    }

}
