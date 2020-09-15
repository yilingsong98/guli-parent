package com.guli.service.trade.controller.api;


import com.guli.service.base.helper.JwtHelper;
import com.guli.service.base.result.R;
import com.guli.service.trade.entity.Order;
import com.guli.service.trade.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
        System.out.println(" request.getSession().getId() = " + request.getSession().getId());
        // 通过request请求对象 获取当前用户的id
        String memberId = JwtHelper.getId(request);
        // 下订单  得到订单id （针对订单支付）
        // 订单展示  也需要得到 订单id
        String orderId = orderService.saveOrder(courseId,memberId);
        return R.ok().data("orderId",orderId);
    }


    @ApiOperation(value = "根据订单号获取订单id",tags = "只能获取自己的订单信息")
    @GetMapping("auth/get/{orderId}")
    public R getById(
            @ApiParam(value = "订单号",required = true)
            @PathVariable String orderId,
            @ApiParam(value = "request对象",required = true)
            HttpServletRequest request){

        String memberId = JwtHelper.getId(request);
        Order order = orderService.getOrderById(orderId,memberId);
        return R.ok().data("item",order);
    }


    @ApiOperation( "判断课程是否购买")
    @GetMapping("auth/is-buy/{courseId}")
    public R isBuyByCourseId(@PathVariable String courseId, HttpServletRequest request) {

        String memberId = JwtHelper.getId(request);
        Boolean isBuy = orderService.isBuyByCourseId(courseId, memberId);
        return R.ok().data("isBuy", isBuy);
    }
}

