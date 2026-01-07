package br.com.zukk.vivo.projectslicing.controller;

import br.com.zukk.vivo.projectslicing.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/token")
@RequiredArgsConstructor
public class TokenController {

    private final JwtService jwtService;

    @GetMapping("/expiration")
    public ResponseEntity<Map<String, Object>> getExpiration(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Token inválido"));
        }

        String token = authHeader.substring(7);
        Date expiration = jwtService.getExpiration(token);

        return ResponseEntity.ok(Map.of(
                "expiration", expiration,
                "timestamp", expiration.getTime(),
                "expiresInSeconds", (expiration.getTime() - System.currentTimeMillis()) / 1000,
                "expiresInMinutes", (expiration.getTime() - System.currentTimeMillis()) / (1000 * 60)
        ));
    }
}
