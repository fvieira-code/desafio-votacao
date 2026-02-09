package br.com.dbserver.desafiovotacao.service.impl;

import br.com.dbserver.desafiovotacao.exception.BusinessException;
import br.com.dbserver.desafiovotacao.exception.NotFoundException;
import br.com.dbserver.desafiovotacao.model.Pauta;
import br.com.dbserver.desafiovotacao.repository.PautaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
class PautaServiceImplTest {

    @Mock
    private PautaRepository pautaRepository;

    private Clock fixedClock;
    private PautaServiceImpl service;

    @BeforeEach
    void setup() {
        fixedClock = Clock.fixed(Instant.parse("2026-02-08T23:00:00Z"), ZoneId.of("UTC"));
        service = new PautaServiceImpl(pautaRepository, fixedClock);
    }

    @Test
    void criar_deveSalvarComTituloTrimECriadaEmDoClock() {
        String titulo = "  Minha pauta  ";
        String descricao = "Descrição";

        when(pautaRepository.save(any(Pauta.class))).thenAnswer(inv -> inv.getArgument(0));

        Pauta result = service.criar(titulo, descricao);

        assertNotNull(result);

        ArgumentCaptor<Pauta> captor = ArgumentCaptor.forClass(Pauta.class);
        verify(pautaRepository, times(1)).save(captor.capture());

        Pauta saved = captor.getValue();
        assertNotNull(saved);

        assertEquals("Minha pauta", saved.getTitulo());

        assertEquals(descricao, saved.getDescricao());

        LocalDateTime expected = LocalDateTime.ofInstant(fixedClock.instant(), fixedClock.getZone());
        assertEquals(expected, saved.getCriadaEm());
    }

    @Test
    void criar_quandoTituloNull_deveLancarBusinessException() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.criar(null, "qualquer"));

        assertEquals("Título é obrigatório.", ex.getMessage());
        verify(pautaRepository, never()).save(any());
    }

    @Test
    void criar_quandoTituloEmBranco_deveLancarBusinessException() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.criar("   ", "qualquer"));

        assertEquals("Título é obrigatório.", ex.getMessage());
        verify(pautaRepository, never()).save(any());
    }

    @Test
    void buscarPorId_quandoExiste_deveRetornarPauta() {
        Long id = 10L;

        Pauta pauta = Pauta.builder()
                .titulo("Pauta")
                .descricao("Desc")
                .criadaEm(LocalDateTime.now(fixedClock))
                .build();

        when(pautaRepository.findById(id)).thenReturn(Optional.of(pauta));

        Pauta result = service.buscarPorId(id);

        assertNotNull(result);
        assertEquals("Pauta", result.getTitulo());
        verify(pautaRepository, times(1)).findById(id);
    }

    @Test
    void buscarPorId_quandoNaoExiste_deveLancarNotFoundExceptionComId() {
        Long id = 999L;
        when(pautaRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> service.buscarPorId(id));

        assertEquals("Pauta não encontrada: " + id, ex.getMessage());
        verify(pautaRepository, times(1)).findById(id);
    }
}

