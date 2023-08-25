package org.dandan.system.service;

import org.dandan.system.domain.SysUser;
import org.dandan.system.dto.SysUserDTO;
import org.dandan.system.repository.SysUserRepository;
import org.dandan.system.vo.SysUserQueryVO;
import org.dandan.system.vo.SysUserUpdateVO;
import org.dandan.system.vo.SysUserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class SysUserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    public Long save(SysUserVO vO) {
        SysUser bean = new SysUser();
        BeanUtils.copyProperties(vO, bean);
        bean = sysUserRepository.save(bean);
        return bean.getUserId();
    }

    public void delete(Long id) {
        sysUserRepository.deleteById(id);
    }

    public void update(Long id, SysUserUpdateVO vO) {
        SysUser bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        sysUserRepository.save(bean);
    }

    public SysUserDTO getById(Long id) {
        SysUser original = requireOne(id);
        return toDTO(original);
    }

    public Page<SysUserDTO> query(SysUserQueryVO vO) {
        throw new UnsupportedOperationException();
    }

    private SysUserDTO toDTO(SysUser original) {
        SysUserDTO bean = new SysUserDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private SysUser requireOne(Long id) {
        return sysUserRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
