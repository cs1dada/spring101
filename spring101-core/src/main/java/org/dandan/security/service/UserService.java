package org.dandan.security.service;

import org.dandan.system.domain.SysUser;

public interface UserService {
    boolean hasUserWithUsername(String username);

    boolean hasUserWithEmail(String email);

    SysUser saveUser(SysUser user);

}
