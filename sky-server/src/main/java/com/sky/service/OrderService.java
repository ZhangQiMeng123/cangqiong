package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    /**
     * 用户下订单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);


    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 历史订单查询
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult getHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据id查看订单信息
     * @param id
     * @return
     */
    OrderVO getOrderDetailById(Long id);

    /**
     * 取消订单
     * @param id
     */
    void deleteByOrderId(Long id) throws Exception;

    /**
     * 再来一单
     * @param id
     */
    void repetitionOne(Long id);

    /**
     * 管理端按条件查询订单详情
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 统计各个订单状态的数量
     * @return
     */
    OrderStatisticsVO getStatusOrdersCount();

    /**
     * 商家接单
     * @param ordersConfirmDTO
     */
    void receiveOrder(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 商家拒单
     * @param ordersRejectionDTO
     */
    void refuseOrders(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    /**
     * 商家取消订单
     * @param ordersCancelDTO
     */
    void cancelOrders(OrdersCancelDTO ordersCancelDTO);

    /**
     * 派送订单
     * @param id
     */
    void delieveOrder(Long id);

    /**
     * 完成订单
     * @param id
     */
    void completeOrders(Long id);

    /**
     * 客户催单
     * @param id
     */
    void reminderOrder(Long id);
}
