package br.com.dbserver.desafiovotacao.controller;

import br.com.dbserver.desafiovotacao.dto.response.mobileui.*;
import br.com.dbserver.desafiovotacao.model.enums.ValorVoto;
import br.com.dbserver.desafiovotacao.service.ResultadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static br.com.dbserver.desafiovotacao.dto.response.mobileui.FieldType.*;

@RestController
@RequestMapping("/api/v1/mobile")
@RequiredArgsConstructor
@Tag(name = "Mobile UI", description = "Endpoints que retornam telas (FORMULÁRIO/SELEÇÃO) para o app montar a UI.")
public class MobileUiController {

    private static final String URL_CRIAR_PAUTA = "/api/v1/pautas";
    private static final String URL_ABRIR_SESSAO = "/api/v1/pautas/{{pautaId}}/sessoes";
    private static final String URL_VOTAR = "/api/v1/pautas/{{pautaId}}/votos";
    private static final String URL_RESULTADO = "/api/v1/pautas/{{pautaId}}/resultado";

    private final ResultadoService resultadoService;

    @GetMapping("/home")
    @Operation(summary = "Tela inicial (SELEÇÃO)", description = "Retorna opções para navegar entre os fluxos do aplicativo.")
    public ScreenResponse home() {
        var items = List.of(
                new SelectionItem(
                        "Cadastrar pauta",
                        "Crie uma nova pauta para votação",
                        new Action("Abrir", "/api/v1/mobile/pautas/nova", Map.of())
                ),
                new SelectionItem(
                        "Abrir sessão",
                        "Abra uma sessão de votação para uma pauta",
                        new Action("Abrir", "/api/v1/mobile/sessoes/abrir", Map.of())
                ),
                new SelectionItem(
                        "Votar",
                        "Registre o voto (SIM/NÃO) para uma pauta",
                        new Action("Abrir", "/api/v1/mobile/votos/registrar", Map.of())
                ),
                new SelectionItem(
                        "Resultado",
                        "Consulte o resultado da votação",
                        new Action("Abrir", "/api/v1/mobile/resultados/consultar", Map.of())
                )
        );

        return new ScreenResponse(
                ScreenType.SELECAO,
                "Assembláia - Votação",
                "Selecione uma opção:",
                null,
                null,
                null,
                items
        );
    }

    @GetMapping("/pautas/nova")
    @Operation(summary = "Tela FORMULÁRIO: Nova pauta")
    public ScreenResponse novaPauta() {
        var fields = List.of(
                new Field("titulo", "Título", TEXT, true, "Ex.: Aprovar orçamento 2026", null),
                new Field("descricao", "Descrição", TEXT, false, "Detalhes da pauta (opcional)", null)
        );

        var primary = new Action(
                "Salvar",
                URL_CRIAR_PAUTA,
                Map.of()
        );

        return new ScreenResponse(
                ScreenType.FORMULARIO,
                "Nova pauta",
                "Preencha os campos e toque em Salvar.",
                fields,
                primary,
                null,
                null
        );
    }

    @GetMapping("/sessoes/abrir")
    @Operation(summary = "Tela FORMULÁRIO: Abrir sessão")
    public ScreenResponse abrirSessao() {
        var fields = List.of(
                new Field("pautaId", "ID da Pauta", NUMBER, true, "Ex.: 1", null),
                new Field("duracaoSegundos", "Duração (segundos)", NUMBER, false, "Default: 60", null)
        );

        var primary = new Action(
                "Abrir sessão",
                URL_ABRIR_SESSAO,
                Map.of()
        );

        return new ScreenResponse(
                ScreenType.FORMULARIO,
                "Abrir sessão",
                "Informe o ID da pauta e, se desejar, a duração.",
                fields,
                primary,
                null,
                null
        );
    }

    @GetMapping("/votos/registrar")
    @Operation(summary = "Tela FORMULÁRIO: Registrar voto")
    public ScreenResponse registrarVoto() {
        var options = List.of(
                new FieldOption("Sim", ValorVoto.SIM.name()),
                new FieldOption("Não", ValorVoto.NAO.name())
        );

        var fields = List.of(
                new Field("pautaId", "ID da Pauta", NUMBER, true, "Ex.: 1", null),
                new Field("associadoId", "ID do Associado", TEXT, true, "Ex.: A-123", null),
                new Field("valor", "Voto", SELECT, true, null, options),
                new Field("cpf", "CPF (opcional)", TEXT, false, "Somente para o bônus do CPF fake", null)
        );

        var primary = new Action(
                "Enviar voto",
                URL_VOTAR,
                Map.of()
        );

        return new ScreenResponse(
                ScreenType.FORMULARIO,
                "Registrar voto",
                "Preencha os campos e envie o voto.",
                fields,
                primary,
                null,
                null
        );
    }

    @GetMapping("/resultados/consultar")
    @Operation(summary = "Tela FORMULÁRIO: Consultar resultado")
    public ScreenResponse consultarResultado() {
        var fields = List.of(
                new Field("pautaId", "ID da Pauta", NUMBER, true, "Ex.: 1", null)
        );

        var primary = new Action(
                "Consultar",
                "/api/v1/mobile/resultados",
                Map.of()
        );

        return new ScreenResponse(
                ScreenType.FORMULARIO,
                "Resultado",
                "Informe a pauta e consulte o resultado.",
                fields,
                primary,
                null,
                null
        );
    }

    @PostMapping("/resultados")
    @Operation(summary = "Proxy POST para resultado", description = "Recebe pautaId no body e devolve uma tela SELECAO com o resultado.")
    public ScreenResponse resultadoProxy(@RequestBody Map<String, Object> body) {

        Object pautaIdObj = body.get("pautaId");
        if (pautaIdObj == null) {
            return new ScreenResponse(
                    ScreenType.SELECAO,
                    "Erro",
                    "pautaId é obrigatório.",
                    null,
                    null,
                    null,
                    List.of(new SelectionItem(
                            "Voltar",
                            null,
                            new Action("Voltar", "/api/v1/mobile/home", Map.of())
                    ))
            );
        }

        Long pautaId;
        try {
            pautaId = Long.valueOf(String.valueOf(pautaIdObj));
        } catch (Exception e) {
            return new ScreenResponse(
                    ScreenType.SELECAO,
                    "Erro",
                    "pautaId inválido. Informe um número (ex.: 1).",
                    null,
                    null,
                    null,
                    List.of(new SelectionItem(
                            "Voltar",
                            null,
                            new Action("Voltar", "/api/v1/mobile/home", Map.of())
                    ))
            );
        }

        var r = resultadoService.obterResultado(pautaId);

        var items = List.of(
                new SelectionItem("Pauta", "ID: " + r.pautaId(), new Action("OK", "/api/v1/mobile/home", Map.of())),
                new SelectionItem("Total SIM", String.valueOf(r.totalSim()), new Action("OK", "/api/v1/mobile/home", Map.of())),
                new SelectionItem("Total NÃO", String.valueOf(r.totalNao()), new Action("OK", "/api/v1/mobile/home", Map.of())),
                new SelectionItem("Total", String.valueOf(r.total()), new Action("OK", "/api/v1/mobile/home", Map.of())),
                new SelectionItem("Resultado", r.resultado(), new Action("OK", "/api/v1/mobile/home", Map.of()))
        );

        return new ScreenResponse(
                ScreenType.SELECAO,
                "Resultado da pauta " + pautaId,
                "Resumo da contabilização",
                null,
                null,
                null,
                items
        );
    }
}
