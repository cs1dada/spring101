package org.dandan.config;

import lombok.extern.slf4j.Slf4j;
import org.dandan.utils.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Service(value = "el"): 這是Spring的@Service注解，表明這是一個Service類，並且指定了bean的名稱為"el"。
 * 當在其他地方需要注入這個Service時，可以使用@Qualifier("el")來指定注入的bean。
 */
@Slf4j
@Service(value = "el")
public class AuthorityConfig {
    public Boolean check(String... permissions) {

        List<String> elPermissions = SecurityUtils.getCurrentUser().getAuthorities().stream().filter(Objects::nonNull).map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        if(elPermissions.isEmpty()){
            log.info("permissions is empty ...");
        }

        return elPermissions.contains("admin") || Arrays.stream(permissions).anyMatch(elPermissions::contains);
    }
}
