package org.dandan.system.vo;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class SysUserUpdateVO extends SysUserVO implements Serializable {
    private static final long serialVersionUID = 1L;

}
