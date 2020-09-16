package com.guli.service.trade.controller.api;


import com.github.wxpay.sdk.WXPayUtil;
import com.guli.common.util.StreamUtils;
import com.guli.service.base.result.R;
import com.guli.service.trade.entity.Order;
import com.guli.service.trade.service.OrderService;
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

    @Autowired
    private OrderService orderService;

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

        String resMessage = "";

        // 校验签名
        if (WXPayUtil.isSignatureValid(notifyXml,weixinPayProperties.getPartnerKey())){
            // 成功 ，下一步
            System.out.println(" 验证签名成功 ");

            // 解析xml数据
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyXml);
            // 获取商户订单号
            String orderNo = notifyMap.get("out_trade_no");
            // 根据订单号获取商户订单数据
            Order order = orderService.getOrderByOrderNo(orderNo);
            // 校验返回的订单金额 是否与商户侧的订单金额一致
            // 先来 健壮性校验
            if (order != null && order.getTotalFee() == Long.parseLong(notifyMap.get("total_fee"))){

                // 幂等性原则 ：无论接口被调用多少次 产生的结果是一致的
                // 接口调用的幂等性原则
                // 先检查 订单状态 (目的 防止应答微信端 没有及时收到)
                // 为了防止并发 加锁
                synchronized (this){    // 分布式项目 使用redis分布式锁
                    if (order.getStatus() == 0){
                        // 修改订单状态 // 记录支付日志
                        orderService.updateOrderStatus(notifyMap);
                    }
                }
                // ......

                HashMap<String, String> returnMap = new HashMap<>();
                returnMap.put("return_code","SUCCESS");
                returnMap.put("return_msg","OK");
                String returnXml = WXPayUtil.mapToXml(returnMap);
                System.out.println(" 支付成功,已应答 ");
                return returnXml;

            } else {
                resMessage = "金额校验失败";
            }
        } else {
            resMessage = "签名失败";
        }

        // 失败 返回失败的应答
        HashMap<String, String> returnMap = new HashMap<>();
        returnMap.put("return_code","FAIL");
        returnMap.put("return_msg",resMessage);
        String returnXml = WXPayUtil.mapToXml(returnMap);
        return returnXml;
    }

}
