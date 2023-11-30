package org.dandan.rest;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import lombok.extern.slf4j.Slf4j;
import org.dandan.dto.SysLogDTO;
import org.dandan.service.SysLogService;
import org.dandan.vo.SysLogQueryVO;
import org.dandan.vo.SysLogUpdateVO;
import org.dandan.vo.SysLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/sysLog")
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    @PostMapping
    public String save(@Valid @RequestBody SysLogVO vO) {
        return sysLogService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        sysLogService.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody SysLogUpdateVO vO) {
        sysLogService.update(id, vO);
    }

    @GetMapping("/{id}")
    public SysLogDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return sysLogService.getById(id);
    }

    @GetMapping
    public Page<SysLogDTO> query(@Valid SysLogQueryVO vO) {
        return sysLogService.query(vO);
    }
}
