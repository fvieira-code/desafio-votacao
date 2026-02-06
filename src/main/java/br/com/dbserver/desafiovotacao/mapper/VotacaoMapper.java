package br.com.dbserver.desafiovotacao.mapper;

import br.com.dbserver.desafiovotacao.dto.response.*;
import br.com.dbserver.desafiovotacao.model.Pauta;
import br.com.dbserver.desafiovotacao.model.SessaoVotacao;
import br.com.dbserver.desafiovotacao.model.Voto;
import br.com.dbserver.desafiovotacao.service.ResultadoService;

public final class VotacaoMapper {

    private VotacaoMapper() {}

    public static PautaResponse toResponse(Pauta p) {
        if (p == null) return null;
        return new PautaResponse(p.getId(), p.getTitulo(), p.getDescricao(), p.getCriadaEm());
    }

    public static SessaoResponse toResponse(SessaoVotacao s) {
        if (s == null) return null;
        return new SessaoResponse(
                s.getId(),
                s.getPauta().getId(),
                s.getAbertaEm(),
                s.getEncerraEm(),
                s.getStatus()
        );
    }

    public static VotoResponse toResponse(Voto v) {
        if (v == null) return null;
        return new VotoResponse(
                v.getId(),
                v.getPauta().getId(),
                v.getAssociadoId(),
                v.getValor(),
                v.getCriadoEm()
        );
    }

    public static ResultadoResponse toResponse(ResultadoService.Resultado r) {
        if (r == null) return null;
        return new ResultadoResponse(
                r.pautaId(),
                r.totalSim(),
                r.totalNao(),
                r.total(),
                r.statusSessao(),
                r.resultado()
        );
    }
}
