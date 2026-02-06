package br.com.dbserver.desafiovotacao.service;

import br.com.dbserver.desafiovotacao.dto.UserDTO;
import br.com.dbserver.desafiovotacao.dto.request.SignUpRequest;
import br.com.dbserver.desafiovotacao.dto.request.SigninRequest;
import br.com.dbserver.desafiovotacao.dto.response.JwtAuthenticationResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;

public interface AuthenticationService {

    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);

    UserDTO atualizar(UserDTO dto);

    List<UserDTO> buscarTodos();

    List<UserDTO> buscarPorNome(String nome);

    UserDTO buscarPorId(Integer id);

    UserDTO getUsuarioLogado(String token);

    UserDTO buscarEntidadePorEmail(String email);

    Map<String, Object>  refreshToken(UserDetails userDetails);

}
