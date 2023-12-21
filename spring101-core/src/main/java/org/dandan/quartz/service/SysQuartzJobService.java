package org.dandan.quartz.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dandan.exception.BadRequestException;
import org.dandan.quartz.domain.SysQuartzJob;
import org.dandan.quartz.dto.QuartzJobInfoDTO;
import org.dandan.quartz.dto.SysQuartzJobDTO;
import org.dandan.quartz.repository.SysQuartzJobRepository;
import org.dandan.quartz.utils.QuartzManage;
import org.dandan.quartz.vo.SysQuartzJobQueryVO;
import org.dandan.quartz.vo.SysQuartzJobUpdateVO;
import org.dandan.quartz.vo.SysQuartzJobVO;
import org.quartz.*;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SysQuartzJobService {

    private final SysQuartzJobRepository sysQuartzJobRepository;
    private final QuartzManage quartzManage;

    @Transactional
    public Long save(SysQuartzJobVO vO) {
        SysQuartzJob bean = new SysQuartzJob();
        BeanUtils.copyProperties(vO, bean);
        bean = sysQuartzJobRepository.save(bean);
        return bean.getJobId();
    }

    @Transactional
    public Long create(SysQuartzJob resource) {
        if(!CronExpression.isValidExpression(resource.getCronExpression())) {
            throw new BadRequestException("Cron格式錯誤");
        }
        resource = sysQuartzJobRepository.save(resource);
        //新job +到scheduler
        quartzManage.addJob(resource);
        return resource.getJobId();

    }

    @Transactional
    public void delete(Long id) {
        sysQuartzJobRepository.deleteById(id);
    }

    @Transactional
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            SysQuartzJob sysQuartzJobjob = requireOne(id);
            // scheuler 刪除 job
            quartzManage.deleteJob(sysQuartzJobjob);
            // 刪除job item
            sysQuartzJobRepository.deleteById(id);
        }
    }

    @Transactional
    public void update(Long id, SysQuartzJobUpdateVO vO) {
        SysQuartzJob bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        sysQuartzJobRepository.save(bean);
    }

    @Transactional
    public Long update(SysQuartzJob resource) {
        if(!CronExpression.isValidExpression(resource.getCronExpression())) {
            throw new BadRequestException("Cron格式錯誤");
        }
        resource = sysQuartzJobRepository.save(resource);
        //原有的job 更新到scheduler
        quartzManage.updateJobCron(resource);

        return resource.getJobId();

    }

    public SysQuartzJobDTO getById(Long id) {
        SysQuartzJob original = requireOne(id);
        return toDTO(original);
    }

    public Page<SysQuartzJobDTO> query(SysQuartzJobQueryVO vO) {
        throw new UnsupportedOperationException();
    }

    private SysQuartzJobDTO toDTO(SysQuartzJob original) {
        SysQuartzJobDTO bean = new SysQuartzJobDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    public SysQuartzJob getSysQuartzJobById(Long id) {
        return requireOne(id);
    }

    private SysQuartzJob requireOne(Long id) {
        return sysQuartzJobRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }

    public void execution(SysQuartzJob quartzJob) {
        quartzManage.runJobNow(quartzJob);
    }

    /**
     * 顯示已註冊的job
     * @param jobName
     * @return
     * @throws SchedulerException
     */
    public List<QuartzJobInfoDTO> displayRegisteredJobs (String jobName) throws SchedulerException {

        List<JobDetail> jobDetails = quartzManage.getJobDetails();
        // 回傳注册的 Job 的信息
        List<QuartzJobInfoDTO> quartzJobInfoDTOList = new ArrayList<>();

        if (jobName.equals("all")) {
            quartzJobInfoDTOList = toQuartzJobInfoDTO(jobDetails);
        } else {
            List<JobDetail> jobs = jobDetails.stream().filter(e -> e.getKey().getName().equals(jobName)).collect(Collectors.toList());
            quartzJobInfoDTOList =  toQuartzJobInfoDTO(jobs);
        }

        return  quartzJobInfoDTOList;
    }

    /**
     * 顯示正在執行的job
     * @param jobName
     * @return
     * @throws SchedulerException
     */
    public List<QuartzJobInfoDTO> displayCurrentExecutingJobs (String jobName) throws SchedulerException {

        List<JobDetail> jobDetailList = new ArrayList<>();
        List<QuartzJobInfoDTO> quartzJobInfoDTOList = new ArrayList<>();

        List<JobExecutionContext> currentlyExecutingJobs =  quartzManage.getCurrentlyExecutingJobs();
        // 打印注册的 Job 的信息
        for (JobExecutionContext jobExecutionContext : currentlyExecutingJobs) {
            JobDetail jobDetail = jobExecutionContext.getJobDetail();
            jobDetailList.add(jobDetail);
        }
        quartzJobInfoDTOList = toQuartzJobInfoDTO(jobDetailList);


        return  quartzJobInfoDTOList;

    }

    /**
     * List<JobDetail> jobs => List<QuartzJobInfoDTO>
     * @param jobs
     * @return
     */
    private List<QuartzJobInfoDTO> toQuartzJobInfoDTO(List<JobDetail> jobs ) {

        List<QuartzJobInfoDTO> jobInfoList = new ArrayList<>();

        for (JobDetail jobDetail : jobs) {
            JobKey key = jobDetail.getKey();
            QuartzJobInfoDTO jobInfo = new QuartzJobInfoDTO(
                    key.getName(),
                    key.getGroup(),
                    jobDetail.getJobClass().getName(),
                    jobDetail.isDurable()
            );
            jobInfoList.add(jobInfo);
        }

        return jobInfoList;
    }
}
