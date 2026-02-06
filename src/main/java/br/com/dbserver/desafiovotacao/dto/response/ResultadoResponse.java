package br.com.dbserver.desafiovotacao.dto.response;

import br.com.dbserver.desafiovotacao.model.enums.StatusSessao;

public record ResultadoResponse(
        Long pautaId,
        long totalSim,
        long totalNao,
        long total,
        StatusSessao statusSessao,
        String resultado
) {}
