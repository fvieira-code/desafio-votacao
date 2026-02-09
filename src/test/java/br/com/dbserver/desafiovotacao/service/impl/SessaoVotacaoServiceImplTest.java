package br.com.dbserver.desafiovotacao.service.impl;

import br.com.dbserver.desafiovotacao.exception.ConflictException;
import br.com.dbserver.desafiovotacao.model.Pauta;
import br.com.dbserver.desafiovotacao.model.SessaoVotacao;
import br.com.dbserver.desafiovotacao.model.enums.StatusSessao;
import br.com.dbserver.desafiovotacao.repository.SessaoVotacaoRepository;
import br.com.dbserver.desafiovotacao.service.PautaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessaoVotacaoServiceImplTest {

    @Mock
    private SessaoVotacaoRepository sessaoRepo;

    @Mock
    private PautaService pautaService;

    private Clock fixedClock;
    private SessaoVotacaoServiceImpl service;

    @BeforeEach
    void setup() {
        fixedClock = Clock.fixed(
                Instant.parse("2026-02-08T22:00:00Z"),
                ZoneId.of("UTC")
        );
        service = new SessaoVotacaoServiceImpl(sessaoRepo, pautaService, fixedClock);
    }

    @Test
    void abrirSessao_comDuracaoValida_deveCriarSessaoAberta() {
        Long pautaId = 1L;
        Pauta pauta = Pauta.builder().id(pautaId).titulo("Pauta").build();

        when(pautaService.buscarPorId(pautaId)).thenReturn(pauta);
        when(sessaoRepo.findFirstByPautaIdAndStatusOrderByAbertaEmDesc(
                pautaId, StatusSessao.ABERTA)).thenReturn(Optional.empty());

        when(sessaoRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        SessaoVotacao result = service.abrirSessao(pautaId, 120L);

        assertNotNull(result);
        assertEquals(StatusSessao.ABERTA, result.getStatus());

        LocalDateTime now = LocalDateTime.ofInstant(fixedClock.instant(), fixedClock.getZone());
        assertEquals(now, result.getAbertaEm());
        assertEquals(now.plusSeconds(120), result.getEncerraEm());
    }

    @Test
    void abrirSessao_semDuracao_deveUsarDuracaoPadrao60s() {
        Long pautaId = 2L;
        Pauta pauta = Pauta.builder().id(pautaId).titulo("Pauta").build();

        when(pautaService.buscarPorId(pautaId)).thenReturn(pauta);
        when(sessaoRepo.findFirstByPautaIdAndStatusOrderByAbertaEmDesc(
                pautaId, StatusSessao.ABERTA)).thenReturn(Optional.empty());

        when(sessaoRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        SessaoVotacao result = service.abrirSessao(pautaId, null);

        LocalDateTime now = LocalDateTime.ofInstant(fixedClock.instant(), fixedClock.getZone());
        assertEquals(now.plusSeconds(60), result.getEncerraEm());
    }

    @Test
    void abrirSessao_comSessaoAbertaNaoEncerrada_deveLancarConflictException() {
        Long pautaId = 3L;
        Pauta pauta = Pauta.builder().id(pautaId).titulo("Pauta").build();

        SessaoVotacao sessaoAberta = SessaoVotacao.builder()
                .status(StatusSessao.ABERTA)
                .encerraEm(LocalDateTime.ofInstant(
                        fixedClock.instant().plusSeconds(30),
                        fixedClock.getZone()))
                .build();

        when(pautaService.buscarPorId(pautaId)).thenReturn(pauta);
        when(sessaoRepo.findFirstByPautaIdAndStatusOrderByAbertaEmDesc(
                pautaId, StatusSessao.ABERTA)).thenReturn(Optional.of(sessaoAberta));

        ConflictException ex = assertThrows(
                ConflictException.class,
                () -> service.abrirSessao(pautaId, 100L)
        );

        assertEquals("Já existe sessão ABERTA para a pauta: " + pautaId, ex.getMessage());
        verify(sessaoRepo, never()).save(any());
    }

    @Test
    void buscarSessaoAberta_quandoEncerradaPorTempo_deveAtualizarStatusParaEncerrada() {
        Long pautaId = 4L;

        SessaoVotacao sessao = SessaoVotacao.builder()
                .status(StatusSessao.ABERTA)
                .encerraEm(LocalDateTime.ofInstant(
                        fixedClock.instant().minusSeconds(10),
                        fixedClock.getZone()))
                .build();

        when(sessaoRepo.findFirstByPautaIdAndStatusOrderByAbertaEmDesc(
                pautaId, StatusSessao.ABERTA)).thenReturn(Optional.of(sessao));

        when(sessaoRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        SessaoVotacao result = service.buscarSessaoAberta(pautaId);

        assertEquals(StatusSessao.ENCERRADA, result.getStatus());
        verify(sessaoRepo).save(sessao);
    }

    @Test
    void buscarSessaoAberta_quandoAindaAberta_deveRetornarSemSalvar() {
        Long pautaId = 5L;

        SessaoVotacao sessao = SessaoVotacao.builder()
                .status(StatusSessao.ABERTA)
                .encerraEm(LocalDateTime.ofInstant(
                        fixedClock.instant().plusSeconds(30),
                        fixedClock.getZone()))
                .build();

        when(sessaoRepo.findFirstByPautaIdAndStatusOrderByAbertaEmDesc(
                pautaId, StatusSessao.ABERTA)).thenReturn(Optional.of(sessao));

        SessaoVotacao result = service.buscarSessaoAberta(pautaId);

        assertEquals(StatusSessao.ABERTA, result.getStatus());
        verify(sessaoRepo, never()).save(any());
    }
}
