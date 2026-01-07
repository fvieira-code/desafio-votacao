package br.com.zukk.vivo.projectslicing.repository;

import br.com.zukk.vivo.projectslicing.model.Consultor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsultorRepository extends JpaRepository<Consultor, Integer> {
    Optional<Consultor> findByCpf(String cpf);

}
