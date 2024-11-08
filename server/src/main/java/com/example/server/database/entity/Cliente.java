package com.example.server.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import java.util.Date;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "documento_identidade", nullable = false, length = 20)
    private String documentoIdentidade;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "telefone", length = 15)
    private String telefone;

    @Column(name = "data_nascimento")
    private Date dataNascimento;

    // Construtores, getters e setters
    public Cliente() {}

    public Cliente(String documentoIdentidade, String email, String telefone, Date dataNascimento) {
        this.documentoIdentidade = documentoIdentidade;
        this.email = email;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
    }

    // Getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentoIdentidade() {
        return documentoIdentidade;
    }

    public void setDocumentoIdentidade(String documentoIdentidade) {
        this.documentoIdentidade = documentoIdentidade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
