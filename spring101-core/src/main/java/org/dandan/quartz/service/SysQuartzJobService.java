package org.dandan.quartz.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dandan.quartz.domain.SysQuartzJob;
import org.dandan.quartz.dto.QuartzJobInfoDTO;
import org.dandan.quartz.dto.SysQuartzJobDTO;
import org.dandan.quartz.repository.SysQuartzJobRepository;
import org.dandan.quartz.utils.QuartzManage;
import org.dandan.quartz.vo.SysQuartzJobQueryVO;
import org.dandan.quartz.vo.SysQuartzJobUpdateVO;
import org.dandan.quartz.vo.SysQuartzJobVO;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SysQuartzJobService {

    private final SysQuartzJobRepository sysQuartzJobRepository;
    private final QuartzManage quartzManage;
    public Long save(SysQuartzJobVO vO) {
        SysQuartzJob bean = new SysQuartzJob();
        BeanUtils.copyProperties(vO, bean);
        bean = sysQuartzJobRepository.save(bean);
        return bean.getJobId();
    }

    public void delete(Long id) {
        sysQuartzJobRepository.deleteById(id);
    }

    public void update(Long id, SysQuartzJobUpdateVO vO) {
        SysQuartzJob bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        sysQuartzJobRepository.save(bean);
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

    public List<QuartzJobInfoDTO> displayRegisteredJobs (String jobName) throws SchedulerException {

        List<JobDetail> jobDetails = quartzManage.getJobDetails();
        // 回傳注册的 Job 的信息
        List<QuartzJobInfoDTO> jobInfoList = new ArrayList<>();

        if (jobName.equals("all")) {
            jobInfoList = getQuartzJobInfo(jobDetails);
        } else {
            List<JobDetail> jobs = jobDetails.stream().filter(e -> e.getKey().getName().equals(jobName)).collect(Collectors.toList());
            jobInfoList =  getQuartzJobInfo(jobs);
        }

        return  jobInfoList;
    }

    public List<QuartzJobInfoDTO> displayCurrentExecutingJobs (String jobName) throws SchedulerException {

        List<JobDetail> jobDetails = new ArrayList<>();
        List<QuartzJobInfoDTO> jobInfoList = new ArrayList<>();

        List<JobExecutionContext> currentlyExecutingJobs =  quartzManage.getCurrentlyExecutingJobs();
        // 打印注册的 Job 的信息
        for (JobExecutionContext jobExecutionContext : currentlyExecutingJobs) {
            JobDetail jobDetail = jobExecutionContext.getJobDetail();
            jobDetails.add(jobDetail);
        }
        jobInfoList = getQuartzJobInfo(jobDetails);


        return  jobInfoList;

    }

    private List<QuartzJobInfoDTO> getQuartzJobInfo(List<JobDetail> jobs ) {

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
