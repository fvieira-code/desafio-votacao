package br.com.dbserver.desafiovotacao.controller;

import br.com.dbserver.desafiovotacao.dto.request.RegistrarVotoRequest;
import br.com.dbserver.desafiovotacao.dto.response.VotoResponse;
import br.com.dbserver.desafiovotacao.mapper.VotacaoMapper;
import br.com.dbserver.desafiovotacao.service.VotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pautas/{pautaId}/votos")
@RequiredArgsConstructor
@Tag(name = "Votos", description = "Registro de votos (SIM/NAO), 1 voto por associado por pauta")
public class VotoController {

    private final VotoService votoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar voto em uma pauta")
    public VotoResponse votar(@PathVariable Long pautaId,
                              @Valid @RequestBody RegistrarVotoRequest req) {
        var voto = votoService.votar(pautaId, req.associadoId(), req.valor(), req.cpf());
        return VotacaoMapper.toResponse(voto);
    }
}
