package br.com.zukk.vivo.projectslicing.dto;

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
public class ConsultorDTO {

    private Integer id;

    @NotBlank
    @Size(max = 250)
    private String nome;

    @NotBlank
    @Size(max = 15)
    private String cpf;

    @NotBlank
    @Size(max = 15)
    private String rg;

    @Size(max = 500)
    private String endereco;
}
