package br.com.dbserver.desafiovotacao.service;

import br.com.dbserver.desafiovotacao.model.Voto;
import br.com.dbserver.desafiovotacao.model.enums.ValorVoto;

public interface VotoService {
    Voto votar(Long pautaId, String associadoId, ValorVoto valor, String cpf);
}
