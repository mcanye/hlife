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
 * 首页相关
 */
@Test(groups = "MinePage")
public class HomePage extends BaseApi{

    private HttpClient httpClient = new HttpClient();
    private static Logger log = LoggerFactory.getLogger(MinePage.class);
    private String uuid ="00000000-258c-468d-ffff-ffffef05ac4a";
    private List<String> video_id_list = new ArrayList<>();
    private String date = getDate(7200000);
    private boolean isin = false;
    private String comment_id;
    private String num_praise;

    /**
     * 获取版本信息
     */
    @Test
    public void systemVersion() throws IOException {
        HashMap<String,String> header = URLFiltration.addHeader(new HashMap<>());
        header.put("platform","Android");
        JSONObject rs = httpClient.getResponseJson(httpClient.post(systemVersion, new HashMap<>(), header));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status, Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取成功","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        String url_android = data.getString("url_android");
        int statusCode = httpClient.getStatusCode(httpClient.get(url_android, new HashMap<>()));
        Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"下载地址不能请求");
        String current = data.getString("current");
        Assert.assertEquals(current.equals(""),false,"版本号不能为空");
    }

    /**
     * 小视频列表
     */
    @Test
    public void videoList() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("city","全国");
        params.put("uuid",uuid);
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
        JSONObject list_obj;
        for (int i = 0;i<list.size();i++){
            list_obj = list.getJSONObject(i);
            String type = list_obj.getString("type");
            if(type.equals("video")){
                String id = list_obj.getString("id");
                Assert.assertEquals(id.equals(""),false,"视频id不能为空");
                video_id_list.add(id);

/*                String content = list_obj.getString("content");
                Assert.assertEquals(content.equals(""),false,"小视频文案不能为空");*/

                String avatar = list_obj.getString("avatar");
                int statusCode = httpClient.getStatusCode(httpClient.get(avatar, new HashMap<>()));
                Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"封面图请求失败");

                JSONObject video = list_obj.getJSONObject("video");
                String url = video.getString("url");
                Assert.assertEquals(url.contains(".m3u8"),true,"切片播放地址不正确");
                String download = video.getString("download");
                statusCode = httpClient.getStatusCode(httpClient.get(download, new HashMap<>()));
                Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"下载地址请求失败");
                String cover = video.getString("cover");
                statusCode = httpClient.getStatusCode(httpClient.get(cover, new HashMap<>()));
                Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"封面图请求失败");

                JSONObject music = list_obj.getJSONObject("music");
                if(music.size()>0){
                    String singer = music.getString("singer");
                    Assert.assertEquals(singer.equals(""),false,"音频分类不能为空");
                    String name = music.getString("name");
                    Assert.assertEquals(name.equals(""),false,"音频名称不能为空");
                    String music1 = music.getString("music");
                    statusCode = httpClient.getStatusCode(httpClient.get(music1, new HashMap<>()));
                    Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"音频播放地址请求失败");
                    String thumb = music.getString("thumb");
                    statusCode = httpClient.getStatusCode(httpClient.get(thumb, new HashMap<>()));
                    Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"音频封面图地址请求失败");
                    String duration = music.getString("duration");
                    Assert.assertEquals(duration.equals(""),false,"音频播放时长不能为空");
                }


                JSONObject share = list_obj.getJSONObject("share");
                
                if(share.size()>0){
                    url = share.getString("url");
                    statusCode = httpClient.getStatusCode(httpClient.get(url, new HashMap<>()));
                    Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"分享H5地址请求失败");
                    String name = share.getString("name");
                    Assert.assertEquals(name.equals(""),false,"分享名称不能为空");
                    String content1 = share.getString("content");
                    Assert.assertEquals(content1.equals(""),false,"分享文案不能为空");
                    String image = share.getString("image");
                    statusCode = httpClient.getStatusCode(httpClient.get(image, new HashMap<>()));
                    Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"分享图片地址请求失败");
                    String video1 = share.getString("video");
                    statusCode = httpClient.getStatusCode(httpClient.get(video1, new HashMap<>()));
                    Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"分享视频地址请求失败");
                }
            }

        }
        for(int i = 0;i<video_id_list.size();i++){
            System.out.println(video_id_list.get(i));
        }
    }

    /**
     * 视频详情
     */
    @Test(dependsOnMethods = { "videoList" })
    public void videoDetail() throws IOException {
        HashMap<String,String> params = new HashMap<>();
        for(int i = 0;i<video_id_list.size();i++){
            params.clear();
            params.put("id",video_id_list.get(i));
            JSONObject rs = httpClient.getResponseJson(httpClient.post(videoDetail, params));
            log.info(rs.toJSONString());
            Reporter.log(rs.toJSONString());
            int status = rs.getIntValue("status");
            String msg = rs.getString("msg");
            Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
            Assert.assertEquals(msg,"获取小视频详情成功","接口返回msg 不正确");
            JSONObject data = rs.getJSONObject("data");
            String type = data.getString("type");
            String id = data.getString("id");
            Assert.assertEquals(id.equals(""),false,"视频id不能为空");

            String content = data.getString("content");
            Assert.assertEquals(content.equals(""),false,"小视频文案不能为空");

            String avatar = data.getString("avatar");
            int statusCode = httpClient.getStatusCode(httpClient.get(avatar, new HashMap<>()));
            Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"封面图请求失败");

            JSONObject video = data.getJSONObject("video");
            String url = video.getString("url");
            Assert.assertEquals(url.contains(".m3u8"),true,"切片播放地址不正确");
            String download = video.getString("download");
            statusCode = httpClient.getStatusCode(httpClient.get(download, new HashMap<>()));
            Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"下载地址请求失败");
            String cover = video.getString("cover");
            statusCode = httpClient.getStatusCode(httpClient.get(cover, new HashMap<>()));
            Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"封面图请求失败");

            JSONObject music = data.getJSONObject("music");
            if(music.size()>0){
                String singer = music.getString("singer");
                Assert.assertEquals(singer.equals(""),false,"音频分类不能为空");
                String name = music.getString("name");
                Assert.assertEquals(name.equals(""),false,"音频名称不能为空");
                String music1 = music.getString("music");
                statusCode = httpClient.getStatusCode(httpClient.get(music1, new HashMap<>()));
                Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"音频播放地址请求失败");
                String thumb = music.getString("thumb");
                statusCode = httpClient.getStatusCode(httpClient.get(thumb, new HashMap<>()));
                Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"音频封面图地址请求失败");
                String duration = music.getString("duration");
                Assert.assertEquals(duration.equals(""),false,"音频播放时长不能为空");
            }


            JSONObject share = data.getJSONObject("share");
            if(share.size()>0){
                url = share.getString("url");
                statusCode = httpClient.getStatusCode(httpClient.get(url, new HashMap<>()));
                Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"分享H5地址请求失败");
                String name = share.getString("name");
                Assert.assertEquals(name.equals(""),false,"分享名称不能为空");
                String content1 = share.getString("content");
                Assert.assertEquals(content1.equals(""),false,"分享文案不能为空");
                String image = share.getString("image");
                statusCode = httpClient.getStatusCode(httpClient.get(image, new HashMap<>()));
                Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"分享图片地址请求失败");
                String video1 = share.getString("video");
                statusCode = httpClient.getStatusCode(httpClient.get(video1, new HashMap<>()));
                Assert.assertEquals(statusCode,Constants.RESPNSE_STATUS_CODE_200,"分享视频地址请求失败");
            }

        }
    }

    /**
     * 视频点赞、取消点赞
     */
    @Test(dependsOnMethods = { "videoDetail" })
    public void videoSupport() throws IOException, InterruptedException {
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("id",video_id_list.get(0));
        JSONObject rs = httpClient.getResponseJson(httpClient.post(videoSupport, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"点赞成功","接口返回msg不正确" );
        JSONObject data = rs.getJSONObject("data");
        String support_nums = data.getString("support_nums");

        loading();
        rs = httpClient.getResponseJson(httpClient.post(videoSupport, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"取消点赞成功","接口返回msg不正确" );
        data = rs.getJSONObject("data");
        String support_nums1 = data.getString("support_nums");
        Assert.assertEquals(support_nums.equals(support_nums1),false,"点赞、取消点赞成功");

    }

    /**
     * 评论列表
     * @throws IOException
     */
    private void publicsGetcomment() throws IOException {
        String publicsGetcomment_1;
        publicsGetcomment_1 = publicsGetcomment+video_id_list.get(0);
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("type","video");
        params.put("page","1");
        JSONObject rs = httpClient.getResponseJson(httpClient.post(publicsGetcomment_1, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取成功","接口返回msg不正确");

        JSONObject data = rs.getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        //todo
        JSONObject list_obj;
        for(int i=0;i<list.size();i++){
            list_obj = list.getJSONObject(i);
            String body = list_obj.getString("body");
            if(body.equals(date)){
                isin = true;
                comment_id = list_obj.getString("comment_id");
                num_praise = list_obj.getString("num_praise");
                break;
            }
        }
    }

    /**
     * 发布评价
     */
    @Test(dependsOnMethods = { "videoDetail" })
    public void publicsWritecomment() throws IOException, InterruptedException {
        publicsWritecomment = publicsWritecomment + video_id_list.get(0);
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("type","video");
        params.put("body",date);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(publicsWritecomment, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"评论成功","接口返回msg不正确");
        loading();
        publicsGetcomment();
        Assert.assertEquals(isin,true,"评论失败");
        isin = false;
    }

    /**
     * 点赞/取消点赞评论
     */
    @Test(dependsOnMethods = { "publicsWritecomment" })
    public void publicsCommentpraise() throws IOException, InterruptedException {

        publicsCommentpraise = publicsCommentpraise + comment_id;
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(publicsCommentpraise, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"点赞成功","接口返回msg不正确");
        loading();
        publicsGetcomment();
        String s1 = num_praise;
        rs = httpClient.getResponseJson(httpClient.post(publicsCommentpraise, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        status = rs.getIntValue("status");
        msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"取消点赞成功！","接口返回msg不正确");
        loading();
        publicsGetcomment();
        String s2 = num_praise;
        Assert.assertEquals(s1.equals(s2),false,"点赞，取消点赞后，评论列表数目没有改变");
    }

    /**
     * 发布二级评论
     */
    @Test(dependsOnMethods = { "publicsWritecomment" })
    public void publicsSetcomment() throws IOException, InterruptedException {
        publicsSetcomment = publicsSetcomment + comment_id;
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("body",date);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(publicsSetcomment, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"评论成功","接口返回msg不正确");
        loading();
        publicsGettwocomment();
        Assert.assertEquals(isin,true,"评论失败");
    }

    /**
     * 二级评论列表
     */
    private void publicsGettwocomment() throws IOException {
        publicsGettwocomment = publicsGettwocomment +comment_id;
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",access_token);
        JSONObject rs = httpClient.getResponseJson(httpClient.post(publicsGettwocomment, params));
        log.info(rs.toJSONString());
        Reporter.log(rs.toJSONString());
        int status = rs.getIntValue("status");
        String msg = rs.getString("msg");
        Assert.assertEquals(status,Constants.RESPNSE_STATUS_CODE_1,"接口请求失败");
        Assert.assertEquals(msg,"获取成功","接口返回msg不正确");
        JSONObject data = rs.getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        JSONObject list_obj;
        for (int i = 0 ; i<list.size();i++){
            list_obj = list.getJSONObject(i);
            String body = list_obj.getString("body");
            if(body.equals(date)){
                isin = true;
                break;
            }
        }
    }

}
