package org.dandan.quartz.dto;

import lombok.Data;

@Data
public class QuartzJobInfoDTO {
    private String name;
    private String group;
    private String jobClass;
    private boolean isDurable;

    public QuartzJobInfoDTO(String name, String group, String jobClass, boolean isDurable) {
        this.name = name;
        this.group = group;
        this.jobClass = jobClass;
        this.isDurable = isDurable;
    }
}
