package br.com.dbserver.desafiovotacao.service.impl;

import br.com.dbserver.desafiovotacao.model.enums.StatusSessao;
import br.com.dbserver.desafiovotacao.model.enums.ValorVoto;
import br.com.dbserver.desafiovotacao.repository.VotoRepository;
import br.com.dbserver.desafiovotacao.service.PautaService;
import br.com.dbserver.desafiovotacao.service.ResultadoService;
import br.com.dbserver.desafiovotacao.service.SessaoVotacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ResultadoServiceImpl implements ResultadoService {

    private final VotoRepository votoRepo;
    private final PautaService pautaService;
    private final SessaoVotacaoService sessaoService;
    private final SessaoEncerramentoService sessaoEncerramentoService;
    private final Clock clock;

    @Override
    @Transactional(readOnly = true)
    public Resultado obterResultado(Long pautaId) {

        sessaoEncerramentoService.encerrarSessaoSeExpirada(pautaId);

        pautaService.buscarPorId(pautaId);

        long sim = 0;
        long nao = 0;

        var agrupado = votoRepo.countByPautaGroupByValor(pautaId);
        for (var r : agrupado) {
            if (r.getValor() == ValorVoto.SIM) sim = r.getTotal() == null ? 0 : r.getTotal();
            if (r.getValor() == ValorVoto.NAO) nao = r.getTotal() == null ? 0 : r.getTotal();
        }

        long total = sim + nao;

        var sessao = sessaoService.buscarSessaoAberta(pautaId);
        StatusSessao status;
        if (sessao == null) {
            status = StatusSessao.ENCERRADA;
        } else {
            var now = LocalDateTime.now(clock);
            boolean expirada = now.isAfter(sessao.getEncerraEm()) || now.isEqual(sessao.getEncerraEm());
            status = expirada ? StatusSessao.ENCERRADA : StatusSessao.ABERTA;
        }

        String resultado;
        if (sim > nao) resultado = "APROVADA";
        else if (nao > sim) resultado = "REPROVADA";
        else resultado = "EMPATE";

        return new Resultado(pautaId, sim, nao, total, status, resultado);
    }
}
