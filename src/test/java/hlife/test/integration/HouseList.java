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
    //房产ID集合
    List<String> house_id_list = new ArrayList<String>();
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
                   params.put("house_cid","1");
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
                           String id = obj.getString("id");
                           house_id_list.add(id);
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
                               Assert.assertEquals(average_price.contains("均价"),true,"没有添加均价文案");
                           }
                           Reporter.log("校验average_price用例通过");
                           Reporter.log("判断总价");
                           String price_range = obj.getString("price_range");
                           if(!price_range.equals("")){
                               Assert.assertEquals(price_range.contains("总价"),true,"没有添加总价文案");
                               Reporter.log("校验price_range_unit用例通过");
                           }



//                  TODO: 2021/8/12 面积区间 待bug修复
//                   Reporter.log("校验area_range");
//                   String area_range = obj.getString("area_range");
//                   if (area_range.equals("") || area_range == null){
//                       Assert.assertEquals(true,false,"area_range不应为空");
//                   }
//                   Reporter.log("校验area_range用例通过");
//                   TODO: 2021/8/12  价格区间 待bug修
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
     * 房产详情
     */
    @Test(dependsOnMethods = { "houseList" })
    public void houseDetail() throws IOException {
        for (int i = 0;i<house_id_list.size();i++){
            HashMap<String,String> params = new HashMap<>();
            params.put("house_id",house_id_list.get(i));
            JSONObject rs = httpClient.getResponseJson(httpClient.post(houseDetail, params));
            log.info(rs.toJSONString());
            Reporter.log(rs.toJSONString());
            int status = rs.getIntValue("status");
            String msg = rs.getString("msg");
            Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
            Assert.assertEquals(msg,"获取房产详情成功","接口返回msg不正确");
            JSONObject data = rs.getJSONObject("data");
            String house_name = data.getString("house_name");
            Assert.assertEquals(house_name.equals(""),false,"房产名不应为空");
            String house_subtitle = data.getString("house_subtitle");
            Assert.assertEquals(house_subtitle.equals(""),false,"房产描述不应为空");
            String address = data.getString("address");
            Assert.assertEquals(address.equals(""),false,"房产位置不能为空");
            JSONArray house_pic = data.getJSONArray("house_pic");
            for(int j=0;j<house_pic.size();j++){
                String s = house_pic.getString(j);
                int statusCode = httpClient.getStatusCode(httpClient.get(s, new HashMap<>()));
                Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"图片无法显示");
            }
            String house_price = data.getString("house_price");
            Assert.assertEquals(house_price.equals(""),false,"房产价格不能为空");
            String price_range = data.getString("price_range");
            if(!price_range.equals("")){
                Assert.assertEquals(price_range.contains("总价"),true,"未标记总价");
            }
            String average_price = data.getString("average_price");
            if(!average_price.equals("")){
                Assert.assertEquals(average_price.contains("均价"),true,"未标记均价");
            }
            String house_unit = data.getString("house_unit");
            Assert.assertEquals(house_unit.equals(""),false,"房产单位不能为空");
            String house_stock = data.getString("house_stock");
            Assert.assertEquals(house_stock.equals(""),false,"房产库存不能为空");
            JSONArray house_tpl = data.getJSONArray("house_tpl");
            if(house_tpl.size()>0){
                JSONObject obj ;
                for(int j = 0;j<house_tpl.size();j++){
                    obj = house_tpl.getJSONObject(j);
                    String id = obj.getString("id");
                    String name = obj.getString("name");
                    String value = obj.getString("value");
                    Assert.assertEquals(id.equals(""),false,"房产模板ID不能为空");
                    Assert.assertEquals(name.equals(""),false,"房产模板名称不能为空");
                    Assert.assertEquals(value.equals(""),false,"房产模板属性不能为空");
                }
            }
            JSONArray house_opt = data.getJSONArray("house_opt");
            if(house_opt.size()>0){
                JSONObject obj ;
                for(int j = 0;j<house_opt.size();j++){
                    obj = house_opt.getJSONObject(j);
                    String name = obj.getString("name");
                    Assert.assertEquals(name.equals(""),false,"房产可选参数属性名称不能为空");
                    JSONArray value = obj.getJSONArray("value");
                    Assert.assertEquals(value.size()>0,true,"可选参数属性值列表不能为空");
                }
            }
            String house_desc = data.getString("house_desc");
            Assert.assertEquals(house_desc.equals(""),false,"房产详情不能为空");
            JSONObject seller_data = data.getJSONObject("seller_data");
            String nick_name = seller_data.getString("nick_name");
            Assert.assertEquals(nick_name.equals(""),false,"发布者名称不能为空");
            String avatar = seller_data.getString("avatar");
            int statusCode = httpClient.getStatusCode(httpClient.get(avatar, new HashMap<>()));
            Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"商家头像显示异常");
            String cate_unit = data.getString("cate_unit");
            Assert.assertEquals(cate_unit.equals(""),false,"房产分类单位不能为空");
            String location_addr = data.getString("location_addr");
            Assert.assertEquals(location_addr.equals(""),false,"房产位置名称不能为空");
            JSONArray house_thumb_pic = data.getJSONArray("house_thumb_pic");
            if(house_thumb_pic.size()>0){
                for (int j = 0 ; j<house_thumb_pic.size();j++){
                    String s = house_thumb_pic.getString(j);
                    String substring = s.substring(0, s.indexOf("?"));
                    int statusCode1 = httpClient.getStatusCode(httpClient.get(substring, new HashMap<>()));
                    Assert.assertEquals(statusCode1,Constants.RESPNSE_STATUS_CODE_200,"图片显示异常");
                }
            }
            JSONObject house_live = data.getJSONObject("house_live");
            String live_pic = house_live.getString("live_pic");
            int statusCode1 = httpClient.getStatusCode(httpClient.get(live_pic, new HashMap<>()));
            Assert.assertEquals(statusCode1,Constants.RESPNSE_STATUS_CODE_200,"直播封面图显示异常");
            JSONObject discount_info = data.getJSONObject("discount_info");
            if(discount_info.size()>0){
                JSONObject label = discount_info.getJSONObject("label");
                String text = label.getString("text");
                Assert.assertEquals(text.equals(""),false,"房产优惠信息文字不能为空");
                String font_color = label.getString("font_color");
                Assert.assertEquals(font_color.equals(""),false,"房产优惠信息文字颜色不能为空");
                String back_color = label.getString("back_color");
                Assert.assertEquals(back_color.equals(""),false,"房产优惠信息背景颜色不能为空");
                String price_text = discount_info.getString("price_text");
                Assert.assertEquals(price_text.equals(""),false,"优惠价格说明不能为空");

            }
            String price_text = data.getString("price_text");
            Assert.assertEquals(price_text.equals(""),false,"房产价格说明不能为空");
            JSONObject house_consult = data.getJSONObject("house_consult");
            String title = house_consult.getString("title");
            Assert.assertEquals(title,"咨询热线","房产咨询信息标题不正确");
            String subtitle = house_consult.getString("subtitle");
            Assert.assertEquals(subtitle,"安全通话隐藏真实号码，致电售楼处了解更多优惠","房产咨询信息副标题不正确");
            String tip = house_consult.getString("tip");
            Assert.assertEquals(tip,"置业顾问1对1服务","房产咨询提示信息不正确");
            String tel = house_consult.getString("tel");
            Assert.assertEquals(tel,"400-166-0309","房产咨询咨询电话不正确");
            JSONObject house_group = data.getJSONObject("house_group");
            if(house_group.size()>0){
                title = house_group.getString("title");
                Assert.assertEquals(title,"看房团","看房团标题不正确");
                String subtitle1 = house_group.getString("subtitle");
                Assert.assertEquals(subtitle1,"免费专车全程服务，专业团队带看","看房团副标题不正确");
            }

        }

    }

    /**
     * 房产全部筛选项
     */
    @Test
    public void houseFilterList() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(houseFilterList, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"房产筛选条件列表成功","接口返回msg不正确");
        JSONArray data = rs.getJSONArray("data");
        JSONObject price = data.getJSONObject(0);
        String type = price.getString("type");
        String name = price.getString("name");
        JSONArray sub = price.getJSONArray("sub");
        Assert.assertEquals(type,"price","价格筛选项类型不正确");
        Assert.assertEquals(name,"价格","价格筛选项文案不正确");
        Assert.assertEquals(sub.size(),8,"价格筛选项数目不正确");


        JSONObject room = data.getJSONObject(1);
        type = room.getString("type");
        name = room.getString("name");
        sub = room.getJSONArray("sub");
        Assert.assertEquals(type,"room","户型筛选项类型不正确");
        Assert.assertEquals(name,"户型","户型筛选项文案不正确");
        Assert.assertEquals(sub.size(),6,"户型筛选项数目不正确");
        JSONObject area = data.getJSONObject(2);
        type = area.getString("type");
        name = area.getString("name");
        sub = area.getJSONArray("sub");
        Assert.assertEquals(type,"area","面积筛选项类型不正确");
        Assert.assertEquals(name,"面积","面积筛选项文案不正确");
        Assert.assertEquals(sub.size(),7,"面积筛选项数目不正确");
        JSONObject order = data.getJSONObject(3);
        type = order.getString("type");
        name = order.getString("name");
        sub = order.getJSONArray("sub");
        Assert.assertEquals(type,"order","排序筛选项类型不正确");
        Assert.assertEquals(name,"排序","排序筛选项文案不正确");
        Assert.assertEquals(sub.size(),5,"排序筛选项数目不正确");

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
