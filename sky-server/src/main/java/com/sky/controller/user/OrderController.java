package com.sky.controller.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")  //给bean起别名，该bean名称为userOrderController，这样在admin中创建同样的OrderController就不会发生冲突
@Slf4j
@Api(tags = "订单相关接口")
@RequestMapping("//user/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 用户下订单
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("用户下订单,{}",ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO=orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    /**
     * 历史订单查询
     * @param ordersPageQueryDTO
     * @return
     */
    @ApiOperation(value = "历史订单查询")
    @GetMapping("/historyOrders")
    public Result<PageResult> getHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO){
         log.info("历史订单查询,{}",ordersPageQueryDTO);
         PageResult pageResult=orderService.getHistoryOrders(ordersPageQueryDTO);
         return Result.success(pageResult);
    }

    /**
     * 根据id查询订单详情
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id查询订单详情")
    @GetMapping("orderDetail/{id}")
    public Result<OrderVO> getOrderById(@PathVariable Long id){
         log.info("根据id查看订单详情,{}",id);
         OrderVO orderVO=orderService.getOrderDetailById(id);
         return Result.success(orderVO);
    }

    /**
     * 取消订单
     * @param id
     * @return
     */
    @PutMapping("/cancel/{id}")
    public Result deleteOrderById(@PathVariable Long id) throws Exception {
        log.info("取消订单");
        orderService.deleteByOrderId(id);
        return Result.success();
    }

    /**
     * 再来一单
     * @param id
     * @return
     */
    @PostMapping("/repetition/{id}")
    @ApiOperation(value = "再来一单")
    public Result repetition(@PathVariable Long id){
        log.info("再来一单，{}",id);
        orderService.repetitionOne(id);
        return Result.success();
    }

    /**
     * 客户催单
     * @param id
     * @return
     */
    @GetMapping("/reminder/{id}")
    public Result reminderOrder(@PathVariable("id") Long id){
        log.info("客户催单");
        orderService.reminderOrder(id);
        return Result.success();
    }
}
