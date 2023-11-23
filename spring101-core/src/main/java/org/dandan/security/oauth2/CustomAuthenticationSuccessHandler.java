package org.dandan.security.oauth2;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dandan.security.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

//    private final TokenProvider tokenProvider;
    private final JwtService jwtService;

    @Value("${app.oauth2.redirectUri}")
    private String redirectUri;

    /**
     * 登入成功之後的hook
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("=== onAuthenticationSuccess");
        handle(request, response, authentication);
        super.clearAuthenticationAttributes(request);
    }

    /**
     * 產出 jwt token
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     */
    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = redirectUri.isEmpty() ?
                determineTargetUrl(request, response, authentication) : redirectUri;

        String token = jwtService.generate(authentication);

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl).queryParam("token", token).build().toUriString();
        log.info("=== targetUrl " + targetUrl);
        log.info("=== token " + token);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
