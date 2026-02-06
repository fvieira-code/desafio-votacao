package br.com.dbserver.desafiovotacao.dto.response;

import java.time.LocalDateTime;

public record PautaResponse(
        Long id,
        String titulo,
        String descricao,
        LocalDateTime criadaEm
) {}
