package br.com.dbserver.desafiovotacao.mapper;

import br.com.dbserver.desafiovotacao.dto.ConsultorDTO;
import br.com.dbserver.desafiovotacao.model.Consultor;

public class ConsultorMapper {

    public static ConsultorDTO toDTO(Consultor entity) {
        return ConsultorDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .cpf(entity.getCpf())
                .rg(entity.getRg())
                .endereco(entity.getEndereco())
                .build();
    }

    public static Consultor toEntity(ConsultorDTO dto) {
        return Consultor.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .rg(dto.getRg())
                .endereco(dto.getEndereco())
                .build();
    }
}
