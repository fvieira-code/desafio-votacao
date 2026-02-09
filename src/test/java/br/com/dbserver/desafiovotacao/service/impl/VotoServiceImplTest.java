package br.com.dbserver.desafiovotacao.service.impl;

import br.com.dbserver.desafiovotacao.exception.BusinessException;
import br.com.dbserver.desafiovotacao.exception.ConflictException;
import br.com.dbserver.desafiovotacao.model.Pauta;
import br.com.dbserver.desafiovotacao.model.SessaoVotacao;
import br.com.dbserver.desafiovotacao.model.Voto;
import br.com.dbserver.desafiovotacao.model.enums.StatusSessao;
import br.com.dbserver.desafiovotacao.model.enums.ValorVoto;
import br.com.dbserver.desafiovotacao.repository.VotoRepository;
import br.com.dbserver.desafiovotacao.service.CpfValidationService;
import br.com.dbserver.desafiovotacao.service.PautaService;
import br.com.dbserver.desafiovotacao.service.SessaoVotacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotoServiceImplTest {

    @Mock private VotoRepository votoRepo;
    @Mock private PautaService pautaService;
    @Mock private SessaoVotacaoService sessaoService;
    @Mock private CpfValidationService cpfValidationService;
    @Mock private SessaoEncerramentoService sessaoEncerramentoService;
    private Clock fixedClock;

    private VotoServiceImpl service;

    @BeforeEach
    void setup() {
        fixedClock = Clock.fixed(Instant.parse("2026-02-08T22:10:00Z"), ZoneId.of("UTC"));
        service = new VotoServiceImpl(
                votoRepo, pautaService, sessaoService, cpfValidationService, sessaoEncerramentoService, fixedClock
        );
    }

    @Test
    void votar_deveEncerrarSessaoSeExpirada_antesDeValidarParametros() {
        Long pautaId = null;

        assertThrows(BusinessException.class, () -> service.votar(pautaId, "A1", ValorVoto.SIM, null));

        verify(sessaoEncerramentoService, times(1)).encerrarSessaoSeExpirada(null);
        verifyNoInteractions(pautaService, sessaoService, cpfValidationService, votoRepo);
    }

    @Test
    void votar_quandoPautaIdNull_deveLancarBusinessException() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.votar(null, "A1", ValorVoto.SIM, null));

        assertEquals("PAUTA_ID é obrigatório.", ex.getMessage());
        verify(sessaoEncerramentoService).encerrarSessaoSeExpirada(null);
        verifyNoInteractions(pautaService, sessaoService, cpfValidationService, votoRepo);
    }

    @Test
    void votar_quandoAssociadoIdBlank_deveLancarBusinessException() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.votar(1L, "   ", ValorVoto.SIM, null));

        assertEquals("ASSOCIADO_ID é obrigatório.", ex.getMessage());
        verify(sessaoEncerramentoService).encerrarSessaoSeExpirada(1L);
        verifyNoInteractions(pautaService, sessaoService, cpfValidationService, votoRepo);
    }

    @Test
    void votar_quandoValorNull_deveLancarBusinessException() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.votar(1L, "A1", null, null));

        assertEquals("VALOR do voto é obrigatório (SIM/NAO).", ex.getMessage());
        verify(sessaoEncerramentoService).encerrarSessaoSeExpirada(1L);
        verifyNoInteractions(pautaService, sessaoService, cpfValidationService, votoRepo);
    }

    @Test
    void votar_quandoNaoExisteSessaoAberta_deveLancarConflictException() {
        Long pautaId = 10L;
        when(pautaService.buscarPorId(pautaId)).thenReturn(Pauta.builder().id(pautaId).titulo("P1").build());
        when(sessaoService.buscarSessaoAberta(pautaId)).thenReturn(null);

        ConflictException ex = assertThrows(ConflictException.class,
                () -> service.votar(pautaId, "A1", ValorVoto.SIM, "52998224725"));

        assertEquals("Não existe sessão ABERTA para a pauta: " + pautaId, ex.getMessage());
        verify(sessaoEncerramentoService).encerrarSessaoSeExpirada(pautaId);
        verify(pautaService).buscarPorId(pautaId);
        verify(sessaoService).buscarSessaoAberta(pautaId);
        verifyNoInteractions(cpfValidationService, votoRepo);
    }

    @Test
    void votar_quandoAssociadoJaVotou_deveLancarConflictException() {
        Long pautaId = 11L;
        when(pautaService.buscarPorId(pautaId)).thenReturn(Pauta.builder().id(pautaId).titulo("P1").build());
        when(sessaoService.buscarSessaoAberta(pautaId)).thenReturn(
                SessaoVotacao.builder().status(StatusSessao.ABERTA).build()
        );

        doNothing().when(cpfValidationService).validateAbleToVote(any());
        when(votoRepo.existsByPautaIdAndAssociadoId(pautaId, "A1")).thenReturn(true);

        ConflictException ex = assertThrows(ConflictException.class,
                () -> service.votar(pautaId, "  A1  ", ValorVoto.NAO, "52998224725"));

        assertEquals("Associado já votou nesta pauta.", ex.getMessage());

        InOrder inOrder = inOrder(sessaoEncerramentoService, pautaService, sessaoService, cpfValidationService, votoRepo);
        inOrder.verify(sessaoEncerramentoService).encerrarSessaoSeExpirada(pautaId);
        inOrder.verify(pautaService).buscarPorId(pautaId);
        inOrder.verify(sessaoService).buscarSessaoAberta(pautaId);
        inOrder.verify(cpfValidationService).validateAbleToVote("52998224725");
        inOrder.verify(votoRepo).existsByPautaIdAndAssociadoId(pautaId, "A1");

        verify(votoRepo, never()).save(any());
    }

    @Test
    void votar_fluxoFeliz_deveSalvarVotoComAssociadoTrimECriadoEmDoClock() {
        Long pautaId = 12L;
        Pauta pauta = Pauta.builder().id(pautaId).titulo("Pauta").build();

        when(pautaService.buscarPorId(pautaId)).thenReturn(pauta);
        when(sessaoService.buscarSessaoAberta(pautaId)).thenReturn(
                SessaoVotacao.builder().status(StatusSessao.ABERTA).build()
        );

        doNothing().when(cpfValidationService).validateAbleToVote(any());
        when(votoRepo.existsByPautaIdAndAssociadoId(pautaId, "A1")).thenReturn(false);

        when(votoRepo.save(any(Voto.class))).thenAnswer(inv -> inv.getArgument(0));

        Voto saved = service.votar(pautaId, "  A1  ", ValorVoto.SIM, "52998224725");

        assertNotNull(saved);
        assertEquals(pauta, saved.getPauta());
        assertEquals("A1", saved.getAssociadoId());
        assertEquals(ValorVoto.SIM, saved.getValor());

        LocalDateTime expectedNow = LocalDateTime.ofInstant(fixedClock.instant(), fixedClock.getZone());
        assertEquals(expectedNow, saved.getCriadoEm());

        ArgumentCaptor<Voto> captor = ArgumentCaptor.forClass(Voto.class);
        verify(votoRepo).save(captor.capture());
        assertEquals("A1", captor.getValue().getAssociadoId());
    }

    @Test
    void votar_quandoDataIntegrityViolation_devePropagarExcecao() {
        Long pautaId = 13L;

        when(pautaService.buscarPorId(pautaId)).thenReturn(Pauta.builder().id(pautaId).titulo("P").build());
        when(sessaoService.buscarSessaoAberta(pautaId)).thenReturn(
                SessaoVotacao.builder().status(StatusSessao.ABERTA).build()
        );

        doNothing().when(cpfValidationService).validateAbleToVote(any());
        when(votoRepo.existsByPautaIdAndAssociadoId(pautaId, "A1")).thenReturn(false);

        when(votoRepo.save(any(Voto.class))).thenThrow(new DataIntegrityViolationException("uk"));

        assertThrows(DataIntegrityViolationException.class,
                () -> service.votar(pautaId, "A1", ValorVoto.SIM, null));
    }
}
