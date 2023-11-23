package org.dandan.system.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.dandan.security.dto.AuthorityDto;
import org.dandan.system.domain.Menu;
import org.dandan.system.domain.Role;
import org.dandan.system.domain.SysUser;
import org.dandan.system.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    public List<AuthorityDto> mapToGrantedAuthorities(SysUser user) {
        Set<String> permissions = new HashSet<>();
        if (user.getAdmin()) {
            permissions.add("admin");
            return permissions.stream().map(AuthorityDto::new)
                    .collect(Collectors.toList());
        }

        /*
        * flatMap(role -> role.getMenus().stream())
        * 使用 flatMap，將每個角色的菜單集合轉換為單獨的流，以便它們可以合併為一個大的流。
        * menu/Permission 存user權限
        * */

        Set<Role> roles = roleRepository.findByUserId(user.getUserId());
        permissions = roles.stream().flatMap(role -> role.getMenus().stream())
                .map(Menu::getPermission)
                .filter(StringUtils::isNotBlank).collect(Collectors.toSet());
        /*
        * 這段程式碼是將 permissions 集合中的每個權限字串轉換為 AuthorityDto 對象，然後收集到一個列表中。
        * */
        return permissions.stream().map(AuthorityDto::new)
                .collect(Collectors.toList());
    }
}
