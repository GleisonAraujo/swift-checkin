package com.example.server.api.reports;

import com.example.server.database.entity.Quarto;
import com.example.server.database.entity.Reserva;
import com.example.server.database.entity.HospedesAtivos;
import com.example.server.database.repository.QuartoRepository;
import com.example.server.database.repository.ReservaRepository;
import com.example.server.database.repository.HospedesAtivosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReportsService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private QuartoRepository quartoRepository;

    @Autowired
    private HospedesAtivosRepository hospedesAtivosRepository;

    // Método para contar todos os hóspedes
    public Long totalHospedes() {
        return hospedesAtivosRepository.countByStatus(true); // Considerando status = true para hóspedes ativos
    }

    // Método para contar todos os quartos disponíveis
    public Long totalQuartosDisponiveis() {
        return quartoRepository.countByDisponibilidade(true); // Contando quartos com disponibilidade = true
    }

    // Método para contar os quartos de tipo específico
    public Long totalQuartoCasal() {
        return quartoRepository.countByTipo("casal"); // Contando quartos do tipo "casal"
    }

    public Long totalQuartoDuplo() {
        return quartoRepository.countByTipo("duplo"); // Contando quartos do tipo "duplo"
    }

    public Long totalQuartoStandard() {
        return quartoRepository.countByTipo("standard"); // Contando quartos do tipo "standard"
    }

    // Método para contar reservas programadas (sem check-in e check-out)
    public Long totalReservasProgramadas() {
        return reservaRepository.countByDataCheckInIsNullAndDataCheckOutIsNull(); // Reservas sem check-in e check-out
    }

    // Método para relatar reservas concluídas (com check-in e check-out)
    public Long relatorioReservasConcluidas() {
        return reservaRepository.countByDataCheckInIsNotNullAndDataCheckOutIsNotNull(); // Reservas com check-in e check-out
    }

    // Método para relatar reservas canceladas (status = false, sem check-in e check-out)
    public Long relatorioReservasCanceladas() {
        return reservaRepository.countByStatusFalseAndDataCheckInIsNullAndDataCheckOutIsNull(); // Reservas canceladas sem check-in e check-out
    }

    // Método para calcular as receitas
    public Double relatorioReceitas() {
        //List<Reserva> reservasConcluidas = reservaRepository.findByDataCheckInIsNotNullAndDataCheckOutIsNotNull();
        double totalReceitas = 0.0;

       // for (Reserva reserva : reservasConcluidas) {
        //    totalReceitas += reserva.getQtdDiaria() * reserva.getQuarto().getPrDiaria(); // Multiplicando diárias pelo valor da diária do quarto
       // }

        return null;
    }
}


    //Metodos

    //totalHospedes * contar todos os hospedes

    //totalQuartosDisponiveis (indepedente do tipo) * contar todos os quartos
    
    //totalQuartoCasal (basedo no tipo) * contar todos os quartos basedo no tipo
    //totalDuplo  (basedo no tipo) * contar todos os quartos basedo no tipo
    //totalStandard  (basedo no tipo) * contar todos os quartos basedo no tipo

    ///totalReservasProgramadas (true / sem checkin e checkout)
    //relatorioReservaConcluidas (true e/ou false / com checkin e checkout)
    //relatorioReservaCanceladas (false / sem checkin e checkout)
    //relatorioReceitas * colocar valor nas reservas (fazer depois)

    

