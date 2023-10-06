package org.dandan.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface JwtService {
    //String generateToken(UserDetails userDetails);

    String generate(Authentication authentication);
    Optional<Jws<Claims>> validateTokenAndGetJws(String token);
}
