package br.com.zukk.vivo.projectslicing.repository;

import br.com.zukk.vivo.projectslicing.model.Ponto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PontoRepository extends JpaRepository<Ponto, Integer> {

    @Query("""
                SELECT p FROM Ponto p
                WHERE p.data BETWEEN :dataInicial AND :dataFinal
                AND (:consultorId IS NULL OR p.consultor.id = :consultorId)
                AND (:clienteId IS NULL OR p.cliente.id = :clienteId)
                ORDER BY p.data ASC
            """)
    List<Ponto> findByFiltro(@Param("dataInicial") LocalDate dataInicial,
                             @Param("dataFinal") LocalDate dataFinal,
                             @Param("consultorId") Integer consultorId,
                             @Param("clienteId") Integer clienteId);

}
