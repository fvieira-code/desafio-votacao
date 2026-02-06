package br.com.dbserver.desafiovotacao.controller;

import br.com.dbserver.desafiovotacao.dto.UserDTO;
import br.com.dbserver.desafiovotacao.dto.request.SignUpRequest;
import br.com.dbserver.desafiovotacao.dto.request.SigninRequest;
import br.com.dbserver.desafiovotacao.dto.response.JwtAuthenticationResponse;
import br.com.dbserver.desafiovotacao.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> users(@RequestParam(required = false) String nome) {
        if (nome != null && !nome.isBlank()) {
            return ResponseEntity.ok(authenticationService.buscarPorNome(nome));
        }
        return ResponseEntity.ok(authenticationService.buscarTodos());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(authenticationService.buscarPorId(id));
    }

}
