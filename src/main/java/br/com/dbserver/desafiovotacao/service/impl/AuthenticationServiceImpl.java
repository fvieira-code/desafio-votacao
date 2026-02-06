package br.com.dbserver.desafiovotacao.service.impl;


import br.com.dbserver.desafiovotacao.dto.UserDTO;
import br.com.dbserver.desafiovotacao.dto.request.SignUpRequest;
import br.com.dbserver.desafiovotacao.dto.request.SigninRequest;
import br.com.dbserver.desafiovotacao.dto.response.JwtAuthenticationResponse;
import br.com.dbserver.desafiovotacao.exception.RecursoNaoEncontradoException;
import br.com.dbserver.desafiovotacao.mapper.UserMapper;
import br.com.dbserver.desafiovotacao.model.Role;
import br.com.dbserver.desafiovotacao.model.User;
import br.com.dbserver.desafiovotacao.repository.UserRepository;
import br.com.dbserver.desafiovotacao.service.AuthenticationService;
import br.com.dbserver.desafiovotacao.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        var user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName())
                .email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER).build();
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public UserDTO atualizar(UserDTO dto) {
        User entity = userRepository.findById(Math.toIntExact(dto.getId()))
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setRole(dto.getRole());
        userRepository.save(entity);
        return new UserDTO(entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getEmail(), entity.getPassword(), entity.getRole());
    }

    @Override
    public List<UserDTO> buscarTodos() {
        return userRepository.findAll()
                .stream()
                .map(u -> new UserDTO(u.getId(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getPassword(), u.getRole()))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> buscarPorNome(String nome) {
        return userRepository.findByFirstNameContainingIgnoreCase(nome)
                .stream()
                .map(u -> new UserDTO(u.getId(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getPassword(), u.getRole()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO buscarPorId(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
        return userMapper.toDTO(user);
    }

    @Override
    public UserDTO getUsuarioLogado(String token) {
        String jwt = token.replace("Bearer ", "");
        String email = jwtService.extractUserName(jwt); /* Método para extrair email/username do token */

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com e-mail: " + email));

        return userMapper.toDTO(user);
    }

    @Override
    public UserDTO buscarEntidadePorEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com e-mail: " + email));

        return userMapper.toDTO(user);
    }

    @Override
    public Map<String, Object> refreshToken(UserDetails userDetails) {

        String novoToken = jwtService.generateToken(userDetails);
        Date expiration = jwtService.getExpiration(novoToken);

        Instant expirationInstant = expiration.toInstant();
        ZonedDateTime expirationBrasilia = expirationInstant.atZone(ZoneId.of("America/Sao_Paulo"));

        Map<String, Object> response = new HashMap<>();
        response.put("token", novoToken);
        response.put("expiresInMinutes", Duration.between(Instant.now(), expirationInstant).toMinutes());
        response.put("expiration", expirationBrasilia);

        return response;
    }

}
