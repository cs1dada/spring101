package org.dandan.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.dandan.security.dto.JwtUserDto;
import org.dandan.security.service.UserService;
import org.dandan.system.domain.SysUser;
import org.dandan.system.repository.SysUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final SysUserRepository userRepository;
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                JwtUserDto jwtUserDto;
                Optional<SysUser> user = userRepository.findByUsername(username);
                if (user.isEmpty()) {
                    throw new UsernameNotFoundException("");
                } else {
                    jwtUserDto = new JwtUserDto(user.get());
                }
                return jwtUserDto;
            }
        };
    }
}
