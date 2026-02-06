package br.com.dbserver.desafiovotacao.controller;

import br.com.dbserver.desafiovotacao.dto.ClienteDTO;
import br.com.dbserver.desafiovotacao.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteDTO> salvar(@Valid @RequestBody ClienteDTO dto) {
        return ResponseEntity.ok(clienteService.salvar(dto));
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<ClienteDTO> buscarPorCnpj(@PathVariable String cnpj) {
        return ResponseEntity.ok(clienteService.buscarPorCnpj(cnpj));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> atualizar(@PathVariable Integer id, @Valid @RequestBody ClienteDTO dto) {
        return ResponseEntity.ok(clienteService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pagina")
    public ResponseEntity<Page<ClienteDTO>> listarPaginado(Pageable pageable) {
        return ResponseEntity.ok(clienteService.listarPaginado(pageable));
    }
}
