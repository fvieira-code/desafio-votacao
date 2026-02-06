package br.com.dbserver.desafiovotacao.controller;

import br.com.dbserver.desafiovotacao.dto.request.AbrirSessaoRequest;
import br.com.dbserver.desafiovotacao.dto.response.SessaoResponse;
import br.com.dbserver.desafiovotacao.mapper.VotacaoMapper;
import br.com.dbserver.desafiovotacao.service.SessaoVotacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pautas/{pautaId}/sessoes")
@RequiredArgsConstructor
@Tag(name = "Sessões", description = "Abertura e consulta de sessão de votação")
public class SessaoVotacaoController {

    private final SessaoVotacaoService sessaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Abrir uma sessão de votação para uma pauta",
            description = "A sessão fica aberta por duracaoSegundos ou 60s por default.")
    public SessaoResponse abrir(@PathVariable Long pautaId,
                                @RequestBody(required = false) AbrirSessaoRequest req) {
        Long duracao = (req == null) ? null : req.duracaoSegundos();
        var sessao = sessaoService.abrirSessao(pautaId, duracao);
        return VotacaoMapper.toResponse(sessao);
    }
}
