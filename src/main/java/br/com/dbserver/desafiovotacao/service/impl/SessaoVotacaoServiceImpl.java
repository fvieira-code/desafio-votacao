package br.com.dbserver.desafiovotacao.service.impl;

import br.com.dbserver.desafiovotacao.exception.ConflictException;
import br.com.dbserver.desafiovotacao.model.SessaoVotacao;
import br.com.dbserver.desafiovotacao.model.enums.StatusSessao;
import br.com.dbserver.desafiovotacao.repository.SessaoVotacaoRepository;
import br.com.dbserver.desafiovotacao.service.PautaService;
import br.com.dbserver.desafiovotacao.service.SessaoVotacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SessaoVotacaoServiceImpl implements SessaoVotacaoService {

    private static final long DEFAULT_DURACAO_SEGUNDOS = 60L;

    private final SessaoVotacaoRepository sessaoRepo;
    private final PautaService pautaService;
    private final Clock clock;

    @Override
    @Transactional
    public SessaoVotacao abrirSessao(Long pautaId, Long duracaoSegundos) {
        var pauta = pautaService.buscarPorId(pautaId);

        var abertaOpt = sessaoRepo.findFirstByPautaIdAndStatusOrderByAbertaEmDesc(pautaId, StatusSessao.ABERTA);
        if (abertaOpt.isPresent() && !isEncerradaPorTempo(abertaOpt.get(), LocalDateTime.now(clock))) {
            throw new ConflictException("Já existe sessão ABERTA para a pauta: " + pautaId);
        }

        long dur = (duracaoSegundos == null || duracaoSegundos <= 0) ? DEFAULT_DURACAO_SEGUNDOS : duracaoSegundos;
        var now = LocalDateTime.now(clock);

        var sessao = SessaoVotacao.builder()
                .pauta(pauta)
                .abertaEm(now)
                .encerraEm(now.plusSeconds(dur))
                .status(StatusSessao.ABERTA)
                .build();

        return sessaoRepo.save(sessao);
    }

    @Override
    @Transactional
    public SessaoVotacao buscarSessaoAberta(Long pautaId) {
        var sessao = sessaoRepo.findFirstByPautaIdAndStatusOrderByAbertaEmDesc(pautaId, StatusSessao.ABERTA)
                .orElse(null);

        if (sessao == null) return null;

        var now = LocalDateTime.now(clock);
        if (isEncerradaPorTempo(sessao, now)) {
            sessao.setStatus(StatusSessao.ENCERRADA);
            return sessaoRepo.save(sessao);
        }

        return sessao;
    }

    private boolean isEncerradaPorTempo(SessaoVotacao s, LocalDateTime now) {
        return !now.isBefore(s.getEncerraEm());
    }
}
