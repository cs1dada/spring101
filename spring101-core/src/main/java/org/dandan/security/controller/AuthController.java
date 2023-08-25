package org.dandan.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.dandan.config.RsaProperties;
import org.dandan.security.dto.AuthUserDto;
import org.dandan.utils.RsaUtils;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "登入驗證")
public class AuthController {
    @Resource
    private AuthenticationManagerBuilder authenticationManagerBuilder;


    @PostMapping("/login")
    @Operation(summary = "登入api", description = "返回token")
    public ResponseEntity<Object> login(@RequestBody AuthUserDto authUser, HttpServletRequest request) throws Exception {

        //password解密
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey,"sa4ZxL+eQSnJyMmI2v+WLYBfl45RaJpaMuBtEHcT20qbN56uHCV9/+gk4OkS8cGXEmDagVYtqONUrNWhwRNE9A==");

        log.info( "Hello World! " + password + " username: " + authUser.getUsername());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authUser.getUsername(), password);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Map<String, Object> authInfo = new HashMap<String, Object>();
        authInfo.put("Principal", authentication.getPrincipal());


        return ResponseEntity.ok(authInfo);
    }

    @PostMapping("/test")
    @Operation(summary = "post測試api", description = "返回token")
    public ResponseEntity<Object> test(@RequestBody AuthUserDto authUser, HttpServletRequest request) throws Exception {

        Map<String, Object> authInfo = new HashMap<String, Object>();
        authInfo.put("token", "11112222333");


        return ResponseEntity.ok(authInfo);
    }

    @GetMapping(value = "/info")
    public ResponseEntity<Object> getUserInfo() {
        Map<String, Object> user = new HashMap<String, Object>();
        user.put("user", "11111111111111111111111111");
        return ResponseEntity.ok(user);
    }
}
