package org.dandan.quartz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dandan.exception.BadRequestException;
import org.dandan.quartz.domain.SysQuartzJob;
import org.dandan.quartz.dto.QuartzJobInfoDTO;
import org.dandan.quartz.service.SysQuartzJobService;
import org.dandan.utils.SpringContextHolder;
import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static org.dandan.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs")
public class QuartzJobController {

    private static final String ENTITY_NAME = "quartzJob";
    private final SysQuartzJobService sysQuartzJobService;

    private void checkBean(String beanName){
        // 避免调用攻击者可以从SpringContextHolder获得控制jdbcTemplate类
        // 并使用getDeclaredMethod调用jdbcTemplate的queryForMap函数，执行任意sql命令。
        if(!SpringContextHolder.getAllServiceBeanName().contains(beanName)){
            throw new BadRequestException("非法的 Bean，请重新输入！");
        }
    }

    //新增job
    @PostMapping
    @Operation(summary = "新增job", security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public ResponseEntity<Object> createQuartzJob(@Validated @RequestBody SysQuartzJob resources){

        if (resources.getJobId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        // 验证Bean是不是合法的，合法的定时任务 Bean 需要用 @Service 定义
        checkBean(resources.getBeanName());
        //新增job並且+到scheduler
        Long jobId = sysQuartzJobService.create(resources);
        log.info("new jobId: {}", jobId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //刪除job
    @DeleteMapping
    @Operation(summary = "刪除job", security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public ResponseEntity<Object> deleteQuartzJob(@RequestBody Set<Long> ids){
        sysQuartzJobService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    //修改job
    @PutMapping
    @Operation(summary = "修改job", security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public ResponseEntity<Object> updateQuartzJob(@Validated @RequestBody SysQuartzJob resources){

        // 验证Bean是不是合法的，合法的定时任务 Bean 需要用 @Service 定义
        checkBean(resources.getBeanName());
        //新增job並且+到scheduler
        sysQuartzJobService.update(resources);


        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //暫停job
    //執行job




    /**
     * 執行job (批次程式)
     * @param id
     * @return
     */
    @PutMapping(value = "/exec/{id}")
    @Operation(summary = "執行job", security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public ResponseEntity<Object> executionQuartzJob(@PathVariable Long id){
        sysQuartzJobService.execution(sysQuartzJobService.getSysQuartzJobById(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 查詢job or 所有註冊的job
     * @param jobName
     * @return
     * @throws SchedulerException
     */
    @GetMapping("/displayJobs/{jobName}")
    @Operation(summary = "顯示所有註冊的job", security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public List<QuartzJobInfoDTO> displayJobs(@Valid @NotNull @PathVariable("jobName") String jobName) throws SchedulerException {
        return sysQuartzJobService.displayRegisteredJobs(jobName);
    }

    //查詢job
    //查詢log
}
