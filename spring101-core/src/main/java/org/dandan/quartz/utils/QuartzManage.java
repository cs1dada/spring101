package org.dandan.quartz.utils;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.dandan.exception.BadRequestException;
import org.dandan.quartz.domain.SysQuartzJob;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
@Component
public class QuartzManage {
    private static final String JOB_NAME = "TASK_";

    @Resource
    private Scheduler scheduler;

    /**
     * 新增job到quartz裡面, 準備執行
     * @param quartzJob
     */
    public void addJob(SysQuartzJob quartzJob){
        try {
            // 构建job信息
            JobDetail jobDetail = JobBuilder.newJob(ExecutionJob.class).
                    withIdentity(JOB_NAME + quartzJob.getJobId(), "dandan").build();

            //通过触发器名和cron 表达式创建 Trigger
            Trigger cronTrigger = newTrigger()
                    .withIdentity(JOB_NAME + quartzJob.getJobId(), "dandan")
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(quartzJob.getCronExpression()))
                    .build();

            cronTrigger.getJobDataMap().put(SysQuartzJob.JOB_KEY, quartzJob);

            //重置启动时间
            ((CronTriggerImpl)cronTrigger).setStartTime(new Date());

            //执行定时任务
            scheduler.scheduleJob(jobDetail,cronTrigger);

            // 暂停任务
            if (quartzJob.getPause()) {
                pauseJob(quartzJob);
            }
        } catch (Exception e){
            log.error("创建定时任务失败", e);
            throw new BadRequestException("创建定时任务失败");
        }
    }

    /**
     * 更新job cron表达式
     * @param quartzJob /
     */
    public void updateJobCron(SysQuartzJob quartzJob){
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.getJobId(), "dandan");
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 如果不存在则创建一个定时任务
            if(trigger == null){
                addJob(quartzJob);
                trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            }
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(quartzJob.getCronExpression());
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            //重置启动时间
            ((CronTriggerImpl)trigger).setStartTime(new Date());
            trigger.getJobDataMap().put(SysQuartzJob.JOB_KEY, quartzJob);

            scheduler.rescheduleJob(triggerKey, trigger);
            // 暂停任务
            if (quartzJob.getPause()) {
                pauseJob(quartzJob);
            }
        } catch (Exception e){
            log.error("更新定时任务失败", e);
            throw new BadRequestException("更新定时任务失败");
        }

    }

    /**
     * 立即执行job
     * @param quartzJob /
     */
    public void runJobNow(SysQuartzJob quartzJob){
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.getJobId(), "dandan");
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 如果不存在则创建一个定时任务
            if(trigger == null) {
                addJob(quartzJob);
            }
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(SysQuartzJob.JOB_KEY, quartzJob);
            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getJobId(), "dandan");
            scheduler.triggerJob(jobKey,dataMap);
        } catch (Exception e){
            log.error("定时任务执行失败", e);
            throw new BadRequestException("定时任务执行失败");
        }
    }
    /**
     * 暂停一个job
     * @param quartzJob /
     */
    public void pauseJob(SysQuartzJob quartzJob){
        try {
            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getJobId(), "dandan");
            scheduler.pauseJob(jobKey);
        } catch (Exception e){
            log.error("定时任务暂停失败", e);
            throw new BadRequestException("定时任务暂停失败");
        }
    }

    /**
     * 刪除一个job
     * @param quartzJob
     */
    public void deleteJob(SysQuartzJob quartzJob) {
        JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getJobId(), "dandan");
        try {
            scheduler.pauseJob(jobKey);
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            log.error("删除定时任务失败", e);
            throw new BadRequestException("删除定时任务失败");
        }
    }

    /**
     * 取得正在執行的所有job
     * @return
     * @throws SchedulerException
     */
    public List<JobExecutionContext> getCurrentlyExecutingJobs() throws SchedulerException {
        return scheduler.getCurrentlyExecutingJobs();
    }

    /**
     * 取得所有job的key
     * @return
     * @throws SchedulerException
     */
    public List<JobKey> getJobKeys() throws SchedulerException {
        Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyGroup());
        return new ArrayList<>(jobKeys);
    }

    /**
     * 取得所有job的JobDetail
     * @return
     * @throws SchedulerException
     */
    public List<JobDetail> getJobDetails() throws SchedulerException {
        Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyGroup());
        List<JobDetail> list = new ArrayList<>();
        for (JobKey e : jobKeys) {
            JobDetail jobDetail = scheduler.getJobDetail(e);
            list.add(jobDetail);
        }
        return list;

    }
}
