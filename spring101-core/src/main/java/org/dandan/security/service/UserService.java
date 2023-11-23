package org.dandan.security.service;

import org.dandan.system.domain.SysUser;

import java.util.Optional;

public interface UserService {
    boolean hasUserWithUsername(String username);

    boolean hasUserWithEmail(String email);

    SysUser saveUser(SysUser user);

    Optional<SysUser> findByUsername(String name);

    Optional<SysUser> findByEmail(String email);

}
