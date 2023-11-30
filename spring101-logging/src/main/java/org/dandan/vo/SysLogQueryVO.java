package org.dandan.vo;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysLogQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * ID
     */
    private Long logId;

    private String description;

    private String logType;

    private String method;

    private String requestIp;

    private Long time;

    private String username;

    private String address;

    private String browser;

    private Date createTime;

    private String params;

    private String exceptionDetail;

}
