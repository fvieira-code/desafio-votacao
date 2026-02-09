package br.com.dbserver.desafiovotacao.service;

import br.com.dbserver.desafiovotacao.model.enums.StatusSessao;

public interface ResultadoService {

    record Resultado(
            Long pautaId,
            long totalSim,
            long totalNao,
            long total,
            StatusSessao statusSessao,
            String resultado
    ) {}

    Resultado obterResultado(Long pautaId);
}
