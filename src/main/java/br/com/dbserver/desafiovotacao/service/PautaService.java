package br.com.dbserver.desafiovotacao.service;

import br.com.dbserver.desafiovotacao.model.Pauta;

public interface PautaService {
    Pauta criar(String titulo, String descricao);
    Pauta buscarPorId(Long id);
}
