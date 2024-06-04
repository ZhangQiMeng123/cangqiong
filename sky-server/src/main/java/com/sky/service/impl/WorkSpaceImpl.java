package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.entity.Orders;
import com.sky.entity.Setmeal;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class WorkSpaceImpl implements WorkSpaceService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    /**
     * 获取今日数据
     * @return
     */
    @Override
    public BusinessDataVO getBusinessData() {
        //获得当天的开始时间
        LocalDateTime beginTime = LocalDateTime.now().with(LocalTime.MIN);
        //获得当天的结束时间
        LocalDateTime endTime = LocalDateTime.now().with(LocalTime.MAX);
        //订单总数
        Integer totalOrders=orderMapper.getOrderByIds(beginTime,endTime,null);
        //有效订单数
        Integer validOrders=orderMapper.getOrderByIds(beginTime,endTime, Orders.COMPLETED);
        //营业额
        Double turnover=orderMapper.getTurnover(beginTime,endTime,Orders.COMPLETED);
        turnover = turnover == null? 0.0 : turnover;
        double orderCompletionRate=0.0;
        double averagePrice=0.0;
        if(totalOrders!=0 && validOrders!=0){
            //完成率
             orderCompletionRate=validOrders.doubleValue()/totalOrders;
            //平均客单价
             averagePrice=turnover/validOrders;
        }
        //新增用户数
        Integer newUserS=userMapper.getUserCount(beginTime,endTime);
        return BusinessDataVO.builder()
                .newUsers(newUserS)
                .validOrderCount(validOrders)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(averagePrice)
                .turnover(turnover)
                .build();
    }

    /**
     * 获取工作台订单管理数据
     * @return
     */
    @Override
    public OrderOverViewVO getOrdersData() {
        //今日开始时间
        LocalDateTime beginTime=LocalDateTime.now().with(LocalTime.MIN);
        //今日结束时间
        LocalDateTime endTime=LocalDateTime.now().with(LocalTime.MAX);
        List<Orders> ordersList=orderMapper.getAllOrder(beginTime,endTime);
        Integer allOrders=ordersList.size();
        Integer cacancelledOrders=0;
        Integer completedOrders=0;
        Integer deliveredOrders=0;
        Integer waitingOrders=0;
        for (Orders order : ordersList) {
            if(order.getStatus().equals(Orders.CANCELLED)) cacancelledOrders++;
            if(order.getStatus().equals(Orders.COMPLETED)) completedOrders++;
            if(order.getStatus().equals(Orders.CONFIRMED)) deliveredOrders++;
            if(order.getStatus().equals(Orders.TO_BE_CONFIRMED)) waitingOrders++;
        }
        return OrderOverViewVO.builder()
                .allOrders(allOrders)
                .cancelledOrders(cacancelledOrders)
                .completedOrders(completedOrders)
                .deliveredOrders(deliveredOrders)
                .waitingOrders(waitingOrders)
                .build();
    }

    /**
     * 工作台获取菜品数据
     * @return
     */
    @Override
    public DishOverViewVO getDishData() {
        List<Dish> dishList=dishMapper.getDishData();
        Integer discontinued=0; // 已停售菜品数量
        Integer sold=0; //已起售菜品你数量
        for (Dish dish : dishList) {
            if(dish.getStatus().equals(StatusConstant.DISABLE)) discontinued++;
            if(dish.getStatus().equals(StatusConstant.ENABLE)) sold++;
        }
        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * 工作台获取套餐数据
     * @return
     */
    @Override
    public SetmealOverViewVO getStemealData() {
        List<Setmeal> setmealList=setmealMapper.getSetMealData();
        Integer discontinued=0; // 已停售菜品数量
        Integer sold=0; //已起售菜品你数量
        for (Setmeal setmeal:setmealList) {
            if(setmeal.getStatus().equals(StatusConstant.DISABLE)) discontinued++;
            if(setmeal.getStatus().equals(StatusConstant.ENABLE)) sold++;
        }
        return SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }
}
