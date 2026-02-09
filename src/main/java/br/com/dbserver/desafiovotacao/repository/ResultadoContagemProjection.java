package br.com.dbserver.desafiovotacao.repository;

import br.com.dbserver.desafiovotacao.model.enums.ValorVoto;

public interface ResultadoContagemProjection {
    ValorVoto getValor();
    Long getTotal();
}

