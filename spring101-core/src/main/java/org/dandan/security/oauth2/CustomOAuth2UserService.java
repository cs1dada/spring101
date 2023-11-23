package org.dandan.security.oauth2;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dandan.security.dto.AuthorityDto;
import org.dandan.security.dto.CustomUserDetails;
import org.dandan.security.service.UserService;
import org.dandan.system.domain.Role;
import org.dandan.system.domain.SysUser;
import org.dandan.system.service.RoleService;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;
    private final RoleService roleService;
    private final List<OAuth2UserInfoExtractor> oAuth2UserInfoExtractors;

//    public CustomOAuth2UserService(UserService userService, RoleService roleService, List<OAuth2UserInfoExtractor> oAuth2UserInfoExtractors) {
//        this.userService = userService;
//        this.roleService = roleService;
//        this.oAuth2UserInfoExtractors = oAuth2UserInfoExtractors;
//    }

    /**
     *
     * 取得social user資料, 注入spring context
     * @param userRequest
     * @return
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        log.info("=== OAuth2User loadUser");
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Optional<OAuth2UserInfoExtractor> oAuth2UserInfoExtractorOptional = oAuth2UserInfoExtractors.stream()
                .filter(oAuth2UserInfoExtractor -> oAuth2UserInfoExtractor.accepts(userRequest))
                .findFirst();
        if (oAuth2UserInfoExtractorOptional.isEmpty()) {
            throw new InternalAuthenticationServiceException("The OAuth2 provider is not supported yet");
        }
        //取得oAuth2User 並注入到CustomUserDetails
        CustomUserDetails customUserDetails = oAuth2UserInfoExtractorOptional.get().extractUserInfo(oAuth2User);
        //oAuth2User 轉成 SysUser 存入db
        SysUser user = upsertUser(customUserDetails);
        //更新CustomUserDetails (id, authority)
        updateCustomUserDetails(customUserDetails, user);
        return customUserDetails;
    }

    /**
     * 更新CustomUserDetails 的 id 跟 authorities
     * @param customUserDetails
     * @param user
     */
    private void updateCustomUserDetails(CustomUserDetails customUserDetails, SysUser user){
        customUserDetails.setId(user.getUserId());

        // 取出user's AuthorityDto
        List<AuthorityDto> authorityDtoList = roleService.mapToGrantedAuthorities(user);
        List<SimpleGrantedAuthority> authorities = authorityDtoList.stream()
                .map(authorityDto -> new SimpleGrantedAuthority(authorityDto.getAuthority()))
                .collect(Collectors.toList());
        customUserDetails.setAuthorities(authorities);
    }

    /**
     * 新增user
     * @param customUserDetails
     * @return
     */
    private SysUser upsertUser(CustomUserDetails customUserDetails) {
        //碰SysUser取出User資料
        Optional<SysUser> userOptional = userService.findByEmail(customUserDetails.getUsername());
        SysUser user;
        //第一次登入, 要把social user轉成 SysUser
        if (userOptional.isEmpty()) {
            user = new SysUser();
            user.setUsername(customUserDetails.getUsername());
            user.setNickName(customUserDetails.getName());
            user.setEmail(customUserDetails.getEmail());
            user.setAvatarPath(customUserDetails.getAvatarUrl());
            user.setProvider(customUserDetails.getProvider());
            user.setAdmin(Boolean.FALSE);
            user.setEnabled(1L);
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            //普通用戶 role id 2
            Optional<Role> normal = roleService.findById(2L);
            user.setRoles(normal.map(Collections::singleton).orElse(Collections.emptySet()));

        } else {
            log.info("=== OAuth2User updateUser");
            user = userOptional.get();
            user.setEmail(customUserDetails.getEmail());
            user.setAvatarPath(customUserDetails.getAvatarUrl());
            user.setUpdateTime(new Date());
        }
        return userService.saveUser(user);
    }
}
