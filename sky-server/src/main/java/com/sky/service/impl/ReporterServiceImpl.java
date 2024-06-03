package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReporterService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReporterServiceImpl implements ReporterService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    /**
     * 营业额数据统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnOverStatistics(LocalDate begin, LocalDate end) {
           //创建一个集合用来装时间
        List<LocalDate> dateList=new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)){
            begin=begin.plusDays(1);  // 往后移一天
            dateList.add(begin);
        }
        List<Double> turnoverList=new ArrayList<>();
        //将时间格式为yyyy-MM-dd 变为yyyy-MM-dd HH:mm:ss
        for (LocalDate localDate : dateList) {
            LocalDateTime beginTime=LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime=LocalDateTime.of(localDate,LocalTime.MAX);
            Map map=new HashMap();
            map.put("status", Orders.COMPLETED);
            map.put("begin",beginTime);
            map.put("end",endTime);
            Double turnover=orderMapper.sumByMap(map);
            turnover=turnover==null?0.0:turnover;
            turnoverList.add(turnover);
        }
        //数据封装

        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .turnoverList(StringUtils.join(turnoverList,","))
                .build();
    }

    /**
     * 用户数量统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserStatistic(LocalDate begin, LocalDate end) {
         List<LocalDate> dateList=new ArrayList<>();
         dateList.add(begin);
         while(!begin.equals(end)){
             begin=begin.plusDays(1);
             dateList.add(begin);
         }

        List<Integer> newUserList=new ArrayList<>();
        List<Integer> totalUserList=new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime=LocalDateTime.of(date,LocalTime.MIN);
            LocalDateTime endTime=LocalDateTime.of(date,LocalTime.MAX);
            Integer newUserCount=getUserCount(beginTime,endTime);
            Integer totalUserCount=getUserCount(null,endTime);
            newUserList.add(newUserCount);
            totalUserList.add(totalUserCount);
        }
        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .newUserList(StringUtils.join(newUserList,","))
                .totalUserList(StringUtils.join(totalUserList,","))
                .build();
    }
    /**
     * 根据时间区间统计用户数量
     * @param beginTime
     * @param endTime
     * @return
     */
    private Integer getUserCount(LocalDateTime beginTime, LocalDateTime endTime) {
        Map map = new HashMap();
        map.put("begin",beginTime);
        map.put("end", endTime);
        return userMapper.countByMap(map);
    }

    /**
     * 订单数量统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO getOrderStatistic(LocalDate begin, LocalDate end) {
        Integer totalOrderCount=0; // 订单总数
        Integer validOrderCount=0; // 有效订单总数
        Double orderCompletionRate=0.0; // 订单完成率
        List<LocalDate> dateList=new ArrayList<>(); //日期列表
        dateList.add(begin);
        while(!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }

        List<Integer> orderCountList=new ArrayList<>(); // 订单数列表
        List<Integer> validOrderCountList=new ArrayList<>(); // 有效订单数列表

        for (LocalDate date : dateList) {
            LocalDateTime beginTime=LocalDateTime.of(date,LocalTime.MIN);
            LocalDateTime endTime=LocalDateTime.of(date,LocalTime.MAX);
            totalOrderCount+=orderMapper.getOrderByIds(beginTime,endTime,null);
            validOrderCount+=orderMapper.getOrderByIds(beginTime,endTime,Orders.COMPLETED);
            orderCountList.add(totalOrderCount);
            validOrderCountList.add(validOrderCount);
        }
        orderCompletionRate=validOrderCount.doubleValue() / totalOrderCount;
        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .orderCountList(StringUtils.join(orderCountList,","))
                .validOrderCountList(StringUtils.join(validOrderCountList,","))
                .orderCompletionRate(orderCompletionRate)
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .build();
    }

    /**
     * 销量前十名统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO getTop10(LocalDate begin, LocalDate end) {

        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<String> nameList=new ArrayList<>(); // 商品名称列表
        List<Integer> numberList=new ArrayList<>(); //销量列表

        List<GoodsSalesDTO> goodsSalesDTOList=orderMapper.getSalesTop10(beginTime,endTime);
        for (GoodsSalesDTO goodsSalesDTO : goodsSalesDTOList) {
            nameList.add(goodsSalesDTO.getName());
        }
        for (GoodsSalesDTO goodsSalesDTO : goodsSalesDTOList) {
            numberList.add(goodsSalesDTO.getNumber());
        }
        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList,","))
                .numberList(StringUtils.join(numberList,","))
                .build();
    }
}
