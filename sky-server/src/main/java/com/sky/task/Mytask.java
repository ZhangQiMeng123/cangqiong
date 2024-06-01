package com.sky.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 测试自定义定时任务
 */
@Component
@Slf4j
public class Mytask {
    int mount=0;
   // @Scheduled(cron ="0/10 * * * * ? " )

    public void task1(){

        log.info("自动执行第{}次,{}",++mount,new Date());
    }


}
