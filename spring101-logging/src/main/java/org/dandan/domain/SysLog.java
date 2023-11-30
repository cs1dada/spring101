package org.dandan.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 系统日志
 */
@Data
@Entity
@Table(name = "sys_log")
public class SysLog implements Serializable {

    private static final long serialVersionUID = 1L;

    public SysLog(String logType, Long time) {
        this.logType = logType;
        this.time = time;
    }

    public SysLog() {

    }
    /**
     * ID
     */
    @Id
    @Column(name = "log_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @Column(name = "description")
    private String description;

    @Column(name = "log_type")
    private String logType;

    @Column(name = "method")
    private String method;

    @Column(name = "request_ip")
    private String requestIp;

    @Column(name = "time")
    private Long time;

    @Column(name = "username")
    private String username;

    @Column(name = "address")
    private String address;

    @Column(name = "browser")
    private String browser;

    @Column(name = "create_time")
    @CreationTimestamp
    private Timestamp createTime;

//    @Column(name = "create_time")
//    private Date createTime;

    @Column(name = "params")
    private String params;

    @Column(name = "exception_detail")
    private byte[] exceptionDetail;


}
