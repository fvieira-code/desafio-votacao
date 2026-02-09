package br.com.dbserver.desafiovotacao.dto.response.mobileui;

public record SelectionItem(
        String title,
        String description,
        Action action
) {}

