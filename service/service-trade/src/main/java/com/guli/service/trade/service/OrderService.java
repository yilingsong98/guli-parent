package com.guli.service.trade.service;

import com.guli.service.trade.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author donkey
 * @since 2020-09-14
 */
public interface OrderService extends IService<Order> {

    String saveOrder(String courseId, String memberId);

    Order getOrderById(String orderId, String memberId);

    Boolean isBuyByCourseId(String courseId, String memberId);

    /**
     * 根据订单号查询订单
     * @param orderNo
     * @return
     */
    Order getOrderByOrderNo(String orderNo);
}
