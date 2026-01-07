package br.com.zukk.vivo.projectslicing.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface JwtService {
    String extractUserName(String token);

    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);

    Date extractExpiration(String token);

    Date getExpiration(String token);

}
