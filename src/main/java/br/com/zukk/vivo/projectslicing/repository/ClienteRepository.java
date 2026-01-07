package br.com.zukk.vivo.projectslicing.repository;

import br.com.zukk.vivo.projectslicing.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByCnpj(String cnpj);
}
