package br.com.dbserver.desafiovotacao.service.impl;

import br.com.dbserver.desafiovotacao.exception.BusinessException;
import br.com.dbserver.desafiovotacao.exception.ConflictException;
import br.com.dbserver.desafiovotacao.model.Voto;
import br.com.dbserver.desafiovotacao.model.enums.ValorVoto;
import br.com.dbserver.desafiovotacao.repository.VotoRepository;
import br.com.dbserver.desafiovotacao.service.CpfValidationService;
import br.com.dbserver.desafiovotacao.service.PautaService;
import br.com.dbserver.desafiovotacao.service.SessaoVotacaoService;
import br.com.dbserver.desafiovotacao.service.VotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VotoServiceImpl implements VotoService {

    private final VotoRepository votoRepo;
    private final PautaService pautaService;
    private final SessaoVotacaoService sessaoService;
    private final CpfValidationService cpfValidationService;
    private final SessaoEncerramentoService sessaoEncerramentoService;
    private final Clock clock;

    @Override
    @Transactional
    public Voto votar(Long pautaId, String associadoId, ValorVoto valor, String cpf) {

        sessaoEncerramentoService.encerrarSessaoSeExpirada(pautaId);

        if (pautaId == null) throw new BusinessException("PAUTA_ID é obrigatório.");
        if (associadoId == null || associadoId.isBlank()) throw new BusinessException("ASSOCIADO_ID é obrigatório.");
        if (valor == null) throw new BusinessException("VALOR do voto é obrigatório (SIM/NAO).");

        var pauta = pautaService.buscarPorId(pautaId);

        var sessao = sessaoService.buscarSessaoAberta(pautaId);
        if (sessao == null) {
            throw new ConflictException("Não existe sessão ABERTA para a pauta: " + pautaId);
        }

        var now = LocalDateTime.now(clock);

        cpfValidationService.validateAbleToVote(cpf);

        var associado = associadoId.trim();
        if (votoRepo.existsByPautaIdAndAssociadoId(pautaId, associado)) {
            throw new ConflictException("Associado já votou nesta pauta.");
        }

        var voto = Voto.builder()
                .pauta(pauta)
                .associadoId(associado)
                .valor(valor)
                .criadoEm(now)
                .build();

        try {
            return votoRepo.save(voto);
        } catch (DataIntegrityViolationException e) {
            throw e;
        }
    }
}
