package org.dandan.quartz.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;
import org.dandan.quartz.dto.SysQuartzLogDTO;
import org.dandan.quartz.service.SysQuartzLogService;
import org.dandan.quartz.vo.SysQuartzLogQueryVO;
import org.dandan.quartz.vo.SysQuartzLogUpdateVO;
import org.dandan.quartz.vo.SysQuartzLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/sysQuartzLog")
public class SysQuartzLogController {

    @Autowired
    private SysQuartzLogService sysQuartzLogService;

    @PostMapping
    public String save(@Valid @RequestBody SysQuartzLogVO vO) {
        return sysQuartzLogService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        sysQuartzLogService.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody SysQuartzLogUpdateVO vO) {
        sysQuartzLogService.update(id, vO);
    }

    @GetMapping("/{id}")
    public SysQuartzLogDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return sysQuartzLogService.getById(id);
    }

    @GetMapping
    public Page<SysQuartzLogDTO> query(@Valid SysQuartzLogQueryVO vO) {
        return sysQuartzLogService.query(vO);
    }
}
