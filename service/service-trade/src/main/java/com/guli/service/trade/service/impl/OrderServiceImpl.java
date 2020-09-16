package com.guli.service.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.guli.service.base.dto.CourseDto;
import com.guli.service.base.dto.MemberDto;
import com.guli.service.base.result.R;
import com.guli.service.trade.entity.Order;
import com.guli.service.trade.entity.PayLog;
import com.guli.service.trade.feign.EduCourseService;
import com.guli.service.trade.feign.UcenterMemberService;
import com.guli.service.trade.mapper.OrderMapper;
import com.guli.service.trade.mapper.PayLogMapper;
import com.guli.service.trade.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.service.trade.utils.OrderNoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author donkey
 * @since 2020-09-14
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduCourseService eduCourseService;

    @Autowired
    private UcenterMemberService ucenterMemberService;

    @Autowired
    private PayLogMapper payLogMapper;


    @Override
    public String saveOrder(String courseId, String memberId) {

        // 判断,如果当前用户已经购买过 当前课程 则 直接返回订单id 让用户跳转到订单页面
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        queryWrapper.eq("member_id",memberId);
        Order orderExist = baseMapper.selectOne(queryWrapper);
        if (orderExist != null){
            // 如果 同一个用户 买了同一个课 返回这个课的订单id
            return orderExist.getId();
        }


        // 创建订单
        Order order = new Order();

        // 生成订单号
        String orderNo = OrderNoUtils.getOrderNo();
        order.setOrderNo(orderNo);

        // 远程访问edu微服务,获得course信息 TODO
//        order.setCourseId(courseId);
//        order.setCourseTitle("title");
//        order.setCourseCover("cover");
//        order.setTeacherName("teacher");
//        order.setTotalFee(1L);
        R rCourseDto = eduCourseService.getCourseDtoById(courseId);
        // getData 就是 controller层 return的data的 courseDto
        // 直接强转 转不了
        // CourseDto courseDto = (CourseDto) rCourseDto.getData().get("courseDto");
        // 远程调用数据传输 被序列化成json字符串 然后 被反序列化成LinkedHashMap 而不是 CourseDto
        // 网络传输过程 一律为 json字符串 反序列化为 LinkedHashMap 而不是 原来的 CourseDto

        ObjectMapper objectMapper = new ObjectMapper();
        // convertValue 此方法 可以将 前面的（LinkedHashMap）类型 转成 后面任意的 类类型
        CourseDto courseDto = objectMapper.convertValue(rCourseDto.getData().get("courseDto"), CourseDto.class);

        order.setCourseId(courseId);
        order.setCourseTitle(courseDto.getTitle());
        order.setCourseCover(courseDto.getCover());
        order.setTeacherName(courseDto.getTeacherName());
        BigDecimal multiply = courseDto.getPrice().multiply(new BigDecimal(100));
        order.setTotalFee(multiply.longValue());

        // 远程访问ucenter微服务,获取用户信息 TODO
        R uMemberDto = ucenterMemberService.getMemberDtoById(memberId);
//        order.setMemberId(memberId);
//        order.setMobile("1234");
//        order.setNickname("nickName");

        MemberDto memberDto = objectMapper.convertValue(uMemberDto.getData().get("memberDto"), MemberDto.class);
        order.setMemberId(memberId);
        order.setMobile(memberDto.getMobile());
        order.setNickname(memberDto.getNickname());

        // 订单的其他信息
        order.setStatus(0); // 未支付
        order.setPayType(1); // 微信支付
        //order.setTotalFee(1000L); // 订单总价格 单位分

        baseMapper.insert(order);
        return order.getId();
    }

    @Override
    public Order getOrderById(String orderId, String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderId).eq("member_id",memberId);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public Boolean isBuyByCourseId(String courseId, String memberId) {

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId)
                .eq("member_id",memberId)
                .eq("status",1);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count.intValue() > 0;
    }


    /**
     * 根据订单号查询订单
     * @param orderNo
     * @return
     */
    @Override
    public Order getOrderByOrderNo(String orderNo) {

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public void updateOrderStatus(Map<String, String> notifyMap) {
        // 取出订单号
        String orderNo = notifyMap.get("out_trade_no");
        // 修改订单状态
        baseMapper.updateStatusByOrderNo(orderNo);

        // 记录支付日志
        PayLog payLog = new PayLog();
        payLog.setOrderNo(orderNo);
        payLog.setPayType(1);
        payLog.setPayTime(new Date());
        payLog.setTotalFee(Long.parseLong(notifyMap.get("total_fee")));
        payLog.setTradeState(notifyMap.get("result_code"));
        payLog.setTransactionId(notifyMap.get("transaction_id"));
        payLog.setAttr(new Gson().toJson(notifyMap));
        payLogMapper.insert(payLog);
    }
}
