package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface OrderDetailMapper {

    /**
     * x向明细表中插入n条数据
     * @param orderDetailArrayList
     */
    void insertBatch(ArrayList<OrderDetail> orderDetailArrayList);

    /**
     * 根据订单的id获取订单详情数据
     * @param orderId
     * @return
     */
    @Select("select * from order_detail where order_id=#{orderId}")
    List<OrderDetail> getHistoryOrderDetails(Long orderId);


}
