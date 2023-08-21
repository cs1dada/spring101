package org.dandan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .authorizeRequests(
//                    requests -> requests
//                            .requestMatchers("/auth/test").permitAll()
//
//////                            .requestMatchers("/swagger-ui.html").permitAll()
//////                            .requestMatchers("/test/admin").hasRole("ADMIN")
//////                            .requestMatchers("/test/normal").hasRole("NORMAL")
//////                            .anyRequest().permitAll()
////                            .anyRequest().authenticated()
////
//            )
//
//            .formLogin(withDefaults())
//            .httpBasic(withDefaults());
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(
                    requests -> requests
                            .anyRequest().permitAll()
            )
                .csrf().disable();


        return http.build();
    }
}
