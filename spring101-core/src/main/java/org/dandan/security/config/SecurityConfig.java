package org.dandan.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dandan.security.oauth2.CustomAuthenticationSuccessHandler;
import org.dandan.security.oauth2.CustomOAuth2UserService;
import org.dandan.security.oauth2.OAuth2UserInfoExtractor;
import org.dandan.security.service.CustomUserDetailsService;
import org.dandan.security.service.TokenAuthenticationFilter;
import org.dandan.security.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Collections;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOauth2UserService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomUserDetailsService customUserDetailsService;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http
            .authorizeHttpRequests(requests -> requests
                    .requestMatchers("/public/**", "/auth/**", "/oauth2/**").permitAll()
                    .requestMatchers("/", "/error", "/csrf", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()
    //                        .requestMatchers("/sysUser/**").hasAnyRole("menu:add", "user:list") //role無效
    //                        .requestMatchers("/sysUser/**").hasAnyAuthority("user:list") //auth有效
                    .anyRequest().authenticated())
            .oauth2Login(oauth2Login -> oauth2Login
                    //oauth2取得user資料
                    .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                            .userService(customOauth2UserService)
                            //.oidcUserService(customOidcUserService())
                    )
                    //登入成功後, 回傳token
                    .successHandler(customAuthenticationSuccessHandler)
            )
            .logout(l -> l.logoutSuccessUrl("/").permitAll())
            .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
            .authenticationProvider(authenticationProvider());


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationProvider: 這是一個Spring Security的介面，用於執行身份驗證。
     * 具體實現是 DaoAuthenticationProvider，它使用 UserDetailsService 提供的使用者資訊進行身份驗證。
     *
     * DaoAuthenticationProvider: 是 AuthenticationProvider 的實現之一。
     * 它使用 UserDetailsService 和 PasswordEncoder 進行身份驗證。
     * UserDetailsService 提供使用者的資訊，而 PasswordEncoder 用於將使用者輸入的密碼進行加密，然後與儲存在資料庫中的加密密碼進行比對。
     * @return
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * 這個方法使用 ProviderManager 並將你的自定義 AuthenticationProvider 注入其中。
     * 這樣的設置允許你使用自定義的 AuthenticationProvider 來處理身份驗證。
     * @param authenticationProvider
     * @return
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
        return new ProviderManager(Collections.singletonList(authenticationProvider));
    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
//            throws Exception {
//        return config.getAuthenticationManager();
//    }

}
