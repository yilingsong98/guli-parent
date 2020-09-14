package com.guli.service.sms.controller.api;


import com.aliyuncs.exceptions.ClientException;
import com.guli.common.util.FormUtils;
import com.guli.common.util.RandomUtils;
import com.guli.service.base.exception.GuliException;
import com.guli.service.base.result.R;
import com.guli.service.base.result.ResultCodeEnum;
import com.guli.service.sms.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/sms")
@Api(tags = "短信管理")
//@CrossOrigin //跨域
@Slf4j
public class ApiSmsController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("发送验证码")
    @GetMapping("send/{mobile}")
    public R getCode(
            @ApiParam(value = "手机号",required = true)
            @PathVariable String mobile){

        // 校验参数（手机效验）
        if (StringUtils.isEmpty(mobile) || !FormUtils.isMobile(mobile)) {
            return R.setResult(ResultCodeEnum.LOGIN_PHONE_ERROR);
        }

        try {
            String checkCode = RandomUtils.getFourBitRandom();
            smsService.send(mobile,checkCode);

            // 向redis中存储验证码
            redisTemplate.opsForValue().set("checkCode::"+mobile,checkCode,5, TimeUnit.MINUTES);

            return R.ok().message("短信发送成功");
        } catch (Exception e) {
            log.error("短信发送失败！业务失败");
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR);
        }

    }

}
