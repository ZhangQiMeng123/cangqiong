package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {


    /**
     * 订单新增
     * @param orders
     */

    void saveOrder(Orders orders);


    /**
     * 根据订单号和用户id查询订单
     * @param orderNumber
     * @param userId
     */
    @Select("select * from orders where number = #{orderNumber} and user_id= #{userId}")
    Orders getByNumberAndUserId(String orderNumber, Long userId);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 查询历史订单信息
     * @param ordersPageQueryDTO
     * @return
     */

    Page<Orders> getHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    @Select("select * from orders where id=#{id}")
    Orders getOrderById(Long id);

    /**
     * 客户端分页查询订单信息
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 查询所有订单
     * @return
     */
    @Select("select * from orders")
    List<Orders> getOrdersCount();

    /**
     * 定时处理订单未支付以及完成但未点击完成状态
     * @param status
     * @param orderTime
     * @return
     */
    @Select("select * from orders where status=#{status} and order_time between #{orderTime} and #{now}")
    List<Orders> getOrdersByOrderTime(Integer status, LocalDateTime orderTime,LocalDateTime now);

    /**
     * 根据动态条件统计营业额
     * @param map
     * @return
     */
    Double sumByMap(Map map);

    /**
     * 获取订单数
     * @param beginTime
     * @param endTime
     * @return
     */
    Integer getOrderByIds(LocalDateTime beginTime, LocalDateTime endTime,Integer status);

    /**
     * 获取销量最好的前十名菜品名字以及销量
     * @param beginTime
     * @param endTime
     * @return
     */
    List<GoodsSalesDTO> getSalesTop10(LocalDateTime beginTime, LocalDateTime endTime);
}
