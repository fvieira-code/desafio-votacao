package br.com.dbserver.desafiovotacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteDTO {
    private Integer id;

    @NotBlank
    @Size(max = 250)
    private String razaoSocial;

    @NotBlank
    @Size(max = 250)
    private String nomeFantasia;

    @NotBlank
    @Size(max = 15)
    private String cnpj;

    @Size(max = 500)
    private String endereco;
}
