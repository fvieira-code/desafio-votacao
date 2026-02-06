package br.com.dbserver.desafiovotacao.repository;

import br.com.dbserver.desafiovotacao.model.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PautaRepository extends JpaRepository<Pauta, Long> {
}
