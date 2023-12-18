package org.dandan.quartz.vo;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Data
public class SysQuartzLogVO implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * ID
     */
    @NotNull(message = "logId can not null")
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
