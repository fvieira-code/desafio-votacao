package br.com.dbserver.desafiovotacao.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_consultor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consultor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consultor")
    private Integer id;

    @Column(name = "nome_consultor", nullable = false, length = 250)
    private String nome;

    @Column(name = "cpf_consultor", nullable = false, length = 15)
    private String cpf;

    @Column(name = "rg_consultor", nullable = false, length = 15)
    private String rg;

    @Column(name = "endereco_consultor", length = 500)
    private String endereco;
}
