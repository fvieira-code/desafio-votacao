package br.com.dbserver.desafiovotacao.dto.response.mobileui;

import java.util.Map;

public record Action(
        String label,
        String url,
        Map<String, Object> body
) {}
