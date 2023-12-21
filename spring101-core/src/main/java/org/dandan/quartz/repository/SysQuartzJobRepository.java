package org.dandan.quartz.repository;

import org.dandan.quartz.domain.SysQuartzJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SysQuartzJobRepository extends JpaRepository<SysQuartzJob, Long>, JpaSpecificationExecutor<SysQuartzJob> {

    List<SysQuartzJob> findByPauseIsFalse();
}