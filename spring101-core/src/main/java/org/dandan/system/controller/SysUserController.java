package org.dandan.system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.dandan.system.dto.SysUserDTO;
import org.dandan.system.service.SysUserService;
import org.dandan.system.vo.SysUserQueryVO;
import org.dandan.system.vo.SysUserUpdateVO;
import org.dandan.system.vo.SysUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static org.dandan.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;
@Slf4j
@Validated
@RestController
@RequestMapping("/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public String save(@Valid @RequestBody SysUserVO vO) {
        return sysUserService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        sysUserService.delete(id);
    }

    @PutMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody SysUserUpdateVO vO) {
        sysUserService.update(id, vO);
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('menu:add', 'user:list')") //無效
    @PreAuthorize("hasAnyAuthority('menu:add')") //有效
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public SysUserDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return sysUserService.getById(id);
    }

    @GetMapping("/test/{id}")
//    @PreAuthorize("hasAnyRole('menu:add', 'user:list')") //無效
    @PreAuthorize("hasAnyAuthority('user:list')") //無效
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public SysUserDTO getById2(@Valid @NotNull @PathVariable("id") Long id) {
        return sysUserService.getById(id);
    }

    @GetMapping
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public Page<SysUserDTO> query(@Valid SysUserQueryVO vO) {
        return sysUserService.query(vO);
    }
}
