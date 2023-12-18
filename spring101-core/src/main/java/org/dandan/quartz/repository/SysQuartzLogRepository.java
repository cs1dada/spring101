package org.dandan.quartz.repository;

import org.dandan.quartz.domain.SysQuartzLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SysQuartzLogRepository extends JpaRepository<SysQuartzLog, Long>, JpaSpecificationExecutor<SysQuartzLog> {

}