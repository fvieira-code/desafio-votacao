package br.com.zukk.vivo.projectslicing.service;

import br.com.zukk.vivo.projectslicing.dto.ConsultorDTO;
import br.com.zukk.vivo.projectslicing.mapper.ConsultorMapper;
import br.com.zukk.vivo.projectslicing.repository.ConsultorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultorService {

    private final ConsultorRepository consultorRepository;

    public List<ConsultorDTO> listarTodos() {
        return consultorRepository.findAll()
                .stream()
                .map(ConsultorMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ConsultorDTO salvar(ConsultorDTO dto) {
        var entity = ConsultorMapper.toEntity(dto);
        var salvo = consultorRepository.save(entity);
        return ConsultorMapper.toDTO(salvo);
    }

    public ConsultorDTO buscarPorCpf(String cpf) {
        var consultor = consultorRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Consultor não encontrado"));
        return ConsultorMapper.toDTO(consultor);
    }

    public ConsultorDTO atualizar(Integer id, ConsultorDTO dto) {
        var existente = consultorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultor não encontrado"));

        existente.setNome(dto.getNome());
        existente.setCpf(dto.getCpf());
        existente.setRg(dto.getRg());
        existente.setEndereco(dto.getEndereco());

        return ConsultorMapper.toDTO(consultorRepository.save(existente));
    }

    public void deletar(Integer id) {
        consultorRepository.deleteById(id);
    }

    public Page<ConsultorDTO> listarPaginado(Pageable pageable) {
        return consultorRepository.findAll(pageable)
                .map(ConsultorMapper::toDTO);
    }

}
