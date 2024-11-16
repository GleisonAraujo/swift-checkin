package com.example.server.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.server.database.entity.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long>{

    Long countByDataCheckInIsNullAndDataCheckOutIsNull(); // Contagem de reservas sem check-in e check-out

    Long countByDataCheckInIsNotNullAndDataCheckOutIsNotNull(); // Contagem de reservas com check-in e check-out

    Long countByStatusFalseAndDataCheckInIsNullAndDataCheckOutIsNull(); // Contagem de reservas canceladas

   // Soma de todos os valores finais das reservas
   @Query("SELECT SUM(r.valorFinal) FROM Reserva r WHERE r.status = false AND r.dataCheckIn IS NOT NULL AND r.dataCheckOut IS NOT NULL")
   Double somarValorFinalDeReservas();
}