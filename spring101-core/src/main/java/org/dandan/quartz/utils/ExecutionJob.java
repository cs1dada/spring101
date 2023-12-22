package org.dandan.quartz.utils;

import org.dandan.quartz.domain.SysQuartzJob;
import org.dandan.quartz.domain.SysQuartzLog;
import org.dandan.quartz.repository.SysQuartzLogRepository;
import org.dandan.quartz.service.SysQuartzJobService;
import org.dandan.utils.SpringContextHolder;
import org.dandan.utils.StringUtils;
import org.dandan.utils.ThrowableUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.concurrent.Future;

/**
 *
 * 封裝job的地方
 * - 執行job的前後塞log紀錄
 * - 傳外部參數給job
 * - 例外處理
 *
 */

@Async
public class ExecutionJob extends QuartzJobBean {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ThreadPoolTaskExecutor executor = SpringContextHolder.getBean("elAsync");

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("ExecutionJob executeInternal !!!");
        SysQuartzJob quartzJob = (SysQuartzJob) context.getMergedJobDataMap().get(SysQuartzJob.JOB_KEY);
        // 获取spring bean
        SysQuartzLogRepository quartzLogRepository = SpringContextHolder.getBean(SysQuartzLogRepository.class);
        SysQuartzJobService quartzJobService = SpringContextHolder.getBean(SysQuartzJobService.class);
        //RedisUtils redisUtils = SpringContextHolder.getBean(RedisUtils.class);

        String uuid = quartzJob.getUuid();

        SysQuartzLog log = new SysQuartzLog();
        log.setJobName(quartzJob.getJobName());
        log.setBeanName(quartzJob.getBeanName());
        log.setMethodName(quartzJob.getMethodName());
        log.setParams(quartzJob.getParams());
        long startTime = System.currentTimeMillis();
        log.setCronExpression(quartzJob.getCronExpression());

        try {
            // 执行任务
            QuartzRunnable task = new QuartzRunnable(quartzJob.getBeanName(), quartzJob.getMethodName(), quartzJob.getParams());
            Future<?> future = executor.submit(task);
            future.get();
            long times = System.currentTimeMillis() - startTime;
            log.setTime(times);
            if(StringUtils.isNotBlank(uuid)) {
                //redisUtils.set(uuid, true);
            }
            // 任务状态
            log.setSuccess(true);
            logger.info("任务执行成功，任务名称：" + quartzJob.getJobName() + ", 执行时间：" + times + "毫秒");
            // 判断是否存在子任务
            if(StringUtils.isNotBlank(quartzJob.getSubTask())){
                String[] tasks = quartzJob.getSubTask().split("[,，]");
                // 执行子任务 todo
                //quartzJobService.executionSubJob(tasks);
            }
        } catch (Exception e) {
            if(StringUtils.isNotBlank(uuid)) {
                //redisUtils.set(uuid, false);
            }
            logger.error("任务执行失败，任务名称：" + quartzJob.getJobName());
            long times = System.currentTimeMillis() - startTime;
            log.setTime(times);
            // 任务状态 0：成功 1：失败
            log.setSuccess(false);
            log.setExceptionDetail(ThrowableUtil.getStackTrace(e));
            // 任务如果失败了则暂停
            if(quartzJob.getPauseAfterFailure() != null && quartzJob.getPauseAfterFailure()){
                quartzJob.setPause(false);
                //更新状态
                quartzJobService.updateIsPause(quartzJob);
            }
            if(quartzJob.getEmail() != null) {
//                EmailService emailService = SpringContextHolder.getBean(EmailService.class);
//                // 邮箱报警
//                if(StringUtils.isNoneBlank(quartzJob.getEmail())){
//                    EmailVo emailVo = taskAlarm(quartzJob, ThrowableUtil.getStackTrace(e));
//                    emailService.send(emailVo, emailService.find());
//                }
            }
        } finally {
            quartzLogRepository.save(log);
        }
    }
}
