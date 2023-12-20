package org.dandan.quartz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;
import org.dandan.quartz.dto.QuartzJobInfoDTO;
import org.dandan.quartz.dto.SysQuartzJobDTO;
import org.dandan.quartz.service.SysQuartzJobService;
import org.dandan.quartz.vo.SysQuartzJobQueryVO;
import org.dandan.quartz.vo.SysQuartzJobUpdateVO;
import org.dandan.quartz.vo.SysQuartzJobVO;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.dandan.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@Validated
@RestController
@RequestMapping("/sysQuartzJob")
public class SysQuartzJobController {

    @Autowired
    private SysQuartzJobService sysQuartzJobService;

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public String save(@Valid @RequestBody SysQuartzJobVO vO) {
        return sysQuartzJobService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        sysQuartzJobService.delete(id);
    }

    @PutMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody SysQuartzJobUpdateVO vO) {
        sysQuartzJobService.update(id, vO);
    }

    @GetMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public SysQuartzJobDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return sysQuartzJobService.getById(id);
    }

    @GetMapping
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public Page<SysQuartzJobDTO> query(@Valid SysQuartzJobQueryVO vO) {
        return sysQuartzJobService.query(vO);
    }

    /**
     * 執行特定job (批次程式)
     * @param id
     * @return
     */
    @PutMapping(value = "/exec/{id}")
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public ResponseEntity<Object> executionQuartzJob(@PathVariable Long id){
        sysQuartzJobService.execution(sysQuartzJobService.getSysQuartzJobById(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 顯示所有註冊的 job
     * @param jobName
     * @return
     * @throws SchedulerException
     */
    @GetMapping("/displayJobs/{jobName}")
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public List<QuartzJobInfoDTO> displayJobs(@Valid @NotNull @PathVariable("jobName") String jobName) throws SchedulerException {
        return sysQuartzJobService.displayRegisteredJobs(jobName);
    }


}
