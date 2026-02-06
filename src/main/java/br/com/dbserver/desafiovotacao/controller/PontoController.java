package br.com.dbserver.desafiovotacao.controller;

import br.com.dbserver.desafiovotacao.dto.PontoDTO;
import br.com.dbserver.desafiovotacao.dto.RelatorioPontoDTO;
import br.com.dbserver.desafiovotacao.service.PontoService;
import br.com.dbserver.desafiovotacao.service.generator.ArquivoGerado;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pontos")
@RequiredArgsConstructor
public class PontoController {

    private final PontoService pontoService;

    @PostMapping
    public ResponseEntity<PontoDTO> salvar(@Valid @RequestBody PontoDTO dto) {
        return ResponseEntity.ok(pontoService.salvar(dto));
    }

    @GetMapping
    public ResponseEntity<List<PontoDTO>> listarTodos() {
        return ResponseEntity.ok(pontoService.listarTodos());
    }

    @GetMapping("/pagina")
    public ResponseEntity<Page<PontoDTO>> listarPaginado(Pageable pageable) {
        return ResponseEntity.ok(pontoService.listarPaginado(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PontoDTO> atualizar(@PathVariable Integer id, @Valid @RequestBody PontoDTO dto) {
        return ResponseEntity.ok(pontoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        pontoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtro")
    public ResponseEntity<RelatorioPontoDTO> buscarPorFiltro(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            @RequestParam(required = false) Integer idConsultor,
            @RequestParam(required = false) Integer idCliente
    ) {
        return ResponseEntity.ok(pontoService.buscarPorFiltroComTotal(dataInicial, dataFinal, idConsultor, idCliente));
    }

    @GetMapping("/gerar/excel")
    public ResponseEntity<InputStreamResource> gerar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            @RequestParam(required = false) Integer idConsultor,
            @RequestParam(required = false) Integer idCliente
    ) throws IOException {

        ArquivoGerado arquivo = pontoService.gerarExcel(dataInicial, dataFinal, idConsultor, idCliente);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + arquivo.getNomeArquivo());

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(arquivo.getConteudo()));
    }

}
