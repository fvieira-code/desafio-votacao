package br.com.dbserver.desafiovotacao.repository;

import br.com.dbserver.desafiovotacao.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    boolean existsByPautaIdAndAssociadoId(Long pautaId, String associadoId);

    @Query("""
        select v.valor as valor, count(v) as total
          from Voto v
         where v.pauta.id = :pautaId
         group by v.valor
    """)
    List<ResultadoContagemProjection> countByPautaGroupByValor(@Param("pautaId") Long pautaId);
}

