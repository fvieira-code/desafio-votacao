package br.com.dbserver.desafiovotacao.dto.response.mobileui;

import java.util.List;

public record ScreenResponse(
        ScreenType type,
        String title,
        String description,
        List<Field> fields,
        Action primaryAction,
        Action secondaryAction,
        List<SelectionItem> items
) {}
