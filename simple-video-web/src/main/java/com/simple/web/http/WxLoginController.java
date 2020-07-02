package com.simple.web.http;

import com.alibaba.fastjson.JSONObject;
import com.simple.commons.exception.GlobalException;
import com.simple.commons.exception.SimpleVideoErrorCode;
import com.simple.dao.model.user.User;
import com.simple.redis.utils.RedisUtil;
import com.simple.service.user.IUserService;
import com.simple.util.MD5;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author bai
 * @Description
 * @Date 2020/3/12 12:13 AM
 * @github https://github.com/baiyan0707
 */
@RestController
@Slf4j
public class WxLoginController {

    //@Value("${wechat.appid}")
    private String appid = "wx1f9bf6eaad8130e4";

    //@Value("${wechat.appsecret}")
    private String appsecret = "753b6de191053806eada02a683bc35d2";

    private String openid;

    @Autowired
    private IUserService userService;

    @Autowired
    RedisUtil redisUtil;

    @PostMapping("/wxLogin")
    @ResponseBody
    public String wxLogin(@RequestBody Map<String, String> userInfoMap) {
       if(userInfoMap.isEmpty()){
           log.error("请求参数非法{}",userInfoMap);
           throw new GlobalException(SimpleVideoErrorCode.SIMPLEVIDEO_ERROR_CODE_000001);
       }
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response;
        String code = userInfoMap.get("code");

        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + appsecret + "&js_code=" + code + "&grant_type=authorization_code";
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            URI uri = builder.build();

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new GlobalException(SimpleVideoErrorCode.SIMPLEVIDEO_ERROR_CODE_999999, e.getMessage());
        }
        // 解析json
        JSONObject jsonObject = (JSONObject) JSONObject.parse(resultString);
        String openid = jsonObject.get("openid") + "";

        //判断当前用户是否存在
        User user = userService.findUserByOpenId(openid);
        if (user == null) {
            //新用户
            user = new User();
            user.setOpenId(openid);
            user.setNickname(userInfoMap.get("nike"));
            user.setSex(Integer.parseInt(userInfoMap.get("sex")));
            user.setAvatar(userInfoMap.get("avaurl"));
            userService.insertUserInfo(user);
        }

        //生成3rd_session码返回给前台
        String rdSessionKey = MD5.getSignKey(32);
        //将openid、session和生成的session存入redis
        String sessionKey = jsonObject.get("session_key") + "";
        redisUtil.setEx(rdSessionKey, sessionKey+":"+openid, 12, TimeUnit.HOURS);

        return rdSessionKey;
    }
}
