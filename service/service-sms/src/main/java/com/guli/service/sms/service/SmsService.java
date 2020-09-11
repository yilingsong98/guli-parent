package com.guli.service.sms.service;

import com.aliyuncs.exceptions.ClientException;

public interface SmsService {

            // 手机号        验证码
    void send(String mobile,String checkCode) throws ClientException;
}
