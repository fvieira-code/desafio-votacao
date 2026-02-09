package br.com.dbserver.desafiovotacao.service;

import br.com.dbserver.desafiovotacao.model.SessaoVotacao;

public interface SessaoVotacaoService {
    SessaoVotacao abrirSessao(Long pautaId, Long duracaoSegundos);
    SessaoVotacao buscarSessaoAberta(Long pautaId);
}
