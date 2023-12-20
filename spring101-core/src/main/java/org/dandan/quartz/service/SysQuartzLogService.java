package org.dandan.quartz.service;

import org.dandan.quartz.domain.SysQuartzLog;
import org.dandan.quartz.dto.SysQuartzLogDTO;
import org.dandan.quartz.repository.SysQuartzLogRepository;
import org.dandan.quartz.vo.SysQuartzLogQueryVO;
import org.dandan.quartz.vo.SysQuartzLogUpdateVO;
import org.dandan.quartz.vo.SysQuartzLogVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class SysQuartzLogService {

    @Autowired
    private SysQuartzLogRepository sysQuartzLogRepository;

    public Long save(SysQuartzLogVO vO) {
        SysQuartzLog bean = new SysQuartzLog();
        BeanUtils.copyProperties(vO, bean);
        bean = sysQuartzLogRepository.save(bean);
        return bean.getLogId();
    }

    public void delete(Long id) {
        sysQuartzLogRepository.deleteById(id);
    }

    public void update(Long id, SysQuartzLogUpdateVO vO) {
        SysQuartzLog bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        sysQuartzLogRepository.save(bean);
    }

    public SysQuartzLogDTO getById(Long id) {
        SysQuartzLog original = requireOne(id);
        return toDTO(original);
    }

    public Page<SysQuartzLogDTO> query(SysQuartzLogQueryVO vO) {
        throw new UnsupportedOperationException();
    }

    private SysQuartzLogDTO toDTO(SysQuartzLog original) {
        SysQuartzLogDTO bean = new SysQuartzLogDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private SysQuartzLog requireOne(Long id) {
        return sysQuartzLogRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
