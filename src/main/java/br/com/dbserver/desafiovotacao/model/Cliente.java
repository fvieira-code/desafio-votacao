package br.com.dbserver.desafiovotacao.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer id;

    @Column(name = "razao_social_cliente", nullable = false, length = 250)
    private String razaoSocial;

    @Column(name = "nome_fantasia_cliente", nullable = false, length = 250)
    private String nomeFantasia;

    @Column(name = "cnpj_cliente", nullable = false, length = 15)
    private String cnpj;

    @Column(name = "endereco_cliente", length = 500)
    private String endereco;
}
