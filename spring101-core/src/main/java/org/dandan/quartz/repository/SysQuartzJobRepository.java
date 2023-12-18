package org.dandan.quartz.repository;

import org.dandan.quartz.domain.SysQuartzJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SysQuartzJobRepository extends JpaRepository<SysQuartzJob, Long>, JpaSpecificationExecutor<SysQuartzJob> {

}