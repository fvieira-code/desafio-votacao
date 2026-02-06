package br.com.dbserver.desafiovotacao.service;

import br.com.dbserver.desafiovotacao.dto.PontoDTO;
import br.com.dbserver.desafiovotacao.dto.RelatorioPontoDTO;
import br.com.dbserver.desafiovotacao.mapper.PontoMapper;
import br.com.dbserver.desafiovotacao.model.Cliente;
import br.com.dbserver.desafiovotacao.model.Consultor;
import br.com.dbserver.desafiovotacao.model.Ponto;
import br.com.dbserver.desafiovotacao.repository.ClienteRepository;
import br.com.dbserver.desafiovotacao.repository.ConsultorRepository;
import br.com.dbserver.desafiovotacao.repository.PontoRepository;
import br.com.dbserver.desafiovotacao.service.generator.ArquivoGerado;
import br.com.dbserver.desafiovotacao.service.generator.PontoExcelGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PontoService {

    private final PontoRepository pontoRepository;
    private final ConsultorRepository consultorRepository;
    private final ClienteRepository clienteRepository;

    @Autowired
    public PontoExcelGenerator pontoExcelGenerator;

    public PontoDTO salvar(PontoDTO dto) {
        Consultor consultor = consultorRepository.findById(dto.getIdConsultor())
                .orElseThrow(() -> new RuntimeException("Consultor não encontrado"));
        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Ponto ponto = PontoMapper.toEntity(dto, consultor, cliente);
        return PontoMapper.toDTO(pontoRepository.save(ponto));
    }

    public List<PontoDTO> listarTodos() {
        return pontoRepository.findAll()
                .stream().map(PontoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Page<PontoDTO> listarPaginado(Pageable pageable) {
        return pontoRepository.findAll(pageable).map(PontoMapper::toDTO);
    }

    public PontoDTO atualizar(Integer id, PontoDTO dto) {
        var existente = pontoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ponto não encontrado"));

        Consultor consultor = consultorRepository.findById(dto.getIdConsultor())
                .orElseThrow(() -> new RuntimeException("Consultor não encontrado"));
        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Ponto atualizado = PontoMapper.toEntity(dto, consultor, cliente);
        atualizado.setId(id);

        return PontoMapper.toDTO(pontoRepository.save(atualizado));
    }

    public void deletar(Integer id) {
        pontoRepository.deleteById(id);
    }

    public RelatorioPontoDTO buscarPorFiltroComTotal(LocalDate dataInicial, LocalDate dataFinal,
                                                     Integer idConsultor, Integer idCliente) {
        List<Ponto> pontos = pontoRepository.findByFiltro(dataInicial, dataFinal, idConsultor, idCliente);
        List<PontoDTO> dtos = pontos.stream().map(PontoMapper::toDTO).toList();

        // soma total de horas
        Duration total = pontos.stream()
                .filter(p -> p.getTotal() != null)
                .map(Ponto::getTotal)
                .map(t -> Duration.ofHours(t.getHour()).plusMinutes(t.getMinute()))
                .reduce(Duration.ZERO, Duration::plus);

        long hours = total.toHours();
        long minutes = total.minusHours(hours).toMinutes();
        String totalHoras = String.format("%02d:%02d", hours, minutes);

        return new RelatorioPontoDTO(dtos, totalHoras);
    }

    public ArquivoGerado gerarExcel(LocalDate dataInicial, LocalDate dataFinal,
                                    Integer idConsultor, Integer idCliente) throws IOException {
        var relatorio = buscarPorFiltroComTotal(dataInicial, dataFinal, idConsultor, idCliente);

        String dataAtual = new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date());
        String nomeArquivo = "relatorio-socioambiental-" + dataAtual + ".xlsx";

        String userHome = System.getProperty("user.home");
        Path downloadsPath = Paths.get(userHome, "Downloads");
        Path caminhoFinal = downloadsPath.resolve(nomeArquivo);

        ByteArrayInputStream planilha = pontoExcelGenerator.gerar(relatorio);
        Files.copy(planilha, caminhoFinal, StandardCopyOption.REPLACE_EXISTING);

        return new ArquivoGerado(nomeArquivo, new ByteArrayInputStream(Files.readAllBytes(caminhoFinal)));
    }

}
