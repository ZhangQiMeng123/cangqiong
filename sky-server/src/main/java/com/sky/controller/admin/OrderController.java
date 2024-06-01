package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "管理端订单相关接口")
@Slf4j
@RestController("adminOrderController")
@RequestMapping("/admin/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @GetMapping("/conditionSearch")
    @ApiOperation(value = "条件搜索订单")
    public Result<PageResult> searchOrders(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("订单搜索");
        PageResult pageResult=orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @ApiOperation(value = "各个状态的订单数量统计")
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> orderCount(){
        log.info("各个状态的订单数量统计");
        OrderStatisticsVO orderStatisticsVO=orderService.getStatusOrdersCount();
        return Result.success(orderStatisticsVO);
    }

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    @ApiOperation(value = "查询订单详情")
    @GetMapping("/details/{id}")
    public Result<OrderVO> getOrderById(@PathVariable Long id){
        log.info("查询订单详情：{}",id);
        OrderVO orderVO=orderService.getOrderDetailById(id);
        return Result.success(orderVO);
    }
    @ApiOperation(value = "商家接单")
    @PutMapping("/confirm")
    public Result receiveOrder(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
          log.info("商家接单:{}",ordersConfirmDTO);
          orderService.receiveOrder(ordersConfirmDTO);
          return Result.success();
    }

    /**
     * 商家拒单
     * @param ordersRejectionDTO
     * @return
     */
    @ApiOperation(value = "商家拒单")
    @PutMapping("/rejection")
    public Result refuseOrders(@RequestBody OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        log.info("商家拒绝接单:{}",ordersRejectionDTO);
        orderService.refuseOrders(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * 商家拒单
     * @param ordersCancelDTO
     * @return
     */
    @ApiOperation(value = "商家取消订单")
    @PutMapping("/cancel")
    public Result cancelOrders(@RequestBody OrdersCancelDTO ordersCancelDTO) throws Exception {
        log.info("商家拒绝接单:{}",ordersCancelDTO);
        orderService.cancelOrders(ordersCancelDTO);
        return Result.success();
    }

    /**
     * 派送订单
     * @param id
     * @return
     */
    @ApiOperation(value = "派送订单")
    @PutMapping("/delivery/{id}")
    public Result deliveryOrder(@PathVariable Long id){
        log.info("派送订单:{}",id);
        orderService.delieveOrder(id);
        return Result.success();
    }

    /**
     * 完成订单
     * @param id
     * @return
     */
    @ApiOperation(value= "完成订单")
    @PutMapping("/complete/{id}")
    public Result completeOrders(@PathVariable Long id){
        log.info("完成订单:{}",id);
        orderService.completeOrders(id);
        return Result.success();
    }
}
