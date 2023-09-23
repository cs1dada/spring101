package org.dandan.security.service;

import lombok.RequiredArgsConstructor;
import org.dandan.security.dto.CustomUserDetails;
import org.dandan.security.dto.JwtUserDto;
import org.dandan.system.domain.SysUser;
import org.dandan.system.repository.SysUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SysUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        SysUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));

        //todo 還沒有role
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        //List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));

        return mapUserToCustomUserDetails(user, authorities);

//        JwtUserDto jwtUserDto;

//        Optional<SysUser> user = userRepository.findByUsername(username);
//        if (user.isEmpty()) {
//            throw new UsernameNotFoundException("");
//        } else {
//            jwtUserDto = new JwtUserDto(user.get());
//        }
//        return jwtUserDto;
    }

    private CustomUserDetails mapUserToCustomUserDetails(SysUser user, List<SimpleGrantedAuthority> authorities) {
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setId(user.getUserId());
        customUserDetails.setUsername(user.getUsername());
        customUserDetails.setPassword(user.getPassword());
        customUserDetails.setName(user.getNickName());
        customUserDetails.setEmail(user.getEmail());
        customUserDetails.setAuthorities(authorities);
        return customUserDetails;
    }
}
