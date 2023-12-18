package org.dandan.quartz.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务日志
 */
@Data
@Entity
@Table(name = "sys_quartz_log")
public class SysQuartzLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @Column(name = "log_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @Column(name = "bean_name")
    private String beanName;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "cron_expression")
    private String cronExpression;

    @Column(name = "is_success")
    private Boolean success;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "method_name")
    private String methodName;

    @Column(name = "params")
    private String params;

    @Column(name = "time")
    private Long time;

    @Column(name = "exception_detail")
    private String exceptionDetail;

}
