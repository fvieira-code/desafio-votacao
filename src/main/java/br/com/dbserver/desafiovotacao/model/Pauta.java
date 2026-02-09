package br.com.dbserver.desafiovotacao.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pauta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "criada_em", nullable = false)
    private LocalDateTime criadaEm;
}
