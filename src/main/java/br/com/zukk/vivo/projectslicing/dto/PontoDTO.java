package br.com.zukk.vivo.projectslicing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PontoDTO {
    private Integer id;

    @NotBlank
    @Size(max = 1000)
    private String atividade;

    @NotNull
    private LocalDate data;

    @NotBlank
    private String dia;

    private LocalTime inicio;
    private LocalTime fim;
    private LocalTime total;

    @NotBlank
    private String status;

    @NotBlank
    private String ticket;

    @NotNull
    private Integer idConsultor;

    @NotNull
    private Integer idCliente;

    private ConsultorDTO consultor;
    private ClienteDTO cliente;
}
