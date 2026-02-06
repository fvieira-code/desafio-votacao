package br.com.dbserver.desafiovotacao.repository;

import br.com.dbserver.desafiovotacao.model.SessaoVotacao;
import br.com.dbserver.desafiovotacao.model.enums.StatusSessao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, Long> {

    Optional<SessaoVotacao> findFirstByPautaIdAndStatusOrderByAbertaEmDesc(Long pautaId, StatusSessao status);
}
