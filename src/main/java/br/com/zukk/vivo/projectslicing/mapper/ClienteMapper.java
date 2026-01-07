package br.com.zukk.vivo.projectslicing.mapper;

import br.com.zukk.vivo.projectslicing.dto.ClienteDTO;
import br.com.zukk.vivo.projectslicing.model.Cliente;

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
