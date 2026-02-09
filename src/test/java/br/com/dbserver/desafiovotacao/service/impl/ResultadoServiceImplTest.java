package br.com.dbserver.desafiovotacao.service.impl;

import br.com.dbserver.desafiovotacao.model.enums.StatusSessao;
import br.com.dbserver.desafiovotacao.model.enums.ValorVoto;
import br.com.dbserver.desafiovotacao.repository.ResultadoContagemProjection;
import br.com.dbserver.desafiovotacao.repository.VotoRepository;
import br.com.dbserver.desafiovotacao.service.PautaService;
import br.com.dbserver.desafiovotacao.service.ResultadoService;
import br.com.dbserver.desafiovotacao.service.SessaoVotacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResultadoServiceImplTest {

    @Mock private VotoRepository votoRepo;
    @Mock private PautaService pautaService;
    @Mock private SessaoVotacaoService sessaoService;
    @Mock private SessaoEncerramentoService sessaoEncerramentoService;

    private Clock fixedClock;
    private ResultadoServiceImpl service;

    @BeforeEach
    void setup() {
        fixedClock = Clock.fixed(
                Instant.parse("2026-02-08T23:30:00Z"),
                ZoneId.of("UTC")
        );
        service = new ResultadoServiceImpl(
                votoRepo, pautaService, sessaoService, sessaoEncerramentoService, fixedClock
        );
    }

    @Test
    void obterResultado_quandoSimMaiorQueNao_deveRetornarAprovada_eSessaoEncerrada() {
        Long pautaId = 1L;

        when(votoRepo.countByPautaGroupByValor(pautaId))
                .thenReturn(List.of(
                        proj(ValorVoto.SIM, 5L),
                        proj(ValorVoto.NAO, 3L)
                ));

        when(sessaoService.buscarSessaoAberta(pautaId)).thenReturn(null);

        ResultadoService.Resultado r = service.obterResultado(pautaId);

        assertEquals(5, r.totalSim());
        assertEquals(3, r.totalNao());
        assertEquals(8, r.total());
        assertEquals(StatusSessao.ENCERRADA, r.statusSessao());
        assertEquals("APROVADA", r.resultado());
    }

    @Test
    void obterResultado_quandoNaoMaiorQueSim_deveRetornarReprovada() {
        Long pautaId = 2L;

        when(votoRepo.countByPautaGroupByValor(pautaId))
                .thenReturn(List.of(
                        proj(ValorVoto.SIM, 1L),
                        proj(ValorVoto.NAO, 4L)
                ));

        when(sessaoService.buscarSessaoAberta(pautaId)).thenReturn(null);

        ResultadoService.Resultado r = service.obterResultado(pautaId);

        assertEquals("REPROVADA", r.resultado());
    }

    @Test
    void obterResultado_quandoEmpate_deveRetornarEmpate() {
        Long pautaId = 3L;

        when(votoRepo.countByPautaGroupByValor(pautaId))
                .thenReturn(List.of(
                        proj(ValorVoto.SIM, 2L),
                        proj(ValorVoto.NAO, 2L)
                ));

        when(sessaoService.buscarSessaoAberta(pautaId)).thenReturn(null);

        ResultadoService.Resultado r = service.obterResultado(pautaId);

        assertEquals("EMPATE", r.resultado());
        assertEquals(4, r.total());
    }

    @Test
    void obterResultado_quandoSessaoAbertaENaoExpirada_statusDeveSerAberta() {
        Long pautaId = 4L;

        when(votoRepo.countByPautaGroupByValor(pautaId))
                .thenReturn(List.of(
                        proj(ValorVoto.SIM, 1L),
                        proj(ValorVoto.NAO, 0L)
                ));

        var sessaoAberta = mock(br.com.dbserver.desafiovotacao.model.SessaoVotacao.class);
        when(sessaoAberta.getEncerraEm()).thenReturn(
                LocalDateTime.ofInstant(
                        fixedClock.instant().plusSeconds(60),
                        fixedClock.getZone()
                )
        );

        when(sessaoService.buscarSessaoAberta(pautaId)).thenReturn(sessaoAberta);

        ResultadoService.Resultado r = service.obterResultado(pautaId);

        assertEquals(StatusSessao.ABERTA, r.statusSessao());
    }

    @Test
    void obterResultado_deveExecutarFluxoNaOrdemCorreta() {
        Long pautaId = 5L;

        when(votoRepo.countByPautaGroupByValor(pautaId)).thenReturn(List.of());
        when(sessaoService.buscarSessaoAberta(pautaId)).thenReturn(null);

        service.obterResultado(pautaId);

        InOrder inOrder = inOrder(
                sessaoEncerramentoService,
                pautaService,
                votoRepo,
                sessaoService
        );

        inOrder.verify(sessaoEncerramentoService).encerrarSessaoSeExpirada(pautaId);
        inOrder.verify(pautaService).buscarPorId(pautaId);
        inOrder.verify(votoRepo).countByPautaGroupByValor(pautaId);
        inOrder.verify(sessaoService).buscarSessaoAberta(pautaId);
    }

    private ResultadoContagemProjection proj(ValorVoto valor, Long total) {
        return new ResultadoContagemProjection() {
            @Override
            public ValorVoto getValor() {
                return valor;
            }

            @Override
            public Long getTotal() {
                return total;
            }
        };
    }
}
