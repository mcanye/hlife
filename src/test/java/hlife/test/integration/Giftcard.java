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
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 礼品卡相关
 */
@Test(groups = "Giftcard")
public class Giftcard extends BaseApi{
    private HttpClient httpClient = new HttpClient();
    private static Logger log = LoggerFactory.getLogger(Giftcard.class);

    private int page=1;
    private List<String> idList = new ArrayList<>();
    private List<String> invoicesIdList = new ArrayList<>();
    private boolean b = false;
    private List<String> orderIdList = new ArrayList<>();
    /**
     * 礼品卡列表
     */
    @Test
    public void coursecardList() throws IOException {

        while (true){
            Reporter.log("判断200,1，msg=获取课程卡列表成功");
            HashMap<String,String> params = new HashMap<>();
            params.put("page",page+"");
            params.put("pagesize","10");
            params.put("card_type","supremacy");
            CloseableHttpResponse post = httpClient.post(coursecardList, params);
            int statusCode = httpClient.getStatusCode(post);
            JSONObject rs = httpClient.getResponseJson(post);
            Assert.assertEquals(statusCode, Constants.RESPNSE_STATUS_CODE_200,"礼品卡列表接口访问失败");
            Reporter.log(rs.toJSONString());
            log.info(rs.toJSONString());
            String msg = rs.getString("msg");
            int status = rs.getIntValue("status");
            Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"礼品卡列表接口访问失败");
            Assert.assertEquals(msg,"获取课程卡列表成功","礼品卡列表接口msg返回不正确");

            JSONObject data = rs.getJSONObject("data");
            JSONObject pager = data.getJSONObject("pager");
            JSONArray list = data.getJSONArray("list");
            if(list.size()>0){
                JSONObject listObj;
                for(int i=0;i<list.size();i++){
                    listObj = list.getJSONObject(i);
                    String id = listObj.getString("id");
                    Reporter.log("判断ID不为空");
                    Assert.assertEquals(id.equals(""),false,"id不应为空");
                    idList.add(id);
                    Reporter.log("判断标题不为空");
                    String theme = listObj.getString("theme");
                    Assert.assertEquals(theme.equals(""),false,"礼品卡标题不应为空");
                    Reporter.log("判断图片可用性" );
                    String img = listObj.getString("img");
                    int statusCode1 = httpClient.getStatusCode(httpClient.get(img, new HashMap<>()));
                    Assert.assertEquals(statusCode1,Constants.RESPNSE_STATUS_CODE_200,"图片不可用："+theme);
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

    /**
     * 礼品卡详情
     */
    @Test(dependsOnMethods = { "coursecardList" })
    public void coursecardDetail() throws IOException {
        Reporter.log("判断id必传");
        CloseableHttpResponse post = httpClient.post(coursecardDetail, new HashMap<>());
        JSONObject rs = httpClient.getResponseJson(post);
        int status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"礼品卡详情接口请求失败");
        String msg = rs.getString("msg");
        Assert.assertEquals(msg,"课程卡id不能为空","礼品卡详情接口返回msg不正确");
        HashMap<String,String> params = new HashMap<>();
        for(int i = 0 ;i<idList.size();i++){
            params.clear();
            params.put("card_id",idList.get(i));
            post = httpClient.post(coursecardDetail, params);
            rs = httpClient.getResponseJson(post);
            status = rs.getIntValue("status");
            Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"礼品卡详情接口请求失败");
            msg = rs.getString("msg");
            Assert.assertEquals(msg,"获取课程卡详情成功","礼品卡详情接口返回msg不正确");
            log.info(rs.toJSONString());
            Reporter.log(rs.toJSONString());
            JSONObject data = rs.getJSONObject("data");
            Reporter.log("判断id是否一致");
            String id = data.getString("id");
            Assert.assertEquals(id,idList.get(i),"ID不匹配");
            Reporter.log("判断标题不为空");
            String theme = data.getString("theme");
            Assert.assertEquals(theme.equals(""),false,"标题不应为空");
            Reporter.log("判断图片可用性");
            String img = data.getString("img");
            int statusCode1 = httpClient.getStatusCode(httpClient.get(img, new HashMap<>()));
            Assert.assertEquals(statusCode1,Constants.RESPNSE_STATUS_CODE_200,"图片不可用："+theme);
            Reporter.log("判断原价不为空");
            String price = data.getString("price");
            Assert.assertEquals(price.equals(""),false,"原价不应为空->price");
            Reporter.log("判断优惠价不为空");
            String discount_price = data.getString("discount_price");
            Assert.assertEquals(discount_price.equals(""),false,"优惠价不应为空->discount_price");
            Reporter.log("判断课程卡次数不为空");
            String nums = data.getString("nums");
            Assert.assertEquals(nums.equals(""),false,"课程卡次数不应为空->nums");
            Reporter.log("判断单节课时长不为空");
            String duration = data.getString("duration");
            Assert.assertEquals(duration.equals(""),false,"单节课时长不应为空->duration");
            Reporter.log("判断赠送好友图片可用性");
            String guide_pic = data.getString("guide_pic");
            int statusCode = httpClient.getStatusCode(httpClient.get(guide_pic, new HashMap<>()));
            Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"赠送好友图片显示异常"+guide_pic);
            Reporter.log("判断视频属性");
            JSONObject video = data.getJSONObject("video");
            String url = video.getString("url");
            statusCode = httpClient.getStatusCode(httpClient.get(url, new HashMap<>()));
            Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"视频播放显示异常"+url);
            String cover = video.getString("cover");
            statusCode = httpClient.getStatusCode(httpClient.get(cover, new HashMap<>()));
            Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"封面图显示异常"+cover);
            Reporter.log("判断课程卡有效期");
            JSONObject expiry = data.getJSONObject("expiry");
            String unit = expiry.getString("unit");
            String value = expiry.getString("value");
            Assert.assertEquals(unit.equals(""),false,"单位不应为空");
            Assert.assertEquals(value.equals(""),false,"数值不应为空");

        }
    }

    /**
     * 课程卡合作单位
     */
    @Test
    public void coursecardCooperation() throws IOException {
        CloseableHttpResponse post = httpClient.post(coursecardCooperation, new HashMap<>());
        JSONObject rs = httpClient.getResponseJson(post);
        int statusCode = httpClient.getStatusCode(post);
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"课程卡合作单位请求失败");
        int status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        String msg = rs.getString("msg");
        Assert.assertEquals(msg,"获取课程卡合作单位成功","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        String content = data.getString("content");
        Assert.assertEquals(content.equals(""),false,"合作单位内容不应为空");
    }

    /**
     * 发票参数校验
     */
    @Test
    public void cardorderVerifyInvoice() throws IOException, InterruptedException {
        Reporter.log("单位，电子普通发票type:3\n" +
                "invoice_type:1");
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("type","3");
        params.put("invoice_type","1");
        Reporter.log("校验名称不能为空");
        JSONObject rs = httpClient.getResponseJson(httpClient.post(cardorderVerifyInvoice, params));
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        Assert.assertEquals(msg,"名称不能为空","接口返回msg不正确");
        Reporter.log("判断邮箱不能为空");
        params.put("title","测试标题");
        Thread.sleep(3000);
        rs = httpClient.getResponseJson(httpClient.post(cardorderVerifyInvoice, params));
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        Assert.assertEquals(msg,"邮箱不能为空","接口返回msg不正确");
        Reporter.log("判断企业索取增值税发票需填写纳税人识别号不为空");
        params.put("email","11@163.com");
        Thread.sleep(3000);
        rs = httpClient.getResponseJson(httpClient.post(cardorderVerifyInvoice, params));
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        Assert.assertEquals(msg,"企业索取增值税发票需填写纳税人识别号","接口返回msg不正确");
        Reporter.log("判断校验成功");
        params.put("taxnumber","123456789012345678");
        Thread.sleep(3000);
        rs = httpClient.getResponseJson(httpClient.post(cardorderVerifyInvoice, params));
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"校验成功","接口返回msg不正确");
        Thread.sleep(3000);

        Reporter.log("个人，普通电子发票invoice_type=1，type=1");
        params.clear();
        params.put("access_token",access_token);
        params.put("invoice_type","1");
        params.put("type","1");
        params.put("title","测试标题");
        params.put("email","11@163.com");

        rs = httpClient.getResponseJson(httpClient.post(cardorderVerifyInvoice, params));
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"校验成功","接口返回msg不正确");
        Thread.sleep(3000);

        Reporter.log("单位，增值税专用发票invoice_type=2，type=3");
        params.clear();
        params.put("access_token",access_token);
        params.put("type","3");
        params.put("invoice_type","2");
        params.put("title","测试标题");
        params.put("address","单位地址");
        params.put("mobile","0427-1234567");
        params.put("open_bank","开户银行");
        params.put("bank_account","1111111111");
        params.put("addressee_address","收货地址");
        params.put("addressee_name","收货人姓名");
        params.put("addressee_phone","17611111111");
        params.put("pro","2");
        params.put("area","37");
        params.put("city","3537");
        params.put("taxnumber","123456789012345678");
        rs = httpClient.getResponseJson(httpClient.post(cardorderVerifyInvoice, params));
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"校验成功","接口返回msg不正确");

    }

    /**
     * 发票抬头列表
     */
    @Test(dependsOnMethods = { "invoicesCreatetitle" })
    public void invoicesListtitle() throws IOException {

        HashMap<String, String> header = URLFiltration.addHeader(new HashMap<>());
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(invoicesListtitle, params, header));
        Reporter.log(rs.toJSONString());
        log.info(rs.toJSONString());
        int status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"发票抬头列表接口请求失败");
        String msg = rs.getString("msg");

        if(msg.equals("暂时没有发票抬头")){
            Assert.assertEquals(msg,"暂时没有发票抬头","发票抬头列表接口请求msg返回不正确");
            return;
        }else {
            Assert.assertEquals(msg,"获取成功","发票抬头列表接口请求msg返回不正确");

        }
        JSONObject data = rs.getJSONObject("data");

        if(data.size()>0){
            JSONArray general_company = data.getJSONArray("general_company");

                JSONObject general_company_obj;
                for(int i = 0 ; i < general_company.size();i++){
                    general_company_obj = general_company.getJSONObject(i);
                    Reporter.log("判断ID不为空");
                    String id = general_company_obj.getString("id");
                    Assert.assertEquals(id.equals(""),false,"id不应为空");
                    invoicesIdList.add(id);
                    Reporter.log("判断title不为空");
                    String title = general_company_obj.getString("title");
                    Assert.assertEquals(title.equals(""),false,"title不应为空");
                    Reporter.log("判断type不为空");
                    String type = general_company_obj.getString("type");
                    Assert.assertEquals(type.equals(""),false,"type不应为空");
                    Reporter.log("判断taxnumber不为空");
                    String taxnumber = general_company_obj.getString("taxnumber");
                    Assert.assertEquals(taxnumber.equals(""),false,"taxnumber不应为空");
                    Reporter.log("判断invoice_type不为空");
                    String invoice_type = general_company_obj.getString("invoice_type");
                    Assert.assertEquals(invoice_type.equals(""),false,"invoice_type不应为空");

                }

            JSONArray added_tax = data.getJSONArray("added_tax");

                JSONObject added_tax_obj;
                for (int j = 0; j<added_tax.size();j++){
                    added_tax_obj = added_tax.getJSONObject(j);
                    Reporter.log("判断ID不为空");
                    String id = added_tax_obj.getString("id");
                    Assert.assertEquals(id.equals(""),false,"id不应为空");
                    invoicesIdList.add(id);
                    Reporter.log("判断title不为空");
                    String title = added_tax_obj.getString("title");
                    Assert.assertEquals(title.equals(""),false,"title不应为空");
                    Reporter.log("判断type不为空");
                    String type = added_tax_obj.getString("type");
                    Assert.assertEquals(type.equals(""),false,"type不应为空");
                    Reporter.log("判断taxnumber不为空");
                    String taxnumber = added_tax_obj.getString("taxnumber");
                    Assert.assertEquals(taxnumber.equals(""),false,"taxnumber不应为空");
                    Reporter.log("判断invoice_type不为空");
                    String invoice_type = added_tax_obj.getString("invoice_type");
                    Assert.assertEquals(invoice_type.equals(""),false,"invoice_type不应为空");
                    Reporter.log("判断open_bank不为空");
                    String open_bank = added_tax_obj.getString("open_bank");
                    Assert.assertEquals(open_bank.equals(""),false,"open_bank不应为空");
                    Reporter.log("判断company_address不为空");
                    String company_address = added_tax_obj.getString("company_address");
                    Assert.assertEquals(company_address.equals(""),false,"company_address不应为空");
                    Reporter.log("判断phone不为空");
                    String phone = added_tax_obj.getString("phone");
                    Assert.assertEquals(phone.equals(""),false,"phone不应为空");
                    Reporter.log("判断bank_account不为空");
                    String bank_account = added_tax_obj.getString("bank_account");
                    Assert.assertEquals(bank_account.equals(""),false,"bank_account不应为空");

                }

            JSONArray general_self = data.getJSONArray("general_self");

                JSONObject general_self_obj;
                for (int k = 0 ; k < general_self.size();k++){
                    general_self_obj = general_self.getJSONObject(k);
                    Reporter.log("判断ID不为空");
                    String id = general_self_obj.getString("id");
                    Assert.assertEquals(id.equals(""),false,"id不应为空");
                    invoicesIdList.add(id);
                    Reporter.log("判断title不为空");
                    String title = general_self_obj.getString("title");
                    Assert.assertEquals(title.equals(""),false,"title不应为空");
                    Reporter.log("判断type不为空");
                    String type = general_self_obj.getString("type");
                    Assert.assertEquals(type.equals(""),false,"type不应为空");
                    Reporter.log("判断invoice_type不为空");
                    String invoice_type = general_self_obj.getString("invoice_type");
                    Assert.assertEquals(invoice_type.equals(""),false,"invoice_type不应为空");
                }



        }

    }

    /**
     * 创建发票模板
     */
    @Test
    public void invoicesCreatetitle() throws IOException {
        Reporter.log("单位，电子普通发票");
        Reporter.log("判断title不能为空");
        HashMap<String, String> header = URLFiltration.addHeader(new HashMap<>());
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(invoicesCreatetitle, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        String msg = rs.getString("msg");
        Assert.assertEquals(msg,"名称不能为空","接口请求msg不正确");

        Reporter.log("判断发票类型不能为空");
        params.put("title","名称");
        rs = httpClient.getResponseJson(httpClient.post(invoicesCreatetitle, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"发票类型不能为空","接口请求msg不正确");

        Reporter.log("判断发票抬头类型不能为空");
        params.put("type","3");
        rs = httpClient.getResponseJson(httpClient.post(invoicesCreatetitle, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"发票抬头类型不能为空","接口请求msg不正确");


        Reporter.log("邮箱不能为空");
        params.put("invoice_type","1");
        rs = httpClient.getResponseJson(httpClient.post(invoicesCreatetitle, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"邮箱不能为空","接口请求msg不正确");

        Reporter.log("纳税人识别号或统一信用代码不能为空");
        params.put("email","aa@qq.com");
        rs = httpClient.getResponseJson(httpClient.post(invoicesCreatetitle, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"纳税人识别号或统一信用代码不能为空","接口请求msg不正确");

        Reporter.log("添加成功");
        params.put("taxnumber","111111111111111111");
        rs = httpClient.getResponseJson(httpClient.post(invoicesCreatetitle, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"添加成功","接口请求msg不正确");


        Reporter.log("单位，增值税专用发票");
        Reporter.log("判断title不能为空");
        params.clear();
        params.put("access_token",access_token);
        rs = httpClient.getResponseJson(httpClient.post(invoicesCreatetitle, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"名称不能为空","接口请求msg不正确");

        Reporter.log("判断发票类型不能为空");
        params.put("title","名称");
        rs = httpClient.getResponseJson(httpClient.post(invoicesCreatetitle, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"发票类型不能为空","接口请求msg不正确");

        Reporter.log("判断发票抬头类型不能为空");
        params.put("type","3");
        rs = httpClient.getResponseJson(httpClient.post(invoicesCreatetitle, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"发票抬头类型不能为空","接口请求msg不正确");

        Reporter.log("纳税人识别号或统一信用代码不能为空");
        params.put("invoice_type","2");
        rs = httpClient.getResponseJson(httpClient.post(invoicesCreatetitle, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"纳税人识别号或统一信用代码不能为空","接口请求msg不正确");

        Reporter.log("单位地址不能为空");
        params.put("taxnumber","111111111111111111");
        rs = httpClient.getResponseJson(httpClient.post(invoicesCreatetitle, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"单位地址不能为空","接口请求msg不正确");

        Reporter.log("座机号码不能为空");
        params.put("company_address","单位地址");
        rs = httpClient.getResponseJson(httpClient.post(invoicesCreatetitle, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"座机号码不能为空","接口请求msg不正确");

        Reporter.log("座机号码不能为空");
        params.put("company_address","单位地址");
        rs = httpClient.getResponseJson(httpClient.post(invoicesCreatetitle, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"座机号码不能为空","接口请求msg不正确");

        Reporter.log("开户银行不能为空");
        params.put("phone","0427-1234567");
        rs = httpClient.getResponseJson(httpClient.post(invoicesCreatetitle, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"开户银行不能为空","接口请求msg不正确");

        Reporter.log("银行账户不能为空");
        params.put("open_bank","开户银行");
        rs = httpClient.getResponseJson(httpClient.post(invoicesCreatetitle, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"银行账户不能为空","接口请求msg不正确");

        Reporter.log("添加成功");
        params.put("bank_account","55555555555555555555");
        rs = httpClient.getResponseJson(httpClient.post(invoicesCreatetitle, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"添加成功","接口请求msg不正确");

        Reporter.log("个人，电子普通发票");
        Reporter.log("判断title不能为空");
        params.clear();
        params.put("access_token",access_token);
        rs = httpClient.getResponseJson(httpClient.post(invoicesCreatetitle, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"名称不能为空","接口请求msg不正确");

        Reporter.log("判断发票类型不能为空");
        params.put("title","名称");
        rs = httpClient.getResponseJson(httpClient.post(invoicesCreatetitle, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"发票类型不能为空","接口请求msg不正确");

        Reporter.log("判断发票抬头类型不能为空");
        params.put("type","1");
        rs = httpClient.getResponseJson(httpClient.post(invoicesCreatetitle, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"发票抬头类型不能为空","接口请求msg不正确");

        Reporter.log("邮箱不能为空");
        params.put("invoice_type","1");
        rs = httpClient.getResponseJson(httpClient.post(invoicesCreatetitle, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_0,"接口请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"邮箱不能为空","接口请求msg不正确");

        Reporter.log("添加成功");
        params.put("email","qq@qq.com");
        rs = httpClient.getResponseJson(httpClient.post(invoicesCreatetitle, params, header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        msg = rs.getString("msg");
        Assert.assertEquals(msg,"添加成功","接口请求msg不正确");
    }

    /**
     * 删除发票模板
     */
    @Test(dependsOnMethods = { "invoicesListtitle" })
    public void invoicesDeletetitle() throws IOException {
        HashMap<String,String> header = URLFiltration.addHeader(new HashMap<>());
        HashMap<String,String> params = new HashMap<>();

        for (int i = 0 ;i<invoicesIdList.size();i++){
            params.clear();
            params.put("access_token",access_token);
            params.put("id",invoicesIdList.get(i));
            JSONObject rs = httpClient.getResponseJson(httpClient.post(invoicesDeletetitle, params, header));
            log.info(rs.toJSONString());
            Reporter.log(rs.toJSONString());
            int status = rs.getIntValue("status");
            String msg = rs.getString("msg");
            Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
            Assert.assertEquals(msg,"删除成功","接口返回msg不正确");
        }
        invoicesIdList.clear();
        invoicesListtitle();
        Assert.assertEquals(invoicesIdList.size(),0,"删除失败");
    }

    /**
     * 创建礼品卡订单
     */
    @Test(dependsOnMethods = { "coursecardList" })
    public void cardorderCreateOrder() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("card_id",idList.get(0));
        JSONObject rs = httpClient.getResponseJson(httpClient.post(cardorderCreateOrder, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"创建礼品卡订单访问失败");
        String msg = rs.getString("msg");
        Assert.assertEquals(msg,"下单成功","接口返回msg不正确");
    }

    /**
     * 我的卡包
     */
    @Test(dependsOnMethods = { "cardorderCreateOrder" })
    public void cardorderList() throws IOException, InterruptedException {

        HashMap<String,String> params = new HashMap<>();
        while (true){
            params.clear();
            params.put("access_token",access_token);
            params.put("page",page+"");
            params.put("pagesize","10");
            JSONObject rs = httpClient.getResponseJson(httpClient.post(cardorderList, params));
            log.info(rs.toJSONString());
            Reporter.log(rs.toJSONString());
            int status = rs.getIntValue("status");
            Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"我的卡包接口请求失败");
            String msg = rs.getString("msg");
            Assert.assertEquals(msg,"获取课程卡订单列表成功");
            JSONObject data = rs.getJSONObject("data");
            JSONObject pager = data.getJSONObject("pager");
            JSONArray list = data.getJSONArray("list");
            if(list.size()>0){
                JSONObject listObj;
                for(int i =0;i<list.size();i++){
                    listObj = list.getJSONObject(i);
                    String cardId = listObj.getString("cardId");
                    if(cardId.equals(idList.get(0))){
                        b = true;
                    }
                    String id = listObj.getString("id");
                    orderIdList.add(id);
                    Reporter.log("判断标题不为空");
                    String theme = listObj.getString("theme");
                    Assert.assertEquals(theme.equals(""),false,"标题不应为空");
                    Reporter.log("判断日期不为空");
                    String created_at = listObj.getString("created_at");
                    Assert.assertEquals(created_at.equals(""),false,"日期不应为空");
                    Reporter.log("面额不应为空");
                    String price = listObj.getString("price");
                    Assert.assertEquals(price.equals(""),false,"面额不应为空");
                    Reporter.log("有效期不应为空");
                    String expiry = listObj.getString("expiry");
                    Assert.assertEquals(expiry.equals(""),false,"有效期不应为空");
                    Reporter.log("订单状态不应为空");
                    String status1 = listObj.getString("status");
                    Assert.assertEquals(status1.equals(""),false,"订单状态不应为空");
                    Reporter.log("订单状态名称不应为空");
                    String statusName = listObj.getString("statusName");
                    Assert.assertEquals(statusName.equals(""),false,"订单状态名称不应为空");
                    Reporter.log("图片有效性");
                    String img = listObj.getString("img");
                    int statusCode = httpClient.getStatusCode(httpClient.get(img, new HashMap<>()));
                    Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"图片异常");
                }

                Reporter.log("判断下预订单是否成功");
                Assert.assertEquals(b,true,"下预订单失败");

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

    /**
     * 取消订单
     */
    @Test(dependsOnMethods = { "cardorderList" })
    public void cardorderCancel() throws IOException {
        for (int i = 0 ; i< orderIdList.size();i++){
            HashMap<String,String> params = new HashMap<>();
            params.put("access_token",access_token);
            String new_cardorderCancel = cardorderCancel + orderIdList.get(i);
            JSONObject rs = httpClient.getResponseJson(httpClient.post(new_cardorderCancel, params));
            log.info(rs.toJSONString());
            Reporter.log(rs.toJSONString());
            int status = rs.getIntValue("status");
            Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
            String msg = rs.getString("msg");
            Assert.assertEquals(msg,"取消成功","接口返回msg不正确");
        }
    }

    /**
     * 删除订单
     */
    @Test(dependsOnMethods = { "cardorderCancel" })
    public void cardorderDelete() throws IOException {
        for (int i = 0 ; i< orderIdList.size();i++){
            HashMap<String,String> params = new HashMap<>();
            params.put("access_token",access_token);
            String new_cardorderDelete = cardorderDelete + orderIdList.get(i);
            JSONObject rs = httpClient.getResponseJson(httpClient.post(new_cardorderDelete, params));
            log.info(rs.toJSONString());
            Reporter.log(rs.toJSONString());
            int status = rs.getIntValue("status");
            Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
            String msg = rs.getString("msg");
            Assert.assertEquals(msg,"删除成功","接口返回msg不正确");
        }
    }
}
