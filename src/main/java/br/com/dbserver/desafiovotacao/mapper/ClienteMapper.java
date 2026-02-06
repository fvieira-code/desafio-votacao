package br.com.dbserver.desafiovotacao.mapper;

import br.com.dbserver.desafiovotacao.dto.ClienteDTO;
import br.com.dbserver.desafiovotacao.model.Cliente;

public class ClienteMapper {

    public static ClienteDTO toDTO(Cliente entity) {
        return ClienteDTO.builder()
                .id(entity.getId())
                .razaoSocial(entity.getRazaoSocial())
                .nomeFantasia(entity.getNomeFantasia())
                .cnpj(entity.getCnpj())
                .endereco(entity.getEndereco())
                .build();
    }

    public static Cliente toEntity(ClienteDTO dto) {
        return Cliente.builder()
                .id(dto.getId())
                .razaoSocial(dto.getRazaoSocial())
                .nomeFantasia(dto.getNomeFantasia())
                .cnpj(dto.getCnpj())
                .endereco(dto.getEndereco())
                .build();
    }
}
