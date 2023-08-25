package org.dandan.system.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.dandan.system.dto.SysUserDTO;
import org.dandan.system.service.SysUserService;
import org.dandan.system.vo.SysUserQueryVO;
import org.dandan.system.vo.SysUserUpdateVO;
import org.dandan.system.vo.SysUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @PostMapping
    public String save(@Valid @RequestBody SysUserVO vO) {
        return sysUserService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        sysUserService.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody SysUserUpdateVO vO) {
        sysUserService.update(id, vO);
    }

    @GetMapping("/{id}")
    public SysUserDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return sysUserService.getById(id);
    }

    @GetMapping
    public Page<SysUserDTO> query(@Valid SysUserQueryVO vO) {
        return sysUserService.query(vO);
    }
}
