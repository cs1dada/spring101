package org.dandan.quartz.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务
 */
@Data
@Entity
@Table(name = "sys_quartz_job")
public class SysQuartzJob implements Serializable {

    public static final String JOB_KEY = "JOB_KEY";

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @Column(name = "job_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    /**
     * Spring Bean名称
     */
    @Column(name = "bean_name")
    private String beanName;

    @Transient
    private String uuid;

    /**
     * cron 表达式
     */
    @Column(name = "cron_expression")
    private String cronExpression;

    /**
     * 状态：1暂停、0启用
     */
    @Column(name = "is_pause")
    private Boolean pause = false;

    /**
     * 任务名称
     */
    @Column(name = "job_name")
    private String jobName;

    /**
     * 方法名称
     */
    @Column(name = "method_name")
    private String methodName;

    /**
     * 参数
     */
    @Column(name = "params")
    private String params;

    /**
     * 备注
     */
    @Column(name = "description")
    private String description;

    /**
     * 负责人
     */
    @Column(name = "person_in_charge")
    private String personInCharge;

    /**
     * 报警邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 子任务ID
     */
    @Column(name = "sub_task")
    private String subTask;

    /**
     * 任务失败后是否暂停
     */
    @Column(name = "pause_after_failure")
    private Boolean pauseAfterFailure;

    /**
     * 创建者
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 更新者
     */
    @Column(name = "update_by")
    private String updateBy;

    /**
     * 创建日期
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

}
