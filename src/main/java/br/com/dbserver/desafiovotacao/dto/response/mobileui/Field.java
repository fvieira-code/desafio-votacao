package br.com.dbserver.desafiovotacao.dto.response.mobileui;

import java.util.List;

public record Field(
        String id,
        String label,
        FieldType type,
        boolean required,
        String placeholder,
        List<FieldOption> options
) {}
