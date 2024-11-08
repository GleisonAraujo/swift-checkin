package com.example.server.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.server.database.entity.ServicoQuarto;

// Repository para a entidade Usuario
@Repository
public interface ServicoQuartoRepository extends JpaRepository<ServicoQuarto, Long> {
}

