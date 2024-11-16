package com.example.server.api.reports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.api.reports.dto.ReportDTO;

@RestController
public class ReportsController {

    @Autowired
    private ReportsService reportsService;

    // Endpoint para obter todos os relatórios em um único GET
    @GetMapping("/reports")
    public ReportDTO getAllReports() {
        ReportDTO reportDTO = new ReportDTO();

        // Preenchendo os dados do DTO
        reportDTO.setTotalHospedes(reportsService.totalHospedes());
        reportDTO.setTotalQuartosDisponiveis(reportsService.totalQuartosDisponiveis());
        reportDTO.setTotalQuartoCasal(reportsService.totalQuartoCasal());
        reportDTO.setTotalQuartoDuplo(reportsService.totalQuartoDuplo());
        reportDTO.setTotalQuartoStandard(reportsService.totalQuartoStandard());
        reportDTO.setTotalReservasProgramadas(reportsService.totalReservasProgramadas());
        reportDTO.setRelatorioReservasConcluidas(reportsService.relatorioReservasConcluidas());
        reportDTO.setRelatorioReservasCanceladas(reportsService.relatorioReservasCanceladas());
        reportDTO.setReceitas(reportsService.receitas());

        return reportDTO;
    }
}
