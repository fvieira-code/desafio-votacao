package br.com.dbserver.desafiovotacao.service.impl;

import br.com.dbserver.desafiovotacao.service.SessaoVotacaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SessaoEncerramentoService {

    private final SessaoVotacaoService sessaoService;

    public SessaoEncerramentoService(SessaoVotacaoService sessaoService) {
        this.sessaoService = sessaoService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void encerrarSessaoSeExpirada(Long pautaId) {
        sessaoService.buscarSessaoAberta(pautaId);
    }
}

