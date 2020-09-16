package com.guli.service.trade.mapper;

import com.guli.service.trade.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 订单 Mapper 接口
 * </p>
 *
 * @author donkey
 * @since 2020-09-14
 */
public interface OrderMapper extends BaseMapper<Order> {

    void updateStatusByOrderNo(String orderNo);
}
