package br.com.dbserver.desafiovotacao.controller;

import br.com.dbserver.desafiovotacao.dto.response.ResultadoResponse;
import br.com.dbserver.desafiovotacao.mapper.VotacaoMapper;
import br.com.dbserver.desafiovotacao.service.ResultadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pautas/{pautaId}/resultado")
@RequiredArgsConstructor
@Tag(name = "Resultado", description = "Contabilização e resultado da votação")
public class ResultadoController {

    private final ResultadoService resultadoService;

    @GetMapping
    @Operation(summary = "Obter resultado da votação da pauta")
    public ResultadoResponse resultado(@PathVariable Long pautaId) {
        var resultado = resultadoService.obterResultado(pautaId);
        return VotacaoMapper.toResponse(resultado);
    }
}
