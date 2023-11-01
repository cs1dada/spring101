package org.dandan.system.service;

import lombok.RequiredArgsConstructor;
import org.dandan.security.dto.AuthorityDto;
import org.dandan.system.domain.SysUser;
import org.dandan.system.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    public List<AuthorityDto> mapToGrantedAuthorities(SysUser user) {
        Set<String> permissions = new HashSet<>();
        if (user.getAdmin()) {
            permissions.add("admin");
            return permissions.stream().map(AuthorityDto::new)
                    .collect(Collectors.toList());
        }

        return permissions.stream().map(AuthorityDto::new)
                .collect(Collectors.toList());
    }
}
