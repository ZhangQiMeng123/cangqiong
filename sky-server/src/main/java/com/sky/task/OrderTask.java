package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 订单定时任务 用来处理超时未支付  订单已完成但未点击完成状态
 */
@Slf4j
@Component
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;
    /**
     * 处理订单超时未支付
     */
    @Scheduled(cron ="0 0/10 * * * ? ")
    public void OrderProcessing1(){
        log.info("处理超时未支付订单：{}",new Date());
        LocalDateTime time=LocalDateTime.now().plusMinutes(-5);
        List<Orders> ordersList=orderMapper.getOrdersByOrderTime(Orders.PENDING_PAYMENT,time,LocalDateTime.now());
        if(ordersList!=null){
            for (Orders order:ordersList){
                order.setStatus(Orders.CANCELLED); // 将订单取消
                order.setCancelReason("超时未支付");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }
    }

    /**
     * 处理订单完成但未点击完成状态的订单
     */
    @Scheduled(cron = "0 1 * * * ?")
    public void OrderProcessing2(){
        log.info("处理派送中订单：{}",new Date());
        LocalDateTime time=LocalDateTime.now().plusMinutes(-5);  // 凌晨一点钟自动处理前一天(24点整)的订单
        List<Orders> ordersList=orderMapper.getOrdersByOrderTime(Orders.DELIVERY_IN_PROGRESS,time,LocalDateTime.now());
        if(ordersList!=null){
            for(Orders order: ordersList){
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            }
        }
    }

    /**
     * 设置时间，自动处理处于待接单的单子
     */
    @Scheduled(cron = "0 0/10 * * * ? ")
    public void orderProcessing3(){
        log.info("处理长时间未接单的单子:{}",new Date());
        //从下单时间到现在已经过去十分钟
        LocalDateTime time=LocalDateTime.now().plusMinutes(-10);
        List<Orders> ordersList=orderMapper.getOrdersByOrderTime(Orders.TO_BE_CONFIRMED,time,LocalDateTime.now());
        if(ordersList!=null){
            for (Orders orders:ordersList){
                orders.setStatus(Orders.CONFIRMED);
                orderMapper.update(orders);
            }
        }
    }
}
