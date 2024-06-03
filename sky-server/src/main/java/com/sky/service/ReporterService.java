package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ReporterService {
    /**
     * 营业额数据统计
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO getTurnOverStatistics(LocalDate begin, LocalDate end);

    /**
     * 用户数量统计
     * @param begin
     * @param end
     * @return
     */
    UserReportVO getUserStatistic(LocalDate begin, LocalDate end);

    /**
     * 订单数量统计
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO getOrderStatistic(LocalDate begin, LocalDate end);

    /**
     * 销量前十名统计
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO getTop10(LocalDate begin, LocalDate end);
}
