package com.sky.mapper;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface OrderMapper {
    /**
     * 订单新增
     * @param orders
     */

    void saveOrder(Orders orders);
}
