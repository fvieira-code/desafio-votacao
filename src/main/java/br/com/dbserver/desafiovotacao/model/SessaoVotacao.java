package br.com.dbserver.desafiovotacao.model;

import br.com.dbserver.desafiovotacao.model.enums.StatusSessao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sessao_votacao",
        indexes = {
                @Index(name = "idx_sessao_pauta_status", columnList = "pauta_id,status"),
                @Index(name = "idx_sessao_pauta_encerra", columnList = "pauta_id,encerra_em")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessaoVotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pauta_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sessao_pauta"))
    private Pauta pauta;

    @Column(name = "aberta_em", nullable = false)
    private LocalDateTime abertaEm;

    @Column(name = "encerra_em", nullable = false)
    private LocalDateTime encerraEm;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusSessao status;
}
