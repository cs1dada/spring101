package org.dandan.quartz.vo;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysQuartzLogQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * ID
     */
    private Long logId;

    private String beanName;

    private Date createTime;

    private String cronExpression;

    private Boolean success;

    private String jobName;

    private String methodName;

    private String params;

    private Long time;

    private String exceptionDetail;

}
