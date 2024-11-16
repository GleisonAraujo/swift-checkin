package com.example.server.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.server.database.entity.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>{
    // Verifica se existe um usuário com o nome informado
    boolean existsByCpf(String cpf);  // Retorna 'true' se o usuário existir
}