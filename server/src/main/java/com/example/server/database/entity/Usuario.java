package com.example.server.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity
public class Usuario {

    @Id
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String nome;

    @Column(name = "fullName", nullable = false, length = 100)
    private String fullName;

    @Column(name = "senha", nullable = false, length = 255)
    private String senha;

    // Construtores, getters e setters
    public Usuario() {}

    public Usuario(String nome, String fullName, String senha) {
        this.nome = nome;
        this.fullName = fullName;
        this.senha = senha;
    }

    // Getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
