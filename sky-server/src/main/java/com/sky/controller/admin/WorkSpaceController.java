package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkSpaceService;
import com.sky.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 工作台相关接口
 */
@Api(tags = "工作台相关功能接口")
@RestController
@RequestMapping("/admin/workspace")
@Slf4j
public class WorkSpaceController {
    @Autowired
    private WorkSpaceService workSpaceService;

    /**
     * 工作台获取今日数据
     * @return
     */
    @ApiOperation(value = "获取今日数据")
    @GetMapping("/businessData")
    public Result<BusinessDataVO> getBusinessData(){
        log.info("获取今日数据");
        //获得当天的开始时间
        LocalDateTime beginTime = LocalDateTime.now().with(LocalTime.MIN);
        //获得当天的结束时间
        LocalDateTime endTime = LocalDateTime.now().with(LocalTime.MAX);
        BusinessDataVO businessDataVO=workSpaceService.getBusinessData(beginTime,endTime);
        return Result.success(businessDataVO);
    }

    /**
     * 工作台订单管理数据
     * @return
     */
    @ApiOperation(value = "工作台订单管理数据")
    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> getOrdersData(){
         log.info("工作台订单管理数据");
         OrderOverViewVO orderOverViewVO=workSpaceService.getOrdersData();
         return Result.success(orderOverViewVO);
    }

    /**
     * 工作台获取菜品数据
     * @return
     */
    @ApiOperation(value = "工作台获取菜品数据")
    @GetMapping("/overviewDishes")
    public Result<DishOverViewVO> getDishData(){
        log.info("获取工作台菜品数据");
        DishOverViewVO dishOverViewVO=workSpaceService.getDishData();
        return Result.success(dishOverViewVO);
    }

    /**
     * 获取套餐管理数据
     * @return
     */
    @ApiOperation(value = "获取菜单管理数据")
    @GetMapping("/overviewSetmeals")
    public Result<SetmealOverViewVO> getSetmealData(){
         log.info("获取菜单管理数据");

         SetmealOverViewVO setmealOverViewVO=workSpaceService.getStemealData();
         return Result.success(setmealOverViewVO);
    }
}
