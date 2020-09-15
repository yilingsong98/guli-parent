package com.guli.service.trade.service.impl;

import com.github.wxpay.sdk.WXPayUtil;
import com.guli.common.util.HttpClientUtils;
import com.guli.service.base.exception.GuliException;
import com.guli.service.base.result.ResultCodeEnum;
import com.guli.service.trade.entity.Order;
import com.guli.service.trade.service.OrderService;
import com.guli.service.trade.service.WeixinPayService;
import com.guli.service.trade.utils.WeixinPayProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WeixinPayServiceImpl implements WeixinPayService {

    @Autowired
    private WeixinPayProperties weixinPayProperties;

    @Autowired
    private OrderService orderService;

    @Override
    public Map<String,Object> createNative(String orderNo, String remoteAddr) {
        try {

            //根据课程订单号获取订单
            Order order = orderService.getOrderByOrderNo(orderNo);

            // 统一下单api
            String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
            HttpClientUtils client = new HttpClientUtils(url);

            // 组装参数
            HashMap<String, String> params = new HashMap<>();
            params.put("appid",weixinPayProperties.getAppId());
            params.put("mch_id", weixinPayProperties.getPartner());//商户号
            params.put("nonce_str", WXPayUtil.generateNonceStr());//生成随机字符串
            // 从order中拿到商品描述（标题）
            params.put("body", order.getCourseTitle());
            params.put("out_trade_no", orderNo);
            // totalFee是long类型 但是传参需要传入String类型
            params.put("total_fee",order.getTotalFee() + "");
            params.put("spbill_create_ip",remoteAddr);
            // 回调地址 在yml中配置
            params.put("notify_url",weixinPayProperties.getNotifyUrl());
            params.put("trade_type","NATIVE");

            String xmlParams = WXPayUtil.generateSignedXml(params,weixinPayProperties.getPartnerKey());
            client.setXmlParam(xmlParams);
            client.setHttps(true);
            // 发送请求
            client.post();
            // 得到响应
            String content = client.getContent();
            System.out.println("content = " + content);

            // 解析响应结果
            Map<String, String> resultMap = WXPayUtil.xmlToMap(content);
            if ("FAIL".equals(resultMap.get("return_code")) || "FAIL".equals(resultMap.get("result_code"))) {
                log.error(resultMap.get("return_code") +
                        " , return msg = "+ resultMap.get("return_msg"));
                log.error(resultMap.get("result_code") +
                        " , err code = "+ resultMap.get("err_code") +
                        " , err code des = "+ resultMap.get("err_code_des"));

                throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
            }

            HashMap<String, Object> map = new HashMap<>();
            map.put("code_url",resultMap.get("code_url"));
            map.put("total_fee",order.getTotalFee());
            map.put("out_trade_no",orderNo);
            map.put("course_id",order.getCourseId());
            return map;
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
        }
    }
}
