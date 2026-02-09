package br.com.dbserver.desafiovotacao.dto.response;

import br.com.dbserver.desafiovotacao.model.enums.StatusSessao;

import java.time.LocalDateTime;

public record SessaoResponse(
        Long id,
        Long pautaId,
        LocalDateTime abertaEm,
        LocalDateTime encerraEm,
        StatusSessao status
) {}
