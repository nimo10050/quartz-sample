package com.nimo.demo02.service;

import com.nimo.demo02.entity.JobEntity;
import com.nimo.demo02.repository.JobEntityRepository;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

@Service
public class JobService {

    @Autowired
    private JobEntityRepository jobEntityRepository;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    public JobDetail getJobDetail(JobEntity jobEntity) {

        Class cls = null;
        try {
            cls = Class.forName(jobEntity.getJobClass());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            // TODO 抛异常
        }

        // 配置 jobDetail
        JobDetail jobDetail = JobBuilder
                .newJob(cls)
                .withIdentity(jobEntity.getName(), jobEntity.getGroup())
                .withDescription(jobEntity.getDescription())
                //.storeDurably()
                .build();

        return jobDetail;
    }

    public Trigger getTrigger(JobEntity jobEntity) {
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity(jobEntity.getName(), jobEntity.getGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(jobEntity.getCron()))
                .build();
        return trigger;
    }

    public JobEntity getJobEntityById(Integer id) {
        return jobEntityRepository.getById(id);
    }

    public JobKey getJobKey(String name, String group) {
        return JobKey.jobKey(name, group);
    }

  /*  public Scheduler getScheduler(Integer id) {
        JobEntity jobEntity = getJobEntityById(id);
        JobDetail jobDetail = getJobDetail(jobEntity);
        Trigger trigger = getTrigger(jobEntity);
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return scheduler;
    }*/

    public Scheduler getScheduler() {
        return schedulerFactoryBean.getScheduler();
    }

}
