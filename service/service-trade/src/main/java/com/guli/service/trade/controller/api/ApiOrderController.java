package com.guli.service.trade.controller.api;


import com.guli.service.base.helper.JwtHelper;
import com.guli.service.base.result.R;
import com.guli.service.trade.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author donkey
 * @since 2020-09-14
 */
@Api(tags = "网站订单管理")
@RestController
@RequestMapping("/api/trade/order")
public class ApiOrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("新增订单")
    @PostMapping("auth/save/{courseId}")
    public R save(@PathVariable String courseId, HttpServletRequest request){
        // 通过request请求对象 获取当前用户的id
        String memberId = JwtHelper.getId(request);
        // 下订单  得到订单id （针对订单支付）
        // 订单展示  也需要得到 订单id
        String orderId = orderService.saveOrder(courseId,memberId);
        return R.ok().data("orderId",orderId);
    }

}

