package com.nimo.demo01.job;

import org.quartz.*;

/**
 * 定义一个任务类
 */
public class HelloJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobKey key = jobDetail.getKey();// group1.job1

        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        System.out.println("开始执行任务  " + key);
    }
}
