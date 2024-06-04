package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

public interface WorkSpaceService {
    /**
     * 获取今日数据
     * @return
     */
    BusinessDataVO getBusinessData(LocalDateTime beginTime,LocalDateTime endTime);

    /**
     * 获取工作台订单管理数据
     * @return
     */
    OrderOverViewVO getOrdersData();

    /**
     * 工作台获取菜品数据
     * @return
     */
    DishOverViewVO getDishData();

    /**
     * 工作台获取套餐数据
     * @return
     */
    SetmealOverViewVO getStemealData();
}
