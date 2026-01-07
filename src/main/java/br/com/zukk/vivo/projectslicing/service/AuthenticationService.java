package br.com.zukk.vivo.projectslicing.service;

import br.com.zukk.vivo.projectslicing.dto.UserDTO;
import br.com.zukk.vivo.projectslicing.dto.request.SignUpRequest;
import br.com.zukk.vivo.projectslicing.dto.request.SigninRequest;
import br.com.zukk.vivo.projectslicing.dto.response.JwtAuthenticationResponse;
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
