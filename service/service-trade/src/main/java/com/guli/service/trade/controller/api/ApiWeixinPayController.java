package com.guli.service.trade.controller.api;


import com.github.wxpay.sdk.WXPayUtil;
import com.guli.common.util.StreamUtils;
import com.guli.service.base.result.R;
import com.guli.service.trade.service.WeixinPayService;
import com.guli.service.trade.utils.WeixinPayProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/trade/weixin-pay")
@Api(tags = "网站微信支付")
@Slf4j
public class ApiWeixinPayController {

    @Autowired
    private WeixinPayService weixinPayService;

    @Autowired
    private WeixinPayProperties weixinPayProperties;

    @GetMapping("create-native/{orderNo}")
    public R createNative(@PathVariable String orderNo, HttpServletRequest request){

        // 获取客户端ip
        String remoteAddr = request.getRemoteAddr();


        // 调用统一下单的api
        Map map = weixinPayService.createNative(orderNo,remoteAddr);

        return R.ok().data(map);
    }


    @PostMapping("callback/notify")
    public String wxNotify(HttpServletRequest request) throws Exception {
        ServletInputStream inputStream = request.getInputStream();
        // 通知结果
        String notifyXml = StreamUtils.inputStream2String(inputStream, "utf-8");

        System.out.println("notifyXml = " + notifyXml);

        // 校验签名
        if (WXPayUtil.isSignatureValid(notifyXml,weixinPayProperties.getPartnerKey())){
            // 成功 ，下一步
            System.out.println(" 验证签名成功 ");
            // 修改订单状态
            HashMap<String, String> returnMap = new HashMap<>();
            returnMap.put("return_code","SUCCESS");
            returnMap.put("return_msg","OK");
            String returnXml = WXPayUtil.mapToXml(returnMap);
            System.out.println(" 支付成功,已应答 ");
            return returnXml;
            // 记录支付日志

            // ......
        }

        // 失败 返回失败的应答
        HashMap<String, String> returnMap = new HashMap<>();
        returnMap.put("return_code","FAIL");
        returnMap.put("return_msg","签名失败");
        String returnXml = WXPayUtil.mapToXml(returnMap);
        return returnXml;
    }

}
