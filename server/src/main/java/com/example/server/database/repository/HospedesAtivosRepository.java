package com.example.server.database.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.server.database.entity.HospedesAtivos;
import com.example.server.database.entity.Reserva;

@Repository
public interface HospedesAtivosRepository extends JpaRepository<HospedesAtivos, Long>{
      Optional<HospedesAtivos> findByReserva(Reserva reserva);
      Long countByStatus(Boolean status); // Contagem de hóspedes ativos (status = true)
}