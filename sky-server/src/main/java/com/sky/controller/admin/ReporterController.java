package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReporterService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 统计报表相关接口
 */
@RestController
@Slf4j
@Api(tags = "统计报表相关接口")
@RequestMapping("/admin/report")
public class ReporterController {
    @Autowired
     private ReporterService reporterService;

    /**
     * 统计营业额
     * @param begin
     * @param end
     * @return
     */
    @ApiOperation(value = "统计营业额")
    @GetMapping("/turnoverStatistics")
     public Result<TurnoverReportVO> turnoverStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end)
    {
           log.info("统计营业额,{},{}",begin,end);
           TurnoverReportVO turnoverReportVO=reporterService.getTurnOverStatistics(begin,end);
           return Result.success(turnoverReportVO);
     }

    /**
     * 用户数量统计
     * @param begin
     * @param end
     * @return
     */
     @ApiOperation(value = "用户数量统计")
     @GetMapping("/userStatistics")
     public Result<UserReportVO> userStatistics(
             @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
             @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
     )
     {
        log.info("用户数量统计");
        UserReportVO userReportVO=reporterService.getUserStatistic(begin,end);
        return Result.success(userReportVO);
     }

    /**
     * 订单数量统计
     * @param begin
     * @param end
     * @return
     */
    @ApiOperation(value = "订单数量统计")
    @GetMapping("/ordersStatistics")
     public Result<OrderReportVO> orderStatistics(
             @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
             @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
     )
     {
          log.info("订单数量统计");
          OrderReportVO orderReportVO=reporterService.getOrderStatistic(begin,end);
          return Result.success(orderReportVO);
     }

    /**
     * 销量前十名统计
     * @param begin
     * @param end
     * @return
     */
    @ApiOperation(value = "销量前十名统计")
    @GetMapping("/top10")
     public Result<SalesTop10ReportVO> getTop10Sale(
             @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
             @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
     )
     {
          log.info("销量前十名");
          SalesTop10ReportVO salesTop10ReportVO=reporterService.getTop10(begin,end);
          return Result.success(salesTop10ReportVO);
     }

    /**
     * 导出运营数据报表
     * @param response
     */
    @GetMapping("/export")
    @ApiOperation(value = "导出运营数据报表")
     public void export(HttpServletResponse response){
        reporterService.exportBusinessData(response);
     }
}
