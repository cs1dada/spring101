package org.dandan.quartz.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dandan.quartz.domain.SysQuartzJob;
import org.dandan.quartz.dto.QuartzJobInfoDTO;
import org.dandan.quartz.repository.SysQuartzJobRepository;
import org.dandan.quartz.service.SysQuartzJobService;
import org.dandan.quartz.utils.QuartzManage;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobRunner implements ApplicationRunner {
    private final SysQuartzJobRepository quartzJobRepository;
    private final QuartzManage quartzManage;
    private final SysQuartzJobService sysQuartzJobService;

    /**
     * 開機就把 待執行job插入scheduler
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<SysQuartzJob> quartzJobs = quartzJobRepository.findByPauseIsFalse();
        quartzJobs.forEach(quartzManage::addJob);

        List<QuartzJobInfoDTO> DTOlist = sysQuartzJobService.displayRegisteredJobs("all");

        log.info(DTOlist.toString());
        log.info("init quartz job 成功 !!!");

    }
}
