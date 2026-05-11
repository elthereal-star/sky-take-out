package com.sky.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author SXBai
 * @create 2026-05-08-23:56
 */
@Component
@Slf4j
public class MyTask {
    /**
     * 定时任务，每五秒出发一次
     */
   // @Scheduled(cron = "0/5 * * * * ?")
    public void executeTask(){log.info("任务开始执行",new Date());}
}
