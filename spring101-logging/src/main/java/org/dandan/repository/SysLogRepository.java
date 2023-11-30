package org.dandan.repository;


import org.dandan.domain.SysLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SysLogRepository extends JpaRepository<SysLog, Long>, JpaSpecificationExecutor<SysLog> {

}