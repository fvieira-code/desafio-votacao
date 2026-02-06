package br.com.dbserver.desafiovotacao.controller;

import br.com.dbserver.desafiovotacao.dto.request.CriarPautaRequest;
import br.com.dbserver.desafiovotacao.dto.response.PautaResponse;
import br.com.dbserver.desafiovotacao.mapper.VotacaoMapper;
import br.com.dbserver.desafiovotacao.service.PautaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pautas")
@RequiredArgsConstructor
@Tag(name = "Pautas", description = "Cadastro de pautas")
public class PautaController {

    private final PautaService pautaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar uma nova pauta")
    public PautaResponse criar(@Valid @RequestBody CriarPautaRequest req) {
        var pauta = pautaService.criar(req.titulo(), req.descricao());
        return VotacaoMapper.toResponse(pauta);
    }
}
