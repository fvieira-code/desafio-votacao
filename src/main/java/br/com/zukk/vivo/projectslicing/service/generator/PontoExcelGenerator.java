package br.com.zukk.vivo.projectslicing.service.generator;

import br.com.zukk.vivo.projectslicing.dto.PontoDTO;
import br.com.zukk.vivo.projectslicing.dto.RelatorioPontoDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PontoExcelGenerator {

    private static final List<String> COLUNAS = List.of(
            "ID", "Data", "Dia", "Início", "Fim", "Total", "Atividade", "Status", "Ticket", "Consultor", "Cliente"
    );

    public ByteArrayInputStream gerar(RelatorioPontoDTO relatorio) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Relatório de Pontos");
            CellStyle headerStyle = criarEstiloNegrito(workbook);

            criarCabecalho(sheet, headerStyle);
            preencherDados(sheet, relatorio.getRegistros());
            adicionarTotal(sheet, relatorio.getTotalHoras(), relatorio.getRegistros().size());

            autoAjustarColunas(sheet, COLUNAS.size());

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private void criarCabecalho(Sheet sheet, CellStyle style) {
        Row header = sheet.createRow(0);
        for (int i = 0; i < COLUNAS.size(); i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(COLUNAS.get(i));
            cell.setCellStyle(style);
        }
    }

    private void preencherDados(Sheet sheet, List<PontoDTO> registros) {
        int rowIdx = 1;
        for (PontoDTO dto : registros) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(dto.getId());
            row.createCell(1).setCellValue(String.valueOf(dto.getData()));
            row.createCell(2).setCellValue(dto.getDia());
            row.createCell(3).setCellValue(dto.getInicio() != null ? dto.getInicio().toString() : "");
            row.createCell(4).setCellValue(dto.getFim() != null ? dto.getFim().toString() : "");
            row.createCell(5).setCellValue(dto.getTotal() != null ? dto.getTotal().toString() : "");
            row.createCell(6).setCellValue(dto.getAtividade());
            row.createCell(7).setCellValue(dto.getStatus());
            row.createCell(8).setCellValue(dto.getTicket());
            row.createCell(9).setCellValue(String.valueOf(dto.getIdConsultor()));
            row.createCell(10).setCellValue(String.valueOf(dto.getIdCliente()));
        }
    }

    private void adicionarTotal(Sheet sheet, String totalHoras, int offset) {
        Row totalRow = sheet.createRow(offset + 2);
        totalRow.createCell(0).setCellValue("Total de Horas:");
        totalRow.createCell(1).setCellValue(totalHoras);
    }

    private void autoAjustarColunas(Sheet sheet, int totalColunas) {
        for (int i = 0; i < totalColunas; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private CellStyle criarEstiloNegrito(Workbook workbook) {
        Font bold = workbook.createFont();
        bold.setBold(true);
        CellStyle style = workbook.createCellStyle();
        style.setFont(bold);
        return style;
    }
}

