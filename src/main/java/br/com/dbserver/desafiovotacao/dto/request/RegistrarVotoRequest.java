package br.com.dbserver.desafiovotacao.dto.request;

import br.com.dbserver.desafiovotacao.model.enums.ValorVoto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistrarVotoRequest(
        @NotBlank(message = "associadoId é obrigatório")
        @Size(max = 80, message = "associadoId deve ter no máximo 80 caracteres")
        String associadoId,

        @NotNull(message = "valor é obrigatório (SIM/NAO)")
        ValorVoto valor,

        String cpf
) {}
