package br.com.zukk.vivo.projectslicing.service;

import br.com.zukk.vivo.projectslicing.dto.ClienteDTO;
import br.com.zukk.vivo.projectslicing.mapper.ClienteMapper;
import br.com.zukk.vivo.projectslicing.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteDTO salvar(ClienteDTO dto) {
        var entity = ClienteMapper.toEntity(dto);
        return ClienteMapper.toDTO(clienteRepository.save(entity));
    }

    public List<ClienteDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(ClienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ClienteDTO buscarPorCnpj(String cnpj) {
        var entity = clienteRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return ClienteMapper.toDTO(entity);
    }

    public ClienteDTO atualizar(Integer id, ClienteDTO dto) {
        var existente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        existente.setRazaoSocial(dto.getRazaoSocial());
        existente.setNomeFantasia(dto.getNomeFantasia());
        existente.setCnpj(dto.getCnpj());
        existente.setEndereco(dto.getEndereco());

        return ClienteMapper.toDTO(clienteRepository.save(existente));
    }

    public void deletar(Integer id) {
        clienteRepository.deleteById(id);
    }

    public Page<ClienteDTO> listarPaginado(Pageable pageable) {
        return clienteRepository.findAll(pageable).map(ClienteMapper::toDTO);
    }
}
