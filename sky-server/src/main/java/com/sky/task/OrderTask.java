package com.sky.task;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 定时任务类，定时处理订单状态
 * @author SXBai
 * @create 2026-05-08-23:59
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理订单超时的方法
     */
    //@Scheduled(cron = "0 * * * * ? ")
    @Scheduled(cron = "1/5 * * * * ? ")
    public void processTimeoutOrder(){
            log.info("定时处理超时订单,{}", LocalDateTime.now());

            LocalDateTime time = LocalDateTime.now().plusMinutes(-15);

        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT,time);

        if(ordersList!=null&&ordersList.size()>0){
            for(Orders orders:ordersList){
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时，自动取消");
                orders.setOrderTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }

    }

    /**
     * 处理一直处于派送中的订单
     */
    //@Scheduled(cron = "0 0 1 * * ? ")//凌晨一点触发一次
    @Scheduled(cron = "0/5 * * * * ? ")
    public void processDeliveryOrder(){
        log.info("处理一直处于派送中的订单，每天凌晨一点自动处理");
        LocalDateTime time = LocalDateTime.now().plusHours(-1);
        List<Orders> ordersList=orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);
        if(ordersList!=null&&ordersList.size()>0){
            for(Orders orders:ordersList){
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }
}
