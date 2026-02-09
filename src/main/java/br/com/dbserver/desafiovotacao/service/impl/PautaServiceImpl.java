package br.com.dbserver.desafiovotacao.service.impl;

import br.com.dbserver.desafiovotacao.exception.BusinessException;
import br.com.dbserver.desafiovotacao.exception.NotFoundException;
import br.com.dbserver.desafiovotacao.model.Pauta;
import br.com.dbserver.desafiovotacao.repository.PautaRepository;
import br.com.dbserver.desafiovotacao.service.PautaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PautaServiceImpl implements PautaService {

    private final PautaRepository pautaRepository;
    private final Clock clock;

    @Override
    @Transactional
    public Pauta criar(String titulo, String descricao) {
        if (titulo == null || titulo.isBlank()) {
            throw new BusinessException("Título é obrigatório.");
        }

        var pauta = Pauta.builder()
                .titulo(titulo.trim())
                .descricao(descricao)
                .criadaEm(LocalDateTime.now(clock))
                .build();

        return pautaRepository.save(pauta);
    }

    @Override
    @Transactional(readOnly = true)
    public Pauta buscarPorId(Long id) {
        return pautaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pauta não encontrada: " + id));
    }
}
