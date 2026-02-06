package br.com.dbserver.desafiovotacao.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "tb_ponto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ponto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ponto")
    private Integer id;

    @Column(name = "atividade", nullable = false, length = 1000)
    private String atividade;

    @Column(name = "data_ponto", nullable = false)
    private LocalDate data;

    @Column(name = "dia_ponto", nullable = false, length = 25)
    private String dia;

    @Column(name = "inicio_ponto")
    private LocalTime inicio;

    @Column(name = "final_ponto")
    private LocalTime fim;

    @Column(name = "total_hora_ponto")
    private LocalTime total;

    @Column(name = "status__ponto", nullable = false, length = 25)
    private String status;

    @Column(name = "ticket_ponto", nullable = false, length = 25)
    private String ticket;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_consultor", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Consultor consultor;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Cliente cliente;
}