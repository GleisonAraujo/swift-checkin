package com.example.server.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.server.database.entity.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long>{

    Long countByDataCheckInIsNullAndDataCheckOutIsNull(); // Contagem de reservas sem check-in e check-out

    Long countByDataCheckInIsNotNullAndDataCheckOutIsNotNull(); // Contagem de reservas com check-in e check-out

    Long countByStatusFalseAndDataCheckInIsNullAndDataCheckOutIsNull(); // Contagem de reservas canceladas

    
}