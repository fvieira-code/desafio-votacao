package br.com.dbserver.desafiovotacao.mapper;

import br.com.dbserver.desafiovotacao.dto.ClienteDTO;
import br.com.dbserver.desafiovotacao.dto.ConsultorDTO;
import br.com.dbserver.desafiovotacao.dto.PontoDTO;
import br.com.dbserver.desafiovotacao.model.Cliente;
import br.com.dbserver.desafiovotacao.model.Consultor;
import br.com.dbserver.desafiovotacao.model.Ponto;
import br.com.dbserver.desafiovotacao.model.*;

public class PontoMapper {

    public static PontoDTO toDTO(Ponto entity) {
        if (entity == null) return null;

        return PontoDTO.builder()
                .id(entity.getId())
                .atividade(entity.getAtividade())
                .data(entity.getData())
                .dia(entity.getDia())
                .inicio(entity.getInicio())
                .fim(entity.getFim())
                .total(entity.getTotal())
                .status(entity.getStatus())
                .ticket(entity.getTicket())
                .idConsultor(entity.getConsultor() != null ? entity.getConsultor().getId() : null)
                .idCliente(entity.getCliente() != null ? entity.getCliente().getId() : null)
                .consultor(toConsultorDTO(entity.getConsultor()))
                .cliente(toClienteDTO(entity.getCliente()))
                .build();
    }

    public static Ponto toEntity(PontoDTO dto, Consultor consultor, Cliente cliente) {
        if (dto == null) return null;

        return Ponto.builder()
                .id(dto.getId())
                .atividade(dto.getAtividade())
                .data(dto.getData())
                .dia(dto.getDia())
                .inicio(dto.getInicio())
                .fim(dto.getFim())
                .total(dto.getTotal())
                .status(dto.getStatus())
                .ticket(dto.getTicket())
                .consultor(consultor)
                .cliente(cliente)
                .build();
    }

    private static ConsultorDTO toConsultorDTO(Consultor consultor) {
        if (consultor == null) return null;

        return ConsultorDTO.builder()
                .id(consultor.getId())
                .nome(consultor.getNome())
                .cpf(consultor.getCpf())
                .rg(consultor.getRg())
                .endereco(consultor.getEndereco())
                .build();
    }

    private static ClienteDTO toClienteDTO(Cliente cliente) {
        if (cliente == null) return null;

        return ClienteDTO.builder()
                .id(cliente.getId())
                .cnpj(cliente.getCnpj())
                .razaoSocial(cliente.getRazaoSocial())
                .nomeFantasia(cliente.getNomeFantasia())
                .endereco(cliente.getEndereco())
                .build();
    }
}

