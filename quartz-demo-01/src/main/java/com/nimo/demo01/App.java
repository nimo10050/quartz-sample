package com.nimo.demo01;

import com.nimo.demo01.job.HelloJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();

            JobDetail jobDetail = newJob(HelloJob.class)
                    .withIdentity("job1", "group1")
                    .build();

            Trigger trigger = newTrigger()
                    .withIdentity("trigger1", "group1")
                    .withSchedule(simpleSchedule().withIntervalInSeconds(4).repeatForever())
                    .usingJobData("test", "冲冲冲！！！")
                    .startAt(new Date())
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);

            //scheduler.start();
            JobKey jobKey = JobKey.jobKey("job1","group1");
            scheduler.triggerJob(jobKey);

            Thread.sleep(9000);

            scheduler.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
