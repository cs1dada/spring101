package org.dandan.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.dandan.security.service.UserService;
import org.dandan.system.domain.SysUser;
import org.dandan.system.repository.SysUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final SysUserRepository userRepository;

    @Override
    public boolean hasUserWithUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public SysUser saveUser(SysUser user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<SysUser> findByUsername(String name) {
        return userRepository.findByUsername(name);
    }


}
