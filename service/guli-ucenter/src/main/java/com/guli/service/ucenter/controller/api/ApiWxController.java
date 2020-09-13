package com.guli.service.ucenter.controller.api;


import com.google.gson.Gson;
import com.guli.common.util.HttpClientUtils;
import com.guli.service.base.exception.GuliException;
import com.guli.service.base.helper.JwtHelper;
import com.guli.service.base.helper.JwtInfo;
import com.guli.service.base.result.ResultCodeEnum;
import com.guli.service.ucenter.entity.Member;
import com.guli.service.ucenter.service.MemberService;
import com.guli.service.ucenter.util.UcenterProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.UUID;

@CrossOrigin
@Controller//注意这里没有配置 @RestController
@RequestMapping("/api/ucenter/wx")
@Slf4j
public class ApiWxController {

    @Autowired
    private UcenterProperties ucenterProperties;

    @Autowired
    private MemberService memberService;

    @GetMapping("login")
    public String genQrConnect(HttpSession session){

        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        //处理回调url
        String redirecturi = "";
        try {
            redirecturi = URLEncoder.encode(ucenterProperties.getRedirectUri(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.URL_ENCODE_ERROR);
        }

        //处理state：生成随机数，存入session
        String state = UUID.randomUUID().toString();
        log.info("生成 state = " + state);
        session.setAttribute("wx_open_state", state);

        String qrcodeUrl = String.format(
                baseUrl,
                ucenterProperties.getAppId(),
                redirecturi,
                state
        );

        return "redirect:" + qrcodeUrl; //客户端跳转：通过客户端的浏览器打开
    }

    @GetMapping("callback")
    public String callback(String code, String state, HttpSession session) {

        //回调被拉起，并获得code和state参数
        System.out.println("callback被调用");
        System.out.println("code = " + code);
        System.out.println("state = " + state);

        String sessionState = (String)session.getAttribute("wx_open_state");
        System.out.println("session state = " + sessionState);

        //校验code 和 state
        if(StringUtils.isEmpty(code) || StringUtils.isEmpty(state)){
            log.error("非法回调请求 - 参数不存在");
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        if(!state.equals(sessionState)){
            log.error("非法回调请求 - state参数错误");
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        //通过code + appid + appsecret换取access_token
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";


        //使用远程服务调用：httpclient
        String accessTokenUrl = String.format(baseAccessTokenUrl,
                ucenterProperties.getAppId(),
                ucenterProperties.getAppSecret(),
                code);

        HttpClientUtils client = new HttpClientUtils(accessTokenUrl);
        client.setHttps(true); //远程接口是https类型

        String result = "";
        try {
            client.get(); // 发送请求
            result = client.getContent();
            System.out.println("result = " + result);
        } catch (Exception e) {
            log.error("获取access_token失败");
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        //将json字符串结果转换成hashmap
        Gson gson = new Gson();
        HashMap resultMap = gson.fromJson(result, HashMap.class);

        // 判断是否是正确的 业务结果 如果有errorcode 就是错误的
        Object errcode = resultMap.get("errcode");
        if(errcode != null){
            Object errmsg = resultMap.get("errmsg");
            // 微信 errorcode 是 double类型 （坑）
            log.error("获取access_token失败：错误码 - " + (Double)errcode + "；错误消息 - " +  (String)errmsg);
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        // 解析 access_token
        String accessToken = (String)resultMap.get("access_token"); //访问令牌
        String openid = (String)resultMap.get("openid"); //微信用户的唯一标识

        System.out.println("accessToken = " + accessToken);
        System.out.println("openid = " + openid);


        //检查用户数据库，当前的微信用户信息是否存在，如果已经存在则无需访问微信接口获取信息
        //根据openid在本地数据库中获取用户信息
        Member member = memberService.getByOpenid(openid);
        if(member == null){

            //携带accessToken访问受保护的资源（微信用户信息）
            String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=" + accessToken +
                    "&openid=" + openid;

            // 将 userInfoUrl 当做 HttpClient 发送出去
            client = new HttpClientUtils(userInfoUrl);

            client.setHttps(true); //远程接口是https类型
            String resultUserInfo = "";
            try {

                client.get();  // 通过get方式发送出去

                resultUserInfo = client.getContent();
                System.out.println("resultUserInfo = " + resultUserInfo);
            } catch (Exception e) {
                log.error("获取用户信息失败");
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }

            //将json字符串结果转换成hashmap
            HashMap resultUserInfoMap = gson.fromJson(resultUserInfo, HashMap.class);
            Object errcodeUserInfo = resultUserInfoMap.get("errcode");
            if(errcodeUserInfo != null){
                Object errmsg = resultUserInfoMap.get("errmsg");
                log.error("获取access_token失败：错误码 - " + (Double)errcodeUserInfo + "；错误消息 - " +  (String)errmsg);
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }

            // 如果没有错误信息 从 resultUserInfoMap 拿到结果
            String nickname = (String)resultUserInfoMap.get("nickname");
            Double sex = (Double)resultUserInfoMap.get("sex");
            String headimgurl = (String)resultUserInfoMap.get("headimgurl");

            System.out.println("nickname = " + nickname);

            //用户注册：根据微信用户信息创建Member对象，并插入数据库
            member = new Member();
            member.setOpenid(openid);
            member.setNickname(nickname);
            member.setAvatar(headimgurl);
            member.setSex(sex.intValue());
            memberService.save(member);
        }

        //生成jwt
        JwtInfo jwtInfo = new JwtInfo();
        jwtInfo.setId(member.getId());
        jwtInfo.setNickname(member.getNickname());
        jwtInfo.setAvatar(member.getAvatar());
        String jwt = JwtHelper.createToken(jwtInfo);
        System.out.println("jwt = " + jwt);
        return "";
    }
}