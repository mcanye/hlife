package hlife.test.integration;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import hlife.base.BaseApi;
import hlife.base.Constants;
import hlife.filtration.URLFiltration;
import hlife.httpclient.HttpClient;
import hlife.parameters.VerificationParameters;
import hlife.test.integration.Fast;
import org.apache.commons.lang3.StringUtils;
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
 * 房产列表相关
 */
@Test(groups = "HouseList")
public class HouseList extends BaseApi{
    private HttpClient httpClient = new HttpClient();
    private VerificationParameters verParameters = new VerificationParameters();
    private static Logger log = LoggerFactory.getLogger(Fast.class);
    //房产标签集合
    List<String> tag_id_list = new ArrayList<>(4);
    //城市集合
    List<String> city_id_list = new ArrayList<String>();
    //品牌集合
    List<String> brand_id_list = new ArrayList<String>();
    int page = 1;
    int pagecount = 1;
    boolean on_or_off = true;

    /**
     * 获取标签集合，tag_id_list
     */
    @Test
    public void tagList() throws IOException {
        HashMap<String, String> params = new HashMap<>();
        JSONObject rs = httpClient.getResponseJson(httpClient.post(tagList,params));
        log.info(rs.toString());
        Reporter.log(rs.toString());
        Reporter.log("判断接口code等于200\n" +
                "判断status=1\n" +
                "判断msg=获取房产标签列表成功");
        Assert.assertEquals(rs.getIntValue("status"), Constants.RESPNSE_STATUS_CODE_1,"获取房产标签接口访问失败");
        Assert.assertEquals(rs.getString("msg"), "获取房产标签列表成功","获取房产标签接口返回msg不正确");
        Reporter.log("用例通过");
        Reporter.log("封装房产标签");
        JSONArray data = rs.getJSONArray("data");

        if (data.size()>0 || data != null){
            JSONObject data_obj;
            for (int i = 0;i <data.size();i++){
                //获取数组中的对象
                data_obj = (JSONObject) data.get(i);
                System.out.println(data_obj.toJSONString());
                tag_id_list.add(i, data_obj.getString("id"));
            }
            System.out.println(tag_id_list.toString());
            Reporter.log(tag_id_list.toString());
        } else {
            Assert.assertEquals(true,false,"获取标签集合失败，data为空");
        }
        Reporter.log("封装房产标签完毕：tag_id_list = " +tag_id_list.toString());
    }

    /**
     * 获取城市集合，city_id_list
     */
    @Test(dependsOnMethods = { "tagList" })
    public void city() throws IOException {

        HashMap<String, String> params = new HashMap<>();
        JSONObject rs = httpClient.getResponseJson(httpClient.post(city,params));
        log.info(rs.toString());
        Reporter.log(rs.toString());
        Reporter.log("判断接口code等于200\n" +
                "判断status=1\n" +
                "判断msg=获取城市列表成功");
        Assert.assertEquals(rs.getIntValue("status"), Constants.RESPNSE_STATUS_CODE_1,"获取城市列表接口访问失败");
        Assert.assertEquals(rs.getString("msg"), "获取城市列表成功","获取城市列表接口返回msg不正确");
        Reporter.log("用例通过");
        Reporter.log("封装城市列表city_id");
        JSONObject data = rs.getJSONObject("data");
        JSONArray all = data.getJSONArray("all");

        if (all.size()>0 || all != null){
            JSONObject obj;
            for (int i = 0;i <all.size();i++){
                //获取数组中的对象
                obj = (JSONObject) all.get(i);
                System.out.println(obj.toJSONString());
                city_id_list.add(i, obj.getString("city_id"));
            }
        } else {
            Assert.assertEquals(true,false,"获取城市集合失败，data为空");
        }
        Reporter.log("封装房产标签完毕：city_id_list = " +city_id_list.toString());
    }

    /**
     * 房产列表
     */
    @Test(dependsOnMethods = { "city" })
    public  void houseList() throws IOException {

        //遍历所有的城市
        for (int i = 0;i<city_id_list.size();i++){
            //遍历城市下所有标签
           for(int j=0;j<tag_id_list.size();j++){
               while (on_or_off){
                   HashMap<String, String> params = new HashMap<>();
                   params.put("pagesize","10");
                   params.put("page",page+"");
                   params.put("city_id",city_id_list.get(i));
                   params.put("tag_id",tag_id_list.get(j));
                   JSONObject rs = httpClient.getResponseJson(httpClient.post(house_list,params));
                   log.info(rs.toString());
                   Reporter.log(rs.toString());
                   Reporter.log("判断接口code等于200\n" +
                           "判断status=1\n" +
                           "判断msg=获取房产列表成功");
                   Assert.assertEquals(rs.getIntValue("status"), Constants.RESPNSE_STATUS_CODE_1,"获取房产列表接口访问失败");
                   Assert.assertEquals(rs.getString("msg"), "获取房产列表成功","获取房产列表接口返回msg不正确");
                   Reporter.log("用例通过");
                   JSONObject data = rs.getJSONObject("data");
                   if (data.size()>0 && data != null){
                       JSONObject pager = data.getJSONObject("pager");

                       Reporter.log("校验所有必返数据有效性");
                       JSONArray list = data.getJSONArray("list");
                       JSONObject obj;
                       for (int list_index=0;list_index<list.size();list_index++){
                           obj = (JSONObject) list.get(list_index);
                           JSONObject house_video = obj.getJSONObject("house_video");
                           Reporter.log("校验封面图");
                           if (house_video !=null && house_video.size()>0){
                               String cover = house_video.getString("cover");
                               int indexOf = cover.indexOf("?");
                               String newCover = cover.substring(0,indexOf);
                               System.out.println(newCover);
                               int statusCode = httpClient.getStatusCode(httpClient.get(newCover, new HashMap<String,String>()));
                               Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"封面图异常");
                           }else {
                               String house_pic = obj.getString("house_pic");
                               int statusCode = httpClient.getStatusCode(httpClient.get(house_pic, null));
                               Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"封面图异常");
                           }
                           Reporter.log("校验封面图用例通过");
                           Reporter.log("校验house_name");
                           String house_name = obj.getString("house_name");
                           if (house_name.equals("") || house_name == null){
                               Assert.assertEquals(true,false,"house_name不应为空");
                           }
                           Reporter.log("校验house_name用例通过");
                           Reporter.log("校验house_name");
                           String house_subtitle = obj.getString("house_subtitle");
                           if (house_subtitle.equals("") || house_subtitle == null){
                               Assert.assertEquals(true,false,"house_subtitle不应为空");
                           }
                           Reporter.log("校验house_subtitle用例通过");

                           Reporter.log("校验house_purpose");

                           Reporter.log("校验location_addr");
                           String location_addr = obj.getString("location_addr");
                           if (location_addr.equals("") || location_addr == null){
                               Assert.assertEquals(true,false,"location_addr不应为空");
                           }
                           Reporter.log("校验location_addr用例通过");
                           Reporter.log("校验average_price");
                           String average_price = obj.getString("average_price");
                           if (average_price.equals("") || average_price == null){
                               Assert.assertEquals(true,false,"average_price不应为空");
                           }
                            Reporter.log("校验average_price用例通过");
// //TODO: 2021/8/12 面积区间 待bug修复
//                   Reporter.log("校验area_range");
//                   String area_range = obj.getString("area_range");
//                   if (area_range.equals("") || area_range == null){
//                       Assert.assertEquals(true,false,"area_range不应为空");
//                   }
//                   Reporter.log("校验area_range用例通过");
//                   //TODO: 2021/8/12  价格区间 待bug修
//                            Reporter.log("校验price_range");
//                   String price_range = obj.getString("price_range");
//                   if (price_range.equals("") || price_range == null){
//                       Assert.assertEquals(true,false,"price_range不应为空");
//                   }
//                   Reporter.log("校验price_range用例通过");
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

    /**
     *房产品牌
     */
    @Test(dependsOnMethods = { "city" })
    public void brandList() throws IOException {

        HashMap<String, String> params = new HashMap<>();
        params.put("house_cid","1");
        JSONObject rs = httpClient.getResponseJson(httpClient.post(brandList,params));
        log.info(rs.toString());
        Reporter.log(rs.toString());
        Reporter.log("判断code=200，status=1,msg=获取房产品牌列表成功");
        int status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"获取房产品牌列表失败");
        String msg = rs.getString("msg");
        Assert.assertEquals(msg,"获取房产品牌列表成功","获取房产品牌接口msg返回错误");
        Reporter.log("用例通过");
        JSONObject data = rs.getJSONObject("data");
        JSONObject pager = data.getJSONObject("pager");
        String count = pager.getString("count");
        JSONArray list = data.getJSONArray("list");
        Reporter.log("校验品牌数量<=10，且与count一致");
        Assert.assertEquals(count,list.size()+"","房产品牌返回数量不正确，count与列表数目对应不上");
        Assert.assertEquals(true,list.size()<=10,"房产品牌返回数量不正确,大于10个");
        Reporter.log("用例通过");
        JSONObject list_obj;
        Reporter.log("校验品牌名称与品牌logo有效性");
        for (int i = 0 ;i<list.size();i++){
            list_obj = (JSONObject) list.get(i);
            String brand_name = list_obj.getString("brand_name");
            String brand_logo = list_obj.getString("brand_logo");
            String brand_id = list_obj.getString("brand_id");
            brand_id_list.add(brand_id);
            if (brand_name.equals("") || brand_name ==null){
                Assert.assertEquals(true,false,"品牌名称不应为空");
            }
            if (brand_logo.equals("") || brand_logo ==null){
                Assert.assertEquals(true,false,"品牌logo不应为空");
            }else {
                int statusCode = httpClient.getStatusCode(httpClient.get(brand_logo, new HashMap<String,String>()));
                Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"房产品牌logo不正确：" + brand_name +":"+brand_logo);
            }
            Reporter.log("用例通过");
            Reporter.log("品牌集成："+brand_id_list.toString());
        }
    }

    @Test(dependsOnMethods = { "brandList" })
    public void houseList_barand() throws IOException {

       //遍历所有城市
       for (int i =0;i<city_id_list.size();i++){
           //遍历所有的品牌
           for (int j = 0 ;j<brand_id_list.size();j++){
               //分页
               while (on_or_off){
                   HashMap<String, String> params = new HashMap<>();
                   params.put("house_cid","1");
                   params.put("city_id",city_id_list.get(i));
                   params.put("brand_id",brand_id_list.get(j));
                   params.put("page",page+"");
                   params.put("pagesize","10");
                   JSONObject rs = httpClient.getResponseJson(httpClient.post(house_list,params));
                   log.info(rs.toString());
                   Reporter.log(rs.toString());
                   Reporter.log("判断接口code等于200\n" +
                           "判断status=1\n" +
                           "判断msg=获取房产列表成功");
                   Assert.assertEquals(rs.getIntValue("status"), Constants.RESPNSE_STATUS_CODE_1,"获取房产列表接口访问失败");
                   Assert.assertEquals(rs.getString("msg"), "获取房产列表成功","获取房产列表接口返回msg不正确");
                   Reporter.log("用例通过");
                   JSONObject data = rs.getJSONObject("data");
                   if(data.size()>0 && data != null){
                       JSONObject pager = data.getJSONObject("pager");
                       Reporter.log("校验所有必返数据有效性");
                       JSONArray list = data.getJSONArray("list");
                       JSONObject obj;
                       for (int list_index=0;list_index<list.size();list_index++){
                           obj = (JSONObject) list.get(list_index);
                           JSONObject house_video = obj.getJSONObject("house_video");
                           Reporter.log("校验封面图");
                           if (house_video !=null && house_video.size()>0){
                               String cover = house_video.getString("cover");
                               int indexOf = cover.indexOf("?");
                               String newCover = cover.substring(0,indexOf);
                               System.out.println(newCover);
                               int statusCode = httpClient.getStatusCode(httpClient.get(newCover, new HashMap<String,String>()));
                               Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"封面图异常");
                           }else {
                               String house_pic = obj.getString("house_pic");
                               int statusCode = httpClient.getStatusCode(httpClient.get(house_pic, null));
                               Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"封面图异常");
                           }
                           Reporter.log("校验封面图用例通过");
                           Reporter.log("校验house_name");
                           String house_name = obj.getString("house_name");
                           if (house_name.equals("") || house_name == null){
                               Assert.assertEquals(true,false,"house_name不应为空");
                           }
                           Reporter.log("校验house_name用例通过");
                           Reporter.log("校验house_name");
                           String house_subtitle = obj.getString("house_subtitle");
                           if (house_subtitle.equals("") || house_subtitle == null){
                               Assert.assertEquals(true,false,"house_subtitle不应为空");
                           }
                           Reporter.log("校验house_subtitle用例通过");

                           Reporter.log("校验house_purpose");

                           Reporter.log("校验location_addr");
                           String location_addr = obj.getString("location_addr");
                           if (location_addr.equals("") || location_addr == null){
                               Assert.assertEquals(true,false,"location_addr不应为空");
                           }
                           Reporter.log("校验location_addr用例通过");
//                   Reporter.log("校验average_price");
//                   String average_price = obj.getString("average_price");
//                   if (average_price.equals("") || average_price == null){
//                       Assert.assertEquals(true,false,"average_price不应为空");
//                   }
//
//                   Reporter.log("校验average_price用例通过");
//                   TODO: 2021/8/12 面积区间 待bug修复
//                   Reporter.log("校验area_range");
//                   String area_range = obj.getString("area_range");
//                   if (area_range.equals("") || area_range == null){
//                       Assert.assertEquals(true,false,"area_range不应为空");
//                   }
//                   Reporter.log("校验area_range用例通过");
//                   TODO: 2021/8/12  价格区间 待bug修复
//                   Reporter.log("校验price_range");
//                   String area_range = obj.getString("price_range");
//                   if (price_range.equals("") || price_range == null){
//                       Assert.assertEquals(true,false,"price_range不应为空");
//                   }
//                   Reporter.log("校验price_range用例通过");
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
}
