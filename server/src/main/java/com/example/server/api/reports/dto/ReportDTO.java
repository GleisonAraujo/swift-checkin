package com.example.server.api.reports.dto;

public class ReportDTO {
    
    private Long totalHospedes;
    private Long totalQuartosDisponiveis;
    private Long totalQuartoCasal;
    private Long totalQuartoDuplo;
    private Long totalQuartoStandard;
    private Long totalReservasProgramadas;
    private Long relatorioReservasConcluidas;
    private Long relatorioReservasCanceladas;

    // Getters and Setters
    public Long getTotalHospedes() {
        return totalHospedes;
    }

    public void setTotalHospedes(Long totalHospedes) {
        this.totalHospedes = totalHospedes;
    }

    public Long getTotalQuartosDisponiveis() {
        return totalQuartosDisponiveis;
    }

    public void setTotalQuartosDisponiveis(Long totalQuartosDisponiveis) {
        this.totalQuartosDisponiveis = totalQuartosDisponiveis;
    }

    public Long getTotalQuartoCasal() {
        return totalQuartoCasal;
    }

    public void setTotalQuartoCasal(Long totalQuartoCasal) {
        this.totalQuartoCasal = totalQuartoCasal;
    }

    public Long getTotalQuartoDuplo() {
        return totalQuartoDuplo;
    }

    public void setTotalQuartoDuplo(Long totalQuartoDuplo) {
        this.totalQuartoDuplo = totalQuartoDuplo;
    }

    public Long getTotalQuartoStandard() {
        return totalQuartoStandard;
    }

    public void setTotalQuartoStandard(Long totalQuartoStandard) {
        this.totalQuartoStandard = totalQuartoStandard;
    }

    public Long getTotalReservasProgramadas() {
        return totalReservasProgramadas;
    }

    public void setTotalReservasProgramadas(Long totalReservasProgramadas) {
        this.totalReservasProgramadas = totalReservasProgramadas;
    }

    public Long getRelatorioReservasConcluidas() {
        return relatorioReservasConcluidas;
    }

    public void setRelatorioReservasConcluidas(Long relatorioReservasConcluidas) {
        this.relatorioReservasConcluidas = relatorioReservasConcluidas;
    }

    public Long getRelatorioReservasCanceladas() {
        return relatorioReservasCanceladas;
    }

    public void setRelatorioReservasCanceladas(Long relatorioReservasCanceladas) {
        this.relatorioReservasCanceladas = relatorioReservasCanceladas;
    }
}