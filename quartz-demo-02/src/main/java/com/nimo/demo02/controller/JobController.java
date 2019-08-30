package com.nimo.demo02.controller;

import com.nimo.demo02.entity.JobEntity;
import com.nimo.demo02.service.JobService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("job")
public class JobController {

    @Autowired
    private JobService jobService;

    /**
     * 保存任务并启动
     * ps: 不知道为什么没调用 start() 方法, 自己就启动了
     * 而且 QuartzConfig 里面的自启动配置也关了, 里面的自启动好像是程序启动时, 定时任务跟着启动
     *
     * @param jobEntity
     * @return
     */
    @PostMapping("/save")
    public Object saveJob(JobEntity jobEntity) {
        // 获取 scheduler 实例
        Scheduler scheduler = jobService.getScheduler();
        // 构建 job 实例
        JobDetail jobDetail = jobService.getJobDetail(jobEntity);
        // 构建 trigger 实例
        Trigger trigger = jobService.getTrigger(jobEntity);
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "添加失败";
        }
        return "添加成功！";
    }


    /**
     * 触发任务执行一次
     *
     * @param name
     * @param group
     * @return
     */
    @PostMapping("/trigger")
    public Object startJob(@RequestParam("name") String name, @RequestParam("group") String group) {

        Scheduler scheduler = jobService.getScheduler();
        JobKey jobKey = jobService.getJobKey(name, group);
        try {
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "触发失败！";
        }
        return "触发成功！";
    }

    /**
     * 暂停任务
     *
     * @param name
     * @param group
     * @return
     */
    @PostMapping("/pause")
    public Object pauseJob(@RequestParam("name") String name, @RequestParam("group") String group) {

        Scheduler scheduler = jobService.getScheduler();
        JobKey jobKey = jobService.getJobKey(name, group);
        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "暂停失败！";
        }
        return "暂停成功！";
    }

    /**
     * 恢复
     *
     * @param name
     * @param group
     * @return
     */
    @PostMapping("/resume")
    public Object resumeJob(@RequestParam("name") String name, @RequestParam("group") String group) {

        Scheduler scheduler = jobService.getScheduler();
        JobKey jobKey = jobService.getJobKey(name, group);
        try {
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "恢复失败！";
        }
        return "恢复成功！";
    }

    /**
     * 移除定时任务
     * @param name
     * @param group
     * @return
     */
    @PostMapping("/remove")
    public Object stopJob(@RequestParam("name") String name, @RequestParam("group") String group) {

        Scheduler scheduler = jobService.getScheduler();
        JobKey jobKey = jobService.getJobKey(name, group);
        TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
        try {
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            // 移除触发器
            scheduler.unscheduleJob(triggerKey);
            // 删除任务
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "移除失败！";
        }
        return "移除成功！";
    }


}
