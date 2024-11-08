package com.example.server.database.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.server.database.entity.HospedesAtivos;

@Repository
public interface HospedesAtivosRepository extends JpaRepository<HospedesAtivos, Long>{
    
}