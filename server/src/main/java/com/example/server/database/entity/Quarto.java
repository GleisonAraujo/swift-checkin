package com.example.server.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity
public class Quarto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false, length = 10)
    private String numero;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    @Column(name = "prDiaria", nullable = false)
    private Double prDiaria;

    @Column(name = "descricao", length = 100)
    private String descricao;

    @Column(name = "disponibilidade", nullable = false)
    private Boolean disponibilidade;

    // Construtores, getters e setters
    public Quarto() {}

    public Quarto(String numero, String tipo, Double prDiaria, String descricao, Boolean disponibilidade) {
        this.numero = numero;
        this.tipo = tipo;
        this.prDiaria = prDiaria;
        this.descricao = descricao;
        this.disponibilidade = disponibilidade;
    }

    // Getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getPrDiaria() {
        return prDiaria;
    }

    public void setPrDiaria(Double prDiaria) {
        this.prDiaria = prDiaria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(Boolean disponibilidade) {
        this.disponibilidade = disponibilidade;
    }
}
