package br.com.dbserver.desafiovotacao.dto.response;

import br.com.dbserver.desafiovotacao.model.enums.ValorVoto;

import java.time.LocalDateTime;

public record VotoResponse(
        Long id,
        Long pautaId,
        String associadoId,
        ValorVoto valor,
        LocalDateTime criadoEm
) {}
