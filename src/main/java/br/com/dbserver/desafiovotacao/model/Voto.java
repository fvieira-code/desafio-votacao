package br.com.dbserver.desafiovotacao.model;

import br.com.dbserver.desafiovotacao.model.enums.ValorVoto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "voto",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_voto_pauta_associado",
                columnNames = {"pauta_id", "associado_id"}
        ),
        indexes = {
                @Index(name = "idx_voto_pauta", columnList = "pauta_id"),
                @Index(name = "idx_voto_pauta_valor", columnList = "pauta_id,valor")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pauta_id", nullable = false, foreignKey = @ForeignKey(name = "fk_voto_pauta"))
    private Pauta pauta;

    @Column(name = "associado_id", nullable = false, length = 80)
    private String associadoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "valor", nullable = false, length = 10)
    private ValorVoto valor;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;
}
