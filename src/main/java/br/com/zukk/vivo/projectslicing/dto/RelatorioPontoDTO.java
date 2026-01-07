package br.com.zukk.vivo.projectslicing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelatorioPontoDTO {
    private List<PontoDTO> registros;
    private String totalHoras;
}
