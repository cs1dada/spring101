package org.dandan.vo;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class SysLogUpdateVO extends SysLogVO implements Serializable {
    private static final long serialVersionUID = 1L;

}
