package com.guli.service.sms.service.impl;


import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.guli.service.base.exception.GuliException;
import com.guli.service.base.result.ResultCodeEnum;
import com.guli.service.sms.service.SmsService;
import com.guli.service.sms.util.SmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsProperties smsProperties;

    @Override
    public void send(String mobile, String checkCode) throws ClientException {
        // 利用配置信息创建client对象
        DefaultProfile profile = DefaultProfile.getProfile(
                smsProperties.getRegionId(),
                smsProperties.getKeyId(),
                smsProperties.getKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        // 创建请求对象
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", smsProperties.getRegionId());
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", smsProperties.getSignName());
        request.putQueryParameter("TemplateCode", smsProperties.getTemplateCode());

        Map<String, String> map = new HashMap<>();
        map.put("code",checkCode);
        Gson gson = new Gson();
        String json = gson.toJson(map);

        request.putQueryParameter("TemplateParam", json);

        // 发送请求 得到响应结果
        CommonResponse response = client.getCommonResponse(request);
        System.out.println(response.getData());

        // json格式的结果字符串
        String data = response.getData();
        // 将json转换成Map
        HashMap<String,String> result = gson.fromJson(data, HashMap.class);
        // 通过Map取值
        String code = result.get("Code");
        String message = result.get("Message");

        if("isv.BUSINESS_LIMIT_CONTROL".equals(code)){
            log.error("短信发送过于频繁：code - " + code + "；message - " + message);
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL);
        }

        if (!"OK".equals(code)) {
            log.error("短信发送失败！ code：" + code + "- message :" + message);
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR);
        }

    }
}
